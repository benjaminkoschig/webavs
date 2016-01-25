package globaz.osiris.db.interets;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CARubriqueManager;

/**
 * 
 * Genre d'intérêt. Utiliser pour connaître la contre partie d'un intérêt en fonction du plan.
 * 
 * @author: Administrator
 */
public class CAGenreInteret extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static int AK_PLAN_TYPE = 1;
    public final static String CS_TYPE_COTISATIONS_ARRIEREES = "228003";
    public final static String CS_TYPE_COTISATIONS_PERSONNELLES = "228004";
    public final static String CS_TYPE_DECOMPTE_FINAL = "228002";
    public final static String CS_TYPE_REMUNERATOIRES = "228005";
    public final static String CS_TYPE_TARDIF = "228001";
    public static final String FIELD_IDGENREINTERET = "IDGENREINTERET";

    public static final String FIELD_IDPLACALINT = "IDPLACALINT";

    public static final String FIELD_IDRUBRIQUE = "IDRUBRIQUE";
    public static final String FIELD_IDTYPEINTERET = "IDTYPEINTERET";
    public static final String FIELD_LIBELLEDE = "LIBELLEDE";
    public static final String FIELD_LIBELLEFR = "LIBELLEFR";
    public static final String FIELD_LIBELLEIT = "LIBELLEIT";

    public static final String TABLE_CAIMGIP = "CAIMGIP";

    private String idGenreInteret = new String();
    private String idPlanCalculInteret = new String();
    private String idRubrique = new String();
    private String idTypeInteret = new String();
    private String libelleDE = new String();
    private String libelleFR = new String();
    private String libelleIT = new String();
    private String numeroRubrique = "";
    private CAPlanCalculInteret planCalculInteret = null;
    private CARubrique rubrique = null;

    /**
     * Commentaire relatif au constructeur CAGenreInteret
     */
    public CAGenreInteret() {
        super();
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente le prochain numéro
        setIdGenreInteret(this._incCounter(transaction, idGenreInteret));
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CAGenreInteret.TABLE_CAIMGIP;
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idGenreInteret = statement.dbReadNumeric(CAGenreInteret.FIELD_IDGENREINTERET);
        idRubrique = statement.dbReadNumeric(CAGenreInteret.FIELD_IDRUBRIQUE);
        idPlanCalculInteret = statement.dbReadNumeric(CAGenreInteret.FIELD_IDPLACALINT);
        libelleFR = statement.dbReadString(CAGenreInteret.FIELD_LIBELLEFR);
        libelleDE = statement.dbReadString(CAGenreInteret.FIELD_LIBELLEDE);
        libelleIT = statement.dbReadString(CAGenreInteret.FIELD_LIBELLEIT);
        idTypeInteret = statement.dbReadNumeric(CAGenreInteret.FIELD_IDTYPEINTERET);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (!JadeStringUtil.isBlank(numeroRubrique)) {
            CARubriqueManager mgr = new CARubriqueManager();
            mgr.setSession(getSession());
            mgr.setForIdExterne(numeroRubrique);
            mgr.find();
            if (mgr.size() != 0) {
                idRubrique = ((CARubrique) mgr.getFirstEntity()).getIdRubrique();
            }
        }
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
        // Clé alternée par type
        if (alternateKey == CAGenreInteret.AK_PLAN_TYPE) {
            statement.writeKey(CAGenreInteret.FIELD_IDPLACALINT,
                    this._dbWriteNumeric(statement.getTransaction(), getIdPlanCalculInteret(), ""));
            statement.writeKey(CAGenreInteret.FIELD_IDTYPEINTERET,
                    this._dbWriteNumeric(statement.getTransaction(), getIdTypeInteret(), ""));
            // Traitement par défaut : pas de clé alternée
        } else {
            throw new Exception("Alternate key not supported");
        }
    }

    /**

	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CAGenreInteret.FIELD_IDGENREINTERET,
                this._dbWriteNumeric(statement.getTransaction(), getIdGenreInteret(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(CAGenreInteret.FIELD_IDGENREINTERET,
                this._dbWriteNumeric(statement.getTransaction(), getIdGenreInteret(), "idGenreInteret"));
        statement.writeField(CAGenreInteret.FIELD_IDRUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
        statement.writeField(CAGenreInteret.FIELD_IDPLACALINT,
                this._dbWriteNumeric(statement.getTransaction(), getIdPlanCalculInteret(), "idPlanCalculInteret"));
        statement.writeField(CAGenreInteret.FIELD_LIBELLEFR,
                this._dbWriteString(statement.getTransaction(), getLibelleFR(), "libelleFR"));
        statement.writeField(CAGenreInteret.FIELD_LIBELLEDE,
                this._dbWriteString(statement.getTransaction(), getLibelleDE(), "libelleDE"));
        statement.writeField(CAGenreInteret.FIELD_LIBELLEIT,
                this._dbWriteString(statement.getTransaction(), getLibelleIT(), "libelleIT"));
        statement.writeField(CAGenreInteret.FIELD_IDTYPEINTERET,
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeInteret(), "idTypeInteret"));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdGenreInteret() {
        return idGenreInteret;
    }

    public String getIdPlanCalculInteret() {
        return idPlanCalculInteret;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public String getIdTypeInteret() {
        return idTypeInteret;
    }

    public String getLibelleDE() {
        return libelleDE;
    }

    public String getLibelleFR() {
        return libelleFR;
    }

    public String getLibelleIT() {
        return libelleIT;
    }

    public String getNumeroRubrique() {
        CARubrique rubr = new CARubrique();
        rubr.setSession(getSession());
        rubr.setIdRubrique(getIdRubrique());
        try {
            rubr.retrieve();
        } catch (Exception e) {
            return "";
        }
        if (!rubr.isNew()) {
            return rubr.getIdExterne();
        } else {
            return "";
        }
    }

    public CAPlanCalculInteret getPlanCalculInteret() {
        if (planCalculInteret == null) {
            planCalculInteret = new CAPlanCalculInteret();
            planCalculInteret.setISession(getSession());
            planCalculInteret.setIdPlanCalculInteret(getIdPlanCalculInteret());
            try {
                planCalculInteret.retrieve();
                if (planCalculInteret.isNew()) {
                    planCalculInteret = null;
                }
            } catch (Exception e) {
                planCalculInteret = null;
            }
        }
        return planCalculInteret;
    }

    public APIRubrique getRubrique() {
        if (rubrique == null) {
            rubrique = new CARubrique();
            rubrique.setISession(getSession());
            rubrique.setIdRubrique(getIdRubrique());
            try {
                rubrique.retrieve();
                if (rubrique.isNew()) {
                    rubrique = null;
                }
            } catch (Exception e) {
                rubrique = null;
            }
        }
        return rubrique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setIdGenreInteret(String newIdGenreInteret) {
        idGenreInteret = newIdGenreInteret;
    }

    public void setIdPlanCalculInteret(String newIdPlanCalculInteret) {
        idPlanCalculInteret = newIdPlanCalculInteret;
    }

    public void setIdRubrique(String newIdRubrique) {
        idRubrique = newIdRubrique;
    }

    public void setIdTypeInteret(String newIdTypeInteret) {
        idTypeInteret = newIdTypeInteret;
    }

    public void setLibelleDE(String newLibelleDE) {
        libelleDE = newLibelleDE;
    }

    public void setLibelleFR(String newLibelleFR) {
        libelleFR = newLibelleFR;
    }

    public void setLibelleIT(String newLibelleIT) {
        libelleIT = newLibelleIT;
    }

    /**
     * @param string
     */
    public void setNumeroRubrique(String string) {
        numeroRubrique = string;
    }
}
