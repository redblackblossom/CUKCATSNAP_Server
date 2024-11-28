package net.catsnap.domain.reservation.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.photographer.document.PhotographerSetting;
import net.catsnap.domain.reservation.document.ReservationTimeFormat;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.entity.Weekday;
import net.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import net.catsnap.domain.reservation.repository.ReservationRepository;
import net.catsnap.domain.reservation.repository.ReservationTimeFormatRepository;
import net.catsnap.domain.reservation.repository.WeekdayReservationTimeMappingRepository;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationValidatorService {

    private final WeekdayReservationTimeMappingRepository weekdayReservationTimeMappingRepository;
    private final ReservationTimeFormatRepository reservationTimeFormatRepository;
    private final WeekdayService weekdayService;
    private final ReservationRepository reservationRepository;

    /*
     * 해당 작가가 해당 일에 예약을 받을 수 있게 했는지 확인하는 메소드 입니다.
     */
    public boolean isWithinAllowedDays(LocalDateTime reservationDateTime,
        PhotographerSetting photographerSetting) {
        Long preReservationDays = photographerSetting.getPreReservationDays();
        LocalDate reservationDate = reservationDateTime.toLocalDate();
        return !reservationDate.isAfter(LocalDate.now().plusDays(preReservationDays));
    }

    public boolean isAfterNow(LocalDateTime reservationDateTime) {
        return reservationDateTime.isAfter(LocalDateTime.now());
    }

    /*
     * 해당 일에 사용자가 원하는 예약 시작 시간이 작가의 예약 시간 테이블에 존재하는지 확인하는 메소드 입니다.
     * wantToReservationTime는 HH:mm형식으로 들어옵니다.
     */
    @Transactional(readOnly = true)
    public boolean isValidStartTimeInTimeFormat(LocalDateTime reservationDateTime,
        Long photographerId) {
        Weekday weekday = weekdayService.getWeekday(reservationDateTime.toLocalDate());
        WeekdayReservationTimeMapping weekdayReservationTimeMapping = weekdayReservationTimeMappingRepository.findByPhotographerIdAndWeekday(
                photographerId, weekday)
            .orElseThrow(
                () -> new ResourceNotFoundException("해당 작가의 해당 요일에 예약 시간 설정이 존재하지 않습니다."));
        String reservationTimeFormatId = weekdayReservationTimeMapping.getReservationTimeFormatId();
        /*
         * 해당 작가의 해당 요일에 예약 시간 설정이 존재하지 않으면 예외 발생
         */
        if (reservationTimeFormatId == null) {
            return false;
        }

        ReservationTimeFormat reservationTimeFormat = reservationTimeFormatRepository.findById(
            reservationTimeFormatId);
        List<LocalTime> photographerStartTimeList = reservationTimeFormat.getStartTimeList();

        return photographerStartTimeList.contains(reservationDateTime.toLocalTime());
    }

    /*
     * 해당 일에 해당 작가의 예약을 조회하고, 시간이 겹치는지 확인하는 메소드 입니다.
     */
    @Transactional(readOnly = true)
    public boolean isNotOverBooking(LocalDateTime reservationDateTime, Long photographerId,
        Long programDurationMinutes) {
        final int dayOfBothSide = 1;//만약, 예약 시간이 00시 이면, 전날도 고려해야 하므로 하루 전 예약 또한 고려 해야 한다.
        LocalDateTime previousReservationDay = reservationDateTime.minusDays(dayOfBothSide);
        LocalDateTime nextReservationDay = reservationDateTime.plusDays(dayOfBothSide);

        List<Reservation> reservationRepositoryList = reservationRepository.findAllByPhotographerIdAndStartTimeBetweenOrderByStartTimeAsc(
            photographerId, previousReservationDay, nextReservationDay);

        //reservationRepositoryList에서 종료 시간이 startTime보다 작은 값 중 가장 큰 값을 찾아야 한다.
        //reservationRepositoryList에서 시작 시간이 endTime보다 큰 값 중 가장 작은 값을 찾아야 한다.
        Reservation lastEndingBeforeStart = Reservation.builder().startTime(LocalDateTime.MIN)
            .endTime(LocalDateTime.MIN).build();
        Reservation firstStartingAfterEnd = Reservation.builder().startTime(LocalDateTime.MAX)
            .endTime(LocalDateTime.MAX).build();

        //처음과 마직막에 더미 데이터를 넣어준다.
        reservationRepositoryList.add(0,
            Reservation.builder().startTime(LocalDateTime.MIN).endTime(LocalDateTime.MIN)
                .build()); //불편...
        reservationRepositoryList.add(
            Reservation.builder().startTime(LocalDateTime.MAX).endTime(LocalDateTime.MAX).build());

        //겹치는 시간 자체가 없어야 한다.
        LocalDateTime wantToReservationDateTimeEnd = reservationDateTime.plusMinutes(
            programDurationMinutes);
        for (Reservation reservation : reservationRepositoryList) {
            if (reservation.getStartTime().isAfter(reservationDateTime)
                && reservation.getEndTime().isBefore(wantToReservationDateTimeEnd)) {
                return false;
            }
            if (reservation.getStartTime().isEqual(reservationDateTime)) {
                return false;
            }
        }

        //reservationRepositoryList에서 종료 시간이 startTime보다 작은 값 중 가장 큰 값을 찾아야 한다.
        //reservationRepositoryList에서 시작 시간이 endTime보다 큰 값 중 가장 작은 값을 찾아야 한다.
        for (Reservation reservation : reservationRepositoryList) {
            if (reservation.getEndTime().isBefore(previousReservationDay)) {
                if (lastEndingBeforeStart.getEndTime().isBefore(reservation.getEndTime())) {
                    lastEndingBeforeStart = reservation;
                }
            }
            if (reservation.getStartTime().isAfter(nextReservationDay)
                && !firstStartingAfterEnd.getStartTime().isEqual(LocalDateTime.MAX)) {
                firstStartingAfterEnd = reservation;
            }
        }

        Duration duration = Duration.between(lastEndingBeforeStart.getEndTime(),
            firstStartingAfterEnd.getStartTime());
        return duration.toMinutes() >= programDurationMinutes;
    }
}
