package org.example.projectmanagementsystem.projection;

import java.math.BigDecimal;

public interface DelayedPmProjection {
    String getPmName();
    Integer getDelayedCount();
    BigDecimal getAverageDelayPercent();
}
