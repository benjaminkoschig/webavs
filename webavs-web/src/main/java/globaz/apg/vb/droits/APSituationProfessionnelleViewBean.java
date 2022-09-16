/*
 * Cr�� le 23 mai 05
 */
package globaz.apg.vb.droits;

import ch.globaz.common.exceptions.Exceptions;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.common.sql.converters.LocalDateConverter;
import ch.globaz.queryexec.bridge.jade.SCM;
import globaz.apg.api.assurance.IAPAssurance;
import globaz.apg.api.process.IAPGenererCompensationProcess;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitProcheAidant;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.menu.MenuPrestation;
import globaz.apg.properties.APProperties;
import globaz.apg.properties.APPropertyTypeDePrestationAcmValues;
import globaz.apg.services.APRechercherAssuranceFromDroitCotisationService;
import globaz.apg.services.APRechercherTypeAcmService;
import globaz.apg.servlet.IAPActions;
import globaz.apg.util.TypePrestation;
import globaz.apg.utils.APGUtils;
import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.*;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.api.IAFAssurance;
import globaz.naos.api.IAFCotisation;
import globaz.naos.api.IAFSuiviCaisseAffiliation;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.lienAffiliation.AFLienAffiliation;
import globaz.naos.db.lienAffiliation.AFLienAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.api.IPRSituationProfessionnelle;
import globaz.prestation.api.PRTypeDemande;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.employeurs.PRDepartement;
import globaz.prestation.db.employeurs.PRDepartementManager;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRCodeSystem;
import globaz.prestation.tools.PRImagesConstants;
import globaz.prestation.tools.PRSession;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.pyxis.adresse.datasource.TIAbstractAdressePaiementDataSource;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementManager;
import globaz.pyxis.db.tiers.TIReferencePaiement;
import globaz.pyxis.db.tiers.TIReferencePaiementManager;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.sql.PreparedStatement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;

/**
 * <H1>Description</H1>
 *
 * @author dvh
 */
public class APSituationProfessionnelleViewBean extends APSituationProfessionnelle implements FWViewBeanInterface {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_SEL_ADRESSE = new Object[]{
            new String[]{"setIdTiersPaiementEmployeurDepuisAdresse", "idTiers"},
            new String[]{"idDomainePaiementEmployeur", "idApplication"}};

    private static final Object[] METHODES_SEL_REFERENCE_QR = new Object[]{
            new String[]{"setIdReferenceQRDepuisReferenceQR", "idReferenceQR"}};

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    private static final String DEST_SUIVANT_APG = IAPActions.ACTION_ENFANT_APG + ".chercher";
    private static final String DEST_CALCULER_PRESTATIONS = IAPActions.ACTION_PRESTATIONS
            + ".actionCalculerToutesLesPrestations";

    private static final String ERREUR_DEPARTEMENT_INTROUVABLE = "DEPARTEMENT_INTROUVABLE";

    private static final String ERREUR_EMPLOYEUR_INTROUVABLE = "EMPLOYEUR_INTROUVABLE";

    private static final Set<String> EXCEPTION_AUTRE_SALAIRE = new HashSet<String>();

    private static final String IS_DROIT_MATERNITE_CANTONALE = "isDroitMaterniteCantonale";

    private static final String LABEL_TITRE_APG = "JSP_TITRE_SAISIE_APG_2";
    private static final String LABEL_TITRE_MAT = "JSP_TITRE_SAISIE_MAT_4";
    private static final String LABEL_TITRE_PAN = "JSP_TITRE_SAISIE_PAN_4";
    private static final String LABEL_TITRE_PAT = "JSP_TITRE_SAISIE_PAT_3";
    private static final String LABEL_TITRE_PRAI = "JSP_TITRE_SAISIE_PAI_3";

    private static final Vector<String[]> MENU_DEROULANT_BLANK = new Vector<String[]>();

    private static final String METHODE_NAME_APRECHERCHERTYPEACMSERVICE_RECHERCHER = "rechercher";

    private static final Object[] METHODES_SEL_EMPLOYEUR = new Object[]{
            new String[]{"idTiersEmployeurDepuisPyxis", "idTiers"}, new String[]{"nomEmployeur", "nom"},
            new String[]{"numAffilieEmployeur", "numAffilieActuel"},
            new String[]{"dateDebutAffiliation", "debutActivite"},
            new String[]{"dateFinAffiliation", "finActivite"}
            /* ,new String[] { "genreAffiliation", "genreAffilie" } */
    };


    static {
        APSituationProfessionnelleViewBean.MENU_DEROULANT_BLANK.add(new String[]{"", ""});
    }

