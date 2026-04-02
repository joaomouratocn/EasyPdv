import { CategoryDto } from "./category-dto";
import { MeasureDto } from "./measure-dto";

export interface ProductDto {
  id: string;
  name: string;
  description: string;
  barcode: string;
  category: CategoryDto;
  measure: MeasureDto;
  stock: number;
  buy_price: number;
  sale_price: number;
  min_stock: number;
  alert_stock: boolean;
}
