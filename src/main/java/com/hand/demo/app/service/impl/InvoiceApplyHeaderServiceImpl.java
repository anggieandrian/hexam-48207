package com.hand.demo.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.hand.demo.app.service.InvoiceApplyLineService;
import com.hand.demo.domain.entity.InvoiceApplyLine;
import com.hand.demo.domain.repository.InvoiceApplyLineRepository;
import com.hand.demo.infra.mapper.InvoiceApplyHeaderMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.redis.RedisHelper;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import com.hand.demo.app.service.InvoiceApplyHeaderService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.hand.demo.domain.entity.InvoiceApplyHeader;
import com.hand.demo.domain.repository.InvoiceApplyHeaderRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * (InvoiceApplyHeader)应用服务
 *
 * @author Anggie A
 * @since 2024-12-10 11:19:55
 */
@Service
public class InvoiceApplyHeaderServiceImpl implements InvoiceApplyHeaderService {
    @Autowired
    private InvoiceApplyHeaderRepository invoiceApplyHeaderRepository;

    @Autowired
    private InvoiceApplyLineRepository invoiceApplyLineRepository;

    @Autowired
    private InvoiceApplyLineService invoiceApplyLineService;

    @Autowired
    private CodeRuleBuilder codeRuleBuilder;

    @Autowired
    private RedisHelper redisHelper;

    @Autowired
    private InvoiceApplyHeaderMapper invoiceApplyHeaderMapper;


    // nomor 3
    @Override
    public Page<InvoiceApplyHeader> selectList(PageRequest pageRequestList, InvoiceApplyHeader invoiceApplyHeaderSelectList) {
        return PageHelper.doPageAndSort(pageRequestList, () -> invoiceApplyHeaderRepository.selectList(invoiceApplyHeaderSelectList));
    }

    // nomor 4 untuk mengatur waktunya
    @Override
    @ProcessLovValue
    public ResponseEntity<InvoiceApplyHeader> detail(Long applyHeaderId){
        String HCacheKey = "hexam-48207:Header" + applyHeaderId;
        String cacheValue = redisHelper.strGet(HCacheKey);
        InvoiceApplyHeader invoiceApplyHeaderDetails;
        List<InvoiceApplyLine> invoiceApplyLineDetails;
        InvoiceApplyHeader invoiceApplyHeaderDetail= invoiceApplyHeaderMapper.selectByPrimaryKey(applyHeaderId);
        CustomUserDetails customerDetails = DetailsHelper.getUserDetails();
        invoiceApplyHeaderDetail.setRequester(customerDetails.getRealName());
        if (StringUtils.isNotBlank(cacheValue)) {
            invoiceApplyHeaderDetails = JSON.parseObject(cacheValue, InvoiceApplyHeader.class);
            // Manually load lines if not present after deserialization
            if (invoiceApplyHeaderDetails.getInvoiceApplyLines() == null || invoiceApplyHeaderDetails.getInvoiceApplyLines().isEmpty()) {
                List<InvoiceApplyLine> linesDetail = invoiceApplyLineRepository.selectByApplyHeaderId(applyHeaderId);
                invoiceApplyHeaderDetails.setInvoiceApplyLines(linesDetail);
            }
        } else {
            invoiceApplyHeaderDetails = invoiceApplyHeaderRepository.selectByPrimaryKey(applyHeaderId);
            InvoiceApplyLine invoiceApplyLineDetail = new InvoiceApplyLine();
            invoiceApplyLineDetail.setApplyHeaderId(applyHeaderId);
            invoiceApplyLineDetails = invoiceApplyLineRepository.selectList(invoiceApplyLineDetail);
            invoiceApplyHeaderDetails.setInvoiceApplyLines(invoiceApplyLineDetails);
            redisHelper.strSet(HCacheKey, JSON.toJSONString(invoiceApplyHeaderDetails));
        }
        return Results.success(invoiceApplyHeaderDetails);
    }

