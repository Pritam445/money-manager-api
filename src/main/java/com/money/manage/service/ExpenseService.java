package com.money.manage.service;

import com.money.manage.dto.ExpenseDTO;
import com.money.manage.dto.IncomeDTO;
import org.springframework.data.domain.Sort;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    ExpenseDTO addExpense(ExpenseDTO dto);
    List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser();
    void deleteExpenses(String expenseId);
    List<ExpenseDTO> getLatest5ExpensesForCurrentUser();
    BigDecimal getTotalExpensesForCurrentUser();
    List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort);
    List<ExpenseDTO> getExpensesForUserOnDate(String profileId, LocalDate date);
    ByteArrayInputStream exportIncomeToExcel(List<ExpenseDTO> expenses);
}
