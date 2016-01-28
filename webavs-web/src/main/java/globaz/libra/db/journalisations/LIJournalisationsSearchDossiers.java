package globaz.libra.db.journalisations;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.journalisation.constantes.JOConstantes;
import globaz.journalisation.db.common.access.IJOCommonComplementJournalDefTable;
import globaz.journalisation.db.common.access.IJOCommonJournalisationDefTable;
import globaz.libra.db.dossiers.LIDossiers;

public class LIJournalisationsSearchDossiers extends LIEcheancesJointDossiersManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forLibelle = new String();

    /**
     * Crée une nouvelle instance de la classe LIJournalisationsSearch.
     */
    public LIJournalisationsSearchDossiers() {
        super();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }

        sqlWhere.append(IJOCommonJournalisationDefTable.CSTYPEJOURNAL);
        sqlWhere.append(" = ");
        sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), JOConstantes.CS_JO_JOURNALISATION));

        if (!JadeStringUtil.isIntegerEmpty(getForIdDossier())) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LIDossiers.FIELDNAME_ID_DOSSIER);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), getForIdDossier()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdExtern())) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LIDossiers.FIELDNAME_ID_EXTERNE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), getForIdExtern()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateDebut())) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonJournalisationDefTable.DATE);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut()));

        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateFin())) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonJournalisationDefTable.DATE);
            sqlWhere.append(" <= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin()));

        }

        if (!JadeStringUtil.isEmpty(forLibelle)) {
            sqlWhere.append(" AND ");
            sqlWhere.append(IJOCommonJournalisationDefTable.LIBELLE);
            sqlWhere.append("=");
            sqlWhere.append("'" + forLibelle + "' ");

        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdUtilisateur())) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            if (getForIdUtilisateur().equals("idFX")) {
                sqlWhere.append(IJOCommonJournalisationDefTable.IDUTILISATEUR);
                sqlWhere.append(" = ");
                sqlWhere.append(this._dbWriteString(statement.getTransaction(), getSession().getUserId()));
            } else {
                sqlWhere.append(IJOCommonJournalisationDefTable.IDUTILISATEUR);
                sqlWhere.append(" = ");
                sqlWhere.append(this._dbWriteString(statement.getTransaction(), getForIdUtilisateur()));
            }

        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdGroupe())) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LIDossiers.FIELDNAME_ID_GROUPE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), getForIdGroupe()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdDomaine())) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(LIDossiers.FIELDNAME_ID_DOMAINE);

            if (getForIdDomaine().startsWith("IN")) {
                sqlWhere.append(" " + getForIdDomaine());
            } else {
                sqlWhere.append(" = ");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), getForIdDomaine()));
            }

        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsType())) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonComplementJournalDefTable.VALEURCODESYSTEME);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), getForCsType()));
        } else {
            if (!JadeStringUtil.isBlankOrZero(getDocuments()) && getDocuments().equals("on")) {
                if (sqlWhere.length() > 0) {
                    sqlWhere.append(" AND ");
                }
                sqlWhere.append(IJOCommonComplementJournalDefTable.VALEURCODESYSTEME);
                sqlWhere.append(" IN (");
                sqlWhere.append(" " + JOConstantes.CS_JO_AVS_FMT_ENVOI_MULTIPLE + ", "
                        + JOConstantes.CS_JO_AVS_FMT_ENVOI_SIMPLE);
                sqlWhere.append(") ");
            }
        }

        return sqlWhere.toString();

    }

    /**
     * @return the forLibelle
     */
    public String getForLibelle() {
        return forLibelle;
    }

    /**
     * @param forLibelle
     *            the forLibelle to set
     */
    public void setForLibelle(String forLibelle) {
        this.forLibelle = forLibelle;
    }
}
