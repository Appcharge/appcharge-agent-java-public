package com.appcharge.server.models.analytics;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AnalyticsRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String[] metrics;
    private String incomeType;
}
