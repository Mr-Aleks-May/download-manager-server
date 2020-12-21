package com.mralexmay.projects.download_manager.server.api.store.plugin;

import com.google.gson.Gson;
import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.dto.PluginPublishDto;
import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.dto.PluginRemoveDto;
import com.mralexmay.projects.download_manager.server.commons.converter.dto.TokenDto;
import com.mralexmay.projects.download_manager.server.commons.converter.dto.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PluginStoreControllerTest {
    private static final Logger LOGGER = LogManager.getLogger();
    private static List<UserDto> registeredUsers = new ArrayList<>();
    private static List<PluginRemoveData> publishedPlugins = new ArrayList<PluginRemoveData>();

    private static final String SIGNUP_API_PATH = "/api/user/signup";
    private static final String REMOVE_USER_ACCOUNT_API_PATH = "/api/user/remove";

    private static final String LOAD_PLUGIN_API_PATH = "/api/store/plugin/publish";
    private static final String REMOVE_PLUGIN_API_PATH = "/api/store/plugin/remove";
    private static final String FIND_ALL_PLUGINS_WITH_TAGS = "/api/store/plugin/find/by_tags";
    private static final String FIND_PLUGIN_BY_PSID = "/api/store/plugin/find/by_psid";
    private static final String FIND_PLUGIN_BY_PART_OF_NAME = "/api/store/plugin/find/by_part_of_name";
    private static final String FIND_ALL_AVAILABLE_PLUGINS = "/api/store/plugin/find/all";


    /**
     * Main point to Spring Boot Tests
     */
    private MockMvc mockMvc;


    @Autowired
    public PluginStoreControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }


    //#######################
    //#### TEST
    //#######################
    @BeforeAll
    public void setUp() {

    }

    @AfterAll
    public void cleanUp() throws Exception {
        // Remove all created plugins.
        for (PluginRemoveData pluginPublishDto : publishedPlugins) {
            pluginRemoveFromStore(pluginPublishDto.token, pluginPublishDto.pluginPublishDto);
        }

        // Remove all created test users.
        for (UserDto userDto : registeredUsers) {
            try {
                removeUserAccount(userDto);
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
    }

    /**
     * Test plugin publishing.
     *
     * @throws Exception
     */
    @Test
    public void publishPluginTest() throws Exception {
        ResultActions resultActions;

        // SHOULD SUCCEED
        // Create new user with random credential.
        final UserDto userDto = createNewTestUserDto();

        // REGISTER NEW USER
        // Get response from server.
        resultActions = registerUser(userDto).andExpect(status().isOk());

        // Get token from response.
        final String token = getToken(userDto, resultActions);


        // CREATE NEW PLUGIN AND PUBLISH IN STORE
        final PluginPublishDto pluginPublishDto = createNewTestPluginPublishDto();
        resultActions = publishPluginInStore(token, pluginPublishDto).andExpect(status().isOk());


        // TEST API CALL WITH WRONG PARAMETERS
        // WRONG TOKEN
        final String wrongStr = new Random().nextInt() + "";
        resultActions = publishPluginInStore(token + wrongStr, pluginPublishDto).andExpect(status().isNotFound());
    }

    /**
     * Test removing after plugin publishing on server.
     *
     * @throws Exception
     */
    @Test
    public void unpublishPluginTest() throws Exception {
        ResultActions resultActions;

        // SHOULD SUCCEED
        // Create new user with random credential.
        final UserDto userDto = createNewTestUserDto();

        // REGISTER NEW USER
        // Get response from server.
        resultActions = registerUser(userDto).andExpect(status().isOk());

        // Get token from response.
        final String token = getToken(userDto, resultActions);


        // CREATE NEW PLUGIN AND PUBLISH IN STORE
        final PluginPublishDto pluginPublishDto = createNewTestPluginPublishDto();
        resultActions = publishPluginInStore(token, pluginPublishDto).andExpect(status().isOk());


        // CREATE PLUGIN REMOVE DTA OBJECT WITH NECESSARY INFORMATION FOR REMOVE PLUGIN FROM STORE
        final PluginRemoveDto pluginRemoveDto = new PluginRemoveDto()
                .setPluginPSID(pluginPublishDto.getPluginPSID())
                .setName(pluginPublishDto.getName())
                .setVersion(pluginPublishDto.getVersion());
        resultActions = pluginRemoveFromStore(token, pluginRemoveDto).andExpect(status().isOk());


        // TEST API CALL WITH WRONG PARAMETERS
        // WRONG TOKEN
        final String wrongStr = new Random().nextInt() + "";
        resultActions = publishPluginInStore(token + wrongStr, pluginPublishDto).andExpect(status().isNotFound());
    }


    /**
     * Test find plugins by tags.
     *
     * @throws Exception
     */
    @Test
    public void findPluginsByTagsTest() throws Exception {
        ResultActions resultActions;

        // SHOULD SUCCEED
        // Create new user with random credential.
        final UserDto userDto = createNewTestUserDto();

        // REGISTER NEW USER
        // Get response from server.
        resultActions = registerUser(userDto).andExpect(status().isOk());


        // Get token from response.
        final String token = getToken(userDto, resultActions);


        // CREATE NEW PLUGIN AND PUBLISH IN STORE
        final PluginPublishDto pluginPublishDto = createNewTestPluginPublishDto();
        resultActions = publishPluginInStore(token, pluginPublishDto).andExpect(status().isOk());

        // SEARCH PLUGINS THAT CONTAINS ALL OF SPECIFIED TAGS
        final Set<String> tags = new HashSet<>(Arrays.asList(pluginPublishDto.getTags().iterator().next()));
        resultActions = pluginGetPluginsFromStoreByTags(token, tags).andExpect(status().isOk());


        // TEST API CALL WITH WRONG PARAMETERS
        // WRONG TOKEN
        final String wrongStr = new Random().nextInt() + "";
        resultActions = publishPluginInStore(token + wrongStr, pluginPublishDto).andExpect(status().isNotFound());
    }

    /**
     * Search plugins by name (part of name) test.
     *
     * @throws Exception
     */
    @Test
    public void findPluginsByName() throws Exception {
        ResultActions resultActions;

        // SHOULD SUCCEED
        // Create new user with random credential.
        final UserDto userDto = createNewTestUserDto();

        // REGISTER NEW USER
        // Get response from server.
        resultActions = registerUser(userDto).andExpect(status().isOk());


        // Get token from response.
        final String token = getToken(userDto, resultActions);


        // CREATE NEW PLUGIN AND PUBLISH IN STORE
        final PluginPublishDto pluginPublishDto = createNewTestPluginPublishDto();
        resultActions = publishPluginInStore(token, pluginPublishDto).andExpect(status().isOk());

        // SEARCH PLUGINS BY PART OF PLUGIN NAME
        resultActions = pluginGetPluginFromStoreByPartOfName(token, pluginPublishDto.getName().substring(2)).andExpect(status().isOk());


        // TEST API CALL WITH WRONG PARAMETERS
        // WRONG TOKEN
        final String wrongStr = new Random().nextInt() + "";
        resultActions = publishPluginInStore(token + wrongStr, pluginPublishDto).andExpect(status().isNotFound());
    }

    /**
     * Get plugin by PSID from store test.
     *
     * @throws Exception
     */
    @Test
    public void getPluginByPSID() throws Exception {
        ResultActions resultActions;

        // SHOULD SUCCEED
        // Create new user with random credential.
        final UserDto userDto = createNewTestUserDto();

        // REGISTER NEW USER
        // Get response from server.
        resultActions = registerUser(userDto).andExpect(status().isOk());


        // Get token from response.
        final String token = getToken(userDto, resultActions);


        // CREATE NEW PLUGIN AND PUBLISH IN STORE
        final PluginPublishDto pluginPublishDto = createNewTestPluginPublishDto();
        resultActions = publishPluginInStore(token, pluginPublishDto).andExpect(status().isOk());

        // SEARCH PLUGIN BY PSID
        resultActions = pluginGetAllAvailablePlugins(token).andExpect(status().isOk());


        // TEST API CALL WITH WRONG PARAMETERS
        // WRONG TOKEN
        final String wrongStr = new Random().nextInt() + "";
        resultActions = publishPluginInStore(token + wrongStr, pluginPublishDto).andExpect(status().isNotFound());
    }

    /**
     * Return all available plugins (limit 200).
     *
     * @throws Exception
     */
    @Test
    public void getAllPlugin() throws Exception {
        ResultActions resultActions;

        // SHOULD SUCCEED
        // Create new user with random credential.
        final UserDto userDto = createNewTestUserDto();

        // REGISTER NEW USER
        // Get response from server.
        resultActions = registerUser(userDto).andExpect(status().isOk());


        // Get token from response.
        final String token = getToken(userDto, resultActions);


        // CREATE NEW PLUGIN AND PUBLISH IN STORE
        final PluginPublishDto pluginPublishDto = createNewTestPluginPublishDto();
        resultActions = publishPluginInStore(token, pluginPublishDto).andExpect(status().isOk());

        // SEARCH PLUGIN BY PSID
        resultActions = pluginGetPluginsFromStoreByPSID(token, pluginPublishDto.getPluginPSID()).andExpect(status().isOk());


        // TEST API CALL WITH WRONG PARAMETERS
        // WRONG TOKEN
        final String wrongStr = new Random().nextInt() + "";
        resultActions = publishPluginInStore(token + wrongStr, pluginPublishDto).andExpect(status().isNotFound());
    }


    //###################
    //#### BASIC API CALLS
    //###################
    public UserDto createNewTestUserDto() {
        final UserDto userDto = new UserDto()
                .setLogin(String.format("TEST-USER-Login-%s", new Random().nextLong()))
                .setPassword(String.format("TEST-USER-Password-%s", new Random().nextLong()));
        registeredUsers.add(userDto);
        return userDto;
    }

    public PluginPublishDto createNewTestPluginPublishDto() {
        final PluginPublishDto pluginPublishDto = new PluginPublishDto()
                .setPluginPSID("TEST-PLUGIN-PSID" + new Random().nextInt())
                .setName("TEST-PLUGIN-Name" + new Random().nextInt())
                .setDescription("TEST-PLUGIN-Description" + new Random().nextInt())
                .setVersion("TEST-PLUGIN-Version" + new Random().nextInt())
                .setTags(new HashSet<>(Arrays.asList("TEST-PLUGIN-Tag" + new Random().nextInt())))
                .setContent(new Byte[]{1});
        return pluginPublishDto;
    }


    public ResultActions registerUser(String login, String password) throws Exception {
        final Gson gson = new Gson();

        final UserDto userDto = new UserDto()
                .setLogin(login)
                .setPassword(password);

        // Register new user.
        return mockMvc.perform(post(SIGNUP_API_PATH)
                .queryParam("login", userDto.getLogin())
                .queryParam("password", userDto.getPassword()));
    }

    public ResultActions registerUser(UserDto userDto) throws Exception {
        // Register new user.
        return registerUser(userDto.getLogin(), userDto.getPassword());
    }

    public String getToken(UserDto userDto, ResultActions resultActions) throws UnsupportedEncodingException {
        final MockHttpServletResponse response = resultActions.andReturn().getResponse();
        final String responseBody = response.getContentAsString();
        final TokenDto tokenDto = new Gson().fromJson(responseBody, TokenDto.class);
        final String token = tokenDto.getToken();
        userDto.setToken(tokenDto);

        return token;
    }

    public ResultActions removeUserAccount(UserDto userDto) throws Exception {
        return mockMvc.perform(delete(REMOVE_USER_ACCOUNT_API_PATH)
                .queryParam("token", userDto.getToken().getToken()));
    }

    public ResultActions publishPluginInStore(String token, PluginPublishDto plugin) throws Exception {
        final Gson gson = new Gson();

        // Serialize object to json.
        final String pluginDtoJson = gson.toJson(plugin);

        final PluginRemoveDto pluginRemoveDto = new PluginRemoveDto()
                .setPluginPSID(plugin.getPluginPSID())
                .setName(plugin.getName())
                .setVersion(plugin.getVersion());
        publishedPlugins.add(new PluginRemoveData(token, pluginRemoveDto));

        // Send serialized data to server.
        return mockMvc.perform(post(LOAD_PLUGIN_API_PATH)
                .queryParam("token", token)
                .content(pluginDtoJson)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public ResultActions pluginRemoveFromStore(String token, PluginRemoveDto plugin) throws Exception {
        final Gson gson = new Gson();

        // Serialize object to json.
        final String pluginDtoJson = gson.toJson(plugin);

        // Send serialized data to server and return response.
        return mockMvc.perform(post(REMOVE_PLUGIN_API_PATH)
                .queryParam("token", token)
                .content(pluginDtoJson)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public ResultActions pluginGetPluginsFromStoreByTags(String token, Set<String> tags) throws Exception {
        final Gson gson = new Gson();

        // Serialize object to json.
        final String pluginDtoJson = gson.toJson(tags);

        // Send serialized data to server and return response.
        return mockMvc.perform(get(FIND_ALL_PLUGINS_WITH_TAGS)
                .queryParam("token", token)
                .content(pluginDtoJson)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public ResultActions pluginGetPluginsFromStoreByPSID(String token, String PSID) throws Exception {
        // Send serialized data to server and return response.
        return mockMvc.perform(get(FIND_PLUGIN_BY_PSID)
                .queryParam("token", token)
                .queryParam("psid", PSID)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public ResultActions pluginGetPluginFromStoreByPartOfName(String token, String partOfName) throws Exception {
        // Send serialized data to server and return response.
        return mockMvc.perform(get(FIND_PLUGIN_BY_PART_OF_NAME)
                .queryParam("token", token)
                .queryParam("part_of_name", partOfName)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public ResultActions pluginGetAllAvailablePlugins(String token) throws Exception {
        // Send serialized data to server and return response.
        return mockMvc.perform(get(FIND_ALL_AVAILABLE_PLUGINS)
                .queryParam("token", token)
                .contentType(MediaType.APPLICATION_JSON));
    }


    private class PluginRemoveData {
        public PluginRemoveDto pluginPublishDto;
        public String token;

        public PluginRemoveData(String token, PluginRemoveDto plugin) {
            this.token = token;
            pluginPublishDto = plugin;
        }
    }
}

//    public void testAndCleanup(Runnable r) {
//
//    }
