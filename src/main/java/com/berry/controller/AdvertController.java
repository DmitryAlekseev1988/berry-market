package com.berry.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.berry.dto.AdvertRequest;
import com.berry.dto.AdvertResponse;
import com.berry.dto.GetAllAdvertsRequest;
import com.berry.entity.AdvertStatus;
import com.berry.service.AdvertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/adverts")
@RequiredArgsConstructor
public class AdvertController {

    private final AdvertService advertService;

    @PostMapping
    public ResponseEntity<AdvertResponse> createAdvert(@Valid @RequestBody AdvertRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(advertService.createAdvert(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertResponse> getAdvertById(@PathVariable Long id) {
        return ResponseEntity.ok(advertService.getAdvertById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<AdvertResponse>> getMyAdverts() {
        return ResponseEntity.ok(advertService.getMyAdverts());
    }

    @GetMapping("/all")
    public ResponseEntity<Page<AdvertResponse>> getAllAdverts(
            @Valid @RequestBody GetAllAdvertsRequest request) {
        return ResponseEntity
                .ok(advertService.getAllAdvertsExcludingMine(request.getPage(), request.getSize()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdvertResponse> updateAdvert(@PathVariable Long id,
            @Valid @RequestBody AdvertRequest request) {
        return ResponseEntity.ok(advertService.updateAdvert(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvert(@PathVariable Long id) {
        advertService.deleteAdvert(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AdvertResponse> updateAdvertStatus(@PathVariable Long id,
            @RequestParam AdvertStatus status) {
        return ResponseEntity.ok(advertService.updateAdvertStatus(id, status));
    }
}
