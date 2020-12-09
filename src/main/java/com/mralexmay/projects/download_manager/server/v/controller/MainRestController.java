package com.mralexmay.projects.download_manager.server.v.controller;

import com.google.gson.Gson;
import com.mralexmay.projects.download_manager.server.v.model.download.DownloadEntity;
import com.mralexmay.projects.download_manager.server.v.model.Settings;
import com.mralexmay.projects.download_manager.server.v.model.Token;
import com.mralexmay.projects.download_manager.server.v.model.User;
import com.mralexmay.projects.download_manager.server.v.repository.DownloadsRepository;
import com.mralexmay.projects.download_manager.server.v.repository.SettingsRepository;
import com.mralexmay.projects.download_manager.server.v.repository.TokenRepository;
import com.mralexmay.projects.download_manager.server.v.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                final Token oldToken = user.get().getToken();
                final Token token = Token.createNewToken();

                user.get().setToken(token);

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
    public ResponseEntity<String> addAll(@RequestParam(name = "token", required = true) String tokenJson,
                                         @RequestParam(name = "downloads", required = true) String downloadsJson) {
        try {
            final Gson gson = new Gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create();
            final Map<String, Object> tokenObj = gson.fromJson(tokenJson, Map.class);
            final String token = (String) tokenObj.get("value");

            final Optional<User> user = userRepository.findAll().stream().filter((User u) -> u.getToken().getValue().equals(token)).findAny();

            if (user.isPresent()) {
                final List<DownloadEntity> downloadEntityList = new ArrayList<>();
                final List<Object> downloadsInfos = gson.fromJson(downloadsJson, List.class);

                for (Object downloadInfo : downloadsInfos) {
                    DownloadEntity d = new DownloadEntity();
                    try {
                        d.deserialize((Map<String, Object>) downloadInfo);
                        downloadsRepository.save(d);
                        downloadEntityList.add(d);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                user.get().addAll(downloadEntityList);

                userRepository.save(user.get());
            }

            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/get_all")
    public ResponseEntity<String> getAll(@RequestParam(name = "token", required = true) String tokenJson) {
        try {
            final Gson gson = new Gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create();
            final Map<String, Object> tokenObj = gson.fromJson(tokenJson, Map.class);
            final String token = (String) tokenObj.get("value");

            List<DownloadEntity> downloads = null;
            final Optional<User> user = userRepository.findAll().stream().filter((User u) -> u.getToken().getValue().equals(token)).findAny();

            if (user.isPresent()) {
                downloads = user.get().getDownloadsList();
            }

            final String downloadsListJson = gson.toJson(downloads);

            return new ResponseEntity<String>(downloadsListJson, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
