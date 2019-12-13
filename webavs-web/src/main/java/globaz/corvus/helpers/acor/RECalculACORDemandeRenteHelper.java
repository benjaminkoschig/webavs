package globaz.corvus.helpers.acor;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import acor.fcalcul.FCalcul;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.corvus.business.services.CorvusCrudServiceLocator;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.corvus.domaine.BaseCalcul;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.DemandeRenteVieillesse;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.corvus.domaine.constantes.CodeCasSpecialRente;
import ch.globaz.corvus.domaine.constantes.EtatDemandeRente;
import ch.globaz.corvus.domaine.constantes.TypeDemandeRente;
import ch.globaz.corvus.utils.rentesverseesatort.RECalculRentesVerseesATort;
import ch.globaz.corvus.utils.rentesverseesatort.REDetailCalculRenteVerseeATort;
import ch.globaz.pyxis.business.services.PyxisCrudServiceLocator;
import ch.globaz.pyxis.domaine.PersonneAVS;
import globaz.commons.nss.NSUtil;
import globaz.corvus.acor.REACORBatchFilePrinter;
import globaz.corvus.acor.parser.REFeuilleCalculVO;
import globaz.corvus.acor.parser.rev09.REACORParser;
import globaz.corvus.acor.parser.rev09.REACORParser.ReturnedValue;
import globaz.corvus.acor.parser.xml.rev10.REACORAnnonceXmlReader;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.api.basescalcul.IREBasesCalcul;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.dao.IREValidationLevel;
import globaz.corvus.dao.REAddRenteAccordee;
import globaz.corvus.dao.REDeleteCascadeDemandeAPrestationsDues;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculManager;
import globaz.corvus.db.ci.RERassemblementCI;
import globaz.corvus.db.ci.RERassemblementCIManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.demandes.REDemandeRenteSurvivant;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.db.demandes.REPeriodeAPI;
import globaz.corvus.db.demandes.REPeriodeAPIManager;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesJointDemandeRente;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesJointDemandeRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATortManager;
import globaz.corvus.db.rentesverseesatort.RECalculRentesVerseesATortManager;
import globaz.corvus.db.rentesverseesatort.wrapper.RECalculRentesVerseesATortConverter;
import globaz.corvus.db.rentesverseesatort.wrapper.RECalculRentesVerseesATortWrapper;
import globaz.corvus.exceptions.REBusinessException;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.helpers.annonces.REAnnoncePonctuelleHelper;
import globaz.corvus.module.calcul.api.REAPICalculateur;
import globaz.corvus.module.calcul.api.REMontantPrestationAPIParPeriode;
import globaz.corvus.regles.REDemandeRegles;
import globaz.corvus.regles.REReglesException;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.beneficiaire.principal.REBeneficiairePrincipal;
import globaz.corvus.utils.codeprestation.enums.RECodePrestationResolver;
import globaz.corvus.vb.acor.RECalculACORDemandeRenteViewBean;
import globaz.corvus.vb.annonces.REAnnoncePonctuelleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRAcorFileContent;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.helpers.PRHybridHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.utils.PRDateUtils;
import globaz.prestation.utils.PRDateUtils.PRDateEquality;
import org.apache.commons.lang.StringUtils;

/**
 * <H1>Description</H1>
 * <p>
 * helper utiliser pour le calcul des demandes de rentes. Les demandes autres que API sont calculée via le logiciel
 * ACOR. Les demandes API sont quant à elle calculée par l'application CORVUS.
 * </p>
 *
 * @author scr
 */
public class RECalculACORDemandeRenteHelper extends PRAbstractHelper {

    /**
     * Cette classe abstraite est la pour factoriser la génération de la clé de comparaison.
     * La clé de comparaison est identique mais généré sur des données différentes si on traite la base de calcul ou la
     * demande de rente
     *
     * @author lga
     */
    private abstract class CleWrapper {

        private TypeDemandeRente typeDemandeRente;
        private String idTiers;
        private boolean ajournement = false;

        public CleWrapper(DemandeRente demandeRente) {
            build(demandeRente);
        }

        public CleWrapper(REBasesCalcul baseCalcul, List<RERenteAccordee> renteAccordees) {
            build(baseCalcul, renteAccordees);
        }

        /**
         * Cette méthode construit les données pour la génération de la clé de comparaison sur la base de la demande de
         * rente
         *
         * @param demandeRente La demande de rente
         */
        private void build(DemandeRente demandeRente) {
            if (demandeRente.getRequerant() == null || demandeRente.getRequerant().getId() == null
                    || demandeRente.getRequerant().getId() == 0) {
                throw new NullPointerException(
                        "Unable to found the idTiersRequerant for DemandeRente with id [" + demandeRente.getId() + "]");
            } else {
                idTiers = demandeRente.getRequerant().getId().toString();
            }

            if (demandeRente.getTypeDemandeRente() == null) {
                throw new NullPointerException(
                        "Unable to found the TypeDemandeRente for DemandeRente with id [" + demandeRente.getId() + "]");
            } else {
                typeDemandeRente = demandeRente.getTypeDemandeRente();
            }

            if (demandeRente instanceof DemandeRenteVieillesse) {
                boolean ajournementDemande = ((DemandeRenteVieillesse) demandeRente).isAvecAjournement();
                String dateRevocationAjournement = ((DemandeRenteVieillesse) demandeRente)
                        .getDateRevocationAjournement();
                boolean dateAjournementVide = JadeStringUtil.isBlankOrZero(dateRevocationAjournement);
                ajournement = ajournementDemande && dateAjournementVide;
            }
        }

        /**
         * Cette méthode construit les données pour la génération de la clé de comparaison sur la base d'une base de
         * calcul et de ses rentes accordées
         *
         * @param baseCalcul     La base de calcul en question
         * @param renteAccordees Les rentes accordées liées à la base de calcul
         */
        private void build(REBasesCalcul baseCalcul, List<RERenteAccordee> renteAccordees) {

            // Résolution de l'idTiers pour la génération de la clé de comparaison
            if (JadeStringUtil.isBlankOrZero(baseCalcul.getIdTiersBaseCalcul())) {
                throw new NullPointerException(
                        "Unable to found the idTiersBaseCalcul for REBaseCalcul with id [" + baseCalcul.getId() + "]");
            } else {
                idTiers = baseCalcul.getIdTiersBaseCalcul();
            }

            // Analyse des rentes accordées (la 1ère) de la base de calcul pour déterminer le type de demande de rente
            // correspondant à la badse de calcul
            if (renteAccordees == null || renteAccordees.size() == 0) {
                throw new NullPointerException("Unable to define the TypeDemandeRente for REBaseCalcul with id ["
                        + baseCalcul.getId() + "] because REREnteAccordee list is null or empty");
            } else {
                RERenteAccordee ra = renteAccordees.get(0);
                // La résolution est fait via la classe RECodePrestationResolver mais le type retourné doit être
                // convertis...
                PRTypeCodePrestation type = RECodePrestationResolver.getGenreDePrestation(ra.getCodePrestation());
                switch (type) {
                    case INVALIDITE:
                        typeDemandeRente = TypeDemandeRente.DEMANDE_INVALIDITE;
                        break;
                    case VIEILLESSE:
                        typeDemandeRente = TypeDemandeRente.DEMANDE_VIEILLESSE;
                        break;
                    case SURVIVANT:
                        typeDemandeRente = TypeDemandeRente.DEMANDE_SURVIVANT;
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "Unable to define the RETypeDemandeRente of the REBasesCalcul with id ["
                                        + baseCalcul.getId() + "]. The first RERenteAccordee with id ["
                                        + ra.getIdPrestationAccordee() + "] has an unknow codePrestation ["
                                        + ra.getCodePrestation() + "]");
                }
            }

            // Analyse de la'journement de la base de calcul
            ajournement = hasCodeCasSpecial08(renteAccordees);

        }

        public boolean hasCodeCasSpecial08(List<RERenteAccordee> renteAccordees) {
            for (RERenteAccordee ra : renteAccordees) {
                if (hasCodeCasSpecial(ra, "08")) {
                    return true;
                }
            }
            return false;
        }

        public boolean isAjournement() {
            return ajournement;
        }

        public TypeDemandeRente getTypeDemandeRente() {
            return typeDemandeRente;
        }

        public String getCleDeComparaison() {
            StringBuilder sb = new StringBuilder();
            sb.append(typeDemandeRente.toString());
            sb.append("-");
            if (!TypeDemandeRente.DEMANDE_SURVIVANT.equals(typeDemandeRente)) {
                sb.append(idTiers);
                sb.append("-");
            }
            sb.append(ajournement);
            return sb.toString();
        }

