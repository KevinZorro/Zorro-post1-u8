package com.example.cleanpedidos.usecase;

import com.example.cleanpedidos.domain.valueobject.PedidoId;
import java.util.List;

// usecase/CrearPedidoUseCase.java
public interface CrearPedidoUseCase {
    PedidoId ejecutar(String clienteNombre, List<LineaPedidoDto> lineas);
}