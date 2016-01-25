package globaz.hermes.db.gestion;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.access.HENbreARCDansLot;
import globaz.hermes.print.itext.HEDocumentZas;
import java.util.Arrays;

/**
 * Les lots envoy�s ou re�us de la Centrale<br>
 * Un lot comporte x annonces<br>
 * Il y'a un lot par jour<br>
 * fichier HELOTSP<br>
 * Date de cr�ation : (20.11.2002 08:43:55)
 * 
 * @author: ado
 */
public class HELotViewBean extends BEntity implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_LOT_ETAT_CHARGE = "114002";
    public final static String CS_LOT_ETAT_ENVOYE = "114004";
    public final static String CS_LOT_ETAT_OUVERT = "114001";
    public final static String CS_LOT_ETAT_TRAITE = "114003";
    // pour les �crans de consultations des annonces en envoi et des annonces en
    // r�ception
    public final static String CS_LOT_PTY_BASSE = "113001";
    public final static String CS_LOT_PTY_HAUTE = "113002";
    // Constantes pour annonces adaptations
    public final static String CS_TYPE_ADAPTATION_RENTES = "116005";
    // Constante codes syst�me
    public final static String CS_TYPE_ADAPTATION_RENTES_PC = "116006";
    // Constante codes syst�me
    public final static String CS_TYPE_ASSURANCE_CHOMAGE = "116007";
    // Constante codes syst�me
    public final static String CS_TYPE_AVIS_DECES = "116003";
    // Constante codes syst�me
    public final static String CS_TYPE_ENVOI = "116001";
    // Le groupe
    public final static String CS_TYPE_GROUPE = "HETYPE";
    // Constante codes syst�me
    public final static String CS_TYPE_RECEPTION = "116002";
    // Constante codes syst�me
    public final static String CS_TYPE_RENTES = "116004";

    // lot non quittanc�
    public final static String LOT_NON_QUITTANCE = "0";

    // lot quittanc�
    public final static String LOT_QUITTANCE = "1";

    public static String[] getLotEnvoi() {
        String[] s = { HELotViewBean.CS_TYPE_ENVOI };
        return s;
    }

    public static String[] getLotReception() {
        String[] s = { HELotViewBean.CS_TYPE_RECEPTION, HELotViewBean.CS_TYPE_AVIS_DECES, HELotViewBean.CS_TYPE_RENTES,
                HELotViewBean.CS_TYPE_ADAPTATION_RENTES, HELotViewBean.CS_TYPE_ADAPTATION_RENTES_PC };
        return s;
    }

    /**
     * Permet de tester retrieve, update, add, delete
     * 
     * @param args
     *            args
     */
    public static void main(String[] args) {
    }

    /** code systeme */
    private FWParametersSystemCodeManager csType = null;
    /** (RMDDEN) date de l'envoi */
    private String dateCentrale = new String();
    /** (RMDDTR) date de traitement */
    private String dateTraitement = new String();
    //
    private HEDocumentZas doc;
    /** (RMTETA) �tat du lot */
    private String etat = new String();
    /** nom de la table pour les lots archiv�s */
    private final String HELOTSP_ARCHIVE = "HELOTSR";
    /** nom de la table pour les lots en cours */
    private final String HELOTSP_EN_COURS = "HELOTSP";
    /** (RMDHEU) heure de traitement */
    private String heureTraitement = new String();
    /** (RMILOT) id du lot */
    private String idLot = new String();
    /** flag pour indiquer si l'annonce concerne l'archive */
    private boolean isArchivage = false;
    /** nombre d'annonces pour ce lot */
    private int nbAnnonces = 0;
    /** (RMTPRI) priorit� du lot */
    private String priorite = new String();
    /** (RMBQUI) oui/non : confirmation de la r�ception */
    private String quittance = new String();
    /** (RMTTYP) le type (envoi ou r�ception) */
    private String type = new String();
    /** (RMLUTI) l'utilisateur qui a fait l'envoi */
    private String utilisateur = new String();

    /**
     * Commentaire relatif au constructeur HELotViewBean
     */
    public HELotViewBean() {
        super();
    }

    /**
     * Constructor HELotViewBean.
     * 
     * @param session
     */
    public HELotViewBean(BSession session) {
        this();
        setSession(session);
    }

    /**
     * Effectue des traitements apr�s une suppression de la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements apr�s la suppression de l'entit� de la BD
     * <p>
     * La transaction n'est pas valid�e si le buffer d'erreurs n'est pas vide apr�s l'ex�cution de
     * <code>_afterDelete()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws java.lang.Exception {
        HEAnnoncesListViewBean annonces = new HEAnnoncesListViewBean();
        annonces.setSession(getSession());
        annonces.setForIdLot(getIdLot());
        annonces.find(transaction);
        for (int i = 0; i < annonces.size(); i++) {
            HEAnnoncesViewBean entity = (HEAnnoncesViewBean) annonces.getEntity(i);
            entity.delete(transaction);
        }
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
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        // ALD 19-03.2004
        HENbreARCDansLot nbreArc = new HENbreARCDansLot();
        nbreArc.setSession(getSession());
        nbreArc.setNumeroLot(getIdLot());
        nbreArc.setIsArchivage(isArchivage());
        nbreArc.retrieve(transaction);
        setNbAnnonces(nbreArc.getNombreARC());
    }

    /**
     * Appell�e avant l'ajout, permet de g�n�rer la PK
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si l'incr�mentation �choue
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        if (!isArchivage) {
            setIdLot(this._incCounter(transaction, "0"));
        }
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
        // if (DateUtils.isFormat(getDateTraitement(), DateUtils.JJMMAAAA_DOTS))
        // {
        // setDateTraitement(DateUtils.convertDate(getDateTraitement(),
        // DateUtils.JJMMAAAA_DOTS, DateUtils.AAAAMMJJ));
        // }
        // if (DateUtils.isFormat(getDateCentrale(), DateUtils.JJMMAAAA_DOTS)) {
        // setDateTraitement(DateUtils.convertDate(getDateCentrale(),
        // DateUtils.JJMMAAAA_DOTS, DateUtils.AAAAMMJJ));
        // }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        if (isArchivage) {
            return HELOTSP_ARCHIVE;
        } else {
            return HELOTSP_EN_COURS;
        }
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propri�t�s �choue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idLot = statement.dbReadNumeric("RMILOT", 0);
        dateCentrale = statement.dbReadDateAMJ("RMDDEN", 0);
        heureTraitement = statement.dbReadString("RMDHEU");
        utilisateur = statement.dbReadString("RMLUTI");
        type = statement.dbReadNumeric("RMTTYP", 0);
        quittance = statement.dbReadString("RMBQUI");
        priorite = statement.dbReadNumeric("RMTPRI", 0);
        etat = statement.dbReadNumeric("RMTETA", 0);
        dateTraitement = statement.dbReadDateAMJ("RMDDTR", 0);
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
     * Ecrit la clef primaire
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("RMILOT", this._dbWriteNumeric(statement.getTransaction(), getIdLot(), ""));
    }

    /**
     * Ecrit les propri�t�s
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @throws Exception
     *             si echec de l'�criture
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("RMILOT", this._dbWriteNumeric(statement.getTransaction(), getIdLot(), "idLot"));
        statement.writeField("RMDDEN",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateCentrale(), "dateCentrale"));
        statement.writeField("RMDHEU",
                this._dbWriteString(statement.getTransaction(), getHeureTraitement(), "heureTraitement"));
        statement.writeField("RMLUTI",
                this._dbWriteString(statement.getTransaction(), getUtilisateur().toUpperCase(), "utilisateur"));
        statement.writeField("RMTTYP", this._dbWriteNumeric(statement.getTransaction(), getType(), "type"));
        statement.writeField("RMBQUI", this._dbWriteString(statement.getTransaction(), getQuittance(), "quittance"));
        statement
                .writeField("RMTPRI", this._dbWriteNumeric(statement.getTransaction(), getPriorite(), "prioriteEnvoi"));
        statement.writeField("RMTETA", this._dbWriteNumeric(statement.getTransaction(), getEtat(), "etatLot"));
        statement.writeField("RMDDTR",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateTraitement(), "dateTraitement"));
    }

    /**
     * Renvoit le type du lot (Envoi/r�ception)
     * 
     * @return String le type
     */
    public String getCsTypeLibelle() {
        if (csType == null) { // charge la liste
            csType = new FWParametersSystemCodeManager();
            csType.setForIdGroupe(HELotViewBean.CS_TYPE_GROUPE);
            csType.setSession(getSession());
            try {
                csType.find();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Peu orthodoxe mais gain de perfomance ENORME !
        // (�vite un find � chaque fois)
        FWParametersSystemCode line;
        for (int i = 0; i < csType.size(); i++) {
            line = (FWParametersSystemCode) csType.getEntity(i);
            if (line.getCurrentCodeUtilisateur().getIdCodeSysteme().equals(type)) {
                return line.getCurrentCodeUtilisateur().getLibelle();
            }
        }
        return "";
    }

    public String getDateCentrale() {
        return dateCentrale;
    }

    /**
     * Renvoit la date d'envoi formatt�e
     * 
     * @return String la date
     */
    public String getDateCentraleLibelle() {
        return getDateCentrale();
    }

    public String getDateTraitement() {
        return dateTraitement;
    }

    public String getDateTraitementLibelle() {
        return getDateTraitement();
    }

    /**
     * Returns the doc.
     * 
     * @return HEDocumentZas
     */
    public HEDocumentZas getDoc() {
        return doc;
    }

    public String getEtat() {
        return etat;
    }

    public String getEtatLibelle() {
        try {
            FWParametersSystemCode cs = new FWParametersSystemCode();
            cs.setSession(getSession());
            cs.getCode(getEtat());
            cs.retrieve();
            return cs.getCurrentCodeUtilisateur().getLibelle();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getHeureTraitement() {
        return heureTraitement;
    }

    /**
     * Retourne la PK du lot
     * 
     * @return String la PK
     */
    public String getIdLot() {
        return idLot;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (19.03.2003 12:52:55)
     * 
     * @return int
     */
    public int getNbAnnonces() {
        return nbAnnonces;
    }

    public String getPriorite() {
        return priorite;
    }

    public String getPrioriteLibelle() {
        try {
            FWParametersSystemCode cs = new FWParametersSystemCode();
            cs.setSession(getSession());
            cs.getCode(getPriorite());
            cs.retrieve();
            return cs.getCurrentCodeUtilisateur().getLibelle();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Renvoit la valeur de la quittance o/n
     * 
     * @return String Oui ou Non
     */
    public String getQuittance() {
        return quittance;
    }

    public String getSqlForDeletePasArc() {
        try {
            // update sans BETWEEN car je veux qu'il inclut les deux bornes
            StringBuffer sql = new StringBuffer("DELETE FROM ");
            sql.append(_getCollection());
            sql.append("HEANNOP ");
            sql.append("WHERE RNLENR NOT LIKE '11%'");
            sql.append(" AND RMILOT = ?");
            return sql.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Renvoit le type du lot (Envoi/r�ception)
     * 
     * @return String le type
     */
    public String getType() {
        return type;
    }

    /**
     * Renvoit le type du lot (Envoi/r�ception)
     * 
     * @return String le type
     */
    public String getTypeLibelle() {
        try {
            return ((HEApplication) getSession().getApplication()).getCsTypeListe(getSession())
                    .getCodeSysteme(getType()).getCurrentCodeUtilisateur().getLibelle();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Renvoit le nom de l'utilisateur
     * 
     * @return String l'utilisateur
     */
    public String getUtilisateur() {
        return utilisateur;
    }

    /**
     * Returns the isArchivage.
     * 
     * @return boolean
     */
    public boolean isArchivage() {
        return isArchivage;
    }

    public boolean isLotEnvoi(String type) {
        return Arrays.asList(HELotViewBean.getLotEnvoi()).contains(type);
    }

    public boolean isLotReception(String type) {
        return Arrays.asList(HELotViewBean.getLotReception()).contains(type);
    }

    public void setArchivage(boolean value) {
        isArchivage = value;
    }

    public void setDateCentrale(String dateCentrale) {
        this.dateCentrale = dateCentrale;
    }

    public void setDateTraitement(String dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setHeureTraitement(String heureTraitement) {
        this.heureTraitement = heureTraitement;
    }

    /**
     * Fixe la pk du lot
     * 
     * @param String
     *            la nouvelle pk
     */
    public void setIdLot(String newIdLot) {
        idLot = newIdLot;
    }

    public void setIsArchivage(String value) {
        isArchivage = Boolean.valueOf(value).booleanValue();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (19.03.2003 12:52:55)
     * 
     * @param newNbAnnonces
     *            int
     */
    public void setNbAnnonces(int newNbAnnonces) {
        nbAnnonces = newNbAnnonces;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    /**
     * Fixe la quittance
     * 
     * @param String
     *            la nouvelle quittance
     */
    public void setQuittance(String newQuittance) {
        quittance = newQuittance;
    }

    /**
     * Fixe le type (Envoi/R�ception)
     * 
     * @param String
     *            le type
     */
    public void setType(String newType) {
        type = newType;
    }

    /**
     * Fixe l'utilisateur
     * 
     * @param String
     *            l'utilisateur
     */
    public void setUtilisateur(String newUtilisateur) {
        utilisateur = newUtilisateur;
    }
}
