-- Links table stores the short code and the target URL
CREATE TABLE IF NOT EXISTS links (
                                     id UUID PRIMARY KEY,
                                     code VARCHAR(16) NOT NULL UNIQUE,
    target_url TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100)
    );

-- Clicks table records every redirect event
CREATE TABLE IF NOT EXISTS clicks (
                                      id BIGSERIAL PRIMARY KEY,
                                      link_id UUID NOT NULL REFERENCES links(id) ON DELETE CASCADE,
    ts TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    ip_hash CHAR(64) NOT NULL,
    user_agent TEXT,
    referrer TEXT
    );

-- Helpful indexes for analytics
CREATE INDEX IF NOT EXISTS idx_links_code ON links(code);
CREATE INDEX IF NOT EXISTS idx_clicks_link_ts ON clicks(link_id, ts DESC);
CREATE INDEX IF NOT EXISTS idx_clicks_referrer ON clicks(referrer);
