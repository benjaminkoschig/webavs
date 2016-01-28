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
import globaz.pavo.db.bta.CIInscriptionRetroBtaHtmlOut;
import globaz.pavo.db.bta.CIInscriptionRetroBtaLog;
import globaz.pavo.db.bta.CIRequerantBta;
import globaz.pavo.db.bta.CIRequerantBtaComparator;
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

public class CIInscriptionRetroBtaProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private TreeMap anneeListRequerant;// liste annee/requerant[]
    private String idDossierBta = "";

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        boolean success = true;
        TreeMap requerantToFlag = new TreeMap(new CIRequerantBtaComparator());
        CIJournal journal = null;
        ArrayList listeLog = new ArrayList();
        CIInscriptionRetroBtaHtmlOut doc = new CIInscriptionRetroBtaHtmlOut();
        doc.setSession(getSession());

        try {
            // on charge le treemap anneeListRequerant
            _getInscriptionsCiToDo();
        } catch (Exception e) {
            throw new Exception("Impossible de trouver les inscriptions � passer au CI" + e.getMessage());
        }

        if (anneeListRequerant == null) {
            success = false;
            throw new Exception("anneeListRequerant is null");
        }

        if ((anneeListRequerant.size() > 0)) {
            // Retrouver les informations sur le dossier
            CIDossierBta dossierBta = new CIDossierBta();
            dossierBta.setSession(getSession());
            dossierBta.setIdDossierBta(idDossierBta);
            dossierBta.retrieve();

            // On r�cup�re les cl� (ann�es) afin de parcourir le treemap
            Set keysAnneListReq = anneeListRequerant.keySet();
            setProgressScaleValue(keysAnneListReq.size());
            // Parcours du treemap
            for (Iterator iter = keysAnneListReq.iterator(); iter.hasNext() /*
                                                                             * && ! isAborted ( )
                                                                             */;) {
                if (isAborted()) {
                    // process annul� on supprime les inscriptions CI pass�es
                    /*
                     * if(journal!=null){ //suppression du journal journal.setSession(getSession());
                     * journal.retrieve(getTransaction()); journal.wantCallMethodBefore(false);
                     * journal.delete(getTransaction()); }
                     */
                    setProgressDescription(getSession().getLabel("MSG_ABORT_RETRO"));
                    return false;
                }
                // on r�cup�re pour chaque cl� les requerants concern�s
                // (ArrayList)
                String annee = iter.next().toString();
                ArrayList listRequerants = (ArrayList) anneeListRequerant.get(annee);
                try {
                    for (int i = 0; i < listRequerants.size(); i++) {
                        boolean inscriptionDejaExistante = false;
                        // on r�cup�re le requ�rant
                        CIRequerantBta requerant = (CIRequerantBta) listRequerants.get(i);

                        // si le requerant n'as pas d�j� �t� trait�
                        if (JadeStringUtil.isBlank(requerant.getDateInscriptionRetroFlag())) {
                            // on r�cup�re le CI du requ�rant
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

                                // Permet d'obtenir le CI et les CI li�s
                                ArrayList idCiLiesList = compteInd.getIdCILies(getTransaction());

                                // V�rifier si il n'existe pas d�j� une
                                // inscription BTA pour ce requ�rant pour
                                // l'ann�e et dans tous les CI li�s �galement
                                // r�cup�ration des �criture
                                CIEcritureManager ecritureManager = new CIEcritureManager();
                                ecritureManager.setSession(getSession());
                                if (idCiLiesList.size() > 0) {
                                    ecritureManager.setForCompteIndividuelIdList(idCiLiesList.toArray());
                                } else {
                                    ecritureManager.setForCompteIndividuelId(compteInd.getCompteIndividuelId());
                                }
                                ecritureManager.setForAnnee(annee);
                                ecritureManager.find();
                                // parcours des �critures
                                for (int k = 0; k < ecritureManager.size(); k++) {
                                    CIEcriture ecriture = (CIEcriture) ecritureManager.getEntity(k);
                                    if (!ecriture.getPartBta().equals("0")) {
                                        // on a une inscription de type BTA pour
                                        // l'ann�e
                                        inscriptionDejaExistante = true;

                                        // v�rifie que la fraction de
                                        // l'inscription d�j� existante
                                        // correspond afin de ne pas d�pass�
                                        // 100% de la BTA
                                        /*
                                         * if(ecriture.getPartBta().equals(String .valueOf(listRequerants.size()))){
                                         * inscriptionDejaExistante = true; } else{getMemoryLog().logMessage(
                                         * "Il existe d�j� une inscription BTA au CI pour " +
                                         * requerant.getNumeroNnssRequerant() +
                                         * " dont la fraction est incoh�rente avec la fraction calcul�e 1/"
                                         * +listRequerants.size(), FWMessage.FATAL, this.getClass().toString()); return
                                         * false; }
                                         */
                                    }
                                }

                                if (!inscriptionDejaExistante) {
                                    // cr�ation du journal
                                    journal = creerJournal(journal, dossierBta);
                                    // on cr�e l'�criture dans le journal
                                    creerEcriture(journal, annee, listRequerants, compteInd);
                                    // ajouter dans la liste pour le ficher HTML
                                    listeLog.add(new CIInscriptionRetroBtaLog(annee,
                                            requerant.getNumeroNnssRequerant(), requerant.getNomRequerant(), requerant
                                                    .getPrenomRequerant(), String.valueOf(listRequerants.size())));
                                }

                                // ajouter le requerant au tableau de requerant
                                // � flager � la fin du process
                                addRequerantToFlag(requerantToFlag, annee, requerant);

                            } else {
                                getMemoryLog().logMessage(
                                        getSession().getLabel("MSG_ERREUR_CI_INTROUVABLE") + " "
                                                + requerant.getNumeroNnssRequerant(), FWMessage.FATAL,
                                        this.getClass().toString());

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

            // on v�rifie que la transaction n'est pas en erreur
            if (getTransaction().hasErrors()) {
                success = false;
                abort();
            }

            if (!isAborted()) {
                // flag les requerants pour que les inscriptions retroactives ne
                // soit plus prise en compte pour ce requerant
                flagRequerant(requerantToFlag);

                // Cr�ation du fichier HTML
                doc.setData(listeLog);
                doc.setFilename("docInscriptionRetroBtaPassee.html");
                this.registerAttachedDocument(doc.getOutputFile());
            } else {
                success = false;
            }
        }
        return success;
    }

    public TreeMap _getInscriptionsCiToDo() {
        anneeListRequerant = new TreeMap();// cl�=ann�e, valeur=tableau de
        // requ�rants
        String sexeRequerantCi = "";

        // Retrouver les informations sur le dossier
        CIDossierBta dossierBta = new CIDossierBta();
        dossierBta.setSession(getSession());
        dossierBta.setIdDossierBta(idDossierBta);
        try {
            dossierBta.retrieve();
            if (!dossierBta.getEtatDossier().equals(CIDossierBta.CS_ETAT_OUVERT)) {
                TreeMap t = new TreeMap();
                t.put("erreur", getSession().getLabel("MSG_ERREUR_DOSSIER_NON_OUVERT"));
                return t;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // r�cup�ration des requerants du dossier
        CIRequerantBtaManager requerantManager = new CIRequerantBtaManager();
        requerantManager.setSession(getSession());
        requerantManager.setForIdDossierBta(getIdDossierBta());
        // requerantManager.setForDateInscriptionRetroFlag("nullOrZero");//recherche
        // que les requerants non flag�
        try {
            requerantManager.find();

            // 1er parcours des requ�rants pour voir si il y a d�j� eu des
            // inscriptions CI pour le dossier
            // si oui on ne peut plus passer de retro-actifs
            int anneeFlagMax = 0;
            for (int i = 0; i < requerantManager.size(); i++) {
                CIRequerantBta requerant = (CIRequerantBta) requerantManager.getEntity(i);
                if (!JadeStringUtil.isBlank(requerant.getDateInscriptionRetroFlag())) {
                    int anneeFlag = JADate.getYear(requerant.getDateInscriptionRetroFlag()).intValue();
                    if (anneeFlag > anneeFlagMax) {
                        anneeFlagMax = anneeFlag;
                    }
                }
            }
            if (anneeFlagMax != 0) {
                TreeMap t = new TreeMap();
                t.put("erreur", getSession().getLabel("MSG_ERREUR_RETRO_IMPOSSIBLE"));
                return t;
            }

            // parcours des requ�rants
            for (int i = 0; i < requerantManager.size(); i++) {
                int nbMaxAnneeRetro = 5;
                String dateCloture = "";// date de cloture du CI si existe
                String anneeEnCours = JADate.getYear(JACalendar.todayJJsMMsAAAA()).toString();
                String annee = String.valueOf(Integer.parseInt(anneeEnCours) - 1); // annee
                // =
                // annee
                // en
                // cours
                // -1
                int anneeInt = Integer.parseInt(annee);

                // R�cup�ration du requ�rant
                CIRequerantBta requerant = (CIRequerantBta) requerantManager.getEntity(i);

                // rechercher du CI du requ�rant
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
                    // R�cup�ration du CI
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
                            // si le motif est de type 71 ou 81 on est dans le
                            // cas d'un rentier
                            if ((rassemblement.getMotifArc().equals("71") || rassemblement.getMotifArc().equals("81"))
                                    && JadeStringUtil.isBlank(rassemblement.getDateRevocation())) {
                                dateCloture = rassemblement.getDateCloture();
                            }
                        }
                    }
                } else {
                    // si le CI d'un requerant n'est pas trouv�
                    TreeMap t = new TreeMap();
                    t.put("erreur",
                            getSession().getLabel("MSG_ERREUR_CI_INTROUVABLE") + " "
                                    + requerant.getNumeroNnssRequerant());
                    return t;
                }

                // si la demande du requerant est accept�e (c'est � dire date
                // debut!=date fin)
                if (!requerant.getDateDebut().equals(requerant.getDateFin())) {

                    // d�finition de l'annee et du nbMaxAnneeRetro en fonction
                    // du cas en pr�sence

                    // si requerant a une date de fin et une date de cloture
                    if (!JadeStringUtil.isBlank(requerant.getDateFin()) && !JadeStringUtil.isBlank(dateCloture)) {
                        int anneeIntTemp1 = JADate.getYear(requerant.getDateFin()).intValue();
                        int anneeIntTemp2 = JADate.getYear(dateCloture).intValue() - 1;
                        if (anneeIntTemp1 < anneeIntTemp2) {
                            anneeInt = JADate.getYear(requerant.getDateFin()).intValue();
                            nbMaxAnneeRetro = nbMaxAnneeRetro - (Integer.parseInt(annee) - anneeInt);
                            annee = JADate.getYear(requerant.getDateFin()).toString();
                        } else {
                            anneeInt = JADate.getYear(dateCloture).intValue();
                            anneeInt = anneeInt - 1;
                            nbMaxAnneeRetro = nbMaxAnneeRetro - (Integer.parseInt(annee) - anneeInt);
                            annee = String.valueOf(anneeInt);
                        }
                    }
                    // si requerant a que 1 date de fin
                    else if (!JadeStringUtil.isBlank(requerant.getDateFin())) {
                        anneeInt = JADate.getYear(requerant.getDateFin()).intValue();
                        nbMaxAnneeRetro = nbMaxAnneeRetro - (Integer.parseInt(annee) - anneeInt);
                        annee = JADate.getYear(requerant.getDateFin()).toString();
                    }
                    // si requerant a que 1 date de cloture rassemblement
                    else if (!JadeStringUtil.isBlank(dateCloture)) {
                        anneeInt = JADate.getYear(dateCloture).intValue();
                        anneeInt = anneeInt - 1;
                        nbMaxAnneeRetro = nbMaxAnneeRetro - (Integer.parseInt(annee) - anneeInt);
                        annee = String.valueOf(anneeInt);
                    }

                    // Remplissage du Treemap
                    while ((anneeInt >= JADate.getYear(requerant.getDateDebut()).intValue()) && (nbMaxAnneeRetro > 0)) {
                        // si annee pas contenue dans le treemap alors insertion
                        // de l'ann�e et d'un tableau contenant le requerant
                        if (!anneeListRequerant.containsKey(annee)) {
                            if (isMajeur(new JADate(requerant.getDateNaissanceRequerant()), Integer.parseInt(annee))
                                    && !CIUtil.isRetraite(new JADate(requerant.getDateNaissanceRequerant()),
                                            sexeRequerantCi, anneeInt)) {
                                // cr�ation d'une list de requerant et insertion
                                // du requerant en cours (si pas en age de
                                // retraite et majeur)
                                ArrayList requerants = new ArrayList();
                                requerants.add(requerant);
                                anneeListRequerant.put(annee, requerants);
                            }
                        } else {
                            // r�cup�ration du tableau de requerants
                            // correspondant � l'ann�e et ajout de requerant en
                            // cours (si pas en age de retraite et majeur)
                            if (isMajeur(new JADate(requerant.getDateNaissanceRequerant()), Integer.parseInt(annee))
                                    && !CIUtil.isRetraite(new JADate(requerant.getDateNaissanceRequerant()),
                                            sexeRequerantCi, anneeInt)) {
                                ArrayList requerants = (ArrayList) anneeListRequerant.get(annee);
                                requerants.add(requerant);
                            }
                        }
                        anneeInt--;
                        nbMaxAnneeRetro--;
                        annee = String.valueOf(anneeInt);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (anneeListRequerant.size() < 1) {
            TreeMap t = new TreeMap();
            t.put("erreur", getSession().getLabel("MSG_AUCUNE_INSCRIPTION_RETRO"));
            return t;
        }

        return anneeListRequerant;
    }

    private void addRequerantToFlag(TreeMap requerantToFlag, String annee, CIRequerantBta requerant) {
        if (!requerantToFlag.containsKey(requerant)) {
            requerantToFlag.put(requerant, annee);
        } else {
            String flagAnnee = (String) requerantToFlag.get(requerant);
            if (Integer.parseInt(flagAnnee) < Integer.parseInt(annee)) {
                requerantToFlag.put(requerant, annee);
            }
        }
    }

    private void creerEcriture(CIJournal journal, String annee, ArrayList listRequerants, CICompteIndividuel compteInd)
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
        ecriture.setPartBta(String.valueOf(listRequerants.size()));// fraction
        // bta
        ecriture.setMontant("0");
        ecriture.add();
    }

    private CIJournal creerJournal(CIJournal journal, CIDossierBta dossierBta) throws Exception {
        // Cr�ation du journal CI pour le dossier BTA en cours
        if (journal == null) {
            journal = new CIJournal();
            journal.setSession(getSession());
            journal.setIdTypeInscription(CIJournal.CS_BTA);
            journal.setLibelle(getSession().getLabel("BTA_RETRO") + " "
                    + dossierBta.getTiersViewBean().getNumAvsActuel());
            journal.setDate(JACalendar.todayJJsMMsAAAA());
            journal.setProprietaire(getSession().getUserId());
            journal.setIdEtat(CIJournal.CS_OUVERT);
            journal.add();
        }
        return journal;
    }

    private void flagRequerant(TreeMap requerantToFlag) throws Exception {
        Set setKeys = requerantToFlag.keySet();
        Iterator iter2 = setKeys.iterator();
        while (iter2.hasNext()) {
            CIRequerantBta req = (CIRequerantBta) iter2.next();
            String anneeForFlag = (String) requerantToFlag.get(req);
            req.setDateInscriptionRetroFlag(anneeForFlag);
            req.wantCallValidate(false);
            req.update(getTransaction());
        }
    }

    public TreeMap getAnneeListRequerant() {
        return anneeListRequerant;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("MSG_INSCRIPTION_RETRO_ECHOUE");
        } else {
            return getSession().getLabel("MSG_INSCRIPTION_RETRO_REUSSI");
        }
    }

    public String getIdDossierBta() {
        return idDossierBta;
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

    public void setAnneeListRequerant(TreeMap anneeListRequerant) {
        this.anneeListRequerant = anneeListRequerant;
    }

    public void setIdDossierBta(String idDossierBta) {
        this.idDossierBta = idDossierBta;
    }
}
