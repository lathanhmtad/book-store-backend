package com.app.config;

import com.app.entity.User;
import com.app.payload.user.UserCreationDto;
import com.app.payload.user.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModalMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        // setup map user request to user entity
        TypeMap<UserCreationDto, User> userCreationDtoUserPropertyMapper = modelMapper.createTypeMap(UserCreationDto.class, User.class);
        userCreationDtoUserPropertyMapper.addMappings(
                mapper -> {
                    mapper.skip(User::setCreatedDate);
                    mapper.skip(User::setCreatedBy);
                    mapper.skip(User::setLastModifiedDate);
                    mapper.skip(User::setLastModifiedBy);
                    mapper.skip(User::setPhoto);
                    mapper.skip(User::setRoles);
                }
        );

        return modelMapper;
    }
}
