package com.example.project.dto.response;

import com.example.project.domain.events.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventResponseMapper {
    @Mapping(target = "data", source = "data")
    EventResponse toEventResponse(Event event);
}