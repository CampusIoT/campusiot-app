package fr.imag.campusiot.app.web.rest;

import fr.imag.campusiot.app.CampusiotApp;

import fr.imag.campusiot.app.domain.Message;
import fr.imag.campusiot.app.repository.MessageRepository;
import fr.imag.campusiot.app.service.MessageService;
import fr.imag.campusiot.app.service.dto.MessageDTO;
import fr.imag.campusiot.app.service.mapper.MessageMapper;
import fr.imag.campusiot.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static fr.imag.campusiot.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.imag.campusiot.app.domain.enumeration.MessageType;
import fr.imag.campusiot.app.domain.enumeration.Network;
/**
 * Test class for the MessageResource REST controller.
 *
 * @see MessageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CampusiotApp.class)
public class MessageResourceIntTest {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final MessageType DEFAULT_TYPE = MessageType.JOIN;
    private static final MessageType UPDATED_TYPE = MessageType.DTUP;

    private static final Integer DEFAULT_FCNT = 0;
    private static final Integer UPDATED_FCNT = 1;

    private static final Network DEFAULT_NETWORK = Network.CAMPUSIOT;
    private static final Network UPDATED_NETWORK = Network.TTN;

    private static final byte[] DEFAULT_PAYLOAD = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PAYLOAD = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PAYLOAD_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PAYLOAD_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_JSON = "AAAAAAAAAA";
    private static final String UPDATED_JSON = "BBBBBBBBBB";

    private static final Float DEFAULT_LATITUDE = -90F;
    private static final Float UPDATED_LATITUDE = -89F;

    private static final Float DEFAULT_LONGITUDE = -180F;
    private static final Float UPDATED_LONGITUDE = -179F;

    private static final Integer DEFAULT_ALTITUDE = 1;
    private static final Integer UPDATED_ALTITUDE = 2;

    private static final Integer DEFAULT_BATTERY_LEVEL = 1;
    private static final Integer UPDATED_BATTERY_LEVEL = 2;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageMapper;
    
    @Autowired
    private MessageService messageService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMessageMockMvc;

    private Message message;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MessageResource messageResource = new MessageResource(messageService);
        this.restMessageMockMvc = MockMvcBuilders.standaloneSetup(messageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createEntity(EntityManager em) {
        Message message = new Message()
            .date(DEFAULT_DATE)
            .type(DEFAULT_TYPE)
            .fcnt(DEFAULT_FCNT)
            .network(DEFAULT_NETWORK)
            .payload(DEFAULT_PAYLOAD)
            .payloadContentType(DEFAULT_PAYLOAD_CONTENT_TYPE)
            .json(DEFAULT_JSON)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .altitude(DEFAULT_ALTITUDE)
            .batteryLevel(DEFAULT_BATTERY_LEVEL);
        return message;
    }

    @Before
    public void initTest() {
        message = createEntity(em);
    }

    @Test
    @Transactional
    public void createMessage() throws Exception {
        int databaseSizeBeforeCreate = messageRepository.findAll().size();

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);
        restMessageMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageDTO)))
            .andExpect(status().isCreated());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate + 1);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMessage.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMessage.getFcnt()).isEqualTo(DEFAULT_FCNT);
        assertThat(testMessage.getNetwork()).isEqualTo(DEFAULT_NETWORK);
        assertThat(testMessage.getPayload()).isEqualTo(DEFAULT_PAYLOAD);
        assertThat(testMessage.getPayloadContentType()).isEqualTo(DEFAULT_PAYLOAD_CONTENT_TYPE);
        assertThat(testMessage.getJson()).isEqualTo(DEFAULT_JSON);
        assertThat(testMessage.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testMessage.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testMessage.getAltitude()).isEqualTo(DEFAULT_ALTITUDE);
        assertThat(testMessage.getBatteryLevel()).isEqualTo(DEFAULT_BATTERY_LEVEL);
    }

    @Test
    @Transactional
    public void createMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = messageRepository.findAll().size();

        // Create the Message with an existing ID
        message.setId(1L);
        MessageDTO messageDTO = messageMapper.toDto(message);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRepository.findAll().size();
        // set the field null
        message.setDate(null);

        // Create the Message, which fails.
        MessageDTO messageDTO = messageMapper.toDto(message);

        restMessageMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageDTO)))
            .andExpect(status().isBadRequest());

        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRepository.findAll().size();
        // set the field null
        message.setType(null);

        // Create the Message, which fails.
        MessageDTO messageDTO = messageMapper.toDto(message);

        restMessageMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageDTO)))
            .andExpect(status().isBadRequest());

        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFcntIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRepository.findAll().size();
        // set the field null
        message.setFcnt(null);

        // Create the Message, which fails.
        MessageDTO messageDTO = messageMapper.toDto(message);

        restMessageMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageDTO)))
            .andExpect(status().isBadRequest());

        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNetworkIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRepository.findAll().size();
        // set the field null
        message.setNetwork(null);

        // Create the Message, which fails.
        MessageDTO messageDTO = messageMapper.toDto(message);

        restMessageMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageDTO)))
            .andExpect(status().isBadRequest());

        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMessages() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList
        restMessageMockMvc.perform(get("/api/messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fcnt").value(hasItem(DEFAULT_FCNT)))
            .andExpect(jsonPath("$.[*].network").value(hasItem(DEFAULT_NETWORK.toString())))
            .andExpect(jsonPath("$.[*].payloadContentType").value(hasItem(DEFAULT_PAYLOAD_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].payload").value(hasItem(Base64Utils.encodeToString(DEFAULT_PAYLOAD))))
            .andExpect(jsonPath("$.[*].json").value(hasItem(DEFAULT_JSON.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].altitude").value(hasItem(DEFAULT_ALTITUDE)))
            .andExpect(jsonPath("$.[*].batteryLevel").value(hasItem(DEFAULT_BATTERY_LEVEL)));
    }
    
    @Test
    @Transactional
    public void getMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get the message
        restMessageMockMvc.perform(get("/api/messages/{id}", message.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(message.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.fcnt").value(DEFAULT_FCNT))
            .andExpect(jsonPath("$.network").value(DEFAULT_NETWORK.toString()))
            .andExpect(jsonPath("$.payloadContentType").value(DEFAULT_PAYLOAD_CONTENT_TYPE))
            .andExpect(jsonPath("$.payload").value(Base64Utils.encodeToString(DEFAULT_PAYLOAD)))
            .andExpect(jsonPath("$.json").value(DEFAULT_JSON.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.altitude").value(DEFAULT_ALTITUDE))
            .andExpect(jsonPath("$.batteryLevel").value(DEFAULT_BATTERY_LEVEL));
    }

    @Test
    @Transactional
    public void getNonExistingMessage() throws Exception {
        // Get the message
        restMessageMockMvc.perform(get("/api/messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Update the message
        Message updatedMessage = messageRepository.findById(message.getId()).get();
        // Disconnect from session so that the updates on updatedMessage are not directly saved in db
        em.detach(updatedMessage);
        updatedMessage
            .date(UPDATED_DATE)
            .type(UPDATED_TYPE)
            .fcnt(UPDATED_FCNT)
            .network(UPDATED_NETWORK)
            .payload(UPDATED_PAYLOAD)
            .payloadContentType(UPDATED_PAYLOAD_CONTENT_TYPE)
            .json(UPDATED_JSON)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .altitude(UPDATED_ALTITUDE)
            .batteryLevel(UPDATED_BATTERY_LEVEL);
        MessageDTO messageDTO = messageMapper.toDto(updatedMessage);

        restMessageMockMvc.perform(put("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageDTO)))
            .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMessage.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMessage.getFcnt()).isEqualTo(UPDATED_FCNT);
        assertThat(testMessage.getNetwork()).isEqualTo(UPDATED_NETWORK);
        assertThat(testMessage.getPayload()).isEqualTo(UPDATED_PAYLOAD);
        assertThat(testMessage.getPayloadContentType()).isEqualTo(UPDATED_PAYLOAD_CONTENT_TYPE);
        assertThat(testMessage.getJson()).isEqualTo(UPDATED_JSON);
        assertThat(testMessage.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testMessage.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testMessage.getAltitude()).isEqualTo(UPDATED_ALTITUDE);
        assertThat(testMessage.getBatteryLevel()).isEqualTo(UPDATED_BATTERY_LEVEL);
    }

    @Test
    @Transactional
    public void updateNonExistingMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageMockMvc.perform(put("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        int databaseSizeBeforeDelete = messageRepository.findAll().size();

        // Get the message
        restMessageMockMvc.perform(delete("/api/messages/{id}", message.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Message.class);
        Message message1 = new Message();
        message1.setId(1L);
        Message message2 = new Message();
        message2.setId(message1.getId());
        assertThat(message1).isEqualTo(message2);
        message2.setId(2L);
        assertThat(message1).isNotEqualTo(message2);
        message1.setId(null);
        assertThat(message1).isNotEqualTo(message2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageDTO.class);
        MessageDTO messageDTO1 = new MessageDTO();
        messageDTO1.setId(1L);
        MessageDTO messageDTO2 = new MessageDTO();
        assertThat(messageDTO1).isNotEqualTo(messageDTO2);
        messageDTO2.setId(messageDTO1.getId());
        assertThat(messageDTO1).isEqualTo(messageDTO2);
        messageDTO2.setId(2L);
        assertThat(messageDTO1).isNotEqualTo(messageDTO2);
        messageDTO1.setId(null);
        assertThat(messageDTO1).isNotEqualTo(messageDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(messageMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(messageMapper.fromId(null)).isNull();
    }
}
