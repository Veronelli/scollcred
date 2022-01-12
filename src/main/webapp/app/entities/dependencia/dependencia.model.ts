import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IDependencia {
  id?: number;
  empleador?: string;
  clientes?: ICliente[] | null;
}

export class Dependencia implements IDependencia {
  constructor(public id?: number, public empleador?: string, public clientes?: ICliente[] | null) {}
}

export function getDependenciaIdentifier(dependencia: IDependencia): number | undefined {
  return dependencia.id;
}
