package com.mralexmay.projects.download_manager.server.api.store.plugin.converter.mapper;

import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.dto.PluginPublishDto;
import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.dto.PluginSearchResultDto;
import com.mralexmay.projects.download_manager.server.api.store.plugin.model.Plugin;
import org.springframework.stereotype.Component;

@Component
public class PluginPublishMapper {
    public PluginPublishDto fromEntityToDto(Plugin plugin) {
        return new PluginPublishDto()
                .setPluginPSID(plugin.getPluginPSID())
                .setName(plugin.getName())
                .setVersion(plugin.getVersion())
                .setTags(plugin.getTags())
                .setDescription(plugin.getDescription());
    }

    public Plugin fromDtoToEntity(PluginPublishDto pluginPublishDto) {
        return new Plugin()
                .setPluginPSID(pluginPublishDto.getPluginPSID())
                .setName(pluginPublishDto.getName())
                .setVersion(pluginPublishDto.getVersion())
                .setDescription(pluginPublishDto.getDescription())
                .setTags(pluginPublishDto.getTags());
    }
}
