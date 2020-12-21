package com.mralexmay.projects.download_manager.server.commons.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Converter
public class StringToSetConverter implements AttributeConverter<Set<String>, String> {

    @Override
    public String convertToDatabaseColumn(Set<String> list) {
        if (list == null) return "";
        return String.join(",", list);
    }

    @Override
    public Set<String> convertToEntityAttribute(String joined) {
        if (joined == null) return new HashSet<>();
        return new HashSet<>(Arrays.asList(joined.split(",")));
    }
}