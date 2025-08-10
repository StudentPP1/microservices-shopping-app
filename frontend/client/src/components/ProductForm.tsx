import { useState } from "react";
import api from "../api/api";

interface Product {
  skuCode: string;
  name: string;
  description: string;
  price: number;
  quantity: number;
}

export default function ProductForm() {
  const [productForm, setProductForm] = useState<Product>({
    skuCode: "",
    name: "",
    description: "",
    price: 0,
    quantity: 0,
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setProductForm((prev) => ({
      ...prev,
      [name]: name === "price" || name === "quantity" ? +value : value,
    }));
  };

  const createProduct = () => {
    if (!productForm.name || !productForm.skuCode) {
      alert("🔴 Fill in required fields");
      return;
    }

    api.post("/product", productForm).then(() => {
      alert("✅ Product created!");
      setProductForm({ skuCode: "", name: "", description: "", price: 0, quantity: 0 });
    });
  };

  return (
    <div style={{
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      minHeight: "100vh",
      backgroundColor: "#1e1e1e",
    }}>
      <form style={{
        background: "#2c2c2c",
        padding: "2rem",
        borderRadius: "1rem",
        width: "100%",
        maxWidth: "400px",
        boxShadow: "0 0 20px rgba(0, 0, 0, 0.5)",
        color: "#fff",
        display: "flex",
        flexDirection: "column",
        gap: "1rem",
      }}>
        <h2 style={{ textAlign: "center" }}>📦 Create Product</h2>

        {["skuCode", "name", "description", "price", "quantity"].map((field) => (
          <div key={field} style={{ display: "flex", flexDirection: "column" }}>
            <label htmlFor={field} style={{ marginBottom: "0.3rem", fontSize: "0.9rem", color: "#bbb" }}>
              {field[0].toUpperCase() + field.slice(1)}
            </label>
            <input
              id={field}
              type={["price", "quantity"].includes(field) ? "number" : "text"}
              name={field}
              placeholder={field}
              value={(productForm as any)[field]}
              onChange={handleChange}
              style={{
                padding: "0.6rem",
                borderRadius: "0.5rem",
                border: "1px solid #444",
                backgroundColor: "#1a1a1a",
                color: "#fff",
                outline: "none",
                transition: "0.2s border",
              }}
              onFocus={(e) => e.currentTarget.style.border = "1px solid #00bcd4"}
              onBlur={(e) => e.currentTarget.style.border = "1px solid #444"}
            />
          </div>
        ))}

        <button
          type="button"
          onClick={createProduct}
          style={{
            padding: "0.8rem",
            backgroundColor: "#00bcd4",
            color: "#000",
            fontWeight: "bold",
            borderRadius: "0.5rem",
            border: "none",
            cursor: "pointer",
            transition: "0.2s background",
          }}
          onMouseOver={(e) => e.currentTarget.style.backgroundColor = "#00acc1"}
          onMouseOut={(e) => e.currentTarget.style.backgroundColor = "#00bcd4"}
        >
          ➕ Create
        </button>
      </form>
    </div>
  );
}
