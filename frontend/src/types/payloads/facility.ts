import { Expose } from 'class-transformer';

export class FacilityPayload {
  @Expose()
  id: number = -1;
  @Expose()
  name: string = "";
  @Expose()
  description: string = "";
  @Expose()
  createBookingGroupIds: number[] = [];
  @Expose()
  cancelBookingGroupIds: number[] = [];
  @Expose()
  managerGroupIds: number[] = [];
  @Expose()
  physicalLabIds: number[] = [];
}
