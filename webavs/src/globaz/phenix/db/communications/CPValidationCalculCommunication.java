package globaz.phenix.db.communications;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.jade.log.JadeLogger;
import globaz.naos.translation.CodeSystem;
import java.util.Vector;
import javax.servlet.http.HttpSession;

public class CPValidationCalculCommunication extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** (ILIDEP) */
    public final static int ALT_KEY_IDDECISION = 1;

    public final static String CS_CREDITEUR = "618001";

    public final static String CS_DEBIT_INF_25 = "618002";

    public final static String CS_DEBIT_SUP_25 = "618003";

    public final static String CS_SANS_CHANGEMENT = "618004";

    /**
     * Retourne une liste des groupes d'extraction
     * 
     * @param httpSession
     * @return vecteur des paramètres de plausibilités
     */
    public static Vector<String[]> getListGroupeExtraction(HttpSession httpSession) {
        Vector<String[]> vList = new Vector<String[]>();
        String[] element = null;
        try {
            BSession session = (BSession) CodeSystem.getSession(httpSession);
            CPParametrePlausibiliteManager manager = new CPParametrePlausibiliteManager();
            manager.setSession(session);
            manager.setForActif(Boolean.TRUE);
            manager.setForEnAvertissement(Boolean.TRUE);
            manager.find();
            vList = new Vector<String[]>(manager.size());
            element = new String[2];
            element[0] = "";
            element[1] = "";
            vList.add(element);
            for (int i = 0; i < manager.size(); i++) {
                CPParametrePlausibilite plausibilite = (CPParametrePlausibilite) manager.getEntity(i);
                if (plausibilite != null) {
                    if (session.getIdLangueISO().equalsIgnoreCase("DE")) {
                        element = new String[2];
                        element[0] = plausibilite.getId();
                        element[1] = plausibilite.getId() + " - " + plausibilite.getDescription_de();
                        vList.add(element);
                    } else if (session.getIdLangueISO().equalsIgnoreCase("IT")) {
                        element = new String[2];
                        element[0] = plausibilite.getId();
                        element[1] = plausibilite.getId() + " - " + plausibilite.getDescription_it();
                        vList.add(element);
                    } else {
                        element = new String[2];
                        element[0] = plausibilite.getId();
                        element[1] = plausibilite.getId() + " - " + plausibilite.getDescription_fr();
                        vList.add(element);
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(null, e);
        }
        return vList;
    }

    public static Vector<String[]> getListPlausibilites(HttpSession httpSession, String inTypeMessage, String canton) {
        Vector<String[]> vList = new Vector<String[]>();
        String[] element = null;
        try {
            BSession session = (BSession) CodeSystem.getSession(httpSession);
            CPRegleParametrePlausibiliteManager manager = new CPRegleParametrePlausibiliteManager();
            manager.setSession(session);
            manager.setForCanton(canton);
            manager.setForActifRegle(Boolean.TRUE);
            manager.setForActifParametre(Boolean.TRUE);
            manager.setInTypeMessage(inTypeMessage);
            manager.find();
            vList = new Vector<String[]>(manager.size());
            element = new String[2];
            element[0] = "";
            element[1] = "";
            vList.add(element);
            for (int i = 0; i < manager.size(); i++) {
                CPRegleParametrePlausibilite plausibilite = (CPRegleParametrePlausibilite) manager.getEntity(i);
                if (plausibilite != null) {
                    if (session.getIdLangueISO().equalsIgnoreCase("DE")) {
                        element = new String[2];
                        element[0] = plausibilite.getId();
                        element[1] = plausibilite.getId() + " - " + plausibilite.getDescriptionParametre_de();
                        vList.add(element);
                    } else if (session.getIdLangueISO().equalsIgnoreCase("IT")) {
                        element = new String[2];
                        element[0] = plausibilite.getId();
                        element[1] = plausibilite.getId() + " - " + plausibilite.getDescriptionParametre_it();
                        vList.add(element);
                    } else {
                        element = new String[2];
                        element[0] = plausibilite.getId();
                        element[1] = plausibilite.getId() + " - " + plausibilite.getDescriptionParametre_fr();
                        vList.add(element);
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(null, e);
        }
        return vList;
    }

    private String dateCalcul = "";

    /** (ILDCAL) */
    private String groupeExtraction = "";
    /** (ILGPEV) */
    private String groupeTaxation = "";
    /** (ILITCO) */
    private String idCommunicationRetour = "";
    /** (IKIRET) */
    private String idDecision = "";
    /** (ILGPTA) */
    private String idDecisionProvisoire = "";

    private String idValidationCommunication = "";

    /** (IAIDEC) */
    private Boolean validation = new Boolean(true);

    // code systeme
    /**
     * Commentaire relatif au constructeur CPCommentaireRemarqueType
     */
    public CPValidationCalculCommunication() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdValidationCommunication(this._incCounter(transaction, idValidationCommunication));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPVCCOP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDecision = statement.dbReadNumeric("IAIDEC");
        idCommunicationRetour = statement.dbReadNumeric("IKIRET");
        idValidationCommunication = statement.dbReadNumeric("ILITCO");
        validation = statement.dbReadBoolean("ILBVAL");
        dateCalcul = statement.dbReadDateAMJ("ILDCAL");
        groupeExtraction = statement.dbReadNumeric("ILGPEV");
        groupeTaxation = statement.dbReadNumeric("ILGPTA");
        idDecisionProvisoire = statement.dbReadNumeric("ILIDEP");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant une clé alternée
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     * @param alternateKey
     *            int le numéro de la clé alternée à utiliser
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        // Traitement par défaut : pas de clé alternée
        // Recherche sur le numéro AVS
        if (alternateKey == CPValidationCalculCommunication.ALT_KEY_IDDECISION) {
            statement.writeKey(_getBaseTable() + "IAIDEC",
                    this._dbWriteNumeric(statement.getTransaction(), getIdDecision(), ""));
        }
    }

    /**
	 * 
	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("ILITCO",
                this._dbWriteNumeric(statement.getTransaction(), getIdValidationCommunication(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("ILITCO", this._dbWriteNumeric(statement.getTransaction(), getIdValidationCommunication(),
                "idValidationCommunication"));
        statement.writeField("IAIDEC", this._dbWriteNumeric(statement.getTransaction(), getIdDecision(), "idDecision"));
        statement.writeField("IKIRET",
                this._dbWriteNumeric(statement.getTransaction(), getIdCommunicationRetour(), "idCommunicationRetour"));
        statement.writeField("ILBVAL", this._dbWriteBoolean(statement.getTransaction(), isValidation(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "validation"));
        statement.writeField("ILDCAL", this._dbWriteDateAMJ(statement.getTransaction(), getDateCalcul(), "dateCalcul"));
        statement.writeField("ILGPEV",
                this._dbWriteNumeric(statement.getTransaction(), getGroupeExtraction(), "groupeExtraction"));
        statement.writeField("ILGPTA",
                this._dbWriteNumeric(statement.getTransaction(), getGroupeTaxation(), "groupeTaxation"));
        statement.writeField("ILIDEP",
                this._dbWriteNumeric(statement.getTransaction(), getIdDecisionProvisoire(), "idDecisionProvisoire"));
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getDateCalcul() {
        return dateCalcul;
    }

    public String getGroupeExtraction() {
        return groupeExtraction;
    }

    public String getGroupeTaxation() {
        return groupeTaxation;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdCommunicationRetour() {
        return idCommunicationRetour;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDecisionProvisoire() {
        return idDecisionProvisoire;
    }

    /**
     * Returns the idValidationCommunication.
     * 
     * @return String
     */
    public String getIdValidationCommunication() {
        return idValidationCommunication;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public Boolean isValidation() {
        return validation;
    }

    public void setDateCalcul(String newDateCalcul) {
        dateCalcul = newDateCalcul;
    }

    public void setGroupeExtraction(String groupeExtraction) {
        this.groupeExtraction = groupeExtraction;
    }

    public void setGroupeTaxation(String groupeTaxation) {
        this.groupeTaxation = groupeTaxation;
    }

    public void setIdCommunicationRetour(String newIdCommunication) {
        idCommunicationRetour = newIdCommunication;
    }

    public void setIdDecision(String newIdDecision) {
        idDecision = newIdDecision;
    }

    public void setIdDecisionProvisoire(String idDecisionProvisoire) {
        this.idDecisionProvisoire = idDecisionProvisoire;
    }

    /**
     * Sets the idValidationCommunication.
     * 
     * @param idValidationCommunication
     *            The idValidationCommunication to set
     */
    public void setIdValidationCommunication(String idValidationCommunication) {
        this.idValidationCommunication = idValidationCommunication;
    }

    public void setValidation(Boolean newValidation) {
        validation = newValidation;
    }
}
