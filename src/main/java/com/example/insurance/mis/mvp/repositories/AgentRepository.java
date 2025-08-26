
package com.example.insurance.mis.mvp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.insurance.mis.mvp.entities.Agent;

public interface AgentRepository extends JpaRepository<Agent, Long>{
    Optional<Agent> findByEmail(String email);
}
