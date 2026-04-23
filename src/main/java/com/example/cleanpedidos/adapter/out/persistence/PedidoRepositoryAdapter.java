package com.example.cleanpedidos.adapter.out.persistence;

import com.example.cleanpedidos.domain.entity.Pedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.port.PedidoRepositoryPort;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

// adapter/out/persistence/PedidoRepositoryAdapter.java
@Component
public class PedidoRepositoryAdapter implements PedidoRepositoryPort {
    private final Map<String, Pedido> persistence = new ConcurrentHashMap<>();

    @Override
    public void guardar(Pedido pedido) {
        persistence.put(pedido.getId().toString(), pedido);
    }

    @Override
    public Optional<Pedido> buscarPorId(PedidoId id) {
        return Optional.ofNullable(persistence.get(id.toString()));
    }

    @Override
    public List<Pedido> buscarTodos() {
        return new ArrayList<>(persistence.values());
    }
}
