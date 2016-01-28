package globaz.musca.itext.impfactbvrutil;

import globaz.globall.db.BSession;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAPassage;
import java.util.ArrayList;

/**
 * Cet objet est créé afin de pouvoir conserver une manière synchrone de charger les données. La méthode
 * <code>getNextDataSource</code> permet de charger les données liées à la prochaine entête pour autant qu'il y ait
 * encore une entête à traiter et ce de manière synchrone, donc elles sont chargées sur l'appel de la méthode
 * contrairement à l'implementation <code>multi threading</code>.
 * 
 * @author VYJ
 */
public class FAImpFactDataSourceSynchroneProvider implements IFAImpFactDataSourceProvider {
    protected static void init(FAApplication application) {
    }

    /**
     * Les entêtes de factures à traiter
     */
    private ArrayList<?> enteteFactures = null;
    /**
     * Permet de savoir quel entête de facture est en cours de traitement -> pour savoir quelle est la prochaine à
     * traiter
     */
    private int index = 0;
    /**
     * Le passage traité
     */
    private FAPassage passage = null;

    /**
     * La session courante
     */
    private BSession session = null;

    /**
     * @param session
     *            La session courante
     * @param enteteFactures
     *            Les entêtes de factures sur lesquelles se baser pour générer les documents et donc charger les données
     * @param passage
     *            Le passage en cours de traitement
     */
    public FAImpFactDataSourceSynchroneProvider(BSession session, ArrayList<?> enteteFactures, FAPassage passage) {
        super();
        this.session = session;
        this.enteteFactures = enteteFactures;
        this.passage = passage;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.musca.itext.util.IFAImpressionFactureDataSourceContainer# getNextDataSource()
     */
    @Override
    public FAImpFactDataSource getNextDataSource() {
        return FAImpFactDataSource.init(session, passage, (FAEnteteFacture) enteteFactures.get(index++));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.itext.util.IFAImpressionFactureDataSourceContainer#hasNext (int)
     */
    @Override
    public boolean hasNext(int factureImpressionNo) {
        return factureImpressionNo < enteteFactures.size();
    }

    /**
     * @param abort
     */
    @Override
    public void setAbort(boolean abort) {
    }
}
