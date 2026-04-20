import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, firstValueFrom } from 'rxjs';
import { Pedido } from '../models/pedido';
import { environment } from '../../../environments/environment'; 
//prd
//import { environment } from '../../../environments/environment'; 

@Injectable({
  providedIn: 'root'
})
export class PedidoService {
  private http = inject(HttpClient);

  // 1. Envia a string de 40 caracteres para o Módulo A (POST)
  enviarPosicional(linhaPosicional: string): Observable<Pedido> {
    return this.http.post<Pedido>(`${environment.moduloAUrl}/posicional`, linhaPosicional, {
      headers: { 'Content-Type': 'text/plain' }
    });
  }

  // 2. Busca a lista de pedidos do Módulo B (GET)
  listarPedidos(): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(environment.moduloBUrl);
  }

  // 3. Acorda os servidores no Render
  acordarApis(): Promise<any> {
      return Promise.allSettled([
        firstValueFrom(this.http.get(environment.moduloAUrl)),
        firstValueFrom(this.http.get(environment.moduloBUrl))
      ]);
    }
}