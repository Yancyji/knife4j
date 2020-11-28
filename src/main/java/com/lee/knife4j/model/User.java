package com.lee.knife4j.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
@Where(clause = "del <> 1")
public class User extends Model{

    private String name;

    private String password;
}