    static {
        APSituationProfessionnelleViewBean.EXCEPTION_AUTRE_SALAIRE
                .add(IPRSituationProfessionnelle.CS_PERIODICITE_HEURE);
        APSituationProfessionnelleViewBean.EXCEPTION_AUTRE_SALAIRE.add(IPRSituationProfessionnelle.CS_PERIODICITE_MOIS);
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Vector affiliationsEmployeur;
    // date de debut de l'affiliation de l'employeur
    private String dateDebutAffiliation = null;
    // date de fin de l'affiliation de l'employeur
    private String dateFinAffiliation = null;

    private String crNomPrenom = "crNomPrenom";

    // champs relatifs a l'economie de requetes pour l'affichage des infos dans
    // l'ecran rc.
    private transient APDroitDTO droitDTO = null;

    private String genreAffiliation = null;

    private String idDroitLAPGBackup = "";

    // Sett� � true lors de la recherche d'un affili� directement par sont no
    // (action : rechercerAffilie)
    private boolean isActionRechercherAffilie = false;
    // champ relatifs a l'employeur
    private String nomEmployeur = "";

    private String nomEmployeurAvecVirgule = "";

    private String numAffilieEmployeur = "";
    private String numAffilieEtTypeAffiliationEmployeur = "";
    // champs supplementaires utilises pour la gestion du choix de l'employeur
    // avec Pyxis
    private boolean retourDepuisPyxis;

    private boolean retourDepuisAdresse;

    // boolean supplementaire utilise pour la gestion de reprise de l'employeur
    // par numero d'affilie
    private boolean retourDesTiers = false;

    private transient TypePrestation typePrestation = null;

    private boolean isIbanValide;
    private String lineAdressePaiementEmployeur;
    private Boolean isRetourRechercheAffilie = false;
    @Getter
    @Setter
    private PRTypeDemande typeDemande;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Retourne <code>true</code> si on est dans le domaine des APG. Par opposition � la maternit�
     *
     * @return
     */
    public boolean isAPG() {
        return IPRDemande.CS_TYPE_APG.equals(getTypePrestation().toCodeSysteme());
    }

    public boolean isMaternite() {
        return IPRDemande.CS_TYPE_MATERNITE.equals(getTypePrestation().toCodeSysteme());
    }

    public boolean isPandemie() {
        return IPRDemande.CS_TYPE_PANDEMIE.equals(getTypePrestation().toCodeSysteme());
    }

    public boolean isPaternite() {
        return IPRDemande.CS_TYPE_PATERNITE.equals(getTypePrestation().toCodeSysteme());
    }

    public boolean isProcheAidant() {
        return IPRDemande.CS_TYPE_PROCHE_AIDANT.equals(getTypePrestation().toCodeSysteme());
    }


    public boolean isModuleActifForPorterEnCompte() {
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

    public Object[] getMethodesSelectionAdressePaiement() {
        return APSituationProfessionnelleViewBean.METHODES_SEL_ADRESSE;
    }
    public Object[] getMethodesSelectionReferencePaiement() {
        return APSituationProfessionnelleViewBean.METHODES_SEL_REFERENCE_QR;
    }

    public String getDomaineAdressePaiementEmployeur() {
        return getDomaineLibelle(idDomainePaiementEmployeur);
    }

    public String getDomaineLibelle(String idCode) {
        if (!JadeStringUtil.isBlankOrZero(idCode)) {
            return getSession().getLabel("DOMAINE_ADRESSE") + " : "
                    + JadeCodesSystemsUtil.getCodeLibelle(getSession(), idCode) + "\n";
        }

        return "";
    }

    private void initAdressePaiementEmployeur() throws Exception {

        TIAdressePaiementData detailTiers = null;
        String adresseLine = "";

        if (!JadeStringUtil.isBlankOrZero(getIdTiersEmployeur())) {

            String idDomainPaiementEmployeur = getIdDomainePaiementEmployeur();
            String idTiersPaiementEmployeur = getIdTiersPaiementEmployeur();

            // si l'id tiers paiement employeur est d�j� renseign�, nous le prenons avec son id domaine stock�
            if (!JadeStringUtil.isBlankOrZero(idTiersPaiementEmployeur)
                    && !JadeStringUtil.isBlankOrZero(idDomainPaiementEmployeur)) {

                detailTiers = PRTiersHelper.getAdressePaiementData(getSession(),
                                                                   getSession()
                                                                           .getCurrentThreadTransaction(), idTiersPaiementEmployeur, idDomainPaiementEmployeur,
                                                                   getIdAffilieEmployeur(), JACalendar.todayJJsMMsAAAA());
            } else {
                // si un employeur d�fini dans la situation

                idTiersPaiementEmployeur = getIdTiersEmployeur();
                idDomainPaiementEmployeur = APGUtils.getCSDomaineFromTypeDemande(getTypePrestation().toCodeSysteme());

                // nous recherchons en cascade du domaine APG ou MATERNITE
                detailTiers = PRTiersHelper.getAdressePaiementData(getSession(),
                                                                   getSession()
                                                                           .getCurrentThreadTransaction(), idTiersPaiementEmployeur, idDomainPaiementEmployeur,
                                                                   getIdAffilieEmployeur(), JACalendar.todayJJsMMsAAAA());
            }

            setAdressePaiementEmployeur(detailTiers);

            final TIAdressePaiementDataSource dataSource = new TIAdressePaiementDataSource();
            dataSource.load(detailTiers);
            initIsIbanValide(dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_COMPTE));

            // formatter le no de ccp ou le no bancaire
            if (JadeStringUtil.isEmpty(detailTiers.getCcp())) {
                adresseLine += getDomaineAdressePaiementEmployeur();
                adresseLine += new TIAdressePaiementBanqueFormater().format(dataSource);
            } else {
                adresseLine += getDomaineAdressePaiementEmployeur();
                adresseLine += new TIAdressePaiementCppFormater().format(dataSource);
            }
        }

        lineAdressePaiementEmployeur = adresseLine;
    }

    public String getAdressePaiementEmployeur() throws Exception {
        // Evite de rechercher plusieurs fois la m�me adresse de paiement lorsque la page est load�e
        if (JadeStringUtil.isBlankOrZero(lineAdressePaiementEmployeur)
                || (isRetourDepuisAdresse() || isRetourDepuisPyxis() || isRetourDesTiers())) {
            initAdressePaiementEmployeur();
        }

        return lineAdressePaiementEmployeur;
    }

    private void initIsIbanValide(String iban) throws Exception {
        if ("CH0000000000000000000".equals(iban)) {
            isIbanValide = false;
            // Pas de mise � jour lorsque l'on clique sur des situations professionnelles existantes
            if (isRetourDepuisAdresse() || isRetourDepuisPyxis() || isRetourDesTiers()
                    || getIsRetourRechercheAffilie()) {
                setIsPorteEnCompte(true);
            }
        } else {
            isIbanValide = true;
        }
    }

    public boolean getIsIbanValide() throws Exception {
        // Permet d'initialiser la variable isIbanValide lorsque l'adresse de paiement instanci�e
        initAdressePaiementEmployeur();
        return isIbanValide;
    }

    public void setAdressePaiementEmployeur(TIAdressePaiementData detailTiers) {
        if (detailTiers != null && !detailTiers.isNew()) {
            setIdTiersPaiementEmployeur(detailTiers.getIdTiers());
            setIdDomainePaiementEmployeur(detailTiers.getIdApplication());
        } else {
            setIdTiersPaiementEmployeur(null);
            setIdDomainePaiementEmployeur(null);
        }
    }

    public String getPersonnelDeclarePar(final String contextPath, final String date) throws Exception {
        // Si le module n'est pas actif pour le porter en compte ou que l'on a pas d'id affili�.
        if (!isModuleActifForPorterEnCompte() || JadeStringUtil.isBlankOrZero(getIdAffilieEmployeur())) {
            return "";
        }

        // Nous recherchons les liens d'affiliation de type personnel d�clarer par avec une date de validit� encore
        // active
        final AFLienAffiliationManager manager = new AFLienAffiliationManager();
        manager.setSession(getSession());
        manager.setForTypeLien(CodeSystem.TYPE_LIEN_PERSONNEL_DECLARE);
        manager.setForAffiliationId(getIdAffilieEmployeur());
        manager.setForDate(date);
        manager.find(BManager.SIZE_NOLIMIT);

        String multiple = "";
        AFLienAffiliation lienAffiliation = null;
        if (manager.getSize() > 1) {
            lienAffiliation = (AFLienAffiliation) manager.get(0);
            multiple = " *";
        } else if (manager.getSize() == 1) {
            lienAffiliation = (AFLienAffiliation) manager.get(0);
        }

        if (lienAffiliation == null) {
            return "";
        }

        final String numeroAffiliation = lienAffiliation.getLienAffiliation().getAffilieNumero();
        final String idAffiliation = lienAffiliation.getLienAffiliation().getAffiliationId();

        final StringBuilder builder = new StringBuilder();
        builder.append(getSession().getLabel("PORTERENCOMPTE_PERSONNEL_DECLARER_PAR"));
        builder.append("<a class='external_link'");
        builder.append("target='_parent'");
        builder.append("href='" + contextPath + "\\naos?");
        builder.append("userAction=naos.affiliation.affiliation.afficher");
        builder.append("&selectedId=" + idAffiliation);
        builder.append("'>");
        builder.append(numeroAffiliation).append(multiple);
        builder.append("</a>");

        return builder.toString();
    }

    /**
     * getter pour l'attribut affiliation employeur
     *
     * @return la valeur courante de l'attribut affiliation employeur
     */
    public Vector getAffiliationsEmployeur() {
        if (affiliationsEmployeur == null) {
            return APSituationProfessionnelleViewBean.MENU_DEROULANT_BLANK;
        }

        return affiliationsEmployeur;
    }

    /**
     * @see globaz.apg.db.droits.APSituationProfessionnelle#getAutreRemuneration()
     */
    @Override
    public String getAutreRemuneration() {
        return JANumberFormatter.fmt(super.getAutreRemuneration(), true, true, true, 2);
    }

    /**
     * @see globaz.apg.db.droits.APSituationProfessionnelle#getAutreSalaire()
     */
    @Override
    public String getAutreSalaire() {
        return JANumberFormatter.fmt(autreSalaire, true, true, true, 2);
    }

    public Vector<String[]> getCsAssociationData() {

        final Map<String, String> csAssociationDataMap = PRCodeSystem.getLibellesPourGroupeInMap(
                getSession(),
                IAPAssurance.CS_GROUPE_TYPE_ASSURANCE);

        final Vector<String[]> csAssociationDataVec = new Vector<String[]>();

        csAssociationDataVec.add(new String[]{"", ""});
        for (final Entry<String, String> entry : csAssociationDataMap.entrySet()) {
            csAssociationDataVec.add(new String[]{entry.getValue(), entry.getKey()});
        }

        return csAssociationDataVec;
    }

    /**
     * @return
     */
    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    /**
     * getter pour l'attribut date debut droit
     *
     * @return la valeur courante de l'attribut date debut droit
     */
    public String getDateDebutDroit() {
        if (droitDTO != null) {
            return droitDTO.getDateDebutDroit();
        } else {
            return "";
        }
    }

    public String getAnneeFromDateDebutDroit() {
        if (droitDTO != null) {
            Date globazDate = JadeDateUtil.getGlobazDate(droitDTO.getDateDebutDroit());

            Calendar cal = Calendar.getInstance();
            cal.setTime(globazDate);

            return String.valueOf(cal.get(Calendar.YEAR));
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    /**
     * getter pour l'attribut departements data
     *
     * @return la valeur courante de l'attribut departements data
     */
    public Vector<String[]> getDepartementsData() {

        if (JadeStringUtil.isIntegerEmpty(getIdAffilieEmployeur())) {
            return APSituationProfessionnelleViewBean.MENU_DEROULANT_BLANK;
        } else {
            try {
                final PRDepartementManager mgr = new PRDepartementManager();

                mgr.setSession(getSession());
                mgr.setForIdAffilie(loadEmployeur().getIdAffilie());
                mgr.find();

                if (!mgr.isEmpty()) {
                    final Vector<String[]> retValue = new Vector<String[]>(
                            APSituationProfessionnelleViewBean.MENU_DEROULANT_BLANK);

                    for (int idDepartement = 0; idDepartement < mgr.size(); ++idDepartement) {
                        final PRDepartement departement = (PRDepartement) mgr.get(idDepartement);

                        retValue.add(new String[]{departement.getIdParticularite(), departement.getDepartement()});
                    }

                    return retValue;
                } else {
                    return APSituationProfessionnelleViewBean.MENU_DEROULANT_BLANK;
                }
            } catch (final Exception e) {
                getSession().addError(
                        getSession().getLabel(APSituationProfessionnelleViewBean.ERREUR_DEPARTEMENT_INTROUVABLE));

                return APSituationProfessionnelleViewBean.MENU_DEROULANT_BLANK;
            }
        }
    }

    /**
     * getter pour l'attribut dest ecran courant
     *
     * @return la valeur courante de l'attribut dest ecran courant
     */
    public String getDestEcranCourant() {
        if (TypePrestation.TYPE_MATERNITE.equals(typePrestation)) {
            return "";
        } else {
            return "";
        }
    }

    /**
     * @return Le nom de l'association donnant droit a des prestations ACM NE
     */
    public String getNomAssociation() {
        return getSession().getCodeLibelle(super.getCsAssuranceAssociation());
    }

    /**
     * getter pour l'attribut dest ecran suivant
     *
     * @return la valeur courante de l'attribut dest ecran suivant
     */
    public String getDestEcranSuivant() {
        if (Arrays.asList(TypePrestation.TYPE_MATERNITE, TypePrestation.TYPE_PANDEMIE,
                          TypePrestation.TYPE_PATERNITE, TypePrestation.TYPE_PROCHE_AIDANT)
                  .contains(typePrestation)) {
            return APSituationProfessionnelleViewBean.DEST_CALCULER_PRESTATIONS + "&selectedId=" + droitDTO.getIdDroit()
                    + "&genreService=" + droitDTO.getGenreService();
        } else {
            return APSituationProfessionnelleViewBean.DEST_SUIVANT_APG;
        }
    }

    public String getAdressePaiementRequerant() throws Exception {

        String adresseLine = "";
        String TIERS_CS_DOMAINE = APGUtils.getCSDomaineFromTypeDemande(getTypePrestation().toCodeSysteme());
        final TIAdressePaiementData detailTiers = PRTiersHelper.getAdressePaiementData(getSession(),
                                                                                       getSession()
                                                                                               .getCurrentThreadTransaction(), getIdTier(), TIERS_CS_DOMAINE,
                                                                                       null, JACalendar.todayJJsMMsAAAA());

        final TIAdressePaiementDataSource dataSource = new TIAdressePaiementDataSource();
        dataSource.load(detailTiers);

        // formatter le no de ccp ou le no bancaire
        if (JadeStringUtil.isEmpty(detailTiers.getCcp())) {
            adresseLine += getDomaineLibelle(detailTiers.getIdApplication());
            adresseLine += new TIAdressePaiementBanqueFormater().format(dataSource);
        } else {
            adresseLine += getDomaineLibelle(detailTiers.getIdApplication());
            adresseLine += new TIAdressePaiementCppFormater().format(dataSource);
        }

        return adresseLine;
    }

    /**
     * M�thode qui retourne le d�tail du requ�rant format� pour les d�tails
     *
     * @return le d�tail du requ�rant format�
     *
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        final PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVSDroit());

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession().getCode(getSession().getSystemCode(
                    "CIPAYORI",
                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(getSession().getSystemCode(
                        "CIPAYORI",
                        tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            return PRNSSUtil.formatDetailRequerantDetail(getNoAVSDroit(), getNomPrenomDroit(),
                                                         tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                                                         getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    public String getIdTier() throws Exception {
        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVSDroit());
        if (tiers != null) {
            return tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut droit DTO
     *
     * @return la valeur courante de l'attribut droit DTO
     */
    public APDroitDTO getDroitDTO() {
        return droitDTO;
    }

    public boolean isJourIsole() {
        if (droitDTO != null) {
            return APGUtils.isTypeAllocationJourIsole(droitDTO.getGenreService());
        }
        return false;
    }

    /**
     * getter pour l'attribut exception autre salaire data
     *
     * @return la valeur courante de l'attribut exception autre salaire data
     */
    public Set<String> getExceptionAutreSalaireData() {
        return APSituationProfessionnelleViewBean.EXCEPTION_AUTRE_SALAIRE;
    }

    public String getGenreAffiliation() {
        return genreAffiliation;
    }

    /**
     * (non-Javadoc)
     *
     * @return la valeur courante de l'attribut heures semaine
     *
     * @see globaz.apg.db.droits.APSituationProfessionnelle#getHeuresSemaine()
     */
    @Override
    public String getHeuresSemaine() {
        return JANumberFormatter.fmt(heuresSemaine, false, true, true, 2);
    }

    /**
     * getter pour l'attribut id affilie employeur
     *
     * @return la valeur courante de l'attribut id affilie employeur
     */
    public String getIdAffilieEmployeur() {
        try {
            return loadEmployeur().getIdAffilie();
        } catch (final Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);

            return "";
        }
    }

    /**
     * getter pour l'attribut id droit LAPGBackup
     *
     * @return la valeur courante de l'attribut id droit LAPGBackup
     */
    public String getIdDroitLAPGBackup() {
        return idDroitLAPGBackup;
    }

    /**
     * champ en relation avec le departement.
     *
     * @return la valeur courante de l'attribut id particularite employeur
     */
    public String getIdParticulariteEmployeur() {
        try {
            return loadEmployeur().getIdParticularite();
        } catch (final Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);

            return "";
        }
    }

    /**
     * getter pour l'attribut id tiers employeur
     *
     * @return la valeur courante de l'attribut id tiers employeur
     */
    public String getIdTiersEmployeur() {
        try {
            return loadEmployeur().getIdTiers();
        } catch (final Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);

            return "";
        }
    }

