package org.frienitto.manitto.controller

import io.swagger.annotations.*
import org.frienitto.manitto.dto.IssueCodeRequest
import org.frienitto.manitto.dto.Response
import org.frienitto.manitto.dto.VerifyCodeRequest
import org.frienitto.manitto.dto.RegisterToken
import org.frienitto.manitto.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(value = ["/api/v1"])
@Api(value = "이메일 인증 Controller", description = "이메일 인증 API")
class AuthController(private val authService: AuthService) {

    @ApiOperation(value = "인증 코드 발송 API")
    @ApiResponses(value = [ApiResponse(code = 202, message = "Successfully send Authorization-Code")])
    @PostMapping(value = ["/issue/code"])
    fun issueCode(
            @RequestBody body: IssueCodeRequest): Response<Unit> {
        authService.sendAuthCodeToEmail(body.receiverInfo)
        return Response(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.reasonPhrase)
    }

    @ApiOperation(value = "인증 코드 확인 API")
    @ApiResponses(value = [ApiResponse(code = 202, message = "Successfully verify Authorization-Code")])
    @PostMapping(value = ["/verify/code"])
    fun verifyCode(@RequestBody body: VerifyCodeRequest): Response<RegisterToken> {
        val registerToken = authService.verifyCode(body.code)
        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, RegisterToken(registerToken))
    }
}