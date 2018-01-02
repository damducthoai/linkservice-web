package com.butchjgo.linkservice.common.repository;

import com.butchjgo.linkservice.common.entity.BadURL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "badURLRepository")
public interface BadURLRepository extends JpaRepository<BadURL, String> {

}
