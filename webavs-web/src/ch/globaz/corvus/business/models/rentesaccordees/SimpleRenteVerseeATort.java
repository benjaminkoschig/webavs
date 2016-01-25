package ch.globaz.corvus.business.models.rentesaccordees;

import globaz.globall.db.BConstants;
import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleRenteVerseeATort extends JadeSimpleModel {

    private static final long serialVersionUID = 1L;

    private String csTypeRenteVerseeATort;
    private String dateDebut;
    private String dateFin;
    private String descriptionSaisieManuelle;
    private String idDemandeRente;
    private String idRenteAccordeeAncienDroit;
    private String idRenteAccordeeNouveauDroit;
    private String idRenteVerseeATort;
    private String idTiers;
    private String montant;
    private String _saisieManuelle;

    public String getCsTypeRenteVerseeATort() {
        return csTypeRenteVerseeATort;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDescriptionSaisieManuelle() {
        return descriptionSaisieManuelle;
    }

    @Override
    public String getId() {
        if (idRenteVerseeATort == null) {
            return null;
        }
        return idRenteVerseeATort;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdRenteAccordeeAncienDroit() {
        return idRenteAccordeeAncienDroit;
    }

    public String getIdRenteAccordeeNouveauDroit() {
        return idRenteAccordeeNouveauDroit;
    }

    public String getIdRenteVerseeATort() {
        return idRenteVerseeATort;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontant() {
        return montant;
    }

    public void setCsTypeRenteVerseeATort(String csTypeRenteVerseeATort) {
        this.csTypeRenteVerseeATort = csTypeRenteVerseeATort;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDescriptionSaisieManuelle(String descriptionSaisieManuelle) {
        this.descriptionSaisieManuelle = descriptionSaisieManuelle;
    }

    @Override
    public void setId(String id) {
        idRenteVerseeATort = id;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdRenteAccordeeAncienDroit(String idRenteAccordeeAncienDroit) {
        this.idRenteAccordeeAncienDroit = idRenteAccordeeAncienDroit;
    }

    public void setIdRenteAccordeeNouveauDroit(String idRenteAccordeeNouveauDroit) {
        this.idRenteAccordeeNouveauDroit = idRenteAccordeeNouveauDroit;
    }

    public void setIdRenteVerseeATort(String idRenteVerseeATort) {
        this.idRenteVerseeATort = idRenteVerseeATort;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public boolean isSaisieManuelle() {
        return BConstants.DB_BOOLEAN_TRUE.equals(_saisieManuelle);
    }

    public String get_saisieManuelle() {
        return _saisieManuelle;
    }

    public void set_saisieManuelle(String saisieManuelle) {
        _saisieManuelle = saisieManuelle;
    }
}
