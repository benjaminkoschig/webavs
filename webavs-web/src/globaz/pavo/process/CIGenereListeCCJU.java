package globaz.pavo.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.divers.TIApplication;
import globaz.pyxis.db.tiers.TITiers;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class CIGenereListeCCJU extends BProcess {

    private static final long serialVersionUID = -4292946034313063164L;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        File outputFile = new File("d://ccju//2006.csv");
        FileWriter out = new FileWriter(outputFile);
        BufferedWriter buf = new BufferedWriter(out);
        AFAffiliationManager mgr = new AFAffiliationManager();
        mgr.setSession(getSession());
        mgr.setFromDateFin("31.12.2011");
        mgr.setForTypeFacturation(AFAffiliationManager.PARITAIRE);
        mgr.changeManagerSize(BManager.SIZE_NOLIMIT);
        mgr.find();
        for (int i = 0; i < 10; i++) {

            // mgr.size()
            AFAffiliation af = (AFAffiliation) mgr.get(i);
            double masse = CEControleEmployeurService.retrieveMasse(getSession(), "2010", af.getAffilieNumero());
            String nom = af.getTiersNom();
            CIEcritureManager ecMgr = new CIEcritureManager();
            ecMgr.setSession(getSession());
            ecMgr.setForEmployeur(af.getAffiliationId());
            ecMgr.setForAnnee("2010");
            ecMgr.setForRee("true");
            int nbSal = ecMgr.getCount();
            TITiers tiers = af.getTiers();
            TIAdresseDataSource d = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    TIApplication.CS_FACTURATION, JACalendar.todayJJsMMsAAAA(), true);
            String branchEco = getSession().getCodeLibelle(af.getBrancheEconomique());
            String ville = "";
            String rue = "";
            String numRue = "";
            String casePostale = "";
            String npa = "";

            if (d != null) {
                ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
                numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                casePostale = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_CASE_POSTALE);
                npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);

            }
            buf.append(af.getAffilieNumero() + ";" + nom + ";" + branchEco + ";" + rue + ";" + numRue + ";"
                    + casePostale + ";" + npa + ";" + ville + ";" + nbSal + ";" + String.valueOf(masse) + ";"
                    + checkIsEbusiness(af) + "\n");
        }

        String line = "";
        buf.append(line.toString());

        buf.flush();

        buf.close();
        out.close();
        return true;
    }

    private String checkIsEbusiness(AFAffiliation af) {

        String isEbusinness = "Pas ebusiness";

        if (af.getDeclarationSalaire().equals(CodeSystem.DS_DAN)
                || af.getDeclarationSalaire().equals(CodeSystem.DS_ENVOI_PUCS)) {

            isEbusinness = "Ebusiness";

        }
        return isEbusinness;
    }

    @Override
    protected String getEMailObject() {
        return "Génération Ree terminée";
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

}
