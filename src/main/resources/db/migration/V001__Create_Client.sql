CREATE TABLE "client"
(
    "id"            TEXT PRIMARY KEY,
    "client_id"     TEXT NOT NULL,
    "client_secret" TEXT NOT NULL,
    "created_at"    TIMESTAMP NULL,
    "updated_at"    TIMESTAMP NULL
);