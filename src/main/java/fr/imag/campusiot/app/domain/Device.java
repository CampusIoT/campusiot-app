package fr.imag.campusiot.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import fr.imag.campusiot.app.domain.enumeration.DeviceModel;

import fr.imag.campusiot.app.domain.enumeration.Network;

import fr.imag.campusiot.app.domain.enumeration.MessageType;

/**
 * A Device.
 */
@Entity
@Table(name = "device")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The device name
     */
    @NotNull
    @ApiModelProperty(value = "The device name", required = true)
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * The device location
     */
    @ApiModelProperty(value = "The device location")
    @Column(name = "location")
    private String location;

    /**
     * The device model
     */
    @NotNull
    @ApiModelProperty(value = "The device model", required = true)
    @Enumerated(EnumType.STRING)
    @Column(name = "model", nullable = false)
    private DeviceModel model;

    /**
     * The device network
     */
    @ApiModelProperty(value = "The device network")
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_network")
    private Network network;

    /**
     * The device DevEUI
     */
    @NotNull
    @Size(min = 16, max = 16)
    @Pattern(regexp = "[a-fA-F0-9]*")
    @ApiModelProperty(value = "The device DevEUI", required = true)
    @Column(name = "deveui", length = 16, nullable = false)
    private String deveui;

    /**
     * The device AppEUI
     */
    @NotNull
    @Size(min = 16, max = 16)
    @Pattern(regexp = "[a-fA-F0-9]*")
    @ApiModelProperty(value = "The device AppEUI", required = true)
    @Column(name = "appeui", length = 16, nullable = false)
    private String appeui;

    /**
     * The device AppKEY
     */
    @NotNull
    @Size(min = 32, max = 32)
    @Pattern(regexp = "[a-fA-F0-9]*")
    @ApiModelProperty(value = "The device AppKEY", required = true)
    @Column(name = "appkey", length = 32, nullable = false)
    private String appkey;

    /**
     * The delay in minutes before declaring the device offline
     */
    @Min(value = 0)
    @ApiModelProperty(value = "The delay in minutes before declaring the device offline")
    @Column(name = "delay_before_offline")
    private Integer delayBeforeOffline;

    /**
     * The creation date
     */
    @NotNull
    @ApiModelProperty(value = "The creation date", required = true)
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * The network registration date
     */
    @NotNull
    @ApiModelProperty(value = "The network registration date", required = true)
    @Column(name = "registered_at", nullable = false)
    private Instant registeredAt;

    /**
     * The last message date
     */
    @ApiModelProperty(value = "The last message date")
    @Column(name = "last_message_at")
    private Instant lastMessageAt;

    /**
     * The last message type
     */
    @ApiModelProperty(value = "The last message type")
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private MessageType type;

    /**
     * The last message fcnt (0 for JOIN)
     */
    @Min(value = 0)
    @ApiModelProperty(value = "The last message fcnt (0 for JOIN)")
    @Column(name = "fcnt")
    private Integer fcnt;

    /**
     * The last latitude of the device
     */
    @DecimalMin(value = "-90")
    @DecimalMax(value = "90")
    @ApiModelProperty(value = "The last latitude of the device")
    @Column(name = "latitude")
    private Float latitude;

    /**
     * The last latitude of the device
     */
    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    @ApiModelProperty(value = "The last latitude of the device")
    @Column(name = "longitude")
    private Float longitude;

    /**
     * The last altitude of the device
     */
    @ApiModelProperty(value = "The last altitude of the device")
    @Column(name = "altitude")
    private Integer altitude;

    /**
     * The last battery level of the device
     */
    @ApiModelProperty(value = "The last battery level of the device")
    @Column(name = "battery_level")
    private Integer batteryLevel;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User createdBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Device name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public Device location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public DeviceModel getModel() {
        return model;
    }

    public Device model(DeviceModel model) {
        this.model = model;
        return this;
    }

    public void setModel(DeviceModel model) {
        this.model = model;
    }

    public Network getNetwork() {
        return network;
    }

    public Device network(Network network) {
        this.network = network;
        return this;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public String getDeveui() {
        return deveui;
    }

    public Device deveui(String deveui) {
        this.deveui = deveui;
        return this;
    }

    public void setDeveui(String deveui) {
        this.deveui = deveui;
    }

    public String getAppeui() {
        return appeui;
    }

    public Device appeui(String appeui) {
        this.appeui = appeui;
        return this;
    }

    public void setAppeui(String appeui) {
        this.appeui = appeui;
    }

    public String getAppkey() {
        return appkey;
    }

    public Device appkey(String appkey) {
        this.appkey = appkey;
        return this;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public Integer getDelayBeforeOffline() {
        return delayBeforeOffline;
    }

    public Device delayBeforeOffline(Integer delayBeforeOffline) {
        this.delayBeforeOffline = delayBeforeOffline;
        return this;
    }

    public void setDelayBeforeOffline(Integer delayBeforeOffline) {
        this.delayBeforeOffline = delayBeforeOffline;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Device createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getRegisteredAt() {
        return registeredAt;
    }

    public Device registeredAt(Instant registeredAt) {
        this.registeredAt = registeredAt;
        return this;
    }

    public void setRegisteredAt(Instant registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Instant getLastMessageAt() {
        return lastMessageAt;
    }

    public Device lastMessageAt(Instant lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
        return this;
    }

    public void setLastMessageAt(Instant lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    public MessageType getType() {
        return type;
    }

    public Device type(MessageType type) {
        this.type = type;
        return this;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Integer getFcnt() {
        return fcnt;
    }

    public Device fcnt(Integer fcnt) {
        this.fcnt = fcnt;
        return this;
    }

    public void setFcnt(Integer fcnt) {
        this.fcnt = fcnt;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Device latitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public Device longitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Integer getAltitude() {
        return altitude;
    }

    public Device altitude(Integer altitude) {
        this.altitude = altitude;
        return this;
    }

    public void setAltitude(Integer altitude) {
        this.altitude = altitude;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public Device batteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
        return this;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public Device createdBy(User user) {
        this.createdBy = user;
        return this;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Device device = (Device) o;
        if (device.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), device.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Device{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            ", model='" + getModel() + "'" +
            ", network='" + getNetwork() + "'" +
            ", deveui='" + getDeveui() + "'" +
            ", appeui='" + getAppeui() + "'" +
            ", appkey='" + getAppkey() + "'" +
            ", delayBeforeOffline=" + getDelayBeforeOffline() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", registeredAt='" + getRegisteredAt() + "'" +
            ", lastMessageAt='" + getLastMessageAt() + "'" +
            ", type='" + getType() + "'" +
            ", fcnt=" + getFcnt() +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", altitude=" + getAltitude() +
            ", batteryLevel=" + getBatteryLevel() +
            "}";
    }
}
