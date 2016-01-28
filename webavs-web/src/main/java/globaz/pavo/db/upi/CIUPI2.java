package globaz.pavo.db.upi;

import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.util.CIUtil;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class CIUPI2 extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static int FEMME_NSSRA = 2;
    public static int HOMME_NSSRA = 1;
    public static int NB_CHAR_CHAMPS_NOM = 80;
    CICompteIndividuel ci = null;
    private boolean dateNaissanceOk = true;
    private boolean echoToConsole = true;
    private String fromNumAvs = "";
    private boolean nationOk = true;
    private boolean nomOk = true;
    BPreparedStatement nssraPrepared = null;
    ResultSet result = null;
    HashMap resultHash = null;
    private boolean sexeOk = true;
    private boolean sourceOk = true;
    private String untillNumAvs = "";

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        System.out.println("start");
        CICompteIndividuelManager mgr = new CICompteIndividuelManager();
        BStatement statement = null;
        BTransaction transactionRead = new BTransaction(getSession());
        try {
            transactionRead.openTransaction();
            mgr.setSession(getSession());
            // manager CI
            mgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            mgr.setForNNSS("true");
            // mgr.changeManagerSize(BManager.SIZE_NOLIMIT);
            // mgr.orderByAvs(false);
            mgr.wantCallMethodAfter(false);
            mgr.wantCallMethodBefore(false);
            // manager ecriture
            statement = mgr.cursorOpen(transactionRead);
            if (echoToConsole) {
                System.out.println("starting execution");
            }
            int nb = 0;
            int err = 0;
            while ((ci = (CICompteIndividuel) mgr.cursorReadNext(statement)) != null) {
                // Réinitialisation
                nomOk = true;
                nationOk = true;
                dateNaissanceOk = true;
                sexeOk = true;
                sourceOk = true;
                nb++;
                if (!compareCI()) {
                    System.out.println(ci.getNumeroAvs());
                    corrigeEntete();
                    err++;
                    if (!getTransaction().hasErrors()) {
                        try {
                            getTransaction().commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                            getTransaction().rollback();
                        }
                    } else {
                        try {
                            getTransaction().rollback();
                            getMemoryLog().logStringBuffer(getTransaction().getErrors(), ci.getNumeroAvs());
                            getTransaction().clearErrorBuffer();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                nssraPrepared.closePreparedStatement();
            }
            // Liste des inactivés

            String outPut = genereInactive(2);
            if (!JadeStringUtil.isBlankOrZero(outPut)) {
                this.registerAttachedDocument(outPut);
            }
            // Liste des invalidés
            outPut = genereInactive(3);
            if (!JadeStringUtil.isBlankOrZero(outPut)) {
                this.registerAttachedDocument(outPut);
            }

            return true;
        } finally {
            try {
                transactionRead.closeTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Compar l'entête au NRA
     * 
     * @return
     * @throws Exception
     */
    private boolean compareCI() throws Exception {
        CIUpiPreparedNssra mgr = new CIUpiPreparedNssra();
        try {
            if (JadeStringUtil.isBlankOrZero(ci.getNumeroAvs())) {
                return true;
            }

            mgr.setSession(getSession());
            nssraPrepared = new BPreparedStatement(getTransaction());
            // nssraPrepared.clearParameters();
            nssraPrepared.prepareStatement(mgr.getSqlNssra());
            nssraPrepared.clearParameters();
            nssraPrepared.setBigDecimal(1, new BigDecimal(ci.getNumeroAvs()));
            result = nssraPrepared.executeQuery();
            // Bidouille pour récupérer uniquement la 1e ligne, il n'y a
            // actuellement pas méthode pour sizer un resultset
            boolean isFirst = true;
            while (result.next() && isFirst) {
                isFirst = false;
                if (!result.getString("NOM").trim().equals(ci.getNomPrenom().trim())) {
                    nomOk = false;

                }
                if ((ci.getSexe().equals(CICompteIndividuel.CS_HOMME) && (result.getInt("SEXE") == CIUPI2.HOMME_NSSRA))
                        || (ci.getSexe().equals(CICompteIndividuel.CS_FEMME) && (result.getInt("SEXE") == CIUPI2.FEMME_NSSRA))) {
                    sexeOk = true;
                } else {
                    sexeOk = false;
                }
                if (!BSessionUtil.compareDateEqual(getSession(),
                        formatDateNaissance(String.valueOf(result.getInt("DNAISS"))), ci.getDateNaissance())) {
                    dateNaissanceOk = false;
                }
                if (!JadeStringUtil.isBlankOrZero(ci.getPaysOrigineId()) && (ci.getPaysOrigineId().length() == 6)) {
                    if (!ci.getPaysOrigineId().substring(3).equals(String.valueOf(result.getInt("NATION")))) {
                        nationOk = false;
                    }
                } else {
                    nationOk = false;
                }
                if (!ci.getSrcUpi().equals(String.valueOf(result.getInt("SOURCE")))) {
                    sourceOk = false;
                }
                if (!nationOk || !dateNaissanceOk || !sexeOk || !nomOk || !sourceOk) {

                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return true;

    }

    /**
     * execute des requêtes pour corriger le cas en cours si il y des différences
     */
    public void corrigeEntete() throws Exception {
        BPreparedStatement corrigeCI = new BPreparedStatement(getTransaction());
        CIUpiPreparedNssra sqlToExcecute = new CIUpiPreparedNssra();
        sqlToExcecute.setSession(getSession());
        corrigeCI.prepareStatement(sqlToExcecute.getSqlUpdateCI());
        // nom
        if (result.getString("NOM").trim().length() > CIUPI2.NB_CHAR_CHAMPS_NOM) {
            corrigeCI.setString(1, result.getString("NOM").trim().substring(0, CIUPI2.NB_CHAR_CHAMPS_NOM));
        } else {
            corrigeCI.setString(1, result.getString("NOM"));
        }
        // Date Naissance
        String dateNaissance = formatDateNaissance(String.valueOf(result.getInt("DNAISS")));
        corrigeCI.setBigDecimal(2, new BigDecimal(CIUtil.formatDateAMJ(dateNaissance)));
        // Nation
        corrigeCI.setBigDecimal(3, new BigDecimal("315" + String.valueOf(result.getInt("NATION"))));
        // Sexe
        if (result.getInt("SEXE") == CIUPI2.HOMME_NSSRA) {
            corrigeCI.setBigDecimal(4, new BigDecimal(CICompteIndividuel.CS_HOMME));
        } else {
            corrigeCI.setBigDecimal(4, new BigDecimal(CICompteIndividuel.CS_FEMME));
        }
        corrigeCI.setBigDecimal(5, new BigDecimal(String.valueOf(result.getInt("SOURCE"))));
        corrigeCI.setBigDecimal(6, new BigDecimal(ci.getCompteIndividuelId()));
        corrigeCI.execute();
    }

    /**
     * Formate la date de naissance
     * 
     * @param newDateNaissance
     * @return date de naissance formatée
     */
    public String formatDateNaissance(String newDateNaissance) {
        try {
            String newDate = newDateNaissance;
            // traitement des dates au format dmmyyyy
            // ajout du 0 en premiere position pour respecter le format
            // (ddmmyyyy) de la classe JADate
            try {
                if ((newDateNaissance.length() == 7) && (Integer.parseInt(newDateNaissance) > 0)) {
                    newDate = "0" + newDateNaissance;
                }
            } catch (Exception e) {
                // essayé pas pu, tant pis
            }
            JADate date = new JADate(newDate);
            return date.toStr(".");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param ci
     * @return une arraylist contenant le résumé de l'erreur : nss, nomPrénom,...
     */
    private ArrayList genereErreur(CICompteIndividuel ci) {
        ArrayList erreurIndividuel = new ArrayList();
        erreurIndividuel.add(ci.getNumeroAvs());
        erreurIndividuel.add(ci.getNomPrenom());
        erreurIndividuel.add(ci.getDateNaissance());
        erreurIndividuel.add(ci.getSexeFormate());
        erreurIndividuel.add(ci.getPaysForNNSS());
        return erreurIndividuel;
    }

    /**
     * - 2 pour les inactivés - 3 pour les invalidés
     * 
     * @throws Exception
     */
    public String genereInactive(int mode) throws Exception {

        ArrayList erreurTotal = new ArrayList();
        CIUpiPreparedNssra mgr = new CIUpiPreparedNssra();
        mgr.setSession(getSession());
        nssraPrepared = new BPreparedStatement(getTransaction());
        // nssraPrepared.clearParameters();
        nssraPrepared.prepareStatement(mgr.getSqlNssraForInactivesOrInvalidate());
        nssraPrepared.clearParameters();
        nssraPrepared.setInt(1, mode);
        ResultSet resultInactive = null;
        resultInactive = nssraPrepared.executeQuery();
        int inact = 0;
        int size = 0;
        while (resultInactive.next()) {
            size++;
            if (size % 1000 == 0) {
                System.out.println(size);
            }
            String noAvs = resultInactive.getString("NAVS");
            if (JadeStringUtil.isBlankOrZero(noAvs)) {
                return "";
            }
            CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
            ciMgr.setSession(getSession());
            ciMgr.setForNumeroAvs(noAvs);
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciMgr.find();
            if (ciMgr.size() > 0) {
                CICompteIndividuel ci = (CICompteIndividuel) ciMgr.getFirstEntity();
                // CIs inactivé
                inact++;
                erreurTotal.add(genereErreur(ci));

            }
        }
        System.out.println("Nb inactifs : " + inact);
        CIGenereInactiveInvalid genere = new CIGenereInactiveInvalid();
        if (mode == 2) {
            genere.setInactive(true);
        } else {
            genere.setInvalid(true);
        }
        genere.setErrors(erreurTotal);
        genere.setSession(getSession());
        return genere.getOutputFile();

    }

    @Override
    protected String getEMailObject() {
        if (!isOnError() && !isAborted()) {
            return "La mise à jour UPI s'est effectuée avec succès!";
        } else {
            return "La mise é jour UPI a échouée!";
        }

    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return GlobazJobQueue.UPDATE_LONG;
    }
}
