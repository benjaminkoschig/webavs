package globaz.osiris.db.irrecouvrable;

import globaz.globall.util.JANumberFormatter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contients l'ensemble des postes pour la mise à charges des recouvrements. Cette classe sert de facade à la liste des
 * postes. Elle offre un ensemble de méthodes permettant la manipulation des données
 * 
 * @author sch
 * 
 */
public class CARecouvrementPosteContainer {
    private BigDecimal montantAttribue;
    private Map<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPostesMap;

    public CARecouvrementPosteContainer() {
        recouvrementPostesMap = new HashMap<CARecouvrementKeyPosteContainer, CARecouvrementPoste>();
        montantAttribue = new BigDecimal("0.0");
    }

    /**
     * Ajoute un message d'erreur dans le recouvrementPoste. Pour identifier le recouvrementPoste il faut fournir le
     * recouvrementPoste(keyRecouvrementPosteContainer)
     * 
     * @param keyRecouvrementPosteContainer
     * @param idSection
     * @param messageErreur
     */
    public void addMessageErreurToPoste(CARecouvrementKeyPosteContainer keyRecouvrementPosteContainer,
            String messageErreur) {
        CARecouvrementPoste recouvrementPoste = recouvrementPostesMap.get(keyRecouvrementPosteContainer);
        recouvrementPoste.setRecouvrementPosteOnError(true);
        recouvrementPoste.addMessageErreur(messageErreur);
    }

    /**
     * Permet d'ajouter un recouvrementPoste dans la structure de données.
     * 
     * @param annee
     * @param idRubrique
     * @param idRubriqueIrrecouvrable
     * @param numeroRubriqueIrrecouvrable
     * @param idRubriqueRecouvrement
     * @param numeroRubriqueRecouvrement
     * @param libelleRubriqueRecouvrement
     * @param cumulCotisationAmortie
     * @param cumulCotisationAmortieCorrigee
     * @param cumulRecouvrementCotisationAmortie
     * @param cumulRecouvrementCotisationAmortieCorrigee
     * @param recouvrement
     * @param ordrePriorite
     * @param valeurInitialeCotAmortie
     * @param valeurInitialeCotRecouvrement
     * @param montantNoteDeCredit
     * @param type
     */
    public void addPosteContainer(Integer annee, String idRubrique, String idRubriqueIrrecouvrable,
            String numeroRubriqueIrrecouvrable, String idRubriqueRecouvrement, String numeroRubriqueRecouvrement,
            String libelleRubriqueRecouvrement, BigDecimal cumulCotisationAmortie,
            BigDecimal cumulCotisationAmortieCorrigee, BigDecimal cumulRecouvrementCotisationAmortie,
            BigDecimal cumulRecouvrementCotisationAmortieCorrigee, BigDecimal recouvrement, String ordrePriorite,
            BigDecimal valeurInitialeCotAmortie, BigDecimal valeurInitialeCotRecouvrement,
            BigDecimal montantNoteDeCredit, CATypeDeRecouvrementPoste type) {

        // création de la clé qui va permettre d'identifier de manière unique le poste
        CARecouvrementKeyPosteContainer keyRecouvrementPosteContainer = new CARecouvrementKeyPosteContainer(annee,
                numeroRubriqueRecouvrement, ordrePriorite);

        CARecouvrementPoste recouvrementPoste = null;
        if (!recouvrementPostesMap.containsKey(keyRecouvrementPosteContainer)) {

            recouvrementPoste = new CARecouvrementPoste(annee, idRubrique, idRubriqueIrrecouvrable,
                    numeroRubriqueIrrecouvrable, idRubriqueRecouvrement, numeroRubriqueRecouvrement,
                    libelleRubriqueRecouvrement, cumulCotisationAmortie, cumulCotisationAmortieCorrigee,
                    cumulRecouvrementCotisationAmortie, cumulRecouvrementCotisationAmortieCorrigee, recouvrement,
                    ordrePriorite, valeurInitialeCotAmortie, valeurInitialeCotRecouvrement, montantNoteDeCredit, type);

            CARecouvrementKeyPosteContainer keyRecouvrementPosteContainerTrouvee = isKeyRecouvrementAnneeRubriqueExistante(keyRecouvrementPosteContainer);
            if (keyRecouvrementPosteContainerTrouvee != null) {
                recouvrementPoste.setRecouvrementKeyPosteContainerPrincipal(keyRecouvrementPosteContainerTrouvee);
            }

            recouvrementPostesMap.put(keyRecouvrementPosteContainer, recouvrementPoste);
        }
    }