        protected boolean hasCodeCasSpecial(RERenteAccordee renteAccordee, String codeCasSpecialRecherche) {
            return codeCasSpecialRecherche.equals(renteAccordee.getCodeCasSpeciaux1())
                    || codeCasSpecialRecherche.equals(renteAccordee.getCodeCasSpeciaux2())
                    || codeCasSpecialRecherche.equals(renteAccordee.getCodeCasSpeciaux3())
                    || codeCasSpecialRecherche.equals(renteAccordee.getCodeCasSpeciaux4())
                    || codeCasSpecialRecherche.equals(renteAccordee.getCodeCasSpeciaux5());
        }
    }

    private class DemandeRenteWrapper extends CleWrapper implements Comparable<DemandeRenteWrapper> {
        private DemandeRente demandeRente;

        public DemandeRenteWrapper(DemandeRente demandeRente) {
            super(demandeRente);
            this.demandeRente = demandeRente;
        }

        public DemandeRente getDemandeRente() {
            return demandeRente;
        }

        @Override
        public int compareTo(DemandeRenteWrapper demande) {
            PRDateEquality result = null;
            try {
                result = PRDateUtils.compare(getDemandeRente().getDateDebutDuDroitInitial(),
                        demande.getDemandeRente().getDateDebutDuDroitInitial());
            } catch (Exception e) {
                return 0;
            }
            switch (result) {
                case BEFORE:
                    return -1;
                case EQUALS:
                    return 0;
                case AFTER:
                    return 1;
                case INCOMPARABLE:

                default:
                    break;
            }
            return 0;
        }

    }

    private class BaseCalculWrapper extends CleWrapper {
        private REBasesCalcul basesCalcul;
        private List<RERenteAccordee> renteAccordees;

        public BaseCalculWrapper(REBasesCalcul basesCalcul, List<RERenteAccordee> renteAccordees) {
            super(basesCalcul, renteAccordees);
            this.basesCalcul = basesCalcul;
            this.renteAccordees = renteAccordees;
        }

        public REBasesCalcul getBasesCalcul() {
            return basesCalcul;
        }

        public boolean hasCodeCasSpecial08() {
            for (RERenteAccordee ra : renteAccordees) {
                if (hasCodeCasSpecial(ra, "08")) {
                    return true;
                }
            }
            return false;
        }
    }

    private static final String DATE_FIN_DEMANDE = "01.01.1000";

    private static final String DATE_DEBUT_DEMANDE = "31.12.9999";

    // Clé pour le traitement des annonces ponctuelles
    class KeyAP {
        public String genreRente = "";
        public String idTiers = "";
    }

    // Valeur pour le traitement des annonces ponctuelles
    class ValueAP {
        String ancienMontantRA = "";
        String ancienRAM = "";
        String idRA = "";
        List<String> idsRCI = new ArrayList<String>();
        String nouveauMontantRA = "";
        String nouveauRAM = "";
    }

    /**
     * Met à jours le champ anneeMontantRam selon le cas identifié ci-dessus. Rentes en cours : [ ] = RA (date début et
     * date de fin) x = date courante Cas 1) rentes en cours [ x ] Année montant du RAM = année courant Cas 2) rentes
     * dans le futur x [ ] Année montant du RAM = année début de la RA Cas 3) rentes rétréoactives [ ] x Année montant
     * du RAM = année(date de fin)
     */
    public static String computeAnneeMontantRAM(final RERenteAccordee ra, final BSession session) throws JAException {

        // date du dernier paiement mensuel comme ref.
        JADate dateDernierPaiement = new JADate(REPmtMensuel.getDateDernierPmt(session));

        JADate dateDebut = null;
        JADate dateFin = null;

        if (!JadeStringUtil.isEmpty(ra.getDateFinDroit())) {
            dateFin = new JADate(ra.getDateFinDroit());
        }

        if (!JadeStringUtil.isEmpty(ra.getDateDebutDroit())) {
            dateDebut = new JADate(ra.getDateDebutDroit());
        }

        int identificationCas = 1; // par défaut

        JACalendarGregorian cal = new JACalendarGregorian();

        int resultComparaison1 = cal.compare(dateDernierPaiement, dateFin);
        int resultComparaison2 = cal.compare(dateDernierPaiement, dateDebut);
        // Cas 1 : rentes en cours
        // Si pas de date de fin, ou que date de fin après date courante...
        if (((dateFin == null) || (JACalendar.COMPARE_FIRSTLOWER == resultComparaison1)
                || (JACalendar.COMPARE_EQUALS == resultComparaison1))
                && ((dateDebut != null) && ((JACalendar.COMPARE_FIRSTUPPER == resultComparaison2)
                || (JACalendar.COMPARE_EQUALS == resultComparaison2)))) {
            identificationCas = 1;
        }
        // Cas 2 : rentes dans le futur
        else if ((dateDebut != null) && (JACalendar.COMPARE_FIRSTLOWER == resultComparaison2)) {
            identificationCas = 2;
        } else if ((dateFin != null) && (JACalendar.COMPARE_FIRSTUPPER == resultComparaison1)) {
            identificationCas = 3;
        } else {
            throw new PRACORException(session.getLabel("ERREUR_ANNEE_MNT_RAM"));
        }

        switch (identificationCas) {

            // Cas 1) rentes en cours
            // Année montant du RAM = année courant
            case 1:

                // mm.aaaa
                JADate dateDerPmt = new JADate(REPmtMensuel.getDateDernierPmt(session));
                return String.valueOf(dateDerPmt.getYear());

            // Cas 2) rentes dans le futur
            // Année montant du RAM = année début de la RA
            case 2:
                JADate date = new JADate(ra.getDateDebutDroit());
                return String.valueOf(date.getYear());

            // Cas 3) rentes rétréoactives
            // Année montant du RAM = année(date de fin)
            case 3:
                date = new JADate(ra.getDateFinDroit());
                return String.valueOf(date.getYear());
            default:
                return null;
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private static DemandeRente copierDemandePourLaRenteAccordee(final Set<DemandeRente> demandesDeLaFamille,
                                                                 final RenteAccordee uneRenteAvecCodeCasSpecial) {
        PersonneAVS donneurDeDroitRenteAccordee = uneRenteAvecCodeCasSpecial.getBaseCalcul().getDonneurDeDroit();

        DemandeRente copie = null;

        for (DemandeRente uneDemandeDeLaFamille : demandesDeLaFamille) {
            if ((copie == null) && uneDemandeDeLaFamille.getRequerant().equals(donneurDeDroitRenteAccordee)) {

                switch (uneDemandeDeLaFamille.getEtat()) {
                    case ENREGISTRE:
                    case AU_CALCUL:
                    case CALCULE:
                        if (uneRenteAvecCodeCasSpecial.correspondAuTypeDeLaDemande(uneDemandeDeLaFamille)) {
                            copie = uneDemandeDeLaFamille;
                        }
                        break;

                    case COURANT_VALIDE:
                    case TERMINE:
                    case TRANSFERE:
                    case VALIDE:
                        if (uneDemandeDeLaFamille.estEnCours()) {
                            copie = CorvusServiceLocator.getDemandeRenteService().copier(uneDemandeDeLaFamille);
                        }
                        break;

                }
            }
        }

        return copie;
    }

    /**
     * <p>
     * Sépare la demande en deux demandes distinctes si besoin.
     * </p>
     * <p>
     * Si des demandes à l'état calculé sont trouvées, elles seront utilisées tel quel, sans copie.
     * </p>
     * <p>
     * Si la/les rente(s) à relocaliser est/sont au bénéfice du requérant de la demande initiale, la demande initiale
     * sera copier afin d'y attacher la rente à relocaliser. Si ce n'est pas le cas, une demande sera recherchée dans
     * les demandes de la famille (recherche : bénéficiaire de la rente à relocalisée = requérant de la demande et type
     * de rente match type de demande) <br />
     * Ce sera cette demande qui sera copiée.
     * </p>
     *
     * @param demande             la demande contenant des rentes possédant et ne possédant pas le code cas spécial 07 / 08 et
     *                            nécessitant donc une séparation en deux
     * @param demandesDeLaFamille les demandes de la famille, une de ces demandes sera copiée et retournée après que les rentes aient
     *                            été rattachées à cette demande
     * @param codeCasSpecial      le code cas spécial determinant si une demande doit être séparée en deux
     * @return la nouvelle demande qui a été créé depuis la séparation (n'est pas encore en BDD), ou la demande initiale
     * s'il n'y a pas eu lieu de copier une demande (les demandes étaient en état calculé)
     * @throws REBusinessException      si le donneur de droit d'une base de calcul (tiers BC) est différent du requérant et qu'aucune
     *                                  demande n'est au nom de ce tiers base calcul dans les demandes de la famille
     * @throws IllegalArgumentException si aucune rente n'est présente dans la demande
     */
    static DemandeRente repartirLesRentesPourCodeCasSpecial(final DemandeRente demande,
                                                            final Set<DemandeRente> demandesDeLaFamille, final CodeCasSpecialRente codeCasSpecial)
            throws REBusinessException {

        Checkers.checkNotNull(demande, "demande");
        Checkers.checkNotEmpty(demande.getRentesAccordees(), "demandesDeLaFamille");
        Checkers.checkNotNull(demandesDeLaFamille, "demandesDeLaFamille");
        Checkers.checkNotNull(codeCasSpecial, "codeCasSpecial");

        Set<RenteAccordee> rentesAccordeesSansCodeCasSpecial = new HashSet<RenteAccordee>();
        rentesAccordeesSansCodeCasSpecial.addAll(demande.filtrerRentesAccordeesSansCodeCasSpecial(codeCasSpecial));

        Set<RenteAccordee> rentesAccordeesAvecCodeCasSpecial = new HashSet<RenteAccordee>();
        rentesAccordeesAvecCodeCasSpecial.addAll(demande.filtrerRentesAccordeesAvecCodeCasSpecial(codeCasSpecial));

        // recherche de la rente à copier
        DemandeRente nouvelleDemande = null;

        /*
         * on parcours les bases de calculs des rentes de la demande, s'il n'y a qu'une seule base de calcul une simple
         * copie de la demande initiale est suffisante
         */
        Map<BaseCalcul, Set<RenteAccordee>> rentesParBasesDeCalcul = new HashMap<BaseCalcul, Set<RenteAccordee>>();
        for (RenteAccordee uneRenteDeLaDemande : demande.getRentesAccordees()) {

            if (rentesParBasesDeCalcul.containsKey(uneRenteDeLaDemande.getBaseCalcul())) {
                rentesParBasesDeCalcul.get(uneRenteDeLaDemande.getBaseCalcul()).add(uneRenteDeLaDemande);
            } else {
                rentesParBasesDeCalcul.put(uneRenteDeLaDemande.getBaseCalcul(),
                        new HashSet<RenteAccordee>(Arrays.asList(uneRenteDeLaDemande)));
            }
        }

        /*
         * si plus d'une base de calcul, on vérifie s'il y a bien une demande pour chaque base de calcul (car on doit
         * copier la demande dont le requérant correspond au tiers base de calcul
         */
        if (rentesParBasesDeCalcul.keySet().size() > 1) {

            for (BaseCalcul uneBaseCalcul : rentesParBasesDeCalcul.keySet()) {

                /*
                 * si le donneur de droit de cette base de calcul est différent du tiers requérant, on doit trouver une
                 * demande à son nom afin de pouvoir la copier par la suite
                 */
                if (!uneBaseCalcul.getDonneurDeDroit().equals(demande.getDossier().getRequerant())) {

                    /*
                     * on parcours les demandes de la famille, si on ne trouve pas de demande correspondant à ce tiers
                     * base de calcul, on lève une exception
                     */
                    boolean uneDemandeEstAuNomDuDonneurDeDroit = false;
                    for (DemandeRente uneDemandeDeLaFamille : demandesDeLaFamille) {
                        if (uneDemandeDeLaFamille.getDossier().getRequerant()
                                .equals(uneBaseCalcul.getDonneurDeDroit())) {
                            uneDemandeEstAuNomDuDonneurDeDroit = true;
                        }
                    }
                    /*
                     * Si aucune demande n'est au nom du donneur de droit, un message d'erreur doit apparaître. Il est
                     * ici intentionnellement vide pour permettre de tester unitairement ce bout de code (sinon appel à
                     * BSession.getLabel qui empêche d'être indépendant de Jade pour le test unitaire). Le message sera
                     * assigné dans la fonction appelante
                     */
                    if (!uneDemandeEstAuNomDuDonneurDeDroit) {
                        throw new REBusinessException("");
                    }
                }
            }

            /*
             * on parcours les rentes avec le code cas spécial, et on regarde si la demande initiale est au nom du
             * donneur de droit de cette rente (tiers base de calcul). Si ce n'est pas le cas, on recherche une demande
             * à copier pour rattacher cette rente avec code cas spécial
             */
            for (RenteAccordee uneRenteAvecCodeCasSpecial : rentesAccordeesAvecCodeCasSpecial) {
                if (!uneRenteAvecCodeCasSpecial.getBaseCalcul().getDonneurDeDroit().equals(demande.getRequerant())
                        && (nouvelleDemande == null)) {
                    nouvelleDemande = RECalculACORDemandeRenteHelper
                            .copierDemandePourLaRenteAccordee(demandesDeLaFamille, uneRenteAvecCodeCasSpecial);

                    if (nouvelleDemande != null) {
                        // on retire la demande avec code cas spécial de la demande initiale
                        demande.setRentesAccordees(RECalculACORDemandeRenteHelper
                                .retirerRenteAccordee(rentesAccordeesAvecCodeCasSpecial, uneRenteAvecCodeCasSpecial));
                        // rattachement de la rente avec code cas spécial à la demande fraîchement copiée
                        nouvelleDemande.setRentesAccordees(Arrays.asList(uneRenteAvecCodeCasSpecial));
                    }
                }
            }

            /*
             * si rien n'a encore été trouvé, c'est que le donneur de droit (tiers base de calcul) de la rente avec code
             * cas spécial est le requérant et sa rente restera donc attachée à la demande initiale. Il nous faut
             * maintenant trouver une demande où attacher les rentes sans code cas spécial, car elles ne doivent pas
             * rester sur la demande initiale où il y a des rentes avec code cas spécial
             */
            if (nouvelleDemande == null) {
                if (rentesAccordeesSansCodeCasSpecial.size() == 0) {
                    // s'il n'y a que des rentes avec code cas spécial, on garde la demande intiale sans copier de
                    // demande
                    nouvelleDemande = demande;
                } else {
                    for (RenteAccordee uneRenteSansCodeCasSpecial : rentesAccordeesSansCodeCasSpecial) {
                        if (!uneRenteSansCodeCasSpecial.getBaseCalcul().getDonneurDeDroit()
                                .equals(demande.getRequerant()) && (nouvelleDemande == null)) {
                            nouvelleDemande = RECalculACORDemandeRenteHelper
                                    .copierDemandePourLaRenteAccordee(demandesDeLaFamille, uneRenteSansCodeCasSpecial);

                            if (nouvelleDemande != null) {
                                // on garde les rentes avec code cas spécial sur la demande initiale
                                demande.setRentesAccordees(rentesAccordeesAvecCodeCasSpecial);
                                // rattachement des rentes sans code cas spéciaux à la demande fraîchement copiée
                                nouvelleDemande.setRentesAccordees(rentesAccordeesSansCodeCasSpecial);
                            }
                        }
                    }
                }
            }
        }

        /*
         * si après les deux étapes du dessus aucune demande n'a été trouvée pour être copiée, ou s'il n'y avait qu'une
         * seule base de calcul entre les différentes rente accordées de la demande, c'est que la demande initiale est
         * la seule demande actuellement en cours pour cette famille, on la copie donc
         */
        if (nouvelleDemande == null) {
            nouvelleDemande = CorvusServiceLocator.getDemandeRenteService().copier(demande);

            // on garde les rentes sans code cas spécial à la demande intiale
            demande.setRentesAccordees(rentesAccordeesSansCodeCasSpecial);
            // rattachement des rentes avec code cas spéciaux à la demande fraîchement copiée
            nouvelleDemande.setRentesAccordees(rentesAccordeesAvecCodeCasSpecial);
        }

        // la date de traitement doit être la même que la demande initiale
        nouvelleDemande.setDateTraitement(demande.getDateTraitement());
        nouvelleDemande.setEtat(EtatDemandeRente.CALCULE);

        return nouvelleDemande;
    }

    /**
     * <p>
     * Réparti les rentes présentes sur la demande sur laquelle on travail dans les demandes non validées de la famille
     * (si cela est possible) afin que les codes prestation des rentes correspondent le mieux aux types des demandes.
     * </p>
     * <p>
     * Exemple :<br/>
     * On importe le calcul sur une demande d'invalidité au nom de la mère. Le père étant décédé, les enfants reçoivent
     * une rente d'orphelin. Vu que l'on recalcul le toute, les rentes complémentaire AI et d'orphelin des enfants
     * seront toutes importées sur la demande d'invalidité de la mère : les rentes d'orphelin seront donc sur une
     * demande ne correspondant pas à leur genre de prestation.<br/>
     * Cette méthode doit vérifier l'existence d'une demande de survivant non validée (au nom du père décédé, ou de la
     * mère) sur laquelle il sera possible d'y rattacher les rentes d'orphelin. Seule une rente non-validée peut être
     * utilisée, comme si une importation de calcul depuis ACOR était en cours.
     * </p>
     * <p>
     * Cette méthode ne contient que la logique métier, mais pas la persitence, et ce pour pouvoir la tester dans un
     * test unitaire. Pensez à update toutes les demandes après utilisation pour sauver les modifications apportées par
     * cette méthode sur les demandes.
     * </p>
     *
     * @param demandeSurLaquelleOnTravail    une demande, à l'état calculé, contenant des rentes dont le code prestation ne
     *                                       correspondant pas au type de la demande
     * @param demandesNonValideesDeLaFamille la liste des demandes non validées de la famille du requérant de la demande
     *                                       sur laquelle on travail
     */
    static void repartirLesRentesSelonLesTypesDesDemandes(DemandeRente demandeSurLaquelleOnTravail,
                                                          Set<DemandeRente> demandesNonValideesDeLaFamille) {

        for (RenteAccordee uneRenteADeplacer : demandeSurLaquelleOnTravail
                .filtrerRentesAccordeesNeCorrespondantPasAuTypeDeLaDemande()) {

            boolean deplacementFait = false;

            for (DemandeRente uneDemandeNonValideeDeLaFamille : demandesNonValideesDeLaFamille) {
                if (!deplacementFait && uneDemandeNonValideeDeLaFamille.codesPrestationsAcceptesPourCeTypeDeDemande()
                        .contains(uneRenteADeplacer.getCodePrestation())) {
                    demandeSurLaquelleOnTravail.retirerRenteAccordee(uneRenteADeplacer);
                    uneDemandeNonValideeDeLaFamille.ajouterRenteAccordee(uneRenteADeplacer);

                    // on passe la demande à l'état calculé, pour pouvoir ensuite préparer la/les décision(s)
                    uneDemandeNonValideeDeLaFamille.setEtat(EtatDemandeRente.CALCULE);

                    deplacementFait = true;
                }
            }
        }
    }

    private static Collection<RenteAccordee> retirerRenteAccordee(final Collection<RenteAccordee> rentesAccordees,
                                                                  final RenteAccordee uneRente) {
        Collection<RenteAccordee> listeSansLaRente = new ArrayList<RenteAccordee>();

        for (RenteAccordee uneRenteAccordeeDeLaListe : rentesAccordees) {
            if (!uneRenteAccordeeDeLaListe.equals(uneRente)) {
                listeSansLaRente.add(uneRenteAccordeeDeLaListe);
            }
        }

        return listeSansLaRente;
    }

    final int CAS_NOUVEAU_CALCUL = 1;

    final int CAS_RECALCUL_DEMANDE_NON_VALIDEE = 3;

    final int CAS_RECALCUL_DEMANDE_VALIDEE = 2;

    private final Map<String, String> mapDemandeReset = new HashMap<>();

    private final Set<String> rentesWithoutBte = new HashSet<>();

    /**
     * DOCUMENT ME!
     *
     * @param viewBean DOCUMENT ME!
     * @param action   DOCUMENT ME!
     * @param session  DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    public FWViewBeanInterface actionCalculerAPI(final FWViewBeanInterface viewBean, final FWAction action,
                                                 final BSession session) throws Exception {

        RECalculACORDemandeRenteViewBean caViewBean = (RECalculACORDemandeRenteViewBean) viewBean;
        BITransaction transaction = null;

        try {
            transaction = (session).newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            PRTiersWrapper tierBeneficiaire44 = PRTiersHelper.getTiersAdresseDomicileParId(session,
                    caViewBean.getIdTiers(), JACalendar.todayJJsMMsAAAA());
            if (tierBeneficiaire44 == null) {
                throw new REBusinessException(session.getLabel("CALCUL_RENTE_API_ADRESSE_DOMICILE_MANDATORY"));
            }

            JACalendar cal = new JACalendarGregorian();

            REDemandeRenteAPI api = new REDemandeRenteAPI();
            api.setSession(session);
            api.setIdDemandeRente(caViewBean.getIdDemandeRente());
            api.retrieve(transaction);

            if (api.isNew()) {
                throw new Exception("Error: Demande API not found for idDemAPI : " + caViewBean.getIdDemandeRente());
            }

            if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(api.getCsEtat())
                    || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(api.getCsEtat())
                    || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(api.getCsEtat())) {

                throw new Exception("Error: impossible de calculer une demande API Validée.");
            }

            if (!IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(api.getCsTypeDemandeRente())) {
                throw new Exception("Error: Le type de la demande doit être API.");
            }

            // Plausi, vérifier que tous les membres de la famille ont une date
            // de naissance et un sexe
            // sauf pour le conjoint inconnu
            // Rechercher les membres de la famille (Enfants & Conjoints)
            globaz.hera.api.ISFSituationFamiliale sfa = SFSituationFamilialeFactory.getSituationFamiliale(session,
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, caViewBean.getIdTiers());
            ISFMembreFamilleRequerant[] mfs = sfa.getMembresFamille(caViewBean.getIdTiers());

            for (int i = 0; (mfs != null) && (i < mfs.length); i++) {
                ISFMembreFamilleRequerant mf = mfs[i];

                if ((JadeStringUtil.isBlankOrZero(mf.getDateNaissance())
                        || JadeStringUtil.isBlankOrZero(mf.getCsSexe()))
                        && !mf.getIdMembreFamille().equals("999999999999")) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(
                            mf.getNom() + " " + mf.getPrenom() + session.getLabel("ERREUR_DATE_NAISS_SEXE_DEFINI"));
                }
            }

            REAPICalculateur calculateur = new REAPICalculateur(session);
            // Tableau ordonnée par type de prestation + date début, ordre
            // croissant.
            REMontantPrestationAPIParPeriode[] montantsPrstAPI = calculateur.calculerPrestationAPI(api,
                    caViewBean.getIdTiers());

            PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, caViewBean.getIdTiers());
            String dateDeces = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES);

            // Récupération des périodes API
            REPeriodeAPIManager mgr1 = new REPeriodeAPIManager();
            mgr1.setSession(session);
            mgr1.setForIdDemandeRente(api.getIdDemandeRente());
            mgr1.setOrderBy(REPeriodeAPI.FIELDNAME_DATE_DEBUT_INVALIDITE + " DESC");
            // Les périodes sont ordonnées par date de début d'invalidité (ordre
            // décroissant)
            // On prend la plus récente...

            String lastDateFinPeriodeAPI = null;
            String lastDateDebutPeriodeAPI = null;
            mgr1.find(transaction, 1);
            if (!mgr1.isEmpty()) {
                REPeriodeAPI periode = (REPeriodeAPI) mgr1.getEntity(0);
                lastDateFinPeriodeAPI = periode.getDateFinInvalidite();
                lastDateDebutPeriodeAPI = periode.getDateDebutInvalidite();
            }

            PRDemande demande = new PRDemande();
            demande.setSession(session);
            demande.setIdDemande(api.getIdDemandePrestation());
            demande.retrieve();

            // Calcul de la date de retraite
            PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, demande.getIdTiers());

            String dateFin = null;
            String dateDebut = null;

            JADate jaDD = new JADate("31.12.2099");
            JADate jaDF = new JADate("01.01.1900");

            for (int i = 0; i < montantsPrstAPI.length; i++) {
                JADate dd = new JADate(montantsPrstAPI[i].getDateDebut());
                JADate df = null;

                if (!JadeStringUtil.isBlankOrZero(montantsPrstAPI[i].getDateFin())) {
                    df = new JADate(montantsPrstAPI[i].getDateFin());
                } else {
                    jaDF = null;
                }

                if (JACalendar.COMPARE_FIRSTUPPER == cal.compare(jaDD, dd)) {
                    jaDD = dd;
                }

                if ((jaDF != null) && (JACalendar.COMPARE_FIRSTLOWER == cal.compare(jaDF, df))) {
                    jaDF = df;
                }
            }

            dateDebut = jaDD.toStr(".");
            if (jaDF == null) {
                dateFin = null;
            } else {
                dateFin = jaDF.toStr(".");
            }

            if (!JadeStringUtil.isBlankOrZero(dateDeces)) {
                if (dateFin == null) {
                    dateFin = dateDeces;
                } else {
                    if (BSessionUtil.compareDateFirstGreater(session, dateFin, dateDeces)) {
                        dateFin = dateDeces;
                    }
                }
            }

            // --------------------------------------------------------------------------------------------------------------------
            // Cas de figure :
            //
            // 1) CAS_NOUVEAU_CALCUL (Standard)
            // ====================================================================================================================
            // ====================================================================================================================
            // Demande source (ENREGISTRE) --> Calculer
            //
            // Résutalt :
            // =============================
            //
            // Demande source
            // |__________ Base Calcul
            // |__________ RA1
            //
            //
            // 2) CAS_RECALCUL_DEMANDE_VALIDEE (Recalcul à partir d'une demande
            // existante, VALIDE ou PAYE)
            // ====================================================================================================================
            // ====================================================================================================================
            //
            // Demande source#1 (PAYE ou VALIDE) --> Calculer
            // |__________ Base Calcul
            // |__________ RA1
            //
            //
            // Résutalt :
            // =============================
            // On va créer une nouvelle demande.
            //
            // Demande source#1
            // |__________ Base Calcul
            // |__________ RA1
            //
            //
            // Demande #2 (idParent==Demande source#1)
            // |__________ Base Calcul
            // |__________ RA3
            //
            //
            //
            // Les rentes accordées RA3 vont annuler RA1. Ce traitement n'est
            // pas automatisé
            // pour le moment.
            //
            // 3) CAS_RECALCUL_DEMANDE_NON_VALIDEE (Recalcul à partir d'une
            // demande existante, non encore validée. ENREGISTRE, AU CALCUL)
            // ====================================================================================================================
            // ====================================================================================================================
            //
            // Demande source#1 (CALCULE) --> Calculer
            // |__________ Base Calcul 1
            // |__________ RA1
            //
            //
            // Résutalt :
            // =============================
            // A l'importation des données de ACOR, on va créer les BC, RA... et
            // supprimer les anciennes, si existantent.
            //
            // Demande source#1
            // |__________ Base Calcul 2
            // |__________ RA3
            //
            // --------------------------------------------------------------------------------------------------------------------

            // Identification du cas à traiter :

            int noCasATraiter = 0;

            if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE.equals(api.getCsEtat())
                    || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL.equals(api.getCsEtat())) {

                noCasATraiter = CAS_NOUVEAU_CALCUL;
            } else if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(api.getCsEtat())) {
                noCasATraiter = CAS_RECALCUL_DEMANDE_VALIDEE;
            } else if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE.equals(api.getCsEtat())) {
                noCasATraiter = CAS_RECALCUL_DEMANDE_NON_VALIDEE;
            } else {
                throw new PRACORException(session.getLabel("ERREUR_RECALCUL_CAS"));
            }

            String idBaseCalcul = doTraiterBaseCalcul(session, (BTransaction) transaction, api, noCasATraiter);

            String previousTypePrestations = "";
            String previousGenreDroitApi = "";
            String currentTypePrestations = "";
            String currentGenreDroitApi = "";
            String currentIdPeriode = "";
            String previousIdPeriode = "";

            // On crée une nouvelle RA pour chaque nouveau type(genre) de
            // prestation.
            RERenteAccordee ra = null;

            String ddRetro = null;
            String dfRetro = null;

            boolean isLastPeriodeForRA = false;
            BigDecimal montantTotalRetro = new BigDecimal(0);
            BigDecimal montantRetroPeriode = new BigDecimal(0);
            int nombreMoisRetro = 0;

            List<Long> idsRA = new LinkedList<Long>();

            String lastPmtjjmmaaaa = REPmtMensuel.getDateDernierPmt(session);
            JADate d = new JADate("01." + lastPmtjjmmaaaa);
            int dd = cal.daysInMonth(d.getMonth(), d.getYear());
            lastPmtjjmmaaaa = String.valueOf(dd) + "." + lastPmtjjmmaaaa;

            ArrayList<REMontantPrestationAPIParPeriode> newTable = new ArrayList<REMontantPrestationAPIParPeriode>();
            // Si date de décès, supprimer les périodes qui commencent après
            // cette date !!
            if (!JadeStringUtil.isBlankOrZero(dateDeces)) {
                for (int i = 0; i < montantsPrstAPI.length; i++) {
                    if (cal.compare(montantsPrstAPI[i].getDateDebut(), dateDeces) != JACalendar.COMPARE_FIRSTUPPER) {
                        // BZ 7712
                        // La date de décès n'est pas null et pas de date de fin
                        if (JadeStringUtil.isBlankOrZero(montantsPrstAPI[i].getDateFin())) {
                            montantsPrstAPI[i].setDateFin(dateDeces);
                        }
                        // Si la date de fin de la période est supérieure à la date de décès on modifie la date de fin
                        if (cal.compare(montantsPrstAPI[i].getDateFin(), dateDeces) != JACalendar.COMPARE_FIRSTLOWER) {
                            montantsPrstAPI[i].setDateFin(dateDeces);
                        }

                        newTable.add(montantsPrstAPI[i]);
                    }
                }
                montantsPrstAPI = newTable.toArray(new REMontantPrestationAPIParPeriode[newTable.size()]);
            }

            String dateFinBackup = dateFin;
            String lastDateFinPeriodeAPIBackup = lastDateFinPeriodeAPI;

            for (int i = 0; i < montantsPrstAPI.length; i++) {

                dateFin = dateFinBackup;
                lastDateFinPeriodeAPI = lastDateFinPeriodeAPIBackup;

                boolean isAPIAI = false;
                boolean isDateEcheance = false;
                boolean isDateFinEcheance = false;
                JADate dateEcheance = null;
                JADate dateFinEcheance = null;

                if (IREDemandeRente.CS_GENRE_DROIT_API_API_AI_RENTE
                        .equalsIgnoreCase(montantsPrstAPI[i].getCsGenreDroitApi())
                        || IREDemandeRente.CS_GENRE_DROIT_API_API_AI_PRST
                        .equalsIgnoreCase(montantsPrstAPI[i].getCsGenreDroitApi())) {
                    isAPIAI = true;

                    if (null != tiers) {
                        // Si homme
                        if (tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE).equals(PRACORConst.CS_HOMME)) {
                            dateEcheance = cal.addYears(
                                    new JADate(tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE)), 65);
                            // Si femme
                        } else {
                            dateEcheance = cal.addYears(
                                    new JADate(tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE)), 64);
                        }
                    }

                    // dateEcheance plus petite ou égale à la date du dernier
                    // paiement mensuel ?
                    if (null != dateEcheance) {
                        dateEcheance = new JADate(cal.lastInMonth(dateEcheance.toStr(".")));
                        JADate dateDerPmt = new JADate(REPmtMensuel.getDateDernierPmt(session));

                        if (cal.compare(dateEcheance, dateDerPmt) == JACalendar.COMPARE_SECONDUPPER) {
                            isDateFinEcheance = true;
                            dateFinEcheance = dateEcheance;
                        } else {
                            isDateEcheance = true;
                        }
                    }
                }

                // Si api AI et date de fin d'échéance
                if (isAPIAI && isDateFinEcheance) {
                    // si datefinvide ou si datefin > datefinecheance
                    if (JadeStringUtil.isBlankOrZero(dateFin)
                            || (cal.compare(new JADate(dateFin), dateFinEcheance) == JACalendar.COMPARE_FIRSTUPPER)) {
                        dateFin = dateFinEcheance.toStr(".");
                        lastDateFinPeriodeAPI = dateFinEcheance.toStr(".");
                    }
                }

                // MAJ des date de début/fin de la demande de rente.
                // Récupération de la 1ère date de début.
                if (montantsPrstAPI != null) {

                    api.setDateDebut(dateDebut);

                    if (JadeStringUtil.isBlankOrZero(lastDateFinPeriodeAPI)
                            && JadeStringUtil.isBlankOrZero(dateDeces)) {
                        api.setDateFin(null);
                    } else {
                        api.setDateFin(dateFin);
                    }
                }

                if (dateDebut.equals("31.12.2099")) {
                    api.setDateDebut(null);
                }

                if (dateFin != null) {
                    if (dateFin.equals("01.01.1900")) {
                        api.setDateFin(null);
                    }
                }

                // Ne pas traiter les périodes qui ont une date de début
                // supérieure ou égale à la date de fin selon l'échéance
                // retraite
                if ((isDateFinEcheance && (cal.compare(dateFinEcheance,
                        new JADate(montantsPrstAPI[i].getDateDebut())) != JACalendar.COMPARE_SECONDLOWER))
                        || (isDateEcheance && (cal.compare(dateEcheance,
                        new JADate(montantsPrstAPI[i].getDateDebut())) != JACalendar.COMPARE_SECONDLOWER))) {
                    continue;
                }

                // Si la date de fin d'échéance est contenu dans la période en
                // cours, modifier la date de fin
                if (isDateFinEcheance) {
                    // si pas de date de fin et datedébut plus petite que date
                    // d'échéance
                    if (JadeStringUtil.isBlankOrZero(montantsPrstAPI[i].getDateFin())
                            && (cal.compare(montantsPrstAPI[i].getDateDebut(),
                            dateFinEcheance.toStr(".")) == JACalendar.COMPARE_FIRSTLOWER)) {
                        montantsPrstAPI[i].setDateFin(dateFinEcheance.toStr("."));
                        // Si date de fin plus grande que date échéance et
                        // datedébut plus petite que date d'échéance
                    } else if ((cal.compare(montantsPrstAPI[i].getDateFin(),
                            dateFinEcheance.toStr(".")) == JACalendar.COMPARE_FIRSTUPPER)
                            && (cal.compare(montantsPrstAPI[i].getDateDebut(),
                            dateFinEcheance.toStr(".")) == JACalendar.COMPARE_FIRSTLOWER)) {
                        montantsPrstAPI[i].setDateFin(dateFinEcheance.toStr("."));
                    }
                }

                currentTypePrestations = montantsPrstAPI[i].getTypePrestation();
                currentGenreDroitApi = montantsPrstAPI[i].getCsGenreDroitApi();
                currentIdPeriode = montantsPrstAPI[i].getIdPeriode();

                REPrestationDue prstDue = null;
                // Nouveau type de prestation

                JADate dfP1 = new JADate(montantsPrstAPI[i].getDateFin());
                JADate ddP2 = new JADate("31.12.2999");

                JADate ddP1 = new JADate(montantsPrstAPI[i].getDateDebut());
                JADate dfP0 = new JADate(DATE_FIN_DEMANDE);

                if (i > 0) {
                    dfP0 = new JADate(montantsPrstAPI[i - 1].getDateFin());
                }

                if (!(i == (montantsPrstAPI.length - 1))) {
                    ddP2 = new JADate(montantsPrstAPI[i + 1].getDateDebut());
                }

                dfP1 = cal.addDays(dfP1, 1);
                dfP0 = cal.addDays(dfP0, 1);
                boolean isNextPeriodeContigue = false;
                boolean isPreviousPeriodeContigue = false;
                if (cal.compare(dfP1, ddP2) == JACalendar.COMPARE_EQUALS) {
                    isNextPeriodeContigue = true;
                }

                if (cal.compare(dfP0, ddP1) == JACalendar.COMPARE_EQUALS) {
                    isPreviousPeriodeContigue = true;
                }

                boolean isPreviousTypePrestationEqualCurrentTypePrestation = currentTypePrestations
                        .equalsIgnoreCase(previousTypePrestations);
                boolean isPreviousGenreDroitApiEqualCurrentGenreDroitApi = currentGenreDroitApi
                        .equalsIgnoreCase(previousGenreDroitApi);
                boolean isPreviousIdPeriodeEquals = currentIdPeriode.equalsIgnoreCase(previousIdPeriode);

                boolean isLastMontantPrstApiForCurrentRenteAccordee = (i == (montantsPrstAPI.length - 1));
                isLastMontantPrstApiForCurrentRenteAccordee = isLastMontantPrstApiForCurrentRenteAccordee
                        || !currentTypePrestations.equalsIgnoreCase(montantsPrstAPI[i + 1].getTypePrestation());
                isLastMontantPrstApiForCurrentRenteAccordee = isLastMontantPrstApiForCurrentRenteAccordee
                        || !currentGenreDroitApi.equalsIgnoreCase(montantsPrstAPI[i + 1].getCsGenreDroitApi());
                isLastMontantPrstApiForCurrentRenteAccordee = isLastMontantPrstApiForCurrentRenteAccordee
                        || !isNextPeriodeContigue;
                isLastMontantPrstApiForCurrentRenteAccordee = isLastMontantPrstApiForCurrentRenteAccordee
                        || !currentIdPeriode.equalsIgnoreCase(montantsPrstAPI[i + 1].getIdPeriode());

                if (!isPreviousTypePrestationEqualCurrentTypePrestation
                        || !isPreviousGenreDroitApiEqualCurrentGenreDroitApi || !isPreviousPeriodeContigue
                        || !isPreviousIdPeriodeEquals) {

                    montantTotalRetro = new BigDecimal(0);
                    ddRetro = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMM(montantsPrstAPI[i].getDateDebut());

                    ra = new RERenteAccordee();
                    ra.setSession(session);
                    ra.setIdBaseCalcul(idBaseCalcul);
                    ra.setIdTiersBeneficiaire(caViewBean.getIdTiers());
                    ra.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
                    // Les rentes accordées de type API ne doivent pas être
                    // envoyées à ACOR, car ACOR ne les calculs pas.
                    ra.setCsGenreDroitApi(currentGenreDroitApi);
                    ra.setCodePrestation(currentTypePrestations);
                    ra.setCodeCasSpeciaux1(montantsPrstAPI[i].getCodeCasSpecial());

                    // Voir dans la situation familiale pour setter le NSS de
                    // son épouse ou ex épouse dans le champ nss complémentaire
                    // 1

                    // 1. Reprendre la situation familiale
                    ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                            ISFSituationFamiliale.CS_DOMAINE_RENTES, ra.getIdTiersBeneficiaire());

                    // 2. Reprendre les relations familiales
                    ISFRelationFamiliale[] rfs = sf.getRelationsConjoints(ra.getIdTiersBeneficiaire(),
                            JACalendar.todayJJsMMsAAAA());

                    // 3. Parcourir toutes les relations et setter les données
                    // nécessaires
                    for (int j = 0; (rfs != null) && (j < rfs.length); j++) {
                        ISFRelationFamiliale rf = rfs[j];

                        String idMbrFamille1 = rf.getIdMembreFamilleHomme();
                        ISFMembreFamille mf1 = sf.getMembreFamille(idMbrFamille1);
                        String idTiers1 = mf1.getIdTiers();

                        if (!idTiers1.equals(ra.getIdTiersBeneficiaire())) {
                            ra.setIdTiersComplementaire1(idTiers1);
                        } else {
                            String idMbrFamille2 = rf.getIdMembreFamilleFemme();
                            ISFMembreFamille mf2 = sf.getMembreFamille(idMbrFamille2);
                            String idTiers2 = mf2.getIdTiers();

                            if (!idTiers2.equals(ra.getIdTiersBeneficiaire())) {
                                ra.setIdTiersComplementaire1(idTiers2);
                            }
                        }
                        // BZ 9926
                        break;

                    }

                    // On ajoute la rente accordée, sans le montant

                    ra.setAnneeMontantRAM(String.valueOf(JACalendar.getYear(JACalendar.todayJJsMMsAAAA())));

                    idsRA.add(Long.parseLong(REAddRenteAccordee.addRenteAccordeeCascade_noCommit(session, transaction,
                            ra, IREValidationLevel.VALIDATION_LEVEL_NONE)));

                    // On ajoute la prestation due
                    prstDue = new REPrestationDue();
                    prstDue.setSession(session);

                    prstDue.setDateDebutPaiement(
                            PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(montantsPrstAPI[i].getDateDebut()));
                    prstDue.setDateFinPaiement(
                            PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(montantsPrstAPI[i].getDateFin()));

                    // Derniere période pour ce type de prestation, en fait la
                    // seule et unique
                    // La dernière correspond au montant courant.

                    if (isLastMontantPrstApiForCurrentRenteAccordee) {

                        // MAJ du montant de la rente accordée
                        ra.retrieve(transaction);
                        ra.setMontantPrestation(montantsPrstAPI[i].getMontant().toString());
                        ra.update(transaction);

                        isLastPeriodeForRA = true;

                        dfRetro = montantsPrstAPI[i].getDateFin();

                        if (JadeStringUtil.isBlankOrZero(dfRetro)) {
                            dfRetro = lastPmtjjmmaaaa;
                        } else if (BSessionUtil.compareDateFirstGreater(session, dfRetro, lastPmtjjmmaaaa)) {
                            dfRetro = lastPmtjjmmaaaa;
                        }
                        nombreMoisRetro = getNombreMois(new JADate(montantsPrstAPI[i].getDateDebut()),
                                new JADate(dfRetro));

                    } else {
                        nombreMoisRetro = getNombreMois(new JADate(montantsPrstAPI[i].getDateDebut()),
                                new JADate(montantsPrstAPI[i].getDateFin()));
                        isLastPeriodeForRA = false;
                    }
                    montantRetroPeriode = montantsPrstAPI[i].getMontant().multiply(new BigDecimal(nombreMoisRetro));
                    montantTotalRetro = montantTotalRetro.add(montantRetroPeriode);

                    // Date début
                    ddRetro = montantsPrstAPI[i].getDateDebut();

                }

                // Dans le même type de prestation
                else {
                    isLastPeriodeForRA = false;

                    // On ajoute la prestation due
                    prstDue = new REPrestationDue();
                    prstDue.setSession(session);

                    prstDue.setDateDebutPaiement(
                            PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(montantsPrstAPI[i].getDateDebut()));
                    prstDue.setDateFinPaiement(
                            PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(montantsPrstAPI[i].getDateFin()));

                    if (isLastMontantPrstApiForCurrentRenteAccordee) {

                        isLastPeriodeForRA = true;
                        // MAJ du montant de la rente accordée
                        ra.retrieve(transaction);
                        ra.setMontantPrestation(montantsPrstAPI[i].getMontant().toString());
                        ra.update(transaction);

                        dfRetro = montantsPrstAPI[i].getDateFin();
                        if (JadeStringUtil.isBlankOrZero(dfRetro)) {
                            dfRetro = lastPmtjjmmaaaa;
                        } else if (BSessionUtil.compareDateFirstGreater(session, dfRetro, lastPmtjjmmaaaa)) {
                            dfRetro = lastPmtjjmmaaaa;
                        }
                        nombreMoisRetro = getNombreMois(new JADate(montantsPrstAPI[i].getDateDebut()),
                                new JADate(dfRetro));

                    } else {
                        isLastPeriodeForRA = false;

                        nombreMoisRetro = getNombreMois(new JADate(montantsPrstAPI[i].getDateDebut()),
                                new JADate(montantsPrstAPI[i].getDateFin()));
                    }
                    montantRetroPeriode = montantsPrstAPI[i].getMontant().multiply(new BigDecimal(nombreMoisRetro));
                    montantTotalRetro = montantTotalRetro.add(montantRetroPeriode);

                }

                prstDue.setCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
                prstDue.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
                prstDue.setCsTypePaiement(IREPrestationDue.CS_TYPE_DE_PMT_PMT_MENS);
                prstDue.setIdRenteAccordee(ra.getIdPrestationAccordee());
                prstDue.setMontant(montantsPrstAPI[i].getMontant().toString());

                // Ajout de la date d'échéance
                if (isAPIAI && isDateEcheance) {

                    // Si date de fin vide
                    if (JadeStringUtil.isBlankOrZero(montantsPrstAPI[i].getDateFin())) {
                        ra.setDateEcheance(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dateEcheance.toStr(".")));
                        ra.update(transaction);
                        // Si dateEchéance dans la période de la ra
                    } else {
                        // donc si datedebut <= dateEcheance && dateFin >=
                        // dateEcheance
                        if (((cal.compare(montantsPrstAPI[i].getDateDebut(),
                                dateEcheance.toStr(".")) == JACalendar.COMPARE_FIRSTLOWER)
                                || (cal.compare(montantsPrstAPI[i].getDateDebut(),
                                dateEcheance.toStr(".")) == JACalendar.COMPARE_EQUALS))

                                && ((cal.compare(montantsPrstAPI[i].getDateFin(),
                                dateEcheance.toStr(".")) == JACalendar.COMPARE_FIRSTUPPER)
                                || (cal.compare(montantsPrstAPI[i].getDateFin(),
                                dateEcheance.toStr(".")) == JACalendar.COMPARE_EQUALS))) {
                            ra.setDateEcheance(
                                    PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dateEcheance.toStr(".")));
                            ra.update(transaction);
                        }
                    }

                }

                if (isLastPeriodeForRA) {
                    // un rétro par rente accordée
                    // On ajoute le rétro ($t)....
                    REPrestationDue prstDueRetro = new REPrestationDue();
                    prstDueRetro.setSession(session);

                    prstDueRetro.setDateDebutPaiement(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(ddRetro));
                    prstDueRetro.setDateFinPaiement(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dfRetro));
                    // prstDueRetro.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);

                    prstDueRetro.setCsType(IREPrestationDue.CS_TYPE_MNT_TOT);
                    prstDueRetro.setCsTypePaiement(IREPrestationDue.CS_TYPE_DE_PMT_PMT_MENS);
                    prstDueRetro.setIdRenteAccordee(ra.getIdPrestationAccordee());
                    prstDueRetro.setMontant(
                            JANumberFormatter.format(montantTotalRetro.toString(), 0.01, 2, JANumberFormatter.NEAR));

                    if (BSessionUtil.compareDateFirstLower(session, prstDueRetro.getDateFinPaiement(),
                            prstDueRetro.getDateDebutPaiement())) {
                        prstDueRetro = new REPrestationDue();
                    } else {
                        prstDueRetro.add(transaction);
                    }

                    // Mise à jours de la date de début / fin de la rente
                    // accordée.
                    ra.retrieve(transaction);
                    ra.setDateDebutDroit(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(ddRetro));

                    ra.setAnneeMontantRAM(RECalculACORDemandeRenteHelper.computeAnneeMontantRAM(ra, session));

                    // MAJ de la date de fin de la ra
                    // Il y a une date de décès...
                    if (!JadeStringUtil.isBlankOrZero(dateDeces)) {

                        if (!JadeStringUtil.isBlankOrZero(montantsPrstAPI[i].getDateFin())) {
                            if (BSessionUtil.compareDateFirstGreaterOrEqual(session, montantsPrstAPI[i].getDateFin(),
                                    dateDeces)) {
                                ra.setDateFinDroit(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dateDeces));
                                ra.setCodeMutation(IREAnnonces.CODE_MUTATION_DECES);
                            } else {
                                ra.setDateFinDroit(PRDateFormater
                                        .convertDate_JJxMMxAAAA_to_MMxAAAA(montantsPrstAPI[i].getDateFin()));
                                ra.setCodeMutation(IREAnnonces.CODE_MUTATION_AUTRE_EVENEMENT);
                            }
                        } else {
                            ra.setDateFinDroit(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dateDeces));
                            ra.setCodeMutation(IREAnnonces.CODE_MUTATION_DECES);
                        }
                    }
                    // Pas de date de décès
                    else {
                        if (!JadeStringUtil.isBlankOrZero(montantsPrstAPI[i].getDateFin())) {
                            ra.setDateFinDroit(
                                    PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(montantsPrstAPI[i].getDateFin()));
                            prstDue.setDateFinPaiement(
                                    PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(montantsPrstAPI[i].getDateFin()));
                            ra.setCodeMutation(IREAnnonces.CODE_MUTATION_AUTRE_EVENEMENT);
                        }

                        // Last check :
                        // Si la dernière période API n'a pas de date de fin, et que
                        // la RA correspond à cette période,
                        // ne pas mettre de date de fin à cette RA

                        // Exemple :
                        // RA [ ][ ]
                        // Période API [ ][
                        //
                        // -->lastDateFinPeriodeAPI == null
                        //
                        // MntPrstParPeriode [ ][ ][ ][ ]
                        //
                        // Le traitement ci-dessous va supprimer la date de fin de
                        // la RA
                        //

                        if (JadeStringUtil.isBlankOrZero(lastDateFinPeriodeAPI)) {

                            if (BSessionUtil.compareDateFirstLower(session, lastDateDebutPeriodeAPI,
                                    ra.getDateFinDroit())
                                    || BSessionUtil.compareDateEqual(session, lastDateDebutPeriodeAPI,
                                    ra.getDateFinDroit())) {

                                if (isAPIAI && isDateEcheance) {
                                    ra.setDateEcheance(
                                            PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dateEcheance.toStr(".")));
                                }

                                ra.setDateFinDroit(null);
                                ra.setCodeMutation(null);
                                prstDue.setDateFinPaiement(null);

                            }
                        }
                    }

                    ra.update(transaction);
                }
                // On crée les annonces

                REBasesCalcul bc = new REBasesCalcul();

                bc.setSession(session);
                bc.setIdBasesCalcul(idBaseCalcul);
                bc.retrieve(transaction);

                // trouver l'état civil

                globaz.hera.api.ISFSituationFamiliale sitFam = SFSituationFamilialeFactory.getSituationFamiliale(
                        session, ISFSituationFamiliale.CS_DOMAINE_RENTES,
                        tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                ISFMembreFamilleRequerant[] membresFamilleRequerant = sitFam
                        .getMembresFamille(ra.getIdTiersBeneficiaire());
                for (int j = 0; j < membresFamilleRequerant.length; j++) {

                    ISFMembreFamilleRequerant membreFamilleRequerant = membresFamilleRequerant[j];

                    if (ra.getIdTiersBeneficiaire().equals(membreFamilleRequerant.getIdTiers())) {
                        ra.setCsEtatCivil(
                                PRACORConst.csEtatCivilHeraToCsEtatCivil(membreFamilleRequerant.getCsEtatCivil()));
                    }
                }

                ra.setIdTiersBaseCalcul(bc.getIdTiersBaseCalcul());
                ra.update(transaction);

                prstDue.add(transaction);
                previousTypePrestations = currentTypePrestations;
                previousGenreDroitApi = currentGenreDroitApi;
                previousIdPeriode = currentIdPeriode;
            }

            api.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
            api.update(transaction);

            // Mis à jours de l'état des prestations dues...
            // Seul la dernière $p doit être en attente, et les autres
            // 'TRAITEE'. Le traitement ci-dessus met l'état
            // des presatations dues à 'TRAITE' -> seul la dernière doit être
            // maj dans l'état 'ATTENTE'.
            // Toutes les $t doivent être en attente. L'état des $t à dàjà été
            // mis à jours précédemment.
            //
            // - RA 81
            // [ $p 'TRAITE' ]
            // [ $t 'ATTENTE' ]
            //
            // - RA 82
            // [ $p 'ATTENTE'
            // [ $t 'ATTENTE'

            REPrestationsDuesJointDemandeRenteManager mgr = new REPrestationsDuesJointDemandeRenteManager();
            mgr.setSession(session);
            mgr.setForNoDemandeRente(caViewBean.getIdDemandeRente());
            mgr.setForCsTypePrestationDue(IREPrestationDue.CS_TYPE_PMT_MENS);
            // Tri de la plus récente à la plus ancienne
            mgr.setOrderBy(REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT + " DESC ");

            mgr.find(transaction, 2);
            for (int i = 0; i < mgr.size(); i++) {
                REPrestationsDuesJointDemandeRente prstDueDem = (REPrestationsDuesJointDemandeRente) mgr.getEntity(i);
                REPrestationDue pd = new REPrestationDue();
                pd.setSession(session);
                pd.setIdPrestationDue(prstDueDem.getIdPrestationDue());
                pd.retrieve(transaction);

                if (i == 0) {
                    pd.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
                    pd.update(transaction);
                    break;
                }
            }

            // Dernière étape : MAJ des adresse de pmt des rentes accordées
            // créées
            REBeneficiairePrincipal.doMajAdressePmtDesRentesAccordees(session, transaction, idsRA);

            // calcul des rentes versées à tort
            if (idsRA.size() >= 1) {
                calculerEtSauverRentesVerseesATort(session, (BTransaction) transaction,
                        Long.parseLong(api.getIdDemandeRente()), idsRA);
            }

            transaction.commit();

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
        return viewBean;
    }

    public FWViewBeanInterface actionExporterScriptACOR(final FWViewBeanInterface viewBean, final FWAction action,
                                                        final BSession session) throws Exception {

        RECalculACORDemandeRenteViewBean caViewBean = (RECalculACORDemandeRenteViewBean) viewBean;

        // Plausi, vérifier que tous les membres de la famille ont une date de
        // naissance et un sexe
        // sauf pour le conjoint inconnu

        // Rechercher les membres de la famille (Enfants & Conjoints)

        String csDomaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;
        try {
            REDemandeRente dem = new REDemandeRente();
            dem.setSession(session);
            dem.setIdDemandeRente(caViewBean.getIdDemandeRente());
            dem.retrieve();

            if (IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL.equals(dem.getCsTypeCalcul())) {
                csDomaine = ISFSituationFamiliale.CS_DOMAINE_CALCUL_PREVISIONNEL;
            }
        } catch (Exception e) {
            csDomaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;
        }

        globaz.hera.api.ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session, csDomaine,
                caViewBean.getIdTiers());
        ISFMembreFamilleRequerant[] membresFamille = sf.getMembresFamille(caViewBean.getIdTiers());

        for (int i = 0; (membresFamille != null) && (i < membresFamille.length); i++) {
            ISFMembreFamilleRequerant mf = membresFamille[i];

            if ((JadeStringUtil.isBlankOrZero(mf.getDateNaissance()) || JadeStringUtil.isBlankOrZero(mf.getCsSexe()))
                    && !mf.getIdMembreFamille().equals(ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU)) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(
                        mf.getNom() + " " + mf.getPrenom() + session.getLabel("ERREUR_DATE_NAISS_SEXE_DEFINI"));
            }
        }

        if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            Map<String, PRAcorFileContent> filesContent = new HashMap<String, PRAcorFileContent>();

            REACORBatchFilePrinter.getInstance().printBatchFileRentes(filesContent, session,
                    caViewBean.loadDemandeRente(session.getCurrentThreadTransaction()),
                    PRACORConst.dossierACOR(session));
            caViewBean.setFilesContent(filesContent);
            caViewBean.setIsFileContent(true);
        }

        return viewBean;
    }

    public FWViewBeanInterface actionImporterScriptACOR(final FWViewBeanInterface viewBean, final FWAction action,
                                                        final BSession session) throws Exception {

        RECalculACORDemandeRenteViewBean caViewbean = (RECalculACORDemandeRenteViewBean) viewBean;
        Long idCopieDemande = null;
        String message = null;
        BITransaction transaction = null;
        caViewbean.setIsFileContent(false);
        LinkedList<Long> idsRentesAccordeesNouveauDroit = new LinkedList<Long>();

        try {
            REDemandeRente demandeRente = caViewbean.loadDemandeRente(null);

            if (IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL.equals(demandeRente.getCsTypeCalcul())) {
                throw new REBusinessException(session.getLabel("ERREUR_IMPORT_CALCUL_PREVISIONNEL"));
            }
            if (IREDemandeRente.CS_TYPE_CALCUL_BILATERALES.equals(demandeRente.getCsTypeCalcul())) {
                throw new REBusinessException(session.getLabel("ERREUR_IMPORT_CALCUL_DEMANDE_BILATERALE"));
            }

            // HACK: on cree une transaction pour etre sur que tous les ajouts
            // peuvent etre rollbackes
            // note: la transaction est enregistree dans la session est sera
            // utilisee dans tous les entity qui l'utilise
            transaction = session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            demandeRente = caViewbean.loadDemandeRente(null);
            reinitialiserToutesDemandesNonValideesFamille(session, caViewbean.getIdTiers());

            // Identification du cas à traiter :
            int noCasATraiter = 0;

            if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE.equals(demandeRente.getCsEtat())
                    || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL.equals(demandeRente.getCsEtat())) {
                noCasATraiter = CAS_NOUVEAU_CALCUL;
            } else if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(demandeRente.getCsEtat())) {
                noCasATraiter = CAS_RECALCUL_DEMANDE_VALIDEE;
            } else if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE.equals(demandeRente.getCsEtat())) {
                noCasATraiter = CAS_RECALCUL_DEMANDE_NON_VALIDEE;
            } else {
                throw new PRACORException(session.getLabel("ERREUR_RECALCUL_CAS"));
            }

            FCalcul fCalcul = null;
            List<FCalcul.Evenement> filterEvents = new ArrayList<>();
            if (!JadeStringUtil.isEmpty(caViewbean.getContenuFeuilleCalculXML())) {
                fCalcul = unmarshalXml(caViewbean.getContenuFeuilleCalculXML());
            }
            if (fCalcul != null && fCalcul.getEvenement() != null) {
                // On analyse filtre les évènements de la feuille de calcul pour ne garder que ceux qui possèdent une base de calcul dont la décision n'est pas vide.
                filterEvents = fCalcul.getEvenement().stream().filter(evenement -> !evenement.getBasesCalcul().stream().filter(basesCalcul -> !basesCalcul.getDecision().isEmpty()).collect(Collectors.toList()).isEmpty()).collect(Collectors.toList());
            }

            // K191213_001, suite à l'incident I191211_020
            // Passage dans le IF si c'est une 10ème révision mais qu'il n'y a pas de décisions dans le f_calcul.xml
            // Si on se trouve dans une 10ème révision et qu'il y a des décisions, on fait le calcul normalement
            // Pour une 9ème révision, le f_calcul.xml sera vide donc on passe dans tous les cas dans le else et on fait le traitement normalement
            if ((fCalcul != null) && (filterEvents.isEmpty())) {
                // Dans ce cas de figure, il n'y a rien à remonter.
                // On contrôle l'arrivée d'un ci additionnel pour le requérant
                // ainsi que ces conjoints et ex-conjoints !!!!

                globaz.hera.api.ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                        ISFSituationFamiliale.CS_DOMAINE_RENTES, caViewbean.getIdTiers());

                ISFMembreFamilleRequerant[] mf = sf.getMembresFamilleRequerant(caViewbean.getIdTiers());
                String idMFRequerant = "";
                for (int i = 0; i < mf.length; i++) {
                    if (ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT.equals(mf[i].getRelationAuRequerant())) {
                        idMFRequerant = mf[i].getIdMembreFamille();
                        break;
                    }
                }

                ISFMembreFamille[] mfe = sf.getMembresFamilleEtendue(idMFRequerant, Boolean.FALSE);
                for (int i = 0; i < mfe.length; i++) {
                    if (ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT.equals(mfe[i].getRelationAuLiant())) {
                        if (!JadeStringUtil.isBlankOrZero(mfe[i].getIdTiers())) {
                            doMajDateTraitementCIAdditionnel(session, transaction, mfe[i].getIdTiers());
                        }
                    }
                }
                doMajDateTraitementCIAdditionnel(session, transaction, caViewbean.getIdTiers());
            } else {

                // !!!!! Contrôle de l'arrivée d'un CI additionnel ici....
                String idTiersRequerant = caViewbean.getIdTiers();

                // Recherche des demandes de RCI de type CI Additionnel,
                // pour tous les membres de la famille ayant une rente active
                // et maj du ci additionnel avec la date de traitement
                globaz.hera.api.ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                        ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant);

                ISFMembreFamilleRequerant[] mf = sf.getMembresFamilleRequerant(idTiersRequerant);
                String ids = "";
                for (int i = 0; i < mf.length; i++) {
                    if (!JadeStringUtil.isBlankOrZero(mf[i].getIdTiers())) {
                        ids += mf[i].getIdTiers() + ",";
                    }
                }
                // La string se termine par une 'virgule' !!
                ids += "-1";

                RERenteAccordeeManager mgr = new RERenteAccordeeManager();
                mgr.setSession(session);
                mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_PARTIEL + ", " + IREPrestationAccordee.CS_ETAT_VALIDE);
                mgr.setForEnCoursAtMois(REPmtMensuel.getDateDernierPmt(session));
                mgr.setForIdTiersBeneficiaireIn(ids);
                mgr.find(transaction);

                // Map pour le traitement des annonces ponctuelles
                /*
                 * |---------|---------------------------------| | Key | Value |
                 * |---------|---------------------------------| |KeyAP | ValueAP | | -idTiers| -ancienRAM | | | -mntRA
                 * | | | -nouveauRAM | | | -ancienMontantRA | | | -nouveauMontantRA |
                 * |---------|---------------------------------| | ... | ... |
                 * |---------|---------------------------------|
                 */

                Map<KeyAP, ValueAP> mapAP = new HashMap<KeyAP, ValueAP>();

                boolean isCIAdditionnel = false;

                // Init de la map avec les anciens montant et RAM
                for (int i = 0; i < mgr.size(); i++) {
                    RERenteAccordee ra = (RERenteAccordee) mgr.getEntity(i);

                    // Identification de l'arrivée d'un CI additionnel
                    RERassemblementCIManager mgr2 = new RERassemblementCIManager();
                    mgr2.setSession(session);
                    mgr2.setForIdTiers(ra.getIdTiersBeneficiaire());
                    mgr2.setForCIAdditionnelOnly(Boolean.TRUE);
                    mgr2.find(transaction);

                    if (!mgr2.isEmpty()) {
                        isCIAdditionnel = true;
                        List<String> idsRCI = new ArrayList<String>();
                        // Parcours de tous les rassemblements de ci additionnels, des
                        // fois qu'il y en ait plus qu'un !!
                        for (int ii = 0; ii < mgr2.size(); ii++) {
                            RERassemblementCI rci = (RERassemblementCI) mgr2.getEntity(ii);
                            rci.setDateTraitement(JACalendar.todayJJsMMsAAAA());
                            rci.update(transaction);
                            idsRCI.add(rci.getIdRCI());
                        }

                        KeyAP k = new KeyAP();
                        k.idTiers = ra.getIdTiersBeneficiaire();
                        k.genreRente = ra.getCodePrestation();
                        ValueAP v = initVAP(session, transaction, ra, idsRCI);
                        mapAP.put(k, v);
                    }
                }

                if (isCIAdditionnel) {
                    int ret = importationCIAdditionnelsDepuisCalculACOR(session, caViewbean, transaction, mapAP);
                    // Traitement standard....
                    if (ret == 1) {
                        idCopieDemande = importationDesAnnoncesDuCalculACOR(session, caViewbean, transaction,
                                idsRentesAccordeesNouveauDroit, noCasATraiter, fCalcul);
                    }
                }
                // Pas de ci additionnel, traitement standard.
                else {
                    idCopieDemande = importationDesAnnoncesDuCalculACOR(session, caViewbean, transaction,
                            idsRentesAccordeesNouveauDroit, noCasATraiter, fCalcul);
                }
            }

            /*
             * Si le calcul à été remonté sur une demande validée, une copie sera automatiquement réalisée
             * Il faut donc travailler sur la copie de la demande et non sur l'original
             */

            if (idCopieDemande != null && !idCopieDemande.equals(0)) {
                caViewbean.setIdDemandeRente(String.valueOf(idCopieDemande));
            }

            // Inforom D0112 : recherche si des remarques particulières sont présentes dans la feuille de calcul
            traiterLesRemarquesParticulieresDeLaFeuilleDeCalculAcor(session, transaction, caViewbean);

            /*
             * On ne lance le calcul des rentes versées à tort que si des rentes accordées ont été créées lors de
             * l'importation du calcul, sinon cela résulte en une requête SQL avec une clause WHERE trop large et une
             * explosion de la consommation mémoire lors de la recherche des données pour le calcul des rentes versées à
             * tort
             */
            if (idsRentesAccordeesNouveauDroit.size() > 0) {
                calculerEtSauverRentesVerseesATort(session, (BTransaction) transaction,
                        Long.valueOf(caViewbean.getIdDemandeRente()), idsRentesAccordeesNouveauDroit);
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        try {
            BSessionUtil.initContext(session, this);
            /*
             * Dispatch des rentes accordées devant être déplacé dans une autre demande
             * Try/catch suivant uniquement pour donner du context à l'exception
             */
            long idDemande = 0;
            try {
                /*
                 * Si la demande à été copiée dans le traitement précédent, on travaille avec la copie de la demande
                 */
                if (idCopieDemande != null) {
                    idDemande = idCopieDemande;
                } else {
                    idDemande = Long.valueOf(caViewbean.getIdDemandeRente());
                }

                // Mettre le bon id dans le viewbean pour arriver avec le bon id dans l'écran des rentes accordées
                caViewbean.setIdDemandeRente(Long.toString(idDemande));

                long idTiersDemande = Long.valueOf(caViewbean.getIdTiers());

                // L'appel de cette méthode peut dans certain cas retourner un message d'information à l'utilisateur
                message = separerLesDemandesSiBesoin(idTiersDemande, idDemande);

            } catch (NumberFormatException exception) {
                String msg = "Unable to get id of the REDemandeRente [" + caViewbean.getIdDemandeRente()
                        + "] or the idTiersDemandeRente [" + caViewbean.getIdTiers() + "]. Cause : "
                        + exception.toString();
                throw new IllegalArgumentException(msg, exception);
            }

            /*
             * BZ 9627 : on parcours les rentes versées à tort de la demande sur laquelle on vient d'importer, et on
             * vérifie que les rentes versées à tort soient bien rattachées à la bonne demande (en comparant la demande
             * sur laquelle est rattaché la rente du nouveau droit ayant permis de calculer cette rente versée à tort)
             */

            RERenteVerseeATortManager renteVerseeATortManager = new RERenteVerseeATortManager();
            renteVerseeATortManager.setForIdDemandeRente(idDemande);
            renteVerseeATortManager.find(BManager.SIZE_NOLIMIT);

            for (RERenteVerseeATort uneRenteVerseeATort : renteVerseeATortManager.getContainerAsList()) {

                /*
                 * Dans le cas où il n'y a pas d'ID de rente du nouveau droit (c'est à dire qu'il y avait un trou
                 * dans
                 * la période du nouveau droit), on ne fait rien pour la rente versée à tort
                 */
                if (uneRenteVerseeATort.getIdRenteAccordeeNouveauDroit() == null) {
                    continue;
                }

                RERenteAccordee renteAccordee = new RERenteAccordee();
                renteAccordee.setIdPrestationAccordee(uneRenteVerseeATort.getIdRenteAccordeeNouveauDroit().toString());
                renteAccordee.retrieve();

                REBasesCalcul baseCalcul = new REBasesCalcul();
                baseCalcul.setIdBasesCalcul(renteAccordee.getIdBaseCalcul());
                baseCalcul.retrieve();

                REDemandeRente demandeRente2 = new REDemandeRente();
                demandeRente2.setIdRenteCalculee(baseCalcul.getIdRenteCalculee());
                demandeRente2.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
                demandeRente2.retrieve();

                // si la demande sur laquelle est liée la rente accordée est différente de la demande de la rente
                // versée
                // à tort, on met à jour la rente versée à tort pour refléter la rente accordée
                if (!uneRenteVerseeATort.getIdDemandeRente()
                        .equals(Long.parseLong(demandeRente2.getIdDemandeRente()))) {
                    uneRenteVerseeATort.retrieve();
                    uneRenteVerseeATort.setIdDemandeRente(Long.parseLong(demandeRente2.getIdDemandeRente()));
                    uneRenteVerseeATort.update();
                }
            }

        } catch (RETechnicalException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        } finally {
            BSessionUtil.stopUsingContext(this);
        }
        if (!rentesWithoutBte.isEmpty()) {
            Iterator<String> iterator = rentesWithoutBte.iterator();
            StringBuilder allNumber = new StringBuilder();
            while (iterator.hasNext()) {
                allNumber.append(iterator.next());
                if (iterator.hasNext()) {
                    allNumber.append(", ");
                } else {
                    allNumber.append(".");
                }
            }
            viewBean.setMessage(FWMessageFormat.format(session.getLabel("JSP_BTE_MANUEL"), allNumber.toString()));
            viewBean.setMsgType(FWViewBeanInterface.WARNING);
        }
        if (!JadeStringUtil.isBlank(message)) {
            viewBean.setMessage(message);
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        return viewBean;
    }


    /**
     * Récupère le contenu du fichier f_calcul.xml venant de ACOR.
     *
     * @param xml : le contenu du fichier f_calcul.xml
     * @return la feuille de calcul.
     * @throws JAXBException
     */
    private FCalcul unmarshalXml(String xml) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("acor.fcalcul:org.gotess.xns.core");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        try {
            return (FCalcul) unmarshaller.unmarshal(new StringReader(xml));

        } catch (JAXBException exception) {
            JadeLogger.error(this, "JAXB validation has thrown a JAXBException : " + exception.toString());
            throw exception;
        }
    }

    private void miseAjourDateDesDemandes(BSession session, Set<DemandeRenteWrapper> ids) throws Exception {
        for (DemandeRenteWrapper demande : ids) {
            miseAjourDateDeLaDemande(session, demande.getDemandeRente().getId());
        }
    }

    /**
     * Met à jour les date de début et de fin de la demande en fonction des dates de début et de fin des rentes
     * accordées.
     * Si aucune rente accordée n'est présente les dates de la demande ne seront pas mis a jour.
     *
     * @param session la session à utiliser
     * @param id      L'id de la demande à mettre à jour
     * @throws Exception
     */
    private void miseAjourDateDeLaDemande(BSession session, Long id) throws Exception {

        RERenteAccJoinTblTiersJoinDemRenteManager raMgr2 = new RERenteAccJoinTblTiersJoinDemRenteManager();
        raMgr2.setSession(session);
        raMgr2.setForNoDemandeRente(Long.toString(id));
        raMgr2.find();

        // Mise à jour des date de la demande uniquement si au moins une rente accordée sont présentes
        if (raMgr2.getContainer().size() > 0) {
            REDemandeRente demRente = new REDemandeRente();
            demRente.setIdDemandeRente(Long.toString(id));
            demRente.setSession(session);
            demRente.retrieve();

            String dd = DATE_DEBUT_DEMANDE;
            String df = DATE_FIN_DEMANDE;
            boolean isPeriodeInfinie = false;
            JACalendar cal = new JACalendarGregorian();

            // Boucler sur toutes les ra pour avoir la plus petite date de début
            for (RERenteAccJoinTblTiersJoinDemandeRente ra2 : raMgr2.getContainerAsList()) {

                if (cal.compare(dd, ra2.getDateDebutDroit()) == JACalendar.COMPARE_SECONDLOWER) {
                    dd = ra2.getDateDebutDroit();
                }

                if (JadeStringUtil.isBlankOrZero(ra2.getDateFinDroit())) {
                    isPeriodeInfinie = true;
                } else {
                    // pour la date de fin, il faut prendre le dernier jour du mois et non pas le premier comme le fait
                    // les JADate
                    JACalendarGregorian calendar = new JACalendarGregorian();
                    JADate ra2Df = calendar.addMonths(new JADate(ra2.getDateFinDroit()), 1);
                    ra2Df = calendar.addDays(ra2Df, -1);

                    if (cal.compare(df, ra2Df.toStr(".")) == JACalendar.COMPARE_SECONDUPPER) {
                        df = ra2Df.toStr(".");
                    }
                }
            }
            // Mis à jour de la date de début uniquement si elle est différente de DATE_DEBUT_DEMANDE
            if (!DATE_DEBUT_DEMANDE.equals(dd)) {
                demRente.setDateDebut(dd);
            }

            // et la plus grande date de fin (ou vide)
            if (isPeriodeInfinie) {
                demRente.setDateFin("");
            } else if (!DATE_FIN_DEMANDE.equals(df)) {
                demRente.setDateFin(df);
            } else {
                demRente.setDateFin("");
            }

            demRente.setDateTraitement(REACORParser.retrieveDateTraitement(demRente));
            demRente.update();
        }

    }

    /**
     * Réinitialise toutes les demandes de la famille (sauf API) en état non validé avant remonté du calcul
     *
     * @param session
     * @param idTiersRequerant
     * @throws IllegalArgumentException
     * @throws NumberFormatException
     * @throws ServletException
     */
    private void reinitialiserToutesDemandesNonValideesFamille(final BSession session, String idTiersRequerant)
            throws IllegalArgumentException, NumberFormatException, ServletException {

        if (JadeStringUtil.isBlankOrZero(idTiersRequerant)) {
            throw new IllegalArgumentException("actionImporterScriptACOR : the idTiers is empty");
        }

        Set<DemandeRente> demandesNonValideesDeLaFamille = null;

        PRHybridHelper.initContext(session, this);
        try {
            PersonneAVS requerant = PyxisCrudServiceLocator.getPersonneAvsCrudService()
                    .read(Long.valueOf(idTiersRequerant));
            Set<DemandeRente> demandesDeLaFamille = CorvusServiceLocator.getDemandeRenteService()
                    .demandesDuRequerantEtDeSaFamille(requerant);
            demandesNonValideesDeLaFamille = demandesStandardsOuTransitoires(demandesNonValidees(demandesDeLaFamille));

        } finally {
            PRHybridHelper.stopUsingContext(this);
        }

        /*
         * On filtre les demande de type API et celles en état VALIDE
         */
        List<Long> idDemandesAReinitialise = new ArrayList<Long>();
        for (DemandeRente dem : demandesNonValideesDeLaFamille) {
            if (filtrerDemandeAReinitialiser(dem)) {
                idDemandesAReinitialise.add(dem.getId());
            }
        }

        for (Long id : idDemandesAReinitialise) {
            reinitialiserDemande(session, id);
        }
    }

    /**
     * Return true si la demande doit être réinitialisée
     *
     * @param dem
     * @return
     */
    private boolean filtrerDemandeAReinitialiser(DemandeRente dem) {
        if (!TypeDemandeRente.DEMANDE_API.equals(dem.getTypeDemandeRente())) {
            if (!EtatDemandeRente.VALIDE.equals(dem.getEtat()) && !EtatDemandeRente.COURANT_VALIDE.equals(dem.getEtat())
                    && !EtatDemandeRente.TRANSFERE.equals(dem.getEtat())
                    && !EtatDemandeRente.TERMINE.equals(dem.getEtat())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Réinitialise une demande
     *
     * @param session
     * @param id
     */
    private void reinitialiserDemande(BSession session, long id) {
        REDemandeRente demande = new REDemandeRente();
        demande.setSession(session);
        demande.setId(String.valueOf(id));
        try {
            demande.retrieve();
            if (!demande.isNew()) {
                REDeleteCascadeDemandeAPrestationsDues.reinitiliserDemandeRente(demande);
                demande.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
                demande.setIdRenteCalculee("");
                demande.update();
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            throw new RETechnicalException(e.toString(), e);
        }
    }

    private void calculerEtSauverRentesVerseesATort(final BSession session, final BTransaction transaction,
                                                    final Long idDemandeRente, final List<Long> idsRA) throws Exception {

        // suppression des rentes versées à tort de la demande (dans le cas d'une ré-importation)
        RERenteVerseeATortManager renteVerseeATortManager = new RERenteVerseeATortManager();
        renteVerseeATortManager.setSession(session);
        renteVerseeATortManager.setForIdDemandeRente(idDemandeRente);
        renteVerseeATortManager.find(BManager.SIZE_NOLIMIT);

        for (RERenteVerseeATort uneRenteVerseeATortDejaExistante : renteVerseeATortManager.getContainerAsList()) {
            uneRenteVerseeATortDejaExistante.delete(transaction);
        }

        // calcul des rentes pour la demande importée
        RECalculRentesVerseesATortManager calculRentesVerseesATortManager = new RECalculRentesVerseesATortManager();
        calculRentesVerseesATortManager.setSession(session);
        calculRentesVerseesATortManager.setIdsRenteAccordeeNouveauDroit(new HashSet<Long>(idsRA));
        calculRentesVerseesATortManager.find(BManager.SIZE_NOLIMIT);

        RECalculRentesVerseesATortWrapper wrapper = RECalculRentesVerseesATortConverter
                .convertToWrapper(calculRentesVerseesATortManager.getContainerAsList());
        // si aucune rente, l'ID de la demande ne sera pas assigné
        if (wrapper.getIdDemandeRente() == null) {
            wrapper.setIdDemandeRente(idDemandeRente);
        }

        String moisDernierPaiement = REPmtMensuel.getDateDernierPmt(session);

        Collection<REDetailCalculRenteVerseeATort> detailsCalculRentesVerseesATort = RECalculRentesVerseesATort
                .calculerRentesVerseesATort(wrapper, moisDernierPaiement);
        Collection<RERenteVerseeATort> rentesVerseesATort = RECalculRentesVerseesATortConverter
                .convertToEntity(detailsCalculRentesVerseesATort);

        for (RERenteVerseeATort uneRenteVerseeATort : rentesVerseesATort) {
            if (uneRenteVerseeATort.getIdRenteVerseeATort() != null) {
                RERenteVerseeATort renteVerseeATortEnBase = new RERenteVerseeATort();
                renteVerseeATortEnBase.setSession(session);
                renteVerseeATortEnBase.setIdRenteVerseeATort(uneRenteVerseeATort.getIdRenteVerseeATort());
                renteVerseeATortEnBase.retrieve(transaction);

                renteVerseeATortEnBase.setIdDemandeRente(uneRenteVerseeATort.getIdDemandeRente());
                renteVerseeATortEnBase
                        .setIdRenteAccordeeAncienDroit(uneRenteVerseeATort.getIdRenteAccordeeAncienDroit());
                renteVerseeATortEnBase
                        .setIdRenteAccordeeNouveauDroit(uneRenteVerseeATort.getIdRenteAccordeeNouveauDroit());
                renteVerseeATortEnBase.setTypeRenteVerseeATort(uneRenteVerseeATort.getTypeRenteVerseeATort());
                renteVerseeATortEnBase.setMontant(uneRenteVerseeATort.getMontant());
                renteVerseeATortEnBase.update(transaction);
            } else {
                if (uneRenteVerseeATort.getIdRenteAccordeeNouveauDroit() != null
                        && uneRenteVerseeATort.getIdRenteAccordeeNouveauDroit() != 0) {
                    uneRenteVerseeATort.setSession(session);
                    uneRenteVerseeATort.add(transaction);
                }
            }
        }
    }

    /**
     * Filtre les demandes et retourne les demandes dans l'état ENREGISTRE, AU_CALCUL et CALCULE
     *
     * @param demandes Les demandes à filtrer
     * @return Les demandes filtrées
     */
    private Set<DemandeRente> demandesNonValidees(final Set<DemandeRente> demandes) {
        Set<DemandeRente> demandesNonValidees = new HashSet<DemandeRente>();
        if (demandes != null) {
            for (DemandeRente uneDemande : demandes) {
                switch (uneDemande.getEtat()) {
                    case AU_CALCUL:
                    case CALCULE:
                    case ENREGISTRE:
                        demandesNonValidees.add(uneDemande);
                        break;

                    default:
                        break;
                }
            }
        }
        return demandesNonValidees;
    }

    /**
     * Filtre les demandes et retourne les demandes dans l'état VALIDE et COURANT_VALIDE
     *
     * @param demandes Les demandes à filtrer
     * @return Les demandes filtrées
     */
    private Set<DemandeRente> demandesValidees(final Set<DemandeRente> demandes) {
        Set<DemandeRente> demandesValidees = new HashSet<DemandeRente>();
        if (demandes != null) {
            for (DemandeRente uneDemande : demandes) {
                switch (uneDemande.getEtat()) {
                    case VALIDE:
                    case COURANT_VALIDE:
                        demandesValidees.add(uneDemande);
                        break;
                    default:
                        break;
                }
            }
        }
        return demandesValidees;
    }

    private Set<DemandeRente> demandesStandardsOuTransitoires(final Set<DemandeRente> demandes) {
        Set<DemandeRente> demandesSantardsOuTransitoires = new HashSet<DemandeRente>();

        for (DemandeRente uneDemande : demandes) {
            switch (uneDemande.getTypeCalcul()) {
                case STANDARD:
                case TRANSITOIRE:
                    demandesSantardsOuTransitoires.add(uneDemande);
                    break;

                default:
                    break;
            }
        }

        return demandesSantardsOuTransitoires;
    }

    /*
     *
     * Voir avec RJE, besoin de cas tests !!!!
     *
     * Si RAM ne change pas, ACOR ne va rien crééer. -> mettre date de traitement au CI Additionnel lors de
     * l'importation et permettre l'importation sans planter l'applic.
     */
    private void doMajDateTraitementCIAdditionnel(final BSession session, final BITransaction transaction,
                                                  final String idTiers) throws Exception {

        // Identification de l'arrivée d'un CI additionnel
        RERassemblementCIManager mgr = new RERassemblementCIManager();
        mgr.setSession(session);
        mgr.setForIdTiers(idTiers);
        mgr.setForCIAdditionnelOnly(Boolean.TRUE);
        mgr.find(transaction);
        if (!mgr.isEmpty()) {
            // Parcours de tous les rassemblements de ci additionnels, des fois
            // qu'il y en ait plus qu'un !!
            for (int i = 0; i < mgr.size(); i++) {
                RERassemblementCI rci = (RERassemblementCI) mgr.getEntity(i);
                rci.setDateTraitement(JACalendar.todayJJsMMsAAAA());
                rci.update(transaction);
            }
        }
    }

    private void doMAJExtraData(final BSession session, final BTransaction transaction, final FCalcul fCalcul,
                                final List<Long> rentesAccordees) throws Exception {
        /* Pour chacune des rentes accordées et $p, maj du taux de réduction pour anticipation et maj des années bte entière, demi et quart dans les bases de calculs.
         Ces données sont remontées de la feuille de calcul. */
        if (fCalcul != null) {

            // On récupère la liste des Rentes Accordées.
            RERenteAccordeeManager raManager = new RERenteAccordeeManager();
            raManager.setSession(session);
            raManager.setForIdsRentesAccordees(StringUtils.join(rentesAccordees, ","));
            raManager.find(transaction, BManager.SIZE_NOLIMIT);

            for (FCalcul.Evenement eachEvenement : fCalcul.getEvenement()) {
                for (FCalcul.Evenement.BasesCalcul eachBaseCalcul : eachEvenement.getBasesCalcul()) {

                    // Pour chaque Base de Calcul, on récupère les années BTE et le taux de réduction d'anticipation s'ils existent.
                    String an1 = null;
                    String an2 = null;
                    String an4 = null;
                    FCalcul.Evenement.BasesCalcul.BaseRam.Bte bte = null;
                    if (Objects.nonNull(eachBaseCalcul.getBaseRam())) {
                        bte = eachBaseCalcul.getBaseRam().getBte();
                    }
                    if (Objects.nonNull(bte)) {
                        an1 = String.valueOf(bte.getAn1());
                        an2 = String.valueOf(bte.getAn2());
                        an4 = String.valueOf(bte.getAn4());
                    }
                    String tauxReductionAnticipation = null;
                    if (Objects.nonNull(eachBaseCalcul.getAnticipation()) && !eachBaseCalcul.getAnticipation().getTranche().isEmpty())
                        tauxReductionAnticipation = String.valueOf(eachBaseCalcul.getAnticipation().getTranche().get(0).getTauxReductionAnticipation());

                    for (FCalcul.Evenement.BasesCalcul.Decision eachDecision : eachBaseCalcul.getDecision()) {
                        for (FCalcul.Evenement.BasesCalcul.Decision.Prestation eachPrestation : eachDecision.getPrestation()) {
                            // Il faut retrouver la base de calcul + rente
                            // accordee et $p
                            // pour leur affecter les années bte et taux
                            // réduction
                            if (Objects.nonNull(eachPrestation.getRente())) {
                                String nss = eachPrestation.getBeneficiaire();
                                String ddAAAAMMJJ = String.valueOf(eachPrestation.getRente().getDebutDroit());

                                boolean baseCalculSimFound = false;
                                for (RERenteAccordee eachRA : raManager.getContainerAsList()) {
                                    PRAssert.notIsNew(eachRA, null);

                                    PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, eachRA.getIdTiersBeneficiaire());
                                    String nss2 = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                                    nss = NSUtil.unFormatAVS(nss);
                                    nss2 = NSUtil.unFormatAVS(nss2);

                                    String ddAAAAMMJJ2 = PRDateFormater
                                            .convertDate_JJxMMxAAAA_to_AAAAMMJJ(eachRA.getDateDebutDroit());

                                    String codePrst1 = eachRA.getCodePrestation();
                                    String codePrst2 = String.valueOf(eachPrestation.getRente().getGenre());

                                    if (nss.equals(nss2) && ddAAAAMMJJ.equals(ddAAAAMMJJ2) && codePrst1.equals(codePrst2)) {

                                        if (Objects.nonNull(tauxReductionAnticipation)) {
                                            eachRA.setTauxReductionAnticipation(tauxReductionAnticipation);
                                            eachRA.update(transaction);
                                        }

                                        if (Objects.isNull(eachBaseCalcul.getAnalysePeriodes()) && isAnDecimalNonNull(bte) && !baseCalculSimFound) {
                                            REBasesCalcul baseCalculSim = getBaseCalculSim(session, transaction, eachRA);
                                            // Si les nb BTE de la base de calcul similaire sont non nuls, on set leur valeur.
                                            if (!JadeStringUtil.isBlankOrZero(baseCalculSim.getNombreAnneeBTE1()) || !JadeStringUtil.isBlankOrZero(baseCalculSim.getNombreAnneeBTE2()) || !JadeStringUtil.isBlankOrZero(baseCalculSim.getNombreAnneeBTE4())) {
                                                an1 = baseCalculSim.getNombreAnneeBTE1();
                                                an2 = baseCalculSim.getNombreAnneeBTE2();
                                                an4 = baseCalculSim.getNombreAnneeBTE4();
                                                rentesWithoutBte.remove(eachRA.getId());
                                                baseCalculSimFound = true;
                                            }
                                            // Sinon, on set leur valeur à vide et on ajoute l'id de la rente calculée à une liste pour prévenir l'utilisateur.
                                            else {
                                                an1 = StringUtils.EMPTY;
                                                an2 = StringUtils.EMPTY;
                                                an4 = StringUtils.EMPTY;
                                                rentesWithoutBte.add(eachRA.getId());
                                            }
                                        }

                                        if (!JadeStringUtil.isBlankOrZero(an1) || !JadeStringUtil.isBlankOrZero(an2) || !JadeStringUtil.isBlankOrZero(an4)) {
                                            // On récupère la base de calcul liée à cette rente de calcul depuis la base de données.
                                            REBasesCalcul basesCalc = new REBasesCalcul();
                                            basesCalc.setSession(session);
                                            basesCalc.setIdBasesCalcul(eachRA.getIdBaseCalcul());
                                            basesCalc.retrieve(transaction);
                                            PRAssert.notIsNew(basesCalc, null);

                                            basesCalc.setNombreAnneeBTE1(an1);
                                            basesCalc.setNombreAnneeBTE2(an2);
                                            basesCalc.setNombreAnneeBTE4(an4);
                                            basesCalc.update(transaction);
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isAnDecimalNonNull(FCalcul.Evenement.BasesCalcul.BaseRam.Bte bte) {
        if (Objects.nonNull(bte) && Objects.nonNull(bte.getAnDecimal())) {
            return bte.getAnDecimal() != 0.0;
        }
        return false;
    }

    /**
     * Permet de récupérer la base de calcul similaire à celle de la rente calculée : même code prestation, même tiers bénéficiaire et état "en cours".
     * On contrôle également sur les dates de droit pour récupérer la dernière rente.
     *
     * @param session
     * @param transaction
     * @param eachRA : la rente calculée
     * @return la base de calcul similaire à celle de la rente calculée.
     * @throws Exception
     */
    private REBasesCalcul getBaseCalculSim(final BSession session, final BTransaction transaction, RERenteAccordee eachRA) throws Exception {
        RERenteAccordeeManager raManager = new RERenteAccordeeManager();
        raManager.setSession(session);
        raManager.setForCodesPrestationsIn(eachRA.getCodePrestation());
        raManager.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL + ", " + IREPrestationAccordee.CS_ETAT_DIMINUE);
        raManager.setForIdTiersBeneficiaire(eachRA.getIdTiersBeneficiaire());
        raManager.find(transaction, BManager.SIZE_NOLIMIT);

        String idBaseCalculSim = StringUtils.EMPTY;
        String dateFinDroitRente = StringUtils.EMPTY;
        for (RERenteAccordee eachRente : raManager.getContainerAsList()) {
            // On contrôle si la date de début de rente est bien antérieure à celle que l'on calcule.
            if (eachRente.getDateDebutDroit().compareTo(eachRA.getDateDebutDroit()) < 0) {
                // Si la date de fin de rente est vide, il s'agit de la dernière rente similaire.
                if (eachRente.getDateFinDroit().isEmpty()) {
                    idBaseCalculSim = eachRente.getIdBaseCalcul();
                    break;
                } else {
                    // Sinon, on récupère la rente dont la date de fin est la plus récente.
                    if (dateFinDroitRente.compareTo(eachRente.getDateFinDroit()) < 0) {
                        dateFinDroitRente = eachRente.getDateFinDroit();
                        idBaseCalculSim = eachRente.getIdBaseCalcul();
                    }
                }
            }
        }
        REBasesCalcul baseCalculSim = new REBasesCalcul();
        baseCalculSim.setIdBasesCalcul(idBaseCalculSim);
        baseCalculSim.setSession(session);
        baseCalculSim.retrieve(transaction);

        return baseCalculSim;
    }

    private void doTraitementCIAdd(final BSession session, final BITransaction transaction, final KeyAP k,
                                   final ValueAP v) throws Exception {
        List<String> l = v.idsRCI;
        for (Iterator<String> iterator = l.iterator(); iterator.hasNext(); ) {
            String idRCI = iterator.next();
            RERassemblementCI rci = new RERassemblementCI();
            rci.setSession(session);
            rci.setIdRCI(idRCI);
            rci.retrieve(transaction);
            rci.setDateTraitement(JACalendar.todayJJsMMsAAAA());
            rci.update(transaction);
        }

        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(session);
        ra.setIdPrestationAccordee(v.idRA);
        ra.retrieve(transaction);

        REBasesCalcul bc = new REBasesCalcul();
        bc.setSession(session);
        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
        bc.retrieve(transaction);

        bc.setRevenuAnnuelMoyen(v.nouveauMontantRA);
        bc.update(transaction);

        // Création des annonces ponctuelles pour toutes les RA, y compris les
        // complémentaires, ayant des montant différents.
        REAnnoncePonctuelleViewBean vb = new REAnnoncePonctuelleViewBean();
        vb.setISession(session);
        vb.setIdRenteAccordee(ra.getIdPrestationAccordee());
        vb.setCsEtatCivil(ra.getCsEtatCivil());
        vb.setCs1(ra.getCodeCasSpeciaux1());
        vb.setCs2(ra.getCodeCasSpeciaux2());
        vb.setCs3(ra.getCodeCasSpeciaux3());
        vb.setCs4(ra.getCodeCasSpeciaux4());
        vb.setCs5(ra.getCodeCasSpeciaux5());

        vb.setCodeRefugie(ra.getCodeRefugie());
        vb.setRevenuPrisEnCompte9(bc.getRevenuPrisEnCompte());
        vb.setRAM(bc.getRevenuAnnuelMoyen());
        vb.setDegreInvalidite(bc.getDegreInvalidite());
        vb.setCleInfirmite(bc.getCleInfirmiteAyantDroit());
        vb.setSurvenanceEvenementAssure(bc.getSurvenanceEvtAssAyantDroit());
        vb.setIsInvaliditePrecoce(bc.isInvaliditePrecoce());
        vb.setOfficeAI(bc.getCodeOfficeAi());
        vb.setDroitApplique(bc.getDroitApplique());
        vb.setIdTiersBeneficiaire(ra.getIdTiersBeneficiaire());

        vb.setIdTiersComplementaire1(ra.getIdTiersComplementaire1());
        vb.setIdTiersComplementaire2(ra.getIdTiersComplementaire2());

        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, ra.getIdTiersComplementaire1());
        vb.setNssComplementaire1(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
        tw = PRTiersHelper.getTiersParId(session, ra.getIdTiersComplementaire2());
        vb.setNssComplementaire2(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

        // Set code etat civil et code canton !!!
        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, ra.getIdTiersBeneficiaire());
        String csEtatCivil = null;
        String csCantonDomicile = null;

        ISFMembreFamilleRequerant[] mf = sf.getMembresFamille(ra.getIdTiersBeneficiaire());
        for (int i = 0; i < mf.length; i++) {
            ISFMembreFamilleRequerant membre = mf[i];
            // On récupère le bénéficiaire en tant que membre de famille
            if (ra.getIdTiersBeneficiaire().equals(membre.getIdTiers())) {
                csEtatCivil = membre.getCsEtatCivil();
                csCantonDomicile = membre.getCsCantonDomicile();
                if (csCantonDomicile == null) {
                    csCantonDomicile = session.getCode(membre.getCsNationalite());
                }
                break;
            }
        }
        // Peut arriver dans le cas d'un enfant de la situation familialle, par exemple.
        if (csEtatCivil == null) {
            csEtatCivil = ISFSituationFamiliale.CS_ETAT_CIVIL_CELIBATAIRE;
        }
        vb.setCodeEtatCivil(PRACORConst.csEtatCivilHeraToAcorForRentes(session, csEtatCivil));
        vb.setCsEtatCivil(PRACORConst.csEtatCivilHeraToCsEtatCivil(csEtatCivil));
        vb.setCanton(PRACORConst.csCantonToAcor(csCantonDomicile));

        vb.setCodeRefugie(ra.getCodeRefugie());
        vb.setGenrePrestation(ra.getCodePrestation());
        vb.setIdBaseCalcul(ra.getIdBaseCalcul());

        // Créer l'annonce ponctuelle.
        REAnnoncePonctuelleHelper helper = new REAnnoncePonctuelleHelper();

        helper.ajouterAnnoncePonctuelle(vb, null, session);
    }

    private int doTraitementForCIAdditionnel(final BSession session, final BITransaction transaction,
                                             final Map<KeyAP, ValueAP> mapAP) throws Exception {

        /*
         * 1)
         */
        // Parcours et traitement de la map
        Set<KeyAP> keys = mapAP.keySet();
        Iterator<KeyAP> i = keys.iterator();

        // Si au moins un changement dans les montants, on sort et effectue le traitement
        // standard.
        while (i.hasNext()) {
            RECalculACORDemandeRenteHelper.KeyAP keyAP = i.next();
            ValueAP v = mapAP.get(keyAP);

            if (!v.ancienMontantRA.equals(v.nouveauMontantRA)) {
                return 1;
            }
        }

        // IMPORTANT : La suite n'est jamais exécutée ! v.ancienMontantRA n'est et ne sera jamais égal à
        // v.nouveauMontantRA (les montants ne sont pas formatté de la même manière -> 2350.00 et 0002350)
        // Cette partie n'est pas à corriger !!

        i = keys.iterator();

        // Si on arrive ici, on créé les annonces ponctuelles uniquement pour
        // les RA
        // ayant un RAM qui diffère.
        while (i.hasNext()) {
            RECalculACORDemandeRenteHelper.KeyAP keyAP = i.next();
            ValueAP v = mapAP.get(keyAP);
            if (!v.ancienRAM.equals(v.nouveauRAM)) {
                doTraitementCIAdd(session, transaction, keyAP, v);
            }
        }

        return 0;
    }

    /**
     * Créé / MAJ de la base de calcul, suivant le cas à traiter.
     *
     * @param session
     * @param transaction
     * @param demandeSource
     * @param noCasATraiter
     * @return l'id de la base de calcul.
     * @throws Exception
     */
    private String doTraiterBaseCalcul(final BSession session, final BTransaction transaction,
                                       final REDemandeRente demandeSource, final int noCasATraiter) throws Exception {

        switch (noCasATraiter) {

            case CAS_NOUVEAU_CALCUL:
                RERenteCalculee rc = new RERenteCalculee();
                rc.setSession(session);
                rc.setIdRenteCalculee(demandeSource.getIdRenteCalculee());
                rc.retrieve(transaction);

                if (rc.isNew()) {

                    // Avant de le remplacer, on s'assure qu'il n'y a plus aucune
                    // base de calcul liée à cet ancien ID !!!
                    if (!JadeStringUtil.isBlankOrZero(demandeSource.getIdRenteCalculee())) {
                        REBasesCalculManager mgr = new REBasesCalculManager();
                        mgr.setSession(session);
                        mgr.setForIdRenteCalculee(demandeSource.getIdRenteCalculee());
                        mgr.find(transaction, 2);
                        if (!mgr.isEmpty()) {
                            throw new Exception(session.getLabel("ERREUR_INCOHERANCE_DONNEES") + rc.getIdRenteCalculee()
                                    + "/" + demandeSource.getIdDemandeRente());
                        }
                    }

                    // Rente calculee
                    RERenteCalculee rc1 = new RERenteCalculee();
                    rc1.setSession(session);
                    rc1.add(transaction);
                    demandeSource.setIdRenteCalculee(rc1.getIdRenteCalculee());
                } else {
                    demandeSource.setIdRenteCalculee(rc.getIdRenteCalculee());
                }

                demandeSource.setDateTraitement(REACORParser.retrieveDateTraitement(demandeSource));
                demandeSource.update(transaction);

                REBasesCalculDixiemeRevision bc = new REBasesCalculDixiemeRevision();
                bc.setSession(session);
                bc.setIdRenteCalculee(demandeSource.getIdRenteCalculee());
                bc.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
                // Pas important dans le cas des API, on met 10ème révision par
                // défaut
                bc.setDroitApplique(REBasesCalcul.REVISION_NO_10);

                // BZ 4876 Ajout de différentes données dans la base de calcul
                REDemandeRenteAPI demAPI = new REDemandeRenteAPI();
                demAPI.setSession(session);
                demAPI.setIdDemandeRente(demandeSource.getIdDemandeRente());
                demAPI.retrieve();

                if (!demAPI.isNew()) {
                    bc.setIsDemandeRenteAPI(true);
                }

                bc.setCleInfirmiteAyantDroit(
                        session.getCode(demAPI.getCsInfirmite()) + session.getCode(demAPI.getCsAtteinte()));
                bc.setCodeOfficeAi(demAPI.getCodeOfficeAI());

                // Date survenance événement vide ? Première période
                if (!JadeStringUtil.isBlankOrZero(demAPI.getDateSuvenanceEvenementAssure())) {
                    bc.setSurvenanceEvtAssAyantDroit(
                            PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(demAPI.getDateSuvenanceEvenementAssure()));
                } else {
                    REPeriodeAPIManager apiMgr = new REPeriodeAPIManager();
                    apiMgr.setSession(session);
                    apiMgr.setForIdDemandeRente(demAPI.getIdDemandeRente());
                    apiMgr.find();

                    JADate dateMin = new JADate("31.12.2999");

                    JACalendar cal = new JACalendarGregorian();

                    for (REPeriodeAPI periode : apiMgr.getContainerAsList()) {
                        JADate dateEnCours = new JADate(periode.getDateDebutInvalidite());

                        if (cal.compare(dateMin, dateEnCours) == JACalendar.COMPARE_SECONDLOWER) {
                            dateMin = dateEnCours;
                        }

                    }

                    bc.setSurvenanceEvtAssAyantDroit(
                            PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateMin.toStrAMJ()));

                }
                // Fin BZ 4876

                // chargement de la demande de prestation pour retrieve de
                // l'idTiersRequerant
                PRDemande demandePrest = new PRDemande();
                demandePrest.setSession(session);
                demandePrest.setIdDemande(demandeSource.getIdDemandePrestation());
                demandePrest.retrieve(transaction);

                bc.setIdTiersBaseCalcul(demandePrest.getIdTiers());
                bc.setReferenceDecision("0");
                bc.add(transaction);
                return bc.getIdBasesCalcul();

            case CAS_RECALCUL_DEMANDE_NON_VALIDEE:
                // Rente calculee
                rc = new RERenteCalculee();
                rc.setSession(session);
                rc.setIdRenteCalculee(demandeSource.getIdRenteCalculee());
                rc.retrieve(transaction);
                if (rc.isNew()) {
                    throw new PRACORException("!!! RC not found. idRC/idDemande = " + demandeSource.getIdRenteCalculee()
                            + "/" + demandeSource.getIdDemandeRente());
                }

                demandeSource.setDateTraitement(REACORParser.retrieveDateTraitement(demandeSource));
                demandeSource.update(transaction);

                REBasesCalcul newBC = new REBasesCalculDixiemeRevision();

                newBC.setIdRenteCalculee(rc.getIdRenteCalculee());
                newBC.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
                // Pas important dans le cas des API, on met 10ème révision par
                // défaut
                newBC.setDroitApplique(REBasesCalcul.REVISION_NO_10);
                REDeleteCascadeDemandeAPrestationsDues.supprimerBasesCalculCascade_noCommit(session, transaction, rc,
                        IREValidationLevel.VALIDATION_LEVEL_NONE);

                // chargement de la demande de prestation pour retrieve de
                // l'idTiersRequerant
                PRDemande demandePrest1 = new PRDemande();
                demandePrest1.setSession(session);
                demandePrest1.setIdDemande(demandeSource.getIdDemandePrestation());
                demandePrest1.retrieve(transaction);

                newBC.setIdTiersBaseCalcul(demandePrest1.getIdTiers());

                // BZ 4876 Ajout de différentes données dans la base de calcul
                demAPI = new REDemandeRenteAPI();
                demAPI.setSession(session);
                demAPI.setIdDemandeRente(demandeSource.getIdDemandeRente());
                demAPI.retrieve();

                if (!demAPI.isNew()) {
                    newBC.setIsDemandeRenteAPI(true);
                }

                newBC.setCleInfirmiteAyantDroit(session.getCode(demAPI.getCsInfirmite()));
                newBC.setCodeOfficeAi(demAPI.getCodeOfficeAI());

                // Date survenance événement vide ? Première période
                if (!JadeStringUtil.isBlankOrZero(demAPI.getDateSuvenanceEvenementAssure())) {
                    newBC.setSurvenanceEvtAssAyantDroit(
                            PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(demAPI.getDateSuvenanceEvenementAssure()));
                } else {
                    REPeriodeAPIManager apiMgr = new REPeriodeAPIManager();
                    apiMgr.setSession(session);
                    apiMgr.setForIdDemandeRente(demAPI.getIdDemandeRente());
                    apiMgr.find();

                    JADate dateMin = new JADate("31.12.2999");

                    JACalendar cal = new JACalendarGregorian();

                    for (REPeriodeAPI periode : apiMgr.getContainerAsList()) {

                        JADate dateEnCours = new JADate(periode.getDateDebutInvalidite());

                        if (cal.compare(dateMin, dateEnCours) == JACalendar.COMPARE_SECONDLOWER) {
                            dateMin = dateEnCours;
                        }

                    }

                    newBC.setSurvenanceEvtAssAyantDroit(
                            PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateMin.toStrAMJ()));

                }
                // Fin BZ 4876

                newBC.setReferenceDecision("0");
                newBC.add(transaction);
                return newBC.getIdBasesCalcul();

            case CAS_RECALCUL_DEMANDE_VALIDEE:

                // On crée un clone de la demande source, comme demande 'enfant'
                REDemandeRente copieDemandeEnfant = REDemandeRegles.corrigerDemandeRente(session, transaction,
                        demandeSource);

                rc = new RERenteCalculee();
                rc.setSession(session);
                rc.add(transaction);
                copieDemandeEnfant.setIdRenteCalculee(rc.getIdRenteCalculee());
                copieDemandeEnfant.setDateTraitement(REACORParser.retrieveDateTraitement(copieDemandeEnfant));
                copieDemandeEnfant.update(transaction);

                copieDemandeEnfant.update(transaction);

                bc = new REBasesCalculDixiemeRevision();
                bc.setSession(session);
                bc.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
                bc.setIdRenteCalculee(rc.getIdRenteCalculee());
                bc.setDroitApplique(REBasesCalcul.REVISION_NO_10);

                // BZ 4876 Ajout de différentes données dans la base de calcul
                demAPI = new REDemandeRenteAPI();
                demAPI.setSession(session);
                demAPI.setIdDemandeRente(demandeSource.getIdDemandeRente());
                demAPI.retrieve();

                if (!demAPI.isNew()) {
                    bc.setIsDemandeRenteAPI(true);
                }

                bc.setCleInfirmiteAyantDroit(session.getCode(demAPI.getCsInfirmite()));
                bc.setCodeOfficeAi(demAPI.getCodeOfficeAI());

                // Date survenance événement vide ? Première période
                if (!JadeStringUtil.isBlankOrZero(demAPI.getDateSuvenanceEvenementAssure())) {
                    bc.setSurvenanceEvtAssAyantDroit(
                            PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(demAPI.getDateSuvenanceEvenementAssure()));
                } else {
                    REPeriodeAPIManager apiMgr = new REPeriodeAPIManager();
                    apiMgr.setSession(session);
                    apiMgr.setForIdDemandeRente(demAPI.getIdDemandeRente());
                    apiMgr.find();

                    JADate dateMin = new JADate("31.12.2999");

                    JACalendar cal = new JACalendarGregorian();

                    for (REPeriodeAPI periode : apiMgr.getContainerAsList()) {

                        JADate dateEnCours = new JADate(periode.getDateDebutInvalidite());

                        if (cal.compare(dateMin, dateEnCours) == JACalendar.COMPARE_SECONDLOWER) {
                            dateMin = dateEnCours;
                        }

                    }

                    bc.setSurvenanceEvtAssAyantDroit(
                            PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateMin.toStrAMJ()));

                }
                // Fin BZ 4876

                // chargement de la demande de prestation pour retrieve de
                // l'idTiersRequerant
                PRDemande demandePrest2 = new PRDemande();
                demandePrest2.setSession(session);
                demandePrest2.setIdDemande(demandeSource.getIdDemandePrestation());
                demandePrest2.retrieve(transaction);

                bc.setIdTiersBaseCalcul(demandePrest2.getIdTiers());
                bc.setReferenceDecision("0");
                bc.add(transaction);
                return bc.getIdBasesCalcul();

            default:
                throw new PRACORException(session.getLabel("ERREUR_RECALCUL_CAS"));
        }

    }

    /**
     * retrouve par introspection la methode a executer quand on arrive dans ce helper avec une action custom.
     *
     * @param viewBean DOCUMENT ME!
     * @param action   DOCUMENT ME!
     * @param session  DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface execute(final FWViewBeanInterface viewBean, final FWAction action,
                                          final BISession session) {

        return deleguerExecute(viewBean, action, session);
    }

    /**
     * @param date1
     * @param date2
     * @return le nombre de mois en 2 dates. Condition : date1 antérieure à date2
     */
    private int getNombreMois(final JADate date1, final JADate date2) {
        int diffAnnee = date2.getYear() - date1.getYear();

        if (diffAnnee == 0) {
            return (date2.getMonth() - date1.getMonth()) + 1;
        } else if (diffAnnee == 1) {
            return ((12 - date1.getMonth()) + 1) + date2.getMonth();
        } else {
            return ((diffAnnee - 1) * 12) + (((12 - date1.getMonth()) + 1) + date2.getMonth());
        }
    }

    /**
     * Récupère les info complémentaire liées à la demande si elle existent, le cas échéant les info complémentaire
     * seront crées et référencé dans la demande
     *
     * @return Dans tous les cas, l'entité PRInfoCompl associée à la demande
     * @throws Exception S'il n'est pas possible de récupérer les infos compl
     */
    private PRInfoCompl getOrCreateInfoComplementaire(final BSession session, final BTransaction transaction,
                                                      final REDemandeRente demande) throws Exception {
        PRInfoCompl infoComplementaire = new PRInfoCompl();
        infoComplementaire.setSession(session);

        String idInfoComplementaire = demande.getIdInfoComplementaire();

        // Si les info compl n'existent pas, on créer l'entité et on la référence dans la demande
        if (JadeStringUtil.isBlankOrZero(idInfoComplementaire)) {
            infoComplementaire.add(transaction);
            demande.setIdInfoComplementaire(infoComplementaire.getIdInfoCompl());
            demande.update(transaction);
        }
        // Si les info compl existent, on tente de la récupérer
        else {
            infoComplementaire.setIdInfoCompl(idInfoComplementaire);
            infoComplementaire.retrieve(transaction);
            if (infoComplementaire.isNew()) {
                String message = session
                        .getLabel("CALCULER_DECISION_ERREUR_IMPOSSIBLE_TROUVER_INFORMATION_COMPLEMENTAIRE");
                message = message.replace("{0}", idInfoComplementaire);
                throw new Exception(message);
            }
        }
        return infoComplementaire;
    }

    /**
     * @param session
     * @param caViewbean
     * @param transaction
     * @param mapAP
     * @return
     * @throws PRACORException
     * @throws Exception
     */
    private int importationCIAdditionnelsDepuisCalculACOR(final BSession session,
                                                          final RECalculACORDemandeRenteViewBean caViewbean, final BITransaction transaction,
                                                          final Map<KeyAP, ValueAP> mapAP) throws PRACORException, Exception {

        List<REFeuilleCalculVO> fcs = globaz.corvus.acor.parser.rev09.REACORParser.parseCIAdd(session, transaction,
                caViewbean.loadDemandeRente(null), new StringReader(caViewbean.getContenuAnnoncePay()));

        // Contrôle des cas à traiter en faisant les différences entre
        // la MAP de traitement des annonces ponctuelles et les données
        // de la feuille de calcul !!!
        Set<KeyAP> keys = mapAP.keySet();

        for (Iterator<KeyAP> iterator2 = keys.iterator(); iterator2.hasNext(); ) {
            KeyAP keyAP = iterator2.next();

            // On itère pour voir si cette clé correspond à une BC-RA
            // remontée de ACOR !!!
            for (Iterator<REFeuilleCalculVO> iterator = fcs.iterator(); iterator.hasNext(); ) {
                REFeuilleCalculVO fcvo = iterator.next();

                List<REFeuilleCalculVO.ElementVO> elementsFC = fcvo.getElementsAP();

                for (Iterator<REFeuilleCalculVO.ElementVO> iterator3 = elementsFC.iterator(); iterator3.hasNext(); ) {
                    REFeuilleCalculVO.ElementVO elmFC = iterator3.next();

                    if (keyAP.idTiers.equals(elmFC.getIdTiers()) && keyAP.genreRente.equals(elmFC.getGenreRente())) {

                        // !!!Element found, on set les nouveaux montant dans la MAP
                        ValueAP val = mapAP.get(keyAP);
                        val.nouveauMontantRA = elmFC.getMontantRente();
                        val.nouveauRAM = elmFC.getRAM();
                    }
                }
            }
        }

        int ret = doTraitementForCIAdditionnel(session, transaction, mapAP);
        return ret;
    }

    /**
     * @param session
     * @param caViewbean
     * @param transaction
     * @param rentesAccordees
     * @param noCasATraiter
     * @param fCalcul
     * @return L'id de la copie de la demande si la demande original à été copié
     * @throws PRACORException
     * @throws Exception
     */
    private Long importationDesAnnoncesDuCalculACOR(final BSession session,
                                                    final RECalculACORDemandeRenteViewBean caViewbean, final BITransaction transaction,
                                                    final LinkedList<Long> rentesAccordees, final int noCasATraiter, final FCalcul fCalcul)
            throws PRACORException, Exception {

        StringReader annoncePayReader = new StringReader(caViewbean.getContenuAnnoncePay());
        REDemandeRente demande = caViewbean.loadDemandeRente(null);

        ReturnedValue returnedValue = globaz.corvus.acor.parser.rev09.REACORParser.parse(session, transaction, demande,
                annoncePayReader, noCasATraiter);

        rentesAccordees.addAll(returnedValue.getIdRenteAccordees());

        /*
         * Maj de qqes données supplémentaire récupérée depuis la
         * feuille de calcul acor taux de reduction, BTE entière, demi, quart...
         */
        if ((fCalcul != null) && (fCalcul.getEvenement().size() > 0)) {
            doMAJExtraData(session, (BTransaction) transaction, fCalcul, rentesAccordees);
        }

        /* Lecture du fichier annonce.rr en priorité */
        /*
         * Mis en commentaire car le fichier ne sera plus généré par ACOR
         * if (!JadeStringUtil.isEmpty(caViewbean.getContenuAnnonceRR())) {
         * globaz.corvus.acor.parser.rev09.REACORParser.parseAnnonceRR(session, transaction, new StringReader(
         * caViewbean.getContenuAnnonceRR()), rentesAccordees);
         * } /*
         * /* Lecture du fichier annonce.xml si annonce.rr n'existe pas
         */
        if (!JadeStringUtil.isEmpty(caViewbean.getContenuAnnonceXML())) {
            REACORAnnonceXmlReader annonceXmlReader = new REACORAnnonceXmlReader();
            annonceXmlReader.readAnnonceXmlContent(session, (BTransaction) transaction,
                    caViewbean.getContenuAnnonceXML(), rentesAccordees);
        } else {
            // Si aucun fichier d'annonce à lire, on ne lit rien
        }
        return returnedValue.getIdCopieDemande();
    }

    private ValueAP initVAP(final BSession session, final BITransaction transaction, final RERenteAccordee ra,
                            final List<String> idsRCI) throws Exception {

        ValueAP v = new ValueAP();
        v.ancienMontantRA = ra.getMontantPrestation();
        REBasesCalcul bc = new REBasesCalcul();
        bc.setSession(session);
        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
        bc.retrieve(transaction);
        v.ancienRAM = bc.getRevenuAnnuelMoyen();
        v.idRA = ra.getIdPrestationAccordee();
        v.idsRCI = idsRCI;

        return v;
    }

    /**
     * Récupère ou créer les info complémentaire liées à la demande et renseigne les flags boolean liés au rente avec
     * remarque particulière
     *
     * @param session
     * @param transaction
     * @param idDemande
     * @param hasRenteLimitee
     * @param isRenteAvecSupplementPourPersonneVeuve
     * @param isRenteAvecDebutDroit5AnsAvantDepotDemande
     * @throws Exception
     */
    private void miseAJourInfoComplementaire(final BSession session, final BITransaction transaction,
                                             final String idDemande, final boolean hasRenteLimitee, final boolean isRenteAvecSupplementPourPersonneVeuve,
                                             final boolean isRenteAvecDebutDroit5AnsAvantDepotDemande,
                                             final boolean isRenteAvecMontantMinimumMajoreInvalidite, final boolean isRenteReduitePourSurassurance)
            throws Exception {
        if (JadeStringUtil.isBlankOrZero(idDemande)) {
            String message = session.getLabel("CALCULER_DECISION_ERREUR_ID_DEMANDE_EST_VIDE");
            throw new Exception(message);
        }
        REDemandeRente demande = new REDemandeRente();
        demande.setSession(session);
        demande.setIdDemandeRente(idDemande);
        demande.retrieve(transaction);
        if (demande.isNew()) {
            String message = session.getLabel("CALCULER_DECISION_ERREUR_IMPOSSIBLE_TROUVER_DEMANDE");
            message = message.replace("{0}", idDemande);
            throw new Exception(message);
        }
        PRInfoCompl infoComplementaire = getOrCreateInfoComplementaire(session, (BTransaction) transaction, demande);
        infoComplementaire.setIsRenteLimitee(hasRenteLimitee);
        infoComplementaire.setIsRenteAvecSupplementPourPersonneVeuve(isRenteAvecSupplementPourPersonneVeuve);
        infoComplementaire.setIsRenteAvecDebutDroit5AnsAvantDepotDemande(isRenteAvecDebutDroit5AnsAvantDepotDemande);
        infoComplementaire.setIsRenteAvecMontantMinimumMajoreInvalidite(isRenteAvecMontantMinimumMajoreInvalidite);
        infoComplementaire.setIsRenteReduitePourSurassurance(isRenteReduitePourSurassurance);
        infoComplementaire.update(transaction);
    }

    /**
     * @param idTiersRequrant   L'id tiers du requérant de la demande pour laquelle on remonte le calcul
     * @param idDemandeCourante Id de la demande pour laquelle on est en train de remonter le calcul
     * @throws Exception
     */
    private String separerLesDemandesSiBesoin(final Long idTiersRequrant, long idDemandeCourante) throws
            Exception {

        BSession session = BSessionUtil.getSessionFromThreadContext();
        if (session == null) {
            throw new RETechnicalException(
                    "separerLesDemandesSiBesoin(idTiersRequrant, idDemandeCourante) : Unable to get a  session from the threadContext");
        }

        if (idDemandeCourante == 0) {
            String message = session.getLabel("IMPORTATION_CALCUL_ACOR_IMPOSSIBLE_RETROUVER_DEMANDE_COURANTE_ID_0");
            throw new REBusinessException(message);
        }

        PersonneAVS requerant = PyxisCrudServiceLocator.getPersonneAvsCrudService().read(idTiersRequrant);
        Set<DemandeRente> demandesDeLaFamille = CorvusServiceLocator.getDemandeRenteService()
                .demandesDuRequerantEtDeSaFamille(requerant);

        // En premier lieu il faut récupérer la demande courante avant le filtrage sous peine de problème dans le cas de
        // calcul de demande validée
        DemandeRente demandeCourante = recupererDemandeCourante(demandesDeLaFamille, idDemandeCourante);
        if (demandeCourante == null) {
            String message = session.getLabel("IMPORTATION_CALCUL_ACOR_IMPOSSIBLE_RETROUVER_DEMANDE_COURANTE");
            message = message.replace("{0}", Long.toString(idDemandeCourante));
            throw new REBusinessException(message);
        }

        // On ne prend que les demandes non validées
        demandesDeLaFamille = demandesNonValidees(demandesDeLaFamille);
        // de type standard ou transitoire
        demandesDeLaFamille = demandesStandardsOuTransitoires(demandesDeLaFamille);
        // sans les demandes API
        demandesDeLaFamille = filtrerDemandeAPI(demandesDeLaFamille);

        /*
         * Récupération des base de calcul de la demande courante pour voir si des CCS 08 sont présent ou pas dans les
         * bases de calcul
         */
        List<BaseCalculWrapper> basesCalculsDemandeCourante = getBCWrapperDeLaDemande(demandeCourante, session);

        boolean bcAvecCCS08Present = false;
        boolean bcAvecCCS08Absent = false;
        for (BaseCalculWrapper baseCalcul : basesCalculsDemandeCourante) {
            if (baseCalcul.hasCodeCasSpecial08()) {
                bcAvecCCS08Present = true;
            } else {
                bcAvecCCS08Absent = true;
            }
        }

        /*
         * Génération des clé de comparaison pour les demandes ouvertes de la famille
         */
        DemandeRenteWrapper demandeCouranteWrapper = new DemandeRenteWrapper(demandeCourante);
        Set<DemandeRenteWrapper> demandesDeLaFamilleWrappers = new HashSet<DemandeRenteWrapper>();
        for (DemandeRente demandeRente : demandesDeLaFamille) {
            DemandeRenteWrapper demandeRenteCle = new DemandeRenteWrapper(demandeRente);
            demandesDeLaFamilleWrappers.add(demandeRenteCle);
        }

        /*
         * Le fait que l'on trouve des bases de calcul avec CCS 08 et d'autres sans CCS 08 va déterminer la façon dont
         * on va dispatcher les BC dans les demandes ouvertes de la familles
         */
        String message = null;
        if (bcAvecCCS08Present) {
            if (bcAvecCCS08Absent) {
                message = deplacerRenteAccordeesModeB(requerant, demandeCouranteWrapper, basesCalculsDemandeCourante,
                        demandesDeLaFamilleWrappers, session);
            } else {
                deplacerRenteAccordeesModeA(requerant, demandeCouranteWrapper, basesCalculsDemandeCourante,
                        demandesDeLaFamilleWrappers, session);
            }
        } else {
            deplacerRenteAccordeesModeA(requerant, demandeCouranteWrapper, basesCalculsDemandeCourante,
                    demandesDeLaFamilleWrappers, session);
        }

        miseAjourDateDeLaDemande(session, demandeCourante.getId());
        miseAjourDateDesDemandes(session, demandesDeLaFamilleWrappers);
        return message;
    }

    /**
     * @param demandeCourante
     * @param session
     * @throws Exception
     */
    private List<BaseCalculWrapper> getBCWrapperDeLaDemande(DemandeRente demandeCourante, BSession session)
            throws Exception {
        List<BaseCalculWrapper> basesCalculWrappers = new ArrayList<RECalculACORDemandeRenteHelper.BaseCalculWrapper>();

        REDemandeRente demandeEntity = new REDemandeRente();
        demandeEntity.setSession(session);
        demandeEntity.setIdDemandeRente(demandeCourante.getId().toString());
        demandeEntity.retrieve();
        if (demandeEntity.isNew()) {
            String message = session.getLabel("IMPORTATION_CALCUL_ACOR_IMPOSSIBLE_RETROUVER_DEMANDE");
            message = message.replace("{0}", demandeCourante.getId().toString());
            throw new REBusinessException(message);
        }

        if (!JadeStringUtil.isBlankOrZero(demandeEntity.getIdRenteCalculee())) {

            REBasesCalculManager manager = new REBasesCalculManager();
            manager.setSession(session);
            manager.setForIdRenteCalculee(demandeEntity.getIdRenteCalculee());
            manager.find(BManager.SIZE_NOLIMIT);

            RERenteAccordeeManager raManager = new RERenteAccordeeManager();
            raManager.setSession(session);

            // Conversion ou wrapping des base de calcul dans un objet qui nous permet d'analyser les infos voulue
            for (Object o : manager.getContainer()) {
                REBasesCalcul basesCalcul = (REBasesCalcul) o;
                raManager.setForIdBaseCalcul(basesCalcul.getId());
                raManager.find(BManager.SIZE_NOLIMIT);
                List<RERenteAccordee> ras = new ArrayList<RERenteAccordee>();
                for (Object object : raManager.getContainer()) {
                    ras.add((RERenteAccordee) object);
                }
                basesCalculWrappers.add(new BaseCalculWrapper(basesCalcul, ras));
            }
        }
        return basesCalculWrappers;
    }

    /**
     * Répartis les rentes accordées selon le modèle A.
     * Le modèle A de répartition des rentes accordées est utilisé lorsque de code cas spéciaux 08 sont présent dans au
     * moins une des RA de toutes les demandes ouverte de la famille
     *
     * @param demandeCouranteCle
     * @param demandesDeLaFamilleCle
     * @throws Exception
     */
    private void deplacerRenteAccordeesModeA(PersonneAVS requerant, DemandeRenteWrapper demandeCouranteCle,
                                             List<BaseCalculWrapper> basesCalculsDemandeCourante, Set<DemandeRenteWrapper> demandesDeLaFamilleCle,
                                             BSession session) throws Exception {
        // Si des demandes ouverte existent pour la famille. Sinon on ne fait rien
        if (demandesDeLaFamilleCle != null && demandesDeLaFamilleCle.size() > 0) {
            String cleDemandeCourante = demandeCouranteCle.getCleDeComparaison();

            // On va boucler sur toutes les bases de calcul de la demande courante
            for (BaseCalculWrapper bcDemandeCourante : basesCalculsDemandeCourante) {

                String cleBaseCalcul = bcDemandeCourante.getCleDeComparaison();

                // Si la clé de la base de calcul est la même que la demande courante, on ne la déplace pas
                if (!cleDemandeCourante.equals(cleBaseCalcul)) {

                    // On va rechercher dans les demandes ouverte de la famille si une clé correspond
                    for (DemandeRenteWrapper demandeRenteWrapper : demandesDeLaFamilleCle) {

                        if (cleBaseCalcul.equals(demandeRenteWrapper.getCleDeComparaison())) {
                            // les 2 clés sont identiques, on peut déplacer la base de calcul dans la demande
                            deplacerBaseDeCalculDansDemande(session, bcDemandeCourante.getBasesCalcul(),
                                    demandeRenteWrapper.getDemandeRente().getId().toString());
                        }
                    }
                }
            }
        }
    }

    /**
     * Répartis les rentes accordées selon le modèle B.
     * Le modèle B de répartition des rentes accordées est utilisé lorsque des rentes accordées contiennent des CCS 08
     * et d'autres non
     *
     * @param demandeCouranteCle
     * @param demandesDeLaFamilleWrapper
     * @throws Exception
     */
    private String deplacerRenteAccordeesModeB(PersonneAVS requerant, DemandeRenteWrapper demandeCouranteCle,
                                               List<BaseCalculWrapper> basesCalculsDemandeCourante, Set<DemandeRenteWrapper> demandesDeLaFamilleWrapper,
                                               BSession session) throws Exception {

        List<String> messages = new ArrayList<String>();

        /**
         * Ce qui nous intéresse dans un premier temps est de savoir si la demande est ajournée et si la date de
         * révocation de l'ajournement est vide (la méthode isAjournement() nous donne cette info).
         * Si c'est le cas :
         * - On laisse les base de calcul contenant des CCS 08 et on enlève les BCs qui n'en ont pas
         * Si ce n'est pas le cas, on effectue le traitement inverse .
         * - On enlève les base de calcul contenant des CCS 08 et on laisse les BCs qui n'en ont pas
         */
        List<BaseCalculWrapper> baseCalculAvecCCS08ADeplacer = new ArrayList<BaseCalculWrapper>();
        List<BaseCalculWrapper> baseCalculSansCCS08ADeplacer = new ArrayList<BaseCalculWrapper>();
        boolean deplacementBCAvecCCS08 = false;
        boolean deplacementBCSansCCS08 = false;

        // Déplacement des BC SANS CCS 08
        if (demandeCouranteCle.isAjournement()) {
            deplacementBCSansCCS08 = true;
            for (BaseCalculWrapper bc : basesCalculsDemandeCourante) {
                if (!bc.hasCodeCasSpecial08()) {
                    baseCalculSansCCS08ADeplacer.add(bc);
                }
            }
        }
        // Déplacement des BC AVEC CCS 08
        else {
            deplacementBCAvecCCS08 = true;
            for (BaseCalculWrapper bc : basesCalculsDemandeCourante) {
                if (bc.hasCodeCasSpecial08()) {
                    baseCalculAvecCCS08ADeplacer.add(bc);
                }
            }
        }

        // --------------------------------------------------//
        Set<DemandeRente> demandesValideesDeLaFamille = CorvusServiceLocator.getDemandeRenteService()
                .demandesDuRequerantEtDeSaFamille(requerant);

        // On ne prend que les demandes validées
        demandesValideesDeLaFamille = demandesValidees(demandesValideesDeLaFamille);
        // de type standard ou transitoire
        demandesValideesDeLaFamille = demandesStandardsOuTransitoires(demandesValideesDeLaFamille);
        // sans les demandes API
        demandesValideesDeLaFamille = filtrerDemandeAPI(demandesValideesDeLaFamille);

        List<DemandeRenteWrapper> demandesValideesDeLaFamilleWrappers = new LinkedList<DemandeRenteWrapper>();
        for (DemandeRente demandeRente : demandesValideesDeLaFamille) {
            demandesValideesDeLaFamilleWrappers.add(new DemandeRenteWrapper(demandeRente));
        }
        Collections.sort(demandesValideesDeLaFamilleWrappers);

        // --------------------------------------------------//

        /**
         * La il peu y avoir soit un déplacement des bases de calcul avec CCS 08, soit déplacement des bases de calcul
         * sans CS08
         */

        /**
         * 1er cas (ajournement = non)
         * ajournement = false OU date de révocation ajournement n'est pas vide
         * On doit déplacer les bases de calcul avec CCS 08
         */
        List<BaseCalculWrapper> baseCalculADeplacer = new ArrayList<BaseCalculWrapper>();
        if (deplacementBCAvecCCS08 && baseCalculAvecCCS08ADeplacer.size() > 0) {
            baseCalculADeplacer.addAll(baseCalculAvecCCS08ADeplacer);
        }
        /**
         * 2ème cas
         * On doit déplacer les bases de calcul SANS CCS 08
         * Dans ce cas, on vas rechercher
         */
        else if (deplacementBCSansCCS08 && baseCalculSansCCS08ADeplacer.size() > 0) {
            baseCalculADeplacer.addAll(baseCalculSansCCS08ADeplacer);
        }

        if (baseCalculADeplacer.size() > 0) {
            Iterator<BaseCalculWrapper> iterator = baseCalculADeplacer.iterator();

            // On boucle sur toute les bases de calcul a déplacer

            while (iterator.hasNext()) {
                BaseCalculWrapper baseDeCalcul = iterator.next();
                String cleBaseCalcul = baseDeCalcul.getCleDeComparaison();
                boolean deplace = false;

                // On a la base de calcul à déplacer, on va regarder dans les demandes ouverte de la famille s'il est
                // possible de déplacer la BC dans une de ces demandes
                for (DemandeRenteWrapper demande : demandesDeLaFamilleWrapper) {

                    // Si la clé de la BC est égal à la clé de la demande on peut déplacer la BC dans la demande
                    if (cleBaseCalcul.equals(demande.getCleDeComparaison())) {
                        deplacerBaseDeCalculDansDemande(session, baseDeCalcul.getBasesCalcul(),
                                demande.getDemandeRente().getId().toString());
                        deplace = true;
                        break;
                    }
                }

                /*
                 * Si la base de calcul n'a pas pu être déplacée c'est qu'on à pas trouver de demande ouverte du bon
                 * type.
                 * Il faut voir s'il y a possibilité de copier un demande existante
                 */
                if (!deplace) {
                    // On boucle sur les demandes validées de la famille
                    for (DemandeRenteWrapper demandeValidee : demandesValideesDeLaFamilleWrappers) {

                        /*
                         * Si la clé de la BC correspond à la demande, on va copier la demande et déplacer la base de
                         * calcul dans la nouvelle demande
                         */
                        if (cleBaseCalcul.equals(demandeValidee.getCleDeComparaison())) {
                            REDemandeRente copie = deplacerBaseDeCalculDansCopieDeLaDemande(session,
                                    baseDeCalcul.getBasesCalcul(), demandeValidee);

                            /*
                             * On ajoute la nouvelle demande non validée que l'on vient de copier dans la liste des
                             * demande non validées de la famille. Si ce n'est pas fait et qu'il y a plusieurs base de
                             * calcul à déplacer on risque de retrouver des copies à double
                             */
                            DemandeRente demandeDomain = CorvusCrudServiceLocator.getDemandeRenteCrudService()
                                    .read(new Long(copie.getIdDemandeRente()));
                            demandesDeLaFamilleWrapper.add(new DemandeRenteWrapper(demandeDomain));
                            deplace = true;
                            if (deplace) {
                                break;
                            }
                        }
                    }
                    /*
                     * Si on à pas trouvé de demande correspondant à la BC, on va notifier l'utilisateur
                     */
                    if (!deplace) {
                        String userMessage = session
                                .getLabel("IMPORTATION_CALCUL_ACOR_IMPOSSIBLE_DEPLACER_BASE_CALCUL");

                        String typeDemande = session
                                .getCodeLibelle(String.valueOf(baseDeCalcul.getTypeDemandeRente().getCodeSysteme()));

                        String ajournement = " ";
                        if (baseDeCalcul.isAjournement()) {
                            ajournement = " " + session.getLabel("IMPORTATION_CALCUL_ACOR_AJOURNEMENT") + " ";
                        }

                        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session,
                                baseDeCalcul.getBasesCalcul().getIdTiersBaseCalcul());
                        String infoTiers = tiers.getNom() + " " + tiers.getPrenom() + " " + tiers.getNSS();

                        userMessage = userMessage.replace("{0}", typeDemande);
                        userMessage = userMessage.replace("{1}", ajournement);
                        userMessage = userMessage.replace("{2}", infoTiers);
                        messages.add(userMessage);
                    }
                }
            }

        }
        StringBuilder sb = new StringBuilder();
        if (messages.size() > 0) {
            for (String m : messages) {
                sb.append(m).append("</br>");
            }
        }
        return sb.toString();
    }

    /**
     * Créer une copie de la demande et déplace la rente accordées dedans
     *
     * @param session
     * @param baseDeCalcul    La base de calcul à déplacer
     * @param demandeOriginal
     * @return <code>true</code> si le déplacement à pu être réalisé
     * @throws Exception
     * @throws REReglesException
     */
    private REDemandeRente deplacerBaseDeCalculDansCopieDeLaDemande(BSession session, REBasesCalcul
            baseDeCalcul,
                                                                    DemandeRenteWrapper demandeOriginal) throws REReglesException, Exception {

        // Recherche du bon type de demande avant la copie
        TypeDemandeRente type = demandeOriginal.getDemandeRente().getTypeDemandeRente();
        REDemandeRente demande = null;

        switch (type) {
            case DEMANDE_INVALIDITE:
                demande = new REDemandeRenteInvalidite();
                break;
            case DEMANDE_SURVIVANT:
                demande = new REDemandeRenteSurvivant();
                break;
            case DEMANDE_VIEILLESSE:
                demande = new REDemandeRenteVieillesse();
                break;
            case DEMANDE_API:
            default:
                throw new Exception("Impossible de copier la demande. Le type [" + type + "] de la demande avec l'id ["
                        + demandeOriginal.getDemandeRente().getId() + "] est inccorecte ");
        }

        demande.setSession(session);
        demande.setId(demandeOriginal.getDemandeRente().getId().toString());
        demande.retrieve();
        if (demande.isNew()) {
            String message = session.getLabel("IMPORTATION_CALCUL_ACOR_IMPOSSIBLE_RETROUVER_DEMANDE_POUR_COPIE");
            message = message.replace("{0}", demande.getId().toString());
            throw new REBusinessException(message);
        }

        REDemandeRente copieDemande = REDemandeRegles.copierDemandeRente(session, session.getCurrentThreadTransaction(),
                demande);
        if (copieDemande == null) {
            String message = session.getLabel("IMPORTATION_CALCUL_ACOR_IMPOSSIBLE_COPIER_DEMANDE");
            message = message.replace("{0}", demande.getId().toString());
            message = message.replace("{1}", baseDeCalcul.getIdBasesCalcul());
            throw new REBusinessException(message);
        }
        deplacerBaseDeCalculDansDemande(session, baseDeCalcul, copieDemande.getIdDemandeRente());
        return copieDemande;
    }

    /**
     * Déplace la base de calcul dans la demande
     *
     * @param session
     * @param basesCalcul La base de calcul à déplacer
     * @param idDemande
     * @throws Exception
     */
    private void deplacerBaseDeCalculDansDemande(BSession session, REBasesCalcul basesCalcul, String idDemande)
            throws Exception {

        // 1 - Récupération de la demande
        if (JadeStringUtil.isBlankOrZero(idDemande)) {
            String message = session
                    .getLabel("IMPORTATION_CALCUL_ACOR_IMPOSSIBLE_RETROUVER_DEMANDE_POUR_INSERER_BASE_CALCUL");
            message = message.replace("{0}", idDemande);
            message = message.replace("{1}", basesCalcul.getIdBasesCalcul());
            throw new REBusinessException(message);
        }

        REDemandeRente demande = new REDemandeRente();
        demande.setSession(session);
        demande.setId(idDemande);
        demande.retrieve();

        if (demande.isNew()) {
            String message = session
                    .getLabel("IMPORTATION_CALCUL_ACOR_IMPOSSIBLE_RETROUVER_DEMANDE_POUR_INSERER_BASE_CALCUL");
            message = message.replace("{0}", demande.getId().toString());
            message = message.replace("{1}", basesCalcul.getIdBasesCalcul());
            throw new REBusinessException(message);
        }

        // 2 - Est-ce qu'une rente calculée existe pour cette demande
        RERenteCalculee renteCalculee = new RERenteCalculee();
        renteCalculee.setSession(session);

        String idRenteCalculee = demande.getIdRenteCalculee();
        if (!JadeStringUtil.isBlankOrZero(idRenteCalculee)) {
            renteCalculee.setIdRenteCalculee(idRenteCalculee);
            renteCalculee.retrieve();

            if (renteCalculee.isNew()) {
                // Ok, on a un id rente calculée dans la demande mais pas de rente calculée... va créer la rente
                // calculée
                renteCalculee.save();
                demande.setIdRenteCalculee(renteCalculee.getIdRenteCalculee());
            }
        }
        // Pas de rente calculée -> on la créée
        else {
            renteCalculee.save();
            demande.setIdRenteCalculee(renteCalculee.getIdRenteCalculee());
        }
        demande.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
        demande.update();

        basesCalcul.setIdRenteCalculee(renteCalculee.getIdRenteCalculee());
        basesCalcul.update();

    }

    /**
     * Recherche la demande en fonction de l'id <code>idDemandeCourante</code> passé en argument</br>
     * 1 - Boucle sur les demandes afin de trouver la demande recherchée en fonction de son id
     * 2 - Si la demande à été trouvée, elle sera supprimé du Set de demandes <code>demandes</code> Méthode safe si
     * demandes est null
     *
     * @param demandes          Le Set de demandes dans lequel la recherche sera effectué
     * @param idDemandeCourante L'id de la demande à récupérer
     * @return La demande courante si trouvée sinon null.
     */
    private DemandeRente recupererDemandeCourante(Set<DemandeRente> demandes, long idDemandeCourante) {
        DemandeRente demandeCourante = null;
        if (demandes != null) {
            for (DemandeRente demandeRente : demandes) {
                if (idDemandeCourante == demandeRente.getId()) {
                    demandeCourante = demandeRente;
                    break;
                }
            }
        }

        if (demandeCourante != null) {
            demandes.remove(demandeCourante);
        }
        return demandeCourante;
    }

    /**
     * Filtre les demandes de type API. Retourne un Set de DemandeRente sans les rentes API.
     * Méthode safe face à l'argument <code>demandes</code> null.
     *
     * @param demandes Les demandes à filtrer
     * @return Un Set de DemandeRente sans les demandes de type API. <strong>Ne retourne jamais null</strong>
     */
    private Set<DemandeRente> filtrerDemandeAPI(Set<DemandeRente> demandes) {
        Set<DemandeRente> demandesFiltrees = new HashSet<DemandeRente>();
        if (demandes != null && demandes.size() > 0) {
            for (DemandeRente demande : demandes) {
                if (!TypeDemandeRente.DEMANDE_API.equals(demande.getTypeDemandeRente())) {
                    demandesFiltrees.add(demande);
                }
            }
        }
        return demandesFiltrees;
    }

    private void traitementDeplacementBCRAPD(final String idDemandeRente, final String idBaseCalcul,
                                             final BSession session, final BITransaction transaction) throws Exception {

        REDemandeRente newDemande = new REDemandeRente();
        newDemande.setIdDemandeRente(idDemandeRente);
        newDemande.retrieve(transaction);

        RERenteCalculee rc;

        if (JadeStringUtil.isBlankOrZero(newDemande.getIdRenteCalculee())) {
            rc = new RERenteCalculee();
            rc.setSession(session);
            rc.add(transaction);

            if (!mapDemandeReset.containsKey(newDemande.getIdDemandeRente())) {
                mapDemandeReset.put(newDemande.getIdDemandeRente(), rc.getIdRenteCalculee());
            }

        } else {
            rc = new RERenteCalculee();
            rc.setSession(session);
            rc.setIdRenteCalculee(newDemande.getIdRenteCalculee());
            rc.retrieve(transaction);

            if (rc.isNew()) {

                // Avant de le remplacer, on s'assure qu'il n'y a plus aucune
                // base de calcul liée à cet ancien ID !!!
                REBasesCalculManager mgr = new REBasesCalculManager();
                mgr.setSession(session);
                mgr.setForIdRenteCalculee(newDemande.getIdRenteCalculee());
                mgr.find(transaction, 2);
                if (!mgr.isEmpty()) {
                    throw new Exception(session.getLabel("ERREUR_INCOHERANCE_DONNEES") + rc.getIdRenteCalculee() + "/"
                            + newDemande.getIdDemandeRente());
                }

                rc = new RERenteCalculee();
                rc.setSession(session);
                rc.add(transaction);
            }

            if (!mapDemandeReset.containsKey(newDemande.getIdDemandeRente())) {
                // Effacer toutes les bases calcul et cascade de la demande...
                REDeleteCascadeDemandeAPrestationsDues.supprimerBasesCalculCascade_noCommit(session, transaction, rc,
                        IREValidationLevel.VALIDATION_LEVEL_NONE);

                mapDemandeReset.put(newDemande.getIdDemandeRente(), rc.getIdRenteCalculee());
            }

        }

        // Modifier l'idRenteCalculee de la base de calcul de la ra à déplacer
        REBasesCalcul bc = new REBasesCalcul();
        bc.setSession(session);
        bc.setIdBasesCalcul(idBaseCalcul);
        bc.retrieve(transaction);

        bc.setIdRenteCalculee(rc.getIdRenteCalculee());
        bc.update(transaction);

        // Mise à jour de la demande
        newDemande.setIdRenteCalculee(rc.getIdRenteCalculee());
        newDemande.update(transaction);

    }

    /**
     * Mandat InfoRom D0112 : ajout de remarque dans la décision si rente veuf limitée Il s'agit de rechercher une
     * phrase contenue dans le feuille de calcul ACOR Si cette phrase est présente, mise à jour du champ WCBVLI dans
     * PRINFCOM. Le but de la mise à jour de ce champs est; lors de la préparation de la décision, si ce champs est à
     * vrai, des remarques seront automatiquement insérée dans la décision
     *
     * @param session
     * @param caViewbean
     * @param transaction
     * @throws Exception
     */
    private void traiterLesRemarquesParticulieresDeLaFeuilleDeCalculAcor(final BSession session,
                                                                         final BITransaction transaction, final RECalculACORDemandeRenteViewBean caViewbean) throws Exception {
        String feuilleDeCalculAcor = caViewbean.getContenuFeuilleCalculXML();

        boolean isRenteLimitee = false;
        boolean isRenteAvecSupplementPourPersonneVeuve = false;
        boolean isRenteAvecDebutDroit5AnsAvantDepotDemande = false;
        boolean isRenteAvecMontantMinimumMajoreInvalidite = false;
        boolean isRenteReduitePourSurassurance = false;

        // Si la feuille de calcul existe, on va rechercher si des remarques particulière sont présentes
        if (!JadeStringUtil.isEmpty(feuilleDeCalculAcor)) {
            String contenuARechercher = session
                    .getLabel("CALCULER_DECISION_CLE_ACOR_RENTE_LIMITEE_18ANS_PLUS_JEUNE_ENFANT");
            isRenteLimitee = feuilleDeCalculAcor.contains(contenuARechercher);

            contenuARechercher = session.getLabel("CALCULER_DECISION_CLE_ACOR_MONTANT_AVEC_SUPPLEMENT_PERSONNE_VEUVE");
            isRenteAvecSupplementPourPersonneVeuve = feuilleDeCalculAcor.contains(contenuARechercher);

            contenuARechercher = session.getLabel("CALCULER_DECISION_CLE_ACOR_DEBUT_DROIT_5ANS_AVANT_DEPOT_DEMANDE");
            isRenteAvecDebutDroit5AnsAvantDepotDemande = feuilleDeCalculAcor.contains(contenuARechercher);

            contenuARechercher = session.getLabel("CALCULER_DECISION_CLE_ACOR_MONTANT_MINIMUM_MAJORE_INVALIDITE");
            isRenteAvecMontantMinimumMajoreInvalidite = feuilleDeCalculAcor.contains(contenuARechercher);

            contenuARechercher = session.getLabel("CALCULER_DECISION_CLE_ACOR_RENTE_REDUITE_POUR_SURASSURANCE");
            isRenteReduitePourSurassurance = feuilleDeCalculAcor.contains(contenuARechercher);

        }
        // Dans tous les cas on met à jour les champs des infos complémentaires
        miseAJourInfoComplementaire(session, transaction, caViewbean.getIdDemandeRente(), isRenteLimitee,
                isRenteAvecSupplementPourPersonneVeuve, isRenteAvecDebutDroit5AnsAvantDepotDemande,
                isRenteAvecMontantMinimumMajoreInvalidite, isRenteReduitePourSurassurance);
    }
}
