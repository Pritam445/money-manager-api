package com.money.manage.repository;

import com.money.manage.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity,String> {

    List<CategoryEntity> findByProfileId(String profileId);

    Optional<CategoryEntity> findByIdAndProfileId(String id, String profileId);

    List<CategoryEntity> findByTypeAndProfileId(String type,String profileId);

    Boolean existsByNameAndProfileId(String name,String profileId);

}
