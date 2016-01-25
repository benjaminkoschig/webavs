package globaz.pavo.process;

import globaz.draco.application.DSApplication;
import globaz.draco.util.DSUtil;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationForDSManager;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class CIGenereListeAdresseCCJU extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AFAffiliationForDSManager affManager = null;
    private String annee = "2013";

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        File outputFile = new File("d://ccju//2013.csv");
        FileWriter out = new FileWriter(outputFile);
        BufferedWriter buf = new BufferedWriter(out);
        affManager = new AFAffiliationForDSManager();
        affManager.setSession(getSession());
        // Spécifique CCJU, tri par agence communale
        try {
            if (DSUtil.wantTriAgenceComm()) {
                affManager.setWantTriAgenceCommunale(true);
                affManager.orderByAgencePuisNumAff();
                affManager.setForSingleAdresseMode(new Boolean(true));
                affManager.setForTypeLien("507007");
            } else {
                affManager.setOrderBy("MALNAF");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // On fixe la date de début et la date de fin
        JADate datedebut = new JADate();
        JADate datefin = new JADate();
        String anneedebut = "";
        String anneeFin = "";
        int anneeDeclaration = 0;
        if (!JadeStringUtil.isBlankOrZero(getAnnee())) {
            anneeDeclaration = new Integer(getAnnee().trim()).intValue() - 1;
            System.out.println(anneeDeclaration);
            datedebut.setYear(anneeDeclaration + 2);
            datedebut.setDay(01);
            datedebut.setMonth(01);
            anneedebut = datedebut.toString();
            datefin.setYear(anneeDeclaration + 1);
            datefin.setDay(31);
            datefin.setMonth(12);
            anneeFin = datefin.toString();
        }
        // Si un type d'assurance a été sélectionné

        affManager.setFromDateFin(anneeFin);
        if (!JadeStringUtil.isBlankOrZero(getAnnee())) {
            affManager.setForTillDateDebut(anneedebut);
        }

        affManager.setForTypeFacturation(AFAffiliationManager.PARITAIRE);

        affManager.setForTypeDeclaration("807001");

        try {
            affManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        } catch (Exception e2) {
            getMemoryLog().logMessage(e2.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }

        System.out.println(affManager.size());
        for (int i = 0; i < affManager.size(); i++) {
            AFAffiliation af = (AFAffiliation) affManager.get(i);
            TITiers tiers = af.getTiers();
            String adresse = tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    DSApplication.CS_DOMAINE_DECLARATION_SALAIRES, JACalendar.todayJJsMMsAAAA());
            adresse = adresse.replaceAll("\n", ";");
            buf.append(af.getAffilieNumero() + ";" + adresse + "\n");
        }
        buf.flush();
        buf.close();
        out.close();
        return !isOnError();
    }

    public AFAffiliationForDSManager getAffManager() {
        return affManager;
    }

    public String getAnnee() {
        return annee;
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return GlobazJobQueue.READ_LONG;
    }

    public void setAffManager(AFAffiliationForDSManager affManager) {
        this.affManager = affManager;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

}
