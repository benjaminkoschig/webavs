package globaz.hermes.db.parametrage;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.hermes.utils.StringUtils;

/**
 * Classe repr�sentant les crit�re. Date de cr�ation : (30.10.2002 13:47:38)
 * 
 * @author: ado
 */
public class HECriteres extends FWParametersSystemCode {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String groupe = "HECRITER";
    private java.lang.String idTypeCode = "11100003";

    public HECriteres() {
        super();
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // choper le prochain incr�ment
        String lib = getLibelle();
        setId(_incCounter(transaction, "0"));
        // on tronque
        int i = 0;
        setLibelle(StringUtils.trimString(lib, 80));
        FWParametersUserCode uc = new FWParametersUserCode();
        // uc.setLibelle(StringUtils.trimString(getLibelle(), 60));
        uc.setCodeUtilisateur("-");
        uc.setLibelle(StringUtils.trimString(lib, 60));
        setCurrentCodeUtilisateur(uc);
        // Type de code
        setIdTypeCode(idTypeCode);
        // groupe
        setGroupe(groupe);
        // num�ro ordre
        setOrdre("1");
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
        // Faut ici setter l'abr�viation
        // et tronquer l'abr�viation et le libelle
        setLibelle(StringUtils.trimString(getLibelle(), 80));
        FWParametersUserCode uc = new FWParametersUserCode();
        // uc.setLibelle(StringUtils.trimString(getLibelle(), 60));
        uc.setCodeUtilisateur("-");
        uc.setLibelle(StringUtils.trimString(getLibelle(), 60));
        setCurrentCodeUtilisateur(uc);
    }

    /**
     * Sauvegarde les valeurs des propri�t�s propres de l'entit� composant la cl� primaire
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propri�t�s a �chou�e
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws java.lang.Exception {
        statement.writeKey(_getBaseTable() + "PCOSID", _dbWriteNumeric(statement.getTransaction(), getIdCode(), "ID"));
        statement.writeKey("PLAIDE", _dbWriteString(statement.getTransaction(), getSession().getIdLangue()));
    }
}
