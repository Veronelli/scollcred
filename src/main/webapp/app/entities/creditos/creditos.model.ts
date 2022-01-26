import * as dayjs from 'dayjs';
import { IMutual } from 'app/entities/mutual/mutual.model';
import { ICliente } from 'app/entities/cliente/cliente.model';

export interface ICreditos {
  id?: number;
  monto?: number;
  pagoCuota?: number;
  cantidadCuotas?: number;
  tomado?: dayjs.Dayjs;
  inicioPago?: dayjs.Dayjs;
  mutual?: IMutual;
  cliente?: ICliente | null;
}
export interface CreditosDTO {
  credtios: ICreditos[];
  length: number;
}

export class Creditos implements ICreditos {
  constructor(
    public id?: number,
    public monto?: number,
    public pagoCuota?: number,
    public cantidadCuotas?: number,
    public tomado?: dayjs.Dayjs,
    public inicioPago?: dayjs.Dayjs,
    public mutual?: IMutual,
    public cliente?: ICliente | null
  ) {}
}

export function getCreditosIdentifier(creditos: ICreditos): number | undefined {
  return creditos.id;
}
