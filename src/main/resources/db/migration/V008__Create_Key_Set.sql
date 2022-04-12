CREATE TABLE "key_set"
(
    "id"          TEXT PRIMARY KEY,
    "client_id"   TEXT,
    "token_type"  TEXT NOT NULL,
    "alg"         TEXT NOT NULL,
    "private_key" TEXT NOT NULL,
    "public_key"  TEXT NOT NULL,
    "created_at"  TIMESTAMP DEFAULT now(),

    CONSTRAINT "fk__key_sets__client_id" FOREIGN KEY ("client_id") REFERENCES "client" ("id")
)