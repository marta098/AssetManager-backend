package com.dhl.assetmanager.repository;

import com.dhl.assetmanager.entity.Asset;
import com.dhl.assetmanager.entity.AssetStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends Repository<Asset, Long> {

    List<Asset> findAll();

    void save(Asset asset);

    Optional<Asset> findById(long id);

    List<Asset> findAllByCurrentUser_Id(long userId);

    Optional<Asset> findBySerialNumber(String serialNumber);

    int countAllByStatus(AssetStatus status);

    @Query("SELECT count(a) from Asset a where a.deprecation <= CURRENT_TIMESTAMP")
    int countDeprecatedAssets();

    int count();

    boolean existsBySerialNumber(String serialNumber);

}