package com.mralexmay.projects.download_manager.server.v4.controller;

import com.google.gson.Gson;
import com.mralexmay.projects.download_manager.server.v4.model.DownloadEntity;
import com.mralexmay.projects.download_manager.server.v4.model.Settings;
import com.mralexmay.projects.download_manager.server.v4.model.Token;
import com.mralexmay.projects.download_manager.server.v4.model.User;
import com.mralexmay.projects.download_manager.server.v4.repository.DownloadsRepository;
import com.mralexmay.projects.download_manager.server.v4.repository.SettingsRepository;
import com.mralexmay.projects.download_manager.server.v4.repository.TokenRepository;
import com.mralexmay.projects.download_manager.server.v4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MainRestController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    SettingsRepository settingsRepository;
    @Autowired
    DownloadsRepository downloadsRepository;


    @GetMapping("/test")
    public ResponseEntity<String> test() {
        try {
            return new ResponseEntity<>("Welcome", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestParam(required = true) String login,
                                         @RequestParam(required = true) String password) {
        try {
            final Optional<User> user = userRepository.findAll().stream().filter((User u) -> u.getLogin().equals(login)).findAny();

            if (user.isPresent() && password.equals(user.get().getPassword())) {
                final Token oldToken = user.get().getAccessToken();
                final Token token = Token.createNewToken();

                user.get().setAccessToken(token);

                tokenRepository.save(token);
                userRepository.save(user.get());

                tokenRepository.delete(oldToken);

                final Gson gson = new Gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create();
                final String response = gson.toJson(token);

                return new ResponseEntity<String>(response, HttpStatus.OK);
            }

            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestParam(required = true) String login,
                                         @RequestParam(required = true) String password) {
        try {
            final Optional<User> user = userRepository.findAll().stream().filter((User u) -> u.getLogin().equals(login)).findAny();

            if (!user.isPresent()) {
                final Token token = Token.createNewToken();
                final Settings settings = new Settings();

                final User newUser = new User(login, password, token, settings, new ArrayList<>());

                tokenRepository.save(token);
                settingsRepository.save(settings);
                userRepository.save(newUser);

                final Gson gson = new Gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create();
                final String response = gson.toJson(token);

                return new ResponseEntity<String>(response, HttpStatus.OK);
            }

            return new ResponseEntity<>("User already exists!", HttpStatus.CONFLICT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/download/add_all")
    public ResponseEntity<String> addAll(@RequestParam(required = true) String token,
                                         @RequestParam(required = true) List<com.mraleksmay.projects.download_manager.common.model.download.Download> downloads) {
        try {
            final Gson gson = new Gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create();
            final String response = gson.toJson(token);

            return new ResponseEntity<String>(response, HttpStatus.OK);

//            return new ResponseEntity<>("User already exists!", HttpStatus.CONFLICT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
