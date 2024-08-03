package team5.doghae.domain.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import team5.doghae.common.entity.BaseEntityWithUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("deleted_at is null")
@Table(name = "user")
public class User extends BaseEntityWithUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_name")
    private String nickName;

    @Enumerated(EnumType.STRING)
    private SocialCode socialCode;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String profileImageUrl;

    private LocalDateTime deletedAt;

    public User(String email, String nickName, SocialCode socialCode) {
        this.email = email;
        this.nickName = nickName;
        this.socialCode = socialCode;
        this.userRole = UserRole.USER;
        this.profileImageUrl = null;
    }

    public static User create(String email, SocialCode socialCode) {
        return User.builder()
                .email(email)
                .socialCode(socialCode)
                .build();
    }

    public void changeProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void deleteUser() {
        this.deletedAt = LocalDateTime.now();
    }

}
