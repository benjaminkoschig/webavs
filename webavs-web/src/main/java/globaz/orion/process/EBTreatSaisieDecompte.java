package globaz.orion.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.util.JACalendar;
import globaz.hercule.utils.CEUtils;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.releve.AFApercuReleve;
import globaz.naos.db.releve.AFApercuReleveLineFacturation;
import globaz.naos.db.releve.AFApercuReleveManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.ICommonConstantes;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Periode;
import ch.globaz.orion.business.models.sdd.RecapSaisieDecompte;
import ch.globaz.orion.business.models.sdd.RecapSaisieDecompteBuilder;
import ch.globaz.orion.businessimpl.services.sdd.SddServiceImpl;
import ch.globaz.xmlns.eb.sdd.DecompteAndLignes;
import ch.globaz.xmlns.eb.sdd.DecompteEntity;
import ch.globaz.xmlns.eb.sdd.DecompteStatutEnum;
import ch.globaz.xmlns.eb.sdd.DecompteTypeEnum;
import ch.globaz.xmlns.eb.sdd.LigneDeDecompteEntity;

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

            // ****TEST*****
            // List<DecompteAndLignes> listDecomptes = new ArrayList<DecompteAndLignes>();
            // listDecomptes.add(createDecompteAndLignes());
            // ****TEST*****

            // Pas de saisie, on fait rien
            if (listDecomptes.isEmpty()) {
                getMemoryLog().logMessage(getSession().getLabel("PROCESS_SDD_AUCUNE_SDD"), FWMessage.INFORMATION,
                        this.getClass().getName());
                return true;
            }

            // Parcours des saisies des d�comptes
            // Lecture des cotisations � mettre � jour venant de eBusiness
            for (int i = 0; (i < listDecomptes.size()) && !isAborted(); i++) {

                DecompteAndLignes decompte = listDecomptes.get(i);

                // 1 - recherche de l'affiliation
                // ----------------------------------------
                AFAffiliation affiliation = AFAffiliationUtil.getAffiliation(
                        Integer.toString(decompte.getDecompte().getIdAffiliationWebavs()), getSession());

                // 2 - Diff�rent check pour valider que l'on peut bien cr�er le d�compte
                // ----------------------------------------
                if (!hasAllPreConditionsSatisfied(affiliation, decompte)) {
                    SddServiceImpl.changeStatus(decompte.getDecompte().getIdDecompte(), DecompteStatutEnum.ERREUR,
                            getSession());
                    continue;
                }

                // 3 - Traite le d�compte
                // ----------------------------------------
                traiteDecompte(decompte, affiliation);

            }

            // Impression de la liste excel r�capitulative
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
            // Cr�ation du relev�
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
     * Valide toutes les conditions pour r�aliser le relev�
     * 
     * @param affiliation Une affiliation
     * @param decompte Un d�compte provenant de eBusiness
     * @return
     * @throws Exception
     */
    private boolean hasAllPreConditionsSatisfied(AFAffiliation affiliation, DecompteAndLignes decompte)
            throws Exception {
        // Check si periode d'affiliation valide
        if (hasPeriodeAffiliationNonValideForReleve(affiliation, decompte)) {
            return false;
        }

        // Check si on peut bien cr�er ce type de d�compte
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

        // Set le type de relev�
        releve.setType(determineTypeReleve(decompte));

        // Fixe les dates
        releve.setDateDebut(resolveDateDebut(decompte));
        releve.setDateFin(resolveDateFin(decompte));
        releve.setDateReception(resolveDateDeSaisie(decompte));

        // Set la m�thode des int�rets
        releve.setInterets(CodeSystem.INTERET_MORATOIRE_AUTOMATIQUE);

        // Set l'�tat du relev�
        releve.setNewEtat(CodeSystem.ETATS_RELEVE_SAISIE);
        releve.setEtat(CodeSystem.ETATS_RELEVE_SAISIE);

        releve.retrieveIdPassage();

        // G�n�ration de la liste des cotisations
        releve.generationCotisationList();

        // Mise � jour des masses suivant le SDD
        fillMassesInEachCotisation(decompte.getLignesDeDecompte(), releve);

        // Set des la facturation
        releve.setIdExterneFacture("201610000");
        releve.setIdSousTypeFacture("227010");

        releve.setWantControleCotisation(false);

        // Calcul des cotisations
        releve.calculeCotisation();

        // Ajout du relev�
        releve.add(getTransaction());

        addInfosTraitement(affiliation, decompte, determineTypeReleve(decompte), "");
    }

    static void fillMassesInEachCotisation(List<LigneDeDecompteEntity> lignes, AFApercuReleve releve) {
        for (int j = 0; j < releve.getCotisationList().size(); j++) {
            AFApercuReleveLineFacturation releveLine = releve.getCotisationList().get(j);
            for (LigneDeDecompteEntity ligne : lignes) {
                int idAssuranceReleve = Integer.parseInt(releveLine.getCotisationId());
                if (idAssuranceReleve == ligne.getIdCotisationWebavs() && ligne.getNouvelleMasse() != null) {
                    releveLine.setMasse(ligne.getNouvelleMasse().doubleValue());
                }
            }

        }
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

            // Si on est en pr�sence d'un d�compte compl�mentaire et qu'il n'y a pas de d�compte p�riodique
            // On ajout une remarque dans les infos de traitements
            if (DecompteTypeEnum.COMPLEMENTAIRE == decompte.getDecompte().getType()) {
                addInfosTraitement(affiliation, decompte, determineTypeReleve(decompte),
                        getSession().getLabel("PROCESS_SDD_TYPE_COMP_ET_AUCUNE_PERIODIQUE"));
                hasNonCoherence = true;
            }

            // Pas de relev� pr�sent, on peut donc cr�er le relev�.
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
            throw new IllegalArgumentException("Le type de relev� n'est pas connu : "
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

        return "";
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
            tiers = null;
            throw new Exception("Technical Exception, Unabled to retrieve the tiers ( idTiers = "
                    + affiliation.getIdTiers() + ")", e);
        }

        if (tiers != null && !tiers.isNew()) {

            TIAdresseDataSource d = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT, affiliation.getAffilieNumero(), JACalendar.todayJJsMMsAAAA(),
                    true, null);

            if (d == null) {
                d = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        ICommonConstantes.CS_APPLICATION_COTISATION, affiliation.getAffilieNumero(),
                        JACalendar.todayJJsMMsAAAA(), true, null);
            }

            return d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA)
                    + d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
        }

        return "";
    }

    private DecompteAndLignes createDecompteAndLignes() {

        DecompteEntity d = new DecompteEntity();
        d.setCreationDate(createDate("20161031"));
        d.setDateDeSaisie(createDate("20161031"));
        d.setIdAffiliationWebavs(52561); // 000.6789
        d.setIdPartner(0);
        d.setIdUser(0);

        d.setLastModificationDate(createDate("20161031"));
        d.setMoisDecompte(createDate("20161015"));
        d.setStatut(DecompteStatutEnum.SAISIE);
        d.setType(DecompteTypeEnum.PERIODIQUE);
        d.setVersion(1);

        DecompteAndLignes dl = new DecompteAndLignes();
        dl.setDecompte(d);

        return dl;
    }

    private void getLignesDeDecomptesTest(List<LigneDeDecompteEntity> lignes) {
        LigneDeDecompteEntity line1 = new LigneDeDecompteEntity();
        line1.setIdCotisationWebavs(53212);
        line1.setNouvelleMasse(new BigDecimal("112345.15"));
        lignes.add(line1);

        LigneDeDecompteEntity line2 = new LigneDeDecompteEntity();
        line2.setIdCotisationWebavs(53213);
        line2.setNouvelleMasse(new BigDecimal("112345.15"));
        lignes.add(line2);

        LigneDeDecompteEntity line3 = new LigneDeDecompteEntity();
        line3.setIdCotisationWebavs(55967);
        line3.setNouvelleMasse(new BigDecimal("112345.15"));
        lignes.add(line3);

        LigneDeDecompteEntity line4 = new LigneDeDecompteEntity();
        line4.setIdCotisationWebavs(56477);
        line4.setNouvelleMasse(new BigDecimal("112345.15"));
        lignes.add(line4);
    }

    private XMLGregorianCalendar createDate(String date) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date(date).getDate());

        XMLGregorianCalendar xmlDate = null;

        try {
            xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        return xmlDate;
    }
}
