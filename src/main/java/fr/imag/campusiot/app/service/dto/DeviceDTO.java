package fr.imag.campusiot.app.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import fr.imag.campusiot.app.domain.enumeration.DeviceModel;
import fr.imag.campusiot.app.domain.enumeration.Network;
import fr.imag.campusiot.app.domain.enumeration.MessageType;

/**
 * A DTO for the Device entity.
 */
public class DeviceDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String location;

    @NotNull
    private DeviceModel model;

    private Network network;

    @NotNull
    @Size(min = 16, max = 16)
    @Pattern(regexp = "[a-fA-F0-9]*")
    private String deveui;

    @NotNull
    @Size(min = 16, max = 16)
    @Pattern(regexp = "[a-fA-F0-9]*")
    private String appeui;

    @NotNull
    @Size(min = 32, max = 32)
    @Pattern(regexp = "[a-fA-F0-9]*")
    private String appkey;

    @Min(value = 0)
    private Integer delayBeforeOffline;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant registeredAt;

    private Instant lastMessageAt;

    private MessageType type;

    @Min(value = 0)
    private Integer fcnt;

    @DecimalMin(value = "-90")
    @DecimalMax(value = "90")
    private Float latitude;

    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    private Float longitude;

    private Integer altitude;

    private Integer batteryLevel;

    private Long createdById;

    private String createdByLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public DeviceModel getModel() {
        return model;
    }

    public void setModel(DeviceModel model) {
        this.model = model;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public String getDeveui() {
        return deveui;
    }

    public void setDeveui(String deveui) {
        this.deveui = deveui;
    }

    public String getAppeui() {
        return appeui;
    }

    public void setAppeui(String appeui) {
        this.appeui = appeui;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public Integer getDelayBeforeOffline() {
        return delayBeforeOffline;
    }

    public void setDelayBeforeOffline(Integer delayBeforeOffline) {
        this.delayBeforeOffline = delayBeforeOffline;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Instant registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Instant getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(Instant lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Integer getFcnt() {
        return fcnt;
    }

    public void setFcnt(Integer fcnt) {
        this.fcnt = fcnt;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Integer getAltitude() {
        return altitude;
    }

    public void setAltitude(Integer altitude) {
        this.altitude = altitude;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long userId) {
        this.createdById = userId;
    }

    public String getCreatedByLogin() {
        return createdByLogin;
    }

    public void setCreatedByLogin(String userLogin) {
        this.createdByLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeviceDTO deviceDTO = (DeviceDTO) o;
        if (deviceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deviceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DeviceDTO{" +
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
            ", createdBy=" + getCreatedById() +
            ", createdBy='" + getCreatedByLogin() + "'" +
            "}";
    }
}
