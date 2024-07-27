package team5.doghae.domain.oauth.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import team5.doghae.common.controller.ControllerTestSetup;
import team5.doghae.common.controller.WithCustomUser;
import team5.doghae.common.security.jwt.JwtProvider;
import team5.doghae.domain.oauth.dto.ResponseJwtToken;
import team5.doghae.domain.oauth.service.KakaoOAuthService;
import team5.doghae.domain.oauth.service.OAuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
class AuthControllerTest extends ControllerTestSetup {

    private final String BASIC_URL = "/oauth2";

    @MockBean
    private OAuthService oAuthService;
    @MockBean
    private KakaoOAuthService kakaoOAuthService;
    @MockBean
    private JwtProvider jwtProvider;

    @Nested
    @DisplayName("kakao 메서드는")
    class KakaoLoginTest {

        @Test
        @DisplayName("정상 흐름이면 access token과 refresh token을 발급한다.")
        void return_access_token_and_refresh_token() throws Exception {
            //given
            String authCode = "authCode";
            String jwtAccessToken = "jwtAccessToken";
            String jwtRefreshToken = "jwtRefreshToken";
            given(kakaoOAuthService.login(any(String.class)))
                    .willReturn(ResponseJwtToken.of(jwtAccessToken, jwtRefreshToken));

            //when
            ResultActions result = noSecurityMockMvc.perform(get(BASIC_URL + "/kakao/login?code=" + authCode)
                    .with(SecurityMockMvcRequestPostProcessors.csrf()));

            //then
            result.andExpect(status().isOk())
                    .andExpect(header().string("Authorization", jwtAccessToken))
                    .andExpect(cookie().value("refreshToken", jwtRefreshToken));
        }
    }

    @Nested
    @DisplayName("getAccessToken 메서드는")
    class GetAccessTokenTest {

        @Test
        @DisplayName("정상 흐름이면 access token을 발급한다.")
        void return_access_token() throws Exception {
            //given
            String jwtAccessToken = "jwtAccessToken";
            given(jwtProvider.createNewAccessTokenFromRefreshToken(any(String.class)))
                    .willReturn(jwtAccessToken);

            //when
            ResultActions result = noSecurityMockMvc.perform(get(BASIC_URL + "/refresh")
                    .header("Authorization", "Bearer " + "refreshToken"));

            //then

            result.andExpect(status().isOk())
                    .andExpect(header().string("Authorization", jwtAccessToken));
            verify(jwtProvider).validRefreshToken(any(String.class));
        }
    }

    @Nested
    @WithCustomUser
    @DisplayName("deleteAccount 메서드는")
    class DeleteAccountTest {

        @Test
        @DisplayName("정상 흐름이면 계정을 삭제하고 204를 반환한다.")
        void delete_account() throws Exception {
            //given
            //when
            ResultActions result = mockMvc.perform(delete(BASIC_URL + "/users")
                    .with(SecurityMockMvcRequestPostProcessors.csrf()));

            //then
            result.andExpect(status().isNoContent());
            verify(oAuthService).deleteAccount(any(Long.class));
        }
    }
}
