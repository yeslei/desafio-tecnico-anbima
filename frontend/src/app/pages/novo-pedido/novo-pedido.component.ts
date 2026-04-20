import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PedidoService } from '../../core/services/pedido.service';

@Component({
  selector: 'app-novo-pedido',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './novo-pedido.component.html',
  styleUrls: ['./novo-pedido.component.css']
})
export class NovoPedidoComponent {
  private pedidoService = inject(PedidoService);

  linhaPosicional: string = '';
  jsonResultado: any = null;
  erro: string = '';
  enviando: boolean = false;

  enviar() {
  this.enviando = true;
  this.jsonResultado = null;
  this.erro = '';

  this.pedidoService.enviarPosicional(this.linhaPosicional).subscribe({
    next: (res) => {
      this.jsonResultado = res;
      this.linhaPosicional = '';
      this.enviando = false;
    },
    error: (err) => {
      this.enviando = false;
      
      if (typeof err.error === 'string') {
        this.erro = err.error;
      } 

      else if (err.error && err.error.message) {
        this.erro = err.error.message;
      }

      else {
        this.erro = 'Não foi possível conectar ao Módulo A. Verifique se o servidor está rodando.';
      }
      
      console.error('Erro detalhado:', err);
    }
  });
}
}