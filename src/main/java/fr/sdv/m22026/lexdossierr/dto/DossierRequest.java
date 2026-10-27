package fr.sdv.m22026.lexdossierr.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DossierRequest(@NotBlank @Size(max=50) String reference, @NotBlank @Size(max=50) String title, @NotNull Long clientId) {
}