    /**
     * Mise à jour du CARecouvrementPoste passé en paramètre
     * 
     * @param recouvrement
     * @param recouvrementPoste
     */
    private void updatePosteContainer(BigDecimal recouvrement, CARecouvrementPoste recouvrementPoste) {

        recouvrementPoste.additionnerToRecouvrement(recouvrement);
    }

    /**
     * Cette méthode crée ou met à jour un CARecouvrementPoste
     * 
     * @param annee
     * @param idRubrique
     * @param idRubriqueIrrecouvrable
     * @param numeroRubriqueIrrecouvrable
     * @param idRubriqueRecouvrement
     * @param numeroRubriqueRecouvrement
     * @param libelleRubriqueRecouvrement
     * @param cumulCotisationAmortie
     * @param cumulCotisationAmortieCorrigee
     * @param cumulRecouvrementCotisationAmortie
     * @param cumulRecouvrementCotisationAmortieCorrigee
     * @param recouvrement
     * @param ordrePriorite
     * @param valeurInitialeCotAmortie
     * @param valeurInitialeCotRecouvrement
     * @param montantNoteDeCredit
     * @param type
     */
    public void addOrUpdatePosteContainer(Integer annee, String idRubrique, String idRubriqueIrrecouvrable,
            String numeroRubriqueIrrecouvrable, String idRubriqueRecouvrement, String numeroRubriqueRecouvrement,
            String libelleRubriqueRecouvrement, BigDecimal cumulCotisationAmortie,
            BigDecimal cumulCotisationAmortieCorrigee, BigDecimal cumulRecouvrementCotisationAmortie,
            BigDecimal cumulRecouvrementCotisationAmortieCorrigee, BigDecimal recouvrement, String ordrePriorite,
            BigDecimal valeurInitialeCotAmortie, BigDecimal valeurInitialeCotRecouvrement,
            BigDecimal montantNoteDeCredit, CATypeDeRecouvrementPoste type) {

        // création de la clé qui va permettre d'identifier de manière unique le poste
        CARecouvrementKeyPosteContainer keyRecouvrementPosteContainer = new CARecouvrementKeyPosteContainer(annee,
                numeroRubriqueRecouvrement, ordrePriorite);

        CARecouvrementKeyPosteContainer keyRecouvrementPosteContainerTrouvee = isKeyRecouvrementAnneeRubriqueExistante(keyRecouvrementPosteContainer);
        if (keyRecouvrementPosteContainerTrouvee != null) {

            CARecouvrementPoste recouvrementPoste = recouvrementPostesMap.get(keyRecouvrementPosteContainerTrouvee);
            updatePosteContainer(recouvrement, recouvrementPoste);
        } else {
            addPosteContainer(annee, idRubrique, idRubriqueIrrecouvrable, numeroRubriqueIrrecouvrable,
                    idRubriqueRecouvrement, numeroRubriqueRecouvrement, libelleRubriqueRecouvrement,
                    cumulCotisationAmortie, cumulCotisationAmortieCorrigee, cumulRecouvrementCotisationAmortie,
                    cumulRecouvrementCotisationAmortieCorrigee, recouvrement, ordrePriorite, valeurInitialeCotAmortie,
                    valeurInitialeCotRecouvrement, montantNoteDeCredit, type);
        }
    }

    /**
     * Cette méthode permet de rechercher si la clé passé en paramètre (année, numéroRubrique) figure dans
     * recouvrementPostesMap si c'est le cas la clé de recouvrementPostesMap est retournée
     * 
     * @param keyRecouvrementPosteContainer
     * @return CARecouvrementKeyPosteContainer : retourne la clé trouvée dans CARecouvrementPostesMap en fonction de
     *         l'année et la rubrique. Null si pas trouvé
     */
    private CARecouvrementKeyPosteContainer isKeyRecouvrementAnneeRubriqueExistante(
            CARecouvrementKeyPosteContainer keyRecouvrementPosteContainer) {

        CARecouvrementKeyPosteContainer keyRecouvrementKeyPosteContainertrouvee = null;

        Set<CARecouvrementKeyPosteContainer> keyRecouvrementAnneeNumRubriqueSet = recouvrementPostesMap.keySet();
        for (CARecouvrementKeyPosteContainer key : keyRecouvrementAnneeNumRubriqueSet) {
            CAKeyAnneeNumRubriqueComparator comparator = new CAKeyAnneeNumRubriqueComparator();
            if (comparator.compare(key, keyRecouvrementPosteContainer) == 0) {
                keyRecouvrementKeyPosteContainertrouvee = key;
                break;
            }
        }
        return keyRecouvrementKeyPosteContainertrouvee;
    }

