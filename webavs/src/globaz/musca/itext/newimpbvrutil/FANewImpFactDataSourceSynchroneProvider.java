package globaz.musca.itext.newimpbvrutil;

import globaz.globall.db.BSession;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAPassage;
import java.util.ArrayList;

/**
 * Cet objet est cr�� afin de pouvoir conserver une mani�re synchrone de charger les donn�es. La m�thode
 * <code>getNextDataSource</code> permet de charger les donn�es li�es � la prochaine ent�te pour autant qu'il y ait
 * encore une ent�te � traiter et ce de mani�re synchrone, donc elles sont charg�es sur l'appel de la m�thode
 * contrairement � l'implementation <code>multi threading</code>.
 * 
 * @author VYJ
 */
public class FANewImpFactDataSourceSynchroneProvider implements IFANewImpFactDataSourceProvider {
    protected static void init(FAApplication application) {
    }

    /**
     * Les ent�tes de factures � traiter
     */
    private ArrayList<?> enteteFactures = null;
    /**
     * Permet de savoir quel ent�te de facture est en cours de traitement -> pour savoir quelle est la prochaine �
     * traiter
     */
    private int index = 0;
    /**
     * Le passage trait�
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
     *            Les ent�tes de factures sur lesquelles se baser pour g�n�rer les documents et donc charger les donn�es
     * @param passage
     *            Le passage en cours de traitement
     */
    public FANewImpFactDataSourceSynchroneProvider(BSession session, ArrayList<?> enteteFactures, FAPassage passage) {
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
    public FANewImpFactDataSource getNextDataSource() {
        return FANewImpFactDataSource.init(session, passage, (FAEnteteFacture) enteteFactures.get(index++));
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
