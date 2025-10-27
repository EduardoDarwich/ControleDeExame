package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.dataTransferObject.logDTO.HistoryDTO;
import com.SCX.ControleDeExame.domain.appointment.Appointment;
import com.SCX.ControleDeExame.domain.log.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LogRepository extends JpaRepository<Log, UUID> {

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.logDTO.HistoryDTO(a.name, l.userAction, l.hour_event )
            from Log l
            join l.authId a
            where a.id = :authId
            """)
    List<HistoryDTO> findAllByAuth(@Param("authId") UUID authId);
}
