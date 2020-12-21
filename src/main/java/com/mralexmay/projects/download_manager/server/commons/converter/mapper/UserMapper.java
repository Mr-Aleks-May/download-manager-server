package com.mralexmay.projects.download_manager.server.commons.converter.mapper;

import com.mralexmay.projects.download_manager.server.commons.model.User;
import com.mralexmay.projects.download_manager.server.commons.converter.dto.UserDto;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {
    private TokenMapper tokenMapper;


    public UserMapper(TokenMapper tokenMapper) {
        this.tokenMapper = tokenMapper;
    }


    public UserDto fromEntityToDto(User user) {
        return new UserDto()
                .setLogin(user.getLogin())
                .setPassword(user.getPassword())
                .setToken(tokenMapper.fromEntityToDto(user.getToken()))
                .setDownloads(user.getDownloads());
    }

    public User fromDtoToEntity(UserDto userDto) {
        return new User()
                .setLogin(userDto.getLogin())
                .setPassword(userDto.getPassword())
                .setToken(tokenMapper.fromDtoToEntity(userDto.getToken()))
                .setDownloads(userDto.getDownloads());
    }
}
