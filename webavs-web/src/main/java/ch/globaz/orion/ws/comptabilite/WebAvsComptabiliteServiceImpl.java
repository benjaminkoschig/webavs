package ch.globaz.orion.ws.comptabilite;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageManager;
import globaz.musca.process.FAImpressionFactureProcess;
import globaz.musca.process.FAImpressionFactureProcess.ModeFonctionnementEnum;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import ch.globaz.common.document.reference.ReferenceBVR;
import globaz.osiris.external.IntRole;
import globaz.osiris.print.itext.list.CAProcessImpressionExtraitCompteAnnexe;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.orion.business.constantes.EBProperties;
import ch.globaz.orion.ws.enums.OrderByDirWebAvs;
import ch.globaz.orion.ws.enums.Role;
import ch.globaz.orion.ws.exceptions.WebAvsException;
import ch.globaz.orion.ws.service.UtilsService;

@WebService(endpointInterface = "ch.globaz.orion.ws.comptabilite.WebAvsComptabiliteService")
public class WebAvsComptabiliteServiceImpl implements WebAvsComptabiliteService {

    private static final String MONTANT_ZERO = "0.00";
    private static final String LANGUE_DEFAUT = "fr";

    private static Logger logger = LoggerFactory.getLogger(WebAvsComptabiliteServiceImpl.class);

    @Override
    public List<ApercuCompteAnnexe> listerApercuCompteAnnexe(String numeroAffilie, String langue) {
        boolean wantExcludeCompteAnnexeAffiliePersonnel = false;
        try {
            wantExcludeCompteAnnexeAffiliePersonnel = EBProperties.ECL_EXCLURE_CA_AFFILIE_PERSONNEL.getBooleanValue();
        } catch (PropertiesException e) {
            throw new RuntimeException("The properties ECL_EXCLURE_CA_AFFILIE_PERSONNEL has not be found", e);
        }

        if (JadeStringUtil.isBlankOrZero(numeroAffilie)) {
            throw new IllegalArgumentException("numero affilie cannot be blank or zero");
        }

        if (JadeStringUtil.isBlankOrZero(langue)) {
            langue = LANGUE_DEFAUT;
        }

        List<ApercuCompteAnnexe> listeApercuCompteAnnexe = new ArrayList<ApercuCompteAnnexe>();

        // Récupération d'une session
        BSession session = UtilsService.initSession();

        // récupération des comptes annexes pour l'affilié
        CACompteAnnexeManager compteAnnexeManager = new CACompteAnnexeManager();
        compteAnnexeManager.setSession(session);
        compteAnnexeManager.setForIdExterneRole(numeroAffilie);
        try {
            compteAnnexeManager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            JadeLogger.error("unable to find compteAnnexe for affilie : " + numeroAffilie, e);
        }

        for (int i = 0; i < compteAnnexeManager.size(); i++) {
            CACompteAnnexe compteAnnexe = (CACompteAnnexe) compteAnnexeManager.get(i);
            String solde = compteAnnexe.getSolde();
            FWCurrency soldeCurrency = new FWCurrency(solde);
            ApercuCompteAnnexe apercuCompteAnnexe = new ApercuCompteAnnexe(compteAnnexe.getIdCompteAnnexe(),
                    compteAnnexe.getRole().getDescription(langue), soldeCurrency.toStringFormat());

            // si on veut exclure les comptes annexes avec le rôle "affilié personnel" on vérifie le type de rôle avant
            // d'ajouter
            if (wantExcludeCompteAnnexeAffiliePersonnel) {
                // si role est différent de "affilié personnel" on l'ajoute
                if (!IntRole.ROLE_AFFILIE_PERSONNEL.equals(compteAnnexe.getRole().getIdRole())) {
                    listeApercuCompteAnnexe.add(apercuCompteAnnexe);
                }
            } else {
                listeApercuCompteAnnexe.add(apercuCompteAnnexe);
            }

        }

        return listeApercuCompteAnnexe;
    }

