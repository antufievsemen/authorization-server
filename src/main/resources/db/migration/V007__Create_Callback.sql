CREATE TABLE "callback"
(
    "id"         TEXT PRIMARY KEY,
    "url"        TEXT NOT NULL,
    "created_at" TIMESTAMP DEFAULT now()
);

CREATE TABLE "client_callbacks"
(
    "client_id"   TEXT NOT NULL,
    "callback_id" TEXT NOT NULL,

    CONSTRAINT "pk__client_id__callback_id" PRIMARY KEY ("client_id", "callback_id"),
    CONSTRAINT "fk__client_callbacks__client_id" FOREIGN KEY ("client_id") REFERENCES "client" ("id"),
    CONSTRAINT "fk__client_callbacks__callback_id" FOREIGN KEY ("callback_id") REFERENCES "callback" ("id")
);