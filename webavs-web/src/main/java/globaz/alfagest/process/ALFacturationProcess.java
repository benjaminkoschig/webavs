package globaz.alfagest.process;

import globaz.alfagest.db.ALDecompteADI;
import globaz.alfagest.db.ALDecompteADIManager;
import globaz.alfagest.db.ALDossier;
import globaz.alfagest.db.ALEntetePrestation;
import globaz.alfagest.db.ALEntetePrestationManager;
import globaz.alfagest.db.ALParametre;
import globaz.alfagest.db.ALPrestationPaiement;
import globaz.alfagest.db.ALPrestationPaiementManager;
import globaz.alfagest.db.ALRecap;
import globaz.alfagest.db.ALRecapManager;
import globaz.alfagest.facturation.ALFacturationProtocole;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAModulePassageManager;
import globaz.musca.util.FAUtil;
import globaz.naos.api.IAFAffiliation;
import globaz.osiris.api.APISection;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Créé le 18 janv. 06
 * 
 * @author dch
 * 
 *         Process de facturation ALFAGEST, appelé par le module de facturation (ALFacturation)
 */
public class ALFacturationProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String dateComptable = null;
    private String idModuleFacturation = "";
    private String idTypeFacturation = "905018";

    // passage
    String numeroPassage = null;

    // définit le type de cotisation à prendre en compte - par défaut 0 => les 2
    int typeCotisation = 0;

    /**
     * Nettoyage après erreur ou exécution.
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Exécution du process
     */
    @Override
    protected boolean _executeProcess() throws Exception {

        // Facturation OK
        boolean noError = true;

        // Flag pour stipuler un avertissement sur l'affiliation de la ligne de factu
        boolean isOnWarning = false;

        // Tableau stockant les prestations avec un avertissement
        ArrayList tablePrestWarning = new ArrayList();

        // Message d'avertissement de l'affiliation
        String errors = "";

        // sélection des factures
        String selectionFacture = null;

        // prestation de paiement pour itérer
        ALPrestationPaiement prestation = null;

        // en-tête de facture pour itérer
        FAEnteteFacture enteteFacture = null;

        // gestion des exceptions...
        try {
            // on charge le numéro de sélection de facture
            selectionFacture = getSelectionFacture();

            // on peut charger la liste des prestations
            ALPrestationPaiementManager prestations = loadPrestations(selectionFacture, getTypeCotisation());

            // pour chaque prestation, on crée un afact qu'on rajoute dans l'en-tête de facture
            for (int i = 0; i < prestations.size(); i++) {
                try {
                    // la i-ème prestation
                    prestation = (ALPrestationPaiement) prestations.get(i);

                    if (!JadeStringUtil.isEmpty(prestation.getTotalMontant())
                            && (Double.parseDouble(prestation.getTotalMontant()) != 0)) {
                        // Recherche du dossier afin d'obtenir le type d'allocataire
                        IAFAffiliation affiliationAF = getAffiliationFacturation(prestation.getIdAffiliation());
                        if (affiliationAF == null) {
                            throw new Exception("Erreur d'accès à l'affiliation pour la récap : "
                                    + prestation.getIdRecap());
                        }
                        if (affiliationAF.getISession() != null) {
                            errors = "";
                            errors = affiliationAF.getISession().getErrors().toString();
                            if (!JadeStringUtil.isEmpty(errors)) {
                                isOnWarning = true;
                            }
                        }
                        // on demande l'en-tête de facture correspondante
                        enteteFacture = FAUtil.getEnteteFacture(
                                numeroPassage,
                                affiliationAF.getIdTiers(),
                                CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                                        getSession().getApplication()), affiliationAF.getAffilieNumero(),
                                APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION, prestation.getNumeroFacture(),
                                getSession(), getTransaction());

                        // on rajoute l'affact dans l'en-tête de facture
                        createLigneFacture(prestation, enteteFacture, affiliationAF);

                        // enregistrement dans la DB des changements pour la recap
                        updateRecap(prestation.getIdRecap());

                        // Commit de la ligne de facturation
                        getTransaction().commit();

                        // Set l'état de la récap utilisée pour le protocole à 'CO' temporairement
                        ((ALPrestationPaiement) prestations.get(i)).setEtatRecap("CO");

                        if (isOnWarning) {
                            ((ALPrestationPaiement) prestations.get(i)).setMsgErreur(errors);
                            tablePrestWarning.add((prestations.get(i)));
                            isOnWarning = false;
                        }
                    } else {
                        // enregistrement dans la DB des changements pour la recap
                        updateRecap(prestation.getIdRecap());

                        // Commit de la ligne de facturation
                        getTransaction().commit();
                    }
                } catch (Exception e) {
                    getTransaction().rollback();
                    updateRecapError(prestation.getIdRecap());
                    ((ALPrestationPaiement) prestations.get(i)).setMsgErreur(e.getMessage());
                    ((ALPrestationPaiement) prestations.get(i)).setEtatRecap("ER");
                }
            }

            // on efface les prestations <<bidon>> ADI
            deleteADI();

            // Recherche la somme total des facture passé dans MUSCA
            String totalFacture = retrieveSommeMUSCA(numeroPassage);

            // envoie du protocole par e-mail à l'utilisateur
            ALFacturationProtocole protocole = new ALFacturationProtocole();

            noError = protocole.create(prestations, tablePrestWarning,
                    ALFacturationProtocole.PROTOCOLE_PAIEMENTS_INDIRECTS, selectionFacture, numeroPassage, false,
                    getSession(), getTransaction(), totalFacture);

            protocole.sendByEmail();

            // Si le protocole retourne false on sort immédiatemment
            if (!noError) {
                return noError;
            }

            // on remet en SA les prestations restées en TR et les récaps associées si TR
            updateRecapsInitialState();
            // on débloque la facturation
            unlockFactu();
        } catch (Exception e) {
            getTransaction().addErrors(e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());

            return false;
        }

        // tout s'est bien passé
        return true;
    }

    /**
     * Exécute le processus de facturation en appelant la méthode protected
     */
    public boolean _executeProcessFacturation() throws Exception {
        return _executeProcess();
    }

    /**
     * Crée une ligne de facture
     */
    private void createLigneFacture(ALPrestationPaiement prestation, FAEnteteFacture enteteFacture, IAFAffiliation affil)
            throws Exception {
        // On reset le buffer d'erreur de la transaction
        getTransaction().clearErrorBuffer();

        FAAfact aFact = new FAAfact();

        aFact.setISession(getSession());
        aFact.setIdEnteteFacture(enteteFacture.getIdEntete());
        aFact.setIdPassage(numeroPassage);
        aFact.setIdModuleFacturation(getIdModuleFacturation());

        aFact.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
        aFact.setNonImprimable(Boolean.FALSE);
        aFact.setNonComptabilisable(Boolean.FALSE);
        aFact.setAQuittancer(Boolean.FALSE);
        aFact.setAnneeCotisation("");
        // aFact.setIdRubrique(prestation.getChiffreStatistique());
        aFact.setIdExterneRubrique(prestation.getChiffreStatistique());
        aFact.setDebutPeriode(formatPeriodeDe(prestation.getPeriodeRecapDe()));
        aFact.setFinPeriode(formatPeriodeA(prestation.getPeriodeRecapA()));
        aFact.setMontantFacture(String.valueOf(Double.parseDouble(prestation.getTotalMontant()) * (-1)));

        try {
            aFact.add(getTransaction());

            // erreur dans la transaction?
            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }

        } catch (Exception e) {
            throw new Exception("Erreur à l'enregistrement de l'AFact: " + e.getMessage());
        }
    }

    /**
     * Efface les prestations ADI <<bidon>>
     */
    private void deleteADI() throws Exception {
        // On reset le buffer d'erreur de la transaction
        getTransaction().clearErrorBuffer();

        // prestations
        ALEntetePrestationManager prestationsADI = new ALEntetePrestationManager();
        ALEntetePrestationManager prestationsBidon = new ALEntetePrestationManager();

        // pour itérer
        ALEntetePrestation prestationADI = null;
        ALEntetePrestation prestationBidon = null;

        // chargement des en-têtes de prestation ADI
        try {
            prestationsADI.setIdCategorieTarif("*ADI");
            prestationsADI.setEtatPrestation("CO");
            prestationsADI.setSession(getSession());
            prestationsADI.find(getTransaction(), 0);

            // erreur dans la transaction?
            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }
        } catch (Exception e) {
            throw new Exception("Erreur à la lecture des prestations ADI:" + e.getMessage());
        }

        // chargement des en-têtes de prestation <<bidon>>
        try {
            prestationsBidon.setIdRecap("99999999");
            prestationsBidon.setSession(getSession());
            prestationsBidon.find(getTransaction(), 0);

            // erreur dans la transaction?
            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }
        } catch (Exception e) {
            throw new Exception("Erreur à la lecture des prestations <<bidon>> ADI: " + e.getMessage());
        }

        // itération sur tous les prestations ADI
        for (int i = 0; i < prestationsADI.size(); i++) {
            prestationADI = (ALEntetePrestation) prestationsADI.get(i);

            // itération sur tous les prestations <<bidon>>
            for (int j = 0; j < prestationsBidon.size(); j++) {
                prestationBidon = (ALEntetePrestation) prestationsBidon.get(j);

                if (prestationADI.getIdDossier().equals(prestationBidon.getIdDossier())
                        && (Integer.parseInt(prestationADI.getPeriodeDe()) <= Integer.parseInt(prestationBidon
                                .getPeriodeDe()))
                        && (Integer.parseInt(prestationADI.getPeriodeA()) >= Integer.parseInt(prestationBidon
                                .getPeriodeA()))) {
                    try {
                        prestationBidon.delete();

                        // erreur dans la transaction?
                        if (!getTransaction().hasErrors()) {
                            getTransaction().commit();
                        } else {
                            throw new Exception(getTransaction().getErrors().toString());
                        }
                    } catch (Exception e) {
                        throw new Exception("Erreur à la suppression d'une prestation <<bidon>> ADI:" + e.getMessage());
                    }
                }
            }
            // Passage du décompte de la prestation de TR à CO
            ALDecompteADIManager decompteMan = new ALDecompteADIManager();
            ALDecompteADI decompteADI = new ALDecompteADI();
            try {
                decompteMan.setIdPrestationADI(prestationADI.getIdEntetePrestation());
                decompteMan.setSession(getSession());
                decompteMan.find(getTransaction(), 0);
                // erreur dans la transaction?
                if (getTransaction().hasErrors()) {
                    throw new Exception(getTransaction().getErrors().toString());
                }

            } catch (Exception e) {
                throw new Exception("Impossible de charger la liste des décomptes ADI : " + e.getMessage());
            }
            if (!decompteMan.isEmpty()) {
                try {
                    decompteADI = (ALDecompteADI) decompteMan.get(0);
                    decompteADI.setEtatDecompte("CO");
                    decompteADI.setSession(getSession());
                    decompteADI.save(getTransaction());
                    // erreur dans la transaction?
                    if (getTransaction().hasErrors()) {
                        throw new Exception(getTransaction().getErrors().toString());
                    }
                } catch (Exception e) {
                    throw new Exception("Impossible de mettre à jour le décompte ADI : " + e.getMessage());
                }
            }
        }
    }

    /**
     * Formatte la periodeA, passant de YYYYMM à DDMMYYYY où DD est le dernier jour du mois
     */
    private String formatPeriodeA(String date) {
        // calendrier pour trouver le dernier jour du mois
        JACalendarGregorian calendar = new JACalendarGregorian();

        // représentation de la date
        int month = Integer.parseInt(date.substring(4, 6));
        int year = Integer.parseInt(date.substring(0, 4));
        int day = calendar.daysInMonth(month, year);

        // bingo!
        return String.valueOf(day) + date.substring(4, 6) + date.substring(0, 4);
    }

    /**
     * Formatte la periodeDe, passant de YYYYMM à 01MMYYYY
     */
    private String formatPeriodeDe(String date) {
        return "01" + date.substring(4, 6) + date.substring(0, 4);
    }

    /**
     * Méthode qui retourne une affiliation AF suivant le type de lien succursalle
     */
    protected IAFAffiliation getAffiliationFacturation(String idAffiliation) {
        Hashtable params = new Hashtable();
        IAFAffiliation newAff = null;
        params.put(IAFAffiliation.FIND_FOR_AFFILIATIONID, idAffiliation);

        try {
            // Affiliation
            IAFAffiliation affiliation = (IAFAffiliation) getSession().getAPIFor(IAFAffiliation.class);
            IAFAffiliation affiliations[] = affiliation.findAffiliationAF(params);

            if ((affiliations != null) && (affiliations.length > 0)) {
                affiliation = affiliations[0];
                affiliation.setISession(getSession());
            } else {
                affiliation = null;
            }
            newAff = affiliation.getAffiliationFacturationAF(JACalendar.today().toString());

            if (newAff == null) {
                return affiliation;
            }
        } catch (Exception e) {
            System.out.println("Impossible de remonter l'affiliation : " + e.getMessage());
        }
        return newAff;
    }

    /**
     * @return
     */
    public String getDateComptable() {
        return dateComptable;
    }

    /**
     * ?
     */
    @Override
    protected String getEMailObject() {
        return null;
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    /**
     * @return
     */
    public String getNumeroPassage() {
        return numeroPassage;
    }

    /**
     * Lit le numéro de facture à partir de JAFPPRM, envoie une exception si la facturation n'est pas prête
     */
    private String getSelectionFacture() throws Exception {
        ALParametre parametre = new ALParametre();
        String factu = null;

        // lecture du flag et du numero de facture dans JAFPPRM
        try {
            parametre.setIdentificationApplication("AF");
            parametre.setProvenanceActeur("CAISSEAF");
            parametre.setIdentificationActeur("1");
            parametre.setTypeParametre("FACTU");
            parametre.setDateValeur("19000101");

            parametre.setSession(getSession());
            parametre.retrieve(getTransaction());

            // erreur dans la transaction?
            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }
        } catch (Exception e) {
            throw new Exception("Erreur à la lecture du numéro de facture: " + e.getMessage());
        }

        // lecture de la valeur
        factu = parametre.getValeurAlphanumerique();

        if (factu.length() < 8) {
            throw new Exception("Numéro de facture invalide");
        }

        // si le flag vaut 0, on ne peut pas continuer
        if (factu.charAt(0) == '0') {
            throw new Exception("Facturation des AF pas prête!");
        } else if (factu.charAt(0) != '1') {
            throw new Exception("Numéro de facture invalide");
        }

        return factu.substring(2, 8);
    }

    /**
     * Retourne le type de cotisation pris en compte
     * 
     * @return - 0: les 2 / 1 : paritaire / 2:personnelle
     */
    protected int getTypeCotisation() {
        return typeCotisation;
    }

    public String getTypeModule() {
        return idTypeFacturation;
    }

    /**
     * Renvoie la Job Queue à utiliser pour soumettre le process (constantes dans <code>GlobazJobQueue</code>).
     * 
     * @return la Job Queue à utiliser pour soumettre le process
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }

    private ALDossier loadDossier(String idDossier) throws Exception {
        ALDossier dossier = new ALDossier();
        try {
            dossier.setIdDossier(idDossier);
            dossier.setSession(getSession());
            dossier.retrieve();
        } catch (Exception e) {
            throw new Exception("Impossible de retrouver le dossier avec l'ID : " + idDossier);
        }

        return dossier;
    }

    /**
     * Charge les prestation selon une selection sur les numéros de facture et les retourne
     */
    private ALPrestationPaiementManager loadPrestations(String selectionFacture, int typeCotisation) throws Exception {
        ALPrestationPaiementManager prestations = new ALPrestationPaiementManager();

        try {
            prestations.setNumeroFacture(selectionFacture);
            prestations.setSession(getSession());

            prestations.setTypeCotisation(typeCotisation);
            prestations.find(getTransaction(), 0);

            // erreur dans la transaction?
            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }
        } catch (Exception e) {
            throw new Exception("Erreur à la lecture des prestations: " + e.getMessage());
        }

        return prestations;
    }

    /**
     * Recheche la somme total des prestations passées à MUSCA
     * 
     * @return La somme des prestations
     */
    private String retrieveSommeMUSCA(String numPassage) throws Exception {
        // montant de facturation MUSCA
        String montantTotal = null;
        String idModuleFact = null;
        // reset du buffer de transaction
        getTransaction().clearErrorBuffer();

        FAModulePassageManager factPassage = new FAModulePassageManager();
        FAAfactManager factManager = new FAAfactManager();

        // Recherche du numéro de module AF
        try {
            factPassage.setForIdTypeModule(idTypeFacturation);
            factPassage.setSession(getSession());
            factPassage.find(getTransaction(), 1);
            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }
        } catch (Exception e) {
            throw new Exception("Impossible de retrouver l'ID du module de facturation AF : " + e.getMessage());
        }

        idModuleFact = getIdModuleFacturation();

        try {
            montantTotal = factManager.getSumPassage(numPassage, idModuleFact, getTransaction());

            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }
        } catch (Exception e) {
            throw new Exception("Impossible de charger la facturation pour le passage " + numPassage + ", module"
                    + idModuleFact);
        }

        if (montantTotal == null) {
            return "0";
        } else {
            return montantTotal;
        }

    }

    /**
     * @param string
     */
    public void setDateComptable(String string) {
        dateComptable = string;
    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }

    /**
     * @param passage
     */
    public void setNumeroPassage(String newIdPassage) {
        numeroPassage = newIdPassage;
    }

    /**
     * Définit le type de cotisation à prendre en compte
     * 
     * @param newType
     *            - 0: les 2 / 1 : paritaire / 2:personnelle
     */
    public void setTypeCotisation(int newType) {
        typeCotisation = newType;
    }

    public void setTypeModule(String typeModule) {
        idTypeFacturation = typeModule;
    }

    /**
     * Remet le flag bloquant la facturation à 0
     */
    private void unlockFactu() throws Exception {
        // On reset le buffer d'erreur de la transaction
        getTransaction().clearErrorBuffer();

        ALParametre parametre = new ALParametre();

        try {
            // paramétrisation
            parametre.setIdentificationApplication("AF");
            parametre.setProvenanceActeur("CAISSEAF");
            parametre.setIdentificationActeur("1");
            parametre.setTypeParametre("FACTU");
            parametre.setDateValeur("19000101");

            // lecture
            parametre.setSession(getSession());
            parametre.retrieve(getTransaction());

            // mise à jour
            parametre.setValeurAlphanumerique("0 000000000");
            parametre.update();

            // erreur dans la transaction?
            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                throw new Exception(getTransaction().getErrors().toString());
            }
        } catch (Exception e) {
            throw new Exception("Erreur au débloquage de la facturation: " + e.getMessage());
        }
    }

    /**
     * Met à jour la recap en base de données. Son état passe de TR en CO, ainsi que toutes les prestations. Celles-ci
     * ont également leur numéro de passage mis à jour.
     */
    private void updateRecap(String idRecap) throws Exception {
        // On reset le buffer d'erreur de la transaction
        getTransaction().clearErrorBuffer();

        // recap
        ALRecap recap = new ALRecap();

        // en-têtes de prestations
        ALEntetePrestationManager entetesPrestations = new ALEntetePrestationManager();
        ALEntetePrestation entetePrestation = null;

        // chargement des en-têtes
        try {
            entetesPrestations.setIdRecap(idRecap);
            entetesPrestations.setSession(getSession());
            entetesPrestations.find(getTransaction(), 0);

            // erreur dans la transaction?
            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }
        } catch (Exception e) {
            throw new Exception("Erreur à la lecture des prestations de la récap " + idRecap + " : " + e.getMessage());
        }

        // on met à jour chaque en-tête de prestation de la recap
        int nbrEnteteUpdt = 0;
        for (int i = 0; i < entetesPrestations.size(); i++) {
            entetePrestation = (ALEntetePrestation) entetesPrestations.get(i);

            try {
                if (Double.parseDouble(entetePrestation.getMontant()) != 0) {
                    entetePrestation.setEtatPrestation("CO");
                    entetePrestation.setIdPassageFacturation(numeroPassage);
                    entetePrestation.setDateVersementCompensation(JACalendar.format(getDateComptable(),
                            JACalendar.FORMAT_YYYYMMDD));
                    entetePrestation.save(getTransaction());
                    nbrEnteteUpdt++;
                }
            } catch (Exception e) {
                throw new Exception("Impossible de mettre à jour la prestation "
                        + entetePrestation.getIdEntetePrestation() + " : " + e.getMessage());
            }
        }
        // erreur dans la transaction?
        if (getTransaction().hasErrors()) {
            throw new Exception(getTransaction().getErrors().toString());
        }

        if (nbrEnteteUpdt > 0) {
            // chargement et update de la recap
            try {
                // on charge la recap...
                recap.setIdRecap(idRecap);
                recap.setSession(getSession());
                recap.retrieve(getTransaction());

                if (getTransaction().hasErrors()) {
                    throw new Exception("Lecture impossible.");
                }

                // ..change l'état...
                recap.setEtat("CO");

                // ...et enregristre
                recap.save(getTransaction());
            } catch (Exception e) {
                throw new Exception("Erreur à la mise à jour de la récap " + idRecap + " : " + e.getMessage());
            }
        }
    }

    /**
     * Traite une récap en erreur en lui affectant le code *ERR dans le champs LETAT
     */
    private void updateRecapError(String idRecap) throws Exception {
        // On reset le buffer d'erreur de la transaction
        getTransaction().clearErrorBuffer();

        // recap
        ALRecap recap = new ALRecap();

        // en-têtes de prestations
        ALEntetePrestationManager entetesPrestations = new ALEntetePrestationManager();
        ALEntetePrestation entetePrestation = null;

        try {
            // on charge la recap...
            recap.setIdRecap(idRecap);
            recap.setSession(getSession());
            recap.retrieve(getTransaction());

            if (getTransaction().hasErrors()) {
                throw new Exception("Lecture impossible.");
            }

            // ..change l'état...
            recap.setEtat("TR");

            // ...et enregistre
            recap.save(getTransaction());

            // erreur dans la transaction?
            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                throw new Exception(getTransaction().getErrors().toString());
            }
        } catch (Exception e) {
            System.out.println("Erreur à la mise à jour de la récap " + idRecap + " : " + e.getMessage());
        }

        try {
            entetesPrestations.setIdRecap(idRecap);
            entetesPrestations.setSession(getSession());
            entetesPrestations.find(getTransaction(), 0);

            // erreur dans la transaction?
            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }
        } catch (Exception e) {
            throw new Exception("Erreur à la lecture des prestations de la récap " + idRecap + " : " + e.getMessage());
        }

        // on met à jour chaque en-tête de prestation de la recap
        for (int i = 0; i < entetesPrestations.size(); i++) {
            entetePrestation = (ALEntetePrestation) entetesPrestations.get(i);

            try {
                entetePrestation.setEtatPrestation("TR");
                entetePrestation.setIdPassageFacturation(numeroPassage);
                entetePrestation.save(getTransaction());

                // erreur dans la transaction?
                if (!getTransaction().hasErrors()) {
                    getTransaction().commit();
                } else {
                    throw new Exception(getTransaction().getErrors().toString());
                }
            } catch (Exception e) {
                throw new Exception("Impossible de mettre à jour la prestation "
                        + entetePrestation.getIdEntetePrestation() + " : " + e.getMessage());
            }
        }
    }

    // Remet l'état SA aux récaps et prestations respectives qui sont restées en TR
    // après la facturation
    private void updateRecapsInitialState() throws Exception {

        // on récupère l'id de chaque récap concernée, on la met en TR et on récupère ses prestations
        ALEntetePrestation currentPrestation = null;

        // en-têtes de prestations
        ALEntetePrestationManager entetesPrestations = new ALEntetePrestationManager();
        entetesPrestations.setSession(getSession());
        entetesPrestations.setEtatPrestation("TR");

        try {
            entetesPrestations.find(getTransaction(), 0);
            // erreur dans la transaction?
            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }
        } catch (Exception e) {
            throw new Exception("Erreur à la lecture des prestations restées en TR :" + e.getMessage());
        }

        // on met en SA chaque prestation restée en TR
        for (int i = 0; i < entetesPrestations.size(); i++) {
            currentPrestation = (ALEntetePrestation) entetesPrestations.get(i);
            currentPrestation.setEtatPrestation("SA");
            try {
                currentPrestation.save(getTransaction());
            } catch (Exception e) {
                throw new Exception("Impossible de mettre à jour la prestation "
                        + currentPrestation.getIdEntetePrestation() + " : " + e.getMessage());
            }
        }

        // RECAPS
        ALRecap currentRecap = null;
        ALRecapManager recaps = new ALRecapManager();

        recaps.setEtatRecap("TR");
        recaps.setSession(getSession());

        try {
            recaps.find(getTransaction(), 0);
            // erreur dans la transaction?
            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }
        } catch (Exception e) {
            throw new Exception("Erreur à la lecture des recaps restées en TR :" + e.getMessage());
        }

        // on met en SA chaque récap restée en TR

        for (int j = 0; j < recaps.size(); j++) {
            currentRecap = (ALRecap) recaps.get(j);
            currentRecap.setEtat("SA");
            try {
                currentRecap.save(getTransaction());
            } catch (Exception e) {
                throw new Exception("Impossible de mettre à jour la récap " + currentRecap.getIdRecap() + " : "
                        + e.getMessage());
            }

        }

    }

}
