package com.example.cleanpedidos.config;

import com.example.cleanpedidos.usecase.CrearPedidoUseCase;
import com.example.cleanpedidos.usecase.ConsultarPedidoUseCase;
import com.example.cleanpedidos.usecase.port.PedidoRepositoryPort;
import com.example.cleanpedidos.usecase.impl.CrearPedidoService;
import com.example.cleanpedidos.usecase.impl.ConsultarPedidoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// config/PedidoConfiguration.java
@Configuration
public class PedidoConfiguration {
    @Bean
    public CrearPedidoUseCase crearPedidoUseCase(PedidoRepositoryPort repo) {
        return new CrearPedidoService(repo);
    }

    @Bean
    public ConsultarPedidoUseCase consultarPedidoUseCase(PedidoRepositoryPort repo) {
        return new ConsultarPedidoService(repo);
    }
}
