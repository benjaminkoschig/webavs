/**
 * 
 */
package globaz.amal.vb.famille;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.adressecourrier.TIPaysManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.contribuable.ContribuableOnly;
import ch.globaz.amal.business.models.contribuable.ContribuableSearch;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.FamilleContribuableView;
import ch.globaz.amal.business.models.famille.FamilleContribuableViewSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMFamilleViewBean extends BJadePersistentObjectViewBean {
    private Contribuable contribuable = null;
    private FamilleContribuable familleContribuable = null;

    // Get all subsides informations
    private List<FamilleContribuableView> listeFamilleContribuableView = new ArrayList<FamilleContribuableView>();
    // Get all subsides informations by family member
    private Map<String, List<FamilleContribuableView>> listeFamilleContribuableViewMember = new HashMap<String, List<FamilleContribuableView>>();

    public AMFamilleViewBean() {
        super();
        familleContribuable = new FamilleContribuable();
        contribuable = new Contribuable();
        if (familleContribuable.getSimpleFamille().getIdContribuable() != null) {
            setIdContribuable(familleContribuable.getSimpleFamille().getIdContribuable());
        }
    }

    public AMFamilleViewBean(FamilleContribuable familleContribuable) {
        super();
        this.familleContribuable = familleContribuable;
        contribuable = new Contribuable();
        if (this.familleContribuable.getSimpleFamille().getIdContribuable() != null) {
            setIdContribuable(this.familleContribuable.getSimpleFamille().getIdContribuable());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        familleContribuable = AmalServiceLocator.getFamilleContribuableService().create(familleContribuable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        familleContribuable = AmalServiceLocator.getFamilleContribuableService().delete(familleContribuable);
    }

    public Contribuable getContribuable() {
        return contribuable;
    }

    public FamilleContribuable getFamilleContribuable() {
        return familleContribuable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return familleContribuable.getId();
    }

    /**
     * @param id
     *            ID du code système
     * 
     * @return libelle général du code système correspondant
     * 
     */
    public String getLibelleCodeSysteme(String id) {

        if ((id == null) || (id.length() <= 0)) {
            return "";
        }

        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();

        cm.setSession(getSession());
        cm.setForCodeUtilisateur(id);
        cm.setForIdGroupe("AMMODELES");
        cm.setForIdLangue(getSession().getIdLangue());
        try {
            cm.find();
        } catch (Exception e) {
            JadeLogger.error(this, "Error cm.find : " + e.toString());
            return "";
        }

        FWParametersCode code = (FWParametersCode) cm.getEntity(0);

        if (code == null) {
            return "";
        } else {
            return code.getLibelle();
        }
    }

    /**
     * 
     * @param id
     *            code system to find
     * @param codeGroupe
     *            AMMODELES, AMTYDE, etc...
     * @return
     */
    public String getLibelleCodeSysteme(String id, String codeGroupe) {
        if ((id == null) || (id.length() <= 0)) {
            return "";
        }

        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();

        cm.setSession(getSession());
        cm.setForCodeUtilisateur(id);
        cm.setForIdGroupe(codeGroupe);
        cm.setForIdLangue(getSession().getIdLangue());
        try {
            cm.find();
        } catch (Exception e) {
            JadeLogger.error(this, "Error cm.find : " + e.toString());
            return "";
        }

        FWParametersCode code = (FWParametersCode) cm.getEntity(0);

        if (code == null) {
            return "";
        } else {
            return code.getLibelle();
        }
    }

    public String getLibellePays(String idPays) {
        BSession session = getSession();

        TIPaysManager paysManager = new TIPaysManager();
        paysManager.setSession(session);
        paysManager.setForIdPays(idPays);
        try {
            paysManager.find();
            TIPays pays = (TIPays) paysManager.getEntity(0);
            return pays.getLibelle();
        } catch (Exception e) {
            session.addWarning("Code pays introuvable pour l'idPays : " + idPays);
        }
        return "";
    }

    /**
     * @return the listeFamilleContribuableViewMember
     */
    public Map<String, List<FamilleContribuableView>> getListeFamilleContribuableViewMember() {
        return listeFamilleContribuableViewMember;
    }

    public String getPays(String idPays) {

        if (!JadeStringUtil.isBlankOrZero(idPays)) {
            BSession session = getSession();

            TIPaysManager paysManager = new TIPaysManager();
            paysManager.setSession(session);
            paysManager.setForIdPays(idPays);
            try {
                paysManager.find();
                TIPays pays = (TIPays) paysManager.getEntity(0);
                return pays.getCodeIso();
            } catch (Exception e) {
                session.addWarning("Code pays introuvable pour l'idPays : " + idPays);
            }
        }

        return "";

    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    public String getSexe(String idSexe) {
        if ("516001".equals(idSexe)) {
            return "H";
        } else if ("516002".equals(idSexe)) {
            return "F";
        } else {
            return "N/A";
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if ((familleContribuable != null) && (familleContribuable.getSimpleFamille() != null)) {
            return new BSpy(familleContribuable.getSimpleFamille().getSpy());
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        familleContribuable = AmalServiceLocator.getFamilleContribuableService().read(getId());
    }

    public void retrieveContribuable() throws Exception {

        // Retrieve Everybody from the family and find the contribuable
        ContribuableSearch search = new ContribuableSearch();
        search.setForIdContribuable(contribuable.getContribuable().getId());
        search = AmalServiceLocator.getContribuableService().search(search);

        // Set first contribuable (to get after the id and the idtiers of the main contribuable)
        ContribuableOnly resultContribuableSeul = null;
        if (search.getSearchResults().length > 0) {
            resultContribuableSeul = (ContribuableOnly) search.getSearchResults()[0];
        }

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            Contribuable currentContribuable = (Contribuable) it.next();
            // Set contribuable
            if ((null != resultContribuableSeul)
                    && resultContribuableSeul.getContribuable().getIdTier()
                            .equals(currentContribuable.getPersonneEtendue().getPersonneEtendue().getIdTiers())) {
                contribuable = currentContribuable;
                // Setting the adresse
                try {
                    contribuable.setAdresseComplexModel(AmalServiceLocator.getContribuableService()
                            .getContribuableAdresse(
                                    currentContribuable.getPersonneEtendue().getPersonneEtendue().getIdTiers()));
                } catch (Exception e) {
                    JadeLogger.error(this, "Error Loading adresse : " + contribuable.getId() + " _ " + e.getMessage());

                }
                // Setting the histo numero
                try {
                    contribuable.setHistoNumeroContribuable(AmalServiceLocator.getContribuableService()
                            .getContribuableHistoriqueNoContribuable(
                                    currentContribuable.getPersonneEtendue().getPersonneEtendue().getIdTiers()));
                } catch (Exception e) {
                    JadeLogger.error(this, "Error Loading histo numéro contribuable : " + contribuable.getId() + " _ "
                            + e.getMessage());
                }

            }
        }

        RetrieveFamilleContribuableViewList();
    }

    private void RetrieveFamilleContribuableViewList() throws Exception {
        // Retrieve Detail Famille
        FamilleContribuableViewSearch famillesearch = new FamilleContribuableViewSearch();

        // Set searched parameters
        famillesearch.setForIdContribuable(contribuable.getId());
        // Perform search
        famillesearch = AmalServiceLocator.getFamilleContribuableService().search(famillesearch);

        // Create empty arrays
        listeFamilleContribuableView = new ArrayList<FamilleContribuableView>();
        listeFamilleContribuableViewMember = new HashMap<String, List<FamilleContribuableView>>();

        // return results
        for (Iterator it = Arrays.asList(famillesearch.getSearchResults()).iterator(); it.hasNext();) {
            FamilleContribuableView familleContribuableView = (FamilleContribuableView) it.next();
            listeFamilleContribuableView.add(familleContribuableView);

            // Fill the Map by member
            // String memberId = familleContribuableView.getNumAvsActuel();
            String memberId = familleContribuableView.getFamilleId();
            // si pas de no AVS, prendre nom-prenom
            if (JadeStringUtil.isBlankOrZero(memberId)) {
                memberId = familleContribuableView.getNomPrenom();
            }
            if (!JadeStringUtil.isBlankOrZero(memberId)) {
                List<FamilleContribuableView> currentListMember = listeFamilleContribuableViewMember.get(memberId);
                if (null == currentListMember) {
                    // create annee historique key and related arraylist
                    currentListMember = new ArrayList<FamilleContribuableView>();
                }
                // add and put result in the map
                currentListMember.add(familleContribuableView);
                listeFamilleContribuableViewMember.put(memberId, currentListMember);
            }
        }
    }

    public void setContribuable(Contribuable contribuable) {
        this.contribuable = contribuable;
    }

    public void setFamilleContribuable(FamilleContribuable familleContribuable) {
        this.familleContribuable = familleContribuable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        familleContribuable.setId(newId);

    }

    public void setIdContribuable(String contribuableId) {
        contribuable.getContribuable().setId(contribuableId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        familleContribuable = AmalServiceLocator.getFamilleContribuableService().update(familleContribuable);
    }

}
