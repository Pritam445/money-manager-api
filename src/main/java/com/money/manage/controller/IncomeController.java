package com.money.manage.controller;

import com.money.manage.dto.ExpenseDTO;
import com.money.manage.dto.IncomeDTO;
import com.money.manage.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping("/add")
    public ResponseEntity<IncomeDTO> addExpense(@RequestBody IncomeDTO incomeDTO){

        IncomeDTO saved = incomeService.addExpense(incomeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }

    @GetMapping("/currentMonthIncome")
    public ResponseEntity<List<IncomeDTO>> getCurrentMonthExpenses() {

        List<IncomeDTO> expenses = incomeService.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.ok(expenses);
    }

    @DeleteMapping("/{incomeId}")
    public ResponseEntity<String> deleteExpenses(@PathVariable String incomeId) {

        incomeService.deleteIncome(incomeId);

        return ResponseEntity.ok("Deleted Successfully");

    }
}
