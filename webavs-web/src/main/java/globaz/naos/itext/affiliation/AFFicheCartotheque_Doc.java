package globaz.naos.itext.affiliation;

import globaz.babel.api.ICTDocument;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEControleEmployeurManager;
import globaz.hercule.db.reviseur.CEReviseur;
import globaz.hercule.db.reviseur.CEReviseurManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.controleEmployeur.AFControleEmployeur;
import globaz.naos.db.controleEmployeur.AFControleEmployeurManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import globaz.pyxis.db.tiers.TIMoyenCommunication;
import globaz.pyxis.db.tiers.TIMoyenCommunicationManager;
import globaz.webavs.common.ICommonConstantes;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Document de fiche cartotheque.
 * </p>
 * 
 * @author vre
 */
public class AFFicheCartotheque_Doc extends FWIDocumentManager {

    private class LigneConfig {

        private Integer idVal;
        private String texteCentre;
        private String texteDroite;
        private String texteGauche;

        public LigneConfig(int idVal, int nivColCentre, int posColCentre) {
            if (idVal != 0) {
                this.idVal = new Integer(idVal);
            }

            texteCentre = getTexte(nivColCentre, posColCentre);
        }

        public LigneConfig(int idVal, int nivColGauche, int posColGauche, int nivColCentre, int posColCentre,
                int nivColDroite, int posColDroite) {
            this(idVal, nivColCentre, posColCentre);
            texteDroite = getTexte(nivColDroite, posColDroite);
            texteGauche = getTexte(nivColGauche, posColGauche);
        }

        /**
         * getter pour l'attribut id val.
         * 
         * @return la valeur courante de l'attribut id val
         */
        public Integer getIdVal() {
            return idVal;
        }

        private String getTexte(int niveau, int position) {
            if ((niveau > 0) && (position > 0)) {
                return textes.getTextes(niveau).getTexte(position).getDescription();
            } else {
                return null;
            }
        }

        /**
         * getter pour l'attribut texte centre.
         * 
         * @return la valeur courante de l'attribut texte centre
         */
        public String getTexteCentre() {
            return texteCentre;
        }

        /**
         * getter pour l'attribut texte droite.
         * 
         * @return la valeur courante de l'attribut texte droite
         */
        public String getTexteDroite() {
            return texteDroite;
        }

        /**
         * getter pour l'attribut texte gauche.
         * 
         * @return la valeur courante de l'attribut texte gauche
         */
        public String getTexteGauche() {
            return texteGauche;
        }

        /**
         * setter pour l'attribut texte centre.
         * 
         * @param string
         *            une nouvelle valeur pour cet attribut
         */
        public void setTexteCentre(String string) {
            texteCentre = string;
        }

        /**
         * setter pour l'attribut texte droite.
         * 
         * @param string
         *            une nouvelle valeur pour cet attribut
         */
        public void setTexteDroite(String string) {
            texteDroite = string;
        }

        /**
         * setter pour l'attribut texte gauche.
         * 
         * @param string
         *            une nouvelle valeur pour cet attribut
         */
        public void setTexteGauche(String string) {
            texteGauche = string;
        }
    }

    private static final int DEFAULT_SIZE = 8;
    private static final String F_CENTRE = "F_CENTER";

    private static final String F_DROITE = "F_RIGHT";

    private static final String F_GAUCHE = "F_LEFT";
    private static final String FILE_TITLE = "Cartothèque";
    // définir le chemin d'accès à l'image devant s'afficher dans l'en-tête
    private static final String IMG_FILE = AFApplication.DEFAULT_APPLICATION_NAOS_REP + "/model/LOGO_CCJU.png";

    private static final String P_HEADER_LINE_1 = "P_HEADER_LINE_1";
    private static final String P_HEADER_LINE_2 = "P_HEADER_LINE_2";
    private static final String P_HEADER_LINE_3 = "P_HEADER_LINE_3";
    private static final String P_IMG_PATH = "P_IMG_PATH";

    private static final long serialVersionUID = 2175999096995085190L;
    private static final String TEMPLATE_FILE_NAME = "NAOS_FICHE_CARTOTHEQUE";

    // infos chargées dans loadAffiliation()
    private AFAffiliation affiliation;

    private String affiliationConjoint = "";
    private String affiliationId;

    private String agenceCommunale = "";
    private String agenceCommunaleDom = "";

    private String anneeDernierControleEmployeur = "";
    private String anneeProchainControleEmployeur = "";
    private String anneeTaxation = "";
    private AFSuiviCaisseAffiliation caisseAF;
    private AFSuiviCaisseAffiliation caisseAVS;
    private String cotisationAnnulle = "";
    private String dateSituation;
    // champs à afficher
    private List<String> displayFieds = new ArrayList<String>();
    private String email = "";
    private String fax = "";
    private String forfaitAC = "";
    private String forfaitAF = "";
    private String forfaitAVS = "";
    private String forfaitCollaborateurAgri = "";
    private String forfaitTravailleurAgri = "";
    private String genreProchainControleEmployeur = "";
    private boolean hasNext = true;
    private final Map<Integer, LigneConfig> idValToLigneConfig = new HashMap<Integer, LigneConfig>();
    private final List<LigneConfig> lignesConfigs = new LinkedList<LigneConfig>();

