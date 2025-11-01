package com.berry.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.berry.dto.AdvertRequest;
import com.berry.dto.AdvertResponse;
import com.berry.dto.BerryGroupResponse;
import com.berry.dto.BerryResponse;
import com.berry.dto.SearchRequest;
import com.berry.dto.UserResponse;
import com.berry.entity.Advert;
import com.berry.entity.AdvertStatus;
import com.berry.entity.Berry;
import com.berry.entity.BerryGroup;
import com.berry.entity.User;
import com.berry.repository.AdvertRepository;
import com.berry.repository.BerryRepository;
import com.berry.repository.UserRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdvertService {

    private final AdvertRepository advertRepository;
    private final BerryRepository berryRepository;
    private final UserRepository userRepository;

    @Transactional
    public AdvertResponse createAdvert(AdvertRequest request) {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        Long currentUserId = Long.parseLong(userIdStr);

        Berry berry = berryRepository.findById(request.getBerryId()).orElseThrow(
                () -> new com.berry.exception.ResourceNotFoundException("Ягода не найдена"));

        User user = userRepository.findById(currentUserId).orElseThrow(
                () -> new com.berry.exception.ResourceNotFoundException("Пользователь не найден"));

        Advert advert = new Advert();
        advert.setBerry(berry);
        advert.setQuantity(request.getQuantity());
        advert.setPrice(request.getPrice());
        advert.setAddress(request.getAddress());
        advert.setDescription(request.getDescription());
        advert.setType(request.getType());
        advert.setStatus(AdvertStatus.ACTIVE);
        advert.setUser(user);

        Advert savedAdvert = advertRepository.save(advert);
        return convertToResponse(savedAdvert);
    }

    public AdvertResponse getAdvertById(Long id) {
        Advert advert = advertRepository.findById(id).orElseThrow(
                () -> new com.berry.exception.ResourceNotFoundException("Объявление не найдено"));
        return convertToResponse(advert);
    }

    public List<AdvertResponse> getMyAdverts() {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        Long currentUserId = Long.parseLong(userIdStr);
        List<Advert> advertList = advertRepository.findByUserId(currentUserId);
        return advertList.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public Page<AdvertResponse> getAllAdvertsExcludingMine(int page, int size) {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        Long currentUserId = Long.parseLong(userIdStr);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Advert> advertPage = advertRepository.findByUserIdNotAndStatus(currentUserId,
                AdvertStatus.ACTIVE, pageable);
        return advertPage.map(this::convertToResponse);
    }

    @Transactional
    public AdvertResponse updateAdvert(Long id, AdvertRequest request) {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        Long currentUserId = Long.parseLong(userIdStr);

        Advert advert = advertRepository.findById(id).orElseThrow(
                () -> new com.berry.exception.ResourceNotFoundException("Объявление не найдено"));

        if (!advert.getUser().getId().equals(currentUserId)) {
            throw new com.berry.exception.UnauthorizedException(
                    "У вас нет прав для изменения этого объявления");
        }

        Berry berry = berryRepository.findById(request.getBerryId()).orElseThrow(
                () -> new com.berry.exception.ResourceNotFoundException("Ягода не найдена"));

        advert.setBerry(berry);
        advert.setQuantity(request.getQuantity());
        advert.setPrice(request.getPrice());
        advert.setAddress(request.getAddress());
        advert.setDescription(request.getDescription());
        advert.setType(request.getType());

        Advert updatedAdvert = advertRepository.save(advert);
        return convertToResponse(updatedAdvert);
    }

    @Transactional
    public void deleteAdvert(Long id) {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        Long currentUserId = Long.parseLong(userIdStr);

        Advert advert = advertRepository.findById(id).orElseThrow(
                () -> new com.berry.exception.ResourceNotFoundException("Объявление не найдено"));

        if (!advert.getUser().getId().equals(currentUserId)) {
            throw new com.berry.exception.UnauthorizedException(
                    "У вас нет прав для изменения этого объявления");
        }

        advertRepository.delete(advert);
    }

    @Transactional
    public AdvertResponse updateAdvertStatus(Long id, AdvertStatus status) {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        Long currentUserId = Long.parseLong(userIdStr);

        Advert advert = advertRepository.findById(id).orElseThrow(
                () -> new com.berry.exception.ResourceNotFoundException("Объявление не найдено"));

        if (!advert.getUser().getId().equals(currentUserId)) {
            throw new com.berry.exception.UnauthorizedException(
                    "У вас нет прав для изменения этого объявления");
        }

        advert.setStatus(status);
        Advert updatedAdvert = advertRepository.save(advert);
        return convertToResponse(updatedAdvert);
    }

    // поиск объявлений по фильтрам
    public List<AdvertResponse> searchAdverts(SearchRequest searchRequest) {
        Specification<Advert> spec = buildSearchSpecification(searchRequest);
        List<Advert> advertList = advertRepository.findAll(spec);
        return advertList.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    // создаем спецификацию для поиска
    private Specification<Advert> buildSearchSpecification(SearchRequest searchRequest) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("status"), AdvertStatus.ACTIVE));

            if (searchRequest.getBerryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("berry").get("id"),
                        searchRequest.getBerryId()));
            }

            if (searchRequest.getBerryGroupId() != null) {
                Join<Advert, Berry> berryJoin = root.join("berry");
                predicates.add(criteriaBuilder.equal(berryJoin.get("group").get("id"),
                        searchRequest.getBerryGroupId()));
            }

            if (searchRequest.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"),
                        searchRequest.getMinPrice()));
            }
            if (searchRequest.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"),
                        searchRequest.getMaxPrice()));
            }

            if (searchRequest.getAdvertType() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("type"), searchRequest.getAdvertType()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // конвертация entity в response
    private AdvertResponse convertToResponse(Advert advert) {
        AdvertResponse response = new AdvertResponse();
        response.setId(advert.getId());
        response.setBerry(convertBerryToResponse(advert.getBerry()));
        response.setQuantity(advert.getQuantity());
        response.setPrice(advert.getPrice());
        response.setAddress(advert.getAddress());
        response.setDescription(advert.getDescription());
        response.setAdvertType(advert.getType());
        response.setStatus(advert.getStatus());
        response.setUser(convertUserToResponse(advert.getUser()));
        response.setCreatedAt(advert.getCreatedAt());
        return response;
    }

    private BerryResponse convertBerryToResponse(Berry berry) {
        BerryResponse response = new BerryResponse();
        response.setId(berry.getId());
        response.setName(berry.getName());
        response.setBerryGroup(convertBerryGroupToResponse(berry.getGroup()));
        return response;
    }

    private BerryGroupResponse convertBerryGroupToResponse(BerryGroup berryGroup) {
        BerryGroupResponse response = new BerryGroupResponse();
        response.setId(berryGroup.getId());
        response.setName(berryGroup.getName());
        response.setDescription(berryGroup.getDescription());
        return response;
    }

    private UserResponse convertUserToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setUserType(user.getType());
        return response;
    }

}
