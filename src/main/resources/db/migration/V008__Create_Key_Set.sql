CREATE TABLE "key_sets"
(
    "id"          TEXT PRIMARY KEY,
    "client_id"   TEXT  NOT NULL UNIQUE,
    "token_type"  TEXT  NOT NULL,
    "alg"         TEXT  NOT NULL,
    "private_key" BYTEA NOT NULL,
    "public_key"  BYTEA NOT NULL,
    "created_at"  TIMESTAMP DEFAULT now(),

    CONSTRAINT "fk__key_sets__client_id" FOREIGN KEY ("client_id") REFERENCES "clients" ("client_id")
)