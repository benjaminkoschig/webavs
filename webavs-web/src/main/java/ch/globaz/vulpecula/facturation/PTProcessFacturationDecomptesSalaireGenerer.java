package ch.globaz.vulpecula.facturation;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.CotisationCalculee;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeProvenance;
import ch.globaz.vulpecula.external.models.affiliation.Adhesion;
import ch.globaz.vulpecula.util.I18NUtil;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.FWFindParameter;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CARubrique;

/**
 * Processus de facturation des décomptes salaires
 * Va rechercher les décomptes à l'état validé ou rectifié
 * Créé les afacts
 * 
 * @author JPA
 */
public final class PTProcessFacturationDecomptesSalaireGenerer extends PTProcessFacturation {
    private static final long serialVersionUID = 2947007104564077249L;

    private static final String TYPE_ASS_AUTRES = "812004";
    private static final String TYPE_ASS_FFPP_MASSE_SALARIALE = "812027";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public PTProcessFacturationDecomptesSalaireGenerer() {
        super();
    }

    public PTProcessFacturationDecomptesSalaireGenerer(final BProcess parent) {
        super(parent);
    }

    /**
     * Cette méthode exécute le processus de facturation des décomptes
     * 
     * @return boolean est-ce que la facturation s'est effectuée avec succès
     * @throws Exception
     */
    @Override
    protected boolean launch() {
        // Suppression des entete et afact du journal
        deleteEnteteEtAfactForIdPassage(getIdPassage());
        /*
         * Récupération du paramètre de date limite de facturation
         */
        String dateLimite = null;
        try {
            dateLimite = FWFindParameter.findParameter(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction(), "68041001", "DATELIMITE",
                    "01.01.2018", "", 0);
            JadeLogger.info(dateLimite, dateLimite);
        } catch (Exception e) {
            JadeLogger.error(e, e.getMessage());
        }
        
        // Recherche des décomptes validés ou rectifiés
        // Pour des raisons de mem on passe par un Deque (LinkedList) qui permet de libérer la mem après chaque
        // itération.
        // La mem va monter et être libéré quand il y aura besoin d'espace.
        Deque<Decompte> deque = new LinkedList<Decompte>(VulpeculaRepositoryLocator.getDecompteRepository()
                .findDecomptesForFacturation(new Date(dateLimite)));
        Boolean mustPrintSpecialEbusiness = VulpeculaServiceLocator.getPropertiesService()
                .mustImprimerFactureSpecialEbusiness();
        // Boucler sur les décomptes et charger les lignes de salaires
        while (!deque.isEmpty() && !isAborted()) {
            Decompte decompte = deque.removeFirst();

            try {
                // Création de l'entête de facture
                FAEnteteFacture enteteFacture = createEnteteFacture(decompte, mustPrintSpecialEbusiness);
                // On charge les lignes de salaires et les cotis
                fillLignesAndCotisations(decompte);

                String idRubriqueForDifferenceRectifie = findIdRubriqueForEcart(decompte);

                // Pour chaque cotisation on créé une ligne de facture
                for (CotisationCalculee cotisationCalculee : decompte.getAllCotisationsCalculeesGroupByAnnee()) {
                    createAfact(cotisationCalculee, enteteFacture, decompte, idRubriqueForDifferenceRectifie);
                }

                if (!getTransaction().hasErrors()) {
                    // On va mettre à jour le numéro de passage et l'état du décompte à validé
                    majEtatDecompteEtNumeroPassage(decompte);
                } else {
                    if (JadeThread.logMessages() != null && JadeThread.logMessages().length > 0) {
                        for (JadeBusinessMessage message : JadeThread.logMessages()) {
                            getTransaction().addErrors(message.getMessageId());
                            LOGGER.error(message.getMessageId());
                        }
                    }
                    return false;
                }
            } catch (Exception ex) {
                String message = "Decompte : " + decompte.getId() + ", N° d'affilié : "
                        + decompte.getEmployeurAffilieNumero() + "\r\n" + ex.toString();
                LOGGER.error(message);
                getTransaction().addErrors(message);
                return false;
            }
        }
        return !isAborted();
    }

    /**
     * Retourne l'id de la rubrique sur laquelle on va comptabiliser la différence de plus ou moins X francs (selon la
     * priorité dans Administration).
     * Seules les décomptes validées de type complémentaire et période sont concernées. Après demande du BMS tous type
     * de décompte sont concernés.
     * 
     * @param decompte Décompte sur lequel rechercher la rubrique
     * @return L'id rubrique ou null si non concerné
     */
    private String findIdRubriqueForEcart(Decompte decompte) {
        String idRubriqueForDifference = null;
        if (EtatDecompte.VALIDE.equals(decompte.getEtat()) && !decompte.getMontantDifference().isZero()
                && !TypeProvenance.EBUSINESS.equals(decompte.getTypeProvenance())) {
            idRubriqueForDifference = findIdRubriqueForEcart(decompte.getTableCotisationsCalculees());
        }
        return idRubriqueForDifference;
    }

