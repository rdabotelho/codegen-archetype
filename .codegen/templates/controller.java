package com.example.demo.controller;

import com.example.demo.domain.dto.DomainNameDTO;
import com.example.demo.domain.mapper.DomainNameMapper;
import com.example.demo.service.DomainNameService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Transactional
public class DomainNameController {

    private final Logger log = LoggerFactory.getLogger(DomainNameController.class);

    @Autowired
    private DomainNameService domainNameService;

    @PostMapping("/domainNames")
    public ResponseEntity<DomainNameDTO> createDomainName(@RequestBody DomainNameDTO domainName) throws Exception {
        log.debug("REST request to save domainName : {}", domainName);
        if (domainName.getId() != null) {
            throw new RuntimeException("A new domainName cannot already have an ID");
        }
        DomainNameDTO result = domainNameService.createDomainName(DomainNameMapper.INSTANCE.toEntity(domainName))
                .map(it -> DomainNameMapper.INSTANCE.toDTO(it))
                .get();
        return ResponseEntity
                .created(new URI("/api/domainNames/" + result.getId()))
                .body(result);
    }

    @PutMapping("/domainNames")
    public ResponseEntity<DomainNameDTO> updateDomainName(@RequestBody DomainNameDTO domainName) {
        log.debug("REST request to update domainName : {}", domainName);
        if (domainName.getId() == null) {
            throw new RuntimeException("Invalid id");
        }
        return domainNameService.getDomainNameById(domainName.getId())
                .map(it -> ResponseEntity.ok().body(DomainNameMapper.INSTANCE.toDTO(domainNameService.updateDomainName(it).get())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/domainNames")
    public List<DomainNameDTO> getAllDomainNames() {
        log.debug("REST request to get all domainNames");
        return domainNameService.getAllDomainNames().stream()
                .map(it -> DomainNameMapper.INSTANCE.toDTO(it))
                .collect(Collectors.toList());
    }

    @GetMapping("/domainNames/{id}")
    public ResponseEntity<DomainNameDTO> getDomainName(@PathVariable Long id) {
        log.debug("REST request to get domainName : {}", id);
        return domainNameService.getDomainNameById(id)
                .map(it -> ResponseEntity.ok().body(DomainNameMapper.INSTANCE.toDTO(domainNameService.updateDomainName(it).get())))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/domainNames/{id}")
    public ResponseEntity<Void> deleteDomainName(@PathVariable Long id) {
        log.debug("REST request to delete domainName : {}", id);
        domainNameService.deleteDomainName(id);
        return ResponseEntity.noContent().build();
    }
    
}