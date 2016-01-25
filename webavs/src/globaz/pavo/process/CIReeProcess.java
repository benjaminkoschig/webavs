package globaz.pavo.process;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.jade.common.Jade;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Utilisation de la classe : globaz.globall.tools.GlobazCommandLineJob
 * 
 * Avec les arguments suivant : application="PAVO" classname="globaz.pavo.process.CIReeProcess" user="h514glo"
 * password="glob4az" eMailAddress="sco@globaz.ch" anneeInf="2008" anneeSup="2010"
 * 
 * Ne pas oublier de mettre -Xmx1024m -Xms512m
 * 
 * @author sco
 */
public class CIReeProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {

        try {

            String emailAdresse = "jmc@globaz.ch";
            if (args.length > 0) {
                emailAdresse = args[0];
            }
            String user = "globazd";
            if (args.length > 1) {
                user = args[1];
            }
            String pwd = "ssiiadm";
            if (args.length > 2) {
                pwd = args[2];
            }

            BSession session = (BSession) GlobazServer.getCurrentSystem()
                    .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession(user, pwd);
            CIReeProcess process = new CIReeProcess();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String anneeInf = "2005";
    private String anneeSup = "2010";

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        String usr = getSession().getUserId();
        File outputFile = new File("d://ree/ree" + usr + "-" + Jade.getInstance().getDefaultJdbcSchema() + "-"
                + anneeInf + "-" + anneeSup + ".csv");
        FileWriter out = new FileWriter(outputFile);
        BufferedWriter buf = new BufferedWriter(out);
        for (int i = Integer.parseInt(anneeInf); i <= Integer.parseInt(anneeSup); i++) {
            System.out.println("Annee : " + i);
            CIEcritureManager ecMgr = new CIEcritureManager();
            ecMgr.setSession(getSession());
            ecMgr.setForRee("true");
            ecMgr.setForAnnee(String.valueOf(i));
            ecMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
            ecMgr.setOrderBy("KANAVS");
            ecMgr.find(getTransaction());
            System.out.println("Found : " + ecMgr.size());
            for (int j = 0; j < ecMgr.size(); j++) {
                CIEcriture ec = (CIEcriture) ecMgr.get(j);
                StringBuffer line = new StringBuffer();
                line.append(NSUtil.formatAVSUnknown(ec.getAvs()) + ";");
                line.append(ec.getNomPrenom() + ";");
                line.append(ec.getSexeCode() + ";");
                line.append(ec.getDateDeNaissance() + ";");
                line.append(ec.getPaysCode() + ";");
                try {
                    line.append(ec.getNoNomEmployeurBis() + ";");
                } catch (Exception e) {
                    line.append(";");
                }
                line.append(ec.getRevenu() + ";");
                line.append(ec.getAnnee() + ";");
                line.append(ec.getMoisDebut() + ";");
                line.append(ec.getMoisFin() + ";");
                line.append(getSession().getCode(ec.getGenreEcriture()) + ";");
                line.append(getSession().getCode(ec.getExtourne()) + ";");
                line.append(getSession().getCode(ec.getCodeSpecial()) + ";\n");
                buf.append(line.toString());
                if (j % 10000 == 0) {
                    System.out.println(j);
                    buf.flush();
                }
            }
            buf.flush();

        }
        buf.close();
        out.close();
        // TODO Auto-generated method stub
        return true;
    }

    public String getAnneeInf() {
        return anneeInf;
    }

    public String getAnneeSup() {
        return anneeSup;
    }

    @Override
    protected String getEMailObject() {
        return "Génération Ree terminée " + Jade.getInstance().getDefaultJdbcSchema() + "-" + anneeInf + "-" + anneeSup;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setAnneeInf(String anneeInf) {
        this.anneeInf = anneeInf;
    }

    public void setAnneeSup(String anneeSup) {
        this.anneeSup = anneeSup;
    }

}
