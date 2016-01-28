/*
 * Cr�� le 11 mai 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.leo.db.parametrage;

import globaz.envoi.db.parametreEnvoi.access.ENComplementFormuleManager;
import globaz.envoi.db.parametreEnvoi.access.IENComplementFormuleDefTable;
import globaz.envoi.db.parametreEnvoi.access.IENDefinitionFormuleDefTable;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.leo.constantes.ILEConstantes;

/**
 * @author ald
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class LEComplementFormuleManager extends ENComplementFormuleManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean wantDebutOrFin = true;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();
        if (!isWantDebutOrFin()) {
            sqlFrom.append(super._getFrom(statement));
            sqlFrom.append("  INNER JOIN " + _getCollection() + IENDefinitionFormuleDefTable.TABLE_NAME + " on ("
                    + _getCollection() + IENComplementFormuleDefTable.TABLE_NAME + "."
                    + IENComplementFormuleDefTable.ID_FORMULE + " = " + _getCollection()
                    + IENDefinitionFormuleDefTable.TABLE_NAME + "."
                    + IENDefinitionFormuleDefTable.ID_DEFINITION_FORMULE + " )");
            sqlFrom.append("  INNER JOIN (SELECT * FROM " + _getCollection() + IENComplementFormuleDefTable.TABLE_NAME
                    + " WHERE " + IENComplementFormuleDefTable.CS_TYPE_FORMULE + " = " + ILEConstantes.CS_IS_DEB_OR_FIN
                    + ") Cpl  ");
            sqlFrom.append(" ON Cpl." + IENComplementFormuleDefTable.ID_FORMULE + " = " + _getCollection()
                    + IENComplementFormuleDefTable.TABLE_NAME + ".PFO1ID ");
        } else {
            sqlFrom.append(super._getFrom(statement));
            sqlFrom.append("  INNER JOIN " + _getCollection() + "ENPPDEF on (" + _getCollection() + "ENPPFO2.PFO1ID="
                    + _getCollection() + "ENPPDEF.PDEFID)");
        }
        return sqlFrom.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();
        if (!isWantDebutOrFin()) {
            if (!JAUtil.isStringEmpty(getForCsValeur())) {
                if (!JAUtil.isStringEmpty(sqlWhere.toString())) {
                    sqlWhere.append(" AND ");
                }
                sqlWhere.append(_getCollection() + IENComplementFormuleDefTable.TABLE_NAME + "."
                        + IENComplementFormuleDefTable.CS_VALEUR + "=" + getForCsValeur());
            }
            if (!JAUtil.isStringEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            if (JAUtil.isStringEmpty(getForCsValeur())) {
                sqlWhere.append(_getCollection() + IENComplementFormuleDefTable.TABLE_NAME + "."
                        + IENComplementFormuleDefTable.CS_TYPE_FORMULE + " = " + ILEConstantes.CS_IS_DEB_OR_FIN
                        + " AND ");
            }
            sqlWhere.append(" Cpl." + IENComplementFormuleDefTable.CS_VALEUR + " = " + ILEConstantes.CS_NON);
        } else {
            sqlWhere.append(super._getWhere(statement));
        }
        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LEComplementFormuleViewBean();
    }

    /*
     * nous renseigne si l'on d�sire les formules de d�but et de fin.
     */
    public boolean isWantDebutOrFin() {
        return wantDebutOrFin;
    }

    /*
     * d�fini si l'on d�sire �galement les formules qui forment le d�but et la fin du suivi
     */
    public void setWantDebutOrFin(boolean b) {
        wantDebutOrFin = b;
    }

}
