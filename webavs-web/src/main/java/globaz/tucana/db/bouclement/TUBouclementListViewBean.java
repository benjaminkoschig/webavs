package globaz.tucana.db.bouclement;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.db.bouclement.access.ITUBouclementDefTable;
import globaz.tucana.db.bouclement.access.ITUNoPassageDefTable;
import globaz.tucana.db.bouclement.access.TUBouclementManager;

/**
 * @author fgo
 * @version 1.0 Permet d'afficher une liste des bouclements avec ces 4 no de passage Table : TUBPBOU
 */
public class TUBouclementListViewBean extends TUBouclementManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forNoACM = new String();
    private String forNoAF = new String();

    private String forNoCA = new String();
    private String forNoCG = new String();

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection()).append(ITUBouclementDefTable.TABLE_NAME).append(" AS bou");
        // jointure sur passage CA
        sqlFrom.append(" LEFT OUTER JOIN ");
        sqlFrom.append(_getCollection()).append(ITUNoPassageDefTable.TABLE_NAME).append(" AS pca");
        sqlFrom.append(" ON bou.").append(ITUBouclementDefTable.ID_BOUCLEMENT).append("=pca.")
                .append(ITUNoPassageDefTable.ID_BOUCLEMENT).append(" AND pca.")
                .append(ITUNoPassageDefTable.CS_APPLICATION).append("=").append(ITUCSConstantes.CS_APPLICATION_CA);
        // jointure sur passage CG
        sqlFrom.append(" LEFT OUTER JOIN ");
        sqlFrom.append(_getCollection()).append(ITUNoPassageDefTable.TABLE_NAME).append(" AS pcg");
        sqlFrom.append(" ON bou.").append(ITUBouclementDefTable.ID_BOUCLEMENT).append("=pcg.")
                .append(ITUNoPassageDefTable.ID_BOUCLEMENT).append(" AND pcg.")
                .append(ITUNoPassageDefTable.CS_APPLICATION).append("=").append(ITUCSConstantes.CS_APPLICATION_CG);
        // jointure sur passage ACM
        sqlFrom.append(" LEFT OUTER JOIN ");
        sqlFrom.append(_getCollection()).append(ITUNoPassageDefTable.TABLE_NAME).append(" AS pacm");
        sqlFrom.append(" ON bou.").append(ITUBouclementDefTable.ID_BOUCLEMENT).append("=pacm.")
                .append(ITUNoPassageDefTable.ID_BOUCLEMENT).append(" AND pacm.")
                .append(ITUNoPassageDefTable.CS_APPLICATION).append("=").append(ITUCSConstantes.CS_APPLICATION_ACM);
        // jointure sur passage AF
        sqlFrom.append(" LEFT OUTER JOIN ");
        sqlFrom.append(_getCollection()).append(ITUNoPassageDefTable.TABLE_NAME).append(" AS paf");
        sqlFrom.append(" ON bou.").append(ITUBouclementDefTable.ID_BOUCLEMENT).append("=paf.")
                .append(ITUNoPassageDefTable.ID_BOUCLEMENT).append(" AND paf.")
                .append(ITUNoPassageDefTable.CS_APPLICATION).append("=").append(ITUCSConstantes.CS_APPLICATION_AF);
        return sqlFrom.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // StringBuffer sqlWhere = new StringBuffer(super._getWhere(statement));
        StringBuffer sqlWhere = new StringBuffer();

        // traitement du positionnement
        if (getForAnneeComptable().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("bou.").append(ITUBouclementDefTable.ANNEE_COMPTABLE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForAnneeComptable()));
        }
        // traitement du positionnement
        if (getForDateCreation().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("bou.").append(ITUBouclementDefTable.DATE_CREATION).append("=")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getForDateCreation()));
        }
        // traitement du positionnement
        if (getForDateEtat().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("bou.").append(ITUBouclementDefTable.DATE_ETAT).append("=")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getForDateEtat()));
        }
        // traitement du positionnement
        if (getForIdBouclement().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("bou.").append(ITUBouclementDefTable.ID_BOUCLEMENT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdBouclement()));
        }
        // traitement du positionnement
        if (getForIdImportation().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("bou.").append(ITUBouclementDefTable.ID_IMPORTATION).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdImportation()));
        }
        // traitement du positionnement
        if (getForMoisComptable().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("bou.").append(ITUBouclementDefTable.MOIS_COMPTABLE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForMoisComptable()));
        }
        // traitement du positionnement
        if (getForSoldeBouclement().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("bou.").append(ITUBouclementDefTable.SOLDE_BOUCLEMENT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForSoldeBouclement()));
        }
        // traitement du positionnement
        if (getForCsAgence().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("bou.").append(ITUBouclementDefTable.CS_AGENCE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsAgence()));
        }
        // traitement du positionnement
        if (getForCsEtat().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("bou.").append(ITUBouclementDefTable.CS_ETAT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsEtat()));
        }

        // traitement sur le numéro CA
        if (getForNoCA().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("(pca.").append(ITUNoPassageDefTable.NO_PASSAGE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForNoCA())).append(" OR pca.")
                    .append(ITUNoPassageDefTable.NO_PASSAGE).append(" is null)");
        }
        // traitement sur le numéro CG
        if (getForNoCG().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("(pcg.").append(ITUNoPassageDefTable.NO_PASSAGE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForNoCG())).append(" OR pcg.")
                    .append(ITUNoPassageDefTable.NO_PASSAGE).append(" is null)");
        }
        // traitement sur le numéro ACM
        if (getForNoACM().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("(pacm.").append(ITUNoPassageDefTable.NO_PASSAGE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForNoACM())).append(" OR pacm.")
                    .append(ITUNoPassageDefTable.NO_PASSAGE).append(" is null)");
        }
        // traitement sur le numéro AF
        if (getForNoAF().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("(paf.").append(ITUNoPassageDefTable.NO_PASSAGE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForNoAF())).append(" OR paf.")
                    .append(ITUNoPassageDefTable.NO_PASSAGE).append(" is null)");
        }

        return sqlWhere.toString();
    }

    /**
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    public BEntity _newEntity() throws Exception {
        return new TUBouclementViewBean();
    }

    /**
     * @return le numéro ACM
     */
    public String getForNoACM() {
        return forNoACM;
    }

    /**
     * @return le numéro AF
     */
    public String getForNoAF() {
        return forNoAF;
    }

    /**
     * @return le numéro CA
     */
    public String getForNoCA() {
        return forNoCA;
    }

    /**
     * @return le numéro CG
     */
    public String getForNoCG() {
        return forNoCG;
    }

    /**
     * @param string
     *            le numéro ACM indiqué dans la clause Where
     */
    public void setForNoACM(String string) {
        forNoACM = string;
    }

    /**
     * @param string
     *            le numéro AF indiqué dans la clause Where
     */
    public void setForNoAF(String string) {
        forNoAF = string;
    }

    /**
     * @param string
     *            le numéro CA indiqué dans la clause Where
     */
    public void setForNoCA(String string) {
        forNoCA = string;
    }

    /**
     * @param string
     *            le numéro CG indiqué dans la clause Where
     */
    public void setForNoCG(String string) {
        forNoCG = string;
    }
}
