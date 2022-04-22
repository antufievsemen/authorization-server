CREATE TABLE "grant_types"
(
    "id"   TEXT PRIMARY KEY,
    "type" TEXT NOT NULL
);

INSERT INTO "grant_types"
VALUES ('pQxMfh0Uw8', 'AUTHORIZATION_CODE');
INSERT INTO "grant_types"
VALUES ('SNi3ta0CFM', 'REFRESH_TOKEN');
INSERT INTO "grant_types"
VALUES ('y4axhrS5xr', 'PKCE');
INSERT INTO "grant_types"
VALUES ('fN6Y5ssgif', 'CLIENT_CREDENTIALS');
INSERT INTO "grant_types"
VALUES ('xlXLLgSVSe', 'OPEN_ID');
INSERT INTO "grant_types"
VALUES ('zEz9fOntUm', 'IMPLICIT');

CREATE TABLE "client_grant_types"
(
    "client_id"     TEXT NOT NULL,
    "grant_type_id" TEXT NOT NULL,

    CONSTRAINT "pk__clients_grant_types" PRIMARY KEY ("client_id", "grant_type_id"),
    CONSTRAINT "fk__clients_grant_types__client_id" FOREIGN KEY ("client_id") REFERENCES "clients" ("id"),
    CONSTRAINT "fk__clients_grant_types__grant_type_id" FOREIGN KEY ("grant_type_id") REFERENCES "grant_types" ("id")
);
