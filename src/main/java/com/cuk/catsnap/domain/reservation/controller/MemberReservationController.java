package com.cuk.catsnap.domain.reservation.controller;

import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import com.cuk.catsnap.domain.reservation.dto.ReservationResponse;
import com.cuk.catsnap.global.result.PagedData;
import com.cuk.catsnap.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "예약 관련 API", description = "사용자가 예약한 예약 조회, 새로운 예약하기, 특정 작가의 예약을 조회할 수 있는 API입니다.")
@RestController
@RequestMapping("/reservation")
public class MemberReservationController {

    @Operation(summary = "예약을 조회하는 API", description = "예약을 조회하는 API입니다. 쿼리 파라미터 type으로 적절한 유형의 예약을 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SR000", description = "성공적으로 예약목록을 조회했습니다."),
            @ApiResponse(responseCode = "400 SR000", description = "예약 조회 실패", content = @Content(schema = @Schema(implementation = ResultResponse.class))),
    })
    @GetMapping("/member/my")
    public ResultResponse<PagedData<ReservationResponse.MyReservationList>> getMyReservation(
            @Parameter(description="all : 내 모든 예약(정렬 : 최근 예약한 시간 느릴수록 먼저옴) upcoming : 미래에 시작하는 예약(정렬 : 미래 예약 중 현재와 가까운 것이 먼저옴) ")
            @RequestParam("type")
            String reservationQuery,
            @RequestParam
            Pageable pageable){
        return null;
    }

    @Operation(summary = "특정 월의 예약 유무를 일별로 조회", description = "특정 월의 예약 유무를 일별로 조회하는 API입니다. 예) 2024년 9월에 예약은 ? -> 2024년 9월 7일, 2024년 9월 13일")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SR000", description = "성공적으로 예약목록을 조회했습니다.")
    })
    @GetMapping("/member/my/month")
    public ResultResponse<ReservationResponse.MonthReservationCheckList> getMyMonthReservationCheck(
            @Parameter(description = "조회하고 싶은 달", example = "yyyy-MM")
            @RequestParam("month")
            @DateTimeFormat(pattern = "yyyy-MM")
            LocalDate reservationMonth
    ){
        return null;
    }

    @Operation(summary = "특정 일의 예약 목록을 조회", description = "특정 일의 예약 목록을 조회하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SR000", description = "성공적으로 예약목록을 조회했습니다.")
    })
    @GetMapping("/member/my/day")
    public ResultResponse<ReservationResponse.MemberReservationInformationList> getMyDayReservation(
            @Parameter(description = "조회하고 싶은 일", example = "yyyy-MM-dd")
            @RequestParam("day")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate reservationDay
    ){
        return null;
    }

    @Operation(summary = "특정 작가의 특정 날짜에 예약 가능한 시간 목록 조회", description = "특정 작가의 특정 날짜에 예약 가능한 시간을 조회하는 API입니다. 특정 작가에게 예약을 할 때 조회되는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SR001", description = "성공적으로 예약 가능한 시간을 조회했습니다.")
    })
    @GetMapping("/member/photographer/time")
    public ResultResponse<ReservationResponse.PhotographerAvailableReservationTimeList> getPhotographerAvailableReservationTimeList(
            @RequestParam("photographerId")
            Long photographerId,
            @Parameter(description = "예약을 하고 싶은 날", example = "yyyy-MM-dd")
            @RequestParam("date")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate date
    ){
        return null;
    }

    @Operation(summary = "특정 작가의 예약 가능한 프로그램 조회", description = "특정 작가의 예약 가능한 프로그램을 조회하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SR002", description = "성공적으로 예약 가능한 프로그램을 조회했습니다.")
    })
    @GetMapping("/photographer/program")
    public ResultResponse<ReservationResponse.PhotographerProgramList> getPhotographerProgram(
            @RequestParam("photographerId")
            Long photographerId
    ){
        return null;
    }

    @Operation(summary = "특정 작가의 예약 시 주의사항과 예약 가능한 장소 조회", description = "특정 작가의 예약 시 주의사항과 예약 가능한 장소 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SR003", description = "성공적으로 예약 주의사항과 예약 가능한 장소를 조회했습니다.")
    })
    @GetMapping("/photographer/notification")
    public ResultResponse<ReservationResponse.PhotographerReservationGuidance> getPhotographerReservationGuidance(
            @RequestParam("photographerId")
            Long photographerId
    ){
        return null;
    }

    @Operation(summary = "새로운 예약을 생성하는 API", description = "새로운 예약을 만드는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201 SR004", description = "예약이 성공적으로 완료되었습니다.")
    })
    @PostMapping("/book")
    public ResultResponse<ReservationResponse.ReservationBookResult> postBookReservation(
            @Parameter(description = "새로운 예약 형식")
            @RequestBody
            ReservationRequest.ReservationBook reservationBook
    ){
        return null;
    }
}
