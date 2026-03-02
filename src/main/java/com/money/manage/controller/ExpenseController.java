package com.money.manage.controller;

import com.money.manage.dto.ExpenseDTO;
import com.money.manage.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/add")
    public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO expenseDTO){

        ExpenseDTO saved = expenseService.addExpense(expenseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }

    @GetMapping("/currentMonthExpenses")
    public ResponseEntity<List<ExpenseDTO>> getCurrentMonthExpenses() {

        List<ExpenseDTO> expenses = expenseService.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.ok(expenses);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<String> deleteExpenses(@PathVariable String expenseId) {

        expenseService.deleteExpenses(expenseId);

        return ResponseEntity.ok("Deleted Successfully");

    }
}
