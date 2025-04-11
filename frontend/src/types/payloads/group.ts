import { Expose } from 'class-transformer';

export class GroupPayload {
  @Expose()
  id: number = -1;

  @Expose()
  name: string = '';

  @Expose()
  description: string = '';
}
