package ch.globaz.common.util.errordriller;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Un <code>ErrorDriller</code> permet de rechercher des erreurs dans diverses sources:
 * <ul>
 * <li>BSession</li>
 * <li>ThreadContext</li>
 * <li>FWViewBeanInterface</li>
 * <li>FWListViewBeanInterface (implémente FWViewBeanInterface)</li>
 * <li>BEntity (implémente FWViewBeanInterface)</li>
 * <li>BManager (implémente FWViewBeanInterface)</li>
 * </ul>
 * 
 * L'intention première de cet objet est de pouvoir détecter les éventuelles erreurs qui pourraient empêcher les
 * opérations DB de fonctionner (en particulier les find et retrieve qui ne fonctionnent pas mais ne disent rien si on a
 * un session en erreur).
 * 
 * L'utilisation se fait en deux étapes:
 * <ol>
 * <li>ajout de sources</li>
 * <li>recherches d'erreurs dans les sources ajoutées précédemment</li>
 * </ol>
 * 
 * Quant aux performances, chaque appel à <code>drill()</code> déclenche une analyse en profondeur de toutes les sources
 * à
 * disposition. Mieux vaut donc l'utiliser avec parcimonie et travailler avec la liste d'erreurs reçue plutôt que
 * multiplier les appels à <code>drill()</code>.
 * 
 * @author vch
 * 
 */
// TODO: si la session du ThreadContext est la même qu'une autre session, supprimer l'autre session.
// TODO si deux sessions sont les mêmes en supprimer une
// TODO ajouter transaction aussi
public class ErrorDriller {
    /**
     * Classe interne permettant de stocker une erreur détectée.
     * J'espère que les noms des attributs parlent d'eux-même.
     * 
     * @author vch
     */
    public static class DrilledError {
        public final static SimpleDateFormat date2String = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        public String message;
        public String source;
        public Date detectionDate;

        DrilledError(String message, String source, Date detectionDate) {
            // constructor is package-only
            this.message = message;
            this.source = source;
            this.detectionDate = detectionDate;
        }

        @Override
        public String toString() {
            return "Error [" + message + "] found at [" + source + "] @ " + date2String.format(detectionDate);
        }
    }

    private List<ErrorSource> errorSources = new ArrayList<ErrorSource>();
    private List<Integer> errorSourceHashes = new ArrayList<Integer>(); // to prevent watching twice the same source

    /**
     * Permet d'ajouter une session à ce driller.
     * 
     * @param aSession une BSession. Ne doit jamais être <code>null</code> sinon une IllegalArgumentException sera
     *            jetée.
     * @return cet ErrorDriller
     */
    public ErrorDriller add(BSession aSession) {
        // TODO ajouter les sources des sous-objets éventuels
        if (aSession == null) {
            throw new IllegalArgumentException("ErrorDriller can't add a null session.");
        }
        errorSources.add(ErrorSourceFactory.createSourceFrom(aSession));
        return this;
    }

    /**
     * Permet d'ajouter un FWViewBeanInterface à ce driller.
     * 
     * @param aViewBean un FWViewBeanInterface. Ne doit jamais être <code>null</code> sinon une IllegalArgumentException
     *            sera jetée.
     * @return cet ErrorDriller
     */
    public ErrorDriller add(FWViewBeanInterface aViewBean) {
        // TODO ajouter les sources des sous-objets (voir s'il y a une session)
        if (aViewBean == null) {
            throw new IllegalArgumentException("ErrorDriller can't add a null FWViewBeanInterface.");
        }
        errorSources.add(ErrorSourceFactory.createSourceFrom(aViewBean));
        return this;
    }

    /**
     * Indique à ce driller de creuser dans ThreadContext aussi.
     * 
     * @return cet ErrorDriller
     */
    public ErrorDriller lookInThreadContext() {
        // TODO ajouter les sources des sous-objets
        errorSources.add(ErrorSourceFactory.createSourceFromThreadContext());
        return this;
    }

    /**
     * Construit une liste de toutes les erreurs détectées par cet ErrorDriller dans les sources précédemment ajoutées.
     * Si la liste est vide, c'est qu'il n'y a pas d'erreur.
     * 
     * @return une liste de toutes les erreurs détectées par cet ErrorDriller dans les sources précédemment ajoutées. Ne
     *         renvoie jamais <code>null</code>.
     */
    public List<DrilledError> drill() {
        List<DrilledError> result = new ArrayList<ErrorDriller.DrilledError>();
        for (ErrorSource e : errorSources) {
            result.addAll(e.drill());
        }
        return result;
    }

    private boolean isAlreadyWatched(Object aSource) {
        return errorSourceHashes.contains(System.identityHashCode(aSource));
    }

    private void rememberSource(Object aSource) {
        if (!isAlreadyWatched(aSource)) {
            errorSourceHashes.add(System.identityHashCode(aSource));
        }
    }
}
