import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { PedidoService } from '../../core/services/pedido.service';
import { Pedido } from '../../core/models/pedido';

@Component({
  selector: 'app-lista-pedidos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './lista-pedidos.component.html',
  styleUrls: ['./lista-pedidos.component.css']
})
export class ListaPedidosComponent implements OnInit {
  private pedidoService = inject(PedidoService);
  
  pedidos: Pedido[] = [];
  filtroStatus: string = ''; 

  ngOnInit() {
    this.carregarPedidos();
  }

  carregarPedidos() {
    this.pedidoService.listarPedidos().subscribe({
      next: (dados) => {
        this.pedidos = dados.sort((a, b) => (b.id || 0) - (a.id || 0));
      },
      error: (err) => console.error('Erro ao buscar pedidos:', err)
    });
  }

  get pedidosFiltrados() {
    if (!this.filtroStatus) return this.pedidos;
    return this.pedidos.filter(p => p.status === this.filtroStatus);
  }
}