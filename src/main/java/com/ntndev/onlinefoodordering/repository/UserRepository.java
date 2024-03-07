package com.ntndev.onlinefoodordering.repository;

import com.ntndev.onlinefoodordering.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   public User findByEmail(String username);

}
