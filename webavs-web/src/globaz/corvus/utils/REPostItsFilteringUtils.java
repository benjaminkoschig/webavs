package globaz.corvus.utils;

import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.vb.demandes.REDemandeRenteJointPrestationAccordeeViewBean;
import globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteViewBean;
import globaz.corvus.vb.rentesaccordees.RERenteLieeJointRenteAccordeeViewBean;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import java.util.ArrayList;
import java.util.List;

/**
 * Compilation de m�thodes utilitaires permettant de masquer les post-it's disponibles sur un ensemble de demandes de
 * rente, rentes accord�es et rentes li�es.
 * Ce filtrage ne conserve, pour chaque tiers, que la demande/rente la plus tardive.
 * 
 * Ajout�e dans le cadre du mandat IR398
 * 
 * @author ame
 */
public class REPostItsFilteringUtils {

    /**
     * Structure de donn�es faisant correspondre un ID et une date, permettant de g�n�raliser le traitement d'extraction
     * d'une rente/demande la plus tardive
     * 
     * @author ame
     */
    public static class Element {
        private String id;
        private String date;

        public Element(String id, String date) {
            this.id = id;
            this.date = date;
        }

        /**
         * @return Date utilis�e comme crit�re de tri
         */
        public String getDate() {
            return date;
        }

        /**
         * @return Clef unique (ID demande si demande, ID prestation accord�e si rente) correspondant � la date
         */
        public String getId() {
            return id;
        }

        /**
         * Retourne l'ID dont la date est la plus tardive, parmis une liste d'�l�ments sp�cifi�e
         * 
         * @param allElements Tous les �l�ments consid�r�s
         * @return L'ID de l'�l�ment dont la date est la plus tardive
         */
        public static String extractLastItemId(List<Element> allElements) {
            String latestId = null;
            String latestDate = null;

            try {
                for (Element element : allElements) {
                    String loopDate = new JADate(element.getDate()).toStrAMJ();

                    if (latestDate == null || loopDate.compareTo(latestDate) > 0) {
                        latestDate = loopDate;
                        latestId = element.getId();
                    }
                }
            } catch (JAException exception) {
                throw new RETechnicalException(exception);
            }

            return latestId;
        }
    }

    /**
     * Toutes les m�thodes de cette classe sont statiques, cette classe ne doit pas �tre instanci�e
     */
    private REPostItsFilteringUtils() {
    }

    /**
     * Ne conserve que les post-it's pour la demande la plus tardive pour chaque tiers requ�rant
     * 
     * @param allDemandes Toutes les demandes consid�r�es
     */
    public static void keepPostItsForLastDemandeOnly(Iterable<REDemandeRenteJointPrestationAccordeeViewBean> allDemandes) {

        List<String> allIdTiersRequerant = extractIdTiersWithPostitDemande(allDemandes);

        for (String idTiers : allIdTiersRequerant) {
            List<REDemandeRenteJointPrestationAccordeeViewBean> demandesTiers = filterDemandes(allDemandes, idTiers);

            String idLastDemande = extractLatestDemande(demandesTiers);

            clearPostItsDemandes(demandesTiers, idLastDemande);
        }
    }

    /**
     * Ne conserve que les post-its pour la rente la plus tardive pour chaque tiers b�n�ficiaire
     * 
     * @param allRentesLiees La liste des rentes li�es consid�r�es
     */
    public static void keepPostItsForLastRenteLieeOnly(Iterable<RERenteLieeJointRenteAccordeeViewBean> allRentesLiees) {

        List<String> allIdTiersBeneficiaire = extractIdTiersWithPostItRenteLiee(allRentesLiees);

        for (String idTiers : allIdTiersBeneficiaire) {
            List<RERenteLieeJointRenteAccordeeViewBean> rentesLieesTiers = filterRentesLiees(allRentesLiees, idTiers);

            String idLastPrestationAccordee = extractIdLastRenteLiee(rentesLieesTiers);

            clearPostItsRentesLiees(rentesLieesTiers, idLastPrestationAccordee);
        }
    }

    /**
     * Ne conserve que les post-its pour la rente la plus tardive pour chaque tiers b�n�ficiaire
     * 
     * @param allRentesAccordees Toutes les rentes accord�es affich�es
     */
    public static void keepPostItsForLastRenteAccordeeOnly(
            Iterable<RERenteAccordeeJointDemandeRenteViewBean> allRentesAccordees) throws Exception {

        List<String> allIdTiersBeneficiaire = extractIdTiersWithPostItRenteAccordee(allRentesAccordees);

        for (String idTiers : allIdTiersBeneficiaire) {
            List<RERenteAccordeeJointDemandeRenteViewBean> rentesAccordeesTiers = filterRentesAccordees(
                    allRentesAccordees, idTiers);

            String idLastPrestationAccordee = extractIdLastRenteAccordee(rentesAccordeesTiers);

            clearPostItsRentesAccordees(rentesAccordeesTiers, idLastPrestationAccordee);
        }
    }

