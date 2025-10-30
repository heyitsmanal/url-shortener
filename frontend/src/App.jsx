// src/App.jsx
import { useEffect, useState } from "react";
import { getApiDocs } from "./api";
import CreateLinkForm from "./components/CreateLinkForm";
import LinksList from "./components/LinksList";

export default function App() {
    const [status, setStatus] = useState("Checking /v3/api-docs…");
    const [error, setError] = useState("");
    const [refreshKey, setRefreshKey] = useState(0);

    useEffect(() => {
        getApiDocs()
            .then(() => { setStatus("✅ Backend reachable"); setError(""); })
            .catch((e) => { setStatus("❌ Backend not reachable"); setError(e?.message || "Unknown error"); });
    }, []);

    return (
        <main style={{ fontFamily: "system-ui, sans-serif", padding: 24, maxWidth: 900, margin: "40px auto" }}>
            <h1 style={{ marginBottom: 8 }}>Short your URL </h1>


            <section style={{ marginTop: 24 }}>
                <h2>Create a short link</h2>
                <CreateLinkForm onCreated={() => setRefreshKey(k => k + 1)} />
            </section>

            <section style={{ marginTop: 24 }}>
                <h2>Your links</h2>
                {/* re-render when a link is created */}
                <div key={refreshKey}>
                    <LinksList />
                </div>
            </section>


        </main>
    );
}
