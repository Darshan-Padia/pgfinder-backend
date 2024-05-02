package com.pgfinder.Controller;

import com.pgfinder.Model.Tenant;
import com.pgfinder.Service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    @Autowired
    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    public ResponseEntity<List<Tenant>> getAllTenants() {
        List<Tenant> tenants = tenantService.getAllTenants();
        return new ResponseEntity<>(tenants, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tenant> getTenantById(@PathVariable("id") Long id) {
        Optional<Tenant> tenant = tenantService.getTenantById(id);
        return tenant.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Tenant> createTenant(@RequestBody Tenant tenant) {
        Tenant savedTenant = tenantService.saveOrUpdateTenant(tenant);
        return new ResponseEntity<>(savedTenant, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tenant> updateTenant(@PathVariable("id") Long id, @RequestBody Tenant tenant) {
        Optional<Tenant> existingTenant = tenantService.getTenantById(id);
        if (existingTenant.isPresent()) {
            tenant.setTenantId(id);
            Tenant updatedTenant = tenantService.saveOrUpdateTenant(tenant);
            return new ResponseEntity<>(updatedTenant, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenant(@PathVariable("id") Long id) {
        Optional<Tenant> tenant = tenantService.getTenantById(id);
        if (tenant.isPresent()) {
            tenantService.deleteTenant(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
