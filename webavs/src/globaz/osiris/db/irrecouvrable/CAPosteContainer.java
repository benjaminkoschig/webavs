package globaz.osiris.db.irrecouvrable;

import globaz.globall.util.JANumberFormatter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Contients l'ensemble des postes pour la mise à charges des irrécouvrables. Cette classe sert de facade à la liste des
 * postes. Elle offre un ensemble de méthodes permettant la manipulation des données
 * 
 * @author bjo
 * 
 */
public class CAPosteContainer {
    private Map<CAKeyPosteContainer, CAPoste> postesMap;

    public CAPosteContainer() {
        postesMap = new HashMap<CAKeyPosteContainer, CAPoste>();
    }

    /**
     * Permet d'ajouter une ligne de poste dans la structure de données. La ligne est automatiquement ajoutée dans le
     * bon poste (le poste est crée si nécessaire). Si une ligne existe déjà pour cette section dans le poste, le
     * montant de la ligne est mis à jour.
     * 
     * @param idRubrique
     * @param annee
     * @param isRubriqueIndetermine
     * @param idSection
     * @param numeroSection
     * @param libelleSection
     * @param montantDu
     * @param type
     * @param idRubriqueIrrecouvrable
     * @param numeroRubriqueIrrecouvrable
     * @param libelleRurbiqueIrrecouvrable
     * @param ordrePriorite
     */
    public void addLigneInPosteContainer(String idRubrique, Integer annee, boolean isRubriqueIndetermine,
            String idSection, String numeroSection, String libelleSection, BigDecimal montantDu,
            CATypeLigneDePoste type, String idRubriqueIrrecouvrable, String numeroRubriqueIrrecouvrable,
            String libelleRurbiqueIrrecouvrable, String ordrePriorite) {

        // création de la clé qui va permettre d'identifier de manière unique le poste
        CAKeyPosteContainer keyPosteContainer = new CAKeyPosteContainer(annee, numeroRubriqueIrrecouvrable,
                ordrePriorite, type);

        CAPoste poste = null;
        if (postesMap.containsKey(keyPosteContainer)) {
            poste = postesMap.get(keyPosteContainer);
            CALigneDePoste ligneDePoste = poste.getLigneDePoste(idSection, type);
            if (ligneDePoste != null) {
                ligneDePoste.additionnerToMontantDu(montantDu);
            } else {
                ligneDePoste = new CALigneDePoste(idSection, numeroSection, libelleSection, montantDu, type);
                poste.addLigneInPoste(ligneDePoste);
            }
        } else {
            poste = new CAPoste(annee, idRubrique, idRubriqueIrrecouvrable, isRubriqueIndetermine,
                    numeroRubriqueIrrecouvrable, libelleRurbiqueIrrecouvrable, ordrePriorite, type);
            CALigneDePoste ligneDePoste = new CALigneDePoste(idSection, numeroSection, libelleSection, montantDu, type);
            poste.addLigneInPoste(ligneDePoste);
            postesMap.put(keyPosteContainer, poste);
        }
    }

    /**
     * Ajoute un message d'erreur à une ligne de poste. Pour identifier la ligne il faut fournir le
     * poste(keyPosteContainer) puis la ligne (idSection)
     * 
     * @param keyPosteContainer
     * @param idSection
     * @param messageErreur
     */
    public void addMessageErreurToLigneDePoste(CAKeyPosteContainer keyPosteContainer, String idSection,
            String messageErreur) {
        CAPoste poste = postesMap.get(keyPosteContainer);
        poste.setPosteOnError(true);
        CALigneDePoste ligneDePoste = poste.getLigneDePoste(idSection, keyPosteContainer.getType());
        ligneDePoste.addMessageErreur(messageErreur);
    }

    /**
     * Calcul le montant total des montantDu et des montantAffecte de tous les postes. [0] = cumulMontantDu [1] =
     * cumulMontantAffecte
     * 
     * @return BigDecimal[]
     */
    public BigDecimal[] calculerCumulMontantDuAndAffecte() {
        BigDecimal tabCumulMontantDuAndAffecte[] = new BigDecimal[2];
        BigDecimal cumulMontantDu = new BigDecimal(0);
        BigDecimal cumulMontantAffecte = new BigDecimal(0);

        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            CAPoste poste = posteEntry.getValue();
            cumulMontantDu = cumulMontantDu.add(poste.getMontantDuTotal());
            cumulMontantAffecte = cumulMontantAffecte.add(poste.getMontantAffecteTotal());
        }

