package globaz.tucana.administration;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.tucana.application.TUApplication;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.db.bouclement.access.TUBouclement;
import globaz.tucana.db.bouclement.access.TUBouclementManager;
import globaz.tucana.db.bouclement.access.TUDetail;
import globaz.tucana.db.bouclement.access.TUDetailManager;
import globaz.tucana.db.bouclement.access.TUNoPassage;
import globaz.tucana.db.bouclement.access.TUNoPassageManager;
import globaz.tucana.exception.TUException;
import globaz.tucana.exception.fw.TUFWDeleteException;
import globaz.tucana.exception.fw.TUFWFindException;
import globaz.tucana.exception.fw.TUFWRetrieveException;
import globaz.tucana.exception.fw.TUFWUpdateException;
import globaz.tucana.exception.process.TUProcessNoRecordFound;
import globaz.tucana.process.message.TUMessagesContainer;
import java.rmi.RemoteException;

/**
 * Permet d'intervenir sur un passage
 * 
 * @author fgo date de création : 6 juil. 06
 * @version : version 1.0
 * 
 */
public class TUSuppressionNoPassage {

    /**
     * Mise à jour de la date d'état du bouclement
     * 
     * @param transaction
     * @param idBouclement
     * @throws TUProcessNoRecordFound
     * @throws TUFWUpdateException
     */
    private static void majEntete(BTransaction transaction, String idBouclement) throws TUProcessNoRecordFound,
            TUFWUpdateException {
        TUBouclement bouclement = new TUBouclement();
        bouclement.setSession(transaction.getSession());
        bouclement.setIdBouclement(idBouclement);
        try {
            bouclement.retrieve();
        } catch (Exception e) {
            throw new TUProcessNoRecordFound("TUSuppressionNoPassage.majEntete()",
                    "Aucune entête bouclement trouvée pour le numéro de bouclement : " + idBouclement);
        }
        if (!bouclement.isNew()) {
            bouclement.setDateEtat(JACalendar.todayJJsMMsAAAA());
            try {
                bouclement.update();
            } catch (Exception e) {
                throw new TUFWUpdateException(
                        "TUSuppressionNoPassage.majEntete() - Impossible de mettre à jour le bouclement : "
                                + idBouclement, e);
            }
        }
    }

    /**
     * Renvoie le bouclement concerné par le numéro de passage
     * 
     * @param transaction
     * @param idBouclement
     * @return
     */
    private static String rechercheBouclement(BTransaction transaction, String idBouclement)
            throws TUFWRetrieveException, TUProcessNoRecordFound {
        TUBouclementManager bouclementManager = new TUBouclementManager();
        bouclementManager.setSession(transaction.getSession());
        bouclementManager.setForIdBouclement(idBouclement);
        bouclementManager.setForCsEtat(ITUCSConstantes.CS_ETAT_ENCOURS);
        try {
            BSession sessionTmp = new BSession();
            sessionTmp.setApplication(TUApplication.DEFAULT_APPLICATION_TUCANA);
            bouclementManager.setForCsAgence(sessionTmp.getApplication().getProperty(TUApplication.CS_AGENCE));
            // bouclementManager.setForCsAgence(transaction.getSession().getApplication().getProperty(TUApplication.CS_AGENCE));
            bouclementManager.find();
        } catch (RemoteException e) {
            throw new TUFWRetrieveException("TUSuppressionNoPassage.rechercheBouclement()", e);
        } catch (Exception e) {
            throw new TUFWRetrieveException("TUSuppressionNoPassage.rechercheBouclement()", e);
        } catch (ExceptionInInitializerError e) {
            throw new TUFWRetrieveException("TUSuppressionNoPassage.rechercheBouclement()", e);
        }
        if (bouclementManager.isEmpty()) {
            throw new TUProcessNoRecordFound("TUSuppressionNoPassage.rechercheBouclement()", transaction.getSession()
                    .getLabel("ERR_AUCUN_BOUCLEMENT_TROUVE") + " : " + idBouclement);
        }
        return idBouclement;
    }

