package com.xpto.efood.web.rest;

import com.xpto.efood.repository.PedidoRepository;
import com.xpto.efood.service.PedidoService;
import com.xpto.efood.service.dto.PedidoDTO;
import com.xpto.efood.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.xpto.efood.domain.Pedido}.
 */
@RestController
@RequestMapping("/api")
public class PedidoResource {

    private final Logger log = LoggerFactory.getLogger(PedidoResource.class);

    private static final String ENTITY_NAME = "pedido";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PedidoService pedidoService;

    private final PedidoRepository pedidoRepository;

    public PedidoResource(PedidoService pedidoService, PedidoRepository pedidoRepository) {
        this.pedidoService = pedidoService;
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * {@code POST  /pedidos} : Create a new pedido.
     *
     * @param pedidoDTO the pedidoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pedidoDTO, or with status {@code 400 (Bad Request)} if the pedido has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pedidos")
    public ResponseEntity<PedidoDTO> createPedido(@RequestBody PedidoDTO pedidoDTO) throws URISyntaxException {
        log.debug("REST request to save Pedido : {}", pedidoDTO);
        if (pedidoDTO.getId() != null) {
            throw new BadRequestAlertException("A new pedido cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PedidoDTO result = pedidoService.save(pedidoDTO);
        return ResponseEntity
            .created(new URI("/api/pedidos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pedidos/:id} : Updates an existing pedido.
     *
     * @param id the id of the pedidoDTO to save.
     * @param pedidoDTO the pedidoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pedidoDTO,
     * or with status {@code 400 (Bad Request)} if the pedidoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pedidoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pedidos/{id}")
    public ResponseEntity<PedidoDTO> updatePedido(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PedidoDTO pedidoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pedido : {}, {}", id, pedidoDTO);
        if (pedidoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pedidoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pedidoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PedidoDTO result = pedidoService.save(pedidoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pedidoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pedidos/:id} : Partial updates given fields of an existing pedido, field will ignore if it is null
     *
     * @param id the id of the pedidoDTO to save.
     * @param pedidoDTO the pedidoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pedidoDTO,
     * or with status {@code 400 (Bad Request)} if the pedidoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pedidoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pedidoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pedidos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PedidoDTO> partialUpdatePedido(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PedidoDTO pedidoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pedido partially : {}, {}", id, pedidoDTO);
        if (pedidoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pedidoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pedidoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PedidoDTO> result = pedidoService.partialUpdate(pedidoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pedidoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pedidos} : get all the pedidos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pedidos in body.
     */
    @GetMapping("/pedidos")
    public List<PedidoDTO> getAllPedidos() {
        log.debug("REST request to get all Pedidos");
        return pedidoService.findAll();
    }

    /**
     * {@code GET  /pedidos/:id} : get the "id" pedido.
     *
     * @param id the id of the pedidoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pedidoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pedidos/{id}")
    public ResponseEntity<PedidoDTO> getPedido(@PathVariable Long id) {
        log.debug("REST request to get Pedido : {}", id);
        Optional<PedidoDTO> pedidoDTO = pedidoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pedidoDTO);
    }

    /**
     * {@code DELETE  /pedidos/:id} : delete the "id" pedido.
     *
     * @param id the id of the pedidoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pedidos/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        log.debug("REST request to delete Pedido : {}", id);
        pedidoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
