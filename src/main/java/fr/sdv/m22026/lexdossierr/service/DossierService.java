package fr.sdv.m22026.lexdossierr.service;

import fr.sdv.m22026.lexdossierr.dto.DossierRequest;
import fr.sdv.m22026.lexdossierr.dto.DossierResponse;
import fr.sdv.m22026.lexdossierr.entity.Client;
import fr.sdv.m22026.lexdossierr.entity.Dossier;
import fr.sdv.m22026.lexdossierr.entity.StatutDossier;
import fr.sdv.m22026.lexdossierr.exception.ConflictException;
import fr.sdv.m22026.lexdossierr.exception.ResourceNotFoundException;
import fr.sdv.m22026.lexdossierr.repository.ClientRepository;
import fr.sdv.m22026.lexdossierr.repository.DossierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DossierService {

    private final DossierRepository dossierRepository;
    private final ClientRepository clientRepository;
    public DossierService(DossierRepository dossierRepository, ClientRepository clientRepository) {
        this.dossierRepository = dossierRepository;
        this.clientRepository = clientRepository;
    }

    public DossierResponse createDossier(DossierRequest dossierRequest) {
        if(dossierRepository.existsByReference(dossierRequest.reference())){
            throw new ConflictException("Reference dossier exists" + dossierRequest.reference());
        }
        Client client= clientRepository.findById(dossierRequest.clientId()).orElseThrow(()-> new ResourceNotFoundException("Client not found"));

        Dossier dossier = new Dossier();
        dossier.setReference(dossierRequest.reference());
        dossier.setTitre(dossierRequest.title());
        dossier.setClient(client);

        return DossierResponse.from(dossierRepository.save(dossier));
    }

    public Page<DossierResponse> findAllDossiers(StatutDossier statut, Pageable pageable) {
        if(statut == null) return dossierRepository.findAll(pageable).map(DossierResponse::from);
        return dossierRepository.findByStatut(statut,pageable).map(DossierResponse::from);
    }
    public DossierResponse findById(Long id) {
        Dossier dossier = dossierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier not found: " + id));
        return DossierResponse.from(dossier);
    }

    public DossierResponse close(Long dossierId) {
        Dossier dossier = dossierRepository.findById(dossierId)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier not found: " + dossierId));

        dossier.setStatut(StatutDossier.CLOS);
        return DossierResponse.from(dossierRepository.save(dossier));
    }


}
