package globaz.pavo.process;

import globaz.framework.process.FWProcess;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.inscriptions.CIJournal;
import java.math.BigDecimal;

public class CFCCorrigeJournauxCotPers extends FWProcess {

    private static final long serialVersionUID = -2045473926625032455L;
    private boolean echoToConsole = false;
    private String idJournal = "";
    protected boolean maj = false;

    protected String path = "";

    /**
     * Constructor for CFCCorrigeJournauxCotPers.
     */
    public CFCCorrigeJournauxCotPers() {
        super();
    }

    /**
     * Constructor for CFCCorrigeJournauxCotPers.
     * 
     * @param session
     */
    public CFCCorrigeJournauxCotPers(BSession session) {
        super(session);
    }

    /**
     * Constructor for CFCCorrigeJournauxCotPers.
     * 
     * @param parent
     */
    public CFCCorrigeJournauxCotPers(FWProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
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
            CIJournal journal = new CIJournal();
            journal.setSession(getSession());
            journal.setIdTypeInscription(CIJournal.CS_DECISION_COT_PERS);
            journal.setIdTypeCompte(CIJournal.CS_CI);
            journal.setLibelle("Correction cot. pers. journal Nr. 10752975");
            journal.add(getTransaction());
            String line = fileIn.readLine();
            java.util.StringTokenizer tokens;
            while ((line = fileIn.readLine()) != null) {
                // Lecture des infos du fichier csv
                tokens = new java.util.StringTokenizer(line, ";");
                String numAvs = tokens.nextToken();
                String corr = tokens.nextToken();
                String montant = tokens.nextToken();
                String montantExtourne = tokens.nextToken();
                String moisDebut = tokens.nextToken();
                String moisFin = tokens.nextToken();
                String annee = tokens.nextToken();
                // On retrouve le CI
                CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
                ciMgr.setSession(getSession());
                ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
                ciMgr.setForNumeroAvs(numAvs);
                ciMgr.find(getTransaction());
                String idCi = ((CICompteIndividuel) ciMgr.getFirstEntity()).getCompteIndividuelId();
                if ("M".equals(corr)) {
                    // Onrecherche s'il y a une écriture qui existe
                    CIEcritureManager ecrMgr = new CIEcritureManager();
                    ecrMgr.setSession(getSession());
                    ecrMgr.setForMoisDebut(moisDebut);
                    ecrMgr.setForMoisFin(moisFin);
                    ecrMgr.setForAnnee(annee);
                    ecrMgr.setForIdJournal(idJournal);
                    ecrMgr.setForMontant(montant);
                    ecrMgr.setForCompteIndividuelId(idCi);
                    ecrMgr.find(getTransaction());
                    if (ecrMgr.size() > 1) {
                        getMemoryLog().logMessage("Plus d'une écriture existante " + numAvs + " " + montant,
                                FWMessage.INFORMATION, "Rattrapage cot.pers.");
                        continue;
                    }
                    if (ecrMgr.size() == 0) {
                        getMemoryLog().logMessage("Aucune écriture existante pour " + numAvs + " " + montant,
                                FWMessage.INFORMATION, "Rattrapage cot.pers.");
                        continue;
                    }
                    // Si pas d'erreur, on modifie l'écriture
                    CIEcriture ecrPos = new CIEcriture();
                    ecrPos = (CIEcriture) ecrMgr.getFirstEntity();
                    BigDecimal montantTotal = new BigDecimal(montant);
                    BigDecimal montantExtourneBd = new BigDecimal(montantExtourne);
                    montantTotal = montantTotal.add(montantExtourneBd);
                    ecrPos.setMontant(montantTotal.toString());
                    ecrPos.update(getTransaction());
                    ecrPos.copie();
                    // On s'occupe de la mise en compte maintenant
                    ecrPos.setExtourne("1");
                    ecrPos.setMontant(montantExtourne);
                    // ecrPos.setId("");
                    // ecrPos.setEcritureId("");
                    ecrPos.setIdJournal(journal.getIdJournal());
                    ecrPos.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
                    ecrPos.setCode(CIEcriture.CS_CODE_MIS_EN_COMTE);
                    ecrPos.setDateCiAdditionnel("0");
                    ecrPos.add(getTransaction());
                    if (maj) {
                        getTransaction().commit();
                    }

                } else if ("CORR".equals(corr)) {
                    CIEcritureManager ecrMgr = new CIEcritureManager();
                    ecrMgr.setSession(getSession());
                    ecrMgr.setForMoisDebut(moisDebut);
                    ecrMgr.setForMoisFin(moisFin);
                    ecrMgr.setForAnnee(annee);
                    ecrMgr.setForIdJournal(idJournal);
                    ecrMgr.setForMontant(montant);
                    ecrMgr.setForCompteIndividuelId(idCi);
                    ecrMgr.find(getTransaction());
                    if (ecrMgr.size() > 1) {
                        getMemoryLog().logMessage("Plus d'une écriture existante", FWMessage.INFORMATION,
                                "Rattrapage cot.pers.");
                        continue;
                    }
                    if (ecrMgr.size() == 0) {
                        getMemoryLog().logMessage("Aucune écriture existante pour " + numAvs + " " + montant,
                                FWMessage.INFORMATION, "Rattrapage cot.pers.");
                        continue;
                    }
                    CIEcriture ecrPos = new CIEcriture();
                    ecrPos = (CIEcriture) ecrMgr.getFirstEntity();
                    ecrPos.setId("");
                    ecrPos.setEcritureId("");
                    ecrPos.setIdJournal(journal.getIdJournal());
                    ecrPos.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
                    BigDecimal montantTotal = new BigDecimal(montant);
                    BigDecimal montantExtourneBd = new BigDecimal(montantExtourne);

                    if (montantTotal.compareTo(montantExtourneBd) > 0) {
                        montantTotal = montantTotal.subtract(montantExtourneBd);
                        ecrPos.setMontant(montantTotal.toString());
                        ecrPos.setExtourne("1");

                    } else if (montantTotal.compareTo(montantExtourneBd) < 0) {
                        montantTotal = montantExtourneBd.subtract(montantTotal);
                        ecrPos.setMontant(montantTotal.toString());

                    }
                    ecrPos.setRassemblementOuvertureId("0");

                    ecrPos.add(getTransaction());
                    if (maj) {
                        getTransaction().commit();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return !isAborted();
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return "La correction des inscriptions cot.pers.  a echoué!";
        } else {
            return "La correction des inscriptions cot.pers. s'est effectué avec succès.";
        }
    }

    /**
     * Returns the idJournal.
     * 
     * @return String
     */
    public String getIdJournal() {
        return idJournal;
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
     * Sets the idJournal.
     * 
     * @param idJournal
     *            The idJournal to set
     */
    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
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
