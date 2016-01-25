package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (26.11.2002 08:04:53)
 * 
 * @author: ado
 */
public class HECodeapplicationprogramme extends BEntity implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.11.2002 08:12:18)
     * 
     * @param args
     *            java.lang.String[]
     */
    public static void main(String[] args) {
        try {
            BSession session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            HECodeapplicationprogramme codeapp = new HECodeapplicationprogramme();
            codeapp.setSession(session);
            codeapp.setIdCodeapplicationprogramme("1");
            codeapp.retrieve();
            System.out.println(codeapp.getIdCodeapplicationprogramme());
            System.out.println(codeapp.getIdCodeapplication());
            System.out.println(codeapp.getIdProgramme());
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        }
        System.exit(0);
    }

    /** (RGTCOA) */
    private String idCodeapplication = new String();
    /** Fichier HECOAPP */
    /** (RGICAP) */
    private String idCodeapplicationprogramme = new String();

    /** (RGTPRO) */
    private String idProgramme = new String();

    /**
     * Commentaire relatif au constructeur HECodeapplicationprogramme
     */
    public HECodeapplicationprogramme() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "HECOAPP";
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propri�t�s �choue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCodeapplicationprogramme = statement.dbReadNumeric("RGICAP", 0);
        idProgramme = statement.dbReadString("RGTPRO");
        idCodeapplication = statement.dbReadNumeric("RGTCOA", 0);
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
        statement.writeKey("RGICAP", _dbWriteNumeric(statement.getTransaction(), getIdCodeapplicationprogramme(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(
                "RGICAP",
                _dbWriteNumeric(statement.getTransaction(), getIdCodeapplicationprogramme(),
                        "idCodeapplicationprogramme"));
        statement.writeField("RGTPRO", _dbWriteString(statement.getTransaction(), getIdProgramme(), "idProgramme"));
        statement.writeField("RGTCOA",
                _dbWriteNumeric(statement.getTransaction(), getIdCodeapplication(), "idCodeapplication"));
    }

    public String getIdCodeapplication() {
        return idCodeapplication;
    }

    /**
     * Ins�rez la description de la m�thode ici.
     * 
     * @return String
     */
    public String getIdCodeapplicationprogramme() {
        return idCodeapplicationprogramme;
    }

    public String getIdProgramme() {
        return idProgramme;
    }

    public void setIdCodeapplication(String newIdCodeapplication) {
        idCodeapplication = newIdCodeapplication;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setIdCodeapplicationprogramme(String newIdCodeapplicationprogramme) {
        idCodeapplicationprogramme = newIdCodeapplicationprogramme;
    }

    public void setIdProgramme(String newIdProgramme) {
        idProgramme = newIdProgramme;
    }
}
