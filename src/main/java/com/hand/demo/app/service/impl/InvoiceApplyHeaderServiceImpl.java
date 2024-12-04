package com.hand.demo.app.service.impl;

import com.hand.demo.api.dto.InvoiceHeaderDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.hand.demo.app.service.InvoiceApplyHeaderService;
import org.springframework.stereotype.Service;
import com.hand.demo.domain.entity.InvoiceApplyHeader;
import com.hand.demo.domain.repository.InvoiceApplyHeaderRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (InvoiceApplyHeader)应用服务
 *
 * @author Anggie A
 * @since 2024-12-04 13:33:05
 */
@Service
public class InvoiceApplyHeaderServiceImpl implements InvoiceApplyHeaderService {
    @Autowired
    private InvoiceApplyHeaderRepository invoiceApplyHeaderRepository;
    private InvoiceHeaderDTO invoiceHeaderDTO;
    private LovAdapter lovAdapter;

    @Override
    public Page<InvoiceApplyHeader> selectList(PageRequest pageRequest, InvoiceApplyHeader invoiceApplyHeader) {
        return PageHelper.doPageAndSort(pageRequest, () -> invoiceApplyHeaderRepository.selectList(invoiceApplyHeader));
    }

    @Override
    public void saveData(List<InvoiceApplyHeader> invoiceApplyHeaders) {
        List<InvoiceApplyHeader> insertList = invoiceApplyHeaders.stream().filter(line -> line.getApplyHeaderId() == null).collect(Collectors.toList());
        List<InvoiceApplyHeader> updateList = invoiceApplyHeaders.stream().filter(line -> line.getApplyHeaderId() != null).collect(Collectors.toList());
        invoiceApplyHeaderRepository.batchInsertSelective(insertList);
        invoiceApplyHeaderRepository.batchUpdateByPrimaryKeySelective(updateList);
    }
//    public List<InvoiceHeaderDTO> getAllInvoiceHeaders(Long tenantId, String lang) {
//        List<InvoiceApplyHeader> headers = invoiceApplyHeaderRepository.findByTenantId(tenantId);
//        return headers.stream().map(header -> convertToDto(header, tenantId, lang)).collect(Collectors.toList());
//    }
//
//    private Object convertToDto(InvoiceApplyHeader header, Long tenantId, String lang) {
//    }

    //    @Override
//    public InvoiceHeaderDTO convertToDto(InvoiceApplyHeader header, Long tenantId, String lang) {
//        InvoiceHeaderDTO dto = new InvoiceHeaderDTO();
//        BeanUtils.copyProperties(header, dto);
//
//        // Translate values using the LOV adapter
//        dto.setApplyStatusMeaning(lovAdapter.queryLovMeaning("HEXAM-INV-HEADER-STATUS-48209", tenantId, header.getApplyStatus(), lang));
//        dto.setInvoiceTypeMeaning(lovAdapter.queryLovMeaning("HEXAM-INV-HEADER-TYPE-48209", tenantId, header.getInvoiceType(), lang));
//        dto.setInvoiceColorMeaning(lovAdapter.queryLovMeaning("HEXAM-INV-HEADER-COLOR-48209", tenantId, header.getInvoiceColor(), lang));
//
//        return dto;
//    }
//    @Override
//    public InvoiceHeaderDTO getInvoiceHeaderById(Long id) {
//        Optional<InvoiceApplyHeader> header = invoiceApplyHeaderRepository.findById(id);
//        return header.map(h -> convertToDto(h, h.getTenantId(), "en")).orElse(null); // Default language set to English
//    }
//
//    public InvoiceApplyHeader saveInvoiceHeader(InvoiceHeaderDTO dto) {
//        InvoiceApplyHeader entity = new InvoiceApplyHeader();
//        BeanUtils.copyProperties(dto, entity);
//        return invoiceApplyHeaderRepository.save(entity);
//    }
//
//    public void deleteInvoiceHeader(Long id) {
//        invoiceApplyHeaderRepository.deleteById(id);
//    }

    private InvoiceHeaderDTO mapToDTO(InvoiceApplyHeader header) {
        InvoiceHeaderDTO dto = new InvoiceHeaderDTO();
        BeanUtils.copyProperties(header, dto);
        dto.setApplyStatusMeaning(lovAdapter.queryLovMeaning("HEXAM-INV-HEADER-STATUS-48207", header.getTenantId(), header.getApplyStatus()));
        dto.setInvoiceTypeMeaning(lovAdapter.queryLovMeaning("HEXAM-INV-HEADER-TYPE-48207", header.getTenantId(), header.getInvoiceType()));
        dto.setInvoiceColorMeaning(lovAdapter.queryLovMeaning("HEXAM-INV-HEADER-COLOR-48207", header.getTenantId(), header.getInvoiceColor()));
        return dto;
    }

    @Override
    public List<InvoiceHeaderDTO> getInvoiceHeadersByOrganization(Long tenantId) {
    List<InvoiceApplyHeader> headers = invoiceApplyHeaderRepository.findByOrganizationId(tenantId);
    return headers.stream()
            .map(header -> mapToDTO(header))
            .collect(Collectors.toList());
}

    @Override
    public InvoiceApplyHeader createInvoiceHeader(InvoiceHeaderDTO invoiceHeaderDTO) {
        InvoiceApplyHeader invoiceHeader = new InvoiceApplyHeader();
        // Map DTO to entity before saving
        BeanUtils.copyProperties(invoiceHeaderDTO, invoiceHeader);
        return invoiceApplyHeaderRepository.save(invoiceHeader);
    }

    @Override
    public InvoiceApplyHeader updateInvoiceHeader(InvoiceHeaderDTO invoiceHeaderDTO) {
        InvoiceApplyHeader existingHeader = invoiceApplyHeaderRepository.findById(invoiceHeaderDTO.getApplyHeaderId())
                .orElseThrow(() -> new RuntimeException("Invoice header not found"));
        BeanUtils.copyProperties(invoiceHeaderDTO, existingHeader);
        return invoiceApplyHeaderRepository.save(existingHeader);
    }

    @Override
    public void deleteInvoiceHeader(Long id) {
        invoiceApplyHeaderRepository.deleteById(id);
    }


}

