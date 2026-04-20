import { Component, inject } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PedidoService } from './core/services/pedido.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private pedidoService = inject(PedidoService);

  apiPronta = false;
  acordandoApi = false;

  acordarApi() {
    this.acordandoApi = true;
    
    // Faz o ping no Módulo A e B para ligar os containers no Render
    this.pedidoService.acordarApis().then(() => {
      this.apiPronta = true;
      this.acordandoApi = false;
    }).catch(() => {
      // teste dsv
      this.apiPronta = true;
      this.acordandoApi = false;
    });
  }
}