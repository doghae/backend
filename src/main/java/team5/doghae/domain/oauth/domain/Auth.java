package team5.doghae.domain.oauth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team5.doghae.common.entity.BaseEntity;
import team5.doghae.domain.user.domain.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Auth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String refreshToken;

    private String sub;

    private String idToken;

    @Builder
    public Auth(User user, String refreshToken, String sub, String idToken) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.sub = sub;
        this.idToken = idToken;
    }

    public static Auth create(User user, String refreshToken) {
        return Auth.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void update(String idToken, String refreshToken) {
        this.idToken = idToken;
        this.refreshToken = refreshToken;
    }
}
