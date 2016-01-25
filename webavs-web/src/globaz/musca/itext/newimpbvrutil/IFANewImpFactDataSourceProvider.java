package globaz.musca.itext.newimpbvrutil;

/**
 * Interface d�crivant le contract que doit respecter le container qui va contenir/charger les donn�es d'impression des
 * factures BVR.<br>
 * Deux impl�mentations actuelles, une en mode synchrone et une en mode asynchrone.
 * 
 * @author VYJ
 */
public interface IFANewImpFactDataSourceProvider {
    /**
     * @return Les donn�es du prochain BVR � g�n�rer/imprimer
     */
    public FANewImpFactDataSource getNextDataSource();

    /**
     * @param factureImpressionNo
     *            L'index courant du document g�n�r�
     * @return <code>true</code> si il reste des donn�es � traiter et donc des BVR � g�n�rer, <code>false</code> sinon
     */
    public boolean hasNext(int factureImpressionNo);

    /**
     * @param abort
     */
    public void setAbort(boolean abort);
}