    @Override
    public List<ApercuCompteAnnexe> listerApercuCompteAnnexeInd(String numeroAffilie, String langue) {
        if (JadeStringUtil.isBlankOrZero(numeroAffilie)) {
            throw new IllegalArgumentException("numero affilie cannot be blank or zero");
        }

        if (JadeStringUtil.isBlankOrZero(langue)) {
            langue = LANGUE_DEFAUT;
        }

        List<ApercuCompteAnnexe> listeApercuCompteAnnexe = new ArrayList<ApercuCompteAnnexe>();

        // Récupération d'une session
        BSession session = UtilsService.initSession();

        // récupération des comptes annexes indépendant pour l'affilié
        CACompteAnnexeManager compteAnnexeManager = searchComptesAnnexes(numeroAffilie, Role.INDEPENDANT, session);

        for (int i = 0; i < compteAnnexeManager.size(); i++) {
            CACompteAnnexe compteAnnexe = (CACompteAnnexe) compteAnnexeManager.get(i);
            String solde = compteAnnexe.getSolde();
            FWCurrency soldeCurrency = new FWCurrency(solde);
            ApercuCompteAnnexe apercuCompteAnnexe = new ApercuCompteAnnexe(compteAnnexe.getIdCompteAnnexe(),
                    compteAnnexe.getRole().getDescription(langue), soldeCurrency.toStringFormat());
            listeApercuCompteAnnexe.add(apercuCompteAnnexe);
        }

        return listeApercuCompteAnnexe;
    }

    @Override
    public String genererExtraitCompteAnnexe(String idCompteAnnexe, String dateDebut, String dateFin, String langue) {
        if (JadeStringUtil.isEmpty(idCompteAnnexe)) {
            throw new IllegalArgumentException("idCompteAnnexe is null or empty");
        }

        CAProcessImpressionExtraitCompteAnnexe processImpressionExtraitCompteAnnexe = null;

        // si la date du jour est vide on met la date du jour
        if (JadeStringUtil.isBlankOrZero(dateFin)) {
            Date dateDuJour = new Date();
            dateFin = dateDuJour.getSwissValue();
        }

        try {
            processImpressionExtraitCompteAnnexe = new CAProcessImpressionExtraitCompteAnnexe();
            processImpressionExtraitCompteAnnexe.setEbusinessMode(true);
            processImpressionExtraitCompteAnnexe.setForIdCompteAnnexe(idCompteAnnexe);
            processImpressionExtraitCompteAnnexe.setFromDate(dateDebut);
            processImpressionExtraitCompteAnnexe.setUntilDate(dateFin);
            processImpressionExtraitCompteAnnexe.setPrintLanguageFromScreen(langue.toUpperCase());
            processImpressionExtraitCompteAnnexe.executeProcess();
            return processImpressionExtraitCompteAnnexe.getDocLocation();
        } catch (Exception e) {
            JadeLogger.error("Unabled to generate file extraitCompte for idCompteAnnexe : " + idCompteAnnexe, e);
        }
        return processImpressionExtraitCompteAnnexe.getDocLocation();
    }

    @Override
    public byte[] downloadFile(String filepath) {
        byte[] byteFile = null;
        File docFile = new File(filepath);
        try {
            byteFile = FileUtils.readFileToByteArray(docFile);
        } catch (IOException e) {
            JadeLogger.error("Unabled to download file : " + filepath, e);
        }

        return byteFile;
    }