    @Override
    protected void clean() {
    }

    /**
     * Retourne l'idRubrique où comptabiliser la différence
     * 
     * @param listeCotisation
     */
    String findIdRubriqueForEcart(List<CotisationCalculee> listeCotisation) {

        if (listeCotisation == null) {
            throw new NullPointerException("listeCotisation is null !!");
        }

        Map<TypeAssurance, Integer> typesAssurance = new HashMap<TypeAssurance, Integer>();
        typesAssurance.put(TypeAssurance.COTISATION_AF, 1);
        typesAssurance.put(TypeAssurance.COTISATION_FFPP_CAPITATION, 2);
        typesAssurance.put(TypeAssurance.COTISATION_FFPP_MASSE, 3);
        typesAssurance.put(TypeAssurance.AUTRES, 20);
        typesAssurance.put(TypeAssurance.COTISATION_AVS_AI, 100);

        CotisationCalculee cotiPrioritaire = null;
        for (CotisationCalculee cotisationCalculee : listeCotisation) {
            cotiPrioritaire = compareTypeAssurance(typesAssurance, cotisationCalculee, cotiPrioritaire);
        }

        return cotiPrioritaire.getIdRubrique();
    }

    /**
     * Compare deux assurance pour déterminer la plus prioritaire
     * 
     * @param typeA
     * @param typeB
     * @return le type prioritaire
     * @throws NullPointerException
     */
    private CotisationCalculee compareTypeAssurance(Map<TypeAssurance, Integer> typesAssurance,
            CotisationCalculee typeA, CotisationCalculee typeB) {
        if (typeA == null) {
            throw new NullPointerException("Type A is null !!");
        }
        if (typeB == null) {
            return typeA;
        }

        int valA = 0;
        int valB = 0;

        TypeAssurance typeAssuranceA = typeA.getTypeAssurance();
        TypeAssurance typeAssuranceB = typeB.getTypeAssurance();

        if (typesAssurance.containsKey(typeAssuranceA)) {
            valA = typesAssurance.get(typeAssuranceA);
        } else {
            valA = 1000;
        }

        if (typesAssurance.containsKey(typeAssuranceB)) {
            if (valA == 0) {
                return typeB;
            }
            valB = typesAssurance.get(typeAssuranceB);
            if (valA > valB) {
                return typeB;
            } else {
                return typeA;
            }
        } else {
            return typeA;
        }
    }

    /**
     * On charge les lignes de salaires et les cotis
     * 
     * @param decompte
     * @return montant total
     */
    private void fillLignesAndCotisations(final Decompte decompte) {
        decompte.setLignes(VulpeculaRepositoryLocator.getDecompteSalaireRepository().findByIdDecompteWithDependencies(
                decompte.getId()));
    }

    /**
     * Met le passage à l'état facturation
     * Va renseigner le numéro de passage de facturation dans le décompte
     * 
     * @param decompte
     */
    private void majEtatDecompteEtNumeroPassage(final Decompte decompte) {
        decompte.setEtat(EtatDecompte.FACTURATION);
        decompte.setIdPassageFacturation(getPassage().getIdPassage());
        VulpeculaRepositoryLocator.getDecompteRepository().update(decompte);

        HistoriqueDecompte historique = new HistoriqueDecompte();
        historique.setDecompte(decompte);
        historique.setEtat(EtatDecompte.FACTURATION);
        historique.setDate(new Date(JACalendar.today().toStrAMJ()));
        VulpeculaRepositoryLocator.getHistoriqueDecompteRepository().create(historique);
    }

    /**
     * Créé une entete de facture si non existant, sinon retourne l'entete de facture existant dans le passage
     * 
     * @param decompte
     * @return {@link FAEnteteFacture} enteteFacture
     * @throws Exception
     */
    private FAEnteteFacture createEnteteFacture(final Decompte decompte, Boolean mustPrintSpecial) throws Exception {
        if ((decompte.isTraiterAsSpecial() && (mustPrintSpecial || !decompte.isEBusiness()))
                || TypeDecompte.CONTROLE_EMPLOYEUR.equals(decompte.getType())) {
            return createEnteteFactureWithInteret(decompte.getIdTiers(), decompte.getEmployeurAffilieNumero(), decompte
                    .getNumeroDecompte().getValue(), decompte.getTypeSection().getValue(),
                    decompte.getInteretsMoratoires());
        } else {
            return createEnteteFactureWithoutPrinting(decompte.getIdTiers(), decompte.getEmployeurAffilieNumero(),
                    decompte.getNumeroSection(), decompte.getTypeSection().getValue());
        }

    }

