package com.example.cleanpedidos.usecase;

import java.math.BigDecimal;

public record LineaPedidoDto(String productoNombre, int cantidad, BigDecimal precioUnitario) {
}
