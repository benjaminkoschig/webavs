package globaz.apg.itext.decompte;

import globaz.apg.api.codesystem.IAPCatalogueTexte;
import globaz.apg.api.process.IAPGenererCompensationProcess;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.lots.APFactureACompenser;
import globaz.apg.db.lots.APFactureACompenserManager;
import globaz.apg.db.lots.APLot;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.db.prestation.APRepartitionJointPrestationManager;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.itext.decompte.utils.APDecompte;
import globaz.apg.itext.decompte.utils.APDecompteGenerationParameterValidationError;
import globaz.apg.itext.decompte.utils.APDecompteGenerationParametersValidator;
import globaz.apg.itext.decompte.utils.APMethodeRegroupement;
import globaz.apg.itext.decompte.utils.APPrestationJointRepartitionPOJO;
import globaz.apg.properties.APPropertyTypeDePrestationAcmValues;
import globaz.babel.api.ICTDocument;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.employeurs.PRDepartement;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Le r�le de cette classe est de pr�parer les donn�es (r�cup�ration. regroupement, catalogue de textes, etc)
 * pour la g�n�ration des d�comptes </br>
 * Les �tapes de g�n�ration des d�comptes sont repr�sent� par le type �num�r� 'TraitementCourant'
 * Au d�part, nous sommes dans un �tat 'UNDEFINI', cela signifie qu'aucun document n'a encore �t� g�n�r�.
 * La g�n�ration des d�comptes est compos�e de 3 �tapes principales :</br>
 * - RECUPERATION_AGREGATION_DONNEES : comme son nom l'indique, pr�paration es donn�es et regroupement pour
 * les r�partitions et les ventilations
 * - TRAITEMENT_REPARTITIONS : passage ou l'on va s'occuper de la g�n�ration des d�comptes
 * li�s au r�partitions de paiement
 * - TRAITEMENT_VENTILATIONS : passage ou l'on va s'occuper de la g�n�ration des d�comptes li�s au ventilations</br>
 * Ces �tapes sont mod�lis� par le type �num�r� TraitementCourant. Au d�part, nous sommes
 * dans un �tat 'UNDEFINI', cela signifie qu'aucun document n'a encore �t� g�n�r�.
 * 
 * @author lga
 */
public class APDecompteGenerationProcess extends APAbstractDecomptesGenerationProcess {

    /**
     * D�finit les diff�rentes �tapes du processus Par d�faut, au d�marrage de ce processus nous sommes � l'�tat
     * ind�finis.. H� ouais !
     * 
     * @author lga
     */
    private enum TraitementCourant {
        /**
         * �tat par d�faut au d�marrage du processus
         */
        UNDEFINI,
        /**
         * Pendant cette phase, on s'occupe uniquement de la r�cup�ration et de la pr�paration des donn�es pour la
         * g�n�ration des documents
         */
        RECUPERATION_AGREGATION_DONNEES,
        /**
         * A cette �tape, le processus s'occupe de la g�n�ration des d�comptes li�s aux r�partions de paiement
         */
        TRAITEMENT_REPARTITIONS,
        /**
         * A cette �tape, le processus s'occupe de la g�n�ration des d�comptes li�s aux ventilations
         */
        TRAITEMENT_VENTILATIONS
    }

    private static final long serialVersionUID = -7380328901650776740L;

    /**
     * Id du lot � traiter lors de la g�n�ration des d�comptes
     */
    private String idLot;

    /**
     * Le type de prestation ACM que propose la caisse.
     * 
     * @see APPropertyTypeDePrestationAcmValues
     */
    private APPropertyTypeDePrestationAcmValues typeDePrestationAcm;
    /**
     * La date comptable
     */
    private JADate dateComptable = null;
    /**
     * La date sur le document
     */
    private JADate dateDocument;

    /**
     * OK Est-ce que les d�comptes doivent �tres envoy�es � la GED
     */
    private Boolean isSendToGED;

    private String codeIsoLangue;

    private int idAssuranceAvsParitaire;
    private int idAssuranceAvsPersonnelle;
    private int idAssuranceAcParitaire;
    private int idAssuranceLfaParitaire;
    private int idAssuranceLfaPersonnelle;
    private int idAssuranceFneParitaire;

    private boolean isAfficherConfidentielSurDocument;
    private boolean isAfficherNIPSurDocument;
    private boolean isDecompteRecapitulatif;
    private boolean isNumeroAffiliePourGEDForceAZeroSiVide;
    private boolean isBlankIndexGedNssAZero;

    // ------------------------------------------------------------------
    // Champs de classe internes
    // ------------------------------------------------------------------

    /**
     * OK Renseigne si les prestation standard doivent �tre regroup�es sur un m�me d�compte
     */
    private boolean isregroupementPrestationStandardEtACM_NE = false;

