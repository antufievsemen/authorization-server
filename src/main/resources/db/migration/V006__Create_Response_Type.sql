CREATE TABLE "response_types"
(
    "id"   TEXT PRIMARY KEY,
    "type" TEXT NOT NULL
);

INSERT INTO "response_types" (id, type)
VALUES ('o1UnsZXBkj', 'CODE');
INSERT INTO "response_types" (id, type)
VALUES ('NE6Nf5uPxR', 'TOKEN');
INSERT INTO "response_types" (id, type)
VALUES ('E0fNeRdRWr', 'ID_TOKEN');

CREATE TABLE "client_response_types"
(
    "client_id"        TEXT NOT NULL,
    "response_type_id" TEXT NOT NULL,

    CONSTRAINT "pk__clients_id__response_type_id" PRIMARY KEY ("client_id", "response_type_id"),
    CONSTRAINT "fk__clients_scopes__client_id" FOREIGN KEY ("client_id") REFERENCES "clients" ("id"),
    CONSTRAINT "fk__clients_scopes__response_type_id" FOREIGN KEY ("response_type_id") REFERENCES "response_types" ("id")
);