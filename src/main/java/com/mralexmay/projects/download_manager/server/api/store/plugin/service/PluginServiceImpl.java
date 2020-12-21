package com.mralexmay.projects.download_manager.server.api.store.plugin.service;

import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.PluginEntitiesConverter;
import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.dto.PluginPublishDto;
import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.dto.PluginRemoveDto;
import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.dto.PluginSearchResultDto;
import com.mralexmay.projects.download_manager.server.api.store.plugin.exception.PluginNotPublishException;
import com.mralexmay.projects.download_manager.server.api.store.plugin.model.Plugin;
import com.mralexmay.projects.download_manager.server.api.store.plugin.repository.PluginRepository;
import com.mralexmay.projects.download_manager.server.commons.model.User;
import com.mralexmay.projects.download_manager.server.commons.util.BFileWorker;
import com.mralexmay.projects.download_manager.server.commons.util.FileUtil;
import com.mralexmay.projects.download_manager.server.commons.util.FileWorker;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toUnmodifiableList;

@Component
public class PluginServiceImpl implements PluginService {
    private final PluginRepository pluginRepository;
    private final PluginEntitiesConverter pluginEntitiesConverter;
    private static String pluginsDirStr = "./published_plugins";
    private static String digitalSign = "2473118065";
    private FileWorker fileWorker;


    {
        fileWorker = new BFileWorker();
    }


    // Inject dependencies.
    public PluginServiceImpl(PluginRepository pluginRepository, PluginEntitiesConverter pluginEntitiesConverter) {
        this.pluginRepository = pluginRepository;
        this.pluginEntitiesConverter = pluginEntitiesConverter;
    }


    /**
     * Search plugin with specified plugin unique identifier.
     *
     * @param pluginPSID plugin unique identifier.
     * @return plugin with specified unique identifier.
     */
    @Override
    public Optional<Plugin> findByPSID(String pluginPSID) {
        return pluginRepository.findAll().stream()
                .filter(p -> pluginPSID.equals(p.getPluginPSID()))
                .findAny();
    }

    /**
     * Search plugin based on plugin dto as example.
     *
     * @param pluginDto plugin data transfer object.
     * @return plugin with same data.
     */
    @Override
    public Optional<Plugin> findByDto(PluginRemoveDto pluginDto) {
        return pluginRepository.findAll().stream()
                .filter(p -> {
                    return pluginDto.getPluginPSID().equals(p.getPluginPSID()) &&
                            pluginDto.getName().equals(p.getName()) &&
                            pluginDto.getVersion().equals(p.getVersion());
                })
                .findAny();
    }

    /**
     * Search all plugins with specified tags.
     *
     * @param user initiator.
     * @param tags search tags.
     * @return list of plugins that contains all specified tags.
     */
    @Override
    public List<PluginSearchResultDto> containsAllTags(User user, Set<String> tags) {
        return pluginRepository.findAll().stream()
                .filter(p -> p.getTags().containsAll(tags))
                .map(pluginEntitiesConverter::convertToSearchResultDto)
                .collect(toUnmodifiableList());
    }

    /**
     * Search all plugins that substring name (substring).
     *
     * @param user         user object.
     * @param part_of_name sequence of characters (substring of plugin name).
     * @return list of plugins that contains specified substring.
     */
    @Override
    public List<PluginSearchResultDto> containsInName(User user, String part_of_name) {
        return pluginRepository.findAll().stream()
                .filter(p -> p != null
                        && p.getName() != null
                        && p.getName().contains(part_of_name))
                .map(pluginEntitiesConverter::convertToSearchResultDto)
                .collect(toUnmodifiableList());
    }

    /**
     * Get all plugins.
     *
     * @return
     */
    @Override
    public List<Plugin> findAll() {
        return pluginRepository.findAll();
    }

    /**
     * Publish plugin on server.
     *
     * @param user             author.
     * @param pluginPublishDto plugin to publish.
     * @return if published successfully - true, else - false.
     */
    @Override
    public boolean publishPlugin(User user, PluginPublishDto pluginPublishDto) throws PluginNotPublishException {
        // Create new plugin.
        Plugin plugin = pluginEntitiesConverter.convertFromDto(pluginPublishDto);
        // Set author of plugin.
        plugin.setAuthor(user.getLogin());

        File pluginsDir = new File(pluginsDirStr);
        // Check if directory for published plugins exist.
        if (!pluginsDir.exists()) {
            // Create directories, in case path not exists.
            pluginsDir.mkdirs();
        }

        // Create new unique plugin name, for saving on server.
        String uniqueFileName = System.currentTimeMillis() + "_" + UUID.randomUUID();
        String fullPathStr = String.format("%s/%s", pluginsDir, uniqueFileName);
        File fullPath = new File(fullPathStr);

        // Save plugin to plugin storage.
        try {
            long checkSum = fileWorker.writeOnDiskWithCheckSum(fullPath, pluginPublishDto.getContent());
            plugin.setCheckSum(checkSum);
        } catch (IOException e) {
            // If some error occurred notify user.
            throw new PluginNotPublishException();
        }

        // Save path to plugin file in plugin object.
        plugin.setFullPath(fullPath.getPath());
        plugin.setDigitalSign(digitalSign);

        // Save plugin in DB.
        pluginRepository.save(plugin);

        return true;
    }

    /**
     * Remove plugin from marketplace.
     *
     * @param user         author.
     * @param removePlugin plugin information.
     * @return TRUE if plugin removed successfully, otherwise FALSE.
     */
    @Override
    public boolean removePlugin(User user, PluginRemoveDto removePlugin) throws IOException {
        // Search plugin by unpublishPlugin dto.
        Optional<Plugin> plugin = this.findByDto(removePlugin);

        // Check if plugin found.
        if (plugin.isPresent()) {
            File fullPath = new File(plugin.get().getFullPath());
            if (fullPath.exists()) {
                Files.delete(Paths.get(fullPath.toURI()));
            }

            // Remove plugin from DB.
            pluginRepository.delete(plugin.get());

            return true;
        }

        return false;
    }
}
