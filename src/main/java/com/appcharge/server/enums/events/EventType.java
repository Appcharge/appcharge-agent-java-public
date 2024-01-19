package com.appcharge.server.enums.events;

public enum EventType {
    order_created,
    payment_intent_success,
    payment_intent_failed,
    order_completed_success,
    order_completed_failed,
    order_canceled,
    login_land,
    login_clicked,
    login_screen_presented,
    login_otp_ios_button,
    login_canceled,
    login_approval,
    login_result
}
