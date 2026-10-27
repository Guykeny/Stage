CREATE TABLE Note
(
    id            BIGSERIAL PRIMARY KEY,
    contenu       TEXT   not null,
    creation_date DATE   NOT NULL,
    dossier_id    BIGINT NOT NULL REFERENCES dossier (id)
);