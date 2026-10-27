package fr.sdv.m22026.lexdossierr.service;

import fr.sdv.m22026.lexdossierr.entity.Client;
import fr.sdv.m22026.lexdossierr.exception.ConflictException;
import fr.sdv.m22026.lexdossierr.exception.ResourceNotFoundException;
import fr.sdv.m22026.lexdossierr.repository.ClientRepository;
import fr.sdv.m22026.lexdossierr.repository.DossierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private DossierRepository dossierRepository;

    @InjectMocks
    private ClientService clientService;
    @Test
    void findById_shouldReturnClient_whenClientExists(){
        Client client = new Client();
        client.setId(1L);
        client.setName("Martin");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Client result= clientService.findById(1L);

        assertEquals("Martin",result.getName());

    }
    @Test
    void findById_shouldThrowException_whenClientDoesNotExist(){
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,() -> clientService.findById(99L));
    }

    @Test
    void delete_shouldDeleteClient_whenClientHasNoDossiers(){

        Client client = new Client();
        client.setId(1L);
        client.setName("Martin");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(dossierRepository.existsByClientId(1L)).thenReturn(false);

        clientService.delete(client.getId());

        verify(clientRepository).deleteById(client.getId());

    }
    @Test
    void delete_shouldThrowConflict_whenClientHasDossiers(){
        Client client = new Client();
        client.setId(1L);
        client.setName("Martin");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(dossierRepository.existsByClientId(1L)).thenReturn(true);

        assertThrows(ConflictException.class,() -> clientService.delete(client.getId()));
        verify(clientRepository,never()).deleteById(anyLong());
    }
    @Test
    void delete_shouldThrowNotFound_whenClientDoesNotExist(){
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,() -> clientService.delete(99L));
    }
    @Test
    void update_shouldUpdateFields_whenClientExists(){
        Client client = new Client();
        client.setId(1L);
        client.setName("Martin");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Client result= clientService.update(1L,client);
        assertEquals("Martin Paul",result.getName());

    }


}
