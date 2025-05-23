package com.hoderick.rabbithole.user.api;

import com.hoderick.rabbithole.user.dto.UserProfileDto;
import com.hoderick.rabbithole.user.mapper.UserProfileMapper;
import com.hoderick.rabbithole.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    /**
     * Temporary controller to list all users. Obviously very inefficient
     */
    @GetMapping
    public List<UserProfileDto> getAllUsers() {
        return userProfileRepository.findAll()
                .stream().map(userProfileMapper::toDto)
                .toList();
    }
}
