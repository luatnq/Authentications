package com.example.userservice.mapper;

import com.example.demo.UserDTO;
import com.example.userservice.entity.User;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserDTO convertToDTO(User user) {
        final UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }
//    public static User convertToEntity(U reviewDTO) {
//        final Review review = modelMapper.map(reviewDTO, Review.class);
//        return review;
//    }
}
