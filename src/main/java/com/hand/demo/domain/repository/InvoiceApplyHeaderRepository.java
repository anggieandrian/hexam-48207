package com.hand.demo.domain.repository;

import com.hand.demo.domain.entity.InvoiceApplyLine;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.hand.demo.domain.entity.InvoiceApplyHeader;

import java.util.List;
import java.util.Optional;

/**
 * (InvoiceApplyHeader)资源库
 *
 * @author Anggie A
 * @since 2024-12-10 11:19:55
 */
public interface InvoiceApplyHeaderRepository extends BaseRepository<InvoiceApplyHeader> {
    /**
     * 查询
     *
     * @param invoiceApplyHeader 查询条件
     * @return 返回值
     */
    List<InvoiceApplyHeader> selectList(InvoiceApplyHeader invoiceApplyHeader);


    List<InvoiceApplyHeader> selectList(PageRequest pageRequest, InvoiceApplyHeader invoiceApplyHeader);

    void saveData(List<InvoiceApplyHeader> invoiceApplyHeaders);

    /**
     * 根据主键查询（可关联表）
     *
     * @param applyHeaderId 主键
     * @return 返回值
     */
    InvoiceApplyHeader selectByPrimary(Long applyHeaderId);

    boolean isValidValue(String value, String loveCode);

    List<InvoiceApplyLine> selectByHeaderId(Long applyHeaderId);

    Optional<Object> findById(Long applyHeaderId);
}
