package globaz.itucana.process;

import globaz.globall.db.BTransaction;
import globaz.itucana.exception.TUInterfaceException;
import globaz.itucana.exception.TUModelInstanciationException;

/**
 * Classe abstraite devant �tre �tendue pour la g�n�ration d'un bouclement
 * 
 * @author fgo date de cr�ation : 13 juin 06
 * @version : version 1.0
 * 
 */
public abstract class TUProcessusBouclementAF extends TUProcessusBouclement {

    /**
     * Constructeur
     * 
     * @param _annee
     * @param _mois
     */
    public TUProcessusBouclementAF(String _annee, String _mois) {
        super(_annee, _mois);
    }

    /**
     * @param _annee
     * @param _mois
     * @param _eMail
     */
    public TUProcessusBouclementAF(String _annee, String _mois, String _eMail) {
        super(_annee, _mois, _eMail);
    }

    /**
     * Initialisation du bouclement devant �tre supprim�
     * 
     * @param periode
     * @throws TUModelInstanciationException
     */
    protected abstract void deleteBouclement(String noPassage) throws TUModelInstanciationException;

    /**
     * M�thode r�cup�rant l'impl�mentation du bouclement et ex�cutant le bouclement
     * 
     * @param transaction
     * @throws TUInterfaceException
     */
    public final void processBouclementDelete(BTransaction transaction) throws TUInterfaceException {
        // Appel de l'effacement
        deleteBouclement(getAnnee().concat(getMois()));
    }
}
