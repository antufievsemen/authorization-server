CREATE TABLE "user"
(
    "id"              TEXT PRIMARY KEY,
    "client_id"       TEXT      NOT NULL,
    "created_at"      TIMESTAMP NOT NULL,
    "available_scope" TEXT      NULL,
    "session_id"      TEXT      NULL,
    "consented"       BOOLEAN   NOT NULL DEFAULT FALSE,
    "scopes"          TEXT      NULL,

    CONSTRAINT "fk__login_info__client" FOREIGN KEY ("client_id") REFERENCES "client" ("id")
);