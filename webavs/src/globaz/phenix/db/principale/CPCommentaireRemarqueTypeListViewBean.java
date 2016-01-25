package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPCommentaireRemarqueTypeListViewBean extends CPCommentaireRemarqueTypeManager implements
        FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;

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
    public String getCodeCommentaire(int pos) {
        String codeCommentaire = "";
        try {
            if ((CPCommentaireRemarqueType) getEntity(pos) != null) {
                String idCom = ((CPCommentaireRemarqueType) getEntity(pos)).getIdCommentaire();
                if (!JadeStringUtil.isIntegerEmpty(idCom)) {
                    codeCommentaire = globaz.phenix.translation.CodeSystem.getCode(getSession(), idCom);
                }
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        return codeCommentaire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdCommentaireRemarque(int pos) {
        return ((CPCommentaireRemarqueType) getEntity(pos)).getIdCommentaireRemarque();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getLibelleCommentaire(int pos) {
        String libelleCommentaire = "";
        try {
            if ((CPCommentaireRemarqueType) getEntity(pos) != null) {
                String idCommentaire = ((CPCommentaireRemarqueType) getEntity(pos)).getIdCommentaire();
                if (!JadeStringUtil.isIntegerEmpty(idCommentaire)) {
                    libelleCommentaire = globaz.phenix.translation.CodeSystem.getLibelle(getSession(), idCommentaire);
                }
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        return libelleCommentaire;
    }

    /**
     * Retourne le texte d'une remarque Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getTexteRemarqueType(int pos) {
        String remarqueType = "";
        try {
            if ((CPCommentaireRemarqueType) getEntity(pos) != null) {
                String idRemarqueType = ((CPCommentaireRemarqueType) getEntity(pos)).getIdRemarqueType();
                if (!JadeStringUtil.isIntegerEmpty(idRemarqueType)) {
                    CPRemarqueType rem = new CPRemarqueType();
                    rem.setSession(getSession());
                    remarqueType = rem.getTexteRemarqueType(idRemarqueType);
                }
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        return remarqueType;
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
}
