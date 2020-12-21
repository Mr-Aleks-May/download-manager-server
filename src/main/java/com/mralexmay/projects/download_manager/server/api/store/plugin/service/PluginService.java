package com.mralexmay.projects.download_manager.server.api.store.plugin.service;

import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.dto.PluginPublishDto;
import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.dto.PluginRemoveDto;
import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.dto.PluginSearchResultDto;
import com.mralexmay.projects.download_manager.server.api.store.plugin.exception.PluginNotPublishException;
import com.mralexmay.projects.download_manager.server.api.store.plugin.model.Plugin;
import com.mralexmay.projects.download_manager.server.commons.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Plugins marketplace service.
 */
public interface PluginService {

    /**
     * Search plugin with specified plugin unique identifier.
     *
     * @param pluginPSID plugin unique identifier.
     * @return plugin with specified unique identifier.
     */
    Optional<Plugin> findByPSID(String pluginPSID);

    /**
     * Search plugin based on plugin dto as example.
     *
     * @param pluginDto plugin data transfer object.
     * @return plugin with same data.
     */
    Optional<Plugin> findByDto(PluginRemoveDto pluginDto);

    /**
     * Search all plugins with specified tags.
     *
     * @param user
     * @param searchTags
     * @return
     */
    List<PluginSearchResultDto> containsAllTags(User user, Set<String> searchTags);

    /**
     * Search all plugins that containsInNameStr name (substring).
     *
     * @param user              user object.
     * @param containsInNameStr sequence of characters (substring of plugin name).
     * @return list of plugins that contains specified substring.
     */
    List<PluginSearchResultDto> containsInName(User user, String containsInNameStr);

    /**
     * Get all plugins.
     *
     * @return
     */
    List<Plugin> findAll();

    /**
     * Publish new plugin in marketplace.
     *
     * @param user             author.
     * @param pluginPublishDto plugin information.
     * @return TRUE if plugin published successfully, otherwise FALSE.
     */
    boolean publishPlugin(User user, PluginPublishDto pluginPublishDto) throws PluginNotPublishException;

    /**
     * Remove plugin from marketplace.
     *
     * @param user         author.
     * @param removePlugin plugin information.
     * @return TRUE if plugin removed successfully, otherwise FALSE.
     */
    boolean removePlugin(User user, PluginRemoveDto removePlugin) throws IOException;
}
