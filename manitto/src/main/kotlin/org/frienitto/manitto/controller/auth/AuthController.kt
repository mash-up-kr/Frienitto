package org.frienitto.manitto.controller.auth

import io.swagger.annotations.*
import org.frienitto.manitto.dto.*
import org.frienitto.manitto.exception.NonAuthorizationException
import org.frienitto.manitto.service.AuthService
import org.frienitto.manitto.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1"])
@Api(value = "authController", description = "마니또 가입 및 로그인 API & 인증 API")
class AuthController(private val authService: AuthService, private val userService: UserService) {

    @PostMapping(value = ["/issue/code"])
    fun issueCode(@RequestBody body: IssueCodeRequest): Response<Unit> {
        authService.sendAuthCodeToEmail(body.receiverInfo)
        return Response(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.reasonPhrase)
    }

    @PostMapping(value = ["/verify/code"])
    fun verifyCode(@RequestBody body: VerifyCodeRequest): Response<RegisterToken> {
        val registerToken = authService.verifyCode(body.code)
        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, RegisterToken(registerToken))
    }

    @ApiOperation(value = "회원가입", response = Response::class)
    @ApiResponses(value = [ApiResponse(code = 201, message = "Successfully Sign-up")])
    @PostMapping(value = ["/sign-up"])
    fun signUp(@ApiParam(value = "회원가입하기 위해서 필요한 토큰") @RequestHeader("X-Register-Token") registerToken: String,
               @ApiParam(value = "회원 가입 Body") @RequestBody body: SignUpDto): Response<UserDto> {
        if (!authService.isRegisterable(registerToken)) {
            throw NonAuthorizationException()
        }
        val userDto = userService.signUp(body)

        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, userDto)
    }

    @ApiOperation(value = "로그인",response = Response::class)
    @ApiResponses(value = [ApiResponse(code = 200, message = "Successfully Sign-in")])
    @PostMapping(value = ["/sign-in"])
    fun signIn(@ApiParam(value = "로그인 Body") @RequestBody body: SignInDto): Response<AccessToken> {
        val accessToken = userService.signIn(body)

        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, accessToken)
    }
}