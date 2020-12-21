package com.mralexmay.projects.download_manager.server.api.user.service;

import com.mralexmay.projects.download_manager.server.commons.model.Token;
import com.mralexmay.projects.download_manager.server.commons.model.User;
import com.mralexmay.projects.download_manager.server.api.user.converter.UserEntitiesConverter;
import com.mralexmay.projects.download_manager.server.api.user.converter.dto.CategoryDto;
import com.mralexmay.projects.download_manager.server.api.user.converter.dto.DownloadDto;
import com.mralexmay.projects.download_manager.server.api.user.model.Category;
import com.mralexmay.projects.download_manager.server.api.user.model.Download;
import com.mralexmay.projects.download_manager.server.api.user.model.UserPlugin;
import com.mralexmay.projects.download_manager.server.api.user.repository.*;
import com.mralexmay.projects.download_manager.server.commons.exception.UserAlreadyExistsException;
import com.mralexmay.projects.download_manager.server.commons.exception.UserNotFoundException;
import com.mralexmay.projects.download_manager.server.commons.exception.WrongUserCredentialException;
import com.mralexmay.projects.download_manager.server.api.user.exception.CategoryDownloadsCountBelowZeroException;
import com.mralexmay.projects.download_manager.server.commons.converter.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {
    // Fields.
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final DownloadsRepository downloadRepository;
    private final CategoriesRepository categoryRepository;
    private final UserPluginsRepository userPluginsRepository;
    private final UserEntitiesConverter userEntitiesConverter;


    // Inject dependencies.
    public UserServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, DownloadsRepository downloadRepository, CategoriesRepository categoryRepository, UserPluginsRepository userPluginsRepository, UserEntitiesConverter userEntitiesConverter) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.downloadRepository = downloadRepository;
        this.categoryRepository = categoryRepository;
        this.userPluginsRepository = userPluginsRepository;
        this.userEntitiesConverter = userEntitiesConverter;
    }

    /**
     * Search user by data specified in user dto.
     *
     * @param userDto user dto object.
     * @return user if exists, or empty.
     */
    @Override
    public User findUserBy(UserDto userDto) throws WrongUserCredentialException, UserNotFoundException {
        // Try find user by login.
        Optional<User> userByLogin = this.findUserByLogin(userDto.getLogin());

        // Check if user with specified login exist.
        if (userByLogin.isPresent()) {
            // If user exist, check if password match.
            if (userDto.getPassword().equals(userByLogin.get().getPassword())) {
                // If password and login match, return user object.
                return userByLogin.get();
            }

            // If password not match, throw WrongUserCredentialException.
            throw new WrongUserCredentialException();
        }

        // If user with specified login not found, throw UserNotFoundException.
        throw new UserNotFoundException();
    }


    /**
     * Search user by id.
     *
     * @param id user id.
     * @return user with specified identifier, or empty.
     */
    @Override
    public Optional<User> findUserById(Long id) {
        // Find user by id.
        return userRepository.findAll().stream()
                .filter((User user) -> id.equals(user.getId()))
                .findAny();
    }


    /**
     * Search user by token.
     *
     * @param token user token.
     * @return user with specified token, or empty.
     */
    @Override
    public Optional<User> findUserByToken(String token) {
        // Find user by token.
        return userRepository.findAll().stream()
                .filter((User u) -> u.getToken().getValue().equals(token))
                .findAny();
    }


    /**
     * Search user by login.
     *
     * @param login user login.
     * @return user with specified login, or empty.
     */
    @Override
    public Optional<User> findUserDtoByLogin(String login) {
        // Search user by login.
        // If find, return.
        return userRepository.findAll().stream()
                .filter((User u) -> login.equals(u.getLogin()))
                .findAny();
    }

    /**
     * Search user by login.
     *
     * @param login user login.
     * @return user with specified login, or empty.
     */
    @Override
    public Optional<User> findUserByLogin(String login) {
        // Search user by login.
        // If find, return.
        return userRepository.findAll().stream()
                .filter((User u) -> login.equals(u.getLogin()))
                .findAny();
    }

    /**
     * Return list of all users wrapped in user dto.
     *
     * @return all users converted to dto.
     */
    @Override
    public List<UserDto> findDtoAll() {
        // Return all user dto object.
        return userRepository.findAll().stream()
                .map(this.userEntitiesConverter::convertToDto)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Return list of all registered users.
     *
     * @return all registered users.
     */
    @Override
    public List<User> findAll() {
        // Return all users.
        return userRepository.findAll().stream()
                .collect(Collectors.toUnmodifiableList());
    }


    /**
     * Create new user with specified login and password.
     * If user exists throws UserAlreadyExistsException.
     *
     * @param userDto user data transfer object.
     * @return created user object.
     * @throws UserAlreadyExistsException throws if user exists.
     */
    @Override
    public User createUser(UserDto userDto) throws UserAlreadyExistsException {
        // Search if is user already exists.
        Optional<User> existingUser = this.findUserDtoByLogin(userDto.getLogin());
        // If user exists throws UserAlreadyExistsException.
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(existingUser.get().getId());
        }

        // Else, generate new access token for user.
        Token token = Token.generateNew();
        // Save this token in DB.
        tokenRepository.save(token);

        // Initialize new user.
        userDto.setId(null);
        userDto.setToken(userEntitiesConverter.convertToDto(token));
        userDto.setDownloads(new ArrayList<>());

        // Save new user in DB.
        var newUser = userEntitiesConverter.convertFromDto(userDto);
        var createdUser = this.userRepository.save(newUser);

        // Return new user object.
        return createdUser;
    }

    /**
     * Update/save user state in DB.
     *
     * @param userDto user to update.
     * @return user object.
     */
    @Override
    public User updateUser(UserDto userDto) {
        // Update and save user state.
        User userUpdate = this.userEntitiesConverter.convertFromDto(userDto);
        User updatedUser = this.userRepository.save(userUpdate);

        return updatedUser;
    }

    /**
     * Delete user by id.
     *
     * @param id user id.
     * @return
     */
    @Override
    public boolean deleteUserById(Long id) {
        // Remove user with id.
        this.userRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean deleteUser(User user) throws CategoryDownloadsCountBelowZeroException {
        for (Download download : user.getDownloads()) {
            this.deleteDownload(user, download);
        }

        for (UserPlugin userPlugin : user.getUserPlugins()) {
            for (Category category : userPlugin.getCategories()) {
                this.removeCategory(user, category);
            }

            this.removeUserPlugin(user, userPlugin);
        }

        // Delete user related information from DB.
        this.userRepository.delete(user);

        return true;
    }

    private void removeUserPlugin(User user, UserPlugin userPlugin) {
        // Delete user plugin and update user state.
        user.remove(userPlugin);
        userPluginsRepository.delete(userPlugin);
        userRepository.save(user);
    }

    private void removeCategory(User user, Category category) {
        // Delete download category and update user state.
        categoryRepository.delete(category);
        userRepository.save(user);
    }


    /**
     * Search download specified dsid in user downloads list.
     *
     * @param user user object.
     * @param dsid download serializable unique identifier.
     * @return download.
     */
    @Override
    public Optional<Download> findDownload(User user, String dsid) {
        // Find and return download in user downloads list by download unique serialization identifier (dsid).
        return user.getDownloads().stream()
                .filter(d -> dsid.equals(d.getDownloadDSID()))
                .findAny();
    }

    /**
     * Search download specified dsid in user downloads list.
     *
     * @param user user object.
     * @return download list.
     */
    @Override
    public List<Download> findAllDownload(User user) {
        // Find and return download list in user downloads list.
        return user.getDownloads();
    }

    /**
     * Add download to user download list.
     * Also, if plugin to which belongings download not exists, create it.
     * Otherwise add category to existing plugin from user plugins list.
     *
     * @param toUser
     * @param downloadDto
     * @return if download added successful.
     */
    @Override
    public boolean addDownload(User toUser, DownloadDto downloadDto) {
        // Check if download with same dsid already exists in user downloads list.
        if (toUser.getDownloads().stream()
                .filter(d -> downloadDto.getDSID().equals(d.getDownloadDSID()))
                .findAny()
                .isPresent()) {
            // If download exists, return false.
            // Do not add to user download list again.
            return false;
        }

        // Get category information from download.
        CategoryDto categoryDto = downloadDto.getCategoryDto();
        // Check if category belongs to some plugin in user plugins list.
        Optional<UserPlugin> plugin = toUser.getUserPlugins().stream()
                .filter(p -> p.getPluginPSID().equals(categoryDto.getPluginPSID()))
                .findAny();

        // Convert download to dto.
        Download download = userEntitiesConverter.convertFromDto(downloadDto);

        // Check plugin which belongs this category is found.
        if (plugin.isPresent()) {
            // If it`s so, check if same category already has been added to plugins categories.
            Optional<Category> category = plugin.get().getCategories().stream()
                    .filter(c -> categoryDto.getCategoryCSID().equals(c.getCategoryCSID()))
                    .findAny();

            // Check if category found.
            if (category.isPresent()) {
                // Increase downloads count in category.
                category.get().increaseCount();
                // Save category state in DB.
                categoryRepository.save(category.get());

                // Set category to download.
                download.setCategory(category.get());
            } else {
                // Create new category (new category created in download after converting from dto).
                // Get category from download.
                // Persist category.
                categoryRepository.save(download.getCategory());
            }

            // Save download state in DB.
            downloadRepository.save(download);
        }
        // If plugin not found (not exists).
        else {
            // Get category from download (new category created automatically after converting from download dto to download).
            Category category = download.getCategory();
            // Increase downloads count in category.
            category.increaseCount();

            // Create new plugin.
            UserPlugin newPlugin = new UserPlugin(new ArrayList<>());
            // Set plugin unique indentifier.
            newPlugin.setPluginPSID(category.getPluginPSID());
            // Add category to plugin.
            newPlugin.add(category);

            // Persist category, plugin, download.
            categoryRepository.save(category);
            userPluginsRepository.save(newPlugin);
            downloadRepository.save(download);

            // Add new plugin to user plugins list.
            toUser.add(newPlugin);
        }

        // Add download to user downloads list.
        toUser.add(download);
        // Save user state.
        userRepository.save(toUser);

        // Everything is fine, return true.
        return true;
    }

    @Override
    public boolean addDownloadsList(User toUser, List<DownloadDto> downloadDtoList) {
        // Have all the downloads been added?
        boolean isAllAdded = true;

        // Step through all elements in download dto list,
        for (var downloadDto : downloadDtoList) {
            // Check if a download has been added?
            if (!addDownload(toUser, downloadDto)) {
                // If no, set isAllAdded to false.
                isAllAdded = false;
            }
        }

        // If all downloads were added to user downloads list return true; Otherwise false.
        return isAllAdded;
    }


    /**
     * Remove download from user download list.
     * Also, remove decrease category downloads counter by 1.
     * If category download count reach 0, category will be deleted.
     * If plugin categories count reach 0, plugin will be deleted.
     *
     * @param fromUser user object.
     * @param dsid     download unique identifier in user downloads list.
     * @return if download removed successfully.
     */
    @Override
    public boolean deleteDownload(User fromUser, String dsid) throws CategoryDownloadsCountBelowZeroException {
        // Find download by dsid.
        Optional<Download> download = fromUser.getDownloads().stream()
                .filter(d -> dsid.equals(d.getDownloadDSID()))
                .findAny();

        // Check if download with specified dsid exists in user downloads list.
        if (download.isPresent()) {
            // Return remove status.
            return deleteDownload(fromUser, download.get());
        }

        return false;
    }

    /**
     * Remove download from user download list.
     * Also, remove decrease category counter by 1.
     * If category download count reach 0, category will be deleted.
     * If plugin categories count reach 0, plugin will be deleted.
     *
     * @param fromUser user object.
     * @param download download from user downloads list.
     * @return true if download removed successfully.
     */
    @Override
    public boolean deleteDownload(User fromUser, Download download) throws CategoryDownloadsCountBelowZeroException {
        // Get category from download.
        Category category = download.getCategory();

        // Has the download been successfully removed from the user download list?
        boolean isRemoved = fromUser.remove(download);
        if (isRemoved) {
            // Decrease category downloads count by 1.
            category.decreaseCount();

            // Check, if category downloads count bigger or equal zero.
            if (category.getCount() < 0) {
                // If downloads count smaller than zero, throw exception.
                throw new CategoryDownloadsCountBelowZeroException();
            }
            // If downloads count == 0, remove category.
            if (category.getCount() == 0) {
                // Search plugin of this category.
                Optional<UserPlugin> plugin = fromUser.getUserPlugins().stream()
                        .filter(p -> category.getPluginPSID().equals(p.getPluginPSID()))
                        .findAny();

                // Check if plugin with specified pluginPSID (plugin unique identifier) exists.
                if (plugin.isPresent()) {
                    // If so, remove category from plugin categories.
                    plugin.get().remove(category);

                    // Check plugins categories count.
                    if (plugin.get().getCategoriesCount() == 0) {
                        // If plugin categories empty => remove plugin from user plugins list.
                        fromUser.remove(plugin.get());
                    }
                }
            }

            // Save user state.
            downloadRepository.flush();
            userRepository.save(fromUser);
            userRepository.flush();
        }

        // Return remove status.
        return isRemoved;
    }

    @Override
    public boolean removeDownloadsList(User fromUser, List<String> downloadDSIDDtoList) throws CategoryDownloadsCountBelowZeroException {
        // Have all the downloads been added?
        boolean isAllRemoved = true;

        // Step through all elements in download dto list,
        for (var downloadDSIDDto : downloadDSIDDtoList) {
            // Check if a download has been removed?
            if (!deleteDownload(fromUser, downloadDSIDDto)) {
                // If no, set isAllRemoved to false.
                isAllRemoved = false;
            }
        }

        // If all downloads were removed from user downloads list return true; Otherwise false.
        return isAllRemoved;
    }
}
