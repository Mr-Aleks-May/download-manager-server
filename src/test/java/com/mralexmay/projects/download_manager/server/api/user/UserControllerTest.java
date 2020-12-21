package com.mralexmay.projects.download_manager.server.api.user;

import com.google.gson.Gson;
import com.mralexmay.projects.download_manager.server.commons.converter.dto.TokenDto;
import com.mralexmay.projects.download_manager.server.commons.converter.dto.UserDto;
import com.mralexmay.projects.download_manager.server.api.user.converter.dto.CategoryDto;
import com.mralexmay.projects.download_manager.server.api.user.converter.dto.DownloadDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test Main API controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    private static final String SIGNUP_API_PATH = "/api/user/signup";
    private static final String SIGNIN_API_PATH = "/api/user/signin";
    private static final String ADD_DOWNLOAD_API_PATH = "/api/user/download/add";
    private static final String ADD_DOWNLOADS_LIST_API_PATH = "/api/user/download/add/list";
    private static final String DELETE_DOWNLOAD_API_PATH = "/api/user/download/delete";
    private static final String DELETE_DOWNLOADS_LIST_API_PATH = "/api/user/download/delete/list";


    /**
     * Main point to Spring MVC tests support.
     */
    private MockMvc mockMvc;


    /**
     * Inject dependencies.
     *
     * @param mockMvc
     */
    @Autowired
    public UserControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }


    /**
     * Init values before tests.
     *
     * @throws Exception
     */
    @BeforeAll
    public static void setUp() throws Exception {
    }

    /**
     * Test greetings.
     *
     * @throws Exception
     */
    @Test
    public void welcomeTest() throws Exception {
        mockMvc.perform(get("/api/user/welcome"))
                .andExpect(status().isOk());
    }

    /**
     * Test registration
     *
     * @throws Exception
     */
    @Test
    public void signUpTest() throws Exception {
        // SHOULD WORK FINE
        // Create new user with random credential.
        UserDto userDto = new UserDto().setLogin("Test User " + new Random().nextInt()).setPassword("testpassword1");

        // Register new user.
        ResultActions resultActions;
        resultActions = mockMvc.perform(post(SIGNUP_API_PATH)
                .queryParam("login", userDto.getLogin())
                .queryParam("password", userDto.getPassword()))
                .andExpect(status().isOk());

        // Get response from server.
        MockHttpServletResponse response = resultActions.andReturn().getResponse();

        // Get token from response.
        String responseBody = response.getContentAsString();
        TokenDto tokenDto = new Gson().fromJson(responseBody, TokenDto.class);
        String token = tokenDto.getToken();

        // Check if token exists.
        if (token == null) {
            throw new RuntimeException("Token is null");
        }

        // PASS TEST IF USER NOT CREATED
        // Try register user with same credential.
        mockMvc.perform(post(SIGNUP_API_PATH)
                .queryParam("login", userDto.getLogin())
                .queryParam("password", userDto.getPassword()))
                .andExpect(status().isNotAcceptable());
    }

    /**
     * Test user login.
     *
     * @throws Exception
     */
    @Test
    public void signInTest() throws Exception {
        // SHOULD WORK FINE
        // Create new user with random credential.
        UserDto userDto = new UserDto().setLogin("Test User " + new Random().nextInt()).setPassword("testpassword1");

        ResultActions resultActions;
        // Register new user.
        resultActions = mockMvc.perform(post(SIGNUP_API_PATH)
                .queryParam("login", userDto.getLogin())
                .queryParam("password", userDto.getPassword()))
                .andExpect(status().isOk());

        // Get response from server.
        MockHttpServletResponse responseSignUp = resultActions.andReturn().getResponse();

        // Get token from response.
        String responseBodySignUp = responseSignUp.getContentAsString();
        TokenDto tokenDtoSignUp = new Gson().fromJson(responseBodySignUp, TokenDto.class);
        String tokenSignUp = tokenDtoSignUp.getToken();

        // Check if token exists.
        if (tokenSignUp == null) {
            throw new RuntimeException("Token is null");
        }

        // Login user.
        resultActions = mockMvc.perform(post(SIGNIN_API_PATH)
                .queryParam("login", userDto.getLogin())
                .queryParam("password", userDto.getPassword()))
                .andExpect(status().isOk());


        // Get response from server.
        MockHttpServletResponse responseSignIn = resultActions.andReturn().getResponse();

        // Get token from response.
        String responseBodySignIn;
        TokenDto tokenDtoSignIn;
        String tokenSignIn;

        responseBodySignIn = responseSignIn.getContentAsString();
        tokenDtoSignIn = new Gson().fromJson(responseBodySignIn, TokenDto.class);
        tokenSignIn = tokenDtoSignIn.getToken();

        // Check if token exists.
        if (tokenSignIn == null) {
            throw new RuntimeException("Token is null");
        }


        // TRY LOGIN USER WITH WRONG CREDENTIAL
        // Login user.
        resultActions = mockMvc.perform(
                post(SIGNIN_API_PATH)
                        .queryParam("login", userDto.getLogin() + new Random().nextInt())
                        .queryParam("password", userDto.getPassword()))
                .andExpect(status().isNotFound());
    }

    /**
     * Add new download to user downloads list.
     *
     * @throws Exception
     */
    @Test
    public void addDownloadTest() throws Exception {

        // SHOULD WORK FINE
        // Create new user with random credential.
        UserDto userDto = new UserDto().setLogin("Test User " + new Random().nextInt()).setPassword("testpassword1");

        // Register new user.
        ResultActions resultActions;
        resultActions = mockMvc.perform(post(SIGNUP_API_PATH)
                .queryParam("login", userDto.getLogin())
                .queryParam("password", userDto.getPassword()))
                .andExpect(status().isOk());

        // Get response from server.
        MockHttpServletResponse response = resultActions.andReturn().getResponse();

        // Get token from response.
        String responseBody = response.getContentAsString();
        TokenDto tokenDto = new Gson().fromJson(responseBody, TokenDto.class);
        String token = tokenDto.getToken();

        // Check if token exists.
        if (token == null) {
            throw new RuntimeException("Token is null");
        }

        // Create new category and download.
        CategoryDto categoryDto = new CategoryDto()
                .setCategoryCSID("test")
                .setName("test");
        DownloadDto downloadDto = new DownloadDto()
                .setDSID("test")
                .setFileName("test")
                .setExtension("test")
                .setTmpDir("test")
                .setOutputDir("test")
                .setUrl("test@test.com")
                .setCategoryDto(categoryDto)
                .setCreationTime(System.currentTimeMillis());

        // Create gson object.
        Gson gson = new Gson();
        // Serialize download dto.
        String downloadDtoJson = gson.toJson(downloadDto);

        // Send request to server.
        resultActions = mockMvc.perform(post(ADD_DOWNLOAD_API_PATH)
                .queryParam("token", token)
                .content(downloadDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Add list of downloads test.
     *
     * @throws Exception
     */
    @Test
    public void addDownloadsListTest() throws Exception {

        // SHOULD WORK FINE
        // Create new user with random credential.
        UserDto userDto = new UserDto().setLogin("Test User " + new Random().nextInt()).setPassword("testpassword1");

        // Register new user.
        ResultActions resultActions;
        resultActions = mockMvc.perform(post(SIGNUP_API_PATH)
                .queryParam("login", userDto.getLogin())
                .queryParam("password", userDto.getPassword()))
                .andExpect(status().isOk());

        // Get response from server.
        MockHttpServletResponse response = resultActions.andReturn().getResponse();

        // Get token from response.
        String responseBody = response.getContentAsString();
        TokenDto tokenDto = new Gson().fromJson(responseBody, TokenDto.class);
        String token = tokenDto.getToken();

        // Check if token exists.
        if (token == null) {
            throw new RuntimeException("Token is null");
        }

        // Create new list for downloads.
        List<DownloadDto> downloadsList = new ArrayList<>();
        // Set the number of generated test downloads.
        int downloadsCount = 10;

        // Generate test download entity.
        for (int i = 0; i < downloadsCount; i++) {
            // Create new category and download.
            CategoryDto categoryDto = new CategoryDto()
                    .setCategoryCSID("test")
                    .setName("test")
                    .setPluginPSID("test");
            // Create download with random value.
            DownloadDto downloadDto = new DownloadDto()
                    .setDSID("test" + new Random().nextInt())
                    .setFileName("test" + new Random().nextInt())
                    .setExtension("test" + new Random().nextInt())
                    .setTmpDir("test" + new Random().nextInt())
                    .setOutputDir("test" + new Random().nextInt())
                    .setUrl("test@test.com" + new Random().nextInt())
                    .setCategoryDto(categoryDto)
                    .setCreationTime(System.currentTimeMillis());

            // Add test download to list.
            downloadsList.add(downloadDto);
        }

        // Create gson object.
        Gson gson = new Gson();
        // Serialize downloads list.
        String downloadsListDtoJson = gson.toJson(downloadsList);

        // Send request to server.
        resultActions = mockMvc.perform(post(ADD_DOWNLOADS_LIST_API_PATH)
                .queryParam("token", token)
                .content(downloadsListDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    /**
     * Test deleting download from user.
     *
     * @throws Exception
     */
    @Test
    public void deleteDownloadTest() throws Exception {
        // SHOULD WORK FINE
        // Create new user with random credential.
        UserDto userDto = new UserDto().setLogin("Test User " + new Random().nextInt()).setPassword("testpassword1");

        // Register new user.
        ResultActions resultActions;
        resultActions = mockMvc.perform(post(SIGNUP_API_PATH)
                .queryParam("login", userDto.getLogin())
                .queryParam("password", userDto.getPassword()))
                .andExpect(status().isOk());

        // Get response from server.
        MockHttpServletResponse response = resultActions.andReturn().getResponse();

        // Get token from response.
        String responseBody = response.getContentAsString();
        TokenDto tokenDto = new Gson().fromJson(responseBody, TokenDto.class);
        String token = tokenDto.getToken();

        // Check if token exists.
        if (token == null) {
            throw new RuntimeException("Token is null");
        }

        // Create new category and download.
        CategoryDto categoryDto = new CategoryDto()
                .setCategoryCSID("test")
                .setName("test")
                .setPluginPSID("test");
        DownloadDto downloadDto = new DownloadDto()
                .setDSID("test")
                .setFileName("test")
                .setExtension("test")
                .setTmpDir("test")
                .setOutputDir("test")
                .setUrl("test@test.com")
                .setCategoryDto(categoryDto)
                .setCreationTime(System.currentTimeMillis());

        // Create gson object.
        Gson gson = new Gson();
        // Serialize download dto.
        String downloadDtoJson = gson.toJson(downloadDto);

        // Send request to server and test response.
        resultActions = mockMvc.perform(post(ADD_DOWNLOAD_API_PATH)
                .queryParam("token", token)
                .content(downloadDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        // Send request to server and test response.
        resultActions = mockMvc.perform(delete(DELETE_DOWNLOAD_API_PATH)
                .queryParam("token", token)
                .queryParam("dsid", downloadDto.getDSID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));


        // SHOULD FAIL
        // Send request to server and test response.
        resultActions = mockMvc.perform(delete(DELETE_DOWNLOAD_API_PATH)
                .queryParam("token", token)
                .queryParam("dsid", downloadDto.getDSID() + new Random().nextInt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteDownloadsListTest() throws Exception {

        // SHOULD WORK FINE
        // Create new user with random credential.
        UserDto userDto = new UserDto().setLogin("Test User " + new Random().nextInt()).setPassword("testpassword1");

        // Register new user.
        ResultActions resultActions;
        resultActions = mockMvc.perform(post(SIGNUP_API_PATH)
                .queryParam("login", userDto.getLogin())
                .queryParam("password", userDto.getPassword()))
                .andExpect(status().isOk());

        // Get response from server.
        MockHttpServletResponse response = resultActions.andReturn().getResponse();

        // Get token from response.
        String responseBody = response.getContentAsString();
        TokenDto tokenDto = new Gson().fromJson(responseBody, TokenDto.class);
        String token = tokenDto.getToken();

        // Check if token exists.
        if (token == null) {
            throw new RuntimeException("Token is null");
        }

        // Create new list for downloads.
        List<DownloadDto> downloadsList = new ArrayList<>();
        // Set the number of generated test downloads.
        int downloadsCount = 10;

        // Generate test download entity.
        for (int i = 0; i < downloadsCount; i++) {
            // Create new category and download.
            CategoryDto categoryDto = new CategoryDto()
                    .setCategoryCSID("test")
                    .setName("test")
                    .setPluginPSID("test");
            // Create download with random value.
            DownloadDto downloadDto = new DownloadDto()
                    .setDSID("test" + new Random().nextInt())
                    .setFileName("test" + new Random().nextInt())
                    .setExtension("test" + new Random().nextInt())
                    .setTmpDir("test" + new Random().nextInt())
                    .setOutputDir("test" + new Random().nextInt())
                    .setUrl("test@test.com" + new Random().nextInt())
                    .setCategoryDto(categoryDto)
                    .setCreationTime(System.currentTimeMillis());

            // Add test download to list.
            downloadsList.add(downloadDto);
        }

        // Create gson object.
        Gson gson = new Gson();
        // Serialize downloads list.
        String addDownloadsListDtoJson = gson.toJson(downloadsList);

        // Send request to server.
        resultActions = mockMvc.perform(post(ADD_DOWNLOADS_LIST_API_PATH)
                .queryParam("token", token)
                .content(addDownloadsListDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Retrieve downloads unique id and collect to list.
        List<String> dsidList = downloadsList.stream()
                .map(DownloadDto::getDSID)
                .collect(Collectors.toUnmodifiableList());
        // Serialize downloads unique id list.
        String deleteDownloadsListDtoJson = gson.toJson(dsidList);

        // Send request to server and test response.
        resultActions = mockMvc.perform(delete(DELETE_DOWNLOADS_LIST_API_PATH)
                .queryParam("token", token)
                .content(deleteDownloadsListDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
