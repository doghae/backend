package team5.doghae.domain.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import team5.doghae.common.exception.BusinessException;
import team5.doghae.common.exception.ErrorCode;
import team5.doghae.common.security.jwt.JwtProvider;
import team5.doghae.domain.oauth.domain.Auth;
import team5.doghae.domain.oauth.dto.KakaoOAuthUserProfile;
import team5.doghae.domain.oauth.dto.OAuthAccessToken;
import team5.doghae.domain.oauth.dto.ResponseJwtToken;
import team5.doghae.domain.oauth.exception.OAuthNotFoundException;
import team5.doghae.domain.oauth.properties.KakaoProperties;
import team5.doghae.domain.oauth.repository.AuthRepository;
import team5.doghae.domain.user.domain.SocialCode;
import team5.doghae.domain.user.domain.User;
import team5.doghae.domain.user.service.UserService;
import team5.doghae.domain.user.service.ValidateUserService;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoOAuthService {

    private final KakaoProperties properties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final ValidateUserService validateUserService;
    private final AuthRepository authRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public ResponseJwtToken login(String code) {
        OAuthAccessToken accessToken = getAccessToken(code);
        KakaoOAuthUserProfile oAuthUserProfile = getUserProfile(accessToken.getAccessToken());
        System.out.println("oAuthUserProfile.getEmail() = " + oAuthUserProfile.getKakaoAccount().getEmail());
        User user = validateUserService.validateRegisteredUserByEmail(
                oAuthUserProfile.getKakaoAccount().getEmail(), SocialCode.KAKAO);

        if (user == null) {
            user = userService.registerWithOAuth(
                    oAuthUserProfile.getKakaoAccount().getEmail(), SocialCode.KAKAO, accessToken.getRefreshToken());
        }

        String jwtProviderAccessToken = jwtProvider.createAccessToken(user.getId(), user.getUserRole());
        String jwtProviderRefreshToken = jwtProvider.createRefreshToken(user.getId(), user.getUserRole());

        return ResponseJwtToken.of(jwtProviderAccessToken, jwtProviderRefreshToken);
    }

    @Transactional
    public void deleteAccount(User user) {
        Auth auth = authRepository.findByUser(user)
                .orElseThrow(() -> new OAuthNotFoundException());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", auth.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        String url = properties.getDeleteAccountUrl();
        String response = restTemplate.postForObject(url, request, String.class);

        if (response.contains("error")) throw new BusinessException(ErrorCode.OAUTH_SERVER_FAILED);

        user.deleteUser();
    }

    public OAuthAccessToken getAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.getApiKey());
        body.add("redirect_uri", properties.getRedirectUri());
        body.add("code", code);
        body.add("client_secret", properties.getSecretKey());

        HttpEntity<MultiValueMap<String, String>> requestToken = new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(properties.getTokenUrl(), HttpMethod.POST, requestToken, String.class);
        try {
            return objectMapper.readValue(response.getBody(), OAuthAccessToken.class);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.PARSING_ERROR, e);
        }
    }

    public KakaoOAuthUserProfile getUserProfile(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\"]");


        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> userInfoResponse = restTemplate.exchange(properties.getUserInfoUrl(), HttpMethod.GET,
                httpEntity, String.class);
        System.out.println("userInfoResponse.getBody() = " + userInfoResponse.getBody());
        try {
            return objectMapper.readValue(userInfoResponse.getBody(), KakaoOAuthUserProfile.class);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.PARSING_ERROR, e);
        }
    }
}