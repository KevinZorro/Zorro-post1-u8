package com.example.cleanpedidos.adapter.in.web;

import com.example.cleanpedidos.usecase.LineaPedidoDto;
import java.util.List;

public record CrearPedidoRequest(String clienteNombre, List<LineaPedidoDto> lineas) {
}
