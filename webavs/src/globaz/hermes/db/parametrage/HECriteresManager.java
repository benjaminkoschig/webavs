package globaz.hermes.db.parametrage;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCodeManager;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (30.10.2002 14:47:09)
 * 
 * @author: Administrator
 */
public class HECriteresManager extends FWParametersSystemCodeManager implements
        globaz.framework.bean.FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String FOR_ID_GROUPE = "HECRITER";
    private static final String FOR_ID_TYPE_CODE = "11100003";

    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        super._beforeFind(transaction);
        setForIdGroupe(HECriteresManager.FOR_ID_GROUPE);
        setForIdTypeCode(HECriteresManager.FOR_ID_TYPE_CODE);
    }

    /**
     * Renvoie la composante de s�lection de la requ�te SQL (sans le mot-cl� WHERE)
     * 
     * @return la composante WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // les composants de la requ�te initialis�s avec les options par d�faut
        String sqlWhere = "";
        sqlWhere = "PLAIDE=" + this._dbWriteString(statement.getTransaction(), getSession().getIdLangue());
        if (getFromLibelle().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "PCOSLI>=" + this._dbWriteString(statement.getTransaction(), getFromLibelle());
        }
        if (getForIdTypeCode().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "PCOITC=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeCode());
        }
        if (getForIdSelection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "PCOISE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdSelection());
        }
        if (getForIdGroupe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "PPTYGR=" + this._dbWriteString(statement.getTransaction(), getForIdGroupe());
        }
        if (getForIdLangue().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "PLAIDE=" + this._dbWriteString(statement.getTransaction(), getForIdLangue());
        }
        if (getForCodeUtilisateur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "PCOUID=" + this._dbWriteString(statement.getTransaction(), getForCodeUtilisateur());
        }
        // On ne veut afficher que les codes actifs (qui ont le flag � false !)
        if (isForActif()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "PCODFI=" + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false));
        }
        return sqlWhere;
    }

    /**
     * Cr�e une nouvelle entit�
     * 
     * @return la nouvelle entit�
     * @exception java.lang.Exception
     *                si la cr�ation a �chou�e
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new HECriteres();
    }
}
