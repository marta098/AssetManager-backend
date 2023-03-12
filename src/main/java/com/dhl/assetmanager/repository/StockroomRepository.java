package com.dhl.assetmanager.repository;


import com.dhl.assetmanager.entity.Stockroom;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface StockroomRepository extends Repository<Stockroom, Long> {

    List<Stockroom> findAll();

    Optional<Stockroom> findById(long id);

}
