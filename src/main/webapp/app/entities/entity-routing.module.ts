import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cliente',
        data: { pageTitle: 'Clientes' },
        loadChildren: () => import('./cliente/cliente.module').then(m => m.ClienteModule),
      },
      {
        path: 'creditos',
        data: { pageTitle: 'Creditos' },
        loadChildren: () => import('./creditos/creditos.module').then(m => m.CreditosModule),
      },
      {
        path: 'mutual',
        data: { pageTitle: 'Mutuals' },
        loadChildren: () => import('./mutual/mutual.module').then(m => m.MutualModule),
      },
      {
        path: 'dependencia',
        data: { pageTitle: 'Dependencias' },
        loadChildren: () => import('./dependencia/dependencia.module').then(m => m.DependenciaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