    /**
     * Renseigne sur l'�tape en cours dans ce processus
     */
    private TraitementCourant traitementCourant;

    /**
     * OK Map contenant les d�comptes regroup�es r�cup�r� lors du 1er passage -> traitement des r�partitions
     */
    private Map<String, APDecompte> repartitions;

    /**
     * Iterator utilis� pour boucler sur les d�comptes des repartitions
     */
    private Iterator<String> iteratorRepartitions;

    /**
     * OK Map contenant les d�comptes regroup�es r�cup�r� lors du 2�me passage -> traitement des ventilations
     */
    private Map<String, APDecompte> ventilations;

    /**
     * Iterator utilis� pour boucler sur les d�comptes des ventilations
     */
    private Iterator<String> iteratorVentilations;

    /**
     * Contient le type de prestations contenues dans le lot sous forme de code syst�me
     */
    private String csTypePrestationsLot;

    /**
     * Utilis� pour charger les catalogues de textes
     */
    private ICTDocument documentHelper;

    private final Map<String, ICTDocument> documents = new HashMap<String, ICTDocument>();

    /**
     * Default no-arg constructor
     * 
     * @throws FWIException
     */
    public APDecompteGenerationProcess() throws FWIException {
        super();
        traitementCourant = TraitementCourant.UNDEFINI;
    }

    /**
     * Le but de cette m�thode est la pr�paration des donn�es pour la g�n�ration des d�comptes �tat initial du
     * traitement : UNDEFINI �tat apr�s traitement : TRAITEMENT_DONNEES
     */
    @Override
    public void preparerDonneesPourDecomptes() throws Exception {

        // Aucune action n'a encore �t� r�alis�e pas ce processus, on doit �tre en �tat ind�fini !
        if (!TraitementCourant.UNDEFINI.equals(traitementCourant)) {
            throw new Exception(
                    "APDecompteGenerationProcess.preparerDonneesPourDecomptes() : When process start, traitementCourant must be in state TraitementCourant.UNDEFINI");
        } else {
            traitementCourant = TraitementCourant.RECUPERATION_AGREGATION_DONNEES;
        }

        /*
         * Si la caisse propose des prestations ACM_NE, ces prestations seront regroup�es aves les prestations standard
         */
        isregroupementPrestationStandardEtACM_NE = APPropertyTypeDePrestationAcmValues.ACM_NE
                .equals(getTypeDePrestationAcm());

        // Pr�paration des donn�es pour les r�partitions et les ventilations
        repartitions = preparerDonneesPourDecomptesDesRepartition();
        ventilations = preparerDonneesPourDecomptesDesVentilation();

        // Pr�paration des it�rateurs
        iteratorRepartitions = repartitions.keySet().iterator();
        iteratorVentilations = ventilations.keySet().iterator();

        // Pr�paration du helper pour le catalogue de textes
        preparerHelperCatalogueDetextes();

        // APG ou MAT ?!
        csTypePrestationsLot = loadCsTypePrestation();

        // MAJ de l'�tape du processus
        traitementCourant = TraitementCourant.RECUPERATION_AGREGATION_DONNEES;

        /*
         * Fin de traitement des donn�es, la prochaine �tape est la g�n�ration de chaque document via les appels r�alis�
         * sur la m�thode next()
         */
    }

    /**
     * @throws Exception
     */
    private void preparerHelperCatalogueDetextes() throws Exception {
        documentHelper = PRBabelHelper.getDocumentHelper(getISession());
        documentHelper.setCsDomaine(IPRDemande.CS_TYPE_APG.equals(loadCsTypePrestation()) ? IAPCatalogueTexte.CS_APG
                : IAPCatalogueTexte.CS_MATERNITE);

        documentHelper
                .setCsTypeDocument(IPRDemande.CS_TYPE_APG.equals(loadCsTypePrestation()) ? IAPCatalogueTexte.CS_DECOMPTE_APG
                        : IAPCatalogueTexte.CS_DECOMPTE_MAT);

        documentHelper.setActif(Boolean.TRUE);
    }

    private Map<String, APDecompte> preparerDonneesPourDecomptesDesVentilation() throws Exception {

        // 1 - on r�cup�re l'ensemble des r�partitions contenue dans le lot
        final List<APRepartitionJointPrestation> repartitionDB = getRepartitionsDuLot();

        // 2 - On r�cup�re toutes les ventilations de ces r�partitions. Les ventilations sont �galement des
        // r�partitions....
        final List<APRepartitionJointPrestation> ventilationsDB = recupererLesVentilations(repartitionDB);

        // 3 - on convertis les entit�s DB vers des structure de donn�es plus adapt�es � la g�n�ration des d�comptes
        final List<APPrestationJointRepartitionPOJO> ventilationsPojo = conversionDonneesDBVersDonneesDecompte(ventilationsDB);

        // 5 - On g�n�re les cl�s de regroupement en fonction des param�tres de regroupement pour chaque ventilation
        genererClesDeRegroupement(ventilationsPojo);

        // 5 - Regroupement des prestations pour chaque d�comptes
        final Map<String, APDecompte> decomptes = regrouperPrestation(ventilationsPojo);

        // Ok, maintenant que les prestation sont regroup�es, ils s'agit de d�terminer quelle est le type de chaque
        // rapport
        for (final String key : decomptes.keySet()) {
            decomptes.get(key).determinerLeTypeDuDecompte();
        }

        // // 4 - Traitement sp�cifique aux ventilations
        // this.traiterVentilation(decomptes);

        return decomptes;
    }

