package com.mralexmay.projects.download_manager.server.api.user.controller.rest;

import com.mralexmay.projects.download_manager.server.api.user.converter.mapper.CategoryMapper;
import com.mralexmay.projects.download_manager.server.api.user.converter.mapper.DownloadMapper;
import com.mralexmay.projects.download_manager.server.commons.exception.Exceptions;
import com.mralexmay.projects.download_manager.server.commons.model.User;
import com.mralexmay.projects.download_manager.server.api.user.exception.CategoryDownloadsCountBelowZeroException;
import com.mralexmay.projects.download_manager.server.api.user.exception.DownloadNotAddedException;
import com.mralexmay.projects.download_manager.server.api.user.exception.DownloadNotFoundException;
import com.mralexmay.projects.download_manager.server.api.user.model.Download;
import com.mralexmay.projects.download_manager.server.commons.exception.UserAlreadyExistsException;
import com.mralexmay.projects.download_manager.server.commons.exception.UserNotFoundException;
import com.mralexmay.projects.download_manager.server.commons.exception.WrongUserCredentialException;
import com.mralexmay.projects.download_manager.server.commons.model.Token;
import com.mralexmay.projects.download_manager.server.api.user.converter.dto.DownloadDto;
import com.mralexmay.projects.download_manager.server.commons.converter.dto.UserDto;
import com.mralexmay.projects.download_manager.server.commons.converter.mapper.ResponseTokenMapper;
import com.mralexmay.projects.download_manager.server.commons.response.ResponseMessage;
import com.mralexmay.projects.download_manager.server.api.user.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Main API controller.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    //get log4j handler
    private static final Logger LOGGER = LogManager.getLogger(UserController.class);
    private final UserService userService;
    private final MessageSource messageSource;
    private final ResponseTokenMapper responseTokenMapper;


    // Inject dependencies.
    public UserController(UserService userService, MessageSource messageSource, ResponseTokenMapper responseTokenMapper) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.responseTokenMapper = responseTokenMapper;
    }

    /**
     * Greeting api for customer.
     *
     * @return greetings.
     */
    @GetMapping(value = "/welcome", produces = {"application/json"})
    public ResponseEntity<Object> welcome() {
        try {
            // Return greetings.
            return new ResponseEntity<>(new ResponseMessage("Greetings!").asJson(), HttpStatus.OK);
        } catch (Exception e) {
            // If exception throw, log it.
            LOGGER.error(e);
            // Notify client that something was wrong.

            return new ResponseEntity<>(new ResponseMessage(Exceptions.INTERNAL_SERVER_ERROR).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Register new user on server.
     *
     * @param login    user login
     * @param password user password.
     *                 HASH MD5 (32 characters long).
     * @return access token.
     */
    @PostMapping(value = "/signup", produces = {"application/json"})
    public ResponseEntity<?> signup(@RequestParam(name = "login", required = true) String login,
                                    @RequestParam(name = "password", required = true) String password) {
        try {
            // Create new user with specified login and password.
            UserDto newUser = new UserDto()
                    .setLogin(login)
                    .setPassword(password);

            // Try to create new user based on data in new user object.
            // Get token form user.
            Token token = userService.createUser(newUser).getToken();

            // Return token.
            return new ResponseEntity<>(responseTokenMapper.fromEntityToDto(token), HttpStatus.OK);
        } catch (UserAlreadyExistsException e) {
            // Respond client that user with same login exists.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.USER_ALREADY_EXISTS).asJson(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            // Respond client that server has internal error.
            LOGGER.error(e);

            return new ResponseEntity<>(new ResponseMessage(Exceptions.INTERNAL_SERVER_ERROR).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Login user on server.
     *
     * @param login    user login.
     * @param password user password.
     * @return access token.
     */
    @PostMapping(value = "/signin", produces = {"application/json"})
    public ResponseEntity<?> signin(@RequestParam(name = "login", required = true) String login,
                                    @RequestParam(name = "password", required = true) String password) {
        try {
            // Create new user with specified login and password.
            UserDto newUserDto = new UserDto()
                    .setLogin(login)
                    .setPassword(password);

            // Try to find user with specified data.
            // Get token form user.
            Token token = userService.findUserBy(newUserDto).getToken();

            // Return token.
            return new ResponseEntity<>(responseTokenMapper.fromEntityToDto(token), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            // Respond client that user with specified password not exists.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.USER_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (WrongUserCredentialException e) {
            // Respond client that password was wrong.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.WRONG_PASSWORD).asJson(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            // Notify about internal error.
            LOGGER.error(e);

            return new ResponseEntity<>(new ResponseMessage(Exceptions.INTERNAL_SERVER_ERROR).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Remove user account.
     *
     * @param token access token belonging to user.
     * @return if operation successful, return 200 OK.
     */
    @DeleteMapping(value = "/remove", produces = {"application/json"})
    public ResponseEntity<?> removeUserAccount(@RequestParam(name = "token", required = true) String token) {
        try {
            // Search user with specified token.
            Optional<User> user = userService.findUserByToken(token);

            // Check if user exist.
            if (user.isPresent()) {
                // Try to remove user account; Check result.
                if (userService.deleteUser(user.get())) {
                    // If user account removed return 200 OK.
                    return new ResponseEntity<>(HttpStatus.OK);
                }

                // Throw exception if download not added to user downloads list.
                throw new DownloadNotAddedException();
            }

            // Throws exception, if token: not valid, expires, not belong to any user.
            throw new UserNotFoundException();
        } catch (UserNotFoundException e) {
            // Respond to the client that client with specified login not exists.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.USER_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (WrongUserCredentialException e) {
            // Respond to the client that password not correct.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.WRONG_PASSWORD).asJson(), HttpStatus.NOT_ACCEPTABLE);
        } catch (DownloadNotAddedException e) {
            // Reply to the client that the download is not added to the user's download list.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.DOWNLOAD_NOT_ADDED).asJson(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseMessage(Exceptions.INTERNAL_SERVER_ERROR).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get download from specified user.
     *
     * @param token access token belonging to some user.
     * @param dsid  download serializable unique identifier.
     *              Unique value in user downloads list.
     * @return
     */
    @GetMapping(value = "/download/get", produces = {"application/json"})
    public ResponseEntity<?> getDownload(@RequestParam(name = "token", required = true) String token,
                                         @RequestParam(name = "dsid", required = true) String dsid) {
        try {
            // Search user with specified token.
            Optional<User> user = userService.findUserByToken(token);

            // Check such user exist.
            if (user.isPresent()) {
                // Get download from user downloads list with specified dsid.
                Optional<Download> downloadDto = userService.findDownload(user.get(), dsid);

                // Check if download exists.
                if (downloadDto.isPresent()) {
                    // Return download to client.
                    return new ResponseEntity<>(new DownloadMapper(new CategoryMapper()).fromEntityToDto(downloadDto.get()), HttpStatus.OK);
                }

                // If download not found, throw DownloadNotFoundException.
                throw new DownloadNotFoundException();
            }

            // If user with specified token not found, throw UserNotFoundException.
            throw new UserNotFoundException();
        } catch (UserNotFoundException e) {
            // Respond client that user with specified token not exist or token expires.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.USER_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (WrongUserCredentialException e) {
            // Respond client that password wrong.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.WRONG_PASSWORD).asJson(), HttpStatus.NOT_ACCEPTABLE);
        } catch (DownloadNotFoundException e) {
            // Notify client that download with specified disd not found (in user downloads list).
            return new ResponseEntity<>(new ResponseMessage(Exceptions.DOWNLOAD_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Log error.
            LOGGER.error(e);
            // Tell client about internal error.

            return new ResponseEntity<>(new ResponseMessage(Exceptions.INTERNAL_SERVER_ERROR).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all download from specified user.
     *
     * @param token access token belonging to some user.
     * @param dsid  download serializable unique identifier.
     *              Unique value in user downloads list.
     * @return
     */
    @GetMapping(value = "/download/get/list", produces = {"application/json"})
    public ResponseEntity<?> getDownloadList(@RequestParam(name = "token", required = true) String token) {
        try {
            // Search user with specified token.
            Optional<User> user = userService.findUserByToken(token);

            // Check such user exist.
            if (user.isPresent()) {
                // Get download from user downloads list with specified dsid.
                List<Download> downloads = userService.findAllDownload(user.get());
                // Map downloads.
                List<DownloadDto> downloadsDto = downloads.stream()
                        .map((d) -> new DownloadMapper(new CategoryMapper()).fromEntityToDto(d))
                        .collect(toList());


                // Reply client
                return new ResponseEntity<>(downloadsDto, HttpStatus.OK);
            }

            // If user with specified token not found, throw UserNotFoundException.
            throw new UserNotFoundException();
        } catch (UserNotFoundException e) {
            // Respond client that user with specified token not exist or token expires.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.USER_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (WrongUserCredentialException e) {
            // Respond client that password wrong.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.WRONG_PASSWORD).asJson(), HttpStatus.NOT_ACCEPTABLE);
        } catch (DownloadNotFoundException e) {
            // Notify client that download with specified disd not found (in user downloads list).
            return new ResponseEntity<>(new ResponseMessage(Exceptions.DOWNLOAD_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Log error.
            LOGGER.error(e);
            // Tell client about internal error.

            return new ResponseEntity<>(new ResponseMessage(Exceptions.INTERNAL_SERVER_ERROR).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Add download to user downloads list.
     *
     * @param token       access token belonging to user.
     * @param downloadDto download.
     * @return if download added successfully, return 200 OK.
     */
    @PostMapping(value = "/download/add", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {"application/json"})
    public ResponseEntity<?> addDownload(@RequestParam(name = "token", required = true) String token,
                                         @RequestBody(required = true) DownloadDto downloadDto) {
        try {
            // Search user with specified token.
            Optional<User> user = userService.findUserByToken(token);

            // Check user exist.
            if (user.isPresent()) {
                // Add download to user list.
                if (userService.addDownload(user.get(), downloadDto)) {
                    // If download added successful return 200 OK.
                    return new ResponseEntity<>(HttpStatus.OK);
                }

                // Throw exception if download not added to user downloads list.
                throw new DownloadNotAddedException();
            }

            // Throws exception, if token: not valid, expires, not belong to any user.
            throw new UserNotFoundException();
        } catch (UserNotFoundException e) {
            // Respond to the client that client with specified login not exists.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.USER_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (WrongUserCredentialException e) {
            // Respond to the client that password not correct.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.WRONG_PASSWORD).asJson(), HttpStatus.NOT_ACCEPTABLE);
        } catch (DownloadNotAddedException e) {
            // Reply to the client that the download is not added to the user's download list.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.DOWNLOAD_NOT_ADDED).asJson(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error(e);
            return new ResponseEntity<>(new ResponseMessage(Exceptions.INTERNAL_SERVER_ERROR).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Add download to user downloads list.
     *
     * @param token           access token belonging to user.
     * @param downloadDtoList download.
     * @return if download added successfully, return 200 OK.
     */
    @PostMapping(value = "/download/add/list", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {"application/json"})
    public ResponseEntity<?> addDownloadsList(@RequestParam(name = "token", required = true) String token,
                                              @RequestBody(required = true) List<DownloadDto> downloadDtoList) {
        try {
            // Search user with specified token.
            Optional<User> user = userService.findUserByToken(token);

            // Check if user exist.
            if (user.isPresent()) {
                // Add download to user list.
                if (userService.addDownloadsList(user.get(), downloadDtoList)) {
                    // If download added successful return 200 OK.
                    return new ResponseEntity<>(HttpStatus.OK);
                }

                // Throw exception if download not added to user downloads list.
                throw new DownloadNotAddedException();
            }

            // Throws exception, if token: not valid, expires, not belong to any user.
            throw new UserNotFoundException();
        } catch (UserNotFoundException e) {
            // Respond to the client that client with specified login not exists.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.USER_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (WrongUserCredentialException e) {
            // Respond to the client that password not correct.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.WRONG_PASSWORD).asJson(), HttpStatus.NOT_ACCEPTABLE);
        } catch (DownloadNotAddedException e) {
            // Reply to the client that the download is not added to the user's download list.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.DOWNLOAD_NOT_ADDED).asJson(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseMessage(Exceptions.INTERNAL_SERVER_ERROR).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete download from user download list.
     *
     * @param token access token belonging to some user.
     * @param dsid  download serializable unique identifier.
     *              Unique value in user downloads list.
     * @return
     */
    @DeleteMapping(value = "/download/delete", produces = {"application/json"})
    public ResponseEntity<?> deleteDownload(@RequestParam(name = "token", required = true) String token,
                                            @RequestParam(name = "dsid", required = true) String dsid) {
        try {
            // Search user with specified token.
            Optional<User> user = userService.findUserByToken(token);

            // Check if such user exist.
            if (user.isPresent()) {
                // Try to remove download with specified dsid.
                if (userService.deleteDownload(user.get(), dsid)) {
                    // Return download to client.
                    return new ResponseEntity<>(HttpStatus.OK);
                }

                // If download not found, throw DownloadNotFoundException.
                throw new DownloadNotFoundException();
            }

            // If user with specified token not found, throw UserNotFoundException.
            throw new UserNotFoundException();
        } catch (UserNotFoundException e) {
            // Respond client that user with specified token not exist or token expires.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.USER_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (WrongUserCredentialException e) {
            // Respond client that password wrong.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.WRONG_TOKEN).asJson(), HttpStatus.NOT_ACCEPTABLE);
        } catch (DownloadNotFoundException e) {
            // Notify client that download with specified disd not found (in user downloads list).
            return new ResponseEntity<>(new ResponseMessage(Exceptions.DOWNLOAD_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (CategoryDownloadsCountBelowZeroException e) {
            // Log error.
            LOGGER.error(e);
            // Notify client that category downloads count below zero.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.CATEGORY_DOWNLOADS_COUNT_BELOW_ZERO).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            // Log error.
            LOGGER.error(e);
            // Tell client about internal error.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.INTERNAL_SERVER_ERROR).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/download/delete/list", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {"application/json"})
    public ResponseEntity<?> deleteDownloadsList(@RequestParam(name = "token", required = true) String token,
                                                 @RequestBody(required = true) List<String> downloadDtoDSIDList) {
        try {
            // Search user with specified token.
            Optional<User> user = userService.findUserByToken(token);

            // Check if user exist.
            if (user.isPresent()) {
                // Add download to user list.
                if (userService.removeDownloadsList(user.get(), downloadDtoDSIDList)) {
                    // If download added successful return 200 OK.
                    return new ResponseEntity<>(HttpStatus.OK);
                }

                // Throw exception if download not added to user downloads list.
                throw new DownloadNotAddedException();
            }

            // Throws exception, if token: not valid, expires, not belong to any user.
            throw new UserNotFoundException();
        } catch (UserNotFoundException e) {
            // Respond to the client that client with specified login not exists.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.USER_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (WrongUserCredentialException e) {
            // Respond to the client that password not correct.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.WRONG_PASSWORD).asJson(), HttpStatus.NOT_ACCEPTABLE);
        } catch (DownloadNotAddedException e) {
            // Reply to the client that the download is not added to the user's download list.
            return new ResponseEntity<>(new ResponseMessage(Exceptions.DOWNLOAD_NOT_ADDED).asJson(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error(e);
            return new ResponseEntity<>(new ResponseMessage(Exceptions.INTERNAL_SERVER_ERROR).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
