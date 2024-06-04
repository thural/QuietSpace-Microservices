package com.jellybrains.quietspace_backend_ms.userservice.service.impls;

import com.jellybrains.quietspace_backend_ms.userservice.entity.User;
import com.jellybrains.quietspace_backend_ms.userservice.exception.UserNotFoundException;
import com.jellybrains.quietspace_backend_ms.userservice.mapper.custom.UserMapper;
import com.jellybrains.quietspace_backend_ms.userservice.model.request.UserRequest;
import com.jellybrains.quietspace_backend_ms.userservice.model.response.UserResponse;
import com.jellybrains.quietspace_backend_ms.userservice.repository.UserRepository;
import com.jellybrains.quietspace_backend_ms.userservice.service.UserService;
import com.jellybrains.quietspace_backend_ms.userservice.utils.PagingProvider;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public Page<UserResponse> listUsers(String username, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = PagingProvider.buildPageRequest(pageNumber, pageSize, null);
        Page<User> userPage;

        if (StringUtils.hasText(username)) {
            userPage = userRepository.findAllByUsernameIsLikeIgnoreCase("%" + username + "%", pageRequest);
        } else {
            userPage = userRepository.findAll(pageRequest);
        }

        return userPage.map(userMapper::userEntityToResponse);
    }

    @Override
    public Page<UserResponse> listUsersByQuery(String query, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = PagingProvider.buildPageRequest(pageNumber, pageSize, null);
        Page<User> userPage;

        if (StringUtils.hasText(query)) {
            userPage = userRepository.findAllByQuery(query, pageRequest);
        } else {
            userPage = userRepository.findAll(pageRequest);
        }

        return userPage.map(userMapper::userEntityToResponse);
    }

    @Override
    public Optional<UserResponse> getLoggedUserResponse() {
        UserResponse userResponse = userMapper.userEntityToResponse(getLoggedUser());
        return Optional.of(userResponse);
    }

    @Override
    public User getLoggedUser() {
        String email = null; // TODO: get email of authorized user
        return userRepository.findUserEntityByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Boolean validateUserInRequest(UUID userId) {
        return getLoggedUser().getId().equals(userId);
    }

    @Override
    public List<User> getUsersFromIdList(List<UUID> userIds) {
        return userIds.stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException("user not found")))
                .toList();
    }

    @Override
    public Boolean validateUsersByIdList(List<UUID> userIds) {
        return userIds.stream().allMatch(userRepository::existsById);
    }

    @Override
    public Optional<UserResponse> getUserResponseById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
        UserResponse userResponse = userMapper.userEntityToResponse(user);
        return Optional.of(userResponse);
    }

    @Override
    public Optional<User> getUserById(UUID userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
        return Optional.of(foundUser);
    }

    @Override
    public void deleteUser(UUID userId, String authHeader) {
        User loggedUser = getLoggedUser();

        if (!loggedUser.getRole().equals("admin") && !loggedUser.getId().equals(userId))
            throw new BadRequestException("user denied access to delete the resource");

        userRepository.deleteById(userId);

        // TODO: send event to authentication service
    }

    @Override
    public UserResponse patchUser(UserRequest userRequest) {

        User loggedUser = getLoggedUser();
        boolean isAdmin = loggedUser.getRole().equals("ADMIN");

        if (!isAdmin && !userRequest.getEmail().equals(loggedUser.getEmail()))
            throw new BadRequestException("logged user has no access to requested resource");

        if (StringUtils.hasText(userRequest.getUsername()))
            loggedUser.setUsername(userRequest.getUsername());
        if (StringUtils.hasText(userRequest.getEmail()))
            loggedUser.setEmail(userRequest.getEmail());

        return userMapper.userEntityToResponse(userRepository.save(loggedUser));
    }

}