package org.example.repository;

import org.example.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

}
