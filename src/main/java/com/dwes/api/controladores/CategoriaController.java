package com.dwes.api.controladores;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dwes.api.entidades.Categoria;
import com.dwes.api.errores.CategoriaNotFoundException;
import com.dwes.api.servicios.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v2/categorias")
public class CategoriaController {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Obtener todas las categorías", description = "Devuelve una lista paginada de categorías")
    @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente")
    @ApiResponse(responseCode = "204", description = "No hay categorías disponibles")
    @ApiResponse(responseCode = "400", description = "Parámetros de solicitud incorrectos")
    public ResponseEntity<?> getAllCategorias(Pageable pageable) {
        logger.info("## getAllCategorias ##");
        Page<Categoria> page = categoriaService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una categoría por ID", description = "Devuelve una categoría específica por su ID")
    @ApiResponse(responseCode = "200", description = "Categoría encontrada",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class)) })
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id) {
        logger.info("## getCategoriaById ## id:({})", id);
        Categoria categoria = categoriaService.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException("Categoría con ID " + id + " no encontrada"));
        return ResponseEntity.ok(categoria);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una categoría", description = "Actualiza los detalles de una categoría existente")
    @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada para actualizar")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        logger.info("## actualizarCategoria id({}) ##", id);
        if (!categoriaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoria.setId(id);
        Categoria categoriaActualizada = categoriaService.save(categoria);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Borrar una categoría", description = "Elimina una categoría existente por su ID")
    @ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada para eliminar")
    public ResponseEntity<Void> borrarCategoria(@PathVariable Long id) {
        logger.info("## borrarCategoria id:{} ##", id);
        if (!categoriaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una categoría", description = "Actualiza parcialmente los detalles de una categoría")
    @ApiResponse(responseCode = "200", description = "Categoría actualizada parcialmente")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada para actualización parcial")
    public ResponseEntity<Categoria> actualizarParcialmenteCategoria(@PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        if (!categoriaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Categoria categoriaActual = categoriaService.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException("Categoría con ID " + id + " no encontrada"));

        if (updates.containsKey("nombre")) {
            categoriaActual.setNombre((String) updates.get("nombre"));
        }
        if (updates.containsKey("descripcion")) {
            categoriaActual.setDescripcion((String) updates.get("descripcion"));
        }

        Categoria categoriaActualizada = categoriaService.save(categoriaActual);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva categoría", description = "Crea una nueva categoría y la guarda en la base de datos")
    @ApiResponse(responseCode = "201", description = "Categoría creada con éxito")
    @ApiResponse(responseCode = "400", description = "Datos proporcionados para la nueva categoría son inválidos")
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria nuevaCategoria) {
        logger.info("## crearCategoria ##");
        Categoria categoriaCreada = categoriaService.save(nuevaCategoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaCreada);
    }
}