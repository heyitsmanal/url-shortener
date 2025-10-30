// src/api.js
import axios from "axios";

const base = import.meta.env.VITE_API_BASE_URL || ""; // '' when using Vite proxy
const api = axios.create({
    baseURL: base,
    headers: { "Content-Type": "application/json" },
});

// --- Endpoints ---
export async function getApiDocs() {
    return api.get("/v3/api-docs");
}

export async function createLink({ targetUrl, customCode, createdBy, apiKey }) {
    return api.post(
        "/api/v1/links",
        { targetUrl, customCode, createdBy },
        { headers: { "X-API-Key": apiKey } }
    );
}

export async function listLinks({ q = "", page = 0, size = 10 } = {}) {
    const params = {};
    if (q) params.q = q;
    params.page = page;
    params.size = size;
    return api.get("/api/v1/links", { params });
}

export async function getStats(code) {
    return api.get(`/api/v1/links/${encodeURIComponent(code)}/stats`);
}

export function shortUrl(code) {
    return `/r/${encodeURIComponent(code)}`;
}

export default api;
