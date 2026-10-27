package fr.sdv.m22026.lexdossierr.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "dossier")
public class Dossier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false,length = 50)
    private String reference;
    @Column( nullable = false,length = 50)
    private String titre;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private StatutDossier statut=StatutDossier.OUVERT;

    private LocalDate dateOuverture = LocalDate.now();
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="client_id")
    private Client client;

    public Dossier() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public StatutDossier getStatut() {
        return statut;
    }

    public void setStatut(StatutDossier statut) {
        this.statut = statut;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public LocalDate getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(LocalDate dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
