package com.hand.demo.api.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class InvoiceLineDTO {
    private Long applyHeaderId;
    private String applyHeaderNumber;
    private String invoiceName;
    private String contentName;
    private String taxClassificationNumber;
    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private BigDecimal excludeTaxAmount;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String remark;
}