    /**
     * Retourne une liste contenant toutes les ventilations li�es aux r�partition de paiement pr�sentes dans la liste
     * des r�partitions fournies en param�tres
     * 
     * @param repartitions
     * @return
     * @throws Exception
     */
    private List<APRepartitionJointPrestation> recupererLesVentilations(
            final List<APRepartitionJointPrestation> repartitions) throws Exception {
        // classer les r�partitions par b�n�ficiaire des ventilations
        final List<APRepartitionJointPrestation> ventilations = new ArrayList<APRepartitionJointPrestation>();
        final APRepartitionJointPrestationManager ventilationManager = new APRepartitionJointPrestationManager();
        ventilationManager.setSession(getSession());

        for (final APRepartitionJointPrestation repartition : repartitions) {
            ventilationManager.setForIdParent(repartition.getIdRepartitionBeneficiairePaiement());
            ventilationManager.find(BManager.SIZE_NOLIMIT);

            for (final Object o : ventilationManager.getContainer()) {
                final APRepartitionJointPrestation ventilation = (APRepartitionJointPrestation) o;
                ventilations.add(ventilation);
            }
        }
        return ventilations;
    }

    /**
     * @throws Exception
     */
    private Map<String, APDecompte> preparerDonneesPourDecomptesDesRepartition() throws Exception {

        // 1 - on r�cup�re l'ensemblle des donn�es � traiter pour la g�n�ration des d�comptes
        final List<APRepartitionJointPrestation> repartitionDB = getRepartitionsDuLot();

        // 2 - on convertis les entit�s retourn�es par la DB vers des structure de donn�es plus adapt�es � la g�n�ration
        // des d�comptes
        final List<APPrestationJointRepartitionPOJO> repartitionPojo = conversionDonneesDBVersDonneesDecompte(repartitionDB);

        // 4 - On g�n�re les cl�s de regroupement en fonction des param�tres de regroupement pour chaque r�partition
        genererClesDeRegroupement(repartitionPojo);

        // 5 - Regroupement des prestations pour chaque d�comptes
        final Map<String, APDecompte> decomptes = regrouperPrestation(repartitionPojo);

        // Ok, maintenant que les prestation sont regroup�es, ils s'agit de d�terminer quelle est le type de chaque
        // rapport
        for (final String key : decomptes.keySet()) {
            decomptes.get(key).determinerLeTypeDuDecompte();
        }

        // // 3- Pour chacun des �l�ments retourn�s depuis la DB, on lui ajoute tout ce qui concerne les r�partitions
        // // enfants et les ventilations
        // this.ajouterVentilationsEtCompensations(decomptes);

        return decomptes;
    }

    private void genererClesDeRegroupement(final List<APPrestationJointRepartitionPOJO> data) {
        for (final APPrestationJointRepartitionPOJO repartition : data) {
            if (isregroupementPrestationStandardEtACM_NE) {
                repartition.genererCleDeRegroupement(APMethodeRegroupement.STANDARD_ACM_NE);
            } else {
                repartition.genererCleDeRegroupement(APMethodeRegroupement.SEPARE);
            }
        }
    }

