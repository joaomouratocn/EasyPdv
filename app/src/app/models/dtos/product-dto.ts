export interface ProductDto {
  id: string;
  name: string;
  description: string;
  barcode: string;
  category_id: string;
  measure_id: string;
  stock: number;
  buy_price: number;
  sale_price: number;
  min_stock: number;
  alert_stock: boolean;
}
