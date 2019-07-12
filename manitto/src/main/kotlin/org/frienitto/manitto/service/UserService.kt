package org.frienitto.manitto.service

import org.frienitto.manitto.domain.User
import org.frienitto.manitto.dto.AccessToken
import org.frienitto.manitto.dto.SignInDto
import org.frienitto.manitto.dto.SignUpDto
import org.frienitto.manitto.dto.UserDto
import org.frienitto.manitto.exception.NonAuthorizationException
import org.frienitto.manitto.exception.ResourceNotFoundException
import org.frienitto.manitto.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    @Transactional(readOnly = true)
    fun getUserByToken(userToken: String): User {
        return userRepository.findByToken(userToken) ?: throw NonAuthorizationException()
    }

    @Transactional
    fun signUp(signUpDto: SignUpDto): UserDto {
        val user = User.newUser(username = signUpDto.username, description = signUpDto.description, imageCode = signUpDto.imageCode, email = signUpDto.email, password = signUpDto.password)
        userRepository.save(user)
        return UserDto.from(user)
    }

    fun signIn(signInDto: SignInDto): AccessToken {
        val user = userRepository.findByEmail(signInDto.email) ?: throw ResourceNotFoundException()

        if (user.password != signInDto.password) {
            throw NonAuthorizationException()
        }
        return AccessToken(user.token, user.tokenExpiresDate)
    }
}