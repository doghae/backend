package team5.doghae.domain.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import team5.doghae.common.exception.BusinessException;
import team5.doghae.common.security.jwt.JwtProvider;
import team5.doghae.common.testdata.TestAuth;
import team5.doghae.domain.oauth.dto.*;
import team5.doghae.domain.oauth.properties.KakaoProperties;
import team5.doghae.domain.oauth.repository.AuthRepository;
import team5.doghae.domain.user.domain.SocialCode;
import team5.doghae.domain.user.domain.User;
import team5.doghae.domain.user.service.UserService;
import team5.doghae.domain.user.service.ValidateUserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static team5.doghae.common.testdata.TestUser.*;
import static team5.doghae.common.testdata.TestUser.createUserWithId;

@ExtendWith(MockitoExtension.class)
class KakaoOAuthServiceTest {

    @Mock
    private KakaoProperties kakaoProperties;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserService userService;
    @Mock
    private ValidateUserService validateUserService;
    @Mock
    private AuthRepository authRepository;
    @Mock
    private JwtProvider jwtTokenProvider;
    @InjectMocks
    private KakaoOAuthService kakaoOauthService;

    @Nested
    @DisplayName("login 메소드 테스트")
    class Login_test {

        @Nested
        @DisplayName("REST 요청으로 받은")
        class Rest_request {

            @BeforeEach
            void setUp() {
                given(kakaoProperties.getApiKey()).willReturn("apiKey");
                given(kakaoProperties.getSecretKey()).willReturn("clientSecret");
                given(kakaoProperties.getRedirectUri()).willReturn("redirectUri");
                given(kakaoProperties.getTokenUrl()).willReturn("tokenUrl");
            }

            @Test
            @DisplayName("유저 프로필을 파싱하는데 실패할 경우 오류를 반환한다.")
            void user_profile_parsing_fail_then_return_error() throws Exception {
                // given
                given(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                        .willReturn(ResponseEntity.of(Optional.of("json body")));
                given(objectMapper.readValue(any(String.class), eq(OAuthAccessToken.class)))
                        .willThrow(JsonProcessingException.class);
                // when
                // then
                assertThatThrownBy(() -> kakaoOauthService.login("test"))
                        .isInstanceOf(BusinessException.class);
            }
        }

        @Nested
        @DisplayName("유저 이메일을 알아낸 뒤")
        class After_find_user_email {

