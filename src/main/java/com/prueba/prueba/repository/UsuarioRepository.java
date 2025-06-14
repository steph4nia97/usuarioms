package com.prueba.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.prueba.model.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
