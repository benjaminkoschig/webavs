package globaz.helios.db.comptes;

import globaz.globall.db.BStatement;

/**
 * juste une vue pour l'ecran des mouvement de compte, il n'est pas prevu de travailler avec cette objet
 * 
 * Date de création : (17.12.2002 13:13:01)
 * 
 * @author: Administrator
 */
public class CGMouvementCompteViewBean extends CGEcritureViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String periode = "";

    /**
     * Commentaire relatif au constructeur CGMouvementCompteViewBean.
     */
    public CGMouvementCompteViewBean() {
        super();
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return super._getFields(statement) + ", code";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        super._readProperties(statement);
        periode = statement.dbReadString("CODE");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.12.2002 13:15:13)
     * 
     * @return java.lang.String
     */
    public java.lang.String getPeriode() {
        return periode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.12.2002 13:15:13)
     * 
     * @param newPeriode
     *            java.lang.String
     */
    public void setPeriode(java.lang.String newPeriode) {
        periode = newPeriode;
    }
}