            @BeforeEach
            void setUp() throws Exception {
                given(kakaoProperties.getUserInfoUrl())
                        .willReturn("userInfoUrl");
                given(kakaoProperties.getTokenUrl())
                        .willReturn("tokenUrl");
                given(objectMapper.readValue(any(String.class), any(Class.class)))
                        .willReturn(new OAuthAccessToken("token", "token", "Bearer", 360000, 360000, "scope"));
                given(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                        .willReturn(ResponseEntity.of(Optional.of("json body")));
                given(objectMapper.readValue(any(String.class), eq(OAuthUserProfile.class)))
                        .willReturn(new OAuthUserProfile("email"));
            }

            @Test
            @DisplayName("유저가 존재하지 않을 경우 회원가입을 진행하고 토큰을 반환한다.")
            void user_not_exist_then_register_and_return_token() {
                // given
                User newUser = createUserWithId(1L);
                String accessToken = "accessToken";
                String refreshToken = "refreshToken";

                given(validateUserService.validateRegisteredUserByEmail(any(String.class), eq(SocialCode.KAKAO)))
                        .willReturn(null);
                given(userService.registerWithOAuth(any(String.class), eq(SocialCode.KAKAO), any(String.class)))
                        .willReturn(newUser);
                given(jwtTokenProvider.createAccessToken(eq(newUser.getId()), eq(newUser.getUserRole())))
                        .willReturn(accessToken);
                given(jwtTokenProvider.createRefreshToken(eq(newUser.getId()), eq(newUser.getUserRole())))
                        .willReturn(refreshToken);

                // when
                ResponseJwtToken responseJwtToken = kakaoOauthService.login("test");

                // then
                verify(userService).registerWithOAuth(any(String.class), eq(SocialCode.KAKAO), any(String.class));
                assertThat(responseJwtToken.getRefreshToken()).isEqualTo(refreshToken);
                assertThat(responseJwtToken.getAccessToken()).isEqualTo(accessToken);
            }

            @Test
            @DisplayName("유저가 존재할 경우 회원가입을 진행하지 않고 토큰을 반환한다.")
            void user_exist_then_no_register() {
                // given
                User user = createUserWithId(1L);
                String accessToken = "accessToken";
                String refreshToken = "refreshToken";

                given(validateUserService.validateRegisteredUserByEmail(any(String.class), eq(SocialCode.KAKAO)))
                        .willReturn(user);
                given(jwtTokenProvider.createAccessToken(eq(user.getId()), eq(user.getUserRole())))
                        .willReturn(accessToken);
                given(jwtTokenProvider.createRefreshToken(eq(user.getId()), eq(user.getUserRole())))
                        .willReturn(refreshToken);

                // when
                ResponseJwtToken responseJwtToken = kakaoOauthService.login("test");

                // then
                verify(userService, never()).registerWithOAuth(any(String.class), eq(SocialCode.KAKAO), any(String.class));
                assertThat(responseJwtToken.getAccessToken()).isEqualTo(accessToken);
                assertThat(responseJwtToken.getRefreshToken()).isEqualTo(refreshToken);
            }
        }
    }

    @Nested
    @DisplayName("deleteAccount 메서드 테스트")
    class DeleteAccount_Test {

        @Test
        @DisplayName("유저 아이디에 해당하는 Auth가 없을 경우 회원탈퇴를 진행하지 않는다.")
        void no_auth_then_no_delete() {
            //given
            User user = createUser();
            given(authRepository.findByUser(eq(user)))
                    .willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> kakaoOauthService.deleteAccount(user))
                    .isInstanceOf(BusinessException.class);
            verify(restTemplate, never()).postForObject(any(String.class), any(HttpEntity.class),
                    any(Class.class));
            assertThat(user.getDeletedAt()).isNull();
        }


        @Nested
        @DisplayName("유저 아이디에 해당하는 Auth가 있을 경우")
        class Exist_auth {

            @Test
            @DisplayName("회원탈퇴를 진행한다.")
            void exist_auth_then_delete_user() {
                //given
                User user = createUser();
                given(authRepository.findByUser(eq(user)))
                        .willReturn(Optional.of(TestAuth.createAuth(user)));
                given(kakaoProperties.getDeleteAccountUrl())
                        .willReturn("deleteAccountUrl");
                given(restTemplate.postForObject(any(String.class), any(HttpEntity.class),
                        eq(String.class))).willReturn("");

                //when
                kakaoOauthService.deleteAccount(user);

                //then
                assertThat(user.getDeletedAt()).isNotNull();
            }

            @Test
            @DisplayName("회원탈퇴 REST 요청이 실패하면 예외를 던진다.")
            void rest_request_fail_then_throw_exception() {
                //given
                User user = createUser();
                given(authRepository.findByUser(eq(user))).willReturn(Optional.of(
                        TestAuth.createAuth(user)));
                given(kakaoProperties.getDeleteAccountUrl())
                        .willReturn("deleteAccountUrl");
                given(restTemplate.postForObject(any(String.class), any(HttpEntity.class),
                        eq(String.class)))
                        .willReturn("{\"error\":\"invalid_token\",\"error_description\":\"Invalid Value\"}");

                //when
                //then
                assertThatThrownBy(() -> kakaoOauthService.deleteAccount(user))
                        .isInstanceOf(BusinessException.class);
                assertThat(user.getDeletedAt()).isNull();
            }
        }
    }
}


