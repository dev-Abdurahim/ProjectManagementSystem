package org.example.projectmanagementsystem.util;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public final class WorkingDaysCalculator {

    private static final Set<DayOfWeek> WEEKENDS = new HashSet<>(Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));

    private WorkingDaysCalculator() {
        // Private constructor – util class instansiyalanmasin
    }

    public static int calculate(LocalDate start, LocalDate end){
        if(start.isAfter(end)){
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        long totalDays = ChronoUnit.DAYS.between(start,end) + 1;
        long weekends = countWeekends(start, end);

        return (int) (totalDays - weekends);
    }
    /**
     * Berilgan intervaldagi weekend kunlari sonini hisoblaydi.
     */
    private static long countWeekends(LocalDate start, LocalDate end) {
        long count = 0;
        LocalDate current = start;
        while (!current.isAfter(end)) {
            if (WEEKENDS.contains(current.getDayOfWeek())) {
                count++;
            }
            current = current.plusDays(1);
        }
        return count;
    }

}
