import { useEffect, useState } from "react";
import api from "./api";
import keycloak from "./keycloak";

interface Product {
  skuCode: string;
  name: string;
  description: string;
  price: number;
  quantity: number;
}

function App() {
  const [products, setProducts] = useState<Product[]>([]);
  const [productForm, setProductForm] = useState<Product>({
    skuCode: "",
    name: "",
    description: "",
    price: 0,
    quantity: 0,
  });

  const [orderForm, setOrderForm] = useState({
    skuCode: "",
    quantity: 1,
    price: 0,
    userDetails: {
      email: "",
      firstName: "",
      lastName: "",
    },
  });

  const [inventoryStatus, setInventoryStatus] = useState<string | null>(null);

  useEffect(() => {
    loadProducts();
  }, []);

  const loadProducts = () => {
    api.get("/product")
      .then((res) => setProducts(res.data))
      .catch((err) => console.error(err));
  };

  const handleProductChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setProductForm((prev) => ({
      ...prev,
      [name]: name === "price" || name === "quantity" ? +value : value,
    }));
  };

  const handleOrderChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    if (["email", "firstName", "lastName"].includes(name)) {
      setOrderForm((prev) => ({
        ...prev,
        userDetails: {
          ...prev.userDetails,
          [name]: value,
        },
      }));
    } else {
      setOrderForm((prev) => ({
        ...prev,
        [name]: name === "price" || name === "quantity" ? +value : value,
      }));
    }
  };

  const createProduct = () => {
    api.post("/product", productForm).then(() => {
      alert("✅ Product created!");
      loadProducts();
    });
  };

  const placeOrder = () => {
    api.post("/order", orderForm).then(() => {
      alert("✅ Order placed!");
    });
  };

  const checkInventory = () => {
    api
      .get(`/inventory?skuCode=${orderForm.skuCode}&quantity=${orderForm.quantity}`)
      .then(() => setInventoryStatus("✅ Available"))
      .catch(() => setInventoryStatus("❌ Not available"));
  };

  return (
    <div style={{ padding: "2rem", background: "#121212", color: "#fff", fontFamily: "sans-serif" }}>
      <h1>Welcome, {keycloak.tokenParsed?.preferred_username}</h1>

      <div style={{ marginBottom: "1rem" }}>
        <button onClick={() => keycloak.register()}>🧾 Register</button>
        <button onClick={() => keycloak.login()}>🔐 Login</button>
        <button onClick={() => keycloak.logout()}>🚪 Logout</button>
      </div>

      <hr />

      <h2>📦 Create Product</h2>
      {["skuCode", "name", "description", "price", "quantity"].map((field) => (
        <input
          key={field}
          type={["price", "quantity"].includes(field) ? "number" : "text"}
          name={field}
          placeholder={field}
          value={(productForm as any)[field]}
          onChange={handleProductChange}
          style={{ margin: "0.5rem", padding: "0.5rem" }}
        />
      ))}
      <button onClick={createProduct}>➕ Add Product</button>

      <hr />

      <h2>🛒 Place Order</h2>
      <input
        type="text"
        name="skuCode"
        placeholder="skuCode"
        value={orderForm.skuCode}
        onChange={handleOrderChange}
        style={{ margin: "0.5rem", padding: "0.5rem" }}
      />
      <input
        type="number"
        name="quantity"
        placeholder="quantity"
        value={orderForm.quantity}
        onChange={handleOrderChange}
        style={{ margin: "0.5rem", padding: "0.5rem" }}
      />
      <input
        type="number"
        name="price"
        placeholder="price"
        value={orderForm.price}
        onChange={handleOrderChange}
        style={{ margin: "0.5rem", padding: "0.5rem" }}
      />
      <input
        type="text"
        name="email"
        placeholder="email"
        value={orderForm.userDetails.email}
        onChange={handleOrderChange}
        style={{ margin: "0.5rem", padding: "0.5rem" }}
      />
      <input
        type="text"
        name="firstName"
        placeholder="first name"
        value={orderForm.userDetails.firstName}
        onChange={handleOrderChange}
        style={{ margin: "0.5rem", padding: "0.5rem" }}
      />
      <input
        type="text"
        name="lastName"
        placeholder="last name"
        value={orderForm.userDetails.lastName}
        onChange={handleOrderChange}
        style={{ margin: "0.5rem", padding: "0.5rem" }}
      />
      <div>
        <button onClick={placeOrder}>✅ Place Order</button>
        <button onClick={checkInventory}>📦 Check Inventory</button>
        {inventoryStatus && <span style={{ marginLeft: "1rem" }}>{inventoryStatus}</span>}
      </div>

      <hr />

      <h2>📋 All Products</h2>
      <ul>
        {products.map((p, i) => (
          <li key={i}>
            <strong>{p.name}</strong> - {p.skuCode} (${p.price}) - {p.quantity} pcs
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;