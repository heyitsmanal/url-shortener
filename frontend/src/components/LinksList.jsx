// src/components/LinksList.jsx
import { useEffect, useState } from "react";
import { listLinks, shortUrl, getStats } from "../api";

export default function LinksList() {
    const [q, setQ] = useState("");
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const [data, setData] = useState({ content: [], totalPages: 0, totalElements: 0 });
    const [loading, setLoading] = useState(false);
    const [stats, setStats] = useState(null);
    const [statsLoading, setStatsLoading] = useState(false);
    const [error, setError] = useState("");

    async function load() {
        setLoading(true);
        setError("");
        try {
            const { data } = await listLinks({ q, page, size });
            setData(data);
        } catch (e) {
            setError(e?.response?.data?.detail || e.message);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => { load(); }, [page, size]); // first load
    // explicit search
    function handleSearch(e) { e.preventDefault(); setPage(0); load(); }

    async function openStats(code) {
        setStatsLoading(true);
        try {
            const { data } = await getStats(code);
            setStats({ code, ...data });
        } catch (e) {
            setStats({ error: e?.response?.data?.detail || e.message });
        } finally {
            setStatsLoading(false);
        }
    }

    return (
        <section style={{ marginTop: 24 }}>
            <form onSubmit={handleSearch} style={{ display: "flex", gap: 8 }}>
                <input
                    placeholder="Search by code or target URL…"
                    value={q}
                    onChange={(e) => setQ(e.target.value)}
                    style={{ flex: 1, padding: 10 }}
                />
                <button type="submit">Search</button>
            </form>

            {loading ? <p style={{ marginTop: 12 }}>Loading…</p> : null}
            {error && <p style={{ color: "crimson" }}>{error}</p>}

            <ul style={{ listStyle: "none", padding: 0, marginTop: 16 }}>
                {data.content?.map((l) => (
                    <li key={l.id} style={{ padding: 12, border: "1px solid #eee", borderRadius: 10, marginBottom: 10 }}>
                        <div style={{ display: "flex", justifyContent: "space-between", gap: 8, alignItems: "center" }}>
                            <div>
                                <div><strong>{l.code}</strong> → <a href={shortUrl(l.code)} target="_blank">{shortUrl(l.code)}</a></div>
                                <div style={{ fontSize: 13, opacity: 0.8, wordBreak: "break-all" }}>{l.targetUrl}</div>
                                <div style={{ fontSize: 12, opacity: 0.7 }}>createdBy: {l.createdBy ?? "—"} | clicks: {l.totalClicks ?? 0}</div>
                            </div>
                            <div style={{ display: "flex", gap: 8 }}>
                                <button type="button" onClick={() => navigator.clipboard.writeText(window.location.origin + shortUrl(l.code))}>Copy</button>
                                <button type="button" onClick={() => openStats(l.code)} disabled={statsLoading}>
                                    {statsLoading && stats?.code === l.code ? "Loading…" : "Stats"}
                                </button>
                            </div>
                        </div>
                    </li>
                ))}
            </ul>

            <div style={{ display: "flex", gap: 8, alignItems: "center" }}>
                <button disabled={page <= 0} onClick={() => setPage(p => p - 1)}>Prev</button>
                <span>Page {page + 1} / {Math.max(1, data.totalPages || 1)}</span>
                <button disabled={page + 1 >= (data.totalPages || 1)} onClick={() => setPage(p => p + 1)}>Next</button>
                <select value={size} onChange={(e) => { setSize(Number(e.target.value)); setPage(0); }}>
                    {[5,10,20,50].map(n => <option key={n} value={n}>{n} / page</option>)}
                </select>
            </div>

            {stats && (
                <div style={{ marginTop: 16, padding: 12, border: "1px solid #ddd", borderRadius: 10 }}>
                    {stats.error ? (
                        <div style={{ color: "crimson" }}>Error: {stats.error}</div>
                    ) : (
                        <>
                            <h3 style={{ margin: 0 }}>Stats — {stats.code}</h3>
                            <div style={{ fontSize: 14, marginTop: 6 }}>Total clicks: {stats.totalClicks}</div>
                            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 8, marginTop: 10 }}>
                                <div>
                                    <strong>Daily</strong>
                                    <ul>
                                        {stats.daily?.map(d => (
                                            <li key={d.date}>{d.date}: {d.count}</li>
                                        ))}
                                    </ul>
                                </div>
                                <div>
                                    <strong>Top Referrers</strong>
                                    <ul>
                                        {stats.topReferrers?.map(r => (
                                            <li key={r.referrer || "(direct)"}>{r.referrer || "(direct)"}: {r.count}</li>
                                        ))}
                                    </ul>
                                </div>
                            </div>
                        </>
                    )}
                </div>
            )}
        </section>
    );
}
