import { IDependencia } from 'app/entities/dependencia/dependencia.model';

export interface ICliente {
  id?: number;
  nombre?: string;
  apellido?: string;
  telefono?: string | null;
  dni?: string | null;
  dependencia?: IDependencia | null;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public nombre?: string,
    public apellido?: string,
    public telefono?: string | null,
    public dni?: string | null,
    public dependencia?: IDependencia | null
  ) {}
}

export function getClienteIdentifier(cliente: ICliente): number | undefined {
  return cliente.id;
}
