package globaz.orion.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.parametreAssurance.AFParametreAssurance;
import globaz.naos.db.parametreAssurance.AFParametreAssuranceManager;
import globaz.naos.db.releve.AFApercuReleve;
import globaz.naos.db.releve.AFApercuReleveLineFacturation;
import globaz.naos.db.releve.AFApercuReleveManager;
import globaz.naos.translation.CodeSystem;
import globaz.orion.utils.EBSddUtils;
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.db.comptes.CACompteurManager;
import globaz.osiris.db.services.controleemployeur.CACompteAnnexeService;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.ICommonConstantes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Periode;
import ch.globaz.orion.business.models.sdd.RecapSaisieDecompte;
import ch.globaz.orion.business.models.sdd.RecapSaisieDecompteBuilder;
import ch.globaz.orion.businessimpl.services.sdd.SddServiceImpl;
import ch.globaz.xmlns.eb.sdd.DecompteAndLignes;
import ch.globaz.xmlns.eb.sdd.DecompteStatutEnum;
import ch.globaz.xmlns.eb.sdd.DecompteTypeEnum;

public class EBTreatSaisieDecompte extends BProcess {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final long serialVersionUID = 1L;

    private Map<String, String> libelleTypeRevle;
    private List<RecapSaisieDecompte> listeRecapSaisieDecompte;

    public EBTreatSaisieDecompte() {
        super();
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        boolean status = true;

        try {
            List<DecompteAndLignes> listDecomptes = SddServiceImpl.listSddSaisies(getSession());

            // Pas de saisie, on fait rien
            if (listDecomptes.isEmpty()) {
                getMemoryLog().logMessage(getSession().getLabel("PROCESS_SDD_AUCUNE_SDD"), FWMessage.INFORMATION,
                        this.getClass().getName());
                return true;
            }

            // Parcours des saisies des décomptes
            // Lecture des cotisations à mettre à jour venant de eBusiness
            for (int i = 0; (i < listDecomptes.size()) && !isAborted(); i++) {

                DecompteAndLignes decompte = listDecomptes.get(i);

                // 1 - recherche de l'affiliation
                // ----------------------------------------
                AFAffiliation affiliation = AFAffiliationUtil.getAffiliation(
                        Integer.toString(decompte.getDecompte().getIdAffiliationWebavs()), getSession());

                // 2 - Différent check pour valider que l'on peut bien créer le décompte
                // ----------------------------------------
                if (!hasAllPreConditionsSatisfied(affiliation, decompte)) {
                    SddServiceImpl.changeStatus(decompte.getDecompte().getIdDecompte(), DecompteStatutEnum.ERREUR,
                            getSession());
                    continue;
                }

                // 3 - Traite le décompte
                // ----------------------------------------
                traiteDecompte(decompte, affiliation);

            }

            // Impression de la liste excel récapitulative
            listeExcelSaisieDecompte();

        } catch (Exception e) {

            LOGGER.error("", e);

            this._addError(getTransaction(), getSession().getLabel("PROCESS_SDD_EXCEPTION_OCCURED"));
            CEUtils.addMailInformationsError(getMemoryLog(), CEUtils.stack2string(e), this.getClass().getName());

            status = false;
        }

        return status;
    }

    private void traiteDecompte(DecompteAndLignes decompte, AFAffiliation affiliation) throws Exception {

        try {
            // Création du relevé
            // ----------------------------------------
            createReleve(decompte, affiliation);

            // Mise a jour du status du decompte
            // ----------------------------------------
            SddServiceImpl.changeStatus(decompte.getDecompte().getIdDecompte(), DecompteStatutEnum.TERMINE,
                    getSession());

            // Commit or Rollback
            // ----------------------------------------
            if (getTransaction().hasErrors()) {
                getTransaction().rollback();
                getTransaction().clearErrorBuffer();
            } else {
                getTransaction().commit();
            }

        } catch (Exception e) {
            addInfosTraitement(affiliation, decompte, determineTypeReleve(decompte),
                    getSession().getLabel("PROCESS_SDD_EXCEPTION_RELEVE") + " : " + e.getMessage());
            getTransaction().rollback();
            getTransaction().clearErrorBuffer();
        }
    }

