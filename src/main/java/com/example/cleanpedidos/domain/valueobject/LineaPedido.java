package com.example.cleanpedidos.domain.valueobject;

import java.math.BigDecimal;

// domain/valueobject/LineaPedido.java — value object inmutable
public record LineaPedido(String productoNombre, int cantidad, Dinero
precioUnitario) {
 public Dinero subtotal() {
 return new Dinero(precioUnitario.cantidad()
 .multiply(BigDecimal.valueOf(cantidad)));
 }
}