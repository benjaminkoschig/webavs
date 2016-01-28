package globaz.hermes.test;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (03.02.2003 11:50:53)
 * 
 * @author: Administrator
 */
public class HEAnnoncesTestViewBean extends BEntity implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Lance l'application.
     * 
     * @param args
     *            un tableau d'arguments de ligne de commande
     */
    public static void main(java.lang.String[] args) {
        // Ins�rez ici le code de d�marrage de l'application
        new HEAnnoncesTestViewBean().go();
    }

    /** (RNDDAN) */
    private String date = new String();
    /** Fichier HEANNOP */
    /** (RNIANN) */
    private String idAnnonce = new String();
    /** (RMILOT) */
    private String idLot = new String();
    /** (RNTPRO) */
    private String idProg = new String();
    /** (RNTSTA) */
    private String idStatut = new String();
    /** (RNTMES) */
    private String message = new String();
    /** (RNLENR) */
    private String record = new String();
    /** (RNREFU) */
    private String refUnique = new String();

    /** (RNLUTI) */
    private String user = new String();

    /**
     * Commentaire relatif au constructeur HEAnnoncesTest.
     */
    public HEAnnoncesTestViewBean() {
        super();
    }

    /**
     * Renvoie le nom de la table
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "HEANNOP";
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la base de donn�es
     * 
     * @exception java.lang.Exception
     *                si la lecture des propri�t�s a �chou�e
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAnnonce = statement.dbReadNumeric("RNIANN");
        idLot = statement.dbReadNumeric("RMILOT");
        date = statement.dbReadNumeric("RNDDAN");
        user = statement.dbReadString("RNLUTI");
        idProg = statement.dbReadString("RNTPRO");
        record = statement.dbReadString("RNLENR");
        refUnique = statement.dbReadString("RNREFU");
        idStatut = statement.dbReadNumeric("RNTSTA");
        message = statement.dbReadNumeric("RNTMES");
    }

    /**
     * Valide le contenu de l'entit� (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propri�t�s propres de l'entit� composant la cl� primaire
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propri�t�s a �chou�e
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("RNIANN", _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), ""));
    }

    /**
     * Sauvegarde les valeurs des propri�t�s propres de l'entit� dans la base de donn�es
     * 
     * @param statement
     *            l'instruction � utiliser
     * @exception java.lang.Exception
     *                si la sauvegarde des propri�t�s a �chou�e
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("RNIANN", _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        statement.writeField("RMILOT", _dbWriteNumeric(statement.getTransaction(), getIdLot(), "idLot"));
        statement.writeField("RNDDAN", _dbWriteNumeric(statement.getTransaction(), getDate(), "date"));
        statement.writeField("RNLUTI", _dbWriteString(statement.getTransaction(), getUser(), "user"));
        statement.writeField("RNTPRO", _dbWriteString(statement.getTransaction(), getIdProg(), "idProg"));
        statement.writeField("RNLENR", _dbWriteString(statement.getTransaction(), getRecord(), "record"));
        statement.writeField("RNREFU", _dbWriteString(statement.getTransaction(), getRefUnique(), "refUnique"));
        statement.writeField("RNTSTA", _dbWriteNumeric(statement.getTransaction(), getIdStatut(), "idStatut"));
        statement.writeField("RNTMES", _dbWriteNumeric(statement.getTransaction(), getMessage(), "message"));
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDate() {
        return date;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdAnnonce() {
        return idAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdLot() {
        return idLot;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdMessage() {
        return message;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdProg() {
        return idProg;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdStatut() {
        return idStatut;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRecord() {
        return record;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRefUnique() {
        return refUnique;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUser() {
        return user;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @return java.lang.String
     */
    public void go() {
        globaz.hermes.db.gestion.HEInputAnnonceViewBean viewBean = new globaz.hermes.db.gestion.HEInputAnnonceViewBean();
        globaz.hermes.utils.ChampsMap champsListe = viewBean.getChampsTable();
        for (java.util.Enumeration e = champsListe.keys(); e.hasMoreElements();) {
            Object key = e.nextElement();
            if (globaz.hermes.db.gestion.HEAnnoncesViewBean.isCodeSysteme((String) key)) {
            } else if (globaz.hermes.db.gestion.HEAnnoncesViewBean.isDateField((String) key)) {
            } else if (globaz.hermes.db.gestion.HEAnnoncesViewBean.isCustomField((String) key)) {
                globaz.hermes.db.gestion.HEAnnoncesViewBean.getChampsAsCodeSystemDefaut((String) key);
            } else {
                champsListe.getLongueur(key);
                if (globaz.hermes.db.gestion.HEAnnoncesViewBean.isForbiddenField((String) key)) {
                    // System.out.println(globaz.hermes.db.gestion.HEAnnoncesViewBean.getMotifCU("112019"));
                }
                // viewBean.MOTIF_ANNONCE;
            }
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @param newDate
     *            java.lang.String
     */
    public void setDate(java.lang.String newDate) {
        date = newDate;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @param newIdAnnonce
     *            java.lang.String
     */
    public void setIdAnnonce(java.lang.String newIdAnnonce) {
        idAnnonce = newIdAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @param newIdLot
     *            java.lang.String
     */
    public void setIdLot(java.lang.String newIdLot) {
        idLot = newIdLot;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @param newMessage
     *            java.lang.String
     */
    public void setIdMessage(java.lang.String newMessage) {
        message = newMessage;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @param newIdProg
     *            java.lang.String
     */
    public void setIdProg(java.lang.String newIdProg) {
        idProg = newIdProg;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @param newIdStatut
     *            java.lang.String
     */
    public void setIdStatut(java.lang.String newIdStatut) {
        idStatut = newIdStatut;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @param newRecord
     *            java.lang.String
     */
    public void setRecord(java.lang.String newRecord) {
        record = newRecord;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @param newRefUnique
     *            java.lang.String
     */
    public void setRefUnique(java.lang.String newRefUnique) {
        refUnique = newRefUnique;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.02.2003 13:05:18)
     * 
     * @param newUser
     *            java.lang.String
     */
    public void setUser(java.lang.String newUser) {
        user = newUser;
    }
}
