import { Routes } from '@angular/router';
import { ListaPedidosComponent } from './pages/lista-pedidos/lista-pedidos.component';

export const routes: Routes = [
  { path: '', redirectTo: 'pedidos', pathMatch: 'full' },
  { path: 'pedidos', component: ListaPedidosComponent },
];