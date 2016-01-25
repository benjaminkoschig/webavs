/*
 * Cr�� le 13 juin 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author jer
 * 
 *         Classe qui permet de r�cup�rer une liste de d�compte ADI sur la base d'un id de prestation
 */
public class ALDecompteADIManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idPrestationADI = null;

    /**
     * Cr�e une nouvelle entit�.
     * 
     * @return la nouvelle entit�
     * @exception java.lang.Exception si la cr�ation a �chou�e
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new ALDecompteADI();
    }

    /**
     * Renvoie la composante de s�lection de la requ�te SQL (sans le mot-cl� WHERE).
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
