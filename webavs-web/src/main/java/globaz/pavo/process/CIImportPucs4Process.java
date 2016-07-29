package globaz.pavo.process;

import globaz.commons.nss.NSUtil;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.inscriptions.DSDeclarationListeManager;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuelles;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesListeViewBean;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.db.inscriptions.CIJournalManager;
import globaz.pavo.util.CIUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ch.globaz.common.domaine.Montant;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.domaine.pucs.Employee;
import ch.globaz.orion.business.domaine.pucs.PeriodeSalary;
import ch.globaz.orion.business.domaine.pucs.SalaryAvs;
import ch.globaz.orion.business.domaine.pucs.SalaryCaf;
import ch.swissdec.schema.sd._20130514.salarydeclarationconsumercontainer.DeclareSalaryConsumerType;

/**
 * @author MMO 25.07.2016
 **/

public class CIImportPucs4Process extends BProcess {

    private static final long serialVersionUID = -314714724478084902L;

    private CIImportPucsFileProcess launcherImportPucsFileProcess = null;
    private DeclarationSalaire declarationSalaire;
    private CIApplication appCI;

    private String filename = "";
    private Map<String, String> totalParCanton;
    private String accepteLienDraco = "";
    private DSDeclarationViewBean declaration;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private String dateReceptionForced;
    private String provenance = "";
    private String idsPucsFile = null;
    private String simulation = "";
    private TreeMap<String, String> hJournalExisteDeja;
    private String accepteAnneeEnCours = "";
    private String accepteEcrituresNegatives = "";
    private long totalAvertissement = 0;
    private boolean hasDifferenceAc = false;
    private boolean modeInscription = true;

    // Cette table contient les journaux déjà crée
    // si le journal existait déjà avant le traitement, la clé est quand même mise dans cette table et la valeur sera
    // null.
    private final TreeMap<String, Object> tableJournaux = new TreeMap<String, Object>();

