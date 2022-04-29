CREATE TABLE "key_sets"
(
    "kid"         TEXT PRIMARY KEY,
    "client_id"   TEXT  NOT NULL UNIQUE,
    "alg"         TEXT  NOT NULL,
    "kty"         TEXT  NOT NULL,
    "use"         TEXT  NOT NULL,
    "private_key" BYTEA NOT NULL,
    "e"           BYTEA  NOT NULL,
    "n"           BYTEA  NOT NULL,

    CONSTRAINT "fk__key_sets__client_id" FOREIGN KEY ("client_id") REFERENCES "clients" ("client_id")
)