package com.vpcons.nsereportsystem.utils;

import com.vpcons.nsereportsystem.dto.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class APIResponseUtil {

    public APIResponse successResponse(Object data){
        APIResponse apiResponse = new APIResponse();

        apiResponse.setStatus(AppConstant.SUCCESS_STATUS);
        apiResponse.setMessage(AppConstant.SUCCESS_MESSAGE);
        apiResponse.setData(data);
        return apiResponse;
    }

    public APIResponse createdSuccessResponse(Object data){
        APIResponse apiResponse = new APIResponse();

        apiResponse.setStatus(AppConstant.CREATED_SUCCESS_STATUS);
        apiResponse.setMessage(AppConstant.CREATED_SUCCESS_MESSAGE);
        apiResponse.setData(data);
        return apiResponse;
    }

    public APIResponse errorResponse(Object data){
        APIResponse apiResponse = new APIResponse();

        apiResponse.setStatus(AppConstant.SUCCESS_STATUS);
        apiResponse.setMessage(AppConstant.SUCCESS_MESSAGE);
        apiResponse.setData(data);
        return apiResponse;
    }

    public APIResponse alreadyExist(Object data){
        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatus(AppConstant.ALREADY_EXIST_STATUS);
        apiResponse.setMessage(AppConstant.ALREADY_EXIST_MESSAGE);
        apiResponse.setData(data);
        return apiResponse;
    }

    public APIResponse notFound(Object data){
        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatus(AppConstant.NOT_FOUND_STATUS);
        apiResponse.setMessage(AppConstant.NOT_FOUND_MESSAGE);
        apiResponse.setData(data);
        return apiResponse;
    }

    public APIResponse badRequest(Object data){
        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatus(AppConstant.BAD_REQUEST_STATUS);
        apiResponse.setMessage(AppConstant.BAD_REQUEST_MESSAGE);
        apiResponse.setData(data);
        return apiResponse;
    }


    public APIResponse failResponse(Object data){
        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatus(AppConstant.FAIL_STATUS);
        apiResponse.setMessage(AppConstant.FAIL_MESSAGE);
        apiResponse.setData(data);
        return apiResponse;
    }

    public APIResponse authFailResponse(Object data){
        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatus(AppConstant.AUTH_FAIL_STATUS);
        apiResponse.setMessage(AppConstant.AUTH_FAIL_MESSAGE);
        apiResponse.setData(data);
        return apiResponse;
    }


    //add entry if dding new response function
    public ResponseEntity<APIResponse> apiResponseToEntityResponse(APIResponse apiResponse){
        ResponseEntity<APIResponse> apiResponseResponseEntity = null;
        if(apiResponse == null)
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);//500

        switch (apiResponse.getStatus()){
            case AppConstant.SUCCESS_STATUS:
                apiResponseResponseEntity =  new ResponseEntity<>(apiResponse, HttpStatus.OK);//200
                break;
            case AppConstant.CREATED_SUCCESS_STATUS:
                apiResponseResponseEntity =  new ResponseEntity<>(apiResponse, HttpStatus.CREATED);//201
                break;
            case AppConstant.ERROR_STATUS:
                apiResponseResponseEntity =  new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);//500
                break;
            case AppConstant.NOT_FOUND_STATUS:
                apiResponseResponseEntity =  new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);//404
                break;
            case AppConstant.BAD_REQUEST_STATUS:
                apiResponseResponseEntity =  new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);//400
                break;
            case AppConstant.AUTH_FAIL_STATUS:
                apiResponseResponseEntity =  new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);//401
                break;
            default:
                apiResponseResponseEntity =  new ResponseEntity<>(apiResponse, HttpStatus.FAILED_DEPENDENCY);//424

        }
        return apiResponseResponseEntity;
    }


}

