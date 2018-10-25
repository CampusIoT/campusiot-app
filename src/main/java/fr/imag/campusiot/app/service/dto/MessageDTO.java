package fr.imag.campusiot.app.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import fr.imag.campusiot.app.domain.enumeration.MessageType;
import fr.imag.campusiot.app.domain.enumeration.Network;

/**
 * A DTO for the Message entity.
 */
public class MessageDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant date;

    @NotNull
    private MessageType type;

    @NotNull
    @Min(value = 0)
    private Integer fcnt;

    @NotNull
    private Network network;

    
    @Lob
    private byte[] payload;
    private String payloadContentType;

    @Lob
    private String json;

    @DecimalMin(value = "-90")
    @DecimalMax(value = "90")
    private Float latitude;

    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    private Float longitude;

    private Integer altitude;

    private Integer batteryLevel;

    private Long sentById;

    private String sentByDeveui;

    private Long deviceOwnerId;

    private String deviceOwnerLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
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

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public String getPayloadContentType() {
        return payloadContentType;
    }

    public void setPayloadContentType(String payloadContentType) {
        this.payloadContentType = payloadContentType;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
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

    public Long getSentById() {
        return sentById;
    }

    public void setSentById(Long deviceId) {
        this.sentById = deviceId;
    }

    public String getSentByDeveui() {
        return sentByDeveui;
    }

    public void setSentByDeveui(String deviceDeveui) {
        this.sentByDeveui = deviceDeveui;
    }

    public Long getDeviceOwnerId() {
        return deviceOwnerId;
    }

    public void setDeviceOwnerId(Long userId) {
        this.deviceOwnerId = userId;
    }

    public String getDeviceOwnerLogin() {
        return deviceOwnerLogin;
    }

    public void setDeviceOwnerLogin(String userLogin) {
        this.deviceOwnerLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MessageDTO messageDTO = (MessageDTO) o;
        if (messageDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), messageDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", type='" + getType() + "'" +
            ", fcnt=" + getFcnt() +
            ", network='" + getNetwork() + "'" +
            ", payload='" + getPayload() + "'" +
            ", json='" + getJson() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", altitude=" + getAltitude() +
            ", batteryLevel=" + getBatteryLevel() +
            ", sentBy=" + getSentById() +
            ", sentBy='" + getSentByDeveui() + "'" +
            ", deviceOwner=" + getDeviceOwnerId() +
            ", deviceOwner='" + getDeviceOwnerLogin() + "'" +
            "}";
    }
}
