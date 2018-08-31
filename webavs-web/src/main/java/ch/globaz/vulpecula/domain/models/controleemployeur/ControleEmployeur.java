package ch.globaz.vulpecula.domain.models.controleemployeur;

import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;

public class ControleEmployeur implements DomainEntity {
    private String id;
    private String idUtilisateur;
    private Employeur employeur;
    private String numeroMeroba;
    private Montant montant;
    private Date dateControle;
    private Date dateAu;
    private boolean autresMesures;
    private TypeControle type;
    private String spy;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(String idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getIdEmployeur() {
        return employeur.getId();
    }

    public String getNumeroMeroba() {
        return numeroMeroba;
    }

    public void setNumeroMeroba(String numeroMeroba) {
        this.numeroMeroba = numeroMeroba;
    }

    public Montant getMontant() {
        return montant;
    }

    public String getMontantAsValue() {
        if (montant == null) {
            return null;
        }
        return montant.getValue();
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

    public Date getDateControle() {
        return dateControle;
    }

    public String getDateControleAsSwissValue() {
        if (dateControle == null) {
            return null;
        }
        return dateControle.getSwissValue();
    }

    public void setDateControle(Date dateControle) {
        this.dateControle = dateControle;
    }

    public Date getDateAu() {
        return dateAu;
    }

    public void setDateAu(Date dateAu) {
        this.dateAu = dateAu;
    }

    public TypeControle getType() {
        return type;
    }

    public void setType(TypeControle type) {
        this.type = type;
    }

    public Employeur getEmployeur() {
        return employeur;
    }

    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    public String getDateAuAsSwissValue() {
        if (dateAu == null) {
            return null;
        }
        return dateAu.getSwissValue();
    }

    public String getTypeAsValue() {
        if (type == null) {
            return null;
        }
        return type.getValue();
    }

    public boolean isAutresMesures() {
        return autresMesures;
    }

    public void setAutresMesures(boolean autresMesures) {
        this.autresMesures = autresMesures;
    }
}