    /**
     * getter pour l'attribut methodes selecteur employeur
     *
     * @return la valeur courante de l'attribut methodes selecteur employeur
     */
    public Object[] getMethodesSelecteurEmployeur() {
        return APSituationProfessionnelleViewBean.METHODES_SEL_EMPLOYEUR;
    }

    /**
     * @see globaz.apg.db.droits.APSituationProfessionnelle#getMontantVerse()
     */
    @Override
    public String getMontantVerse() {
        return JANumberFormatter.fmt(super.getMontantVerse(), true, true, true, 2);
    }

    public String getNameClassForAPRechercherTypeAcmService() throws Exception {
        return APRechercherTypeAcmService.class.getName();
    }

    public String getNameMethodForAPRechercherTypeAcmService() throws Exception {

        // getMethod(APSituationProfessionnelleViewBean.METHODE_NAME_APRECHERCHERTYPEACMSERVICE_RECHERCHER, new
        // Class[0]).getName();

        // (new
        // APRechercherTypeAcmService).getClass().getDeclaredMethod(METHODE_NAME_APRECHERCHERTYPEACMSERVICE_RECHERCHER,
        // new Class[I])
        return APSituationProfessionnelleViewBean.METHODE_NAME_APRECHERCHERTYPEACMSERVICE_RECHERCHER;

    }

