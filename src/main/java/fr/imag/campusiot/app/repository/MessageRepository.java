package fr.imag.campusiot.app.repository;

import fr.imag.campusiot.app.domain.Message;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Message entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select message from Message message where message.deviceOwner.login = ?#{principal.username}")
    List<Message> findByDeviceOwnerIsCurrentUser();

}
