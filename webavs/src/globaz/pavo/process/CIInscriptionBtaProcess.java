package globaz.pavo.process;

import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.bta.CIDossierBta;
import globaz.pavo.db.bta.CIDossierBtaManager;
import globaz.pavo.db.bta.CIInscriptionBtaError;
import globaz.pavo.db.bta.CIInscriptionBtaHtmlError;
import globaz.pavo.db.bta.CIInscriptionBtaHtmlOut;
import globaz.pavo.db.bta.CIInscriptionBtaLog;
import globaz.pavo.db.bta.CIRequerantBta;
import globaz.pavo.db.bta.CIRequerantBtaManager;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.compte.CIRassemblementOuverture;
import globaz.pavo.db.compte.CIRassemblementOuvertureManager;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.util.CIUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class CIInscriptionBtaProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private TreeMap dossierListRequerant;
    private ArrayList listeErreur = new ArrayList();
    private boolean simulation = false;

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        boolean success = true;
        CIJournal journal = null;
        ArrayList requerantToFlag = new ArrayList();
        ArrayList listeLog = new ArrayList();
        CIInscriptionBtaHtmlOut doc = new CIInscriptionBtaHtmlOut();
        doc.setSession(getSession());

        String anneeEnCours = JADate.getYear(JACalendar.todayJJsMMsAAAA()).toString();
        String annee = String.valueOf(Integer.parseInt(anneeEnCours) - 1);

        // Envoi de l'année pour le titre du document de log ainsi que le mode
        doc.setAnnee(annee);
        doc.setSimulation(simulation);

        // récupération de la liste des requerants ayant droit
        getInscriptionsCiToDo();

        // Récupération des clé du treemap
        Set setKeys = dossierListRequerant.keySet();
        setProgressScaleValue(setKeys.size());
        Iterator iter = setKeys.iterator();

        // Parcours les dossiers (clé)
        while (iter.hasNext()) {
            if (isAborted()) {
                setProgressDescription(getSession().getLabel("MSG_ABORT_RETRO"));
                return false;
            }

            String idDossier = (String) iter.next();
            ArrayList requerants = (ArrayList) dossierListRequerant.get(idDossier);

            try {
                // Parcours les requérants du dossier en cours (valeur)
                for (int i = 0; i < requerants.size(); i++) {
                    boolean inscriptionDejaExistante = false;
                    CIRequerantBta requerant = (CIRequerantBta) requerants.get(i);

                    // si flag vide ou si annee contenue dans le flag est plus
                    // petite que l'annee en cours-1
                    if (JadeStringUtil.isBlank(requerant.getDateInscriptionRetroFlag())
                            || JADate.getYear(requerant.getDateInscriptionRetroFlag()).intValue() < Integer
                                    .parseInt(annee)) {
                        // on récupère le CI du requérant
                        CICompteIndividuelManager compteIndManager = new CICompteIndividuelManager();
                        compteIndManager.setSession(getSession());
                        compteIndManager.setForNumeroAvs(NSUtil.unFormatAVS(requerant.getNumeroNnssRequerant()));
                        compteIndManager.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);// pour
                        // ne
                        // prendre
                        // que
                        // les
                        // CI
                        // ouverts
                        compteIndManager.find();
                        if (compteIndManager.size() > 0) {
                            CICompteIndividuel compteInd = (CICompteIndividuel) compteIndManager.getFirstEntity();

                            // Vérifier si il n'existe pas déjà une inscription
                            // BTA pour ce requérant pour l'année
                            // récupération des écriture
                            CIEcritureManager ecritureManager = new CIEcritureManager();
                            ecritureManager.setSession(getSession());
                            ecritureManager.setForCompteIndividuelId(compteInd.getCompteIndividuelId());
                            ecritureManager.setForAnnee(annee);
                            ecritureManager.find();
                            // parcours des écritures
                            for (int k = 0; k < ecritureManager.size(); k++) {
                                CIEcriture ecriture = (CIEcriture) ecritureManager.getEntity(k);
                                if (!ecriture.getPartBta().equals("0")) {
                                    // on a une inscription de type BTA pour
                                    // l'année
                                    inscriptionDejaExistante = true;

                                    // vérifie que la fraction de l'inscription
                                    // déjà existante correspond afin de ne pas
                                    // dépassé 100% de la BTA
                                    /*
                                     * if(ecriture.getPartBta().equals(String.valueOf (listRequerants.size()))){
                                     * inscriptionDejaExistante = true; } else{ getMemoryLog().logMessage(
                                     * "Il existe déjà une inscription BTA au CI pour " +
                                     * requerant.getNumeroNnssRequerant() +
                                     * " dont la fraction est incohérente avec la fraction calculée 1/"
                                     * +listRequerants.size(), FWMessage.FATAL, this.getClass().toString()); return
                                     * false; }
                                     */
                                }
                            }

                            if (!inscriptionDejaExistante) {
                                if (!simulation) {
                                    // Création du journal CI pour l'annee
                                    // précédente
                                    journal = creerJournal(journal, annee);

                                    // on crée l'écriture dans le journal
                                    creerEcriture(journal, annee, requerants, compteInd);

                                    // ajouter le requerant au tableau de
                                    // requerant à flager à la fin du process
                                    requerantToFlag.add(requerant);
                                }
                                // ajouter dans la liste pour le ficher HTML
                                listeLog.add(new CIInscriptionBtaLog(requerant.getNumeroNnssRequerant(), requerant
                                        .getNomRequerant(), requerant.getPrenomRequerant(), String.valueOf(requerants
                                        .size())));
                            } else {
                                if (!simulation) {
                                    requerantToFlag.add(requerant);
                                }
                            }

                        } else {
                            getMemoryLog().logMessage(
                                    "Le CI de " + requerant.getNumeroNnssRequerant() + " n'a pas pu être trouvé",
                                    FWMessage.FATAL, this.getClass().toString());

                            // Sortir en cas d'erreurs
                            if (getMemoryLog().isOnFatalLevel()) {
                                return false;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.ERROR, this.getClass().toString());
                success = false;
            }
            incProgressCounter();
        }

        if (!isAborted()) {
            // flag les requerants avec l'annee de la dernière inscription CI
            // pour que les inscriptions retroactives ne soit plus prise en
            // compte pour ce requerant
            flagRequerant(requerantToFlag, annee);

            // création du fichier HTML
            doc.setData(listeLog);
            doc.setFilename("docInscriptionBtaPassee.html");
            registerAttachedDocument(null, doc.getOutputFile());

            // création du fichier d'erreur si nécessaire
            if (listeErreur.size() != 0) {
                CIInscriptionBtaHtmlError docErreur = new CIInscriptionBtaHtmlError();
                docErreur.setSession(getSession());
                docErreur.setData(listeErreur);
                docErreur.setFilename("Erreurs.html");
                docErreur.setAnnee(annee);
                docErreur.setSimulation(simulation);
                registerAttachedDocument(null, docErreur.getOutputFile());
            }
        } else {
            success = false;
        }
        return success;
    }

    private void creerEcriture(CIJournal journal, String annee, ArrayList requerants, CICompteIndividuel compteInd)
            throws Exception {
        CIEcriture ecriture = new CIEcriture();
        ecriture.setSession(getSession());
        ecriture.setIdJournal(journal.getIdJournal());
        ecriture.setCompteIndividuelId(compteInd.getCompteIndividuelId());
        ecriture.setMoisDebut("00");
        ecriture.setMoisFin("00");
        ecriture.setAnnee(annee);
        ecriture.setGre("00");
        ecriture.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
        ecriture.setPartBta(String.valueOf(requerants.size()));// fraction bta
        ecriture.setMontant("0");
        ecriture.add();
    }

    private CIJournal creerJournal(CIJournal journal, String annee) {
        if (journal == null) {
            journal = new CIJournal();
            journal.setSession(getSession());
            journal.setIdTypeInscription(CIJournal.CS_BTA);
            journal.setLibelle(getSession().getLabel("BTA_POUR_ANNEE") + " " + annee);
            journal.setDate(JACalendar.todayJJsMMsAAAA());
            journal.setProprietaire(getSession().getUserId());
            journal.setIdEtat(CIJournal.CS_OUVERT);
            try {
                journal.add();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return journal;
    }

    private void flagRequerant(ArrayList requerantToFlag, String annee) throws Exception {
        for (int j = 0; j < requerantToFlag.size(); j++) {
            CIRequerantBta req = (CIRequerantBta) requerantToFlag.get(j);
            req.setDateInscriptionRetroFlag(annee);
            req.wantCallValidate(false);
            req.update(getTransaction());
        }
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("MSG_INSCRIPTION_ECHOUE");
        } else {
            return getSession().getLabel("MSG_INSCRIPTION_REUSSI");
        }
    }

    public TreeMap getInscriptionsCiToDo() {
        dossierListRequerant = new TreeMap();// clé=idDossier, valeur=tableau de
        // requérants

        // récupération des dossiers ouverts
        CIDossierBtaManager dossierManager = new CIDossierBtaManager();
        dossierManager.setSession(getSession());
        dossierManager.setForEtatDossier(CIDossierBta.CS_ETAT_OUVERT);
        try {
            dossierManager.find();

            // parcours des dossiers
            for (int i = 0; i < dossierManager.size(); i++) {
                // récupération du dossier
                CIDossierBta dossier = (CIDossierBta) dossierManager.getEntity(i);

                // récupération des requerants du dossier
                CIRequerantBtaManager requerantManager = new CIRequerantBtaManager();
                requerantManager.setSession(getSession());
                requerantManager.setForIdDossierBta(dossier.getIdDossierBta());
                try {
                    requerantManager.find();
                    String anneeEnCours = JADate.getYear(JACalendar.todayJJsMMsAAAA()).toString();
                    String annee = String.valueOf(Integer.parseInt(anneeEnCours) - 1);

                    // parcours des requérants
                    for (int j = 0; j < requerantManager.size(); j++) {
                        String dateCloture = "";// date de cloture du CI si
                        // existe
                        String sexeRequerantCi = "";

                        // Récupération du requérant
                        CIRequerantBta requerant = (CIRequerantBta) requerantManager.getEntity(j);

                        // rechercher du CI du requérant
                        CICompteIndividuelManager compteIndManager = new CICompteIndividuelManager();
                        compteIndManager.setSession(getSession());
                        compteIndManager.setForNumeroAvs(NSUtil.unFormatAVS(requerant.getNumeroNnssRequerant()));
                        compteIndManager.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);// pour
                        // ne
                        // prendre
                        // que
                        // les
                        // CI
                        // ouverts
                        compteIndManager.find();
                        if (compteIndManager.size() > 0) {
                            // Récupération du CI
                            CICompteIndividuel compteInd = (CICompteIndividuel) compteIndManager.getFirstEntity();
                            sexeRequerantCi = compteInd.getSexe();

                            // recherche si il existe une date de cloture
                            CIRassemblementOuvertureManager rassemblementManager = new CIRassemblementOuvertureManager();
                            rassemblementManager.setSession(getSession());
                            rassemblementManager.setForCompteIndividuelId(compteInd.getCompteIndividuelId());
                            rassemblementManager.find();
                            if (rassemblementManager.size() > 0) {
                                for (int k = 0; k < rassemblementManager.size(); k++) {
                                    CIRassemblementOuverture rassemblement = (CIRassemblementOuverture) rassemblementManager
                                            .getEntity(k);
                                    // si le motif est de type 71 ou 81 on est
                                    // dans le cas d'un rentier
                                    if ((rassemblement.getMotifArc().equals("71") || rassemblement.getMotifArc()
                                            .equals("81")) && JadeStringUtil.isBlank(rassemblement.getDateRevocation())) {
                                        dateCloture = rassemblement.getDateCloture();
                                    }
                                }
                            }

                            // définit si le requérant a un droit au BTA pour
                            // l'année précédente
                            if (!requerant.getDateDebut().equals(requerant.getDateFin())
                                    && (JadeStringUtil.isBlank(requerant.getDateFin()) || JADate.getYear(
                                            requerant.getDateFin()).intValue() >= Integer.parseInt(annee))
                                    && JADate.getYear(requerant.getDateDebut()).intValue() <= Integer.parseInt(annee)) {
                                if (JadeStringUtil.isBlank(dateCloture)
                                        && isMajeur(new JADate(requerant.getDateNaissanceRequerant()),
                                                Integer.parseInt(annee))
                                        && !CIUtil.isRetraite(new JADate(requerant.getDateNaissanceRequerant()),
                                                sexeRequerantCi, Integer.parseInt(annee))) {
                                    if (!dossierListRequerant.containsKey(dossier.getIdDossierBta())) {
                                        // création d'une list de requerant et
                                        // insertion du requerant en cours dans
                                        // le treemap
                                        ArrayList requerants = new ArrayList();
                                        requerants.add(requerant);
                                        dossierListRequerant.put(dossier.getIdDossierBta(), requerants);
                                    } else {
                                        // récupération du tableau de requerants
                                        // correspondant au dossier et ajout de
                                        // requerant en cours
                                        ArrayList requerants = (ArrayList) dossierListRequerant.get(dossier
                                                .getIdDossierBta());
                                        requerants.add(requerant);
                                    }
                                }
                            }
                        } else {
                            // si le CI d'un requerant n'est pas trouvé
                            listeErreur.add(new CIInscriptionBtaError(requerant.getNumeroNnssRequerant(), requerant
                                    .getNomRequerant(), requerant.getPrenomRequerant(), "LE CI N'A PAS ETE TROUVE"));
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return dossierListRequerant;
    }

    public boolean getSimulation() {
        return simulation;
    }

    public boolean isMajeur(JADate dateNaissance, int annee) {
        boolean majeur = false;
        int anneeDroitBta = dateNaissance.getYear() + 18;
        if (annee >= anneeDroitBta) {
            majeur = true;
        }

        return majeur;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }
}
