package com.sodaweb.controller;

import com.sodaweb.domain.Pedido;
import com.sodaweb.service.CarritoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;
    private final MessageSource messageSource;

    public CarritoController(CarritoService carritoService, MessageSource messageSource) {
        this.carritoService = carritoService;
        this.messageSource = messageSource;
    }

    // HU-03 y HU-04: muestra "Tu pedido" con cantidades, subtotales, total y datos para el retiro
    @GetMapping("/listado")
    public String listado(HttpSession session, Model model) {
        var items = carritoService.getItems(session);
        model.addAttribute("items", items);
        model.addAttribute("total", carritoService.getTotal(items));
        if (!model.containsAttribute("pedido")) { // Si hubo un error se conservan los datos digitados
            model.addAttribute("pedido", new Pedido());
        }
        return "/carrito/listado";
    }

    // Devuelve solo el fragmento con la cantidad, se carga en el encabezado (resultBlock)
    @GetMapping("/contador")
    public String contador(HttpSession session, Model model) {
        model.addAttribute("cantidad", carritoService.getCantidad(session));
        return "/carrito/fragmentos :: contador";
    }

    @PostMapping("/agregar")
    public String agregar(@RequestParam Integer idProducto,
            @RequestParam(required = false) String origen,
            HttpSession session, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = mensaje("carrito.agregado");
        try {
            carritoService.agregar(session, idProducto);
        } catch (IllegalArgumentException | IllegalStateException e) {
            titulo = "error";
            mensaje = e.getMessage();
        }
        redirectAttributes.addFlashAttribute(titulo, mensaje);
        // Se regresa a la página del menú desde donde se agregó el producto
        if (origen != null && origen.startsWith("/") && !origen.startsWith("//")) {
            return "redirect:" + origen;
        }
        return "redirect:/";
    }

    @PostMapping("/actualizar")
    public String actualizar(@RequestParam Integer idProducto, @RequestParam Integer cantidad,
            HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            carritoService.actualizar(session, idProducto, cantidad);
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carrito/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idProducto, HttpSession session,
            RedirectAttributes redirectAttributes) {
        carritoService.eliminar(session, idProducto);
        redirectAttributes.addFlashAttribute("todoOk", mensaje("carrito.eliminado"));
        return "redirect:/carrito/listado";
    }

    private String mensaje(String clave) {
        return messageSource.getMessage(clave, null, LocaleContextHolder.getLocale());
    }
}
