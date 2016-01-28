package globaz.pavo.process;

import globaz.commons.nss.NSUtil;
import globaz.framework.process.FWProcess;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.hermes.application.HEApplication;
import globaz.hermes.utils.DateUtils;
import globaz.hermes.utils.EBCDICFileWriter;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIAnnonceCentrale;
import globaz.pavo.db.compte.CIAnnonceCentraleManager;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.translation.CodeSystem;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.text.ParseException;

public class CIAnnonceCentraleProcess extends FWProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int AFFILE_LENGTH = 11;
    private static final String CODE_APP_ENR_FINAL = "34";
    private static final String CODE_APP_ENR_INIT = "31";
    private static final String CODE_APP_ENR_INSC = "32";
    private static final String CODE_ENR = "01";
    private static final int CURRENCY_LENGTH = 9;
    public static final String ENCODING = "Cp037";
    private static final int ENR_LENGTH = 120;
    public static final int MAX_ECRITURE_LOT_DEFAUT = 50000;
    public static final String MODE_EXECUTION_INFOROM_D0064 = "modeExecutionInforomD0064";
    public final static int NO_ERROR = 0;
    private static final String NOMBRE_ASSURE = "00";
    private static final int NUM_AVS_LENGTH = 11;
    public final static int ON_ERROR = 200;
    private static final String REPORT_REVENUS_DEFAULT = "00000000000";
    private static final String RESERVE_ZEROS_ENR = "0000000000000000";
    private static final int TOTAL_REVENU_LENGTH = 11;
    private static final String VALEUR_AFFILIE_SANS_NUM = "00000000000";
    private String agence = "";
    private String anneeAA = "";
    private CIAnnonceCentrale annonceCentrale = null;
    private String caisse = "";
    private CIEcritureManager ecrituresAAnnoncer = null;
    private EBCDICFileWriter fos = null;
    private int maxEcrituresASelect = 0;
    private String modeExecution = "";
    private String montantInitial = "";
    private int nbreLotMax = 0;
    private File out = null;
    private BSession sessionHermes = null;
    private String startDate = "";
    private BigInteger totalRevenus = null;

    private String typeEnregistrement = CIAnnonceCentrale.TYPE_ENR_ANNONCE_INTERCALAIRE;

    /**
     * Constructor for CIAnnonceCentraleProcess.
     */
    public CIAnnonceCentraleProcess() {
        super();
    }

    /**
     * Constructor for CIAnnonceCentraleProcess.
     * 
     * @param session
     */
    public CIAnnonceCentraleProcess(BSession session) {
        super(session);
        setState(session.getLabel("MSG_ANN_CEN_PROC_START"));
    }

    /**
     * Constructor for CIAnnonceCentraleProcess.
     * 
     * @param parent
     */
    public CIAnnonceCentraleProcess(FWProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            // init valeurs communes
            caisse = StringUtils.padBeforeString(getSession().getApplication().getProperty(CIApplication.CODE_CAISSE),
                    "0", 3);
            agence = StringUtils.padBeforeString(getSession().getApplication().getProperty(CIApplication.CODE_AGENCE),
                    "0", 3);
            anneeAA = DateUtils.getYear(DateUtils.getCurrentDateAMJ(), DateUtils.AAAAMMJJ).substring(2, 4);

            InitProcess();
            //
            sessionHermes = (BSession) GlobazServer.getCurrentSystem()
                    .getApplication(HEApplication.DEFAULT_APPLICATION_HERMES).newSession(getSession());
            genererEcriture(1);
            System.out.println(DateUtils.getTimeStamp() + "Processus terminé -> Transaction ok");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.out.println(DateUtils.getTimeStamp() + "Transaction pas ok -> rollback");
            getTransaction().addErrors(e.getMessage());
        }
        return true;
    }

    private void genererEcriture(int index) throws Exception {

        String dateEnvoiPrevu;
        if (isModeExecutionInforomD0064()) {
            dateEnvoiPrevu = annonceCentrale.getDateEnvoi();
            dateEnvoiPrevu = DateUtils.convertDate(dateEnvoiPrevu, DateUtils.JJMMAAAA_DOTS, DateUtils.AAAAMMJJ);
        } else {
            dateEnvoiPrevu = DateUtils.getDateWithDayIndex(getStartDate(), DateUtils.AAAAMMJJ, index);
        }

        System.out.println("G\u00e9n\u00e9ration du lot num\u00e9ro :" + index);
        System.out.println(DateUtils.getTimeStamp()
                + "Recherche des \u00e9critures \u00e0  annoncer \u00e0  la centrale...");
        setState(getSession().getLabel("MSG_ANN_CEN_PROC_RECHERCHE"));
        ecrituresAAnnoncer = new CIEcritureManager();
        ecrituresAAnnoncer.setSession(getSession());
        ecrituresAAnnoncer.setForDateAnnonceCentrale("0");
        // prendre seulement les genres 6,7
        ecrituresAAnnoncer.setForIdTypeCompteCompta("true");

        if (isModeExecutionInforomD0064()) {
            ecrituresAAnnoncer.find(getTransaction(), BManager.SIZE_NOLIMIT);
        } else {
            ecrituresAAnnoncer.changeManagerSize(getMaxEcrituresASelect() + 1);

            // ecrituresAAnnoncer.setOrderByIdEcr();
            // on recherche toujours le max + 1
            // ainsi on est sûr que le deuxième lot aura au moins une écriture (si
            // nbre trouve = nombre max
            ecrituresAAnnoncer.find(getTransaction(), getMaxEcrituresASelect() + 1);
        }

        setState(getSession().getLabel("MSG_ANN_CEN_PROC_GENERER"));
        System.out.println(DateUtils.getTimeStamp() + "Nombre d'annonce trouv\u00e9es :" + ecrituresAAnnoncer.size());

        boolean isEncoreLotApres;
        if (isModeExecutionInforomD0064()) {
            isEncoreLotApres = false;
        } else {
            isEncoreLotApres = (ecrituresAAnnoncer.size() - getMaxEcrituresASelect()) > 0;
        }

        if (isModeExecutionInforomD0064() && (ecrituresAAnnoncer.size() == 0)) {
            getMemoryLog().logMessage(
                    getSession().getLabel("CI_ANNONCE_CENTRALE_GENERATION_AUCUNE_INSCRIPTION_A_ANNONCER"),
                    FWMessage.INFORMATION, this.getClass().getName());
        }

        if (ecrituresAAnnoncer.size() > 0) {
            String newFilePath = Jade.getInstance().getSharedDir() + CIAnnonceCentrale.PREFIX_FICHIER_SORTIE
                    + dateEnvoiPrevu;
            System.out.println(DateUtils.getTimeStamp() + "Ouverture du fichier : " + newFilePath);
            // ouverture d'un nouveau fichier
            out = new File(newFilePath);
            if (out.exists()) {
                out.delete();
            }
            // ALD 8.7.2005 : pas compatible ZOS fos = new
            // EBCDICFileWriter(out);
            // TODO : a tester modofier car Hermes ne foncitonnait pas sur ZOS
            BufferedWriter fos;
            boolean isFileEBCDIC = "true".equals(sessionHermes.getApplication().getProperty("ftp.file.input.ebcdic"));
            /** config fichier, le fichier reçu a-t'il des carriage return ? **/
            boolean hasCarriageReturns = "true".equals(sessionHermes.getApplication().getProperty(
                    "ftp.file.input.carriagereturn"));
            if (isFileEBCDIC) {
                // j'écris en EBCDIC
                fos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFilePath),
                        CIAnnonceCentraleProcess.ENCODING));
            } else {
                fos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFilePath)));
            }
            // création du prepare statement pour la mise à jour des ecritures
            BPreparedStatement ecrPrepared = new BPreparedStatement(getTransaction());
            ecrPrepared.prepareStatement(ecrituresAAnnoncer.getSqlForUpdateDateCentrale());
            ecrPrepared.setInt(1, Integer.parseInt(DateUtils.getCurrentDateAMJ()));
            // **********************
            System.out.println(DateUtils.getTimeStamp() + "G\u00e9n\u00e9ration de la trame de début du lot...");
            fos.write(getLotHeader(dateEnvoiPrevu));
            if (hasCarriageReturns) {
                fos.write("\n");
            }
            CIEcriture premEcrit = null;
            CIEcriture deuxEcrit = null;
            // String idPremiereEcriture = "0";
            // String idDerniereEcriture = "0";
            System.out.println(DateUtils.getTimeStamp() + "Ecriture de l'annonce 31...");
            fos.write(StringUtils.padAfterString(getEnrInitial(), " ", CIAnnonceCentraleProcess.ENR_LENGTH));
            if (hasCarriageReturns) {
                fos.write("\n");
            }
            System.out.println(DateUtils.getTimeStamp() + "Ecriture des annonces 32...");

            // pas besoin de modification pour InfoRomD0064 car comme isEncoreLotApres=false
            // maxAAnnonces=this.ecrituresAAnnoncer.size() et c'est qu'on veut
            int maxAAnnonces = isEncoreLotApres ? ecrituresAAnnoncer.size() - 1 : ecrituresAAnnoncer.size();

            for (int i = 0; i < maxAAnnonces; i++) {
                premEcrit = (CIEcriture) ecrituresAAnnoncer.getEntity(i);

                totalRevenus = totalRevenus.add(new BigInteger(getMontantSigne(premEcrit)));
                // mise à jour de l'écriture
                ecrPrepared.setInt(2, Integer.parseInt(premEcrit.getEcritureId()));
                ecrPrepared.execute();
                i++;
                if (i < maxAAnnonces) {
                    deuxEcrit = (CIEcriture) ecrituresAAnnoncer.getEntity(i);

                    totalRevenus = totalRevenus.add(new BigInteger(getMontantSigne(deuxEcrit)));
                    // mise à jour de l'écriture
                    ecrPrepared.setInt(2, Integer.parseInt(deuxEcrit.getEcritureId()));
                    ecrPrepared.execute();
                }
                // écriture des annonces dans un fichier
                fos.write(this.getEnrInscription(premEcrit, deuxEcrit));
                if (hasCarriageReturns) {
                    fos.write("\n");
                }
                premEcrit = null;
                deuxEcrit = null;
            }
            if (isModeExecutionInforomD0064()) {
                if (CIAnnonceCentrale.CS_ANNONCE_INTERCALAIRE.equalsIgnoreCase(annonceCentrale.getIdTypeAnnonce())) {
                    setTypeEnregistrement(CIAnnonceCentrale.TYPE_ENR_ANNONCE_INTERCALAIRE);
                } else if (CIAnnonceCentrale.CS_DERNIER_ANNONCE_ANNEE.equalsIgnoreCase(annonceCentrale
                        .getIdTypeAnnonce())) {
                    setTypeEnregistrement(CIAnnonceCentrale.TYPE_ENR_DERNIERE_ANNONCE_ANNEE);
                } else {
                    throw new Exception("Not implemented");
                }

            }
            System.out.println(DateUtils.getTimeStamp() + "Ecriture de l'annonce 34...");
            fos.write(StringUtils.padAfterString(getEnrFinal(isEncoreLotApres), " ",
                    CIAnnonceCentraleProcess.ENR_LENGTH));
            if (hasCarriageReturns) {
                fos.write("\n");
            }
            System.out.println(DateUtils.getTimeStamp() + "G\u00e9n\u00e9ration de la trame de fin du lot...");
            fos.write(getLotFooter(dateEnvoiPrevu, maxAAnnonces));
            if (hasCarriageReturns) {
                fos.write("\n");
            }
            fos.close();
            System.out.println(DateUtils.getTimeStamp() + "Fichier : " + newFilePath + " g\u00e9n\u00e9r\u00e9 !");
            // memorisation du fichier genere
            System.out.println(DateUtils.getTimeStamp() + "Total :" + totalRevenus.toString());

            CIAnnonceCentrale annonce;
            if (isModeExecutionInforomD0064()) {

                annonceCentrale.setNbrInscriptions(String.valueOf(ecrituresAAnnoncer.size()));
                annonceCentrale.setMontantTotal(totalRevenus.toString());
                annonceCentrale.setIdEtat(CIAnnonceCentrale.CS_ETAT_GENERE);
                annonceCentrale.update(getTransaction());

            } else {
                System.out.println(DateUtils.getTimeStamp() + "Ajout d'une nouvelle entrée dans la table CIANCEP");

                // mémorisation de la génération dans la base et mise à jour des
                // annonces générées
                annonce = new CIAnnonceCentrale();
                annonce.setSession(getSession());
                annonce.setDateCreation(JACalendar.todayJJsMMsAAAA());
                annonce.setDateEnvoi(DateUtils.convertDate(dateEnvoiPrevu, DateUtils.AAAAMMJJ, DateUtils.JJMMAAAA_DOTS));
                annonce.setNbrInscriptions(String.valueOf(isEncoreLotApres ? ecrituresAAnnoncer.size() - 1
                        : ecrituresAAnnoncer.size()));
                annonce.setMontantTotal(totalRevenus.toString());
                annonce.setIdTypeAnnonce(getCodeSystemeTypeEnregistrement(isEncoreLotApres));
                annonce.setIdEtat(CIAnnonceCentrale.CS_ETAT_GENERE);
                annonce.add(getTransaction());

            }

            System.out.println(DateUtils.getTimeStamp() + "Fin de génération d'un lot");
            // Clean
            fos = null;
            ecrituresAAnnoncer = null;
            out = null;
            ecrPrepared = null;
            annonce = null;
            premEcrit = null;
            deuxEcrit = null;
            dateEnvoiPrevu = null;
            System.gc();
            Runtime.getRuntime().gc();
            // on peut déjà commiter le lot généré
            getTransaction().commit();

            if (!isModeExecutionInforomD0064()) {
                // faut-il lancer encore la génération des lots ?
                // si il y a encore des écritures après le premier lot
                // et que le nbre max de lot n'est pas atteint --> on continue
                index = index + 1;
                if (isEncoreLotApres && ((getNbreLotMax() == 0) || (getNbreLotMax() >= index))) {
                    genererEcriture(index);
                }
            }

        }
    }

    private String getAffilie(CIEcriture ecriture) throws Exception {
        if (CIEcriture.CS_CIGENRE_8.equals(ecriture.getGenreEcriture())) {
            String noAffilie = "";
            if (!JadeStringUtil.isIntegerEmpty(ecriture.getPartenaireId())) {
                CICompteIndividuel ciPart = new CICompteIndividuel();
                ciPart.setSession(getSession());
                ciPart.setCompteIndividuelId(ecriture.getPartenaireId());
                ciPart.retrieve();
                if (!ciPart.isNew()) {
                    noAffilie = ciPart.getNumeroAvsForSplitting();
                }
            }
            return StringUtils.padAfterString(noAffilie, " ", CIAnnonceCentraleProcess.NUM_AVS_LENGTH);
        } else { // recherche journal
            CIJournal journal = ecriture.getJournal(null, false);
            if (!journal.isNew()) {
                if (CIJournal.CS_APG.equals(journal.getIdTypeInscription())) {
                    return "77777777777";
                } else if (CIJournal.CS_IJAI.equals(journal.getIdTypeInscription())) {
                    return "88888888888";
                } else if (CIJournal.CS_ASSURANCE_CHOMAGE.equals(journal.getIdTypeInscription())) {
                    return ecriture.getCaisseChomageFormattee();
                } else if (CIJournal.CS_ASSURANCE_MILITAIRE.equals(journal.getIdTypeInscription())) {
                    return "66666666666";
                }
            }
            String numAffWithoutDot = StringUtils.removeDots(ecriture.getIdAffilie());
            if (numAffWithoutDot.length() > CIAnnonceCentraleProcess.NUM_AVS_LENGTH) {
                numAffWithoutDot = numAffWithoutDot.substring(0, CIAnnonceCentraleProcess.NUM_AVS_LENGTH);
            }

            return StringUtils.padAfterString(numAffWithoutDot, " ", CIAnnonceCentraleProcess.AFFILE_LENGTH);
        }
    }

    public CIAnnonceCentrale getAnnonceCentrale() {
        return annonceCentrale;
    }

    /**
     * Method getCodeSystemeTypeEnregistrement.
     * 
     * @return String
     */
    private String getCodeSystemeTypeEnregistrement(boolean isEncoreEnrApres) {
        if (CIAnnonceCentrale.TYPE_ENR_ANNONCE_INTERCALAIRE.equals(getTypeEnregistrement(isEncoreEnrApres))) {
            return CIAnnonceCentrale.CS_ANNONCE_INTERCALAIRE;
        } else if (CIAnnonceCentrale.TYPE_ENR_DERNIERE_ANNONCE_ANNEE.equals(getTypeEnregistrement(isEncoreEnrApres))) {
            return CIAnnonceCentrale.CS_DERNIER_ANNONCE_ANNEE;
        } else {
            return "";
        }
    }

    private String getCodeValeur() throws Exception {
        if (totalRevenus.signum() < 0) {
            return "1";
        } else {
            return "0";
        }
    }

    private String getCodeValeurTotal() {
        if (totalRevenus.signum() < 0) {
            return "1";
        } else {
            return "0";
        }
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("MSG_ANN_CEN_PROC_MAIL_SUBJECT");
    }

    private String getEnrFinal(boolean isEncoreEnrAfter) throws ParseException {
        return CIAnnonceCentraleProcess.CODE_APP_ENR_FINAL
                + CIAnnonceCentraleProcess.CODE_ENR
                + caisse
                + agence
                + CIAnnonceCentraleProcess.RESERVE_ZEROS_ENR
                + DateUtils.getYear(DateUtils.getCurrentDateAMJ(), DateUtils.AAAAMMJJ)
                + getCodeValeurTotal()
                + StringUtils.padBeforeString(totalRevenus.abs().toString(), "0",
                        CIAnnonceCentraleProcess.TOTAL_REVENU_LENGTH) + getTypeEnregistrement(isEncoreEnrAfter);
    }

    private String getEnrInitial() throws Exception {
        return CIAnnonceCentraleProcess.CODE_APP_ENR_INIT + CIAnnonceCentraleProcess.CODE_ENR + caisse + agence
                + CIAnnonceCentraleProcess.RESERVE_ZEROS_ENR
                + DateUtils.getYear(DateUtils.getCurrentDateAMJ(), DateUtils.AAAAMMJJ) + getCodeValeur()
                + StringUtils.padBeforeString(getReportRevenus(), "0", CIAnnonceCentraleProcess.TOTAL_REVENU_LENGTH)
                + "0";
    }

    private String getEnrInscription(CIEcriture ecriture) throws Exception {
        return CIAnnonceCentraleProcess.CODE_APP_ENR_INSC
                + CIAnnonceCentraleProcess.CODE_ENR
                + caisse
                + agence
                + getNumAVS(ecriture)
                + CIAnnonceCentraleProcess.NOMBRE_ASSURE
                + getAffilie(ecriture)
                + StringUtils.padAfterString(CodeSystem.getCodeUtilisateur(ecriture.getExtourne(), getSession()), "0",
                        1)
                + StringUtils.padAfterString(CodeSystem.getCodeUtilisateur(ecriture.getGenreEcriture(), getSession()),
                        "0", 1)
                + StringUtils.padAfterString(CodeSystem.getCodeUtilisateur(ecriture.getParticulier(), getSession()),
                        " ", 1)
                + getPartBonifAss(ecriture)
                + StringUtils.padAfterString(CodeSystem.getCodeUtilisateur(ecriture.getCodeSpecial(), getSession()),
                        "0", 2)
                + ecriture.getMoisDebutPad()
                + ecriture.getMoisFinPad()
                + ecriture.getAnnee()
                + StringUtils.padBeforeString(String.valueOf((int) Double.parseDouble(ecriture.getRevenu())), "0",
                        CIAnnonceCentraleProcess.CURRENCY_LENGTH) + anneeAA;
    }

    private String getEnrInscription(CIEcriture premEcriture, CIEcriture dxemeEcriture) throws Exception {
        if (dxemeEcriture == null) {
            return StringUtils.padAfterString(this.getEnrInscription(premEcriture), " ",
                    CIAnnonceCentraleProcess.ENR_LENGTH);
        } else {
            return this.getEnrInscription(premEcriture) + this.getEnrInscription(dxemeEcriture);
        }
    }

    public String getLotFooter(String dateEnvoiPrevu, int nbreAnnoncesLot) throws Exception {
        String footer = new String("9901");
        String agenceForFooter = agence;
        if (JadeStringUtil.isIntegerEmpty(agence)) {
            agenceForFooter = "   ";
        }
        // 2 = 1 enregistrement initial et un enregistrement final
        int nbreEnr = 2 + (nbreAnnoncesLot / 2) + (nbreAnnoncesLot % 2);
        footer += caisse + agenceForFooter;
        // footer += "026001";
        footer = globaz.hermes.utils.StringUtils.padAfterString(footer, " ", 24);
        footer += "T0"
                + globaz.hermes.utils.DateUtils.convertDate(dateEnvoiPrevu, globaz.hermes.utils.DateUtils.AAAAMMJJ,
                        globaz.hermes.utils.DateUtils.JJMMAA);
        footer += globaz.hermes.utils.StringUtils.padBeforeString(String.valueOf(nbreEnr), "0", 6);
        footer = globaz.hermes.utils.StringUtils.padAfterString(footer, " ", 120);
        return footer;
    }

    public String getLotHeader(String dateEnvoiPrevu) throws Exception {
        String agenceForHeader = agence;
        if (JadeStringUtil.isIntegerEmpty(agence)) {
            agenceForHeader = "   ";
        }
        String header = new String("0101");
        header += caisse + agenceForHeader;
        header = globaz.hermes.utils.StringUtils.padAfterString(header, " ", 24);
        header += "T0"
                + globaz.hermes.utils.DateUtils.convertDate(dateEnvoiPrevu, globaz.hermes.utils.DateUtils.AAAAMMJJ,
                        globaz.hermes.utils.DateUtils.JJMMAA);
        header = globaz.hermes.utils.StringUtils.padAfterString(header, " ", 38);
        header = globaz.hermes.utils.StringUtils.padAfterString(header, " ", 120);
        return header;
    }

    /**
     * Returns the maxEcrituresASelect.
     * 
     * @return int
     */
    public int getMaxEcrituresASelect() throws Exception {
        if (maxEcrituresASelect == 0) {
            String maxDansProp = getSession().getApplication().getProperty("maxEcritureParLot");
            if (StringUtils.isStringEmpty(maxDansProp)) {
                return CIAnnonceCentraleProcess.MAX_ECRITURE_LOT_DEFAUT;
            } else {
                return Integer.parseInt(maxDansProp);
            }
        } else {
            return maxEcrituresASelect;
        }
    }

    public String getModeExecution() {
        return modeExecution;
    }

    /**
     * Returns the montantInitial.
     * 
     * @return String
     */
    public String getMontantInitial() {
        return montantInitial;
    }

    private String getMontantSigne(CIEcriture ecriture) {
        int revenuTr = (int) Double.parseDouble(ecriture.getRevenu());
        if (JAUtil.isIntegerEmpty(ecriture.getExtourne()) || CIEcriture.CS_EXTOURNE_2.equals(ecriture.getExtourne())
                || CIEcriture.CS_EXTOURNE_6.equals(ecriture.getExtourne())
                || CIEcriture.CS_EXTOURNE_8.equals(ecriture.getExtourne())) {
            return String.valueOf(revenuTr);
        } else {
            return String.valueOf(-1 * revenuTr);
        }
    }

    /**
     * Returns the nbreLotMax.
     * 
     * @return int
     */
    public int getNbreLotMax() {
        return nbreLotMax;
    }

    private String getNumAVS(CIEcriture ecriture) {
        if (JadeStringUtil.isIntegerEmpty(ecriture.getAvs())) {
            return CIAnnonceCentraleProcess.VALEUR_AFFILIE_SANS_NUM;
        } else {
            if ("true".equals(ecriture.getNumeroavsNNSS())) {
                String avsNegate = NSUtil.unFormatAVS(ecriture.getAvs());
                try {
                    if (avsNegate.trim().length() > 11) {
                        avsNegate = "-" + NSUtil.unFormatAVS(avsNegate).substring(3);
                    }
                } catch (Exception e) {
                    avsNegate = NSUtil.unFormatAVS(ecriture.getAvs());
                }
                return StringUtils.padAfterString(avsNegate, "0", CIAnnonceCentraleProcess.NUM_AVS_LENGTH);
            } else {
                return StringUtils.padAfterString(ecriture.getAvs(), "0", CIAnnonceCentraleProcess.NUM_AVS_LENGTH);
            }
        }
    }

    private String getPartBonifAss(CIEcriture ecriture) {
        if (StringUtils.isStringEmpty(ecriture.getPartBta())) {
            return "  ";
        } else {
            return StringUtils.padBeforeString(ecriture.getPartBta(), "0", 2);
        }
    }

    private String getReportRevenus() throws Exception {
        return totalRevenus.abs().toString();
    }

    /**
     * Returns the startDate.
     * 
     * @return String
     */
    public String getStartDate() {
        if (StringUtils.isStringEmpty(startDate)) {
            startDate = DateUtils.getCurrentDateAMJ();
        }
        return startDate;
    }

    /**
     * Returns the typeEnregistrement.
     * 
     * @return String
     */
    public String getTypeEnregistrement(boolean isEncoreEnrAfter) {
        if (isEncoreEnrAfter) {
            return CIAnnonceCentrale.TYPE_ENR_ANNONCE_INTERCALAIRE;
        } else {
            return typeEnregistrement;
        }
    }

    private void InitProcess() throws Exception {

        CIAnnonceCentraleManager annoncePrecedentes = new CIAnnonceCentraleManager();
        annoncePrecedentes.setSession(getSession());
        annoncePrecedentes.setForAnnee(DateUtils.getYear(DateUtils.getCurrentDateAMJ(), DateUtils.AAAAMMJJ));
        if (isModeExecutionInforomD0064()) {
            annoncePrecedentes.setBeforeDateEnvoi(DateUtils.convertDate(annonceCentrale.getDateEnvoi(),
                    DateUtils.JJMMAAAA_DOTS, DateUtils.AAAAMMJJ));
            annoncePrecedentes.setInStatut(CIAnnonceCentrale.CS_ETAT_GENERE + "," + CIAnnonceCentrale.CS_ETAT_ENVOYE);
        }

        annoncePrecedentes.setOrderByDateEnvoiDesc();
        annoncePrecedentes.find(getTransaction(), 1);

        if (annoncePrecedentes.size() > 0) {
            // on prend le dernier lot envoyé
            CIAnnonceCentrale derniereAnnonce = (CIAnnonceCentrale) annoncePrecedentes.getFirstEntity();
            if (CIAnnonceCentrale.CS_ANNONCE_INTERCALAIRE.equals(derniereAnnonce.getIdTypeAnnonce())) {
                // on a une annonce annonce qui a été faite au préalable ->
                // return le montant mémorisé
                totalRevenus = new BigInteger(derniereAnnonce.getMontantTotal());
                String dateDerniere = DateUtils.convertDate(derniereAnnonce.getDateEnvoi(), DateUtils.JJMMAAAA_DOTS,
                        DateUtils.AAAAMMJJ);
                if (Integer.parseInt(dateDerniere) > Integer.parseInt(DateUtils.getCurrentDateAMJ())) {
                    startDate = dateDerniere;
                } else {
                    startDate = DateUtils.getCurrentDateAMJ();
                }
            } else {
                throw new Exception(getSession().getLabel("MSG_ANN_CEN_ERREUR_DER_ANNEE"));
            }
        } else {
            // aucune annonce à la centrale effectué auparavant -> donc soit
            // revenuDefault soit un montnant definit
            totalRevenus = new BigInteger(
                    StringUtils.isStringEmpty(getMontantInitial()) ? CIAnnonceCentraleProcess.REPORT_REVENUS_DEFAULT
                            : getMontantInitial());
            startDate = DateUtils.getCurrentDateAMJ();
        }
        System.out.println("Paramètres de départ du process : date de g\u00e9n\u00e9ration " + startDate);
        System.out.println("                                  report du revenu " + totalRevenus.toString());
    }

    private boolean isModeExecutionInforomD0064() {
        return CIAnnonceCentraleProcess.MODE_EXECUTION_INFOROM_D0064.equalsIgnoreCase(modeExecution);
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setAnnonceCentrale(CIAnnonceCentrale annonceCentrale) {
        this.annonceCentrale = annonceCentrale;
    }

    /**
     * Sets the maxEcrituresASelect.
     * 
     * @param maxEcrituresASelect
     *            The maxEcrituresASelect to set
     */
    public void setMaxEcrituresASelect(int maxEcrituresASelect) {
        this.maxEcrituresASelect = maxEcrituresASelect;
    }

    public void setModeExecution(String modeExecution) {
        this.modeExecution = modeExecution;
    }

    /**
     * Sets the montantInitial.
     * 
     * @param montantInitial
     *            The montantInitial to set
     */
    public void setMontantInitial(String montantInitial) {
        this.montantInitial = montantInitial;
    }

    /**
     * Sets the nbreLotMax.
     * 
     * @param nbreLotMax
     *            The nbreLotMax to set
     */
    public void setNbreLotMax(int nbreLotMax) {
        this.nbreLotMax = nbreLotMax;
    }

    /**
     * Sets the startDate.
     * 
     * @param startDate
     *            The startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Sets the typeEnregistrement.
     * 
     * @param typeEnregistrement
     *            The typeEnregistrement to set
     */
    public void setTypeEnregistrement(String typeEnregistrement) {
        this.typeEnregistrement = typeEnregistrement;
    }
}
