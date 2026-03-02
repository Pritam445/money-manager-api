package com.money.manage.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecentTransactionDTO {

    private String id;
    private String profileId;
    private String icon;
    private String name;
    private BigDecimal amount;
    private LocalDate date;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
