package globaz.helios.db.comptes;

/**
 * Insérez la description du type ici. Date de création : (10.09.2002 15:43:38)
 * 
 * @author: Administrator
 */
import globaz.jade.client.util.JadeStringUtil;

public class CGExerciceComptableListViewBean extends CGExerciceComptableManager implements
        globaz.framework.bean.FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private java.lang.String action = null;

    private java.lang.String reqForIdMandat = "";
    // critere de recherche depuis ecran rc
    private java.lang.String reqFromDate = "";

    public java.lang.String getAction() {
        return action;
    }

    public String getExerciceComptable(int pos) {
        CGExerciceComptable entity = (CGExerciceComptable) getEntity(pos);

        String exerciceComptable = "";
        String dateDebut = entity.getDateDebut();
        String dateFin = entity.getDateFin();

        if (!JadeStringUtil.isBlank(dateDebut)) {
            exerciceComptable = dateDebut;
            if (!JadeStringUtil.isBlank(dateFin)) {
                exerciceComptable += " - " + dateFin;
            }
        }

        return exerciceComptable;

    }

    public String getIdExerciceComptable(int pos) {
        CGExerciceComptable entity = (CGExerciceComptable) getEntity(pos);
        return entity.getIdExerciceComptable();

    }

    public String getIdMandat(int pos) {
        CGExerciceComptable entity = (CGExerciceComptable) getEntity(pos);
        return entity.getIdMandat();

    }

    public String getMandatLibelle(int pos) {
        CGExerciceComptable entity = (CGExerciceComptable) getEntity(pos);
        return entity.getMandat().getLibelle();
    }

    public String getNumero(int pos) {
        CGExerciceComptable entity = (CGExerciceComptable) getEntity(pos);
        return entity.getIdExerciceComptable();

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.10.2002 15:58:51)
     * 
     * @return java.lang.String
     */
    public java.lang.String getReqForIdMandat() {
        return reqForIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.09.2002 10:18:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String getReqFromDate() {
        return reqFromDate;
    }

    public Boolean isEstCloture(int pos) {
        CGExerciceComptable entity = (CGExerciceComptable) getEntity(pos);
        return entity.isEstCloture();

    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.10.2002 15:58:51)
     * 
     * @param newReqForIdMandat
     *            java.lang.String
     */
    public void setReqForIdMandat(java.lang.String newReqForIdMandat) {
        reqForIdMandat = newReqForIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.09.2002 10:18:50)
     * 
     * @param newReqFromDate
     *            java.lang.String
     */
    public void setReqFromDate(java.lang.String newReqFromDate) {
        reqFromDate = newReqFromDate;
    }
}