    /**
     * Parcours l'ensemble des prestations fournies en param�tres et les tries en fonction de la cl� de regroupement
     * 
     * @param donnees
     *            L'ensemble des prestations � trier
     * @return Une liste contenant des objets Decompte
     * @throws Exception
     */
    private Map<String, APDecompte> regrouperPrestation(final List<APPrestationJointRepartitionPOJO> donnees)
            throws Exception {
        // final Map<String, List<PrestationJointRepartitionPOJO>> decomptesRegroupes = new HashMap<String,
        // List<PrestationJointRepartitionPOJO>>();
        final Map<String, APDecompte> decomptes = new HashMap<String, APDecompte>();

        final APFactureACompenserManager factures = new APFactureACompenserManager();
        factures.setSession(getSession());

        final APRepartitionJointPrestationManager ventilations = new APRepartitionJointPrestationManager();
        ventilations.setSession(getSession());

        for (final APPrestationJointRepartitionPOJO repartitionPojo : donnees) {

            final String key = repartitionPojo.getCleDeRegroupement();
            final APRepartitionJointPrestation repartitionCourante = repartitionPojo.getPrestationJointRepartition();
            APDecompte decompte = null;

            if (!decomptes.containsKey(key)) {
                decompte = new APDecompte(repartitionPojo);
                decomptes.put(key, decompte);
            } else {
                decompte = decomptes.get(key);
            }

            // Dans tous les cas on rajoute la r�partition comme r�partition p�re
            decompte.addRepartitionPere(repartitionPojo);

            ventilations.setForIdParent(repartitionCourante.getIdRepartitionBeneficiairePaiement());
            ventilations.find(BManager.SIZE_NOLIMIT);

            for (int idVentilation = 0; idVentilation < ventilations.size(); ++idVentilation) {
                decompte.addRepartitionEnfant((APRepartitionJointPrestation) ventilations.get(idVentilation));
            }

            // ajouter les factures � compenser pour cette r�partition
            if (!JadeStringUtil.isIntegerEmpty(repartitionCourante.getIdCompensation())) {
                factures.setForIdCompensationParente(repartitionCourante.getIdCompensation());
                factures.find(BManager.SIZE_NOLIMIT);

                for (int idCompensation = 0; idCompensation < factures.size(); ++idCompensation) {
                    decompte.addFactureACompenser((APFactureACompenser) factures.get(idCompensation));
                }
            }
        }
        return decomptes;
    }

    @SuppressWarnings("unchecked")
    private List<APRepartitionJointPrestation> getRepartitionsDuLot() throws Exception {
        final APRepartitionJointPrestationManager repartitionsMgr = new APRepartitionJointPrestationManager();
        repartitionsMgr.setSession(getSession());
        repartitionsMgr.setForIdLot(String.valueOf(getIdLot()));
        repartitionsMgr.setParentOnly(true);
        repartitionsMgr.setOrderBy(APRepartitionPaiements.FIELDNAME_IDTIERS + ","
                + APRepartitionPaiements.FIELDNAME_IDAFFILIE + "," + APPrestation.FIELDNAME_DATEDEBUT + ","
                + APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG);
        repartitionsMgr.find(BManager.SIZE_NOLIMIT);
        return repartitionsMgr.getContainer();
    }

    /**
     * @param data
     * @param repartitionsMgr
     * @throws Exception
     */
    private List<APPrestationJointRepartitionPOJO> conversionDonneesDBVersDonneesDecompte(
            final List<APRepartitionJointPrestation> repartitions) throws Exception {
        final List<APPrestationJointRepartitionPOJO> data = new ArrayList<APPrestationJointRepartitionPOJO>();

        for (final APRepartitionJointPrestation repartitionJointPrestation : repartitions) {

            APSituationProfessionnelle situationProfessionnelle = null;
            PRDepartement departement = null;
            APTypeDePrestation typeDePrestation = null;

            // !! il peut ne pas y avoir de situation prof !!
            situationProfessionnelle = recupererSituationProfessionnelle(repartitionJointPrestation);
            if (situationProfessionnelle != null) {
                final APEmployeur employeur = situationProfessionnelle.loadEmployeur();
                if (employeur != null) {
                    departement = employeur.loadDepartement();
                }
            }
            typeDePrestation = recupererTypeDePrestation(repartitionJointPrestation);

            data.add(new APPrestationJointRepartitionPOJO(repartitionJointPrestation, situationProfessionnelle,
                    departement, typeDePrestation, isModuleActifForPorterEnCompte(), getSession(), getTransaction()));
        }
        return data;
    }

