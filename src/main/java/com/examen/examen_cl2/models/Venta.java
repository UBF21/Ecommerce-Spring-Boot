package com.examen.examen_cl2.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Table(name = "venta")
@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_venta;
    private double total;
    private Date fecha_emision;
    private int id_producto;
    private int id_usuario;


    public double calcularTotal(Producto producto){
        double total = 0.0;
        total = (producto.getCantidad() * producto.getPrecio());

        return total;
    }
    public int decrementoStock(Producto producto)
    {
        int stock = 0;
        stock = (producto.getStock() - producto.getCantidad());
        return stock;
    }

    public static Venta ventaCreada(Producto producto,Usuario usuario){
        Venta venta = new Venta();

        //producto.setCantidad(1);
        producto.setStock(venta.decrementoStock(producto));

        venta.setId_producto(producto.getId());
        venta.setFecha_emision(new Date());
        venta.setTotal(venta.calcularTotal(producto));
        venta.setId_usuario(usuario.getId_usuario());

        return venta;
    }
}
