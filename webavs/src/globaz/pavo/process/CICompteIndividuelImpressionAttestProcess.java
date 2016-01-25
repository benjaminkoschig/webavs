package globaz.pavo.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.hermes.print.itext.HEDocumentRemiseAttestCA;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;

public class CICompteIndividuelImpressionAttestProcess extends BProcess {

    private static final long serialVersionUID = 991958976650549347L;
    private String anneeCot = "";
    private String dateNaiss = "";
    private String langue = "";
    private String motif = "";
    private String nAffilie = "";
    private String nomPrenom = "";
    private String noNnss = "";
    private String sexe = "";

    public CICompteIndividuelImpressionAttestProcess() {
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

        HEDocumentRemiseAttestCA docRemiseCa = new HEDocumentRemiseAttestCA();
        docRemiseCa.setSingle("true");

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
        docRemiseCa.setAnneeCot(getAnneeCot());
        docRemiseCa.setNAffilie(getNAffilie());
        docRemiseCa.setDocumentTitle(getSession().getLabel("MSG_MENU_IMPR_AT"));

        AFAffiliationManager affManager = new AFAffiliationManager();
        AFAffiliation aff;
        affManager.setSession(getSession());
        affManager.setForAffilieNumero(getNAffilie());
        affManager.setForTypesAffParitaires();
        affManager.changeManagerSize(1);
        affManager.find();

        CIApplication app = (CIApplication) getSession().getApplication();
        if (CICompteIndividuel.CS_FEMME.equals(getSexe())) {
            docRemiseCa.setPolitesse(app.getLabel("MSG_MADAME", langue));
        } else {
            docRemiseCa.setPolitesse(app.getLabel("MSG_MONSIEUR", langue));
        }
        if ((aff = (AFAffiliation) affManager.getFirstEntity()) != null) {
            docRemiseCa.setAdresse(aff.getTiers().getAdresseAsString(null, IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    CIApplication.CS_DOMAINE_ADRESSE_CI_ARC, JACalendar.todayJJsMMsAAAA(), aff.getAffilieNumero()));
        }
        // instancier session HERMES car c'est un process HERMES et il y a des problèmes avec les labels 8918
        BSession sessionHE = (BSession) GlobazServer.getCurrentSystem().getApplication("HERMES")
                .newSession(getSession());
        if (sessionHE != null) {
            docRemiseCa.setSession(sessionHE);
        } else {
            docRemiseCa.setSession(getSession());
        }
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

    public String getLangue() {
        return langue;
    }

    protected String getLangueCS(String langue) {
        if (langue.equals("FR")) {
            langue = TITiers.CS_FRANCAIS;
        }
        if (langue.equals("IT")) {
            langue = TITiers.CS_ITALIEN;
        }
        if (langue.equals("DE")) {
            langue = TITiers.CS_ALLEMAND;
        }
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

}
