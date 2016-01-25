package globaz.pavo.process;

import globaz.commons.nss.NSUtil;
import globaz.commons.nss.db.NSSinfo;
import globaz.commons.nss.db.NSSinfoManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;

public class CICheckCSV extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 4205406091264712703L;
    private String path = "";

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        java.io.BufferedReader fileIn = new java.io.BufferedReader(new java.io.FileReader(path));
        String line = fileIn.readLine();
        java.util.StringTokenizer tokens;
        String nss = "";
        System.out.println("Start :");
        while ((line = fileIn.readLine()) != null) {
            tokens = new java.util.StringTokenizer(line, ";");
            while (!nss.startsWith("756") || nss.length() < 11) {
                nss = tokens.nextToken();
            }
            String dateNaissanceCsv = tokens.nextToken();
            dateNaissanceCsv = tokens.nextToken();
            dateNaissanceCsv = tokens.nextToken();

            NSSinfoManager nssMan = new NSSinfoManager();

            nssMan.setSession(getSession());
            nssMan.setForNNSS(NSUtil.unFormatAVS(nss));
            nssMan.setForCodeMutation("0");
            nssMan.setForValidite("1");
            nssMan.changeManagerSize(1);
            nssMan.find();
            String dateNaissance = "";
            if (nssMan.size() > 0) {
                dateNaissance = ((NSSinfo) nssMan.getFirstEntity()).getDateNaissance();
            } else {
                nssMan.setForValidite("0");
                nssMan.find();
                if (nssMan.size() > 0) {
                    dateNaissance = ((NSSinfo) nssMan.getFirstEntity()).getDateNaissance();
                }
            }

            if (!dateNaissance.equals(dateNaissanceCsv)) {
                System.out.println("Erreur : " + nss + " Date Naissance NRA : " + dateNaissance
                        + " Date Naissance CSV : " + dateNaissanceCsv);
            }
            nss = "";
        }
        System.out.println("Fin");

        return false;
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

}