    @Override
    public FactureResultSearch searchFactures(String forNumeroAffilie, FactureStatut statut, String forDateDebut,
            String forDateFin, String forDateDebutEcheance, String forDateFinEcheance, Role forRole, int from, int nb,
            FactureOrderBy orderBy, OrderByDirWebAvs orderByDir) throws WebAvsException {

        if (JadeStringUtil.isBlankOrZero(forNumeroAffilie)) {
            throw new IllegalArgumentException("forNumeroAffilie cannot be blank or zero");
        }

        // Récupération d'une session
        BSession session = UtilsService.initSession();

        // récupération des comptes annexes pour l'affilié
        CACompteAnnexeManager compteAnnexeManager = searchComptesAnnexes(forNumeroAffilie, forRole, session);

        // récupération des sections relatives au compte annexe
        List<Facture> facturesList = new ArrayList<Facture>();

        Long nbAllRows = 0L;
        Long nbMatchingRows = 0L;
        for (int i = 0; i < compteAnnexeManager.size(); i++) {
            CACompteAnnexe compteAnnexe = (CACompteAnnexe) compteAnnexeManager.get(i);
            CASectionManager sectionManager = new CASectionManager();
            sectionManager.setSession(session);
            sectionManager.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
            sectionManager.setOrderBy(buildOrderKey(orderBy, orderByDir));
            sectionManager.changeManagerSize(BManager.SIZE_NOLIMIT);

            try {
                // récupération du nombre total de factures
                nbAllRows += sectionManager.getCount();

                // application des filtres
                if (statut != null) {
                    if (FactureStatut.OUVERTE.equals(statut)) {
                        sectionManager.setForSoldeNot(MONTANT_ZERO);
                    } else if (FactureStatut.SOLDEE.equals(statut)) {
                        sectionManager.setForSolde(MONTANT_ZERO);
                    }
                }

                if (!JadeStringUtil.isEmpty(forDateDebut)) {
                    sectionManager.setFromDate(forDateDebut);
                }
                if (!JadeStringUtil.isEmpty(forDateFin)) {
                    sectionManager.setUntilDate(forDateFin);
                }

                if (!JadeStringUtil.isEmpty(forDateDebutEcheance)) {
                    sectionManager.setFromDateEcheance(forDateDebutEcheance);
                }
                if (!JadeStringUtil.isEmpty(forDateFinEcheance)) {
                    sectionManager.setUntilDateEcheance(forDateFinEcheance);
                }

                // exécution de la requête finale
                sectionManager.find(BManager.SIZE_NOLIMIT);
                nbMatchingRows += sectionManager.getSize();

                // parcours des sections et création des factures pour EBusiness
                if (sectionManager.size() > 0) {
                    List<CASection> sectionsList = sectionManager.toList();
                    int nbMatchingsRowsInt = nbMatchingRows.intValue();
                    int fromIndex = from > nbMatchingsRowsInt ? nbMatchingsRowsInt : from;
                    int toIndex = (from + nb) > nbMatchingsRowsInt ? nbMatchingsRowsInt : (from + nb);
                    sectionsList = sectionsList.subList(fromIndex, toIndex);
                    for (CASection section : sectionsList) {
                        Facture facture = createFactureFromSection(section);
                        facturesList.add(facture);
                    }
                }
            } catch (Exception e) {
                throw new WebAvsException("unable to find factures", e);
            }

        }

        return new FactureResultSearch(facturesList, nbMatchingRows, nbAllRows);
    }

    @Override
    public Facture readFacture(Integer idFacture) throws WebAvsException {
        if (idFacture == null) {
            throw new IllegalArgumentException("idFacture cannot be null");
        }

        Facture facture = null;

        // Récupération d'une session
        BSession session = UtilsService.initSession();

        // récupération de la section
        CASection section = new CASection();
        section.setSession(session);
        section.setIdSection(idFacture.toString());
        try {
            section.retrieve();

            facture = createFactureFromSection(section);
        } catch (Exception e) {
            throw new WebAvsException("unable to retrieve facture for id " + idFacture, e);
        }

        return facture;
    }

    @Override
    public InfosGeneralesFacturation retrieveInfosGeneralesFacturation(String forNumeroAffilie, Role forRole)
            throws WebAvsException {
        if (JadeStringUtil.isEmpty(forNumeroAffilie)) {
            throw new IllegalArgumentException("forNumeroAffilie is null or empty");
        }

        Integer nbFacturesOuvertes = 0;
        Integer nbFacturesEchues = 0;
        BigDecimal soldeOuvert = new BigDecimal(0);
        BigDecimal soldeEchu = new BigDecimal(0);

        Date today = Date.now();
        Date yesterday = today.addDays(-1);

        // Récupération d'une session
        BSession session = UtilsService.initSession();

        // récupération des comptes annexes pour l'affilié
        CACompteAnnexeManager compteAnnexeManager = searchComptesAnnexes(forNumeroAffilie, forRole, session);
        for (int i = 0; i < compteAnnexeManager.size(); i++) {
            CACompteAnnexe compteAnnexe = (CACompteAnnexe) compteAnnexeManager.get(i);

            // récupération des sections ouvertes
            CASectionManager sectionsOuvertesManager = new CASectionManager();
            sectionsOuvertesManager.setSession(session);
            sectionsOuvertesManager.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
            sectionsOuvertesManager.setForSoldeNot(MONTANT_ZERO);
            try {
                sectionsOuvertesManager.find(BManager.SIZE_NOLIMIT);
                nbFacturesOuvertes += sectionsOuvertesManager.size();
                List<CASection> sectionsOuvertesList = sectionsOuvertesManager.toList();

                // calcul du solde total ouvert
                for (CASection sectionOuverte : sectionsOuvertesList) {
                    soldeOuvert = soldeOuvert.add(new BigDecimal(sectionOuverte.getSolde()).setScale(2,
                            RoundingMode.UNNECESSARY));
                }
            } catch (Exception e) {
                throw new WebAvsException("unable to count factures ouvertes", e);
            }

            // récupération des sections échues
            CASectionManager sectionsEchuesManager = new CASectionManager();
            sectionsEchuesManager.setSession(session);
            sectionsEchuesManager.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
            sectionsEchuesManager.setForSoldeNot(MONTANT_ZERO);
            sectionsEchuesManager.setUntilDateEcheance(yesterday.toString());
            try {
                sectionsEchuesManager.find(BManager.SIZE_NOLIMIT);
                nbFacturesEchues += sectionsEchuesManager.size();
                List<CASection> sectionsEchuesList = sectionsEchuesManager.toList();

                // calcul du solde total échu
                for (CASection sectionEchue : sectionsEchuesList) {
                    soldeEchu = soldeEchu.add(new BigDecimal(sectionEchue.getSolde()).setScale(2,
                            RoundingMode.UNNECESSARY));
                }
            } catch (Exception e) {
                throw new WebAvsException("unable to count factures ouvertes", e);
            }
        }
        return new InfosGeneralesFacturation(nbFacturesOuvertes, nbFacturesEchues, soldeOuvert, soldeEchu);
    }

