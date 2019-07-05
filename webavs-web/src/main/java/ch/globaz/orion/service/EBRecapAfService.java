package ch.globaz.orion.service;

import globaz.al.process.dossiers.ALRadiationDossiersFromEbuProcess;
import globaz.framework.controller.FWController;
import globaz.framework.db.postit.FWNoteP;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang.StringUtils;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.models.dossier.CommentaireModel;
import ch.globaz.al.business.models.dossier.CommentaireSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.common.codesystem.CodeSystemUtils;
import ch.globaz.orion.business.models.af.MotifChangementAfEnum;
import ch.globaz.orion.business.models.af.UniteTempsEnum;
import ch.globaz.orion.businessimpl.services.af.AfServiceImpl;

public class EBRecapAfService {
    private static String NOTE_TABLESOURCE = "globaz.al.vb.dossier.ALDossierViewBean";
    private static String USER = "user";
    private static String PERIODE = "periode";
    private static String NB_UNITETRAVAIL = "nbUniteTravail";
    private static String UNITE_TRAVAIL = "uniteTravail";
    private static String MOTIF_CHANGEMENT = "motifChangement";
    private static String DATE_DEBUT_CHANGEMENT = "dateDebutChangement";
    private static String DATE_FIN_CHANGEMENT = "dateFinChangement";
    private static String REMARQUE_DETAIL = "remarqueDetail";
    private static String ID_DOSSIER_AF = "idDossierAf";
    private static String ID_LIGNE_RECAP = "idLigneRecapDetail";
    private static String NUMERO_AFFILIE = "numeroAffilie";
    private static String TEXTE_LIBRE = "texteLibre";
    private static String ID_SELECTED = "selectedId";

    private EBRecapAfService() {

    }

    public static void cloturerRecapAf(HttpSession session, HttpServletRequest request) throws Exception {
        BSession bsession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        AfServiceImpl.cloturerRecapAfById(bsession, request.getParameter(ID_SELECTED));
    }

    public static void validerLigneRecapAf(HttpSession session, HttpServletRequest request) throws Exception {

        BSession bsession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        // Renseignement du bloc note AF
        buildDescriptionNote(request, bsession);

        // Mise à jour du statut de la ligne dans Ebusiness
        updateLigneRecapAfEbu(request, bsession);
    }

    public static void validerRadierLigneRecapAf(HttpSession session, HttpServletRequest request) throws Exception {
        BSession bsession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        try {
            DossierComplexModel dossierAl = findDossierAlComplex(request, bsession);
            if (ALCSDossier.ETAT_RADIE.equals(dossierAl.getDossierModel().getEtatDossier())) {
                throw new Exception("Le dossier est déjà radié");
            }

            // Renseignement du bloc note AF
            buildDescriptionNote(request, bsession);
            // Mention du texte dans l'écran AL0016 Gestion des décision
            ajoutTexteMotifDecision(request, dossierAl, bsession);

            ALRadiationDossiersFromEbuProcess processRadiation = new ALRadiationDossiersFromEbuProcess();
            processRadiation.setDateRadiation(request.getParameter(DATE_FIN_CHANGEMENT));
            processRadiation.setPeriode(request.getParameter(PERIODE));
            processRadiation.setDossier(dossierAl);
            processRadiation.setDateImpression(JadeDateUtil.getGlobazFormattedDate(new Date()));
            processRadiation.setPrintDecisions(true);
            processRadiation.setGED(true);
            processRadiation.setEmail(bsession.getUserEMail());
            processRadiation.setSession(bsession);
            if (!bsession.hasErrors()) {
                processRadiation.run();
            }

        } catch (Exception e) {
            throw new Exception("Impossible de radier le dossier \n" + e.getMessage(), e);
        }

        if (bsession.hasErrors()) {
            throw new Exception("Impossible de radier le dossier" + bsession.getErrors().toString());
        }

        // Mise à jour du statut de la ligne dans Ebusiness
        try {
            updateLigneRecapAfEbu(request, bsession);
        } catch (Exception e) {
            throw new Exception("Impossible de mettre à jour la ligne de récap dans l'EBusiness", e);
        }
    }

