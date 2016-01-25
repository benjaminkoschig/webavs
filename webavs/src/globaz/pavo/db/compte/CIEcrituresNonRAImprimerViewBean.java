/*
 * Créé le 4 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.compte;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.pavo.print.itext.CIEcrituresNonRA_Doc;

/**
 * ViewBean permettant de générer le document qui imprime les inscriptions non connues au RA
 * 
 * @author sda
 * 
 *         Permet d'imprimer tous les assurés non connus au RA pour un journal de type déclaration de salaires ou
 *         déclarations de salaires complémentaires
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
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

    }

}
