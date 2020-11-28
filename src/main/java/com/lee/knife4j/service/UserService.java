package com.lee.knife4j.service;

import com.lee.knife4j.Repository.BaseRepository;
import com.lee.knife4j.Repository.UserRepository;
import com.lee.knife4j.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService<User>{

    @Autowired
    @Qualifier("userRepository")
    private UserRepository repository;

    @Override
    protected BaseRepository<User> getRepository() {
        return repository;
    }
}
