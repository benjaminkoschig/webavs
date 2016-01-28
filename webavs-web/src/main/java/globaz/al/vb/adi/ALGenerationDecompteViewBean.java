package globaz.al.vb.adi;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel;
import ch.globaz.al.business.models.processus.ConfigProcessusModel;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;
import ch.globaz.al.business.services.ALServiceLocator;

public class ALGenerationDecompteViewBean extends BJadePersistentObjectViewBean {

    /**
     * bonification du dossier pour lequel on veut générer
     */

    private DecompteAdiModel decompteAdiModel = null;

    private DossierModel dossierModel = null;

    /**
     * Le numéro de facture de la récap dans laquelle mettre la prestation
     */
    private String noFacture = null;

    private String numProcessus = "0";

    /**
     * La période de traitement sur laquelle on génère la prestation
     */
    private String periodeTraitement = null;

    private List<ProcessusPeriodiqueModel> processusSelectableList = new ArrayList<ProcessusPeriodiqueModel>();

    private RecapitulatifEntrepriseSearchModel searchRecapsExistantesAffilie = new RecapitulatifEntrepriseSearchModel();

    public ALGenerationDecompteViewBean() {
        super();
        decompteAdiModel = new DecompteAdiModel();

    }

    @Override
    public void add() throws Exception {
        ALServiceLocator.getDecompteAdiBusinessService().genererPrestationAdi(getDecompteAdiModel(),
                getPeriodeTraitement(), getNoFacture(), getNumProcessus());

    }

    @Override
    public void delete() throws Exception {
        throw new Exception(this.getClass() + " - Method called (delete) not implemented (might be never called)");

    }

    public DecompteAdiModel getDecompteAdiModel() {
        return decompteAdiModel;
    }

    public String getDescriptionProcessusSelectable(int idx) {

        ProcessusPeriodiqueModel processus = processusSelectableList.get(idx);
        PeriodeAFModel period;
        try {
            period = ALServiceLocator.getPeriodeAFModelService().read(processus.getIdPeriode());

            ConfigProcessusModel config = ALServiceLocator.getConfigProcessusModelService().read(
                    processus.getIdConfig());
            return period.getDatePeriode() + "-" + JadeCodesSystemsUtil.getCodeLibelle(config.getBusinessProcessus());
        } catch (Exception e) {
            return "Unable to get processus";
        }

    }

    public DossierModel getDossierModel() {
        return dossierModel;
    }

    @Override
    public String getId() {
        return decompteAdiModel.getId();
    }

    public String getNoFacture() {
        return noFacture;
    }

    public String getNumProcessus() {
        return numProcessus;
    }

    public String getPeriodeTraitement() {
        return periodeTraitement;
    }

    public List<ProcessusPeriodiqueModel> getProcessusSelectableList() {
        return processusSelectableList;
    }

    /**
     * @return session actuelle
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return (decompteAdiModel != null) && !decompteAdiModel.isNew() ? new BSpy(decompteAdiModel.getSpy())
                : new BSpy(getSession());
    }

    @Override
    public void retrieve() throws Exception {
        throw new Exception(this.getClass() + " - Method called (retrieve) not implemented (might be never called)");

    }

    public void setDecompteAdiModel(DecompteAdiModel decompteAdiModel) {
        this.decompteAdiModel = decompteAdiModel;
    }

    public void setDossierModel(DossierModel dossierModel) {
        this.dossierModel = dossierModel;
    }

    @Override
    public void setId(String newId) {
        decompteAdiModel.setId(newId);

    }

    public void setNoFacture(String noFacture) {
        this.noFacture = noFacture;
    }

    public void setNumProcessus(String numProcessus) {
        this.numProcessus = numProcessus;
    }

    public void setPeriodeTraitement(String periodeTraitement) {
        this.periodeTraitement = periodeTraitement;
    }

    public void setProcessusSelectableList(List<ProcessusPeriodiqueModel> processusSelectableList) {
        this.processusSelectableList = processusSelectableList;
    }

    @Override
    public void update() throws Exception {
        throw new Exception(this.getClass() + " - Method called (update) not implemented (might be never called)");

    }

    public void setSearchRecapsExistantesAffilie(RecapitulatifEntrepriseSearchModel searchRecapsExistantesAffilie) {
        this.searchRecapsExistantesAffilie = searchRecapsExistantesAffilie;
    }

    public RecapitulatifEntrepriseSearchModel getSearchRecapsExistantesAffilie() {
        return searchRecapsExistantesAffilie;
    }

}
