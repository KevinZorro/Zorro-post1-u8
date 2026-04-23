package com.example.cleanpedidos.usecase;

import java.math.BigDecimal;

public record LineaPedidoResponse(
        String productoNombre,
        int cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
