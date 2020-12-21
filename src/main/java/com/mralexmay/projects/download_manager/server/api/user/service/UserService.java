package com.mralexmay.projects.download_manager.server.api.user.service;

import com.mralexmay.projects.download_manager.server.api.user.exception.CategoryDownloadsCountBelowZeroException;
import com.mralexmay.projects.download_manager.server.commons.converter.dto.UserDto;
import com.mralexmay.projects.download_manager.server.commons.exception.UserAlreadyExistsException;
import com.mralexmay.projects.download_manager.server.commons.exception.UserNotFoundException;
import com.mralexmay.projects.download_manager.server.commons.exception.WrongUserCredentialException;
import com.mralexmay.projects.download_manager.server.commons.model.User;
import com.mralexmay.projects.download_manager.server.api.user.converter.dto.DownloadDto;
import com.mralexmay.projects.download_manager.server.api.user.model.Download;

import java.util.List;
import java.util.Optional;

/**
 * Main server service.
 */
public interface UserService {

    /**
     * Search user by data specified in user dto.
     *
     * @param userDto user dto object.
     * @return user if exists, or empty.
     */
    User findUserBy(UserDto userDto) throws WrongUserCredentialException, UserNotFoundException;

    /**
     * Search user by id.
     *
     * @param id user id.
     * @return user with specified identifier, or empty.
     */
    Optional<User> findUserById(Long id);

    /**
     * Search user by token.
     *
     * @param token user token.
     * @return user with specified token, or empty.
     */
    Optional<User> findUserByToken(String token);

    /**
     * Search user by login.
     *
     * @param login user login.
     * @return user with specified login, or empty.
     */
    Optional<User> findUserDtoByLogin(String login);

    /**
     * Search user by login.
     *
     * @param login user login.
     * @return user with specified login, or empty.
     */
    Optional<User> findUserByLogin(String login);

    /**
     * Return list of all users wrapped in user dto.
     *
     * @return all users converted to dto.
     */
    List<UserDto> findDtoAll();

    /**
     * Return list of all registered users.
     *
     * @return all registered users.
     */
    List<User> findAll();


    /**
     * Create new user with specified login and password.
     * If user exists throws UserAlreadyExistsException.
     *
     * @param userDto user data transfer object.
     * @return created user object.
     * @throws UserAlreadyExistsException throws if user exists.
     */
    User createUser(UserDto userDto) throws UserAlreadyExistsException;

    /**
     * Update/save user state in DB.
     *
     * @param userDto user to update.
     * @return user dto object.
     */
    User updateUser(UserDto userDto);

    /**
     * Delete user by id.
     *
     * @param id user id.
     * @return
     */
    boolean deleteUserById(Long id);

    /**
     * Delete user account from DB.
     *
     * @param user
     * @return true if user deleted; otherwise false.
     */
    boolean deleteUser(User user) throws CategoryDownloadsCountBelowZeroException;

    /**
     * Search download specified dsid in user downloads list.
     *
     * @param user user object.
     * @param dsid download serializable unique identifier.
     * @return download.
     */
    Optional<Download> findDownload(User user, String dsid);

    /**
     * Search download specified dsid in user downloads list.
     *
     * @param user user object.
     * @return download list.
     */
    List<Download> findAllDownload(User user);

    /**
     * Add download to user download list.
     *
     * @param toUser      user object.
     * @param downloadDto download details.
     * @return true if all downloads were added successfully; otherwise, false.
     */
    boolean addDownload(User toUser, DownloadDto downloadDto);

    /**
     * Add list of downloads to user downloads.
     *
     * @param toUser          user object.
     * @param downloadDtoList downloads list.
     * @return true if all downloads were added successfully; otherwise, false.
     */
    boolean addDownloadsList(User toUser, List<DownloadDto> downloadDtoList);

    /**
     * Remove download from user download list.
     *
     * @param fromUser user object.
     * @param dsid     download unique identifier in user downloads list.
     * @return if download removed successfully.
     */
    boolean deleteDownload(User fromUser, String dsid) throws CategoryDownloadsCountBelowZeroException;

    boolean deleteDownload(User fromUser, Download download) throws CategoryDownloadsCountBelowZeroException;

    /**
     * Remove all dsid specified in list.
     *
     * @param fromUser            remove specified downloads from user downloads list.
     * @param downloadDSIDDtoList list of unique identifiers to be removed.
     * @return
     */
    boolean removeDownloadsList(User fromUser, List<String> downloadDSIDDtoList) throws CategoryDownloadsCountBelowZeroException;
}
