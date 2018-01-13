package com.butchjgo.linkservice.common.repository;

import com.butchjgo.linkservice.common.domain.AccountInfo;
import com.butchjgo.linkservice.common.domain.AccountEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "accountRepository")
public interface AccountRepository extends JpaRepository<AccountInfo, AccountEntityPK> {
}
