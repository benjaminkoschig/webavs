package ch.globaz.pegasus.tests.util;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.dossier.Dossier;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class CasTest {

    private String dateProchainePaiement = null;
    private Demande demande = null;
    private String description = null;
    private Dossier dossier = null;
    private Droit droit = null;
    private String idGestionnaire = null;
    private String idTiers = null;
    private String NSS = null;
    private String requerantStringIdentifiant = null;

    public CasTest() {

    }

    public CasTest(String nss, String identifiantAsString, String description, String dateProchainPaiement)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        dateProchainePaiement = dateProchainPaiement;
        requerantStringIdentifiant = identifiantAsString;
        this.description = description;
        idGestionnaire = BSessionUtil.getSessionFromThreadContext().getUserId();
        NSS = nss;
        if ((NSS != null) && !JadeStringUtil.isBlankOrZero(NSS)) {
            idTiers = findIdTiers();
        }

        demande = new Demande();
        dossier = new Dossier();
        droit = new Droit();

    }

    private String findIdTiers() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {

        PersonneEtendueSearchComplexModel model = new PersonneEtendueSearchComplexModel();
        model.setForNumeroAvsActuel(NSS);
        model = TIBusinessServiceLocator.getPersonneEtendueService().find(model);
        if (model.getSearchResults().length != 1) {
            return null;
        }
        return ((PersonneEtendueComplexModel) model.getSearchResults()[0]).getTiers().getIdTiers();

    }

    public String getDateProchainePaiement() {
        return dateProchainePaiement;
    }

    public Demande getDemande() {
        return demande;
    }

    public void getDescription() {
        System.out.println("************************************");
        System.out.println(description);
        System.out.println("************************************");
    }

    public Dossier getDossier() {
        return dossier;
    }

    public Droit getDroit() {
        return droit;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNSS() {
        return NSS;
    }

    public String getRequerantStringIdentifiant() {
        return requerantStringIdentifiant;
    }

    public void setDateProchainePaiement(String dateProchainePaiement) {
        this.dateProchainePaiement = dateProchainePaiement;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    public void setDroit(Droit droit) {
        this.droit = droit;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNSS(String nSS) {
        NSS = nSS;
    }

    public void setRequerantStringIdentifiant(String requerantStringIdentifiant) {
        this.requerantStringIdentifiant = requerantStringIdentifiant;
    }
}
