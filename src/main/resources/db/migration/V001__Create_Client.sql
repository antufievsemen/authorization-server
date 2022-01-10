CREATE TABLE "client"
(
    "id"            TEXT PRIMARY KEY,
    "client_id"     TEXT NOT NULL,
    "client_secret" TEXT NOT NULL,
    "scope_id"      TEXT NOT NULL,
    "redirect_uri"  TEXT NULL
);