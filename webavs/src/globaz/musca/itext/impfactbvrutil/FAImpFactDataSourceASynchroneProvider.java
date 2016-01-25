package globaz.musca.itext.impfactbvrutil;

import globaz.jade.log.JadeLogger;
import java.util.LinkedList;

/**
 * Object responsable de fournir au document manager de l'impression des BVR de factures les données à utiliser pour
 * générer les documents. <br>
 * Afin de gagner en performance, le chargement des données et la génération des documents va se faire en parallèle. Cet
 * objet va donc contenir les données chargées en mémoire mais pas encore consommées par le manager de document.<br>
 * <br>
 * Vu que l'on a deux threads qui vont potentiellement accéder au container en même temps et afin d'éviter les problèmes
 * d'accès concurrents, l'accès au sous-objet de gestion de la pile se fait de manière synchrone quelque soit le besoin
 * - ajout ou consommation de données - et le thread.<br>
 * <br>
 * Le type de ce cache est <code>first in -> first out</code>.
 * 
 * @author VYJ
 */
public class FAImpFactDataSourceASynchroneProvider implements IFAImpFactDataSourceProvider {

    /**
     * Mode d'accès au sous-objet de gestion de pile en mode ajout de données
     */
    private final static int ADD_MODE = 0;
    /**
     * Mode d'accès au sous-objet de gestion de pile en mode récupération et suppression du premier objet de la pile
     */
    private final static int GET_MODE = 1;
    /**
     * Mode d'accès au sous-objet de gestion de pile en mode récupère la taille de la pile
     */
    private final static int SIZE_MODE = 4;

    /**
	 * 
	 */
    private boolean abort = false;
    /**
     * Sous-objet de gestoin de cache
     */
    private LinkedList<FAImpFactDataSource> impressionFactureDataSources = null;

    /**
     * Le nombre total d'entête de factures à traiter -> correspond au nombre de document à générer
     */
    private int nbEnteteFactures = 0;

    /**
     * @param session
     *            La session en cours
     * @param _nbEnteteFactures
     *            Le nombre d'entête de factures à traiter et charger au total
     */
    protected FAImpFactDataSourceASynchroneProvider(int _nbEnteteFactures) {
        super();
        nbEnteteFactures = _nbEnteteFactures;
        impressionFactureDataSources = new LinkedList<FAImpFactDataSource>();
    }

    /**
     * ATTENTION : Cette méthode est le seul moyen d'accéder de quelconque manière que ce soit au sous-objet qui gère le
     * container de données.<br>
     * Vu l'asynchronisme entre fournisseur et consommateur il est indispensable que l'accès soit synchronisé, d'où le
     * <code>synchronized</code>.<br>
     * <br>
     * Cette méthode permet donc d'ajouter et de consommer un datasource d'impression facture ainsi que de connaître la
     * taille du cache.
     * 
     * @param mode
     *            Le type d'accès
     * @param impressionFactureDataSource
     *            Si en mode ajout -> l'objet à ajouter
     * @return Si en mode consommation, retourne la première instance du cache de type
     *         <code>FAImpressionFactureDataSource</code> et l'enlève du cache - Si en mode taille, retourne une
     *         instance de <code>Integer</code> contenant la taille du cache.
     */
    private synchronized Object doOperationOnList(int mode, FAImpFactDataSource impressionFactureDataSource) {
        Object returnedObject = null;
        switch (mode) {
            case ADD_MODE:
                impressionFactureDataSources.addLast(impressionFactureDataSource);
                break;
            case GET_MODE:
                returnedObject = impressionFactureDataSources.getFirst();
                impressionFactureDataSources.removeFirst();
                break;
            case SIZE_MODE:
                returnedObject = new Integer(impressionFactureDataSources.size());
                break;
        }
        return returnedObject;
    }

    /**
     * @return Le nombre d'objet de type <code>FAImpressionFactureDataSource</code> contenu actuellement dans le cache
     */
    protected int getCurrentCacheSize() {
        return ((Integer) doOperationOnList(FAImpFactDataSourceASynchroneProvider.SIZE_MODE, null)).intValue();
    }

    /**
     * @return Le prochaine objet à traiter, si aucun objet n'est à actuellement en cache, attend qu'il y ait un objet à
     *         consommer.<br>
     *         Donc avant d'appeler cette méthode, contrôler qu'il y ait encore des objets à consommer via la méthode
     *         <code>hasNext</code>
     */
    @Override
    public FAImpFactDataSource getNextDataSource() {
        while ((getCurrentCacheSize() == 0) && !abort) {
            try {
                Thread.sleep(FAImpFactPropertiesProvider.NEXT_DATASOURCE_WAITING_TIME);
                if (FAImpFactPropertiesProvider.IS_DEBUG_MODE) {
                    JadeLogger.info(this, "nextDatasource - empty stack, waiting "
                            + FAImpFactPropertiesProvider.NEXT_DATASOURCE_WAITING_TIME + "mls");
                }
            } catch (InterruptedException e) {
                JadeLogger.warn(this, "Problem during thread sleeping - reason : " + e.getClass().getName()
                        + " - Message : " + e.getMessage());
            }
        }
        return (FAImpFactDataSource) doOperationOnList(FAImpFactDataSourceASynchroneProvider.GET_MODE, null);
    }

    /**
     * Permet de savoir si il y a encore des factures à traiter
     * 
     * @param factureImpressionNo
     *            Le nombre déjà traitées par le consommateur
     * @return <code>true</code> si il y a encore des factures à traiter, <code>false</code> sinon
     */
    @Override
    public boolean hasNext(int factureImpressionNo) {
        return factureImpressionNo < nbEnteteFactures;
    }

    public boolean hasToAbort() {
        return abort;
    }

    /**
     * @param abort
     */
    @Override
    public void setAbort(boolean abort) {
        this.abort = abort;
    }

    /**
     * @param impressionFactureDataSource
     */
    protected void setImpressionFactureDataSource(FAImpFactDataSource impressionFactureDataSource) {
        doOperationOnList(FAImpFactDataSourceASynchroneProvider.ADD_MODE, impressionFactureDataSource);
    }
}
