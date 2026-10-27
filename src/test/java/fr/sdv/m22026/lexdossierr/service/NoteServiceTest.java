package fr.sdv.m22026.lexdossierr.service;

import fr.sdv.m22026.lexdossierr.dto.NoteRequest;
import fr.sdv.m22026.lexdossierr.dto.NoteResponse;
import fr.sdv.m22026.lexdossierr.entity.Dossier;
import fr.sdv.m22026.lexdossierr.entity.Note;
import fr.sdv.m22026.lexdossierr.entity.StatutDossier;
import fr.sdv.m22026.lexdossierr.exception.ConflictException;
import fr.sdv.m22026.lexdossierr.repository.ClientRepository;
import fr.sdv.m22026.lexdossierr.repository.DossierRepository;
import fr.sdv.m22026.lexdossierr.repository.NoteRepository;
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

public class NoteServiceTest {
    @Mock
    NoteRepository noteRepository;
    @Mock
    DossierRepository dossierRepository;

    @InjectMocks
    NoteService noteService;

    @Test
    void createNote_shouldThrowConflict_whenDossierIsClosed(){
        Dossier dossier = new Dossier();
        dossier.setId(1L);
        dossier.setStatut(StatutDossier.CLOS);
        when(dossierRepository.findById(1L)).thenReturn(Optional.of(dossier));

        assertThrows(ConflictException.class,() -> noteService.createNote(1L, new NoteRequest("Premier rendez vous")));
        verify(noteRepository,never()).save(any());


    }
    @Test
    void createNote_shouldSaveNote_whenDossierIsOpen(){
        Dossier dossier = new Dossier();
        dossier.setId(1L);
        dossier.setStatut(StatutDossier.OUVERT);
        when(dossierRepository.findById(1L)).thenReturn(Optional.of(dossier));
        when(noteRepository.save(any(Note.class))).thenAnswer(i -> i.getArgument(0));


       NoteResponse note= noteService.createNote(1L, new NoteRequest("Premier rendez-vous"));
       assertEquals("Premier rendez-vous",note.contentu());


    }

}
