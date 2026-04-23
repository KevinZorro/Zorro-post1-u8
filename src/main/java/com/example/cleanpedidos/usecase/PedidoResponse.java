package com.example.cleanpedidos.usecase;

import java.math.BigDecimal;
import java.util.List;

public record PedidoResponse(
        String id,
        String clienteNombre,
        String estado,
        BigDecimal total,
        List<LineaPedidoResponse> lineas
) {
}
