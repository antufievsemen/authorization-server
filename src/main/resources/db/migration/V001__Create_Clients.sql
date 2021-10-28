CREATE TABLE "clients"
(
    "id"            TEXT PRIMARY KEY,
    "client_secret" TEXT NOT NULL,
    "grant_type_id" TEXT NOT NULL,
    "scope_id"      TEXT NOT NULL,

    CONSTRAINT "fk__client__grant_type_id" FOREIGN KEY ("grant_type_id") REFERENCES "grant_types" ("id")
)