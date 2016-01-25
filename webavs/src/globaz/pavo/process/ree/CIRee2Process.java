package globaz.pavo.process.ree;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.jade.common.Jade;
import globaz.pavo.application.CIApplication;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Utilisation de la classe : globaz.globall.tools.GlobazCommandLineJob
 * 
 * Avec les arguments suivant : application="PAVO" classname="globaz.pavo.process.ree.CIRee2Process" user="h514glo"
 * password="glob4az" eMailAddress="sco@globaz.ch" anneeInf="2005" anneeSup="2010"
 * 
 * Ne pas oublier de mettre -Xmx1024m -Xms512m
 * 
 * @author sco
 */
public class CIRee2Process extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String FICHIER_FORMAT = ".csv";
    private static final String REPERTOIRE_DEST = "d://";

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
            CIRee2Process process = new CIRee2Process();

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

        // Génération des CI
        generationCIRee();

        // Génération du Ree
        generationAffiliationRee();

        // Génération des liens affiliation
        generationAffiliationLienRee();

        return false;
    }

    private String enteteAF() {
        StringBuffer buff = new StringBuffer();

        buff.append(("HTITIE") + ";");
        buff.append(("MAIAFF") + ";");
        buff.append(("MALNAF") + ";");
        buff.append(("MADDEB") + ";");
        buff.append(("MADFIN") + ";");
        buff.append(("MATMOT") + ";");
        buff.append(("MATTAF") + ";");
        buff.append(("MATBRA") + ";");
        buff.append(("MATJUR") + ";");
        buff.append(("MABEXO") + ";");
        buff.append(("MADFIC") + ";");
        buff.append(("MADFI1") + ";");
        buff.append(("MADFI2") + ";");
        buff.append(("MATDEC") + ";");
        buff.append(("MATMAS") + ";");
        buff.append(("MATMCO") + ";");
        buff.append(("MABIRR") + ";");
        buff.append(("MABOCC") + ";");
        buff.append(("MABMAI") + ";");
        buff.append(("MABLIQ") + ";");
        buff.append(("MABTRA") + ";");
        buff.append(("MABREP") + ";");
        buff.append(("MABREI") + ";");
        buff.append(("MAMMAP") + ";");
        buff.append(("MAMMAA") + ";");
        buff.append(("MATPER") + ";");
        buff.append(("MAIAVS") + ";");
        buff.append(("MALAVS") + ";");
        buff.append(("MAIAFA") + ";");
        buff.append(("MALAFA") + ";");
        buff.append(("MAILAA") + ";");
        buff.append(("MALLAA") + ";");
        buff.append(("MAILPP") + ";");
        buff.append(("MALLPP") + ";");
        buff.append(("MALFED") + ";");
        buff.append(("MAMTCO") + ";");
        buff.append(("MAMFCO") + ";");
        buff.append(("MALNAA") + ";");
        buff.append(("MAICPR") + ";");
        buff.append(("MAICPA") + ";");
        buff.append(("PSPY") + ";");
        buff.append(("MATMCR") + ";");
        buff.append(("MABBMA") + ";");
        buff.append(("MADDSU") + ";");
        buff.append(("MADFSU") + ";");
        buff.append(("MADCRE") + ";");
        buff.append(("MADTEN") + ";");
        buff.append(("MADXDE") + ";");
        buff.append(("MADXFI") + ";");
        buff.append(("MATCDN") + ";");
        buff.append(("MATTAS") + ";");
        buff.append(("MABEAA") + ";");
        buff.append(("MADESC") + ";");
        buff.append(("MADESL") + ";");
        buff.append(("MADDEM") + ";");
        buff.append(("MADESM") + ";");
        buff.append(("MATCFA") + ";");
        buff.append(("HTITIE_1") + ";");
        buff.append(("HNIPAY") + ";");
        buff.append(("HTTTIE") + ";");
        buff.append(("HTTTTI") + ";");
        buff.append(("HTLDE1") + ";");
        buff.append(("HTLDE2") + ";");
        buff.append(("HTLDE3") + ";");
        buff.append(("HTLDE4") + ";");
        buff.append(("HTTLAN") + ";");
        buff.append(("PSPY_1") + ";");
        buff.append(("HTPPHY") + ";");
        buff.append(("HTPMOR") + ";");
        buff.append(("HTINAC") + ";");
        buff.append(("HTLDU1") + ";");
        buff.append(("HTLDU2") + ";");
        buff.append(("HTLDEC") + ";");
        buff.append(("HTLDUC") + ";");
        buff.append(("HTNTIE") + ";");
        buff.append(("HTPOLF") + ";");
        buff.append(("HTPOLD") + ";");
        buff.append(("HTPOLI") + ";");
        buff.append(("HTITIE_2") + ";");
        buff.append(("HXNAVS") + ";");
        buff.append(("HXNAFF") + ";");
        buff.append(("HXNCON") + ";");
        buff.append(("HXAAVS") + ";");
        buff.append(("HXTGAF") + ";");
        buff.append(("HXDDAC") + ";");
        buff.append(("HXDFAC") + ";");
        buff.append(("PSPY_2") + ";");
        buff.append(("HTITIE_3") + ";");
        buff.append(("HJILOC") + ";");
        buff.append(("HPDNAI") + ";");
        buff.append(("HPDDEC") + ";");
        buff.append(("HPTETC") + ";");
        buff.append(("HPTSEX") + ";");
        buff.append(("HPTCAN") + ";");
        buff.append(("HPDIST") + ";");
        buff.append(("PSPY_3") + ";");
        buff.append(("HTITIE_4") + ";");
        buff.append(("HAIADR") + ";");
        buff.append(("HFIAPP") + ";");
        buff.append(("HETTAD") + ";");
        buff.append(("HEDEFA") + ";");
        buff.append(("PSPY_4") + ";");
        buff.append(("HEIAAU") + ";");
        buff.append(("HEMOTI") + ";");
        buff.append(("HEDDAD") + ";");
        buff.append(("HEDFAD") + ";");
        buff.append(("HEIADR") + ";");
        buff.append(("HEIDEX") + ";");
        buff.append(("HESTAT") + ";");
        buff.append(("HAIADR_1") + ";");
        buff.append(("HJILOC_1") + ";");
        buff.append(("HADFAD") + ";");
        buff.append(("HAIADU") + ";");
        buff.append(("HATLAN") + ";");
        buff.append(("HATTAD") + ";");
        buff.append(("HAATTE") + ";");
        buff.append(("HAADR1") + ";");
        buff.append(("HAADR2") + ";");
        buff.append(("HAADR3") + ";");
        buff.append(("HAADR4") + ";");
        buff.append(("HACPOS") + ";");
        buff.append(("HARUE") + ";");
        buff.append(("PSPY_5") + ";");
        buff.append(("HADDAD") + ";");
        buff.append(("HAMOTI") + ";");
        buff.append(("HANRUE") + ";");
        buff.append(("HAIRUE") + ";");
        buff.append(("HJILOC_2") + ";");
        buff.append(("HJICAN") + ";");
        buff.append(("HNIPAY_1") + ";");
        buff.append(("HJPFIL") + ";");
        buff.append(("HJNOPO") + ";");
        buff.append(("HJTNPA") + ";");
        buff.append(("HJNPA") + ";");
        buff.append(("HJCNPA") + ";");
        buff.append(("HJLOCC") + ";");
        buff.append(("HJLOCA") + ";");
        buff.append(("HJLANG") + ";");
        buff.append(("HJLAND") + ";");
        buff.append(("HJAFTR") + ";");
        buff.append(("HJNAGG") + ";");
        buff.append(("HJNOFS") + ";");
        buff.append(("HJDVAL") + ";");
        buff.append(("HJNSEQ") + ";");
        buff.append(("HJDSUP") + ";");
        buff.append(("HJACTI") + ";");
        buff.append(("HJTDES") + ";");
        buff.append(("HJNPAR") + ";");
        buff.append(("HJCPOR") + ";");
        buff.append(("PSPY_6") + ";");
        buff.append(("HJPROV") + ";");
        buff.append(("HNIPAY_2") + ";");
        buff.append(("HKIMON") + ";");
        buff.append(("HNCISO") + ";");
        buff.append(("HNCCEN") + ";");
        buff.append(("HNINDI") + ";");
        buff.append(("HNCFCP") + ";");
        buff.append(("HNCNTE") + ";");
        buff.append(("HNLFR") + ";");
        buff.append(("HNLAL") + ";");
        buff.append(("HNLIT") + ";");
        buff.append(("PSPY_7") + ";");
        buff.append(("HNACTI"));

        return buff.toString();
    }

    private String enteteAFLien() {
        StringBuffer buff = new StringBuffer();

        buff.append("MWILIE" + ";");
        buff.append("MAIAFF" + ";");
        buff.append("AFA_MAIAFF" + ";");
        buff.append("MWTLIE" + ";");
        buff.append("MWDDEB" + ";");
        buff.append("MWDFIN" + ";");
        buff.append("PSPY");

        return buff.toString();
    }

    private void generationAffiliationLienRee() throws Exception {

        String nomFichier = CIRee2Process.REPERTOIRE_DEST + "ree-AFLien-" + Jade.getInstance().getDefaultJdbcSchema()
                + "-" + anneeInf + "-" + anneeSup + CIRee2Process.FICHIER_FORMAT;

        System.out.println("******************************");
        System.out.println("Etape 3 - Génération AF Lien");
        System.out.println("Fichier : " + nomFichier);

        File outputFile = new File(nomFichier);

        FileWriter out = new FileWriter(outputFile);
        BufferedWriter buf = new BufferedWriter(out);

        AFLienReeManager manager = new AFLienReeManager();
        manager.setSession(getSession());
        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        buf.append(enteteAFLien() + "\n");

        System.out.println("Nbr cas : " + manager.size());
        for (int i = 0; i < manager.size(); i++) {
            AFLienRee af = (AFLienRee) manager.get(i);
            buf.append(af.getLine() + "\n");

            if (i % 5000 == 0) {
                System.out.print(i + " - ");
                buf.flush();
            }
        }

        buf.close();
        out.close();

        System.out.println("\nFin --------------------------");
    }

    private void generationAffiliationRee() throws Exception {
        String nomFichier = CIRee2Process.REPERTOIRE_DEST + "ree-AF-" + Jade.getInstance().getDefaultJdbcSchema() + "-"
                + anneeInf + "-" + anneeSup + CIRee2Process.FICHIER_FORMAT;

        System.out.println("******************************");
        System.out.println("Etape 2 - Génération Affiliation");
        System.out.println("Fichier : " + nomFichier);

        File outputFile = new File(nomFichier);

        FileWriter out = new FileWriter(outputFile);
        BufferedWriter buf = new BufferedWriter(out);

        AFReeManager manager = new AFReeManager();
        manager.setSession(getSession());
        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        buf.append(enteteAF() + "\n");

        System.out.println("Nbr cas : " + manager.size());
        for (int i = 0; i < manager.size(); i++) {
            AFRee ec = (AFRee) manager.get(i);
            buf.append(ec.getLine() + "\n");

            if (i % 5000 == 0) {
                System.out.print(i + " - ");
                buf.flush();
            }
        }

        buf.close();
        out.close();

        System.out.println("\nFin --------------------------");
    }

    private void generationCIRee() throws Exception {
        String nomFichier = CIRee2Process.REPERTOIRE_DEST + "ree-CI-" + Jade.getInstance().getDefaultJdbcSchema() + "-"
                + anneeInf + "-" + anneeSup + CIRee2Process.FICHIER_FORMAT;

        System.out.println("******************************");
        System.out.println("Etape 1 - Génération CI");
        System.out.println("Fichier : " + nomFichier);

        File outputFile = new File(nomFichier);

        FileWriter out = new FileWriter(outputFile);
        BufferedWriter buf = new BufferedWriter(out);

        for (int i = Integer.parseInt(anneeInf); i <= Integer.parseInt(anneeSup); i++) {
            System.out.println("Annee : " + i);
            CIEcritureReeManager ecMgr = new CIEcritureReeManager();
            ecMgr.setSession(getSession());
            ecMgr.setForAnnee(Integer.toString(i));
            ecMgr.find(BManager.SIZE_NOLIMIT);
            System.out.println("Nbr cas : " + ecMgr.size());
            for (int j = 0; j < ecMgr.size(); j++) {
                CIEcritureRee ec = (CIEcritureRee) ecMgr.get(j);
                StringBuffer line = new StringBuffer();
                line.append(NSUtil.formatAVSUnknown(ec.getNumAvs()) + ";");
                line.append(ec.getNom() + ";");
                line.append(ec.getSexeCode() + ";");
                line.append(ec.getDateNaissance() + ";");
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
                    System.out.print(j + " - ");
                    buf.flush();
                }
            }
            System.out.println("");
        }

        buf.close();
        out.close();

        System.out.println("\nFin --------------------------");
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
