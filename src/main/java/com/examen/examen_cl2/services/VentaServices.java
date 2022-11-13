package com.examen.examen_cl2.services;

import com.examen.examen_cl2.models.Venta;
import com.examen.examen_cl2.repositories.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ventaServices")
public class VentaServices {

    @Autowired
    VentaRepository ventaRepository;

    public void guardarVenta(Venta venta){
        ventaRepository.save(venta);
    }
}
