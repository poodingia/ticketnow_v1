package com.ticketnow.crud.mapper;


import com.ticketnow.crud.domain.Event;
import com.ticketnow.crud.dto.EventDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "cityId", source = "city.id")
    @Mapping(target = "isApproved", source = "approved")
    @Mapping(target = "category", expression = "java(event.getCategory().name())")
    EventDTO toDTO(Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void partialUpdate(EventDTO eventDTO, @MappingTarget Event event);


}
