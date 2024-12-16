package com.hand.demo.app.service;

import com.hand.demo.api.dto.PrefixDTO;
import com.hand.demo.domain.entity.InvoiceApplyHeader;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * ExampleService
 */
public interface ExampleService {

    Page<PrefixDTO> getInvoiceData(PageRequest pageRequest, PrefixDTO prefixDTO, Long organizationId);
}
