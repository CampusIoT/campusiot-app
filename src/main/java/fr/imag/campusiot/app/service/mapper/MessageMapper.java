package fr.imag.campusiot.app.service.mapper;

import fr.imag.campusiot.app.domain.*;
import fr.imag.campusiot.app.service.dto.MessageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Message and its DTO MessageDTO.
 */
@Mapper(componentModel = "spring", uses = {DeviceMapper.class, UserMapper.class})
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {

    @Mapping(source = "sentBy.id", target = "sentById")
    @Mapping(source = "sentBy.deveui", target = "sentByDeveui")
    @Mapping(source = "deviceOwner.id", target = "deviceOwnerId")
    @Mapping(source = "deviceOwner.login", target = "deviceOwnerLogin")
    MessageDTO toDto(Message message);

    @Mapping(source = "sentById", target = "sentBy")
    @Mapping(source = "deviceOwnerId", target = "deviceOwner")
    Message toEntity(MessageDTO messageDTO);

    default Message fromId(Long id) {
        if (id == null) {
            return null;
        }
        Message message = new Message();
        message.setId(id);
        return message;
    }
}
