import { Routes } from '@angular/router';
import { ListaPedidosComponent } from './pages/lista-pedidos/lista-pedidos.component';
import { NovoPedidoComponent } from './pages/novo-pedido/novo-pedido.component';

export const routes: Routes = [
  { path: '', redirectTo: 'pedidos', pathMatch: 'full' },
  { path: 'pedidos', component: ListaPedidosComponent },
  { path: 'novo-pedido', component: NovoPedidoComponent },
];