package globaz.musca.itext.impfactbvrutil;

import globaz.jade.log.JadeLogger;
import java.util.LinkedList;

/**
 * Object responsable de fournir au document manager de l'impression des BVR de factures les donn�es � utiliser pour
 * g�n�rer les documents. <br>
 * Afin de gagner en performance, le chargement des donn�es et la g�n�ration des documents va se faire en parall�le. Cet
 * objet va donc contenir les donn�es charg�es en m�moire mais pas encore consomm�es par le manager de document.<br>
 * <br>
 * Vu que l'on a deux threads qui vont potentiellement acc�der au container en m�me temps et afin d'�viter les probl�mes
 * d'acc�s concurrents, l'acc�s au sous-objet de gestion de la pile se fait de mani�re synchrone quelque soit le besoin
 * - ajout ou consommation de donn�es - et le thread.<br>
 * <br>
 * Le type de ce cache est <code>first in -> first out</code>.
 * 
 * @author VYJ
 */
public class FAImpFactDataSourceASynchroneProvider implements IFAImpFactDataSourceProvider {

    /**
     * Mode d'acc�s au sous-objet de gestion de pile en mode ajout de donn�es
     */
    private final static int ADD_MODE = 0;
    /**
     * Mode d'acc�s au sous-objet de gestion de pile en mode r�cup�ration et suppression du premier objet de la pile
     */
    private final static int GET_MODE = 1;
    /**
     * Mode d'acc�s au sous-objet de gestion de pile en mode r�cup�re la taille de la pile
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
     * Le nombre total d'ent�te de factures � traiter -> correspond au nombre de document � g�n�rer
     */
    private int nbEnteteFactures = 0;

    /**
     * @param session
     *            La session en cours
     * @param _nbEnteteFactures
     *            Le nombre d'ent�te de factures � traiter et charger au total
     */
    protected FAImpFactDataSourceASynchroneProvider(int _nbEnteteFactures) {
        super();
        nbEnteteFactures = _nbEnteteFactures;
        impressionFactureDataSources = new LinkedList<FAImpFactDataSource>();
    }

    /**
     * ATTENTION : Cette m�thode est le seul moyen d'acc�der de quelconque mani�re que ce soit au sous-objet qui g�re le
     * container de donn�es.<br>
     * Vu l'asynchronisme entre fournisseur et consommateur il est indispensable que l'acc�s soit synchronis�, d'o� le
     * <code>synchronized</code>.<br>
     * <br>
     * Cette m�thode permet donc d'ajouter et de consommer un datasource d'impression facture ainsi que de conna�tre la
     * taille du cache.
     * 
     * @param mode
     *            Le type d'acc�s
     * @param impressionFactureDataSource
     *            Si en mode ajout -> l'objet � ajouter
     * @return Si en mode consommation, retourne la premi�re instance du cache de type
     *         <code>FAImpressionFactureDataSource</code> et l'enl�ve du cache - Si en mode taille, retourne une
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
     * @return Le prochaine objet � traiter, si aucun objet n'est � actuellement en cache, attend qu'il y ait un objet �
     *         consommer.<br>
     *         Donc avant d'appeler cette m�thode, contr�ler qu'il y ait encore des objets � consommer via la m�thode
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
     * Permet de savoir si il y a encore des factures � traiter
     * 
     * @param factureImpressionNo
     *            Le nombre d�j� trait�es par le consommateur
     * @return <code>true</code> si il y a encore des factures � traiter, <code>false</code> sinon
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
