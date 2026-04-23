package com.example.cleanpedidos.usecase.impl;

import com.example.cleanpedidos.domain.entity.Pedido;
import com.example.cleanpedidos.domain.valueobject.LineaPedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.ConsultarPedidoUseCase;
import com.example.cleanpedidos.usecase.LineaPedidoResponse;
import com.example.cleanpedidos.usecase.PedidoResponse;
import com.example.cleanpedidos.usecase.port.PedidoRepositoryPort;
import java.util.List;
import java.util.stream.Collectors;

public class ConsultarPedidoService implements ConsultarPedidoUseCase {
    private final PedidoRepositoryPort repo;

    public ConsultarPedidoService(PedidoRepositoryPort repo) {
        this.repo = repo;
    }

    @Override
    public PedidoResponse buscarPorId(PedidoId id) {
        return repo.buscarPorId(id)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + id));
    }

    @Override
    public List<PedidoResponse> listarTodos() {
        return repo.buscarTodos().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private PedidoResponse toResponse(Pedido pedido) {
        List<LineaPedidoResponse> lineas = pedido.getLineas().stream()
                .map(this::toLineaResponse)
                .collect(Collectors.toList());

        return new PedidoResponse(
                pedido.getId().toString(),
                pedido.getClienteNombre(),
                pedido.getEstado().name(),
                pedido.calcularTotal().cantidad(),
                lineas
        );
    }

    private LineaPedidoResponse toLineaResponse(LineaPedido linea) {
        return new LineaPedidoResponse(
                linea.productoNombre(),
                linea.cantidad(),
                linea.precioUnitario().cantidad(),
                linea.subtotal().cantidad()
        );
    }
}
