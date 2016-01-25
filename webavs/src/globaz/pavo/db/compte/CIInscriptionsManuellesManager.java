/*
 * Créé le 7 mars 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.compte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;

/**
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CIInscriptionsManuellesManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAffilie = "";
    private String forAnnee = "";
    private String fromAffilie = "";
    private String fromAnnee = "";
    private String fromMontant = "";
    /**
	 * 
	 */
    private String order = "";

    public CIInscriptionsManuellesManager() {
        super();
        // TODO Raccord de constructeur auto-généré
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = "";
        if (!JAUtil.isIntegerEmpty(fromMontant)) {
            if (!JAUtil.isStringEmpty(where)) {
                where += " AND ";
            }
            where += "KSMMON >=" + _dbWriteNumeric(statement.getTransaction(), getFromMontant());
        }

        if (!JAUtil.isStringEmpty(fromAnnee)) {
            if (!JAUtil.isStringEmpty(where)) {
                where += " AND ";
            }
            where += "KSNANN >=" + _dbWriteNumeric(statement.getTransaction(), getFromAnnee());
        }

        if (!JAUtil.isStringEmpty(fromAffilie)) {
            if (!JAUtil.isStringEmpty(where)) {
                where += " AND ";
            }
            where += "MALNAF LIKE " + _dbWriteString(statement.getTransaction(), getFromAffilie() + "%");
        }
        if (!JAUtil.isStringEmpty(forAffilie)) {
            if (!JAUtil.isStringEmpty(where)) {
                where += " AND ";
            }
            where += "MALNAF = " + _dbWriteString(statement.getTransaction(), getForAffilie());
        }
        if (!JAUtil.isStringEmpty(forAnnee)) {
            if (!JAUtil.isStringEmpty(where)) {
                where += " AND ";
            }
            where += "KSNANN =" + _dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }

        return where;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {

        return new CIInscriptionsManuelles();
    }

    /**
     * @return
     */
    public String getForAffilie() {
        return forAffilie;
    }

    /**
     * @return
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * @return
     */
    public String getFromAffilie() {
        return fromAffilie;
    }

    /**
     * @return
     */
    public String getFromAnnee() {
        return fromAnnee;
    }

    /**
     * @return
     */
    public String getFromMontant() {
        return fromMontant;
    }

    /**
     * @return
     */
    public String getOrder() {
        return order;
    }

    /**
     * @param string
     */
    public void setForAffilie(String string) {
        forAffilie = string;
    }

    /**
     * @param string
     */
    public void setForAnnee(String string) {
        forAnnee = string;
    }

    /**
     * @param string
     */
    public void setFromAffilie(String string) {
        fromAffilie = string;
    }

    /**
     * @param string
     */
    public void setFromAnnee(String string) {
        fromAnnee = string;
    }

    /**
     * @param string
     */
    public void setFromMontant(String string) {
        fromMontant = string;
    }

    /**
     * @param string
     */
    public void setOrder(String string) {
        order = string;
    }

}
