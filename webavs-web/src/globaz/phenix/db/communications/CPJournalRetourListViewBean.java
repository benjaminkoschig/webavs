/*
 * Créé le 10 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author mmu Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPJournalRetourListViewBean extends BManager {

    private static final long serialVersionUID = 866818870872794065L;
    private Boolean afficheComptabiliseTotal = new Boolean(false);
    private Boolean estAbandonne = new Boolean(false);

    private String forIdJournal = "";
    private String forStatus = "";
    private String forTypeJournal = "";
    private Boolean isJournalEnCours = new Boolean(false);
    private String likeLibelleJournal = "";
    private String orderBy = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CPJOURP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return orderBy;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IWRJOU =" + this._dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }
        // traitement du positionnement
        if (getLikeLibelleJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IWRLIB like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeLibelleJournal() + "%");
        }

        // traitement du positionnement
        if (getForStatus().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IWRSTAT =" + this._dbWriteNumeric(statement.getTransaction(), getForStatus());
        }
        // Affiche tous les status sauf "abandon" par defaut
        if (!getEstAbandonne().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IWRSTAT <> " + this._dbWriteNumeric(statement.getTransaction(), CPJournalRetour.CS_ABANDONNE);
        }

        if (!getAfficheComptabiliseTotal().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " IWRSTAT <> " + CPJournalRetour.CS_COMPTABILISE_TOTAL;
        }

        if (getIsJournalEnCours().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " IWJRNC = \'1\'";
        }

        if (!JadeStringUtil.isEmpty(getForTypeJournal())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " IWTYPJ =" + this._dbWriteString(statement.getTransaction(), getForTypeJournal());
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPJournalRetour();
    }

    public Boolean getAfficheComptabiliseTotal() {
        return afficheComptabiliseTotal;
    }

    /**
     * @return
     */
    public Boolean getEstAbandonne() {
        return estAbandonne;
    }

    /**
     * @return
     */
    public String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * @return
     */
    public String getForStatus() {
        return forStatus;
    }

    public String getForTypeJournal() {
        return forTypeJournal;
    }

    public Boolean getIsJournalEnCours() {
        return isJournalEnCours;
    }

    /**
     * @return
     */
    public String getLikeLibelleJournal() {
        return likeLibelleJournal;
    }

    public void setAfficheComptabiliseTotal(Boolean afficheComptabiliseTotal) {
        this.afficheComptabiliseTotal = afficheComptabiliseTotal;
    }

    /**
     * @param estAbandonne
     */
    public void setEstAbandonne(Boolean estAbandonne) {
        this.estAbandonne = estAbandonne;
    }

    /**
     * @param string
     */
    public void setForIdJournal(String string) {
        forIdJournal = string;
    }

    /**
     * @param string
     */
    public void setForStatus(String string) {
        forStatus = string;
    }

    public void setForTypeJournal(String forTypeJournal) {
        this.forTypeJournal = forTypeJournal;
    }

    public void setIsJournalEnCours(Boolean isJournalEnCours) {
        this.isJournalEnCours = isJournalEnCours;
    }

    /**
     * @param string
     */
    public void setLikeLibelleJournal(String string) {
        likeLibelleJournal = string;
    }

    /**
     * @param string
     */
    public void setOrderBy(String string) {
        if (string.equals("NUM")) {
            orderBy = " IWRJOU DESC ";
        } else if (string.equals("NOM")) {
            orderBy = " IWRLIB ";
        } else if (string.equals("DATERECEPTION")) {
            orderBy = " IWRDAT DESC";
        } else if (string.equals("NBCOMMUNICATION")) {
            orderBy = " IWRCOM ";
        } else if (string.equals("STATUS")) {
            orderBy = " IWRSTAT ";
        }
    }

}
