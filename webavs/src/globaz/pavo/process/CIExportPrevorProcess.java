package globaz.pavo.process;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;

public class CIExportPrevorProcess extends BProcess {

    private static final String NUMINFOROM = "0307CCI";
    private static final String PREVHOR = "ListeExportAssurePrevhor";
    /**
     * 
     */
    private static final long serialVersionUID = -5187938607250992467L;
    private String annee = "";
    private String montantSeuilAnnuel = "";

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        File outputFile = new File(Jade.getInstance().getSharedDir() + CIExportPrevorProcess.PREVHOR
                + JACalendar.format(JACalendar.todayJJsMMsAAAA(), JACalendar.FORMAT_DDMMYYYY) + ".csv");
        FileWriter out = new FileWriter(outputFile);
        try {
            String oldIdAffiliation = "0";
            AFCotisationManager mgr = new AFCotisationManager();
            mgr.setSession(getSession());
            mgr.setForTypeAssurance(CodeSystem.TYPE_ASS_PREVHOR);
            mgr.setForAnneeActive(annee);
            mgr.find(BManager.SIZE_NOLIMIT);
            mgr.setOrder("MAIAFF");
            setProgressScaleValue(mgr.size());

            for (int i = 0; i < mgr.size(); i++) {
                System.out.println(i);
                setProgressCounter(i);
                AFCotisation cotisation = (AFCotisation) mgr.get(i);
                // On retrouve l'affiliation par rapport au plan
                AFPlanAffiliation plan = new AFPlanAffiliation();
                plan.setPlanAffiliationId(cotisation.getPlanAffiliationId());
                plan.setSession(getSession());
                plan.retrieve();
                if (JadeStringUtil.isBlankOrZero(plan.getPlanAffiliationId())) {
                    continue;
                }
                if (oldIdAffiliation.equals(plan.getAffiliationId())) {
                    continue;
                }
                oldIdAffiliation = plan.getAffiliationId();
                CIEcritureManager ecrMgr = new CIEcritureManager();
                ecrMgr.setSession(getSession());
                ecrMgr.setForEmployeur(plan.getAffiliationId());
                ecrMgr.setForAnnee(annee);
                ecrMgr.setOrder("KANAVS");
                ecrMgr.setForExtourne("0");
                ecrMgr.find(BManager.SIZE_NOLIMIT);
                for (int j = 0; j < ecrMgr.size(); j++) {
                    CIEcriture ecr = (CIEcriture) ecrMgr.get(j);
                    if (checkSeuil(ecr.getMontant(), ecr.getMoisDebut(), ecr.getMoisFin())) {
                        out.write(NSUtil.formatAVSUnknown(ecr.getAvs()) + ";" + ecr.getNomPrenomAssure() + ";"
                                + ecr.getDateNaissance() + ";" + getSession().getCodeLibelle(ecr.getSexe()) + ";"
                                + ecr.getMoisDebut() + ";" + ecr.getMoisFin() + ";" + ecr.getAnnee() + ";"
                                + ecr.getIdAffilie() + "\r\n");
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        } finally {
            out.close();
        }
        // création d'un docInfo
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setDocumentTitle(CIExportPrevorProcess.PREVHOR);
        docInfo.setApplicationDomain(CIApplication.DEFAULT_APPLICATION_PAVO);
        docInfo.setDocumentTypeNumber(CIExportPrevorProcess.NUMINFOROM);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        this.registerAttachedDocument(docInfo, outputFile.getAbsolutePath());

        return !isOnError() && !isAborted() && !getTransaction().hasErrors();
    }

    /**
     * Check si le montant de l'inscription proratisé par mois est plus grand que le montant seuil mensualisé
     * 
     * @param montantSeuilAnnuel
     * @param montant
     * @param moisDebut
     * @param moisFin
     * @return
     */
    private boolean checkSeuil(final String montant, final String moisDebut, final String moisFin) {
        BigDecimal nbMois = new BigDecimal(moisFin).subtract(new BigDecimal(moisDebut));
        // On ajoute 1, car écriture 4-4 => 1 mois 4-4+1 = 1 mois ou 01-12 =
        // 12-01+1 = 12 mois
        nbMois = nbMois.add(new BigDecimal("1"));
        BigDecimal salaireMensuel = new BigDecimal(montant).divide(nbMois, BigDecimal.ROUND_HALF_EVEN);
        if (salaireMensuel.compareTo(new BigDecimal(JANumberFormatter.deQuote(montantSeuilAnnuel)).divide(
                new BigDecimal("12"), BigDecimal.ROUND_HALF_EVEN)) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the annee
     */
    public String getAnnee() {
        return annee;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || isAborted()) {
            return "L'exportation PREVHOR a échoué!";
        } else {
            return "L'exportation PREVHOR s'est terminé avec succès!";
        }
    }

    /**
     * @return the montantSeuilAnnuel
     */
    public String getMontantSeuilAnnuel() {
        return montantSeuilAnnuel;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @param annee
     *            the annee to set
     */
    public void setAnnee(final String annee) {
        this.annee = annee;
    }

    /**
     * @param montantSeuilAnnuel
     *            the montantSeuilAnnuel to set
     */
    public void setMontantSeuilAnnuel(final String montantSeuilAnnuel) {
        this.montantSeuilAnnuel = montantSeuilAnnuel;
    }

}
