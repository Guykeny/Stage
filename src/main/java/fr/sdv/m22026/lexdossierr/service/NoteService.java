package fr.sdv.m22026.lexdossierr.service;

import fr.sdv.m22026.lexdossierr.dto.NoteRequest;
import fr.sdv.m22026.lexdossierr.dto.NoteResponse;
import fr.sdv.m22026.lexdossierr.entity.Dossier;
import fr.sdv.m22026.lexdossierr.entity.Note;
import fr.sdv.m22026.lexdossierr.entity.StatutDossier;
import fr.sdv.m22026.lexdossierr.exception.ConflictException;
import fr.sdv.m22026.lexdossierr.exception.ResourceNotFoundException;
import fr.sdv.m22026.lexdossierr.repository.DossierRepository;
import fr.sdv.m22026.lexdossierr.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final DossierRepository dossierRepository;

    public NoteService(NoteRepository noteRepository, DossierRepository dossierRepository) {
        this.noteRepository = noteRepository;
        this.dossierRepository = dossierRepository;
    }

    public NoteResponse createNote(Long dossierId, NoteRequest noteRequest) {
        Dossier dossier = dossierRepository.findById(dossierId).orElseThrow(()-> new ResourceNotFoundException("Dossier not found"+dossierId) );
        if( dossier.getStatut() == StatutDossier.CLOS){
            throw new ConflictException("Dossier is already closed");
        }
        Note note = new Note();
        note.setContenu(noteRequest.contentu());
        note.setDossier(dossier);

        return NoteResponse.from(noteRepository.save(note));
    }
    public List<NoteResponse> findByDossier(Long dossierId) {
        if(dossierRepository.findById(dossierId).isPresent()){
            return noteRepository.findByDossierId(dossierId).stream().map(NoteResponse::from).toList();
        }
        throw new ResourceNotFoundException("Dossier not found");
    }



}
