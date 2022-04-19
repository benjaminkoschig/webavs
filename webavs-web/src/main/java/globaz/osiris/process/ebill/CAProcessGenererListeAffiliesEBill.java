package globaz.osiris.process.ebill;

import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.affiliation.AFListeAffiliationModeFacturationProcess;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.ebill.CAInscriptionEBill;
import globaz.osiris.external.IntAdresseCourrier;
import globaz.osiris.external.IntTiers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CAProcessGenererListeAffiliesEBill extends BProcess {

    public class CACompteAnnexePourListe{
        public static final String NUMERO_INFOROM = "0325CCE";
        private String numeroAffilie;
        private String nss;
        private String nom;
        private String codePostal;
        private String lieu;
        private String role;
        private String hasEBill;
        private String dateInscriptionEBill;
        private String eBillAccountId;
        private String emailEBill;

        public CACompteAnnexePourListe() {
            super();
        }

        public CACompteAnnexePourListe(CACompteAnnexe compte) throws Exception{
            IntTiers tiers = compte.getTiers();
            IntAdresseCourrier addresse = tiers.getAdresseCourrier(IntAdresseCourrier.PRINCIPALE);

            CAInscriptionEBill inscriptionEBill = new CAInscriptionEBill();
            inscriptionEBill.setSession(getSession());
            inscriptionEBill.seteBillAccountID(compte.geteBillAccountID());

            inscriptionEBill.retrieve();
            if(inscriptionEBill.isNew()){
                try{
                    AFAffiliationManager affiliation = new AFAffiliationManager();
                    affiliation.setSession(getSession());
                    affiliation.setForIdTiers(compte.getIdTiers());
                    affiliation.find();

                    numeroAffilie = ((AFAffiliation)affiliation.getFirstEntity()).getAffilieNumero();
                } catch (Exception e){
                    numeroAffilie = "";
                }
                dateInscriptionEBill = "";
            } else{
                numeroAffilie = inscriptionEBill.getNumeroAffilie();
                dateInscriptionEBill = inscriptionEBill.getCreationDate();
            }

            nom = tiers.getNom();
            nss = tiers.getNumAvsActuel();
            if(addresse != null){
                codePostal = addresse.getNumPostal();
                lieu = addresse.getLocalite();
            } else{
                codePostal = "";
                lieu = "";
            }
            role = compte.getIdRole();
            hasEBill = JadeStringUtil.isBlankOrZero(compte.geteBillAccountID()) ? "non" : "oui";
            eBillAccountId = compte.geteBillAccountID();
            emailEBill = compte.geteBillMail();
        }

        @Column(name = "numero_Affilie", order = 1)
        @ColumnStyle(align = Align.LEFT)
        public String getNumeroAffilie() {
            return numeroAffilie;
        }

        @Column(name = "nss", order = 2)
        @ColumnStyle(align = Align.LEFT)
        public String getNss() {
            return nss;
        }

        @Column(name = "nom", order = 3)
        @ColumnStyle(align = Align.LEFT)
        public String getNom() {
            return nom;
        }

        @Column(name = "code_Postal", order = 4)
        @ColumnStyle(align = Align.LEFT)
        public String getCodePostal() {
            return codePostal;
        }

        @Column(name = "lieu", order = 5)
        @ColumnStyle(align = Align.LEFT)
        public String getLieu() {
            return lieu;
        }

        @Column(name = "role", order = 6)
        @ColumnStyle(align = Align.LEFT)
        public String getRole() {
            return role;
        }

        @Column(name = "inscrit_ebill", order = 7)
        @ColumnStyle(align = Align.LEFT)
        public String getHasEBill() {
            return hasEBill;
        }

        @Column(name = "date_inscription_ebill", order = 8)
        @ColumnStyle(align = Align.LEFT)
        public String getDateInscriptionEBill() {
            return dateInscriptionEBill;
        }

        @Column(name = "ebill_account_id", order = 9)
        @ColumnStyle(align = Align.LEFT)
        public String geteBillAccountId() {
            return eBillAccountId;
        }

        @Column(name = "email_ebill", order = 10)
        @ColumnStyle(align = Align.LEFT)
        public String getEmailEBill() {
            return emailEBill;
        }

        public void setNumeroAffilie(String numeroAffilie) {
            this.numeroAffilie = numeroAffilie;
        }

        public void setNss(String nss) {
            this.nss = nss;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public void setCodePostal(String codePostal) {
            this.codePostal = codePostal;
        }

        public void setLieu(String lieu) {
            this.lieu = lieu;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public void setHasEBill(String hasEBill) {
            this.hasEBill = hasEBill;
        }

        public void setDateInscriptionEBill(String dateInscriptionEBill) {
            this.dateInscriptionEBill = dateInscriptionEBill;
        }

        public void seteBillAccountId(String eBillAccountId) {
            this.eBillAccountId = eBillAccountId;
        }

        public void setEmailEBill(String emailEBill) {
            this.emailEBill = emailEBill;
        }
    }

    public CAProcessGenererListeAffiliesEBill() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor for CAProcessImpressionPlan.
     *
     * @param parent
     */
    public CAProcessGenererListeAffiliesEBill(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CAProcessImpressionPlan.
     *
     * @param session
     */
    public CAProcessGenererListeAffiliesEBill(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.setSession(getSession());
        manager.setForIdTiers("4800");
        manager.find(BManager.SIZE_NOLIMIT);
        if (manager.size() <= 0) {
            getMemoryLog().logMessage(getSession().getLabel("LABEL_MAIL_LISTE_AFFILIATION_EBILL_NO_DATA"),
                    FWMessage.INFORMATION, this.getClass().getName());
        }

        createDoc(manager);

        return !(isAborted() || isOnError() || getSession().hasErrors());
    }

    private void createDoc(CACompteAnnexeManager compteAnnexeManager) {
        List<CACompteAnnexePourListe> liste = new ArrayList<>();

        for (int i = 0; i < compteAnnexeManager.size(); i++) {
            CACompteAnnexe compte = (CACompteAnnexe) compteAnnexeManager.getEntity(i);
            try {
                liste.add(new CACompteAnnexePourListe(compte));
            } catch (Exception e) {
                JadeLogger.warn(e,"Problème lors de la recherche de données correspondantes au Tiers n° " + compte.getIdTiers());
            }
        }

        File file = SimpleOutputListBuilderJade.newInstance()
                .initializeContext(getSession())
                .outputNameAndAddPath("ListeAffiliesEBill_test")
                .addList(liste)
                .addTitle("Liste aff", Align.CENTER)
                .asCsv().build();

        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(AFListeAffiliationModeFacturationProcess.NUMERO_INFOROM);
        try {
            this.registerAttachedDocument(docInfo, file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String getEMailObject() {
        return "Gereration Liste Affilies eBill";
    }


    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }
}
