package fr.sdv.m22026.lexdossierr.dto;

import fr.sdv.m22026.lexdossierr.entity.Dossier;
import fr.sdv.m22026.lexdossierr.entity.StatutDossier;

import java.time.LocalDate;

public record DossierResponse(Long id,
                              String reference,
                              String title,
                              StatutDossier statut,
                              LocalDate dateOuverture,
                              Long clientId,
                              String clientName) {
    public static DossierResponse from(Dossier dossier) {
        return new DossierResponse(
                dossier.getId(),
                dossier.getReference(),
                dossier.getTitre(),
                dossier.getStatut(),
                dossier.getDateOuverture(),
                dossier.getClient().getId(),
                dossier.getClient().getName()

        );
    }
}
