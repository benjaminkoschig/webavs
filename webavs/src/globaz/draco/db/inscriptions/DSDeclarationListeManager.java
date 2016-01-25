package globaz.draco.db.inscriptions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class DSDeclarationListeManager extends BManager {

    private static final long serialVersionUID = -6049288531707709595L;
    private String forIdJournal = new String();

    public DSDeclarationListeManager() {
        super();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // traitement du positionnement
        if (getForIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KCID=" + _dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new DSInscriptionsIndividuellesListeViewBean();
    }

    /**
     * @return
     */
    public String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * @param string
     */
    public void setForIdJournal(String string) {
        forIdJournal = string;
    }

}
