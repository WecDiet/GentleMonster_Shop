package com.gentlemonster.GentleMonsterBE.Configurations.Mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gentlemonster.GentleMonsterBE.DTO.Responses.User.UserInforResponse;
import com.gentlemonster.GentleMonsterBE.Entities.User;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MapperConfig {
    @Bean
public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();

    modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

    

        // Tạo TypeMap cho User sang UserInforResponse
        modelMapper.createTypeMap(User.class, UserInforResponse.class)
                .addMappings(mapper -> {
                    // Ánh xạ các trường đơn giản
                    mapper.map(User::getFirstName, UserInforResponse::setFirstName);
                    mapper.map(User::getMiddleName, UserInforResponse::setMiddleName);
                    mapper.map(User::getLastName, UserInforResponse::setLastName);
                    mapper.map(User::getEmail, UserInforResponse::setEmail);
                    mapper.map(User::getGender, UserInforResponse::setGender);
                    mapper.map(User::getPhoneNumber, UserInforResponse::setPhoneNumber);
                    mapper.map(User::getBirthDay, UserInforResponse::setBirthDay);
                    mapper.map(User::getPassword, UserInforResponse::setPassword);
                });

    return modelMapper;
}

}