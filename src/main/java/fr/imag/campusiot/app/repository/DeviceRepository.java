package fr.imag.campusiot.app.repository;

import fr.imag.campusiot.app.domain.Device;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Device entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query("select device from Device device where device.createdBy.login = ?#{principal.username}")
    List<Device> findByCreatedByIsCurrentUser();

}
