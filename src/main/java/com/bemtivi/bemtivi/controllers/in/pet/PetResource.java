package com.bemtivi.bemtivi.controllers.in.pet;

import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.order.dto.OrderDTO;
import com.bemtivi.bemtivi.controllers.in.pet.dto.PetDTO;
import com.bemtivi.bemtivi.controllers.in.pet.mappers.PetWebMapper;
import com.bemtivi.bemtivi.application.business.service.PetBusiness;
import com.bemtivi.bemtivi.exceptions.OperationNotAllowedException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/pets")
public class PetResource {
    private final PetBusiness petManager;
    private final PetWebMapper mapper;

    @GetMapping(value = "/paginacao")
    public ResponseEntity<PageResponseDTO<PetDTO>> findByPagination(
            @RequestParam(name = "isActive", defaultValue = "true", required = false)
            Boolean isActive,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false)
            @Min(value = 1, message = "O número mínimo de elementos da página é 1")
            @Max(value = 30, message = "O número máximo de elementos da página é 30")
            Integer pageSize,
            @RequestParam(name = "page", defaultValue = "0", required = false)
            Integer page,
            @RequestParam(name = "name", required = false)
            String name
    ) {
        return ResponseEntity.ok().body(
                mapper.mapToPageResponseDto(petManager.findByPagination(isActive, pageSize, page, name))
        );
    }

    @GetMapping(value = "/paginacaoporcliente")
    public ResponseEntity<PageResponseDTO<PetDTO>> findByActivationStatusIsActiveAndCustomerId(
            @RequestParam(name = "isActive", defaultValue = "true", required = false)
            Boolean isActive,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false)
            @Min(value = 1, message = "O número mínimo de elementos da página é 1")
            @Max(value = 30, message = "O número máximo de elementos da página é 30")
            Integer pageSize,
            @RequestParam(name = "page", defaultValue = "0", required = false)
            Integer page,
            @Valid @NotNull(message = "O ID do cliente não pode ser nulo")
            @RequestParam(name = "customerId")
            String customerId
    ) {
        return ResponseEntity.ok().body(mapper.mapToPageResponseDto(
                petManager.findByActivationStatus_IsActiveAndOwner_Id(isActive, customerId, pageSize, page)
        ));
    }

    @GetMapping(value = "/{id}/buscar")
    public ResponseEntity<PetDTO> findById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok().body(mapper.mapToDTO(petManager.findById(id)));
    }

    @PostMapping(value = "/inserir")
    public ResponseEntity<PetDTO> insert(
            @Validated(PetDTO.OnCreate.class) @RequestPart(value = "pet") PetDTO petDTO,
            @Valid @NotNull(message = "A imagem deve ser enviada.") @RequestPart(value = "file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            throw new OperationNotAllowedException(RuntimeErrorEnum.ERR0030);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.mapToDTO(petManager.insert(mapper.mapToDomain(petDTO), file))
        );
    }

    @PutMapping(value = "/{id}/atualizar")
    public ResponseEntity<PetDTO> update(
            @PathVariable(name = "id") String id,
            @Validated(PetDTO.OnUpdate.class) @RequestPart(value = "pet") PetDTO petDTO,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return ResponseEntity.ok().body(
                mapper.mapToDTO(petManager.update(id, mapper.mapToDomain(petDTO), file))
        );
    }

    @DeleteMapping(value = "/{id}/desativar")
    public ResponseEntity<Void> deactivate(@PathVariable(name = "id") String id) {
        petManager.deactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/{id}/deletar")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") String id) {
        petManager.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
