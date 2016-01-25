package globaz.tucana.administration;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.db.bouclement.access.TUBouclement;
import globaz.tucana.db.bouclement.access.TUBouclementManager;
import globaz.tucana.db.bouclement.access.TUNoPassageManager;
import globaz.tucana.exception.TUException;
import globaz.tucana.exception.fw.TUFWCountException;
import globaz.tucana.exception.fw.TUFWException;
import globaz.tucana.exception.fw.TUFWFindException;
import globaz.tucana.exception.fw.TUFWRetrieveException;
import globaz.tucana.process.message.TUMessagesContainer;

/**
 * Classe permettant la validation d'un bouclement
 * 
 * @author fgo
 */
public class TUValidationBouclement {

    private static int NOMBRE_APPLICATION = 4;

    /**
     * Chargement du détail manager en fonction du bouclement
     * 
     * @param bouclement
     * @return
     * @throws TUFWFindException
     */
    // private static TUDetailManager chargeDetailManager(TUBouclement
    // bouclement) throws TUFWFindException {
    // TUDetailManager manager = new TUDetailManager();
    // manager.setSession(bouclement.getSession());
    // manager.setForIdBouclement(bouclement.getIdBouclement());
    // try {
    // manager.find();
    // } catch (Exception e) {
    // throw new
    // TUFWFindException("TUValidationBouclement.chargeDetailManager()",manager.getCurrentSqlQuery(),
    // e);
    // }
    // return manager;
    // }
    /**
     * Recherche le bouclement en fonction du mois et de l'année et/ou de l'idBouclement
     * 
     * @param transaction
     * @param idBouclement
     * @param annee
     * @param mois
     * @return le premier bouclement trouvé (le seul)
     * @throws TUFWException
     */
    private static TUBouclement rechercheBouclement(BTransaction transaction, String idBouclement, String annee,
            String mois) throws TUFWFindException, TUFWException {
        if (JadeStringUtil.isBlankOrZero(idBouclement)) {
            TUBouclementManager manager = new TUBouclementManager();
            manager.setSession(transaction.getSession());
            manager.setForAnneeComptable(annee);
            manager.setForMoisComptable(mois);
            manager.setForCsEtat(ITUCSConstantes.CS_ETAT_ENCOURS);
            try {
                manager.find();
            } catch (Exception e) {
                throw new TUFWFindException("TUValidationBouclement.rechercheBouclement()",
                        manager.getCurrentSqlQuery(), e);
            }
            if (manager.isEmpty()) {
                throw new TUFWFindException("TUValidationBouclement.rechercheBouclement()",
                        manager.getCurrentSqlQuery(), transaction.getSession().getLabel("ERR_AUCUN_BOUCLEMENT_ENCOURS"));
            }
            return (TUBouclement) manager.getFirstEntity();
        } else {
            TUBouclement entity = new TUBouclement();
            entity.setSession(transaction.getSession());
            entity.setIdBouclement(idBouclement);
            try {
                entity.retrieve();
            } catch (Exception e) {
                throw new TUFWRetrieveException("TUValidationBouclement.rechercheBouclement()", e);
            }
            if (entity.isNew()) {
                throw new TUFWRetrieveException("TUValidationBouclement.rechercheBouclement()");
            }
            return entity;
        }
    }

    /**
     * @param bouclement
     * @param messages
     * @param validOk
     * @return
     * @throws TUFWCountException
     */
    private static boolean testApplication(TUBouclement bouclement, TUMessagesContainer messages, boolean validOk)
            throws TUFWCountException {
        TUNoPassageManager manager = new TUNoPassageManager();
        manager.setSession(bouclement.getSession());
        manager.setForIdBouclement(bouclement.getIdBouclement());
        try {
            if (manager.getCount() < NOMBRE_APPLICATION) {
                messages.addMessage("TUValidationBouclement.validationBouclement()", FWMessage.AVERTISSEMENT,
                        bouclement.getSession().getLabel("ERR_PASSAGE_NON_IMPORTEE"));
                validOk = false;
            }
        } catch (Exception e) {
            throw new TUFWCountException("TUValidationBouclement.validationBouclement()", manager.getCurrentSqlQuery(),
                    e);
        }
        return validOk;
    }

    /**
     * Validation d'un bouclement
     * 
     * @param transaction
     * @param idBouclement
     * @param annee
     * @param mois
     * @param messages
     * @return
     * @throws TUException
     */
    public static TUBouclement validation(BTransaction transaction, String idBouclement, String annee, String mois,
            TUMessagesContainer messages) throws TUException {
        return validationBouclement(transaction, idBouclement, annee, mois, messages);

    }

    /**
     * Validation du bouclement
     * 
     * @param transaction
     * @param idBouclement
     * @param annee
     * @param mois
     * @param messages
     * @return
     * @throws TUFWException
     */
    private static TUBouclement validationBouclement(BTransaction transaction, String idBouclement, String annee,
            String mois, TUMessagesContainer messages) throws TUFWException {
        TUBouclement bouclement = rechercheBouclement(transaction, idBouclement, annee, mois);
        // test la validité du bouclement
        return validationBouclement(bouclement, messages) ? bouclement : null;
    }

    /**
     * Règles de gestion pour la validation d'un bouclement
     * 
     * @param bouclement
     * @param messages
     * @throws TUFWFindException
     */
    private static boolean validationBouclement(TUBouclement bouclement, TUMessagesContainer messages)
            throws TUFWFindException {
        boolean validOk = true;
        // teste que les 4 applications soient chargées
        validOk = testApplication(bouclement, messages, validOk);

        // teste le détail
        return validationDetail(bouclement, validOk, messages);
    }

    /**
     * Contrôle des règles de gestion pour la validation d'un bouclement
     * 
     * @param bouclement
     * @param validOk
     * @param messages
     * @return
     * @throws TUFWFindException
     */
    private static boolean validationDetail(TUBouclement bouclement, boolean validOk, TUMessagesContainer messages)
            throws TUFWFindException {
        // chargement du manager de détail
        // TUDetailManager manager = chargeDetailManager(bouclement);

        return validOk;
    }

    /**
     * Constructeur
     */
    public TUValidationBouclement() {
        super();
    }

}
