package com.appcharge.server.service.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Signature {
    private final String t;
    private final String v1;
}
