package com.sodaweb.controller;

import com.sodaweb.domain.Pedido;
import com.sodaweb.service.CarritoService;
import com.sodaweb.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    private final PedidoService pedidoService;
    private final CarritoService carritoService;
    private final MessageSource messageSource;

    public PedidoController(PedidoService pedidoService, CarritoService carritoService,
            MessageSource messageSource) {
        this.pedidoService = pedidoService;
        this.carritoService = carritoService;
        this.messageSource = messageSource;
    }

    // HU-04: confirma el pedido para retiro con nombre, teléfono y método de pago
    @PostMapping("/confirmar")
    public String confirmar(@Valid Pedido pedido, BindingResult bindingResult,
            HttpSession session, RedirectAttributes redirectAttributes) {
        var items = carritoService.getItems(session);
        if (items.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", mensaje("pedido.error01"));
            return "redirect:/carrito/listado";
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", mensaje("pedido.error02"));
            redirectAttributes.addFlashAttribute("pedido", pedido);
            return "redirect:/carrito/listado";
        }
        try {
            pedido = pedidoService.registrar(pedido, items);
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("pedido", pedido);
            return "redirect:/carrito/listado";
        }
        carritoService.vaciar(session);
        // Solo quien hizo el pedido puede ver su confirmación
        session.setAttribute("ultimoPedido", pedido.getIdPedido());
        return "redirect:/pedido/recibido/" + pedido.getIdPedido();
    }

    @GetMapping("/recibido/{idPedido}")
    public String recibido(@PathVariable("idPedido") Integer idPedido, HttpSession session, Model model) {
        var pedidoOptional = pedidoService.getPedido(idPedido);
        if (pedidoOptional.isEmpty() || !idPedido.equals(session.getAttribute("ultimoPedido"))) {
            return "redirect:/";
        }
        model.addAttribute("pedido", pedidoOptional.get());
        return "/pedido/recibido";
    }

    private String mensaje(String clave) {
        return messageSource.getMessage(clave, null, LocaleContextHolder.getLocale());
    }
}
