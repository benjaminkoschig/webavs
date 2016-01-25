package globaz.corvus.process;

import globaz.corvus.api.recap.IRERecapMensuelle;
import globaz.corvus.db.recap.access.RERecapElement;
import globaz.corvus.db.recap.access.RERecapElementManager;
import globaz.corvus.db.recap.access.RERecapInfo;
import globaz.corvus.db.recap.access.RERecapInfoManager;
import globaz.corvus.db.recap.access.RERecapMensuelle;
import globaz.corvus.db.recap.access.RERecapMensuelleManager;
import globaz.corvus.process.exception.REProcessException;
import globaz.corvus.process.exception.REProcessLoadRecapMensuelleException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;

/**
 * Process permettant le chargement des informations récap
 * 
 * @author FGO
 */
public class RELoadRecapMensuelleProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** date du paiement (MMxAAAA) */
    private String dateRapportMensuel = "";
    private String idNewRecapMensuelle = "";
    private boolean succeed;

    /**
     * Constructeur
     */
    public RELoadRecapMensuelleProcess() {
    }

    /**
     * Constructeur
     * 
     * @param parent
     */
    public RELoadRecapMensuelleProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur
     * 
     * @param session
     */
    public RELoadRecapMensuelleProcess(BSession session) {
        super(session);
    }

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
        succeed = true;
        try {
            RERecapInfoManager recapInfoMgr = loadManager(getDateRapportMensuel());

            // création de l'entête de la récap
            idNewRecapMensuelle = writeRecapMensuelle();
            for (int i = 0; i < recapInfoMgr.size(); i++) {
                RERecapInfo entity = (RERecapInfo) recapInfoMgr.getEntity(i);
                // création d'un élément de la récap
                writeRecapElement(entity);
            }

            // chargement des totaux du mois précédent
            // la recap du mois précédent est-elle déjà chargée ?
            RERecapMensuelleManager reRecapMensuelleMgrMoisSuivant = new RERecapMensuelleManager();
            reRecapMensuelleMgrMoisSuivant.setSession(getSession());
            reRecapMensuelleMgrMoisSuivant
                    .setForDateRapportMensuel(getDateRapportMensuelPrecedent(getDateRapportMensuel()));
            reRecapMensuelleMgrMoisSuivant.find(getTransaction());

            RERecapMensuelle rerecapMensPrec = (RERecapMensuelle) reRecapMensuelleMgrMoisSuivant.getFirstEntity();

            // Si oui on met à jour les champs "Rentes en cours mois -1" de la
            // nouvelle recap
            if (null != rerecapMensPrec) {
                updateRecapPrecEnCoursMoinsUn(rerecapMensPrec);
            }

        } catch (Exception e) {
            succeed = false;
            getMemoryLog().logMessage(getSession().getLabel("PROCESS_LOAD_RECAP_RECAP"), FWMessage.FATAL, e.toString());
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
     * Retoune la date de paiement (MMxAAAA)
     * 
     * @return the dateRapportMensuel
     */
    public String getDateRapportMensuel() {
        return dateRapportMensuel;
    }

    /**
     * Génération du message d'erreur si aucun enregistrement info trouvé
     */
    /*
     * private void errorMessageNoInfoRecord() { succeed = false; getMemoryLog().
     * logMessage(getSession().getLabel("PROCESS_LOAD_RECAP_FAILED"), FWMessage.WARNING,
     * "Aucun enregistrement disponible"); }
     */

    /**
     * Récupération de la date du rapport précédent
     * 
     * @param dateRapportMensuel
     * @return
     */
    private String getDateRapportMensuelPrecedent(String dateRapportMensuel) {
        JACalendar cal = new JACalendarGregorian();
        try {
            JADate date = new JADate(dateRapportMensuel);
            date = cal.addMonths(date, -1);
            return PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(date.toStrAMJ());
        } catch (JAException e) {
            return "";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        StringBuffer str = new StringBuffer();
        if (getMemoryLog().isOnErrorLevel() || !succeed) {
            str = new StringBuffer(getSession().getLabel("PROCESS_LOAD_RECAP_FAILED"));
        } else {
            str = new StringBuffer(getSession().getLabel("PROCESS_LOAD_RECAP_SUCCESS"));
        }
        str.append(" - (").append(JACalendar.todayJJsMMsAAAA()).append(")");
        return str.toString();
    }

    /**
     * Récupère l'id récap mensuelle
     * 
     * @return the idRecapMensuelle
     */
    public String getIdRecapMensuelle() {
        return idNewRecapMensuelle;
    }

    private String getMontantRenteEnCours(String prefixCodeRecap, String idRecapMensuellePrec)
            throws REProcessException {

        BigDecimal montantTotal = new BigDecimal("0");

        try {

            for (int j = 1; j < 5; j++) {

                RERecapElementManager recapElemMgr = new RERecapElementManager();
                recapElemMgr.setSession(getSession());
                recapElemMgr.setForCodeRecap(prefixCodeRecap + "00" + (new Integer(j)).toString());
                recapElemMgr.setForIdRecapMensuelle(idRecapMensuellePrec);
                recapElemMgr.find();

                RERecapElement recapElem = (RERecapElement) recapElemMgr.getFirstEntity();

                if (null != recapElem) {
                    if (j == 4) {
                        montantTotal = montantTotal
                                .add(new BigDecimal(JadeStringUtil.isEmpty(recapElem.getMontant()) ? "0" : recapElem
                                        .getMontant()).negate());
                    } else {
                        montantTotal = montantTotal.add(new BigDecimal(
                                JadeStringUtil.isEmpty(recapElem.getMontant()) ? "0" : recapElem.getMontant()));
                    }
                }
            }

            return montantTotal.toString();

        } catch (Exception e) {
            throw new REProcessLoadRecapMensuelleException(getClass().getName().concat("getMontantRenteEnCours()"), e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * Chargement du manager info récap
     * 
     * @param dateRapportMensuel
     *            MMxAAAA
     * @return
     * @throws Exception
     */
    private RERecapInfoManager loadManager(String dateRapportMensuel) throws Exception {
        RERecapInfoManager manager = new RERecapInfoManager();
        if (!JadeNumericUtil.isEmptyOrZero(dateRapportMensuel)) {
            manager.setSession(getSession());
            manager.setForDatePmt(dateRapportMensuel);
            manager.setForTotalByCode(true);
            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        }
        return manager;
    }

    /**
     * Modification de la date de paiement (MMxAAAA)
     * 
     * @param newDatePmt
     *            the newDatePmt to set
     */
    public void setDateRapportMensuel(String newDatePmt) {
        dateRapportMensuel = newDatePmt;
    }

    private void updateRecapPrecEnCoursMoinsUn(RERecapMensuelle rerecapMensPrec) throws REProcessException {

        try {
            updateRentesEnCoursMoinsUn(getMontantRenteEnCours("500", rerecapMensPrec.getIdRecapMensuelle()), "500001");
            updateRentesEnCoursMoinsUn(getMontantRenteEnCours("501", rerecapMensPrec.getIdRecapMensuelle()), "501001");
            updateRentesEnCoursMoinsUn(getMontantRenteEnCours("503", rerecapMensPrec.getIdRecapMensuelle()), "503001");
            updateRentesEnCoursMoinsUn(getMontantRenteEnCours("510", rerecapMensPrec.getIdRecapMensuelle()), "510001");
            updateRentesEnCoursMoinsUn(getMontantRenteEnCours("511", rerecapMensPrec.getIdRecapMensuelle()), "511001");
            updateRentesEnCoursMoinsUn(getMontantRenteEnCours("513", rerecapMensPrec.getIdRecapMensuelle()), "513001");

        } catch (Exception e) {
            throw new REProcessLoadRecapMensuelleException(getClass().getName().concat(
                    "updateRecapPrecEnCoursMoinsUn()"), e);
        }

    }

    /**
     * Création de l'enregistrement des éléments d'une récap mensuelle
     * 
     * @param idRecapMensuelle
     * @param entity
     * @throws REProcessException
     */
    /*
     * private void writeRecapElement(String codeRecap, String montant) throws REProcessException { RERecapElement
     * element = new RERecapElement(); element.setSession(getSession());
     * element.setIdRecapMensuelle(idNewRecapMensuelle); element.setCodeRecap(codeRecap); element.setMontant(montant);
     * 
     * try { element.add(getTransaction()); } catch (Exception e) { throw new REProcessLoadRecapMensuelleException
     * (getClass().getName().concat("writeRecapElement()"), e); } }
     */

    private void updateRentesEnCoursMoinsUn(String montant, String codeRecap) throws REProcessException {

        try {

            RERecapElement recapElem = new RERecapElement();
            recapElem.setSession(getSession());
            recapElem.setCodeRecap(codeRecap);
            recapElem.setMontant(montant);
            recapElem.setIdRecapMensuelle(idNewRecapMensuelle);
            recapElem.retrieve(getTransaction());

            if (recapElem.isNew()) {
                recapElem.save(getTransaction());
            } else {
                recapElem.update(getTransaction());
            }

        } catch (Exception e) {
            throw new REProcessLoadRecapMensuelleException(getClass().getName().concat("updateRentesEnCoursMoinsUn()"),
                    e);
        }
    }

    /**
     * Création de l'enregistrement des éléments d'une récap mensuelle
     * 
     * @param idRecapMensuelle
     * @param entity
     * @throws REProcessException
     */
    private void writeRecapElement(RERecapInfo entity) throws REProcessException {
        writeRecapElement(entity.getCodeRecap(), entity.getTotalMontant(), entity.getCas());
    }

    /**
     * Création de l'enregistrement des éléments d'une récap mensuelle
     * 
     * @param idRecapMensuelle
     * @param entity
     * @throws REProcessException
     */
    private void writeRecapElement(String codeRecap, String montant, String cas) throws REProcessException {
        RERecapElement element = new RERecapElement();
        element.setSession(getSession());
        element.setIdRecapMensuelle(idNewRecapMensuelle);
        element.setCodeRecap(codeRecap);
        element.setMontant(montant);
        element.setCas(cas);

        try {
            element.add(getTransaction());
        } catch (Exception e) {
            throw new REProcessLoadRecapMensuelleException(getClass().getName().concat("writeRecapElement()"), e);
        }
    }

    /**
     * Création de l'enregistrement de la récap mensuelle
     * 
     * @return
     * @throws REProcessLoadRecapMensuelleException
     * @throws Exception
     */
    private String writeRecapMensuelle() throws REProcessException {
        RERecapMensuelle recap = new RERecapMensuelle();
        recap.setSession(getSession());
        recap.setDateRapportMensuel(getDateRapportMensuel());
        recap.setCsEtat(IRERecapMensuelle.CS_ETAT_ATTENTE);
        try {
            recap.add(getTransaction());
        } catch (Exception e) {
            throw new REProcessLoadRecapMensuelleException(getClass().getName().concat("writeRecapMensuelle()"), e);
        }
        return recap.getIdRecapMensuelle();
    }
}
