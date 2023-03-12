package com.dhl.assetmanager.repository;

import com.dhl.assetmanager.entity.ChangeHistory;
import org.springframework.data.repository.Repository;

public interface ChangeHistoryRepository extends Repository<ChangeHistory, Long> {

    ChangeHistory save(ChangeHistory order);

}