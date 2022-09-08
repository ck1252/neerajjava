package com.vpcons.nsereportsystem.dto.nse.request;

import lombok.Data;

@Data
public class NSELogoutRequest {
    private String memberCode;
    private String loginId;
}
