package globaz.musca.itext.impfactbvrutil;

/**
 * Interface décrivant le contract que doit respecter le container qui va contenir/charger les données d'impression des
 * factures BVR.<br>
 * Deux implémentations actuelles, une en mode synchrone et une en mode asynchrone.
 * 
 * @author VYJ
 */
public interface IFAImpFactDataSourceProvider {
    /**
     * @return Les données du prochain BVR à générer/imprimer
     */
    public FAImpFactDataSource getNextDataSource();

    /**
     * @param factureImpressionNo
     *            L'index courant du document généré
     * @return <code>true</code> si il reste des données à traiter et donc des BVR à générer, <code>false</code> sinon
     */
    public boolean hasNext(int factureImpressionNo);

    /**
     * @param abort
     */
    public void setAbort(boolean abort);
}