        tabCumulMontantDuAndAffecte[0] = cumulMontantDu;
        tabCumulMontantDuAndAffecte[1] = cumulMontantAffecte;

        return tabCumulMontantDuAndAffecte;
    }

    /**
     * Calcul le montant total des montantDu pour les postes d'une année donnée
     * 
     * @param annee
     * @return
     */
    public BigDecimal calculerCumulMontantDuForAnnee(Integer annee) {
        BigDecimal cumulMontantDu = new BigDecimal(0);

        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            Integer keyAnnee = posteEntry.getKey().getAnnee();
            CAPoste poste = posteEntry.getValue();
            if (keyAnnee.equals(annee)) {
                cumulMontantDu = cumulMontantDu.add(poste.getMontantDuTotal());
            }
        }

        return cumulMontantDu;
    }

    /**
     * Calcul le montant total des montantDu pour les postes d'une année et d'un ordre de priorité donné
     * 
     * @param annee
     * @param ordrePriorite
     * @return
     */
    public BigDecimal calculerCumulMontantDuForAnneeAndOrdrePriorite(Integer annee, String ordrePriorite) {
        BigDecimal cumulMontantDu = new BigDecimal(0);

        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            Integer keyAnnee = posteEntry.getKey().getAnnee();
            String keyOrdrePriorite = posteEntry.getKey().getOrdrePriorite();
            CAPoste poste = posteEntry.getValue();
            if (keyAnnee.equals(annee) && keyOrdrePriorite.equals(ordrePriorite)) {
                cumulMontantDu = cumulMontantDu.add(poste.getMontantDuTotal());
            }
        }

        return cumulMontantDu;
    }

    /**
     * Calcul le montant total des montantDu pour les postes d'un ordre de priorité donné
     * 
     * @param ordrePriorite
     * @return
     */
    public BigDecimal calculerCumulMontantDuForPriorite(String ordrePriorite) {
        BigDecimal cumulMontantDu = new BigDecimal(0);

        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            String keyOrdrePriorite = posteEntry.getKey().getOrdrePriorite();
            CAPoste poste = posteEntry.getValue();
            if (keyOrdrePriorite.equals(ordrePriorite)) {
                cumulMontantDu = cumulMontantDu.add(poste.getMontantDuTotal());
                cumulMontantDu = cumulMontantDu.subtract(poste.getMontantAffecteTotal());
            }
        }

        return cumulMontantDu;
    }

    /**
     * Retourner une map contenant les montants d'amortissements CI et le genre de décision(cotisation personnelle)
     * classés par année (key de la map). Seul les postes de type "cotisation personnelle" sont pris en compte
     * 
     * @return
     */
    public Map<Integer, CAInfoMontantAmortissementCi> calculerMontantAmortissementCiParAnnee() {
        Map<Integer, CAInfoMontantAmortissementCi> infoMontantAmortissementCiMap = new HashMap<Integer, CAInfoMontantAmortissementCi>();

        // parcours des postes
        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            BigDecimal amortissementCiPoste = new BigDecimal(0);
            String genreDecision = null;
            CAKeyPosteContainer posteKey = posteEntry.getKey();
            CAPoste poste = posteEntry.getValue();

            // si il s'agit d'un poste cotisation personnel
            if (CAIrrecouvrableUtils.isRubriqueCotPers(poste.getNumeroRubriqueIrrecouvrable())) {
                for (CALigneDePoste ligneDePoste : poste.getLignesDePosteList()) {
                    BigDecimal solde = ligneDePoste.calculerSolde();
                    BigDecimal cotisationAvs = ligneDePoste.getCotisationAvs();
                    BigDecimal revenuCi = ligneDePoste.getRevenuCi();
                    if (cotisationAvs.signum() != 0) {
                        BigDecimal amortissementCiLigne = solde.multiply(revenuCi).divide(cotisationAvs, 2,
                                BigDecimal.ROUND_HALF_EVEN);
                        amortissementCiPoste = amortissementCiPoste.add(amortissementCiLigne);
                    } else {
                        amortissementCiPoste = amortissementCiPoste.add(new BigDecimal(0));
                    }

                    if (genreDecision == null) {
                        genreDecision = ligneDePoste.getGenreDecision();
                    }
                }
                CAInfoMontantAmortissementCi infoMontantAmortissementCi = new CAInfoMontantAmortissementCi(
                        genreDecision, amortissementCiPoste);
                infoMontantAmortissementCiMap.put(poste.getAnnee(), infoMontantAmortissementCi);
            }
        }

        return infoMontantAmortissementCiMap;
    }

    /**
     * Supprime tous les postes dont le "montantDu" total est négatif. Retourne le total des montants classé par année
     * 
     * @return
     */
    public Map<Integer, BigDecimal> deleteAllPostesNegatifs() {
        Map<Integer, BigDecimal> montantTotalSupprimeParAnnee = new HashMap<Integer, BigDecimal>();

        Iterator<Map.Entry<CAKeyPosteContainer, CAPoste>> postesIterator = postesMap.entrySet().iterator();
        while (postesIterator.hasNext()) {
            Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry = postesIterator.next();
            CAPoste poste = posteEntry.getValue();
            Integer annee = posteEntry.getKey().getAnnee();
            BigDecimal montantDuTotal = poste.getMontantDuTotal();

            // ajout du montant dans la map à retourner et suppression du poste
            if (montantDuTotal.signum() == -1) {
                if (montantTotalSupprimeParAnnee.containsKey(annee)) {
                    BigDecimal nouveauMontantDuTotaL = montantTotalSupprimeParAnnee.get(annee).add(montantDuTotal);
                    montantTotalSupprimeParAnnee.put(annee, nouveauMontantDuTotaL);
                } else {
                    montantTotalSupprimeParAnnee.put(annee, montantDuTotal);
                }
                postesIterator.remove();
            }
        }
        return montantTotalSupprimeParAnnee;
    }

    /**
     * Retourne une liste contenant toutes les années contenues dans la map. Chaque année n'apparaît qu'une seule fois.
     * 
     * @return
     */
    public List<Integer> findAnneesContenues() {
        List<Integer> anneesContenuesList = new ArrayList<Integer>();

        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            Integer annee = posteEntry.getKey().getAnnee();
            if (!anneesContenuesList.contains(annee)) {
                anneesContenuesList.add(annee);
            }
        }

        return anneesContenuesList;
    }

    /**
     * Retourne une map contenant l'ensemble des idSection (ligne de poste) classé par clé du poste
     * 
     * @return
     */
    public Map<CAKeyPosteContainer, List<String>> findPosteCotPers() {
        Map<CAKeyPosteContainer, List<String>> posteCotPersMap = new HashMap<CAKeyPosteContainer, List<String>>();

        // parcours des postes
        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            CAKeyPosteContainer posteKey = posteEntry.getKey();
            CAPoste poste = posteEntry.getValue();

            // si il s'agit d'un poste cotisation personnel
            if (CAIrrecouvrableUtils.isRubriqueCotPers(poste.getNumeroRubriqueIrrecouvrable())) {
                List<String> idSectionList = new ArrayList<String>();

                // parcours des lignes de postes
                List<CALigneDePoste> ligneDePosteList = poste.getLignesDePosteList();
                for (CALigneDePoste ligneDePoste : ligneDePosteList) {
                    idSectionList.add(ligneDePoste.getIdSection());
                }

                posteCotPersMap.put(posteKey, idSectionList);
            }
        }

        return posteCotPersMap;
    }

    /**
     * Retourne la map des postes
     * 
     * @return
     */
    protected Map<CAKeyPosteContainer, CAPoste> getPostesMap() {
        return postesMap;
    }

    @Override
    public String toString() {
        String result = "";
        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            CAKeyPosteContainer key = posteEntry.getKey();
            CAPoste poste = posteEntry.getValue();
            result += "\nkey(numeroRubriqueIrrecouvrable, annee, ordrePriorite, type) : "
                    + key.getNumeroRubriqueIrrecouvrable() + ", " + key.getAnnee() + ", " + key.getOrdrePriorite()
                    + ", " + key.getType();
            result += "\n" + poste.toString();
            result += "###########################################################################";
        }
        return result;
    }

    /**
     * Permet la mise à jour d'une ligne de poste
     * 
     * @param keyPosteContainer
     * @param idSection
     * @param revenuCi
     * @param cotisationAvs
     * @param genreDecision
     */
    public void updateLigneDePoste(CAKeyPosteContainer keyPosteContainer, String idSection, BigDecimal revenuCi,
            BigDecimal cotisationAvs, String genreDecision) {

        CAPoste poste = postesMap.get(keyPosteContainer);
        CALigneDePoste ligneDePoste = poste.getLigneDePoste(idSection, keyPosteContainer.getType());
        ligneDePoste.additionnerToCotisationAvs(cotisationAvs);
        ligneDePoste.additionnerToRevenuCi(revenuCi);
        ligneDePoste.setGenreDecision(genreDecision);
    }

    /**
     * Permet de ventiler les montants de tous les postes correspondant à l'année donnée. Chaque ligne de poste
     * correspondante est ainsi mise à jour
     * 
     * @param annee
     */
    public void ventilerForAnnee(Integer annee) {
        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            Integer keyAnnee = posteEntry.getKey().getAnnee();
            CAPoste poste = posteEntry.getValue();
            if (keyAnnee.equals(annee)) {
                for (CALigneDePoste ligneDePoste : poste.getLignesDePosteList()) {
                    ligneDePoste.additionnerToMontantAffecte(ligneDePoste.getMontantDu());
                }
            }
        }
    }

    /**
     * Permet de ventiler les montants de tous les postes correspondant à l'année et à l'ordre de priorité donnée.
     * Chaque ligne de poste correspondante est ainsi mise à jour
     * 
     * @param annee
     * @param ordrePriorite
     */
    public BigDecimal ventilerForAnneeAndPriorite(Integer annee, String ordrePriorite) {
        BigDecimal cumulMontantForAnneeAndPriorite = new BigDecimal(0);
        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            Integer keyAnnee = posteEntry.getKey().getAnnee();
            String keyOrdrePriorite = posteEntry.getKey().getOrdrePriorite();
            CAPoste poste = posteEntry.getValue();
            if (keyAnnee.equals(annee) && keyOrdrePriorite.equals(ordrePriorite)) {
                for (CALigneDePoste ligneDePoste : poste.getLignesDePosteList()) {
                    ligneDePoste.additionnerToMontantAffecte(ligneDePoste.getMontantDu());
                    // Bug 9167 - Traitement irrécouvrable - la ventilation par année et par priorité ne prend pas le
                    // bon montant à ventiler
                    cumulMontantForAnneeAndPriorite = cumulMontantForAnneeAndPriorite.add(ligneDePoste.getMontantDu());
                }
            }
        }
        return cumulMontantForAnneeAndPriorite;
    }

    /**
     * Permet de ventiler les montants de tous les postes correspondant à l'ordre de priorité donnée. Chaque ligne de
     * poste correspondante est ainsi mise à jour
     * 
     * @param ordrePriorite
     */
    public void ventilerForOrdrePriorite(String ordrePriorite) {
        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            String keyOrdrePriorite = posteEntry.getKey().getOrdrePriorite();
            CAPoste poste = posteEntry.getValue();
            if (keyOrdrePriorite.equals(ordrePriorite)) {
                for (CALigneDePoste ligneDePoste : poste.getLignesDePosteList()) {
                    ligneDePoste.additionnerToMontantAffecte(ligneDePoste.calculerSoldeRound());
                }
            }
        }
    }

    /**
     * Permet de ventiler au prorata les montants de tous les postes correspondant à l'année et à l'ordre de priorité
     * donnée. Chaque ligne de poste correspondante est ainsi mise à jour
     * 
     * @param annee
     * @param ordrePriorite
     * @param montantAVentilerForAnnee
     * @param cumulMontantDuForAnneeAndOrdrePriorite
     * @return
     */
    public BigDecimal ventilerProrataForAnneeAndPriorite(Integer annee, String ordrePriorite,
            BigDecimal montantAVentilerForAnnee, BigDecimal cumulMontantDuForAnneeAndOrdrePriorite) {
        CALigneDePoste saveLastLigneDePoste = null;
        BigDecimal cumulProrataAffecte = new BigDecimal(0);
        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            Integer keyAnnee = posteEntry.getKey().getAnnee();
            String keyOrdrePriorite = posteEntry.getKey().getOrdrePriorite();
            CAPoste poste = posteEntry.getValue();
            if (keyAnnee.equals(annee) && keyOrdrePriorite.equals(ordrePriorite)) {
                for (int i = 0; i < poste.getLignesDePosteList().size(); i++) {
                    CALigneDePoste ligneDePoste = poste.getLignesDePosteList().get(i);

                    BigDecimal resultatMultiplication = montantAVentilerForAnnee.multiply(ligneDePoste.getMontantDu());
                    BigDecimal montantProrata = resultatMultiplication.divide(cumulMontantDuForAnneeAndOrdrePriorite,
                            5, BigDecimal.ROUND_HALF_EVEN);
                    montantProrata = JANumberFormatter.round(montantProrata, 0.05, 2, JANumberFormatter.NEAR);
                    ligneDePoste.setMontantAffecte(montantProrata);

                    cumulProrataAffecte = cumulProrataAffecte.add(montantProrata);

                    saveLastLigneDePoste = ligneDePoste;
                }
            }
        }
        if ((saveLastLigneDePoste != null) && (montantAVentilerForAnnee.compareTo(cumulProrataAffecte) != 0)) {
            BigDecimal differenceAAffecter = montantAVentilerForAnnee.subtract(cumulProrataAffecte);
            saveLastLigneDePoste.additionnerToMontantAffecte(differenceAAffecter);
            cumulProrataAffecte = cumulProrataAffecte.add(differenceAAffecter);
        }
        return cumulProrataAffecte;
    }

    /**
     * Permet de ventiler au prorata les montants de tous les postes correspondant à l'ordre de priorité donnée. Chaque
     * ligne de poste correspondante est ainsi mise à jour
     * 
     * @param ordrePriorite
     * @param montantAVentilerForAnnee
     * @param cumulMontantDuForOrdrePriorite
     */
    public void ventilerProrataForPriorite(String ordrePriorite, BigDecimal montantAVentilerForAnnee,
            BigDecimal cumulMontantDuForOrdrePriorite) {
        CALigneDePoste saveLastLigneDePoste = null;
        BigDecimal cumulProrataAffecte = new BigDecimal(0);
        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            String keyOrdrePriorite = posteEntry.getKey().getOrdrePriorite();
            CAPoste poste = posteEntry.getValue();
            if (keyOrdrePriorite.equals(ordrePriorite)) {
                for (int i = 0; i < poste.getLignesDePosteList().size(); i++) {
                    CALigneDePoste ligneDePoste = poste.getLignesDePosteList().get(i);

                    BigDecimal resultatMultiplication = montantAVentilerForAnnee.multiply(ligneDePoste
                            .calculerSoldeRound());
                    BigDecimal montantProrata = resultatMultiplication.divide(cumulMontantDuForOrdrePriorite, 5,
                            BigDecimal.ROUND_HALF_EVEN);
                    montantProrata = JANumberFormatter.round(montantProrata, 0.05, 2, JANumberFormatter.NEAR);
                    ligneDePoste.additionnerToMontantAffecte(montantProrata);

                    cumulProrataAffecte = cumulProrataAffecte.add(montantProrata);
                    saveLastLigneDePoste = ligneDePoste;
                }
            }
        }
        if ((saveLastLigneDePoste != null) && (montantAVentilerForAnnee.compareTo(cumulProrataAffecte) != 0)) {
            BigDecimal differenceAAffecter = montantAVentilerForAnnee.subtract(cumulProrataAffecte);
            saveLastLigneDePoste.additionnerToMontantAffecte(differenceAAffecter);
        }
    }
}
