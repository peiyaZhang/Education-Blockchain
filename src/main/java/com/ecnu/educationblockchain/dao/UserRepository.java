package com.ecnu.educationblockchain.dao;

import com.ecnu.educationblockchain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User,String> {
    @Override
    List<User> findAll();

}
