package com.dhl.assetmanager.repository;

import com.dhl.assetmanager.entity.Role;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface RoleRepository extends Repository<Role, Long> {

    Optional<Role> findByName(String name);

}