package team5.doghae.domain.oauth.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor
@PropertySource({"classpath:application-prod.yml", "classpath:application-local.yml"})
public class KakaoProperties {

    @Value("${oauth2.kakao.api-key}")
    private String apiKey;
    @Value("${oauth2.kakao.client-secret}")
    private String secretKey;
    @Value("${oauth2.kakao.token-url}")
    private String tokenUrl;
    @Value("${oauth2.kakao.user-info-url}")
    private String userInfoUrl;
    @Value("${oauth2.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${oauth2.kakao.delete-account-url}")
    private String deleteAccountUrl;
    @Value("${oauth2.kakao.logout-url}")
    private String logoutUrl;
    @Value("${oauth2.kakao.admin-key}")
    private String adminKey;

}