    /**
     * Ajout d'un recouvrementPoste
     * 
     * @param poste
     */
    public void addRecouvrementPoste(CARecouvrementPoste poste) {

        CARecouvrementKeyPosteContainer keyRecouvrementPosteContainer = new CARecouvrementKeyPosteContainer(
                poste.getAnnee(), poste.getNumeroRubriqueRecouvrement(), poste.getOrdrePriorite());

        if (!recouvrementPostesMap.containsKey(keyRecouvrementPosteContainer)) {
            recouvrementPostesMap.put(keyRecouvrementPosteContainer, poste);
        }
    }

    /**
     * Calcul le montant total des colonnes de l'écran : (amortissement, déjà recouvert, recouvrement) de tous les
     * postes. <br>
     * [0] = cumulCotisationAmortieTotal <br>
     * [1] = cumulRecouvrementCotisationAmortieTotal <br>
     * [3] = cumulRecouvrement<br>
     * 
     * @return BigDecimal[]
     */
    public BigDecimal[] calculerCumulMontantColonnes() {
        BigDecimal tabCumulMontantCumulColonnes[] = new BigDecimal[3];
        BigDecimal cumulCotisationAmortieTotal = new BigDecimal(0);
        BigDecimal cumulRecouvrementCotisationAmortieTotal = new BigDecimal(0);
        BigDecimal recouvrementTotal = new BigDecimal(0);

        for (Map.Entry<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPosteEntry : recouvrementPostesMap
                .entrySet()) {
            CARecouvrementPoste recouvrementPoste = recouvrementPosteEntry.getValue();
            cumulCotisationAmortieTotal = cumulCotisationAmortieTotal
                    .add(recouvrementPoste.getCumulCotisationAmortie());
            cumulRecouvrementCotisationAmortieTotal = cumulRecouvrementCotisationAmortieTotal.add(recouvrementPoste
                    .getCumulRecouvrementCotisationAmortie());
            recouvrementTotal = recouvrementTotal.add(recouvrementPoste.getRecouvrement());
        }

        tabCumulMontantCumulColonnes[0] = cumulCotisationAmortieTotal;
        tabCumulMontantCumulColonnes[1] = cumulRecouvrementCotisationAmortieTotal;
        tabCumulMontantCumulColonnes[2] = cumulRecouvrementCotisationAmortieTotal;

        return tabCumulMontantCumulColonnes;
    }

    /**
     * Calcul le cumul total des montants disponibles des postes pour une priorité donnée
     * 
     * @param annee
     * @return
     */
    public BigDecimal calculerCumulMontantTotalPostesPourPriorite(String priorite) {
        BigDecimal cumulMontantTotalPostesPourPriorite = new BigDecimal(0);

        for (Map.Entry<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPosteEntry : recouvrementPostesMap
                .entrySet()) {
            String keyOrdrePriorite = recouvrementPosteEntry.getKey().getOrdrePriorite();
            CARecouvrementPoste recouvrementPoste = recouvrementPosteEntry.getValue();
            if (keyOrdrePriorite.equals(priorite)) {
                cumulMontantTotalPostesPourPriorite = cumulMontantTotalPostesPourPriorite.add(recouvrementPoste
                        .getSoldeDisponible());
            }
        }

        return cumulMontantTotalPostesPourPriorite;
    }

    /**
     * Calcul le montant total de tous les recouvrements de tous les postes.
     * 
     * @return BigDecimal
     */
    public BigDecimal calculerCumulRecouvrementPostes() {
        BigDecimal cumulMontantsRecouvrementPostes = new BigDecimal(0);

        for (Map.Entry<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPosteEntry : recouvrementPostesMap
                .entrySet()) {
            CARecouvrementPoste recouvrementPoste = recouvrementPosteEntry.getValue();
            cumulMontantsRecouvrementPostes = cumulMontantsRecouvrementPostes.add(recouvrementPoste.getRecouvrement());
        }

        return cumulMontantsRecouvrementPostes;
    }

