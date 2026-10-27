package fr.sdv.m22026.lexdossierr.service;

import fr.sdv.m22026.lexdossierr.entity.Client;
import fr.sdv.m22026.lexdossierr.entity.Dossier;
import fr.sdv.m22026.lexdossierr.exception.ConflictException;
import fr.sdv.m22026.lexdossierr.exception.ResourceNotFoundException;
import fr.sdv.m22026.lexdossierr.repository.ClientRepository;
import fr.sdv.m22026.lexdossierr.repository.DossierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ClientService {

    private final ClientRepository clientRepository;
    private final DossierRepository dossierRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, DossierRepository dossierRepository) {
        this.clientRepository = clientRepository;
        this.dossierRepository = dossierRepository;
    }

    public List<Client> listerClients() {
        return clientRepository.findAll();
    }
    public Client creer(Client client) {
        return clientRepository.save(client);
    }
    public Client findById(Long id) {
        return clientRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Client not found"));
    }
    // Dans ClientService
    public Client update(Long id, Client client) {
        Client existingClient = clientRepository.findById(id)   // ← fourni par JpaRepository
                .orElseThrow(() -> new RuntimeException("Client not found: " + id));
        existingClient.setName(client.getName());
        existingClient.setEmail(client.getEmail());
        existingClient.setTelephone(client.getTelephone());
        return clientRepository.save(existingClient);           // ← fourni par JpaRepository
    }
    public void delete(Long id) {
         Client  existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found: " + id));

         if(dossierRepository.existsByClientId(existingClient.getId())) {
             throw new ConflictException("Cannot delete client"+ existingClient.getId());
         }
         clientRepository.deleteById(existingClient.getId());
    }

}
