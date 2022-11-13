package com.examen.examen_cl2.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "usuario")
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_usuario;
    private String username;
    private String password;

    @Column(name = "id_Rol")
    private int id_rol;

    @OneToOne
    @JoinColumn(name ="id_rol", updatable = false,insertable = false)
    private Rol rol;

}

