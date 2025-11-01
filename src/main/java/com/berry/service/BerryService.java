package com.berry.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.berry.dto.BerryGroupRequest;
import com.berry.dto.BerryGroupResponse;
import com.berry.dto.BerryRequest;
import com.berry.dto.BerryResponse;
import com.berry.entity.Berry;
import com.berry.entity.BerryGroup;
import com.berry.exception.BadRequestException;
import com.berry.exception.ResourceNotFoundException;
import com.berry.repository.BerryGroupRepository;
import com.berry.repository.BerryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BerryService {

    private final BerryGroupRepository berryGroupRepository;
    private final BerryRepository berryRepository;

    public List<BerryGroupResponse> getAllBerryGroups() {
        return berryGroupRepository.findAll().stream().map(this::mapBerryGroupToResponse)
                .collect(Collectors.toList());
    }

    public BerryGroupResponse getBerryGroupById(Long id) {
        BerryGroup berryGroup = berryGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Группа ягод не найдена"));
        return mapBerryGroupToResponse(berryGroup);
    }

    public List<BerryResponse> getAllBerries() {
        return berryRepository.findAll().stream().map(this::mapBerryToResponse)
                .collect(Collectors.toList());
    }

    public List<BerryResponse> getBerriesByGroupId(Long groupId) {
        return berryRepository.findByGroupId(groupId).stream().map(this::mapBerryToResponse)
                .collect(Collectors.toList());
    }

    public BerryResponse getBerryById(Long id) {
        Berry berry = berryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ягода не найдена"));
        return mapBerryToResponse(berry);
    }

    @Transactional
    public BerryGroupResponse createBerryGroup(BerryGroupRequest request) {
        if (berryGroupRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException(
                    "Группа ягод с названием '" + request.getName() + "' уже существует");
        }

        BerryGroup berryGroup = new BerryGroup();
        berryGroup.setName(request.getName());
        berryGroup.setDescription(request.getDescription());

        berryGroupRepository.save(berryGroup);
        return mapBerryGroupToResponse(berryGroup);
    }

    @Transactional
    public BerryResponse createBerry(BerryRequest request) {
        BerryGroup berryGroup = berryGroupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Группа ягод не найдена"));

        if (berryRepository.existsByName(request.getName())) {
            throw new BadRequestException(
                    "Ягода с названием '" + request.getName() + "' уже существует");
        }

        Berry berry = new Berry();
        berry.setName(request.getName());
        berry.setGroup(berryGroup);

        berryRepository.save(berry);
        return mapBerryToResponse(berry);
    }

    private BerryGroupResponse mapBerryGroupToResponse(BerryGroup berryGroup) {
        BerryGroupResponse response = new BerryGroupResponse();
        response.setId(berryGroup.getId());
        response.setName(berryGroup.getName());
        response.setDescription(berryGroup.getDescription());
        return response;
    }

    private BerryResponse mapBerryToResponse(Berry berry) {
        BerryResponse response = new BerryResponse();
        response.setId(berry.getId());
        response.setName(berry.getName());
        response.setBerryGroup(mapBerryGroupToResponse(berry.getGroup()));
        return response;
    }
}
