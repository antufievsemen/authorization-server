CREATE TABLE "user_refresh_token"
(
    "id"            TEXT PRIMARY KEY,
    "user_id"       TEXT NOT NULL,
    "refresh_token" TEXT NULL,
    "created_at"    TIMESTAMP DEFAULT now(),
    "expired_at"    TIMESTAMP,

    CONSTRAINT "fk__user_refresh_token__user" FOREIGN KEY ("user_id") REFERENCES "user" ("id")
)