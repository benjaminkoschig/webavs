package globaz.pegasus.vb.droit;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCDroitHandler;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ch.globaz.common.domaine.Echeance.EcheanceDomaine;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtenduSearch;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciereSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public abstract class PCAbstractRequerantDonneeFinanciereViewBean extends BJadePersistentObjectViewBean implements
        Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // protected static final String CS_REQUERANT = "64004001";
    protected Map donnees = new HashMap();
    private ModificateurDroitDonneeFinanciere droit = null;
    private String idDossier = null;
    private String idVersion = null;

    protected List listeMembres = new ArrayList();
    private String noVersion = "";
    protected DroitMembreFamille requerant = null;

    /**
	 * 
	 */
    public PCAbstractRequerantDonneeFinanciereViewBean() {
        super();
        droit = new ModificateurDroitDonneeFinanciere();
    }

    /**
     * @param droit
     */
    public PCAbstractRequerantDonneeFinanciereViewBean(ModificateurDroitDonneeFinanciere droit) {
        super();
        this.droit = droit;
    }

    @Override
    public void add() throws Exception {
        throw new DroitException(
                "Cette methode n'est pas autorisée dans le viewbean de page générique des données financières");
    }

    @Override
    public void delete() throws Exception {
        throw new DroitException(
                "Cette methode n'est pas autorisée dans le viewbean de page générique des données financières");
    }

    /**
     * @throws JadePersistenceException
     * @throws DroitException
     * @throws JadeApplicationServiceNotAvailableException
     */
    protected void genereListeMembres() throws JadePersistenceException, DroitException,
            JadeApplicationServiceNotAvailableException {
        MembreFamilleEtenduSearch membreSearch = new MembreFamilleEtenduSearch();
        membreSearch.setForIdDroit(getId());
        membreSearch.setOrderKey("orderByRole");
        membreSearch = PegasusServiceLocator.getDroitService().searchMembreFamilleEtendu(membreSearch);
        for (Iterator it = Arrays.asList(membreSearch.getSearchResults()).iterator(); it.hasNext();) {
            MembreFamilleEtendu membre = (MembreFamilleEtendu) it.next();
            listeMembres.add(membre);

            if (requerant == null) {
                if (membre.getDroitMembreFamille().getSimpleDroitMembreFamille().getCsRoleFamillePC()
                        .equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
                    requerant = membre.getDroitMembreFamille();
                }
            }

        }
    }

    public List getDonnees(String idMembre) throws Exception {
        if (donnees.containsKey(idMembre)) {
            return (List) donnees.get(idMembre);
        } else {
            return new ArrayList();
        }
    }

    /**
     * @return the droit
     */
    public ModificateurDroitDonneeFinanciere getDroit() {
        return droit;
    }

    @Override
    public String getId() {
        return droit.getId();
    }

    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the idVersion
     */
    public String getIdVersion() {
        return idVersion;
    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays(BSession session, String codePays) {

        if ("999".equals(session.getCode(session.getSystemCode("CIPAYORI", codePays)))) {
            return "";
        } else {
            return session.getCodeLibelle(session.getSystemCode("CIPAYORI", codePays));
        }

    }

    public java.util.Collection getMembres() {
        return listeMembres;
    }

    public String getNoVersion() {
        return noVersion;
    }

    public String getRequerantDetail(BSession session) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // DroitSearch search = new DroitSearch();
        // search.setForIdDroit(this.droit.getSimpleDroit().getIdDroit());
        // Integer nbVersionDroit = PegasusServiceLocator.getDroitService().count(search);
        // Integer numDroitSuivant = null;
        // Integer numDroitPrecedant = nbVersionDroit - 1;

        // http://localhost:8080/webavs/pegasus?userAction=
        // pegasus.revenusdepenses.revenuActiviteLucrativeDependante.afficher&selectedId=221&idDemandePc=221&idDossier=131&idDroit=221&
        // noVersion=1&idVersionDroit=221&idTitreMenu=MENU_OPTION_DROITS_REVENUS_DEPENSES&idTitreOnglet=MENU_ONGLET_DROITS_REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE
        // http://localhost:8080/webavs/pegasus?userAction=
        // pegasus.renteijapi.renteAvsAi.afficher&selectedId=221&idDemandePc=221&idDroit=221&idDossier=131&
        // noVersion=1&idVersionDroit=221&idTitreMenu=MENU_OPTION_DROITS_RENTES_AJ_API&idTitreOnglet=MENU_ONGLET_DROITS_RENTE_AVS_API
        // if (!this.droit.getSimpleVersionDroit().getNoVersion().equals(String.valueOf(nbVersionDroit))) {
        // numDroitSuivant = nbVersionDroit + 1;
        // }

        return PCDroitHandler.getInfoHtmlRequerant(droit, requerant);
    }

    public String getIdTiersRequerant() {
        return requerant.getMembreFamille().getSimpleMembreFamille().getIdTiers();
    }

    @Override
    public BSpy getSpy() {

        if ((requerant == null) || JadeStringUtil.isEmpty(requerant.getSpy())) {
            return new BSpy((BSession) getISession());
        } else {
            return new BSpy(requerant.getSpy());
        }

    }

    public boolean isModifiable() {
        return !(IPCDroits.CS_VALIDE.equals(droit.getSimpleVersionDroit().getCsEtatDroit())
                || IPCDroits.CS_COURANT_VALIDE.equals(droit.getSimpleVersionDroit().getCsEtatDroit()) || IPCDroits.CS_HISTORISE
                    .equals(droit.getSimpleVersionDroit().getCsEtatDroit()));
    }

    @Override
    public void retrieve() throws Exception {
        // load droit
        ModificateurDroitDonneeFinanciereSearch droitSearch = new ModificateurDroitDonneeFinanciereSearch();
        droitSearch.setForIdDroit(getId());
        droitSearch.setForIdVersionDroit(getIdVersion());
        droitSearch.setForRoleMembreFamille(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        droitSearch = PegasusServiceLocator.getDroitService().searchDroitDonneeFinanciere(droitSearch);
        if (droitSearch.getSize() == 1) {
            droit = (ModificateurDroitDonneeFinanciere) droitSearch.getSearchResults()[0];
        } else {
            throw new DroitException(
                    "can't retrieve droit. "
                            + droitSearch.getSize()
                            + " element(s) was found for the same no version droit and id droit. With this data: CS_ROLE_FAMILLE_REQUERANT: "
                            + IPCDroits.CS_ROLE_FAMILLE_REQUERANT + ",idVersionDroit: " + getIdVersion()
                            + ", idDroit: " + getId());
        }

        // crée la liste des membres de famille
        genereListeMembres();
    }

    /**
     * @param droit
     *            the droit to set
     */
    public void setDroit(ModificateurDroitDonneeFinanciere droit) {
        this.droit = droit;
    }

    @Override
    public void setId(String newId) {
        droit.setId(newId);
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param idVersion
     *            the idVersion to set
     */
    public void setIdVersion(String idVersion) {
        this.idVersion = idVersion;
    }

    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    @Override
    public void update() throws Exception {
        throw new DroitException(
                "Cette methode n'est pas autorisée dans le viewbean de page générique des données financières");
    }

    public String getEcheanceDomainePegasus() {
        return EcheanceDomaine.PEGASUS.toString();
    }

}
