CREATE TABLE "response_type"
(
    "id"   TEXT PRIMARY KEY,
    "name" TEXT NOT NULL
);

INSERT INTO "response_type" (id, name)
VALUES ('o1UnsZXBkj', 'code');
INSERT INTO "response_type" (id, name)
VALUES ('NE6Nf5uPxR', 'token');
INSERT INTO "response_type" (id, name)
VALUES ('E0fNeRdRWr', 'id_token');

CREATE TABLE "client_response_type"
(
    "client_id"        TEXT NOT NULL,
    "response_type_id" TEXT NOT NULL,

    CONSTRAINT "pk__client_id__response_type_id" PRIMARY KEY ("client_id", "response_type_id"),
    CONSTRAINT "fk__client_scopes__client_id" FOREIGN KEY ("client_id") REFERENCES "client" ("id"),
    CONSTRAINT "fk__client_scopes__response_type_id" FOREIGN KEY ("response_type_id") REFERENCES "response_type" ("id")
);