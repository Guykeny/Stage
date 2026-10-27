package fr.sdv.m22026.lexdossierr.controller;


import fr.sdv.m22026.lexdossierr.dto.DossierRequest;
import fr.sdv.m22026.lexdossierr.dto.DossierResponse;
import fr.sdv.m22026.lexdossierr.dto.NoteRequest;
import fr.sdv.m22026.lexdossierr.dto.NoteResponse;
import fr.sdv.m22026.lexdossierr.entity.StatutDossier;
import fr.sdv.m22026.lexdossierr.service.DossierService;
import fr.sdv.m22026.lexdossierr.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dossiers")
public class DossierController {

    private final DossierService dossierService;
    private final NoteService noteService;

    public DossierController(DossierService dossierService, NoteService noteService) {
        this.dossierService = dossierService;
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<DossierResponse> create(@Valid @RequestBody DossierRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dossierService.createDossier(request));
    }

    @GetMapping()
    public Page<DossierResponse> findAll(@RequestParam(required = false) StatutDossier statut,@PageableDefault(size = 10, sort = "dateOuverture", direction = Sort.Direction.DESC) Pageable pageable) {
        return dossierService.findAllDossiers(statut, pageable);
    }

    @GetMapping("/{id}")
    public DossierResponse findById(@PathVariable Long id) {
        return dossierService.findById(id);
    }
    @PostMapping("/{id}/notes")
    public ResponseEntity<NoteResponse> addNote(@PathVariable Long id, @Valid @RequestBody NoteRequest noteRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.createNote(id, noteRequest));
    }
    @GetMapping("/{id}/notes")
    public List<NoteResponse> findAllNotes(@PathVariable Long id) {
        return noteService.findByDossier(id);
    }
    @PatchMapping("/{id}/close")
    public DossierResponse close(@PathVariable Long id) {
        return dossierService.close(id);
    }
}