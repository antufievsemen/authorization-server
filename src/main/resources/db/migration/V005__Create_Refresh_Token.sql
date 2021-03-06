CREATE TABLE "refresh_tokens"
(
    "id"         TEXT PRIMARY KEY,
    "user_id"    TEXT      NOT NULL,
    "token"      TEXT      NOT NULL,
    "created_at" TIMESTAMP DEFAULT now(),
    "updated_at" TIMESTAMP DEFAULT now(),
    "expired_at" TIMESTAMP NOT NULL,

    CONSTRAINT "fk__refresh_token__user__user_id" FOREIGN KEY ("user_id") REFERENCES "users" ("id")
)