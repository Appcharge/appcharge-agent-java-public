package com.appcharge.server.enums.events;

public enum EventType {
    order_created,
    payment_intent_success,
    payment_intent_failed,
    order_completed_success,
    order_completed_failed,
    order_canceled
}
