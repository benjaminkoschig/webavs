package globaz.auriga.vb.decisioncap;

import globaz.auriga.bean.EnfantDecisionCapBean;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ch.globaz.auriga.business.models.SimpleDecisionCAP;
import ch.globaz.auriga.business.models.SimpleEnfantDecisionCAP;
import ch.globaz.auriga.business.services.AurigaServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class AUDecisionCapAjaxViewBean extends BJadePersistentObjectViewBean implements FWAJAXViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String assuranceLibelle;
    private SimpleDecisionCAP decisionCap;
    private List<EnfantDecisionCapBean> listEnfantDecisionCapBean;
    private String listIdEnfants;

    public AUDecisionCapAjaxViewBean() {
        super();
        decisionCap = new SimpleDecisionCAP();
        listEnfantDecisionCapBean = new ArrayList<EnfantDecisionCapBean>();
    }

    @Override
    public void add() throws Exception {
        decisionCap = AurigaServiceLocator.getDecisionCAPService().create(decisionCap,
                convertStringToList(listIdEnfants));
    }

    /**
     * Split la chaine de caractère (séparateur ",") puis retourne le résulat sous forme de List
     * 
     * @param listIdEnfants
     * @return
     */
    private List<String> convertStringToList(String listIdEnfants) {
        List<String> idEnfantsArray = new ArrayList<String>();
        if (!JadeStringUtil.isBlankOrZero(listIdEnfants)) {
            String[] splitList = listIdEnfants.split(",");
            for (String idEnfant : splitList) {
                idEnfantsArray.add(idEnfant);
            }
        }
        return idEnfantsArray;
    }

    @Override
    public void delete() throws Exception {
        decisionCap = AurigaServiceLocator.getDecisionCAPService().delete(decisionCap);
    }

    public String getAssuranceLibelle() {
        return assuranceLibelle;
    }

    public SimpleDecisionCAP getDecisionCap() {
        return decisionCap;
    }

    @Override
    public String getId() {
        return decisionCap.getIdDecision();
    }

    public List<EnfantDecisionCapBean> getListEnfantDecisionCapBean() {
        return listEnfantDecisionCapBean;
    }

    public String getListIdEnfants() {
        return listIdEnfants;
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BSpy getSpy() {
        return decisionCap.isNew() ? new BSpy((BSession) getISession()) : new BSpy(decisionCap.getSpy());
    }

    @Override
    public boolean hasList() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterator iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    public void putIdEnfantToListIdEnfants(String idEnfant) {
        if (!JadeStringUtil.isBlankOrZero(listIdEnfants)) {
            listIdEnfants = listIdEnfants + "," + idEnfant;
        } else {
            listIdEnfants = idEnfant;
        }
    }

    @Override
    public void retrieve() throws Exception {
        // si on a un selectedId (read et update décision)
        if (!JadeStringUtil.isBlankOrZero(getId())) {
            // chargement de la décision CAP
            decisionCap = AurigaServiceLocator.getDecisionCAPService().read(decisionCap.getId());

            // chargement de l'assurance pour le libellé de l'assurance seulement
            AFAssuranceManager assuranceManager = new AFAssuranceManager();
            assuranceManager.setSession(BSessionUtil.getSessionFromThreadContext());
            assuranceManager.setForTypeAssurance(decisionCap.getCategorie());
            assuranceManager.find();
            if (assuranceManager.size() > 0) {
                AFAssurance assurance = (AFAssurance) assuranceManager.getFirstEntity();
                assuranceLibelle = assurance.getAssuranceLibelle();
            }

            // chargement des enfants de la décision
            List<SimpleEnfantDecisionCAP> listEnfantDecisionCap = new ArrayList<SimpleEnfantDecisionCAP>();
            listEnfantDecisionCap = AurigaServiceLocator.getEnfantDecisionCAPService().getListEnfantDecisionCap(
                    decisionCap.getId());

            // recherche des informations sur les enfants
            if (listEnfantDecisionCap.size() > 0) {
                for (SimpleEnfantDecisionCAP enfantDecisionCap : listEnfantDecisionCap) {
                    // recherche du tiers complex pour les informations sur les enfants
                    PersonneEtendueComplexModel tiersComplex = TIBusinessServiceLocator.getPersonneEtendueService()
                            .read(enfantDecisionCap.getIdTiers());

                    // création du bean
                    EnfantDecisionCapBean enfantDecisionCapBean = new EnfantDecisionCapBean(
                            enfantDecisionCap.getIdEnfantDecision(), enfantDecisionCap.getIdDecision(),
                            enfantDecisionCap.getIdTiers(), enfantDecisionCap.getDateNaissance(),
                            enfantDecisionCap.getDateRadiation(), enfantDecisionCap.getMontant(), tiersComplex
                                    .getTiers().getDesignation1(), tiersComplex.getTiers().getDesignation2());
                    listEnfantDecisionCapBean.add(enfantDecisionCapBean);
                    putIdEnfantToListIdEnfants(enfantDecisionCap.getIdTiers());
                }
            }
        }
    }

    public void setAssuranceLibelle(String assuranceLibelle) {
        this.assuranceLibelle = assuranceLibelle;
    }

    public void setDecisionCap(SimpleDecisionCAP decisionCap) {
        this.decisionCap = decisionCap;
    }

    @Override
    public void setGetListe(boolean getListe) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setId(String newId) {
        decisionCap.setIdDecision(newId);
    }

    public void setListEnfantDecisionCapBean(List<EnfantDecisionCapBean> listEnfantDecisionCapBean) {
        this.listEnfantDecisionCapBean = listEnfantDecisionCapBean;
    }

    public void setListIdEnfants(String listIdEnfants) {
        this.listIdEnfants = listIdEnfants;
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        // TODO Auto-generated method stub
    }

    @Override
    public void update() throws Exception {
        decisionCap = AurigaServiceLocator.getDecisionCAPService().update(decisionCap,
                convertStringToList(listIdEnfants));
    }

}