    /**
     * Valide toutes les conditions pour réaliser le relevé
     * 
     * @param affiliation Une affiliation
     * @param decompte Un décompte provenant de eBusiness
     * @return
     * @throws Exception
     */
    private boolean hasAllPreConditionsSatisfied(AFAffiliation affiliation, DecompteAndLignes decompte)
            throws Exception {
        // Check si periode d'affiliation valide
        if (hasPeriodeAffiliationNonValideForReleve(affiliation, decompte)) {
            return false;
        }

        // Check si on peut bien créer ce type de décompte
        if (hasNonCoherenceWithTypeDecompte(affiliation, decompte)) {
            return false;
        }

        return true;
    }

    private void createReleve(DecompteAndLignes decompte, AFAffiliation affiliation) throws Exception {
        AFApercuReleve releve = new AFApercuReleve();
        releve.setSession(getSession());
        releve.setAffilieNumero(affiliation.getAffilieNumero());
        releve.setIdTiers(affiliation.getIdTiers());

        // Set le type de relevé
        releve.setType(determineTypeReleve(decompte));

        // Fixe les dates
        releve.setDateDebut(resolveDateDebut(decompte));
        releve.setDateFin(resolveDateFin(decompte));
        releve.setDateReception(resolveDateDeSaisie(decompte));

        // Set la méthode des intérets
        releve.setInterets(CodeSystem.INTERET_MORATOIRE_AUTOMATIQUE);

        // Set l'état du relevé
        releve.setNewEtat(CodeSystem.ETATS_RELEVE_SAISIE);
        releve.setEtat(CodeSystem.ETATS_RELEVE_SAISIE);

        releve.retrieveIdPassage();

        // Génération de la liste des cotisations
        releve.generationCotisationList();

        // Mise à jour des masses suivant le SDD
        EBSddUtils.computeDataForReleve(decompte.getLignesDeDecompte(), releve);

        releve.setWantControleCotisation(false);

        // TRaite la problèmatique de la FFPP masse salariale.
        // La FFPP capitation est gérée par la génération de la liste des cotisations.
        traiteFFPP(releve);

        // Calcul des cotisations
        releve.calculeCotisation();

        // Ajout du relevé
        releve.add(getTransaction());

        addInfosTraitement(affiliation, decompte, determineTypeReleve(decompte), "");
    }

    /**
     * Traite la problèmatique de la cotisation FFPP masse salariale annuelle
     * 
     * @param releve
     * @throws Exception
     */
    private void traiteFFPP(AFApercuReleve releve) throws Exception {

        // Si on est en présence d'un relevé complémentaire
        if (CodeSystem.TYPE_RELEVE_COMPLEMENT.equals(releve.getType())) {

            // On s'assure que l'on recalcul par la FFPP capitation
            for (AFApercuReleveLineFacturation lineFacturation : releve.getCotisationList()) {
                // Assurance de type FFPP (Capitation)
                if (CodeSystem.TYPE_ASS_FFPP.equals(lineFacturation.getTypeAssurance())) {
                    lineFacturation.setMontantCalculer(0);
                }
            }

            return;
        }

        for (AFApercuReleveLineFacturation lineFacturation : releve.getCotisationList()) {
            // Assurance de type FFPP (Masse salariale) && de périodicité annuel
            if (CodeSystem.TYPE_ASS_FFPP_MASSE.equals(lineFacturation.getTypeAssurance())
                    && !CodeSystem.PERIODICITE_MENSUELLE.equals(lineFacturation.getPeriodiciteCoti())) {
                traiteFFPPMasseSalariale(releve, lineFacturation);
            }
        }
    }

