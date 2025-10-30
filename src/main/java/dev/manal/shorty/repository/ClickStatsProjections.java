package dev.manal.shorty.repository;

import java.time.LocalDate;

public interface ClickStatsProjections {
    interface DayCount {
        LocalDate getDay();
        long getCnt();
    }
    interface RefCount {
        String getRef();
        long getCnt();
    }
}
