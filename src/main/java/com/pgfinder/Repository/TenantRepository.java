package com.pgfinder.Repository;

import com.pgfinder.Model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    // Add custom query methods if needed
    Tenant findByTenantId(Long tenantId);
}
