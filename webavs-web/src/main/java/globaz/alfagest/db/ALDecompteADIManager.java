/*
 * Créé le 13 juin 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author jer
 * 
 *         Classe qui permet de récupérer une liste de décompte ADI sur la base d'un id de prestation
 */
public class ALDecompteADIManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idPrestationADI = null;

    /**
     * Crée une nouvelle entité.
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception si la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new ALDecompteADI();
    }

    /**
     * Renvoie la composante de sélection de la requête SQL (sans le mot-clé WHERE).
     * 
     * @return la composante WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = null;

        if (idPrestationADI != null && !idPrestationADI.equals("")) {
            if (where == null) {
                where = "A2IDE = " + _dbWriteNumeric(statement.getTransaction(), idPrestationADI);
            } else {
                where += " AND A2IDE = " + _dbWriteNumeric(statement.getTransaction(), idPrestationADI);
            }
        }

        return where;
    }

    /**
     * @return
     */
    public String getIdPrestationADI() {
        return idPrestationADI;
    }

    /**
     * @param string
     */
    public void setIdPrestationADI(String string) {
        idPrestationADI = string;
    }

}
