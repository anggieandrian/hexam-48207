package com.hand.demo.api.controller.v1;

import com.hand.demo.domain.entity.InvoiceApplyLine;
import com.hand.demo.domain.entity.InvoiceInfoQueue;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.interfaces.sdk.invoke.InterfaceInvokeSdk;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hand.demo.app.service.InvoiceApplyHeaderService;
import com.hand.demo.domain.entity.InvoiceApplyHeader;
import com.hand.demo.domain.repository.InvoiceApplyHeaderRepository;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * (InvoiceApplyHeader)表控制层
 *
 * @author Anggie A
 * @since 2024-12-10 11:19:56
 */

@RestController("invoiceApplyHeaderController.v1")
@RequestMapping("/v1/{organizationId}/invoice-apply-headers")
public class InvoiceApplyHeaderController extends BaseController {

    @Autowired
    private InvoiceApplyHeaderRepository invoiceApplyHeaderRepository;

    @Autowired
    private InvoiceApplyHeaderService invoiceApplyHeaderService;


    // untuk nomor satu (3)
    @ApiOperation(value = "LIST")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<InvoiceApplyHeader>> list(InvoiceApplyHeader invoiceApplyHeader,
                                                         @PathVariable Long organizationId,
                                                         @ApiIgnore @SortDefault(value = InvoiceApplyHeader.FIELD_APPLY_HEADER_ID,
                                                                 direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<InvoiceApplyHeader> list = invoiceApplyHeaderService.selectList(pageRequest, invoiceApplyHeader);
        return Results.success(list);
    }


    // untuk nomor dua (4)
    @ApiOperation(value = "DETAILS")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{applyHeaderId}/detail")
    @ProcessLovValue
        public ResponseEntity<InvoiceApplyHeader> detail(@PathVariable Long applyHeaderId) {
            return invoiceApplyHeaderService.detail(applyHeaderId);
    }

    // untuk nomor tiga (5)
    @ApiOperation(value = "SAVE DATA" )
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<InvoiceApplyHeader>> save(@PathVariable Long organizationId, @RequestBody List<InvoiceApplyHeader> invoiceApplyHeaders) {
        validObject(invoiceApplyHeaders);
        SecurityTokenHelper.validTokenIgnoreInsert(invoiceApplyHeaders);
        invoiceApplyHeaders.forEach(item -> item.setTenantId(organizationId));
        invoiceApplyHeaderService.saveData(invoiceApplyHeaders);
        return Results.success(invoiceApplyHeaders);
    }


    // no 6
    @ApiOperation(value = "REMOVEBYID")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> removeById(@RequestBody List<InvoiceApplyHeader> invoiceApplyHeaders) {
        SecurityTokenHelper.validToken(invoiceApplyHeaders);
        invoiceApplyHeaderRepository.batchDeleteByPrimaryKey(invoiceApplyHeaders);
        return Results.success();
    }

    // nomor 8
    @ApiOperation(value = "REMOVE")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping("/deleteid")
    public ResponseEntity<?> remove(@RequestBody List<InvoiceApplyHeader> invoiceApplyHeaders) {
        SecurityTokenHelper.validToken(invoiceApplyHeaders);
        invoiceApplyHeaderRepository .batchDeleteByPrimaryKey(invoiceApplyHeaders);
        return Results.success();
    }

    // nomor 9 untuk export
    @GetMapping("/export")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @ExcelExport(InvoiceApplyHeader.class)
    public ResponseEntity<List<InvoiceApplyHeader>> export(
            @RequestParam("exportType") String exportType,
            @ApiIgnore @RequestParam(value = "exportParam", required = false) ExportParam exportParam,
            HttpServletResponse response,
            @PathVariable Long organizationId,
            @ApiIgnore @SortDefault(value = InvoiceApplyHeader.FIELD_APPLY_HEADER_ID, direction = Sort.Direction.ASC)
            PageRequest pageRequest) {

        InvoiceApplyHeader invoiceApplyHeader = new InvoiceApplyHeader();
        invoiceApplyHeader.setTenantId(organizationId);
        Page<InvoiceApplyHeader> page = invoiceApplyHeaderService.selectList(pageRequest, invoiceApplyHeader);
        return ResponseEntity.ok(page);
    }

}

