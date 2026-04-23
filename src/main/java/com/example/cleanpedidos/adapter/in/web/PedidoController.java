package com.example.cleanpedidos.adapter.in.web;

import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.CrearPedidoUseCase;
import com.example.cleanpedidos.usecase.ConsultarPedidoUseCase;
import com.example.cleanpedidos.usecase.PedidoResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// adapter/in/web/PedidoController.java
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
 private final CrearPedidoUseCase crearUseCase;
 private final ConsultarPedidoUseCase consultarUseCase;
 public PedidoController(CrearPedidoUseCase crearUseCase,
 ConsultarPedidoUseCase consultarUseCase) {
 this.crearUseCase = crearUseCase;
 this.consultarUseCase = consultarUseCase;
 }
 @PostMapping
 @ResponseStatus(HttpStatus.CREATED)
 public Map<String, String> crear(@RequestBody CrearPedidoRequest req)
{
 PedidoId id = crearUseCase.ejecutar(
 req.clienteNombre(), req.lineas());
 return Map.of("pedidoId", id.toString());
 }
 @GetMapping("/{id}")
 public PedidoResponse buscar(@PathVariable String id) {
 return consultarUseCase.buscarPorId(new
PedidoId(UUID.fromString(id)));
 }
 @GetMapping
 public List<PedidoResponse> listar() {
 return consultarUseCase.listarTodos();
 }
}