    /**
     * getter pour l'attribut no AVSDroit
     *
     * @return la valeur courante de l'attribut no AVSDroit
     */
    public String getNoAVSDroit() {
        if (droitDTO != null) {
            if (droitDTO.getNoAVS().length() > 12) {
                return droitDTO.getNoAVS();
            } else {
                return "756." + droitDTO.getNoAVS();
            }
        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut nom employeur
     *
     * @return la valeur courante de l'attribut nom employeur
     */
    public String getNomEmployeur() {
        if (JadeStringUtil.isEmpty(nomEmployeur)) {
            try {
                nomEmployeur = loadEmployeur().loadNom();
            } catch (final Exception e) {
                _addError(
                        getSession().getCurrentThreadTransaction(),
                        getSession().getLabel(APSituationProfessionnelleViewBean.ERREUR_EMPLOYEUR_INTROUVABLE));
            }
        }

        return nomEmployeur;
    }

    public String getNomEmployeurAvecVirgule() {
        loadNomEmployeurAvecVirgule(getIdTiersEmployeur(), getIdAffilieEmployeur());
        return nomEmployeurAvecVirgule;
    }

    /**
     * getter pour l'attribut nom prenom droit
     *
     * @return la valeur courante de l'attribut nom prenom droit
     */
    public String getNomPrenomDroit() {
        if (droitDTO != null) {
            return droitDTO.getNomPrenom();
        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut num affilie employeur
     *
     * @return la valeur courante de l'attribut num affilie employeur
     */
    public String getNumAffilieEmployeur() {
        if (JadeStringUtil.isEmpty(numAffilieEmployeur)) {
            try {
                final IPRAffilie affilie = loadEmployeur().loadAffilie();

                if (affilie != null) {
                    numAffilieEmployeur = affilie.getNumAffilie();
                }
            } catch (final Exception e) {
                _addError(
                        getSession().getCurrentThreadTransaction(),
                        getSession().getLabel(APSituationProfessionnelleViewBean.ERREUR_EMPLOYEUR_INTROUVABLE));
            }
        }

        return numAffilieEmployeur;
    }

    public String getNumAffilieEtTypeAffiliationEmployeur() {
        if (JadeStringUtil.isEmpty(numAffilieEtTypeAffiliationEmployeur)) {
            try {
                final IPRAffilie affilie = loadEmployeur().loadAffilie();
                String tAff = "";
                if (affilie != null) {

                    numAffilieEtTypeAffiliationEmployeur = affilie.getNumAffilie();
                    try {
                        tAff = getSession().getCodeLibelle(affilie.getTypeAffiliation());
                    } catch (final Exception e) {
                        tAff = "";
                    }
                    numAffilieEtTypeAffiliationEmployeur += " " + tAff;

                }
            } catch (final Exception e) {
                _addError(
                        getSession().getCurrentThreadTransaction(),
                        getSession().getLabel(APSituationProfessionnelleViewBean.ERREUR_EMPLOYEUR_INTROUVABLE));
            }
        }

        return numAffilieEtTypeAffiliationEmployeur;
    }

    /**
     * @see globaz.apg.db.droits.APSituationProfessionnelle#getRevenuIndependant()
     */
    @Override
    public String getRevenuIndependant() {
        return JANumberFormatter.fmt(revenuIndependant, true, true, true, 2);
    }

    /**
     * @see globaz.apg.db.droits.APSituationProfessionnelle#getSalaireHoraire()
     */
    @Override
    public String getSalaireHoraire() {
        return JANumberFormatter.fmt(salaireHoraire, true, true, true, 2);
    }

    /**
     * @see globaz.apg.db.droits.APSituationProfessionnelle#getSalaireMensuel()
     */
    @Override
    public String getSalaireMensuel() {
        return JANumberFormatter.fmt(salaireMensuel, true, true, true, 2);
    }

    /**
     * @see globaz.apg.db.droits.APSituationProfessionnelle#getSalaireNature()
     */
    @Override
    public String getSalaireNature() {
        return JANumberFormatter.fmt(salaireNature, true, true, true, 2);
    }

    public String getCrNomPrenom() {
        return "crNomPrenom";
    }

    public void setCrNomPrenom(String crNomPrenom) {
        this.crNomPrenom = crNomPrenom;
    }

    /**
     * getter pour l'attribut titre page
     *
     * @return la valeur courante de l'attribut titre page
     */
    public String getTitrePage() {
        if (TypePrestation.TYPE_MATERNITE.equals(typePrestation)) {
            return getSession().getLabel(APSituationProfessionnelleViewBean.LABEL_TITRE_MAT);
        } else if (TypePrestation.TYPE_APG.equals(typePrestation)) {
            return getSession().getLabel(APSituationProfessionnelleViewBean.LABEL_TITRE_APG);
        } else if (TypePrestation.TYPE_PANDEMIE.equals(typePrestation)) {
            return getSession().getLabel(APSituationProfessionnelleViewBean.LABEL_TITRE_PAN);
        } else if (TypePrestation.TYPE_PATERNITE.equals(typePrestation)) {
            return getSession().getLabel(APSituationProfessionnelleViewBean.LABEL_TITRE_PAT);
        } else if (TypePrestation.TYPE_PROCHE_AIDANT.equals(typePrestation)) {
            return getSession().getLabel(APSituationProfessionnelleViewBean.LABEL_TITRE_PRAI);
        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut type prestation
     *
     * @return la valeur courante de l'attribut type prestation
     */
    public TypePrestation getTypePrestation() {
        return typePrestation;
    }

    /**
     * getter pour l'attribut versement assure
     *
     * @return la valeur courante de l'attribut versement assure
     */
    public String getVersementAssure() {
        if (isVersementEmployeur.booleanValue()) {
            return PRImagesConstants.IMAGE_ERREUR;
        } else {
            return PRImagesConstants.IMAGE_OK;
        }
    }

    /**
     * vrais si l'affilie cotise a une assurance LAMat
     *
     * @return
     */
    public boolean hasCotisationAssuranceLAMat() {

        if (!JadeStringUtil.isEmpty(getIdAffilieEmployeur())) {

            try {

                final List<IAFAssurance> assurancesList = APRechercherAssuranceFromDroitCotisationService
                        .rechercher(getIdDroit(), getIdAffilieEmployeur(), getSession());

                for (final IAFAssurance assuranceCourante : assurancesList) {
                    if (IAFAssurance.TYPE_ASS_MATERNITE.equals(assuranceCourante.getTypeAssurance())) {
                        return true;
                    }
                }

            } catch (final Exception e) {
                setMessage(e.getMessage());
                setMsgType(FWViewBeanInterface.ERROR);
            }
        }
        return false;
    }

    /**
     * return true si l'affilie cotise une assurance paritaire
     *
     * @return
     */
    public boolean hasCotisationsAssuranceParitaire() {

        if (!JadeStringUtil.isEmpty(getIdAffilieEmployeur())) {

            try {

                // on cherche l'affiliation du tiers
                final List<String> affiliations = new ArrayList<String>();
                final IAFAffiliation affiliation = (IAFAffiliation) getSession().getAPIFor(IAFAffiliation.class);
                affiliation.setISession(PRSession.connectSession(getSession(), AFApplication.DEFAULT_APPLICATION_NAOS));
                final Hashtable param = new Hashtable();
                param.put(IAFAffiliation.FIND_FOR_AFFILIATIONID, getIdAffilieEmployeur());
                final IAFAffiliation[] afRet = affiliation.findAffiliation(param);

                // le tiers est un affilie
                if (afRet != null) {
                    for (int i = 0; i < afRet.length; i++) {
                        affiliations.add(afRet[i].getAffiliationId());
                    }
                }

                final Iterator iter = affiliations.iterator();

                while (iter.hasNext()) {
                    // on cherche toutes les cotisation de l'affiliation
                    final IAFCotisation cotisation = (IAFCotisation) getSession().getAPIFor(IAFCotisation.class);
                    cotisation.setISession(
                            PRSession.connectSession(getSession(), AFApplication.DEFAULT_APPLICATION_NAOS));
                    final Hashtable paramsAffiliation = new Hashtable() {

                        /**
                         *
                         */
                        private static final long serialVersionUID = 1L;
                    };
                    paramsAffiliation.put(IAFCotisation.FIND_FOR_AFFILIATION_ID, iter.next());
                    final IAFCotisation[] cotisations = cotisation.findCotisations(paramsAffiliation);

                    // pour verifier que la cotisation soit valide au moment du
                    // droit
                    final APDroitLAPG droit = new APDroitLAPG();
                    droit.setSession(getSession());
                    droit.setIdDroit(getIdDroit());
                    droit.retrieve();

                    // pour toutes les cotisation
                    for (int i = 0; i < cotisations.length; i++) {

                        if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), cotisations[i].getDateDebut(),
                                                                      droit.getDateDebutDroit())
                                && (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(),
                                                                                cotisations[i].getDateFin(), droit.getDateDebutDroit())
                                || JadeStringUtil.isEmpty(cotisations[i].getDateFin()))) {

                            // on cherche l'assurance
                            final IAFAssurance assurance = (IAFAssurance) getSession().getAPIFor(IAFAssurance.class);
                            assurance.setISession(
                                    PRSession.connectSession(getSession(), AFApplication.DEFAULT_APPLICATION_NAOS));
                            assurance.setAssuranceId(cotisations[i].getAssuranceId());
                            assurance.retrieve(getSession().getCurrentThreadTransaction());

                            // si l'assurance est de type pariaire
                            if (IAFAssurance.PARITAIRE.equals(assurance.getAssuranceGenre())) {
                                return true;
                            }
                        }
                    }
                }
            } catch (final Exception e) {
                setMessage(e.getMessage());
                setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        return false;
    }

    /**
     * return true si l'affilie cotise a notre caisse
     *
     * @return
     */
    public boolean hasCotisationsInOurCaisseForPeriodeDroit() {

        if (!JadeStringUtil.isEmpty(getIdAffilieEmployeur())) {
            try {
                final IAFSuiviCaisseAffiliation suiveCaisseAffiliation = (IAFSuiviCaisseAffiliation) getSession()
                        .getAPIFor(IAFSuiviCaisseAffiliation.class);
                suiveCaisseAffiliation
                        .setISession(PRSession.connectSession(getSession(), AFApplication.DEFAULT_APPLICATION_NAOS));
                final Hashtable<String, String> criteres = new Hashtable<String, String>();
                criteres.put(IAFSuiviCaisseAffiliation.FIND_FOR_AFFILIATION_ID, getIdAffilieEmployeur());
                criteres.put(
                        IAFSuiviCaisseAffiliation.FIND_FOR_GENRE_CAISSE,
                        IAFSuiviCaisseAffiliation.GENRE_CAISSE_AVS);
                final IAFSuiviCaisseAffiliation[] result = suiveCaisseAffiliation.findSuiviCaisse(criteres);

                if ((result != null) && (result.length > 0)) {

                    // pour verifier que le suivi soit valide au moment du droit
                    final APDroitLAPG droit = new APDroitLAPG();
                    droit.setSession(getSession());
                    droit.setIdDroit(getIdDroit());
                    droit.retrieve();

                    // pour tous les suivis
                    for (int i = 0; result.length > i; i++) {

                        // Si date de fin du suivi existe
                        if (!JadeStringUtil.isBlankOrZero(result[i].getDateFin())) {

                            // Si date de fin du suivi est plus grande ou �gale
                            // au d�but du droit
                            if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), result[i].getDateFin(),
                                                                            droit.getDateDebutDroit())) {
                                return false;
                            }

                            // Si pas de date de fin du suivi
                        } else {

                            // Si date de d�but du suivi est plus petite ou
                            // �gale au d�but du droit
                            if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), result[i].getDateDebut(),
                                                                          droit.getDateDebutDroit())) {
                                return false;
                            }
                        }
                    }
                }

            } catch (final Exception e) {
                setMessage(e.getMessage());
                setMsgType(FWViewBeanInterface.ERROR);
            }
        }
        return true;
    }

    /**
     * @return
     */
    public boolean isActionRechercherAffilie() {
        return isActionRechercherAffilie;
    }

    /**
     * Si la caisse � activ� les prestations compl�mentaires ACM NE via la propri�t� :
     * APProperties.TYPE_DE_PRESTATION_ACM
     *
     * @return Si la caisse � activ� les prestations compl�mentaires ACM NE
     *
     * @throws PropertiesException
     * @see APProperties
     */
    public boolean isPrestationAcmNeEnable() throws PropertiesException {
        final String propertyValue = getPropertyValue();
        return APPropertyTypeDePrestationAcmValues.ACM_NE.getPropertyValue().equals(propertyValue);
    }

    /**
     * Si la caisse � activ� les prestations compl�mentaires ACM ALFA via la propri�t� :
     * APProperties.TYPE_DE_PRESTATION_ACM
     *
     * @return Si la caisse � activ� les prestations compl�mentaires ACM ALFA
     *
     * @throws PropertiesException
     * @see APProperties
     */
    public Boolean isPrestationAcmAlfaEnable() throws PropertiesException {
        final String propertyValue = getPropertyValue();
        if (isPandemie() || isPaternite() || isProcheAidant()) {
            return false;
        } else {
            return APPropertyTypeDePrestationAcmValues.ACM_ALFA.getPropertyValue().equals(propertyValue);
        }
    }

    public Boolean isPrestationAcm2AlfaEnable() throws PropertiesException {
        if (isPandemie() || isPaternite() || isProcheAidant()) {
            return false;
        } else {
            return APProperties.PRESTATION_ACM_2_ACTIF.getBooleanValue();
        }
    }

    /**
     * @return
     *
     * @throws PropertiesException
     */
    private String getPropertyValue() throws PropertiesException {
        // R�cup�ration de la valeur de la property. Exception si pas d�clar�e
        final String propertyValue = APProperties.TYPE_DE_PRESTATION_ACM.getValue();
        // validation en fonction de son domaine de valeur. Exception si valeur ne fait pas partie du domaine
        CommonPropertiesUtils.validatePropertyValue(APProperties.TYPE_DE_PRESTATION_ACM, propertyValue,
                                                    APPropertyTypeDePrestationAcmValues.propertyValues());
        return propertyValue;
    }

    /**
     * getter pour l'attribut departement enabled
     *
     * @return la valeur courante de l'attribut departement enabled
     */
    public boolean isDepartementEnabled() {
        boolean retValue = false;

        try {
            final APApplication app = (APApplication) GlobazSystem
                    .getApplication(APApplication.DEFAULT_APPLICATION_APG);

            retValue = Boolean.valueOf(app.getProperty(APApplication.PROPERTY_DEPARTEMENT_ENABLED)).booleanValue();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return retValue;
    }

    public Boolean isDroitMaterniteCantonaleFromProperties() {
        return isPropertyTrue(APSituationProfessionnelleViewBean.IS_DROIT_MATERNITE_CANTONALE);
    }

    /**
     * getter pour l'attribut modifiable
     *
     * @return la valeur courante de l'attribut modifiable
     */
    public boolean isModifiable() {
        if (droitDTO != null) {
            return droitDTO.isModifiable();
        } else {
            return false;
        }
    }

    private boolean isPropertyTrue(final String propertyName) {

        Boolean propertyValeur = null;

        try {
            propertyValeur = Boolean.TRUE.toString()
                                         .equals(globaz.prestation.application.PRAbstractApplication
                                                         .getApplication(globaz.apg.application.APApplication.DEFAULT_APPLICATION_APG)
                                                         .getProperty(propertyName));

        } catch (final Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }

        if (propertyValeur == null) {
            propertyValeur = Boolean.FALSE;
        }

        return propertyValeur.booleanValue();
    }

    public boolean isProtectedAffiliation() throws Exception {

        final AFAffiliation affiliation = new AFAffiliation();
        affiliation.setSession(getSession());
        affiliation.setId(getIdAffilieEmployeur());
        affiliation.retrieve();

        if (affiliation.isNew()) {
            return false;
        } else {
            if (affiliation.hasRightAccesSecurity()) {
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean isProtectedCI() throws Exception {

        final APDroitLAPG droit = new APDroitLAPG();
        droit.setSession(getSession());
        droit.setIdDroit(getIdDroit());
        droit.retrieve();

        final PRDemande demande = new PRDemande();
        demande.setSession(getSession());
        demande.setIdDemande(droit.getIdDemande());
        demande.retrieve();

        final PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), demande.getIdTiers());

        if (tiers != null) {
            final CICompteIndividuelManager ciManager = new CICompteIndividuelManager();
            ciManager.setSession(getSession());
            ciManager.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciManager.setForNumeroAvs(NSUtil.unFormatAVS(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
            ciManager.wantCallMethodBeforeFind(false);
            ciManager.find();

            if (ciManager.isEmpty()) {
                return false;
            } else {
                final CICompteIndividuel ci = (CICompteIndividuel) ciManager.getFirstEntity();
                if (ci.hasUserShowRight(getSession().getCurrentThreadTransaction())) {
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    /**
     * getter pour l'attribut retour depuis pyxis
     *
     * @return la valeur courante de l'attribut retour depuis pyxis
     */
    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    /**
     * getter pour l'attribut retour depuis pyxis
     *
     * @return la valeur courante de l'attribut retour depuis pyxis
     */
    public boolean isRetourDepuisAdresse() {
        return retourDepuisAdresse;
    }

    /**
     * @return
     */
    public boolean isRetourDesTiers() {
        return retourDesTiers;
    }

    /**
     * getter pour l'attribut type APG
     *
     * @return la valeur courante de l'attribut type APG
     */
    public boolean isTypeAPG() {
        return TypePrestation.TYPE_APG.equals(typePrestation);
    }

    public boolean isQrIban(){
        TIAdressePaiement adressePaiement = loadAdressePaiement();
        if(adressePaiement != null){
            return adressePaiement.isQRIban();
        }
        return false;
    }

    public String getNumeroCompte(){
        TIAdressePaiement adressePaiement = loadAdressePaiement();
        if(adressePaiement != null){
            return adressePaiement.getNumCompteBancaire();
        }
        return StringUtils.EMPTY;
    }

    private TIAdressePaiement loadAdressePaiement(){
        if(!StringUtils.isEmpty(getIdTiersPaiementEmployeur())) {
            try {
                TIAdressePaiementManager mgr = new TIAdressePaiementManager();
                mgr.setSession(getSession());
                mgr.setForIdAdressePaiement(getIdTiersPaiementEmployeur());
                mgr.find(BManager.SIZE_NOLIMIT);
                if (mgr.size() > 0) {
                    return (TIAdressePaiement) mgr.get(0);
                }

            } catch (Exception e) {
                JadeLogger.error(e, "La reference QR de la situation professionnelle n'a pas pu �tre rechercher !");
            }
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     */
    public void resetAffilie() {
        nomEmployeur = "";
    }

    /**
     * @return
     */
    public String retrieveDateDebutAffiliation() {

        if (!JadeStringUtil.isEmpty(getIdAffilieEmployeur())) {
            try {
                final IAFAffiliation affiliation = (IAFAffiliation) getSession().getAPIFor(IAFAffiliation.class);
                affiliation.setISession(PRSession.connectSession(getSession(), AFApplication.DEFAULT_APPLICATION_NAOS));
                final Hashtable criteres = new Hashtable();
                criteres.put(IAFAffiliation.FIND_FOR_NOAFFILIE, getIdAffilieEmployeur());
                final IAFAffiliation[] result = affiliation.findAffiliation(criteres);

                if ((result != null) && (result.length > 0)) {
                    return result[0].getDateDebut();
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * @return
     */
    public String retrieveDateFinAffiliation() {

        if (!JadeStringUtil.isEmpty(getIdAffilieEmployeur())) {
            try {
                final IAFAffiliation affiliation = (IAFAffiliation) getSession().getAPIFor(IAFAffiliation.class);
                affiliation.setISession(PRSession.connectSession(getSession(), AFApplication.DEFAULT_APPLICATION_NAOS));
                final Hashtable<String, String> criteres = new Hashtable<String, String>();
                criteres.put(IAFAffiliation.FIND_FOR_NOAFFILIE, getIdAffilieEmployeur());
                final IAFAffiliation[] result = affiliation.findAffiliation(criteres);

                if ((result != null) && (result.length > 0)) {
                    return result[0].getDateFin();
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return "";

    }

    /**
     * getter pour l'attribut id affilie employeur
     *
     * @return la valeur courante de l'attribut id affilie employeur
     */
    public boolean retrieveIsEmployeurNonActif() {
        try {
            final IAFAffiliation affiliation = (IAFAffiliation) getSession().getAPIFor(IAFAffiliation.class);
            affiliation.setISession(PRSession.connectSession(getSession(), AFApplication.DEFAULT_APPLICATION_NAOS));
            final Hashtable<String, String> criteres = new Hashtable<String, String>();
            criteres.put(IAFAffiliation.FIND_FOR_NOAFFILIE, getNumAffilieEmployeur());
            // pas d'employeur
            if (JadeStringUtil.isEmpty(getNumAffilieEmployeur())) {
                return true;
            }

            final IAFAffiliation[] result = affiliation.findAffiliation(criteres);

            if ((result == null) || (result.length == 0)) {
                return true;
            } else {
                // on cherche une affiliation de type non-actif
                for (int i = 0; i < result.length; i++) {
                    if (result[i].getTypeAffiliation().equals(IAFAffiliation.TYPE_AFFILI_NON_ACTIF)) {
                        return true;
                    }
                }
                return false;
            }
        } catch (final Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);

            return true;
        }
    }

    /**
     * @param b
     */
    public void setActionRechercherAffilie(final boolean b) {
        isActionRechercherAffilie = b;
    }

    /**
     * setter pour l'attribut affiliation employeur
     * <p>
     * Note: cette methode insere un blanc dans le vecteur en premiere position.
     * </p>
     *
     * @param affiliationsEmployeur une nouvelle valeur pour cet attribut
     */
    public void setAffiliationsEmployeur(final Vector<String[]> affiliationsEmployeur) {
        if (affiliationsEmployeur != null) {
            this.affiliationsEmployeur = affiliationsEmployeur;
            affiliationsEmployeur.addAll(0, APSituationProfessionnelleViewBean.MENU_DEROULANT_BLANK);
        } else {
            this.affiliationsEmployeur = APSituationProfessionnelleViewBean.MENU_DEROULANT_BLANK;
        }
    }

    /**
     * @see globaz.apg.db.droits.APSituationProfessionnelle#setAutreRemuneration(java.lang.String)
     */
    @Override
    public void setAutreRemuneration(final String string) {
        super.setAutreRemuneration(JANumberFormatter.deQuote(string));
    }

    /**
     * @see globaz.apg.db.droits.APSituationProfessionnelle#setAutreSalaire(java.lang.String)
     */
    @Override
    public void setAutreSalaire(final String string) {
        autreSalaire = JANumberFormatter.deQuote(string);
    }

    /**
     * @param string
     */
    public void setDateDebutAffiliation(final String string) {
        dateDebutAffiliation = string;
    }

    /**
     * @param string
     */
    public void setDateFinAffiliation(final String string) {
        dateFinAffiliation = string;
    }

    /**
     * setter pour l'attribut droit DTO
     *
     * @param droitDTO une nouvelle valeur pour cet attribut
     */
    public void setDroitDTO(final APDroitDTO droitDTO) {
        this.droitDTO = droitDTO;

        if (droitDTO != null) {
            idDroit = droitDTO.getIdDroit();
        }
    }

    public void setGenreAffiliation(final String string) {
        genreAffiliation = string;
    }

    /**
     * setter pour l'attribut id affilie employeur
     *
     * @param idAffilieEmployeur une nouvelle valeur pour cet attribut
     */
    public void setIdAffilieEmployeur(final String idAffilieEmployeur) {
        try {
            loadEmployeur().setIdAffilie(idAffilieEmployeur);
        } catch (final Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }
    }

    /**
     * setter pour l'attribut id droit LAPGBackup
     *
     * @param idDroitLAPGBackup une nouvelle valeur pour cet attribut
     */
    public void setIdDroitLAPGBackup(final String idDroitLAPGBackup) {
        this.idDroitLAPGBackup = idDroitLAPGBackup;
    }

    /**
     * setter pour l'attribut id particulier
     *
     * @param string une nouvelle valeur pour cet attribut
     */
    public void setIdParticularite(final String string) {
        try {
            loadEmployeur().setIdParticularite(string);
        } catch (final Exception e) {
            getSession()
                    .addError(getSession().getLabel(APSituationProfessionnelleViewBean.ERREUR_EMPLOYEUR_INTROUVABLE));
        }
    }

    /**
     * setter pour l'attribut id particularite employeur
     *
     * @param idParticulariteEmployeur une nouvelle valeur pour cet attribut
     */
    public void setIdParticulariteEmployeur(final String idParticulariteEmployeur) {
        try {
            loadEmployeur().setIdParticularite(idParticulariteEmployeur);
        } catch (final Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }
    }

    /**
     * setter pour l'attribut id tiers employeur
     *
     * @param idTiersEmployeur une nouvelle valeur pour cet attribut
     */
    public void setIdTiersEmployeur(final String idTiersEmployeur) {
        try {
            loadEmployeur().setIdTiers(idTiersEmployeur);
        } catch (final Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }
    }

    /**
     * setter pour l'attribut id tiers employeur depuis pyxis
     * <p>
     * Note: cette methode renseigne le champ retourDepuisPyxis a vrai ce qui sera interprete dans l'action comme le
     * fait que l'on revient depuis pyxis, il est necessaire de reinitialiser ce champ a la fin.
     * </p>
     *
     * @param idTiersEmployeur une nouvelle valeur pour cet attribut
     */
    public void setIdTiersEmployeurDepuisPyxis(final String idTiersEmployeur) {
        setIdTiersEmployeur(idTiersEmployeur);
        setIdAffilieEmployeur("");
        setIdTiersPaiementEmployeur("");
        setIdDomainePaiementEmployeur("");
        numAffilieEmployeur = "";
        numAffilieEtTypeAffiliationEmployeur = "";
        genreAffiliation = "";
        retourDepuisPyxis = true;
    }

    /**
     * setter pour l'attribut id tiers employeur depuis pyxis
     * <p>
     * Note: cette methode renseigne le champ retourDepuisPyxis a vrai ce qui sera interprete dans l'action comme le
     * fait que l'on revient depuis pyxis, il est necessaire de reinitialiser ce champ a la fin.
     * </p>
     *
     * @param idTiersEmployeur une nouvelle valeur pour cet attribut
     */
    public void setIdTiersPaiementEmployeurDepuisAdresse(final String idTiersPaiement) {
        setIdTiersPaiementEmployeur(idTiersPaiement);
        retourDepuisAdresse = true;
    }

    public void setIdReferenceQRDepuisReferenceQR(final String idReferenceQR){
        setIdReferenceQREmployeur(idReferenceQR);
        retourDepuisAdresse = true;
    }

    /**
     * @see globaz.apg.db.droits.APSituationProfessionnelle#setMontantVerse(java.lang.String)
     */
    @Override
    public void setMontantVerse(final String string) {
        super.setMontantVerse(JANumberFormatter.deQuote(string));
    }

    /**
     * setter pour l'attribut nom employeur
     *
     * @param string une nouvelle valeur pour cet attribut
     */
    public void setNomEmployeur(final String string) {
        nomEmployeur = string;
    }

    public void loadNomEmployeurAvecVirgule(String idTiers, String idAffilie) {
        String result = "";

        try {
            if (JadeStringUtil.isIntegerEmpty(idTiers)) {
                nomEmployeurAvecVirgule = result;
                return;
            }

            if (JadeStringUtil.isIntegerEmpty(idAffilie)) {

                PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), idTiers);
                if (tiers != null) {
                    result = tiers.getProperty(PRTiersWrapper.PROPERTY_NOM);
                    result += "," + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                    result = result.trim();

                } else {
                    tiers = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
                    if (tiers != null) {
                        String temp = tiers.getProperty(PRTiersWrapper.PROPERTY_NOM);
                        if (!JadeStringUtil.isBlank(temp)) {
                            result = temp.trim();
                        }
                        temp = tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                        if (!JadeStringUtil.isBlank(temp)) {
                            result += "," + temp.trim();
                        }
                        temp = tiers.getProperty(PRTiersWrapper.PROPERTY_DESIGNATION_3);
                        if (!JadeStringUtil.isBlank(temp)) {
                            result += "," + temp.trim();
                        }
                    }
                }

            } else {
                IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(),
                                                                                  getSession().getCurrentThreadTransaction(), idAffilie, idTiers);

                if (affilie != null) {
                    result = affilie.getNom();

                    // Dans le cas d'un independant, il y a aussi un prenom
                    if (!JadeStringUtil.isEmpty(result) && !JadeStringUtil.isEmpty(affilie.getNoAVS())) {

                        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), idTiers);

                        if (tiers != null && !JadeStringUtil.isEmpty(tiers.getProperty(PRTiersWrapper.PROPERTY_NOM))) {
                            result += "," + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                        }
                    }
                }
            }

            // Supression des caract�res sp�ciaux dans les noms des employeurs, car
            // si existant le fichier batch g�n�r� va s'interrompre, car non support� par la commande DOS : ECHO
            result = cleanNomEmployeur(result);

        } catch (final Exception e) {
            _addError(
                    getSession().getCurrentThreadTransaction(),
                    getSession().getLabel(APSituationProfessionnelleViewBean.ERREUR_EMPLOYEUR_INTROUVABLE));
        }

        nomEmployeurAvecVirgule = result;
    }

    /**
     * �limine les caract�re sp�ciaux pouvant cr�er des probl�mes</br>
     * Supression des caract�res sp�ciaux dans les noms des employeurs, car
     * si existant
     * le fichier batch g�n�r� va s'interrompre, car non support� par la
     * commande DOS : ECHO
     *
     * @param result
     *
     * @return
     */
    private String cleanNomEmployeur(String result) {
        result = result.replace('&', ' ');
        result = result.replace('<', ' ');
        result = result.replace('>', ' ');
        result = result.replace('\'', ' ');
        result = result.replace('"', ' ');
        return result;
    }

    public void setNomEmployeurAvecVirgule(final String string) {
        nomEmployeurAvecVirgule = string;
    }

    /**
     * setter pour l'attribut num affilie employeur
     *
     * @param numAffilieEmployeur une nouvelle valeur pour cet attribut
     */
    public void setNumAffilieEmployeur(final String numAffilieEmployeur) {
        this.numAffilieEmployeur = numAffilieEmployeur;
    }

    /**
     * setter pour l'attribut retour depuis pyxis
     *
     * @param retourDepuisPyxis une nouvelle valeur pour cet attribut
     */
    public void setRetourDepuisPyxis(final boolean retourDepuisPyxis) {
        this.retourDepuisPyxis = retourDepuisPyxis;
    }

    /**
     * setter pour l'attribut retour depuis pyxis
     *
     * @param retourDepuisAdresse une nouvelle valeur pour cet attribut
     */
    public void setRetourDepuisAdresse(final boolean retourDepuisAdresse) {
        this.retourDepuisAdresse = retourDepuisAdresse;
    }

    /**
     * @param b
     */
    public void setRetourDesTiers(final boolean b) {
        retourDesTiers = b;
    }

    public void setNbJourIndemnise(final String nbJourIndemnise) {
        this.nbJourIndemnise = Optional.of(nbJourIndemnise)
                                       .filter(s -> !s.trim().isEmpty())
                                       .map(Integer::parseInt)
                                       .orElse(null);
    }

    /**
     * @see globaz.apg.db.droits.APSituationProfessionnelle#setRevenuIndependant(java.lang.String)
     */
    @Override
    public void setRevenuIndependant(final String string) {
        revenuIndependant = JANumberFormatter.deQuote(string);
    }

    /**
     * @see globaz.apg.db.droits.APSituationProfessionnelle#setSalaireHoraire(java.lang.String)
     */
    @Override
    public void setSalaireHoraire(final String string) {
        salaireHoraire = JANumberFormatter.deQuote(string);
    }

    /**
     * @see globaz.apg.db.droits.APSituationProfessionnelle#setSalaireMensuel(java.lang.String)
     */
    @Override
    public void setSalaireMensuel(final String string) {
        salaireMensuel = JANumberFormatter.deQuote(string);
    }

    /**
     * @see globaz.apg.db.droits.APSituationProfessionnelle#setSalaireNature(java.lang.String)
     */
    @Override
    public void setSalaireNature(final String string) {
        salaireNature = JANumberFormatter.deQuote(string);
    }

    /**
     * setter pour l'attribut type prestation
     *
     * @param prestation une nouvelle valeur pour cet attribut
     */
    public void setTypePrestation(final TypePrestation prestation) {
        typePrestation = prestation;
    }

    public Boolean getIsRetourRechercheAffilie() {
        return isRetourRechercheAffilie;
    }

    public void setIsRetourRechercheAffilie(Boolean isRetourRechercheAffilie) {
        this.isRetourRechercheAffilie = isRetourRechercheAffilie;
    }

    public MenuPrestation getMenuPrestation() {
        return MenuPrestation.of(this.getTypeDemande());
    }

    public Integer resolveNbJourIndemnise() {
        if (this.nbJourIndemnise != null && this.nbJourIndemnise > 0) {
            return this.nbJourIndemnise;
        }
        return this.calculerNbjourDuDroit() - this.calculerNbjourIndemiserPourCeDroit();
    }

    public boolean displayJourIndemnise() {
        return this.typeDemande.isProcheAidant();
    }

    public int calculerNbjourDuDroit() {
        if (this.typeDemande.isProcheAidant()) {
            APDroitProcheAidant apDroitProcheAidant = new APDroitProcheAidant();
            apDroitProcheAidant.setSession(this.getSession());
            apDroitProcheAidant.setIdDroit(this.idDroit);
            return apDroitProcheAidant.calculerNbjourTotalDuDroit();
        }
        return 0;
    }


    public int calculerNbjourIndemiserPourCeDroit() {
        if (this.typeDemande.isProcheAidant()) {
            APDroitProcheAidant apDroitProcheAidant = new APDroitProcheAidant();
            apDroitProcheAidant.setSession(this.getSession());
            apDroitProcheAidant.setIdDroit(this.idDroit);
            return apDroitProcheAidant.calculerNbJourIndemnise();
        }
        return 0;
    }

    public int calculNbJourDejaIndemnise() {
        if (this.typeDemande.isProcheAidant()) {
            return calculerNbjourIndemiserPourCeDroit() - getNbJourIndemnise();
        }
        return 0;
    }

    public String translate(String label, Object... arguments) {
        return MessageFormat.format(FWMessageFormat.prepareQuotes(getSession().getLabel(label), false), arguments);
    }

    public void updateJourEmployeurIdentique(BTransaction transaction, boolean joursIdentiques)throws Exception {
        if(typeDemande.isProcheAidant()) {
            APSituationProfessionnelleManager managerSitu = new APSituationProfessionnelleManager();
            managerSitu.setSession(this.getSession());
            managerSitu.setForIdDroit(idDroit);
            managerSitu.setNotForIdSituationProfessionnelle(idSituationProf);
            managerSitu.find(transaction, BManager.SIZE_NOLIMIT);
            managerSitu.<APSituationProfessionnelle>getContainerAsList()
                    .forEach(situationProfessionnelle -> Exceptions.checkedToUnChecked(() -> {
                                situationProfessionnelle.wantCallValidate(false);
                                situationProfessionnelle.setSession(this.getSession());
                                situationProfessionnelle.setIsJoursIdentiques(joursIdentiques);
                                situationProfessionnelle.update(transaction);
                            })
                    );
        }
    }

    public boolean hasJourEmployeurIdentique()throws Exception {
        if(typeDemande.isProcheAidant()) {
            APSituationProfessionnelleManager managerSitu = new APSituationProfessionnelleManager();
            managerSitu.setSession(this.getSession());
            managerSitu.setForIdDroit(idDroit);
            managerSitu.find(BManager.SIZE_NOLIMIT);
            List<APSituationProfessionnelle> sitProf = managerSitu.<APSituationProfessionnelle>getContainerAsList();
            return sitProf.isEmpty()
                    || sitProf.stream().anyMatch(s -> s.getIsJoursIdentiques());
        }
        return true;
    }

    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        super._afterAdd(transaction);
        updateJourEmployeurIdentique(transaction, this.isJoursIdentiques);
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        super._afterUpdate(transaction);
        updateJourEmployeurIdentique(transaction, this.isJoursIdentiques);
    }



}
