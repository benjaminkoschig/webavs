/**
 * 
 */
package globaz.perseus.vb.pcfaccordee;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.perseus.utils.plancalcul.PFPlanCalculHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFPlanCalculViewBean extends BJadePersistentObjectViewBean {

    private String conjointInfos = null;
    private List<String> enfantsCompris = null;
    private String localite = null;
    private String monnaie = "CHF";
    private String nssInfos = null;
    private PCFAccordee pcfAccordee = null;
    private String periode = null;
    private PFPlanCalculHandler planCalculHandler = null;
    private String requerantInfos = null;

    public PFPlanCalculViewBean() {
        super();
        pcfAccordee = new PCFAccordee();
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @return the conjointInfos
     */
    public String getConjointInfos() {
        return conjointInfos;
    }

    /**
     * @return the enfantsCompris
     */
    public List<String> getEnfantsCompris() {
        return enfantsCompris;
    }

    @Override
    public String getId() {
        return pcfAccordee.getId();
    }

    /**
     * @return the localite
     */
    public String getLocalite() {
        return localite;
    }

    /**
     * @return the monnaie
     */
    public String getMonnaie() {
        return monnaie;
    }

    /**
     * @return the nssInfos
     */
    public String getNssInfos() {
        return nssInfos;
    }

    /**
     * @return the pcfAccordee
     */
    public PCFAccordee getPcfAccordee() {
        return pcfAccordee;
    }

    /**
     * @return the periode
     */
    public String getPeriode() {
        return periode;
    }

    /**
     * @return the planCalculHandler
     */
    public PFPlanCalculHandler getPlanCalculHandler() {
        return planCalculHandler;
    }

    /**
     * @return the requerantInfos
     */
    public String getRequerantInfos() {
        return requerantInfos;
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        pcfAccordee = PerseusServiceLocator.getPCFAccordeeService().read(getId());

        nssInfos = pcfAccordee.getCalcul().getDonneeString(OutputData.ENTETE_REQUERANT_NSS);
        if ("0".equals(nssInfos)) {
            nssInfos = "";
        }
        requerantInfos = pcfAccordee.getCalcul().getDonneeString(OutputData.ENTETE_REQUERANT_INFOS);
        if ("0".equals(requerantInfos)) {
            requerantInfos = "";
        }
        localite = pcfAccordee.getCalcul().getDonneeString(OutputData.ENTETE_LOCALITE);
        if ("0".equals(localite)) {
            localite = "";
        }
        conjointInfos = pcfAccordee.getCalcul().getDonneeString(OutputData.ENTETE_CONJOINT_INFOS);
        if ("0".equals(conjointInfos)) {
            conjointInfos = "";
        }
        String enfants = pcfAccordee.getCalcul().getDonneeString(OutputData.ENTETE_ENFANTS_INFOS);
        if ("0".equals(enfants)) {
            enfantsCompris = new ArrayList<String>();
        } else {
            String[] array = enfants.split(",");
            enfantsCompris = Arrays.asList(array);
        }

        periode = pcfAccordee.getCalcul().getDonneeString(OutputData.ENTETE_PERIODE);
        if ("0".equals(periode)) {
            periode = "";
        }

        planCalculHandler = new PFPlanCalculHandler(pcfAccordee, getISession());
    }

    /**
     * @param conjointInfos
     *            the conjointInfos to set
     */
    public void setConjointInfos(String conjointInfos) {
        this.conjointInfos = conjointInfos;
    }

    @Override
    public void setId(String newId) {
        pcfAccordee.setId(newId);

    }

    /**
     * @param localite
     *            the localite to set
     */
    public void setLocalite(String localite) {
        this.localite = localite;
    }

    /**
     * @param monnaie
     *            the monnaie to set
     */
    public void setMonnaie(String monnaie) {
        this.monnaie = monnaie;
    }

    /**
     * @param nssInfos
     *            the nssInfos to set
     */
    public void setNssInfos(String nssInfos) {
        this.nssInfos = nssInfos;
    }

    /**
     * @param pcfAccordee
     *            the pcfAccordee to set
     */
    public void setPcfAccordee(PCFAccordee pcfAccordee) {
        this.pcfAccordee = pcfAccordee;
    }

    /**
     * @param periode
     *            the periode to set
     */
    public void setPeriode(String periode) {
        this.periode = periode;
    }

    /**
     * @param planCalculHandler
     *            the planCalculHandler to set
     */
    public void setPlanCalculHandler(PFPlanCalculHandler planCalculHandler) {
        this.planCalculHandler = planCalculHandler;
    }

    /**
     * @param requerantInfos
     *            the requerantInfos to set
     */
    public void setRequerantInfos(String requerantInfos) {
        this.requerantInfos = requerantInfos;
    }

    @Override
    public void update() throws Exception {

    }

}
