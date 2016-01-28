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
 * <li>FWListViewBeanInterface (impl�mente FWViewBeanInterface)</li>
 * <li>BEntity (impl�mente FWViewBeanInterface)</li>
 * <li>BManager (impl�mente FWViewBeanInterface)</li>
 * </ul>
 * 
 * L'intention premi�re de cet objet est de pouvoir d�tecter les �ventuelles erreurs qui pourraient emp�cher les
 * op�rations DB de fonctionner (en particulier les find et retrieve qui ne fonctionnent pas mais ne disent rien si on a
 * un session en erreur).
 * 
 * L'utilisation se fait en deux �tapes:
 * <ol>
 * <li>ajout de sources</li>
 * <li>recherches d'erreurs dans les sources ajout�es pr�c�demment</li>
 * </ol>
 * 
 * Quant aux performances, chaque appel � <code>drill()</code> d�clenche une analyse en profondeur de toutes les sources
 * �
 * disposition. Mieux vaut donc l'utiliser avec parcimonie et travailler avec la liste d'erreurs re�ue plut�t que
 * multiplier les appels � <code>drill()</code>.
 * 
 * @author vch
 * 
 */
// TODO: si la session du ThreadContext est la m�me qu'une autre session, supprimer l'autre session.
// TODO si deux sessions sont les m�mes en supprimer une
// TODO ajouter transaction aussi
public class ErrorDriller {
    /**
     * Classe interne permettant de stocker une erreur d�tect�e.
     * J'esp�re que les noms des attributs parlent d'eux-m�me.
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
     * Permet d'ajouter une session � ce driller.
     * 
     * @param aSession une BSession. Ne doit jamais �tre <code>null</code> sinon une IllegalArgumentException sera
     *            jet�e.
     * @return cet ErrorDriller
     */
    public ErrorDriller add(BSession aSession) {
        // TODO ajouter les sources des sous-objets �ventuels
        if (aSession == null) {
            throw new IllegalArgumentException("ErrorDriller can't add a null session.");
        }
        errorSources.add(ErrorSourceFactory.createSourceFrom(aSession));
        return this;
    }

    /**
     * Permet d'ajouter un FWViewBeanInterface � ce driller.
     * 
     * @param aViewBean un FWViewBeanInterface. Ne doit jamais �tre <code>null</code> sinon une IllegalArgumentException
     *            sera jet�e.
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
     * Indique � ce driller de creuser dans ThreadContext aussi.
     * 
     * @return cet ErrorDriller
     */
    public ErrorDriller lookInThreadContext() {
        // TODO ajouter les sources des sous-objets
        errorSources.add(ErrorSourceFactory.createSourceFromThreadContext());
        return this;
    }

    /**
     * Construit une liste de toutes les erreurs d�tect�es par cet ErrorDriller dans les sources pr�c�demment ajout�es.
     * Si la liste est vide, c'est qu'il n'y a pas d'erreur.
     * 
     * @return une liste de toutes les erreurs d�tect�es par cet ErrorDriller dans les sources pr�c�demment ajout�es. Ne
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
