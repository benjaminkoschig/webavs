package globaz.musca.itext.newimpbvrutil;

import globaz.globall.db.BSession;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAPassage;
import java.util.ArrayList;

/**
 * Permet de fournir en fonction des propriétés applicatives le bon container - soit une référence sur celui qui permet
 * de charger de manière asynchrone les données, soit directement un "container" qui fourni en mode synchrone les
 * données via le loader
 * 
 * @author VYJ
 */
public abstract class FANewImpFactDataSourceProviderFactory {
    /**
     * Fourni le bon container en fonction des propriétés applicatives
     * 
     * @param application
     *            L'application FAApplication qui contient les propriétés
     * @param session
     *            La session courante pour pouvoir la définir au container et par la suite au loader pour charger les
     *            données
     * @param passage
     *            Le passage en cours de traitement
     * @param enteteFactures
     *            Les entêtes de factures à traiter
     * @return Une instance de container qui permette d'accéder aux données chargées soit en mode synchrone soit en mode
     *         asynchrone
     */
    public static IFANewImpFactDataSourceProvider newInstance(FAApplication application, BSession session,
            FAPassage passage, ArrayList<?> enteteFactures) {
        FANewImpFactPropertiesProvider.init(application);
        IFANewImpFactDataSourceProvider provider = null;
        // Si on est en mode asynchrone, alors utilise le multi threading
        // container
        if (FANewImpFactPropertiesProvider.IS_MODE_ASYNCHRONE) {
            provider = new FANewImpFactDataSourceASynchroneProvider(enteteFactures.size());
            // Lance le processus de chargement
            new Thread(new FANewImpFactDataSourceLoader(session, (FANewImpFactDataSourceASynchroneProvider) provider,
                    enteteFactures, passage)).start();
        } else {
            provider = new FANewImpFactDataSourceSynchroneProvider(session, enteteFactures, passage);
        }
        // retourne l'instance du container
        return provider;
    }
}
