package fr.sdv.m22026.lexdossierr.service;

import fr.sdv.m22026.lexdossierr.dto.DossierRequest;
import fr.sdv.m22026.lexdossierr.entity.Dossier;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DossierServiceTest {
    @Mock
    private DossierRepository dossierRepository;
    @Mock
    private ClientRepository clientRepository;
    @InjectMocks
    private DossierService dossierService;

    @Test
    void create_shouldThrowConflict_whenReferenceAlreadyExists(){
        when(dossierRepository.existsByReference("2026-0001")).thenReturn(true);

        assertThrows(ConflictException.class, ()-> dossierService.createDossier(new DossierRequest("2026-0001","Litige",1L)));
        verify(dossierRepository,never()).save(any());
    }
    @Test
    void create_shouldThrowNotFound_whenClientDoesNotExist(){
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());
        when(dossierRepository.existsByReference("2026-0001")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->dossierService.createDossier(new DossierRequest("2026-0001","Moi",99L)));
    }
}