    @Override
    public String genererFacture(String idFacture, String langue) throws WebAvsException {
        if (JadeStringUtil.isEmpty(idFacture)) {
            throw new IllegalArgumentException("idFacture is null or empty");
        }

        BSession session = UtilsService.initSession();

        // recherche de la section
        CASection section = new CASection();
        section.setSession(session);
        section.setIdSection(idFacture);
        try {
            section.retrieve();
        } catch (Exception e) {
            throw new WebAvsException("unable to retrieve facture for id " + idFacture, e);
        }

        // recherche du passage de facturation
        if (JadeStringUtil.isEmpty(section.getIdJournal())) {
            throw new WebAvsException("unable to find passage facturation");
        }
        FAPassageManager passageManager = new FAPassageManager();
        passageManager.setSession(session);
        passageManager.setForIdJournal(section.getIdJournal());
        try {
            passageManager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new WebAvsException("unable to retrieve passage facturation for facture id " + idFacture, e);
        }

        if (passageManager.size() == 0) {
            throw new WebAvsException("no facture founded " + idFacture);
        } else if (passageManager.size() > 1) {
            throw new WebAvsException("more than one passage founded " + idFacture);
        }

        FAPassage passage = (FAPassage) passageManager.getFirstEntity();
        String numeroAffilie = section.getCompteAnnexe().getIdExterneRole();
        String idPassage = passage.getIdPassage();
        return printFacture(numeroAffilie, idPassage);
    }

    private String printFacture(String numeroAffilie, String idPassage) throws WebAvsException {
        try {
            FAImpressionFactureProcess impressionFactureProcess = new FAImpressionFactureProcess();
            impressionFactureProcess.setSession(initSessionMusca());
            impressionFactureProcess.setIdPassage(idPassage);
            impressionFactureProcess.setFromIdExterneRole(numeroAffilie);
            impressionFactureProcess.setTillIdExterneRole(numeroAffilie);
            impressionFactureProcess.setDateImpression(JACalendar.today().toStr("."));
            impressionFactureProcess.setEnvoyerGed(false);
            impressionFactureProcess.setSendCompletionMail(false);
            impressionFactureProcess.setEMailAddress("xxx@globaz.ch");
            impressionFactureProcess.setModeFonctionnement(ModeFonctionnementEnum.STANDARD);
            impressionFactureProcess.setEbusinessMode(true);
            impressionFactureProcess.executeProcess();

            if (impressionFactureProcess.hasAttachedDocuments()) {
                JadePublishDocument document = (JadePublishDocument) impressionFactureProcess.getAttachedDocuments()
                        .get(0);
                return document.getDocumentLocation();
            } else {
                throw new IllegalStateException("no attached documents");
            }
        } catch (Exception e) {
            throw new WebAvsException("unable to print facture");
        }
    }

    private static Facture createFactureFromSection(CASection section) throws WebAvsException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Facture facture = new Facture();
        facture.setIdFacture(Integer.parseInt(section.getIdSection()));
        try {
            facture.setDate(sdf.parse(section.getDateSection()));
            facture.setDateEcheance(sdf.parse(section.getDateEcheance()));
        } catch (Exception e) {
            throw new WebAvsException("unable to parse date", e);
        }
        facture.setDescription(section.getDescription());
        facture.setMontant(new BigDecimal(section.getBase()).setScale(2, RoundingMode.UNNECESSARY));
        facture.setNumeroFacture(section.getIdExterne());

