package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.codesystem.CodeSystem;
import ch.globaz.common.codesystem.CodeSystemUtils;
import ch.globaz.common.util.prestations.MotifVersementUtil;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.PrestationPeriode;

public class PrestationOvDecompte {
    private List<OrdreVersement> allocationsNoel = new ArrayList<OrdreVersement>();
    private List<OrdreVersement> creanciers = new ArrayList<OrdreVersement>();
    private List<OrdreVersement> dettes = new ArrayList<OrdreVersement>();
    private InfosTiers infosConjoint = new InfosTiers();
    private InfosTiers infosRequerant = new InfosTiers();
    private List<OrdreVersement> joursAppoint = new ArrayList<OrdreVersement>();
    private BigDecimal prestationAmount = new BigDecimal(0);
    private List<PrestationPeriode> prestationsPeriodes = new ArrayList<PrestationPeriode>();
    private String dateDecision;
    private String dateDebut;
    private String dateFin;
    private String refPaiement;

    public String getRefPaiement() {
        return refPaiement;
    }

    public void setRefPaiement(String refPaiement) {
        this.refPaiement = refPaiement;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public List<OrdreVersement> getAllocationsNoel() {
        return allocationsNoel;
    }

    public CompteAnnexeSimpleModel getCompteAnnexeConjoint() {
        return infosConjoint.getCompteAnnexe();
    }

    public CompteAnnexeSimpleModel getCompteAnnexeRequerant() {
        return infosRequerant.getCompteAnnexe();
    }

    public List<OrdreVersement> getCreanciers() {
        return creanciers;
    }

    public List<OrdreVersement> getDettes() {
        return dettes;
    }

    public String getIdCompteAnnexeConjoint() {
        return infosConjoint.getIdCompteAnnexe();
    }

    public String getIdCompteAnnexeRequerant() {
        return infosRequerant.getIdCompteAnnexe();
    }

    public String getIdDomaineApplicationConjoint() {
        return infosConjoint.getIdDomaineApplication();
    }

    public String getIdDomaineApplicationRequerant() {
        return infosRequerant.getIdDomaineApplication();
    }

    public String getIdTiersAddressePaiementConjoint() {
        return infosConjoint.getIdTiersAddressePaiement();
    }

    public String getIdTiersAddressePaiementRequerant() {
        return infosRequerant.getIdTiersAddressePaiement();
    }

    public String getIdTiersConjoint() {
        return infosConjoint.getIdTiers();
    }

    public String getIdTiersRequerant() {
        return infosRequerant.getIdTiers();
    }

    public InfosTiers getInfosConjoint() {
        return infosConjoint;
    }

    public InfosTiers getInfosRequerant() {
        return infosRequerant;
    }

    public List<OrdreVersement> getJoursAppoint() {
        return joursAppoint;
    }

    public BigDecimal getPrestationAmount() {
        return prestationAmount;
    }

    public List<PrestationPeriode> getPrestationsPeriodes() {
        return prestationsPeriodes;
    }

    public boolean hasConjoint() {
        return (infosConjoint.getCompteAnnexe() != null)
                && !JadeStringUtil.isBlankOrZero(infosConjoint.getIdCompteAnnexe());
    }

    public void setAllocationsNoel(List<OrdreVersement> allocationsNoel) {
        this.allocationsNoel = allocationsNoel;
    }

    public void setCompteAnnexeConjoint(CompteAnnexeSimpleModel compteAnnexeConjoint) {
        infosConjoint.setCompteAnnexe(compteAnnexeConjoint);
    }

    public void setCompteAnnexeRequerant(CompteAnnexeSimpleModel compteAnnexeRequerant) {
        infosRequerant.setCompteAnnexe(compteAnnexeRequerant);
    }

    public void setCreanciers(List<OrdreVersement> creanciers) {
        this.creanciers = creanciers;
    }

    public void setDettes(List<OrdreVersement> dettes) {
        this.dettes = dettes;
    }

    public void setIdDomaineApplicationConjoint(String idDomaineApplicationConjoint) {
        infosConjoint.setIdDomaineApplication(idDomaineApplicationConjoint);
    }

    public void setIdDomaineApplicationRequerant(String idDomaineApplicationRequerant) {
        infosRequerant.setIdDomaineApplication(idDomaineApplicationRequerant);
    }

    public void setIdTiersAddressePaiementConjoint(String idTiersAddressePaiementConjoint) {
        infosConjoint.setIdTiersAddressePaiement(idTiersAddressePaiementConjoint);
    }

    public void setIdTiersAddressePaiementRequerant(String idTiersAddressePaiementRequerant) {
        infosRequerant.setIdTiersAddressePaiement(idTiersAddressePaiementRequerant);
    }

    public String getNssConjoint() {
        return infosConjoint.getNss();
    }

    public void setNssConjoint(String nss) {
        infosConjoint.setNss(nss);
    }

    public String getNomConjoint() {
        return infosConjoint.getNom();
    }

    public void setNomConjoint(String nom) {
        infosConjoint.setNom(nom);
    }

    public String getPrenomConjoint() {
        return infosConjoint.getPrenom();
    }

    public void setPrenomConjoint(String prenom) {
        infosConjoint.setPrenom(prenom);
    }

    public String getNssRequerant() {
        return infosRequerant.getNss();
    }

    public void setNssRequerant(String nss) {
        infosRequerant.setNss(nss);
    }

    public String getNomRequerant() {
        return infosRequerant.getNom();
    }

    public void setNomRequerant(String nom) {
        infosRequerant.setNom(nom);
    }

    public String getPrenomRequerant() {
        return infosRequerant.getPrenom();
    }

    public void setPrenomRequerant(String prenom) {
        infosRequerant.setPrenom(prenom);
    }

    public void setIdTiersConjoint(String idTiersConjoint) {
        infosConjoint.setIdTiers(idTiersConjoint);
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        infosRequerant.setIdTiers(idTiersRequerant);
    }

    public void setInfosConjoint(InfosTiers infosConjoint) {
        this.infosConjoint = infosConjoint;
    }

    public void setInfosRequerant(InfosTiers infosRequerant) {
        this.infosRequerant = infosRequerant;
    }

    public void setJoursAppoint(List<OrdreVersement> joursAppoint) {
        this.joursAppoint = joursAppoint;
    }

    public void setPrestationAmount(BigDecimal prestationAmount) {
        this.prestationAmount = prestationAmount;
    }

    public void setPrestationsPeriodes(List<PrestationPeriode> prestationsPeriodes) {
        this.prestationsPeriodes = prestationsPeriodes;
    }

    public String resolveReferencePaiementConjoint() {
        return infosConjoint.getNss() + " " + infosConjoint.getNom() + " " + infosConjoint.getPrenom();
    }

    public String resolveReferencePaiementRequerant() {
        return infosConjoint.getNss() + " " + infosConjoint.getNom() + " " + infosConjoint.getPrenom();
    }

    /**
     * call the utils MotifVersement formater
     * 
     * @return
     */
    public String formatDecision() {
        String idTiersPrincipal = getIdTiersAddressePaiementRequerant();

        if (JadeStringUtil.isBlankOrZero(idTiersPrincipal) || Long.parseLong(idTiersPrincipal) < 0) {
            idTiersPrincipal = getIdTiersRequerant();
        }

        String isoLangFromIdTiers = PRTiersHelper.getIsoLangFromIdTiers(BSessionUtil.getSessionFromThreadContext(),
                idTiersPrincipal);

        // Libelle
        String libelle = "";
        try {
            CodeSystem csLibelle = CodeSystemUtils.searchCodeSystemTraduction("64055001",
                    BSessionUtil.getSessionFromThreadContext(), isoLangFromIdTiers);
            libelle = csLibelle.getTraduction();
        } catch (Exception e) {
            JadeLogger.warn(e, e.getMessage());
            libelle = BSessionUtil.getSessionFromThreadContext().getCodeLibelle("64055001");
        }

        // periode
        final String dateFinFinal = JadeStringUtil.isBlankOrZero(getDateFin()) ? REPmtMensuel
                .getDateDernierPmt(BSessionUtil.getSessionFromThreadContext()) : getDateFin();
        final String periode = getDateDebut() + " - " + dateFinFinal;

        // Message
        String message = MotifVersementUtil.getTranslatedLabelFromIsolangue(isoLangFromIdTiers,
                "PEGASUS_COMPTABILISATION_DECISION_DU", BSessionUtil.getSessionFromThreadContext());
        message += " " + getDateDecision();

        return MotifVersementUtil.formatDecision(getNssRequerant(), getNomRequerant() + " " + getPrenomRequerant(),
                getRefPaiement(), libelle, periode, message);
    }

}
