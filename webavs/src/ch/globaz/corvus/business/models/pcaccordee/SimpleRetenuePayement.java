package ch.globaz.corvus.business.models.pcaccordee;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeSimpleModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

/**
 * @author LFO
 * 
 */
public class SimpleRetenuePayement extends JadeSimpleModel {

    /*
     * private String idCompteAnnexe = ""; private String idInfoCompta = ""; private String idTiersAdressePmt = "";
     */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adressePaiement = "";
    private String cantonImposition = null;
    private String csGenreRetenue = null;
    private String csTypeRetenue = null;
    private String dateDebutRetenue = null;
    private String dateFinRetenue = null;
    private String idDomaineApplicatif = null;
    private String idExterne = null;
    private String idParentRetenue = null;
    private String idRenteAccordee = null;
    private String idRetenue = null;
    private String idRubrique = null;
    private String idTiersAdressePmt = null;
    private String idTypeSection = null;
    private String montantDejaRetenu = null;
    private String montantRetenuMensuel = null;
    private String montantTotalARetenir = null;
    private String noFacture = null;
    private String referenceInterne = null;
    private String rex = null;

    // private transient CASection section = null;
    private String role = null;
    private String tauxImposition = "";

    /**
     * Récupère l'adresse de paiement.
     * 
     * @return adresse de paiement formatée
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String getAdressePaiement() {

        return adressePaiement;
    }

    public String getCantonImposition() {
        return cantonImposition;
    }

    public String getCsGenreRetenue() {
        return csGenreRetenue;
    }

    public String getCsTypeRetenue() {
        return csTypeRetenue;
    }

    public String getDateDebutRetenue() {
        return dateDebutRetenue;
    }

    public String getDateFinRetenue() {
        return dateFinRetenue;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return getIdRetenue();
    }

    public String getIdDomaineApplicatif() {
        return idDomaineApplicatif;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdParentRetenue() {
        return idParentRetenue;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdRetenue() {
        return idRetenue;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public String getIdTiersAdressePmt() {
        return idTiersAdressePmt;
    }

    public String getIdTypeSection() {
        return idTypeSection;
    }

    public String getMontantDejaRetenu() {
        return montantDejaRetenu;
    }

    public String getMontantRetenuMensuel() {
        return montantRetenuMensuel;
    }

    public String getMontantTotalARetenir() {
        return montantTotalARetenir;
    }

    public String getNoFacture() {
        return noFacture;
    }

    public String getReferenceInterne() {
        return referenceInterne;
    }

    public String getRex() {
        return rex;
    }

    public String getRole() {
        return role;
    }

    public String getTauxImposition() {
        return tauxImposition;
    }

    public void setCantonImposition(String cantonImposition) {
        this.cantonImposition = cantonImposition;
    }

    public void setCsGenreRetenue(String csGenreRetenue) {
        this.csGenreRetenue = csGenreRetenue;
    }

    public void setCsTypeRetenue(String csTypeRetenue) {
        this.csTypeRetenue = csTypeRetenue;
    }

    public void setDateDebutRetenue(String dateDebutRetenue) {
        this.dateDebutRetenue = dateDebutRetenue;
    }

    public void setDateFinRetenue(String dateFinRetenue) {
        this.dateFinRetenue = dateFinRetenue;
    }

    @Override
    public void setId(String id) {
        idRetenue = id;
    }

    public void setIdDomaineApplicatif(String idDomaineApplicatif) {
        this.idDomaineApplicatif = idDomaineApplicatif;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdParentRetenue(String idParentRetenue) {
        this.idParentRetenue = idParentRetenue;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIdRetenue(String idRetenue) {
        this.idRetenue = idRetenue;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public void setIdTiersAdressePmt(String idTiersAdressePmt) {
        this.idTiersAdressePmt = idTiersAdressePmt;
    }

    public void setIdTypeSection(String idTypeSection) {
        this.idTypeSection = idTypeSection;
    }

    public void setMontantDejaRetenu(String montantDejaRetenu) {
        this.montantDejaRetenu = montantDejaRetenu;
    }

    public void setMontantRetenuMensuel(String montantRetenuMensuel) {
        this.montantRetenuMensuel = montantRetenuMensuel;
    }

    public void setMontantTotalARetenir(String montantTotalARetenir) {
        this.montantTotalARetenir = montantTotalARetenir;
    }

    public void setNoFacture(String noFacture) {
        this.noFacture = noFacture;
    }

    public void setReferenceInterne(String referenceInterne) {
        this.referenceInterne = referenceInterne;
    }

    public void setRex(String rex) {
        this.rex = rex;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setTauxImposition(String tauxImposition) {
        this.tauxImposition = tauxImposition;
    }

}
