package com.appcharge.server.models.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OtpAuthenticationRequest {
    public enum DeviceType {
        DESKTOP,
        MOBILE,
        APPCHARGE
    }

    private DeviceType device;
    private String date;
}
