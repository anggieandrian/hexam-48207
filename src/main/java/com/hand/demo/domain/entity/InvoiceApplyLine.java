package com.hand.demo.domain.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.hzero.export.annotation.ExcelColumn;

/**
 * (InvoiceApplyLine)实体类
 *
 * @author Anggie A
 * @since 2024-12-10 11:20:17
 */

@Getter
@Setter
@ApiModel("")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "todo_invoice_apply_line")
public class InvoiceApplyLine extends AuditDomain {
    private static final long serialVersionUID = 284686256686134300L;

    public static final String FIELD_APPLY_LINE_ID = "applyLineId";
    public static final String FIELD_APPLY_HEADER_ID = "applyHeaderId";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE10 = "attribute10";
    public static final String FIELD_ATTRIBUTE11 = "attribute11";
    public static final String FIELD_ATTRIBUTE12 = "attribute12";
    public static final String FIELD_ATTRIBUTE13 = "attribute13";
    public static final String FIELD_ATTRIBUTE14 = "attribute14";
    public static final String FIELD_ATTRIBUTE15 = "attribute15";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";
    public static final String FIELD_ATTRIBUTE6 = "attribute6";
    public static final String FIELD_ATTRIBUTE7 = "attribute7";
    public static final String FIELD_ATTRIBUTE8 = "attribute8";
    public static final String FIELD_ATTRIBUTE9 = "attribute9";
    public static final String FIELD_CONTENT_NAME = "contentName";
    public static final String FIELD_EXCLUDE_TAX_AMOUNT = "excludeTaxAmount";
    public static final String FIELD_INVOICE_NAME = "invoiceName";
    public static final String FIELD_QUANTITY = "quantity";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_TAX_AMOUNT = "taxAmount";
    public static final String FIELD_TAX_CLASSIFICATION_NUMBER = "taxClassificationNumber";
    public static final String FIELD_TAX_RATE = "taxRate";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TOTAL_AMOUNT = "totalAmount";
    public static final String FIELD_UNIT_PRICE = "unitPrice";

    @Id
    @ExcelColumn (en = "Apply Line Id", order = 2)
    @GeneratedValue
    private Long applyLineId;

    @ExcelColumn (en = "Tenant Id", order = 3)
    private Long tenantId;

    @ApiModelProperty(value = "header id")
    @ExcelColumn (en = "Apply Header Id", order = 4)
    private Long applyHeaderId;

    @ExcelColumn (en = "invoice Name", order =5)
    private String invoiceName;

    @ExcelColumn (en = "Content Name", order = 6)
    private String contentName;

    @ExcelColumn (en = "Tax Classification Number", order = 7)
    private String taxClassificationNumber;

    @ExcelColumn (en = "unit_price", order = 8)
    private BigDecimal unitPrice;

    @ExcelColumn (en = "quantity", order = 9)
    private BigDecimal quantity;

    @ApiModelProperty(value = "total_amount - tax_amount")
    @ExcelColumn (en = "Exclude Tax Amount", order = 10)
    private BigDecimal excludeTaxAmount;

    @ApiModelProperty(value = "eg: 0.08")
    @ExcelColumn (en = "Tax Rate", order = 11)
    private BigDecimal taxRate;

    @ApiModelProperty(value = "total_amount * tax_rate")
    @ExcelColumn (en = "Tax Amount", order = 12)
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "unit_price * quantity")
    @ExcelColumn (en = "Total Amount", order = 13)
    private BigDecimal totalAmount;

    @ExcelColumn (en = "Remark", order = 14)
    private Object remark;

    private String attribute1;

    private String attribute10;

    private String attribute11;

    private String attribute12;

    private String attribute13;

    private String attribute14;

    private String attribute15;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String attribute5;

    private String attribute6;

    private String attribute7;

    private String attribute8;

    private String attribute9;



}

