package globaz.hermes.db.parametrage;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.hermes.utils.Constantes;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (11.11.2002 18:04:32)
 * 
 * @author: ado
 */
public class HEParametrageannonce extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (RENCED) */
    private String codeEnregistrementDebut = new String();
    /** (RENCEF) */
    private String codeEnregistrementFin = new String();
    // code systeme
    private FWParametersSystemCode csCodeApplication = null;
    /** (RETLIB) */
    private String idCSCodeApplication = new String();
    /** Fichier HEPAREP */
    /** (REIPAE) */
    private String idParametrageAnnonce = new String();
    /** (RETUTI) */
    private String idUtilisateur = new String();

    /**
     * Commentaire relatif au constructeur HEParametrageAnnonce
     */
    public HEParametrageannonce() {
        super();
    }

    /**
     * Constructor HEParametrageannonce.
     * 
     * @param bSession
     */
    public HEParametrageannonce(BSession bSession) {
        this();
        setSession(bSession);
    }

    /**
     * Effectue des traitements avant un ajout dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant l'ajout de l'entit� dans la BD
     * <p>
     * L'ex�cution de l'ajout n'est pas effectu�e si le buffer d'erreurs n'est pas vide apr�s l'ex�cution de
     * <code>_beforeAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        setIdParametrageAnnonce(_incCounter(transaction, "0"));
    }

    /**
     * Effectue des traitements avant une mise � jour dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant la mise � jour de l'entit� dans la BD
     * <p>
     * L'ex�cution de la mise � jour n'est pas effectu�e si le buffer d'erreurs n'est pas vide apr�s l'ex�cution de
     * <code>_beforeUpdate()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws java.lang.Exception {
        try {
            if (getCodeEnregistrementFin().trim().equals("")) {
                // Le champ fin est pas renseign�, on le met � 0
                // pour le update et le parseInt
                setCodeEnregistrementFin("0");
            }
            int debut = Integer.parseInt(getCodeEnregistrementDebut());
            int fin = Integer.parseInt(getCodeEnregistrementFin());
            if (fin != 0 && fin < debut) { // fin peut pas �tre < debut sauf
                // s'il est � z�ro
                // fin avant d�but...
                transaction.addErrors(Constantes.FR_E_00005);
                throw new Exception(Constantes.FR_E_00005);
            }
        } catch (NumberFormatException e) {
            // Soit champ d�but � blanc
            // soit String et pas int saisi
            transaction.addErrors(Constantes.FR_E_00004 + e.getMessage());
            throw new Exception(Constantes.FR_E_00004 + e.getMessage());
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "HEPAREP";
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propri�t�s �choue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idParametrageAnnonce = statement.dbReadNumeric("REIPAE", 0);
        codeEnregistrementDebut = statement.dbReadNumeric("RENCED", 0);
        codeEnregistrementFin = statement.dbReadNumeric("RENCEF", 0);
        idCSCodeApplication = statement.dbReadNumeric("RETLIB", 0);
        idUtilisateur = statement.dbReadNumeric("RETUTI", 0);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**
	
	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("REIPAE", _dbWriteNumeric(statement.getTransaction(), getIdParametrageAnnonce(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("REIPAE",
                _dbWriteNumeric(statement.getTransaction(), getIdParametrageAnnonce(), "idParametrageAnnonce"));
        statement.writeField("RENCED",
                _dbWriteNumeric(statement.getTransaction(), getCodeEnregistrementDebut(), "codeEnregistrementDebut"));
        statement.writeField("RENCEF",
                _dbWriteNumeric(statement.getTransaction(), getCodeEnregistrementFin(), "codeEnregistrementFin"));
        statement.writeField("RETLIB",
                _dbWriteNumeric(statement.getTransaction(), getIdCSCodeApplication(), "code application"));
        statement
                .writeField("RETUTI", _dbWriteNumeric(statement.getTransaction(), getIdUtilisateur(), "idUtilisateur"));
    }

    /**
     * Format joliment les enregistrements<br>
     * 0 devient ""<br>
     * 1 devient 01
     * 
     * @param s
     *            la chaine � formatter
     * @return String la chaine formatt�e
     */
    public String formatEnregistrement(String s) {
        s = s.trim();
        if (s.equals("0")) {
            return "";
        } else {
            // la string s contient forc�ment un integer, mais on est
            // jamais trop prudent
            try {
                int intS = Integer.parseInt(s);
                if (intS <= 9) {
                    return "0" + intS;
                } else {
                    return s;
                }
            } catch (Exception e) { // ben voil�, c'�tait pas une valeur
                // num�rique !
                // on renvoit quand m�me quelque chose...
                e.printStackTrace();
                return s;
            }
        }
    }

    public String getCodeEnregistrementDebut() {
        return formatEnregistrement(codeEnregistrementDebut);
    }

    public String getCodeEnregistrementFin() {
        return formatEnregistrement(codeEnregistrementFin);
    }

    public FWParametersSystemCode getCsCodeApplication() throws Exception {
        if (csCodeApplication == null) {
            // liste pas encore chargee, on la charge
            csCodeApplication = new FWParametersSystemCode();
            csCodeApplication.setSession(getSession());
            csCodeApplication.getCode(getIdCSCodeApplication());
        }
        return csCodeApplication;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.11.2002 08:51:59)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCSCodeApplication() {
        return idCSCodeApplication;
    }

    /**
     * Ins�rez la description de la m�thode ici.
     * 
     * @return String
     */
    public String getIdParametrageAnnonce() {
        return idParametrageAnnonce;
    }

    public String getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setCodeEnregistrementDebut(String newCodeEnregistrementDebut) {
        codeEnregistrementDebut = newCodeEnregistrementDebut;
    }

    public void setCodeEnregistrementFin(String newCodeEnregistrementFin) {
        codeEnregistrementFin = newCodeEnregistrementFin;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.11.2002 08:54:30)
     * 
     * @param newCsCodeApplication
     *            globaz.globall.parameters.FWParametersSystemCode
     */
    void setCsCodeApplication(globaz.globall.parameters.FWParametersSystemCode newCsCodeApplication) {
        csCodeApplication = newCsCodeApplication;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.11.2002 08:51:59)
     * 
     * @param newIdCSCodeApplication
     *            java.lang.String
     */
    public void setIdCSCodeApplication(java.lang.String newIdCSCodeApplication) {
        idCSCodeApplication = newIdCSCodeApplication;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setIdParametrageAnnonce(String newIdParametrageAnnonce) {
        idParametrageAnnonce = newIdParametrageAnnonce;
    }

    public void setIdUtilisateur(String newIdUtilisateur) {
        idUtilisateur = newIdUtilisateur;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toMyString() {
        return getIdParametrageAnnonce();
    }

}
