import { CategoryDto } from './category-dto';
import { MeasureDto } from './measure-dto';

export interface ProductDto {
  id: string;
  name: string;
  description: string;
  barcode: string;
  markup: number;
  category: CategoryDto;
  measure: MeasureDto;
  min_stock: number;
  alert_stock: boolean;
}
