package com.bemtivi.bemtivi.controllers.in.administrator.dto;

import com.bemtivi.bemtivi.application.enums.UserRoleEnum;
import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import jakarta.validation.constraints.*;

public record AdministratorDTO(
        String id,
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, message = "O campo nome está muito curto.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 100, message = "O campo nome está muito longo.")
        @NotNull(groups = {OnCreate.class}, message = "O campo nome deve ser preenchido.")
        String name,
        @Email(groups = {OnCreate.class, OnUpdate.class}, message = "O campo e-mail está em formato inválido")
        @NotBlank(groups = {OnCreate.class}, message = "O campo e-mail deve ser preenchido.")
        String email,
        Boolean isEmailActive,
        String pathImage,
        UserRoleEnum role,
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 8, message = "O campo senha está muito curto.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 100, message = "O camp senha está muito longo.")
        @NotNull(groups = {OnCreate.class}, message = "O campo senha deve ser preenchido.")
        String password,
        ActivationStatusDTO activationStatus
) {
        public interface OnCreate {}
        public interface OnUpdate {}
}
