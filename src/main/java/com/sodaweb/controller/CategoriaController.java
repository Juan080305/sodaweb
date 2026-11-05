package com.sodaweb.controller;
import com.sodaweb.domain.Categoria;
import com.sodaweb.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller @RequestMapping("/admin/categorias") public class CategoriaController {
 private final CategoriaService service;
 public CategoriaController(CategoriaService service){this.service=service;}
 @GetMapping public String listar(Model model){model.addAttribute("categorias",service.listar());return "categorias/lista";}
 @GetMapping("/nueva") public String nueva(Model model){model.addAttribute("categoria",new Categoria());return "categorias/formulario";}
 @GetMapping("/{id}/editar") public String editar(@PathVariable Long id,Model model){model.addAttribute("categoria",service.buscar(id));return "categorias/formulario";}
 @PostMapping("/guardar") public String guardar(@Valid @ModelAttribute("categoria") Categoria categoria,BindingResult result,Model model,RedirectAttributes redirect){
  if(result.hasErrors())return "categorias/formulario";
  try{service.guardar(categoria);redirect.addFlashAttribute("mensaje","Categoría guardada correctamente");return "redirect:/admin/categorias";}
  catch(IllegalArgumentException ex){model.addAttribute("error",ex.getMessage());return "categorias/formulario";}
 }
 @PostMapping("/{id}/desactivar") public String desactivar(@PathVariable Long id,RedirectAttributes redirect){service.desactivar(id);redirect.addFlashAttribute("mensaje","Categoría desactivada sin borrar sus relaciones");return "redirect:/admin/categorias";}
 @PostMapping("/{id}/activar") public String activar(@PathVariable Long id,RedirectAttributes redirect){service.activar(id);redirect.addFlashAttribute("mensaje","Categoría activada");return "redirect:/admin/categorias";}
}
