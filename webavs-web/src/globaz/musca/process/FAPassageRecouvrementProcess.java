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
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.api.IFAEnteteFacture;
import globaz.musca.api.IFAPassage;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APISectionDescriptor;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.translation.CACodeSystem;
import globaz.pyxis.api.osiris.TITiersOSI;
import java.util.Collection;
import java.util.Iterator;

/**
 * Génération de la facturation. Date de création : (25.11.2002 11:52:37)
 * 
 * @author: BTC
 */
public class FAPassageRecouvrementProcess extends FAGenericProcess {

    private static final long serialVersionUID = -6704498740235200188L;

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public FAPassageRecouvrementProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public FAPassageRecouvrementProcess(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public FAPassageRecouvrementProcess(globaz.globall.db.BSession session) {
        super(session);
    }

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
        ;
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
        boolean estTraite = _executeRecouvrementProcess(passage);

        // finaliser le passage (le déverrouiller)
        if (!_finalizePassageSetState(passage, FAPassage.CS_ETAT_TRAITEMENT)) {
            abort();
            return false;
        }
        return estTraite;
    }

    public boolean _executeRecouvrementProcess(IFAPassage passage) {
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

        String errorMessage = updateEnteteForRecouvrement(passage, getSession(), getTransaction(), "", "");
        if (!JadeStringUtil.isEmpty(errorMessage)) {
            getMemoryLog().logMessage(errorMessage, FWMessage.ERREUR, "");
            succes = false;
        }

        return succes;
    }

    /**
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

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public String updateEnteteForRecouvrement(IFAPassage passage, BSession session, BTransaction transaction,
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
            // PO 7274: sélectionner les facture positives ou celle ayant le montant max
            if (app.isModeReporterMontantMinimal()) {
                entManager.setForTotalFactureBigger(app.getMontantMinimeMax());
            } else {
                entManager.setForTotalFactureBigger(app.getMontantMinimePos());
            }
            // Inforom 169 : on exclut les catégries 34 et 35. Pas de LSV possible pour ces décomptes.
            entManager.setNotInIdSousType(APISection.ID_CATEGORIE_SECTION_DIVIDENDE + ", "
                    + APISection.ID_CATEGORIE_SECTION_ICI);

            // Commit les validation faites par d'autres modules,
            transaction.commit();

            statement = entManager.cursorOpen(transaction);
            FAEnteteFacture entFacture = null;
            boolean contentieux;
            // itérer sur toutes les entêtes de factures
            int compt = 0;
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
                    contentieux = false;

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
                        // Rembourser si l'adresse de paiment existe
                        String idPaiement = "";
                        try {
                            idPaiement = entFacture.getTiersIdAdressePaiement(transaction,
                                    passage.getDateFacturation(), TITiersOSI.DOMAINE_RECOUVREMENT);
                        } catch (Exception e) {
                            // l'id de la facture n'a pas pu être retrouvé...
                        }

                        if (!JadeStringUtil.isBlankOrZero(idPaiement)) {
                            entFacture.setIdAdressePaiement(idPaiement);

                            // si l'affilié possède un compte annexe avec un
                            // motif de contentieux contentieux actif

                            CACompteAnnexe compteAnnexe = FAGenericProcess.getCompteAnnexe(entFacture, session,
                                    transaction);
                            if (compteAnnexe.isASurveiller().booleanValue()
                                    || compteAnnexe.isMotifExistant(CACodeSystem.CS_IRRECOUVRABLE)) {
                                entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_RETENU_COMPTE_ANNEX_BLOQUE);
                            } else if (compteAnnexe.getSoldeToCurrency().isPositive()) {
                                Collection<?> sections = compteAnnexe.propositionCompensation(
                                        APICompteAnnexe.PC_TYPE_MONTANT, APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN,
                                        entFacture.getTotalFacture());
                                Iterator<?> it = sections.iterator();
                                while (it.hasNext()) {
                                    APISection sec = (APISection) it.next();
                                    if ((sec.getContentieuxEstSuspendu().booleanValue() && FAGenericProcess
                                            .hasSursisNotRentier(sec, session)) || sec.isSectionAuContentieux()) {
                                        entFacture
                                                .setIdModeRecouvrement(FAEnteteFacture.CS_MODE_RETENU_COMPTE_ANNEX_BLOQUE);
                                        contentieux = true;
                                        break;
                                    }
                                }
                                if (!contentieux) {
                                    // mettre l'entete en mode remboursement
                                    entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT);
                                }
                            } else {
                                // mettre l'entete en mode remboursement
                                entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT);
                            }

                            try {
                                entFacture.update(transaction);
                            } catch (Exception e) {
                                errorMessage = "Problème de remise à zéro de l'adresse de paiement. " + e.getMessage();
                            }
                        } else {
                            entFacture.setIdAdressePaiement("");
                            entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_AUTOMATIQUE);
                            try {
                                entFacture.update(transaction);
                            } catch (Exception e) {
                                errorMessage = "Problème de remise à zéro de l'adresse de paiement. " + e.getMessage();
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

            transaction.addErrors("FAPassageRecouvrementProcess: " + e.getMessage());

        }
        // test du passage
        return errorMessage;

    }
}
