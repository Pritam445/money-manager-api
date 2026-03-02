package com.money.manage.service.ServiceImpl;

import com.money.manage.dto.ExpenseDTO;
import com.money.manage.dto.IncomeDTO;
import com.money.manage.entity.CategoryEntity;
import com.money.manage.entity.ExpenseEntity;
import com.money.manage.entity.IncomeEntity;
import com.money.manage.entity.ProfileEntity;
import com.money.manage.repository.CategoryRepository;

import com.money.manage.repository.IncomeRepository;

import com.money.manage.service.IncomeService;
import com.money.manage.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

    private final CategoryRepository categoryRepository;
    private  final IncomeRepository incomeRepository ;
    private final ProfileService profileService;

    private IncomeDTO toDTO(IncomeEntity entity){

        return IncomeDTO.builder()
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

    private IncomeEntity toEntity(CategoryEntity category, ProfileEntity profileEntity, IncomeDTO expenseDTO) {

        return IncomeEntity.builder()
                .name(expenseDTO.getName())
                .icon(expenseDTO.getIcon())
                .amount(expenseDTO.getAmount())
                .category(category)
                .profile(profileEntity)
                .date(expenseDTO.getDate())
                .build();

    }


    @Override
    public IncomeDTO addExpense(IncomeDTO dto) {

        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException(("Category Not Found")));
        IncomeEntity newIncome = toEntity(category,profile,dto);
        newIncome = incomeRepository.save(newIncome);
        return toDTO(newIncome);
    }

    @Override
    public List<IncomeDTO> getCurrentMonthExpensesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);

        return list.stream().map(this::toDTO).toList();
    }

    @Override
    public List<IncomeDTO> getLatest5IncomesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());

        return list.stream().map(this::toDTO).toList();

    }

    @Override
    public List<IncomeDTO> getIncomeForUserOnDate(String profileId, LocalDate date) {
        List<IncomeEntity> list =incomeRepository.findByProfileIdAndDate(profileId,date);

        return list.stream().map(this::toDTO).toList();
    }

    @Override
    public List<IncomeDTO> filterIncome(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {

        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);

        return list.stream().map(this::toDTO).toList();
    }

    @Override
    public BigDecimal getTotalIncomeForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = incomeRepository.findTotalExpenseByProfileId(profile.getId());
        return total !=null ? total:BigDecimal.ZERO;
    }

    @Override
    public void deleteIncome(String incomeId) {
        ProfileEntity profile = profileService.getCurrentProfile();
        IncomeEntity income = incomeRepository.findById(incomeId).orElseThrow(() -> new RuntimeException("Income Not Found"));

        if (!income.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("Unauthorized to delete this expense");
        }

        incomeRepository.delete(income);
    }


}
