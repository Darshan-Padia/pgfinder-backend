package com.pgfinder.Service;

import com.pgfinder.Model.Tenant;
import com.pgfinder.Repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;

    @Autowired
    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    public Optional<Tenant> getTenantById(Long id) {
        return tenantRepository.findById(id);
    }

    public Tenant saveOrUpdateTenant(Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    public void deleteTenant(Long id) {
        tenantRepository.deleteById(id);
    }
}
