package com.hand.demo.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hand.demo.api.dto.PrefixDTO;
import com.hand.demo.app.service.ExampleService;
import com.hand.demo.infra.mapper.ExampleMapper;
import com.hand.demo.infra.mapper.InvoiceApplyHeaderMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.apaas.common.userinfo.domain.UserVO;
import org.hzero.boot.apaas.common.userinfo.infra.feign.IamRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * ExampleServiceImpl
 */
@Service
public class ExampleServiceImpl implements ExampleService {

    @Autowired
    private InvoiceApplyHeaderMapper invoiceApplyHeaderMapper;

    @Autowired
    private IamRemoteService iamRemoteService;

    @Autowired
    private ExampleMapper exampleMapper;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public Page<PrefixDTO> getInvoiceData(PageRequest pageRequest, PrefixDTO prefixDTO, Long organizationId){
        try{
            ResponseEntity<String> data =iamRemoteService.selectSelf();
            String body = data.getBody();
            UserVO userVO = objectMapper.readValue(body, UserVO.class);
            Boolean tenantAdminFlag = userVO.getTenantAdminFlag();
            PrefixDTO prefixDTO1 = new PrefixDTO();
            if(tenantAdminFlag != null && !tenantAdminFlag)
            {
                prefixDTO.setCreatedBy(userVO.getId().toString());
            }
            return PageHelper.doPageAndSort(pageRequest, () -> exampleMapper.selectList(prefixDTO1));
        }catch (Exception e){
            throw new RuntimeException("Failed to get user info");
        }
    }

}



