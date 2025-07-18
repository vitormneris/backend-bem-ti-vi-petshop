package com.bemtivi.bemtivi.persistence.entities.jpa.customer;

import com.bemtivi.bemtivi.application.enums.UserRoleEnum;
import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.appointment.AppointmentEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.comment.CommentEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.order.OrderEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.pet.PetEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_clientes")
public class CustomerEntity {
    @Id
    @Column(name = "cliente_id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "nome", nullable = false, length = 100)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String cpf;
    @Column(name="email_esta_ativo" , nullable = false)
    private Boolean isEmailActive;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "papel_do_usuario", nullable = false)
    private UserRoleEnum role;
    @Column(name = "senha", nullable = false, length = 100)
    private String password;
    @Column(name = "data_de_nascimento", nullable = false)
    private LocalDate birthDate;
    @Column(name = "caminho_da_imagem", nullable = false)
    private String pathImage;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "telefone_id", referencedColumnName = "telefone_id")
    private TelephoneEntity telephones;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id", referencedColumnName = "endereco_id")
    private AddressEntity address;
    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    private Set<PetEntity> pets;
    @OneToMany(mappedBy = "customer", orphanRemoval = true)
    private Set<OrderEntity> orders;
    @OneToMany(mappedBy = "customer", orphanRemoval = true)
    private Set<AppointmentEntity> appointments;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<CommentEntity> comments;
    @Column(name = "codigo_email")
    private String codeForEmail;
    @Column(name = "email_pretendido")
    private String emailIntended;
    @Column(name = "codigo_senha")
    private String codeForPassword;
    @Column(name = "senha_pretendida")
    private String codeIntended;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
