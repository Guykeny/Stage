package fr.sdv.m22026.lexdossierr.controller;

import fr.sdv.m22026.lexdossierr.entity.Client;
import fr.sdv.m22026.lexdossierr.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> lister() {
        return clientService.listerClients();
    }
    @PostMapping
    public ResponseEntity <Client>  creer(@Valid @RequestBody Client client) {
        Client created = clientService.creer(client);
        return ResponseEntity.ok().body(created);
    }
    @PutMapping("/{id}")
    public Client  modifier(@PathVariable Long id,@Valid @RequestBody Client client) {
        return clientService.update(id,client);

    }
    @GetMapping("/{id}")
    public Client findById(@PathVariable Long id) {
        return clientService.findById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        clientService.delete(id);
    }
}
