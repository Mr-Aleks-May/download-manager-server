package com.mralexmay.projects.download_manager.server.api.store.plugin.converter.mapper;

import com.mralexmay.projects.download_manager.server.api.store.plugin.converter.dto.PluginSearchResultDto;
import com.mralexmay.projects.download_manager.server.api.store.plugin.model.Plugin;
import org.springframework.stereotype.Component;

@Component
public class PluginSearchResultMapper {
    public PluginSearchResultDto fromEntityToDto(Plugin plugin) {
        return new PluginSearchResultDto()
                .setPluginPSID(plugin.getPluginPSID())
                .setName(plugin.getName())
                .setVersion(plugin.getVersion())
                .setCheckSum(plugin.getCheckSum())
                .setDescription(plugin.getDescription())
                .setTags(plugin.getTags())
                .setAuthor(plugin.getAuthor());
    }

    public Plugin fromDtoToEntity(PluginSearchResultDto pluginSearchResultDto) {
        return new Plugin()
                .setPluginPSID(pluginSearchResultDto.getPluginPSID())
                .setName(pluginSearchResultDto.getName())
                .setVersion(pluginSearchResultDto.getVersion())
                .setCheckSum(pluginSearchResultDto.getCheckSum())
                .setDescription(pluginSearchResultDto.getDescription())
                .setTags(pluginSearchResultDto.getTags());
    }
}
