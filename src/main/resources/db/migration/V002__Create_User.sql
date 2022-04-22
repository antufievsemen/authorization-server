CREATE TABLE "users"
(
    "id"         TEXT PRIMARY KEY,
    "client_id"  TEXT        NOT NULL,
    "created_at" TIMESTAMP   NULL,
    "session_id" TEXT UNIQUE NULL,
    "state"      TEXT UNIQUE NULL,

    CONSTRAINT "fk__users__clients__client_id" FOREIGN KEY ("client_id") REFERENCES "clients" ("id")
);