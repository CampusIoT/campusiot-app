package fr.imag.campusiot.app.service;

import com.fasterxml.jackson.databind.JsonNode;

import fr.imag.campusiot.app.service.dto.MessageDTO;

public interface NetworkParserService {
	public MessageDTO parse(JsonNode json, JsonNode mqttHeaders);
}