    private void traiteFFPPMasseSalariale(AFApercuReleve releve, AFApercuReleveLineFacturation lineFacturation)
            throws Exception {
        String masseNMoins1 = "";

        try {

            String idRubriqueAssuranceAf = null;
            // REcherche cotisation AF du même canton que la FFPP
            for (AFApercuReleveLineFacturation line : releve.getCotisationList()) {
                if (CodeSystem.TYPE_ASS_COTISATION_AF.equals(line.getTypeAssurance())
                        && lineFacturation.getAssuranceCanton().equals(line.getAssuranceCanton())) {
                    idRubriqueAssuranceAf = line.getAssuranceRubriqueId();
                }
            }

            // Aucune rubrique trouvé, on ne fait rien
            if (idRubriqueAssuranceAf == null) {
                return;
            }

            int anneeCompteurMoins1 = Integer.parseInt(releve.getDateDebut().substring(6)) - 1;

            // Recherche à N-1 la valeur du compteur AF
            CACompteurManager manager = new CACompteurManager();
            manager.setSession(getSession());
            manager.setForAnnee(Integer.toString(anneeCompteurMoins1));
            manager.setForIdCompteAnnexe(retreaveIdCompteAnnexe(getSession(), releve.getAffilieNumero()));
            manager.setForIdRubrique(idRubriqueAssuranceAf);

            manager.find(BManager.SIZE_USEDEFAULT);

            if (!manager.isEmpty()) {
                CACompteur compteur = (CACompteur) manager.getFirstEntity();
                masseNMoins1 = compteur.getCumulMasse();
            }

        } catch (Exception e) {
            JadeLogger.error(this, "Impossible de récupérer les compteurs AF pour " + releve.getAffilieNumero() + " : "
                    + e.toString());
            throw e;
        }

        // Gestion masse minimum
        Double masseMinimumParametre = findMasseMinimum(lineFacturation.getAssuranceId(), releve.getDateDebut());
        if (masseMinimumParametre != null) {

            double masseTaxe = Double.parseDouble(masseNMoins1);
            if (masseTaxe > masseMinimumParametre.doubleValue()) {
                // Affectation de la masse dans le compteur AF
                lineFacturation.setMasse(masseNMoins1);
            }

        } else {
            // Affectation de la masse dans le compteur AF
            lineFacturation.setMasse(masseNMoins1);
        }
    }

    /**
     * Recherche de la masse minimum pour taxer la FFPP
     * 
     * @param idAssurance
     * @param date
     * @return
     * @throws Exception
     */
    private Double findMasseMinimum(String idAssurance, String date) throws Exception {
        AFParametreAssuranceManager paramManager = new AFParametreAssuranceManager();
        paramManager.setSession(getSession());
        paramManager.setForIdAssurance(idAssurance);
        paramManager.setForGenre(CodeSystem.GEN_PARAM_ASS_MASSE_MINIMUM);
        paramManager.setForDate(date);
        paramManager.find(BManager.SIZE_USEDEFAULT);

        Double masseMinimum = null;
        if (!paramManager.isEmpty()) {
            AFParametreAssurance param = (AFParametreAssurance) paramManager.getFirstEntity();

            try {
                masseMinimum = Double.parseDouble(JANumberFormatter.deQuote(param.getValeur()));
            } catch (NumberFormatException e) {
                JadeLogger.error(this, "Propriété Masse minimum incorrect : " + param.getValeurNum());
            }

        }

        return masseMinimum;
    }

    /**
     * Récupération de l'id Compte annexe de l'affilié
     * 
     * @param session
     * @param numeroAffilie
     * @return
     * @throws Exception
     */
    private String retreaveIdCompteAnnexe(BSession session, String numeroAffilie) throws Exception {

        String roleForAffilie = CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(session.getApplication());
        if (JadeStringUtil.isEmpty(roleForAffilie)) {
            throw new Exception("unable to load propertie roleForAffilie");
        }

        String idCompteAnnexe;
        try {
            idCompteAnnexe = CACompteAnnexeService.getIdCompteAnnexeByRole(getSession(), roleForAffilie, numeroAffilie);
        } catch (Exception e) {
            idCompteAnnexe = null;
        }

        return idCompteAnnexe;
    }

