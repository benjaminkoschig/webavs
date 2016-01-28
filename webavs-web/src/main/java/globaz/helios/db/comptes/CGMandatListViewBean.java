package globaz.helios.db.comptes;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;

public class CGMandatListViewBean extends CGMandatManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // pour critere recherche
    private String reqCritere = "";
    private java.lang.String reqLibelle = "";

    /**
     * Commentaire relatif au constructeur CGMandatListViewBean.
     */
    public CGMandatListViewBean() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * Critères de recherche pour une liste de mandats
     * 
     */
    @Override
    protected void _beforeFind(globaz.globall.db.BTransaction transaction) {
        // Gestion des criteres de recherche
        if (!JadeStringUtil.isBlank(getReqCritere())) {
            if (getReqCritere().equals(CGMandat.CS_RECHERCHE_NOM)) {
                // Critere nom
                String langue = getSession().getIdLangueISO();
                if (langue.equals("IT")) {
                    setFromLibelleIt(getReqLibelle());
                    setOrderby(CGMandat.FIELD_NOM_IT);
                } else if (langue.equals("AL")) {
                    setFromLibelleDe(getReqLibelle());
                    setOrderby(CGMandat.FIELD_NOM_DE);
                } else {
                    setFromLibelleFr(getReqLibelle());
                    setOrderby(CGMandat.FIELD_NOM_FR);
                }
            } else if (getReqCritere().equals(CGMandat.CS_RECHERCHE_NUMERO)) {
                // Critere numero
                setFromIdMandat(getReqLibelle());
                setOrderby(CGMandat.FIELD_NUMERO);
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.10.2002 14:42:59)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDescription(int pos) {
        CGMandat entity = (CGMandat) getEntity(pos);
        return entity.getLibelle();
    }

    public String getIdMandat(int pos) {
        CGMandat entity = (CGMandat) getEntity(pos);
        return entity.getIdMandat();

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.09.2002 13:40:05)
     * 
     * @return java.lang.String
     */
    public java.lang.String getReqCritere() {
        return reqCritere;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.10.2002 16:39:32)
     * 
     * @return java.lang.String
     */
    public java.lang.String getReqLibelle() {
        return reqLibelle;
    }

    public Boolean isEstVerrouille(int pos) {
        CGMandat entity = (CGMandat) getEntity(pos);
        return entity.isEstVerrouille();

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.09.2002 13:40:05)
     * 
     * @param newCritere
     *            java.lang.String
     */
    public void setReqCritere(java.lang.String newCritere) {
        reqCritere = newCritere;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.10.2002 16:39:32)
     * 
     * @param newReqLibelle
     *            java.lang.String
     */
    public void setReqLibelle(java.lang.String newReqLibelle) {
        reqLibelle = newReqLibelle;
    }
}
