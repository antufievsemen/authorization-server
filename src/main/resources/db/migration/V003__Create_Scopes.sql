CREATE TABLE "scope"
(
    "id"    TEXT PRIMARY KEY,
    "scope" TEXT NOT NULL
);

ALTER TABLE "client"
    ADD
        CONSTRAINT "fk__client__scope_id" FOREIGN KEY ("scope_id") REFERENCES "scope" ("id");