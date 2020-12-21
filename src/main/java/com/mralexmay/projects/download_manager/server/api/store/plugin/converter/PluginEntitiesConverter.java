package com.mralexmay.projects.download_manager.server.api.store.plugin.converter;

import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.dto.PluginPublishDto;
import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.dto.PluginSearchResultDto;
import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.mapper.PluginPublishMapper;
import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.mapper.PluginSearchResultMapper;
import com.mralexmay.projects.download_manager.server.api.store.plugin.model.Plugin;
import org.springframework.stereotype.Component;

@Component
public class PluginEntitiesConverter {
    private final PluginPublishMapper pluginPublishMapper;
    private final PluginSearchResultMapper pluginSearchResultMapper;


    public PluginEntitiesConverter(PluginPublishMapper pluginPublishMapper, PluginSearchResultMapper pluginSearchResultMapper) {
        this.pluginPublishMapper = pluginPublishMapper;
        this.pluginSearchResultMapper = pluginSearchResultMapper;
    }


    public PluginPublishDto convertToPublishDto(Plugin plugin) {
        return pluginPublishMapper.fromEntityToDto(plugin);
    }

    public Plugin convertFromDto(PluginPublishDto pluginPublishDto) {
        return pluginPublishMapper.fromDtoToEntity(pluginPublishDto);
    }


    public PluginSearchResultDto convertToSearchResultDto(Plugin plugin) {
        return pluginSearchResultMapper.fromEntityToDto(plugin);
    }

    public Plugin convertFromDto(PluginSearchResultDto pluginSearchResultDto) {
        return pluginSearchResultMapper.fromDtoToEntity(pluginSearchResultDto);
    }
}
