CREATE TABLE dossier (
                         id BIGSERIAL PRIMARY KEY,
                         reference VARCHAR(50) NOT NULL UNIQUE,
                         titre VARCHAR(200) NOT NULL,
                         statut VARCHAR(20) NOT NULL,
                         date_ouverture DATE NOT NULL,
                         client_id BIGINT NOT NULL REFERENCES client(id)
);