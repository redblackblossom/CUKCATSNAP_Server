package com.cuk.catsnap.domain.photographer.service;

import com.cuk.catsnap.domain.photographer.converter.PhotographerConverter;
import com.cuk.catsnap.domain.photographer.dto.PhotographerRequest;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.photographer.repository.PhotographerRepository;
import com.cuk.catsnap.domain.reservation.service.PhotographerReservationService;
import com.cuk.catsnap.global.Exception.photographer.DuplicatedPhotographerException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhotographerServiceImpl implements PhotographerService{

    private final PasswordEncoder passwordEncoder;
    private final PhotographerRepository photographerRepository;
    private final PhotographerConverter photographerConverter;

    private final PhotographerReservationService photographerReservationService;

    @Override
    public void singUp(PhotographerRequest.PhotographerSignUp photographerSignUp) {
        photographerRepository.findByIdentifier(photographerSignUp.getIdentifier())
                .ifPresent(Photographer ->{
                    throw new DuplicatedPhotographerException("이미 존재하는 아이디입니다.");
                });

        String encodedPassword = passwordEncoder.encode(photographerSignUp.getPassword());
        Photographer photographer = photographerConverter.photographerSignUpToPhotographer(photographerSignUp,encodedPassword);
        photographerRepository.save(photographer);

        // weekdayReservationTimeMapping 생성 (예약을 형식을 요일에 매핑하는 테이블 생성)
        photographerReservationService.createJoinedPhotographerReservationTimeFormat(photographer);

        // todo : 이용약관 동의 여부 확인
    }
}
