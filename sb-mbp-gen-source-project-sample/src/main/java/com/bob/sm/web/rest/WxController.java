package com.bob.sm.web.rest;

import com.bob.sm.dto.help.*;
import com.bob.sm.service.WxService;
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
    @PostMapping("/public/get-wx-open-id")
    public ResponseEntity<WxLoginStatusDTO> getOpenId(
            @RequestBody @Valid ParamWxOpenIdDTO wxOpenIdParamDTO) {
        log.debug("REST request to get openId : {}", wxOpenIdParamDTO);
        WxLoginStatusDTO wxLoginStatusDTO = wxService.getOpenIdAndLogin(wxOpenIdParamDTO);
        return new ResponseEntity<>(wxLoginStatusDTO, null, HttpStatus.OK);
    }

    /**
     * POST  /get-wx-js-api-info : Get JsApi Info.
     *
     * @return the ResponseEntity with status 200 (OK) and the result in body
     */
    @PostMapping("/public/get-wx-js-api-info")
    public ResponseEntity<ReturnCommonDTO<ReturnWxJsapiInfoDTO>> getJsApiInfo(@RequestBody @Valid ParamWxJsApiDTO wxJsApiDTO) {
        log.debug("REST request to get jsApi Info : {}", wxJsApiDTO);
        ReturnCommonDTO<ReturnWxJsapiInfoDTO> rtn = wxService.getJsapiInfoByCurrentAccessToken(wxJsApiDTO.getUrl());
        return new ResponseEntity<>(rtn, null, HttpStatus.OK);
    }

    /**
     * POST  /get-wx-js-api-info : Get JsApi Info.
     *
     * @return the ResponseEntity with status 200 (OK) and the result in body
     */
    @PostMapping("/public/get-address-by-loglat")
    public ResponseEntity<ReturnCommonDTO<ReturnMapAddress>> getAddressByLogLat(
            @RequestBody @Valid ParamLogLatDTO logLatDTO) {
        log.debug("REST request to get address by longitude and latitude Info : {}", logLatDTO);
        ReturnCommonDTO<ReturnMapAddress> rtn = wxService.getAddressByLogLat(logLatDTO);
        return new ResponseEntity<>(rtn, null, HttpStatus.OK);
    }

}
