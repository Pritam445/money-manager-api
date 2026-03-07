package com.money.manage.service;


import com.money.manage.dto.ExpenseDTO;
import com.money.manage.dto.IncomeDTO;
import org.springframework.data.domain.Sort;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeService {

    IncomeDTO addExpense(IncomeDTO dto);
    List<IncomeDTO> getCurrentMonthIncomesForCurrentUser();
    void deleteIncome(String incomeId);
    List<IncomeDTO> getLatest5IncomesForCurrentUser();
    BigDecimal getTotalIncomeForCurrentUser();
    List<IncomeDTO> filterIncome(LocalDate startDate, LocalDate endDate, String keyword, Sort sort);
    List<IncomeDTO> getIncomeForUserOnDate(String profileId, LocalDate date);
    ByteArrayInputStream exportIncomeToExcel(List<IncomeDTO> incomes);

}