    /**
     * Création de la ligne de facture selon la cotisation calculee pour une entete de facture
     * 
     * @param cotisationCalculee
     * @param enteteFacture
     * @param decompte
     * @throws Exception
     */
    private void createAfact(CotisationCalculee cotisationCalculee, FAEnteteFacture enteteFacture, Decompte decompte,
            String idRubriqueForDifferenceRectifie) throws Exception {
        Annee anneeCotisation = cotisationCalculee.getAnnee();

        // création de l'afact
        FAAfact afact = initAfact(enteteFacture.getIdEntete(), FAModuleFacturation.CS_MODULE_DECOMPTES_SALAIRE);

        // Recherche de la rubrique
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(getSession());
        rubrique.setIdRubrique(cotisationCalculee.getCotisation().getAssurance().getRubriqueId());
        rubrique.retrieve();

        if (!rubrique.isNew()) {
            if (rubrique.getNatureRubrique().equals(APIRubrique.COTISATION_AVEC_MASSE)
                    || rubrique.getNatureRubrique().equals(APIRubrique.COTISATION_SANS_MASSE)) {
                afact.setAnneeCotisation(String.valueOf(anneeCotisation.getValue()));
            }
        }

        afact.setLibelle(cotisationCalculee.getCotisation().getAssuranceLibelle(I18NUtil.getUserLocale()));
        afact.setIdRubrique(cotisationCalculee.getCotisation().getAssurance().getRubriqueId());
        afact.setDebutPeriode(decompte.getPeriode().getPeriodeDebutAsSwissValue());
        afact.setFinPeriode(decompte.getPeriode().getPeriodeFinAsSwissValue());
        // afact.setDebutPeriode(anneeCotisation.getFirstDayOfYear().getSwissValue());
        // afact.setFinPeriode(anneeCotisation.getLastDayOfYear().getSwissValue());

        int anneeDebutDecompte = 0;
        int anneeFinDecompte = 0;

        if (!JadeStringUtil.isBlankOrZero(decompte.getPeriodeDebut().getSwissValue())) {
            anneeDebutDecompte = decompte.getPeriodeDebut().getYear();
            if (anneeDebutDecompte < Integer.parseInt(anneeCotisation.toString())) {
                afact.setDebutPeriode(anneeCotisation.getFirstDayOfYear().getSwissValue());
            }
        }

        if (!JadeStringUtil.isBlankOrZero(decompte.getPeriodeFin().getSwissValue())) {
            anneeFinDecompte = decompte.getPeriodeFin().getYear();
            if (anneeFinDecompte > Integer.parseInt(anneeCotisation.toString())) {
                afact.setFinPeriode(anneeCotisation.getLastDayOfYear().getSwissValue());
            }
        }

        int idCaisseMetier = 0;
        Adhesion adh = VulpeculaRepositoryLocator.getAdhesionRepository().findCaisseMetier(decompte.getIdEmployeur());
        if (adh != null) {
            idCaisseMetier = Integer.valueOf(adh.getPlanCaisse().getAdministration().getIdTiers());
        }

        afact.setNumCaisse("" + idCaisseMetier);

        Montant masse = cotisationCalculee.getMontant();
        Montant montantFacture = masse.multiply(cotisationCalculee.getTaux()).normalize();

        if (!rubrique.isNew()) {
            afact.setTauxFacture(String.valueOf(cotisationCalculee.getTaux().getValue()));
            afact.setMasseFacture(JANumberFormatter.fmt(masse.getValueNormalisee(), true, false, true, 2));
            if (idRubriqueForDifferenceRectifie != null
                    && idRubriqueForDifferenceRectifie.equals(rubrique.getIdRubrique())) {
                montantFacture = addMontantRectifie(decompte, montantFacture);
            }
            afact.setMontantFacture(JANumberFormatter.fmt(montantFacture.getValueNormalisee(), true, false, true, 2));
        }

        try {
            if (isAfactAAjouter(masse, rubrique, montantFacture)) {
                afact.add();
            }
        } catch (Exception e) {
            String message = "Decompte : " + decompte.getId() + ", N° d'affilié : "
                    + decompte.getEmployeurAffilieNumero() + ", Cotisation : "
                    + cotisationCalculee.getCotisationLibelle(Locale.FRANCE) + ", Montant : "
                    + cotisationCalculee.getMontantAsValue() + "\r\n" + e.toString();
            LOGGER.error(message);
            getTransaction().addErrors(message);
        }
    }

    /**
     * @param decompte
     * @param montantFacture
     * @return
     */
    private Montant addMontantRectifie(Decompte decompte, Montant montantFacture) {
        if (!decompte.getMontantDifference().isZero()) {
            montantFacture = montantFacture.substract(decompte.getMontantDifference());
        }
        return montantFacture;
    }
}
