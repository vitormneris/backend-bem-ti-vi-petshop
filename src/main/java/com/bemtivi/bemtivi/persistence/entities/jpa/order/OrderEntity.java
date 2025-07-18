package com.bemtivi.bemtivi.persistence.entities.jpa.order;

import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.payment.PixEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_pedidos")
public class OrderEntity {
    @Id
    @Column(name = "pedido_id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT-3")
    @Column(name = "momento", nullable = false)
    private Instant moment;
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false, referencedColumnName = "cliente_id")
    private CustomerEntity customer;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false)
    private PaymentStatusEnum paymentStatus;
    @Column(name = "preco_total", nullable = false)
    private BigDecimal totalPrice;
    @Column(name = "pagamento_id")
    private Long paymentId;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pedido_id")
    private List<OrderItemEntity> orderItems;
    @Column(name = "entregar_endereco", nullable = false)
    private Boolean deliverToAddress;
    @Column(name = "metodo_pagamento_pix", nullable = false)
    private Boolean methodPaymentByPix;
    @Embedded
    private PixEntity pix;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
