/*
 * Créé le 18 mars 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.compteur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;

/**
 * @author spa Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CAUpdateCompteurManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = new String();
    private String forIdCompteAnnexe = new String();
    private String forIdRubrique = new String();

    /**
	 * 
	 */
    public CAUpdateCompteurManager() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "IDCOMPTEANNEXE, ANNEECOTISATION, SUM(MONTANT) AS MONTANT, SUM(MASSE) AS MASSE";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "caoperp";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "IDCOMPTEANNEXE,ANNEECOTISATION";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "IDTYPEOPERATION LIKE 'E%' AND (ETAT = " + APIOperation.ETAT_PROVISOIRE + " OR ETAT = "
                + APIOperation.ETAT_COMPTABILISE + ")";
        // CompteAnnexe
        if (!JadeStringUtil.isIntegerEmpty(getForIdCompteAnnexe())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCOMPTEANNEXE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe());
        }
        // Rubrique
        if (!JadeStringUtil.isIntegerEmpty(getForIdRubrique())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCOMPTE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRubrique());
        }
        // Annee
        if (!JadeStringUtil.isIntegerEmpty(getForAnnee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ANNEECOTISATION=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }
        // Ajout du group by
        sqlWhere += " GROUP BY IDCOMPTEANNEXE,ANNEECOTISATION";
        // Retour
        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAUpdateCompteur();
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
    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    /**
     * @return
     */
    public String getForIdRubrique() {
        return forIdRubrique;
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
    public void setForIdCompteAnnexe(String string) {
        forIdCompteAnnexe = string;
    }

    /**
     * @param string
     */
    public void setForIdRubrique(String string) {
        forIdRubrique = string;
    }

}
