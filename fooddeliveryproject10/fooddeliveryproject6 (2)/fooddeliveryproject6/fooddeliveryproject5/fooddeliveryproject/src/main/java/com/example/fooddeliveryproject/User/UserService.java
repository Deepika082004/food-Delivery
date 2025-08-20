//package com.example.fooddeliveryproject.User;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class UserService {
//
//    private final UserAccountRepository userAccountRepository;
//
//    // Convert DTO → Entity
//    private UserAccount mapToEntity(UserAccountRequestBean dto) {
//        UserAccount user = new UserAccount();
//        user.setUsername(dto.getUsername());
//        user.setPassword(dto.getPassword());
//        user.setRole(dto.getRole());
//        return user;
//    }
//
//    // Convert Entity → DTO
//    private UserAccountResponseBean mapToDto(UserAccount entity) {
//        UserAccountResponseBean dto = new UserAccountResponseBean();
//        dto.setUsername(entity.getUsername());
//        dto.setPassword(entity.getPassword());
//        dto.setRole(entity.getRole());
//        return dto;
//    }
//
//    // Create User
//    public UserAccountResponseBean createUser(UserAccountRequestBean requestDto) {
//        UserAccount user = mapToEntity(requestDto);
//        UserAccount saved = userAccountRepository.save(user);
//        return mapToDto(saved);
//    }
//
//    // Get by ID
//    public UserAccountResponseBean getUserById(UUID id) {
//        UserAccount user = userAccountRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
//        return mapToDto(user);
//    }
//
//    // Get all
//    public List<UserAccountResponseBean> getAllUsers() {
//        return userAccountRepository.findAll()
//                .stream()
//                .map(this::mapToDto)
//                .collect(Collectors.toList());
//    }
//
//    // Update
//    public UserAccountResponseBean updateUser(UUID id, UserAccountRequestBean requestDto) {
//        UserAccount existingUser = userAccountRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
//
//        existingUser.setUsername(requestDto.getUsername());
//        existingUser.setPassword(requestDto.getPassword());
//
//        UserAccount updated = userAccountRepository.save(existingUser);
//        return mapToDto(updated);
//    }
//
//    // Delete
//    public void deleteUser(UUID id) {
//        userAccountRepository.deleteById(id);
//    }
//
//
//}