    // nomor 5
    @Override
    @Transactional
    @ProcessLovValue
    public void saveData(List<InvoiceApplyHeader> invoiceApplyHeaderSave) {
        List<InvoiceApplyHeader> valid = invoiceApplyHeaderSave.stream()
                .filter(header -> invoiceApplyHeaderRepository.isValidValue(header.getApplyStatus(), "HEXAM-INV-HEADER-STATUS-48207"))
                .filter(header -> invoiceApplyHeaderRepository.isValidValue(header.getInvoiceType(), "HEXAM-INV-HEADER-TYPE-48207"))
                .filter(header -> invoiceApplyHeaderRepository.isValidValue(header.getInvoiceColor(), "HEXAM-INV-HEADER-COLOR-48207"))
                .collect(Collectors.toList());

        if (valid.size() != invoiceApplyHeaderSave.size()) {
            throw new CommonException("Some invoice apply headers have invalid values");
        }
    
        // untuk format nomornya menggunakan coderule
        List<InvoiceApplyHeader> insertList = invoiceApplyHeaderSave.stream().filter(line -> line.getApplyHeaderId() == null)
                .peek(header -> header.setApplyHeaderNumber(codeRuleBuilder.generateCode("HEXAM-INV-48207", new HashMap<>())))
                .collect(Collectors.toList());
        List<InvoiceApplyHeader> updateList = invoiceApplyHeaderSave.stream().filter(line -> line.getApplyHeaderId() != null)
                .peek(header -> {
                    InvoiceApplyHeader currentVersion = invoiceApplyHeaderRepository.selectByPrimaryKey(header.getApplyHeaderId());
                    if (currentVersion != null) {
                        header.setObjectVersionNumber(currentVersion.getObjectVersionNumber());
                    }
                }).collect(Collectors.toList());
    
        invoiceApplyHeaderRepository.batchInsertSelective(insertList);
        invoiceApplyHeaderRepository.batchUpdateByPrimaryKeySelective(updateList);
    
        Set<Long> headerIds = invoiceApplyHeaderSave.stream()
                .map(InvoiceApplyHeader::getApplyHeaderId)
                .collect(Collectors.toSet());
        for (Long headerId : headerIds) {
            updateHAmounts(headerId);
            invoiceApplyLineService.updateRedisCache(headerId);
        }
    }


    // untuk update data dan perhitungan
    @Transactional
    @Override
    public void updateHAmounts(Long applyHeaderId) {
        List<InvoiceApplyLine> lines = invoiceApplyHeaderRepository.selectByHeaderId(applyHeaderId);

        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal excludeTaxAmount = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;

        for (InvoiceApplyLine Ammountline : lines) {
            totalAmount = totalAmount.add(Ammountline.getTotalAmount());
            excludeTaxAmount = excludeTaxAmount.add(Ammountline.getExcludeTaxAmount());
            taxAmount = taxAmount.add(Ammountline.getTaxAmount());
        }

        InvoiceApplyHeader Ammountheader = invoiceApplyHeaderRepository.selectByPrimary(applyHeaderId);
        if (Ammountheader != null){
            Ammountheader.setTotalAmount(totalAmount);
            Ammountheader.setExcludeTaxAmount(excludeTaxAmount);
            Ammountheader.setTaxAmount(taxAmount);
            Ammountheader.setObjectVersionNumber(Ammountheader.getObjectVersionNumber());
        }

        invoiceApplyHeaderRepository.updateByPrimaryKeySelective(Ammountheader);
    }


    @Override
    public InvoiceApplyHeader selectByPrimary(Long applyHeaderId) {
        return null;
    }

    // nomor 6 ini untuk formatnya
    @Override
    public ResponseEntity<InvoiceApplyHeader> removeById(Long applyHeaderId) {
        InvoiceApplyHeader invoiceApplyHeaderSelectByPrimary = invoiceApplyHeaderRepository.selectByPrimaryKey(applyHeaderId);

        if (invoiceApplyHeaderSelectByPrimary != null) {
            invoiceApplyHeaderSelectByPrimary.setDelFlag(1);
            invoiceApplyHeaderRepository.updateByPrimaryKeySelective(invoiceApplyHeaderSelectByPrimary);
            deleteRedisC(invoiceApplyHeaderSelectByPrimary);
        }
        return Results.success(invoiceApplyHeaderSelectByPrimary);
    }

    // no 6 untuk menyesuaikan dengan redisnya
    @Override
    public void deleteRedisC(InvoiceApplyHeader header){
        String cacheKey = "hexam-48207:Header" + header.getApplyHeaderId();
        redisHelper.delKey(cacheKey);
    }

    @Override
    public void getinvoiceSchedulingTask(String delFlag, String applyStatus, String invoiceColor, String invoiceType) {

    }
}

