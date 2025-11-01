package com.berry.controller;

import com.berry.dto.BerryGroupRequest;
import com.berry.dto.BerryGroupResponse;
import com.berry.dto.BerryRequest;
import com.berry.dto.BerryResponse;
import com.berry.service.BerryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/berries")
@RequiredArgsConstructor
public class BerryController {

    private final BerryService berryService;

    @GetMapping("/groups/{groupId}/types")
    public ResponseEntity<List<BerryResponse>> getBerriesByGroupId(
            @PathVariable @NonNull Long groupId) {
        return ResponseEntity.ok(berryService.getBerriesByGroupId(groupId));
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<BerryGroupResponse> getBerryGroupById(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(berryService.getBerryGroupById(id));
    }

    @GetMapping("/groups")
    public ResponseEntity<List<BerryGroupResponse>> getAllBerryGroups() {
        return ResponseEntity.ok(berryService.getAllBerryGroups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BerryResponse> getBerryById(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(berryService.getBerryById(id));
    }

    @PostMapping("/groups")
    public ResponseEntity<BerryGroupResponse> createBerryGroup(
            @Valid @RequestBody BerryGroupRequest request) {
        return ResponseEntity.ok(berryService.createBerryGroup(request));
    }

    @GetMapping
    public ResponseEntity<List<BerryResponse>> getAllBerries() {
        return ResponseEntity.ok(berryService.getAllBerries());
    }

    @PostMapping
    public ResponseEntity<BerryResponse> createBerry(@Valid @RequestBody BerryRequest request) {
        return ResponseEntity.ok(berryService.createBerry(request));
    }
}
