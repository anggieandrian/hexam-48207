package com.hand.demo.api.dto;

import com.hand.demo.domain.entity.InvoiceApplyHeader;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hzero.core.cache.CacheValue;
import org.hzero.core.cache.Cacheable;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity
public class InvoiceHeaderDTO extends InvoiceApplyHeader {

    private String applyStatusMeaning;
    private String invoiceTypeMeaning;
    private String invoiceColorMeaning;

}
