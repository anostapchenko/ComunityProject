package com.javamentor.qa.platform.webapp.controllers.dtocontroller;

import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "UserDto", description = "Get UserDto id")
@RestController
@RequestMapping("/api/user/")
public class UserResourseController {
    final private UserDtoService userDtoService;

    public UserResourseController(UserDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }

    @GetMapping("{userId}")
    @Operation(summary = "Получение UserDto по id")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
    })
    @ApiResponse(responseCode = "400", description = "Неверные учетные данные", content = {
            @Content(mediaType = "application/json")
    })

    public ResponseEntity<Optional<UserDto>> getUserDtoId(@PathVariable("userId") long id) {
        Optional<UserDto> userDto = userDtoService.findUserDtoById(id);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
