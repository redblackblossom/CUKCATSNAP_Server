package com.cuk.catsnap.domain.photographer.service;

import com.cuk.catsnap.domain.photographer.converter.PhotographerConverter;
import com.cuk.catsnap.domain.photographer.document.PhotographerSetting;
import com.cuk.catsnap.domain.photographer.dto.PhotographerRequest;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.photographer.repository.PhotographerRepository;
import com.cuk.catsnap.domain.photographer.repository.PhotographerSettingRepository;
import com.cuk.catsnap.domain.reservation.service.PhotographerReservationService;
import com.cuk.catsnap.global.Exception.photographer.DuplicatedPhotographerException;
import com.cuk.catsnap.global.security.contextholder.GetAuthenticationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PhotographerServiceImpl implements PhotographerService{

    private final PasswordEncoder passwordEncoder;
    private final PhotographerRepository photographerRepository;
    private final PhotographerSettingRepository photographerSettingRepository;
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


        /*
        * 작가 회원가입 시 초기화 작업
        */
        // weekdayReservationTimeMapping 생성 (예약을 형식을 요일에 매핑하는 테이블 생성)
        photographerReservationService.createJoinedPhotographerReservationTimeFormat(photographer);
        // photographerSetting 초기화
        initializePhotographerSetting(photographer.getId());
        // todo : 이용약관 동의 여부 확인
    }

    @Override
    public void initializePhotographerSetting(Long photographerId) {
        PhotographerSetting photographerSetting = PhotographerSetting.builder()
                .photographerId(photographerId)
                .autoReservationAccept(false)
                .enableOverBooking(false)
                .preReservationDays(14L)
                .announcement("")
                .build();
        photographerSettingRepository.save(photographerSetting);
    }

    @Override
    public PhotographerSetting getPhotographerSetting() {
        Long photographerId = GetAuthenticationInfo.getUserId();
        return photographerSettingRepository.findByPhotographerId(photographerId);
    }

    @Override
    public void updatePhotographerSetting(PhotographerRequest.PhotographerSetting photographerSetting) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        PhotographerSetting photographerSettingDocument = PhotographerSetting.builder()
                .photographerId(photographerId)
                .autoReservationAccept(photographerSetting.getAutoReservationAccept())
                .enableOverBooking(photographerSetting.getEnableOverBooking())
                .preReservationDays(photographerSetting.getPreReservationDays())
                .announcement(photographerSetting.getAnnouncement())
                .build();
        photographerSettingRepository.updatePhotographerSetting(photographerSettingDocument);
    }
}
