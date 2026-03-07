package com.money.manage.controller;

import com.money.manage.dto.ExpenseDTO;
import com.money.manage.dto.IncomeDTO;
import com.money.manage.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
    public ResponseEntity<List<IncomeDTO>> getCurrentMonthIncomes() {

        List<IncomeDTO> expenses = incomeService.getCurrentMonthIncomesForCurrentUser();
        return ResponseEntity.ok(expenses);
    }

    @DeleteMapping("/{incomeId}")
    public ResponseEntity<String> deleteExpenses(@PathVariable String incomeId) {

        incomeService.deleteIncome(incomeId);

        return ResponseEntity.ok("Deleted Successfully");

    }

    @GetMapping
    public ResponseEntity<BigDecimal> getTotalIncomeForCurrentUser() {
        BigDecimal totalIncomeForCurrentUser = incomeService.getTotalIncomeForCurrentUser();

        return ResponseEntity.ok(totalIncomeForCurrentUser);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadIncomeExcel() throws IOException {

        List<IncomeDTO> incomes = incomeService.getCurrentMonthIncomesForCurrentUser();

        ByteArrayInputStream stream = incomeService.exportIncomeToExcel(incomes);

        InputStreamResource file = new InputStreamResource(stream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=incomes.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}
