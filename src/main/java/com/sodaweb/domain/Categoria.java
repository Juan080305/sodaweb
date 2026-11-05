package com.sodaweb.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="categorias") public class Categoria {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank(message="El nombre es obligatorio") @Size(max=100) @Column(nullable=false,length=100,unique=true) private String nombre;
 @Size(max=255) private String descripcion;
 @Column(nullable=false) private boolean activa=true;
 public Long getId(){return id;} public void setId(Long id){this.id=id;}
 public String getNombre(){return nombre;} public void setNombre(String nombre){this.nombre=nombre;}
 public String getDescripcion(){return descripcion;} public void setDescripcion(String descripcion){this.descripcion=descripcion;}
 public boolean isActiva(){return activa;} public void setActiva(boolean activa){this.activa=activa;}
}
