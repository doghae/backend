package team5.doghae.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import team5.doghae.domain.auth.dto.ResponseJwtToken;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> {

    private final boolean status = true;

    private T data;

    public static <T> SuccessResponse<T> of(T data) {
        SuccessResponse<T> successResponse = new SuccessResponse<>();
        successResponse.data = data;

        return successResponse;
    }

    public ResponseEntity<SuccessResponse<T>> setStatus(HttpStatus httpStatus) {
        return ResponseEntity
                .status(httpStatus)
                .body(this);
    }

    public ResponseEntity<SuccessResponse<T>> setAccessToken(String tokens) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Authorization", tokens)
                .body(this);
    }


    public ResponseEntity<SuccessResponse<T>> setRefreshToken(ResponseJwtToken tokens) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Authorization", tokens.getAccessToken())
                .header("Set-Cookie", "refreshToken=" + tokens.getRefreshToken() + ";" +
                        " Path=/; HttpOnly; SameSite=Strict; Secure;")
                .body(this);
    }

}
