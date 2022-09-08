package com.vpcons.nsereportsystem.dto.nse.request;

import lombok.Data;

@Data
public class NSETokenRequest {
    private String memberCode;
    private String loginId;
    private String password;
}
