package globaz.pavo.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.hermes.print.itext.HEDocumentRemiseCertifCA;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.util.AFUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;

public class CICompteIndividuelImpressionCertifProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeCot = "";
    private String dateNaiss = "";
    private String employeur = "";
    private String langue = "";
    private String motif = "";
    private String nAffilie = "";
    private String nomPrenom = "";
    private String noNnss = "";
    private String sexe = "";
    private String typeAffilie = "";

    public CICompteIndividuelImpressionCertifProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        if (JadeStringUtil.isBlank(nAffilie)) {
            getTransaction().addErrors(getSession().getLabel("MSG_ECRITURE_AFFILIE"));
            abort();
            return false;
        }
        HEDocumentRemiseCertifCA docRemiseCa = new HEDocumentRemiseCertifCA();
        docRemiseCa.setSingle("true");
        docRemiseCa.setDocumentTitle(getSession().getLabel("MSG_MENU_IMPR_CA"));
        String[] nom;
        if (JadeStringUtil.indexOf(getNomPrenom(), ',') != -1) {
            nom = getNomPrenom().split(",");
        } else {
            nom = getNomPrenom().split(" ");
        }
        if (nom.length > 1) {
            docRemiseCa.setNom(nom[0]);
            docRemiseCa.setPrenom(nom[1]);
        }

        docRemiseCa.setDateNaiss(getDateNaiss());
        docRemiseCa.setNnss(getNoNnss());
        docRemiseCa.setMotif(getMotif());
        docRemiseCa.setLangueSingle(TITiers.toLangueIso(getLangue()));
        docRemiseCa.setNAffilie(getNAffilie());

        AFAffiliationManager affManager = new AFAffiliationManager();
        AFAffiliation aff;
        affManager.setSession(getSession());
        affManager.setForAffilieNumero(getNAffilie());
        if (AFUtil.isAffiliationParitaire(getTypeAffilie())) {
            affManager.setForTypesAffParitaires();
        } else if (AFUtil.isAffiliationPersonnelle(getTypeAffilie())) {
            affManager.setForTypesAffPersonelles();
        }
        affManager.changeManagerSize(1);
        affManager.find();

        CIApplication app = (CIApplication) getSession().getApplication();
        if (CICompteIndividuel.CS_FEMME.equals(getSexe())) {
            docRemiseCa.setPolitesse(app.getLabel("MSG_MADAME", TITiers.toLangueIso(langue)));
        } else {
            docRemiseCa.setPolitesse(app.getLabel("MSG_MONSIEUR", TITiers.toLangueIso(langue)));
        }
        if ((aff = (AFAffiliation) affManager.getFirstEntity()) != null) {

            docRemiseCa.setAdresse(aff.getTiers().getAdresseAsString(null, IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    CIApplication.CS_DOMAINE_ADRESSE_CI_ARC, JACalendar.todayJJsMMsAAAA(), aff.getAffilieNumero()));

            // docRemiseCa.setPolitesse(titre);
        }

        if (getEmployeur().equals("on")) {
            docRemiseCa.setIsEmployeur("true");
        }

        docRemiseCa.setSession(getSession());
        docRemiseCa.setEMailAddress(getEMailAddress());
        BProcessLauncher.start(docRemiseCa);
        return false;
    }

    public String getAnneeCot() {
        return anneeCot;
    }

    public String getDateNaiss() {
        return dateNaiss;
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getEmployeur() {
        return employeur;
    }

    public String getLangue() {
        return langue;
    }

    public String getMotif() {
        return motif;
    }

    public String getNAffilie() {
        return nAffilie;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public String getNoNnss() {
        return noNnss;
    }

    public String getSexe() {
        return sexe;
    }

    public String getTypeAffilie() {
        return typeAffilie;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setAnneeCot(String anneeCot) {
        this.anneeCot = anneeCot;
    }

    public void setDateNaiss(String dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public void setEmployeur(String employeur) {
        this.employeur = employeur;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setNAffilie(String affilie) {
        nAffilie = affilie;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public void setNoNnss(String noNnss) {
        this.noNnss = noNnss;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setTypeAffilie(String typeAffilie) {
        this.typeAffilie = typeAffilie;
    }
}
