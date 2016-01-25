package globaz.osiris.db.irrecouvrable;

import java.util.Comparator;

/**
 * Cette classe permet de comparer deux CARecouvrementKeyPosteContainer sur une partie de la clé uniquement
 * La clé de CARecouvrementKeyPosteContainer est (Integer annee, String numeroRubriqueRecouvrement, String
 * ordrePriorite)
 * la comparaison est effectuée sur (Integer annee, String numeroRubriqueRecouvrement)
 * 
 * @author sch
 * 
 */
public class CAKeyAnneeNumRubriqueComparator implements Comparator<CARecouvrementKeyPosteContainer> {

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(CARecouvrementKeyPosteContainer key1, CARecouvrementKeyPosteContainer key2)
            throws IllegalArgumentException {
        if (key1 == null || key2 == null) {
            throw new IllegalArgumentException("Parameters can not be null");
        }
        int resultCompare = key1.getAnnee().compareTo(key2.getAnnee());
        if (resultCompare != 0) {
            return resultCompare;
        } else {
            return key1.getNumeroRubriqueRecouvrement().compareTo(key2.getNumeroRubriqueRecouvrement());
        }
    }
}
