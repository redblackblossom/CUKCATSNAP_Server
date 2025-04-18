package net.catsnap.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.dto.member.request.MemberSignUpRequest;
import net.catsnap.domain.auth.interceptor.AnyUser;
import net.catsnap.domain.auth.service.MemberAuthService;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.result.code.MemberResultCode;
import net.catsnap.global.security.dto.AccessTokenResponse;
import net.catsnap.global.security.dto.SecurityRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원(모델)의 로그인 관련 API", description = "회원(모델)의 회원가입, 로그인 관련 API입니다")
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class MemberAuthController {

    private final MemberAuthService memberAuthService;


    @Operation(summary = "회원가입 API(구현 완료)", description = "회원가입을 할 수 있는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201 SM000", description = "성공적으로 회원가입을 했습니다."),
        @ApiResponse(responseCode = "409 EM000", description = "중복된 ID로 회원가입이 불가능 합니다.")
    })
    @PostMapping("/member/signup/catsnap")
    @AnyUser
    public ResponseEntity<ResultResponse<MemberResultCode>> signUp(
        @Parameter(description = "회원가입 양식", required = true)
        @RequestBody
        MemberSignUpRequest memberSignUpRequest
    ) {
        memberAuthService.singUp(memberSignUpRequest);
        return ResultResponse.of(MemberResultCode.MEMBER_SIGN_UP);
    }

    /*
     *로그인 처리는 Spring Security의 필터로 처리하므로 해당 메서드는 필요하지 않습니다.
     *해당 컨트롤러는 API 명세만을 위한 것입니다.
     */
    @Operation(summary = "자체 서비스 API(구현 완료)",
        description = """
            자체 서비스 로그인 API입니다. (네이버, 카카오 등 OAuth 로그인 아님)
            로그인 성공 시 리프레시 토큰을 쿠키에 담아서 반환합니다.
            """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200 SY000", description = "로그인 성공"),
        @ApiResponse(responseCode = "401 EY000", description = "로그인 실패 (아이디 또는 비밀번호가 일치하지 않음)"),
        @ApiResponse(responseCode = "400 EY001", description = "로그인 실패 (잘못된 로그인 API 요청 형식)"),
    })
    @PostMapping("/member/signin/catsnap")
    public ResponseEntity<ResultResponse<AccessTokenResponse>> signIn(
        @Parameter(description = "로그인 양식", required = true)
        @RequestBody
        SecurityRequest.CatsnapSignInRequest memberSignIn
    ) {
        return null;
    }

    @Operation(summary = "네이버 소셜로그인 API(구현 완료)",
        description = """
            네이버 소셜로그인으로 회원가입과 로그인을 할 수 있는 API입니다.
            로그인 성공 시 리프레시 토큰을 쿠키에 담아서 반환합니다.
            """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201 SM000", description = "성공적으로 회원가입(로그인)을 했습니다."),
    })
    @PostMapping("/oauth2/authorization/naver")
    public ResponseEntity<ResultResponse<AccessTokenResponse>> oAuthSignUp(
    ) {
        return null;
    }
}
