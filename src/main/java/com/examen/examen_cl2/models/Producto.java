package com.examen.examen_cl2.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Table(name = "producto")
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private int id;

    @NotBlank(message = "El Campo es requerido.")
    @Size(max = 200)
    private String nombre;

    @Min(value = 1,message = "El Precio debe ser minimo 1.")
    private double precio;

    private int cantidad;

    @Min(value = 1,message = "El Stock debe ser minimo 1 .")
    private int stock;
    private String img;
    private String estado;



}
