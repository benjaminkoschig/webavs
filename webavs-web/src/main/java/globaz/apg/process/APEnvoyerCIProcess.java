/*
 * Cr?? le 18 juil. 05
 */
package globaz.apg.process;

import globaz.apg.api.prestation.IAPInscriptionCI;
import globaz.apg.db.prestation.APInscriptionCI;
import globaz.apg.db.prestation.APInscriptionCIManager;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APEnvoyerCIProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean isRegen;
    private String noPassage = "";
    APInscriptionCI previousInscriptionCI = null;
    private long montantBrutTotalInscriptionCI = 0;
    private int nbInscriptionCIOnOneFileLine = 0;
    private boolean pandemie = false;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr?e une nouvelle instance de la classe APEnvoyerCIProcess.
     */
    public APEnvoyerCIProcess() {
        super();
    }

    /**
     * Cr?e une nouvelle instance de la classe APEnvoyerCIProcess.
     * 
     * @param parent DOCUMENT ME!
     */
    public APEnvoyerCIProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Cr?e une nouvelle instance de la classe APEnvoyerCIProcess.
     * 
     * @param session DOCUMENT ME!
     */
    public APEnvoyerCIProcess(BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        BSession session = getSession();
        BTransaction transaction = getTransaction();
        FileOutputStream fo = null;
        BufferedWriter bw = null;

        try {
            JADate today = JACalendar.today();
            File file;
            if (pandemie) {
                file = File.createTempFile("PANDEMIE_INSCRIPTIONCI", ".txt");

            } else {
                file = File.createTempFile("INSCRIPTIONCI", ".txt");
            }
            file.deleteOnExit();

            fo = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fo));

            // creation du premier enregistrement (31)
            bw.write(creePremierEnregistrement() + "\n");

            // creation des enregistrements (32)
            APInscriptionCIManager inscriptionCIManager = new APInscriptionCIManager();
            inscriptionCIManager.setSession(session);
            inscriptionCIManager.setPandemie(pandemie);

            if (isRegen.booleanValue()) {
                inscriptionCIManager.setForNoPassage(getNoPassage());
            } else {
                inscriptionCIManager.setForStatut(IAPInscriptionCI.CS_OUVERT);

                // choix de la date. Si on est en d?cembre il faut envoyer
                // toutes celles qui sont avant novembre de cette
                // ann?e
                // (inclu) sinon toutes celles qui sont avant l'ann?e en cours
                // JADate today = JACalendar.today();

                if (JACalendar.getMonthName(today.getMonth()).equals(JACalendar.getMonthName(11))) {
                    // on prend les inscriptions aux CI jusque novembre de cette
                    // ann?e
                    inscriptionCIManager.setToMoisAnnee("11" + today.getYear());
                } else {
                    // on prend jusque l'ann?e pr?c?dente
                    inscriptionCIManager.setToAnnee(Integer.toString(today.getYear() - 1));
                }
            }

            inscriptionCIManager.find(BManager.SIZE_NOLIMIT);
            APInscriptionCI inscriptionCIMerged = null;
            if (inscriptionCIManager.size() >= 1) {
                inscriptionCIMerged = (APInscriptionCI) inscriptionCIManager.getFirstEntity();
                if (!isRegen) {
                    updateInscriptionCI(inscriptionCIMerged, transaction);
                }
            }

            for (int i = 1; i < inscriptionCIManager.size(); i++) {
                APInscriptionCI aInscriptionCI = (APInscriptionCI) inscriptionCIManager.getEntity(i);

                if (!isRegen) {
                    updateInscriptionCI(aInscriptionCI, transaction);
                }

                if (isInscriptionCIToMerge(inscriptionCIMerged, aInscriptionCI)) {

                    inscriptionCIMerged.setMontantBrut(Float.toString(Float.parseFloat(aInscriptionCI.getMontantBrut())
                            + Float.parseFloat(inscriptionCIMerged.getMontantBrut())));

                    if (Integer.parseInt(aInscriptionCI.getMoisFin()) > Integer.parseInt(inscriptionCIMerged
                            .getMoisFin())) {
                        inscriptionCIMerged.setMoisFin(aInscriptionCI.getMoisFin());
                    }

                } else {
                    if (!(Float.valueOf(inscriptionCIMerged.getMontantBrut()).floatValue() == 0.0)) {
                        writeInscriptionCIInFile(inscriptionCIMerged, bw);
                    }
                    inscriptionCIMerged = aInscriptionCI;

                }

            }

            if (inscriptionCIMerged != null) {
                if (!(Float.valueOf(inscriptionCIMerged.getMontantBrut()).floatValue() == 0.0)) {
                    writeInscriptionCIInFile(inscriptionCIMerged, bw);
                }

            }

            if (nbInscriptionCIOnOneFileLine == 1) {
                bw.write(JadeStringUtil.leftJustify(new String(" "), 60));
                bw.write("\n");
            }

            // creation du dernier enregistrement
            bw.write(creeEnregistrementFinal());
            bw.write("\n");

            this.registerAttachedDocument(file.getAbsolutePath());
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel("ENVOYER_CI"));

            return false;
        } finally {
            try {
                bw.close();
                fo.close();
            } catch (IOException e1) {
                e1.printStackTrace();

                return false;
            }
        }

        return true;
    }

    /**
     * @throws Exception DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        if (getParent() == null) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
            } else {
                setSendCompletionMail(true);
            }

            setControleTransaction(getTransaction() == null);
        }

        if (getSession().hasErrors()) {
            abort();
        }
    }

    private void writeInscriptionCIInFile(APInscriptionCI inscriptionCIMerged, BufferedWriter bw) throws Exception {

        bw.write(getEnregistrementCI(inscriptionCIMerged, pandemie));
        montantBrutTotalInscriptionCI += Float.valueOf(inscriptionCIMerged.getMontantBrut()).longValue();
        nbInscriptionCIOnOneFileLine = nbInscriptionCIOnOneFileLine + 1;

        if (nbInscriptionCIOnOneFileLine == 2) {
            bw.write("\n");
            nbInscriptionCIOnOneFileLine = 0;
        }

    }

    private boolean isInscriptionCIToMerge(APInscriptionCI inscriptionDeBase, APInscriptionCI inscriptionToMerge) {

        boolean isInscriptionCIToMerge = false;

        isInscriptionCIToMerge = inscriptionDeBase.getNoAVS().equalsIgnoreCase(inscriptionToMerge.getNoAVS());
        isInscriptionCIToMerge = isInscriptionCIToMerge
                && inscriptionDeBase.getAnnee().equalsIgnoreCase(inscriptionToMerge.getAnnee());

        isInscriptionCIToMerge = isInscriptionCIToMerge
                && Integer.parseInt(inscriptionToMerge.getMoisDebut()) <= (Integer.parseInt(inscriptionDeBase
                        .getMoisFin()) + 1);

        return isInscriptionCIToMerge;

    }

    /**
     * creation du dernier enregistrement du fichier d'envoi au CI
     * 
     * @param totalAnnonce le montant brut total des annonces contenue dans le fichier
     * @return
     * @throws Exception
     */
    private String creeEnregistrementFinal() throws Exception {
        StringBuffer buffer = new StringBuffer(120);
        buffer.append("34");
        buffer.append("01");
        buffer.append(JadeStringUtil.rightJustify(
                CaisseHelperFactory.getInstance().getNoCaisse(getSession().getApplication()), 3, '0'));
        buffer.append(JadeStringUtil.rightJustify(
                CaisseHelperFactory.getInstance().getNoAgence(getSession().getApplication()), 3, '0'));
        buffer.append("0000000000000000");
        buffer.append(JACalendar.today().getYear());
        buffer.append((montantBrutTotalInscriptionCI < 0) ? "1" : "0");
        buffer.append(JadeStringUtil.rightJustify(Long.toString(Math.abs(montantBrutTotalInscriptionCI)), 11, '0'));
        buffer.append("0");

        return JadeStringUtil.leftJustify(buffer.toString(), 120);
    }

    /**
     * Creation du premien enregistrement du fichier d'envoi au CI
     * 
     * @return
     * @throws Exception
     */
    private String creePremierEnregistrement() throws Exception {
        StringBuffer buffer = new StringBuffer(120);
        buffer.append("31");
        buffer.append("01");
        buffer.append(JadeStringUtil.rightJustify(
                CaisseHelperFactory.getInstance().getNoCaisse(getSession().getApplication()), 3, '0'));
        buffer.append(JadeStringUtil.rightJustify(
                CaisseHelperFactory.getInstance().getNoAgence(getSession().getApplication()), 3, '0'));
        buffer.append("0000000000000000");
        buffer.append(JACalendar.today().getYear());
        buffer.append("0000000000000");

        return JadeStringUtil.leftJustify(buffer.toString(), 120);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return "envoie CI";
    }

    /**
     * Creation de lecriture du fichier d'envoi au CI correspondant a l'inscription CI
     * 
     * @param inscriptionCI
     * @param pandemie
     * @return
     * @throws Exception
     */
    private String getEnregistrementCI(APInscriptionCI inscriptionCI, boolean pandemie) throws Exception {
        StringBuffer buffer = new StringBuffer(60);
        buffer.append("32");
        buffer.append("01");
        buffer.append(JadeStringUtil.rightJustify(
                CaisseHelperFactory.getInstance().getNoCaisse(getSession().getApplication()), 3, '0'));
        buffer.append(JadeStringUtil.rightJustify(
                CaisseHelperFactory.getInstance().getNoAgence(getSession().getApplication()), 3, '0'));

        String nss = JadeStringUtil.change(inscriptionCI.getNoAVS(), ".", "");
        // NNSS
        if (nss.trim().length() == 13) {
            nss = "-" + nss.substring(3);
        }
        buffer.append(nss);
        buffer.append("00");
        if (pandemie) {
            buffer.append("55555555555");
        } else {
        buffer.append("77777777777");
        }
        buffer.append((Float.valueOf(inscriptionCI.getMontantBrut()).floatValue() > 0.0) ? "0" : "1");
        buffer.append("1");
        buffer.append("00000");
        buffer.append((inscriptionCI.getMoisDebut().length() == 1) ? ("0" + inscriptionCI.getMoisDebut())
                : inscriptionCI.getMoisDebut());
        buffer.append((inscriptionCI.getMoisFin().length() == 1) ? ("0" + inscriptionCI.getMoisFin()) : inscriptionCI
                .getMoisFin());
        buffer.append(inscriptionCI.getAnnee());
        buffer.append(JadeStringUtil.rightJustify(
                Integer.toString(Math.abs(Float.valueOf(inscriptionCI.getMontantBrut()).intValue())), 9, '0'));
        buffer.append(JadeStringUtil.rightJustify(Integer.toString(JACalendar.today().getYear() % 100), 2, '0'));

        return buffer.toString();
    }

    /**
     * @return
     */
    public Boolean getIsRegen() {
        return isRegen;
    }

    /**
     * @return
     */
    public String getNoPassage() {
        return noPassage;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @param boolean1
     */
    public void setIsRegen(Boolean boolean1) {
        isRegen = boolean1;
    }

    /**
     * @param string
     */
    public void setNoPassage(String string) {
        noPassage = string;
    }

    /**
     * Change le status d'une inscription CI a valide
     * 
     * @param inscriptionCI
     * @param transaction
     * @throws Exception
     */
    private void updateInscriptionCI(APInscriptionCI inscriptionCI, BTransaction transaction) throws Exception {
        inscriptionCI.setStatut(IAPInscriptionCI.CS_VALIDE);
        inscriptionCI.setNoPassage(getNoPassage());
        inscriptionCI.update(transaction);
    }

    public void setPandemie(boolean b) {
        pandemie = b;
    }

    public boolean getPandemie() {
        return pandemie;
    }

}
