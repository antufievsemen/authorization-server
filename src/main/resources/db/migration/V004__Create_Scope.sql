CREATE TABLE "scope"
(
    "id"   TEXT PRIMARY KEY,
    "name" TEXT NOT NULL
);

INSERT INTO "scope" (id, name)
VALUES ('oc0llQ26GV', 'openid');
INSERT INTO "scope" (id, name)
VALUES ('zF5w28Bnxg', 'email');
INSERT INTO "scope" (id, name)
VALUES ('RV5lbC1Bjb', 'profile');
INSERT INTO "scope" (id, name)
VALUES ('8B8qi4h27f', 'offline_access');

CREATE TABLE "client_scopes"
(
    "client_id" TEXT NOT NULL,
    "scope_id"  TEXT NOT NULL,

    CONSTRAINT "pk__client_id__scope__id" PRIMARY KEY ("client_id", "scope_id"),
    CONSTRAINT "fk__client_scopes__client_id" FOREIGN KEY ("client_id") REFERENCES "client" ("id"),
    CONSTRAINT "fk__client_scopes__scope_id" FOREIGN KEY ("scope_id") REFERENCES "scope" ("id")
);

CREATE TABLE "user_scopes"
(
    "user_id"  TEXT NOT NULL,
    "scope_id" TEXT NOT NULL,

    CONSTRAINT "pk__user_id__scope__id" PRIMARY KEY ("user_id", "scope_id"),
    CONSTRAINT "fk__user_scopes__client_id" FOREIGN KEY ("user_id") REFERENCES "users" ("id"),
    CONSTRAINT "fk__user_scopes__scope_id" FOREIGN KEY ("scope_id") REFERENCES "scope" ("id")
);