package team5.doghae.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team5.doghae.common.resolver.AuthUser;
import team5.doghae.common.response.SuccessResponse;
import team5.doghae.common.security.jwt.JwtTokenInfo;
import team5.doghae.domain.user.dto.UserRequest;
import team5.doghae.domain.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/nickname")
    public ResponseEntity<SuccessResponse<String>> getNickname(
            @AuthUser JwtTokenInfo jwtTokenInfo
    ) {
        return SuccessResponse.of(
                userService.getNickname(jwtTokenInfo.getUserId())
        ).setStatus(HttpStatus.OK);
    }

    @PostMapping("/nickname")
    public ResponseEntity<Void> changeNickname(
            @AuthUser JwtTokenInfo jwtTokenInfo,
            @RequestBody UserRequest.Nickname userRequest
    ) {
        userService.changeNickname(jwtTokenInfo.getUserId(), userRequest.getNickname());
        return ResponseEntity.noContent().build();
    }
}
