package globaz.pegasus.vb.droit;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
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
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnellesSearch;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciereSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class PCSaisieDonneesPersonnellesViewBean extends BJadePersistentObjectViewBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Map donnees = new HashMap();
    private ModificateurDroitDonneeFinanciere droit = null;
    private String idDossier = null;
    private String idVersion = null;
    private List<DroitMembreFamille> listeMembres = new ArrayList<DroitMembreFamille>();

    protected DroitMembreFamille requerant = null;

    /**
	 * 
	 */
    public PCSaisieDonneesPersonnellesViewBean() {
        super();
        droit = new ModificateurDroitDonneeFinanciere();
    }

    @Override
    public void add() throws Exception {
        throw new DroitException(
                "Cette methode n'est pas autorisée dans le viewbean de page générique des données personnelles");
    }

    @Override
    public void delete() throws Exception {
        throw new DroitException(
                "Cette methode n'est pas autorisée dans le viewbean de page générique des données personnelles");
    }

    /**
     * @return the donnees
     */
    public DonneesPersonnelles getDonnees(String idMembre) throws Exception {
        if (donnees.containsKey(idMembre)) {
            return (DonneesPersonnelles) donnees.get(idMembre);
        } else {
            return null;
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

    public String getInfoRepondant(DonneesPersonnelles donnee) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        String infos = "";
        if (!JadeStringUtil.isBlankOrZero(donnee.getSimpleDonneesPersonnelles().getIdTiersRepondant())) {
            TiersSimpleModel simpleTiersSimpleModel = TIBusinessServiceLocator.getTiersService().read(
                    donnee.getSimpleDonneesPersonnelles().getIdTiersRepondant());
            if ((simpleTiersSimpleModel != null) && !JadeStringUtil.isEmpty(simpleTiersSimpleModel.getIdTiers())) {
                infos = simpleTiersSimpleModel.getDesignation1() + " " + simpleTiersSimpleModel.getDesignation2();
            }
        }

        return infos;
    }

    /**
     * @return the listeMembres
     */
    public List getListeMembres() {
        return listeMembres;
    }

    /**
     * @return the requerant
     */
    public DroitMembreFamille getRequerant() {
        return requerant;
    }

    public String getRequerantDetail(BSession session) {
        return PCDroitHandler.getInfoHtmlRequerant(droit, requerant);
    }

    @Override
    public BSpy getSpy() {

        if ((requerant == null) || JadeStringUtil.isEmpty(requerant.getSpy())) {
            return new BSpy((BSession) getISession());
        } else {
            return new BSpy(requerant.getSpy());
        }

    }

    @Override
    public void retrieve() throws Exception {
        // load droit
        ModificateurDroitDonneeFinanciereSearch droitSearch = new ModificateurDroitDonneeFinanciereSearch();
        droitSearch.setForIdDroit(getId());
        droitSearch.setForRoleMembreFamille(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        droitSearch.setOrderKey("byVersion");
        droitSearch = PegasusServiceLocator.getDroitService().searchDroitDonneeFinanciere(droitSearch);
        if (droitSearch.getSize() > 0) {
            droit = (ModificateurDroitDonneeFinanciere) droitSearch.getSearchResults()[0];
        } else {
            throw new DroitException("can't retrieve droit. No requerant was found for the same id droit");
        }

        // load personnes
        DroitMembreFamilleSearch membreSearch = new DroitMembreFamilleSearch();
        membreSearch.setForIdDroit(getId());
        membreSearch.setOrderKey("orderByRole");
        membreSearch = PegasusServiceLocator.getDroitService().searchDroitMembreFamille(membreSearch);
        for (Iterator it = Arrays.asList(membreSearch.getSearchResults()).iterator(); it.hasNext();) {
            DroitMembreFamille membre = (DroitMembreFamille) it.next();
            listeMembres.add(membre);

            if (requerant == null) {
                if (membre.getSimpleDroitMembreFamille().getCsRoleFamillePC()
                        .equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
                    requerant = membre;
                }
            }
        }

        // cherche les données financières
        DonneesPersonnellesSearch search = new DonneesPersonnellesSearch();
        search.setForIdDroit(getId());
        search.setWhereKey(DonneesPersonnellesSearch.FOR_DROIT);
        search = PegasusServiceLocator.getDroitService().searchDonneesPersonnelles(search);

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            DonneesPersonnelles donnee = (DonneesPersonnelles) it.next();

            String idMbrFamille = donnee.getDroitMbrFam().getIdDroitMembreFamille();

            donnees.put(idMbrFamille, donnee);
        }

    }

    /**
     * @param donnees
     *            the donnees to set
     */
    public void setDonnees(Map donnees) {
        this.donnees = donnees;
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

    /**
     * @param listeMembres
     *            the listeMembres to set
     */
    public void setListeMembres(List listeMembres) {
        this.listeMembres = listeMembres;
    }

    /**
     * @param requerant
     *            the requerant to set
     */
    public void setRequerant(DroitMembreFamille requerant) {
        this.requerant = requerant;
    }

    @Override
    public void update() throws Exception {
        throw new DroitException(
                "Cette methode n'est pas autorisée dans le viewbean de page générique des données personnelles");
    }

}
