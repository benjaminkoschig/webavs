package ch.globaz.al.businessimpl.calcul.types;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;

import ch.globaz.al.business.exceptions.calcul.ALCalculModeException;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;
import ch.globaz.al.businessimpl.calcul.modes.CalculMode;

/**
 * Classe mère de tous les types de calcul. Elle fournit l'implémentation "de base" de la méthode <code>compute</code>
 * qui va lancer l'exécution du calcul selon le mode passé en paramètre
 * 
 * @author jts
 * 
 */
public abstract class CalculTypeAbstract implements CalculType {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.calcul.types.CalculType#compute(ch.globaz.al
     * .business.models.dossier.DossierComplexModelAbstract, ch.globaz.al.businessimpl.calcul.modes.CalculMode,
     * java.lang.String)
     */
    @Override
    public List<CalculBusinessModel> compute(ContextCalcul context, CalculMode calcMode)
            throws JadeApplicationException, JadePersistenceException {

        if (context == null) {
            throw new ALCalculModeException("CalculTypeAbstract#compute : context is null");
        }

        if (calcMode == null) {
            throw new ALCalculModeException("CalculTypeAbstract#compute : calcMode is null");
        }

        // exécution et retour du calcul
        return calcMode.compute(context);
    }
}