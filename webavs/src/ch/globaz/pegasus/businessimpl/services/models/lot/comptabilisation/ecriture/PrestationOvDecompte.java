package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

}
