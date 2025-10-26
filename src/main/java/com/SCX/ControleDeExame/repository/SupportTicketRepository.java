package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.supportTicket.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, UUID> {
}
