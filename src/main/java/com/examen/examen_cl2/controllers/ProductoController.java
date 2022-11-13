package com.examen.examen_cl2.controllers;

import com.examen.examen_cl2.models.Producto;
import com.examen.examen_cl2.models.Usuario;
import com.examen.examen_cl2.models.Venta;
import com.examen.examen_cl2.repositories.UsuarioRepository;
import com.examen.examen_cl2.services.ProductoServicesConsumerApi;
import com.examen.examen_cl2.services.VentaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RequestMapping("/producto")
@Controller
public class ProductoController {

    @Autowired
    VentaServices ventaServices;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    ProductoServicesConsumerApi api;


    @GetMapping("/listar")
    public String listarProductos(Model model, @AuthenticationPrincipal User user) {
        List<Producto> listado = api.findAll();
        model.addAttribute("cantidad", listado.size());
        model.addAttribute("listado", listado);
        return "listado";
    }

    @GetMapping("/cargarForm")
    public String cargarFormulario(Model model) {
        model.addAttribute("producto", new Producto());
        return "form";
    }

    @PostMapping("/guardar")
    public String guardarFormulario(Model model, @Validated @ModelAttribute(name = "producto") Producto producto, BindingResult bindingResult, @RequestParam("imagen") MultipartFile imagen) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("producto", producto);
            return "form";
        }

        producto.setCantidad(0);

        saveIMG(imagen, producto);
        Producto respuesta = api.save(producto);
        return validarGuardarProducto(producto, model);


    }

    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable int id, Model model) {
        Producto producto = api.findById(id);
        model.addAttribute("producto", producto);
        return "form";
    }

    @PostMapping("/comprar")
    public String comprarProducto(@RequestParam int id, @RequestParam(value = "cantidad", required = false) int cantidad, Model model, @AuthenticationPrincipal User user) {

        Producto producto = api.findById(id);

        if (cantidad > producto.getStock()) {
            return "redirect:/producto/detalle-compra/" + id + "?errorCantidad";
        }

        if (cantidad == 0) {
            return "redirect:/producto/detalle-compra/" + id + "?errorCantidadZero";
        }

        producto.setCantidad(cantidad);
        Usuario usuario = usuarioRepository.findByUsername(user.getUsername());

        Venta venta = Venta.ventaCreada(producto, usuario);

        ventaServices.guardarVenta(venta);

        Producto respuesta = api.save(producto);

        return validarCompraProducto(respuesta);

    }


    @GetMapping("/detalle-compra/{id}")

    public String detalleCompra(Model model, @PathVariable int id, @RequestParam(value = "success", required = false) String success, @RequestParam(value = "error", required = false) String error, @RequestParam(value = "errorCantidad", required = false) String errorCantidad, @RequestParam(name = "errorCantidadZero",required = false) String errorCantidadZero) {

        Producto producto = api.findById(id);

        model.addAttribute("producto", producto);

        validarDetalleCompra(model, success, error, errorCantidad,errorCantidadZero);

        return "detalle-compra";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarFormulario(@PathVariable int id) {
        Producto producto = api.findById(id);
        producto.setEstado("I");

        Producto respuesta = api.save(producto);
        return "redirect:/producto/listar";
    }


    //region Metodos Auxiliares
    public void validarDetalleCompra(Model model, String success, String error, String errorCantidad, String errorCantidadZero) {
        if (success != null) {
            model.addAttribute("success", "Se Compró correctamente");
        } else if (error != null) {
            model.addAttribute("error", "Error al realizar la compra.");
        } else if (errorCantidad != null) {
            model.addAttribute("errorCantidad", "La cantidad es mayor al stock.");
        } else if (errorCantidadZero != null) {
            model.addAttribute("errorCantidadZero","Ingrese un cantidad a comprar.");
        }
    }

    public void saveIMG(MultipartFile imagen, Producto producto) {

        if (!imagen.isEmpty()) {
            //Path directorioImagenes = Paths.get("src//main//resources//static/img");
            String rutaAbsoluta = "D://DAWII//img";

            try {
                byte[] byteImg = imagen.getBytes();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + imagen.getOriginalFilename());
                Files.write(rutaCompleta, byteImg);
                producto.setImg(imagen.getOriginalFilename());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {

            Producto obj = api.findById(producto.getId());
            producto.setImg(obj.getImg());
        }
    }

    public String validarCompraProducto(Producto respuesta) {

        String redirect;

        if (respuesta != null) {
            System.out.println("Respuesta de producto actualizado al realizar la compra ==> " + respuesta);
            redirect = "redirect:/producto/detalle-compra/" + respuesta.getId() + "?success";
        } else {
            System.out.println("Respuesta de producto no actualizado al realizar la compra ==> " + respuesta);
            redirect = "redirect:/producto/detalle-compra/" + respuesta.getId() + "?error";
        }
        return redirect;
    }

    public String validarGuardarProducto(Producto respuesta, Model model) {
        String vista = "form";

        if (respuesta != null) {
            model.addAttribute("success", "Se guardó correctamente.");
            System.out.println("Respuesta de producto Insertado ==> " + respuesta);
            return vista;
        } else {
            model.addAttribute("error", "Se guardó correctamente.");
            System.out.println("Respuesta de producto Insertado ==> " + respuesta);
            return vista;
        }
    }
    //endregion
}
