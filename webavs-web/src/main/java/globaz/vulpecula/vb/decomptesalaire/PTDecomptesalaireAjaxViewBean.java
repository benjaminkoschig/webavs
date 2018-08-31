package globaz.vulpecula.vb.decomptesalaire;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.Absence;
import ch.globaz.vulpecula.domain.models.decompte.CodeErreur;
import ch.globaz.vulpecula.domain.models.decompte.CodeErreurDecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteRepository;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteSalaireRepository;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.util.I18NUtil;
import ch.globaz.vulpecula.web.gson.AbsenceGSON;
import ch.globaz.vulpecula.web.gson.DecompteSalaireGSON;
import com.google.common.base.Preconditions;

/**
 * ViewBean Ajax pour le décompte salaire
 * 
 * @since Web@BMS 0.01.02
 */
public class PTDecomptesalaireAjaxViewBean extends BJadePersistentObjectViewBean implements FWAJAXViewBeanInterface {
    private static final long serialVersionUID = 3659584992167434383L;

    public static final String SEQUENCE_PREVIOUS = "PREVIOUS";
    public static final String SEQUENCE_NEXT = "NEXT";

    private transient DecompteSalaire decompteSalaire;
    private DecompteSalaireGSON decompteSalaireGSON;
    private String idDecompte = "";
    private String sequence = "";
    private String navigation = "";
    private String idPosteTravail;
    private String descriptionPosteTravail = "";
    private String idDecompteSalaire = "";
    private boolean isPasserSuivant = false;
    private boolean isDeleted = false;
    private boolean cotisationMustReload = false;

    private transient final DecompteSalaireRepository decompteSalaireRepository = VulpeculaRepositoryLocator
            .getDecompteSalaireRepository();
    private transient final DecompteRepository decompteRepository = VulpeculaRepositoryLocator.getDecompteRepository();

    public PTDecomptesalaireAjaxViewBean() {
        decompteSalaire = new DecompteSalaire();
        decompteSalaireGSON = new DecompteSalaireGSON();
    }

    @Override
    public void add() throws ViewException {
        Preconditions.checkArgument(!JadeStringUtil.isEmpty(idPosteTravail));

        PosteTravail posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);

        Decompte decompte = decompteRepository.findById(idDecompte);
        List<DecompteSalaire> liste = decompteSalaireRepository.findByIdDecompte(idDecompte);

        int sequenceMax = 0;
        for (DecompteSalaire decompteSalaire : liste) {
            int currentSeq = Integer.parseInt(decompteSalaire.getSequence());
            if (sequenceMax < currentSeq) {
                sequenceMax = currentSeq;
            }
        }
        decompteSalaireGSON.setSequence(String.valueOf(++sequenceMax));
        decompteSalaireGSON.setTauxContribuable(posteTravail.getTauxContribuable().getValue());

        decompteSalaire.setDecompte(decompte);
        decompteSalaire.setPosteTravail(posteTravail);
        convertGson2Model(decompteSalaireGSON, decompteSalaire);
        decompteSalaire.setaTraiter(false);

        VulpeculaServiceLocator.getDecompteService().ajoutCotisationsPourPoste(decompteSalaire);
        try {
            decompteSalaire = VulpeculaServiceLocator.getDecompteSalaireService()
                    .create(decompteSalaire, getMasseAC2());
        } catch (UnsatisfiedSpecificationException e) {
            throw new ViewException(e);
        }
        // On ajoute les cotisations

        decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findById(decompteSalaire.getId());
        descriptionPosteTravail = decompteSalaire.getPosteTravail().getDescriptionTravailleur();

