package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * Decrit les champs pour une annonce donn�e<br>
 * Dans la table champAnnonce (HECHANP), y'a un champ RNDCAD qui indique le cadrage � droite<br>
 * ou gauche, valeur qui donne aussi le type de comp�tion (gauche, droite, avec des z�ros ou des blancs, cf DT)<br>
 * Si cette valeur est � 1, c'est � gauche, si c'est � 0 c'est � droite Date de cr�ation : (27.11.2002 08:41:23)
 * 
 * @author: ado
 */
public class HEChampannonceViewBean extends BEntity implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** valeur cadrage */
    public final static int CADRAGE_DROITE = 2;
    /** valeur cadrage */
    public final static int CADRAGE_DROITE_BLANC = 3;
    /** valeur cadrage */
    public final static int CADRAGE_GAUCHE = 1;
    /** valeur cadrage */
    public final static char CAR_CADRAGE_DROITE = '0';
    /** valeur cadrage */
    public final static char CAR_CADRAGE_GAUCHE = ' ';

    /**
     * Lance l'application.
     * 
     * @param args
     *            un tableau d'arguments de ligne de commande
     */
    public static void main(java.lang.String[] args) {
        // Ins�rez ici le code de d�marrage de l'application
        try {
            BSession session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            HEChampannonceViewBean caVB = new HEChampannonceViewBean();
            caVB.setSession(session);
            caVB.setIdChampAnnonce("2");
            caVB.retrieve();
            System.out.println(caVB.isObligatoire() + " - " + caVB.getIdChamp() + "-" + caVB.getCadrage());
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        }
        System.exit(0);
    }

    /** (RDNCAD) */
    private String cadrage = new String();
    /** (RDNDEB) */
    private String debut = new String();
    /** (RDTCHA) */
    private String idChamp = new String();
    /** Fichier HECHANP */
    /** (RDICHA) */
    private String idChampAnnonce = new String();
    /** (REIPAE) */
    private String idParametrageAnnonce = new String();
    /** is Obligatoire * */
    private boolean isObligatoire = false;
    /** (RDNLON) */
    private String longueur = new String();

    /** (RDNOBL) */
    private String obligatoire = new String();

    // code systeme
    /**
     * Commentaire relatif au constructeur HEChampannonceViewBean
     */
    public HEChampannonceViewBean() {
        super();
    }

    /**
     * Effectue des traitements apr�s une lecture dans la BD et apr�s avoir vid� le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements apr�s la lecture de l'entit� dans la BD
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        setObligatoire(obligatoire);
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "HECHANP";
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propri�t�s �choue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idChampAnnonce = statement.dbReadNumeric("RDICHA");
        idParametrageAnnonce = statement.dbReadNumeric("REIPAE");
        idChamp = statement.dbReadNumeric("RDTCHA");
        debut = statement.dbReadNumeric("RDNDEB");
        longueur = statement.dbReadNumeric("RDNLON");
        obligatoire = statement.dbReadString("RDNOBL");
        cadrage = statement.dbReadString("RNDCAD");
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
     * Sauvegarde les valeurs des propri�t�s propres de l'entit� composant une cl� altern�e
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propri�t�s a �chou�e
     * @param alternateKey
     *            int le num�ro de la cl� altern�e � utiliser
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        // Traitement par d�faut : pas de cl� altern�e
        // private String idParametrageAnnonce = new String();
        statement.writeKey("REIPAE",
                _dbWriteNumeric(statement.getTransaction(), getIdParametrageAnnonce(), "id Parametrage HEIREAP"));
        statement.writeKey("RDTCHA", _dbWriteNumeric(statement.getTransaction(), getIdChamp(), "id champ 118xxx"));
        // private String idChamp = new String();
    }

    /**
	
	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("RDICHA", _dbWriteNumeric(statement.getTransaction(), getIdChampAnnonce(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("RDICHA",
                _dbWriteNumeric(statement.getTransaction(), getIdChampAnnonce(), "idChampAnnonce"));
        statement.writeField("REIPAE",
                _dbWriteNumeric(statement.getTransaction(), getIdParametrageAnnonce(), "idParametrageAnnonce"));
        statement.writeField("RDTCHA", _dbWriteNumeric(statement.getTransaction(), getIdChamp(), "idChamp"));
        statement.writeField("RDNDEB", _dbWriteNumeric(statement.getTransaction(), getDebut(), "debut"));
        statement.writeField("RDNLON", _dbWriteNumeric(statement.getTransaction(), getLongueur(), "longueur"));
        statement.writeField("RDNOBL", _dbWriteString(statement.getTransaction(), getObligatoire(), "obligatoire"));
        statement.writeField("RNDCAD", _dbWriteString(statement.getTransaction(), getCadrage(), "cadrage"));
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.11.2002 11:40:01)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCadrage() {
        return cadrage;
    }

    public String getDebut() {
        return debut;
    }

    public String getIdChamp() {
        return idChamp;
    }

    /**
     * Ins�rez la description de la m�thode ici.
     * 
     * @return String
     */
    public String getIdChampAnnonce() {
        return idChampAnnonce;
    }

    public String getIdParametrageAnnonce() {
        return idParametrageAnnonce;
    }

    public String getLongueur() {
        return longueur;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2002 08:46:09)
     * 
     * @return java.lang.String
     */
    private java.lang.String getObligatoire() {
        return obligatoire;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2002 08:47:15)
     * 
     * @return boolean
     */
    public boolean isObligatoire() {
        return isObligatoire;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.11.2002 11:40:01)
     * 
     * @param newCadrage
     *            java.lang.String
     */
    public void setCadrage(java.lang.String newCadrage) {
        cadrage = newCadrage;
    }

    public void setDebut(String newDebut) {
        debut = newDebut;
    }

    public void setIdChamp(String newIdChamp) {
        idChamp = newIdChamp;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setIdChampAnnonce(String newIdChampAnnonce) {
        idChampAnnonce = newIdChampAnnonce;
    }

    public void setIdParametrageAnnonce(String newIdParametrageAnnonce) {
        idParametrageAnnonce = newIdParametrageAnnonce;
    }

    public void setLongueur(String newLongueur) {
        longueur = newLongueur;
    }

    public void setObligatoire(boolean newIsObligatoire) {
        // 0 est pas obligatoire
        // 1 est obligatoire
        isObligatoire = newIsObligatoire;
        if (isObligatoire) {
            obligatoire = "1";
        } else {
            obligatoire = "0";
        }
    }

    private void setObligatoire(String isObligatoire) {
        // 0 est pas obligatoire
        // 1 est obligatoire
        if (isObligatoire.equals("1")) {
            setObligatoire(true);
        } else if (isObligatoire.equals("0")) {
            setObligatoire(false);
        } else {
            setObligatoire(false);
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toMyString() {
        return getIdChampAnnonce() + "-" + getIdChamp();
    }
}
