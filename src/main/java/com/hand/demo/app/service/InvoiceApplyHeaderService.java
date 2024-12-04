package com.hand.demo.app.service;

import com.hand.demo.api.dto.InvoiceHeaderDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import com.hand.demo.domain.entity.InvoiceApplyHeader;

import java.util.List;

/**
 * (InvoiceApplyHeader)应用服务
 *
 * @author Anggie A
 * @since 2024-12-04 13:33:05
 */
public interface InvoiceApplyHeaderService {

    /**
     * 查询数据
     *
     * @param pageRequest         分页参数
     * @param invoiceApplyHeaders 查询条件
     * @return 返回值
     */
    Page<InvoiceApplyHeader> selectList(PageRequest pageRequest, InvoiceApplyHeader invoiceApplyHeaders);

    void saveData(List<InvoiceApplyHeader> invoiceApplyHeaders);

    List<InvoiceHeaderDTO> getInvoiceHeadersByOrganization(Long tenantId);

    InvoiceApplyHeader createInvoiceHeader(InvoiceHeaderDTO invoiceHeaderDTO);

    InvoiceApplyHeader updateInvoiceHeader(InvoiceHeaderDTO invoiceHeaderDTO);

    void deleteInvoiceHeader(Long id);

    /**
     * 保存数据
     *
     * @param invoiceApplyHeaders 数据
     */

}