        decompteSalaireGSON.setIdPosteTravail(decompteSalaire.getIdPosteTravail());
        decompteSalaireGSON.setIdDecompteSalaire(decompteSalaire.getId());
        decompteSalaireGSON.setFirst(VulpeculaRepositoryLocator.getDecompteSalaireRepository().isFirstDecompteSalaire(
                getIdDecompte(), decompteSalaire.getSequence()));
        decompteSalaireGSON.setLast(VulpeculaRepositoryLocator.getDecompteSalaireRepository().isLastDecompteSalaire(
                getIdDecompte(), decompteSalaire.getSequence()));
        decompteSalaireGSON.setSpy(computeSpy(decompteSalaire.getSpy()));
        decompteSalaireGSON.setMasseAC2(decompteSalaire.getMasseAC2().getValue());
        // decompteSalaireGSON.setTauxContribuable(decompteSalaire.getTauxContribuableForCaissesSocialesAsValue(false));
        decompteSalaireGSON.setTauxSaisieEbu(decompteSalaire.getTauxSaisieEbu());
        if (decompteSalaire.getTravailleur().getIdTiers() == null
                || decompteSalaire.getTravailleur().getIdTiers().isEmpty()) {
            decompteSalaireGSON.setIdTiersTravailleur(null);
        } else {
            decompteSalaireGSON.setIdTiersTravailleur(decompteSalaire.getPosteTravail().getTravailleurIdTiers());
        }

    }

    @Override
    public void retrieve() throws Exception {
        if (SEQUENCE_NEXT.equals(navigation)) {
            decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                    .findNextByIdDecompteAndSequence(getIdDecompte(), sequence);
            decompteSalaire.setTauxAfficheErreur(decompteSalaire.getTauxContribuableAfficheAsValue());
        } else if (SEQUENCE_PREVIOUS.equals(navigation)) {
            decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                    .findPreviousByIdDecompteAndSequence(getIdDecompte(), sequence);
            decompteSalaire.setTauxAfficheErreur(decompteSalaire.getTauxContribuableAfficheAsValue());
        } else {
            decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findById(idDecompteSalaire);
            decompteSalaire.setTauxAfficheErreur(decompteSalaire.getTauxContribuableAfficheAsValue());
        }

        loadInfosComplementaires();
    }

    @Override
    public void update() throws JadePersistenceException {
        if (!isDeleted) {
            decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findById(
                    decompteSalaireGSON.getIdDecompteSalaire());
            decompteSalaire = convertGson2Model(decompteSalaireGSON, decompteSalaire);

            boolean isNewTravailleur = decompteSalaire.isNewTravailleur();
            if (isNewTravailleur) {
                Validate.notEmpty(idPosteTravail);
                decompteSalaire.setPosteTravail(VulpeculaRepositoryLocator.getPosteTravailRepository().findById(
                        idPosteTravail));
            }

            if (cotisationMustReload || isNewTravailleur) {
                VulpeculaRepositoryLocator.getDecompteSalaireRepository().deleteCotisationsDecompte(decompteSalaire);
                decompteSalaire.getCotisationsDecompte().clear();
                VulpeculaServiceLocator.getDecompteService().ajoutCotisationsPourPoste(decompteSalaire);
            }

            try {
                VulpeculaServiceLocator.getDecompteSalaireService().update(decompteSalaire, getMasseAC2());
            } catch (UnsatisfiedSpecificationException e) {
                throw new ViewException(e);
            }
        } else {
            isDeleted = false;
        }

        if (isPasserSuivant) {
            if (decompteSalaire != null && decompteSalaire.getDecompte().isEBusiness()) {
                decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                        .findNextAQuittancerByIdDecompteAndSequence(getIdDecompte(), sequence);
                decompteSalaire.setTauxAfficheErreur(decompteSalaire.getTauxContribuableAfficheAsValue());
            } else {
                decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                        .findNextByIdDecompteAndSequence(getIdDecompte(), sequence);
                decompteSalaire.setTauxAfficheErreur(decompteSalaire.getTauxContribuableAfficheAsValue());
            }
            loadInfosComplementaires();
        }
    }

    private void loadInfosComplementaires() {
        // Conversion decompteSalaire en decompteSalaireGSON
        decompteSalaireGSON = convertModel2Gson(decompteSalaire);
        decompteSalaireGSON.setFirst(VulpeculaRepositoryLocator.getDecompteSalaireRepository().isFirstDecompteSalaire(
                getIdDecompte(), decompteSalaire.getSequence()));
        if (decompteSalaire.getDecompte().isEBusiness()) {
            decompteSalaireGSON.setLast(VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                    .isLastDecompteSalaireAQuittancer(getIdDecompte(), decompteSalaire.getSequence()));
        } else {
            decompteSalaireGSON.setLast(VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                    .isLastDecompteSalaire(getIdDecompte(), decompteSalaire.getSequence()));
        }
        if (decompteSalaire.getPosteTravail().getId().isEmpty()) {
            idPosteTravail = null;
        } else {
            idPosteTravail = String.valueOf(decompteSalaire.getPosteTravail().getId());
        }
        decompteSalaireGSON.setTauxAAfficher(decompteSalaire.getTauxAfficheErreur());
        descriptionPosteTravail = decompteSalaire.getPosteTravail().getDescriptionTravailleur();
        sequence = decompteSalaire.getSequence();
        decompteSalaireGSON.setEnErreur(getIsEnErreur(decompteSalaire));
    }

    private DecompteSalaireGSON convertModel2Gson(final DecompteSalaire decompteSalaire) {
        decompteSalaireGSON.setIdPosteTravail(decompteSalaire.getIdPosteTravail());
        decompteSalaireGSON.setIdDecompteSalaire(decompteSalaire.getId());
        decompteSalaireGSON.setHeures(Double.toString(decompteSalaire.getHeures()));
        decompteSalaireGSON.setSalaireHoraire(decompteSalaire.getSalaireHoraire().toStringFormat());
        decompteSalaireGSON.setSalaireTotal(decompteSalaire.getSalaireTotal().toStringFormat());

        decompteSalaireGSON.setTauxContribuable(decompteSalaire.getTauxContribuableForCaissesSocialesAsValue(false));

        // decompteSalaireGSON.setTauxAAfficher(decompteSalaire.getTauxAfficheErreur());
        decompteSalaireGSON.setTauxAAfficher(decompteSalaire.getTauxContribuableAfficheAsValue());
        // decompteSalaireGSON.setTauxSaisieEbu(decompteSalaire.getTauxSaisieEbu());
        decompteSalaireGSON.setTauxSaisieEbu(decompteSalaire.getTauxSaisieEbu());

        decompteSalaireGSON.setSequence(decompteSalaire.getSequence());
        decompteSalaireGSON.setAbsences(decompteSalaire.getAbsences());
        decompteSalaireGSON.setMensuel(decompteSalaire.isMensuel());
        decompteSalaireGSON.setSpy(computeSpy(decompteSalaire.getSpy()));
        decompteSalaireGSON.setPeriodeDebut(decompteSalaire.getPeriode().getDateDebutAsSwissValue());
        decompteSalaireGSON.setForcerFranchise0(decompteSalaire.isForcerFranchise0());
        if (decompteSalaire.getTravailleur().getIdTiers() == null
                || decompteSalaire.getTravailleur().getIdTiers().isEmpty()) {
            decompteSalaireGSON.setIdTiersTravailleur(null);
        } else {
            decompteSalaireGSON.setIdTiersTravailleur(decompteSalaire.getTravailleur().getIdTiers());
        }
        decompteSalaireGSON.setPeriodeFin(decompteSalaire.getPeriode().getDateFinAsSwissValue());
        decompteSalaireGSON.setMasseFranchise(decompteSalaire.getMontantFranchise().getValue());
        decompteSalaireGSON.setRemarque(decompteSalaire.getRemarque());
        decompteSalaireGSON.setCodeErreur(decompteSalaire.getListeCodeErreur());
        decompteSalaireGSON.setQuittancer(!decompteSalaire.isaTraiter());
        decompteSalaireGSON.setLigneSupprimee(false);
        decompteSalaireGSON.setMajFinPoste(false);
        for (CodeErreurDecompteSalaire code : decompteSalaire.getListeCodeErreur()) {
            if (code.getCodeErreur().equals(CodeErreur.LIGNE_SUPPRIMEE)) {
                decompteSalaireGSON.setLigneSupprimee(true);
            }
        }

        CotisationDecompte cotisationAC2 = decompteSalaire.getCotisationAC2();
        if (cotisationAC2 != null) {
            if (cotisationAC2.getMasseForcee()) {
                decompteSalaireGSON.setMasseAC2(cotisationAC2.getMasse().getValue());
            } else {
                decompteSalaireGSON.setMasseAC2("0.00");
            }
        } else {
            decompteSalaireGSON.setMasseAC2("0.00");
        }

        decompteSalaireGSON.setCorrelationId(decompteSalaire.getCorrelationId());
        decompteSalaireGSON.setNouveauTravailleur(decompteSalaire.isNewTravailleur());
        if (decompteSalaire.getAnneeCotisations() != null) {
            decompteSalaireGSON.setAnneeCotisations(decompteSalaire.getAnneeCotisations().getValue());
        }

        if (decompteSalaire.getStatus() != null) {
            decompteSalaireGSON.setStatus(decompteSalaire.getStatus());
        }

        if (!JadeStringUtil.isBlankOrZero(decompteSalaire.getVacancesFeriesAsValue())) {
            decompteSalaireGSON.setVacances(decompteSalaire.getVacancesFeriesAsValue());
        } else {
            decompteSalaireGSON.setVacances("0.00");
        }
        if (!JadeStringUtil.isBlankOrZero(decompteSalaire.getGratificationsAsValue())) {
            decompteSalaireGSON.setGratifications(decompteSalaire.getGratificationsAsValue());
        } else {
            decompteSalaireGSON.setGratifications("0.00");
        }
        if (!JadeStringUtil.isBlankOrZero(decompteSalaire.getAbsencesJustifieesAsValue())) {
            decompteSalaireGSON.setAbsencesJustifiees(decompteSalaire.getAbsencesJustifieesAsValue());
        } else {
            decompteSalaireGSON.setAbsencesJustifiees("0.00");
        }
        if (!JadeStringUtil.isBlankOrZero(decompteSalaire.getApgComplementaireSMAsValue())) {
            decompteSalaireGSON.setApgComplSm(decompteSalaire.getApgComplementaireSMAsValue());
        } else {
            decompteSalaireGSON.setApgComplSm("0.00");
        }

        decompteSalaireGSON.setEnErreur(getIsEnErreur(decompteSalaire));

        return decompteSalaireGSON;
    }

    /**
     * @param decompteSalaireGSON
     * @param decompteSalaire2
     * @return
     */
    private DecompteSalaire convertGson2Model(final DecompteSalaireGSON decompteSalaireGSON,
            final DecompteSalaire decompteSalaire2) {
        if (JadeStringUtil.isEmpty(decompteSalaireGSON.getPeriodeFin())) {
            throw new IllegalStateException(SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                    SpecificationMessage.DECOMPTE_SALAIRE_DATE_FIN_OBLIGATOIRE));
        }

        /*
         * Contrôle que la période de la ligne de salaire se trouve bien sans la période du décompte.
         */
        Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findById(idDecompte);
        if (!checkPeriodesValidity(decompte, decompteSalaireGSON)) {
            throw new IllegalStateException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "ERROR_VALIDATION_PERIODE_DECOMPTE_SALAIRE"));
        }
        decompteSalaire2.setHeures(unformatNumber(decompteSalaireGSON.getHeures()));
        decompteSalaire2.setSalaireHoraire(new Montant(decompteSalaireGSON.getSalaireHoraire()));
        decompteSalaire2.setSalaireTotal(new Montant(decompteSalaireGSON.getSalaireTotal()));
        decompteSalaire2.setPeriode(new Periode(decompteSalaireGSON.getPeriodeDebut(), decompteSalaireGSON
                .getPeriodeFin()));
        decompteSalaire2.setSequence(decompteSalaireGSON.getSequence());
        decompteSalaire2.setMontantFranchise(new Montant(decompteSalaireGSON.getMasseFranchise()));
        decompteSalaire2.setTauxAfficheErreur(decompteSalaireGSON.getTauxAAfficher());
        decompteSalaire2.setTauxForEbu(new Taux(decompteSalaireGSON.getTauxContribuable()));
        decompteSalaire2.setTauxSaisieEbu(decompteSalaireGSON.getTauxSaisieEbu());
        decompteSalaire2.setForcerFranchise0(decompteSalaireGSON.isForcerFranchise0());

        List<Absence> absences = new ArrayList<Absence>();
        for (AbsenceGSON absenceGSON : decompteSalaireGSON.getAbsencesGSON()) {
            Absence absence = absenceGSON.convertToDomain(decompteSalaire2);
            absences.add(absence);
        }

        decompteSalaire2.setAbsences(absences);
        decompteSalaire2.setaTraiter(!decompteSalaireGSON.isQuittancer());
        // On ne traite plus la maj du poste via les décomptes
        // decompteSalaire2.setMajFinPoste(decompteSalaireGSON.isMajFinPoste());
        decompteSalaire2.setMajFinPoste(false);
        if (decompteSalaireGSON.getAnneeCotisations() > 0) {
            decompteSalaire2.setAnneeCotisations(new Annee(decompteSalaireGSON.getAnneeCotisations()));
        }

        if (decompteSalaireGSON.getStatus() != null) {
            decompteSalaire2.setStatus(decompteSalaireGSON.getStatus());
        }

        return decompteSalaire2;
    }

    /**
     * Permet de contrôler que la période du salaire ne se trouve pas en dehors de la période du décompte.
     * 
     * @param decompte Décompte à tenir compte pour la période.
     * @param decompteSalaire Salaire à tenir compte pour la comparaison avec la période du décompte.
     * @return Vrai si la période du salaire ne se trouve pas en dehors de la période du décompte.
     */
    private Boolean checkPeriodesValidity(Decompte decompte, DecompteSalaireGSON decompteSalaire) {

        /*
         * Formattage correct des périodes pour le contrôle
         */
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM.yyyy");
        Integer anneeDebutDecompte = formatter.parseDateTime(decompte.getPeriodeDebutAsSwissValue()).getYear();
        Integer anneeFinDecompte = formatter.parseDateTime(decompte.getPeriodeFinAsSwissValue()).getYear();

        formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
        Integer anneeDebutDecompteSalaire = formatter.parseDateTime(decompteSalaire.getPeriodeDebut()).getYear();
        Integer anneeFinDecompteSalaire = formatter.parseDateTime(decompteSalaire.getPeriodeFin()).getYear();

        /*
         * Contrôle que la période de la ligne de salaire se trouve bien à l'intérieur de la période de décompte.
         */

        if (anneeDebutDecompteSalaire < anneeDebutDecompte || anneeFinDecompteSalaire > anneeFinDecompte) {
            return false;
        }

        return true;
    }

    /**
     * Retourne la masseAC2 présent dans la requête GSON.
     * Si null ou vide, on retourne zéro.
     * 
     * @return MasseAC2 ou zéro
     */
    private Montant getMasseAC2() {
        Montant masseAC2 = Montant.ZERO;
        String ac2 = decompteSalaireGSON.getMasseAC2();
        if (!JadeStringUtil.isEmpty(ac2)) {
            masseAC2 = new Montant(ac2);
        }
        return masseAC2;
    }

    private Montant getFranchise() {
        Montant montantFranchise = Montant.ZERO;
        String franchise = decompteSalaireGSON.getMasseFranchise();
        if (!JadeStringUtil.isEmpty(franchise)) {
            montantFranchise = new Montant(franchise);
        }
        return montantFranchise;
    }

    /**
     * Format le spy pour l'écran
     * 
     * @return spy formaté
     */
    private String computeSpy(final String spy) {
        String formatSpy = "";
        BSpy bspy = new BSpy(spy);

        formatSpy = bspy.getDate() + ", " + bspy.getTime() + " - " + bspy.getUser();

        return formatSpy;
    }

    @Override
    public void delete() throws Exception {
        isDeleted = true;
        VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                .deleteById(decompteSalaireGSON.getIdDecompteSalaire());
    }

    /**
     * @return the decompteSalaire
     */
    public DecompteSalaire getDecompteSalaire() {
        return decompteSalaire;
    }

    /**
     * @return the decompteSalaireGSON
     */
    public DecompteSalaireGSON getDecompteSalaireGSON() {
        return decompteSalaireGSON;
    }

    /**
     * @return the descriptionPosteTravail
     */
    public String getDescriptionPosteTravail() {
        return descriptionPosteTravail;
    }

    @Override
    public String getId() {
        return String.valueOf(decompteSalaire.getId());
    }

    /**
     * @return the idDecompte
     */
    public String getIdDecompte() {
        return idDecompte;
    }

    /**
     * @return the idPosteTravail
     */
    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return null;
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(decompteSalaire.getSpy());
    }

    /**
     * @return the cotisationMustReload
     */
    public boolean isCotisationMustReload() {
        return cotisationMustReload;
    }

    /**
     * @param cotisationMustReload the cotisationMustReload to set
     */
    public void setCotisationMustReload(boolean cotisationMustReload) {
        this.cotisationMustReload = cotisationMustReload;
    }

    @Override
    public boolean hasList() {
        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    /**
     * @param decompteSalaire
     *            the decompteSalaire to set
     */
    public void setDecompteSalaire(final DecompteSalaire decompteSalaire) {
        this.decompteSalaire = decompteSalaire;
    }

    /**
     * @param decompteSalaireGSON
     *            the decompteSalaireGSON to set
     */
    public void setDecompteSalaireGSON(final DecompteSalaireGSON decompteSalaireGSON) {
        this.decompteSalaireGSON = decompteSalaireGSON;
    }

    @Override
    public void setGetListe(final boolean getListe) {
    }

    @Override
    public void setId(final String newId) {
        decompteSalaire.setId(newId);
    }

    /**
     * @param idDecompte
     *            the idDecompte to set
     */
    public void setIdDecompte(final String idDecompte) {
        this.idDecompte = idDecompte;
    }

    /**
     * @param idPosteTravail
     *            the idPosteTravail to set
     */
    public void setIdPosteTravail(final String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    @Override
    public void setListViewBean(final FWViewBeanInterface fwViewBeanInterface) {
    }

    /**
     * @param navigation the navigation to set
     */
    public void setNavigation(final String navigation) {
        this.navigation = navigation;
    }

    public void setSequence(final String sequence) {
        this.sequence = sequence;
    }

    public void setIdDecompteSalaire(final String idDecompteSalaire) {
        this.idDecompteSalaire = idDecompteSalaire;
    }

    /**
     * Supprime le formattage des milliers (dequote)
     * 
     * @param formatedNumber : nombre formatté
     * @return float sans caractère de formattage
     */
    private double unformatNumber(final String formatedNumber) {
        return Double.valueOf(formatedNumber.replace("'", ""));
    }

    public void setAbsencesGSON(final List<AbsenceGSON> absencesGSON) {
        decompteSalaireGSON.setAbsencesGSON(absencesGSON);
    }

    public void setIsPasserSuivant(final boolean isPasserSuivant) {
        this.isPasserSuivant = isPasserSuivant;
    }

    /**
     * Retourne true si le décompte salaire peut être édité.
     * 
     * @return true si {@link EtatDecompte#OUVERT} ou {@link EtatDecompte#GENERE}
     */
    public boolean isEditable() {
        if (decompteSalaire == null || decompteSalaire.getDecompte() == null) {
            return false;
        }
        return decompteSalaire.getDecompte().isEditable();
    }

    public boolean getIsEnErreur(DecompteSalaire decompteSalaire) {
        return !decompteSalaire.getPeriodeDebut().getAnnee()
                .equals(decompteSalaire.getDecompte().getPeriodeDebut().getAnnee());
    }
}
