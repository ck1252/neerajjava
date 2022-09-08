package com.vpcons.nsereportsystem.controller;

import com.vpcons.nsereportsystem.config.BootManager;
import com.vpcons.nsereportsystem.dto.response.APIResponse;
import com.vpcons.nsereportsystem.service.NSEServiceLive;
import com.vpcons.nsereportsystem.utils.APIResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController("nse")
@CrossOrigin(origins = "*")
public class NSEController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    APIResponseUtil apiResponseUtil;

    @Autowired
    NSEServiceLive nseServiceLive;

    @Autowired
    BootManager bootManager;

    @GetMapping("/createtoken")
    public ResponseEntity<APIResponse> createToken(){
        logger.info("Craete token request");
        APIResponse apiResponse = nseServiceLive.getToken();
        return apiResponseUtil.apiResponseToEntityResponse(apiResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<APIResponse> logoutToken(@RequestHeader("Authorization") String authorization){
        logger.info(" token logout request");
        APIResponse apiResponse = nseServiceLive.logout(authorization);
        return apiResponseUtil.apiResponseToEntityResponse(apiResponse);
    }

    @PostMapping("/balanceupload")
    public ResponseEntity<APIResponse> tradingEodBalanceUpload(@RequestParam("file") MultipartFile file){
        logger.info("tradingEodBalanceUpload request");
        APIResponse apiResponse = nseServiceLive.uploadTradingBalance(file);
        return apiResponseUtil.apiResponseToEntityResponse(apiResponse);
    }

    @PostMapping("/tradingupload")
    public ResponseEntity<APIResponse> tradingUpload(@RequestParam("file") MultipartFile file){
        logger.info("tradingEodBalanceUpload request");
        APIResponse apiResponse = nseServiceLive.uploadTrading(file);
        return apiResponseUtil.apiResponseToEntityResponse(apiResponse);
    }

    @GetMapping("/shutdown")
    public ResponseEntity<APIResponse> Shutdown(){
        logger.info("shutdown request");
        bootManager.initiateShutdown(0);
        APIResponse apiResponse = apiResponseUtil.failResponse("Shutdown failed");
        return apiResponseUtil.apiResponseToEntityResponse(apiResponse);
    }


}
