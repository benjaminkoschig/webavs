package globaz.hercule.process.traitement;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.translation.CodeSystem;

/**
 * Processus de rattrapage annuel des périodes de couverture. <BR>
 * Pour chaque affilié <BR>
 * - on recalcule la note de collaboration suivant le contentieux et la déclaration de salaire<BR>
 * - On recalcule la note globale (Le nombre de points) si les notes ont changées<BR>
 * - On récupère la masse salariale => On recalcule la période de couverture - On met a jour les informations au niveau
 * du groupe si besoin
 * 
 * @author SCO
 * @since SCO 7 juin 2010
 */
public class CEAttributionRisquelProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeNoga = "";
    private String periodicite = "";

    /**
     * Constructeur de CERattrapageAnnuelProcess
     */
    public CEAttributionRisquelProcess() {
        super();
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        if (JadeStringUtil.isBlankOrZero(codeNoga)) {
            abort();
            return false;
        }
        if (JadeStringUtil.isBlankOrZero(periodicite)) {
            abort();
            return false;
        }
        AFAffiliationManager mgr = new AFAffiliationManager();
        mgr.setSession(getSession());
        mgr.setForCodeNoga(codeNoga);
        mgr.changeManagerSize(BManager.SIZE_NOLIMIT);
        mgr.find(getTransaction());
        setProgressScaleValue(mgr.size());
        for (int i = 0; i < mgr.size(); i++) {
            AFAffiliation aff = (AFAffiliation) mgr.get(i);
            setProgressDescription("Num Affilie : " + aff.getAffilieNumero() + " | " + i + " / " + mgr.size());
            if (isAborted()) {
                return false;
            }
            AFParticulariteAffiliation part = new AFParticulariteAffiliation();
            part.setSession(getSession());
            part.setChampNumerique(periodicite);
            part.setDateDebut(JACalendar.todayJJsMMsAAAA());
            part.setParticularite(CodeSystem.PARTIC_PERIO_CONTR_EMPLOYEUR);
            part.setAffiliationId(aff.getAffiliationId());
            part.add();
            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                getMemoryLog().logMessage(getTransaction().getErrors().toString() + " : " + aff.getAffilieNumero(),
                        FWMessage.ERREUR, "Attribution d'un risque");
                getTransaction().rollback();
                getTransaction().clearErrorBuffer();

            }

        }
        return true;
    }

    public String getCodeNoga() {
        return codeNoga;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("RISQUE_GLOBAL_ERREUR");
        } else {
            return getSession().getLabel("RISQUE_GLOBAL_OK");
        }
    }

    public String getPeriodicite() {
        return periodicite;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setCodeNoga(String codeNoga) {
        this.codeNoga = codeNoga;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

}