    /**
     * Retourner une map contenant les montants de recouvrement CI et le genre de décision(cotisation personnelle)
     * classés par année (key de la map). Seul les postes de type "cotisation personnelle" sont pris en compte
     * 
     * @return
     */
    public Map<Integer, CAInfoMontantRecouvrementCi> calculerMontantRecouvrementCiParAnnee() {
        Map<Integer, CAInfoMontantRecouvrementCi> infoMontantRecouvrementCiMap = new HashMap<Integer, CAInfoMontantRecouvrementCi>();

        // parcours des postes
        for (Map.Entry<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPosteEntry : recouvrementPostesMap
                .entrySet()) {
            BigDecimal recouvrementCiPoste = new BigDecimal(0);
            String genreDecision = null;
            CARecouvrementPoste recouvrementPoste = recouvrementPosteEntry.getValue();

            // si il s'agit d'un poste cotisation personnel
            if (CAIrrecouvrableUtils.isRubriqueCotPers(recouvrementPoste.getNumeroRubriqueIrrecouvrable())) {
                BigDecimal solde = recouvrementPoste.getRecouvrement();
                BigDecimal cotisationAvs = recouvrementPoste.getCotisationAvs();
                BigDecimal revenuCi = recouvrementPoste.getRevenuCi();
                if (cotisationAvs.signum() != 0) {
                    BigDecimal amortissementCiLigne = solde.multiply(revenuCi).divide(cotisationAvs, 2,
                            BigDecimal.ROUND_HALF_EVEN);
                    recouvrementCiPoste = recouvrementCiPoste.add(amortissementCiLigne);
                } else {
                    recouvrementCiPoste = recouvrementCiPoste.add(new BigDecimal(0));
                }

                if (genreDecision == null) {
                    genreDecision = recouvrementPoste.getGenreDecision();
                }

                CAInfoMontantRecouvrementCi infoMontantRecouvrementCi = new CAInfoMontantRecouvrementCi(genreDecision,
                        recouvrementCiPoste);
                infoMontantRecouvrementCiMap.put(recouvrementPoste.getAnnee(), infoMontantRecouvrementCi);
            }
        }

        return infoMontantRecouvrementCiMap;
    }

    /**
     * Retourne une liste contenant toutes les années contenues dans la map. Chaque année n'apparaît qu'une seule fois.
     * 
     * @return
     */
    public List<Integer> findAnneesContenues() {
        List<Integer> anneesContenuesList = new ArrayList<Integer>();

        for (Map.Entry<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPosteEntry : recouvrementPostesMap
                .entrySet()) {
            Integer annee = recouvrementPosteEntry.getKey().getAnnee();
            if (!anneesContenuesList.contains(annee)) {
                anneesContenuesList.add(annee);
            }
        }

        return anneesContenuesList;
    }

    /**
     * Retourne une map contenant l'ensemble des postes (cot.pers.) classé par clé du poste
     * 
     * @return
     */
    public Map<CARecouvrementKeyPosteContainer, CARecouvrementPoste> findRecouvrementPosteCotPers() {
        Map<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPosteCotPersMap = new HashMap<CARecouvrementKeyPosteContainer, CARecouvrementPoste>();

        // parcours des recouvrementPostes
        for (Map.Entry<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPosteEntry : recouvrementPostesMap
                .entrySet()) {
            CARecouvrementKeyPosteContainer posteKey = recouvrementPosteEntry.getKey();
            CARecouvrementPoste recouvrementPoste = recouvrementPosteEntry.getValue();

            // si il s'agit d'un poste cotisation personnel
            if (CAIrrecouvrableUtils.isRubriqueCotPers(recouvrementPoste.getNumeroRubriqueIrrecouvrable())) {

                // Si le champ recouvrement est différent de zéro on ajoute le poste dans la map
                if (recouvrementPoste.getRecouvrement().compareTo(new BigDecimal(0)) != 0) {
                    recouvrementPosteCotPersMap.put(posteKey, recouvrementPoste);
                }
            }
        }

        return recouvrementPosteCotPersMap;
    }

    public BigDecimal getMontantAttribue() {
        return montantAttribue;
    }

    /**
     * Retourne la map des postes de recouvrement
     * 
     * @return
     */
    public Map<CARecouvrementKeyPosteContainer, CARecouvrementPoste> getRecouvrementPostesMap() {
        return recouvrementPostesMap;
    }

