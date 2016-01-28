/*
 * Créé le 20 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.process.communications.envoiWritterImpl;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichage;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import globaz.phenix.db.communications.CPReceptionReader;
import globaz.phenix.db.communications.CPReceptionReaderManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.CPUtil;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.webavs.common.CommonProperties;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

/**
 * @author mmu Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPVDWritter {

    private final static String BASE_FILENAME = "ENVOI_FISC.xml";
    private static final String CODE_INDEPENDANT = "PCI";
    private static final String CODE_SALARIE_SANS_EMPLOYEUR = "SAL";
    private static final String CODE_SANS_ACTIVITE_LUCRATIVE = "PSA";
    private static final String DEMANDE_URGENTE = "NON";
    private static final String NUMERO_DEMANDE = "1";
    private static final String TAG_ADRESSEIDV = "<adrIdv>";
    // private static final String TAG_ADRTIERS1 = "<adrLgn1>";
    // private static final String UNTAG_ADRTIERS1 = "</adrLgn1>";
    // private static final String TAG_ADRTIERS2 = "<adrLgn2>";
    // private static final String UNTAG_ADRTIERS2 = "</adrLgn2>";
    private static final String TAG_ADRLIGNE = "adrLgn";
    private static final String TAG_ADRTIERS = "<adrTie>";
    private static final String TAG_DATEDEMANDE = "<datDem>";
    private static final String TAG_DATENAISSANCE = "<datNaiIdv>";
    private static final String TAG_DEBPERIODE = "<debPrdCcn>";
    private static final String TAG_EMAILRESP = "<eMaiCtcTie>";
    private static final String TAG_ENTETE = "<demandeCommunicationInformationFiscaleTiers>";
    private static final String TAG_ENTETEDEM = "<enteteDemande>";
    private static final String TAG_FINPERIODE = "<finPrdCcn>";
    private static final String TAG_FOOTER = "</demandeCommunicationInformationFiscaleTiers>";
    private static final String TAG_IDDEMANDE = "<identificationDemande>";
    private static final String TAG_IDTIERS = "<identificationTiers>";
    private static final String TAG_INDIVIDU = "<individuConcerne>";
    private static final String TAG_INFODEMANDE = "<informationDemande>";
    private static final String TAG_NBRE = "<nbrIdvDem>";
    private static final String TAG_NOM = "<nomIdv>";
    private static final String TAG_NOMRESP = "<nomCtcTie>";
    private static final String TAG_NOMTIERS = "<nomTie>";
    private static final String TAG_NUMAFF = "<numAffTie>";
    private static final String TAG_NUMAVS = "<numAvs>";
    private static final String TAG_NUMCTB = "<numCtb>";
    private static final String TAG_NUMDEMANDE = "<numDem>";
    private static final String TAG_NUMNSS = "<numAVS13>";
    private static final String TAG_NUMTIERS = "<numTie>";
    private static final String TAG_PERFISC = "<prdFisCcn>";
    private static final String TAG_PRENOM = "<preIdv>";
    private static final String TAG_QUEUE = "<queueDemande>";
    private static final String TAG_TELEPHONE = "<telCtcTie>";
    private static final String TAG_TYPCOMM = "<codTypCom>";
    private static final String TAG_URGENCEDEMANDE = "<demUrg>";
    private static final String TAG_VERSION = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>";
    private static final String UNTAG_ADRESSEIDV = "</adrIdv>";
    private static final String UNTAG_ADRTIERS = "</adrTie>";
    private static final String UNTAG_DATEDEMANDE = "</datDem>";
    private static final String UNTAG_DATENAISSANCE = "</datNaiIdv>";
    private static final String UNTAG_DEBPERIODE = "</debPrdCcn>";
    private static final String UNTAG_EMAILRESP = "</eMaiCtcTie>";
    private static final String UNTAG_ENTETEDEM = "</enteteDemande>";
    private static final String UNTAG_FINPERIODE = "</finPrdCcn>";
    private static final String UNTAG_IDDEMANDE = "</identificationDemande>";
    private static final String UNTAG_IDTIERS = "</identificationTiers>";
    private static final String UNTAG_INDIVIDU = "</individuConcerne>";
    private static final String UNTAG_INFODEMANDE = "</informationDemande>";
    private static final String UNTAG_NBRE = "</nbrIdvDem>";
    private static final String UNTAG_NOM = "</nomIdv>";
    private static final String UNTAG_NOMRESP = "</nomCtcTie>";
    private static final String UNTAG_NOMTIERS = "</nomTie>";
    private static final String UNTAG_NUMAFF = "</numAffTie>";
    private static final String UNTAG_NUMAVS = "</numAvs>";
    private static final String UNTAG_NUMCTB = "</numCtb>";
    private static final String UNTAG_NUMDEMANDE = "</numDem>";
    private static final String UNTAG_NUMNSS = "</numAVS13>";
    private static final String UNTAG_NUMTIERS = "</numTie>";
    private static final String UNTAG_PERFISC = "</prdFisCcn>";
    private static final String UNTAG_PRENOM = "</preIdv>";
    private static final String UNTAG_QUEUE = "</queueDemande>";
    private static final String UNTAG_TELEPHONE = "</telCtcTie>";
    private static final String UNTAG_TYPCOMM = "</codTypCom>";
    private static final String UNTAG_URGENCEDEMANDE = "</demUrg>";

    private boolean casErrone = false;
    private String date_impression = "";

    public String filename = null;
    private BProcess processAppelant = null;
    private BSession session;
    private boolean tagNss = false;
    private String user = "";

    public CPVDWritter(BSession session) {
        this.session = session;
        setUser(getSession().getUserFullName());
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures...
     */
    public String create(CPCommunicationFiscaleAffichageManager manager, BTransaction transaction) {
        CPCommunicationFiscaleAffichage entity;
        BStatement statement = null;
        java.io.FileWriter fileOut = null;
        try {
            statement = manager.cursorOpen(transaction);

            /*
             * try { CPReceptionReaderManager manager = new CPReceptionReaderManager(); manager.setSession(session);
             * manager.setForIdCanton(IConstantes.CS_LOCALITE_CANTON_VAUD); manager.find(); if(manager.size() > 0){
             * CPReceptionReader reader = (CPReceptionReader) manager.getFirstEntity();
             * if(reader.getNomFichier().length() > 0){ nom = reader.getNomFichier(); }else{ nom = ((BApplication)
             * getSession().getApplication()).getProperty("nomPourEnvoiFisc"); } }else{ nom = ((BApplication)
             * getSession().getApplication()).getProperty("nomPourEnvoiFisc"); } nom += "-" +
             * JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD)+ ".xml"; filename = nom; } catch
             * (Exception e) { filename = BASE_FILENAME; }
             */

            try {
                String nom = "";
                try {
                    String dateTagNss = FWFindParameter
                            .findParameter(transaction, "10502000", "CPVDDATNSS", "0", "", 2);
                    if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), JACalendar.todayJJsMMsAAAA(),
                            dateTagNss)) {
                        tagNss = true;
                    }
                } catch (Exception e) {
                    tagNss = false;
                }
                try {
                    CPReceptionReaderManager managerReader = new CPReceptionReaderManager();
                    managerReader.setSession(session);
                    managerReader.setForIdCanton(IConstantes.CS_LOCALITE_CANTON_VAUD);
                    managerReader.find();
                    if (managerReader.size() > 0) {
                        CPReceptionReader reader = (CPReceptionReader) managerReader.getFirstEntity();
                        if (reader.getNomFichier().length() > 0) {
                            nom = reader.getNomFichier();
                        } else {
                            nom = (getSession().getApplication()).getProperty("nomPourEnvoiFisc");
                        }
                    } else {
                        nom = (getSession().getApplication()).getProperty("nomPourEnvoiFisc");
                    }
                } catch (Exception e) {
                    filename = (getSession().getApplication()).getProperty("nomPourEnvoiFisc");
                }
                filename = nom;
                String type = "";
                if (!JadeStringUtil.isEmpty(manager.getInGenreAffilie())) {
                    type = globaz.phenix.translation.CodeSystem.getCodeUtilisateur(getSession(),
                            CPDecision.CS_NON_ACTIF);
                } else if (!JadeStringUtil.isEmpty(manager.getNotInGenreAffilie())) {
                    type = globaz.phenix.translation.CodeSystem.getCodeUtilisateur(getSession(),
                            CPDecision.CS_INDEPENDANT);
                }
                if (JadeStringUtil.isEmpty(type)) {
                    filename = filename + "_" + JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD)
                            + ".xml";
                } else {
                    filename = filename + "_" + JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD) + "_"
                            + type + ".xml";
                }
            } catch (Exception e) {
                filename = CPVDWritter.BASE_FILENAME;
            }
            File file = new File(getFilename());
            if (file.exists()) {
                file.delete();
            }
            fileOut = new FileWriter(file);
            creationEntete(fileOut);
            int nbCas = manager.getCount();
            int nb = 0;
            while (((entity = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                    && (!entity.isNew())) {
                // Test validité num contribuable
                casErrone = false;
                String numContri = entity.getNumContri(entity.getAnneeDecision());
                numContri = CPToolBox.unFormat(numContri);
                if ((numContri.length() > 8) || JadeStringUtil.isEmpty(numContri)) {
                    casErrone = true;
                }
                // Test validité numAVS
                String numAvs = "";
                String numAvs13 = "";
                if (entity.getNumAvs() != null) {
                    int tailleMin = 11;
                    numAvs = NSUtil.unFormatAVS(entity.getNumAvs());
                    if (numAvs.length() == 13) {
                        numAvs13 = numAvs;
                        // Recherche de l'ancien n° avs
                        numAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numAvs, getSession()));
                    }
                    // Test si pas de correspondance NSS -> NAVS
                    if (numAvs.length() > tailleMin) {
                        casErrone = true;
                    } else if (!JadeStringUtil.isEmpty(numAvs)) {
                        if (numAvs.length() < tailleMin) {
                            int difference = tailleMin - entity.getNumAvs().length();
                            // numero = entity.getNumAvs();
                            for (int i = 0; i < difference; i++) {
                                numAvs += "0";
                            }
                        }
                    } else { // numAVs vide
                        casErrone = true;
                    }
                } else {
                    casErrone = true;
                }
                // Test validité numAVS13

                if (!casErrone && JadeStringUtil.isEmpty(numAvs13)) {
                    // Recherche du nouveau n° avs
                    numAvs13 = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numAvs, getSession()));
                    if (numAvs13.length() != 13) {
                        casErrone = true;
                    }
                }
                nb++;
                if (!casErrone) {
                    creationIndividuHeader(fileOut);
                    createNom(fileOut, entity);
                    createPrenom(fileOut, entity);
                    createDateNaissance(fileOut, entity);
                    createNumContribuable(fileOut, numContri);
                    if (tagNss) {
                        createNumNSS(fileOut, numAvs13);
                    }
                    createNumAVS(fileOut, numAvs);
                    createNumAFF(fileOut, entity);
                    createTypeCommunication(fileOut, entity);
                    createPeriodeFiscale(fileOut, entity);
                    createPeriode(fileOut, entity);
                    createAdresse(fileOut, entity);
                    creationIndividuFooter(fileOut);
                    // Mise à jour de la date d'envoi
                    CPCommunicationFiscale comFis = new CPCommunicationFiscale();
                    comFis.setSession(getSession());
                    comFis.setIdCommunication(entity.getIdCommunication());
                    comFis.retrieve(transaction);
                    if (!comFis.isNew()) {
                        if (manager.getDemandeAnnulee().equals(Boolean.FALSE)) {
                            comFis.setDateEnvoi(JACalendar.todayJJsMMsAAAA());
                        } else {
                            comFis.setDateEnvoiAnnulation(JACalendar.todayJJsMMsAAAA());
                        }
                        comFis.update(transaction);
                    }
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } else {
                    nbCas--;
                }
            }
            creationFooter(fileOut, nbCas);
            fileOut.close();
        } catch (IOException e) {
            JadeLogger.error(this, e);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return getFilename();
    }

    private void createAdresse(FileWriter fileOut, CPCommunicationFiscaleAffichage entity) throws Exception {
        if (entity.getTiers() != null) {
            if (entity.getTiers().getAdresseAsString() != null) {
                fileOut.write(CPVDWritter.TAG_ADRESSEIDV);
                // On parcourt l'adresse
                String[] adresseTab = JadeStringUtil.split(entity.getTiers().getAdresseAsString(), '\n', 10);
                for (int i = 0; i < adresseTab.length; i++) {
                    if (adresseTab[i].length() > 0) {
                        fileOut.write("<" + CPVDWritter.TAG_ADRLIGNE + (i + 1) + ">");
                        fileOut.write(adresseTab[i].replaceAll("&", " et "));
                        fileOut.write("</" + CPVDWritter.TAG_ADRLIGNE + (i + 1) + ">");
                    }
                }
                fileOut.write(CPVDWritter.UNTAG_ADRESSEIDV);
            }
        }
    }

    private void createDateNaissance(FileWriter fileOut, CPCommunicationFiscaleAffichage entity) throws Exception {
        if (entity.getTiers() != null) {
            fileOut.write(CPVDWritter.TAG_DATENAISSANCE + entity.getTiers().getDateNaissance().replaceAll("&", " et ")
                    + CPVDWritter.UNTAG_DATENAISSANCE);
        }
    }

    private void createNom(FileWriter fileOut, CPCommunicationFiscaleAffichage entity) throws IOException {
        if (entity.getNom() != null) {
            fileOut.write(CPVDWritter.TAG_NOM + entity.getNom().replaceAll("&", " et ") + CPVDWritter.UNTAG_NOM);
        }
    }

    private void createNumAFF(FileWriter fileOut, CPCommunicationFiscaleAffichage entity) throws IOException {
        if (entity.getNumAffilie() != null) {
            fileOut.write(CPVDWritter.TAG_NUMAFF + entity.getNumAffilie() + CPVDWritter.UNTAG_NUMAFF);
        }
    }

    private void createNumAVS(FileWriter fileOut, String numAvs) throws IOException {
        fileOut.write(CPVDWritter.TAG_NUMAVS + numAvs + CPVDWritter.UNTAG_NUMAVS);
    }

    private void createNumContribuable(FileWriter fileOut, String numContri) throws IOException, Exception {
        if (numContri.length() < 8) {
            int tailleMin = 8;
            int difference = tailleMin - numContri.length();
            String numero = numContri;
            for (int i = 0; i < difference; i++) {
                numero += "0";
            }
            fileOut.write(CPVDWritter.TAG_NUMCTB + numero + CPVDWritter.UNTAG_NUMCTB);
        } else {
            fileOut.write(CPVDWritter.TAG_NUMCTB + numContri + CPVDWritter.UNTAG_NUMCTB);
        }
    }

    private void createNumNSS(FileWriter fileOut, String numAvs13) throws IOException {
        fileOut.write(CPVDWritter.TAG_NUMNSS + numAvs13 + CPVDWritter.UNTAG_NUMNSS);
    }

    private void createPeriode(FileWriter fileOut, CPCommunicationFiscaleAffichage entity) throws Exception {
        String dateDeb = "";
        String dateFin = "";
        AFAffiliation affiliation = new AFAffiliation();
        affiliation.setSession(getSession());
        affiliation.setId(entity.getIdAffiliation());
        affiliation.retrieve();
        if ((!JACalendar.isNull(affiliation.getDateDebut()))
                && ((!JadeStringUtil.isEmpty(entity.getAnneeRevenuDebut())))) {
            if (JACalendar.getYear(affiliation.getDateDebut()) == JadeStringUtil.parseInt(entity.getAnneeRevenuDebut(),
                    0)) {
                dateDeb = affiliation.getDateDebut();
            } else {
                dateDeb = "01.01." + entity.getAnneeRevenuDebut();
            }
        } else {
            if (entity.getAnneeRevenuDebut().length() > 0) {
                dateDeb = "01.01." + entity.getAnneeRevenuDebut();
            } else {
                dateDeb = "";
            }
        }
        if ((!JACalendar.isNull(affiliation.getDateFin())) && ((!JadeStringUtil.isEmpty(entity.getAnneeRevenuFin())))) {
            if (JACalendar.getYear(affiliation.getDateFin()) == JadeStringUtil.parseInt(entity.getAnneeRevenuFin(), 0)) {
                dateFin = affiliation.getDateFin();
            } else {
                dateFin = "31.12." + entity.getAnneeRevenuFin();
            }
        } else {
            if (entity.getAnneeRevenuFin().length() > 0) {
                dateFin = "31.12." + entity.getAnneeRevenuFin();
            } else {
                dateFin = "";
            }
        }
        fileOut.write(CPVDWritter.TAG_DEBPERIODE);
        if (dateDeb.length() > 0) {
            fileOut.write(dateDeb);
        }
        fileOut.write(CPVDWritter.UNTAG_DEBPERIODE);
        fileOut.write(CPVDWritter.TAG_FINPERIODE);
        if (dateFin.length() > 0) {
            fileOut.write(dateFin);
        }
        fileOut.write(CPVDWritter.UNTAG_FINPERIODE);
    }

    private void createPeriodeFiscale(FileWriter fileOut, CPCommunicationFiscaleAffichage entity) throws IOException {
        if (entity.getAnneeDecision() != null) {
            fileOut.write(CPVDWritter.TAG_PERFISC + entity.getAnneeDecision() + CPVDWritter.UNTAG_PERFISC);
        }
    }

    private void createPrenom(FileWriter fileOut, CPCommunicationFiscaleAffichage entity) throws IOException {
        if (entity.getPrenom() != null) {
            fileOut.write(CPVDWritter.TAG_PRENOM + entity.getPrenom().replaceAll("&", " et ")
                    + CPVDWritter.UNTAG_PRENOM);
        }
    }

    private void createTypeCommunication(FileWriter fileOut, CPCommunicationFiscaleAffichage entity) throws IOException {
        if (entity.getGenreAffilie() != null) {
            String codeType = "";
            if (entity.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_AGRICULTEUR)) {
                // codeType = CODE_AGRICULTEUR; -> plus utilisé (séance du
                // 20.01.09)
                codeType = CPVDWritter.CODE_INDEPENDANT;
            } else if (entity.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_NON_ACTIF)) {
                codeType = CPVDWritter.CODE_SANS_ACTIVITE_LUCRATIVE;
            } else if (entity.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_TSE)) {
                codeType = CPVDWritter.CODE_SALARIE_SANS_EMPLOYEUR;
            } else {
                codeType = CPVDWritter.CODE_INDEPENDANT;
            }
            fileOut.write(CPVDWritter.TAG_TYPCOMM + codeType + CPVDWritter.UNTAG_TYPCOMM);
        }
    }

    private void creationEntete(FileWriter fileOut) throws Exception {
        fileOut.write(CPVDWritter.TAG_VERSION);
        fileOut.write(CPVDWritter.TAG_ENTETE);
        fileOut.write(CPVDWritter.TAG_ENTETEDEM);
        fileOut.write(CPVDWritter.TAG_IDTIERS);
        fileOut.write(CPVDWritter.TAG_NOMTIERS);
        if (getAgence() != null) {
            if (getAgence().getNom().length() >= 50) {
                fileOut.write(getAgence().getNom().substring(0, 50).replaceAll("&", " et "));
            } else {
                fileOut.write(getAgence().getNom().replaceAll("&", " et "));
            }
        }
        fileOut.write(CPVDWritter.UNTAG_NOMTIERS);
        fileOut.write(CPVDWritter.TAG_NUMTIERS);
        if (getNumCaisse() != null) {
            fileOut.write(getNumCaisse().replaceAll("&", " et "));
        }
        fileOut.write(CPVDWritter.UNTAG_NUMTIERS);
        fileOut.write(CPVDWritter.TAG_ADRTIERS);
        if (getAgence() != null) {
            if (getAgence().getAdresseAsString() != null) {
                writeAdresse(fileOut);
            }
        }
        fileOut.write(CPVDWritter.UNTAG_ADRTIERS);
        fileOut.write(CPVDWritter.UNTAG_IDTIERS);
        fileOut.write(CPVDWritter.TAG_IDDEMANDE);
        fileOut.write(CPVDWritter.TAG_NUMDEMANDE);
        fileOut.write(CPVDWritter.NUMERO_DEMANDE);
        fileOut.write(CPVDWritter.UNTAG_NUMDEMANDE);
        fileOut.write(CPVDWritter.TAG_DATEDEMANDE);
        fileOut.write(JACalendar.todayJJsMMsAAAA());
        fileOut.write(CPVDWritter.UNTAG_DATEDEMANDE);
        fileOut.write(CPVDWritter.TAG_NOMRESP);
        if (getSession() != null) {
            fileOut.write(getSession().getUserName().replaceAll("&", " et "));
        }
        fileOut.write(CPVDWritter.UNTAG_NOMRESP);
        fileOut.write(CPVDWritter.TAG_EMAILRESP);
        if (getSession() != null) {
            fileOut.write(getSession().getUserEMail().replaceAll("&", " et "));
        }
        fileOut.write(CPVDWritter.UNTAG_EMAILRESP);
        fileOut.write(CPVDWritter.TAG_TELEPHONE);
        // TODO OCA aller rechercher telephone user en session
        fileOut.write(CPVDWritter.UNTAG_TELEPHONE);
        fileOut.write(CPVDWritter.UNTAG_IDDEMANDE);
        fileOut.write(CPVDWritter.TAG_INFODEMANDE);
        fileOut.write(CPVDWritter.TAG_URGENCEDEMANDE);
        fileOut.write(CPVDWritter.DEMANDE_URGENTE);
        fileOut.write(CPVDWritter.UNTAG_URGENCEDEMANDE);
        fileOut.write(CPVDWritter.UNTAG_INFODEMANDE);
        fileOut.write(CPVDWritter.UNTAG_ENTETEDEM);
    }

    private void creationFooter(FileWriter fileOut, int managerSize) throws IOException {
        fileOut.write(CPVDWritter.TAG_QUEUE);
        fileOut.write(CPVDWritter.TAG_NBRE);
        fileOut.write("" + managerSize);
        fileOut.write(CPVDWritter.UNTAG_NBRE);
        fileOut.write(CPVDWritter.UNTAG_QUEUE);
        fileOut.write(CPVDWritter.TAG_FOOTER);
    }

    private void creationIndividuFooter(FileWriter fileOut) throws IOException {
        fileOut.write(CPVDWritter.UNTAG_INDIVIDU);

    }

    private void creationIndividuHeader(FileWriter fileOut) throws IOException {
        fileOut.write(CPVDWritter.TAG_INDIVIDU);
    }

    private TIAdministrationViewBean getAgence() {
        try {
            if (getNumCaisse() != null) {
                TIAdministrationManager admin = new TIAdministrationManager();
                admin.setSession(getSession());
                admin.setForGenreAdministration(TIAdministrationViewBean.CS_CAISSE_COMPENSATION);
                admin.setForCodeAdministration(getNumCaisse());
                admin.find();
                TIAdministrationViewBean vb = (TIAdministrationViewBean) admin.getFirstEntity();
                return vb;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public String getDate_impression() {
        return date_impression;
    }

    public String getFilename() {
        return filename;
    }

    private String getNumCaisse() {
        try {
            return (getSession().getApplication()).getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE);
        } catch (Exception e) {
            return "";
        }
    }

    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    public BSession getSession() {
        return session;
    }

    public String getUser() {
        return user;
    }

    public void setDate_impression(String string) {
        date_impression = string;
    }

    public void setProcessAppelant(BProcess processAppelant) {
        this.processAppelant = processAppelant;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setUser(String string) {
        user = string;
    }

    private void writeAdresse(FileWriter fileOut) throws Exception {
        String adresseTab[] = new String[2];
        // On parcourt l'adresse
        TIAdresseDataSource adresse = getAgence().getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                "519005", "", true);
        if (adresse != null) {
            Hashtable<?, ?> data = adresse.getData();
            data = adresse.getData();
            adresseTab[0] = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE.replaceAll("&", " et "))
                    + " " + (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO.replaceAll("&", " et "));
            adresseTab[1] = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA.replaceAll("&", " et "))
                    + " " + (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE.replaceAll("&", " et "));
        }
        for (int i = 0; i < adresseTab.length; i++) {
            if ((adresseTab[i] != null) && (adresseTab[i].length() > 0)) {
                fileOut.write("<" + CPVDWritter.TAG_ADRLIGNE + (i + 1) + ">");
                fileOut.write(adresseTab[i]);
                fileOut.write("</" + CPVDWritter.TAG_ADRLIGNE + (i + 1) + ">");
            }
        }
    }
}
