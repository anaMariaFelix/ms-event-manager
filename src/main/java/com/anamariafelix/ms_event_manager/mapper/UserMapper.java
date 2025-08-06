package com.anamariafelix.ms_event_manager.mapper;

import com.anamariafelix.ms_event_manager.dto.UserCreateDTO;
import com.anamariafelix.ms_event_manager.dto.UserResponseDTO;
import com.anamariafelix.ms_event_manager.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static User toUser(UserCreateDTO userCreateDTO){

        return new ModelMapper().map(userCreateDTO, User.class);
    }

    public static UserResponseDTO toUserDTO(User user){

        return new ModelMapper().map(user, UserResponseDTO.class);
    }
}
