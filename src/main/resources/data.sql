BEGIN;

-- The source names MUST be the same as those defined in VulnSource.java
-- The config keys MUST be the same as those defined in VulnSourceConfig.ConfigKey
-- It's recommended to encrypt sensitive data before saving to database. See encryption.md
INSERT INTO vuln_source_config(source, import_enabled, import_cron, subscribe_enabled, query_enabled, config) VALUES
('OSV', true, '0 0 2 * * ? *', true, true, '{}'::jsonb),
('NVD', true, '0 0 2 * * ? *', true, true, '{}'::jsonb),
('OSS_INDEX', true, '0 0 2 * * ? *', true, true, '{}'::jsonb),
('VTOPIA', false, null, false, false, '{}'::jsonb),
('PRISM_7_CAI', false, null, false, false, '{}'::jsonb)
ON CONFLICT (source) DO NOTHING;

COMMIT;
