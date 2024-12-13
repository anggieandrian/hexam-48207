package com.hand.demo.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.hand.demo.app.service.InvoiceApplyLineService;
import com.hand.demo.domain.entity.InvoiceApplyLine;
import com.hand.demo.domain.repository.InvoiceApplyLineRepository;
import io.choerodon.core.oauth.DetailsHelper;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


@ImportService(templateCode = "IMPORT_LINE_48207")
public class ImportLineServiceImpl implements IBatchImportService {
    @Autowired
    private InvoiceApplyLineRepository invoiceApplyLineRepository;

    @Autowired
    private InvoiceApplyLineService invoiceApplyLineService;

    @Override
    public Boolean doImport(List<String> data) {
        List<InvoiceApplyLine> invoiceApplyLinesImport = new ArrayList<>();

        for (String line : data) {
            InvoiceApplyLine importL = JSON.parseObject(line, InvoiceApplyLine.class);
            InvoiceApplyLine queryParamL = new InvoiceApplyLine();
            queryParamL.setApplyLineId(importL.getApplyLineId());
            InvoiceApplyLine invoiceApplyLinesImpot = null;

            if (importL.getApplyLineId() != null) {
                invoiceApplyLinesImpot = invoiceApplyLineRepository.selectOne(queryParamL);
            }
            if (invoiceApplyLinesImpot == null) {
                Long tenantId = DetailsHelper.getUserDetails().getTenantId();
                importL.setTenantId(tenantId);
                invoiceApplyLinesImport.add(importL);
            } else {
                importL.setApplyLineId(invoiceApplyLinesImpot.getApplyLineId());
                importL.setObjectVersionNumber(invoiceApplyLinesImpot.getObjectVersionNumber());
                invoiceApplyLinesImport.add(importL);
            }
        }
        if (!invoiceApplyLinesImport.isEmpty()) {
            invoiceApplyLineService.saveData(invoiceApplyLinesImport);
        }

        return true;
    }
}
