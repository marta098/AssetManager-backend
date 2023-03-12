package com.dhl.assetmanager.repository;

import com.dhl.assetmanager.entity.Role;
import com.dhl.assetmanager.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends Repository<User, Long> {

    @Query("select u from User u where u.isDeleted = false")
    List<User> findAll();

    @Query("select u from User u where u.isDeleted = false order by u.role")
    List<User> findByOrderByRoleAsc();

    @Query("select u from User u where u.role = ?1 and u.isDeleted = false")
    List<User> findAllByRole(Role role);

    void save(User user);

    @Query("select u from User u where u.username = ?1 and u.isDeleted = false ")
    Optional<User> findByUsername(String username);

    @Query("select u from User u where u.username = ?1 and u.isDeleted = false ")
    User findByUsernameLdap(String username);

    @Query("select u from User u where u.id = ?1 and u.isDeleted = false")
    Optional<User> findById(long id);

    @Query("select u from User u where u.email = ?1 and u.isDeleted = false")
    User findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}