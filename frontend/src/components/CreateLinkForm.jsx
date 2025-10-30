// src/components/CreateLinkForm.jsx
import { useState } from "react";
import { createLink } from "../api";

export default function CreateLinkForm({ onCreated }) {
    const [targetUrl, setTargetUrl] = useState("");
    const [customCode, setCustomCode] = useState("");
    const [createdBy, setCreatedBy] = useState("manal");
    const [apiKey, setApiKey] = useState("dev-key");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function handleSubmit(e) {
        e.preventDefault();
        setLoading(true);
        setError("");
        try {
            const { data } = await createLink({ targetUrl, customCode, createdBy, apiKey });
            onCreated?.(data);
            setTargetUrl("");
            setCustomCode("");
        } catch (err) {
            setError(err?.response?.data?.detail || err.message);
        } finally {
            setLoading(false);
        }
    }

    return (
        <form onSubmit={handleSubmit} style={{ display: "grid", gap: 12 }}>
            <div>
                <label>Long URL</label>
                <input
                    type="url"
                    required
                    placeholder="https://example.org/very/long/link"
                    value={targetUrl}
                    onChange={(e) => setTargetUrl(e.target.value)}
                    style={{ width: "100%", padding: 10 }}
                />
            </div>
            <div style={{ display: "grid", gap: 8, gridTemplateColumns: "1fr 1fr", alignItems: "end" }}>
                <div>
                    <label>Custom code (optional)</label>
                    <input
                        placeholder="hello"
                        value={customCode}
                        onChange={(e) => setCustomCode(e.target.value)}
                        style={{ width: "100%", padding: 10 }}
                    />
                </div>
                <div>
                    <label>Created by</label>
                    <input
                        value={createdBy}
                        onChange={(e) => setCreatedBy(e.target.value)}
                        style={{ width: "100%", padding: 10 }}
                    />
                </div>
            </div>
            <div style={{ display: "grid", gap: 8, gridTemplateColumns: "1fr 1fr" }}>
                <div>
                    <label>API Key (Header: X-API-Key)</label>
                    <input
                        value={apiKey}
                        onChange={(e) => setApiKey(e.target.value)}
                        style={{ width: "100%", padding: 10 }}
                    />
                </div>
                <button disabled={loading} type="submit" style={{ padding: 12 }}>
                    {loading ? "Creating..." : "Create Short Link"}
                </button>
            </div>
            {error && (
                <div style={{ background: "#fee", border: "1px solid #f99", padding: 10, borderRadius: 8 }}>
                    {error}
                </div>
            )}
        </form>
    );
}