    @Override
    public String toString() {
        String result = "";
        for (Map.Entry<CARecouvrementKeyPosteContainer, CARecouvrementPoste> posteEntry : recouvrementPostesMap
                .entrySet()) {
            CARecouvrementKeyPosteContainer key = posteEntry.getKey();
            CARecouvrementPoste posteRecouvrement = posteEntry.getValue();
            result += "\nkey(numeroRubriqueRecouvrement, ordrePriorite) : " + key.getNumeroRubriqueRecouvrement()
                    + ", " + key.getOrdrePriorite();
            result += "\n" + posteRecouvrement.toString();
            result += "###########################################################################";
        }
        return result;
    }

    /**
     * Permet la mise à jour d'un recouvrementPoste
     * 
     * @param keyRecouvrementPosteContainer
     * @param revenuCi
     * @param cotisationAvs
     */
    public void updateRecouvrementPoste(CARecouvrementKeyPosteContainer keyRecouvrementPosteContainer,
            BigDecimal revenuCi, BigDecimal cotisationAvs, String genreDecision) {

        CARecouvrementPoste recouvrementPoste = recouvrementPostesMap.get(keyRecouvrementPosteContainer);
        recouvrementPoste.additionnerToRevenuCi(revenuCi);
        recouvrementPoste.additionnerToCotisationAvs(cotisationAvs);
        recouvrementPoste.setGenreDecision(genreDecision);
    }

    /**
     * Permet la mise à jour d'un recouvrementPoste avec une note de crédit
     * 
     * @param CARecouvrementPoste
     * @param recouvrement
     * @param montantNoteDeCredit
     */
    private void updateNoteDeCreditRecouvrementPoste(CARecouvrementPoste recouvrementPoste, BigDecimal recouvrement,
            BigDecimal montantNoteDeCredit) {

        recouvrementPoste.additionnerToRecouvrement(recouvrement);
        recouvrementPoste.additionnerToMontantNoteDeCredit(montantNoteDeCredit);
    }

    /**
     * Permet de ventiler les montants de tous les postes correspondant à l'ordre de priorité donnée.
     * 
     * @param ordrePriorite
     */
    public void ventilerForOrdrePriorite(String ordrePriorite) {
        for (Map.Entry<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPosteEntry : recouvrementPostesMap
                .entrySet()) {
            String keyOrdrePriorite = recouvrementPosteEntry.getKey().getOrdrePriorite();
            CARecouvrementPoste recouvrementPoste = recouvrementPosteEntry.getValue();
            if (keyOrdrePriorite.equals(ordrePriorite)) {
                if (recouvrementPoste.getSoldeDisponible().compareTo(new BigDecimal(0)) <= 0) {
                    continue;
                }
                recouvrementPoste.additionnerToRecouvrement(recouvrementPoste.getSoldeDisponible());
                montantAttribue = montantAttribue.add(recouvrementPoste.getSoldeDisponible());

            }
        }
    }