    static String resolveDateDebut(DecompteAndLignes decompte) {
        return resolveDateFromXmlGregorianCalendar(decompte.getDecompte().getMoisDecompte()).getFirstDayOfMonth()
                .getSwissValue();
    }

    static String resolveDateFin(DecompteAndLignes decompte) {
        return resolveDateFromXmlGregorianCalendar(decompte.getDecompte().getMoisDecompte()).getLastDayOfMonth()
                .getSwissValue();
    }

    private boolean hasNonCoherenceWithTypeDecompte(AFAffiliation affiliation, DecompteAndLignes decompte)
            throws Exception {

        boolean hasNonCoherence = false;

        AFApercuReleveManager manager = new AFApercuReleveManager();
        manager.setSession(getSession());
        manager.setForAffilieNumero(affiliation.getAffilieNumero());
        manager.setForDateDebut(resolveDateDebut(decompte));
        manager.find(BManager.SIZE_USEDEFAULT);

        // Si manager vide
        if (manager.isEmpty()) {

            // Si on est en présence d'un décompte complémentaire et qu'il n'y a pas de décompte périodique
            // On ajout une remarque dans les infos de traitements
            if (DecompteTypeEnum.COMPLEMENTAIRE == decompte.getDecompte().getType()) {
                addInfosTraitement(affiliation, decompte, determineTypeReleve(decompte),
                        getSession().getLabel("PROCESS_SDD_TYPE_COMP_ET_AUCUNE_PERIODIQUE"));
                hasNonCoherence = true;
            }

            // Pas de relevé présent, on peut donc créer le relevé.
        } else if (DecompteTypeEnum.PERIODIQUE == decompte.getDecompte().getType()) {

            addInfosTraitement(affiliation, decompte, determineTypeReleve(decompte),
                    getSession().getLabel("PROCESS_SDD_RELEVE_PERIODIQUE_DEJA_PRESENT"));

            hasNonCoherence = true;
        }

        return hasNonCoherence;
    }

    private boolean hasPeriodeAffiliationNonValideForReleve(AFAffiliation affiliation, DecompteAndLignes decompte)
            throws Exception {

        boolean hasReleveEnDehorsAffiliation;

        Periode periodeAff = new Periode(affiliation.getDateDebut(), affiliation.getDateFin());
        if (periodeAff
                .isDateDansLaPeriode(resolveDateFromXmlGregorianCalendar(decompte.getDecompte().getMoisDecompte())
                        .getSwissValue())) {
            hasReleveEnDehorsAffiliation = false;
        } else {
            hasReleveEnDehorsAffiliation = true;

            addInfosTraitement(affiliation, decompte, determineTypeReleve(decompte),
                    getSession().getLabel("PROCESS_SDD_DECOMPTE_EN_DEHORS_PERIODE_AFFILIATION"));
        }

        return hasReleveEnDehorsAffiliation;
    }

    private static String determineTypeReleve(DecompteAndLignes decompte) {

        if (DecompteTypeEnum.COMPLEMENTAIRE == decompte.getDecompte().getType()) {
            return CodeSystem.TYPE_RELEVE_COMPLEMENT;
        } else if (DecompteTypeEnum.PERIODIQUE == decompte.getDecompte().getType()) {
            return CodeSystem.TYPE_RELEVE_PERIODIQUE;
        } else {
            throw new IllegalArgumentException("Le type de relevé n'est pas connu : "
                    + decompte.getDecompte().getType());
        }
    }

    private void addInfosTraitement(AFAffiliation affiliation, DecompteAndLignes decompte, String typeReleve,
            String libelleErreur) throws Exception {

        RecapSaisieDecompte saisie = new RecapSaisieDecompteBuilder().withNumeroAffilie(affiliation.getAffilieNumero())
                .withNomPrenom(affiliation.getRaisonSociale()).withLocalite(retrieveLocalite(affiliation))
                .withPeriode(resolveMoisAnneeReleve(decompte)).withType(findLibelleForTypeReleve(typeReleve))
                .withDateSaisie(resolveDateDeSaisie(decompte)).withErreur(libelleErreur).build();

        addRecapSaisieDecompte(saisie);
    }

