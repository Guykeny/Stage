package fr.sdv.m22026.lexdossierr.repository;


import fr.sdv.m22026.lexdossierr.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByDossierId(Long dossierId);
}
