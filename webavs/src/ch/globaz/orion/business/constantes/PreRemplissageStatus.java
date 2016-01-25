package ch.globaz.orion.business.constantes;

/**
 * Statut de process de pre-remplissage d'une DAN pour un affilié
 * 
 * @author sco
 * @since 3 mai 2011
 */
public class PreRemplissageStatus {

    /**
     * Il n'y a pas de compte e-business présent pour faire le pré-remplissage
     */
    public static final PreRemplissageStatus COMPTE_EBUSINESS_ABSENT = new PreRemplissageStatus(0);
    /**
     * Une erreur est survenu lors du processus de remplissage
     */
    public static final PreRemplissageStatus PRE_REMPLISSAGE_DAN_KO = new PreRemplissageStatus(2);
    /**
     * Pré-remplissage réussi
     */
    public static final PreRemplissageStatus PRE_REMPLISSAGE_DAN_OK = new PreRemplissageStatus(3);
    /**
     * Une dan pré-rempli est présente
     */
    public static final PreRemplissageStatus PRE_REMPLISSAGE_DAN_PRESENTE = new PreRemplissageStatus(1);

    /**
     * Variable qui défini de manière unique le status
     */
    private int type = -1;

    /**
     * Constructeur privé, impossibilité d'instancier un nouveau status puisqu'il n'y en a aucun intérêt.
     * 
     * @param _type
     *            Le type correspond au status défini
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
     * @return Le type du status défini - valeur numérique
     */
    public String getType() {
        return Integer.toString(type);
    }
}
