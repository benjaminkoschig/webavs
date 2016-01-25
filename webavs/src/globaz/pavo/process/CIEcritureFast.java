package globaz.pavo.process;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Object représentant une écriture. TODO: remplacer les SQL joins par l'API (à faire après optimisation de l'API).
 * Actuellement, fait accès au tables de PYXIS Date de création : (12.11.2002 13:05:40)
 * 
 * @author: David Girardin
 */
public class CIEcritureFast extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_ANNEE_JEUNESSE = "306001";
    // Type de compte
    public final static String CS_CI = "303001";

    public final static String CS_CI_SUSPENS = "303002";
    public final static String CS_CI_SUSPENS_SUPPRIMES = "303003";
    // Code genre d''une inscription CI
    public final static String CS_CIGENRE_0 = "310000";
    public final static String CS_CIGENRE_1 = "310001";
    public final static String CS_CIGENRE_2 = "310002";
    public final static String CS_CIGENRE_3 = "310003";
    public final static String CS_CIGENRE_4 = "310004";
    public final static String CS_CIGENRE_5 = "310005";
    public final static String CS_CIGENRE_6 = "310006";
    public final static String CS_CIGENRE_7 = "310007";
    public final static String CS_CIGENRE_8 = "310008";
    public final static String CS_CIGENRE_9 = "310009";
    // Code amortissement
    public final static String CS_CODE_AMORTISSEMENT = "313001";
    public final static String CS_CODE_EXEMPTION = "313002";
    public final static String CS_CODE_PROVISOIRE = "313004";
    public final static String CS_CODE_SURSIS = "313003";
    public final static String CS_CORRECTION = "303005";
    // Code spécial pour inscription CI
    public final static String CS_COTISATION_MINIMALE = "312001";
    // Code extourne d''une inscription CI
    public final static String CS_EXTOURNE_1 = "311001";
    public final static String CS_EXTOURNE_2 = "311002";
    public final static String CS_EXTOURNE_3 = "311003";
    public final static String CS_EXTOURNE_5 = "311005";
    public final static String CS_EXTOURNE_6 = "311006";
    public final static String CS_EXTOURNE_7 = "311007";
    public final static String CS_EXTOURNE_8 = "311008";
    public final static String CS_EXTOURNE_9 = "311009";
    public final static String CS_GENRE_6 = "303004";
    public final static String CS_GENRE_7 = "303007";
    // Branche économique
    public final static String CS_HORLOGERIE = "314033";
    public final static String CS_LACUNE_COTISATION_APPOINT = "306003";
    public final static String CS_LACUNE_COTISATION_JEUNESSE = "306002";
    public final static String CS_LICHTENSTEIN = "306006";
    // Particulier
    public final static String CS_MANDAT_NORMAL = "306000";
    public final static String CS_NONFORMATTEUR_INDEPENDANT = "312002";
    public final static String CS_NONFORMATTEUR_SALARIE = "312003";
    public final static String CS_PARTAGE_CI_CLOTURES = "306005";
    public final static String CS_PARTAGE_RAM = "306004";
    public final static String CS_TEMPORAIRE = "303006";
    public final static String CS_TEMPORAIRE_SUSPENS = "303008";
    /** (KBIECR) */
    private String ecritureId = new String();
    /** (KBITIE) */
    private String employeur = new String();

    // code systeme
    /**
     * Commentaire relatif au constructeur CIEcriture
     */
    public CIEcritureFast() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CIECRIP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        ecritureId = statement.dbReadNumeric("KBIECR");
        employeur = statement.dbReadNumeric("KBITIE");

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**

 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + _getTableName() + ".KBIECR",
                _dbWriteNumeric(statement.getTransaction(), getEcritureId(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

    }

    /**
     * Returns the ecritureId.
     * 
     * @return String
     */
    public String getEcritureId() {
        return ecritureId;
    }

    /**
     * Returns the employeur.
     * 
     * @return String
     */
    public String getEmployeur() {
        return employeur;
    }

    /**
     * Sets the ecritureId.
     * 
     * @param ecritureId
     *            The ecritureId to set
     */
    public void setEcritureId(String ecritureId) {
        this.ecritureId = ecritureId;
    }

    /**
     * Sets the employeur.
     * 
     * @param employeur
     *            The employeur to set
     */
    public void setEmployeur(String employeur) {
        this.employeur = employeur;
    }

}
