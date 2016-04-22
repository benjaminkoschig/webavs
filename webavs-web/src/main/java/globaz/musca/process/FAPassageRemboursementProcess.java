package globaz.musca.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.api.IFAEnteteFacture;
import globaz.musca.api.IFAPassage;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APISectionDescriptor;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.exceptions.CATechnicalException;
import globaz.osiris.translation.CACodeSystem;
import globaz.pyxis.api.osiris.TITiersOSI;
import java.util.Collection;
import java.util.Iterator;
import ch.globaz.common.domaine.Date;
import com.google.common.base.Preconditions;

/**
 * Génération de la facturation. Date de création : (25.11.2002 11:52:37)
 * 
 * @author: BTC
 */
public class FAPassageRemboursementProcess extends FAGenericProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public FAPassageRemboursementProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public FAPassageRemboursementProcess(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public FAPassageRemboursementProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    /*
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51) @auteur: BTC @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        // prendre le passage en cours;
        passage = new FAPassage();
        passage.setIdPassage(getIdPassage());
        passage.setSession(getSession());
        try {
            passage.retrieve(getTransaction());
        } catch (Exception e) {
            getMemoryLog().logMessage("Impossible de retourner le passage: " + e.getMessage(),
                    FWViewBeanInterface.ERROR, passage.getClass().getName());
        }
        // Vérifier si le passage a les critères de validité pour une impression
        if (!_passageIsValid(passage)) {
            abort();
            return false;
        } // initialiser le passage
        if (!_initializePassage(passage)) {
            abort();
            return false;
        }
        // Exécuter l'impression
        // ----------------------------------------------------------------
        boolean estTraite = _executeRemboursementProcess(passage);

        // finaliser le passage (le déverrouiller)
        if (!_finalizePassageSetState(passage, FAPassage.CS_ETAT_TRAITEMENT)) {
            abort();
            return false;
        }
        return estTraite;
    }

    public boolean _executeRemboursementProcess(IFAPassage passage) {
        boolean succes = true;
        setState(getSession().getLabel("PROCESSSTATE_TRAITEMENT_PASSAGE"));

        if ((passage == null) || passage.isNew()) {
            passage = new FAPassage();
            passage.setIdPassage(getIdPassage());
            passage.setISession(getSession());
            try {
                passage.retrieve(getTransaction());
                this.setPassage((FAPassage) passage);
            } catch (Exception e) {
                getMemoryLog().logMessage("Impossible de retourner le passage: " + e.getMessage(),
                        FWViewBeanInterface.ERROR, passage.getClass().getName());
            }
            ;
        }

        String errorMessage = updateEnteteForRemboursement(passage, getSession(), getTransaction(), "", "");
        if (!JadeStringUtil.isEmpty(errorMessage)) {
            getMemoryLog().logMessage(errorMessage, FWMessage.ERREUR, "");
            succes = false;
        }

        return succes;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2003 09:08:01)
     * 
     * @param passage
     *            le passage à comptabilisé
     * @param entity
     *            contenant les afacts
     * @return la date de l'écriture
     */
    public String _getDateForEcriture(IFAPassage passage, IFAEnteteFacture entity) {
        FAApplication application = null;
        // Si mode de remboursement, c'est la date de facturation qui fait foi
        if (FAEnteteFacture.CS_MODE_REMBOURSEMENT.equalsIgnoreCase(entity.getIdModeRecouvrement())
                || FAEnteteFacture.CS_MODE_FORCEE_REMBOURSEMENT.equalsIgnoreCase(entity.getIdModeRecouvrement())
                || (new FWCurrency(entity.getTotalFacture()).isNegative())) {
            return passage.getDateFacturation();
            // Sinon en mode de recouvrement, c'est la date d'échéance qui fait
            // foi
        } else {
            try {
                application = (FAApplication) GlobazServer.getCurrentSystem().getApplication(
                        getSession().getApplicationId());
                APISectionDescriptor sectionDescriptor = application.getSectionDescriptor(getSession());
                // initialiser le section descriptor avec les paramètres de
                // l'entête de facture
                sectionDescriptor.setSection(entity.getIdExterneFacture(), entity.getIdTypeFacture(),
                        entity.getIdSousType(), passage.getDateFacturation(), "", "");
                return sectionDescriptor.getDateEcheanceLegale();
            } catch (Exception e) {
                return null;
            }
        }
    }

    protected void fixIdForModeRecouvrement(CACompteAnnexe compteAnnexe, IFAPassage passage,
            FAEnteteFacture entFacture, Collection<?> sections, BSession session) throws Exception {

        // par defaut on rembourse.
        entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_REMBOURSEMENT);

        if (compteAnnexe != null && !compteAnnexe.isNew()) {
            Date dateFacturation = new Date(passage.getDateFacturation());
            String annee = dateFacturation.getAnnee();

            // Si il est au contentieux => on bloque le remboursement
            if (isCAContentieux(compteAnnexe, annee)) {
                entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_RETENU_COMPTE_ANNEX_BLOQUE);
            } else if (compteAnnexe.getSoldeToCurrency().isPositive()) {
                Iterator<?> it = sections.iterator();

                // Si section au contentieux, on bloque le remboursement
                while (it.hasNext()) {
                    CASection section = (CASection) it.next();
                    if (isSectionEchue(section, passage, session)) {
                        if (isSectionContentieux(section)) {
                            entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_RETENU_COMPTE_ANNEX_BLOQUE);
                            break;
                        }
                    }
                }
            }
        }

    }

    protected String giveDomaineForAdressePaiement() {
        return TITiersOSI.DOMAINE_REMBOURSEMENT;
    }

    protected boolean isCAContentieux(CACompteAnnexe compteAnnexe, String annee) throws CATechnicalException {
        Preconditions.checkNotNull(annee, "The parameters [annee] can't be null");

        if (compteAnnexe.isASurveiller().booleanValue() || compteAnnexe.isMotifExistant(CACodeSystem.CS_RENTIER)
                || compteAnnexe.isMotifExistant(CACodeSystem.CS_IRRECOUVRABLE) || compteAnnexe.isEnFaillte(annee)) {
            return true;
        }
        return false;
    }

    protected boolean isSectionContentieux(CASection section) throws CATechnicalException {
        if (section.hasMotifContentieux(CACodeSystem.CS_RENTIER)
                || section.hasMotifContentieux(CACodeSystem.CS_IRRECOUVRABLE) || section.isSectionAuxPoursuites(true)
                || !JadeStringUtil.isBlankOrZero(section.getIdPlanRecouvrement())) {
            return true;
        }
        return false;
    }

    protected boolean isSectionEchue(CASection section, IFAPassage passage, BSession session) throws Exception {
        // Integer dateFactu = new Integer(passage.getDateFacturation());
        // Integer dateEcheanceSection = new Integer(section.getDateEcheance());
        JADate dateEcheanceSection = new JADate(section.getDateEcheance());
        JADate dateFactu = new JADate(passage.getDateFacturation());
        JACalendar cal = session.getApplication().getCalendar(); // new
        // JAGregorianCalendar
        if (cal.compare(dateFactu, dateEcheanceSection) == JACalendar.COMPARE_FIRSTUPPER) {
            String dateExecutionEtape = section._getDateExecutionSommation();
            if (JadeStringUtil.isBlank(dateExecutionEtape)) {
                return false;
            } else {
                JADate dateSommation = new JADate(dateExecutionEtape);
                dateSommation = cal.addDays(dateSommation, 15);
                // dateSommation = CADateUtil.getDateOuvrable(dateSommation);
                return (cal.compare(dateFactu, dateSommation) == JACalendar.COMPARE_FIRSTUPPER);
            }
        }
        return false;

    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    public String updateEnteteForRemboursement(IFAPassage passage, BSession session, BTransaction transaction,
            String fromNumAffilie, String tillNumAffilie) {
        String errorMessage = "";
        BStatement statement = null;
        FAEnteteFactureManager entManager = new FAEnteteFactureManager();

        try {
            FAApplication app = (FAApplication) GlobazServer.getCurrentSystem().getApplication(
                    FAApplication.DEFAULT_APPLICATION_MUSCA);
            entManager.setSession(session);
            entManager.setForIdPassage(passage.getIdPassage());
            if (!JadeStringUtil.isEmpty(fromNumAffilie)) {
                entManager.setFromIdExterneRole(fromNumAffilie);
            }
            if (!JadeStringUtil.isEmpty(tillNumAffilie)) {
                entManager.setForTillIdExterneRole(tillNumAffilie);
            }

            // sélectionner toutes les facture négatives jusqu'au/sans les
            // montants minimes
            entManager.setForTillTotalFacture(app.getMontantMinimeNeg());

            // Commit les validation faites par d'autres modules,
            transaction.commit();

            statement = entManager.cursorOpen(transaction);
            FAEnteteFacture entFacture = null;
            int compt = 0;
            // itérer sur toutes les entêtes de factures
            while ((entFacture = (FAEnteteFacture) entManager.cursorReadNext(statement)) != null) {
                compt++;
                setProgressDescription(entFacture.getIdExterneRole() + " <br>" + compt + "/" + entManager.size()
                        + "<br>");
                if (isAborted()) {
                    setProgressDescription("Traitement interrompu<br> sur l'affilié : " + entFacture.getIdExterneRole()
                            + " <br>" + compt + "/" + entManager.size() + "<br>");
                    if ((getParent() != null) && getParent().isAborted()) {
                        getParent().setProcessDescription(
                                "Traitement interrompu<br> sur l'affilié : " + entFacture.getIdExterneRole() + " <br>"
                                        + compt + "/" + entManager.size() + "<br>");
                    }
                    break;
                } else {

                    // MODE INACTIF?
                    if (FAEnteteFacture.CS_MODE_RETENU.equals(entFacture.getIdModeRecouvrement())) {
                        // mettre à blanc la zone idAdressePaiement
                        entFacture.setIdAdressePaiement("");
                        try {
                            entFacture.update(transaction);
                        } catch (Exception e) {
                            errorMessage = "Problème de remise à zéro de l'adresse de paiement. " + e.getMessage();
                        }
                    } else {
                        if (!FAEnteteFacture.CS_MODE_FORCEE_REMBOURSEMENT.equals(entFacture.getIdModeRecouvrement())) {
                            // Rembourser si l'adresse de paiment existe

                            String idPaiement = "";
                            try {
                                idPaiement = entFacture.getTiersIdAdressePaiement(transaction,
                                        passage.getDateFacturation(), giveDomaineForAdressePaiement());
                            } catch (Exception e) {
                                // l'id de la facture n'a pas pu être
                                // retrouvé...
                            }

                            if (!JadeStringUtil.isBlankOrZero(idPaiement)) {
                                entFacture.setIdAdressePaiement(idPaiement);

                                // si l'affilié possède un compte annexe avec un
                                // motif de contentieux contentieux actif

                                CACompteAnnexe compteAnnexe = FAGenericProcess.getCompteAnnexe(entFacture, session,
                                        transaction);
                                Collection<?> sections = compteAnnexe
                                        .getListeSections(APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN);

                                fixIdForModeRecouvrement(compteAnnexe, passage, entFacture, sections, session);

                                // if
                                // (compteAnnexe.isASurveiller().booleanValue()
                                // ||
                                // compteAnnexe.isMotifExistant(CACodeSystem.CS_IRRECOUVRABLE))
                                // {
                                // entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_RETENU_COMPTE_ANNEX_BLOQUE);
                                // } else if
                                // (compteAnnexe.getSoldeToCurrency().isPositive())
                                // {
                                // Collection sections =
                                // compteAnnexe.propositionCompensation(CACompteAnnexe.PC_TYPE_MONTANT,
                                // APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN,
                                // entFacture.getTotalFacture());
                                // Iterator it = sections.iterator();
                                // while (it.hasNext()) {
                                // APISection sec = (APISection) it.next();
                                // if
                                // ((sec.getContentieuxEstSuspendu().booleanValue()
                                // && hasSursisNotRentier(sec,session)) ||
                                // sec.isSectionAuContentieux()) {
                                // entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_RETENU_COMPTE_ANNEX_BLOQUE);
                                // contentieux = true;
                                // break;
                                // }
                                // }
                                // if (!contentieux) {
                                // // mettre l'entete en mode remboursement
                                // entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_REMBOURSEMENT);
                                // }
                                // } else {
                                // // mettre l'entete en mode remboursement
                                // entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_REMBOURSEMENT);
                                // }

                                try {
                                    entFacture.update(transaction);
                                } catch (Exception e) {
                                    errorMessage = "Problème de remise à zéro de l'adresse de paiement. "
                                            + e.getMessage();
                                }
                            } else {
                                // mettre à blanc la zone idAdressePaiement
                                entFacture.setIdAdressePaiement("");
                                entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_AUTOMATIQUE);
                                try {
                                    entFacture.update(transaction);
                                } catch (Exception e) {
                                    errorMessage = "Problème de remise à zéro de l'adresse de paiement. "
                                            + e.getMessage();
                                }
                            }
                        }
                    }
                }

            }
            transaction.commit();
        } catch (Exception e) {

            try {
                transaction.rollback();
                entManager.cursorClose(statement);
            } catch (Exception e1) {
            }
            if (JadeStringUtil.isEmpty(errorMessage)) {
                errorMessage = e.getMessage();
            } else {
                errorMessage = errorMessage + " : " + e.getMessage();
            }

            transaction.addErrors("FAPassageRemboursementProcess: " + e.getMessage());

        }
        // test du passage
        return errorMessage;

    }
}
