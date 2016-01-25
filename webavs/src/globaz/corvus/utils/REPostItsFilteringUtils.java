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
 * Compilation de méthodes utilitaires permettant de masquer les post-it's disponibles sur un ensemble de demandes de
 * rente, rentes accordées et rentes liées.
 * Ce filtrage ne conserve, pour chaque tiers, que la demande/rente la plus tardive.
 * 
 * Ajoutée dans le cadre du mandat IR398
 * 
 * @author ame
 */
public class REPostItsFilteringUtils {

    /**
     * Structure de données faisant correspondre un ID et une date, permettant de généraliser le traitement d'extraction
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
         * @return Date utilisée comme critère de tri
         */
        public String getDate() {
            return date;
        }

        /**
         * @return Clef unique (ID demande si demande, ID prestation accordée si rente) correspondant à la date
         */
        public String getId() {
            return id;
        }

        /**
         * Retourne l'ID dont la date est la plus tardive, parmis une liste d'éléments spécifiée
         * 
         * @param allElements Tous les éléments considérés
         * @return L'ID de l'élément dont la date est la plus tardive
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
     * Toutes les méthodes de cette classe sont statiques, cette classe ne doit pas être instanciée
     */
    private REPostItsFilteringUtils() {
    }

    /**
     * Ne conserve que les post-it's pour la demande la plus tardive pour chaque tiers requérant
     * 
     * @param allDemandes Toutes les demandes considérées
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
     * Ne conserve que les post-its pour la rente la plus tardive pour chaque tiers bénéficiaire
     * 
     * @param allRentesLiees La liste des rentes liées considérées
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
     * Ne conserve que les post-its pour la rente la plus tardive pour chaque tiers bénéficiaire
     * 
     * @param allRentesAccordees Toutes les rentes accordées affichées
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
     * Retourne les tiers dont une rente accordée possède au moins un post-it
     * 
     * @param allRentesAccordees La liste des rentes accordées à traiter
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
     * Retourne la liste des tiers dont une demande possède au moins un post-it
     * 
     * @param allDemandes La liste des demandes à traiter
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
     * Retourne la liste des tiers dont une rente liée possède au moins un post-it
     * 
     * @param allRentesLiees La liste des rentes liées à traiter
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
     * Filtre les rentes liées appartenant à un bénéficiaire donné
     * 
     * @param allRentesLiees Toutes les rentes liées considérées
     * @param idTiersBeneficiaire L'ID du tiers bénéficiaire considéré
     * @return Les rentes liées dont le bénéficiaire est le tiers spécifié
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
     * Filtre les demandes appartenant à un requérant donné
     * 
     * @param allDemandes Toutes les rentes liées considérées
     * @param idTiersRequerant L'ID du tiers requérant considéré
     * @return Les demandes dont le requérant est le tiers spécifié
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
     * Filtre les rentes accordées appartenant à un bénéficiaire donné
     * 
     * @param allDemandes Toutes les rentes liées considérées
     * @param idTiersBeneficiaire L'ID du tiers bénéficiaire considéré
     * @return Les rentes accordées dont le bénéficiaire est le tiers spécifié
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
     * Met à zéro le nombre de post-its pour toutes les demandes spécifiées, SAUF pour celle dont l'ID spécifié
     * 
     * @param allDemandes Toutes les demandes à traiter
     * @param idDemandeToKeep L'ID de la demande dont le nombre de post-it's ne doit pas être mis à zero
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
     * Met à zéro le nombre de post-its pour toutes les rentes accordées spécifiées, SAUF pour celle dont l'ID spécifié
     * 
     * @param allRentesAccordees Toutes les rentes accordées à traiter
     * @param idPrestationAccordeeToKeep L'ID de la prestation accordée dont le nombre de post-it's ne doit pas être mis
     *            à zero
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
     * Met à zéro le nombre de post-its pour toutes les rentes liées spécifiées, SAUF pour celle dont l'ID spécifié
     * 
     * @param allRentesLiees Toutes les rentes liées à traiter
     * @param idPrestationAccordeeToKeep L'ID de la prestation accordée dont le nombre de post-it's ne doit pas être mis
     *            à zero
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
     * Extrait l'ID de la rente accordée dont la date de début de droit est la plus tardive
     * 
     * @param allRentesAccordees Toutes les rentes accordées considérées
     * @return L'ID de la prestation accordée la plus tardive
     */
    private static String extractIdLastRenteAccordee(List<RERenteAccordeeJointDemandeRenteViewBean> allRentesAccordees) {
        List<Element> buffer = new ArrayList<Element>();

        for (RERenteAccordeeJointDemandeRenteViewBean renteAccordee : allRentesAccordees) {
            buffer.add(new Element(renteAccordee.getIdPrestationAccordee(), renteAccordee.getDateDebutDroit()));
        }

        return Element.extractLastItemId(buffer);
    }

    /**
     * Extrait l'ID de la rente liée dont la date de début de droit est la plus tardive
     * 
     * @param allRentesLiees Toutes les rentes liées considérées
     * @return L'ID de la prestation accordée la plus tardive
     */
    private static String extractIdLastRenteLiee(List<RERenteLieeJointRenteAccordeeViewBean> allRentesLiees) {
        List<Element> buffer = new ArrayList<Element>();

        for (RERenteLieeJointRenteAccordeeViewBean renteLiee : allRentesLiees) {
            buffer.add(new Element(renteLiee.getIdPrestationAccordee(), renteLiee.getDateDebutDroit()));
        }

        return Element.extractLastItemId(buffer);
    }

    /**
     * Extrait l'ID de la demande dont la date de début est la plus tardive
     * 
     * @param allDemandes Toutes les demandes considérées
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