    /**
     * Recherche les enregistrements lien no passage application
     * 
     * @param transaction
     * @param csApplication
     * @param noPassage
     * @return
     */
    private static TUNoPassageManager recherchePassageManager(BTransaction transaction, String csApplication,
            String noPassage) throws TUFWFindException, TUProcessNoRecordFound {

        TUNoPassageManager passageManager = new TUNoPassageManager();
        passageManager.setSession(transaction.getSession());
        passageManager.setForCsApplication(csApplication);
        passageManager.setForNoPassage(noPassage);

        try {
            passageManager.find(transaction, BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new TUFWFindException("TUSuppressionNoPassage.recherchePassageManager()",
                    passageManager.getCurrentSqlQuery(), e);
        }
        if (passageManager.size() == 0) {
            throw new TUProcessNoRecordFound("TUSuppressionNoPassage.recherchePassageManager()",
                    "Aucun enregistrement trouvé pour le numéro de passage : " + noPassage);
        }

        TUNoPassage passage = (TUNoPassage) passageManager.getEntity(0);

        TUNoPassageManager passageManager2 = new TUNoPassageManager();
        passageManager2.setSession(transaction.getSession());
        passageManager2.setForCsApplication(csApplication);
        passageManager2.setForNoPassage(noPassage);
        passageManager2.setForIdBouclement(passage.getIdBouclement());

        try {
            passageManager2.find(transaction, BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new TUFWFindException("TUSuppressionNoPassage.recherchePassageManager()",
                    passageManager2.getCurrentSqlQuery(), e);
        }
        if (passageManager2.size() == 0) {
            throw new TUProcessNoRecordFound("TUSuppressionNoPassage.recherchePassageManager()",
                    "Aucun enregistrement trouvé pour le numéro de passage : " + noPassage);
        }

        return passageManager2;
    }

    /**
     * Permet la suppression d'un numéro de passage
     * 
     * @param transaction
     * @param csApplication
     * @param noPassage
     * @param messages
     * @throws TUProcessNoRecordFound
     * @throws TUException
     */
    public static void suppression(BTransaction transaction, String csApplication, String noPassage,
            TUMessagesContainer messages) throws TUProcessNoRecordFound, TUException {
        try {
            suppressionPassage(transaction, csApplication, noPassage, messages);
        } catch (TUFWFindException e) {
            messages.addMessage("TUSuppressionNoPassage.suppression() : " + e.toString(), FWMessage.ERREUR, "Erreur");
            throw e;
        } catch (TUFWDeleteException e) {
            messages.addMessage("TUSuppressionNoPassage.suppression() : " + e.toString(), FWMessage.ERREUR, "Erreur");
            throw e;
        } catch (TUFWRetrieveException e) {
            messages.addMessage("TUSuppressionNoPassage.suppression() : " + e.toString(), FWMessage.ERREUR, "Erreur");
            throw e;
        }
    }

    /**
     * Suppression du détail du bouclement en fonction du numéro de passage
     * 
     * @param transaction
     * @param idBouclement
     * @param csApplication
     * @throws TUFWFindException
     * @throws TUProcessNoRecordFound
     * @throws TUFWDeleteException
     * @throws TUFWUpdateException
     */
    private static void suppressionDetail(BTransaction transaction, String idBouclement, String csApplication)
            throws TUFWFindException, TUProcessNoRecordFound, TUFWDeleteException, TUFWUpdateException {
        TUDetailManager detailManager = new TUDetailManager();
        TUDetail detail = null;

        detailManager.setSession(transaction.getSession());
        detailManager.setForIdBouclement(idBouclement);
        detailManager.setForCsApplication(csApplication);
        try {
            detailManager.find(transaction, BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new TUFWFindException("TUSuppressionNoPassage.suppressionDetail()",
                    detailManager.getCurrentSqlQuery(), e);
        }

        if (detailManager.size() == 0) {
            throw new TUProcessNoRecordFound("TUSuppressionNoPassage.suppressionDetail()",
                    "Aucun détail de bouclement trouvé pour le numéro de bouclement : " + idBouclement);
        }
        for (int i = 0; i < detailManager.size(); i++) {
            detail = (TUDetail) detailManager.getEntity(i);
            try {
                detail.delete(transaction);
            } catch (Exception e) {
                throw new TUFWDeleteException("TUSuppressionNoPassage.suppressionDetail()", e);
            }
        }
        // mise à jour de la date d'état du bouclement
        majEntete(transaction, idBouclement);
    }

    /**
     * Supprime l'enregistrement de lien entre bouclement et n° passage
     * 
     * @param transaction
     * @param csApplication
     * @param noPassage
     * @param messages
     * @throws TUProcessNoRecordFound
     */
    private static void suppressionPassage(BTransaction transaction, String csApplication, String noPassage,
            TUMessagesContainer messages) throws TUFWFindException, TUFWDeleteException, TUFWRetrieveException,
            TUProcessNoRecordFound {
        // Recherche le lien numéro de passage
        TUNoPassageManager passageManager = null;
        try {
            passageManager = recherchePassageManager(transaction, csApplication, noPassage);
        } catch (TUProcessNoRecordFound e1) {
            messages.addMessage("TUSuppressionNoPassage.suppressionPassage() : " + e1.toString(),
                    FWMessage.INFORMATION, "Information");
        }
        // Lecture du manager pour traitement
        for (int i = 0; passageManager != null && i < passageManager.size(); i++) {
            TUNoPassage passage = (TUNoPassage) passageManager.getEntity(i);
            try {
                // suppression des enregistrements de détail et mise à jour de
                // la date d'état du bouclement
                suppressionDetail(transaction, rechercheBouclement(transaction, passage.getIdBouclement()),
                        passage.getCsApplication());

                passage.delete(transaction);

            } catch (TUProcessNoRecordFound e1) {
                messages.addMessage("TUSuppressionNoPassage.suppressionPassage() : " + e1.toString(),
                        FWMessage.INFORMATION, "Information");
                throw new TUProcessNoRecordFound("TUSuppressionNoPassage.suppressionPassage()", e1);
            } catch (TUFWUpdateException e) {
                messages.addMessage("TUSuppressionNoPassage.suppressionPassage() : " + e.toString(),
                        FWMessage.INFORMATION, "Information");
            } catch (Exception e) {
                throw new TUFWDeleteException("TUSuppressionNoPassage.suppressionLienBouclementPassage()", e);
            }
        }
    }

    /**
     * Constructeur
     */
    public TUSuppressionNoPassage() {
        super();
    }
}
