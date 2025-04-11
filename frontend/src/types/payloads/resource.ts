import { ResourceTypePayload } from 'src/types/payloads/resource_type';
import { Expose } from 'class-transformer';

export class ResourcePayload {
  @Expose()
  id: number = -1;
  @Expose()
  specification: string = '';
  @Expose()
  type: ResourceTypePayload = new ResourceTypePayload();
}
