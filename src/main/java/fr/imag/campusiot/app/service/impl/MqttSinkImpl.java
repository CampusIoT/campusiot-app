package fr.imag.campusiot.app.service.impl;

import java.io.IOException;
import java.util.Set;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.imag.campusiot.app.repository.DeviceRepository;
import fr.imag.campusiot.app.service.DeviceService;
import fr.imag.campusiot.app.service.MessageService;
import fr.imag.campusiot.app.service.NetworkParserService;
import fr.imag.campusiot.app.service.dto.MessageDTO;

@Configuration
@IntegrationComponentScan
@EnableIntegration
@Service
public class MqttSinkImpl {

	private final Logger log = LoggerFactory.getLogger(MqttSinkImpl.class);

	// TODO should be configured by IoD

	@Value("${mqtt.url:tcp://localhost:1883}")
	private String brokerUrl;

	@Value("${mqtt.cliendId:campus-iot-123456789}")
	private String clientId;

	@Value("${mqtt.topic:application/#}")
	private String topic;

	@Value("${mqtt.username:anUser}")
	private String username;

	@Value("${mqtt.password:__SUPER_SECRET_PASSWORD__}")
	private String password;

	@Value("${mqtt.qos:2}")
	private int qos;

	@Value("${mqtt.completionTimeout:5000}")
	private int completionTimeout;

	private DeviceRepository deviceRepository;
	private DeviceService deviceService;

	private MessageService messageService;
	private NetworkParserService networkParserService;

	public MqttSinkImpl(MessageService messageService, DeviceService deviceService,
			NetworkParserService networkParserService, DeviceRepository deviceRepository) {
		this.messageService = messageService;
		this.deviceService = deviceService;
		this.networkParserService = networkParserService;
		this.deviceRepository = deviceRepository;
	}

	@Bean
	public MessageChannel mqttInputChannel() {
		DirectChannel dc = new DirectChannel();
		// dc.subscribe(inbound());
		return dc;
	}

	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		MqttConnectOptions options = new MqttConnectOptions();
		options.setServerURIs(new String[] { brokerUrl });
		// options.setUserName(username);
		// options.setPassword(password.toCharArray());
		factory.setConnectionOptions(options);
		return factory;
	}

	@Bean
	public MessageProducer inbound() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(brokerUrl, clientId,
				mqttClientFactory(), topic);
		adapter.setCompletionTimeout(completionTimeout);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(qos);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
		return new MessageHandler() {
			@Override
			public void handleMessage(Message<?> message) throws MessagingException {

				ObjectMapper mapper = new ObjectMapper();

				ObjectNode mqttHeaders = mapper.createObjectNode();

				MessageHeaders headers = message.getHeaders();
				Set<String> headerKeys = headers.keySet();
				for (String key : headerKeys) {
					mqttHeaders.put(key, headers.get(key).toString());
				}

				log.debug("handle message with headers : {}", mqttHeaders.toString());
				try {
					log.debug("handle message with headers : {}",
							mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mqttHeaders));
				} catch (JsonProcessingException e) {
					log.warn("can not print pretty message headers");
				}

				Object payload = message.getPayload();
				if (!(payload instanceof String)) {
					log.debug("Can not process payload of type {}", payload.getClass());
					return;
				}

				try {
					JsonNode json = mapper.readTree((String) payload);

					((ObjectNode) json).put("mqtt_headers", mqttHeaders);

					MessageDTO messageDTO = networkParserService.parse(json, mqttHeaders);

					// TODO update the device
					// deviceService.update(deviceDTO);
					// messageService.create(messageDTO);
					messageService.save(messageDTO);

				} catch (IOException e) {
					log.warn("Can not process the json payload", e);
					return;
				}
			}
		};
	}
}