    static String resolveMoisAnneeReleve(DecompteAndLignes decompte) {
        return resolveDateFromXmlGregorianCalendar(decompte.getDecompte().getMoisDecompte()).getMoisAnneeFormatte();
    }

    static Date resolveDateFromXmlGregorianCalendar(XMLGregorianCalendar dateXmlGregorian) {
        return new Date(dateXmlGregorian.toGregorianCalendar().getTime());
    }

    static String resolveDateDeSaisie(DecompteAndLignes decompte) {
        return (new Date(decompte.getDecompte().getDateDeSaisie().toGregorianCalendar().getTime())).getSwissValue();
    }

    private String findLibelleForTypeReleve(String typeReleve) {
        if (libelleTypeRevle == null) {
            libelleTypeRevle = new HashMap<String, String>();
        }

        if (libelleTypeRevle.get(typeReleve) == null) {

            FWParametersSystemCode code = new FWParametersSystemCode();
            code.setIdCode(typeReleve);
            code.setSession(getSession());
            code.setIdLangue(getSession().getIdLangue());
            try {
                code.retrieve();
                if (code.isNew()) {
                    JadeLogger.error(this, "Error retrieving code systeme for : " + typeReleve);
                } else {
                    libelleTypeRevle.put(typeReleve, code.getLibelle());
                    return code.getLibelle();
                }
            } catch (Exception e) {
                JadeLogger.error(this, "Error retrieving code systeme for : " + typeReleve + " >> " + e.getMessage());
            }

        }

        return libelleTypeRevle.get(typeReleve);
    }

    private void listeExcelSaisieDecompte() throws Exception {

        EBImprimerSaisieDecompteProcess listeRecap = new EBImprimerSaisieDecompteProcess();
        listeRecap.setParentWithCopy(this);
        listeRecap.setEMailAddress(getEMailAddress());
        listeRecap.setListeRecapSaisieDecompte(getListeRecapSaisieDecompte());
        listeRecap.executeProcess();

    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("PROCESS_SDD_SAISIE_DECOMPTE_KO");
        } else {
            return getSession().getLabel("PROCESS_SDD_SAISIE_DECOMPTE_OK");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    @Override
    protected void _executeCleanUp() {
        // do nothing
    }

    public void addRecapSaisieDecompte(RecapSaisieDecompte saisie) {
        if (listeRecapSaisieDecompte == null) {
            listeRecapSaisieDecompte = new ArrayList<RecapSaisieDecompte>();
        }

        listeRecapSaisieDecompte.add(saisie);
    }

    public List<RecapSaisieDecompte> getListeRecapSaisieDecompte() {
        return new ArrayList<RecapSaisieDecompte>(listeRecapSaisieDecompte);
    }

    public String retrieveLocalite(AFAffiliation affiliation) throws Exception {

        TITiers tiers = new TITiers();
        tiers.setSession(getSession());
        tiers.setIdTiers(affiliation.getIdTiers());
        try {
            tiers.retrieve();
        } catch (Exception e) {
            throw new Exception("Technical Exception, Unabled to retrieve the tiers ( idTiers = "
                    + affiliation.getIdTiers() + ")", e);
        }

        if (!tiers.isNew()) {

            TIAdresseDataSource d = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT, affiliation.getAffilieNumero(), JACalendar.todayJJsMMsAAAA(),
                    true, null);

            if (d == null) {
                d = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        ICommonConstantes.CS_APPLICATION_COTISATION, affiliation.getAffilieNumero(),
                        JACalendar.todayJJsMMsAAAA(), true, null);
            }

            return d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA) + " "
                    + d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
        }

        return "";
    }
}
