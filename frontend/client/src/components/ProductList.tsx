import { useEffect, useState } from "react";
import api from "../api/api";
import keycloak from "../api/keycloak";
import OrderModal from "./OrderModal";

interface Product {
  skuCode: string;
  name: string;
  description: string;
  price: number;
  quantity: number;
}

export default function ProductList() {
  const [products, setProducts] = useState<Product[]>([]);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);

  useEffect(() => {
    api.get("/product")
      .then((res) => setProducts(res.data))
      .catch(console.error);
  }, []);

  console.info("ProductList rendered with products:", products);
  
  return (
    <div style={{ padding: "2rem", color: "#fff" }}>
      <h1>Welcome, {keycloak.tokenParsed?.preferred_username}</h1>
      <h2>📋 All Products</h2>

      <ul>
        {products.map((p, i) => (
          <li
            key={i}
            onClick={() => setSelectedProduct(p)}
            style={{ cursor: "pointer", padding: "0.5rem", marginBottom: "0.5rem", border: "1px solid #555" }}
          >
            <strong>{p.name}</strong> - {p.skuCode} (${p.price}) — {p.quantity} pcs
          </li>
        ))}
      </ul>

      {selectedProduct && (
        <OrderModal
          skuCode={selectedProduct.skuCode}
          price={selectedProduct.price}
          onClose={() => setSelectedProduct(null)}
        />
      )}
    </div>
  );
}
