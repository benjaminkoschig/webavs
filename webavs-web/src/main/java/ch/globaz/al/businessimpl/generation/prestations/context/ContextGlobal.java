package ch.globaz.al.businessimpl.generation.prestations.context;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.persistence.model.JadeAbstractModel;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationPrestationsContextException;

/**
 * Contexte utilisé pendant la génération globale. Il permet de stocker la liste des dossiers à traiter pour un affilié
 * ainsi que le contexte de l'affilié concerné
 * 
 * @author jts
 */
public class ContextGlobal {
    /**
     * Retourne une nouvelle instance de <code>GlobalContext</code>
     * 
     * @param dossiers
     *            liste des dossiers à traiter
     * @param context
     *            Contexte d'affilié
     * @return Instance du contexte
     * @throws JadeApplicationException
     *             exception levée si l'un des paramètres n'est pas valide
     */
    public static ContextGlobal getInstance(JadeAbstractModel[] dossiers, ContextAffilie context)
            throws JadeApplicationException {

        if (dossiers == null) {
            throw new ALGenerationPrestationsContextException("ContextGlobal#getInstance : dossiers is null");
        }

        if (context == null) {
            throw new ALGenerationPrestationsContextException("ContextGlobal#getInstance : context is null");
        }

        ContextGlobal c = new ContextGlobal();

        c.context = context;
        c.dossiers = dossiers;
        return c;
    }

    /**
     * Contexte de l'affilié
     */
    private ContextAffilie context = null;

    /**
     * Liste des dossiers à traiter
     */
    private JadeAbstractModel[] dossiers = null;

    /**
     * Constructeur privé. Utiliser <code>getInstance</code> pour obtenir une nouvelle instance
     * 
     * @see ContextGlobal#getInstance(JadeAbstractModel[], ContextAffilie)
     */
    private ContextGlobal() {
        // Utiliser la méthode getInstance
    }

    /**
     * Retourne le nombre de dossiers contenus dans le contexte
     * 
     * @return nombre de dossiers
     */
    public int countDossiers() {
        return dossiers.length;
    }

    /**
     * @return le contexte de l'affilié
     */
    public ContextAffilie getContextAffilie() {
        return context;
    }

    /**
     * @return la liste des dossiers
     */
    public JadeAbstractModel[] getDossiers() {
        return dossiers;
    }
}
