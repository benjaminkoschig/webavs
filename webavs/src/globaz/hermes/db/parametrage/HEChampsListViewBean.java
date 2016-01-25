package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCodeManager;

/**
 * Insérez la description du type ici. Date de création : (27.11.2002 10:06:09)
 * 
 * @author: ado
 */
public class HEChampsListViewBean extends FWParametersSystemCodeManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Lance l'application.
     * 
     * @param args
     *            un tableau d'arguments de ligne de commande
     */
    public static void main(java.lang.String[] args) {
        try {
            BSession session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            HEChampsListViewBean champsList = new HEChampsListViewBean();
            champsList.setSession(session);
            champsList.find();
            System.out.println(champsList.size());
            HEChampsViewBean champs = (HEChampsViewBean) champsList.getEntity(0);
            System.out.println(champs.getIdCode() + "-" + champs.getCodeUti() + "-" + champs.getLibelle());
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        }
        System.exit(0);
    }

    private String fromCodeUti = "";

    public HEChampsListViewBean() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.01.2002 11:07:08)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws java.lang.Exception {
        setForIdGroupe(HEChampsViewBean.GROUPE);
        setForIdTypeCode(HEChampsViewBean.TYPE_CODE);
    }

    @Override
    protected java.lang.String _getOrder(BStatement statement) {
        return "PCOUID, PCOSLI";
    }

    /**
     * Renvoie la composante de sélection de la requête SQL (sans le mot-clé WHERE)
     * 
     * @return la composante WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // les composants de la requête initialisés avec les options par défaut
        String sqlWhere = "";
        sqlWhere = "PLAIDE=" + _dbWriteString(statement.getTransaction(), getSession().getIdLangue());
        if (getFromLibelle().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "PCOSLI>=" + _dbWriteString(statement.getTransaction(), getFromLibelle());
        }
        if (getForIdTypeCode().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "PCOITC=" + _dbWriteNumeric(statement.getTransaction(), getForIdTypeCode());
        }
        if (getForIdSelection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "PCOISE=" + _dbWriteNumeric(statement.getTransaction(), getForIdSelection());
        }
        if (getForIdGroupe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "PPTYGR=" + _dbWriteString(statement.getTransaction(), getForIdGroupe());
        }
        if (getForIdLangue().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "PLAIDE=" + _dbWriteString(statement.getTransaction(), getForIdLangue());
        }
        if (getForCodeUtilisateur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "PCOUID=" + _dbWriteString(statement.getTransaction(), getForCodeUtilisateur());
        }
        //
        if (getFromCodeUti().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "PCOUID>=" + _dbWriteString(statement.getTransaction(), getFromCodeUti());
        }
        // On ne veut afficher que les codes actifs (qui ont le flag à false !)
        if (isForActif()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "PCODFI=" + _dbWriteBoolean(statement.getTransaction(), new Boolean(false));
        }
        return sqlWhere;
    }

    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new HEChampsViewBean();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 14:02:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromCodeUti() {
        return fromCodeUti;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 14:02:07)
     * 
     * @param newFromCodeUti
     *            java.lang.String
     */
    public void setFromCodeUti(java.lang.String newFromCodeUti) {
        fromCodeUti = newFromCodeUti;
    }
}
