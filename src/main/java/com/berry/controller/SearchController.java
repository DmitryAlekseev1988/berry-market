package com.berry.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.berry.dto.AdvertResponse;
import com.berry.dto.SearchRequest;
import com.berry.service.AdvertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final AdvertService advertService;

    @PostMapping
    public ResponseEntity<List<AdvertResponse>> searchAdverts(
            @Valid @RequestBody SearchRequest searchRequest) {
        return ResponseEntity.ok(advertService.searchAdverts(searchRequest));
    }
}
