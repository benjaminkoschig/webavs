package globaz.corvus.vb.demandes;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Comparator;

/**
 * Comparateur permettant de trier la liste des {@link REDemandeRenteJointPrestationAccordeeViewBeanComparator}<br/>
 * de la même manière que la requête SQL le faisait (les données étant dans le désordre dû au traitement du list view
 * bean)
 * 
 * @author PBA
 * @see REDemandeRenteJointPrestationAccordeeListViewBean#afterFind()
 */
public class REDemandeRenteJointPrestationAccordeeViewBeanComparator implements
        Comparator<REDemandeRenteJointPrestationAccordeeViewBean> {

    /**
     * Défini quel type de comparaison doit être faite sur la liste à trier
     * 
     * @author PBA
     * 
     */
    public static enum TypeComparaison {
        Alphabetique,
        DateReception,
        Etat
    }

    /**
     * Type de comparaison à lancer lors du tri de la liste
     * 
     * @see TypeComparaison
     */
    private TypeComparaison typeComparaison;

    public REDemandeRenteJointPrestationAccordeeViewBeanComparator(TypeComparaison typeComparaison) {
        this.typeComparaison = typeComparaison;
    }

    /**
     * {@inheritDoc}<br/>
     * <br/>
     * Implementation de l'interface Comparator permettant de trier une liste<br/>
     * Lance la comparaison correspondant à l'attribut {@link #typeComparaison}
     */
    @Override
    public int compare(REDemandeRenteJointPrestationAccordeeViewBean vb1,
            REDemandeRenteJointPrestationAccordeeViewBean vb2) {
        switch (typeComparaison) {
            case Alphabetique:
                return compareAlphabetique(vb1, vb2);
            case DateReception:
                return compareDateReception(vb1, vb2);
            case Etat:
                return compareEtat(vb1, vb2);
            default:
                return compareAlphabetique(vb1, vb2);
        }
    }

    /**
     * Comparaison sur le nom et le prénom
     * 
     * @param vb1
     * @param vb2
     * @return 1 si vb1 a un nom (ou un prénom si le nom est identique) plus éloigné dans l'alphabète que vb2<br/>
     *         -1 si vb1 a un nom (ou un prénom si le nom est identique) moins éloigné dans l'alphabète que vb2<br/>
     *         retourne le résultat de
     *         {@link #compareDateDroit(REDemandeRenteJointPrestationAccordeeViewBean, REDemandeRenteJointPrestationAccordeeViewBean)
     *         compareDateDroit(vb1,vb2)} si les noms et les prénoms sont identiques
     */
    int compareAlphabetique(REDemandeRenteJointPrestationAccordeeViewBean vb1,
            REDemandeRenteJointPrestationAccordeeViewBean vb2) {
        int comparaisonNom = vb1.getNom().compareTo(vb2.getNom());
        if (comparaisonNom == 0) {
            int comparaisonPrenom = vb1.getPrenom().compareTo(vb2.getPrenom());

            if (comparaisonPrenom == 0) {
                return compareDateDroit(vb1, vb2);
            }
            // pour ramener le résultat à 1 ou -1
            return comparaisonPrenom / Math.abs(comparaisonPrenom);
        }
        // pour ramener le résultat à 1 ou -1
        return comparaisonNom / Math.abs(comparaisonNom);
    }

    /**
     * Compare 2 dates de type JJ.MM.AAAA
     * 
     * @param date1
     * @param date2
     * @return Retourne 1 si date1 et plus ancienne que date2<br/>
     *         0 si les dates sont les mêmes<br/>
     *         -1 si date1 est plus jeune que date2<br/>
     *         Si une des deux dates est vide (chaîne vide) ou invalide, retourne 0
     */
    int compareDate(String date1, String date2) {

        if (JadeStringUtil.isBlank(date1) || JadeStringUtil.isBlank(date2)) {
            return 0;
        }

        if (JadeDateUtil.isDateBefore(date1, date2)) {
            return 1;
        } else if (JadeDateUtil.isDateAfter(date1, date2)) {
            return -1;
        }
        return 0;
    }

    /**
     * Comparaison sur la période de droit<br/>
     * utilise {@link #compareDate(String, String)}
     * 
     * @param vb1
     * @param vb2
     * @return 1 si la date de fin de droit de vb1 (ou de début si les dates de fin de droit sont identiques) est plus
     *         ancienne que celle de vb2<br/>
     *         -1 si la date de fin de droit de vb1 (ou de début si les dates de fin de droit sont identiques) est plus
     *         jeune que celle de vb2<br/>
     *         0 si les dates de début et de fin de droit sont identiques
     * @see #compareDate(String, String)
     */
    int compareDateDroit(REDemandeRenteJointPrestationAccordeeViewBean vb1,
            REDemandeRenteJointPrestationAccordeeViewBean vb2) {
        int compareDateFinDroit = compareDate(formatDate(vb1.getDateFin()), formatDate(vb2.getDateFin()));

        if (compareDateFinDroit == 0) {
            return compareDate(formatDate(vb1.getDateDebut()), formatDate(vb2.getDateDebut()));
        }
        return compareDateFinDroit;
    }

    /**
     * Compare les date de réception des demandes
     * 
     * @param vb1
     * @param vb2
     * @return 1 si la date de réception de vb1 est plus ancienne que celle de vb2<br/>
     *         -1 si la date de réception de vb1 est plus jeune que celle de vb2<br/>
     *         retourne le résultat de
     *         {@link #compareAlphabetique(REDemandeRenteJointPrestationAccordeeViewBean, REDemandeRenteJointPrestationAccordeeViewBean)
     *         compareAlphabetique(vb1,vb2)} si les dates de réception sont identiques
     */
    int compareDateReception(REDemandeRenteJointPrestationAccordeeViewBean vb1,
            REDemandeRenteJointPrestationAccordeeViewBean vb2) {
        int compareDateReception = compareDate(vb1.getDateReception(), vb2.getDateReception());

        if (compareDateReception == 0) {
            int compareDateDroit = compareDateDroit(vb1, vb2);

            if (compareDateDroit == 0) {
                return compareAlphabetique(vb1, vb2);
            }
            return compareDateDroit;
        }
        return compareDateReception;
    }

    /**
     * Compare le code système de l'état de la demande des deux entités
     * 
     * @param vb1
     * @param vb2
     * @return 1 si le code système de l'état de la demande de vb1 est plus grand que celui de vb2<br/>
     *         -1 si le code système de l'état de la demande de vb1 est plus petit que celui de vb2</br> retourne le
     *         résultat de
     *         {@link #compareAlphabetique(REDemandeRenteJointPrestationAccordeeViewBean, REDemandeRenteJointPrestationAccordeeViewBean)
     *         compareAlphabetique(vb1,vb2)} si les deux codes système sont identiques
     */
    int compareEtat(REDemandeRenteJointPrestationAccordeeViewBean vb1, REDemandeRenteJointPrestationAccordeeViewBean vb2) {
        Integer etatDemande1 = Integer.parseInt(vb1.getCsEtatDemande());
        Integer etatDemande2 = Integer.parseInt(vb2.getCsEtatDemande());

        if (etatDemande1.intValue() == etatDemande2.intValue()) {
            return compareAlphabetique(vb1, vb2);
        }

        return etatDemande1.compareTo(etatDemande2);
    }

    /**
     * Format la date en entrée pour une utilisation dans {@link #compareDate(String, String)}
     * 
     * @param date
     * @return la date formatée, si valide. Sinon retourne la date du 31.12.2099
     */
    String formatDate(String date) {
        if (JadeDateUtil.isGlobazDate(date)) {
            return date;
        }

        if (!JadeStringUtil.isBlank(date) && JadeDateUtil.isGlobazDateMonthYear(date)) {
            String[] splitedDate = date.split("[.]");

            if (splitedDate[1].matches("[1-2][0-9][0-9][0-9]")) {
                if (splitedDate[0].matches("[0][1-9]||[1][1-9]")) {
                    return "01." + date;
                }
                return "01.0" + date;
            }
        }
        return "31.12.2099";
    }

    public TypeComparaison getTypeComparaison() {
        return typeComparaison;
    }

    public void setTypeComparaison(TypeComparaison typeComparaison) {
        this.typeComparaison = typeComparaison;
    }
}