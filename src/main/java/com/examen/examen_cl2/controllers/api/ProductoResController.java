package com.examen.examen_cl2.controllers.api;

import com.examen.examen_cl2.models.Producto;
import com.examen.examen_cl2.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
public class ProductoResController {

    @Autowired
    ProductoRepository productoRepository;


    @GetMapping("/productos")
    List<Producto> listarProductos() {
        List<Producto> productos = productoRepository.findAll();
        return productos;
    }

    @GetMapping("/productos/{id}")
    Producto obtenerProducto(@PathVariable int id) {
        Optional<Producto> producto = productoRepository.findById(id);

        return producto.orElseGet(Producto::new);
    }


    @PostMapping("/productos")
    Producto agregarProducto(@RequestBody Producto producto){
        return productoRepository.save(producto);
    }


    @PutMapping("/productos/{id}")
    Producto actualizarProducto(@RequestBody Producto producto,@PathVariable int id){

        return productoRepository.save(producto);
    }

    @DeleteMapping("/productos/{id}")
    void eliminarProducto(@PathVariable int id){
        productoRepository.deleteById(id);
    }
}
