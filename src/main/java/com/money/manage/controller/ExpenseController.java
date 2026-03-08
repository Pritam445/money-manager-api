package com.money.manage.controller;

import com.money.manage.dto.ExpenseDTO;
import com.money.manage.dto.IncomeDTO;
import com.money.manage.service.EmailService;
import com.money.manage.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final EmailService emailService;

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

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadIncomeExcel() throws IOException {

        List<ExpenseDTO> expenses = expenseService.getCurrentMonthExpensesForCurrentUser();

        ByteArrayInputStream stream = expenseService.exportIncomeToExcel(expenses);

        InputStreamResource file = new InputStreamResource(stream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=incomes.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    @GetMapping("/email")
    public ResponseEntity<String> sendIncomeReport(@RequestParam String email)
            throws Exception {

        List<ExpenseDTO> incomes = expenseService.getCurrentMonthExpensesForCurrentUser();

        ByteArrayInputStream excelFile =
                expenseService.exportIncomeToExcel(incomes);

        emailService.sendEmailWithFile(
                email,
                "Your Income Report",
                "Please find attached your income report.",
                excelFile,
                "income-report.xlsx"
        );

        return ResponseEntity.ok("Email sent successfully");
    }

}
