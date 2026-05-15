package org.example.trellite.user;

import org.example.trellite.common.BaseService;
import org.example.trellite.common.ResourceNotFoundException;
import org.example.trellite.user.dto.UserRequest;
import org.example.trellite.user.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements BaseService<UserRequest, UserResponse, Long> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    @Override
    public List<UserResponse> getAll() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getById(Long id) {
        return userRepository
                .findByUserId(id)
                .map(userMapper::toResponse)
                .orElseThrow( () -> new ResourceNotFoundException("User with id of " + id + " not found."));
    }

    @Override
    public UserResponse save(UserRequest dto) {
        var model = userMapper.toModel(dto);
        var saved = userRepository.save(model);
        return userMapper.toResponse(saved);
    }

    @Override
    public UserResponse update(Long id, UserRequest dto) {
        var existing = userRepository
                .findByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id of " + id + " not found."));
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setPassword(dto.getPassword());
        return userMapper.toResponse(userRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(
                userRepository
                        .findByUserId(id)
                        .orElseThrow( () -> new ResourceNotFoundException("User with id of " + id + " not found.") )
        );
    }

}
