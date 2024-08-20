package com.example.Repository;

import com.example.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findByMobile(String mobile); // mobile - username


}
