package globaz.pegasus.vb.dossier;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSessionUtil;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.models.dossier.DossierRCList;
import ch.globaz.pegasus.business.models.dossier.DossierRCListSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCDossierListViewBean extends BJadePersistentObjectListViewBean {
    private String csEtat = null;
    private DossierRCListSearch dossierSearch = null;
    private String isRechercheMembreFamille = null;

    public PCDossierListViewBean() {
        super();
        dossierSearch = new DossierRCListSearch();
    }

    public String getGedLabel() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_GED_LINK_LABEL");
    }

    @Override
    public void find() throws Exception {
        List<String> list = new ArrayList<String>();
        if (("EN_REVISION").equals(csEtat)) {
            list.add(IPCDemandes.CS_REVISION);
        } else if (("EN_COURS").equals(csEtat)) {
            list.add(IPCDemandes.CS_OCTROYE);
            list.add(IPCDemandes.CS_REVISION);
        } else if ("EN_PREMIERE_INSTRUCTION".equals(csEtat)) {
            list.add(IPCDemandes.CS_EN_ATTENTE_JUSTIFICATIFS);
            list.add(IPCDemandes.CS_EN_ATTENTE_CALCUL);
        }

        // pour la recherche sur le membres de famille avec NSS/NOM/PRENOM
        if ((isRechercheMembreFamille != null) && isRechercheMembreFamille.equalsIgnoreCase("ON")) {
            dossierSearch.setWhereKey(DossierRCListSearch.SEARCH_DEFINITION_RECHERCHE_FAMILLE);
        }

        dossierSearch.setInCsEtatDemande(list);
        dossierSearch = PegasusServiceLocator.getDossierService().searchRCList(dossierSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < dossierSearch.getSize() ? new PCDossierViewBean(
                (DossierRCList) dossierSearch.getSearchResults()[idx]) : new PCDossierViewBean();
    }

    public String getCsEtat() {
        return csEtat;
    }

    /**
     * @return the dossierSearch
     */
    public DossierRCListSearch getDossierSearch() {
        return dossierSearch;
    }

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return dossierSearch.getForDateNaissance();
    }

    public String getIsRechercheMembreFamille() {
        return isRechercheMembreFamille;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return dossierSearch;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    /**
     * @param dossierSearch
     *            the dossierSearch to set
     */
    public void setDossierSearch(DossierRCListSearch dossierSearch) {
        this.dossierSearch = dossierSearch;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        dossierSearch.setForDateNaissance(forDateNaissance);
    }

    public void setIsRechercheMembreFamille(String isRechercheMembreFamille) {
        this.isRechercheMembreFamille = isRechercheMembreFamille;
    }

}
