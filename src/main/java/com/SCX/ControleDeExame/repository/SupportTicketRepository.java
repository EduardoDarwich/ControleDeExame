package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.dataTransferObject.supportTicketDTO.ResponseSupportDTO;
import com.SCX.ControleDeExame.domain.appointment.Appointment;
import com.SCX.ControleDeExame.domain.supportTicket.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, UUID> {
   SupportTicket findByAuthId_Id(UUID id);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.supportTicketDTO.ResponseSupportDTO(
            s.id,
            s.subject,
            s.message,
            s.response,
            s.finished
            )
            from SupportTicket s
            where authId = :userId
            """)
    List<ResponseSupportDTO> findByUser(@Param("userId") UUID userId);
}
