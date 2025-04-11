import { Expose } from 'class-transformer';

export class ResourceTypePayload {
  @Expose()
  id: number = -1;
  @Expose()
  name: string = '';
  @Expose()
  description: string = '';
  @Expose()
  icon: string = '';
}
