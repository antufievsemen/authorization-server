CREATE TABLE "callbacks"
(
    "id"         TEXT PRIMARY KEY,
    "url"        TEXT NOT NULL,
    "created_at" TIMESTAMP DEFAULT now()
);

CREATE TABLE "client_callbacks"
(
    "client_id"   TEXT NOT NULL,
    "callback_id" TEXT NOT NULL,

    CONSTRAINT "pk__clients_id__callback_id" PRIMARY KEY ("client_id", "callback_id"),
    CONSTRAINT "fk__clients_callbacks__client_id" FOREIGN KEY ("client_id") REFERENCES "clients" ("id"),
    CONSTRAINT "fk__clients_callbacks__callback_id" FOREIGN KEY ("callback_id") REFERENCES "callbacks" ("id")
);