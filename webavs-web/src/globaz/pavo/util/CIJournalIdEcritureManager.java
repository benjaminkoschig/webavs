/*
 * Créé le 10 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.util;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CIEcriture;

/**
 * Manager selectionnant tous les assurés inconnus au RA pour un journal donné
 * 
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CIJournalIdEcritureManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean certificat = false;
    private boolean ecrituresNonRA = false;
    private boolean exclureRA = false;
    private String forIdJournal = null;

    public CIJournalIdEcritureManager() {
        super();
        // TODO Raccord de constructeur auto-généré
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return _getCollection() + "CIINDIP.KAIIND, " + _getCollection() + "CIINDIP.KANAVS, " + _getCollection()
                + "CIINDIP.KALNOM";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CIINDIP, " + _getCollection() + "CIECRIP, " + _getCollection() + "CIJOURP ";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = _getCollection() + "CIECRIP.KCID = " + _getCollection() + "CIJOURP.KCID AND "
                + _getCollection() + "CIECRIP.KAIIND = " + _getCollection() + "CIINDIP.KAIIND";

        if (isExclureRA()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIINDIP.KAIREG = " + CICompteIndividuel.CS_REGISTRE_PROVISOIRE;

        }

        if (isCertificat()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND KBBIMP ='1'";
            } else {
                sqlWhere += " KBBIMP ='1'";
            }

        }

        if (isEcrituresNonRA()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ((LENGTH(RTRIM(" + _getCollection() + "CIINDIP.KANAVS)) >=8 AND " + _getCollection()
                        + "CIINDIP.KALNOM !='')OR(LENGTH(RTRIM(" + _getCollection() + "CIINDIP.KANAVS)) >=8 AND "
                        + _getCollection() + "CIINDIP.KALNOM ='')OR(LENGTH(RTRIM(" + _getCollection()
                        + "CIINDIP.KANAVS)) <8 AND " + _getCollection() + "CIINDIP.KALNOM !=''))";

            } else {

                sqlWhere += " ((LENGTH(RTRIM(" + _getCollection() + "CIINDIP.KANAVS)) >=8 AND " + _getCollection()
                        + "CIINDIP.KALNOM !='')OR(LENGTH(RTRIM(" + _getCollection() + "CIINDIP.KANAVS)) >=8 AND "
                        + _getCollection() + "CIINDIP.KALNOM ='')OR(LENGTH(RTRIM(" + _getCollection()
                        + "CIINDIP.KANAVS)) <8 AND " + _getCollection() + "CIINDIP.KALNOM !=''))";
            }
        }

        if (getForIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KCID="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }
        sqlWhere += " GROUP BY " + _getCollection() + "CIINDIP.KAIIND, " + _getCollection() + "CIINDIP.KANAVS, "
                + _getCollection() + "CIINDIP.KALNOM";
        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIEcriture();
    }

    /**
     * Renvoie l'id du journal
     * 
     * @return forIdJournal
     */
    public String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * Renvoie un valeur indiquant si un certificat est demandé ou non
     * 
     * @return certificat
     */
    public boolean isCertificat() {
        return certificat;
    }

    /**
     * Renvoie une valeur indiquant si on veut uniquement les assurés dont le nom est connu et/ou qui ont un numéro AVS
     * d'au moins huit chiffres
     * 
     * @return ecrituresNonRA
     */
    public boolean isEcrituresNonRA() {
        return ecrituresNonRA;
    }

    /**
     * Renvoie une valeur indiquant si on veut les assures non connus au RA
     * 
     * @return exclureRA
     */
    public boolean isExclureRA() {
        return exclureRA;
    }

    /**
     * Sette une valeur indiquant si un certificat est demandé ou non
     * 
     * @param b
     */
    public void setCertificat(boolean b) {
        certificat = b;
    }

    /**
     * Sette une valeur indiquant si on veut uniquement les assurés dont le nom est connu et /ou qui ont un numéro AVS
     * d'au moins huit chiffres
     * 
     * @param b
     */
    public void setEcrituresNonRA(boolean b) {
        ecrituresNonRA = b;
    }

    /**
     * Sette une valeur indiquant si on veut les assurés non connus au RA
     * 
     * @param b
     */
    public void setExclureRA(boolean b) {
        exclureRA = b;
    }

    /**
     * Sette l'id du journal
     * 
     * @param string
     */
    public void setForIdJournal(String string) {
        forIdJournal = string;
    }

}
