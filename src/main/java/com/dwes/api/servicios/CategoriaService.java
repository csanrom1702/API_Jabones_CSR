package com.dwes.api.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dwes.api.entidades.Categoria;
import com.dwes.api.entidades.Producto;

public interface CategoriaService {
	Page<Categoria> findAll(Pageable pageable);
    Optional<Categoria> findById(Long categoriaId);
    Categoria save(Categoria categoria);
    void deleteById(Long categoriaId);
    List<Producto> findProductosByCategoriaId(Long categoriaId);
	boolean existsById(Long id);
}
