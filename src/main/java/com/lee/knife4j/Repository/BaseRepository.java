package com.lee.knife4j.Repository;

import com.lee.knife4j.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends Model> extends JpaRepository<T, Long> {

    Page<Long> find(String search, Pageable pageable);
}