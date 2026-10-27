package fr.sdv.m22026.lexdossierr.repository;


import fr.sdv.m22026.lexdossierr.entity.Dossier;
import fr.sdv.m22026.lexdossierr.entity.StatutDossier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DossierRepository extends JpaRepository<Dossier, Long> {

    boolean existsByReference(String reference);
    boolean existsByClientId(Long clientId);

    Page<Dossier> findByStatut(StatutDossier statut, Pageable pageable);
}
