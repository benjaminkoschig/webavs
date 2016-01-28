/*
 * Créé le 10 août 05
 * 
 * Attention: la convention de nomage des fichier doit être conservée
 */
package globaz.phenix.process.communications;

import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.xml.JadeXmlReader;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.log.JadeLogger;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.communications.CPReceptionReader;
import globaz.phenix.db.communications.CPReceptionReaderManager;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author mmu Charge les communications fiscales dans la BD en 2 étapes: <LI>1. Transformation du fichier reçu en
 *         fichier xml standard: conforme au schéma définit en interne. Le document peut être découpé en plusieurs
 *         fichiers xml contenant chaqun n communications fiscales. <BR>
 *         Le nombre n est définit dans le fichier PHENIX.properties sous la propiété: NbComMax <LI>2. Transforme les
 *         documents xml en CPCommunicationFiscalRetour (BEntity) et les ajoute <BR>
 *         Les documents ascii/xml reçus sont enregistré dans le répertoire:
 *         \phenixRoot\work\InputReceptionCommunication <BR>
 *         Les documents transformés au format xml définit sont enregistrés dans le répertoire
 *         \phenixRoot\work\FormatedReceptionCommunication
 *         <P>
 *         <BR>
 *         Ce process peut être applé dans trois contextes distinct:<BR>
 *         <LI>1. Récéption de communications sans contexte, appelé à partir du menu principal, ce opération recéptionne
 *         les données en créant un nouveau journal <BR>
 *         est définit par la constante RECEPTION_TOLAL_CREER_JOURNAL
 *         <LI>2. Réception de communications dans un journal déjà crée, appelé à partir du menu contextuel d'un journal
 *         de communication. <BR>
 *         !! un journal ne peut réceptionner qu'un seul fichier <BR>
 *         est définit par la constante RECEPTION_TOTAL
 *         <LI>3. Réception de communication après un échec de réception, appelé à partir du menu contextuel d'un
 *         journal de communication. <BR>
 *         Cette opération est a lieu quand le fichier a déjà été réceptionné mais que toutes les communications n'ont
 *         pas été insérées <BR>
 *         est définit par la constante RECEPTIONNER_ENCORE <BR>
 *         Le type de réception doit être fourni au process via la méthode setTypeReception()
 */

/*
 * Ce process peut être appélé @author mmu
 */
