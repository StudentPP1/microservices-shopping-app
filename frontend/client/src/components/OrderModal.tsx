import { useState } from "react";
import api from "../api/api";
import keycloak from "../api/keycloak";

interface OrderModalProps {
    skuCode: string;
    price: number;
    onClose: () => void;
}

export default function OrderModal({ skuCode, price, onClose }: OrderModalProps) {
    const [quantity, setQuantity] = useState(1);

    const placeOrder = () => {
        const order = {
            skuCode,
            quantity,
            price,
            userDetails: {
                email: keycloak.tokenParsed?.email,
                firstName: keycloak.tokenParsed?.given_name,
                lastName: keycloak.tokenParsed?.family_name,
            },
        };

        api.post("/order", order).then(() => {
            alert("✅ Order placed!");
            onClose();
        });
    };

    return (
        <div style={{
            position: "fixed", top: 0, left: 0, width: "100%", height: "100%",
            background: "rgba(0,0,0,0.6)", display: "flex", justifyContent: "center", alignItems: "center"
        }}>
            <div style={{ background: "#222", padding: "2rem", borderRadius: "1rem", color: "#fff" }}>
                <h3>🛒 Order for <strong>{skuCode}</strong></h3>
                <p>Price per item: ${price}</p>
                <input
                    type="number"
                    value={quantity}
                    min={1}
                    onChange={(e) => setQuantity(+e.target.value)}
                    style={{ padding: "0.5rem", marginBottom: "1rem" }}
                />
                <div>
                    <button onClick={placeOrder} style={{ marginRight: "1rem" }}>✅ Confirm</button>
                    <button onClick={onClose}>❌ Cancel</button>
                </div>
            </div>
        </div>
    );
}
