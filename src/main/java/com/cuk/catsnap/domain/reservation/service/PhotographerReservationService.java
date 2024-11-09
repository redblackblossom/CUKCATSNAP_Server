package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import com.cuk.catsnap.domain.reservation.dto.MonthReservationCheckListResponse;
import com.cuk.catsnap.domain.reservation.dto.PhotographerProgramListResponse;
import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import com.cuk.catsnap.domain.reservation.dto.photographer.request.ReservationTimeFormatRequest;
import com.cuk.catsnap.domain.reservation.dto.photographer.response.PhotographerReservationInformationListResponse;
import com.cuk.catsnap.domain.reservation.dto.photographer.response.ReservationTimeFormatIdResponse;
import com.cuk.catsnap.domain.reservation.dto.photographer.response.photographerProgramIdResponse;
import com.cuk.catsnap.domain.reservation.entity.ReservationState;
import com.cuk.catsnap.domain.reservation.entity.Weekday;
import java.time.LocalDate;
import java.util.List;

public interface PhotographerReservationService {

    void createJoinedPhotographerReservationTimeFormat(Photographer photographer);

    ReservationTimeFormatIdResponse createReservationTimeFormat(
        ReservationTimeFormatRequest reservationTimeFormatRequest,
        String reservationTimeFormatId);

    List<ReservationTimeFormat> getMyReservationTimeFormatList();

    void deleteReservationTimeFormat(String reservationTimeFormatId);

    void mappingWeekdayToReservationTimeFormat(String reservationTimeFormatId, Weekday weekday);

    void unmappingWeekdayToReservationTimeFormatByWeekday(Weekday weekday);

    photographerProgramIdResponse createProgram(
        ReservationRequest.PhotographerProgram photographerProgram,
        Long programId);

    PhotographerProgramListResponse getMyProgramList();

    int softDeleteProgram(Long programId);

    MonthReservationCheckListResponse getReservationListByMonth(LocalDate month);

    PhotographerReservationInformationListResponse getReservationDetailListByDay(LocalDate day);

    void changeReservationState(Long reservationId, ReservationState reservationState);
}
