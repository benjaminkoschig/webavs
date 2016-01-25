/*
 * Créé le 10 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.process.communications;

import globaz.framework.util.FWLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourGEViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourJUViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourNEViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVSViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.divers.CPPeriodeFiscaleManager;
import globaz.phenix.mapping.retour.castor.Communication;
import globaz.phenix.mapping.retour.castor.CommunicationReception;
import globaz.phenix.mapping.retour.castor.EnteteCommunication;
import globaz.phenix.mapping.retour.castor.ErrorItem;
import globaz.phenix.mapping.retour.castor.ErrorMessage;
import globaz.pyxis.constantes.IConstantes;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;

/**
 * @author mmu Transforme les documents xml en CPCommunicationFiscalRetour (BEntity) et les ajoute
 */
public class CPProcessXmlToEntity extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ArrayList inputFileName;

    private CPJournalRetour journalRetour = null;

    private String majNumContribuable = "";

    private int totalNbFile;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
        // TODO Raccord de méthode auto-généré

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        boolean successful = true;
        int curNbFile = 0;
        int curCommunication;

        setTotalNbFile(getInputFileName().size());
        try {
            // lit les fichiers un après un
            for (; curNbFile < getTotalNbFile(); curNbFile++) {
                FileReader reader = new FileReader((String) getInputFileName().get(curNbFile));
                // On utilise une transaction par fichier
                BITransaction transaction = null;
                try {
                    transaction = getSession().newTransaction();
                    transaction.openTransaction();
                    // Marshal l'objet CommunicationReception
                    CommunicationReception reception = (CommunicationReception) Unmarshaller.unmarshal(
                            CommunicationReception.class, reader);
                    EnteteCommunication entete = reception.getEnteteCommunication();
                    Communication communication = null;
                    int nbEnreg = reception.getCommunicationCount();
                    getParent().setProgressScaleValue(nbEnreg);
                    // Chaque communication du fichier est lue et insérée
                    for (curCommunication = 0; curCommunication < nbEnreg; curCommunication++) {
                        communication = reception.getCommunication(curCommunication);
                        communication.setMajNumContribuable(getMajNumContribuable());
                        addCommunicationRetour(communication, entete, transaction);
                        if (transaction.hasErrors()) {
                            throw new Exception(transaction.getErrors().toString());
                        }
                        getParent().incProgressCounter();
                    }

                    updateJournalNbReussit(transaction);

                    if (transaction.hasErrors()) {
                        throw new Exception(transaction.getErrors().toString());
                    }
                    transaction.commit();

                } catch (MarshalException e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    throw new Exception(getSession().getLabel("PROCXMLTOENT_ERROR_FIlE_READ") + e.getMessage() + "("
                            + e.getClass().toString() + ")");
                } catch (Exception e1) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    throw e1;
                } finally {
                    if (transaction != null) {
                        try {
                            transaction.closeTransaction();
                        } catch (Exception e) {

                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0146") + getInputFileName().get(curNbFile),
                    FWMessage.ERREUR, "CPProcessXmlToEntity");
            successful = false;
        } catch (Exception e) {
            this._addError(getSession().getLabel("CP_MSG_0136") + e.getMessage());
            successful = false;
        }
        return successful;

    }

    /**
     * Copie les attributs de la communication xml sur le BEntity
     * 
     * @param communication
     * @param communicationFiscale
     */
    private void addCommunicationRetour(Communication comXml, EnteteCommunication entete, BITransaction transaction)
            throws Exception {
        if (IConstantes.CS_LOCALITE_CANTON_NEUCHATEL.equalsIgnoreCase(journalRetour.getCanton())) {
            CPCommunicationFiscaleRetourNEViewBean comRetourNE = new CPCommunicationFiscaleRetourNEViewBean();
            this.setParameters(comXml, entete, comRetourNE, transaction);
            comRetourNE.add(transaction);
            String ikiretid = comRetourNE.getIdRetour();
            // backup
            comRetourNE = new CPCommunicationFiscaleRetourNEViewBean();
            this.setParameters(comXml, entete, comRetourNE, transaction);
            comRetourNE.setForBackup(true);
            comRetourNE.setIdRetour(ikiretid);
            comRetourNE.add(transaction);
        } else if (IConstantes.CS_LOCALITE_CANTON_JURA.equalsIgnoreCase(journalRetour.getCanton())) {
            CPCommunicationFiscaleRetourJUViewBean comRetourJU = new CPCommunicationFiscaleRetourJUViewBean();
            this.setParameters(comXml, entete, comRetourJU, transaction);
            comRetourJU.add(transaction);
            String ikiretid = comRetourJU.getIdRetour();
            // backup
            comRetourJU = new CPCommunicationFiscaleRetourJUViewBean();
            this.setParameters(comXml, entete, comRetourJU, transaction);
            comRetourJU.setForBackup(true);
            comRetourJU.setIdRetour(ikiretid);
            comRetourJU.setWantMajBackup(true);
            comRetourJU.add(transaction);
        } else if (IConstantes.CS_LOCALITE_CANTON_GENEVE.equalsIgnoreCase(journalRetour.getCanton())) {
            CPCommunicationFiscaleRetourGEViewBean comRetourGE = new CPCommunicationFiscaleRetourGEViewBean();
            this.setParameters(comXml, entete, comRetourGE, transaction);
            comRetourGE.add(transaction);
            String ikiretid = comRetourGE.getIdRetour();
            // backup
            comRetourGE = new CPCommunicationFiscaleRetourGEViewBean();
            this.setParameters(comXml, entete, comRetourGE, transaction);
            comRetourGE.setForBackup(true);
            comRetourGE.setIdRetour(ikiretid);
            comRetourGE.setWantMajBackup(true);
            comRetourGE.add(transaction);
        } else if (IConstantes.CS_LOCALITE_CANTON_VALAIS.equalsIgnoreCase(journalRetour.getCanton())) {
            CPCommunicationFiscaleRetourVSViewBean comRetourVS = new CPCommunicationFiscaleRetourVSViewBean();
            this.setParameters(comXml, entete, comRetourVS, transaction);
            comRetourVS.add(transaction);
            String ikiretid = comRetourVS.getIdRetour();
            // backup
            comRetourVS = new CPCommunicationFiscaleRetourVSViewBean();
            this.setParameters(comXml, entete, comRetourVS, transaction);
            comRetourVS.setForBackup(true);
            comRetourVS.setIdRetour(ikiretid);
            comRetourVS.setWantMajBackup(true);
            comRetourVS.add(transaction);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * @return retourne le nom du fichier d'un index donné
     */
    public ArrayList getInputFileName() {
        return inputFileName;
    }

    /**
     * @return
     */
    public CPJournalRetour getJournalRetour() {
        return journalRetour;
    }

    public String getMajNumContribuable() {
        return majNumContribuable;
    }

    /**
     * @return
     */
    public int getTotalNbFile() {
        return totalNbFile;
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

    /**
     * @param string
     */
    public void setInputFileName(ArrayList list) {
        inputFileName = list;
    }

    /**
     * @param retour
     */
    public void setJournalRetour(CPJournalRetour retour) {
        journalRetour = retour;
    }

    public void setMajNumContribuable(String majNumContribuable) {
        this.majNumContribuable = majNumContribuable;
    }

    /**
     * Copie les attributs de la communication xml sur le BEntity
     * 
     * @param communication
     * @param communicationFiscale
     */
    private void setParameters(Communication comXml, EnteteCommunication entete,
            CPCommunicationFiscaleRetourGEViewBean communicationFiscaleRetour, BITransaction transaction)
            throws Exception {

        // Ajout des zones génériques (de l'entité parent)
        this.setParameters(comXml, entete, (CPCommunicationFiscaleRetourViewBean) communicationFiscaleRetour,
                transaction);
        // Ajout des zones spécifiques GE
        communicationFiscaleRetour.setGeNumCaisse(comXml.getGeNumCaisse());
        communicationFiscaleRetour.setGeNumDemande(comXml.getGeNumDemande());
        communicationFiscaleRetour.setGeGenreAffilie(comXml.getGeGenreAffilie());
        communicationFiscaleRetour.setGeNumAffilie(comXml.getGeNumAffilie());
        communicationFiscaleRetour.setGeNumAvs(comXml.getGeNumAvs());
        communicationFiscaleRetour.setGeNNSS(comXml.getGeNNSS());
        communicationFiscaleRetour.setGeNumContribuable(comXml.getGeNumContribuable());
        communicationFiscaleRetour.setGeNom(comXml.getGeNom());
        communicationFiscaleRetour.setGeNomAFC(comXml.getGeNomAFC());
        communicationFiscaleRetour.setGePrenom(comXml.getGePrenom());
        communicationFiscaleRetour.setGePrenomAFC(comXml.getGePrenomAFC());
        communicationFiscaleRetour.setGeNumAvsConjoint(comXml.getGeNumAvsConjoint());
        communicationFiscaleRetour.setGeNomConjoint(comXml.getGeNomConjoint());
        communicationFiscaleRetour.setGePrenomConjoint(comXml.getGePrenomConjoint());
        communicationFiscaleRetour.setGeNumCommunication(comXml.getGeNumCommunication());
        if ("1".equalsIgnoreCase(comXml.getGePersonneNonIdentifiee())) {
            communicationFiscaleRetour.setGePersonneNonIdentifiee(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setGePersonneNonIdentifiee(Boolean.FALSE);
        }
        if ("1".equalsIgnoreCase(comXml.getGeImpotSource())) {
            communicationFiscaleRetour.setGeImpotSource(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setGeImpotSource(Boolean.FALSE);
        }
        if ("1".equalsIgnoreCase(comXml.getGeNonAssujettiIBO())) {
            communicationFiscaleRetour.setGeNonAssujettiIBO(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setGeNonAssujettiIBO(Boolean.FALSE);
        }
        if ("1".equalsIgnoreCase(comXml.getGePasActiviteDeclaree())) {
            communicationFiscaleRetour.setGePasActiviteDeclaree(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setGePasActiviteDeclaree(Boolean.FALSE);
        }
        if ("1".equalsIgnoreCase(comXml.getGeImpositionSelonDepense())) {
            communicationFiscaleRetour.setGeImpositionSelonDepense(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setGeImpositionSelonDepense(Boolean.FALSE);
        }
        if ("1".equalsIgnoreCase(comXml.getGeTaxationOffice())) {
            communicationFiscaleRetour.setGeTaxationOffice(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setGeTaxationOffice(Boolean.FALSE);
        }
        communicationFiscaleRetour.setGeObservations(comXml.getGeObservations());
        communicationFiscaleRetour.setGeDateTransfertMAD(comXml.getGeDateTransfertMAD());
        if ("1".equalsIgnoreCase(comXml.getGePension())) {
            communicationFiscaleRetour.setGePension(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setGePension(Boolean.FALSE);
        }
        if ("1".equalsIgnoreCase(comXml.getGeRetraite())) {
            communicationFiscaleRetour.setGeRetraite(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setGeRetraite(Boolean.FALSE);
        }
        if ("1".equalsIgnoreCase(comXml.getGeRenteVieillesse())) {
            communicationFiscaleRetour.setGeRenteVieillesse(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setGeRenteVieillesse(Boolean.FALSE);
        }
        if ("1".equalsIgnoreCase(comXml.getGeRenteInvalidite())) {
            communicationFiscaleRetour.setGeRenteInvalidite(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setGeRenteInvalidite(Boolean.FALSE);
        }
        if ("1".equalsIgnoreCase(comXml.getGePensionAlimentaire())) {
            communicationFiscaleRetour.setGePensionAlimentaire(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setGePensionAlimentaire(Boolean.FALSE);
        }
        if ("1".equalsIgnoreCase(comXml.getGeRenteViagere())) {
            communicationFiscaleRetour.setGeRenteViagere(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setGeRenteViagere(Boolean.FALSE);
        }
        if ("1".equalsIgnoreCase(comXml.getGeIndemniteJournaliere())) {
            communicationFiscaleRetour.setGeIndemniteJournaliere(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setGeIndemniteJournaliere(Boolean.FALSE);
        }
        if ("1".equalsIgnoreCase(comXml.getGeBourses())) {
            communicationFiscaleRetour.setGeBourses(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setGeBourses(Boolean.FALSE);
        }
        if ("1".equalsIgnoreCase(comXml.getGeDivers())) {
            communicationFiscaleRetour.setGeDivers(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setGeDivers(Boolean.FALSE);
        }
        if ("1".equalsIgnoreCase(comXml.getGeNonAssujettiIFD())) {
            communicationFiscaleRetour.setGeNonAssujettiIFD(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setGeNonAssujettiIFD(Boolean.FALSE);
        }
        communicationFiscaleRetour.setGeExplicationsDivers(comXml.getGeExplicationDivers());
        // Ajout des logs
        setParametersLog(comXml, communicationFiscaleRetour, transaction);
    }

    /**
     * Copie les attributs de la communication xml sur le BEntity
     * 
     * @param communication
     * @param communicationFiscale
     */
    private void setParameters(Communication comXml, EnteteCommunication entete,
            CPCommunicationFiscaleRetourJUViewBean communicationFiscaleRetour, BITransaction transaction)
            throws Exception {
        // Ajout des zones génériques (de l'entité parent)
        this.setParameters(comXml, entete, (CPCommunicationFiscaleRetourViewBean) communicationFiscaleRetour,
                transaction);
        // JU
        communicationFiscaleRetour.setJuCodeApplication(comXml.getJuFiller());
        communicationFiscaleRetour.setJuNumContribuable(comXml.getJuNumContribuable());
        communicationFiscaleRetour.setJuLot(comXml.getJuLot());
        communicationFiscaleRetour.setJuNbrJour1(comXml.getJuNbrJour1());
        communicationFiscaleRetour.setJuNbrJour2(comXml.getJuNbrJour2());
        communicationFiscaleRetour.setJuGenreAffilie(comXml.getJuGenreAffilie());
        communicationFiscaleRetour.setJuGenreTaxation(comXml.getJuGenreTaxation());
        if ("X".equalsIgnoreCase(comXml.getJuEpoux())) {
            communicationFiscaleRetour.setJuEpoux(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setJuEpoux(Boolean.FALSE);
        }
        communicationFiscaleRetour.setJuFiller(comXml.getJuFiller());
        if ("X".equalsIgnoreCase(comXml.getJuTaxeMan())) {
            communicationFiscaleRetour.setJuTaxeMan(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setJuTaxeMan(Boolean.FALSE);
        }
        communicationFiscaleRetour.setJuDateNaissance(comXml.getJuDateNaissance());
        communicationFiscaleRetour.setJuNewNumContribuable(comXml.getJuNewNumContribuable());
        // Ajoute les logs générés lors de la réception
        setParametersLog(comXml, communicationFiscaleRetour, transaction);
    }

    /**
     * Copie les attributs de la communication xml sur le BEntity
     * 
     * @param communication
     * @param communicationFiscale
     */
    private void setParameters(Communication comXml, EnteteCommunication entete,
            CPCommunicationFiscaleRetourNEViewBean communicationFiscaleRetour, BITransaction transaction)
            throws Exception {
        // Ajout des zones génériques (de l'entité parent)
        this.setParameters(comXml, entete, (CPCommunicationFiscaleRetourViewBean) communicationFiscaleRetour,
                transaction);
        // NE
        communicationFiscaleRetour.setNeFiller(comXml.getNeFiller());
        communicationFiscaleRetour.setNeNumAvs(comXml.getNeNumAvs());
        communicationFiscaleRetour.setNeNumCaisse(comXml.getNeNumCaisse());
        communicationFiscaleRetour.setNeGenreAffilie(comXml.getNeGenreAffilie());
        communicationFiscaleRetour.setNePremiereLettreNom(comXml.getNePremiereLettreNom());
        communicationFiscaleRetour.setNeNumContribuable(comXml.getNeNumContribuable());
        communicationFiscaleRetour.setNeNumCommune(comXml.getNeNumCommune());
        communicationFiscaleRetour.setNeNumBDP(comXml.getNeNumBDP());
        communicationFiscaleRetour.setNeNumClient(comXml.getNeNumClient());
        communicationFiscaleRetour.setNeDateDebutAssuj(comXml.getNeDateDebutAssuj());
        communicationFiscaleRetour.setNeGenreTaxation(comXml.getNeGenreTaxation());
        if ("2".equalsIgnoreCase(comXml.getNeTaxationRectificative())) {
            communicationFiscaleRetour.setNeTaxationRectificative(Boolean.TRUE);
        } else {
            communicationFiscaleRetour.setNeTaxationRectificative(Boolean.FALSE);
        }
        communicationFiscaleRetour.setNeFortuneAnnee1(comXml.getNeFortuneAnnee1());
        communicationFiscaleRetour.setNePensionAnnee1(comXml.getNePensionAnnee1());
        communicationFiscaleRetour.setNePension(comXml.getNePension());
        communicationFiscaleRetour.setNePensionAlimentaire1(comXml.getNePensionAlimentaire1());
        communicationFiscaleRetour.setNePensionAlimentaire(comXml.getNePensionAlimentaire());
        communicationFiscaleRetour.setNeRenteViagere1(comXml.getNeRenteViagere1());
        communicationFiscaleRetour.setNeRenteViagere(comXml.getNeRenteViagere());
        communicationFiscaleRetour.setNeIndemniteJour1(comXml.getNeIndemniteJour1());
        communicationFiscaleRetour.setNeIndemniteJour(comXml.getNeIndemniteJour());
        communicationFiscaleRetour.setNeRenteTotale1(comXml.getNeRenteTotale1());
        communicationFiscaleRetour.setNeRenteTotale(comXml.getNeRenteTotale());
        communicationFiscaleRetour.setNeDateValeur(comXml.getNeDateValeur());
        communicationFiscaleRetour.setNeDossierTaxe(comXml.getNeDossierTaxe());
        communicationFiscaleRetour.setNeDossierTrouve(comXml.getNeDossierTrouve());
        // Ajoute les logs générés lors de la réception
        setParametersLog(comXml, communicationFiscaleRetour, transaction);
    }

    /**
     * Copie les attributs de la communication xml sur le BEntity
     * 
     * @param communication
     * @param communicationFiscale
     */
    private void setParameters(Communication comXml, EnteteCommunication entete,
            CPCommunicationFiscaleRetourViewBean communicationFiscaleRetour, BITransaction transaction)
            throws Exception {
        // REMISE DES PARAMETRES DE LA COMMUNICATION FISCALE A ZERO
        communicationFiscaleRetour.setId("");
        communicationFiscaleRetour.setIdRetour("");
        communicationFiscaleRetour.setPeriodeFiscale(null);
        communicationFiscaleRetour.setCommunicationFiscale(null);
        communicationFiscaleRetour.setMajNumContribuable(comXml.getMajNumContribuable());

        // DONNEES DIRECTEMENT COPIABLES DEPUIS LA COMMUNICATION
        // !JadeStringUtil.isNull(comXml.getPeriodeIFD())
        if (JadeStringUtil.isNull(comXml.getAnnee1()) || comXml.getAnnee1().equals("")
                || comXml.getAnnee1().equals("0")) {
            if (!JadeStringUtil.isNull(comXml.getPeriodeIFD()) || !comXml.getPeriodeIFD().equals("")
                    || !comXml.getPeriodeIFD().equals("0")) {
                int varNum = Integer.parseInt(comXml.getPeriodeIFD());
                varNum = 2000 + varNum;
                communicationFiscaleRetour.setAnnee1(Integer.toString(varNum));
            }
        } else {
            communicationFiscaleRetour.setAnnee1(comXml.getAnnee1());
        }
        communicationFiscaleRetour.setAnnee2(comXml.getAnnee2());
        communicationFiscaleRetour.setGenreAffilie(comXml.getGenreAffilie());
        if (communicationFiscaleRetour.isNonActif()) {
            communicationFiscaleRetour.setFortune(comXml.getFortune());
        } else {
            communicationFiscaleRetour.setCapital(comXml.getCapital());
        }
        communicationFiscaleRetour.setCotisation1(comXml.getCotisation1());
        communicationFiscaleRetour.setCotisation2(comXml.getCotisation2());
        communicationFiscaleRetour.setDateFortune(comXml.getDateFortune());
        communicationFiscaleRetour.setGenreTaxation(comXml.getGenreTaxation());
        communicationFiscaleRetour.setIdCommunication(comXml.getIdDemande());
        communicationFiscaleRetour.setRevenu1(comXml.getRevenu1());
        communicationFiscaleRetour.setRevenu2(comXml.getRevenu2());
        if (!JadeStringUtil.isNull(comXml.getAnnee1()) || !comXml.getAnnee1().equals("")
                || !comXml.getAnnee1().equals("0")) {
            String numIfd = "";
            CPPeriodeFiscaleManager periode = new CPPeriodeFiscaleManager();
            periode.setSession(getSession());
            periode.setForAnneeDecisionDebut(comXml.getAnnee1());
            periode.find();
            if (periode.size() > 0) {
                numIfd = ((CPPeriodeFiscale) periode.getFirstEntity()).getNumIfd();
            }
            communicationFiscaleRetour.setNumIfd(numIfd);
        } else {
            communicationFiscaleRetour.setNumIfd(comXml.getPeriodeIFD());
        }
        communicationFiscaleRetour.setIdJournalRetour(journalRetour.getIdJournalRetour());
        communicationFiscaleRetour.setDateRetour(entete.getDateReceptionCom());
        communicationFiscaleRetour.setGeneration(Boolean.TRUE);
        communicationFiscaleRetour.setDebutExercice1(comXml.getDebutExercice1());
        communicationFiscaleRetour.setFinExercice1(comXml.getFinExercice1());
        communicationFiscaleRetour.setDebutExercice2(comXml.getDebutExercice2());
        communicationFiscaleRetour.setFinExercice2(comXml.getFinExercice2());
    }

    private void setParameters(Communication comXml, EnteteCommunication entete,
            CPCommunicationFiscaleRetourVSViewBean communicationFiscaleRetour, BITransaction transaction)
            throws Exception {
        // Ajout des zones génériques (de l'entité parent)
        this.setParameters(comXml, entete, (CPCommunicationFiscaleRetourViewBean) communicationFiscaleRetour,
                transaction);
        communicationFiscaleRetour.setNumContribuable(comXml.getVsNumCtb());
        // VS
        communicationFiscaleRetour.setVsNumCtb(comXml.getVsNumCtb());
        communicationFiscaleRetour.setVsAnneeTaxation(comXml.getVsAnneeTaxation());
        communicationFiscaleRetour.setVsDateDemandeCommunication(comXml.getVsDateDemandeCommunication());
        communicationFiscaleRetour.setVsDateCommunication(comXml.getVsDateCommunication());
        communicationFiscaleRetour.setVsDateTaxation(comXml.getVsDateTaxation());
        communicationFiscaleRetour.setVsCodeTaxation1(comXml.getVsCodeTaxation1());
        communicationFiscaleRetour.setVsCodeTaxation2(comXml.getVsCodeTaxation2());
        communicationFiscaleRetour.setVsTypeTaxation(comXml.getVsTypeTaxation());
        communicationFiscaleRetour.setVsNumAffilie(comXml.getVsNumAffilie());
        communicationFiscaleRetour.setVsNumAvsAffilie(comXml.getVsNumAvsAffilie());
        communicationFiscaleRetour.setVsDateNaissanceAffilie(comXml.getVsDateNaissanceAffilie());
        communicationFiscaleRetour.setVsDateDebutAffiliation(comXml.getVsDateDebutAffiliation());
        communicationFiscaleRetour.setVsDateFinAffiliation(comXml.getVsDateFinAffiliation());
        communicationFiscaleRetour.setVsNomAffilie(comXml.getVsNomAffilie());
        communicationFiscaleRetour.setVsAdresseAffilie1(comXml.getVsAdresseAffilie1());
        communicationFiscaleRetour.setVsAdresseAffilie2(comXml.getVsAdresseAffilie2());
        communicationFiscaleRetour.setVsAdresseAffilie3(comXml.getVsAdresseAffilie3());
        communicationFiscaleRetour.setVsAdresseAffilie4(comXml.getVsAdresseAffilie4());
        communicationFiscaleRetour.setVsNoPostalLocalite(comXml.getVsNoPostalLocalite());
        communicationFiscaleRetour.setVsNoCaisseAgenceAffilie(comXml.getVsNoCaisseAgenceAffilie());
        communicationFiscaleRetour.setVsNoCaisseProfessionnelleAffilie(comXml.getVsNoCaisseProfessionnelleAffilie());
        communicationFiscaleRetour.setVsDateDebutAffiliationCaisseProfessionnelle(comXml
                .getVsDateDebutAffiliationCaisseProfessionnelle());
        communicationFiscaleRetour.setVsDateFinAffiliationCaisseProfessionnelle(comXml
                .getVsDateFinAffiliationCaisseProfessionnelle());
        communicationFiscaleRetour.setVsNumAffilieInterneCaisseProfessionnelle(comXml
                .getVsNumAffilieInterneCaisseProfessionnelle());
        communicationFiscaleRetour.setVsCotisationAvsAffilie(comXml.getVsCotisationAvsAffilie());
        communicationFiscaleRetour.setVsEtatCivilAffilie(comXml.getVsEtatCivilAffilie());
        communicationFiscaleRetour.setVsSexeAffilie(comXml.getVsSexeAffilie());
        communicationFiscaleRetour.setVsNumAffilieConjoint(comXml.getVsNumAffilieConjoint());
        communicationFiscaleRetour.setVsNumAvsConjoint(comXml.getVsNumAvsConjoint());
        communicationFiscaleRetour.setVsDateNaissanceConjoint(comXml.getVsDateNaissanceConjoint());
        communicationFiscaleRetour.setVsDateDebutAffiliationConjoint(comXml.getVsDateDebutAffiliationConjoint());
        communicationFiscaleRetour.setVsDateFinAffiliationConjoint(comXml.getVsDateFinAffiliationConjoint());
        communicationFiscaleRetour.setVsNomConjoint(comXml.getVsNomConjoint());
        communicationFiscaleRetour.setVsAdresseConjoint1(comXml.getVsAdresseConjoint1());
        communicationFiscaleRetour.setVsAdresseConjoint2(comXml.getVsAdresseConjoint2());
        communicationFiscaleRetour.setVsAdresseConjoint3(comXml.getVsAdresseConjoint3());
        communicationFiscaleRetour.setVsAdresseConjoint4(comXml.getVsAdresseConjoint4());
        communicationFiscaleRetour.setVsNumPostalLocaliteConjoint(comXml.getVsNumPostalLocaliteConjoint());
        communicationFiscaleRetour.setVsNumCaisseAgenceConjoint(comXml.getVsNumCaisseAgenceConjoint());
        communicationFiscaleRetour
                .setVsNumCaisseProfessionnelleConjoint(comXml.getVsNumCaisseProfessionnelleConjoint());
        communicationFiscaleRetour.setVsDateDebutAffiliationConjointCaisseProfessionnelle(comXml
                .getVsDateDebutAffiliationConjointCaisseProfessionnelle());
        communicationFiscaleRetour.setVsDateFinAffiliationConjointCaisseProfessionnelle(comXml
                .getVsDateFinAffiliationConjointCaisseProfessionnelle());
        communicationFiscaleRetour.setVsNumAffilieInterneConjointCaisseProfessionnelle(comXml
                .getVsNumAffilieInterneConjointCaisseProfessionnelle());
        communicationFiscaleRetour.setVsCotisationAvsConjoint(comXml.getVsCotisationAvsConjoint());
        communicationFiscaleRetour.setVsNomPrenomContribuableAnnee(comXml.getVsNomPrenomContribuableAnnee());
        communicationFiscaleRetour.setVsAdresseCtb1(comXml.getVsAdresseCtb1());
        communicationFiscaleRetour.setVsAdresseCtb2(comXml.getVsAdresseCtb2());
        communicationFiscaleRetour.setVsAdresseCtb3(comXml.getVsAdresseCtb3());
        communicationFiscaleRetour.setVsAdresseCtb4(comXml.getVsAdresseCtb4());
        communicationFiscaleRetour.setVsNumPostalLocaliteCtb(comXml.getVsNumPostalLocaliteCtb());
        communicationFiscaleRetour.setVsEtatCivilCtb(comXml.getVsEtatCivilCtb());
        communicationFiscaleRetour.setVsSexeCtb(comXml.getVsSexeCtb());
        communicationFiscaleRetour.setVsLangue(comXml.getVsLangue());
        communicationFiscaleRetour.setVsNumAvsCtb(comXml.getVsNumAvsCtb());
        communicationFiscaleRetour.setVsDateNaissanceCtb(comXml.getVsDateNaissanceCtb());
        communicationFiscaleRetour.setVsDebutActiviteCtb(comXml.getVsDebutActiviteCtb());
        communicationFiscaleRetour.setVsFinActiviteCtb(comXml.getVsFinActiviteCtb());
        communicationFiscaleRetour.setVsDebutActiviteConjoint(comXml.getVsDebutActiviteConjoint());
        communicationFiscaleRetour.setVsFinActiviteConjoint(comXml.getVsFinActiviteConjoint());
        communicationFiscaleRetour.setVsRevenuNonAgricoleCtb(comXml.getVsRevenuNonAgricoleCtb());
        communicationFiscaleRetour.setVsRevenuNonAgricoleConjoint(comXml.getVsRevenuNonAgricoleConjoint());
        communicationFiscaleRetour.setVsRevenuAgricoleCtb(comXml.getVsRevenuAgricoleCtb());
        communicationFiscaleRetour.setVsRevenuAgricoleConjoint(comXml.getVsRevenuAgricoleConjoint());
        communicationFiscaleRetour
                .setVsCapitalPropreEngageEntrepriseCtb(comXml.getVsCapitalPropreEngageEntrepriseCtb());
        communicationFiscaleRetour.setVsCapitalPropreEngageEntrepriseConjoint(comXml
                .getVsCapitalPropreEngageEntrepriseConjoint());
        communicationFiscaleRetour.setVsRevenuRenteCtb(comXml.getVsRevenuRenteCtb());
        communicationFiscaleRetour.setVsRevenuRenteConjoint(comXml.getVsRevenuRenteConjoint());
        communicationFiscaleRetour.setVsFortunePriveeCtb(comXml.getVsFortunePriveeCtb());
        communicationFiscaleRetour.setVsFortunePriveeConjoint(comXml.getVsFortunePriveeConjoint());
        communicationFiscaleRetour.setVsSalairesContribuable(comXml.getVsSalairesContribuable());
        communicationFiscaleRetour.setVsSalairesConjoint(comXml.getVsSalairesConjoint());
        communicationFiscaleRetour.setVsAutresRevenusCtb(comXml.getVsAutresRevenusCtb());
        communicationFiscaleRetour.setVsAutresRevenusConjoint(comXml.getVsAutresRevenusConjoint());
        communicationFiscaleRetour.setVsRachatLpp(comXml.getVsRachatLpp());
        communicationFiscaleRetour.setVsRachatLppCjt(comXml.getVsRachatLppCjt());
        communicationFiscaleRetour.setVsLibre3(comXml.getVsLibre3());
        communicationFiscaleRetour.setVsLibre4(comXml.getVsLibre4());
        communicationFiscaleRetour.setVsReserve(comXml.getVsReserve());
        communicationFiscaleRetour.setVsNbJoursTaxation(comXml.getVsNbJoursTaxation());
        communicationFiscaleRetour.setVsNumCtbSuivant(comXml.getVsNumCtbSuivant());
        communicationFiscaleRetour.setVsDateDecesCtb(comXml.getVsDateDecesCtb());
        communicationFiscaleRetour.setVsReserveDateNaissanceConjoint(comXml.getVsReserveDateNaissanceConjoint());
        communicationFiscaleRetour.setVsReserveFichierImpression(comXml.getVsReserveFichierImpression());
        communicationFiscaleRetour.setVsReserveTriNumCaisse(comXml.getVsReserveTriNumCaisse());
        // Ajoute les logs générés lors de la réception
        setParametersLog(comXml, communicationFiscaleRetour, transaction);
    }

    /**
     * Copie les attributs de la communication xml sur le BEntity
     * 
     * @param communication
     * @param communicationFiscale
     */
    private void setParametersLog(Communication comXml,
            CPCommunicationFiscaleRetourViewBean communicationFiscaleRetour, BITransaction transaction)
            throws Exception {
        // Ajoute les logs générés lors de la réception
        ErrorMessage errorMsg = comXml.getErrorMessage();
        if (errorMsg.getErrorItemCount() > 0) {
            ErrorItem[] errors = errorMsg.getErrorItem();
            FWLog log = new FWLog();
            log.setSession(getSession());
            log.add(transaction);
            for (int i = 0; i < errors.length; i++) {
                ErrorItem item = errors[i];
                FWMessage msg = new FWMessage();
                msg.setSession(getSession());
                msg.setMessageId(CPCommunicationFiscaleRetourViewBean.LOG_SOURCE_RECEPTION);
                msg.setComplement(item.getErrorText());
                msg.setTypeMessage(String.valueOf(item.getErrorNiveau()));
                msg.setIdSource(this.getClass().getName());
                msg.setIdLog(log.getIdLog());
                msg.add(transaction);
                // Stocker le niveau d'erreur
                if (msg.getTypeMessage().compareTo(log.getErrorLevel()) > 0) {
                    log.setErrorLevel(msg.getTypeMessage());
                    log.setIdHighestMessage(msg.getIdMessage());
                }
            }
            log.update(transaction);
            communicationFiscaleRetour.setIdLog(log.getIdLog());
            communicationFiscaleRetour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_ERREUR);
        } else {
            communicationFiscaleRetour.setIdLog("");
            communicationFiscaleRetour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE);
        }
    }

    /**
     * @param i
     */
    public void setTotalNbFile(int i) {
        totalNbFile = i;
    }

    /**
     * incrémente le nombre de fichiers inséré avec succes dans le journal attaché à la communication
     * 
     * @param transaction
     * @return true si tous les fichiers on été inséré (nbRéussit = nbFichier)
     * @throws Exception
     */
    protected boolean updateJournalNbReussit(BITransaction transaction) throws Exception {
        boolean success = false;
        // reprend le journal laissé par le parent
        CPJournalRetour journal = getJournalRetour();
        journal.incNbFichierReussit();
        if (journal.getNbFichierTotal().equals(journal.getNbFichierReussit())) {
            journal.setSucces(new Boolean(true));
            journal.setStatus(CPJournalRetour.CS_RECEPTION_TOTAL);
            success = true;
        }
        journal.update(transaction);
        return success;
    }

}
