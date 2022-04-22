CREATE TABLE "scopes"
(
    "id"   TEXT PRIMARY KEY,
    "name" TEXT NOT NULL
);

INSERT INTO "scopes" (id, name)
VALUES ('oc0llQ26GV', 'openid');
INSERT INTO "scopes" (id, name)
VALUES ('zF5w28Bnxg', 'email');
INSERT INTO "scopes" (id, name)
VALUES ('RV5lbC1Bjb', 'profile');
INSERT INTO "scopes" (id, name)
VALUES ('8B8qi4h27f', 'offline_access');

CREATE TABLE "client_scopes"
(
    "client_id" TEXT NOT NULL,
    "scope_id"  TEXT NOT NULL,

    CONSTRAINT "pk__client_id__scope__id" PRIMARY KEY ("client_id", "scope_id"),
    CONSTRAINT "fk__clients_scopes__client_id" FOREIGN KEY ("client_id") REFERENCES "clients" ("id"),
    CONSTRAINT "fk__clients_scopes__scope_id" FOREIGN KEY ("scope_id") REFERENCES "scopes" ("id")
);

CREATE TABLE "user_scopes"
(
    "user_id"  TEXT NOT NULL,
    "scope_id" TEXT NOT NULL,

    CONSTRAINT "pk__user_id__scope__id" PRIMARY KEY ("user_id", "scope_id"),
    CONSTRAINT "fk__users_scopes__client_id" FOREIGN KEY ("user_id") REFERENCES "users" ("id"),
    CONSTRAINT "fk__users_scopes__scope_id" FOREIGN KEY ("scope_id") REFERENCES "scopes" ("id")
);