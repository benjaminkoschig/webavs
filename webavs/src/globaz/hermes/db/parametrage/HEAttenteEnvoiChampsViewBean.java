package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (21.03.2003 15:09:18)
 * 
 * @author: Administrator
 */
public class HEAttenteEnvoiChampsViewBean extends HEAttenteRetourChampsOptimizedViewBean implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur HEAttenteEnvoiChampsViewBean.
     */
    public HEAttenteEnvoiChampsViewBean() {
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
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la base de donn�es
     * 
     * @exception java.lang.Exception
     *                si la lecture des propri�t�s a �chou�e
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        valeur = statement.dbReadString("CHAMP"); // VALEUR
        idChamp = statement.dbReadNumeric("IDCHAMP"); // RDTCHA
        libelleChamp = statement.dbReadString("CHAMPLIB"); // PCOLUT
        longueur = statement.dbReadNumeric("RDNLON"); // LONGUEUR
        statut = statement.dbReadNumeric("RNTSTA"); // RNTSTA
    }
}
