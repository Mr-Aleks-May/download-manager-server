package com.mralexmay.projects.download_manager.server.api.user.converter;

import com.mralexmay.projects.download_manager.server.commons.converter.dto.TokenDto;
import com.mralexmay.projects.download_manager.server.commons.model.Token;
import com.mralexmay.projects.download_manager.server.commons.model.User;
import com.mralexmay.projects.download_manager.server.api.user.converter.mapper.CategoryMapper;
import com.mralexmay.projects.download_manager.server.api.user.converter.mapper.DownloadMapper;
import com.mralexmay.projects.download_manager.server.api.user.model.Download;
import com.mralexmay.projects.download_manager.server.commons.converter.dto.UserDto;
import com.mralexmay.projects.download_manager.server.commons.converter.mapper.TokenMapper;
import com.mralexmay.projects.download_manager.server.commons.converter.mapper.UserMapper;
import com.mralexmay.projects.download_manager.server.api.user.model.Category;
import com.mralexmay.projects.download_manager.server.api.user.converter.dto.CategoryDto;
import com.mralexmay.projects.download_manager.server.api.user.converter.dto.DownloadDto;
import org.springframework.stereotype.Component;

/**
 * Convert entities to data transfer objects and from data transfer objects to entities.
 */
@Component
public class UserEntitiesConverterImpl implements UserEntitiesConverter {
    private final TokenMapper tokenMapper;
    private final DownloadMapper downloadMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;


    // Inject dependencies.
    public UserEntitiesConverterImpl(TokenMapper tokenMapper, DownloadMapper downloadMapper, CategoryMapper categoryMapper, UserMapper userMapper) {
        this.tokenMapper = tokenMapper;
        this.downloadMapper = downloadMapper;
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
    }


    /**
     * Convert user object to user data transfer object.
     *
     * @param user user object.
     * @return user data transfer object.
     */
    @Override
    public UserDto convertToDto(User user) {
        return this.userMapper.fromEntityToDto(user);
    }

    /**
     * Convert user data transfer object to user object.
     *
     * @param userDto user data transfer object (dto).
     * @return user object.
     */
    @Override
    public User convertFromDto(UserDto userDto) {
        return this.userMapper.fromDtoToEntity(userDto);
    }

    /**
     * Convert download object to user data transfer object (dto).
     *
     * @param download download object.
     * @return download data transfer object (dto).
     */
    @Override
    public DownloadDto convertToDto(Download download) {
        return this.downloadMapper.fromEntityToDto(download);
    }

    /**
     * Convert download data transfer object to user object.
     *
     * @param downloadDto download data transfer object (dto).
     * @return download object.
     */
    @Override
    public Download convertFromDto(DownloadDto downloadDto) {
        return this.downloadMapper.fromDtoToEntity(downloadDto);
    }

    /**
     * Convert category object to user data transfer object (dto).
     *
     * @param category category object.
     * @return category data transfer object (dto).
     */
    @Override
    public CategoryDto convertToDto(Category category) {
        return this.categoryMapper.fromEntityToDto(category);
    }

    /**
     * Convert category data transfer object to user object.
     *
     * @param categoryDto category data transfer object (dto).
     * @return category object.
     */
    @Override
    public Category convertFromDto(CategoryDto categoryDto) {
        return this.categoryMapper.fromDtoToEntity(categoryDto);
    }

    /**
     * Convert token object to user data transfer object (dto).
     *
     * @param token token object.
     * @return token data transfer object (dto).
     */
    @Override
    public TokenDto convertToDto(Token token) {
        return this.tokenMapper.fromEntityToDto(token);
    }

    /**
     * Convert token data transfer object to user object.
     *
     * @param tokenDto token data transfer object (dto).
     * @return token object.
     */
    @Override
    public Token convertFromDto(TokenDto tokenDto) {
        return this.tokenMapper.fromDtoToEntity(tokenDto);
    }
}