    private static void ajoutTexteMotifDecision(HttpServletRequest request, DossierComplexModel dossierAl,
            BSession session) throws JadeApplicationException, JadePersistenceException {
        String motifChangement = request.getParameter(MOTIF_CHANGEMENT);
        if (JadeStringUtil.isEmpty(motifChangement)) {
            JadeThread.logError(EBRecapAfService.class.getName(),
                    "al.droit.droitModel.motifFin.codesystemIntegrity.type");
            return;
        }

        CommentaireSearchModel commentaireSearch = new CommentaireSearchModel();

        commentaireSearch.setForIdDossier(dossierAl.getDossierModel().getIdDossier());
        commentaireSearch.setForTypeCommentaire(ALCSDossier.COMMENTAIRE_TYPE_DECISION);
        commentaireSearch = ALImplServiceLocator.getCommentaireModelService().search(commentaireSearch);

        CommentaireModel commentaire;

        if (commentaireSearch.getSize() != 0) {
            commentaire = (CommentaireModel) commentaireSearch.getSearchResults()[0];
        } else {
            commentaire = new CommentaireModel();
            commentaire.setType(ALCSDossier.COMMENTAIRE_TYPE_DECISION);
            commentaire.setIdDossier(dossierAl.getDossierModel().getIdDossier());
        }

        // déterminer la langue à utiliser
        String langue = defineLangue(dossierAl);

        String texte = defineTextForMotif(request.getParameter(MOTIF_CHANGEMENT), langue, session);
        if (!JadeStringUtil.isEmpty(request.getParameter(TEXTE_LIBRE))) {
            texte += "\r\n" + request.getParameter(TEXTE_LIBRE);
        }

        commentaire.setTexte(texte);

        if (commentaire.isNew()) {
            ALServiceLocator.getCommentaireModelService().create(commentaire);
        } else {
            ALServiceLocator.getCommentaireModelService().update(commentaire);
        }

    }

