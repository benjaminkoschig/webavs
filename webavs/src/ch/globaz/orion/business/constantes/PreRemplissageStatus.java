package ch.globaz.orion.business.constantes;

/**
 * Statut de process de pre-remplissage d'une DAN pour un affili�
 * 
 * @author sco
 * @since 3 mai 2011
 */
public class PreRemplissageStatus {

    /**
     * Il n'y a pas de compte e-business pr�sent pour faire le pr�-remplissage
     */
    public static final PreRemplissageStatus COMPTE_EBUSINESS_ABSENT = new PreRemplissageStatus(0);
    /**
     * Une erreur est survenu lors du processus de remplissage
     */
    public static final PreRemplissageStatus PRE_REMPLISSAGE_DAN_KO = new PreRemplissageStatus(2);
    /**
     * Pr�-remplissage r�ussi
     */
    public static final PreRemplissageStatus PRE_REMPLISSAGE_DAN_OK = new PreRemplissageStatus(3);
    /**
     * Une dan pr�-rempli est pr�sente
     */
    public static final PreRemplissageStatus PRE_REMPLISSAGE_DAN_PRESENTE = new PreRemplissageStatus(1);

    /**
     * Variable qui d�fini de mani�re unique le status
     */
    private int type = -1;

    /**
     * Constructeur priv�, impossibilit� d'instancier un nouveau status puisqu'il n'y en a aucun int�r�t.
     * 
     * @param _type
     *            Le type correspond au status d�fini
     */
    private PreRemplissageStatus(int _type) {
        super();
        type = _type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PreRemplissageStatus) && (((PreRemplissageStatus) obj).type == type);
    }

    /**
     * @return Le type du status d�fini - valeur num�rique
     */
    public String getType() {
        return Integer.toString(type);
    }
}
