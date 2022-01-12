export interface IMutual {
  id?: number;
  nombre?: string;
}

export class Mutual implements IMutual {
  constructor(public id?: number, public nombre?: string) {}
}

export function getMutualIdentifier(mutual: IMutual): number | undefined {
  return mutual.id;
}
