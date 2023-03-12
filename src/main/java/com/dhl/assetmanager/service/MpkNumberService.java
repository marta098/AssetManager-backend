package com.dhl.assetmanager.service;

import com.dhl.assetmanager.dto.response.MpkNumberDto;
import com.dhl.assetmanager.mapper.MpkNumberMapper;
import com.dhl.assetmanager.repository.MpkNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MpkNumberService {

    private final MpkNumberRepository mpkNumberRepository;

    private final MpkNumberMapper mpkNumberMapper;

    public List<MpkNumberDto> getAllMpkNumbers() {
        return mpkNumberRepository.findAll().stream()
                .map(mpkNumberMapper::mpkNumberToMpkNumberDto)
                .collect(Collectors.toList());
    }

}
