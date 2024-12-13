package com.hand.demo.app.job;


import com.hand.demo.app.service.InvoiceApplyHeaderService;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@JobHandler("HEXAM-48207")
public class HandlerJob {

    private final InvoiceApplyHeaderService applyHeaderService;

    @Autowired
    public HandlerJob(InvoiceApplyHeaderService applyHeaderService) {
        this.applyHeaderService = applyHeaderService;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool Stool) {
        applyHeaderService.getinvoiceSchedulingTask(map.get("delFlag"), map.get("applyStatus"), map.get("invoiceColor"),
                map.get("invoiceType"));
        return ReturnT.SUCCESS;
    }
}

