package globaz.pavo.process;

import globaz.framework.process.FWProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAUtil;
import globaz.pavo.db.compte.CIEcritureManager;
import java.math.BigDecimal;
import java.util.Calendar;

public class CICalculMasseSalariale extends FWProcess {

    private static final long serialVersionUID = -1814799067481282755L;
    private boolean echoToConsole = false;
    private String fromAnnee = new String();

    private String untilAnnee = new String();

    /**
     * Constructor for CICalculMasseSalariale.
     */
    public CICalculMasseSalariale() {
        super();
    }

    /**
     * Constructor for CICalculMasseSalariale.
     * 
     * @param session
     */
    public CICalculMasseSalariale(BSession session) {
        super(session);
    }

    /**
     * Constructor for CICalculMasseSalariale.
     * 
     * @param parent
     */
    public CICalculMasseSalariale(FWProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        int fromAnneeBoucle = Calendar.getInstance().get(java.util.Calendar.YEAR) - 5;
        int untilAnneeBoucle = Calendar.getInstance().get(java.util.Calendar.YEAR) - 1;
        if (!JAUtil.isStringEmpty(getFromAnnee())) {
            fromAnneeBoucle = Integer.parseInt(getFromAnnee());
        }
        if (!JAUtil.isStringEmpty(getUntilAnnee())) {
            untilAnneeBoucle = Integer.parseInt(getFromAnnee());
        }

        for (int annee = fromAnneeBoucle; annee <= untilAnneeBoucle; annee++) {
            CIEcritureManager mgrEcrituresPositives = new CIEcritureManager();
            mgrEcrituresPositives.setSession(getSession());
            mgrEcrituresPositives.setForEmployeur("22980");
            mgrEcrituresPositives.setForAnnee(String.valueOf(annee));
            mgrEcrituresPositives.setForExtourne("0");
            BigDecimal sommePos = new BigDecimal("0");
            try {
                sommePos = mgrEcrituresPositives.getSum("KBMMON");
            } catch (Exception e) {
                e.printStackTrace();
            }
            CIEcritureManager mgrEcrituresNegatives = new CIEcritureManager();
            mgrEcrituresNegatives.setSession(getSession());
            mgrEcrituresNegatives.setForEmployeur("22980");
            mgrEcrituresNegatives.setForAnnee(String.valueOf(annee));
            mgrEcrituresNegatives.setForNotExtourne("0");
            BigDecimal sommeNeg = new BigDecimal("0");
            try {
                sommeNeg = mgrEcrituresPositives.getSum("KBMMON");
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Annee : pos " + annee + " : " + sommePos);
            System.out.println("Annee : neg " + annee + ":" + sommeNeg);
            sommePos.subtract(sommeNeg);
            System.out.println("Annee : res " + annee + ":" + sommePos);

        }
        return !isOnError();
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return "Le calcul de la masse salariale a echoué!";
        } else {
            return "Le calcul de la masse salariale s'est effectué avec succès.";
        }
    }

    /**
     * Returns the fromAnnee.
     * 
     * @return String
     */
    public String getFromAnnee() {
        return fromAnnee;
    }

    /**
     * Returns the untilAnnee.
     * 
     * @return String
     */
    public String getUntilAnnee() {
        return untilAnnee;
    }

    /**
     * Returns the echoToConsole.
     * 
     * @return boolean
     */
    public boolean isEchoToConsole() {
        return echoToConsole;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
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
     * Sets the fromAnnee.
     * 
     * @param fromAnnee
     *            The fromAnnee to set
     */
    public void setFromAnnee(String fromAnnee) {
        this.fromAnnee = fromAnnee;
    }

    /**
     * Sets the untilAnnee.
     * 
     * @param untilAnnee
     *            The untilAnnee to set
     */
    public void setUntilAnnee(String untilAnnee) {
        this.untilAnnee = untilAnnee;
    }

}
