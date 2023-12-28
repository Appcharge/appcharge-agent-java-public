package com.appcharge.server.models.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ItemBalance {
    private final String publisherProductId;
    private final int quantity;
}
