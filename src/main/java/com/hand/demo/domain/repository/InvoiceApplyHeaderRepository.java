package com.hand.demo.domain.repository;

import com.hand.demo.api.dto.InvoiceHeaderDTO;
import org.hzero.mybatis.base.BaseRepository;
import com.hand.demo.domain.entity.InvoiceApplyHeader;

import java.util.List;
import java.util.Optional;

/**
 * (InvoiceApplyHeader)资源库
 *
 * @author Anggie A
 * @since 2024-12-04 13:33:04
 */
public interface InvoiceApplyHeaderRepository extends BaseRepository<InvoiceApplyHeader> {
    /**
     * 查询
     *
     * @param invoiceApplyHeader 查询条件
     * @return 返回值
     */
    List<InvoiceApplyHeader> selectList(InvoiceApplyHeader invoiceApplyHeader);

    /**
     * 根据主键查询（可关联表）
     *
     * @param applyHeaderId 主键
     * @return 返回值
     */
    InvoiceApplyHeader selectByPrimary(Long applyHeaderId);

    List<InvoiceApplyHeader> findByTenantId(Long tenantId);

    InvoiceApplyHeader save(InvoiceApplyHeader entity);

    void deleteById(Long id);

    Optional<InvoiceApplyHeader> findById(Long id);

    List<InvoiceApplyHeader> findByOrganizationId(Long tenantId);

    List<InvoiceHeaderDTO> getInvoiceHeadersByOrganization(Long tenantId);
}
