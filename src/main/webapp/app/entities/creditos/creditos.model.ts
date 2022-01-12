import * as dayjs from 'dayjs';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { IMutual } from 'app/entities/mutual/mutual.model';

export interface ICreditos {
  id?: number;
  emisionCuotas?: number;
  monto?: number;
  pagoCuota?: number;
  cantidadCuotas?: number;
  tomado?: dayjs.Dayjs;
  inicioPago?: dayjs.Dayjs;
  cliente?: ICliente;
  mutual?: IMutual;
}

export class Creditos implements ICreditos {
  constructor(
    public id?: number,
    public emisionCuotas?: number,
    public monto?: number,
    public pagoCuota?: number,
    public cantidadCuotas?: number,
    public tomado?: dayjs.Dayjs,
    public inicioPago?: dayjs.Dayjs,
    public cliente?: ICliente,
    public mutual?: IMutual
  ) {}
}

export function getCreditosIdentifier(creditos: ICreditos): number | undefined {
  return creditos.id;
}
