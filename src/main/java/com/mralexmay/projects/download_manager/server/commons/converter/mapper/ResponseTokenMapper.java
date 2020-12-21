package com.mralexmay.projects.download_manager.server.commons.converter.mapper;

import com.mralexmay.projects.download_manager.server.commons.model.Token;
import com.mralexmay.projects.download_manager.server.commons.converter.dto.ResponseTokenDto;
import org.springframework.stereotype.Component;


@Component
public class ResponseTokenMapper {
    public ResponseTokenDto fromEntityToDto(Token token) {
        return new ResponseTokenDto()
                .setToken(token.getValue());
    }

    public Token fromDtoToEntity(ResponseTokenDto tokenDto) {
        return new Token()
                .setValue(tokenDto.getToken());
    }
}
