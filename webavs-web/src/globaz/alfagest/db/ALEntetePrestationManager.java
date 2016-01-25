package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Créé le 18 janv. 06
 * 
 * @author dch
 * 
 *         Manager d'en-têtes de prestations (ALEntetePrestation)
 */
public class ALEntetePrestationManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idEntetePrestation = null;
    private String idRecap = null;
    private String idCategorieTarif = null;
    private String etatPrestation = null;
    private String idPassageFacturation = null;
    private String typeBonification = null;

    /**
     * Crée une nouvelle entité.
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception si la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new ALEntetePrestation();
    }

    /**
     * Renvoie la composante de sélection de la requête SQL (sans le mot-clé WHERE).
     * 
     * @return la composante WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = null;

        if (idEntetePrestation != null && !idEntetePrestation.equals("")) {
            if (where == null) {
                where = "MID = " + _dbWriteNumeric(statement.getTransaction(), idEntetePrestation);
            } else {
                where += " AND MID = " + _dbWriteNumeric(statement.getTransaction(), idEntetePrestation);
            }
        }

        if (idRecap != null && !idRecap.equals("")) {
            if (where == null) {
                where = "MIDREC = " + _dbWriteNumeric(statement.getTransaction(), idRecap);
            } else {
                where += " AND MIDREC = " + _dbWriteNumeric(statement.getTransaction(), idRecap);
            }
        }

        if (idCategorieTarif != null && !idCategorieTarif.equals("")) {
            if (where == null) {
                where = "MIDTR = " + _dbWriteString(statement.getTransaction(), idCategorieTarif);
            } else {
                where += " AND MIDTR = " + _dbWriteString(statement.getTransaction(), idCategorieTarif);
            }
        }

        if (etatPrestation != null && !etatPrestation.equals("")) {
            if (where == null) {
                where = "METAT = " + _dbWriteString(statement.getTransaction(), etatPrestation);
            } else {
                where += " AND METAT = " + _dbWriteString(statement.getTransaction(), etatPrestation);
            }
        }

        if (idPassageFacturation != null && !idPassageFacturation.equals("")) {
            if (where == null) {
                where = "MPSGF = " + _dbWriteNumeric(statement.getTransaction(), idPassageFacturation);
            } else {
                where += " AND MPSGF = " + _dbWriteNumeric(statement.getTransaction(), idPassageFacturation);
            }
        }

        if (typeBonification != null && !typeBonification.equals("")) {
            if (where == null) {
                where = "MBONI = " + _dbWriteString(statement.getTransaction(), typeBonification);
            } else {
                where += " AND MBONI = " + _dbWriteString(statement.getTransaction(), typeBonification);
            }
        }

        return where;
    }

    /**
     * @return
     */
    public String getEtatPrestation() {
        return etatPrestation;
    }

    /**
     * @return
     */
    public String getIdCategorieTarif() {
        return idCategorieTarif;
    }

    /**
     * @return
     */
    public String getIdEntetePrestation() {
        return idEntetePrestation;
    }

    /**
     * @return
     */
    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    /**
     * @return
     */
    public String getIdRecap() {
        return idRecap;
    }

    /**
     * @return
     */
    public String getTypeBonification() {
        return typeBonification;
    }

    /**
     * @param string
     */
    public void setEtatPrestation(String string) {
        etatPrestation = string;
    }

    /**
     * @param string
     */
    public void setIdCategorieTarif(String string) {
        idCategorieTarif = string;
    }

    /**
     * @param string
     */
    public void setIdEntetePrestation(String string) {
        idEntetePrestation = string;
    }

    /**
     * @param string
     */
    public void setIdPassageFacturation(String string) {
        idPassageFacturation = string;
    }

    /**
     * @param string
     */
    public void setIdRecap(String string) {
        idRecap = string;
    }

    /**
     * @param string
     */
    public void setTypeBonification(String string) {
        typeBonification = string;
    }
}