interface Product {
  productId: number;
  productName: string;
  productDescription: string;
  categoryCode: string;
  price: number;
  productStock: number;
}

interface Products {
  content: Product[];
  totalPages: number;
  totalElements: number;
  first: boolean;
  last: boolean;
}

