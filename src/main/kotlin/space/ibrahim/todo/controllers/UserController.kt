package space.ibrahim.todo.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.MissingRequiredPropertiesException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import space.ibrahim.todo.models.UserRegistration
import space.ibrahim.todo.services.UserAlreadyExistsException
import space.ibrahim.todo.services.UserService

@RestController
@RequestMapping("/api/v1/user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @PostMapping(consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun createUser(@ModelAttribute user: UserRegistration): ResponseEntity<Any> =
        try {
            ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user))
        } catch (e: UserAlreadyExistsException) {
            ResponseEntity.badRequest().body(e.message)
        } catch (e: MissingRequiredPropertiesException) {
            ResponseEntity.badRequest().body(e.message)
        }

    @GetMapping
    fun login(): ResponseEntity<Any> {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return ResponseEntity.ok().body(userService.findUserByUsername(authentication.name))
    }
}