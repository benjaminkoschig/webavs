package globaz.aquila.db.batch;

import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.batch.transition.COTransitionAction;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.taxes.COTaxe;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.process.interetmanuel.visualcomponent.CAInteretManuelVisualComponent;
import globaz.osiris.utils.CAUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Représente le model de la vue "_de"
 * 
 * @author Pascal Lovy, 29-nov-2004
 */
public class COTransitionViewBean extends COTransition implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_MAX_ROWS = 8;
    private static final int DEFAULT_SHOW_ROWS = 1;

    public static final String LIBELLE = "libelle";
    public static final String MONTANT = "montant";

    private static final String PROPERTY_MAX_ROWS = "fraisMaxRows";
    private static final String PROPERTY_SHOW_ROWS = "fraisShowRows";
    public static final String RUB_DESCRIPTION = "rubDescription";
    public static final String RUBRIQUE = "rubIdExterne";

    private COTransitionAction action = null;
    private COContentieux contentieux = null;

    private List fraisEtInterets = null;
    private String idContentieux = "";
    private List<CAInteretManuelVisualComponent> interetCalcule = null;
    private String libSequence = "";

    private Boolean refresh;

    private String selectedId = "";
    private List<COTaxe> taxes = null;

    /**
	 *
	 */
    public COTransitionViewBean() {
        super();
        fraisEtInterets = new ArrayList();
    }

    /**
     * @return the action
     */
    public COTransitionAction getAction() {
        return action;
    }

    /**
     * @return the contentieux
     */
    public COContentieux getContentieux() {
        return contentieux;
    }

    /**
     * @return the fraisEtInterets
     */
    public List getFraisEtInterets() {
        return fraisEtInterets;
    }

    /**
     * @param i
     * @return HashMap
     */
    public HashMap getFraisEtInterets(int i) {
        if (this.getFraisEtInterets().size() >= i) {
            return (HashMap) this.getFraisEtInterets().get(i);
        } else {
            return null;
        }
    }

    /**
     * @return the idContentieux
     */
    public String getIdContentieux() {
        return idContentieux;
    }

    /**
     * @return
     */
    public List<CAInteretManuelVisualComponent> getInteretCalcule() {
        return interetCalcule;
    }

    /**
     * @return the libSequence
     */
    public String getLibSequence() {
        return libSequence;
    }

    /**
     * @return le nombre maximum de ligne
     */
    public int getMaxRows() {
        try {
            return Integer.parseInt(getSession().getApplication().getProperty(COTransitionViewBean.PROPERTY_MAX_ROWS)
                    .trim());
        } catch (Exception e) {
            return COTransitionViewBean.DEFAULT_MAX_ROWS;
        }
    }

    /**
     * @return the refresh
     */
    public Boolean getRefresh() {
        return refresh;
    }

    /**
     * @return le code HTML du select des rubriques
     */
    public String getRubriquesCode() {
        ArrayList listeNature = null;
        try {
            listeNature = (ArrayList) COServiceLocator.getConfigService().getFraisNaturesRubriques(getSession());
        } catch (Exception e) {
            return "";
        }

        if (listeNature == null) {
            return "";
        }

        return CAUtil.getForNatureRubriqueFraisIn(listeNature, getSession());
    }

    /**
     * @return the selectedId
     */
    public String getSelectedId() {
        return selectedId;
    }

    /**
     * @return le nombre de ligne à afficher.
     */
    public int getShowRows() {
        try {
            return Integer.parseInt(getSession().getApplication().getProperty(COTransitionViewBean.PROPERTY_SHOW_ROWS)
                    .trim());
        } catch (Exception e) {
            return COTransitionViewBean.DEFAULT_SHOW_ROWS;
        }
    }

    /**
     * @return List liste des taxes
     */
    public List<COTaxe> getTaxes() {
        return taxes;
    }

    /**
     * @return true s'il s'agit de frais variables
     */
    public boolean isFraisVariables() {

        Iterator iterator;

        try {
            iterator = getEtapeSuivante().loadEtapeInfoConfigs().iterator();
        } catch (Exception e) {
            return false;
        }

        while (iterator.hasNext()) {
            String libelle = ((COEtapeInfoConfig) iterator.next()).getCsLibelle();

            if (COEtapeInfoConfig.CS_FRAIS_VARIABLES.equals(libelle)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return true s'il s'agit d'intérêts variables
     */
    public boolean isInteretsVariables() {

        Iterator iterator;

        try {
            iterator = getEtapeSuivante().loadEtapeInfoConfigs().iterator();
        } catch (Exception e) {
            return false;
        }

        while (iterator.hasNext()) {
            String libelle = ((COEtapeInfoConfig) iterator.next()).getCsLibelle();

            if (COEtapeInfoConfig.CS_INTERETS.equals(libelle)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param action
     *            the action to set
     */
    public void setAction(COTransitionAction action) {
        this.action = action;
    }

    /**
     * @param contentieux
     *            the contentieux to set
     */
    public void setContentieux(COContentieux contentieux) {
        this.contentieux = contentieux;
    }

    /**
     * @param fraisEtInterets
     *            the fraisEtInterets to set
     */
    public void setFraisEtInterets(ArrayList fraisEtInterets) {
        this.fraisEtInterets = fraisEtInterets;
    }

    /**
     * @param idContentieux
     *            the idContentieux to set
     */
    public void setIdContentieux(String idContentieux) {
        this.idContentieux = idContentieux;
    }

    /**
     * @param interetCalcule
     */
    public void setInteretCalcule(List<CAInteretManuelVisualComponent> interetCalcule) {
        this.interetCalcule = interetCalcule;
    }

    /**
     * @param libSequence
     *            the libSequence to set
     */
    public void setLibSequence(String libSequence) {
        this.libSequence = libSequence;
    }

    /**
     * @param refresh
     *            the refresh to set
     */
    public void setRefresh(Boolean refresh) {
        this.refresh = refresh;
    }

    /**
     * @param selectedId
     *            the selectedId to set
     */
    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    /**
     * @param list
     */
    public void setTaxes(List<COTaxe> list) {
        taxes = list;
    }
}
