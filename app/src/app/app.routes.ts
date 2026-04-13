import { Routes } from '@angular/router';
import { CategoryComponent } from './pages/category-component/category-component';
import { MeasureComponent } from './pages/measure-component/measure-component';
import { ProductComponent } from './pages/product-component/product-component';
import { HomePage } from './pages/home-page/home-page';
import { PageNotFound } from './pages/page-not-found/page-not-found';
import { CreateEditProductComponent } from './pages/create-edit-product-component/create-edit-product-component';
import { CreateEditCustomers } from './pages/create-edit-customers/create-edit-customers';
import { Customers } from './pages/customers/customers';

export const routes: Routes = [
  { path: '', component: HomePage },
  { path: 'products', component: ProductComponent },
  { path: 'products/new', component: CreateEditProductComponent },
  { path: 'products/edit/:id', component: CreateEditProductComponent },
  { path: 'customers', component: Customers },
  { path: 'customers/new', component: CreateEditCustomers },
  { path: 'customers/edit/:id', component: CreateEditCustomers },
  { path: 'categories', component: CategoryComponent },
  { path: 'measures', component: MeasureComponent },
  { path: '**', component: PageNotFound },
];
