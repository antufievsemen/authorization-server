CREATE TABLE "user"
(
    "id"            TEXT PRIMARY KEY,
    "login"         TEXT NOT NULL,
    "password"      TEXT NOT NULL,
    "login_info_id" TEXT NOT NULL,
    "user_info_id"  TEXT NOT NULL
);

CREATE TABLE "login_info"
(
    "sub"             TEXT PRIMARY KEY,
    "client_id"       TEXT NOT NULL,
    "available_scope" TEXT NULL,
    "session_id"      TEXT NULL,

        CONSTRAINT "fk__login_info__client" FOREIGN KEY ("client_id") REFERENCES "client" ("id")
);

CREATE TABLE "user_info"
(
    "id"            TEXT PRIMARY KEY,
    "firstname"     TEXT NOT NULL,
    "lastname"      TEXT NOT NULL,
    "gender"        TEXT NOT NULL,
    "phoneNumber"   TEXT NOT NULL,
    "phoneVerified" BOOLEAN DEFAULT FALSE,
    "email"         TEXT NOT NULL,
    "emailVerified" BOOLEAN DEFAULT FALSE
);


ALTER TABLE "user"
    ADD CONSTRAINT
        "fk__user__login_info_id" FOREIGN KEY ("login_info_id") REFERENCES "login_info" ("sub");
ALTER TABLE "user"
    ADD CONSTRAINT
        "fk__user__user_info_id" FOREIGN KEY ("user_info_id") REFERENCES "user_info" ("id");