    private boolean isModuleActifForPorterEnCompte() {
        boolean isModulePorterEnCompte = false;
        try {
            IAPGenererCompensationProcess moduleCompensation = (IAPGenererCompensationProcess) getSession()
                    .getApplication().getImplementationFor(getSession(), IAPGenererCompensationProcess.class);

            if (moduleCompensation != null && moduleCompensation instanceof BProcess) {
                isModulePorterEnCompte = moduleCompensation.isModulePorterEnCompte();
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        return isModulePorterEnCompte;
    }

    /**
     * @param repartitionJointPrestation
     * @return
     */
    private APTypeDePrestation recupererTypeDePrestation(final APRepartitionJointPrestation repartitionJointPrestation) {
        APTypeDePrestation typeDePrestation;
        final String codeSystemGenrePrestation = repartitionJointPrestation.getGenrePrestationPrestation();
        if (JadeStringUtil.isIntegerEmpty(codeSystemGenrePrestation)) {
            throw new IllegalArgumentException("Can not resolve the APTypeDePrestation for the empty systemCode ["
                    + codeSystemGenrePrestation + "]");
        }

        int codeSystemGenrePrestationInteger = 0;
        try {
            codeSystemGenrePrestationInteger = Integer.valueOf(codeSystemGenrePrestation);
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Can not resolve the APTypeDePrestation for the invalid systemCode ["
                    + codeSystemGenrePrestation + "]. Parent exception message : " + e.toString(), e);
        }
        typeDePrestation = APTypeDePrestation.resoudreTypeDePrestationParCodeSystem(codeSystemGenrePrestationInteger);
        if (typeDePrestation == null) {
            throw new IllegalArgumentException("Can not resolve the APTypeDePrestation for the systemCode ["
                    + codeSystemGenrePrestation + "]");
        }
        return typeDePrestation;
    }

    /**
     * Validation interne de l'ensemble des param�tres d'entr�e du processus de g�n�ration des d�comptes
     * 
     * @throws FWIException
     *             dans le cas ou des erreurs sont d�tect�es
     */
    public final void validationDesParametres() throws Exception {
        final APDecompteGenerationParametersValidator validator = new APDecompteGenerationParametersValidator();
        final List<APDecompteGenerationParameterValidationError> erreurs = validator.validProcess(this);
        if (erreurs.size() != 0) {
            final StringBuilder message = new StringBuilder();
            for (final APDecompteGenerationParameterValidationError erreur : erreurs) {
                message.append(getSession().getLabel(erreur.getLabelKey()));
            }
            throw new Exception(message.toString());
        }
    }

    @Override
    protected void _validate() throws Exception {
        validationDesParametres();
    }

    /**
     * R�cup�re une situation professionnelle en fonction de son id
     * 
     * @return la situation professionnelle si existante sinon <code>null</code>
     * @throws Exception
     *             En cas de probl�me lors de l'acc�s � la DB
     */
    private APSituationProfessionnelle recupererSituationProfessionnelle(
            final APRepartitionJointPrestation prestationJointRepartition) throws Exception {
        APSituationProfessionnelle situationProfessionnelle = null;
        final String idSitProf = prestationJointRepartition.getIdSituationProfessionnelle();

        if (!JadeStringUtil.isIntegerEmpty(idSitProf)) {
            situationProfessionnelle = new APSituationProfessionnelle();
            situationProfessionnelle.setSession(getSession());
            situationProfessionnelle.setIdSituationProf(idSitProf);
            situationProfessionnelle.retrieve();
        }

        if ((situationProfessionnelle != null) && situationProfessionnelle.isNew()) {
            situationProfessionnelle = null;
        }
        return situationProfessionnelle;
    }

    @Override
    public final boolean hasNextDocument() throws FWIException {
        boolean next = false;
        APDecompte decompteCourant = null;
        /*
         * Si la valeur du champs 'traitementCourant' == TRAITEMENT_DONNEES, c'est que la pr�paration des donn�es � �t�
         * r�alis�e mais qu'aucun document n'a encore �t� g�n�r� ! On ne passe qu'une fois dans cette portion du switch
         */
        if (TraitementCourant.RECUPERATION_AGREGATION_DONNEES.equals(traitementCourant)) {
            traitementCourant = TraitementCourant.TRAITEMENT_REPARTITIONS;
            next = iteratorRepartitions.hasNext();

            // Si oui, il faut r�cup�rer le d�compte en question. Si on ne trouve pas de r�partition � ce niveau, on ne
            // vas pas aller voir les ventilations
            if (next) {
                decompteCourant = repartitions.get(iteratorRepartitions.next());
            }
        } else if (TraitementCourant.TRAITEMENT_REPARTITIONS.equals(traitementCourant)) {
            // Si oui, il faut r�cup�rer le d�compte en question
            next = iteratorRepartitions.hasNext();
            if (next) {
                decompteCourant = repartitions.get(iteratorRepartitions.next());
            }

            /*
             * S'il n'y � plus de documents � g�n�rer pour les r�partitions, il faut g�n�rer les documents li�s aux
             * ventilations
             */
            if (!next) {
                // Si oui, il faut r�cup�rer le d�compte en question
                next = iteratorVentilations.hasNext();
                if (next) {
                    decompteCourant = ventilations.get(iteratorVentilations.next());
                    traitementCourant = TraitementCourant.TRAITEMENT_VENTILATIONS;
                }
            }
        } else if (TraitementCourant.TRAITEMENT_VENTILATIONS.equals(traitementCourant)) {
            // Si oui, il faut r�cup�rer le d�compte en question
            next = iteratorVentilations.hasNext();
            if (next) {
                decompteCourant = ventilations.get(iteratorVentilations.next());
            }
        } else {
            throw new FWIException("APDecompteGenerationProcess.hasNextDocument() : unconsistent state Founded ["
                    + traitementCourant
                    + "], expected [TRAITEMENT_REPARTITIONS, TRAITEMENT_VENTILATIONS]. Unexpected error...");
        }

        // Plus de d�compte � g�n�rer mais next toujours � true --> incoh�rence
        if ((decompteCourant == null) && next) {
            throw new FWIException("Incoh�rence dans la m�thode next() : il n'y � plus de d�compte � g�n�rer");
        }

        // Encore des d�comptes � g�n�rer mais next � false --> incoh�rence
        if ((decompteCourant != null) && !next) {
            throw new FWIException("Incoh�rence dans la m�thode next() : il y � toujours des d�comptes � g�n�rer");
        }

        if (next) {
            final ICTDocument catalogueTextes = chargerCatalogue(decompteCourant);
            if (catalogueTextes == null) {
                throw new FWIException("Impossible de retrouver le catalogue de textes pour la g�n�ration du d�comptes");
            }
            setDecompteCourant(decompteCourant);
            setCatalogueTextesCourant(catalogueTextes);
        }
        return next;
    }

    /**
     * charge le catalogue de texte pour le d�compte courant.
     * <p>
     * cette m�thode utilise la variable documentHelper qui est initialis�e dans {@link #beforeExecuteReport()
     * beforeExecuteReport()}.
     * </p>
     * <p>
     * Les catalogues ne sont charges qu'un fois, ils sont ensuite mis en cache.
     * </p>
     * 
     * @throws FWIException
     */
    private final ICTDocument chargerCatalogue(final APDecompte decompteCourant) throws FWIException {
        // charger le catalogue de texte pour le d�compte decompteCourant
        try {

            final String cleCatalogue = cleCatalogue(decompteCourant);

            ICTDocument document = documents.get(cleCatalogue);
            if (document == null) {
                documentHelper.setNom(decompteCourant.getTypeDeDecompte().getNomDocument());
                documentHelper.setCsDestinataire(decompteCourant.getIsPaiementEmployeur() ? ICTDocument.CS_EMPLOYEUR
                        : ICTDocument.CS_ASSURE);

                // on cherche la langue du destinataire
                if (decompteCourant.getIsPaiementEmployeur()) {
                    final IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(),
                            getTransaction(), decompteCourant.getIdAffilie(), "");

                    if (affilie == null) {
                        PRTiersWrapper tierWrapper = PRTiersHelper.getTiersParId(getSession(),
                                decompteCourant.getIdTiers());

                        if (tierWrapper == null) {
                            tierWrapper = PRTiersHelper.getAdministrationParId(getISession(),
                                    decompteCourant.getIdTiers());
                        }
                        if (tierWrapper == null) {
                            throw new Exception("Unable to found any 'TiersWrapper' with idTiers ["
                                    + decompteCourant.getIdTiers()
                                    + "]. Can not load catalogue de textes. Paiement type : employeur");
                        }

                        codeIsoLangue = getSession().getCode(tierWrapper.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                        codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
                    } else {
                        codeIsoLangue = getSession().getCode(affilie.getLangue());
                        codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
                    }
                } else {
                    PRTiersWrapper tierWrapper = PRTiersHelper
                            .getTiersParId(getSession(), decompteCourant.getIdTiers());

                    if (tierWrapper == null) {
                        tierWrapper = PRTiersHelper.getAdministrationParId(getISession(), decompteCourant.getIdTiers());
                    }

                    if (tierWrapper == null) {
                        throw new Exception("Unable to found any 'TiersWrapper' with idTiers ["
                                + decompteCourant.getIdTiers()
                                + "]. Can not load catalogue de textes. Paiement type : employ�");
                    }

                    codeIsoLangue = getSession().getCode(tierWrapper.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                    codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
                }

                if (JadeStringUtil.isEmpty(codeIsoLangue)) {
                    throw new Exception(
                            "Can not found the ISO code for language. Can not load APDecompte catalogue for tiers with id ["
                                    + decompteCourant.getIdTiers() + "]");
                }
                documentHelper.setCodeIsoLangue(codeIsoLangue);

                final ICTDocument[] candidats = documentHelper.load();
                if (candidats == null) {
                    throw new FWIException("impossible de charger le catalogue de texte");
                }

                if ((candidats != null) && (candidats.length > 0)) {
                    document = candidats[0];
                    documents.put(cleCatalogue, document);
                }
            } else {
                if (document.getCodeIsoLangue() != null) {
                    codeIsoLangue = PRUtil.getISOLangueTiers(document.getCodeIsoLangue());
                }
            }
            return document;
        } catch (final Exception e) {
            throw new FWIException(e.toString(), e);
        }
    }

    /**
     * Cr�e une cl� qui identifie de mani�re unique un catalogue.
     * 
     * @return
     * @throws Exception
     */
    private String cleCatalogue(final APDecompte decompteCourant) throws Exception {

        String codeIsoLangue = null;

        // on cherche la langue du destinataire
        if (decompteCourant.getIsPaiementEmployeur()) {
            final IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getTransaction(),
                    decompteCourant.getIdAffilie(), "");

            if (affilie == null) {
                // Cas des employeurs non affili�
                PRTiersWrapper tierWrapper = PRTiersHelper.getTiersParId(getSession(), decompteCourant.getIdTiers());

                if (tierWrapper == null) {
                    tierWrapper = PRTiersHelper.getAdministrationParId(getISession(), decompteCourant.getIdTiers());
                }

                codeIsoLangue = getSession().getCode(tierWrapper.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
            } else {
                codeIsoLangue = getSession().getCode(affilie.getLangue());
                codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
            }
        } else {
            PRTiersWrapper tierWrapper = PRTiersHelper.getTiersParId(getSession(), decompteCourant.getIdTiers());

            if (tierWrapper == null) {
                tierWrapper = PRTiersHelper.getAdministrationParId(getISession(), decompteCourant.getIdTiers());
            }
            if (tierWrapper == null) {
                System.err.println("APDecompte-00001) ERROR !!!! Ne doit jamais arriver ici !!!!!!");
                codeIsoLangue = "fr";
            } else {
                codeIsoLangue = getSession().getCode(tierWrapper.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
            }
        }
        return decompteCourant.getTypeDeDecompte().getNomDocument() + "_" + decompteCourant.getIsPaiementEmployeur()
                + "_" + codeIsoLangue;
    }

    /**
     * retrouve le type de prestation contenues dans le lot.
     * 
     * @return un code syst�me (PRTYPDEMAN)
     * @throws Exception
     */
    private String loadCsTypePrestation() throws Exception {
        final APLot lot = new APLot();
        lot.setIdLot(getIdLot());
        lot.setSession(getSession());
        lot.retrieve();
        if (lot.isNew()) {
            throw new Exception("Can not load the APLot with id [" + getIdLot() + "]. It's new...");
        }
        return lot.getTypeLot();
    }

    @Override
    public final GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public final JADate getDateComptable() {
        return dateComptable;
    }

    @Override
    public final void setDateComptable(final JADate date) {
        dateComptable = date;
    }

    @Override
    public final JADate getDateDocument() {
        return dateDocument;
    }

    @Override
    public final void setDateDocument(final JADate date) {
        dateDocument = date;
    }

    /**
     * @return the idLot
     */
    @Override
    public final String getIdLot() {
        return idLot;
    }

    /**
     * @param idLot
     *            the idLot to set
     */
    @Override
    public final void setIdLot(final String idLot) {
        this.idLot = idLot;
    }

    @Override
    public final void setIsSendToGED(final boolean sentToGed) {
        isSendToGED = sentToGed;
    }

    @Override
    public final boolean getIsSendToGED() {
        return isSendToGED;
    }

    /**
     * @return the typePrestationsLotCS
     */
    @Override
    public final String getCSTypePrestationsLot() {
        return csTypePrestationsLot;
    }

    /**
     * @param idAssuranceAvsParitaire
     *            the idAssuranceAvsParitaire to set
     */
    public final void setIdAssuranceAvsParitaire(final int idAssuranceAvsParitaire) {
        this.idAssuranceAvsParitaire = idAssuranceAvsParitaire;
    }

    /**
     * @param idAssuranceAvsPersonnelle
     *            the idAssuranceAvsPersonnelle to set
     */
    public final void setIdAssuranceAvsPersonnelle(final int idAssuranceAvsPersonnelle) {
        this.idAssuranceAvsPersonnelle = idAssuranceAvsPersonnelle;
    }

    /**
     * @param idAssuranceAcParitaire
     *            the idAssuranceAcParitaire to set
     */
    public final void setIdAssuranceAcParitaire(final int idAssuranceAcParitaire) {
        this.idAssuranceAcParitaire = idAssuranceAcParitaire;
    }

    /**
     * @param idAssuranceLfaParitaire
     *            the idAssuranceLfaParitaire to set
     */
    public final void setIdAssuranceLfaParitaire(final int idAssuranceLfaParitaire) {
        this.idAssuranceLfaParitaire = idAssuranceLfaParitaire;
    }

    /**
     * @param idAssuranceLfaPersonnelle
     *            the idAssuranceLfaPersonnelle to set
     */
    public final void setIdAssuranceLfaPersonnelle(final int idAssuranceLfaPersonnelle) {
        this.idAssuranceLfaPersonnelle = idAssuranceLfaPersonnelle;
    }

    /**
     * @return the idAssuranceAvsParitaire
     */
    @Override
    public final int getIdAssuranceAvsParitaire() {
        return idAssuranceAvsParitaire;
    }

    /**
     * @return the idAssuranceAvsPersonnelle
     */
    @Override
    public final int getIdAssuranceAvsPersonnelle() {
        return idAssuranceAvsPersonnelle;
    }

    /**
     * @return the idAssuranceAcParitaire
     */
    @Override
    public final int getIdAssuranceAcParitaire() {
        return idAssuranceAcParitaire;
    }

    /**
     * @return the idAssuranceLfaParitaire
     */
    @Override
    public final int getIdAssuranceLfaParitaire() {
        return idAssuranceLfaParitaire;
    }

    /**
     * @return the idAssuranceLfaPersonnelle
     */
    @Override
    public final int getIdAssuranceLfaPersonnelle() {
        return idAssuranceLfaPersonnelle;
    }

    /**
     * @return the isAfficherConfidentielSurDocument
     */
    @Override
    public final boolean getIsAfficherConfidentielSurDocument() {
        return isAfficherConfidentielSurDocument;
    }

    /**
     * @param isAfficherConfidentielSurDocument
     *            the isAfficherConfidentielSurDocument to set
     */
    public final void setIsAfficherConfidentielSurDocument(final boolean isAfficherConfidentielSurDocument) {
        this.isAfficherConfidentielSurDocument = isAfficherConfidentielSurDocument;
    }

    /**
     * @return the isAfficherNIPSurDocument
     */
    @Override
    public final boolean getIsAfficherNIPSurDocument() {
        return isAfficherNIPSurDocument;
    }

    /**
     * @param isAfficherNIPSurDocument
     *            the isAfficherNIPSurDocument to set
     */
    public final void setIsAfficherNIPSurDocument(final boolean isAfficherNIPSurDocument) {
        this.isAfficherNIPSurDocument = isAfficherNIPSurDocument;
    }

    /**
     * @return the isDecompteRecapitulatif
     */
    @Override
    public final boolean getIsDecompteRecapitulatif() {
        return isDecompteRecapitulatif;
    }

    /**
     * @param isDecompteRecapitulatif
     *            the isDecompteRecapitulatif to set
     */
    public final void setIsDecompteRecapitulatif(final boolean isDecompteRecapitulatif) {
        this.isDecompteRecapitulatif = isDecompteRecapitulatif;
    }

    /**
     * @return the isNumeroAffiliePourGEDForceAZeroSiVide
     */
    @Override
    public final boolean getIsNumeroAffiliePourGEDForceAZeroSiVide() {
        return isNumeroAffiliePourGEDForceAZeroSiVide;
    }

    /**
     * @param isNumeroAffiliePourGEDForceAZeroSiVide
     *            the isNumeroAffiliePourGEDForceAZeroSiVide to set
     */
    public final void setIsNumeroAffiliePourGEDForceAZeroSiVide(final boolean isNumeroAffiliePourGEDForceAZeroSiVide) {
        this.isNumeroAffiliePourGEDForceAZeroSiVide = isNumeroAffiliePourGEDForceAZeroSiVide;
    }

    /**
     * @return the isBlankIndexGedNssAZero
     */
    @Override
    public final boolean getIsBlankIndexGedNssAZero() {
        return isBlankIndexGedNssAZero;
    }

    /**
     * @param isBlankIndexGedNssAZero
     *            the isBlankIndexGedNssAZero to set
     */
    public final void setIsBlankIndexGedNssAZero(final boolean isBlankIndexGedNssAZero) {
        this.isBlankIndexGedNssAZero = isBlankIndexGedNssAZero;
    }

    @Override
    public boolean isTraitementDesVentilations() {
        return TraitementCourant.TRAITEMENT_VENTILATIONS.equals(traitementCourant);
    }

    /**
     * @return the isregroupementPrestationStandardEtACM_NE
     */
    public final boolean getIsIsregroupementPrestationStandardEtACM_NE() {
        return isregroupementPrestationStandardEtACM_NE;
    }

    /**
     * @param isregroupementPrestationStandardEtACM_NE
     *            the isregroupementPrestationStandardEtACM_NE to set
     */
    public final void setIsregroupementPrestationStandardEtACM_NE(final boolean isregroupementPrestationStandardEtACM_NE) {
        this.isregroupementPrestationStandardEtACM_NE = isregroupementPrestationStandardEtACM_NE;
    }

    /**
     * @return the typeDePrestationAcm
     */
    public final APPropertyTypeDePrestationAcmValues getTypeDePrestationAcm() {
        return typeDePrestationAcm;
    }

    /**
     * @param typeDePrestationAcm
     *            the typeDePrestationAcm to set
     */
    public final void setTypeDePrestationAcm(final APPropertyTypeDePrestationAcmValues typeDePrestationAcm) {
        this.typeDePrestationAcm = typeDePrestationAcm;
    }

    @Override
    public int getIdAssuranceFneParitaire() {
        return idAssuranceFneParitaire;
    }

    /**
     * @param idAssuranceFneParitaire the idAssuranceFneParitaire to set
     */
    public final void setIdAssuranceFneParitaire(int idAssuranceFneParitaire) {
        this.idAssuranceFneParitaire = idAssuranceFneParitaire;
    }

    @Override
    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }
}
