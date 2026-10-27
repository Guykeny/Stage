package fr.sdv.m22026.lexdossierr.dto;

import jakarta.validation.constraints.NotBlank;

public record NoteRequest(@NotBlank String contentu) {
}
