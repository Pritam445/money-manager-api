package com.money.manage.service.ServiceImpl;

import com.money.manage.dto.ExpenseDTO;
import com.money.manage.entity.CategoryEntity;
import com.money.manage.entity.ExpenseEntity;
import com.money.manage.entity.ProfileEntity;
import com.money.manage.repository.CategoryRepository;
import com.money.manage.repository.ExpenseRepository;
import com.money.manage.service.CategoryService;
import com.money.manage.service.ExpenseService;
import com.money.manage.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final CategoryRepository categoryRepository;
    private  final ExpenseRepository expenseRepository;
    private final ProfileService profileService;

    private ExpenseDTO toDTO(ExpenseEntity entity){

        return ExpenseDTO.builder()
                .id(entity.getId())
                .icon(entity.getIcon())
                .name(entity.getName())
                .amount(entity.getAmount())
                .date(entity.getDate())
                .categoryId(entity.getCategory().getId())
                .categoryName(entity.getCategory().getName())
                .updatedAt(entity.getUpdatedAt())
                .createdAt(entity.getCreatedAt())
                .build();

    }

    private ExpenseEntity toEntity(CategoryEntity category, ProfileEntity profileEntity, ExpenseDTO expenseDTO) {

        return ExpenseEntity.builder()
                .id(expenseDTO.getId())
                .name(expenseDTO.getName())
                .icon(expenseDTO.getIcon())
                .amount(expenseDTO.getAmount())
                .category(category)
                .profile(profileEntity)
                .date(expenseDTO.getDate())
                .build();

    }


    @Override
    public ExpenseDTO addExpense(ExpenseDTO dto) {

        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException(("Category Not Found")));
        ExpenseEntity newExpense = toEntity(category,profile,dto);
        newExpense = expenseRepository.save(newExpense);
        return toDTO(newExpense);

    }

    @Override
    public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser() {

        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);

        return list.stream().map(this::toDTO).toList();

    }

    @Override
    public List<ExpenseDTO> getExpensesForUserOnDate(String profileId, LocalDate date) {

        List<ExpenseEntity> list =expenseRepository.findByProfileIdAndDate(profileId,date);

        return list.stream().map(this::toDTO).toList();
    }

    @Override
    public List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {

        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);

        return list.stream().map(this::toDTO).toList();

    }

    @Override
    public BigDecimal getTotalExpensesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = expenseRepository.findTotalExpenseByProfileId(profile.getId());
        return total !=null ? total:BigDecimal.ZERO;
    }

    @Override
    public List<ExpenseDTO> getLatest5ExpensesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> list = expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());

        return list.stream().map(this::toDTO).toList();

    }

    @Override
    public void deleteExpenses(String expenseId) {

        ProfileEntity profile = profileService.getCurrentProfile();
        ExpenseEntity expense = expenseRepository.findById(expenseId).orElseThrow(() -> new RuntimeException("Expense Not Found"));

        if (!expense.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("Unauthorized to delete this expense");
        }

        expenseRepository.delete(expense);
    }
}
