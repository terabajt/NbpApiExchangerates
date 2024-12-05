package com.pasynekmichal.repository;

import com.pasynekmichal.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
