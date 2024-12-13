package com.hand.demo.app.service.impl;

import com.hand.demo.app.service.InvoiceApplyHeaderService;
import com.hand.demo.domain.entity.InvoiceApplyHeader;
import com.hand.demo.domain.repository.InvoiceApplyHeaderRepository;
import io.choerodon.core.oauth.DetailsHelper;
import io.micrometer.core.instrument.util.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;

import com.alibaba.fastjson.JSON;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// nomor 9 untuk Import
@ImportService(templateCode = "IMPORT_HEADER_48207")
public class ImportHeaderServiceImpl implements IBatchImportService {
    @Autowired
    private InvoiceApplyHeaderRepository invoiceApplyHeaderRepository;
    @Autowired
    private InvoiceApplyHeaderService invoiceApplyHeaderService;


    @Override
    public Boolean doImport(List<String> data) {
        List<InvoiceApplyHeader> invoiceApplyHeaders = new ArrayList<>();
        // import header data and save it to database
        for (String header : data) {
            InvoiceApplyHeader importH = JSON.parseObject(header, InvoiceApplyHeader.class);
            InvoiceApplyHeader queryParamH = new InvoiceApplyHeader();
            queryParamH.setApplyHeaderNumber(importH.getApplyHeaderNumber());
            InvoiceApplyHeader existingHeader = null;

            if (StringUtils.isNotBlank(importH.getApplyHeaderNumber())) {
                existingHeader = invoiceApplyHeaderRepository.selectOne(queryParamH);
            }
            if (existingHeader != null) {
                importH.setApplyHeaderId(existingHeader.getApplyHeaderId());
                importH.setObjectVersionNumber(existingHeader.getObjectVersionNumber());
                invoiceApplyHeaders.add(importH);
            } else {
                Long tenantId = DetailsHelper.getUserDetails().getTenantId();
                importH.setTenantId(tenantId);
                importH.setTotalAmount(BigDecimal.ZERO);
                importH.setExcludeTaxAmount(BigDecimal.ZERO);
                importH.setTaxAmount(BigDecimal.ZERO);
                invoiceApplyHeaders.add(importH);
            }
        }
        if (!invoiceApplyHeaders.isEmpty()) {
            invoiceApplyHeaderService.saveData(invoiceApplyHeaders);
        }
        return true;
    }
}
