package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Cr�� le 26 janv. 06
 * 
 * @author dch
 * 
 *         Manager d'en-t�tes de prestations (ALEntetePrestation)
 */
public class ALDossierManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDossier = null;

    /**
     * Cr�e une nouvelle entit�.
     * 
     * @return la nouvelle entit�
     * @exception java.lang.Exception si la cr�ation a �chou�e
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new ALDossier();
    }

    /**
     * Renvoie la composante de s�lection de la requ�te SQL (sans le mot-cl� WHERE).
     * 
     * @return la composante WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = null;

        if (idDossier != null && !idDossier.equals("")) {
            if (where == null) {
                where = "EID = " + _dbWriteNumeric(statement.getTransaction(), idDossier);
            } else {
                where += " AND EID = " + _dbWriteNumeric(statement.getTransaction(), idDossier);
            }
        }

        return where;
    }

    /**
     * @return
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @param string
     */
    public void setIdDossier(String string) {
        idDossier = string;
    }

}