    /**
     * Retourne les tiers dont une rente accord�e poss�de au moins un post-it
     * 
     * @param allRentesAccordees La liste des rentes accord�es � traiter
     * @return La liste des ID tiers
     */
    private static List<String> extractIdTiersWithPostItRenteAccordee(
            final Iterable<RERenteAccordeeJointDemandeRenteViewBean> allRentesAccordees) {
        List<String> buffer = new ArrayList<String>();

        for (RERenteAccordeeJointDemandeRenteViewBean vb : allRentesAccordees) {
            String idTiersBeneficiaire = vb.getIdTiersBeneficiaire();

            if (vb.hasPostit() && buffer.contains(idTiersBeneficiaire) == false) {
                buffer.add(idTiersBeneficiaire);
            }
        }
        return buffer;
    }

    /**
     * Retourne la liste des tiers dont une demande poss�de au moins un post-it
     * 
     * @param allDemandes La liste des demandes � traiter
     * @return La liste des ID tiers
     */
    private static List<String> extractIdTiersWithPostitDemande(
            Iterable<REDemandeRenteJointPrestationAccordeeViewBean> allDemandes) {
        List<String> output = new ArrayList<String>();

        for (REDemandeRenteJointPrestationAccordeeViewBean demande : allDemandes) {
            String idTiersRequerant = demande.getIdTiersRequerant();

            if (demande.hasPostIt() && output.contains(idTiersRequerant) == false) {
                output.add(idTiersRequerant);
            }
        }

        return output;
    }

    /**
     * Retourne la liste des tiers dont une rente li�e poss�de au moins un post-it
     * 
     * @param allRentesLiees La liste des rentes li�es � traiter
     * @return La liste des ID tiers
     */
    private static List<String> extractIdTiersWithPostItRenteLiee(
            Iterable<RERenteLieeJointRenteAccordeeViewBean> allRentesLiees) {
        List<String> output = new ArrayList<String>();

        for (RERenteLieeJointRenteAccordeeViewBean renteLiee : allRentesLiees) {
            if (renteLiee.hasPostit()) {
                String idTiersBeneficiaire = renteLiee.getIdTiersBeneficiaire();

                if (output.contains(idTiersBeneficiaire) == false) {
                    output.add(idTiersBeneficiaire);
                }
            }
        }

        return output;
    }

    /**
     * Filtre les rentes li�es appartenant � un b�n�ficiaire donn�
     * 
     * @param allRentesLiees Toutes les rentes li�es consid�r�es
     * @param idTiersBeneficiaire L'ID du tiers b�n�ficiaire consid�r�
     * @return Les rentes li�es dont le b�n�ficiaire est le tiers sp�cifi�
     */
    private static List<RERenteLieeJointRenteAccordeeViewBean> filterRentesLiees(
            Iterable<RERenteLieeJointRenteAccordeeViewBean> allRentesLiees, String idTiersBeneficiaire) {
        List<RERenteLieeJointRenteAccordeeViewBean> output = new ArrayList<RERenteLieeJointRenteAccordeeViewBean>();

        for (RERenteLieeJointRenteAccordeeViewBean renteLiee : allRentesLiees) {
            if (renteLiee.getIdTiersBeneficiaire().equals(idTiersBeneficiaire)) {
                output.add(renteLiee);
            }
        }

        return output;
    }

    /**
     * Filtre les demandes appartenant � un requ�rant donn�
     * 
     * @param allDemandes Toutes les rentes li�es consid�r�es
     * @param idTiersRequerant L'ID du tiers requ�rant consid�r�
     * @return Les demandes dont le requ�rant est le tiers sp�cifi�
     */
    private static List<REDemandeRenteJointPrestationAccordeeViewBean> filterDemandes(
            Iterable<REDemandeRenteJointPrestationAccordeeViewBean> allDemandes, String idTiersRequerant) {
        List<REDemandeRenteJointPrestationAccordeeViewBean> output = new ArrayList<REDemandeRenteJointPrestationAccordeeViewBean>();

        for (REDemandeRenteJointPrestationAccordeeViewBean demande : allDemandes) {
            if (idTiersRequerant.equals(demande.getIdTiersRequerant())) {
                output.add(demande);
            }
        }
        return output;
    }

    /**
     * Filtre les rentes accord�es appartenant � un b�n�ficiaire donn�
     * 
     * @param allDemandes Toutes les rentes li�es consid�r�es
     * @param idTiersBeneficiaire L'ID du tiers b�n�ficiaire consid�r�
     * @return Les rentes accord�es dont le b�n�ficiaire est le tiers sp�cifi�
     */
    private static List<RERenteAccordeeJointDemandeRenteViewBean> filterRentesAccordees(
            final Iterable<RERenteAccordeeJointDemandeRenteViewBean> allRentesAccordees, String idTiersBeneficiaire) {
        List<RERenteAccordeeJointDemandeRenteViewBean> output = new ArrayList<RERenteAccordeeJointDemandeRenteViewBean>();

        for (RERenteAccordeeJointDemandeRenteViewBean renteAccordee : allRentesAccordees) {
            if (idTiersBeneficiaire.equals(renteAccordee.getIdTiersBeneficiaire())) {
                output.add(renteAccordee);
            }
        }

        return output;
    }

