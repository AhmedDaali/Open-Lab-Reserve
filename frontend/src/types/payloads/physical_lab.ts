import { Expose } from 'class-transformer';

export class PhysicalLab {
  @Expose()
  id: number = -1;
  @Expose()
  name: string = '';
  @Expose()
  description: string = '';
  @Expose()
  createBookingGroupIds: number[] = [];
  @Expose()
  cancelBookingGroupIds: number[] = [];
  @Expose()
  managerGroupIds: number[] = [];
  @Expose()
  facilityId: number = -1;
}
