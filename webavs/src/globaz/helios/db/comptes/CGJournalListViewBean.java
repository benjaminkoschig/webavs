package globaz.helios.db.comptes;

import globaz.helios.api.ICGJournal;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Insérez la description du type ici. Date de création : (09.09.2002 15:09:14)
 * 
 * @author: Administrator
 */

public class CGJournalListViewBean extends CGJournalManager implements globaz.framework.bean.FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String reqCritere = "";
    private java.lang.String reqLibelle = "";
    private java.lang.String reqUntilCritere = "";
    private java.lang.String reqUntilLibelle = "";

    /**
     * Commentaire relatif au constructeur CGModeleEcritureListViewBean.
     */
    public CGJournalListViewBean() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * Critères de recherche
     * 
     */
    @Override
    protected void _beforeFind(globaz.globall.db.BTransaction transaction) {
        // Gestion des criteres de recherche
        if (!JadeStringUtil.isBlank(getReqCritere())) {
            if (CGJournal.CS_TRI_LIBELLE.equals(getReqCritere())) {
                // Critere libelle
                setFromLibelle(getReqLibelle());
                setOrderby(CGJournal.FIELD_LIBELLE);
            } else if (CGJournal.CS_TRI_NUMERO.equals(getReqCritere())) {
                // Critere numero
                setFromNumero(getReqLibelle());
                setOrderby(CGJournal.FIELD_NUMERO + " DESC");
            } else if (CGJournal.CS_TRI_DATE.equals(getReqCritere())) {
                // Critere numero
                setFromDate(getReqLibelle());
                setOrderby(CGJournal.FIELD_DATE + " DESC");
            } else if (CGJournal.CS_TRI_PROPRIETAIRE.equals(getReqCritere())) {
                // Critere propriétaire
                setFromProprietaire(getReqLibelle());
                setOrderby(CGJournal.FIELD_PROPRIETAIRE + " DESC");
            }
        }

        if (!JadeStringUtil.isBlank(getReqUntilCritere())) {
            if (CGJournal.CS_TRI_LIBELLE.equals(getReqUntilCritere())) {
                // Critere libelle
                setUntilLibelle(getReqUntilLibelle());
                setOrderby(CGJournal.FIELD_LIBELLE);
            } else if (CGJournal.CS_TRI_NUMERO.equals(getReqUntilCritere())) {
                // Critere numero
                setUntilNumero(getReqUntilLibelle());
                setOrderby(CGJournal.FIELD_NUMERO + " DESC");
            } else if (CGJournal.CS_TRI_DATE.equals(getReqUntilCritere())) {
                // Critere numero
                setUntilDate(getReqUntilLibelle());
                setOrderby(CGJournal.FIELD_DATE + " DESC");
            } else if (CGJournal.CS_TRI_PROPRIETAIRE.equals(getReqUntilCritere())) {
                // Critere propriétaire
                setUntilProprietaire(getReqUntilLibelle());
                setOrderby(CGJournal.FIELD_PROPRIETAIRE + " DESC");
            }
        }

        if (ICGJournal.CS_ETAT_TOUS.equals(getForIdEtat())) {
            // pade restriction sur l'etat si on veux afficher ts les journaux
            setForIdEtat("");
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 11:23:38)
     * 
     * @return java.lang.String
     */
    public java.lang.String getReqCritere() {
        return reqCritere;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 11:27:41)
     * 
     * @return java.lang.String
     */
    public java.lang.String getReqLibelle() {
        return reqLibelle;
    }

    /**
     * Returns the reqUntilCritere.
     * 
     * @return java.lang.String
     */
    public java.lang.String getReqUntilCritere() {
        return reqUntilCritere;
    }

    /**
     * Returns the reqUntilLibelle.
     * 
     * @return java.lang.String
     */
    public java.lang.String getReqUntilLibelle() {
        return reqUntilLibelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 11:23:38)
     * 
     * @param newReqCritere
     *            java.lang.String
     */
    public void setReqCritere(java.lang.String newReqCritere) {
        reqCritere = newReqCritere;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 11:27:41)
     * 
     * @param newReqLibelle
     *            java.lang.String
     */
    public void setReqLibelle(java.lang.String newReqLibelle) {
        reqLibelle = newReqLibelle;
    }

    /**
     * Sets the reqUntilCritere.
     * 
     * @param reqUntilCritere
     *            The reqUntilCritere to set
     */
    public void setReqUntilCritere(java.lang.String reqUntilCritere) {
        this.reqUntilCritere = reqUntilCritere;
    }

    /**
     * Sets the reqUntilLibelle.
     * 
     * @param reqUntilLibelle
     *            The reqUntilLibelle to set
     */
    public void setReqUntilLibelle(java.lang.String reqUntilLibelle) {
        this.reqUntilLibelle = reqUntilLibelle;
    }

}
