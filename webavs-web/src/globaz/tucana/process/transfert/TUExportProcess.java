package globaz.tucana.process.transfert;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.db.bouclement.access.TUBouclement;
import globaz.tucana.db.bouclement.access.TUBouclementManager;
import globaz.tucana.exception.fw.TUFWException;
import globaz.tucana.exception.fw.TUFWFindException;
import globaz.tucana.exception.fw.TUFWRetrieveException;
import globaz.tucana.exception.fw.TUFWUpdateException;
import globaz.tucana.exception.process.TUCreationBouclementSuivantException;
import globaz.tucana.exception.process.TUProcessException;
import globaz.tucana.exception.process.TUProcessNoRecordFound;
import globaz.tucana.transaction.TUTransactionHandler;
import globaz.tucana.transfert.TUExportHandler;

/**
 * Process pour l'exportation du bordereau
 * 
 * @author fgo date de création : 25.06.2006
 * @version : version 1.0
 */
public class TUExportProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = "";
    private String idBouclement = "";
    private String mois = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        boolean succeed = true;
        TUTransactionHandler transactionHandler = new TUTransactionHandler(getSession()) {
            @Override
            protected void handleBean(BTransaction transaction) throws Exception {
                if (process(transaction)) {
                    ;
                }
                transaction.commit();
            }
        };

        try {
            transactionHandler.execute();
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "process", e);
            succeed = false;
        }
        return succeed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setSendCompletionMail(true);
        setSendMailOnError(true);
        setControleTransaction(true);

    }

    /**
     * Change le statut du bouclement en exporté et met à jour la date d'état
     * 
     * @param transaction
     * @return le bouclement
     * @throws TUProcessNoRecordFound
     * @throws TUFWException
     */
    private TUBouclement changeStatutBouclement(BTransaction transaction) throws TUProcessNoRecordFound, TUFWException {
        TUBouclement entity = null;
        if (JadeStringUtil.isBlankOrZero(getIdBouclement())) {
            TUBouclementManager manager = new TUBouclementManager();
            manager.setSession(transaction.getSession());
            manager.setForCsEtat(ITUCSConstantes.CS_ETAT_BOUCLE);
            manager.setForAnneeComptable(getAnnee());
            manager.setForMoisComptable(getMois());
            try {
                manager.find(transaction);
                if (manager.isEmpty()) {
                    throw new TUProcessNoRecordFound(this.getClass().getName() + ".changeStatutBouclement()",
                            transaction.getSession().getLabel("ERR_AUCUN_BOUCLEMENT_BOUCLE"));
                }
                entity = (TUBouclement) manager.getFirstEntity();
            } catch (Exception e) {
                throw new TUFWFindException(this.getClass().getName() + ".changeStatutBouclement()",
                        manager.getCurrentSqlQuery(), e);
            }

        } else {
            // chargement de l'entête si idBouclement existe
            entity = new TUBouclement();
            entity.setSession(transaction.getSession());
            entity.setIdBouclement(getIdBouclement());
            try {
                entity.retrieve();
            } catch (Exception e) {
                throw new TUFWRetrieveException(this.getClass().getName() + ".changeStatutBouclement()", e);
            }
            if (entity.isNew()) {
                throw new TUFWRetrieveException(this.getClass().getName() + ".changeStatutBouclement()");
            }
        }
        entity.setDateEtat(JACalendar.todayJJsMMsAAAA());
        entity.setCsEtat(ITUCSConstantes.CS_ETAT_EXPORTE);
        try {
            entity.update(transaction);
        } catch (Exception e) {
            throw new TUFWUpdateException(this.getClass().getName() + ".changeStatutBouclement()", e);
        }
        return entity;
    }

    private void creationBouclementSuivant(BTransaction transaction, TUBouclement bouclement)
            throws TUCreationBouclementSuivantException {
        JADate myDate;
        try {
            myDate = new JADate("01".concat(JadeStringUtil.fillWithZeroes(bouclement.getMoisComptable(), 2)).concat(
                    bouclement.getAnneeComptable()));
        } catch (JAException e) {
            throw new TUCreationBouclementSuivantException(this.getClass().getName()
                    .concat("creationBouclementSuivant() : Problème lors de la création de la date"), e);
        }
        JACalendarGregorian calendar = new JACalendarGregorian();
        myDate = calendar.addMonths(myDate, 1);

        TUBouclementManager manager = new TUBouclementManager();
        manager.setSession(bouclement.getSession());
        manager.setForMoisComptable(Integer.toString(myDate.getMonth()));
        manager.setForAnneeComptable(Integer.toString(myDate.getYear()));
        manager.setForCsAgence(bouclement.getCsAgence());

        try {
            if (manager.getCount() == 0) {

                TUBouclement entity = new TUBouclement();
                entity.setSession(bouclement.getSession());
                entity.setAnneeComptable(Integer.toString(myDate.getYear()));
                entity.setMoisComptable(Integer.toString(myDate.getMonth()));
                entity.setCsAgence(bouclement.getCsAgence());
                entity.setCsEtat(ITUCSConstantes.CS_ETAT_ENCOURS);
                entity.setDateCreation(JACalendar.todayJJsMMsAAAA());
                entity.setDateEtat(JACalendar.todayJJsMMsAAAA());
                try {
                    entity.add(transaction);
                } catch (Exception e) {
                    throw new TUCreationBouclementSuivantException(this.getClass().getName()
                            .concat("creationBouclementSuivant() : problème lors de l'ajout du nouveau bouclement"), e);
                }
            }
        } catch (Exception e) {
            throw new TUCreationBouclementSuivantException(
                    this.getClass()
                            .getName()
                            .concat("creationBouclementSuivant() : problème lors de la vérification avant l'ajout du nouveau bouclement"),
                    e);
        }
    }

    /**
     * Récupération de l'année
     * 
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {

        StringBuffer str = new StringBuffer();
        str.append(getSession().getLabel(isOnError() ? "PRO_TIT_EXPORTATION_ERROR" : "PRO_TIT_EXPORTATION"));
        str.append(" (").append(getMois()).append(".").append(getAnnee()).append(")");
        return str.toString();
    }

    /**
     * Récupère l'id bouclement
     * 
     * @return
     */
    public String getIdBouclement() {
        return idBouclement;
    }

    /**
     * Récupération du mois
     * 
     * @return
     */
    public String getMois() {
        return mois;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @param transaction
     * @return
     */
    public boolean process(BTransaction transaction) {
        boolean processValid = true;
        try {
            // vérifie que le numéro de passage a ete saisi
            if (JadeNumericUtil.isEmptyOrZero(getAnnee()) || JadeNumericUtil.isEmptyOrZero(getMois())) {
                getMemoryLog().logMessage(transaction.getSession().getLabel("ERR_MOIS_ANNEE_VIDE"), FWMessage.ERREUR,
                        "--> ");
                JadeCodingUtil.catchException(this, "process", new TUProcessException(transaction.getSession()
                        .getLabel("ERR_MOIS_ANNEE_VIDE")));
                processValid = false;
            } else {
                // Modification du statut de l'entête
                TUBouclement bouclement = changeStatutBouclement(transaction);
                // le traitement à réaliser
                String document = TUExportHandler.generate(transaction, rechercheBouclement(transaction));
                // Création de l'entête suivante
                creationBouclementSuivant(transaction, bouclement);

                getMemoryLog().logMessage(getSession().getLabel("PROCESS_SUCCES"), FWMessage.INFORMATION,
                        getSession().getLabel("PRO_TIT_EXPORTATION"));
                registerAttachedDocument(document);
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(getSession().getLabel("PROCESS_ERROR"), FWMessage.ERREUR,
                    getSession().getLabel("PRO_TIT_EXPORTATION_ERROR"));
            try {
                e.printStackTrace();
                transaction.rollback();
            } catch (Exception e1) {
                JadeCodingUtil.catchException(this, "process", e1);
            }
            JadeCodingUtil.catchException(this, "process", e);
            processValid = false;
        }
        return processValid;
    }

    private String rechercheBouclement(BTransaction transaction) throws TUFWFindException {
        TUBouclementManager bouclementManager = new TUBouclementManager();
        bouclementManager.setSession(transaction.getSession());
        bouclementManager.setForAnneeComptable(getAnnee());
        bouclementManager.setForMoisComptable(getMois());
        try {
            bouclementManager.find(transaction, BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new TUFWFindException(this.getClass().getName() + " "
                    + transaction.getSession().getLabel("ERR_LECTURE_BOUCLEMENT"),
                    bouclementManager.getCurrentSqlQuery(), e);
        }
        return ((TUBouclement) bouclementManager.getEntity(0)).getIdBouclement();
    }

    /**
     * Modification de l'année
     * 
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * Modification de l'id de bouclement
     * 
     * @param idBouclement
     */
    public void setIdBouclement(String idBouclement) {
        this.idBouclement = idBouclement;
    }

    /**
     * Modification du mois
     * 
     * @param string
     */
    public void setMois(String string) {
        mois = string;
    }
}
