package globaz.musca.itext.impfactbvrutil;

import globaz.globall.db.BSession;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAPassage;
import java.util.ArrayList;

/**
 * Permet de fournir en fonction des propri?t?s applicatives le bon container - soit une r?f?rence sur celui qui permet
 * de charger de mani?re asynchrone les donn?es, soit directement un "container" qui fourni en mode synchrone les
 * donn?es via le loader
 * 
 * @author VYJ
 */
public abstract class FAImpFactDataSourceProviderFactory {
    /**
     * Fourni le bon container en fonction des propri?t?s applicatives
     * 
     * @param application
     *            L'application FAApplication qui contient les propri?t?s
     * @param session
     *            La session courante pour pouvoir la d?finir au container et par la suite au loader pour charger les
     *            donn?es
     * @param passage
     *            Le passage en cours de traitement
     * @param enteteFactures
     *            Les ent?tes de factures ? traiter
     * @return Une instance de container qui permette d'acc?der aux donn?es charg?es soit en mode synchrone soit en mode
     *         asynchrone
     */
    public static IFAImpFactDataSourceProvider newInstance(FAApplication application, BSession session,
            FAPassage passage, ArrayList<?> enteteFactures) {
        FAImpFactPropertiesProvider.init(application);
        IFAImpFactDataSourceProvider provider = null;
        // Si on est en mode asynchrone, alors utilise le multi threading
        // container
        if (FAImpFactPropertiesProvider.IS_MODE_ASYNCHRONE) {
            provider = new FAImpFactDataSourceASynchroneProvider(enteteFactures.size());
            // Lance le processus de chargement
            new Thread(new FAImpFactDataSourceLoader(session, (FAImpFactDataSourceASynchroneProvider) provider,
                    enteteFactures, passage)).start();
        } else {
            provider = new FAImpFactDataSourceSynchroneProvider(session, enteteFactures, passage);
        }
        // retourne l'instance du container
        return provider;
    }
}
