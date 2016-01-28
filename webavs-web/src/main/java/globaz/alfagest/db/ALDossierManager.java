package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Créé le 26 janv. 06
 * 
 * @author dch
 * 
 *         Manager d'en-têtes de prestations (ALEntetePrestation)
 */
public class ALDossierManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDossier = null;

    /**
     * Crée une nouvelle entité.
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception si la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new ALDossier();
    }

    /**
     * Renvoie la composante de sélection de la requête SQL (sans le mot-clé WHERE).
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