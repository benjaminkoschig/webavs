package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * Insérez la description du type ici. Date de création : (12.03.2003 08:30:24)
 * 
 * @author: Administrator
 */
public class HELienChampAnnonceViewBean extends BEntity implements FWViewBeanInterface {
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
        // Insérez ici le code de démarrage de l'application
        try {
            BSession session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            HELienChampAnnonceViewBean lien = new HELienChampAnnonceViewBean();
            lien.setSession(session);
            // AJOUT
            lien.setIdLienAnnonce("1");
            lien.setIdChampAnnonceDepart("1");
            lien.setIdChampAnnonceRetour("2");
            lien.add();
            // Retrieve
            lien = new HELienChampAnnonceViewBean();
            lien.setSession(session);
            lien.setIdLienChampAnnonce("1");
            lien.retrieve();
            // MODIFY
            lien.setIdChampAnnonceRetour("3");
            lien.update();
            // Delete
            lien.retrieve();
            if (lien.hasErrors()) {
                throw new Exception(lien.getErrors().toString());
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        }
        System.exit(0);
    }

    /** (RDICHA) */
    private String idChampAnnonceDepart = new String();
    /** (HEC_RDICHA) */
    private String idChampAnnonceRetour = new String();
    /** (RIILIA) */
    private String idLienAnnonce = new String();

    /** Fichier HELICAP */
    /** (RJILCA) */
    private String idLienChampAnnonce = new String();

    /**
     * Commentaire relatif au constructeur HELienChampAnnonceViewBean.
     */
    public HELienChampAnnonceViewBean() {
        super();
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        setIdLienChampAnnonce(_incCounter(transaction, getIdLienChampAnnonce()));
    }

    /**
     * Renvoie le nom de la table
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "HELICAP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la base de données
     * 
     * @exception java.lang.Exception
     *                si la lecture des propriétés a échouée
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idLienChampAnnonce = statement.dbReadNumeric("RJILCA");
        idLienAnnonce = statement.dbReadNumeric("RIILIA");
        idChampAnnonceDepart = statement.dbReadNumeric("RDICHA");
        idChampAnnonceRetour = statement.dbReadNumeric("HEC_RDICHA");
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant la clé primaire
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("RJILCA", _dbWriteNumeric(statement.getTransaction(), getIdLienChampAnnonce(), ""));
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité dans la base de données
     * 
     * @param statement
     *            l'instruction à utiliser
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("RJILCA",
                _dbWriteNumeric(statement.getTransaction(), getIdLienChampAnnonce(), "idChampAnnonce"));
        statement
                .writeField("RIILIA", _dbWriteNumeric(statement.getTransaction(), getIdLienAnnonce(), "idLienAnnonce"));
        statement.writeField("RDICHA",
                _dbWriteNumeric(statement.getTransaction(), getIdChampAnnonceDepart(), "idChampAnnonceDepart"));
        statement.writeField("HEC_RDICHA",
                _dbWriteNumeric(statement.getTransaction(), getIdChampAnnonceRetour(), "idChampAnnonceRetour"));
    }

    public String getIdChampAnnonceDepart() {
        return idChampAnnonceDepart;
    }

    public String getIdChampAnnonceRetour() {
        return idChampAnnonceRetour;
    }

    public String getIdLienAnnonce() {
        return idLienAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2003 08:40:04)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdLienChampAnnonce() {
        return idLienChampAnnonce;
    }

    public void setIdChampAnnonceDepart(String newIdChampAnnonceDepart) {
        idChampAnnonceDepart = newIdChampAnnonceDepart;
    }

    public void setIdChampAnnonceRetour(String newIdChampAnnonceRetour) {
        idChampAnnonceRetour = newIdChampAnnonceRetour;
    }

    public void setIdLienAnnonce(String newIdLienAnnonce) {
        idLienAnnonce = newIdLienAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2003 08:40:04)
     * 
     * @param newIdLienChampAnnonce
     *            java.lang.String
     */
    public void setIdLienChampAnnonce(java.lang.String newIdLienChampAnnonce) {
        idLienChampAnnonce = newIdLienChampAnnonce;
    }
}
