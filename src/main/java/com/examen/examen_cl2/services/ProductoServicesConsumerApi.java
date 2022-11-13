package com.examen.examen_cl2.services;

import com.examen.examen_cl2.models.Producto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service("api")
public class ProductoServicesConsumerApi {

    private final String url = "http://localhost:8080/productos";



    public Producto findById(int id)
    {
        String ruta = url+"/" + id;
        RestTemplate res = new RestTemplate();
        Producto producto = res.getForObject(ruta, Producto.class);

        if (producto == null) new Producto();

        return producto;
    }

    public List<Producto> findAll(){

        String ruta = url;
        RestTemplate res = new RestTemplate();
        Producto[] productos = res.getForObject(ruta, Producto[].class);

        if (Objects.isNull(productos)) return Collections.emptyList();

        return Arrays.asList(productos);
    }

    public Producto save(Producto producto){

        String ruta = url;
        RestTemplate res = new RestTemplate();
        Producto respuesta = res.postForObject(ruta, producto, Producto.class);

        if (Objects.isNull(respuesta)) respuesta = new Producto();

        return respuesta;
    }
}
