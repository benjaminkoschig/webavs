package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;

public class AnnonceRPHandlerFactory {
    /**
     * Retourne une instance du handler qui devra gérer le traitement de l'annonce de type RECEPTION
     * 
     * @param message L'annonce sedex
     * 
     * @return instance du handler
     * 
     * @throws AnnonceSedexException
     *             Exception levée si aucun builder ne correspond au type de l'annonce passée en paramètre
     */
    public static AnnonceHandlerAbstract getAnnonceHandler(Object message) throws JadeApplicationException {
        if (message instanceof ch.gdk_cds.xmlns.pv_5201_000101._3.Message) {
            // -------------- Nouvelle décision -----------------
            ch.gdk_cds.xmlns.pv_5201_000101._3.Message decree = (ch.gdk_cds.xmlns.pv_5201_000101._3.Message) message;
            return new AnnonceDecreeHandler(decree);
        } else if (message instanceof ch.gdk_cds.xmlns.pv_5211_000102._3.Message) {
            // -------------- Confirmation d'une décision -----------------
            ch.gdk_cds.xmlns.pv_5211_000102._3.Message decreeConfirmation = (ch.gdk_cds.xmlns.pv_5211_000102._3.Message) message;
            return new AnnonceDecreeConfirmationHandler(decreeConfirmation);
        } else if (message instanceof ch.gdk_cds.xmlns.pv_5211_000103._3.Message) {
            // -------------- Rejet d'une décision -----------------
            ch.gdk_cds.xmlns.pv_5211_000103._3.Message decreeReject = (ch.gdk_cds.xmlns.pv_5211_000103._3.Message) message;
            return new AnnonceDecreeRejectHandler(decreeReject);
        } else if (message instanceof ch.gdk_cds.xmlns.pv_5201_000201._3.Message) {
            // -------------- Interruption -----------------
            ch.gdk_cds.xmlns.pv_5201_000201._3.Message stop = (ch.gdk_cds.xmlns.pv_5201_000201._3.Message) message;
            return new AnnonceStopHandler(stop);
        } else if (message instanceof ch.gdk_cds.xmlns.pv_5211_000202._3.Message) {
            // -------------- Confirmation d'une interruption -----------------
            ch.gdk_cds.xmlns.pv_5211_000202._3.Message stopConfirmation = (ch.gdk_cds.xmlns.pv_5211_000202._3.Message) message;
            return new AnnonceStopConfirmationHandler(stopConfirmation);
        } else if (message instanceof ch.gdk_cds.xmlns.pv_5211_000203._3.Message) {
            // -------------- Rejet d'une interruption ---
            // -------------
            ch.gdk_cds.xmlns.pv_5211_000203._3.Message stopReject = (ch.gdk_cds.xmlns.pv_5211_000203._3.Message) message;
            return new AnnonceStopRejectHandler(stopReject);
        } else if (message instanceof ch.gdk_cds.xmlns.pv_5211_000301._3.Message) {
            // -------------- Mutation du rapport d'assurance -----------------
            ch.gdk_cds.xmlns.pv_5211_000301._3.Message mutation = (ch.gdk_cds.xmlns.pv_5211_000301._3.Message) message;
            return new AnnonceMutationHandler(mutation);
        } else if (message instanceof ch.gdk_cds.xmlns.pv_5202_000401._3.Message) {
            // -------------- Demande du rapport d'assurance -----------------
            ch.gdk_cds.xmlns.pv_5202_000401._3.Message insuranceQuery = (ch.gdk_cds.xmlns.pv_5202_000401._3.Message) message;
            return new AnnonceInsuranceQueryHandler(insuranceQuery);
        } else if (message instanceof ch.gdk_cds.xmlns.pv_5212_000402._3.Message) {
            // -------------- Réponse du rapport d'assurance -----------------
            ch.gdk_cds.xmlns.pv_5212_000402._3.Message insuranceQueryResult = (ch.gdk_cds.xmlns.pv_5212_000402._3.Message) message;
            return new AnnonceInsuranceQueryResultHandler(insuranceQueryResult);
        } else if (message instanceof ch.gdk_cds.xmlns.pv_5203_000501._3.Message) {
            // -------------- Etat des décisions -----------------
            ch.gdk_cds.xmlns.pv_5203_000501._3.Message decreeInventory = (ch.gdk_cds.xmlns.pv_5203_000501._3.Message) message;
            return new AnnonceDecreeInventoryHandler(decreeInventory);
        } else if (message instanceof ch.gdk_cds.xmlns.pv_5213_000601._3.Message) {
            // -------------- Effectif des assurés -----------------
            ch.gdk_cds.xmlns.pv_5213_000601._3.Message insuranceInventory = (ch.gdk_cds.xmlns.pv_5213_000601._3.Message) message;
            return new AnnonceInsuranceInventoryHandler(insuranceInventory);
        } else if (message instanceof ch.gdk_cds.xmlns.pv_5214_000701._3.Message) {
            // -------------- Décompte annuel -----------------
            ch.gdk_cds.xmlns.pv_5214_000701._3.Message statement = (ch.gdk_cds.xmlns.pv_5214_000701._3.Message) message;
            return new AnnonceStatementHandler(statement);
        } else if (message instanceof ch.gdk_cds.xmlns.pv_5215_000802._3.Message) {
            // -------------- Reponse prime tarifaire -----------------
            ch.gdk_cds.xmlns.pv_5215_000802._3.Message statement = (ch.gdk_cds.xmlns.pv_5215_000802._3.Message) message;
            return new AnnoncePremiumQueryResultHandler(statement);
        }else {
            throw new AnnonceSedexException("AnnonceHandlerFactory.getAnnonceHandler : Unknown type !");
        }
    }
}
