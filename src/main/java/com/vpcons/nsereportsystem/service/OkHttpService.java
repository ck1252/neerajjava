package com.vpcons.nsereportsystem.service;

import com.vpcons.nsereportsystem.utils.NSEUtil;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class OkHttpService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NSEUtil nseUtil;

    public String postRequest(String url, String bodyStr, Map<String, String> header) {
        logger.info("API url: " + url);
        logger.info("API body: " + bodyStr);
        logger.info("API headers: " + header);

        Headers headerbuild = Headers.of(header);

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("Gzip");
        RequestBody body = RequestBody.create(mediaType, nseUtil.compress(bodyStr));
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .headers(headerbuild)
                //.addHeader("Authorization",auth)
                .build();
        try {
            Response response = client.newCall(request).execute();
            logger.info("response code {}", response.code());
            String responseBody = new String(nseUtil.decompress(response.body().bytes()));
            logger.info("response body {}", responseBody);
            return responseBody;
        } catch (Exception e) {
            logger.error("eoorr", e);
            throw new RuntimeException(e);
        }
    }

    public String postFormRequest(String url, String fileName, String bodyStr, Map<String, String> header) {
        logger.info("API url: " + url);
        logger.info("API body: " + bodyStr);
        logger.info("API headers: " + header);
        logger.info("API fileName: " + fileName);

        Headers headerbuild = Headers.of(header);

        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
        ;

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("File", fileName,
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                nseUtil.compress(bodyStr)))
                .build();


        // RequestBody body = RequestBody.create(mediaType, nseUtil.compress(bodyStr) );
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .headers(headerbuild)
                //.addHeader("Authorization",auth)
                .build();
        try {
            Response response = client.newCall(request).execute();
            logger.info("response code {}", response.code());
            String responseBody = new String(nseUtil.decompress(response.body().bytes()));
            logger.info("response body {}", responseBody);
            return responseBody;
        } catch (Exception e) {
            logger.error("eoorr", e);
            throw new RuntimeException(e);
        }
    }

}
