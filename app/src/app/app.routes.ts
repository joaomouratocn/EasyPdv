import { Routes } from '@angular/router';
import { CategoryComponent } from './pages/category-component/category-component';
import { MeasureComponent } from './pages/measure-component/measure-component';
import { ProductComponent } from './pages/product-component/product-component';
import { HomePage } from './pages/home-page/home-page';
import { PageNotFound } from './pages/page-not-found/page-not-found';
import { CreateEditProductComponent } from './pages/create-edit-product-component/create-edit-product-component';

export const routes: Routes = [
  { path: '', component: HomePage },
  { path: 'products', component: ProductComponent },
  { path: 'products/new', component: CreateEditProductComponent },
  { path: 'products/edit/:id', component: CreateEditProductComponent },
  { path: 'categories', component: CategoryComponent },
  { path: 'measures', component: MeasureComponent },
  { path: '**', component: PageNotFound },
];
