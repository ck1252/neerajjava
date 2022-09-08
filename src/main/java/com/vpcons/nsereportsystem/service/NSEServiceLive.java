package com.vpcons.nsereportsystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpcons.nsereportsystem.dto.nse.request.NSEBankBalanceRequest;
import com.vpcons.nsereportsystem.dto.nse.request.NSELogoutRequest;
import com.vpcons.nsereportsystem.dto.nse.request.NSETokenRequest;
import com.vpcons.nsereportsystem.dto.nse.response.NSETokenResponse;
import com.vpcons.nsereportsystem.dto.response.APIResponse;
import com.vpcons.nsereportsystem.utils.APIResponseUtil;
import com.vpcons.nsereportsystem.utils.AppConstant;
import com.vpcons.nsereportsystem.utils.NSEUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class NSEServiceLive {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${vpcons.nse.api.token}")
    private String NSE_API_URL_CREATE_TOKEN;

    @Value("${vpcons.nse.api.bank-balance}")
    private String NSE_API_URL_UPLOAD_BANK_BALANCE;

    @Value("${vpcons.nse.api.trading-upload}")
    private String NSE_API_URL_UPLOAD_TRADING;

    @Value("${vpcons.nse.member-code}")
    private String NSE_MEMBER_CODE;

    @Value("${vpcons.nse.login-id}")
    private String NSE_LOGIN_ID;

    @Value("${vpcons.nse.secret}")
    private String NSE_SECRET;

    @Value("${vpcons.nse.password}")
    private String NSE_PASSWORD;

    @Value("#{'${vpcons.nse.api.success}'.split(',')}")
    private List<String> NSE_SUCCESS_CODE_LIST;

    @Value("${vpcons.nse.api.referer}")
    private String NSE_REFERER;

    @Value("${vpcons.nse.api.user-agent}")
    private String NSE_USER_AGENT;


    @Autowired
    APIResponseUtil apiResponseUtil;

    @Autowired
    OkHttpService okHttpService;

    @Autowired
    NSEUtil nseUtil;

    public APIResponse getToken() {
        logger.info("token requested");
        APIResponse apiResponse = apiResponseUtil.failResponse(AppConstant.UNKNOWN_ERROR);

        NSETokenResponse nseTokenResponse = genrateToken();
        if (NSE_SUCCESS_CODE_LIST.contains(nseTokenResponse.getStatus())) {
            logger.info("Token created success");
            apiResponse = apiResponseUtil.successResponse(nseTokenResponse);
        } else {
            logger.warn("NSE resonse: {}", nseTokenResponse);
            apiResponse = apiResponseUtil.failResponse("Could not genrate token");
        }


        return apiResponse;
    }

    private NSETokenResponse genrateToken() {
        try {
            NSETokenRequest nseTokenRequest = buildTokenRequest();
            ObjectMapper objectMapper = new ObjectMapper();
            String body = objectMapper.writeValueAsString(nseTokenRequest);

            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Accept", "*/*");
            // headers.put("Content-Encoding","Gzip");
            headers.put("Accept-Encoding", "gzip, deflate, br");
            // headers.put("Accept-Language","en-US");
            // headers.put("Connection","keep-alive");
            //headers.put("Host","api.nseindia.com");
            if(NSE_REFERER != null && NSE_REFERER.length() > 5)
                headers.put("Referer", NSE_REFERER);
            if(NSE_USER_AGENT != null && NSE_USER_AGENT.length() > 5)
                headers.put("User-Agent", NSE_USER_AGENT);


            String responseBody = okHttpService.postRequest(NSE_API_URL_CREATE_TOKEN, body, headers);
            NSETokenResponse nseTokenResponse = objectMapper.readValue(responseBody, NSETokenResponse.class);
            return nseTokenResponse;
        } catch (Exception e) {
            logger.error("genrate token error", e);
        }
        return null;
    }

    public APIResponse logout(String authToken) {
        logger.info("token requested");
        APIResponse apiResponse = apiResponseUtil.failResponse(AppConstant.UNKNOWN_ERROR);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = null;
        try {
            body = objectMapper.writeValueAsString(buildLogoutRequest());
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Accept", "*/*");
            // headers.put("Content-Encoding","Gzip");
            headers.put("Accept-Encoding", "gzip, deflate, br");
            // headers.put("Accept-Language","en-US");
            // headers.put("Connection","keep-alive");
            //headers.put("Host","api.nseindia.com");
            if(NSE_REFERER != null && NSE_REFERER.length() > 5)
                headers.put("Referer", NSE_REFERER);
            if(NSE_USER_AGENT != null && NSE_USER_AGENT.length() > 5)
                headers.put("User-Agent", NSE_USER_AGENT);
            headers.put("Authorization", authToken);
            String responseBody = okHttpService.postRequest(NSE_API_URL_CREATE_TOKEN, body, headers);
            logger.info("response str {}", responseBody);
            apiResponse = apiResponseUtil.successResponse(responseBody);
        } catch (JsonProcessingException e) {
            logger.error("Error while creating token");
            apiResponse = apiResponseUtil.errorResponse("Create token failed: " + e.getMessage());
        }


        return apiResponse;
    }

    public APIResponse uploadTradingBalance(MultipartFile file) {
        APIResponse apiResponse = apiResponseUtil.failResponse(AppConstant.UNKNOWN_ERROR);
        //uploadFile(file);

        try {
            String csvJson = CSVfileToJson(file.getInputStream());
            //logger.info(csvJson);
            NSEBankBalanceRequest nseBankBalanceRequest = new NSEBankBalanceRequest();
            nseBankBalanceRequest.setBankBalanceData(new ObjectMapper().readTree(csvJson));
            // apiResponse = apiResponseUtil.successResponse(nseBankBalanceRequest);


            ObjectMapper objectMapper = new ObjectMapper();
            String body = objectMapper.writeValueAsString(nseBankBalanceRequest);

            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "multipart/form-data");
            headers.put("Accept", "*/*");
            headers.put("Content-Encoding", "Gzip");
            headers.put("Accept-Encoding", "gzip, deflate, br");
            // headers.put("Accept-Language","en-US");
            // headers.put("Connection","keep-alive");
            //headers.put("Host","api.nseindia.com");
            if(NSE_REFERER != null && NSE_REFERER.length() > 5)
                headers.put("Referer", NSE_REFERER);
            if(NSE_USER_AGENT != null && NSE_USER_AGENT.length() > 5)
                headers.put("User-Agent", NSE_USER_AGENT);
            headers.put("Authorization", "Bearer " + genrateToken().getToken());
            String fileName = file.getOriginalFilename();
            fileName = fileName.split("\\.")[0];
            fileName = fileName + ".txt.gz";

            String responseBody = okHttpService.postFormRequest(NSE_API_URL_UPLOAD_BANK_BALANCE, fileName, body, headers);
            logger.info(NSE_API_URL_UPLOAD_BANK_BALANCE + " responseBody: ", responseBody);
            apiResponse = apiResponseUtil.successResponse(new ObjectMapper().readTree(responseBody));
        } catch (IOException e) {
            logger.error("Error while creating token");
            apiResponse = apiResponseUtil.errorResponse("Read CSV file error: " + e.getMessage());
        }


        return apiResponse;
    }


    public APIResponse uploadTrading(MultipartFile file) {
        APIResponse apiResponse = apiResponseUtil.failResponse(AppConstant.UNKNOWN_ERROR);
        //uploadFile(file);

        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "multipart/form-data");
            headers.put("Accept", "*/*");
            headers.put("Content-Encoding", "Gzip");
            headers.put("Accept-Encoding", "gzip, deflate, br");
            // headers.put("Accept-Language","en-US");
            // headers.put("Connection","keep-alive");
            //headers.put("Host","api.nseindia.com");
            if(NSE_REFERER != null && NSE_REFERER.length() > 5)
                headers.put("Referer", NSE_REFERER);
            if(NSE_USER_AGENT != null && NSE_USER_AGENT.length() > 5)
                headers.put("User-Agent", NSE_USER_AGENT);
            headers.put("Authorization", "Bearer " + genrateToken().getToken());
            String fileName = file.getOriginalFilename();
            fileName = fileName.split("\\.")[0];
            fileName = fileName + ".csv.gz";

            String body = new String(file.getBytes());

            String responseBody = okHttpService.postFormRequest(NSE_API_URL_UPLOAD_TRADING, fileName, body, headers);
            logger.info(NSE_API_URL_UPLOAD_TRADING + " responseBody: ", responseBody);
            apiResponse = apiResponseUtil.successResponse(new ObjectMapper().readTree(responseBody));
        } catch (IOException e) {
            logger.error("Error while creating token");
            apiResponse = apiResponseUtil.errorResponse("Read CSV file error: " + e.getMessage());
        }


        return apiResponse;
    }


    private String CSVfileToJson(InputStream is) {
        try {
            List<String> result = new ArrayList<>();
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
            is.close();
            return csvListToJson(result);
        } catch (IOException e) {
            logger.error("readind CVS error", e);
        }
        return null;
    }

    private String csvListToJson(List<String> csv) {

        //remove empty lines
        //this will affect permanently the list.
        //be careful if you want to use this list after executing this method
        csv.removeIf(e -> e.trim().isEmpty());

        //csv is empty or have declared only columns
        if (csv.size() <= 1) {
            return "[]";
        }

        //get first line = columns names
        String[] columns = csv.get(0).split(",");

        //get all rows
        StringBuilder json = new StringBuilder("[\n");
        csv.subList(1, csv.size()) //substring without first row(columns)
                .stream()
                .map(e -> e.split(","))
                .filter(e -> e.length == columns.length) //values size should match with columns size
                .forEach(row -> {

                    json.append("\t{\n");

                    for (int i = 0; i < columns.length; i++) {
                        json.append("\t\t\"")
                                .append(columns[i])
                                .append("\" : \"")
                                .append(row[i])
                                .append("\",\n"); //comma-1
                    }

                    //replace comma-1 with \n
                    json.replace(json.lastIndexOf(","), json.length(), "\n");

                    json.append("\t},"); //comma-2

                });

        //remove comma-2
        json.replace(json.lastIndexOf(","), json.length(), "");

        json.append("\n]");

        return json.toString();

    }

    private boolean uploadFile(MultipartFile file) {
        try {
            InputStream in = file.getInputStream();
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            String fileLocation = path.substring(0, path.length() - 1) + file.getOriginalFilename();
            FileOutputStream f = new FileOutputStream(fileLocation);
            int ch = 0;
            while ((ch = in.read()) != -1) {
                f.write(ch);
            }
            f.flush();
            f.close();
            logger.info("messageFile: " + file.getOriginalFilename() + " has been uploaded successfully!");
            return true;
        } catch (Exception e) {
            logger.error("error", e);
        }
        return false;
    }

    private NSETokenRequest buildTokenRequest() throws Exception {
        NSETokenRequest nseTokenRequest = new NSETokenRequest();
        nseTokenRequest.setLoginId(NSE_LOGIN_ID);
        // nseTokenRequest.setPassword(NSE_API_PASSWORD);
        nseTokenRequest.setPassword(nseUtil.getAPIPassword(NSE_PASSWORD, NSE_SECRET));
        nseTokenRequest.setMemberCode(NSE_MEMBER_CODE);
        return nseTokenRequest;
    }

    private NSELogoutRequest buildLogoutRequest() {
        NSELogoutRequest nseLogoutRequest = new NSELogoutRequest();
        nseLogoutRequest.setLoginId(NSE_LOGIN_ID);
        nseLogoutRequest.setMemberCode(NSE_MEMBER_CODE);
        return nseLogoutRequest;
    }

}
