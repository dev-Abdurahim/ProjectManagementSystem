package org.example.projectmanagementsystem.mapper;

import org.example.projectmanagementsystem.dto.dashboard.MonthlyAllocationDto;
import org.example.projectmanagementsystem.entity.MonthlyAllocation;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
@Mapper(componentModel = "spring")
public interface MonthlyRevenueMapper {

    MonthlyAllocationDto toDto(MonthlyAllocation entity);

    List<MonthlyAllocationDto> toDtoList(List<MonthlyAllocation> entities);

    default YearMonth map(LocalDate value) {
        return value == null ? null : YearMonth.from(value);
    }

    default LocalDate map(YearMonth value) {
        return value == null ? null : value.atDay(1);
    }
}
