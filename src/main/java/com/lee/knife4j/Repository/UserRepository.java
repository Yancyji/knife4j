package com.lee.knife4j.Repository;

import com.lee.knife4j.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends BaseRepository<User>{

    @Override
    @Query("select t.id from User t where t.name like ?1")
    Page<Long> find(String search, Pageable pageable);
}
