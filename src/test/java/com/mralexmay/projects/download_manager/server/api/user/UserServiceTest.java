package com.mralexmay.projects.download_manager.server.api.user;

import com.mralexmay.projects.download_manager.server.api.user.converter.dto.DownloadDto;
import com.mralexmay.projects.download_manager.server.api.user.exception.CategoryDownloadsCountBelowZeroException;
import com.mralexmay.projects.download_manager.server.api.user.model.Download;
import com.mralexmay.projects.download_manager.server.commons.converter.dto.UserDto;
import com.mralexmay.projects.download_manager.server.commons.exception.UserAlreadyExistsException;
import com.mralexmay.projects.download_manager.server.commons.exception.UserNotFoundException;
import com.mralexmay.projects.download_manager.server.commons.exception.WrongUserCredentialException;
import com.mralexmay.projects.download_manager.server.commons.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class UserServiceTest {
    /**
     * Tests search user by data specified in user dto.
     */
    @Test
    void findUserByTest() {

    }

    /**
     * Tests search user by id.
     */
    @Test
    void findUserByIdTest() {

    }

    /**
     * Tests search user by token.
     */
    @Test
    void findUserByTokenTest() {

    }

    /**
     * Tests search user by login.
     */
    @Test
    void findUserDtoByLoginTest() {

    }

    /**
     * Tests search user by login.
     */
    @Test
    void findUserByLoginTest() {

    }

    /**
     * Tests return list of all users wrapped in user dto.
     *
     * @return all users converted to dto.
     */
    @Test
    void findDtoAllTest() {

    }

    /**
     * Tests return list of all registered users.
     */
    @Test
    void findAllUsersTest() {

    }


    /**
     * Tests create new user with specified login and password.
     * If user exists throws UserAlreadyExistsException.
     */
    @Test
    void createUserTest() throws UserAlreadyExistsException {

    }

    /**
     * Tests update/save user state in DB.
     */
    @Test
    void updateUserTest() {

    }

    /**
     * Tests delete user by id.
     */
    @Test
    void deleteUserByIdTest() {

    }

    /**
     * Tests delete user account from DB.
     */
    @Test
    void deleteUserTest() throws CategoryDownloadsCountBelowZeroException {

    }

    /**
     * Tests search download specified dsid in user downloads list.
     */
    @Test
    void findDownloadTest() {

    }

    /**
     * Tests add download to user download list.
     */
    @Test
    void addDownloadTest() {

    }

    /**
     * Tests add list of downloads to user downloads.
     */
    @Test
    void addDownloadsListTest() {

    }

    /**
     * Tests remove download from user download list.
     */
    @Test
    void deleteDownloadTest() {

    }

    /**
     * Tests remove all dsid specified in list.
     */
    @Test
    void removeDownloadsListTest() {

    }
}
