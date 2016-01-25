package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.jade.log.JadeLogger;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPSortieListViewBean extends CPSortieManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;
    private String colonneSelection = "";
    private String dateSortie = "";
    private String idAffiliationCharge = "";

    /**
	 * 
	 */
    public CPSortieListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPSortieViewBean();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAnnee(int pos) {
        return ((CPSortie) getEntity(pos)).getAnnee();
    }

    public String getColonneSelection() {
        return colonneSelection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getDateSortie(int pos) {
        if (!((CPSortie) getEntity(pos)).getIdAffiliation().equalsIgnoreCase(idAffiliationCharge)) {
            boolean special = ((CPSortie) getEntity(pos)).getIsSpecial();
            dateSortie = ((CPSortie) getEntity(pos))._getDateSortie(special);
            idAffiliationCharge = (((CPSortie) getEntity(pos)).getIdAffiliation());
        }
        return dateSortie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdAffiliation(int pos) {
        return ((CPSortie) getEntity(pos)).getIdAffiliation();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdPassage(int pos) {
        return ((CPSortie) getEntity(pos)).getIdPassage();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdSortie(int pos) {
        return ((CPSortie) getEntity(pos)).getIdSortie();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdTiers(int pos) {
        return ((CPSortie) getEntity(pos)).getIdTiers();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getLibelleMotif(int pos) {

        String libelleMotif = "";
        try {
            boolean special = ((CPSortie) getEntity(pos)).getIsSpecial();
            // Special = sortie issue d'une date de fin d'affiliation (sinon date de début d'affiliation)
            if (!special) {
                return "";
            } else {
                libelleMotif = globaz.phenix.translation.CodeSystem.getLibelle(getSession(),
                        ((CPSortie) getEntity(pos)).getMotif());
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();

            return "";
        }
        return libelleMotif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getMontantCI(int pos) {
        return ((CPSortie) getEntity(pos)).getMontantCI();
    }

    public String getTierDescription(int pos) throws Exception {
        CPSortie entity = (CPSortie) getEntity(pos);
        return entity.getDescriptionTiers();
    }

    public Boolean isChecked(int pos) {
        return ((CPSortie) getEntity(pos)).getChecked();
    }

    public Boolean isRecap(int pos) {
        return ((CPSortie) getEntity(pos)).getIsRecap();
    }

    // Indique si c'est une sortie issue d'une modif de ddate début d'affiliation
    public Boolean isSpecial(int pos) {
        return ((CPSortie) getEntity(pos)).getIsSpecial();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setAction(String action) {
        this.action = action;
    }

    public void setColonneSelection(String value) {
        colonneSelection = value;
    }

}
