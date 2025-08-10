import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import ProductList from "./components/ProductList";
import ProductForm from "./components/ProductForm";
import keycloak from "./api/keycloak";

function App() {
  return (
    <Router>
      <div style={{ padding: "2rem", background: "#121212", color: "#fff" }}>
        <nav>
          <Link to="/" style={{ marginRight: "1rem", color: "#0ff" }}>🛒 Products</Link>
          <Link to="/create" style={{ color: "#0ff" }}>➕ Add Product</Link>
        </nav>
        <div style={{ marginTop: "1rem" }}>
          <button onClick={() => keycloak.register()}>🧾 Register</button>
          <button onClick={() => keycloak.login()}>🔐 Login</button>
          <button onClick={() => keycloak.logout()}>🚪 Logout</button>
        </div>

        <Routes>
          <Route path="/" element={<ProductList />} />
          <Route path="/create" element={<ProductForm />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
