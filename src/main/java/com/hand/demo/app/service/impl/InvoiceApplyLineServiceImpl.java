package com.hand.demo.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.hand.demo.app.service.InvoiceApplyHeaderService;
import com.hand.demo.domain.entity.InvoiceApplyHeader;
import com.hand.demo.domain.repository.InvoiceApplyHeaderRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.redis.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import com.hand.demo.app.service.InvoiceApplyLineService;
import org.springframework.stereotype.Service;
import com.hand.demo.domain.entity.InvoiceApplyLine;
import com.hand.demo.domain.repository.InvoiceApplyLineRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * (InvoiceApplyLine)应用服务
 *
 * @author Anggie A
 * @since 2024-12-10 11:20:18
 */
@Service
public class InvoiceApplyLineServiceImpl implements InvoiceApplyLineService {
    @Autowired
    private InvoiceApplyLineRepository invoiceApplyLineRepository;

    @Autowired
    private InvoiceApplyHeaderService invoiceApplyHeaderService;

    @Autowired
    private InvoiceApplyHeaderRepository invoiceApplyHeaderRepository;

    @Autowired
    private RedisHelper redisHelper;


    @Override
    public Page<InvoiceApplyLine> selectList(PageRequest pageRequest, InvoiceApplyLine invoiceApplyLine) {
        return PageHelper.doPageAndSort(pageRequest, () -> invoiceApplyLineRepository.selectList(invoiceApplyLine));
    }


    // nomor 7
    @Override
    public void saveData(List<InvoiceApplyLine> invoiceApplyLines) {
        List<InvoiceApplyLine> valid = invoiceApplyLines.stream()
                .filter(line -> line.getApplyLineId() == null)
                .filter(line -> invoiceApplyLineRepository.isValidHeaderId(line.getApplyHeaderId()))
                .collect(Collectors.toList());

        for (InvoiceApplyLine line : valid) {
            BigDecimal totalAmount = line.getUnitPrice().multiply(line.getQuantity());
            BigDecimal taxAmount = totalAmount.multiply(line.getTaxRate());
            BigDecimal excludeTaxAmount = totalAmount.subtract(taxAmount);

            line.setTotalAmount(totalAmount);
            line.setTaxAmount(taxAmount);
            line.setExcludeTaxAmount(excludeTaxAmount);
        }
        List<InvoiceApplyLine> updateList = invoiceApplyLines.stream().filter(line -> line.getApplyLineId() != null).map(line -> {
            InvoiceApplyLine currVer = invoiceApplyLineRepository.selectByPrimaryKey(line.getApplyLineId());
            if (currVer != null) {
                line.setObjectVersionNumber(currVer.getObjectVersionNumber());
            }
            return  line;
        }).collect(Collectors.toList());


        invoiceApplyLineRepository.batchInsertSelective(valid);
        invoiceApplyLineRepository.batchUpdateByPrimaryKeySelective(updateList);


        Set<Long> headerIds = invoiceApplyLines.stream()
                .map(InvoiceApplyLine::getApplyHeaderId)
                .collect(Collectors.toSet());
        for (Long headerId : headerIds) {
            invoiceApplyHeaderService.updateHAmounts(headerId);
            if (invoiceApplyLines != null) {
                updateRedisCache(headerId);
            }
        }
    }


    @Override
    public void updateRedisCache(Long applyHeaderId) {
        String headerCacheKey = "hexam-48207:Header" + applyHeaderId;

        InvoiceApplyHeader invoiceApplyHeader = invoiceApplyHeaderRepository.selectByPrimary(applyHeaderId);
        List<InvoiceApplyLine> invoiceApplyLines = invoiceApplyLineRepository.selectByHeaderId(applyHeaderId);
        invoiceApplyHeader.setInvoiceApplyLines(invoiceApplyLines);
        redisHelper.strSet(headerCacheKey, JSON.toJSONString(invoiceApplyHeader), 6, TimeUnit.MINUTES);
    }




}

