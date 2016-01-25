package globaz.pavo.process;

import globaz.framework.process.FWProcess;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIRassemblementOuverture;
import globaz.pavo.db.compte.CIRassemblementOuvertureManager;

public class CFCRepriseExtraitsManuels extends BProcess {

    private static final long serialVersionUID = -1576370936228706986L;
    private boolean echoToConsole = false;
    protected boolean maj = false;

    protected String path = "";

    /**
     * Constructor for CFCRepriseExtraitsManuels.
     */
    public CFCRepriseExtraitsManuels() {
        super();
    }

    /**
     * Constructor for CFCRepriseExtraitsManuels.
     * 
     * @param session
     */
    public CFCRepriseExtraitsManuels(BSession session) {
        super(session);
    }

    /**
     * Constructor for CFCRepriseExtraitsManuels.
     * 
     * @param parent
     */
    public CFCRepriseExtraitsManuels(FWProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        long counter = 0;
        long fatals = 0;
        long modifs = 0;
        long ciVides = 0;
        long time = System.currentTimeMillis();
        try {
            if (echoToConsole) {
                System.out.println("opening file " + path);
            }
            java.io.BufferedReader fileIn = new java.io.BufferedReader(new java.io.FileReader(path));
            if (echoToConsole) {
                System.out.println("starting process...");
                if (maj) {
                    System.out.println("MODE UPDATE!");
                }
            }
            String line = fileIn.readLine();
            java.util.StringTokenizer tokens;

            while ((line = fileIn.readLine()) != null || counter < 1000) {
                counter++;
                if (counter % 1000 == 0) {
                    if (echoToConsole) {
                        long loop = System.currentTimeMillis();
                        System.out.println(counter + " executed in " + ((float) (loop - time) / 60000) + "min. ");
                        System.out.println(" with " + fatals + " rass non trouvés " + ciVides + " cipasTrouvé, "
                                + modifs + " modifications");
                    }
                }
                CICompteIndividuel ci = new CICompteIndividuel();
                CIRassemblementOuverture rass = new CIRassemblementOuverture();
                CIRassemblementOuvertureManager rassMgr = new CIRassemblementOuvertureManager();
                CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
                tokens = new java.util.StringTokenizer(line, ";");
                String noAvs = tokens.nextToken();
                String dateOrdre = tokens.nextToken();
                // on retrouve le CI
                ciMgr.setSession(getSession());
                ciMgr.setForNumeroAvs(noAvs);
                ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
                ciMgr.find(getTransaction());
                if (ciMgr.size() == 0) {
                    ciVides++;
                    System.out.println("ci non trouvé " + noAvs + " " + dateOrdre);
                    continue;
                }
                ci = (CICompteIndividuel) ciMgr.getFirstEntity();
                // on retrouve le rao
                rassMgr.setForCompteIndividuelId(ci.getCompteIndividuelId());
                rassMgr.setSession(getSession());
                rassMgr.setForDateOrdre(dateOrdre);
                rassMgr.setForMotifArc("98");
                rassMgr.setForTypeEnregistrement(CIRassemblementOuverture.CS_RASSEMBLEMENT);
                rassMgr.find(getTransaction());
                if (rassMgr.size() == 0) {
                    fatals++;
                    System.out.println("Rassembl non trouvé " + noAvs + " " + dateOrdre);
                    continue;
                }
                rass = (CIRassemblementOuverture) rassMgr.getFirstEntity();
                rass.setTypeEnregistrement(CIRassemblementOuverture.CS_EXTRAIT);
                if (maj) {
                    rass.update(getTransaction());
                }
                if (maj) {
                    getTransaction().commit();
                }
                modifs++;
            }
        } catch (Exception e) {
            if (echoToConsole) {
                e.printStackTrace();
            }
            getMemoryLog().logMessage(e.toString(), FWMessage.FATAL, getClass().getName());
        }
        if (echoToConsole) {
            System.out.println("Process done.");
            long end = System.currentTimeMillis();
            System.out.print(counter + " executed in " + ((float) (end - time) / 3600000) + "h. ");
            System.out.println(" with " + fatals + " rassembl non trouvés, " + ciVides + " ci non Trouvé, " + modifs
                    + " modifications");
        }

        return !isAborted();
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return "Le traitement des extrait manuels a echoué!";
        } else {
            return "Le traitement des extraits manuels s'est effectué avec succès.";
        }
    }

    /**
     * Returns the path.
     * 
     * @return String
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns the echoToConsole.
     * 
     * @return boolean
     */
    public boolean isEchoToConsole() {
        return echoToConsole;
    }

    /**
     * Returns the maj.
     * 
     * @return boolean
     */
    public boolean isMaj() {
        return maj;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Sets the echoToConsole.
     * 
     * @param echoToConsole
     *            The echoToConsole to set
     */
    public void setEchoToConsole(boolean echoToConsole) {
        this.echoToConsole = echoToConsole;
    }

    /**
     * Sets the maj.
     * 
     * @param maj
     *            The maj to set
     */
    public void setMaj(boolean maj) {
        this.maj = maj;
    }

    /**
     * Sets the path.
     * 
     * @param path
     *            The path to set
     */
    public void setPath(String path) {
        this.path = path;
    }
}
