package com.bob.sm.web.rest;

import com.bob.sm.dto.help.ParamWxOpenIdDTO;
import com.bob.sm.dto.help.WxLoginStatusDTO;
import com.bob.sm.service.WxService;
import com.bob.sm.web.rest.errors.BadRequestAlertException;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class WxController {

    private final Logger log = LoggerFactory.getLogger(WxController.class);

    @Autowired
    private WxService wxService;

    /**
     * POST  /get-open-id : Get Open Id.
     *
     * @param wxOpenIdParamDTO the request parameter
     * @return the ResponseEntity with status 200 (OK) and the result in body
     */
    @PostMapping("/get-open-id")
    @Timed
    public ResponseEntity<WxLoginStatusDTO> getOpenId(
            @RequestBody @Valid ParamWxOpenIdDTO wxOpenIdParamDTO) {
        log.debug("REST request to get openId : {}", wxOpenIdParamDTO);
        if (wxOpenIdParamDTO.getJsCode() == null || "".equals(wxOpenIdParamDTO.getJsCode().trim())
                || wxOpenIdParamDTO.getWxName() == null || "".equals(wxOpenIdParamDTO.getWxName().trim())) {
            throw new BadRequestAlertException("jsCode和wxName不得为空", "fsAppOpenIdParamDTO", "paramnull");
        }
        WxLoginStatusDTO wxLoginStatusDTO = wxService.getOpenIdAndLogin(wxOpenIdParamDTO);
        return new ResponseEntity<>(wxLoginStatusDTO, null, HttpStatus.OK);
    }

}