    /**
     * Met � z�ro le nombre de post-its pour toutes les demandes sp�cifi�es, SAUF pour celle dont l'ID sp�cifi�
     * 
     * @param allDemandes Toutes les demandes � traiter
     * @param idDemandeToKeep L'ID de la demande dont le nombre de post-it's ne doit pas �tre mis � zero
     */
    private static void clearPostItsDemandes(List<REDemandeRenteJointPrestationAccordeeViewBean> allDemandes,
            String idDemandeToKeep) {

        if (idDemandeToKeep != null) {
            for (REDemandeRenteJointPrestationAccordeeViewBean demande : allDemandes) {
                if (idDemandeToKeep.equals(demande.getId()) == false) {
                    demande.setPostIt("0");
                }
            }
        }
    }

    /**
     * Met � z�ro le nombre de post-its pour toutes les rentes accord�es sp�cifi�es, SAUF pour celle dont l'ID sp�cifi�
     * 
     * @param allRentesAccordees Toutes les rentes accord�es � traiter
     * @param idPrestationAccordeeToKeep L'ID de la prestation accord�e dont le nombre de post-it's ne doit pas �tre mis
     *            � zero
     */
    private static void clearPostItsRentesAccordees(List<RERenteAccordeeJointDemandeRenteViewBean> allRentesAccordees,
            String idPrestationAccordeeToKeep) {
        if (idPrestationAccordeeToKeep != null) {
            for (RERenteAccordeeJointDemandeRenteViewBean renteAccordee : allRentesAccordees) {
                if (idPrestationAccordeeToKeep.equals(renteAccordee.getIdPrestationAccordee()) == false) {
                    renteAccordee.setNbPostit("0");
                }
            }
        }
    }

    /**
     * Met � z�ro le nombre de post-its pour toutes les rentes li�es sp�cifi�es, SAUF pour celle dont l'ID sp�cifi�
     * 
     * @param allRentesLiees Toutes les rentes li�es � traiter
     * @param idPrestationAccordeeToKeep L'ID de la prestation accord�e dont le nombre de post-it's ne doit pas �tre mis
     *            � zero
     */
    private static void clearPostItsRentesLiees(List<RERenteLieeJointRenteAccordeeViewBean> allRentesLiees,
            String idPrestationAccordeeToKeep) {
        if (idPrestationAccordeeToKeep != null) {
            for (RERenteLieeJointRenteAccordeeViewBean renteLiee : allRentesLiees) {
                if (idPrestationAccordeeToKeep.equals(renteLiee.getIdPrestationAccordee()) == false) {
                    renteLiee.setPostit("0");
                }
            }
        }
    }

    /**
     * Extrait l'ID de la rente accord�e dont la date de d�but de droit est la plus tardive
     * 
     * @param allRentesAccordees Toutes les rentes accord�es consid�r�es
     * @return L'ID de la prestation accord�e la plus tardive
     */
    private static String extractIdLastRenteAccordee(List<RERenteAccordeeJointDemandeRenteViewBean> allRentesAccordees) {
        List<Element> buffer = new ArrayList<Element>();

        for (RERenteAccordeeJointDemandeRenteViewBean renteAccordee : allRentesAccordees) {
            buffer.add(new Element(renteAccordee.getIdPrestationAccordee(), renteAccordee.getDateDebutDroit()));
        }

        return Element.extractLastItemId(buffer);
    }

    /**
     * Extrait l'ID de la rente li�e dont la date de d�but de droit est la plus tardive
     * 
     * @param allRentesLiees Toutes les rentes li�es consid�r�es
     * @return L'ID de la prestation accord�e la plus tardive
     */
    private static String extractIdLastRenteLiee(List<RERenteLieeJointRenteAccordeeViewBean> allRentesLiees) {
        List<Element> buffer = new ArrayList<Element>();

        for (RERenteLieeJointRenteAccordeeViewBean renteLiee : allRentesLiees) {
            buffer.add(new Element(renteLiee.getIdPrestationAccordee(), renteLiee.getDateDebutDroit()));
        }

        return Element.extractLastItemId(buffer);
    }

    /**
     * Extrait l'ID de la demande dont la date de d�but est la plus tardive
     * 
     * @param allDemandes Toutes les demandes consid�r�es
     * @return L'ID de la demande la plus tardive
     */
    private static String extractLatestDemande(List<REDemandeRenteJointPrestationAccordeeViewBean> allDemandes) {
        List<Element> buffer = new ArrayList<Element>();

        for (REDemandeRenteJointPrestationAccordeeViewBean demande : allDemandes) {
            buffer.add(new Element(demande.getId(), demande.getDateDebut()));
        }

        return Element.extractLastItemId(buffer);
    }
}
