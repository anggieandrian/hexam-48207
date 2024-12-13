package com.hand.demo.infra.repository.impl;


import com.hand.demo.domain.entity.InvoiceApplyLine;
import com.hand.demo.domain.repository.InvoiceApplyHeaderRepository;
import com.hand.demo.infra.mapper.InvoiceApplyHeaderMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.form.constant.FormConstants;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.constant.LovConstants;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hand.demo.domain.entity.InvoiceApplyHeader;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * (InvoiceApplyHeader)资源库
 *
 * @author Anggie A
 * @since 2024-12-10 11:19:55
 */
@Component
public class InvoiceApplyHeaderRepositoryImpl extends BaseRepositoryImpl<InvoiceApplyHeader> implements InvoiceApplyHeaderRepository {
    @Autowired
    private InvoiceApplyHeaderMapper invoiceApplyHeaderMapper;


    @Override
    public List<InvoiceApplyHeader> selectList(InvoiceApplyHeader invoiceApplyHeader) {
        return invoiceApplyHeaderMapper.selectList(invoiceApplyHeader);
    }

    @Override
    public List<InvoiceApplyHeader> selectList(PageRequest pageRequest, InvoiceApplyHeader invoiceApplyHeader) {
        return Collections.emptyList();
    }


    @Override
    public void saveData(List<InvoiceApplyHeader> invoiceApplyHeaders) {
        return;
    }


    @Override
    public InvoiceApplyHeader selectByPrimary(Long applyHeaderId) {
        InvoiceApplyHeader invoiceApplyHeader = new InvoiceApplyHeader();
        invoiceApplyHeader.setApplyHeaderId(applyHeaderId);
        List<InvoiceApplyHeader> invoiceApplyHeaders = invoiceApplyHeaderMapper.selectList(invoiceApplyHeader);
        if (invoiceApplyHeaders.size() == 0) {
            return null;
        }
        return invoiceApplyHeaders.get(0);
    }

    // untuk memasukan formatnya
    @Override
    public boolean isValidValue(String value, String loveCode) {
        Map<String, List<String>> lovValues = new HashMap<>();
        lovValues.put("HEXAM-INV-HEADER-STATUS-48207", Arrays.asList("D", "S", "F", "C"));
        lovValues.put("HEXAM-INV-HEADER-TYPE-48207", Arrays.asList("R", "B"));
        lovValues.put("HEXAM-INV-HEADER-COLOR-48207", Arrays.asList("P", "E"));
    
        if (!lovValues.containsKey(loveCode)) {
            throw new IllegalArgumentException("Invalid LOV code: " + loveCode);
        }
    
        return lovValues.get(loveCode).contains(value);
    }

    @Override
    public List<InvoiceApplyLine> selectByHeaderId(Long applyHeaderId) {
        return Collections.emptyList();
    }

    @Override
    public Optional<Object> findById(Long applyHeaderId) {
        return Optional.empty();
    }


}

