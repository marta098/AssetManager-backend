package com.dhl.assetmanager.repository;

import com.dhl.assetmanager.entity.Location;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends Repository<Location, Long> {

    List<Location> findAll();

    Optional<Location> findById(long id);

}
