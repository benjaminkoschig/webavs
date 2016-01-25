package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Créé le 19 janv. 06
 * 
 * @author dch
 * 
 *         Manager d'en-têtes de prestations (ALEntetePrestation)
 */
public class ALParametreManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String identificationApplication = "";
    private String provenanceActeur = "";
    private String identificationActeur = "";
    private String typeParametre = "";
    private String dateValeur = "";

    /**
     * Crée une nouvelle entité.
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception si la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new ALParametre();
    }

    /**
     * Renvoie la composante de sélection de la requête SQL (sans le mot-clé WHERE).
     * 
     * @return la composante WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = null;

        if (identificationApplication != null && !identificationApplication.equals("")) {
            if (where == null) {
                where = "OAPPLI = " + _dbWriteString(statement.getTransaction(), identificationApplication);
            } else {
                where += " AND OAPPLI = " + _dbWriteString(statement.getTransaction(), identificationApplication);
            }
        }

        if (provenanceActeur != null && !provenanceActeur.equals("")) {
            if (where == null) {
                where = "OPROV = " + _dbWriteString(statement.getTransaction(), provenanceActeur);
            } else {
                where += " AND OPROV = " + _dbWriteString(statement.getTransaction(), provenanceActeur);
            }
        }

        if (identificationActeur != null && !identificationActeur.equals("")) {
            if (where == null) {
                where = "OIDACT = " + _dbWriteNumeric(statement.getTransaction(), identificationActeur);
            } else {
                where += " AND OIDACT = " + _dbWriteNumeric(statement.getTransaction(), identificationActeur);
            }
        }

        if (typeParametre != null && !typeParametre.equals("")) {
            if (where == null) {
                where = "OTPARM = " + _dbWriteString(statement.getTransaction(), typeParametre);
            } else {
                where += " AND OTPARM = " + _dbWriteString(statement.getTransaction(), typeParametre);
            }
        }

        if (dateValeur != null && !dateValeur.equals("")) {
            if (where == null) {
                where = "ODVAL = " + _dbWriteNumeric(statement.getTransaction(), dateValeur);
            } else {
                where += " AND ODVAL = " + _dbWriteNumeric(statement.getTransaction(), dateValeur);
            }
        }

        return where;
    }

    /**
     * @return
     */
    public String getDateValeur() {
        return dateValeur;
    }

    /**
     * @return
     */
    public String getIdentificationActeur() {
        return identificationActeur;
    }

    /**
     * @return
     */
    public String getIdentificationApplication() {
        return identificationApplication;
    }

    /**
     * @return
     */
    public String getProvenanceActeur() {
        return provenanceActeur;
    }

    /**
     * @return
     */
    public String getTypeParametre() {
        return typeParametre;
    }

    /**
     * @param string
     */
    public void setDateValeur(String string) {
        dateValeur = string;
    }

    /**
     * @param string
     */
    public void setIdentificationActeur(String string) {
        identificationActeur = string;
    }

    /**
     * @param string
     */
    public void setIdentificationApplication(String string) {
        identificationApplication = string;
    }

    /**
     * @param string
     */
    public void setProvenanceActeur(String string) {
        provenanceActeur = string;
    }

    /**
     * @param string
     */
    public void setTypeParametre(String string) {
        typeParametre = string;
    }
}