    /**
     * Cette méthode permet de mettre à jour un recouvrementPoste avec les informations des notes de crédit par ordre de
     * priorité elle retourne true dès qu'une rubrique de cot. pers est trouvée
     * 
     * @param ordrePriorite
     * @param montantRestantARecouvrir
     * @param cumulMontantDisponiblePostesPourOrdrePriorite
     * @param cumulMontantsOperationsSectionsMap
     * @return true si une rubrique de cot. pers. est trouvée
     */
    public boolean traiterNoteDeCreditForPriorite(
            String ordrePriorite,
            BigDecimal montantRestantARecouvrir,
            BigDecimal cumulMontantDisponiblePostesPourOrdrePriorite,
            Map<CAKeyCumulMontantsOperationsSections, CACumulMontantsNotesDeCreditSection> cumulMontantsOperationsSectionsMap) {
        boolean hasRubriqueCotPers = false;

        List<CARecouvrementKeyPosteContainer> listeDesRecouvrementPosteASupprimer = new ArrayList<CARecouvrementKeyPosteContainer>();

        for (Map.Entry<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPosteEntry : recouvrementPostesMap
                .entrySet()) {
            String keyOrdrePriorite = recouvrementPosteEntry.getKey().getOrdrePriorite();
            CARecouvrementPoste recouvrementPoste = recouvrementPosteEntry.getValue();
            CARecouvrementKeyPosteContainer recouvrementKeyPosteContainer = recouvrementPosteEntry.getKey();
            if (recouvrementPoste.getRecouvrementKeyPosteContainerPrincipal() != null) {
                listeDesRecouvrementPosteASupprimer.add(recouvrementKeyPosteContainer);
            }
            if (keyOrdrePriorite.equals(ordrePriorite)) {
                CAKeyCumulMontantsOperationsSections keyCumulMontantsOperationsSections = new CAKeyCumulMontantsOperationsSections(
                        recouvrementPoste.getAnnee(), recouvrementPoste.getIdRubriqueRecouvrement());
                if (cumulMontantsOperationsSectionsMap.containsKey(keyCumulMontantsOperationsSections)) {
                    CACumulMontantsNotesDeCreditSection cumulMontantsOperationsSection = cumulMontantsOperationsSectionsMap
                            .get(keyCumulMontantsOperationsSections);
                    if (recouvrementPoste.getType().equals(CATypeDeRecouvrementPoste.SALARIE)) {
                        updateNoteDeCreditRecouvrementPoste(recouvrementPoste,
                                cumulMontantsOperationsSection.getMontantSalarie(),
                                cumulMontantsOperationsSection.getMontantSalarie());
                    } else if (recouvrementPoste.getType().equals(CATypeDeRecouvrementPoste.EMPLOYEUR)) {
                        updateNoteDeCreditRecouvrementPoste(recouvrementPoste,
                                cumulMontantsOperationsSection.getMontantEmployeur(),
                                cumulMontantsOperationsSection.getMontantEmployeur());
                    } else {
                        if (CAIrrecouvrableUtils.isRubriqueCotPers(recouvrementPoste.getNumeroRubriqueIrrecouvrable())) {
                            hasRubriqueCotPers = true;
                        }
                        updateNoteDeCreditRecouvrementPoste(recouvrementPoste,
                                cumulMontantsOperationsSection.getMontantSimple(),
                                cumulMontantsOperationsSection.getMontantSimple());
                    }
                }
            }
        }
        // Supprimer les recouvrementPostes qui ne doivent pas s'afficher dans l'écran
        for (CARecouvrementKeyPosteContainer caRecouvrementKeyPosteContainer : listeDesRecouvrementPosteASupprimer) {
            recouvrementPostesMap.remove(caRecouvrementKeyPosteContainer);
        }
        return hasRubriqueCotPers;
    }

    /**
     * Permet de ventiler au prorata les montants de tous les postes correspondant à l'ordre de priorité donnée.
     * 
     * @param ordrePriorite
     * @param montantARecouvrir
     * @param cumulMontantDuForOrdrePriorite
     * @return
     */
    public BigDecimal ventilerProrataForPriorite(String ordrePriorite, BigDecimal montantRestantARecouvrir,
            BigDecimal cumulMontantDisponiblePostesPourOrdrePriorite) {
        CARecouvrementPoste saveLastRecouvrementPoste = null;
        BigDecimal cumulProrataRecouvrement = new BigDecimal(0);
        for (Map.Entry<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPosteEntry : recouvrementPostesMap
                .entrySet()) {
            String keyOrdrePriorite = recouvrementPosteEntry.getKey().getOrdrePriorite();
            CARecouvrementPoste recouvrementPoste = recouvrementPosteEntry.getValue();
            if (keyOrdrePriorite.equals(ordrePriorite)) {
                if (recouvrementPoste.getSoldeDisponible().compareTo(new BigDecimal("0")) != 0) {

                    BigDecimal resultatMultiplication = montantRestantARecouvrir.multiply(recouvrementPoste
                            .getSoldeDisponible());
                    BigDecimal montantProrata = resultatMultiplication.divide(
                            cumulMontantDisponiblePostesPourOrdrePriorite, 5, BigDecimal.ROUND_HALF_EVEN);
                    montantProrata = JANumberFormatter.round(montantProrata, 0.05, 2, JANumberFormatter.NEAR);

                    recouvrementPoste.additionnerToRecouvrement(montantProrata);
                    cumulProrataRecouvrement = cumulProrataRecouvrement.add(montantProrata);

                    saveLastRecouvrementPoste = recouvrementPoste;
                }
            }
        }
        if ((saveLastRecouvrementPoste != null) && (montantRestantARecouvrir.compareTo(cumulProrataRecouvrement) != 0)) {
            BigDecimal differenceAAffecter = montantRestantARecouvrir.subtract(cumulProrataRecouvrement);
            saveLastRecouvrementPoste.additionnerToRecouvrement(differenceAAffecter);
            cumulProrataRecouvrement = cumulProrataRecouvrement.add(differenceAAffecter);
        }
        return cumulProrataRecouvrement;
    }
}