    protected Element getSoapBodyPayloadElement(String filePath) throws SAXException, IOException,
            ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        Document doc = dbf.newDocumentBuilder().parse(getClass().getResourceAsStream(filePath));
        NodeList nodes = doc.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Body");
        nodes = nodes.item(0).getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                return (Element) nodes.item(i);
            }
        }

        return null;
    }

    private DeclareSalaryConsumerType unmarshallDeclareSalaryConsumerTypeFromSoapBody(String path) throws SAXException,
            IOException, ParserConfigurationException, JAXBException {
        Element element = getSoapBodyPayloadElement(path);
        JAXBContext jc = JAXBContext.newInstance(DeclareSalaryConsumerType.class);
        DeclareSalaryConsumerType value = jc.createUnmarshaller().unmarshal(element, DeclareSalaryConsumerType.class)
                .getValue();
        return value;
    }

    private void convertPucs4FileToDeclarationSalaire() throws SAXException, IOException, ParserConfigurationException,
            JAXBException {

        DeclareSalaryConsumerType value = unmarshallDeclareSalaryConsumerTypeFromSoapBody(filename);
        PUCS4SalaryConverter salaryConverterPUCS4 = new PUCS4SalaryConverter();

        declarationSalaire = salaryConverterPUCS4.convert(value);

    }

    public String getAccepteLienDraco() {
        return accepteLienDraco;
    }

    public void setAccepteLienDraco(String accepteLienDraco) {
        this.accepteLienDraco = accepteLienDraco;
    }

    public String getSimulation() {
        return simulation;
    }

    public void setSimulation(String simulation) {
        this.simulation = simulation;
    }

    public String getAccepteAnneeEnCours() {
        return accepteAnneeEnCours;
    }

    public void setAccepteAnneeEnCours(String accepteAnneeEnCours) {
        this.accepteAnneeEnCours = accepteAnneeEnCours;
    }

    public String getAccepteEcrituresNegatives() {
        return accepteEcrituresNegatives;
    }

    public void setAccepteEcrituresNegatives(String accepteEcrituresNegatives) {
        this.accepteEcrituresNegatives = accepteEcrituresNegatives;
    }

    public boolean hasDifferenceAc() {
        return hasDifferenceAc;
    }

    public String getIdsPucsFile() {
        return idsPucsFile;
    }

    public void setIdsPucsFile(String idsPucsFile) {
        this.idsPucsFile = idsPucsFile;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public CIImportPucsFileProcess getLauncherImportPucsFileProcess() {
        return launcherImportPucsFileProcess;
    }

    public void setLauncherImportPucsFileProcess(CIImportPucsFileProcess launcherImportPucsFileProcess) {
        this.launcherImportPucsFileProcess = launcherImportPucsFileProcess;
    }

    public CIImportPucs4Process() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
        // Nothing to do

    }

    private void testPeriode(String annee, int moisDebut, int moisFin, String numeroAVS, CIEcriture ecriture,
            ArrayList<String> errors, String numeroAffilie) {

        // Plausi période
        try {

            if (((moisDebut < 1) || (moisDebut > 12)) && (99 != moisDebut) && (66 != moisDebut)) {
                errors.add(getSession().getLabel("DT_MOIS_DEBUT_INVALIDE"));

            } else {

                ecriture.setMoisDebut("" + moisDebut);

            }

            if ((moisFin < 1) || ((moisFin > 12) && (99 != moisFin) && (66 != moisFin))) {
                errors.add(getSession().getLabel("DT_MOIS_FIN_INVALIDE"));

            } else {

                ecriture.setMoisFin("" + moisFin);
            }
            if (moisDebut > moisFin) {
                errors.add(getSession().getLabel("DT_MOIS_DEBUT_PLUS_GRAND"));

            }
            if ((99 == moisDebut) && (99 == moisFin)) {
                if (!ecriture.getWrapperUtil().rechercheEcritureSemblablesDt(getTransaction(),
                        CIUtil.formatNumeroAffilie(getSession(), numeroAffilie), numeroAVS)) {
                    errors.add(getSession().getLabel("MSG_ECRITURE_99"));
                }
            }

            // année en cours et future sont interdites

            if (!"true".equalsIgnoreCase(accepteAnneeEnCours)) {
                if (Integer.parseInt(annee) >= JACalendar.today().getYear()) {
                    errors.add(getSession().getLabel("DT_ANNEE_TROP_GRANDE"));

                }
            } else {
                if (Integer.parseInt(annee) > JACalendar.today().getYear()) {
                    errors.add(getSession().getLabel("DT_ANNEE_TROP_GRANDE"));

                }
            }
        } catch (Exception ex) {
            errors.add(getSession().getLabel("DT_MOIS_INVALIDE"));

        }
    }

    public static void main(String[] args) {
        System.out.println(Integer.parseInt("01"));
    }

    private Montant initMontantTo0IfNull(Montant montant) {

        try {
            montant.getValue();
        } catch (NullPointerException e) {
            return new Montant(0);
        }

        return montant;

    }

    private void createInscription(String numeroAVS, String nom, String prenom, PeriodeSalary periode,
            Montant montantAVS, Montant montantCAF, Montant montantAC, Montant montantAC2, String canton)
            throws Exception {

        montantAVS = initMontantTo0IfNull(montantAVS);
        montantCAF = initMontantTo0IfNull(montantCAF);
        montantAC = initMontantTo0IfNull(montantAC);
        montantAC2 = initMontantTo0IfNull(montantAC2);

        String nomPrenom = nom + "," + prenom;

        long nbrInscriptionsNegatives = 0;
        FWCurrency montantInscriptionsNegatives = new FWCurrency();

        long nbrInscriptionsCI = 0;
        FWCurrency montantInscriptionsCI = new FWCurrency();

        long nbrInscriptionsErreur = 0;
        FWCurrency montantInscriptionsErreur = new FWCurrency();

        long nbrInscriptionsSuspens = 0;
        FWCurrency montantInscriptionsSuspens = new FWCurrency();

        ArrayList<String> info = new ArrayList<String>();
        ArrayList<String> errors = new ArrayList<String>();

        String annee = periode.getDateDebut().getAnnee();
        int moisDebut = Integer.parseInt(periode.getDateDebut().getMois());
        int moisFin = Integer.parseInt(periode.getDateFin().getMois());
        int jourDebut = Integer.parseInt(periode.getDateDebut().getJour());
        int jourFin = Integer.parseInt(periode.getDateFin().getJour());

        AFAffiliation affilie = appCI.getAffilieByNo(getSession(),
                CIUtil.formatNumeroAffilie(getSession(), declarationSalaire.getNumeroAffilie()), true, false, "", "",
                annee, "", "");
        String dateDebutAffiliation = null;
        String dateFinAffiliation = null;
        if (affilie != null) {
            dateDebutAffiliation = affilie.getDateDebut();
            dateFinAffiliation = affilie.getDateFin();

        }

        if (declarationSalaire.isAfSeul()) {
            launcherImportPucsFileProcess.setTraitementAFSeul(true);
            if (affilie != null) {
                if (!JadeStringUtil.isBlankOrZero(canton)) {
                    // Cumul par canton
                    if (totalParCanton.containsKey(canton)) {
                        // Cumul du montant
                        FWCurrency cumul = new FWCurrency(totalParCanton.get(canton));
                        cumul.add(montantCAF.getValue());
                        totalParCanton.put(canton, cumul.toString());
                    } else {
                        totalParCanton.put(canton, montantCAF.getValue());
                    }
                }

            } else {
                launcherImportPucsFileProcess.getMemoryLog().logMessage(
                        getSession().getLabel("MSG_AFFILIE_NON_VALIDE") + " - Affilié  "
                                + declarationSalaire.getNumeroAffilie() + " - Année " + annee, FWMessage.ERREUR, "");
            }

        }

        CIEcriture ecriture = new CIEcriture();
        ecriture.setForAffilieParitaire(true);
        if (!"true".equalsIgnoreCase(getAccepteLienDraco())) {
            ecriture.setSession((BSession) getSessionCI(getSession()));
        } else {
            ecriture.setSession(getSession());
        }

        // Pour les déclarations de salaire:
        // trouver le journal à utiliser pour ce record.
        // il y a un journal par année/affilié.
        // si le journal n'existe pas on le crée et on le garde dans une table car
        // il peut être utilisé par plusieurs ligne du fichier.
        // si le journal existe préalablement au traitement du fichier, on génère une erreur.
        // findJournal retourne le journal à utiliser, ou null si le jounal à utilisé existait déjà
        // avant ce traitement,
        // ce qui n'est pas autorisé.
        // Pour les cot. pers: les inscriptions qui concernent l'année en cours vont dans le journal
        // de l'année en cours, les autres sont dans le même journal.
        CIJournal journal = null;
        String key = "";

        if (!declarationSalaire.isAfSeul()) {

            journal = _findJournal(annee, affilie.getTiersNom());

            key = _getKey(annee, affilie.getTiersNom());

        }

        if (journal == null && !declarationSalaire.isAfSeul()) {

            // Erreur, ce journal existe déjà avant ce traitement
            hJournalExisteDeja.put(key,
                    getSession().getLabel("DT_JOURNAL_NON_CREE") + " " + declarationSalaire.getNumeroAffilie() + "/"
                            + annee);
        } else {

            ecriture.setAnnee(annee);

            boolean breakTests = false;

            // Plausi période
            testPeriode(annee, moisDebut, moisFin, numeroAVS, ecriture, errors, declarationSalaire.getNumeroAffilie());

            // Plausi montant
            boolean montantPositif = montantAVS.isPositive() || montantAVS.isZero();

            String montantEcr = montantAVS.getValue();

            if (montantPositif) {
                montantEcr = testAndSetPourMontantPositif(ecriture, errors, montantEcr);
            } else {
                montantEcr = montantAVS.negate().getValue();
                nbrInscriptionsNegatives++;
                montantInscriptionsNegatives.sub(montantEcr);
                montantEcr = testAndSetPourMontantNegatif(ecriture, errors, montantEcr);
            }
            // Période affiliation
            if (!_isInPeriodeAffiliation(dateDebutAffiliation, dateFinAffiliation, annee, moisDebut, moisFin)) {
                // Les dates ne correspondent pas avec la période d'affiliation
                errors.add(getSession().getLabel("DT_ERR_DATE_AFFILIATION"));
            }
            // Plausi no avs
            String noAvs = testEndSetInfoPourNumAvs(numeroAVS, nomPrenom, ecriture, errors);

            ecriture.setNomPrenom(CIDeclaration.getNomFormatCI(nomPrenom));

            /************************************************************
             * Modif. 03.05.2006 Plus de plausi sur le nom, car on insère plus la virgule
             * automatiquement
             * */
            int anneeNaissance = determineAnneeNaissance(numeroAVS, ecriture);

            if (!JadeStringUtil.isBlankOrZero(montantCAF.getValue()) && !declarationSalaire.isAfSeul()) {
                BigDecimal mntAf = new BigDecimal(montantCAF.getValue());
                BigDecimal mntAvs = new BigDecimal(montantAVS.getValue());
                if (mntAf.compareTo(mntAvs) != 0) {
                    errors.add(getSession().getLabel("DT_AF_DIFF_AVS"));
                }

            }

            //
            // !!! Attention test basé sur le numéro AVS pour calculé l'age
            //
            if ((anneeNaissance + 1918) > Integer.parseInt(annee)) {
                // test de l'age à partir du n°AVS
                errors.add(getSession().getLabel("DT_ERR_AGE_MIN"));
                breakTests = true;
            } else {
                if (CIDeclaration.isAvs0(noAvs)) {
                    ecriture.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE_SUSPENS);
                    info.add(getSession().getLabel("DT_AVS_0"));
                } else {
                    if (noAvs.length() < 11) {
                        // avs trop court
                        ecriture.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE_SUSPENS);
                        info.add(getSession().getLabel("DT_ERR_AVS_11"));
                    }
                }
                if (!ecriture.rechercheCI(getTransaction(), null, false, false) || getTransaction().hasErrors()) {
                    // erreur de création de CI
                    info.add(getSession().getLabel("DT_NUM_AVS_INVALIDE"));
                    breakTests = true;
                }
            }

            // test sur le total des inscriptions pour l'affiliation et l'année en cours
            if (declarationSalaire.isAfSeul()) {
                breakTests = true;
            } else if ("true".equalsIgnoreCase(accepteEcrituresNegatives) && !montantPositif) {
                if (CICompteIndividuel.CS_REGISTRE_PROVISOIRE.equals(ecriture.getCI(getTransaction(), false)
                        .getRegistre())) {
                    errors.add(getSession().getLabel("MSG_DT_INCONNU_ET_NEG"));
                } else {

                    BigDecimal totalPourAff = new BigDecimal("0");
                    try {
                        totalPourAff = ecriture.getWrapperUtil().rechercheEcritureEmpResult(getTransaction(),
                                CIUtil.formatNumeroAffilie(getSession(), declarationSalaire.getNumeroAffilie()));
                        totalPourAff = totalPourAff.subtract(new BigDecimal(montantEcr.trim()));
                    } catch (Exception e) {
                        totalPourAff = new BigDecimal("0");

                    }
                    int res = totalPourAff.compareTo(new BigDecimal("0"));
                    if (res < 0) {
                        errors.add(getSession().getLabel("MSG_ECRITURE_SUMEMP"));
                    }
                }
            }

            if (!breakTests) {
                if (CICompteIndividuel.CS_REGISTRE_PROVISOIRE.equals(ecriture.getCI(getTransaction(), false)
                        .getRegistre())) {
                    // CI Provisoire
                    traitementSiRegistreProvisoire(annee, moisDebut, moisFin, ecriture, errors, info,
                            declarationSalaire.getNumeroAffilie(), noAvs);
                } else {
                    // ci ok, recherche des écritures identiques
                    CIEcritureManager ecrMgr = new CIEcritureManager();
                    ecrMgr.setSession(getSession());
                    ecrMgr.setForAnnee(annee);
                    ecrMgr.setForCompteIndividuelId(ecriture.getCI(getTransaction(), false).getCompteIndividuelId());
                    ecrMgr.setForAffilie(CIUtil.formatNumeroAffilie(getSession(), declarationSalaire.getNumeroAffilie()));
                    ecrMgr.find(getTransaction());
                    for (int i = 0; i < ecrMgr.size(); i++) {
                        CIEcriture ecr = (CIEcriture) ecrMgr.getEntity(i);
                        if (CIEcriture.CS_CODE_PROVISOIRE.equals(ecr.getCode())) {
                            // erreur: écriture provisoire déjà présente
                            errors.add(getSession().getLabel("DT_INSCR_PROV"));
                            break;
                        }
                        if ((ecr.getMoisDebut().equals(moisDebut + ""))
                                && (ecr.getMoisFin().equals(moisFin + ""))
                                && ("01".equals(ecr.getGreFormat()) || "11".equals(ecr.getGreFormat())
                                        || "07".equals(ecr.getGreFormat()) || "17".equals(ecr.getGreFormat()))
                                && ((ecr.getMontant().substring(0, ecr.getMontant().length() - 3)).equals(ecriture
                                        .getMontant().substring(0, ecriture.getMontant().length() - 3)))) {

                            if ((JadeStringUtil.isEmpty(ecriture.getExtourne()) && "0".equals(ecr.getExtourne()))
                                    || ecr.getExtourne().equals(ecriture.getExtourne())) {

                                // erreur: écriture identique
                                errors.add(getSession().getLabel("DT_INSCR_IDENTIQUE"));

                                break;
                            }

                        }
                    }
                    // au CI, tester clôture
                    String clo = ecriture.getCI(getTransaction(), false).getDerniereCloture(true);
                    if (ecriture.aCloturer(new JADate(clo))) {
                        // écriture avant clôture

                        if ((errors.size() == 0) && (info.size() == 0)) {
                            nbrInscriptionsCI++;

                            if (montantPositif) {
                                montantInscriptionsCI.add(montantEcr);
                            } else {
                                montantInscriptionsCI.sub(montantEcr);
                            }
                        }
                    } else {
                        if (!ecriture.getCI(getTransaction(), false).isCiOuvert().booleanValue()) {
                            if (ecriture.isPeriodeDeCotisationACheval(getTransaction(), new JADate(clo))) {
                                errors.add(getSession().getLabel("MSG_IK_PERIODE_A_CHEVAL"));
                            } else {
                                info.add(getSession().getLabel("DT_ECR_APRES_CLOTURE"));
                            }
                        } else {
                            if (ecriture.isPeriodeDeCotisationACheval(getTransaction(), new JADate(clo))) {
                                errors.add(getSession().getLabel("MSG_IK_PERIODE_A_CHEVAL"));
                            }
                            if ((errors.size() == 0) && (info.size() == 0)) {
                                nbrInscriptionsCI++;
                                if (montantPositif) {
                                    montantInscriptionsCI.add(montantEcr);
                                } else {
                                    montantInscriptionsCI.sub(montantEcr);
                                }
                            }
                        }
                    }
                }
            }

            if (errors.size() != 0) {
                // si il y a eu des erreurs
                nbrInscriptionsErreur++;
                if (montantPositif) {
                    montantInscriptionsErreur.add(montantEcr);
                } else {
                    montantInscriptionsErreur.sub(montantEcr);
                }
                // result = false;
            } else {
                // pas d'erreur
                if (info.size() != 0) {
                    nbrInscriptionsSuspens++;
                    if (montantPositif) {
                        montantInscriptionsSuspens.add(montantEcr);
                    } else {
                        montantInscriptionsSuspens.sub(montantEcr);
                    }
                }
                // -------------------------------------------------------------------------------
                // Ajout écriture et mis a jour du journal
                // -------------------------------------------------------------------------------
                if (modeInscription && !declarationSalaire.isAfSeul()) {
                    // journal trouvé
                    ecriture.setIdJournal(journal.getIdJournal());
                    // Si on est en mode linkDraco, on ne passe pas par l'écriture, mais par
                    // l'inscription DRACO
                    if ("true".equalsIgnoreCase(accepteLienDraco)) {
                        DSInscriptionsIndividuelles insc = new DSInscriptionsIndividuelles();
                        // recherche de la déclaration en question
                        DSDeclarationListeManager decMgr = new DSDeclarationListeManager();
                        decMgr.setForIdJournal(journal.getIdJournal());
                        decMgr.setSession((BSession) getSessionDS(getSession()));
                        decMgr.wantCallMethodAfter(false);
                        decMgr.find(getTransaction());
                        if (decMgr.size() > 0) {
                            DSInscriptionsIndividuellesListeViewBean declarationDraco = (DSInscriptionsIndividuellesListeViewBean) decMgr
                                    .getFirstEntity();
                            insc.setDeclaration(declarationDraco);
                            insc.setSession((BSession) getSessionDS(getSession()));
                            insc.setIdDeclaration(declarationDraco.getIdDeclaration());
                            insc.setGenreEcriture(ecriture.getGre());
                            if (!JadeStringUtil.isIntegerEmpty(String.valueOf(jourDebut))) {
                                insc.setPeriodeDebut(jourDebut + "." + moisDebut);
                            } else {
                                insc.setPeriodeDebut(ecriture.getMoisDebutPad());
                            }
                            if (!JadeStringUtil.isIntegerEmpty(String.valueOf(jourFin))) {
                                insc.setPeriodeFin(jourFin + "." + moisFin);
                            } else {
                                insc.setPeriodeFin(ecriture.getMoisFinPad());
                            }
                            if (!JadeStringUtil.isIntegerEmpty(canton)) {
                                try {
                                    String codeCanton = CIUtil.codeUtilisateurToCodeSysteme(getTransaction(), canton,
                                            "PYCANTON", getSession());
                                    insc.setCodeCanton(codeCanton);
                                } catch (Exception e) {
                                }
                            } else {
                                String codeCanton = AFAffiliationUtil.getCantonAFForDS(affilie,
                                        JACalendar.todayJJsMMsAAAA());
                                insc.setCodeCanton(codeCanton);

                            }
                            insc.setFromPucs(true);
                            insc.setMontant(JANumberFormatter.deQuote(ecriture.getMontant()));
                            insc.setNumeroAvs(CIUtil.unFormatAVS(ecriture.getAvs()));
                            insc.setNomPrenom(ecriture.getNomPrenom());
                            insc.setAnneeInsc(ecriture.getAnnee());

                            if (!JadeStringUtil.isBlankOrZero(montantCAF.getValue())) {
                                insc.setMontantAf(JANumberFormatter.deQuote(montantCAF.getValue()));
                            }

                            insc.add(getTransaction());

                            boolean differenceAc = false;
                            if (!DeclarationSalaireProvenance.fromValueWithOutException(provenance).isDan()
                                    && !JadeStringUtil.isBlank(montantAC.getValue())) {
                                BigDecimal montantCommunique = new BigDecimal(montantAC.getValue());
                                BigDecimal montantEcriture = null;
                                if (!JadeStringUtil.isBlank(insc.getACI())) {
                                    montantEcriture = new BigDecimal(insc.getACI());
                                } else {
                                    montantEcriture = new BigDecimal("0");
                                }
                                if (montantCommunique.compareTo(montantEcriture) != 0) {
                                    info.add(getSession().getLabel("MSG_MONTANT_AC") + " "
                                            + new FWCurrency(montantCommunique.toString()).toStringFormat() + " / "
                                            + new FWCurrency(montantEcriture.toString()).toStringFormat());
                                    if ((nbrInscriptionsSuspens == 0) && (nbrInscriptionsSuspens == 0)) {
                                        totalAvertissement = totalAvertissement + 1;
                                    }
                                    differenceAc = true;
                                    hasDifferenceAc = true;
                                }

                            }
                            if (!DeclarationSalaireProvenance.fromValueWithOutException(provenance).isDan()
                                    && !JadeStringUtil.isBlank(montantAC2.getValue())) {
                                BigDecimal montantCommunique = new BigDecimal(montantAC2.getValue());
                                BigDecimal montantEcriture = null;
                                if (!JadeStringUtil.isBlank(insc.getACII())) {
                                    montantEcriture = new BigDecimal(insc.getACII());
                                } else {
                                    montantEcriture = new BigDecimal("0");
                                }
                                if (montantCommunique.compareTo(montantEcriture) != 0) {
                                    info.add(getSession().getLabel("MSG_MONTANT_AC2") + " "
                                            + new FWCurrency(montantCommunique.toString()).toStringFormat() + " / "
                                            + new FWCurrency(montantEcriture.toString()).toStringFormat());
                                    if ((nbrInscriptionsSuspens == 0) && (nbrInscriptionsSuspens == 0)) {
                                        totalAvertissement = totalAvertissement + 1;
                                    }
                                    differenceAc = true;
                                    hasDifferenceAc = true;
                                }

                            }
                            // Si différence AC ou AC2 pour SwissDec, il faut remettre les montants
                            // communiqués via SwissDec
                            // car le montant ne doit pas être recalculé (période fausse)
                            if (differenceAc && DSDeclarationViewBean.PROVENANCE_SWISSDEC.equals(provenance)
                                    && !insc.isNew() && !insc.hasErrors()) {
                                insc.setACI(montantAC.getValue());
                                insc.setACII(montantAC2.getValue());
                                insc.wantCallValidate(false);
                                insc.update(getTransaction());
                            }

                            if (CIUtil.isRetraite(ecriture.getAvs(), Integer.parseInt(ecriture.getAnnee()),
                                    ecriture.getAvsNNSS(), getSession())) {
                                info.add(getSession().getLabel("MSG_PUCS_ASSURE_RETRAITE"));
                            }
                        } else {
                            getTransaction().addErrors(getSession().getLabel("DECL_NON_EXISTANTE"));
                        }
                    } else {
                        ecriture.setNoSumNeeded(true);
                        // Modif. mettre le kbbatt à 2 pour concordance nnss
                        ecriture.setWantForDeclaration(new Boolean(false));
                        ecriture.add(getTransaction());

                    }
                    getTransaction().disableSpy();
                    // journal.updateInscription(getTransaction());
                    // getTransaction().enableSpy();
                    if (!getTransaction().hasErrors()) {
                        getTransaction().commit();
                    }
                }
            }

        }

    }

    private void traitementSiRegistreProvisoire(String annee, int moisDebut, int moisFin, CIEcriture ecriture,
            ArrayList<String> errors, ArrayList<String> info, String numeroAffilie, String noAvs) throws Exception {
        // assuré inconnu
        info.add(getSession().getLabel("DT_ASSURE_INCONNU"));
        CICompteIndividuel ci = new CICompteIndividuel();
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        if ((!JadeStringUtil.isEmpty(ecriture.getAvs()) || !JadeStringUtil.isEmpty(ecriture.getNomPrenom()))
                && !"00000000".equals(ecriture.getAvs())) {

            if (!JadeStringUtil.isEmpty(ecriture.getAvs())) {
                ciMgr.setForNumeroAvs(ecriture.getAvs());
            } else {
                ciMgr.setForNomPrenom(ecriture.getNomPrenom());
            }
        } else if ("00000000".equals(ecriture.getAvs())) {
            ciMgr.setForNumeroAvs(noAvs);
        }
        if (!JadeStringUtil.isEmpty(ciMgr.getForNumeroAvs()) || !JadeStringUtil.isEmpty(ciMgr.getForNomPrenom())) {
            ciMgr.setSession(getSession());
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
            ciMgr.find(getTransaction());
            if (ciMgr.size() != 0) {
                ci = (CICompteIndividuel) ciMgr.getFirstEntity();
                CIEcritureManager ecrMgr = new CIEcritureManager();
                ecrMgr.setSession(getSession());
                ecrMgr.setForAnnee(annee);
                ecrMgr.setForCompteIndividuelId(ci.getCompteIndividuelId());
                ecrMgr.setForAffilie(CIUtil.formatNumeroAffilie(getSession(), numeroAffilie));
                ecrMgr.find(getTransaction());
                for (int i = 0; i < ecrMgr.size(); i++) {
                    CIEcriture ecr = (CIEcriture) ecrMgr.getEntity(i);
                    if ((ecr.getMoisDebut().equals(moisDebut + ""))
                            && (ecr.getMoisFin().equals(moisFin + ""))
                            && ("01".equals(ecr.getGreFormat()) || "11".equals(ecr.getGreFormat())
                                    || "07".equals(ecr.getGreFormat()) || "17".equals(ecr.getGreFormat()))

                            // && (ecr.getMontant().equals(ecriture.getMontant()))) {
                            && ((ecr.getMontant().substring(0, ecr.getMontant().length() - 3)).equals(ecriture
                                    .getMontant().substring(0, ecriture.getMontant().length() - 3)))) {

                        if ((JadeStringUtil.isEmpty(ecriture.getExtourne()) && "0".equals(ecr.getExtourne()))
                                || ecr.getExtourne().equals(ecriture.getExtourne())) {

                            // erreur: écriture identique
                            errors.add(getSession().getLabel("DT_INSCR_IDENTIQUE"));

                            break;
                        }
                    }
                }
            }
        }
    }

    private int determineAnneeNaissance(String numeroAVS, CIEcriture ecriture) throws JAException {

        int anneeNaissance = 60;

        if (numeroAVS.trim().length() < 13) {
            try {
                anneeNaissance = Integer.parseInt(numeroAVS.substring(3, 5));
            } catch (Exception e) {
                System.out.println("Erreur : " + numeroAVS);
            }
        } else {
            CICompteIndividuel ci = ecriture.getForcedCi(getTransaction());
            if (ci != null) {
                JADate dateNaiss = new JADate(ci.getDateNaissance());
                anneeNaissance = dateNaiss.getYear();
                String anneeString = String.valueOf(anneeNaissance);
                if (anneeString.length() == 4) {
                    anneeString = anneeString.substring(2, 4);
                    anneeNaissance = Integer.parseInt(anneeString);
                }

            } else {
                anneeNaissance = 70;
            }

        }

        return anneeNaissance;
    }

    private String testEndSetInfoPourNumAvs(String numeroAVS, String nomPrenom, CIEcriture ecriture,
            ArrayList<String> errors) {
        String noAvs = numeroAVS.trim();
        // Modif v4.12 => dans pucs, le no peut être vide, pour avoir un identiant, on set le no
        if (JadeStringUtil.isBlank(noAvs)) {
            noAvs = "00000000000";
        }
        if (noAvs.endsWith("000") && (noAvs.trim().length() != 13)) {
            noAvs = numeroAVS.substring(0, numeroAVS.lastIndexOf("000"));
        }
        // pour les cas ersam catherine
        if (noAvs.trim().startsWith("000")) {
            if (!JadeStringUtil.isEmpty(nomPrenom.trim())) {
                ecriture.setAvs("");
            } else {
                ecriture.setAvs(noAvs);
            }
        } else {
            ecriture.setAvs(noAvs);
        }
        if (CIUtil.isNNSSlengthOrNegate(noAvs)) {
            ecriture.setNumeroavsNNSS("true");
            ecriture.setAvsNNSS("true");
        }
        if ("true".equalsIgnoreCase(ecriture.getNumeroavsNNSS()) && !NSUtil.nssCheckDigit(ecriture.getAvs())) {
            errors.add(getSession().getLabel("MSG_CI_VAL_AVS"));

        }
        return noAvs;
    }

    private boolean _isInPeriodeAffiliation(String dateDebutAffiliation, String dateFinAffiliation, String annee,
            int moisDebut, int moisFin) throws Exception {
        if ((dateDebutAffiliation == null) || (dateFinAffiliation == null)) {
            return false;
        }
        JADate debAff = new JADate(dateDebutAffiliation);
        JADate finAff = new JADate(("".equals(dateFinAffiliation)) ? "01.01.9999" : dateFinAffiliation);
        // Si c'est 99-99, on compare juste l'année
        if (((99 == moisDebut) && (99 == moisFin)) || ((66 == moisDebut) && (66 == moisFin))) {
            int anneeInt = Integer.parseInt(annee);
            int anneeDebutAff = debAff.getYear();
            if (anneeInt < anneeDebutAff) {
                return false;
            }
            if (!JAUtil.isDateEmpty(dateFinAffiliation)) {
                int anneeFinAff = finAff.getYear();
                if (anneeInt > anneeFinAff) {
                    return false;
                }
            }
        } else {
            JADate deb = new JADate(1, moisDebut, Integer.parseInt(annee));
            JADate fin = new JADate(1, moisFin, Integer.parseInt(annee));
            if (!BSessionUtil.compareDateBetweenOrEqual(getTransaction().getSession(), debAff.toStr("."),
                    finAff.toStr("."), deb.toStr("."))) {
                return false;
            }
            if (!BSessionUtil.compareDateBetweenOrEqual(getTransaction().getSession(), debAff.toStr("."),
                    finAff.toStr("."), fin.toStr("."))) {
                return false;
            }
        }
        return true;
    }

    private String testAndSetPourMontantNegatif(CIEcriture ecriture, ArrayList<String> errors, String montantEcr) {

        if (!"true".equalsIgnoreCase(getAccepteEcrituresNegatives())) {
            FWCurrency cur = new FWCurrency(montantEcr);
            errors.add(getSession().getLabel("DT_ECRITURE_NEGATIVE"));
            ecriture.setGre("11");
            ecriture.setExtourne(CIEcriture.CS_EXTOURNE_1);
            ecriture.setMontant(cur.toStringFormat());
        } else {
            try {
                FWCurrency cur = new FWCurrency(montantEcr);
                if (cur.compareTo(new FWCurrency("1")) == -1) {
                    errors.add(getSession().getLabel("DT_MONTANT_INF_1CHF"));
                } else {
                    ecriture.setGre("11");
                    ecriture.setExtourne(CIEcriture.CS_EXTOURNE_1);
                    ecriture.setMontant(cur.toStringFormat());
                }
            } catch (Exception inex) {
                errors.add(getSession().getLabel("DT_MONTANT_INVALIDE"));
                montantEcr = "0.00";

            }
        }
        return montantEcr;
    }

    private String testAndSetPourMontantPositif(CIEcriture ecriture, ArrayList<String> errors, String montantEcr) {
        try {
            FWCurrency cur = new FWCurrency(montantEcr);
            if (cur.compareTo(new FWCurrency("1")) == -1) {
                errors.add(getSession().getLabel("DT_MONTANT_INF_1CHF"));
                ecriture.setMontant(montantEcr);
            } else {
                ecriture.setGre("01");
                ecriture.setMontant(cur.toStringFormat());
            }
        } catch (Exception inex) {
            errors.add(getSession().getLabel("DT_MONTANT_INVALIDE"));
            montantEcr = "        0.00";

        }
        return montantEcr;
    }

    /*
     * Rretourne un clé qui sera utilisée pour trier les treemaps du process cette clé est composée du n° affilié et de
     * l'année
     */
    private String _getKey(String annee, String nomAffilie) throws Exception {

        return CIUtil.UnFormatNumeroAffilie(getSession(), declarationSalaire.getNumeroAffilie()) + "/" + annee + "#"
                + nomAffilie;

    }

    private BISession getSessionDS(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionDraco");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("DRACO").newSession(local);
            local.setAttribute("sessionDraco", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /*
     * Cherche le journal à utiliser pour ce record Si le journal a déjà été ouvert dans ce process, on le réutilise
     * sinon on le crée (si modeInscription)
     */
    private CIJournal _findJournal(String annee, String nomAffilie) throws Exception {

        // cle pour pouvoir stock un journal par affilié/année
        String key = _getKey(annee, nomAffilie);
        CIJournal journal = null;
        String numAffFormate = CIUtil.formatNumeroAffilie(getSession(), declarationSalaire.getNumeroAffilie());
        if (tableJournaux.containsKey(key)) {
            journal = (CIJournal) tableJournaux.get(key);
        } else {
            // on a pas encore eu à traité ce journal.
            // si il existe dejà dans la DB, on génère une erreur
            CIJournalManager jrnMgr = new CIJournalManager();
            jrnMgr.setSession(getSession());
            jrnMgr.setForAnneeCotisation(annee);
            jrnMgr.setForIdTypeInscription(CIJournal.CS_DECLARATION_SALAIRES);
            jrnMgr.setForIdAffiliation(numAffFormate);
            int size = jrnMgr.getCount(getTransaction());
            journal = new CIJournal();
            journal.setSession(getSession());
            journal.setAnneeCotisation(annee);
            journal.setIdAffiliation(numAffFormate, true, false);
            journal.setLibelle(getSession().getLabel("MSG_LIBELLE_JOURNAL_DECLARATION"));
            journal.setIdTypeCompte(CIJournal.CS_PROVISOIRE);
            journal.setDateReception(dateReceptionForced);
            boolean wantCreatePrincipale = true;
            if (size == 0) {
                // si il n'existe pas encore dans la DB, on le crée (sauf en mode simulation)
                journal.setIdTypeInscription(CIJournal.CS_DECLARATION_SALAIRES);
            } else {
                wantCreatePrincipale = false;
                journal.setIdTypeInscription(CIJournal.CS_DECLARATION_COMPLEMENTAIRE);
            }
            try {
                if (modeInscription) {

                    // mode inscription
                    journal.add(getTransaction());
                    if (!getTransaction().hasErrors()) {
                        if ("true".equalsIgnoreCase((accepteLienDraco))) {
                            declaration = null;
                            DSDeclarationListViewBean dsMgr = new DSDeclarationListViewBean();
                            dsMgr.setSession((BSession) getSessionDS(getSession()));
                            dsMgr.setForAffiliationId(journal.getIdAffiliation());
                            dsMgr.setForAnnee(journal.getAnneeCotisation());
                            dsMgr.setForEtat(DSDeclarationViewBean.CS_OUVERT);
                            if (wantCreatePrincipale) {
                                dsMgr.setForTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
                            } else {
                                dsMgr.setForTypeDeclaration(DSDeclarationViewBean.CS_COMPLEMENTAIRE);
                            }
                            dsMgr.find(getTransaction());
                            if (dsMgr.size() > 0) {
                                declaration = (DSDeclarationViewBean) dsMgr.getFirstEntity();
                                declaration.setIdJournal(journal.getIdJournal());

                                declaration.setProvenance(getProvenance());

                                declaration.setIdPucsFile(idsPucsFile);
                                declaration.update(getTransaction());
                            } else {
                                declaration = new DSDeclarationViewBean();
                                declaration.setAffiliationId(journal.getIdAffiliation());
                                declaration.setSession((BSession) getSessionDS(getSession()));
                                declaration.setNumeroAffilie(declarationSalaire.getNumeroAffilie());
                                declaration.setAnnee(journal.getAnneeCotisation());
                                declaration.setProvenance(getProvenance());
                                declaration.setIdPucsFile(idsPucsFile);

                                if (wantCreatePrincipale) {
                                    declaration.setTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
                                } else {
                                    declaration.setTypeDeclaration(DSDeclarationViewBean.CS_COMPLEMENTAIRE);
                                }
                                declaration.setEtat(DSDeclarationViewBean.CS_OUVERT);
                                declaration.setIdJournal(journal.getIdJournal());
                                // Modif FER 1-5-6-1
                                if (!JadeStringUtil.isBlankOrZero(dateReceptionForced)) {
                                    declaration.setDateRetourEff(dateReceptionForced);
                                }
                                declaration.add(getTransaction());

                            }
                        }
                    }
                    // Maj type de déclaration de salaire swissdec
                    if (!getTransaction().hasErrors() && declaration != null
                            && DSDeclarationViewBean.PROVENANCE_SWISSDEC.equalsIgnoreCase(getProvenance())
                            && !JadeStringUtil.isBlankOrZero(declaration.getAffiliationId())) {
                        AFAffiliation affiliation = declaration.getAffiliation();
                        if (affiliation != null && !affiliation.isNew()) {
                            affiliation.setDeclarationSalaire(CodeSystem.DS_SWISSDEC);
                            affiliation.setWantGenerationSuiviLAALPP(false);
                            affiliation.update(getTransaction());
                        }
                    }

                    if (getTransaction().hasErrors()) {
                        journal = null;
                        if (launcherImportPucsFileProcess != null) {
                            launcherImportPucsFileProcess
                                    .setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
                        }
                        getMemoryLog().logMessage("Erreur transaction:" + getTransaction().getErrors().toString(),
                                FWMessage.INFORMATION, "Importation CI");
                        // Pour éviter de logger dans le mail et d'ajouter une erreur dans la transaction alors que le
                        // cas est géré
                        getTransaction().clearErrorBuffer();
                    }
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
                journal = null;
            }
            tableJournaux.put(key, journal);
        }
        return journal;
    }

    private BISession getSessionCI(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionPavo");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("PAVO").newSession(local);
            local.setAttribute("sessionPavo", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    private void initProcess() throws Exception {
        appCI = (CIApplication) GlobazServer.getCurrentSystem().getApplication(CIApplication.DEFAULT_APPLICATION_PAVO);
        totalParCanton = new HashMap<String, String>();
        dateReceptionForced = JACalendar.todayJJsMMsAAAA();
        hJournalExisteDeja = new TreeMap<String, String>();
        modeInscription = JadeStringUtil.isBlankOrZero(getSimulation());
    }

    private void createInscriptionAVSAF(Employee employe, SalaryAvs salaireAVS, SalaryCaf salaireCAF) throws Exception {
        createInscription(employe.getNss(), employe.getNom(), employe.getPrenom(), salaireAVS.getPeriode(),
                salaireAVS.getMontantAvs(), salaireCAF.getMontant(), salaireAVS.getMontantAc1(),
                salaireAVS.getMontantAc2(), salaireCAF.getCanton());

    }

    private void createInscriptionAVS(Employee employe, SalaryAvs salaireAVS) throws Exception {

        createInscription(employe.getNss(), employe.getNom(), employe.getPrenom(), salaireAVS.getPeriode(),
                salaireAVS.getMontantAvs(), new Montant(0), salaireAVS.getMontantAc1(), salaireAVS.getMontantAc2(), "");

    }

    private void createInscriptionAF(Employee employe, SalaryCaf salaireCAF) throws Exception {

        createInscription(employe.getNss(), employe.getNom(), employe.getPrenom(), salaireCAF.getPeriode(),
                new Montant(0), salaireCAF.getMontant(), new Montant(0), new Montant(0), salaireCAF.getCanton());

    }

    private List<SalaryCaf> traiterSalaireAVSAF(Employee employe) throws Exception {

        List<SalaryCaf> listSalaryCAFDejaTraite = new ArrayList<SalaryCaf>();

        for (SalaryAvs salaireAVS : employe.getSalariesAvs()) {

            SalaryCaf salaireCAFMemePeriode = null;

            for (SalaryCaf salaireCAF : employe.getSalariesCaf()) {
                if (salaireAVS.getPeriode().equals(salaireCAF.getPeriode())) {
                    salaireCAFMemePeriode = salaireCAF;
                    listSalaryCAFDejaTraite.add(salaireCAF);
                    break;
                }
            }

            if (salaireCAFMemePeriode != null) {
                createInscriptionAVSAF(employe, salaireAVS, salaireCAFMemePeriode);
            } else {
                createInscriptionAVS(employe, salaireAVS);
            }

        }

        return listSalaryCAFDejaTraite;

    }

    private void traiterSoldeSalaireCAF(Employee employe, List<SalaryCaf> listSalaryCAFDejaTraite) throws Exception {

        for (SalaryCaf salaireCAF : employe.getSalariesCaf()) {
            if (!listSalaryCAFDejaTraite.contains(salaireCAF)) {
                createInscriptionAF(employe, salaireCAF);
            }
        }
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {
            filename = "/globaz/pavo/process/7011054.xml";
            initProcess();
            convertPucs4FileToDeclarationSalaire();

            for (Employee employe : declarationSalaire.getEmployees()) {
                traiterSoldeSalaireCAF(employe, traiterSalaireAVSAF(employe));
            }

        } catch (Exception e) {

            getMemoryLog().logMessage(e.toString(), FWMessage.INFORMATION, this.getClass().getName());
            return false;
        }

        return !(isAborted() || isOnError() || getSession().hasErrors());

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    @Override
    protected String getEMailObject() {

        return "TODO";
    }

}
