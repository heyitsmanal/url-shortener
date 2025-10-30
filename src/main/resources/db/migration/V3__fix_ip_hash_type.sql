-- Make ip_hash a VARCHAR(64) so it matches the JPA mapping
ALTER TABLE clicks ALTER COLUMN ip_hash TYPE VARCHAR(64);
