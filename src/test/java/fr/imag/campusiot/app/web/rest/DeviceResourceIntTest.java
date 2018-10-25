package fr.imag.campusiot.app.web.rest;

import fr.imag.campusiot.app.CampusiotApp;

import fr.imag.campusiot.app.domain.Device;
import fr.imag.campusiot.app.repository.DeviceRepository;
import fr.imag.campusiot.app.service.DeviceService;
import fr.imag.campusiot.app.service.dto.DeviceDTO;
import fr.imag.campusiot.app.service.mapper.DeviceMapper;
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

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static fr.imag.campusiot.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.imag.campusiot.app.domain.enumeration.DeviceModel;
import fr.imag.campusiot.app.domain.enumeration.Network;
import fr.imag.campusiot.app.domain.enumeration.MessageType;
/**
 * Test class for the DeviceResource REST controller.
 *
 * @see DeviceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CampusiotApp.class)
public class DeviceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final DeviceModel DEFAULT_MODEL = DeviceModel.SICONIA;
    private static final DeviceModel UPDATED_MODEL = DeviceModel.ADENUIS_TEMP;

    private static final Network DEFAULT_NETWORK = Network.CAMPUSIOT;
    private static final Network UPDATED_NETWORK = Network.TTN;

    private static final String DEFAULT_DEVEUI = "AAAAAAAAAAAAAAAA";
    private static final String UPDATED_DEVEUI = "BBBBBBBBBBBBBBBB";

    private static final String DEFAULT_APPEUI = "AAAAAAAAAAAAAAAA";
    private static final String UPDATED_APPEUI = "BBBBBBBBBBBBBBBB";

    private static final String DEFAULT_APPKEY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_APPKEY = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Integer DEFAULT_DELAY_BEFORE_OFFLINE = 0;
    private static final Integer UPDATED_DELAY_BEFORE_OFFLINE = 1;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_REGISTERED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REGISTERED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MESSAGE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MESSAGE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final MessageType DEFAULT_TYPE = MessageType.JOIN;
    private static final MessageType UPDATED_TYPE = MessageType.DTUP;

    private static final Integer DEFAULT_FCNT = 0;
    private static final Integer UPDATED_FCNT = 1;

    private static final Float DEFAULT_LATITUDE = -90F;
    private static final Float UPDATED_LATITUDE = -89F;

    private static final Float DEFAULT_LONGITUDE = -180F;
    private static final Float UPDATED_LONGITUDE = -179F;

    private static final Integer DEFAULT_ALTITUDE = 1;
    private static final Integer UPDATED_ALTITUDE = 2;

    private static final Integer DEFAULT_BATTERY_LEVEL = 1;
    private static final Integer UPDATED_BATTERY_LEVEL = 2;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceMapper deviceMapper;
    
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDeviceMockMvc;

    private Device device;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DeviceResource deviceResource = new DeviceResource(deviceService);
        this.restDeviceMockMvc = MockMvcBuilders.standaloneSetup(deviceResource)
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
    public static Device createEntity(EntityManager em) {
        Device device = new Device()
            .name(DEFAULT_NAME)
            .location(DEFAULT_LOCATION)
            .model(DEFAULT_MODEL)
            .network(DEFAULT_NETWORK)
            .deveui(DEFAULT_DEVEUI)
            .appeui(DEFAULT_APPEUI)
            .appkey(DEFAULT_APPKEY)
            .delayBeforeOffline(DEFAULT_DELAY_BEFORE_OFFLINE)
            .createdAt(DEFAULT_CREATED_AT)
            .registeredAt(DEFAULT_REGISTERED_AT)
            .lastMessageAt(DEFAULT_LAST_MESSAGE_AT)
            .type(DEFAULT_TYPE)
            .fcnt(DEFAULT_FCNT)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .altitude(DEFAULT_ALTITUDE)
            .batteryLevel(DEFAULT_BATTERY_LEVEL);
        return device;
    }

    @Before
    public void initTest() {
        device = createEntity(em);
    }

    @Test
    @Transactional
    public void createDevice() throws Exception {
        int databaseSizeBeforeCreate = deviceRepository.findAll().size();

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);
        restDeviceMockMvc.perform(post("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isCreated());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate + 1);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDevice.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testDevice.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testDevice.getNetwork()).isEqualTo(DEFAULT_NETWORK);
        assertThat(testDevice.getDeveui()).isEqualTo(DEFAULT_DEVEUI);
        assertThat(testDevice.getAppeui()).isEqualTo(DEFAULT_APPEUI);
        assertThat(testDevice.getAppkey()).isEqualTo(DEFAULT_APPKEY);
        assertThat(testDevice.getDelayBeforeOffline()).isEqualTo(DEFAULT_DELAY_BEFORE_OFFLINE);
        assertThat(testDevice.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDevice.getRegisteredAt()).isEqualTo(DEFAULT_REGISTERED_AT);
        assertThat(testDevice.getLastMessageAt()).isEqualTo(DEFAULT_LAST_MESSAGE_AT);
        assertThat(testDevice.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDevice.getFcnt()).isEqualTo(DEFAULT_FCNT);
        assertThat(testDevice.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testDevice.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testDevice.getAltitude()).isEqualTo(DEFAULT_ALTITUDE);
        assertThat(testDevice.getBatteryLevel()).isEqualTo(DEFAULT_BATTERY_LEVEL);
    }

    @Test
    @Transactional
    public void createDeviceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deviceRepository.findAll().size();

        // Create the Device with an existing ID
        device.setId(1L);
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceMockMvc.perform(post("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setName(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc.perform(post("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModelIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setModel(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc.perform(post("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeveuiIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setDeveui(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc.perform(post("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAppeuiIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setAppeui(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc.perform(post("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAppkeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setAppkey(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc.perform(post("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setCreatedAt(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc.perform(post("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegisteredAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setRegisteredAt(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc.perform(post("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDevices() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList
        restDeviceMockMvc.perform(get("/api/devices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
            .andExpect(jsonPath("$.[*].network").value(hasItem(DEFAULT_NETWORK.toString())))
            .andExpect(jsonPath("$.[*].deveui").value(hasItem(DEFAULT_DEVEUI.toString())))
            .andExpect(jsonPath("$.[*].appeui").value(hasItem(DEFAULT_APPEUI.toString())))
            .andExpect(jsonPath("$.[*].appkey").value(hasItem(DEFAULT_APPKEY.toString())))
            .andExpect(jsonPath("$.[*].delayBeforeOffline").value(hasItem(DEFAULT_DELAY_BEFORE_OFFLINE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].registeredAt").value(hasItem(DEFAULT_REGISTERED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastMessageAt").value(hasItem(DEFAULT_LAST_MESSAGE_AT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fcnt").value(hasItem(DEFAULT_FCNT)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].altitude").value(hasItem(DEFAULT_ALTITUDE)))
            .andExpect(jsonPath("$.[*].batteryLevel").value(hasItem(DEFAULT_BATTERY_LEVEL)));
    }
    
    @Test
    @Transactional
    public void getDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get the device
        restDeviceMockMvc.perform(get("/api/devices/{id}", device.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(device.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL.toString()))
            .andExpect(jsonPath("$.network").value(DEFAULT_NETWORK.toString()))
            .andExpect(jsonPath("$.deveui").value(DEFAULT_DEVEUI.toString()))
            .andExpect(jsonPath("$.appeui").value(DEFAULT_APPEUI.toString()))
            .andExpect(jsonPath("$.appkey").value(DEFAULT_APPKEY.toString()))
            .andExpect(jsonPath("$.delayBeforeOffline").value(DEFAULT_DELAY_BEFORE_OFFLINE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.registeredAt").value(DEFAULT_REGISTERED_AT.toString()))
            .andExpect(jsonPath("$.lastMessageAt").value(DEFAULT_LAST_MESSAGE_AT.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.fcnt").value(DEFAULT_FCNT))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.altitude").value(DEFAULT_ALTITUDE))
            .andExpect(jsonPath("$.batteryLevel").value(DEFAULT_BATTERY_LEVEL));
    }

    @Test
    @Transactional
    public void getNonExistingDevice() throws Exception {
        // Get the device
        restDeviceMockMvc.perform(get("/api/devices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device
        Device updatedDevice = deviceRepository.findById(device.getId()).get();
        // Disconnect from session so that the updates on updatedDevice are not directly saved in db
        em.detach(updatedDevice);
        updatedDevice
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .model(UPDATED_MODEL)
            .network(UPDATED_NETWORK)
            .deveui(UPDATED_DEVEUI)
            .appeui(UPDATED_APPEUI)
            .appkey(UPDATED_APPKEY)
            .delayBeforeOffline(UPDATED_DELAY_BEFORE_OFFLINE)
            .createdAt(UPDATED_CREATED_AT)
            .registeredAt(UPDATED_REGISTERED_AT)
            .lastMessageAt(UPDATED_LAST_MESSAGE_AT)
            .type(UPDATED_TYPE)
            .fcnt(UPDATED_FCNT)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .altitude(UPDATED_ALTITUDE)
            .batteryLevel(UPDATED_BATTERY_LEVEL);
        DeviceDTO deviceDTO = deviceMapper.toDto(updatedDevice);

        restDeviceMockMvc.perform(put("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDevice.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testDevice.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testDevice.getNetwork()).isEqualTo(UPDATED_NETWORK);
        assertThat(testDevice.getDeveui()).isEqualTo(UPDATED_DEVEUI);
        assertThat(testDevice.getAppeui()).isEqualTo(UPDATED_APPEUI);
        assertThat(testDevice.getAppkey()).isEqualTo(UPDATED_APPKEY);
        assertThat(testDevice.getDelayBeforeOffline()).isEqualTo(UPDATED_DELAY_BEFORE_OFFLINE);
        assertThat(testDevice.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDevice.getRegisteredAt()).isEqualTo(UPDATED_REGISTERED_AT);
        assertThat(testDevice.getLastMessageAt()).isEqualTo(UPDATED_LAST_MESSAGE_AT);
        assertThat(testDevice.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDevice.getFcnt()).isEqualTo(UPDATED_FCNT);
        assertThat(testDevice.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testDevice.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testDevice.getAltitude()).isEqualTo(UPDATED_ALTITUDE);
        assertThat(testDevice.getBatteryLevel()).isEqualTo(UPDATED_BATTERY_LEVEL);
    }

    @Test
    @Transactional
    public void updateNonExistingDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc.perform(put("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeDelete = deviceRepository.findAll().size();

        // Get the device
        restDeviceMockMvc.perform(delete("/api/devices/{id}", device.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Device.class);
        Device device1 = new Device();
        device1.setId(1L);
        Device device2 = new Device();
        device2.setId(device1.getId());
        assertThat(device1).isEqualTo(device2);
        device2.setId(2L);
        assertThat(device1).isNotEqualTo(device2);
        device1.setId(null);
        assertThat(device1).isNotEqualTo(device2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceDTO.class);
        DeviceDTO deviceDTO1 = new DeviceDTO();
        deviceDTO1.setId(1L);
        DeviceDTO deviceDTO2 = new DeviceDTO();
        assertThat(deviceDTO1).isNotEqualTo(deviceDTO2);
        deviceDTO2.setId(deviceDTO1.getId());
        assertThat(deviceDTO1).isEqualTo(deviceDTO2);
        deviceDTO2.setId(2L);
        assertThat(deviceDTO1).isNotEqualTo(deviceDTO2);
        deviceDTO1.setId(null);
        assertThat(deviceDTO1).isNotEqualTo(deviceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(deviceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(deviceMapper.fromId(null)).isNull();
    }
}
