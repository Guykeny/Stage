package fr.sdv.m22026.lexdossierr.repository;

import fr.sdv.m22026.lexdossierr.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ClientRepository  extends JpaRepository<Client, Long> {

    public List<Client> findAll();
    public Optional <Client>findById(Long id);
    public void deleteById(Long id);
    public Client save(Client client);




}
