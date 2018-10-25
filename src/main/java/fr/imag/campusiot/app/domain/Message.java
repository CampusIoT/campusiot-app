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

import fr.imag.campusiot.app.domain.enumeration.MessageType;

import fr.imag.campusiot.app.domain.enumeration.Network;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The message date
     */
    @NotNull
    @ApiModelProperty(value = "The message date", required = true)
    @Column(name = "jhi_date", nullable = false)
    private Instant date;

    /**
     * The message type
     */
    @NotNull
    @ApiModelProperty(value = "The message type", required = true)
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private MessageType type;

    /**
     * The message fcnt (0 for JOIN)
     */
    @NotNull
    @Min(value = 0)
    @ApiModelProperty(value = "The message fcnt (0 for JOIN)", required = true)
    @Column(name = "fcnt", nullable = false)
    private Integer fcnt;

    /**
     * The Device network
     */
    @NotNull
    @ApiModelProperty(value = "The Device network", required = true)
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_network", nullable = false)
    private Network network;

    /**
     * The payload
     */
    
    @ApiModelProperty(value = "The payload", required = true)
    @Lob
    @Column(name = "payload", nullable = false)
    private byte[] payload;

    @Column(name = "payload_content_type", nullable = false)
    private String payloadContentType;

    /**
     * The json message sent by the network server
     */
    @ApiModelProperty(value = "The json message sent by the network server")
    @Lob
    @Column(name = "json")
    private String json;

    /**
     * The latitude of the device
     */
    @DecimalMin(value = "-90")
    @DecimalMax(value = "90")
    @ApiModelProperty(value = "The latitude of the device")
    @Column(name = "latitude")
    private Float latitude;

    /**
     * The latitude of the device
     */
    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    @ApiModelProperty(value = "The latitude of the device")
    @Column(name = "longitude")
    private Float longitude;

    /**
     * The altitude of the device
     */
    @ApiModelProperty(value = "The altitude of the device")
    @Column(name = "altitude")
    private Integer altitude;

    /**
     * The battery level of the device
     */
    @ApiModelProperty(value = "The battery level of the device")
    @Column(name = "battery_level")
    private Integer batteryLevel;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Device sentBy;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User deviceOwner;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public Message date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public MessageType getType() {
        return type;
    }

    public Message type(MessageType type) {
        this.type = type;
        return this;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Integer getFcnt() {
        return fcnt;
    }

    public Message fcnt(Integer fcnt) {
        this.fcnt = fcnt;
        return this;
    }

    public void setFcnt(Integer fcnt) {
        this.fcnt = fcnt;
    }

    public Network getNetwork() {
        return network;
    }

    public Message network(Network network) {
        this.network = network;
        return this;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public byte[] getPayload() {
        return payload;
    }

    public Message payload(byte[] payload) {
        this.payload = payload;
        return this;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public String getPayloadContentType() {
        return payloadContentType;
    }

    public Message payloadContentType(String payloadContentType) {
        this.payloadContentType = payloadContentType;
        return this;
    }

    public void setPayloadContentType(String payloadContentType) {
        this.payloadContentType = payloadContentType;
    }

    public String getJson() {
        return json;
    }

    public Message json(String json) {
        this.json = json;
        return this;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Message latitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public Message longitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Integer getAltitude() {
        return altitude;
    }

    public Message altitude(Integer altitude) {
        this.altitude = altitude;
        return this;
    }

    public void setAltitude(Integer altitude) {
        this.altitude = altitude;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public Message batteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
        return this;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Device getSentBy() {
        return sentBy;
    }

    public Message sentBy(Device device) {
        this.sentBy = device;
        return this;
    }

    public void setSentBy(Device device) {
        this.sentBy = device;
    }

    public User getDeviceOwner() {
        return deviceOwner;
    }

    public Message deviceOwner(User user) {
        this.deviceOwner = user;
        return this;
    }

    public void setDeviceOwner(User user) {
        this.deviceOwner = user;
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
        Message message = (Message) o;
        if (message.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), message.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", type='" + getType() + "'" +
            ", fcnt=" + getFcnt() +
            ", network='" + getNetwork() + "'" +
            ", payload='" + getPayload() + "'" +
            ", payloadContentType='" + getPayloadContentType() + "'" +
            ", json='" + getJson() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", altitude=" + getAltitude() +
            ", batteryLevel=" + getBatteryLevel() +
            "}";
    }
}
