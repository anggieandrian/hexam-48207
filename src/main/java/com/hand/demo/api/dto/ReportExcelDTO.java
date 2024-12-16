package com.hand.demo.api.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.List;


@Getter
@Setter
public class ReportExcelDTO {
    private String tenantName;
    private String invoiceApplyNumberFrom;
    private Date invoiceCreationDateFrom;
    private Date submitTimeFrom;
    private List<String> applyStatuses; // This supports multiple selections for apply statuses.
    private String invoiceType;
}
