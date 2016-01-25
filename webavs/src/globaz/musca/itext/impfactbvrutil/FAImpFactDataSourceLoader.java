package globaz.musca.itext.impfactbvrutil;

import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAPassage;
import java.util.ArrayList;

/**
 * Object permettant de charger les datas n�cessaire � l'impression des factures BVR.
 * 
 * @author VYJ
 */
public class FAImpFactDataSourceLoader implements Runnable {
    /**
     * Les ent�tes de factures � charger
     */
    private ArrayList<?> entetesFactures = null;
    /**
     * Container partag� qui va stocker les donn�es pour les factures � imprimer
     */
    private FAImpFactDataSourceASynchroneProvider impressionFactureProvider = null;
    /**
     * Le passage � traiter
     */
    private FAPassage passage = null;
    /**
     * La session � utiliser pour charger les datas
     */
    private BSession session = null;

    /**
     * @param session
     *            La session en cours
     * @param _impressionFactureProvider
     *            Le container des donn�es de factures � imprimer qui sera � remplir
     * @param entetesFactures
     *            Les ent�tes de factures dont on doit charger les donn�es
     * @param passage
     *            Le passage que l'on va traiter
     */
    protected FAImpFactDataSourceLoader(BSession session,
            FAImpFactDataSourceASynchroneProvider _impressionFactureProvider, ArrayList<?> entetesFactures,
            FAPassage passage) {
        super();
        this.session = session;
        impressionFactureProvider = _impressionFactureProvider;
        this.entetesFactures = entetesFactures;
        this.passage = passage;
    }

    private boolean hasToAbort() {
        return impressionFactureProvider.hasToAbort();
    }

    /**
     * Permet de charger l'ensemble des datas, charge en parall�le de la g�n�ration, au fur et � mesure que le
     * consommateurs prend les datas, cet objet les charges. Le container fait office de pile, cette m�thode s'arr�te de
     * charger � partir d'une certaine taille de la pile et attend que la pile descende histoire de ne pas trop charger
     * la m�moire.
     * 
     * @throws Exception
     */
    private void process() {
        // On charge une map avec comme index les num�ros de caisses par rapport aux idRubrique
        for (int i = 0; (i < entetesFactures.size()) && !hasToAbort(); i++) {
            // Load les donn�es et va stocker ces donn�es dans le provider de
            // datas.
            impressionFactureProvider.setImpressionFactureDataSource(FAImpFactDataSource.init(session, passage,
                    (FAEnteteFacture) entetesFactures.get(i)));
            // Regarde si le cache du provider est plein, si oui, alors attend
            // que le consommateurs en consomme.
            while ((impressionFactureProvider.getCurrentCacheSize() > FAImpFactPropertiesProvider.MAX_STACK_SIZE)
                    && !hasToAbort()) {
                try {
                    Thread.sleep(FAImpFactPropertiesProvider.STACK_FULL_WAITING_TIME);
                    if (FAImpFactPropertiesProvider.IS_DEBUG_MODE) {
                        JadeLogger.info(this, "Datasource loader - finished waiting - current cache size : "
                                + impressionFactureProvider.getCurrentCacheSize());
                    }
                } catch (InterruptedException e) {
                    JadeLogger.warn(this, "problem with thread sleeping, reason : " + e.getClass().getName() + " - "
                            + e.getMessage());
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        process();
    }
}
