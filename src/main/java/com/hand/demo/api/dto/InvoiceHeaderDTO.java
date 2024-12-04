package com.hand.demo.api.dto;

import com.hand.demo.domain.entity.InvoiceApplyHeader;
import lombok.Data;

@Data
public class InvoiceHeaderDTO extends InvoiceApplyHeader {
    private String applyStatusMeaning;
    private String invoiceTypeMeaning;
    private String invoiceColorMeaning;
}
