package globaz.apg.process;

import globaz.apg.excel.APListePrestationsExcel;
import globaz.apg.vb.prestation.APPrestationJointLotTiersDroitListViewBean;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.common.Jade;
import globaz.jade.publish.client.JadePublishDocument;

public class APListePrestationExcelProcess extends BProcess {

    private String forCsSexe = "";

    private String forDateNaissance = "";

    private String forEtat = "";
    private String forIdDroit = "";
    private String forNoLot = "";

    private String forTypeDroit = "";
    private String fromDateDebut = "";
    private String toDateFin = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";

    private BSession session;



    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            APListePrestationsExcel listePrestatons = new APListePrestationsExcel(getSession());
            listePrestatons.setForCsSexe(forCsSexe);
            listePrestatons.setForDateNaissance(forDateNaissance);
            listePrestatons.setForEtat(forEtat);
            listePrestatons.setForIdDroit(forIdDroit);
            listePrestatons.setForNoLot(forNoLot);
            listePrestatons.setForTypeDroit(forTypeDroit);
            listePrestatons.setLikeNom(likeNom);
            listePrestatons.setLikePrenom(likePrenom);
            listePrestatons.setLikeNumeroAVS(likeNumeroAVS);
            listePrestatons.setLikeNumeroAVSNNSS(likeNumeroAVSNNSS);
            listePrestatons.setFromDateDebut(fromDateDebut);
            listePrestatons.setToDateFin(toDateFin);
            listePrestatons.prepareData();
            if (!listePrestatons.getList().isEmpty()) {
                listePrestatons.creerDocument();
                String path = Jade.getInstance().getHomeDir()+"work/";
                JadePublishDocument documentAPublier = new JadePublishDocument(listePrestatons.getOutputFile(path), listePrestatons.getDocInfoExcel());
                this.registerAttachedDocument(documentAPublier);
            }
        }catch(Exception e){
                _addError(getSession().getCurrentThreadTransaction(), e.getMessage());
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel("JSP_PRESTATIONS_PAN"));
                return false;
        }
        return true;
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public String getForEtat() {
        return forEtat;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public String getForNoLot() {
        return forNoLot;
    }

    public void setForNoLot(String forNoLot) {
        this.forNoLot = forNoLot;
    }

    public String getForTypeDroit() {
        return forTypeDroit;
    }

    public void setForTypeDroit(String forTypeDroit) {
        this.forTypeDroit = forTypeDroit;
    }

    public String getFromDateDebut() {
        return fromDateDebut;
    }

    public void setFromDateDebut(String fromDateDebut) {
        this.fromDateDebut = fromDateDebut;
    }

    public String getToDateFin() {
        return toDateFin;
    }

    public void setToDateFin(String toDateFin) {
        this.toDateFin = toDateFin;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public String getLikeNumeroAVSNNSS() {
        return likeNumeroAVSNNSS;
    }

    public void setLikeNumeroAVSNNSS(String likeNumeroAVSNNSS) {
        this.likeNumeroAVSNNSS = likeNumeroAVSNNSS;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }
}
