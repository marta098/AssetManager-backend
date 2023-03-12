package com.dhl.assetmanager.repository;

import com.dhl.assetmanager.entity.MpkNumber;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface MpkNumberRepository extends Repository<MpkNumber, Long> {

    Optional<MpkNumber> findById(long id);

    List<MpkNumber> findAll();

}
