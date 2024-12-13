package com.hand.demo.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import com.hand.demo.domain.entity.InvoiceApplyHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * (InvoiceApplyHeader)应用服务
 *
 * @author Anggie A
 * @since 2024-12-10 11:19:55
 */
public interface InvoiceApplyHeaderService {

    /**
     * 查询数据
     *
     * @param pageRequest         分页参数
     * @param invoiceApplyHeaders 查询条件
     * @return 返回值
     */
    Page<InvoiceApplyHeader> selectList(PageRequest pageRequest, InvoiceApplyHeader invoiceApplyHeaderSelectList);

    /**
     * 保存数据
     *
     * @param invoiceApplyHeaders 数据
     */
    void saveData(List<InvoiceApplyHeader> invoiceApplyHeaderSave);


    @Transactional
    void updateHAmounts(Long applyHeaderId);

    InvoiceApplyHeader selectByPrimary(Long applyHeaderId);

    ResponseEntity<InvoiceApplyHeader> detail(Long applyHeaderId);

    // nomor 6 ini untuk formatnya
    ResponseEntity<InvoiceApplyHeader> removeById(Long applyHeaderId);

    void deleteRedisC(InvoiceApplyHeader header);

    void getinvoiceSchedulingTask(String delFlag, String applyStatus, String invoiceColor, String invoiceType);
}

