package com.vpcons.nsereportsystem.dto.nse.response;

import lombok.Data;

import java.util.List;

@Data
public class NSETokenResponse {
    private String memberCode;
    private List<String> code;
    private String loginId;
    private String token;
    private String status;
}
