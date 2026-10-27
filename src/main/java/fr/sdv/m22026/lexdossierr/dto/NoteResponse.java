package fr.sdv.m22026.lexdossierr.dto;

import fr.sdv.m22026.lexdossierr.entity.Note;

import java.time.LocalDateTime;

public record NoteResponse(Long id, String contentu, LocalDateTime creationDate, Long dossierId) {

    public static NoteResponse from(Note note){
        return new NoteResponse(
                note.getId(),
                note.getContenu(),
                note.getCreationDate(),
                note.getDossier().getId()
        );
    }
}
