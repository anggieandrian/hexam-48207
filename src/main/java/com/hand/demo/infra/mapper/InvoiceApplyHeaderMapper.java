package com.hand.demo.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import com.hand.demo.domain.entity.InvoiceApplyHeader;

import java.util.List;

/**
 * (InvoiceApplyHeader)应用服务
 *
 * @author Anggie A
 * @since 2024-12-10 11:19:55
 */
public interface InvoiceApplyHeaderMapper extends BaseMapper<InvoiceApplyHeader> {
    /**
     * 基础查询
     *
     * @param invoiceApplyHeader 查询条件
     * @return 返回值
     */
    //com.hand.demo.infra.mapper.InvoiceApplyHeaderMapper.selectList
    List<InvoiceApplyHeader> selectList(InvoiceApplyHeader invoiceApplyHeader);
}

