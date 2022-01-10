CREATE TABLE "user"
(
    "login"         TEXT PRIMARY KEY,
    "password"      TEXT NOT NULL,
    "login_info_id" TEXT NOT NULL
);

CREATE TABLE "login_info"
(
    "session_id"      TEXT PRIMARY KEY,
    "client_id"       TEXT NOT NULL,
    "available_scope" TEXT NULL,

    CONSTRAINT "fk__login_info__client" FOREIGN KEY ("client_id") REFERENCES "client" ("id")
);

ALTER TABLE "user"
    ADD CONSTRAINT
        "fk__user__login_info_id" FOREIGN KEY ("login_info_id") REFERENCES "login_info" ("id")