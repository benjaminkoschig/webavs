package globaz.leo.db.envoi;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEChampUtilisateurDefTable;

/**
 * @author jpa
 */
public class LEChampUtilisateurListViewBean extends BManager {
    private static final long serialVersionUID = 1L;
    private String forCsChamp = new String();
    private String forCsGroupe = new String();
    private String forIdChampUtilisateur = new String();
    private String forIdJournalisation = new String();
    private String forValeur = new String();

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + ILEChampUtilisateurDefTable.TABLE_NAME;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return ILEChampUtilisateurDefTable.CS_GROUPE + " , " + ILEChampUtilisateurDefTable.ID_CHAMP_UTILISATEUR
                + " DESC , " + ILEChampUtilisateurDefTable.ID_JOURNALISATION;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();
        // forCsChamp
        if (!JadeStringUtil.isBlank(getForCsChamp())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ILEChampUtilisateurDefTable.CS_CHAMP + " = " + getForCsChamp());
        }
        // forCsGroupe
        if (!JadeStringUtil.isBlank(getForCsGroupe())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ILEChampUtilisateurDefTable.CS_GROUPE + " = " + getForCsGroupe());
        }
        // forIdChampUtilisateur
        if (!JadeStringUtil.isBlank(getForIdChampUtilisateur())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ILEChampUtilisateurDefTable.ID_CHAMP_UTILISATEUR + " = " + getForIdChampUtilisateur());
        }
        // forIdJournalisation
        if (!JadeStringUtil.isBlank(getForIdJournalisation())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ILEChampUtilisateurDefTable.ID_JOURNALISATION + " = " + getForIdJournalisation());
        }
        // forValeur
        if (!JadeStringUtil.isBlank(getForValeur())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ILEChampUtilisateurDefTable.VALEUR + " = " + getForValeur());
        }
        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LEChampUtilisateurViewBean();
    }

    /*
     * GETTER AND SETTER
     */
    public String getForCsChamp() {
        return forCsChamp;
    }

    public String getForCsGroupe() {
        return forCsGroupe;
    }

    public String getForIdChampUtilisateur() {
        return forIdChampUtilisateur;
    }

    public String getForIdJournalisation() {
        return forIdJournalisation;
    }

    public String getForValeur() {
        return forValeur;
    }

    public void setForCsChamp(String string) {
        forCsChamp = string;
    }

    public void setForCsGroupe(String string) {
        forCsGroupe = string;
    }

    public void setForIdChampUtilisateur(String string) {
        forIdChampUtilisateur = string;
    }

    public void setForIdJournalisation(String string) {
        forIdJournalisation = string;
    }

    public void setForValeur(String string) {
        forValeur = string;
    }
}
