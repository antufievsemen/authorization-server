CREATE TABLE "clients"
(
    "id"            TEXT PRIMARY KEY,
    "client_id"     TEXT NOT NULL UNIQUE,
    "client_secret" TEXT NOT NULL UNIQUE,
    "created_at"    TIMESTAMP NULL,
    "updated_at"    TIMESTAMP NULL
);