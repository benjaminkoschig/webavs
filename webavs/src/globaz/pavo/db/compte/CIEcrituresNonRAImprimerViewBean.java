/*
 * Cr�� le 4 mai 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.compte;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.pavo.print.itext.CIEcrituresNonRA_Doc;

/**
 * ViewBean permettant de g�n�rer le document qui imprime les inscriptions non connues au RA
 * 
 * @author sda
 * 
 *         Permet d'imprimer tous les assur�s non connus au RA pour un journal de type d�claration de salaires ou
 *         d�clarations de salaires compl�mentaires
 */
public class CIEcrituresNonRAImprimerViewBean extends CIEcrituresNonRA_Doc {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CIEcrituresNonRAImprimerViewBean() throws Exception {

    }

    /**
     * Permet l'impression des inscriptions non connues au RA
     * 
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */

    public CIEcrituresNonRAImprimerViewBean(globaz.globall.db.BSession session) throws Exception {
        super(session);
    }

    /**
     * Valide le contenu de l'entit� (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

    }

}
