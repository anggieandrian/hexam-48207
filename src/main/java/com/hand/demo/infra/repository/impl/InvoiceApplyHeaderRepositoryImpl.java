package com.hand.demo.infra.repository.impl;

import com.hand.demo.api.dto.InvoiceHeaderDTO;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;
import com.hand.demo.domain.entity.InvoiceApplyHeader;
import com.hand.demo.domain.repository.InvoiceApplyHeaderRepository;
import com.hand.demo.infra.mapper.InvoiceApplyHeaderMapper;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * (InvoiceApplyHeader)资源库
 *
 * @author Anggie A
 * @since 2024-12-04 13:33:05
 */
@Component
public class InvoiceApplyHeaderRepositoryImpl extends BaseRepositoryImpl<InvoiceApplyHeader> implements InvoiceApplyHeaderRepository {
    @Resource
    private InvoiceApplyHeaderMapper invoiceApplyHeaderMapper;

    @Override
    public List<InvoiceApplyHeader> selectList(InvoiceApplyHeader invoiceApplyHeader) {
        return invoiceApplyHeaderMapper.selectList(invoiceApplyHeader);
    }

    @Override
    public InvoiceApplyHeader selectByPrimary(Long applyHeaderId) {
        InvoiceApplyHeader invoiceApplyHeader = new InvoiceApplyHeader();
        invoiceApplyHeader.setApplyHeaderId(applyHeaderId);
        List<InvoiceApplyHeader> invoiceApplyHeaders = invoiceApplyHeaderMapper.selectList(invoiceApplyHeader);
        if (invoiceApplyHeaders.isEmpty()) {
            return null;
        }
        return invoiceApplyHeaders.get(0);
    }

    @Override
    public List<InvoiceApplyHeader> findByTenantId(Long tenantId) {
        return Collections.emptyList();
    }

    @Override
    public InvoiceApplyHeader save(InvoiceApplyHeader entity) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Optional<InvoiceApplyHeader> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<InvoiceApplyHeader> findByOrganizationId(Long tenantId) {
        return Collections.emptyList();
    }

    @Override
    public List<InvoiceHeaderDTO> getInvoiceHeadersByOrganization(Long tenantId) {
        return Collections.emptyList();
    }

}