public class CPProcessReceptionCommunication extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String RECEPTION_TOLAL_CREER_JOURNAL = "1";
    public final static String RECEPTION_TOTAL = "2";
    public final static String RECEPTIONNER_ENCORE = "3";

    private String csCanton = null;

    private String dateReception = "";
    private String idJournalRetour = "";
    private String inputFileName = "";

    private boolean isXMLFormat = false;
    private CPJournalRetour journalRetour = null;
    private String libelleJournal = "";
    private String nbCommunication = "";
    private int nbXmlFiles = 0;

    private String nomFichier = "";
    private String nomFichierXml;

    private String readerPath = "";

    private String typeReception = "";

    // relatif à la transformation ascii/xml --> xml
    private ArrayList<String> xmlFileProduced = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        boolean successful = false;
        // La réception peut se faire dans trois contexte:
        try {
            _setReader();
            // Recherche variable si mise à jour du numéro de contribuable
            String majNumContribuable = ((CPApplication) getSession().getApplication()).getMajNumContribuable();
            // RECEPTIONNER ET CREER JOURNAL: On veut receptionner un fichier
            // et l'inscrire comme un nouveau journal
            if (CPProcessReceptionCommunication.RECEPTION_TOLAL_CREER_JOURNAL.equals(typeReception)) {
                successful = lireFichierRecu();
                if (!successful) {
                    return false;
                }
                successful = creerJournal();
                if (!successful) {
                    return false;
                }

                if (isXMLFormat) {
                    successful = insererCommunicationXML(majNumContribuable);
                } else {
                    successful = insererCommunication(majNumContribuable);
                }

                // RECEPTIONNER SEULEMENT: On veut receptionner un fichier
                // mais un journal existe déjà.
            } else if (CPProcessReceptionCommunication.RECEPTION_TOTAL.equals(typeReception)) {
                successful = lireFichierRecu();
                if (!successful) {
                    return false;
                }
                successful = majJournal();
                if (!successful) {
                    return false;
                }
                if (isXMLFormat) {
                    successful = insererCommunicationXML(majNumContribuable);
                } else {
                    successful = insererCommunication(majNumContribuable);
                    // RECEPTIONNER ENCORE: Si le fichier xml a été généré
                    // mais que les données n'ont pu être insérées dans la base,
                    // on refait une tentative
                }
            } else if (CPProcessReceptionCommunication.RECEPTIONNER_ENCORE.equals(typeReception)) {
                journalRetour = getJournalRetour();
                String nomFichier = journalRetour.getNomFichier();
                xmlFileProduced = getFichiersXmlAReprendre(nomFichier);
                if (isXMLFormat) {
                    successful = insererCommunicationXML(majNumContribuable);
                } else {
                    successful = insererCommunication(majNumContribuable);
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    getSession().getLabel("PROCRECEPTCOM_ERROR_RECEPTION_IMPOSSIBLE") + e.getMessage(),
                    FWMessage.ERREUR, "CPProcessReceptionCommunication");
            successful = false;
        }
        return successful;
    }

    /**
	 *
	 */
    private void _setReader() throws Exception {
        CPReceptionReaderManager cantonList = new CPReceptionReaderManager();
        cantonList.setSession(getSession());
        cantonList.setForIdCanton(getCsCanton());
        cantonList.find(getTransaction());
        if (cantonList.size() > 0) {
            CPReceptionReader reader = (CPReceptionReader) cantonList.getFirstEntity();
            setXMLFormat(reader.isFormatXml());
            setReaderPath(reader.getNomClass());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
        }
        int typeRecept = Integer.parseInt(typeReception);
        if ((typeRecept <= 0) || (typeRecept > 3)) {
            getSession().addError(getSession().getLabel("PROCRECEPTCOM_ERROR_NO_TYPERECEPTION"));
        }
        if ((typeReception == CPProcessReceptionCommunication.RECEPTIONNER_ENCORE)
                || (typeReception == CPProcessReceptionCommunication.RECEPTION_TOTAL)) {
            if (JadeStringUtil.isIntegerEmpty(getIdJournalRetour())) {
                getSession().addError(getSession().getLabel("PROCRECEPTCOM_ERROR_NO_IDJOURNAL"));
            }
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        if (getSession().hasErrors()) {
            abort();
        }
    }

    /*
     * CREATION DU JOURNAL DE RETOUR
     */
    private boolean creerJournal() {
        CPJournalRetour journal = new CPJournalRetour();
        BITransaction transaction = null;
        try {
            journal.setSession(getSession());
            journal.setLibelleJournal(getLibelleJournal());
            journal.setNbFichierTotal(String.valueOf(nbXmlFiles));
            journal.setNbFichierReussit("0");
            journal.setNbCommunication(getNbCommunication());
            journal.setCanton(getCsCanton());
            journal.setStatus(CPJournalRetour.CS_RECEPTION_PARTIEL);
            journal.setTypeJournal(CPJournalRetour.CS_TYPE_JOURNAL_ANCIEN);
            if (nbXmlFiles != 0) {
                if (isXMLFormat) {
                    journal.setNomFichier(nomFichierXml);
                } else {
                    journal.setNomFichier(xmlFileProduced.get(0));
                }
            } else {
                throw new Exception(getSession().getLabel("PROCRECEPTCOM_ERROR_FICHIER_SANS_COMM"));
            }
            journal.setDateReception(JACalendar.todayJJsMMsAAAA());
            transaction = getSession().newTransaction();
            transaction.openTransaction();
            journal.add(transaction);
            setIdJournalRetour(journal.getIdJournalRetour());
            if (!transaction.hasErrors()) {
                transaction.commit();
            } else {
                throw new Exception(journal.getErrors().toString());
            }
            // garde le journal dans l'objet pour l'insertion
            journalRetour = journal;
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    getMemoryLog().logMessage(
                            getSession().getLabel("PROCRECEPTCOM_ERROR_CREATION_JOURNAL") + e1.getMessage() + " ("
                                    + e1.getClass().toString() + ")", FWMessage.ERREUR,
                            "CPProcessReceptionCommunication");
                }
            }
            getMemoryLog().logMessage(getSession().getLabel("PROCRECEPTCOM_ERROR_CREATION_JOURNAL") + e.getMessage(),
                    FWMessage.ERREUR, "CPProcessReceptionCommunication");
            return false;
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    getMemoryLog().logMessage(
                            getSession().getLabel("PROCRECEPTCOM_ERROR_CREATION_JOURNAL") + e.getMessage() + " ("
                                    + e.getClass().toString() + ")", FWMessage.ERREUR,
                            "CPProcessReceptionCommunication");
                }
            }
        }
    }

    /**
     * @return
     */
    public String getCsCanton() {
        return csCanton;
    }

    /**
     * @return
     */
    public String getDateReception() {
        return dateReception;
    }

    private Document getDocument(String lfile) {
        try {
            return JadeXmlReader.parseFile(new FileInputStream(lfile));
        } catch (Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCRECEPTCOM_EMAIL_OBJECT");
    }

    private ArrayList<String> getFichiersXmlAReprendre(String nFichier) {
        ArrayList<String> fileList = new ArrayList<String>();
        int nbFichierCur = Integer.parseInt(journalRetour.getNbFichierReussit());
        int nbFichierTotal = Integer.parseInt(journalRetour.getNbFichierTotal());
        // Recherche de la position du nombre
        int indexDebut = nFichier.indexOf("#");// retrouver la base
        while (indexDebut < nFichier.indexOf("#", indexDebut)) {
            indexDebut = nFichier.indexOf("#", indexDebut);
        }
        String baseFichier = nFichier.substring(0, indexDebut);
        int indexFin = nFichier.indexOf(".", indexDebut);
        String finFichier = nFichier.substring(indexFin);

        for (int i = nbFichierCur; i < nbFichierTotal; i++) {
            String nom = baseFichier + "#" + i + finFichier;
            if (new File(nom).exists()) {
                fileList.add(nom);
            } else {
                getMemoryLog().logMessage(
                        getSession().getLabel("PROCRECEPTCOM_ERROR_RERECEPTION_FILE_NOT_FOUND") + nom,
                        FWMessage.ERREUR, "CPProcessReceptionCommunication");
            }
        }
        return fileList;
    }

    /**
     * @return
     */
    public String getIdJournalRetour() {
        return idJournalRetour;
    }

    /**
     * @return
     */
    public String getInputFileName() {
        return inputFileName;
    }

    // Retrouve le journal
    private CPJournalRetour getJournalRetour() throws Exception {
        // Recherche le journal
        CPJournalRetour journal = new CPJournalRetour();
        journal.setSession(getSession());
        journal.setIdJournalRetour(getIdJournalRetour());
        journal.retrieve();
        if (journal.isNew()) {
            String message = (journal.hasErrors() ? (": " + journal.getErrors().toString()) : "");
            throw new Exception(getSession().getLabel("PROCRECEPTCOM_ERROR_JOURNAL_NOT_FOUND") + message);
        }
        return journal;
    }

    /**
     * @return
     */
    public String getLibelleJournal() {
        return libelleJournal;
    }

    /**
     * @return
     */
    public String getNbCommunication() {
        return nbCommunication;
    }

    /**
     * @return
     */
    public String getNomFichier() {
        return nomFichier;
    }

    private String getNomXSLT() {
        try {
            Class<?> cl = Class.forName(getReaderPath());
            return cl.getName().substring(cl.getName().lastIndexOf('.') + 1, cl.getName().length());
        } catch (Exception e1) {
            getMemoryLog().logMessage(
                    getSession().getLabel("PROCRECEPTCOM_ERROR_READER_INSTANCIATION") + e1.getMessage(),
                    FWMessage.ERREUR, "CPProcessReceptionCommunication");
            return "";
        }
    }

    /**
     * @return
     */
    public String getReaderPath() {
        return readerPath;
    }

    public String getTypeReception() {
        return typeReception;
    }

    /*
     * TRANSFORMATION DES DOCUMENTS XML EN BENTITY ET INSERTION DANS LA BD
     */
    private boolean insererCommunication(String majNumContribuable) throws Exception {
        CPProcessXmlToEntity xmlToE = new CPProcessXmlToEntity();
        xmlToE.setParentWithCopy(this);
        xmlToE.setSendMailOnError(false);
        xmlToE.setInputFileName(xmlFileProduced);
        xmlToE.setJournalRetour(journalRetour);
        xmlToE.setMajNumContribuable(majNumContribuable);
        xmlToE.executeProcess();
        return true;
    }

    private boolean insererCommunicationXML(String majNumContribuable) throws Exception {
        CPProcessXmlReader xmlReaderImpl = null;
        try {
            Class<?> cl = Class.forName(getReaderPath());
            xmlReaderImpl = (CPProcessXmlReader) cl.newInstance();
        } catch (Exception e1) {
            getMemoryLog().logMessage(
                    getSession().getLabel("PROCRECEPTCOM_ERROR_READER_INSTANCIATION") + e1.getMessage(),
                    FWMessage.ERREUR, "CPProcessReceptionCommunication");
            return false;
        }
        xmlReaderImpl.setXmlFile(nomFichierXml);
        xmlReaderImpl.setParentWithCopy(this);
        xmlReaderImpl.setSendMailOnError(false);
        xmlReaderImpl.setJournalRetour(journalRetour);
        xmlReaderImpl.setMajNumContribuable(majNumContribuable);
        xmlReaderImpl.executeProcess();
        return true;
    }

    /**
     * @return
     */
    public boolean isXMLFormat() {
        return isXMLFormat;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /*
     * Lecture du fichier de reception et transformation en xml les fichiers xml générés se trouve dans le tableau
     * xmlFileProduced
     */
    private boolean lireFichierRecu() throws Exception {
        if (isXMLFormat()) {
            /*
             * 1. TRANSFORMATION DU FICHIER XML EN XML via XSLT
             */
            // Process the file using xslt
            // On recoit le fichier XML
            String XMLfile = Jade.getInstance().getHomeDir() + "work/" + getInputFileName();
            // On lit le fichier
            // On copie le fichier brut en bd
            JadeFsFacade.copyFile("jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + getInputFileName(),
                    XMLfile);
            // Création de la source DOM
            Document document = getDocument(XMLfile);
            // On crée le fichierXML de base
            transformFile(XMLfile, document, getNomXSLT());
            // On va lire le fichier XML créé, afin de lui soutirer des
            // informations importantes
            // Nous avons les moyens de vous faire parler
            // Création de la source DOM
            Document documentTransforme = getDocument(XMLfile);
            // On reseigne les champs nécessaires au process
            NodeList elements = CPCommunicationsUtils.getEnteteCommunication(documentTransforme);
            setNbCommunication(CPCommunicationsUtils.getNbreReceptionCom(elements));
            setDateReception(CPCommunicationsUtils.getDateReceptionCom(elements));
            // On set a un, car on a un seul fichier XML, du fait que l'on fait
            // que de transformer LE fichier XML reçu par le fisc.
            nbXmlFiles = 1;
            nomFichierXml = XMLfile;
            return true;

            // getMemoryLog().logMessage(getSession().getLabel("PROCRECEPTCOM_ERROR_XML_TRANSFORMATION_NOT_IMPLEMENTED"),
            // FWMessage.ERREUR, "CPProcessReceptionCommunication");
            // return false;
        } else { // ascii
            /*
             * 1. TRANSFORMATION DU FICHIER ASCII EN XML
             */
            // Instancier le reader
            CPProcessAsciiReader asciiReaderImpl = null;
            try {
                Class<?> cl = Class.forName(getReaderPath());
                asciiReaderImpl = (CPProcessAsciiReader) cl.newInstance();
            } catch (Exception e1) {
                getMemoryLog().logMessage(
                        getSession().getLabel("PROCRECEPTCOM_ERROR_READER_INSTANCIATION") + e1.getMessage(),
                        FWMessage.ERREUR, "CPProcessReceptionCommunication");
                return false;
            }
            asciiReaderImpl.setParentWithCopy(this);
            asciiReaderImpl.setSendMailOnError(false);
            JadeFsFacade.copyFile("jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + getInputFileName(),
                    Jade.getInstance().getHomeDir() + "work/" + getInputFileName());

            asciiReaderImpl.setInputFileName(Jade.getInstance().getHomeDir() + "work/" + getInputFileName());
            String outputFileLocation = Jade.getInstance().getHomeDir()
                    + "phenixRoot/work/FormatedReceptionCommunication/";
            asciiReaderImpl.setOutputDirectory(outputFileLocation);
            // D:/Websphere/webavs_workspace/webavs/Web Content/

            // Lance le process
            asciiReaderImpl.executeProcess();
            setDateReception(asciiReaderImpl.getDateReception());
            // recherche le nom les fichiers créés
            xmlFileProduced = asciiReaderImpl.getOutputFilePath();
            setNbCommunication(asciiReaderImpl.getNbEntreesLues());
            nbXmlFiles = xmlFileProduced.size();
            return true;
        }
    }

    /**
     * @return
     */
    private boolean majJournal() {
        BITransaction transaction = null;
        try {
            CPJournalRetour journal = getJournalRetour();
            // Met à jour les champs
            journal.setDateReception(getDateReception());
            journal.setNbFichierTotal(String.valueOf(nbXmlFiles));
            journal.setNbFichierReussit("0");
            journal.setNbCommunication(getNbCommunication());
            journal.setStatus(CPJournalRetour.CS_RECEPTION_PARTIEL);
            if (nbXmlFiles != 0) {
                journal.setNomFichier(xmlFileProduced.get(0));
            } else {
                throw new Exception(getSession().getLabel("PROCRECEPTCOM_ERROR_FICHIER_SANS_COMM"));
            }
            transaction = getSession().newTransaction();
            transaction.openTransaction();

            journal.update(transaction);
            if (!transaction.hasErrors()) {
                transaction.commit();
            } else {
                throw new Exception(journal.getErrors().toString());
            }
            // garde le journal dans l'objet pour l'insertion
            journalRetour = journal;
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    getMemoryLog().logMessage(
                            getSession().getLabel("PROCRECEPTCOM_ERROR_CREATION_JOURNAL") + e1.getMessage() + " ("
                                    + e1.getClass().toString() + ")", FWMessage.ERREUR,
                            "CPProcessReceptionCommunication");
                }
            }
            getMemoryLog().logMessage(
                    getSession().getLabel("PROCRECEPTCOM_ERROR_CREATION_JOURNAL") + e.getMessage() + " ("
                            + e.getClass().toString() + ")", FWMessage.ERREUR, "CPProcessReceptionCommunication");
            return false;
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    getMemoryLog().logMessage(
                            getSession().getLabel("PROCRECEPTCOM_ERROR_CREATION_JOURNAL") + e.getMessage() + " ("
                                    + e.getClass().toString() + ")", FWMessage.ERREUR,
                            "CPProcessReceptionCommunication");
                }
            }
        }
    }

    /**
     * @param string
     */
    public void setCsCanton(String string) {
        csCanton = string;
    }

    /**
     * @param string
     */
    public void setDateReception(String string) {
        dateReception = string;
    }

    /**
     * @param string
     */
    public void setIdJournalRetour(String string) {
        idJournalRetour = string;
    }

    /**
     * @param string
     */
    public void setInputFileName(String string) {
        inputFileName = string;
    }

    /**
     * @param string
     */
    public void setLibelleJournal(String string) {
        libelleJournal = string;
    }

    /**
     * @param string
     */
    public void setNbCommunication(String string) {
        nbCommunication = string;
    }

    /**
     * @param string
     */
    public void setNomFichier(String string) {
        nomFichier = string;
    }

    /**
     * @param string
     */
    public void setReaderPath(String string) {
        readerPath = string;
    }

    /**
     * @param i
     */
    public void setTypeReception(String i) {
        typeReception = i;
    }

    /**
     * @param b
     */
    public void setXMLFormat(boolean b) {
        isXMLFormat = b;
    }

    private void transformerXml(Transformer transformer, String fichierSource, Document document) {
        try {
            // Création du fichier de sortie
            Result resultat = new StreamResult(fichierSource);
            // Transformation
            transformer.transform(new DOMSource(document), resultat);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    private void transformFile(String source, Document document, String nomXSLT) {
        try {
            // création de la fabrique
            TransformerFactory fabrique = TransformerFactory.newInstance();
            // récupération du transformeur
            String stylePath = Jade.getInstance().getHomeDir() + "phenixRoot/xslt/" + nomXSLT + ".xslt";
            File stylesheet = new File(stylePath);
            StreamSource stylesource = new StreamSource(stylesheet);
            Transformer transformer = null;
            transformer = fabrique.newTransformer(stylesource);
            // configuration du transformeur
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformerXml(transformer, source, document);
        } catch (Exception e) {
            getMemoryLog().logMessage(getSession().getLabel("PROCRECEPTCOM_ERROR_XML_TRANSFORMATION"),
                    FWMessage.ERREUR, "CPProcessReceptionCommunication");
        }
    }

}