    private static String defineLangue(DossierComplexModel dossierAl) throws JadeApplicationException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        String langue;
        // si on veut la même langue que l'affilié
        if (dossierAl.getAllocataireComplexModel().getAllocataireModel().getLangueAffilie()) {
            langue = ALServiceLocator.getLangueAllocAffilieService().langueTiersAffilie(
                    dossierAl.getDossierModel().getNumeroAffilie());
        }
        // langue allocataire
        else {
            langue = ALServiceLocator.getLangueAllocAffilieService().langueTiersAlloc(
                    dossierAl.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(),
                    dossierAl.getDossierModel().getNumeroAffilie());
        }
        return langue;
    }

    private static String defineTextForMotif(String motif, String langue, BSession session) {
        String text;

        if (MotifChangementAfEnum.ACCIDENT.name().equals(motif)) {
            text = CodeSystemUtils.searchCodeSystemTraduction(MotifChangementAfEnum.ACCIDENT.getCs(), session, langue)
                    .getTraduction();
        } else if (MotifChangementAfEnum.MALADIE.name().equals(motif)) {
            text = CodeSystemUtils.searchCodeSystemTraduction(MotifChangementAfEnum.MALADIE.getCs(), session, langue)
                    .getTraduction();
        } else if (MotifChangementAfEnum.CONGE_MATERNITE.name().equals(motif)) {
            text = CodeSystemUtils.searchCodeSystemTraduction(MotifChangementAfEnum.CONGE_MATERNITE.getCs(), session,
                    langue).getTraduction();
        } else if (MotifChangementAfEnum.CONGE_NON_PAYE.name().equals(motif)) {
            text = CodeSystemUtils.searchCodeSystemTraduction(MotifChangementAfEnum.CONGE_NON_PAYE.getCs(), session,
                    langue).getTraduction();
        } else if (MotifChangementAfEnum.VACANCES.name().equals(motif)) {
            text = CodeSystemUtils.searchCodeSystemTraduction(MotifChangementAfEnum.VACANCES.getCs(), session, langue)
                    .getTraduction();
        } else if (MotifChangementAfEnum.FIN_ACTIVITE.name().equals(motif)) {
            text = CodeSystemUtils.searchCodeSystemTraduction(MotifChangementAfEnum.FIN_ACTIVITE.getCs(), session,
                    langue).getTraduction();
        } else if (MotifChangementAfEnum.DECES.name().equals(motif)) {
            text = CodeSystemUtils.searchCodeSystemTraduction(MotifChangementAfEnum.DECES.getCs(), session, langue)
                    .getTraduction();
        } else {
            text = "";
        }

        return text;
    }

    private static void buildDescriptionNote(HttpServletRequest request, BSession session) throws Exception {
        String today = JadeDateUtil.getGlobazFormattedDate(new Date());

        String user = request.getParameter(USER);
        String periode = request.getParameter(PERIODE);
        String nbUniteTravail = request.getParameter(NB_UNITETRAVAIL);
        String uniteTravail = request.getParameter(UNITE_TRAVAIL);
        String motif = request.getParameter(MOTIF_CHANGEMENT);
        String dateDebutChangement = request.getParameter(DATE_DEBUT_CHANGEMENT);
        String dateFinChangement = request.getParameter(DATE_FIN_CHANGEMENT);
        String remarque = request.getParameter(REMARQUE_DETAIL);
        String idDossierAf = request.getParameter(ID_DOSSIER_AF);

        if (JadeStringUtil.isEmpty(uniteTravail)) {
            throw new IllegalArgumentException("L'unité de travail doit être renseignée");
        }

        StringBuilder sbDescription = new StringBuilder();

        sbDescription.append("Récap ").append(periode);

        if (motif != "MOIS") {
            String uniteTravailLabel = session.getLabel(UniteTempsEnum.valueOf(uniteTravail).getLabel());
            sbDescription.append(" ").append(nbUniteTravail).append(" ").append(uniteTravailLabel).append(",");
        }
        if (!StringUtils.isEmpty(motif)) {
            String motifLabel = session.getLabel(MotifChangementAfEnum.valueOf(motif).getLabel());
            sbDescription.append(" Motif : ").append(motifLabel).append(",");
        }
        if (!StringUtils.isEmpty(dateDebutChangement)) {
            sbDescription.append(" Date de début : ").append(dateDebutChangement);
        }
        if (!StringUtils.isEmpty(dateFinChangement)) {
            sbDescription.append(" Date de fin : ").append(dateFinChangement);
        }
        if (!StringUtils.isEmpty(remarque)) {
            sbDescription.append(" Remarque : ").append(remarque);
        }

        // Renseignement du bloc note AF
        FWNoteP note = new FWNoteP();

        note.setSession(session);
        note.setDescription(sbDescription.toString());
        note.setUser(user);
        note.setDateCreation(today);
        note.setSourceId(idDossierAf);
        note.setMemo("");
        note.setTableSource(NOTE_TABLESOURCE);

        note.add();
    }

    private static void updateLigneRecapAfEbu(HttpServletRequest request, BSession session)
            throws DatatypeConfigurationException {
        String idLigneRecap = request.getParameter(ID_LIGNE_RECAP);
        String nbUniteTravail = request.getParameter(NB_UNITETRAVAIL);
        String uniteTravail = request.getParameter(UNITE_TRAVAIL);
        String motif = request.getParameter(MOTIF_CHANGEMENT);
        String dateDebutChangement = request.getParameter(DATE_DEBUT_CHANGEMENT);
        String dateFinChangement = request.getParameter(DATE_FIN_CHANGEMENT);
        String remarque = request.getParameter(REMARQUE_DETAIL);

        if (JadeStringUtil.isEmpty(remarque)) {
            remarque = null;
        }

        Integer nbUniteTravailInt = null;
        XMLGregorianCalendar dateDebutXMLGregCal = null;
        XMLGregorianCalendar dateFinXMLGregCal = null;

        if (!StringUtils.isEmpty(nbUniteTravail)) {
            nbUniteTravailInt = Integer.valueOf(nbUniteTravail);
        }

        if (!StringUtils.isEmpty(dateDebutChangement)) {
            GregorianCalendar dateDebutGregCal = new GregorianCalendar();
            Date dateDebut = JadeDateUtil.getGlobazDate(dateDebutChangement);
            dateDebutGregCal.setTime(dateDebut);
            dateDebutXMLGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateDebutGregCal);
        }

        if (!StringUtils.isEmpty(dateFinChangement)) {
            GregorianCalendar dateFinGregCal = new GregorianCalendar();
            Date dateFin = JadeDateUtil.getGlobazDate(dateFinChangement);
            dateFinGregCal.setTime(dateFin);
            dateFinXMLGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateFinGregCal);
        }

        AfServiceImpl.updateStatutLigneRecapToTreatedByCaisse(session, idLigneRecap, nbUniteTravailInt, uniteTravail,
                motif, dateDebutXMLGregCal, dateFinXMLGregCal, remarque);
    }

    private static DossierComplexModel findDossierAlComplex(HttpServletRequest request, BSession session)
            throws JadeApplicationException, JadePersistenceException {
        DossierComplexSearchModel search = new DossierComplexSearchModel();
        search.setForIdDossier(request.getParameter(ID_DOSSIER_AF));
        search.setLikeNumeroAffilie(request.getParameter(NUMERO_AFFILIE));

        JadePersistenceManager.search(search);

        if (search.getSize() == 1) {
            return (DossierComplexModel) search.getSearchResults()[0];
        } else {
            return null;
        }
    }
}
