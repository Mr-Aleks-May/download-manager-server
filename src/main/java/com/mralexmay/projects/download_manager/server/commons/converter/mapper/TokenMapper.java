package com.mralexmay.projects.download_manager.server.commons.converter.mapper;

import com.mralexmay.projects.download_manager.server.commons.converter.dto.TokenDto;
import com.mralexmay.projects.download_manager.server.commons.model.Token;
import org.springframework.stereotype.Component;


@Component
public class TokenMapper {
    public TokenDto fromEntityToDto(Token token) {
        return new TokenDto()
                .setId(token.getId())
                .setToken(token.getValue())
                .setExpires(token.getExpires());
    }

    public Token fromDtoToEntity(TokenDto tokenDto) {
        return new Token()
                .setId(tokenDto.getId())
                .setValue(tokenDto.getToken())
                .setExpires(tokenDto.getExpires());
    }
}
