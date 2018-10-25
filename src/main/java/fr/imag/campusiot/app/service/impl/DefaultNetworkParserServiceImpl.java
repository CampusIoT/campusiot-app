package fr.imag.campusiot.app.service.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import fr.imag.campusiot.app.domain.enumeration.MessageType;
import fr.imag.campusiot.app.domain.enumeration.Network;
import fr.imag.campusiot.app.service.dto.MessageDTO;

@Service
public class DefaultNetworkParserServiceImpl implements fr.imag.campusiot.app.service.NetworkParserService {

	@Override
	public MessageDTO parse(JsonNode json, JsonNode mqttHeaders) {
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setJson(json.toString());
		messageDTO.setType(MessageType.DTUP);
		messageDTO.setSentByDeveui("1234567890ABCDEF");
		messageDTO.setFcnt(1);
		messageDTO.setDate(Instant.now());
		messageDTO.setNetwork(Network.CAMPUSIOT);
		messageDTO.setPayload(new byte[] { 0x01, 0x02 });
		messageDTO.setPayloadContentType("application/octet-stream");

		// TODO One NetworkParserService per Network
		// TODO According to the network
		// TODO get the deveui in the json object
		// TODO get the fcnt in the json object
		// TODO get the frmpayload in the json object
		// TODO get the battery level in the json object
		// TODO get the latitude in the json object
		// TODO get the longitude in the json object
		// TODO get the altitude in the json object
		return messageDTO;
	}

}
