package com.mralexmay.projects.download_manager.server.api.user.converter;

import com.mralexmay.projects.download_manager.server.commons.converter.dto.TokenDto;
import com.mralexmay.projects.download_manager.server.commons.model.Token;
import com.mralexmay.projects.download_manager.server.commons.model.User;
import com.mralexmay.projects.download_manager.server.api.user.model.Download;
import com.mralexmay.projects.download_manager.server.commons.converter.dto.UserDto;
import com.mralexmay.projects.download_manager.server.api.user.model.Category;
import com.mralexmay.projects.download_manager.server.api.user.converter.dto.CategoryDto;
import com.mralexmay.projects.download_manager.server.api.user.converter.dto.DownloadDto;

/**
 * Convert entities to data transfer objects and back.
 */
public interface UserEntitiesConverter {

    /**
     * Convert user object to user data transfer object (dto).
     *
     * @param user user object.
     * @return user data transfer object (dto).
     */
    UserDto convertToDto(User user);

    /**
     * Convert user data transfer object to user object.
     *
     * @param userDto user data transfer object (dto).
     * @return user object.
     */
    User convertFromDto(UserDto userDto);

    /**
     * Convert download object to user data transfer object (dto).
     *
     * @param download download object.
     * @return download data transfer object (dto).
     */
    DownloadDto convertToDto(Download download);

    /**
     * Convert download data transfer object to user object.
     *
     * @param downloadDto download data transfer object (dto).
     * @return download object.
     */
    Download convertFromDto(DownloadDto downloadDto);

    /**
     * Convert category object to user data transfer object (dto).
     *
     * @param category category object.
     * @return category data transfer object (dto).
     */
    CategoryDto convertToDto(Category category);

    /**
     * Convert category data transfer object to user object.
     *
     * @param categoryDto category data transfer object (dto).
     * @return category object.
     */
    Category convertFromDto(CategoryDto categoryDto);

    /**
     * Convert token object to user data transfer object (dto).
     *
     * @param token token object.
     * @return token data transfer object (dto).
     */
    TokenDto convertToDto(Token token);

    /**
     * Convert token data transfer object to user object.
     *
     * @param tokenDto token data transfer object (dto).
     * @return token object.
     */
    Token convertFromDto(TokenDto tokenDto);
}
