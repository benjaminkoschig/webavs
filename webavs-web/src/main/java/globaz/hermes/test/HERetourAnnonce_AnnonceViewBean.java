package globaz.hermes.test;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (20.01.2003 12:55:21)
 * 
 * @author: Administrator
 */
public class HERetourAnnonce_AnnonceViewBean extends BEntity implements FWViewBeanInterface {
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
    }

    private String creator = "";
    private String date = "";
    private String statut = "";
    private String statutLibelle = "";
    private String typeAnnonce = "";

    private String typeAnnonceAttendue = "";

    /**
     * Commentaire relatif au constructeur HERetourAnnonce_AnnonceViewBean.
     */
    public HERetourAnnonce_AnnonceViewBean() {
        super();
    }

    /**
     * Renvoie le nom de la table
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la base de donn�es
     * 
     * @exception java.lang.Exception
     *                si la lecture des propri�t�s a �chou�e
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        //
        /*
         * libelleChamp = statement.dbReadString("PCOLUT"); enregistrement = statement.dbReadString("RNLENR");
         * libelleAnnonce = statement.dbReadString("PCOUID") + "-" + statement.dbReadString("PCOLUT"); codeApplication =
         * statement.dbReadString("PCOUID"); date = statement.dbReadString("RNDDAN"); idCSStatut =
         * statement.dbReadString("RNTSTA");
         */
        //
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
    }
}