        if (StringUtils.isEmpty(section.getReferenceBvr())) {
            try {
                // On regénère la référence BVR
                ReferenceBVR refBvr = new ReferenceBVR();
                refBvr.setBVR(section, section.getSolde());
                facture.setReferenceBvr(refBvr.getLigneReference());
            } catch (Exception e) {
                throw new WebAvsException("unable to get reference BVR", e);
            }
        } else {
            facture.setReferenceBvr(section.getReferenceBvr());
        }
        facture.setRemarque(section.getResumeContentieux());
        facture.setSolde(new BigDecimal(section.getSolde()).setScale(2, RoundingMode.UNNECESSARY));
        facture.setStatut(definirStatutFacture(section.getSolde()));

        int nbJoursRetard = 0;
        if (!MONTANT_ZERO.equals(section.getSolde())) {
            nbJoursRetard = calculerNbJoursRetard(section.getDateEcheance());
        }
        facture.setNbJoursRetard(nbJoursRetard);
        return facture;
    }

    private CACompteAnnexeManager searchComptesAnnexes(String forNumeroAffilie, Role forRole, BSession session) {
        if (JadeStringUtil.isEmpty(forNumeroAffilie)) {
            throw new IllegalArgumentException("forNumeroAffilie is null or empty");
        }

        CACompteAnnexeManager compteAnnexeManager = new CACompteAnnexeManager();
        compteAnnexeManager.setSession(session);
        compteAnnexeManager.setForIdExterneRole(forNumeroAffilie);
        String role;
        switch (forRole) {
            case EMPLOYEUR:
                role = IntRole.ROLE_AFFILIE_PARITAIRE;
                break;
            case INDEPENDANT:
                role = IntRole.ROLE_AFFILIE_PERSONNEL;
                break;
            case AF:
                role = IntRole.ROLE_AF;
                break;
            default:
                throw new IllegalArgumentException("invalid role " + forRole);
        }
        compteAnnexeManager.setForSelectionRole(role);

        try {
            compteAnnexeManager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            JadeLogger.error("Unable to find compteAnnexe for affilie : " + forNumeroAffilie, e);
        }
        return compteAnnexeManager;
    }

    private static FactureStatut definirStatutFacture(String solde) {
        if (MONTANT_ZERO.equals(solde)) {
            return FactureStatut.SOLDEE;
        } else {
            return FactureStatut.OUVERTE;
        }
    }

    private static Integer calculerNbJoursRetard(String dateEcheance) {
        if (!JadeDateUtil.isGlobazDate(dateEcheance)) {
            throw new IllegalArgumentException("dateEchance is not valid");
        }

        if (JadeDateUtil.isDateAfter(JACalendar.today().toStr("."), dateEcheance)) {
            JACalendar cal = new JACalendarGregorian();
            try {
                return (int) cal.daysBetween(dateEcheance, JACalendar.today().toStr("."));
            } catch (JAException e) {
                throw new IllegalStateException("unable to define daysBetween " + dateEcheance + " and today");
            }
        } else {
            return 0;
        }
    }

    private String buildOrderKey(FactureOrderBy orderBy, OrderByDirWebAvs orderByDir) {
        StringBuilder orderKey = new StringBuilder();
        switch (orderBy) {
            case ORDER_BY_DATE:
                if (orderByDir.equals(OrderByDirWebAvs.ASC)) {
                    orderKey.append(CASectionManager.ORDER_DATE);
                } else {
                    orderKey.append(CASectionManager.ORDER_DATE_DESCEND);
                }
                break;
            case ORDER_BY_NO_FACTURE:
                if (orderByDir.equals(OrderByDirWebAvs.ASC)) {
                    orderKey.append(CASectionManager.ORDER_IDEXTERNE_DATE_ASC);
                } else {
                    orderKey.append(CASectionManager.ORDER_IDEXTERNE_DESCEND);
                }

                break;
            default:
                throw new IllegalArgumentException("order by not allowed" + orderBy);
        }

        return orderKey.toString();
    }

    /**
     * Initialise une session MUSCA
     * 
     * @return
     */
    private static BSession initSessionMusca() {
        BSession session = null;

        try {
            session = (BSession) GlobazServer.getCurrentSystem().getApplication("MUSCA").newSession();
            session._connectAnonymous();
        } catch (Exception e) {
            JadeLogger.error("An error happened while getting a new session!", e);
        }

        return session;
    }
}
