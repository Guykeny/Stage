CREATE TABLE echeance (
                          id BIGSERIAL PRIMARY KEY,
                          label VARCHAR(200) NOT NULL,
                          due_date DATE NOT NULL,
                          done BOOLEAN NOT NULL DEFAULT FALSE,
                          dossier_id BIGINT NOT NULL REFERENCES dossier(id)
);