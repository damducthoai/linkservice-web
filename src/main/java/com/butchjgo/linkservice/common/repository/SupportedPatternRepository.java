package com.butchjgo.linkservice.common.repository;

import com.butchjgo.linkservice.common.entity.SupportedPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "supportedPatternRepository")
public interface SupportedPatternRepository extends JpaRepository<SupportedPattern, String> {
}
