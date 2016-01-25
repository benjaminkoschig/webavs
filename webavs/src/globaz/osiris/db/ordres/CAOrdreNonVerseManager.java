package globaz.osiris.db.ordres;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationOrdreManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager permettant de contrôler si un ordre de versmenet non traité est en cours pour une section.
 * 
 * @author DDA
 * 
 */
public class CAOrdreNonVerseManager extends CAOperationOrdreManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAdressePaiement;
    private Boolean forOrdresNonRecouvert = new Boolean(false);
    private Boolean forOrdresNonVerse = new Boolean(true);

    private Boolean forTousOrdresEnAttente = new Boolean(false);

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAOperation.TABLE_CAOPERP + " LEFT OUTER JOIN " + _getCollection()
                + CAOrdreVersement.TABLE_CAOPOVP + " ON " + _getCollection() + CAOperation.TABLE_CAOPERP + "."
                + CAOperation.FIELD_IDOPERATION + "=" + _getCollection() + CAOrdreVersement.TABLE_CAOPOVP + "."
                + CAOrdreVersement.FIELD_IDORDRE + " LEFT OUTER JOIN " + _getCollection()
                + CACompteAnnexe.TABLE_CACPTAP + " ON " + _getCollection() + CAOperation.TABLE_CAOPERP + "."
                + CAOperation.FIELD_IDCOMPTEANNEXE + "=" + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "."
                + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + " LEFT OUTER JOIN " + _getCollection()
                + CAOrdreGroupe.TABLE_CAORGRP + " ON " + _getCollection() + CAOrdreGroupe.TABLE_CAORGRP + "."
                + CAOrdreGroupe.FIELD_IDORDREGROUPE + "=" + _getCollection() + CAOrdreVersement.TABLE_CAOPOVP + "."
                + CAOrdreVersement.FIELD_IDORDREGROUPE + " LEFT OUTER JOIN " + _getCollection()
                + CAJournal.TABLE_CAJOURP + " ON " + _getCollection() + CAJournal.TABLE_CAJOURP + "."
                + CAJournal.FIELD_IDJOURNAL + "=" + _getCollection() + CAOperation.TABLE_CAOPERP + "."
                + CAOperation.FIELD_IDJOURNAL;
    }

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        if (getForTousOrdresEnAttente().booleanValue()) {
            List list = new ArrayList();
            list.add(APIOperation.CAOPERATIONORDREVERSEMENT);
            list.add(APIOperation.CAOPERATIONORDREVERSEMENTAVANCE);
            list.add(APIOperation.CAOPERATIONORDRERECOUVREMENT);
            setForIdTypeOperationIn(list);

        } else {
            setForIdTypeOperation(APIOperation.CAOPERATIONORDREVERSEMENT);
        }
        String sqlWhere = super._getWhere(statement);

        if (isForOrdresNonVerse() || isForOrdresNonRecouvert()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "((" + _getCollection() + CAOrdreVersement.TABLE_CAOPOVP + "."
                    + CAOrdreVersement.FIELD_IDORDREGROUPE + " > 0";
            sqlWhere += " AND ";
            sqlWhere += _getCollection() + CAOrdreGroupe.TABLE_CAORGRP + "." + CAOrdreGroupe.FIELD_ETAT + " not in ("
                    + CAOrdreGroupe.TRANSMIS + ", " + CAOrdreGroupe.GENERE + ", " + CAOrdreGroupe.ANNULE + ")";
            sqlWhere += ") or (";
            sqlWhere += _getCollection() + CAOrdreVersement.TABLE_CAOPOVP + "." + CAOrdreVersement.FIELD_IDORDREGROUPE
                    + " = 0 and " + CAOrdreVersement.FIELD_ESTBLOQUE + " = " + BConstants.DB_BOOLEAN_FALSE_DELIMITED;
            sqlWhere += "))";
            // Si les 2 sont remplis, le test est déjà fait plus haut
            if (isForOrdresNonVerse() && !isForOrdresNonRecouvert()) {
                sqlWhere += " AND ";
                sqlWhere += _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDTYPEOPERATION
                        + "='" + APIOperation.CAOPERATIONORDREVERSEMENT + "'";
            }
            if (isForOrdresNonRecouvert() && !isForOrdresNonVerse()) {
                sqlWhere += " AND ";
                sqlWhere += _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDTYPEOPERATION
                        + "='" + APIOperation.CAOPERATIONORDRERECOUVREMENT + "'";
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdAdressePaiement())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + CAOrdreVersement.TABLE_CAOPOVP + "."
                    + CAOrdreVersement.FIELD_IDADRESSEPAIEMENT + " = " + getForIdAdressePaiement();
        }

        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }
        sqlWhere += _getCollection() + CAJournal.TABLE_CAJOURP + "." + CAJournal.FIELD_ETAT + " <> " + CAJournal.ANNULE;

        return sqlWhere;
    }

    public String getForIdAdressePaiement() {
        return forIdAdressePaiement;
    }

    /**
     * Récupère si le test sur les ordes non recouverts doit être appliqué
     * 
     * @return
     */
    public Boolean getForOrdresNonRecouvert() {
        return forOrdresNonRecouvert;
    }

    public Boolean getForOrdresNonVerse() {
        return forOrdresNonVerse;
    }

    /**
     * @return the forTousOrdresEnAttente
     */
    public Boolean getForTousOrdresEnAttente() {
        return forTousOrdresEnAttente;
    }

    public String getSql(BStatement statement) {
        StringBuffer sqlBuffer = new StringBuffer("SELECT ");
        sqlBuffer.append(CAOperation.FIELD_IDSECTION);
        sqlBuffer.append(" FROM ");
        sqlBuffer.append(_getFrom(statement));
        sqlBuffer.append(" WHERE ");
        sqlBuffer.append(_getWhere(statement));
        return sqlBuffer.toString();
    }

    /**
     * Récupère si le test sur les ordes non recouverts doit être appliqué
     * 
     * @return
     */
    public boolean isForOrdresNonRecouvert() {
        return getForOrdresNonRecouvert().booleanValue();
    }

    public boolean isForOrdresNonVerse() {
        return getForOrdresNonVerse().booleanValue();
    }

    public void setForIdAdressePaiement(String forIdAdressePaiement) {
        this.forIdAdressePaiement = forIdAdressePaiement;
    }

    /**
     * modifie le test sur les ordres non recouverts
     * 
     * @param forOrdresNonRecouvert
     */
    public void setForOrdresNonRecouvert(Boolean forOrdresNonRecouvert) {
        this.forOrdresNonRecouvert = forOrdresNonRecouvert;
    }

    public void setForOrdresNonVerse(Boolean enAttente) {
        forOrdresNonVerse = enAttente;
    }

    /**
     * @param forTousOrdresEnAttente
     *            the forTousOrdresEnAttente to set
     */
    public void setForTousOrdresEnAttente(Boolean forTousOrdresEnAttente) {
        this.forTousOrdresEnAttente = forTousOrdresEnAttente;
    }

}
