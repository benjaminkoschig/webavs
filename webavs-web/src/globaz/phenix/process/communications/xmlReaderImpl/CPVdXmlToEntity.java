/*
 * Créé le 18 avril 07
 */
package globaz.phenix.process.communications.xmlReaderImpl;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.log.JadeLogger;
import globaz.phenix.db.communications.CPCommentaireCommunication;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVDViewBean;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.divers.CPPeriodeFiscaleManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.process.communications.CPCommunicationsUtils;
import globaz.phenix.process.communications.CPProcessXmlReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author jpa Transforme les documents xml en CPCommunicationFiscalRetour (BEntity) et les ajoute
 */
public class CPVdXmlToEntity extends CPProcessXmlReader {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String CODE_TO = "to";
    private static final String GENRE_AGR = "AGR";
    private static final String GENRE_PCI = "PCI";
    private static final String GENRE_PSA = "PSA";
    private static final String GENRE_SAL = "SAL";
    private String dateReception = "";

    private Document document = null;

    private CPJournalRetour journalRetour = null;

    private String xmlFile = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        boolean successful = true;
        BITransaction transaction = null;
        try {
            // On va rechercher le document
            document = getDocument(xmlFile);
            NodeList elements1 = CPCommunicationsUtils.getEnteteCommunication(document);
            setDateReception(CPCommunicationsUtils.getDateReceptionCom(elements1));
            // On boucle sur les communications
            NodeList elements = CPCommunicationsUtils.getCommunications(document);
            getParent().setProgressScaleValue(elements.getLength());
            // On utilise une transaction par fichier
            transaction = getSession().newTransaction();
            transaction.openTransaction();
            for (int i = 0; i < elements.getLength(); i++) {
                Element communication = (Element) elements.item(i);
                try {
                    // On ajoute la communication dans le journal
                    addCommunicationRetour(transaction, communication);
                    transaction.commit();
                    successful = true;
                    getParent().incProgressCounter();
                } catch (Exception e) {
                    getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
                    transaction.rollback();
                    successful = false;
                    throw new Exception(getSession().getLabel("CP_MSG_0136") + e.getMessage());
                }
            }
            // On va mettre a jour le journal
            updateJournalNbReussit(transaction);
            try {
                if (transaction.hasErrors()) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
                transaction.rollback();
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0136") + e.getMessage(), FWMessage.ERREUR,
                    "CPProcessXmlToEntity");
            successful = false;
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    getMemoryLog().logMessage(
                            getSession().getLabel(
                                    "PROCXMLTOENT_ERROR_INSERT_DOC" + e.getMessage() + " (" + e.getClass().toString()
                                            + ")"), FWMessage.ERREUR, "CPProcessXmlToEntity");
                }
            }
        }
        return successful;

    }

    /**
     * Copie les attributs de la communication xml sur le BEntity
     * 
     * @param communication
     * @param communicationFiscale
     */
    private void addCommunicationRetour(BITransaction transaction, Element communication) throws Exception {
        CPCommunicationFiscaleRetourVDViewBean comRetour = new CPCommunicationFiscaleRetourVDViewBean();
        setParameters(comRetour, transaction, communication);
        comRetour.add(transaction);
        String ikiretid = comRetour.getIdRetour();
        // backup
        comRetour = new CPCommunicationFiscaleRetourVDViewBean();
        setParameters(comRetour, transaction, communication);
        comRetour.setForBackup(true);
        comRetour.setIdRetour(ikiretid);
        comRetour.add(transaction);

        if (transaction.hasErrors()) {
            getMemoryLog().logMessage(comRetour.getValeurChampRecherche() + " - " + transaction.getErrors().toString(),
                    FWMessage.ERREUR, "");
        }
    }

    public String getDateReception() {
        return dateReception;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return null;
    }

    // Possibilités PCI, IAF, PSA, SAL, AGR
    public String getGenreAffilie(String codeAffVD) {
        if (codeAffVD.equals(CPVdXmlToEntity.GENRE_PCI)) {
            codeAffVD = CPDecision.CS_INDEPENDANT;
        } else if (codeAffVD.equals(CPVdXmlToEntity.GENRE_PSA)) {
            codeAffVD = CPDecision.CS_NON_ACTIF;
        } else if (codeAffVD.equals(CPVdXmlToEntity.GENRE_SAL)) {
            codeAffVD = CPDecision.CS_TSE;
        } else if (codeAffVD.equals(CPVdXmlToEntity.GENRE_AGR)) {
            codeAffVD = CPDecision.CS_AGRICULTEUR;
        } else {
            codeAffVD = "";
        }
        return codeAffVD;
    }

    public String getGenreTaxation(String codeAffVD) {
        if (codeAffVD.equals(CPVdXmlToEntity.CODE_TO)) {
            codeAffVD = CPCommentaireCommunication.CS_GENRE_TO;
        } else {
            codeAffVD = CPCommentaireCommunication.CS_GENRE_TD;
        }
        return codeAffVD;
    }

    public String getIdIfd(String annee) {
        String numIfd = "";
        if ((annee != null) || (annee != "")) {
            try {
                CPPeriodeFiscaleManager periode = new CPPeriodeFiscaleManager();
                periode.setSession(getSession());
                periode.setForAnneeDecisionDebut(annee);
                periode.find();
                if (periode.size() > 0) {
                    numIfd = ((CPPeriodeFiscale) periode.getFirstEntity()).getIdIfd();
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return numIfd;
    }

    /**
     * @return
     */
    public CPJournalRetour getJournalRetour() {
        return journalRetour;
    }

    @Override
    public String getXmlFile() {
        return xmlFile;
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

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    /**
     * @param retour
     */
    @Override
    public void setJournalRetour(CPJournalRetour retour) {
        journalRetour = retour;
    }

    /**
     * Copie les attributs de la communication xml sur le BEntity
     * 
     * @param communication
     * @param communicationFiscale
     */
    private void setParameters(CPCommunicationFiscaleRetourVDViewBean communicationFiscaleRetour,
            BITransaction transaction, Element communicationElement) throws Exception {
        // TODO revenu1 renseigné seulement si PSI?
        // TODO Cas 424.659.920 : revenu2 renseigné avec revActLucPSAAVS et
        // revenu1 non renseigné
        // TODO AGR et SAL (traité)traité?
        // TODO Tenir des remarques ci-dessus pour les impressions
        // TODO Pas de script de création pour CPCRVDP
        // TODO Mettre les différents montants en numérique! idem pour les dates
        // => revoir readProperties et write properties !
        // On lie la communication au journalRetour
        communicationFiscaleRetour.setIdJournalRetour(journalRetour.getIdJournalRetour());
        // On rempli les champs fournit dans le fichier électronique du fisc
        communicationFiscaleRetour.setAnnee1(CPCommunicationsUtils.getAnnee(communicationElement));
        communicationFiscaleRetour.setDepensesTrainVie(CPCommunicationsUtils.getDepensesTV(communicationElement));
        communicationFiscaleRetour.setAnnee2(CPCommunicationsUtils.getAnnee2(communicationElement));
        String genreAffilie = CPCommunicationsUtils.getGenreAffilie(communicationElement);
        communicationFiscaleRetour.setGenreAffilie(getGenreAffilie(CPCommunicationsUtils
                .getGenreAffilie(communicationElement)));
        communicationFiscaleRetour.setGenreTaxation(getGenreTaxation(CPCommunicationsUtils
                .getGenreTaxation(communicationElement)));
        // communicationFiscaleRetour.setRevenu2(CPCommunicationsUtils.getRevenu2(communicationElement));
        // // A voir sur quel champ est mis le tag Revenu2
        communicationFiscaleRetour.setRevenuAnnee1(CPCommunicationsUtils.getRevenuNet(communicationElement)); // ???
        communicationFiscaleRetour.setIdIfd(getIdIfd(CPCommunicationsUtils.getPeriodeFiscale(communicationElement)));
        // Champs communs VD
        communicationFiscaleRetour.setVdNumAvs(CPCommunicationsUtils.getNumAVS(communicationElement));
        communicationFiscaleRetour.setVdNumCaisse(CPCommunicationsUtils.getNumCaisse(communicationElement));
        communicationFiscaleRetour.setVdNumDemande(CPCommunicationsUtils.getNumDemande(communicationElement));
        communicationFiscaleRetour.setVdNumAffilie(CPCommunicationsUtils.getNumAffilie(communicationElement));
        communicationFiscaleRetour.setVdNomPrenom(CPCommunicationsUtils.getNomPrenom(communicationElement));
        communicationFiscaleRetour.setVdNumContribuable(CPCommunicationsUtils.getNumContribuable(communicationElement));
        communicationFiscaleRetour.setVdGenreAffilie(CPCommunicationsUtils.getGenreAffilie(communicationElement));
        communicationFiscaleRetour.setVdDebPeriode(CPCommunicationsUtils.getDebutPeriode(communicationElement));
        communicationFiscaleRetour.setVdFinPeriode(CPCommunicationsUtils.getFinPeriode(communicationElement));
        communicationFiscaleRetour.setVdAddChez(CPCommunicationsUtils.getAddChez(communicationElement));
        communicationFiscaleRetour.setVdAddRue(CPCommunicationsUtils.getAddRue(communicationElement));
        communicationFiscaleRetour.setVdNumLocalite(CPCommunicationsUtils.getNumLocalite(communicationElement));
        communicationFiscaleRetour.setVdDatNaissance(CPCommunicationsUtils.getDateNaissance(communicationElement));
        communicationFiscaleRetour.setVdAssujCtb(CPCommunicationsUtils.getAssujetissementCtb(communicationElement));
        communicationFiscaleRetour.setVdTypeTax(CPCommunicationsUtils.getTypeTax(communicationElement));
        communicationFiscaleRetour.setVdNumCaisse(CPCommunicationsUtils
                .getNumCaisse(communicationElement, genreAffilie));
        communicationFiscaleRetour.setVdNatureComm(CPCommunicationsUtils.getNatureCommunication(communicationElement,
                genreAffilie));
        communicationFiscaleRetour.setVdDateDemande(CPCommunicationsUtils.getDateDemande(communicationElement));
        if (CPVdXmlToEntity.GENRE_PSA.equalsIgnoreCase(genreAffilie)) {
            communicationFiscaleRetour.setRevenu1(CPCommunicationsUtils.getMontantRente(communicationElement));
            communicationFiscaleRetour.setFortune(CPCommunicationsUtils.getFortune(communicationElement));
            communicationFiscaleRetour.setDateFortune(CPCommunicationsUtils.getDateFortune(communicationElement));
            communicationFiscaleRetour.setVdFortuneImposable(CPCommunicationsUtils
                    .getFortuneImposable(communicationElement));
            communicationFiscaleRetour.setVdDateDetFortune(CPCommunicationsUtils
                    .getDateDetFortune(communicationElement));
            communicationFiscaleRetour.setVdMontantRente(CPCommunicationsUtils.getMontantRente(communicationElement));
            communicationFiscaleRetour.setVdRevenuActivitesLucratives(CPCommunicationsUtils
                    .getRevenuActivitesLucratives(communicationElement));
            communicationFiscaleRetour.setVdCodeNatureRente(CPCommunicationsUtils
                    .getCodeNatureRente(communicationElement));
            communicationFiscaleRetour.setVdPenAliObtenue(CPCommunicationsUtils
                    .getPensionAlimentaireObtenue(communicationElement));
            communicationFiscaleRetour.setVdDroitHabitation(CPCommunicationsUtils
                    .getDroitHabitation(communicationElement));
            communicationFiscaleRetour.setVdImpositionDepense(CPCommunicationsUtils
                    .getImpositionDepense(communicationElement));
        } else if (CPVdXmlToEntity.GENRE_PCI.equalsIgnoreCase(genreAffilie)) {
            communicationFiscaleRetour.setRevenu1(CPCommunicationsUtils.getRevenuNet(communicationElement));
            communicationFiscaleRetour.setCotisation1(CPCommunicationsUtils.getCotisation(communicationElement));
            communicationFiscaleRetour.setCotisation2(CPCommunicationsUtils.getCotisation2(communicationElement));
            communicationFiscaleRetour.setCapital(CPCommunicationsUtils.getCapital(communicationElement));
            communicationFiscaleRetour.setDebutExercice1(CPCommunicationsUtils.getDebutExercice(communicationElement));
            communicationFiscaleRetour.setFinExercice1(CPCommunicationsUtils.getFinExercice(communicationElement));
            communicationFiscaleRetour.setDebutExercice2(CPCommunicationsUtils.getDebutExercice2(communicationElement));
            communicationFiscaleRetour.setFinExercice2(CPCommunicationsUtils.getFinExercice2(communicationElement));
            communicationFiscaleRetour.setVdDebAssuj(CPCommunicationsUtils
                    .getDebutAssujetissement(communicationElement));
            communicationFiscaleRetour.setVdFinAssuj(CPCommunicationsUtils.getFinAssujetissement(communicationElement));
            communicationFiscaleRetour.setVdCapInvesti(CPCommunicationsUtils.getCapitalInvesti(communicationElement));
            communicationFiscaleRetour.setVdDatDetCapInvesti(CPCommunicationsUtils
                    .getDateDetCapitalInvesti(communicationElement));
            communicationFiscaleRetour.setVdRevActInd(CPCommunicationsUtils.getRevenuActiviteInd(communicationElement));
            communicationFiscaleRetour.setVdGIprof(CPCommunicationsUtils.getGIprofOccasionnel(communicationElement));
            communicationFiscaleRetour.setVdExcesLiquidite(CPCommunicationsUtils
                    .getExcedantLiquiditeProf(communicationElement));
            communicationFiscaleRetour.setVdSalVerseCjt(CPCommunicationsUtils
                    .getSalaireVerseConjoint(communicationElement));
            communicationFiscaleRetour.setVdRevenuNet(CPCommunicationsUtils.getRevenuNetAgr(communicationElement));
        } else if (CPVdXmlToEntity.GENRE_SAL.equalsIgnoreCase(genreAffilie)) {
            // SAL (TSE)
            communicationFiscaleRetour.setVdNumCaisse(CPCommunicationsUtils.getNumCaisseSal(communicationElement));
            communicationFiscaleRetour.setVdSalaireCotisant(CPCommunicationsUtils
                    .getSalaireCotisant(communicationElement));
            communicationFiscaleRetour.setRevenu1(CPCommunicationsUtils.getSalaireCotisant(communicationElement));
            communicationFiscaleRetour.setCotisation1(CPCommunicationsUtils.getCotisation(communicationElement));
            communicationFiscaleRetour.setCotisation2(CPCommunicationsUtils.getCotisation2(communicationElement));
            communicationFiscaleRetour.setCapital(CPCommunicationsUtils.getCapital(communicationElement));
            communicationFiscaleRetour.setDebutExercice1(CPCommunicationsUtils.getDebutExercice(communicationElement));
            communicationFiscaleRetour.setFinExercice1(CPCommunicationsUtils.getFinExercice(communicationElement));
            communicationFiscaleRetour.setDebutExercice2(CPCommunicationsUtils.getDebutExercice2(communicationElement));
            communicationFiscaleRetour.setFinExercice2(CPCommunicationsUtils.getFinExercice2(communicationElement));
            communicationFiscaleRetour.setVdDebAssuj(CPCommunicationsUtils
                    .getDebutAssujetissement(communicationElement));
            communicationFiscaleRetour.setVdFinAssuj(CPCommunicationsUtils.getFinAssujetissement(communicationElement));
            communicationFiscaleRetour.setVdCapInvesti(CPCommunicationsUtils.getCapitalInvesti(communicationElement));
            communicationFiscaleRetour.setVdDatDetCapInvesti(CPCommunicationsUtils
                    .getDateDetCapitalInvesti(communicationElement));
            communicationFiscaleRetour.setVdRevActInd(CPCommunicationsUtils.getRevenuActiviteInd(communicationElement));
            communicationFiscaleRetour.setVdGIprof(CPCommunicationsUtils.getGIprofOccasionnel(communicationElement));
            communicationFiscaleRetour.setVdExcesLiquidite(CPCommunicationsUtils
                    .getExcedantLiquiditeProf(communicationElement));
            communicationFiscaleRetour.setVdSalVerseCjt(CPCommunicationsUtils
                    .getSalaireVerseConjoint(communicationElement));
            communicationFiscaleRetour.setDebutExercice1(communicationFiscaleRetour.getVdDebPeriode());
            communicationFiscaleRetour.setFinExercice1(communicationFiscaleRetour.getVdFinPeriode());
        } else if (CPVdXmlToEntity.GENRE_AGR.equalsIgnoreCase(genreAffilie)) {
            // TODO voir si test à faire comme indépendant
            communicationFiscaleRetour.setVdRevenuNet(CPCommunicationsUtils.getRevenuNetAgr(communicationElement));
            communicationFiscaleRetour.setVdNumCaisse(CPCommunicationsUtils.getNumCaisseAgr(communicationElement));
            communicationFiscaleRetour.setRevenu1(CPCommunicationsUtils.getRevenuNet(communicationElement));
            communicationFiscaleRetour.setCotisation1(CPCommunicationsUtils.getCotisation(communicationElement));
            communicationFiscaleRetour.setCotisation2(CPCommunicationsUtils.getCotisation2(communicationElement));
            communicationFiscaleRetour.setCapital(CPCommunicationsUtils.getCapital(communicationElement));
            communicationFiscaleRetour.setDebutExercice1(CPCommunicationsUtils.getDebutExercice(communicationElement));
            communicationFiscaleRetour.setFinExercice1(CPCommunicationsUtils.getFinExercice(communicationElement));
            communicationFiscaleRetour.setDebutExercice2(CPCommunicationsUtils.getDebutExercice2(communicationElement));
            communicationFiscaleRetour.setFinExercice2(CPCommunicationsUtils.getFinExercice2(communicationElement));
            communicationFiscaleRetour.setVdDebAssuj(CPCommunicationsUtils
                    .getDebutAssujetissement(communicationElement));
            communicationFiscaleRetour.setVdFinAssuj(CPCommunicationsUtils.getFinAssujetissement(communicationElement));
            communicationFiscaleRetour.setVdCapInvesti(CPCommunicationsUtils.getCapitalInvesti(communicationElement));
            communicationFiscaleRetour.setVdDatDetCapInvesti(CPCommunicationsUtils
                    .getDateDetCapitalInvesti(communicationElement));
            communicationFiscaleRetour.setVdRevActInd(CPCommunicationsUtils.getRevenuActiviteInd(communicationElement));
            communicationFiscaleRetour.setVdGIprof(CPCommunicationsUtils.getGIprofOccasionnel(communicationElement));
            communicationFiscaleRetour.setVdExcesLiquidite(CPCommunicationsUtils
                    .getExcedantLiquiditeProf(communicationElement));
            communicationFiscaleRetour.setVdSalVerseCjt(CPCommunicationsUtils
                    .getSalaireVerseConjoint(communicationElement));
            communicationFiscaleRetour.setDebutExercice1(communicationFiscaleRetour.getVdDebPeriode());
            communicationFiscaleRetour.setFinExercice1(communicationFiscaleRetour.getVdFinPeriode());
        }

        String commentaire = CPCommunicationsUtils.getCommentaire(communicationElement, genreAffilie);
        // Limiter les commentaires à 150 caractères.
        if (commentaire.length() >= 150) {
            commentaire = commentaire.substring(0, 149);
        }
        communicationFiscaleRetour.setVdCommentaire(commentaire);

        communicationFiscaleRetour.setDateRetour(getDateReception());
    }

    @Override
    public void setXmlFile(String _xmlFile) {
        xmlFile = _xmlFile;
    }

    /**
     * incrémente le nombre de fichiers inséré avec succes dans le journal attaché à la communication
     * 
     * @param transaction
     * @return true si tous les fichiers on été inséré (nbRéussit = nbFichier)
     * @throws Exception
     */
    protected boolean updateJournalNbReussit(BITransaction transaction) throws Exception {
        CPJournalRetour journal = getJournalRetour();
        journal.setSucces(new Boolean(true));
        journal.setStatus(CPJournalRetour.CS_RECEPTION_TOTAL);
        journal.update(transaction);
        return true;
    }

}
