CREATE TABLE "grant_type"
(
    "id"   TEXT PRIMARY KEY,
    "type" TEXT NOT NULL
);

INSERT INTO "grant_type"
VALUES ('pQxMfh0Uw8', 'AUTHORIZATION_CODE');
INSERT INTO "grant_type"
VALUES ('SNi3ta0CFM', 'REFRESH_TOKEN');
INSERT INTO "grant_type"
VALUES ('y4axhrS5xr', 'PKCE');
INSERT INTO "grant_type"
VALUES ('fN6Y5ssgif', 'CLIENT_CREDENTIALS');
INSERT INTO "grant_type"
VALUES ('xlXLLgSVSe', 'OPEN_ID');
INSERT INTO "grant_type"
VALUES ('zEz9fOntUm', 'IMPLICIT');

CREATE TABLE "client_grant_types"
(
    "client_id"     TEXT NOT NULL,
    "grant_type_id" TEXT NOT NULL,

    CONSTRAINT "pk__client_grant_type" PRIMARY KEY ("client_id", "grant_type_id"),
    CONSTRAINT "fk__client_grant_type__client_id" FOREIGN KEY ("client_id") REFERENCES "client" ("id"),
    CONSTRAINT "fk__client_grant_type__grant_type_id" FOREIGN KEY ("grant_type_id") REFERENCES "grant_type" ("id")
);
