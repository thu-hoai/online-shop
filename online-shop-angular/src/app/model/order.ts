interface Order {
  orderId: number;
  userId: number;
  orderStatusCode: string;
  orderItem: OrderItem[];
  orderDate: string;
  orderAmount: number;
}

interface OrderItem {
  productId: number;
  productName: string;
  productStock: number;
  price: number;
  orderId: number;
  status: string;
  quantity: number;
}