    private String telephone = "";
    private ICTDocument textes;

    public AFFicheCartotheque_Doc() {
        super();
    }

    public AFFicheCartotheque_Doc(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    public AFFicheCartotheque_Doc(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    private void abort(String message) {
        this.abort();
        getMemoryLog().logMessage(message, FWMessage.ERREUR, this.getClass().getName());
    }

    private void abortAndReThrow(FWIException e) throws FWIException {
        this.abort(e.getMessage());
        throw e;
    }

    private String adresse(String typeAdresse) throws Exception {
        if (typeAdresse.equals(IConstantes.CS_AVOIR_ADRESSE_COURRIER)) {
            String adresse = affiliation.getTiers().getAdresseAsString(typeAdresse,
                    ICommonConstantes.CS_APPLICATION_COTISATION,
                    affiliation != null ? affiliation.getAffilieNumero() : null, getDateSituation(),
                    new TIAdresseFormater(), false, null);
            if (JadeStringUtil.isBlank(adresse)) {
                adresse = affiliation.getTiers().getAdresseAsString(typeAdresse, IConstantes.CS_APPLICATION_DEFAUT,
                        affiliation != null ? affiliation.getAffilieNumero() : null, getDateSituation(),
                        new TIAdresseFormater(), false, null);
            }
            return JadeStringUtil.change(adresse, "\n", " / ");
        } else {
            String adresse = affiliation.getTiers().getAdresseAsString(typeAdresse, IConstantes.CS_APPLICATION_DEFAUT,
                    affiliation != null ? affiliation.getAffilieNumero() : null, getDateSituation(),
                    new TIAdresseFormater(), true, null);
            return JadeStringUtil.change(adresse, "\n", " / ");
        }

    }

    @Override
    public void beforeBuildReport() throws FWIException {
        getDocumentInfo().setDocumentTypeNumber("0006CAF");
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        setTemplateFile(AFFicheCartotheque_Doc.TEMPLATE_FILE_NAME);
        setFileTitle(AFFicheCartotheque_Doc.FILE_TITLE);
        loadCatalogue();
        loadAffiliation();
        createEnTete();
        createLigneConfigs();
    }

    @Override
    public void createDataSource() throws Exception {
        peuplerLignes();

        LinkedList<HashMap<String, String>> dataSource = new LinkedList<HashMap<String, String>>();

        for (Iterator<LigneConfig> lignesIter = lignesConfigs.iterator(); lignesIter.hasNext();) {
            LigneConfig ligneConfig = lignesIter.next();
            HashMap<String, String> champs = new HashMap<String, String>();
            String ligneVide = "<style forecolor=\"#FFFFFF\">.</style>";

            if (!JadeStringUtil.isEmpty(ligneConfig.getTexteGauche())) {
                champs.put(AFFicheCartotheque_Doc.F_GAUCHE, ligneConfig.getTexteGauche());
            } else {
                champs.put(AFFicheCartotheque_Doc.F_GAUCHE, ligneVide);
            }

            if (!JadeStringUtil.isEmpty(ligneConfig.getTexteCentre())) {
                champs.put(AFFicheCartotheque_Doc.F_CENTRE, ligneConfig.getTexteCentre());
            } else {
                champs.put(AFFicheCartotheque_Doc.F_CENTRE, ligneVide);
            }

            if (!JadeStringUtil.isEmpty(ligneConfig.getTexteDroite())) {
                champs.put(AFFicheCartotheque_Doc.F_DROITE, ligneConfig.getTexteDroite());
            } else {
                champs.put(AFFicheCartotheque_Doc.F_DROITE, ligneVide);
            }

            dataSource.add(champs);
        }

        this.setDataSource(dataSource);
    }

    private void createEnTete() throws FWIException {
        try {
            this.setParametres(AFFicheCartotheque_Doc.P_IMG_PATH,
                    ((AFApplication) getSession().getApplication()).getExternalModelPath()
                            + AFFicheCartotheque_Doc.IMG_FILE);
            this.setParametres(AFFicheCartotheque_Doc.P_HEADER_LINE_1, textes.getTextes(1).getTexte(1).getDescription()
                    + " " + dateSituation);

            // caisse AVS
            String noCaisse = "150";

            if (caisseAVS != null) {
                noCaisse = caisseAVS.getAdministration().getCodeAdministration();
            }

            this.setParametres(AFFicheCartotheque_Doc.P_HEADER_LINE_2, textes.getTextes(1).getTexte(2).getDescription()
                    + " " + noCaisse);

            // caisse AF
            noCaisse = "";

            if (caisseAF != null) {
                noCaisse = caisseAF.getAdministration().getCodeAdministration();
            }

            this.setParametres(AFFicheCartotheque_Doc.P_HEADER_LINE_3, textes.getTextes(1).getTexte(3).getDescription()
                    + " " + noCaisse);
        } catch (Exception e) {
            abortAndReThrow(new FWIException("Impossible de créer l'en-tête du document: " + e.getMessage(), e));
        }
    }

    /*
     * Crée les instances des lignes de configuration
     */
    private void createLigneConfigs() {
        lignesConfigs.add(new LigneConfig(0, 2, 1, 2, 2, 2, 3)); // titres cols
        lignesConfigs.add(new LigneConfig(0, 2, 4, 0, 0, 0, 0)); // sous-titres
        // cols
        lignesConfigs.add(new LigneConfig(4, 2, 5)); // référence
        lignesConfigs.add(new LigneConfig(5, 2, 6)); // nom/prénom
        lignesConfigs.add(new LigneConfig(6, 2, 7)); // caisse avs
        lignesConfigs.add(new LigneConfig(7, 2, 8)); // caisse AF
        lignesConfigs.add(new LigneConfig(8, 2, 9)); // agence communale AVS
        lignesConfigs.add(new LigneConfig(9, 2, 10)); // numéro d'affilié
        lignesConfigs.add(new LigneConfig(10, 2, 11)); // numéro AVS
        lignesConfigs.add(new LigneConfig(11, 2, 12)); // numéro de contribuable
        lignesConfigs.add(new LigneConfig(12, 2, 13)); // ancienne affiliation
        lignesConfigs.add(new LigneConfig(13, 2, 14)); // conjoint affilié
        lignesConfigs.add(new LigneConfig(14, 2, 15)); // etat civil
        lignesConfigs.add(new LigneConfig(15, 2, 16)); // nationalité
        lignesConfigs.add(new LigneConfig(16, 2, 17)); // personnalité juridique
        lignesConfigs.add(new LigneConfig(17, 2, 18)); // code professionnel
        lignesConfigs.add(new LigneConfig(18, 2, 19)); // adresse de courrier
        lignesConfigs.add(new LigneConfig(19, 2, 20)); // adresse privée
        lignesConfigs.add(new LigneConfig(20, 2, 21)); // agence communale AVS
        lignesConfigs.add(new LigneConfig(21, 2, 22)); // adresse d'exploitation
        lignesConfigs.add(new LigneConfig(22, 2, 23)); // adresse de paiement
        lignesConfigs.add(new LigneConfig(27, 2, 30)); // contacts: téléphone
        lignesConfigs.add(new LigneConfig(28, 2, 31)); // fax
        lignesConfigs.add(new LigneConfig(29, 2, 32)); // email
        lignesConfigs.add(new LigneConfig(30, 2, 33)); // cotisations
        // personnelles
        lignesConfigs.add(new LigneConfig(31, 2, 34)); // date d'affiliation
        lignesConfigs.add(new LigneConfig(32, 2, 35)); // date de radiation
        lignesConfigs.add(new LigneConfig(33, 2, 36)); // motif de radiation
        lignesConfigs.add(new LigneConfig(34, 2, 37)); // année de taxation
        lignesConfigs.add(new LigneConfig(35, 2, 38)); // genre de taxation
        lignesConfigs.add(new LigneConfig(36, 2, 39)); // cotisations annuelles
        lignesConfigs.add(new LigneConfig(37, 2, 40)); // revenu soumis à
        // cotisation
        lignesConfigs.add(new LigneConfig(38, 2, 41)); // periodicite
        lignesConfigs.add(new LigneConfig(39, 2, 42)); // cotisations paritaires
        lignesConfigs.add(new LigneConfig(40, 2, 34)); // date d'affiliation
        lignesConfigs.add(new LigneConfig(41, 2, 35)); // date de radiation
        lignesConfigs.add(new LigneConfig(42, 2, 36)); // motif de radiation
        lignesConfigs.add(new LigneConfig(43, 2, 43)); // forfait AVS
        lignesConfigs.add(new LigneConfig(44, 2, 44)); // forfaire AC
        lignesConfigs.add(new LigneConfig(45, 2, 45)); // forfait AF
        lignesConfigs.add(new LigneConfig(46, 2, 46)); // forfait collaborateur
        // agricole
        lignesConfigs.add(new LigneConfig(47, 2, 47)); // forfait de travailleur
        // agricole
        lignesConfigs.add(new LigneConfig(48, 2, 41)); // periodicite
        // lignesConfigs.add(new LigneConfig(49, 2, 49)); // situation AF
        lignesConfigs.add(new LigneConfig(50, 2, 49)); // exonération générale
        lignesConfigs.add(new LigneConfig(51, 2, 50)); // dernier contrôle
        // d'employeur
        lignesConfigs.add(new LigneConfig(52, 2, 51)); // prochain contrôle
        // d'employeur
        lignesConfigs.add(new LigneConfig(53, 2, 52)); // genre de contrôle
        // d'employeur

        for (Iterator<LigneConfig> lignesIter = lignesConfigs.iterator(); lignesIter.hasNext();) {
            LigneConfig ligneConfig = lignesIter.next();

            idValToLigneConfig.put(ligneConfig.getIdVal(), ligneConfig);
        }
    }

    private String findAncienneAffiliation() throws Exception {
        String ancienneAffiliation = affiliation.getAncienAffilieNumero();

        if (ancienneAffiliation == null) {
            ancienneAffiliation = "";
        }

        if (affiliation.getOldAffiliation() != null) {
            ancienneAffiliation += " " + affiliation.getOldAffiliation().getTiers().getNom();
        }

        return ancienneAffiliation;
    }

    protected String format(String label, String[] args) {
        StringBuffer msgBuf = new StringBuffer();
        String message = getSession().getLabel(label);

        msgBuf.append(message.charAt(0));

        for (int idChar = 1; idChar < message.length(); ++idChar) {
            if ((message.charAt(idChar - 1) == '\'') && (message.charAt(idChar) != '\'')) {
                msgBuf.append('\'');
            }

            msgBuf.append(message.charAt(idChar));
        }

        return MessageFormat.format(msgBuf.toString(), args).toString();
    }

    /**
     * getter pour l'attribut affiliation id.
     * 
     * @return la valeur courante de l'attribut affiliation id
     */
    public String getAffiliationId() {
        return affiliationId;
    }

    /**
     * getter pour l'attribut date impression.
     * 
     * @return la valeur courante de l'attribut date impression
     */
    public String getDateSituation() {
        return dateSituation;
    }

    public List<String> getDisplayFields() {
        return displayFieds;
    }

    private String getLangueDestinataire() {
        // la fiche cartothèque est conservée pour la caisse, c'est donc la
        // langue de la session qui est la bonne
        return getSession().getIdLangueISO();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    // retourne le libelle du type d'assurance s'il s'agit d'un independant ou
    // null sinon.
    private String libelleTypeAffiliationCotPar() {
        if (CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equals(affiliation.getTypeAffiliation())) {
            return getSession().getCodeLibelle(affiliation.getTypeAffiliation());
        } else {
            return getSession().getCodeLibelle(affiliation.getTypeAffiliation());
        }
    }

    // retourne le libelle du type d'assurance s'il s'agit d'un independant ou
    // null sinon.
    private String libelleTypeAffiliationCotPers() {
        if (CodeSystem.TYPE_AFFILI_INDEP.equals(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equals(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_NON_ACTIF.equals(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE.equals(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_TSE.equals(affiliation.getTypeAffiliation())) {

            return getSession().getCodeLibelle(affiliation.getTypeAffiliation());
        } else {
            return getSession().getCodeLibelle(affiliation.getTypeAffiliation());
        }

    }

    /**
     * charger l'affiliation
     * 
     * @throws FWIException
     */
    private void loadAffiliation() throws FWIException {
        try {
            affiliation = new AFAffiliation();
            affiliation.setSession(getSession());
            affiliation.setAffiliationId(affiliationId);
            affiliation.retrieve();

            // date de situation si pas saisie
            if (JadeStringUtil.isEmpty(getDateSituation())) {
                setDateSituation(AFAffiliationUtil.getDateSituation(affiliation));
            }

            super.setDocumentTitle(getSession().getLabel("TITRE_DOC_CARTOTHEQUE") + " "
                    + affiliation.getAffilieNumero());

            // charger les moyens de communications du tiers
            TIMoyenCommunicationManager moyenCommunicationManager = new TIMoyenCommunicationManager();
            moyenCommunicationManager.setForIdContact(affiliation.getIdTiers());
            moyenCommunicationManager.setSession(getSession());
            moyenCommunicationManager.find();

            for (int idMoyen = 0; idMoyen < moyenCommunicationManager.size(); ++idMoyen) {
                TIMoyenCommunication moyenCommunication = (TIMoyenCommunication) moyenCommunicationManager.get(idMoyen);

                if (TIMoyenCommunication.EMAIL.equals(moyenCommunication.getTypeCommunication())) {
                    email = moyenCommunication.getMoyen();
                } else if (TIMoyenCommunication.FAX.equals(moyenCommunication.getTypeCommunication())) {
                    fax = moyenCommunication.getMoyen();
                } else if (TIMoyenCommunication.PRIVE.equals(moyenCommunication.getTypeCommunication())
                        && JadeStringUtil.isEmpty(telephone)) {
                    telephone = moyenCommunication.getMoyen();
                } else if (TIMoyenCommunication.PROFESSIONNEL.equals(moyenCommunication.getTypeCommunication())) {
                    telephone = moyenCommunication.getMoyen();
                } else if (TIMoyenCommunication.PORTABLE.equals(moyenCommunication.getTypeCommunication())) {
                    telephone = moyenCommunication.getMoyen();
                }
            }

            // charger les suivis de caisse
            JACalendar cal = getSession().getApplication().getCalendar();
            JADate dateSituation = new JADate(this.dateSituation);

            for (Iterator suiviIter = affiliation.getSuiviCaisseList().iterator(); suiviIter.hasNext();) {
                AFSuiviCaisseAffiliation suiviCaisseAffiliation = (AFSuiviCaisseAffiliation) suiviIter.next();

                if (CodeSystem.GENRE_CAISSE_AF.equals(suiviCaisseAffiliation.getGenreCaisse())
                        || CodeSystem.GENRE_CAISSE_AVS.equals(suiviCaisseAffiliation.getGenreCaisse())) {
                    JADate dateDebut = new JADate(suiviCaisseAffiliation.getDateDebut());
                    JADate dateFin = null;

                    if (!JadeStringUtil.isEmpty(suiviCaisseAffiliation.getDateFin())) {
                        dateFin = new JADate(suiviCaisseAffiliation.getDateFin());
                    }

                    if ((cal.compare(dateDebut, dateSituation) != JACalendar.COMPARE_FIRSTUPPER)
                            && ((dateFin == null) || (cal.compare(dateSituation, dateFin) != JACalendar.COMPARE_FIRSTUPPER))) {
                        if (CodeSystem.GENRE_CAISSE_AF.equals(suiviCaisseAffiliation.getGenreCaisse())) {
                            caisseAF = suiviCaisseAffiliation;
                        } else {
                            caisseAVS = suiviCaisseAffiliation;
                        }
                    }
                }
            }

            // trouver l'affiliation du conjoint
            TICompositionTiersManager compositionTiersManager = new TICompositionTiersManager();
            compositionTiersManager.setForIdTiersEnfantParent(affiliation.getIdTiers());
            compositionTiersManager.setForTypeLien(TICompositionTiers.CS_CONJOINT);
            compositionTiersManager.setForDateEntreDebutEtFin(this.dateSituation);
            compositionTiersManager.setSession(getSession());
            compositionTiersManager.find();

            AFAffiliationManager affiliationManager = new AFAffiliationManager();

            if (!compositionTiersManager.isEmpty()) {
                TICompositionTiers compositionTiers = (TICompositionTiers) compositionTiersManager.get(0);

                // charger l'affiliation du conjoint
                if (affiliation.getIdTiers().equals(compositionTiers.getIdTiersEnfant())) {
                    affiliationManager.setForIdTiers(compositionTiers.getIdTiersParent());
                } else {
                    affiliationManager.setForIdTiers(compositionTiers.getIdTiersEnfant());
                }

                affiliationManager.setForEntreDate(this.dateSituation);
                affiliationManager.setSession(getSession());
                affiliationManager.find();

                if (affiliationManager.size() == 1) {
                    AFAffiliation affiliationConjoint = (AFAffiliation) affiliationManager.get(0);

                    this.affiliationConjoint = affiliationConjoint.getTiers().getNom() + "/"
                            + affiliationConjoint.getAffilieNumero();
                }
            }

            // trouver l'agence communale
            compositionTiersManager.setForIdTiersParent(affiliation.getIdTiers());
            compositionTiersManager.setForTypeLien("507007");
            compositionTiersManager.setForDateEntreDebutEtFin(this.dateSituation);
            compositionTiersManager.setSession(getSession());
            compositionTiersManager.find();

            if (!compositionTiersManager.isEmpty()) {
                agenceCommunale = ((TICompositionTiers) compositionTiersManager.get(0)).getNomTiersEnfant();
            }

            // agence communale domicile
            compositionTiersManager.setForTypeLien("507008");
            compositionTiersManager.find();
            if (!compositionTiersManager.isEmpty()) {
                agenceCommunaleDom = ((TICompositionTiers) compositionTiersManager.get(0)).getNomTiersEnfant();
            }

            // trouver les contrôles d'employeur
            JADate dateEffective = null;
            if (AFUtil.isNouveauControleEmployeur(getSession())) {

                CEControleEmployeurManager controleEmployeurManager = new CEControleEmployeurManager();

                controleEmployeurManager.setForAffiliationId(affiliationId);
                controleEmployeurManager.setSession(getSession());
                controleEmployeurManager.find();

                for (int idControle = 0; idControle < controleEmployeurManager.size(); ++idControle) {
                    CEControleEmployeur controleEmployeur = (CEControleEmployeur) controleEmployeurManager
                            .get(idControle);

                    if (JadeStringUtil.isEmpty(controleEmployeur.getDateEffective())) {
                        // date effective est vide, il s'agit du prochain contrôle
                        // prévu
                        anneeProchainControleEmployeur = controleEmployeur.getDatePrevue().substring(6);
                    } else {
                        JADate date = new JADate(controleEmployeur.getDatePrevue());

                        if ((dateEffective == null)
                                || (cal.compare(dateEffective, date) == JACalendar.COMPARE_SECONDUPPER)) {
                            dateEffective = date;

                            CEReviseurManager revManager = new CEReviseurManager();
                            revManager.setSession(getSession());
                            revManager.setForVisa(controleEmployeur.getControleurNom());
                            revManager.find();

                            if (!revManager.isEmpty()) {
                                genreProchainControleEmployeur = ((CEReviseur) revManager.getFirstEntity())
                                        .getNomReviseur();
                            }
                        }
                    }
                }

                if (dateEffective != null) {
                    anneeProchainControleEmployeur = String.valueOf(dateEffective.getYear());
                }

            } else {
                AFControleEmployeurManager controleEmployeurManager = new AFControleEmployeurManager();

                controleEmployeurManager.setForAffiliationId(affiliationId);
                controleEmployeurManager.setSession(getSession());
                controleEmployeurManager.find();

                for (int idControle = 0; idControle < controleEmployeurManager.size(); ++idControle) {
                    AFControleEmployeur controleEmployeur = (AFControleEmployeur) controleEmployeurManager
                            .get(idControle);

                    if (JadeStringUtil.isEmpty(controleEmployeur.getDateEffective())) {
                        // date effective est vide, il s'agit du prochain contrôle
                        // prévu
                        anneeProchainControleEmployeur = controleEmployeur.getDatePrevue().substring(6);
                    } else {
                        JADate date = new JADate(controleEmployeur.getDatePrevue());

                        if ((dateEffective == null)
                                || (cal.compare(dateEffective, date) == JACalendar.COMPARE_SECONDUPPER)) {
                            dateEffective = date;
                            genreProchainControleEmployeur = controleEmployeur.getControleurNom();
                        }
                    }
                }

                if (dateEffective != null) {
                    anneeProchainControleEmployeur = String.valueOf(dateEffective.getYear());
                }
            }

            // charger les cotisations
            AFCotisationManager cotisationManager = new AFCotisationManager();
            String exceptionForfaitAVS = null;
            String exceptionForfaitAC = null;
            String exceptionForfaitAF = null;

            cotisationManager.setForAffiliationId(affiliationId);
            cotisationManager.setForDate(this.dateSituation);
            cotisationManager.setSession(getSession());
            cotisationManager.find();

            BigDecimal masseAnnuelleTotale = new BigDecimal("0");
            for (int idCotis = 0; idCotis < cotisationManager.size(); ++idCotis) {
                AFCotisation cotisation = (AFCotisation) cotisationManager.get(idCotis);

                if (CodeSystem.GENRE_ASS_PARITAIRE.equals(cotisation.getAssurance().getAssuranceGenre())) {
                    if (cotisation.getAssurance().getTypeAssurance().equals(CodeSystem.TYPE_ASS_COTISATION_AVS_AI)) {
                        if (CodeSystem.MOTIF_FIN_EXCEPTION.equals(cotisation.getMotifFin())) {
                            exceptionForfaitAVS = cotisation.getMasseAnnuelle();
                        } else {
                            masseAnnuelleTotale = masseAnnuelleTotale
                                    .add(new BigDecimal(cotisation.getMasseAnnuelle()));
                        }
                    } else if (cotisation.getAssurance().getTypeAssurance().equals(CodeSystem.TYPE_ASS_COTISATION_AF)) {
                        if (CodeSystem.MOTIF_FIN_EXCEPTION.equals(cotisation.getMotifFin())) {
                            exceptionForfaitAF = cotisation.getMasseAnnuelle();
                        } else {
                            forfaitAF = cotisation.getMasseAnnuelle();
                        }
                    } else if (cotisation.getAssurance().getTypeAssurance().equals("812006")) {
                        // cotisation AC
                        if (CodeSystem.MOTIF_FIN_EXCEPTION.equals(cotisation.getMotifFin())) {
                            exceptionForfaitAC = cotisation.getMasseAnnuelle();
                        } else {
                            forfaitAC = cotisation.getMasseAnnuelle();
                        }
                    } else if (cotisation.getAssurance().getAssuranceId().equals("12")) {
                        // collaborateur agricole
                        forfaitCollaborateurAgri = cotisation.getMasseAnnuelle();
                    } else if (cotisation.getAssurance().getAssuranceId().equals("14")) {
                        // collaborateur agricole
                        forfaitTravailleurAgri = cotisation.getMasseAnnuelle();
                    }
                } else {
                    // cotisations personnelles pour l'assurance AVS
                    if (cotisation.getAssurance().getTypeAssurance().equals(CodeSystem.TYPE_ASS_COTISATION_AVS_AI)) {
                        anneeTaxation = cotisation.getAnneeDecision();
                        cotisationAnnulle = cotisation.getMontantAnnuel();
                    }
                }
            }

            forfaitAVS = JANumberFormatter.formatNoRound(masseAnnuelleTotale.toString(), 2);

            // utiliser les exceptions si nécessaire
            if (!JadeStringUtil.isEmpty(exceptionForfaitAC)) {
                forfaitAC = exceptionForfaitAC;
            }

            if (!JadeStringUtil.isEmpty(exceptionForfaitAF)) {
                forfaitAF = exceptionForfaitAF;
            }

            if (!JadeStringUtil.isEmpty(exceptionForfaitAVS)) {
                forfaitAVS = exceptionForfaitAVS;
            }
        } catch (FWIException e) {
            abortAndReThrow(e);
        } catch (Exception e) {
            abortAndReThrow(new FWIException("Une erreur est survenue lors du chargement de l'affiliation: "
                    + e.getMessage(), e));
        }
    }

    /**
     * charge le catalogue de texte pour ce document.
     * 
     * @throws FWIException
     *             si le catalogue ne peut être trouvé
     */
    private void loadCatalogue() throws FWIException {
        try {
            ICTDocument loader = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

            loader.setActif(Boolean.TRUE);
            loader.setDefault(Boolean.TRUE);
            loader.setCsDomaine(CodeSystem.DOMAINE_CAT_AFF);
            loader.setCsTypeDocument(CodeSystem.TYPE_CAT_FICHE_CARTOTHEQUE);
            loader.setCodeIsoLangue(getLangueDestinataire());

            ICTDocument[] candidats = loader.load();

            if ((candidats != null) || (candidats.length != 0)) {
                textes = candidats[0];
            } else {
                textes = null;
            }
        } catch (Exception e) {
            abortAndReThrow(new FWIException("Impossible de charger le catalogue de texte pour ce document: "
                    + e.getMessage(), e));
        }

        if (textes == null) {
            abortAndReThrow(new FWIException("Impossible de charger le catalogue de texte pour ce document"));
        }
    }

    private String motifFin() {
        String motifFin = affiliation.getMotifFin();

        if ("0".equals(motifFin)) {
            return "";
        } else {
            return motifFin;
        }
    }

    @Override
    public boolean next() throws FWIException {
        boolean retValue = hasNext;

        if (hasNext) {
            hasNext = !hasNext;
        }

        return retValue;
    }

    /**
     * renseigne les valeurs de la colonne de droite du document de fiche cartotheque.
     * 
     * @throws Exception
     */
    private void peuplerLignes() throws Exception {
        // référence
        this.setValeur(4, getSession().getCodeLibelle(affiliation.getTiers().getTitreTiers()));
        // nom/prénom
        this.setValeur(5, affiliation.getTiers().getNom());
        // caisse avs
        this.setValeur(6, (caisseAVS != null) ? caisseAVS.getAdministration().getCodeAdministration() : "150");
        // caisse AF
        this.setValeur(7, (caisseAF != null) ? caisseAF.getAdministration().getCodeAdministration() : "");
        // agence communale AVS
        this.setValeur(8, agenceCommunale);
        // numéro d'affilié
        this.setValeur(9, affiliation.getAffilieNumero(), 14);
        // numéro AVS
        this.setValeur(10, affiliation.getTiers().getNumAvsActuel());
        // numéro de contribuable
        this.setValeur(11, affiliation.getTiers().getNumContribuableActuel());
        // ancienne affiliation
        this.setValeur(12, findAncienneAffiliation());
        // conjoint affilié
        this.setValeur(13, affiliationConjoint);
        // etat civil
        this.setValeur(14, getSession().getCodeLibelle(affiliation.getTiers().getEtatCivil()));
        // nationalité
        this.setValeur(15, affiliation.getTiers().getPays().getLibelle());
        // personnalité juridique
        this.setValeur(16, getSession().getCodeLibelle(affiliation.getPersonnaliteJuridique()));
        // code professionnel
        this.setValeur(17, getSession().getCodeLibelle(affiliation.getBrancheEconomique()));
        // adresse de courrier
        this.setValeur(18, adresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER));
        // adresse privée
        this.setValeur(19, adresse(IConstantes.CS_AVOIR_ADRESSE_DOMICILE));
        // agence communale AVS
        this.setValeur(20, agenceCommunaleDom);
        // adresse d'exploitation
        this.setValeur(21, adresse("508021"));
        this.setValeur(22, JadeStringUtil.change(affiliation.getTiers().getAdressePrincipalePaiement(), "\n", " / ")); // adresse
        // de paiement
        this.setValeur(27, telephone); // contacts: téléphone
        this.setValeur(28, fax); // fax
        this.setValeur(29, email); // email

        // cotisations personnelles
        if (CodeSystem.TYPE_AFFILI_INDEP.equals(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equals(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_NON_ACTIF.equals(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_TSE.equals(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE.equals(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_FICHIER_CENT.equals(affiliation.getTypeAffiliation())) {

            this.setValeur(30, libelleTypeAffiliationCotPers()); // cotisations
            // personnelles
            this.setValeur(31, affiliation.getDateDebut().substring(3)); // date
            // d'affiliation
            this.setValeur(32, JadeStringUtil.isEmpty(affiliation.getDateFin()) ? "" : affiliation.getDateFin()
                    .substring(3)); // date
            // de
            // radiation
            this.setValeur(33, getSession().getCodeLibelle(motifFin())); // motif de
            // radiation
            this.setValeur(34, anneeTaxation); // année de taxation
            this.setValeur(35, ""); // TODO: genre de taxation
            this.setValeur(36, "Sfr. " + cotisationAnnulle); // cotisations annuelles
            this.setValeur(37, ""); // TODO: revenu soumis à cotisation
            this.setValeur(38, getSession().getCodeLibelle(affiliation.getPeriodicite())); // periodicite
        }

        // cotisations paritaires
        if ((((CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equals(affiliation.getTypeAffiliation()))
                || (CodeSystem.TYPE_AFFILI_EMPLOY.equals(affiliation.getTypeAffiliation()))
                || (CodeSystem.TYPE_AFFILI_EMPLOY_D_F.equals(affiliation.getTypeAffiliation()))
                || (CodeSystem.TYPE_AFFILI_LTN.equals(affiliation.getTypeAffiliation())) || (CodeSystem.TYPE_AFFILI_FICHIER_CENT
                    .equals(affiliation.getTypeAffiliation()))))) {
            this.setValeur(39, libelleTypeAffiliationCotPar()); // cotisations
            // paritaires
            this.setValeur(40, affiliation.getDateDebut().substring(3)); // date
            // d'affiliation
            this.setValeur(41, JadeStringUtil.isEmpty(affiliation.getDateFin()) ? "" : affiliation.getDateFin()
                    .substring(3)); // date
            // de
            // radiation
            this.setValeur(42, getSession().getCodeLibelle(motifFin())); // motif de
            // radiation
            this.setValeur(43, forfaitAVS); // forfait AVS
            this.setValeur(44, forfaitAC); // forfaire AC
            this.setValeur(45, forfaitAF); // forfait AF
            this.setValeur(46, forfaitCollaborateurAgri); // forfait collaborateur
            // agricole
            this.setValeur(47, forfaitTravailleurAgri); // forfait de travailleur
            // agricole
            this.setValeur(48, getSession().getCodeLibelle(affiliation.getPeriodicite())); // periodicite
        }

        this.setValeur(
                50,
                getSession().getLabel(
                        affiliation.isExonerationGenerale().booleanValue() ? "NAOS_LIBELLE_OUI" : "NAOS_LIBELLE_NON")); // exonération
        // générale
        this.setValeur(51, anneeDernierControleEmployeur); // dernier contrôle
        // d'employeur
        this.setValeur(52, anneeProchainControleEmployeur); // prochain contrôle
        // d'employeur
        this.setValeur(53, genreProchainControleEmployeur); // genre de contrôle
        // d'employeur
    }

    /**
     * setter pour l'attribut affiliation id.
     * 
     * @param affiliationId
     *            une nouvelle valeur pour cet attribut
     */
    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
    }

    /**
     * setter pour l'attribut date impression.
     * 
     * @param dateImpression
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateSituation(String dateImpression) {
        dateSituation = dateImpression;
    }

    /**
     * 
     * @param value
     */
    public void setDisplayFields(List value) {
        if (displayFieds == null) {
            displayFieds = new ArrayList<String>();
        }
        displayFieds.clear();
        displayFieds.addAll(value);
    }

    /**
     * 
     * @param fields
     */
    public void setDisplayFields(String fields) {
        if (displayFieds == null) {
            displayFieds = new ArrayList<String>();
        }
        displayFieds.add(fields);
    }

    /**
     * renseigne la valeur de la colonne de droite en utilisant la taille par défaut.
     * 
     * @param idChamp
     *            le numéro de la valeur tel que défini dans le mandat 104
     * @param valeur
     *            la valeur
     */
    private void setValeur(int idChamp, String valeur) {
        this.setValeur(idChamp, valeur, AFFicheCartotheque_Doc.DEFAULT_SIZE);
    }

    /**
     * renseigne la valeur de la colonne de droite en utilisant une taille de caractère différente.
     * 
     * @param idChamp
     *            le numéro de la valeur tel que défini dans le mandat 104
     * @param valeur
     *            la valeur
     * @param size
     *            la taille de caractère
     * 
     * @throws NullPointerException
     *             si idchamp ne correspond pas à une valeur exacte.
     */
    private void setValeur(int idChamp, String valeur, int size) {
        LigneConfig ligneConfig = idValToLigneConfig.get(new Integer(idChamp));

        if (ligneConfig == null) {
            throw new NullPointerException("Il n'y a pas de ligne pour ce champ");
        }

        if (!JadeStringUtil.isEmpty(valeur)) {
            if ((size != AFFicheCartotheque_Doc.DEFAULT_SIZE)) {
                valeur = "<style size=\"" + size + "\">" + valeur + "</style>";
            }

            ligneConfig.setTexteDroite(valeur);
        }
    }
}
