package com.mralexmay.projects.download_manager.server.api.store.plugin.controller.rest;

import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.PluginEntitiesConverter;
import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.dto.PluginPublishDto;
import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.dto.PluginRemoveDto;
import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.dto.PluginSearchResultDto;
import com.mralexmay.projects.download_manager.server.api.store.plugin.exception.PluginNotFoundException;
import com.mralexmay.projects.download_manager.server.api.store.plugin.exception.PluginNotPublishException;
import com.mralexmay.projects.download_manager.server.api.store.plugin.model.Plugin;
import com.mralexmay.projects.download_manager.server.api.store.plugin.service.PluginService;
import com.mralexmay.projects.download_manager.server.api.user.service.UserService;
import com.mralexmay.projects.download_manager.server.commons.exception.Exceptions;
import com.mralexmay.projects.download_manager.server.commons.exception.UserNotFoundException;
import com.mralexmay.projects.download_manager.server.commons.model.User;
import com.mralexmay.projects.download_manager.server.commons.response.ResponseMessage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;

@RestController
@RequestMapping("/api/store/plugin")
public class PluginStoreController {
    private static final Logger LOGGER = LogManager.getLogger(PluginStoreController.class);
    private UserService userService;
    private PluginService pluginService;
    private PluginEntitiesConverter pluginEntitiesConverter;


    @Autowired
    public PluginStoreController(UserService userService, PluginService pluginService, PluginEntitiesConverter pluginEntitiesConverter) {
        this.userService = userService;
        this.pluginService = pluginService;
        this.pluginEntitiesConverter = pluginEntitiesConverter;
    }


    @PostMapping(value = "/publish", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> publishPluginInStore(@RequestParam(name = "token", required = true) String token,
                                                  @RequestBody(required = true) PluginPublishDto pluginPublishDto) {
        try {
            // Search user by token.
            Optional<User> user = userService.findUserByToken(token);

            // Check if user exists.
            if (user.isPresent()) {
                // Publish plugin
                if (pluginService.publishPlugin(user.get(), pluginPublishDto)) {
                    return new ResponseEntity<>(HttpStatus.OK);
                }

                throw new PluginNotPublishException();
            }

            throw new UserNotFoundException();
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage(Exceptions.USER_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (PluginNotPublishException e) {
            LOGGER.log(Level.ERROR, e);
            return new ResponseEntity<>(new ResponseMessage(Exceptions.PLUGINS_NOT_PUBLISHED).asJson(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
            return new ResponseEntity<>(new ResponseMessage(Exceptions.INTERNAL_SERVER_ERROR).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/remove", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> removePluginFromStore(@RequestParam(name = "token", required = true) String token,
                                                   @RequestBody(required = true) PluginRemoveDto pluginRemoveDto) {
        try {
            // Search user by token.
            Optional<User> user = userService.findUserByToken(token);

            // Check if user exists.
            if (user.isPresent()) {
                // Unpublish plugin.
                if (pluginService.removePlugin(user.get(), pluginRemoveDto)) {
                    return new ResponseEntity<>(HttpStatus.OK);
                }

                throw new PluginNotPublishException();
            }

            throw new UserNotFoundException();
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage(Exceptions.USER_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (PluginNotPublishException e) {
            LOGGER.log(Level.ERROR, e);
            return new ResponseEntity<>(new ResponseMessage(Exceptions.PLUGINS_NOT_PUBLISHED).asJson(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
            return new ResponseEntity<>(new ResponseMessage(Exceptions.INTERNAL_SERVER_ERROR).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/find/by_tags", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findPluginsByTags(@RequestParam(name = "token", required = true) String token,
                                               @RequestBody(required = true) Set<String> tags) {
        try {
            // Search user by token.
            Optional<User> user = userService.findUserByToken(token);

            // Check if user exists.
            if (user.isPresent()) {
                // Search plugins with tags.
                List<PluginSearchResultDto> plugins = pluginService.containsAllTags(user.get(), tags);

                return new ResponseEntity<>(new ResponseMessage(plugins).asJson(), HttpStatus.OK);
            }

            throw new UserNotFoundException();
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage(Exceptions.USER_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
            return new ResponseEntity<>(new ResponseMessage(Exceptions.INTERNAL_SERVER_ERROR).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/find/by_part_of_name", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findPluginsByName(@RequestParam(name = "token", required = true) String token,
                                               @RequestParam(name = "part_of_name") String partOfName) {
        try {
            // Search user by token.
            Optional<User> user = userService.findUserByToken(token);

            // Check if user exists.
            if (user.isPresent()) {
                // Search plugins by specified substring.
                List<PluginSearchResultDto> plugins = pluginService.containsInName(user.get(), partOfName);

                return new ResponseEntity<>(new ResponseMessage(plugins).asJson(), HttpStatus.OK);
            }

            throw new UserNotFoundException();
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage(Exceptions.USER_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
            return new ResponseEntity<>(new ResponseMessage(Exceptions.INTERNAL_SERVER_ERROR).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/find/by_psid", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findPluginByPSID(@RequestParam(name = "token", required = true) String token,
                                              @RequestParam(name = "psid") String pluginPSID) {
        try {
            // Search user by token.
            Optional<User> user = userService.findUserByToken(token);

            // Check if user exists.
            if (user.isPresent()) {
                // Search plugins by specified substring.
                Optional<Plugin> plugin = pluginService.findByPSID(pluginPSID);

                if (plugin.isPresent()) {
                    return new ResponseEntity<>(new ResponseMessage(plugin.get()).asJson(), HttpStatus.OK);
                }

                throw new PluginNotFoundException();
            }

            throw new UserNotFoundException();
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage(Exceptions.USER_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (PluginNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage(Exceptions.PLUGIN_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
            return new ResponseEntity<>(new ResponseMessage(Exceptions.INTERNAL_SERVER_ERROR).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/find/all", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findPluginByPSID(@RequestParam(name = "token", required = true) String token) {
        try {
            // Search user by token.
            Optional<User> user = userService.findUserByToken(token);

            // Check if user exists.
            if (user.isPresent()) {
                // Return all available plugins (limit 200).
                List<PluginSearchResultDto> plugins = pluginService.findAll().stream()
                        .map(pluginEntitiesConverter::convertToSearchResultDto)
                        .limit(200)
                        .collect(toUnmodifiableList());

                return new ResponseEntity<>(plugins, HttpStatus.OK);
            }

            throw new UserNotFoundException();
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage(Exceptions.USER_NOT_FOUND).asJson(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
            return new ResponseEntity<>(new ResponseMessage(Exceptions.INTERNAL_SERVER_ERROR).asJson(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
