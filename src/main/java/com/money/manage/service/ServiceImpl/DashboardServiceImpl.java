package com.money.manage.service.ServiceImpl;

import com.money.manage.dto.ExpenseDTO;
import com.money.manage.dto.IncomeDTO;
import com.money.manage.dto.RecentTransactionDTO;
import com.money.manage.entity.ProfileEntity;
import com.money.manage.service.DashboardService;
import com.money.manage.service.ExpenseService;
import com.money.manage.service.IncomeService;
import com.money.manage.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;


    @Override
    public Map<String, Object> getDashboardData() {

        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String,Object> returnValue = new LinkedHashMap<>();
        List<IncomeDTO> latestIncomes = incomeService.getLatest5IncomesForCurrentUser();
        List<ExpenseDTO> latestExpenses = expenseService.getLatest5ExpensesForCurrentUser();
        List<RecentTransactionDTO> recentTransactions = concat(latestIncomes.stream().map(income ->
                RecentTransactionDTO.builder()
                        .id(income.getId())
                        .profileId(profile.getId())
                        .name(income.getName())
                        .icon(income.getIcon())
                        .amount(income.getAmount())
                        .date(income.getDate())
                        .createdAt(income.getCreatedAt())
                        .updatedAt(income.getUpdatedAt())
                        .type("income")
                        .build()

        ), latestExpenses.stream().map(expense ->
                RecentTransactionDTO.builder()
                        .id(expense.getId())
                        .name(expense.getName())
                        .profileId(profile.getId())
                        .icon(expense.getIcon())
                        .amount(expense.getAmount())
                        .type("expense")
                        .date(expense.getDate())
                        .createdAt(expense.getCreatedAt())
                        .updatedAt(expense.getUpdatedAt())
                        .build()
        )).sorted((a,b) ->{
            int cmp = b.getDate().compareTo(a.getDate());
            if (cmp == 0 && a.getCreatedAt() !=null && b.getCreatedAt() !=null) {
                return b.getCreatedAt().compareTo(a.getCreatedAt());
            }
            return cmp;
        }).toList();

        returnValue.put("totalBalance", incomeService.getTotalIncomeForCurrentUser().subtract(expenseService.getTotalExpensesForCurrentUser()));
        returnValue.put("totalIncome",incomeService.getTotalIncomeForCurrentUser());
        returnValue.put("totalExpense", expenseService.getTotalExpensesForCurrentUser());
        returnValue.put("recent5Expenses", latestExpenses);
        returnValue.put("recent5Incomes",latestIncomes);
        returnValue.put("recentTransactions",recentTransactions);

        return returnValue;

    }
}
