package globaz.al.vb.decision;

import globaz.al.process.decision.ALListDecisionProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Date;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

public class ALDecisionListViewBean extends BJadePersistentObjectViewBean {

    private static String PERIODICITE_MENSUELLE = "men";
    private static String PERIODICITE_TRIMERSTRIELLE = "tri";

    /**
     * Date période à par défaut date de prochaine facturation
     */
    private String datePeriodeA = null;

    /**
     * Date période de par défaut date de la dernière facturation
     */

    private String datePeriodeDe = null;
    /**
     * email (par défaut celle de l'utilisateur)
     */
    private String email = null;

    private String choixPeriodicite = null;

    public ALDecisionListViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
        String codeSystemePeriodicite;

        if (PERIODICITE_MENSUELLE.equals(choixPeriodicite)) {
            codeSystemePeriodicite = globaz.naos.translation.CodeSystem.PERIODICITE_MENSUELLE;
        } else {
            codeSystemePeriodicite = globaz.naos.translation.CodeSystem.PERIODICITE_TRIMESTRIELLE;
        }

        ALListDecisionProcess listDecision = new ALListDecisionProcess();
        listDecision.setEmail(getEmail());
        listDecision.setDateDebut(getDatePeriodeDe());
        listDecision.setDateFin(getDatePeriodeA());
        listDecision.setCsPeriodicite(codeSystemePeriodicite);
        listDecision.setSession(getSession());
        BProcessLauncher.start(listDecision, false);
    }

    @Override
    public void delete() throws Exception {
    }

    /**
     * @return the datePeriodeA
     */
    public String getDatePeriodeA() {
        if (JadeStringUtil.isBlank(datePeriodeA)) {
            datePeriodeA = getTodayDate();
        }
        return datePeriodeA;
    }

    /**
     * @return the datePeriodeDe
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public String getDatePeriodeDe() throws JadeApplicationServiceNotAvailableException, JadeApplicationException,
            JadePersistenceException {

        if (JadeStringUtil.isBlank(datePeriodeDe)) {
            RecapitulatifEntrepriseSearchModel recap = new RecapitulatifEntrepriseSearchModel();
            recap.setDefinedSearchSize(10);
            recap.setWhereKey("compensationFactu");
            recap.setOrderKey("lastChange");
            recap = ALServiceLocator.getRecapitulatifEntrepriseModelService().search(recap);
            if (recap.getSize() > 0) {
                RecapitulatifEntrepriseModel recapDate = (RecapitulatifEntrepriseModel) recap.getSearchResults()[0];
                datePeriodeDe = (JadeStringUtil.substring(recapDate.getSpy(), 6, 2)) + "."
                        + (JadeStringUtil.substring(recapDate.getSpy(), 4, 2)) + "."
                        + (JadeStringUtil.substring(recapDate.getSpy(), 0, 4));

            }
        }

        return datePeriodeDe;
    }

    /**
     * @return the email
     */
    /**
     * @return the email
     */
    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }
        return email;

    }

    @Override
    public String getId() {
        return null;
    }

    /**
     * @return session actuelle
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * @return La date du jour
     */
    public String getTodayDate() {
        Date date = new Date();
        return JadeDateUtil.getGlobazFormattedDate(date);
    }

    @Override
    public void retrieve() throws Exception {
    }

    /**
     * @param datePeriodeA
     *            the datePeriodeA to set
     */
    public void setDatePeriodeA(String datePeriodeA) {
        this.datePeriodeA = datePeriodeA;
    }

    /**
     * @param datePeriodeDe
     *            the datePeriodeDe to set
     */
    public void setDatePeriodeDe(String datePeriodeDe) {
        this.datePeriodeDe = datePeriodeDe;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setId(String arg0) {
    }

    @Override
    public void update() throws Exception {
    }

    public String getChoixPeriodicite() {
        return choixPeriodicite;
    }

    public void setChoixPeriodicite(String choixPeriodicite) {
        this.choixPeriodicite = choixPeriodicite;
    }

}
