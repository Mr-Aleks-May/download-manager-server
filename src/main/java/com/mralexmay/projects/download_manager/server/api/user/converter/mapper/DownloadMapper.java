package com.mralexmay.projects.download_manager.server.api.user.converter.mapper;

import com.mralexmay.projects.download_manager.server.api.user.model.Download;
import com.mralexmay.projects.download_manager.server.api.user.converter.dto.DownloadDto;

import org.springframework.stereotype.Component;

@Component
public class DownloadMapper {
    private final CategoryMapper categoryMapper;

    public DownloadMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public DownloadDto fromEntityToDto(Download download) {
        return new DownloadDto()
                .setDSID(download.getDownloadDSID())
                .setFileName(download.getFileName())
                .setExtension(download.getExtension())
                .setTmpDir(download.getTmpDir())
                .setOutputDir(download.getOutputDir())
                .setUrl(download.getUrl())
                .setCategoryDto(categoryMapper.fromEntityToDto(download.getCategory()))
                .setCreationTime(download.getCreationTime());
    }

    public Download fromDtoToEntity(DownloadDto downloadDto) {
        return new Download()
                .setDownloadDSID(downloadDto.getDSID())
                .setFileName(downloadDto.getFileName())
                .setExtension(downloadDto.getExtension())
                .setTmpDir(downloadDto.getTmpDir())
                .setOutputDir(downloadDto.getOutputDir())
                .setUrl(downloadDto.getUrl())
                .setCategory(categoryMapper.fromDtoToEntity(downloadDto.getCategoryDto()))
                .setCreationTime(downloadDto.getCreationTime());
    }
}
