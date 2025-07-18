package com.bemtivi.bemtivi.persistence.entities.jpa.appointment;

import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.payment.PixEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.pet.PetEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.service.ServiceEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_agendamentos")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "agendamento_id")
    private String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "data_e_hora", nullable = false)
    private LocalDateTime dateTime;
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false, referencedColumnName = "cliente_id")
    private CustomerEntity customer;
    @Column(name = "preco", nullable = false)
    private BigDecimal price;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status_de_pagamento", nullable = false)
    private PaymentStatusEnum paymentStatus;
    @Column(name = "pagamento_id")
    private Long paymentId;
    @ManyToOne
    @JoinColumn(name = "servico_id", referencedColumnName = "servico_id")
    private ServiceEntity service;
    @ManyToOne
    @JoinColumn(name = "pet_id", referencedColumnName = "pet_id")
    private PetEntity pet;
    @Column(name = "metodo_pagamento_pix", nullable = false)
    private Boolean methodPaymentByPix;
    @Embedded
    private PixEntity pix;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
