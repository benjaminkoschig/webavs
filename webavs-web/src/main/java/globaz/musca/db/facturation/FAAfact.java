package globaz.musca.db.facturation;

import globaz.commons.nss.NSUtil;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.api.IFAPrintDoc;
import globaz.musca.application.FAApplication;
import globaz.musca.external.ServicesFacturation;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.osiris.utils.CAUtil;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueContribuable;
import globaz.pyxis.db.tiers.TIRole;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

public class FAAfact extends BEntity implements Serializable, IFAPrintDoc, Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int AK_ENTETEFACTURE = 1;

    public static final int AK_INTERETSMORATOIRES = 1;
    private static final String CAISSE_PROF = "509028";
    public final static String CS_AFACT_COMPENSATION = "904002";
    public final static String CS_AFACT_COMPENSATION_INTERNE = "904004";
    // Type Afact
    public final static String CS_AFACT_STANDART = "904001";
    public final static String CS_AFACT_TABLEAU = "904003";
    public static final String FIELD_AFFICHTAUX = "AFFICHTAUX";
    public static final String FIELD_ANNEECOTISATION = "ANNEECOTISATION";
    public static final String FIELD_AQUITTANCER = "AQUITTANCER";
    public static final String FIELD_CANTON = "CANTON";
    public static final String FIELD_DATEDEBUTPERIODE = "DATEDEBUTPERIODE";
    public static final String FIELD_DATEFINPERIODE = "DATEFINPERIODE";
    public static final String FIELD_DATEVALEUR = "DATEVALEUR";
    public static final String FIELD_EHIDOR = "EHIDOR";
    public static final String FIELD_EHLLID = "EHLLID";
    public static final String FIELD_EHLLIF = "EHLLIF";
    public static final String FIELD_EHLLII = "EHLLII";
    public static final String FIELD_EHNORD = "EHNORD";
    public static final String FIELD_HTLDE1 = "HTLDE1";
    public static final String FIELD_HTLDE2 = "HTLDE2";
    public static final String FIELD_IDAFACT = "IDAFACT";
    public static final String FIELD_IDASSURANCE = "IDASSURANCE";
    public static final String FIELD_IDENTETEFACTURE = "IDENTETEFACTURE";
    public static final String FIELD_IDEXTDEBCOM = "IDEXTDEBCOM";
    public static final String FIELD_IDEXTFACCOM = "IDEXTFACCOM";
    public static final String FIELD_IDMODFAC = "IDMODFAC";
    public static final String FIELD_IDPASSAGE = "IDPASSAGE";
    public static final String FIELD_IDREMARQUE = "IDREMARQUE";
    public static final String FIELD_IDROLDEBCOM = "IDROLDEBCOM";
    public static final String FIELD_IDRUBRIQUE = "IDRUBRIQUE";
    public static final String FIELD_IDSECTION = "IDSECTION";
    public static final String FIELD_IDTIEDEBCOM = "IDTIEDEBCOM";
    public static final String FIELD_IDTYPEAFACT = "IDTYPEAFACT";
    public static final String FIELD_IDTYPFACCOM = "IDTYPFACCOM";
    public static final String FIELD_LIBELLE = "LIBELLE";
    public static final String FIELD_MASSEDEJAFACTUREE = "MASSEDEJAFACTUREE";
    public static final String FIELD_MASSEFACTURE = "MASSEFACTURE";
    public static final String FIELD_MASSEINITIALE = "MASSEINITIALE";
    public static final String FIELD_MONTANTDEJAFACTURE = "MONTANTDEJAFACTURE";
    public static final String FIELD_MONTANTFACTURE = "MONTANTFACTURE";
    public static final String FIELD_MONTANTINITIAL = "MONTANTINITIAL";
    public static final String FIELD_NONCOMPTABILISABLE = "NONCOMPTABILISABLE";
    public static final String FIELD_NONIMPRIMABLE = "NONIMPRIMABLE";
    public static final String FIELD_NUMCAISSE = "NUMCAISSE";

    public static final String FIELD_REFERENCEEXTERNE = "REFERENCEEXTERNE";
    public static final String FIELD_TAUXDEJAFACTURE = "TAUXDEJAFACTURE";
    public static final String FIELD_TAUXFACTURE = "TAUXFACTURE";
    public static final String FIELD_TAUXINITIAL = "TAUXINITIAL";

    public static final String FIELD_TYPECALCULIM = "TYPECALCULIM";
    public static final String FIELD_USER = "USER";
    public static final int LONGUEUR_MAX_LIBELLE = 38;

    public static final String TABLE_FAAFACP = "FAAFACP";

    public final static String TABLE_FIELDS = FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_IDAFACT + ", "
            + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_IDENTETEFACTURE + ", " + FAAfact.TABLE_FAAFACP + "."
            + FAAfact.FIELD_IDPASSAGE + ", " + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_IDMODFAC + ", "
            + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_IDREMARQUE + ", " + FAAfact.TABLE_FAAFACP + "."
            + FAAfact.FIELD_IDTYPEAFACT + ", " + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_IDRUBRIQUE + ", "
            + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_ANNEECOTISATION + ", " + FAAfact.TABLE_FAAFACP + "."
            + FAAfact.FIELD_LIBELLE + ", " + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_DATEDEBUTPERIODE + ", "
            + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_DATEFINPERIODE + ", " + FAAfact.TABLE_FAAFACP + "."
            + FAAfact.FIELD_MONTANTINITIAL + ", " + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_MONTANTDEJAFACTURE
            + ", " + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_MONTANTFACTURE + ", " + FAAfact.TABLE_FAAFACP + "."
            + FAAfact.FIELD_MASSEINITIALE + ", " + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_MASSEDEJAFACTUREE + ", "
            + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_MASSEFACTURE + ", " + FAAfact.TABLE_FAAFACP + "."
            + FAAfact.FIELD_TAUXINITIAL + ", " + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_TAUXDEJAFACTURE + ", "
            + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_TAUXFACTURE + ", " + FAAfact.TABLE_FAAFACP + "."
            + FAAfact.FIELD_REFERENCEEXTERNE + ", " + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_USER + ", "
            + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_NONIMPRIMABLE + ", " + FAAfact.TABLE_FAAFACP + "."
            + FAAfact.FIELD_NONCOMPTABILISABLE + ", " + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_AQUITTANCER + ", "
            + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_IDROLDEBCOM + ", " + FAAfact.TABLE_FAAFACP + "."
            + FAAfact.FIELD_IDEXTDEBCOM + ", " + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_IDTIEDEBCOM + ", "
            + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_IDTYPFACCOM + ", " + FAAfact.TABLE_FAAFACP + "."
            + FAAfact.FIELD_IDEXTFACCOM + ", FAREMAP.TEXTE, " + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_NUMCAISSE
            + ", " + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_DATEVALEUR + ", " + FAAfact.TABLE_FAAFACP + ".PSPY, "
            + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_AFFICHTAUX + ", " + FAAfact.TABLE_FAAFACP + "."
            + FAAfact.FIELD_IDSECTION + ", " + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_CANTON + ", "
            + FAAfact.TABLE_FAAFACP + "." + FAAfact.FIELD_IDASSURANCE;
    private Boolean affichtaux = new Boolean(true);
    private String anneeCotisation = new String();
    private Boolean aQuittancer = new Boolean(false);
    private boolean bNewIdExterneRubrique = false;
    private String canton = new String();
    private Boolean checkIdExterneFactureCompensation = new Boolean(true);
    private String codeISOLangue;
    private boolean controleDeuxFrancs = true; // par d�faut contr�ler +/- 2Francs
    private boolean cotiMasseAZero = false;
    private String dateValeur = new String();
    private String debutPeriode = new String();
    private FAEnteteFactureViewBean enteteFacture = null;
    private String finPeriode = new String();
    private Boolean forceDelete = new Boolean(false);
    private String idAfact = new String();
    private String idAssurance = new String();
    private String idEnteteFacture = new String();
    private String idExterneDebiteurCompensation = new String();
    private String idExterneFactureCompensation = new String();
    private String idExterneRole = new String();
    private String idExterneRubrique = new String();
    private String idModuleFacturation = new String();
    private String idOrdreRegroupement = new String();
    private String idPassage = new String();
    private String idRemarque = new String();
    private String idRole = new String();
    private String idRoleDebiteurCompensation = new String();
    private String idRubrique = new String();
    private String idSection = new String();
    private String idTiersDebiteurCompensation = new String();
    private String idTypeAfact = new String();
    private String idTypeFactureCompensation = new String();
    private String libelle = new String();
    private String libelleOrdreDe = new String();
    private String libelleOrdreFr = new String();
    private String libelleOrdreIt = new String();
    private String libelleRubrique = new String();
    private String masseDejaFacturee = new String();
    private String masseFacture = new String();
    private String masseInitiale = new String();
    private String montantDejaFacture = new String();
    private String montantFacture = new String();
    private String montantInitial = new String();
    private boolean newFacturation = false;
    private boolean noCheckPlausiRubriqueMasse = false;

    private Boolean nonComptabilisable = new Boolean(false);

    private Boolean nonImprimable = new Boolean(false);
    private String numCaisse = new String();
    private String numCaisseOrdre = new String();
    private String ordreRegroupement = new String();
    private String referenceExterne = new String();
    private String remarque = new String();
    private APIRubrique rubrique = null;
    private String saveMontantFacture = new String();
    private String tauxDejaFacture = new String();
    private String tauxFacture = new String();
    private String tauxInitial = new String();
    private ITITiers tiers = null;
    private String tiersDesignation1 = new String();
    private String tiersDesignation2 = new String();
    private String typeCalculInteretMoratoire = "0";
    private String typeModule = new String();
    private String user = new String();

    @Getter
    @Setter
    private String typeCalcul = "";

    /**
     * Commentaire relatif au constructeur FAAfact
     */
    public FAAfact() {
        super();
    }

    /*
     * Traitement apr�s suppression
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws java.lang.Exception {
        // Mise � jour du total dans l'ent�te
        FAEnteteFacture entete = new FAEnteteFacture();
        FAAfact afact = new FAAfact();
        afact.setIdEnteteFacture(getIdEnteteFacture());
        afact.setMontantFacture(getMontantFacture());
        afact.setNonComptabilisable(isNonComptabilisable());
        afact.setAQuittancer(isAQuittancer());
        entete.addAfact(transaction, afact);
    }

    /*
     * Traitement apr�s suppression
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws java.lang.Exception {
        // Suppression des remarques
        if ((!globaz.jade.client.util.JadeStringUtil.isBlank(idRemarque)) && (!idRemarque.equalsIgnoreCase("0"))) {
            FARemarque rema = new FARemarque();
            rema.setSession(transaction.getSession());
            rema.setIdRemarque(getIdRemarque());
            try {
                rema.retrieve(transaction);
                rema.delete(transaction);
            } catch (Exception e) {
                _addError(transaction, "Erreur lors de la suppression de la remarque. IdAfact " + getIdAfact());
            }
        }
        // Mise � jour du total dans l'ent�te
        FAEnteteFacture entete = new FAEnteteFacture();
        FAAfact afact = new FAAfact();
        afact.setIdEnteteFacture(getIdEnteteFacture());
        afact.setMontantFacture(getMontantFacture());
        afact.setNonComptabilisable(isNonComptabilisable());
        afact.setAQuittancer(isAQuittancer());
        // ne pas remettre � jour le montant de l'ent�te car on veut aussi la
        // supprimer
        if (!getForceDelete().booleanValue()) {
            entete.removeAfact(transaction, afact);
        }
    }

    /**
     * Effectue des traitements apr�s une lecture dans la BD et apr�s avoir vid� le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements apr�s la lecture de l'entit� dans la BD
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        // R�cup�rer la rubrique
        if (JadeStringUtil.isEmpty(idExterneRubrique) || JadeStringUtil.isBlank(idExterneRubrique)) {
            getRubrique(transaction);
            // -> Les informations de la rubrique sont repris directement dans
            // la requ�te, getRubrique est appeler dans _validate
        }
        if (JadeStringUtil.isEmpty(remarque)) {
            // Recherche de la remarque
            setRemarque(FARemarque.getRemarque(getIdRemarque(), transaction));
        }
    }

    /*
     * Traitement apr�s mise � jour
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws java.lang.Exception {
        // Mise � jour du total dans l'ent�te
        FAEnteteFacture entete = new FAEnteteFacture();
        FAAfact afact = new FAAfact();
        afact.setIdEnteteFacture(getIdEnteteFacture());
        afact.setMontantFacture(getMontantFacture());
        afact.setNonComptabilisable(isNonComptabilisable());
        afact.setAQuittancer(isAQuittancer());
        entete.updateTotal(transaction, afact);
        // Test si c'est une compensation li� � un report de section
        // Si c'est le cas on met � jour la section en fonction de l'�tat de la
        // compensation.
        if (idTypeAfact.equals(FAAfact.CS_AFACT_COMPENSATION)) {
            _mettreAJourSection(transaction);
        }
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incr�mente de +1 le num�ro
        setIdAfact(this._incCounter(transaction, "0"));
        // Initialisation des valeurs par d�faut
        initDefaultValues();
        // Mise � blanc des zones suivant le type d'afact
        _clearData();
        // Calcul des montant � facturer
        if (idTypeAfact.equalsIgnoreCase(FAAfact.CS_AFACT_TABLEAU)) {
            _calculAFacturer(transaction);
        }
        setUser(getSession().getUserId());
        // Mise � jour de la remarque
        if (!globaz.jade.client.util.JadeStringUtil.isBlank(getRemarque())) {
            FARemarque rem = new FARemarque();
            rem.setTexte(getRemarque());
            try {
                rem.add(transaction);
                setIdRemarque(rem.getIdRemarque());
            } catch (Exception e) {
                _addError(transaction, "Erreur lors de la mise � jour de la remarque. idAfact " + getIdAfact());
            }
        }
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        if (getIdTypeAfact().equals(FAAfact.CS_AFACT_COMPENSATION_INTERNE) && (enteteFacture != null)) {
            // PO 9300 - Ne pas prendre le montant sauvegard� car il n'est pas retransmis depuis l'�cran
            FWCurrency montantFacture = changeSigneMontant(getMontantFacture());
            FAAfactManager afactCompenseMana = initManagerPourContrepartie(montantFacture);
            if (afactCompenseMana.size() > 0) {
                for (int i = 0; i < afactCompenseMana.size(); i++) {
                    if (!getIdEnteteFacture().equals(((FAAfact) afactCompenseMana.getEntity(i)).getIdEnteteFacture())) {
                        FAAfact afactCompense = (FAAfact) afactCompenseMana.getEntity(i);
                        afactCompense.wantCallMethodBefore(false);
                        afactCompense.delete();
                        miseAjourTotalEntete(transaction, afactCompense);
                    }
                }
            }
        }
        // Test si c'est une compensation li� � un report de section
        // Si c'est le cas on met � jour la section en fonction de l'�tat de la
        // compensation.
        if (getIdTypeAfact().equals(FAAfact.CS_AFACT_COMPENSATION)) {
            _mettreAJourSection(transaction);
        }
    }

    /*
     * Traitement avant mise � jour
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws java.lang.Exception {
        // Mise � blanc des zones suivant le type d'afact
        _clearData();
        // Calcul des montant � facturer
        if (idTypeAfact.equalsIgnoreCase(FAAfact.CS_AFACT_TABLEAU)) {
            _calculAFacturer(transaction);
        }
        setUser(getSession().getUserId());
        // Si c'est un afact de compensation interne modifier aussi la contre
        // partie
        if (getIdTypeAfact().equals(FAAfact.CS_AFACT_COMPENSATION_INTERNE) && (enteteFacture != null)) {
            // PO 9300 - Prendre le montant intitial de la compensation
            FWCurrency montantFacture = changeSigneMontant(getSaveMontantFacture());
            FAAfactManager afactCompenseMana = initManagerPourContrepartie(montantFacture);
            if (afactCompenseMana.size() > 0) {
                for (int i = 0; i < afactCompenseMana.size(); i++) {
                    if (!getIdEnteteFacture().equals(((FAAfact) afactCompenseMana.getEntity(i)).getIdEnteteFacture())) {
                        FAAfact afactCompense = (FAAfact) afactCompenseMana.getEntity(i);
                        afactCompense.wantCallMethodBefore(false);
                        afactCompense.setAQuittancer(isAQuittancer());
                        // Modifier le montant de l'autre compensation en changeant le signe
                        afactCompense.setMontantFacture(changeSigneMontant(getMontantFacture()).toString());
                        afactCompense.update();
                        miseAjourTotalEntete(transaction, afactCompense);
                    }
                }
            }
        }
        // Mise � jour de la remarque
        FARemarque rem = new FARemarque();
        rem.setSession(transaction.getSession());
        rem.setTexte(getRemarque());
        try {
            if ((!globaz.jade.client.util.JadeStringUtil.isBlank(getIdRemarque()) && (!getIdRemarque()
                    .equalsIgnoreCase("0")))) {
                rem.setIdRemarque(getIdRemarque());
                transaction.disableSpy();
                rem.update(transaction);
                transaction.enableSpy();
            } else if (!globaz.jade.client.util.JadeStringUtil.isBlank(getRemarque())) {
                rem.add(transaction);
                setIdRemarque(rem.getIdRemarque());
            }
        } catch (Exception e) {
            _addError(transaction, "Erreur lors de la mise � jour de la remarque. idAfact " + getIdAfact());
        }
    }

    /*
     * Calcul la masse, le taux et le montant � facturer valable uniquement pour les afacts de type TABLEAU
     */
    protected void _calculAFacturer(BTransaction transaction) throws java.lang.Exception {
        FWCurrency masseInit = new FWCurrency(0);
        FWCurrency masseDejaFact = new FWCurrency(0);
        FWCurrency montantInit = new FWCurrency(0);
        FWCurrency montantDejaFac = new FWCurrency(0);
        FWCurrency montantFact = new FWCurrency(0);
        // masse � facturer
        if (!globaz.jade.client.util.JadeStringUtil.isBlank(masseInitiale)) {
            masseInit = new FWCurrency(masseInitiale);
        }
        if (!globaz.jade.client.util.JadeStringUtil.isBlank(masseDejaFacturee)) {
            masseDejaFact = new FWCurrency(masseDejaFacturee);
        }
        masseInit.sub(masseDejaFact);
        if(!CodeSystem.TYPE_CALCUL_MONTANT_FIXE.equals(typeCalcul)) {
            setMasseFacture(masseInit.toString());
        }
        // taux � facturer
        if (!globaz.jade.client.util.JadeStringUtil.isBlank(tauxInitial)) {
            setTauxFacture(tauxInitial);
        } else {
            setTauxFacture("0");
        }
        // montant � facturer
        if (!globaz.jade.client.util.JadeStringUtil.isBlank(montantInitial)) {
            montantInit = new FWCurrency(montantInitial);
        }
        if (!globaz.jade.client.util.JadeStringUtil.isBlank(montantDejaFacture)) {
            montantDejaFac = new FWCurrency(montantDejaFacture);
        }
        montantFact = new FWCurrency("0");
        montantFact.add(montantInit);
        montantFact.sub(montantDejaFac);
        setMontantFacture(montantFact.toString());
    }

    /*
     * Traitement avant mise � jour
     */
    protected void _clearData() throws java.lang.Exception {
        // Si type afact = COMPENSATION
        if (idTypeAfact.equalsIgnoreCase(FAAfact.CS_AFACT_COMPENSATION)
                || getIdTypeAfact().equals(FAAfact.CS_AFACT_COMPENSATION_INTERNE)) {
            setAnneeCotisation("");
            setDebutPeriode("");
            setFinPeriode("");
            setMasseFacture("");
            setTauxFacture("");
        } else {
            // si type afact diff�rent de COMPENSATION
            setIdRoleDebiteurCompensation("");
            setIdExterneDebiteurCompensation("");
            setIdTypeFactureCompensation("");
            setIdExterneFactureCompensation("");
        }
        // Si type afact diff�rent de TABLEAU
        if (!idTypeAfact.equalsIgnoreCase(FAAfact.CS_AFACT_TABLEAU)) {
            setMasseInitiale("");
            setMasseDejaFacturee("");
            setTauxInitial("");
            setTauxDejaFacture("");
            setMontantInitial("");
            setMontantDejaFacture("");
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.03.2003 12:43:51)
     * 
     * @return globaz.musca.db.facturation.FAPassageViewBean
     */
    public FAEnteteFactureViewBean _getEnteteFacture(BTransaction transaction) {
        // Chargement si n�cessaire
        if (enteteFacture == null) {
            try {
                enteteFacture = new FAEnteteFactureViewBean();
                enteteFacture.setSession(getSession());
                enteteFacture.setIdEntete(getIdEnteteFacture());
                enteteFacture.retrieve(transaction);
            } catch (Exception e) {
                _addError(transaction,
                        "Exception raised in FAAfact._getEnteteFacture(): idAfact " + getIdAfact() + e.getMessage());
            }
        }
        return enteteFacture;
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return FAAfact.TABLE_FIELDS + ", PMTRADP.LIBELLE AS LIBELLERUB, " + CARubrique.TABLE_CARUBRP + "."
                + CARubrique.FIELD_IDEXTERNE + ", FAREMAP.IDREMARQUE, FAREMAP.TEXTE ";
    }

    /**
     * Renvoie la clause FROM
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + FAAfact.TABLE_FAAFACP + " AS " + FAAfact.TABLE_FAAFACP + " LEFT JOIN "
                + _getCollection() + CARubrique.TABLE_CARUBRP + " AS " + CARubrique.TABLE_CARUBRP + " ON ("
                + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_IDRUBRIQUE + "=" + FAAfact.TABLE_FAAFACP + "."
                + FAAfact.FIELD_IDRUBRIQUE + ") LEFT JOIN " + _getCollection()
                + "PMTRADP AS PMTRADP ON (PMTRADP.IDTRADUCTION = " + CARubrique.TABLE_CARUBRP + "."
                + CARubrique.FIELD_IDTRADUCTION + " AND PMTRADP.codeisolangue="
                + this._dbWriteString(statement.getTransaction(), getSession().getIdLangueISO()).toUpperCase() + ") "
                + "LEFT JOIN " + _getCollection() + "FAREMAP AS FAREMAP ON (" + FAAfact.TABLE_FAAFACP + "."
                + FAAfact.FIELD_IDREMARQUE + "=FAREMAP.IDREMARQUE) ";

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return FAAfact.TABLE_FAAFACP;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.03.2003 11:12:44)
     * 
     * @return ITITiers
     */
    protected ITITiers _getTiers() {
        return this._getTiers(null);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (24.02.2003 19:23:58)
     * 
     * @return ITIPersonneAvs
     * @param idRole
     *            String
     * @param id
     *            String
     */
    protected ITITiers _getTiers(BTransaction transaction) {
        // Si cache vide
        if (tiers == null) {
            try {
                FAApplication app = (FAApplication) getSession().getApplication();
                if (transaction == null) {
                    tiers = app.getTiersByRole(getSession(), getIdRoleDebiteurCompensation(),
                            getIdExterneDebiteurCompensation(), getIdTiersDebiteurCompensation(), new String[] {
                                    "getIdTiers", "getDesignation1", "getDesignation2", "getAdresseAsString",
                                    "getLangue", "getLocalite", "getNumAvsActuel" });
                } else {
                    tiers = app.getTiersByRole(transaction, getIdRoleDebiteurCompensation(),
                            getIdExterneDebiteurCompensation(), getIdTiersDebiteurCompensation(), new String[] {
                                    "getIdTiers", "getDesignation1", "getDesignation2", "getAdresseAsString",
                                    "getLangue", "getLocalite", "getNumAvsActuel" });
                }
            } catch (Exception e) {
                _addError(transaction, e.getMessage());
            }
        }
        return tiers;
    }

    private void _mettreAJourSection(BTransaction transaction) {
        try {
            CACompteAnnexeManager comptMana = new CACompteAnnexeManager();
            comptMana.setSession(getSession());
            comptMana.setForIdExterneRole(getIdExterneDebiteurCompensation());
            comptMana.setForIdRole(getIdRoleDebiteurCompensation());
            comptMana.find();
            if (!comptMana.equals(null) && (comptMana.size() == 1)) {
                CASectionManager sectionMana = new CASectionManager();
                sectionMana.setSession(getSession());
                sectionMana.setForIdExterne(getIdExterneFactureCompensation());
                sectionMana.setForIdCompteAnnexe(((CACompteAnnexe) comptMana.getFirstEntity()).getIdCompteAnnexe());
                sectionMana.find();
                if (!sectionMana.equals(null) && (sectionMana.size() == 1)) {
                    CASection section = new CASection();
                    section = (CASection) sectionMana.getFirstEntity();
                    if (isAQuittancer().booleanValue()
                            && APISection.MODE_REPORT.equals(section.getIdModeCompensation())) {
                        section.setIdPassageComp("0");
                        section.update(transaction);
                    } else if (!isAQuittancer().booleanValue()
                            && APISection.MODE_REPORT.equals(section.getIdModeCompensation())) {
                        section.setIdPassageComp(getIdPassage());
                        section.update(transaction);
                    }
                }
            }
        } catch (Exception e) {
            _addError(transaction, "Erreur lors de la mise � jour de la section en mode report. idAfact "
                    + getIdAfact());
        }
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAfact = statement.dbReadNumeric(FAAfact.FIELD_IDAFACT);
        idEnteteFacture = statement.dbReadNumeric(FAAfact.FIELD_IDENTETEFACTURE);
        idPassage = statement.dbReadNumeric(FAAfact.FIELD_IDPASSAGE);
        idModuleFacturation = statement.dbReadNumeric(FAAfact.FIELD_IDMODFAC);
        idRemarque = statement.dbReadNumeric(FAAfact.FIELD_IDREMARQUE);
        idTypeAfact = statement.dbReadNumeric(FAAfact.FIELD_IDTYPEAFACT);
        idRubrique = statement.dbReadNumeric(FAAfact.FIELD_IDRUBRIQUE);
        anneeCotisation = statement.dbReadNumeric(FAAfact.FIELD_ANNEECOTISATION);
        libelle = statement.dbReadString(FAAfact.FIELD_LIBELLE);
        debutPeriode = statement.dbReadDateAMJ(FAAfact.FIELD_DATEDEBUTPERIODE);
        finPeriode = statement.dbReadDateAMJ(FAAfact.FIELD_DATEFINPERIODE);
        montantInitial = statement.dbReadNumeric(FAAfact.FIELD_MONTANTINITIAL, 2);
        montantDejaFacture = statement.dbReadNumeric(FAAfact.FIELD_MONTANTDEJAFACTURE, 2);
        montantFacture = statement.dbReadNumeric(FAAfact.FIELD_MONTANTFACTURE, 2);
        masseInitiale = statement.dbReadNumeric(FAAfact.FIELD_MASSEINITIALE, 2);
        masseDejaFacturee = statement.dbReadNumeric(FAAfact.FIELD_MASSEDEJAFACTUREE, 2);
        masseFacture = statement.dbReadNumeric(FAAfact.FIELD_MASSEFACTURE, 2);
        tauxInitial = statement.dbReadNumeric(FAAfact.FIELD_TAUXINITIAL, 5);
        tauxDejaFacture = statement.dbReadNumeric(FAAfact.FIELD_TAUXDEJAFACTURE, 5);
        tauxFacture = statement.dbReadNumeric(FAAfact.FIELD_TAUXFACTURE, 5);
        referenceExterne = statement.dbReadString(FAAfact.FIELD_REFERENCEEXTERNE);
        user = statement.dbReadString(FAAfact.FIELD_USER);
        nonImprimable = statement.dbReadBoolean(FAAfact.FIELD_NONIMPRIMABLE);
        nonComptabilisable = statement.dbReadBoolean(FAAfact.FIELD_NONCOMPTABILISABLE);
        aQuittancer = statement.dbReadBoolean(FAAfact.FIELD_AQUITTANCER);
        idRoleDebiteurCompensation = statement.dbReadNumeric(FAAfact.FIELD_IDROLDEBCOM);
        idExterneDebiteurCompensation = statement.dbReadString(FAAfact.FIELD_IDEXTDEBCOM);
        idTiersDebiteurCompensation = statement.dbReadNumeric(FAAfact.FIELD_IDTIEDEBCOM);
        idTypeFactureCompensation = statement.dbReadNumeric(FAAfact.FIELD_IDTYPFACCOM);
        idExterneFactureCompensation = statement.dbReadString(FAAfact.FIELD_IDEXTFACCOM);

        libelleRubrique = statement.dbReadString("LIBELLERUB");
        idOrdreRegroupement = statement.dbReadNumeric(FAAfact.FIELD_EHIDOR);
        ordreRegroupement = statement.dbReadNumeric(FAAfact.FIELD_EHNORD);
        libelleOrdreFr = statement.dbReadString(FAAfact.FIELD_EHLLIF);
        libelleOrdreDe = statement.dbReadString(FAAfact.FIELD_EHLLID);
        libelleOrdreIt = statement.dbReadString(FAAfact.FIELD_EHLLII);
        idExterneRubrique = statement.dbReadString(CARubrique.FIELD_IDEXTERNE);
        tiersDesignation1 = statement.dbReadString(FAAfact.FIELD_HTLDE1);
        tiersDesignation2 = statement.dbReadString(FAAfact.FIELD_HTLDE2);
        idExterneRole = statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE);
        idRole = statement.dbReadString(CACompteAnnexe.FIELD_IDROLE);
        remarque = FARemarque.getRemarque(statement);
        numCaisse = statement.dbReadNumeric(FAAfact.FIELD_NUMCAISSE);
        numCaisseOrdre = statement.dbReadNumeric("EHIDCA");
        dateValeur = statement.dbReadDateAMJ(FAAfact.FIELD_DATEVALEUR);
        affichtaux = statement.dbReadBoolean(FAAfact.FIELD_AFFICHTAUX);
        typeCalculInteretMoratoire = statement.dbReadNumeric(FAAfact.FIELD_TYPECALCULIM);
        idSection = statement.dbReadNumeric(FAAfact.FIELD_IDSECTION);
        canton = statement.dbReadNumeric(FAAfact.FIELD_CANTON);
        idAssurance = statement.dbReadNumeric(FAAfact.FIELD_IDASSURANCE);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _propertyMandatory(statement.getTransaction(), getIdAfact(), "L'identifiant de l'afact doit �tre renseign�");
        _propertyMandatory(statement.getTransaction(), getIdTypeAfact(), "Le type d'afact doit �tre renseign�");
        _propertyMandatory(statement.getTransaction(), getIdEnteteFacture(), "La facture doit �tre renseign�e");
        _propertyMandatory(statement.getTransaction(), getIdPassage(), "Le num�ro de passage doit �tre renseign�");
        _propertyMandatory(statement.getTransaction(), getIdModuleFacturation(),
                "Le module de facturation doit �tre renseign�");

        // Gestion de la mise en forme des masses et des montants
        if ((!JadeStringUtil.isBlankOrZero(getMasseInitiale()) && JadeStringUtil.endsWith(getMasseInitiale(), "-"))
                || (!JadeStringUtil.isBlankOrZero(getMasseDejaFacturee()) && JadeStringUtil.endsWith(
                        getMasseDejaFacturee(), "-"))
                || (!JadeStringUtil.isBlankOrZero(getMasseFacture()) && JadeStringUtil.endsWith(getMasseFacture(), "-"))
                || (!JadeStringUtil.isBlankOrZero(getMontantDejaFacture()) && JadeStringUtil.endsWith(
                        getMontantDejaFacture(), "-"))
                || (!JadeStringUtil.isBlankOrZero(getMontantInitial()) && JadeStringUtil.endsWith(getMontantInitial(),
                        "-"))
                || (!JadeStringUtil.isBlankOrZero(getMontantFacture()) && JadeStringUtil.endsWith(getMontantFacture(),
                        "-"))) {
            _addError(statement.getTransaction(), "Le signe de n�gation doit se trouver au d�but du nombre");
            wantCallMethodAfter(false);
        } else {

            // R�cup�rer les montants
            FWCurrency cMontantFacture = new FWCurrency(getMontantFacture());
            FWCurrency cMasseFacture = new FWCurrency(getMasseFacture());
            BigDecimal bdTauxFacture = JAUtil.createBigDecimal(getTauxFacture());

            // V�rifier la rubrique
            APIRubrique _rub = validerRubrique(statement, cMontantFacture, cMasseFacture, bdTauxFacture);

            // La date de fin de p�riode doit �tre sup�rieure ou �gale � celle
            // de d�but et la
            // p�riode doit �tre comprise dans l'ann�e de cotisation si celle ci
            // est renseign�e.
            if ((!globaz.jade.client.util.JadeStringUtil.isBlank(debutPeriode))
                    || (!globaz.jade.client.util.JadeStringUtil.isBlank(finPeriode))) {
                if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(anneeCotisation)) {
                    try {
                        int anneeCoti = Integer.parseInt(anneeCotisation);
                        int anneeDebut = globaz.globall.util.JACalendar.getYear(debutPeriode);
                        int anneeFin = globaz.globall.util.JACalendar.getYear(finPeriode);
                        if ((anneeDebut != anneeCoti) || (anneeFin != anneeCoti)) {
                            _addError(statement.getTransaction(),
                                    "La p�riode doit �tre incluse dans l'ann�e de cotisation si celle ci est renseign�e. ");
                        }
                    } catch (Exception e) {
                        _addError(statement.getTransaction(), "Erreur lors de l'extraction de l'ann�e. idAfact "
                                + getIdAfact());
                    }
                }
                try {
                    if (BSessionUtil.compareDateFirstGreater(getSession(), debutPeriode, finPeriode)) {
                        _addError(statement.getTransaction(),
                                "Le d�but de la p�riode doit �tre inf�rieure � celle de fin. ");
                    }
                } catch (Exception e) {
                    _addError(statement.getTransaction(), "Erreur lors du contr�le de la p�riode. ");
                }
            }
            // Si type afact = COMPENSATION
            if (idTypeAfact.equalsIgnoreCase(FAAfact.CS_AFACT_COMPENSATION)
                    || idTypeAfact.equalsIgnoreCase(FAAfact.CS_AFACT_COMPENSATION_INTERNE)) {
                // Contr�le du n� de tiers d�biteur
                // Si r�le = Assur� => contr�le n� avs
                if (JadeStringUtil.isBlankOrZero(getIdRoleDebiteurCompensation())) {
                    if (!JadeStringUtil.isBlankOrZero(getIdRole())) {
                        setIdRoleDebiteurCompensation(getIdRole());
                    }
                }
                if (JadeStringUtil.isBlankOrZero(getIdTiersDebiteurCompensation())) {
                    if (!JadeStringUtil.isBlankOrZero(getIdEnteteFacture())) {
                        FAEnteteFacture entete = new FAEnteteFacture();
                        entete.setSession(getSession());
                        entete.setIdEntete(getIdEnteteFacture());
                        entete.retrieve();
                        setIdTiersDebiteurCompensation(entete.getIdTiers());
                    }
                }
                if (getIdRoleDebiteurCompensation().equalsIgnoreCase(TIRole.CS_ASSURE)) {
                    TIHistoriqueAvs histAvs = new TIHistoriqueAvs();
                    histAvs.setSession(getSession());
                    try {
                        String idCode = histAvs._ctrlCodeSaisi(statement.getTransaction(),
                                getIdTiersDebiteurCompensation(), getIdExterneDebiteurCompensation());
                        // R�cup�ration des erreurs engendr�es dans la m�thode
                        // _ctrlCodeSaisi()
                        setIdTiersDebiteurCompensation(idCode);
                    } catch (Exception e) {
                        _addError(statement.getTransaction(), "Erreur lors du contr�le de la saisie. ");
                    }
                }
                // Si r�le = contribuable => contr�le n� de contribuable
                if (getIdRoleDebiteurCompensation().equalsIgnoreCase(TIRole.CS_CONTRIBUABLE)) {
                    TIHistoriqueContribuable histContr = new TIHistoriqueContribuable();
                    histContr.setSession(getSession());
                    try {
                        String idCode = histContr._ctrlCodeSaisi(statement.getTransaction(),
                                getIdTiersDebiteurCompensation(), getIdExterneDebiteurCompensation());
                        // R�cup�ration des erreurs engendr�es dans la m�thode
                        // _ctrlCodeSaisi()
                        setIdTiersDebiteurCompensation(idCode);
                    } catch (Exception e) {
                        _addError(statement.getTransaction(), "Erreur lors du contr�le de la saisie. ");
                    }
                }
                if (!JadeStringUtil.isBlankOrZero(getNumCaisse())) {
                    _addError(statement.getTransaction(),
                            "Erreur, le num�ro de caisse ne doit pas �tre saisie pour un afact de type compensation ! "
                                    + enteteFacture.getIdExterneRole());
                }
            }
            if (idTypeAfact.equalsIgnoreCase(FAAfact.CS_AFACT_COMPENSATION)) {
                // Contr�le du num�ro de d�compte
                if (getCheckIdExterneFactureCompensation().booleanValue()) {
                    if (!JadeStringUtil.isEmpty(idExterneFactureCompensation)) {
                        CACompteAnnexeManager compte = new CACompteAnnexeManager();
                        compte.setSession((BSession) ((FAApplication) getSession().getApplication())
                                .getSessionOsiris(getSession()));
                        // PO 4216
                        if (!NSUtil.nssCheckDigit(getIdExterneDebiteurCompensation())) {
                            rechercheIdTiersPourAffilie(statement.getTransaction());
                        }

                        if (JadeStringUtil.isEmpty(getIdExterneDebiteurCompensation())) {
                            FAEnteteFacture entFac = new FAEnteteFacture();
                            entFac.setSession(getSession());
                            entFac.setIdEntete(getIdEnteteFacture());
                            entFac.retrieve();
                            setIdExterneDebiteurCompensation(entFac.getIdExterneRole());
                            setIdRoleDebiteurCompensation(entFac.getIdRole());
                            setIdTiersDebiteurCompensation(entFac.getIdTiers());
                        }
                        // compte.setForIdExterneRole(getIdExterneDebiteurCompensation());
                        compte.setForIdTiers(getIdTiersDebiteurCompensation());
                        compte.setForIdRole(getIdRoleDebiteurCompensation());
                        try {
                            compte.find();
                            if (compte.size() > 0) {
                                boolean sectionTrouve = false;
                                for (int i = 0; i < compte.size(); i++) {
                                    CASectionManager section = new CASectionManager();
                                    section.setSession((BSession) ((FAApplication) getSession().getApplication())
                                            .getSessionOsiris(getSession()));
                                    section.setForIdExterne(idExterneFactureCompensation);
                                    section.setForIdCompteAnnexe(((CACompteAnnexe) compte.getEntity(i))
                                            .getIdCompteAnnexe());
                                    section.setForIdTypeSection(getIdTypeFactureCompensation());
                                    section.find();
                                    if (section.size() > 0) {
                                        sectionTrouve = true;
                                        // PO 9316
                                        setIdSection(((CASection) section.getFirstEntity()).getIdSection());
                                    }
                                }
                                if (!sectionTrouve) {
                                    _addError(statement.getTransaction(),
                                            "Le num�ro de d�compte n'existe pas en comptabilit�. "
                                                    + getIdExterneFactureCompensation() + " pour l'affili� "
                                                    + getIdExterneDebiteurCompensation());
                                }
                            } else {
                                _addError(statement.getTransaction(),
                                        "Le num�ro de d�compte n'existe pas en comptabilit�. "
                                                + getIdExterneFactureCompensation() + " pour l'affili� "
                                                + getIdExterneDebiteurCompensation());
                            }
                        } catch (Exception e) {
                            _addError(statement.getTransaction(), "Erreur lors du contr�le du d�compte. ");
                        }
                    } else {
                        _addError(statement.getTransaction(), "Le num�ro de d�compte doit �tre saisi. ");
                    }
                }
            }
            // Si type afact = COMPENSATION INTERNE
            if (idTypeAfact.equalsIgnoreCase(FAAfact.CS_AFACT_COMPENSATION_INTERNE)) {
                // Contr�le du num�ro de d�compte
                if (JadeStringUtil.isEmpty(getIdExterneDebiteurCompensation())) {
                    FAEnteteFacture entFac = new FAEnteteFacture();
                    entFac.setSession(getSession());
                    entFac.setIdEntete(getIdEnteteFacture());
                    entFac.retrieve();
                    setIdExterneDebiteurCompensation(entFac.getIdExterneRole());
                    setIdRoleDebiteurCompensation(entFac.getIdRole());
                    setIdTiersDebiteurCompensation(entFac.getIdTiers());
                }
                if ((!JadeStringUtil.isEmpty(idExterneFactureCompensation))
                        && !JadeStringUtil.isEmpty(getIdExterneDebiteurCompensation())) {
                    FAEnteteFactureManager entFac = new FAEnteteFactureManager();
                    entFac.setSession(getSession());
                    entFac.setForIdPassage(getIdPassage());
                    entFac.setForIdExterneRole(getIdExterneDebiteurCompensation());
                    entFac.setForIdExterneFacture(getIdExterneFactureCompensation());
                    try {
                        entFac.find();
                        if (entFac.size() == 0) {
                            _addError(statement.getTransaction(),
                                    "Le num�ro de d�compte n'existe pas dans le passage. "
                                            + getIdExterneFactureCompensation() + " pour l'affili� "
                                            + getIdExterneDebiteurCompensation());
                        }
                    } catch (Exception e) {
                        _addError(statement.getTransaction(), "Erreur lors du contr�le du d�compte. ");
                    }
                }
            }
            // Si type afact = TABLEAU
            if (idTypeAfact.equalsIgnoreCase(FAAfact.CS_AFACT_TABLEAU) && (isControleDeuxFrancs())) {
                // Contr�le si un montant a �t� saisi qu'il ne soit pas
                // diff�rent (si rubrique de type masse)
                // � + ou - 2 Fr. de la masse * taux
                validerAfactTableau(statement, _rub);
            }
            if (idTypeAfact.equalsIgnoreCase(FAAfact.CS_AFACT_COMPENSATION)
                    || idTypeAfact.equalsIgnoreCase(FAAfact.CS_AFACT_COMPENSATION_INTERNE)) {
                if (JadeStringUtil.isBlank(getIdExterneDebiteurCompensation())) {
                    FAEnteteFacture entete = _getEnteteFacture(statement.getTransaction());
                    if (!JadeStringUtil.isBlank(entete.getIdExterneRole())) {
                        setIdExterneDebiteurCompensation(entete.getIdExterneRole());
                        setIdTiersDebiteurCompensation(entete.getIdTiers());
                        setIdRoleDebiteurCompensation(entete.getIdRole());
                    }
                }
            }
            if ((getNumCaisse() != null) && !getNumCaisse().equals("") && !getNumCaisse().equals("0")) {
                TIAdministrationManager adminManager = new TIAdministrationManager();
                adminManager.setSession(getSession());
                adminManager.setForIdTiersAdministration(getNumCaisse());
                adminManager.setForGenreAdministration(FAAfact.CAISSE_PROF);
                try {
                    adminManager.find();
                } catch (Exception e) {
                    _addError(null, e.getMessage());
                }
                if (adminManager.size() == 0) {
                    _addError(statement.getTransaction(), "Erreur : Ce num�ro de caisse n'existe pas " + getNumCaisse()
                            + " pour l'affili� " + _getEnteteFacture(statement.getTransaction()).getIdExterneRole());
                }
            }
        }
    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == FAAfact.AK_INTERETSMORATOIRES) {
            statement.writeKey(FAAfact.FIELD_REFERENCEEXTERNE,
                    this._dbWriteString(statement.getTransaction(), getReferenceExterne(), ""));
        } else {
            throw new Exception("Unknown alternate key " + alternateKey);
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FAAfact.FIELD_IDAFACT, this._dbWriteNumeric(statement.getTransaction(), getIdAfact(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FAAfact.FIELD_IDAFACT,
                this._dbWriteNumeric(statement.getTransaction(), getIdAfact(), "idAfact"));
        statement.writeField(FAAfact.FIELD_IDENTETEFACTURE,
                this._dbWriteNumeric(statement.getTransaction(), getIdEnteteFacture(), "idEnteteFacture"));
        statement.writeField(FAAfact.FIELD_IDPASSAGE,
                this._dbWriteNumeric(statement.getTransaction(), getIdPassage(), "idPassage"));
        statement.writeField(FAAfact.FIELD_IDMODFAC,
                this._dbWriteNumeric(statement.getTransaction(), getIdModuleFacturation(), "idModuleFacturation"));
        statement.writeField(FAAfact.FIELD_IDREMARQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRemarque(), "idRemarque"));
        statement.writeField(FAAfact.FIELD_IDTYPEAFACT,
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeAfact(), "idTypeAfact"));
        statement.writeField(FAAfact.FIELD_IDRUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
        statement.writeField(FAAfact.FIELD_ANNEECOTISATION,
                this._dbWriteNumeric(statement.getTransaction(), getAnneeCotisation(), "anneeCotisation"));
        statement.writeField(FAAfact.FIELD_LIBELLE,
                this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField(FAAfact.FIELD_DATEDEBUTPERIODE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDebutPeriode(), "debutPeriode"));
        statement.writeField(FAAfact.FIELD_DATEFINPERIODE,
                this._dbWriteDateAMJ(statement.getTransaction(), getFinPeriode(), "finPeriode"));
        statement.writeField(FAAfact.FIELD_MONTANTINITIAL,
                this._dbWriteNumeric(statement.getTransaction(), getMontantInitial(), "montantInitial"));
        statement.writeField(FAAfact.FIELD_MONTANTDEJAFACTURE,
                this._dbWriteNumeric(statement.getTransaction(), getMontantDejaFacture(), "montantDejaFacture"));
        statement.writeField(FAAfact.FIELD_MONTANTFACTURE,
                this._dbWriteNumeric(statement.getTransaction(), getMontantFacture(), "montantFacture"));
        statement.writeField(FAAfact.FIELD_MASSEINITIALE,
                this._dbWriteNumeric(statement.getTransaction(), getMasseInitiale(), "masseInitiale"));
        statement.writeField(FAAfact.FIELD_MASSEDEJAFACTUREE,
                this._dbWriteNumeric(statement.getTransaction(), getMasseDejaFacturee(), "masseDejaFacturee"));
        statement.writeField(FAAfact.FIELD_MASSEFACTURE,
                this._dbWriteNumeric(statement.getTransaction(), getMasseFacture(), "masseFacture"));
        statement.writeField(FAAfact.FIELD_TAUXINITIAL,
                this._dbWriteNumeric(statement.getTransaction(), getTauxInitial(), "tauxInitial"));
        statement.writeField(FAAfact.FIELD_TAUXDEJAFACTURE,
                this._dbWriteNumeric(statement.getTransaction(), getTauxDejaFacture(), "tauxDejaFacture"));
        statement.writeField(FAAfact.FIELD_TAUXFACTURE,
                this._dbWriteNumeric(statement.getTransaction(), getTauxFacture(), "tauxFacture"));
        statement.writeField(FAAfact.FIELD_REFERENCEEXTERNE,
                this._dbWriteString(statement.getTransaction(), getReferenceExterne(), "referenceExterne"));
        statement.writeField(FAAfact.FIELD_USER, this._dbWriteString(statement.getTransaction(), getUser(), "user"));
        statement.writeField(FAAfact.FIELD_NONIMPRIMABLE, this._dbWriteBoolean(statement.getTransaction(),
                isNonImprimable(), BConstants.DB_TYPE_BOOLEAN_CHAR, "nonImprimable"));
        statement.writeField(FAAfact.FIELD_NONCOMPTABILISABLE, this._dbWriteBoolean(statement.getTransaction(),
                isNonComptabilisable(), BConstants.DB_TYPE_BOOLEAN_CHAR, "nonComptabilisable"));
        statement.writeField(FAAfact.FIELD_AQUITTANCER, this._dbWriteBoolean(statement.getTransaction(),
                isAQuittancer(), BConstants.DB_TYPE_BOOLEAN_CHAR, "aQuittancer"));
        statement.writeField(FAAfact.FIELD_IDROLDEBCOM, this._dbWriteNumeric(statement.getTransaction(),
                getIdRoleDebiteurCompensation(), "idRoleDebiteurCompensation"));
        statement.writeField(FAAfact.FIELD_IDEXTDEBCOM, this._dbWriteString(statement.getTransaction(),
                getIdExterneDebiteurCompensation(), "idExterneDebiteurCompensation"));
        statement.writeField(FAAfact.FIELD_IDTIEDEBCOM, this._dbWriteNumeric(statement.getTransaction(),
                getIdTiersDebiteurCompensation(), "idTiersDebiteurCompensation"));
        statement.writeField(FAAfact.FIELD_IDTYPFACCOM, this._dbWriteNumeric(statement.getTransaction(),
                getIdTypeFactureCompensation(), "idTypeFactureCompensation"));
        statement.writeField(FAAfact.FIELD_IDEXTFACCOM, this._dbWriteString(statement.getTransaction(),
                getIdExterneFactureCompensation(), "idExterneFactureCompensation"));
        statement.writeField(FAAfact.FIELD_NUMCAISSE,
                this._dbWriteNumeric(statement.getTransaction(), getNumCaisse(), "numCaisse"));
        statement.writeField(FAAfact.FIELD_DATEVALEUR,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateValeur(), "dateValeur"));
        statement.writeField(FAAfact.FIELD_AFFICHTAUX, this._dbWriteBoolean(statement.getTransaction(),
                getAffichtaux(), BConstants.DB_TYPE_BOOLEAN_CHAR, "affichtaux"));
        statement.writeField(FAAfact.FIELD_TYPECALCULIM, this._dbWriteNumeric(statement.getTransaction(),
                getTypeCalculInteretMoratoire(), "typeCalculInteretMoratoire"));
        statement.writeField(FAAfact.FIELD_IDSECTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdSection(), "idSection"));
        statement.writeField(FAAfact.FIELD_CANTON,
                this._dbWriteNumeric(statement.getTransaction(), getCanton(), "canton"));
        statement.writeField(FAAfact.FIELD_IDASSURANCE,
                this._dbWriteNumeric(statement.getTransaction(), getIdAssurance(), "idAssurance"));
    }

    public boolean canDoNext() {
        return false;
    }

    public boolean canDoPrev() {
        return false;
    }

    public FWCurrency changeSigneMontant(String montant) {
        if (!JadeStringUtil.isEmpty(montant)) {
            FWCurrency montantFacture = new FWCurrency(JANumberFormatter.deQuote(montant));
            if (montantFacture.isPositive()) {
                montantFacture.negate();
            } else {
                montantFacture.abs();
            }
            return montantFacture;
        } else {
            return new FWCurrency("0");
        }
    }

    /**
     * Insert the method's description here. Creation date: (18.06.2003 09:01:14)
     * 
     * @return java.lang.Object
     * @exception java.lang.CloneNotSupportedException
     *                The exception description.
     */
    @Override
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }

    public Boolean getAffichtaux() {
        return affichtaux;
    }

    public String getAnneeCotisation() {
        if (JadeStringUtil.isIntegerEmpty(anneeCotisation)) {
            return "";
        } else {
            return anneeCotisation;
        }
    }

    public String getCanton() {
        return canton;
    }

    public Boolean getCheckIdExterneFactureCompensation() {
        return checkIdExterneFactureCompensation;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.05.2003 18:18:55)
     * 
     * @return int
     */
    public String getCodeISOLangue() {
        return codeISOLangue;
    }

    public int getCount() {
        return 0;
    }

    /**
     * @return
     */
    public String getDateValeur() {
        return dateValeur;
    }

    public String getDebutPeriode() {
        return debutPeriode;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.03.2003 10:50:06)
     * 
     * @return String
     */
    public String getDescriptionDecompte() {
        return _getEnteteFacture(null).getDescriptionDecompte();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.03.2003 11:24:17)
     * 
     * @return String
     */
    public String getDescriptionTiers() {
        String result = _getEnteteFacture(null).getDescriptionTiers();
        if (JadeStringUtil.isEmpty(result) || JadeStringUtil.isBlank(result)) {
            return getIdExterneDebiteurCompensation() + " " + getNomTiers();
        } else {
            return result;
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.03.2003 17:17:26)
     * 
     * @return String
     */
    public String getDescriptionTiersForList() {
        return _getEnteteFacture(null).getDescriptionTiersForList();
    }

    /**
     * @return
     */
    public FAEnteteFactureViewBean getEnteteFacture() {
        return enteteFacture;
    }

    public String getFinPeriode() {
        return finPeriode;
    }

    /**
     * Returns the forceDelete.
     * 
     * @return Boolean
     */
    public Boolean getForceDelete() {
        return forceDelete;
    }

    /**
     * Getter
     */
    public String getIdAfact() {
        return idAfact;
    }

    public String getIdAssurance() {
        return idAssurance;
    }

    public String getIdEnteteFacture() {
        return idEnteteFacture;
    }

    public String getIdExterneDebiteurCompensation() {
        return idExterneDebiteurCompensation;
    }

    public String getIdExterneFactureCompensation() {
        return idExterneFactureCompensation;
    }

    /**
     * @return
     */
    public String getIdExterneRole() {
        return idExterneRole;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (09.12.2002 17:48:07)
     * 
     * @return String
     */
    public String getIdExterneRubrique() {
        return idExterneRubrique;
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    /**
     * @return
     */
    public String getIdOrdreRegroupement() {
        return idOrdreRegroupement;
    }

    /**
     * Getter
     */
    public String getIdPassage() {
        return idPassage;
    }

    public String getIdRemarque() {
        return idRemarque;
    }

    /**
     * @return
     */
    public String getIdRole() {
        return idRole;
    }

    public String getIdRoleDebiteurCompensation() {
        return idRoleDebiteurCompensation;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdTiersDebiteurCompensation() {
        return idTiersDebiteurCompensation;
    }

    public String getIdTiersEntete() {
        String result = _getEnteteFacture(null).getIdTiers();
        return result;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.04.2003 13:57:02)
     * 
     * @return String
     */
    public String getIdTiersExterneRole() {
        return _getEnteteFacture(null).getIdExterneRole();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.04.2003 13:57:02)
     * 
     * @return String
     */
    public String getIdTiersRole() {
        return _getEnteteFacture(null).getIdRole();
    }

    public String getIdTypeAfact() {
        return idTypeAfact;
    }

    public String getIdTypeFactureCompensation() {
        return idTypeFactureCompensation;
    }

    /**
     * @see globaz.osiris.external.CAIntElementCalculInteretMoratoire#getImRubrique()
     */
    public APIRubrique getImRubrique() {
        return getRubrique(null);
    }

    /**
     * @see globaz.osiris.external.CAIntElementCalculInteretMoratoire#getImRubrique()
     */
    public APIRubrique getImRubrique(BITransaction transaction) {
        return getRubrique((BTransaction) transaction);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.03.2003 10:44:37)
     * 
     * @return String
     */
    public String getISOLangueTiers() {
        // reprendre la m�thode de l'ent�te
        return _getEnteteFacture(null).getISOLangueTiers();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.03.2003 10:44:37)
     * 
     * @return String
     */
    public String getLangueTiers(FAEnteteFactureViewBean factureViewBean) {
        // reprendre la m�thode de l'ent�te
        return factureViewBean.getLangueTiers();
    }

    public String getLibelle() {
        return libelle;
    }

    public String getLibelleCompta(FAEnteteFacture enteteFacture, boolean secCompense) throws Exception {
        String s = "";
        if (JadeStringUtil.isBlank(getLibelle())) {
            if (idTypeAfact.equals(FAAfact.CS_AFACT_COMPENSATION)) {
                try {
                    CACompteAnnexe compte = new CACompteAnnexe();
                    compte.setSession(getSession());
                    compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                    compte.setIdRole(getIdRoleDebiteurCompensation());
                    compte.setIdExterneRole(getIdExterneDebiteurCompensation());
                    compte.retrieve();

                    CASectionManager secMana = new CASectionManager();
                    secMana.setSession(getSession());
                    secMana.setForIdExterne(getIdExterneFactureCompensation());
                    secMana.setForIdCompteAnnexe(compte.getIdCompteAnnexe());
                    secMana.find();

                    if (secMana.size() > 0) {
                        CASection section = (CASection) secMana.getFirstEntity();
                        if ((CAUtil.isSoldeSectionLessOrEqualTaxes(section.getSolde(), section.getTaxes()))
                                && !JadeStringUtil.isIntegerEmpty(section.getSolde())) {
                            if (JadeStringUtil.isIntegerEmpty(getIdExterneFactureCompensation())) {
                                if (secCompense) {
                                    s = getSession().getApplication().getLabel("FACTAXESOMMATION", getISOLangueTiers());
                                } else {
                                    s = getSession().getApplication().getLabel("FACTAXESOMMATION", getISOLangueTiers());
                                }
                            } else {
                                if (secCompense) {
                                    s = getSession().getApplication().getLabel("FACTAXESOMMATION", getISOLangueTiers())
                                            + " - " + enteteFacture.getIdExterneFacture();
                                } else {
                                    s = getSession().getApplication().getLabel("FACTAXESOMMATION", getISOLangueTiers())
                                            + " - " + getIdExterneFactureCompensation();
                                }

                            }
                        } else if ((section.getIdModeCompensation().equals(APISection.MODE_REPORT)
                                || section.getIdModeCompensation().equals(APISection.MODE_COMP_COMPLEMENTAIRE)
                                || section.getIdModeCompensation().equals(APISection.MODE_COMP_CONT_EMPLOYEUR)
                                || section.getIdModeCompensation().equals(APISection.MODE_COMP_COT_PERS) || section
                                .getIdModeCompensation().equals(APISection.MODE_COMP_DEC_FINAL))
                                && !JadeStringUtil.isEmpty(getIdExterneFactureCompensation())) {
                            if (JadeStringUtil.isIntegerEmpty(getIdExterneFactureCompensation())) {
                                if (secCompense) {
                                    s = getSession().getApplication().getLabel("FACREPORT", getISOLangueTiers());
                                } else {
                                    s = getSession().getApplication().getLabel("FACREPORT", getISOLangueTiers());
                                }
                            } else {
                                if (secCompense) {
                                    s = getSession().getApplication().getLabel("FACREPORTSUR", getISOLangueTiers())
                                            + " - " + enteteFacture.getIdExterneFacture();
                                } else {
                                    s = getSession().getApplication().getLabel("FACREPORTDE", getISOLangueTiers())
                                            + " - " + getIdExterneFactureCompensation();
                                }
                            }
                        } else {
                            if (JadeStringUtil.isIntegerEmpty(getIdExterneFactureCompensation())) {
                                s = this.getLibelleRubrique();
                            } else {
                                if (isLibelleCompTropLong(getSession())) {
                                    if (secCompense) {
                                        s = getSession().getApplication().getLabel("FACCOMPSUR_PROP",
                                                getISOLangueTiers())
                                                + " - "
                                                + enteteFacture.getIdExterneFacture()
                                                + " "
                                                + enteteFacture.getIdExterneRole();
                                    } else {
                                        s = getSession().getApplication().getLabel("FACCOMPDE_PROP",
                                                getISOLangueTiers())
                                                + " - "
                                                + getIdExterneFactureCompensation()
                                                + " "
                                                + getIdExterneDebiteurCompensation();
                                    }
                                } else {
                                    if (secCompense) {
                                        s = getSession().getApplication().getLabel("FACCOMPSUR", getISOLangueTiers())
                                                + " - " + enteteFacture.getIdExterneFacture() + " "
                                                + enteteFacture.getIdExterneRole();
                                    } else {
                                        s = getSession().getApplication().getLabel("FACCOMPDE", getISOLangueTiers())
                                                + " - " + getIdExterneFactureCompensation() + " "
                                                + getIdExterneDebiteurCompensation();
                                    }
                                }

                            }
                        }
                        return s;
                    }
                    return s;
                } catch (Exception e) {
                    JadeLogger.error(this, e);
                    return "";
                }
            } else if (idTypeAfact.equals(FAAfact.CS_AFACT_COMPENSATION_INTERNE)) {
                if (JadeStringUtil.isIntegerEmpty(getIdExterneFactureCompensation())) {
                    s = this.getLibelleRubrique();
                } else {
                    if (secCompense) {
                        s = getSession().getApplication().getLabel("FACCOMPSUR", getISOLangueTiers()) + " - "
                                + enteteFacture.getIdExterneFacture() + " " + getIdExterneRole();
                    } else {
                        s = getSession().getApplication().getLabel("FACCOMPDE", getISOLangueTiers()) + " - "
                                + getIdExterneFactureCompensation() + " " + getIdExterneRole();
                    }
                }
                return s;
            }
        }
        return getLibelle();
    }

    public String getLibelleOrdre(String langue) {
        if (langue.equalsIgnoreCase("fr")) {
            return getLibelleOrdreFr();
        }
        if (langue.equalsIgnoreCase("de")) {
            return getLibelleOrdreDe();
        } else {
            return getLibelleOrdreIt();
        }
    }

    /**
     * @return
     */
    public String getLibelleOrdreDe() {
        return libelleOrdreDe;
    }

    /**
     * @return
     */
    public String getLibelleOrdreFr() {
        return libelleOrdreFr;
    }

    /**
     * @return
     */
    public String getLibelleOrdreIt() {
        return libelleOrdreIt;
    }

    public String getLibelleRetourLigne() {
        if (getLibelle().length() > FAAfact.LONGUEUR_MAX_LIBELLE) {
            String retourLigne = getLibelle().substring(0, FAAfact.LONGUEUR_MAX_LIBELLE);
            setLibelle(getLibelle().substring(0, retourLigne.lastIndexOf(" ")) + "\n"
                    + getLibelle().substring(retourLigne.lastIndexOf(" ") + 1));
        }
        return libelle;
    }

    /**
     * @return
     */
    public String getLibelleRetourLigneSansModifEtatObject() {
        String libelleLocal = libelle;
        if (getLibelle().length() > FAAfact.LONGUEUR_MAX_LIBELLE) {
            String retourLigne = getLibelle().substring(0, FAAfact.LONGUEUR_MAX_LIBELLE);
            libelleLocal = getLibelle().substring(0, retourLigne.lastIndexOf(" ")) + "\n"
                    + getLibelle().substring(retourLigne.lastIndexOf(" ") + 1);
        }
        return libelleLocal;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.04.2003 16:55:52)
     * 
     * @return String
     */
    public String getLibelleRubrique() {
        if (JadeStringUtil.isEmpty(libelleRubrique) || JadeStringUtil.isBlank(libelleRubrique)) {
            try {
                FAApplication app = (FAApplication) getSession().getApplication();
                libelleRubrique = app.getLibelleRubrique(getSession(), getIdRubrique());
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return libelleRubrique;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.04.2003 16:55:52)
     * 
     * @return String
     */
    public String getLibelleRubrique(BTransaction transaction) {
        return this.getLibelleRubrique(transaction, getSession().getIdLangueISO());

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.04.2003 16:55:52)
     * 
     * @return String
     */
    public String getLibelleRubrique(BTransaction transaction, String codeISOLangue) {
        String tempLibRubrique = libelleRubrique;
        if (JadeStringUtil.isBlank(libelle) || !codeISOLangue.equalsIgnoreCase((getSession().getIdLangueISO()))) {
            try {
                FAApplication app = (FAApplication) getSession().getApplication();
                tempLibRubrique = app.getLibelleRubrique(transaction, getIdRubrique(), codeISOLangue);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return tempLibRubrique;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.04.2003 16:55:52)
     * 
     * @return String
     */
    public String getLibelleRubrique(String langue) {
        if (JadeStringUtil.isBlank(libelle)) {
            try {
                FAApplication app = (FAApplication) getSession().getApplication();
                libelle = app.getLibelleRubrique(getSession(), getIdRubrique(), langue);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return libelle;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.04.2003 16:55:52)
     *
     * @return String
     */
    public String getLibelleRubriqueSansModifEtatObjet(String langue) {
        String libelleLocal = libelle;
        if (JadeStringUtil.isBlank(libelleLocal)) {
            try {
                FAApplication app = (FAApplication) getSession().getApplication();
                libelleLocal = app.getLibelleRubrique(getSession(), getIdRubrique(), langue);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return libelleLocal;
    }

    public String getLibelleSurFacture(String langue) {
        String s = "";
        if (JadeStringUtil.isBlank(libelle)) {
            if (idTypeAfact.equals(FAAfact.CS_AFACT_COMPENSATION)) {
                try {
                    CACompteAnnexe compte = new CACompteAnnexe();
                    compte.setSession(getSession());
                    compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                    compte.setIdRole(getIdRoleDebiteurCompensation());
                    compte.setIdExterneRole(getIdExterneDebiteurCompensation());
                    compte.retrieve();

                    CASectionManager secMana = new CASectionManager();
                    secMana.setSession(getSession());
                    secMana.setForIdExterne(getIdExterneFactureCompensation());
                    secMana.setForIdCompteAnnexe(compte.getIdCompteAnnexe());
                    secMana.find();

                    if (secMana.size() > 0) {
                        CASection section = (CASection) secMana.getFirstEntity();
                        if ((CAUtil.isSoldeSectionLessOrEqualTaxes(section.getSolde(), section.getTaxes()))
                                && !JadeStringUtil.isIntegerEmpty(section.getSolde())) {
                            if (JadeStringUtil.isIntegerEmpty(getIdExterneFactureCompensation())) {
                                s = getSession().getApplication().getLabel("FACTAXESOMMATION", getISOLangueTiers());
                            } else {
                                s = getSession().getApplication().getLabel("FACTAXESOMMATION", getISOLangueTiers())
                                        + " - " + getIdExterneFactureCompensation() + " "
                                        + section.getDescription(getISOLangueTiers());
                            }
                        } else if ((section.getIdModeCompensation().equals(APISection.MODE_REPORT)
                                || section.getIdModeCompensation().equals(APISection.MODE_COMP_COMPLEMENTAIRE)
                                || section.getIdModeCompensation().equals(APISection.MODE_COMP_CONT_EMPLOYEUR)
                                || section.getIdModeCompensation().equals(APISection.MODE_COMP_COT_PERS) || section
                                .getIdModeCompensation().equals(APISection.MODE_COMP_DEC_FINAL))
                                && !JadeStringUtil.isEmpty(getIdExterneFactureCompensation())) {
                            if (JadeStringUtil.isIntegerEmpty(getIdExterneFactureCompensation())) {
                                s = getSession().getApplication().getLabel("FACREPORT", langue);
                            } else {
                                s = getSession().getApplication().getLabel("FACREPORT", langue) + " - "
                                        + getIdExterneFactureCompensation() + " " + section.getDescription(langue);
                            }
                        } else {
                            if (JadeStringUtil.isIntegerEmpty(getIdExterneFactureCompensation())) {
                                s = this.getLibelleRubrique(langue);
                            } else if (section.getCompteAnnexe().getIdExterneRole().equals(getIdExterneRole())) {
                                s = this.getLibelleRubrique(langue) + " - " + getIdExterneFactureCompensation() + " "
                                        + section.getDescription(langue);
                            } else {
                                s = this.getLibelleRubrique(langue) + " - " + getIdExterneFactureCompensation() + " "
                                        + section.getDescription(langue) + " "
                                        + section.getCompteAnnexe().getIdExterneRole();
                            }
                        }
                        return s;
                    }
                    return s;
                } catch (Exception e) {
                    JadeLogger.error(this, e);
                    return "";
                }
            } else if (idTypeAfact.equals(FAAfact.CS_AFACT_COMPENSATION_INTERNE)) {
                try {
                    if (JadeStringUtil.isIntegerEmpty(getIdExterneFactureCompensation())) {
                        s = getSession().getApplication().getLabel("FACCOMPENINTERNE", getISOLangueTiers());
                    } else if (getIdExterneDebiteurCompensation().equals(getIdExterneRole())) {
                        s = getSession().getApplication().getLabel("FACCOMPENINTERNE", getISOLangueTiers()) + " - "
                                + getIdExterneFactureCompensation();
                    } else {
                        s = getSession().getApplication().getLabel("FACCOMPENINTERNE", getISOLangueTiers()) + " - "
                                + getIdExterneFactureCompensation() + " - " + getIdExterneDebiteurCompensation();
                    }
                    return s;
                } catch (Exception e) {
                    JadeLogger.error(this, e);
                    return "";
                }
            } else {
                if (JadeStringUtil.isIntegerEmpty(anneeCotisation)) {
                    return this.getLibelleRubrique(langue);
                } else {
                    return this.getLibelleRubrique(langue) + " " + anneeCotisation;
                }
            }
        } else {
            return libelle;
        }
    }

    public String getLibelleSurFactureSansModifEtatObjet(String langue) {
        String s = "";
        if (JadeStringUtil.isBlank(libelle)) {
            if (idTypeAfact.equals(FAAfact.CS_AFACT_COMPENSATION)) {
                try {
                    CACompteAnnexe compte = new CACompteAnnexe();
                    compte.setSession(getSession());
                    compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                    compte.setIdRole(getIdRoleDebiteurCompensation());
                    compte.setIdExterneRole(getIdExterneDebiteurCompensation());
                    compte.retrieve();

                    CASectionManager secMana = new CASectionManager();
                    secMana.setSession(getSession());
                    secMana.setForIdExterne(getIdExterneFactureCompensation());
                    secMana.setForIdCompteAnnexe(compte.getIdCompteAnnexe());
                    secMana.find();

                    if (secMana.size() > 0) {
                        CASection section = (CASection) secMana.getFirstEntity();
                        if ((CAUtil.isSoldeSectionLessOrEqualTaxes(section.getSolde(), section.getTaxes()))
                                && !JadeStringUtil.isIntegerEmpty(section.getSolde())) {
                            if (JadeStringUtil.isIntegerEmpty(getIdExterneFactureCompensation())) {
                                s = getSession().getApplication().getLabel("FACTAXESOMMATION", getISOLangueTiers());
                            } else {
                                s = getSession().getApplication().getLabel("FACTAXESOMMATION", getISOLangueTiers())
                                        + " - " + getIdExterneFactureCompensation() + " "
                                        + section.getDescription(getISOLangueTiers());
                            }
                        } else if ((section.getIdModeCompensation().equals(APISection.MODE_REPORT)
                                || section.getIdModeCompensation().equals(APISection.MODE_COMP_COMPLEMENTAIRE)
                                || section.getIdModeCompensation().equals(APISection.MODE_COMP_CONT_EMPLOYEUR)
                                || section.getIdModeCompensation().equals(APISection.MODE_COMP_COT_PERS) || section
                                .getIdModeCompensation().equals(APISection.MODE_COMP_DEC_FINAL))
                                && !JadeStringUtil.isEmpty(getIdExterneFactureCompensation())) {
                            if (JadeStringUtil.isIntegerEmpty(getIdExterneFactureCompensation())) {
                                s = getSession().getApplication().getLabel("FACREPORT", langue);
                            } else {
                                s = getSession().getApplication().getLabel("FACREPORT", langue) + " - "
                                        + getIdExterneFactureCompensation() + " " + section.getDescription(langue);
                            }
                        } else {
                            if (JadeStringUtil.isIntegerEmpty(getIdExterneFactureCompensation())) {
                                s = this.getLibelleRubriqueSansModifEtatObjet(langue);
                            } else if (section.getCompteAnnexe().getIdExterneRole().equals(getIdExterneRole())) {
                                s = this.getLibelleRubriqueSansModifEtatObjet(langue) + " - " + getIdExterneFactureCompensation() + " "
                                        + section.getDescription(langue);
                            } else {
                                s = this.getLibelleRubriqueSansModifEtatObjet(langue) + " - " + getIdExterneFactureCompensation() + " "
                                        + section.getDescription(langue) + " "
                                        + section.getCompteAnnexe().getIdExterneRole();
                            }
                        }
                        return s;
                    }
                    return s;
                } catch (Exception e) {
                    JadeLogger.error(this, e);
                    return "";
                }
            } else if (idTypeAfact.equals(FAAfact.CS_AFACT_COMPENSATION_INTERNE)) {
                try {
                    if (JadeStringUtil.isIntegerEmpty(getIdExterneFactureCompensation())) {
                        s = getSession().getApplication().getLabel("FACCOMPENINTERNE", getISOLangueTiers());
                    } else if (getIdExterneDebiteurCompensation().equals(getIdExterneRole())) {
                        s = getSession().getApplication().getLabel("FACCOMPENINTERNE", getISOLangueTiers()) + " - "
                                + getIdExterneFactureCompensation();
                    } else {
                        s = getSession().getApplication().getLabel("FACCOMPENINTERNE", getISOLangueTiers()) + " - "
                                + getIdExterneFactureCompensation() + " - " + getIdExterneDebiteurCompensation();
                    }
                    return s;
                } catch (Exception e) {
                    JadeLogger.error(this, e);
                    return "";
                }
            } else {
                if (JadeStringUtil.isIntegerEmpty(anneeCotisation)) {
                    return this.getLibelleRubriqueSansModifEtatObjet(langue);
                } else {
                    return this.getLibelleRubriqueSansModifEtatObjet(langue) + " " + anneeCotisation;
                }
            }
        } else {
            return libelle;
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.04.2003 14:44:36)
     * 
     * @return String
     */
    public String getLocaliteTiers() {
        return _getEnteteFacture(null).getLocaliteTiers();
    }

    public String getMasseDejaFacturee() {
        if (JadeStringUtil.endsWith(masseDejaFacturee, "-")) {
            return masseDejaFacturee;
        }
        return JANumberFormatter.fmt(masseDejaFacturee.toString(), true, false, true, 2);
    }

    public String getMasseFacture() {
        if (JadeStringUtil.endsWith(masseFacture, "-")) {
            return masseFacture;
        }
        return JANumberFormatter.fmt(masseFacture.toString(), true, false, true, 2);
    }

    /**
     * Retourne la masse facture au format Currency.
     *
     * @return globaz.framework.util.FWCurrency
     */
    public FWCurrency getMasseFactureToCurrency() {
        return new FWCurrency(masseFacture);
    }

    public String getMasseInitiale() {
        if (JadeStringUtil.endsWith(masseInitiale, "-")) {
            return masseInitiale;
        }
        return JANumberFormatter.fmt(masseInitiale.toString(), true, false, true, 2);
    }

    public String getMontantDejaFacture() {
        if (JadeStringUtil.endsWith(montantDejaFacture, "-")) {
            return montantDejaFacture;
        }
        return JANumberFormatter.fmt(montantDejaFacture.toString(), true, true, true, 2);
    }

    public String getMontantFacture() {
        if (JadeStringUtil.endsWith(montantFacture, "-")) {
            return montantFacture;
        }
        return JANumberFormatter.fmt(montantFacture.toString(), true, true, false, 2);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.12.2002 16:24:56)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    public FWCurrency getMontantFactureToCurrency() {
        return new FWCurrency(montantFacture);
    }

    public String getMontantInitial() {
        if (JadeStringUtil.endsWith(montantInitial, "-")) {
            return montantInitial;
        }
        return JANumberFormatter.fmt(montantInitial.toString(), true, true, true, 2);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.03.2003 10:44:37)
     * 
     * @return String
     */
    public String getNomTiers() {
        // R�cup�rer le tiers
        ITITiers tiers = this._getTiers();
        if (tiers == null) {
            return "";
        } else {
            return tiers.getDesignation1() + " " + tiers.getDesignation2();
        }
    }

    /**
     * @return
     */
    public String getNumCaisse() {
        return numCaisse;
    }

    /**
     * @return
     */
    public String getNumCaisseOrdre() {
        return numCaisseOrdre;
    }

    public int getOffset() {
        return 0;
    }

    /**
     * @return
     */
    public String getOrdreRegroupement() {
        return ordreRegroupement;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public String getRemarque() {
        return remarque;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (09.12.2002 18:03:25)
     * 
     * @return sAPIRubrique
     * @param transaction
     *            BTransaction
     */
    public APIRubrique getRubrique(BTransaction transaction) {
        // Recharger la rubrique si null ou si nouvelle id
        if (((rubrique == null) || bNewIdExterneRubrique)) {
            try {
                // R�cup�rer l'application
                FAApplication app = (FAApplication) GlobazServer.getCurrentSystem().getApplication(
                        globaz.musca.application.FAApplication.DEFAULT_APPLICATION_MUSCA);
                // R�cup�rer la rubrique � partir du cache
                if (bNewIdExterneRubrique) {
                    rubrique = app.getRubriqueFromCache(transaction, getIdExterneRubrique(), APIRubrique.AK_IDEXTERNE);
                } else {
                    rubrique = app.getRubriqueFromCache(transaction, getIdRubrique(), 0);
                }
                if (rubrique.isNew()) {
                    _addError(transaction, "La rubrique comptable n'a pas pu �tre r�cup�r�e ! (IdEnteteFacture : "
                            + getIdEnteteFacture() + ")");
                    rubrique = null;
                    // Rubrique trouv�e
                } else {
                    bNewIdExterneRubrique = false;
                    idRubrique = rubrique.getIdRubrique();
                    idExterneRubrique = rubrique.getIdExterne();
                    if (JadeStringUtil.isBlank(codeISOLangue)) {
                        libelleRubrique = rubrique.getDescription();
                    } else {
                        libelleRubrique = rubrique.getDescription();
                    }
                }
            } catch (Exception e) {
                _addError(transaction, e.getClass().getName() + " : " + e.getMessage() + " idEnteteFacture : "
                        + getIdEnteteFacture());
                rubrique = null;
            }
        }
        // retourne la rubrique
        return rubrique;
    }

    public String getSaveMontantFacture() {
        return saveMontantFacture;
    }

    public String getTauxDejaFacture() {
        return JANumberFormatter.formatZeroValues(tauxDejaFacture, 2);
    }

    public String getTauxFacture() {
        return JANumberFormatter.formatZeroValues(tauxFacture, 2);
    }

    public String getTauxInitial() {
        return JANumberFormatter.formatZeroValues(tauxInitial, 2);
    }

    /**
     * @return
     */
    public String getTiersDesignation1() {
        return tiersDesignation1;
    }

    /**
     * @return
     */
    public String getTiersDesignation2() {
        return tiersDesignation2;
    }

    public String getTotalFacture() {
        return _getEnteteFacture(null).getTotalFacture();
    }

    public String getTypeCalculInteretMoratoire() {
        return typeCalculInteretMoratoire;
    }

    public String getTypeModule() {
        return typeModule;
    }

    public String getUser() {
        return user;
    }

    /**
     * Y-a-t'il des int�r�ts soumis li�s � l'ent�te.
     * 
     * @return
     */
    public boolean hasInteretSoumis() {
        if (!JadeStringUtil.isBlankOrZero(getIdModuleFacturation())) {
            try {
                FAModuleFacturation module = new FAModuleFacturation();
                module.setSession(getSession());
                module.setIdModuleFacturation(getIdModuleFacturation());
                module.retrieve();
                if (FAModuleFacturation.CS_MODULE_IM_TARDIF.equals(module.getIdTypeModule())
                        || FAModuleFacturation.CS_MODULE_IM_COTARR.equals(module.getIdTypeModule())) {
                    if (!JadeStringUtil.isEmpty(getReferenceExterne())) {
                        CAInteretMoratoireManager manager = new CAInteretMoratoireManager();
                        manager.setSession(getSession());
                        manager.setForMotifCalcul(CAInteretMoratoire.CS_SOUMIS);
                        manager.setForIdJournalFacturation(getIdPassage());
                        manager.setForIdInteretMoratoire(getReferenceExterne());
                        try {
                            manager.find();
                        } catch (Exception e) {
                            _addError(null, "Erreur lors de la r�cup�ration des int�r�ts li�s. ");
                        }
                        return manager.size() > 0;
                    }
                    return false;
                }
            } catch (Exception e) {
                _addError(null, "Erreur lors de la r�cup�ration des int�r�ts li�s. ");
            }
        }
        return false;
    }

    /**
     * Pr�pare les valeurs par d�faut du bean avant un ajout Date de cr�ation : (22.02.2003 15:31:42)
     */
    public void initDefaultValues() throws Exception {
        // Type d'afact
        if (JadeStringUtil.isIntegerEmpty(getIdTypeAfact())) {
            setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
        }
        // Module par d�faut
        if ((getSession() != null) && JadeStringUtil.isIntegerEmpty(getIdModuleFacturation())) {
            setIdModuleFacturation(((FAApplication) getSession().getApplication())
                    .getIdDefaultModuleAfact(getSession()));
        }
    }

    public FAAfactManager initManagerPourContrepartie(FWCurrency montantFacture) throws Exception {
        FAAfactManager afactCompenseMana = new FAAfactManager();
        afactCompenseMana.setSession(getSession());
        afactCompenseMana.setForIdExterneDebiteurCompensation(enteteFacture.getIdExterneRole());
        afactCompenseMana.setForIdExterneFactureCompensation(enteteFacture.getIdExterneFacture());
        afactCompenseMana.setForIdPassage(getIdPassage());
        afactCompenseMana.setForIdTypeAfact(FAAfact.CS_AFACT_COMPENSATION_INTERNE);
        afactCompenseMana.setForMontant(montantFacture.toString());
        afactCompenseMana.find();
        return afactCompenseMana;
    }

    public Boolean isAQuittancer() {
        return aQuittancer;
    }

    /**
     * Returns the controleDeuxFrancs.
     * 
     * @return boolean
     */
    public boolean isControleDeuxFrancs() {
        return controleDeuxFrancs;
    }

    public boolean isCotiMasseAZero() {
        return cotiMasseAZero;
    }

    /**
     * V�rifier si la propertie libelleCompensationTropLong est active.
     */
    public boolean isLibelleCompTropLong(BSession session) {
        String libelleCompTropLong = "";
        try {
            libelleCompTropLong = session.getApplication().getProperty("libelleCompesationTropLong");
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        if (!JadeStringUtil.isBlank(libelleCompTropLong)) {
            return Boolean.valueOf(libelleCompTropLong).booleanValue();
        } else {
            return false;
        }
    }

    public boolean isNewFacturation() {
        return newFacturation;
    }

    /**
     * Returns the noCheckPlausiRubriqueMasse.
     * 
     * @return boolean
     */
    public boolean isNoCheckPlausiRubriqueMasse() {
        return noCheckPlausiRubriqueMasse;
    }

    public Boolean isNonComptabilisable() {
        return nonComptabilisable;
    }

    @Override
    public Boolean isNonImprimable() {
        return nonImprimable;
    }

    public void miseAjourTotalEntete(BTransaction transaction, FAAfact afactCompense) throws Exception {
        FAEnteteFacture enteteCompense = new FAEnteteFacture();
        enteteCompense.setSession(getSession());
        enteteCompense.setIdEntete(afactCompense.getIdEnteteFacture());
        enteteCompense.retrieve();
        enteteCompense.updateTotal(transaction, afactCompense);
    }

    public void rechercheIdTiersPourAffilie(BTransaction transaction) throws Exception {
        if (!JadeStringUtil.isBlank(getIdExterneDebiteurCompensation())) {
            // formatage du numero selon caisse
            try {
                TIApplication app = (TIApplication) GlobazSystem.getApplication("PYXIS");
                IFormatData affilieFormater = app.getAffileFormater();
                if (affilieFormater != null) {
                    setIdExterneDebiteurCompensation(affilieFormater.format(getIdExterneDebiteurCompensation()));
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
            // Recherche des affiliation pour ce num�ro
            AFAffiliationManager affiManager = new AFAffiliationManager();
            affiManager.setSession(getSession());
            affiManager.setForAffilieNumero(getIdExterneDebiteurCompensation());
            if (!JadeStringUtil.isBlank(getIdTiersDebiteurCompensation())) {
                // l'id tiers est r�cup�r� seulement quand on utilise la
                // PopupList
                affiManager.setForIdTiers(getIdTiersDebiteurCompensation());
            }
            affiManager.find();
            if (affiManager.size() == 0) {
                if (!JadeStringUtil.isBlank(getIdTiersDebiteurCompensation())) {
                    // Si pas trouver avec l'idTiers et numAffili� on recherche
                    // seulement avec l'idTiers
                    setIdTiersDebiteurCompensation("");
                    affiManager.setForIdTiers("");
                    affiManager.setForAffilieNumero(getIdExterneDebiteurCompensation());
                    affiManager.find(transaction);
                }
            } else if (affiManager.size() == 1) {
                setIdTiersDebiteurCompensation(((AFAffiliation) affiManager.getFirstEntity()).getIdTiers());
            } else {
                // Si l'id tiers n'a pas �t� renseign�, il se peut qu'un m�me
                // num�ro d'affili� soit attribu� � 2 tiers diff�rents
                if (JadeStringUtil.isBlank(getIdTiersDebiteurCompensation())) {
                    // Enregistrement du premier id tiers trouv� dans une
                    // variable d'aide
                    String premierIdTiers = ((AFAffiliation) affiManager.getFirstEntity()).getIdTiers();
                    boolean trouveAutreIdTiers = false;
                    int i = 0;
                    while ((i < affiManager.size()) && !trouveAutreIdTiers) {
                        AFAffiliation affiliation = (AFAffiliation) affiManager.getEntity(i);
                        if (!affiliation.getIdTiers().equals(premierIdTiers)) {
                            trouveAutreIdTiers = true;
                        }
                        i++;
                    }
                    if (trouveAutreIdTiers) {
                        _addError(transaction,
                                "Plusieurs tiers ont ce num�ro d'affili�. Veuillez s�lectionner un tiers dans la liste propos�e.");
                    } else {
                        setIdTiersDebiteurCompensation(premierIdTiers);
                    }
                }
            }

        }
    }

    public void setAffichtaux(Boolean affichtaux) {
        this.affichtaux = affichtaux;
    }

    public void setAnneeCotisation(String newAnneeCotisation) {
        anneeCotisation = JANumberFormatter.deQuote(newAnneeCotisation);
    }

    public void setAQuittancer(Boolean newAQuittancer) {
        aQuittancer = newAQuittancer;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setCheckIdExterneFactureCompensation(Boolean checkIdExterneFactureCompensation) {
        this.checkIdExterneFactureCompensation = checkIdExterneFactureCompensation;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.05.2003 18:18:55)
     * 
     * @param newCodeISOLangue
     *            int
     */
    public void setCodeISOLangue(String newCodeISOLangue) {
        codeISOLangue = newCodeISOLangue;
    }

    /**
     * Sets the controleDeuxFrancs.
     * 
     * @param controleDeuxFrancs
     *            The controleDeuxFrancs to set
     */
    public void setControleDeuxFrancs(boolean controleDeuxFrancs) {
        this.controleDeuxFrancs = controleDeuxFrancs;
    }

    public void setCotiMasseAZero(boolean cotiMasseAZero) {
        this.cotiMasseAZero = cotiMasseAZero;
    }

    /**
     * @param string
     */
    public void setDateValeur(String string) {
        dateValeur = string;
    }

    public void setDebutPeriode(String newDebutPeriode) {
        debutPeriode = newDebutPeriode;
    }

    /**
     * @param bean
     */
    public void setEnteteFacture(FAEnteteFactureViewBean bean) {
        enteteFacture = bean;
    }

    public void setFinPeriode(String newFinPeriode) {
        finPeriode = newFinPeriode;
    }

    /**
     * Sets the forceDelete.
     * 
     * @param forceDelete
     *            The forceDelete to set
     */
    public void setForceDelete(Boolean forceDelete) {
        this.forceDelete = forceDelete;
    }

    /**
     * Setter
     */
    public void setIdAfact(String newIdAfact) {
        idAfact = newIdAfact;
    }

    public void setIdAssurance(String idAssurance) {
        this.idAssurance = idAssurance;
    }

    public void setIdEnteteFacture(String newIdEnteteFacture) {
        idEnteteFacture = newIdEnteteFacture;
        // RAZ si diff�rent
        if ((enteteFacture != null) && !enteteFacture.getIdEntete().equals(newIdEnteteFacture)) {
            enteteFacture = null;
        }
    }

    public void setIdExterneDebiteurCompensation(String newIdExterneDebiteurCompensation) {
        idExterneDebiteurCompensation = newIdExterneDebiteurCompensation;
    }

    public void setIdExterneFactureCompensation(String newIdExterneFactureCompensation) {
        idExterneFactureCompensation = newIdExterneFactureCompensation;
    }

    /**
     * @param string
     */
    public void setIdExterneRole(String string) {
        idExterneRole = string;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (09.12.2002 17:48:07)
     * 
     * @param newIdExterneRubrique
     *            String
     */
    public void setIdExterneRubrique(String newIdExterneRubrique) {
        if (!idExterneRubrique.equals(newIdExterneRubrique)) {
            bNewIdExterneRubrique = true;
        }
        idExterneRubrique = newIdExterneRubrique;
    }

    public void setIdModuleFacturation(String newIdModuleFacturation) {
        idModuleFacturation = newIdModuleFacturation;
    }

    public void setIdNumCaisse(String string) {
        if (string.equals("") || string.equals(null)) {
            setNumCaisse("");
        } else {
            TIAdministrationManager adminManager = new TIAdministrationManager();
            adminManager.setSession(getSession());
            adminManager.setForCodeAdministration(string);
            adminManager.setForGenreAdministration(FAAfact.CAISSE_PROF);
            try {
                adminManager.find();
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
            if (adminManager.size() > 0) {
                setNumCaisse(((TIAdministrationViewBean) adminManager.getFirstEntity()).getIdTiersAdministration());
            } else {
                setNumCaisse("0");
            }
        }

    }

    /**
     * @param string
     */
    public void setIdOrdreRegroupement(String string) {
        idOrdreRegroupement = string;
    }

    /**
     * Setter
     */
    public void setIdPassage(String newIdPassage) {
        idPassage = newIdPassage;
    }

    public void setIdRemarque(String newIdRemarque) {
        idRemarque = newIdRemarque;
    }

    /**
     * @param string
     */
    public void setIdRole(String string) {
        idRole = string;
    }

    public void setIdRoleDebiteurCompensation(String newIdRoleDebiteurCompensation) {
        idRoleDebiteurCompensation = newIdRoleDebiteurCompensation;
    }

    public void setIdRubrique(String newIdRubrique) {
        idRubrique = newIdRubrique;
        rubrique = null;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdTiersDebiteurCompensation(String newIdTiersDebiteurCompensation) {
        idTiersDebiteurCompensation = newIdTiersDebiteurCompensation;
        // RAZ cache si id ne correspond pas
        if ((tiers != null) && !tiers.getIdTiers().equals(newIdTiersDebiteurCompensation)) {
            tiers = null;
        }
    }

    public void setIdTypeAfact(String newIdTypeAfact) {
        idTypeAfact = newIdTypeAfact;
    }

    public void setIdTypeFactureCompensation(String newIdTypeFactureCompensation) {
        idTypeFactureCompensation = newIdTypeFactureCompensation;
    }

    public void setLibelle(String newLibelle) {
        libelle = newLibelle;
    }

    /**
     * @param string
     */
    public void setLibelleOrdreDe(String string) {
        libelleOrdreDe = string;
    }

    /**
     * @param string
     */
    public void setLibelleOrdreFr(String string) {
        libelleOrdreFr = string;
    }

    /**
     * @param string
     */
    public void setLibelleOrdreIt(String string) {
        libelleOrdreIt = string;
    }

    public void setMasseDejaFacturee(String newMasseDejaFacturee) {
        masseDejaFacturee = JANumberFormatter.deQuote(newMasseDejaFacturee);
    }

    public void setMasseFacture(String newMasseFacture) {
        masseFacture = JANumberFormatter.deQuote(newMasseFacture);
    }

    public void setMasseInitiale(String newMasseInitiale) {
        masseInitiale = JANumberFormatter.deQuote(newMasseInitiale);
    }

    public void setMontantDejaFacture(String newMontantDejaFacture) {
        montantDejaFacture = JANumberFormatter.deQuote(newMontantDejaFacture);
    }

    public void setMontantFacture(String newMontantFacture) {
        montantFacture = JANumberFormatter.deQuote(newMontantFacture);
    }

    public void setMontantInitial(String newMontantInitial) {
        montantInitial = JANumberFormatter.deQuote(newMontantInitial);
    }

    public void setNewFacturation(boolean newFacturation) {
        this.newFacturation = newFacturation;
    }

    /**
     * Sets the noCheckPlausiRubriqueMasse.
     * 
     * @param noCheckPlausiRubriqueMasse
     *            The noCheckPlausiRubriqueMasse to set
     */
    public void setNoCheckPlausiRubriqueMasse(boolean noCheckPlausiRubriqueMasse) {
        this.noCheckPlausiRubriqueMasse = noCheckPlausiRubriqueMasse;
    }

    public void setNonComptabilisable(Boolean newNonComptabilisable) {
        nonComptabilisable = newNonComptabilisable;
    }

    public void setNonImprimable(Boolean newNonImprimable) {
        nonImprimable = newNonImprimable;
    }

    /**
     * @param string
     */
    public void setNumCaisse(String string) {
        numCaisse = string;
    }

    /**
     * @param string
     */
    public void setNumCaisseOrdre(String string) {
        numCaisseOrdre = string;
    }

    /**
     * @param string
     */
    public void setOrdreRegroupement(String string) {
        ordreRegroupement = string;
    }

    public void setReferenceExterne(String newReferenceExterne) {
        referenceExterne = newReferenceExterne;
    }

    public void setRemarque(String newRemarque) {
        remarque = newRemarque;
    }

    public void setSaveMontantFacture(String saveMontantFacture) {
        this.saveMontantFacture = saveMontantFacture;
    }

    public void setTauxDejaFacture(String newTauxDejaFacture) {
        tauxDejaFacture = JANumberFormatter.deQuote(newTauxDejaFacture);
    }

    public void setTauxFacture(String newTauxFacture) {
        tauxFacture = JANumberFormatter.deQuote(newTauxFacture);
    }

    public void setTauxInitial(String newTauxInitial) {
        tauxInitial = JANumberFormatter.deQuote(newTauxInitial);
    }

    /**
     * @param string
     */
    public void setTiersDesignation1(String string) {
        tiersDesignation1 = string;
    }

    /**
     * @param string
     */
    public void setTiersDesignation2(String string) {
        tiersDesignation2 = string;
    }

    public void setTypeCalculInteretMoratoire(String typeCalculInteretMoratoire) {
        this.typeCalculInteretMoratoire = typeCalculInteretMoratoire;
    }

    public void setTypeModule(String typeModule) {
        this.typeModule = typeModule;
    }

    public void setUser(String newUser) {
        user = newUser;
    }

    public int size() {
        return 0;
    }

    public String toMyString() {
        return super.toString() + ": idAfact=" + getIdAfact() + ", idEnteteFacture=" + getIdEnteteFacture()
                + ", idPassage=" + getIdPassage();
    }

    protected void validerAfactTableau(BStatement statement, APIRubrique _rub) {
        if (_rub.getNatureRubrique().equalsIgnoreCase(APIRubrique.COTISATION_AVEC_MASSE)) {
            if (!globaz.jade.client.util.JadeStringUtil.isBlank(montantInitial)) {
                BigDecimal masse = new BigDecimal(masseInitiale);
                BigDecimal taux = new BigDecimal(tauxInitial);
                BigDecimal montant = new BigDecimal(montantInitial);
                BigDecimal montant1 = montant.subtract(new BigDecimal("2"));
                BigDecimal montant2 = montant.add(new BigDecimal("2"));
                BigDecimal calcul = masse.multiply(taux);
                calcul = calcul.divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_DOWN);
                if ((calcul.compareTo(montant1) < 0) || (calcul.compareTo(montant2) > 0)) {
                    _addError(statement.getTransaction(),
                            "Le montant de base n'est pas �gal � +/- 2Fr. � la masse de base * le taux. ");
                }
            }
            if (!globaz.jade.client.util.JadeStringUtil.isBlank(montantDejaFacture)) {
                BigDecimal masse = new BigDecimal(masseDejaFacturee);
                BigDecimal taux = new BigDecimal(tauxDejaFacture);
                BigDecimal montant = new BigDecimal(montantDejaFacture);
                BigDecimal montant1 = montant.subtract(new BigDecimal("2"));
                BigDecimal montant2 = montant.add(new BigDecimal("2"));
                BigDecimal calcul = masse.multiply(taux);
                calcul = calcul.divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_DOWN);
                if ((calcul.compareTo(montant1) < 0) || (calcul.compareTo(montant2) > 0)) {
                    _addError(statement.getTransaction(),
                            "Le montant d�j� factur� n'est pas �gal � +/- 2Fr. � la masse d�j� factur�e * le taux. ");
                }
            }
        }
    }

    protected APIRubrique validerRubrique(BStatement statement, FWCurrency cMontantFacture, FWCurrency cMasseFacture,
            BigDecimal bdTauxFacture) throws Exception {
        APIRubrique _rub = getRubrique(statement.getTransaction());
        if ((_rub == null) || _rub.isNew()) {
            _addError(statement.getTransaction(), "Rubrique inconnue " + getIdExterneRubrique());
        } else if (!APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(_getEnteteFacture(statement.getTransaction())
                .getIdTypeFacture())) {
            if (_rub.getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_CREANCIER)
                    || _rub.getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_DEBITEUR)) {
                _addError(
                        statement.getTransaction(),
                        "La rubrique saisie ne peut pas �tre de type compte cr�ancier ou compte d�biteur."
                                + _rub.getIdExterne());
            }
            if (getIdTypeAfact().equals(FAAfact.CS_AFACT_COMPENSATION)
                    || getIdTypeAfact().equals(FAAfact.CS_AFACT_COMPENSATION_INTERNE)) {
                if (!_rub.getNatureRubrique().equalsIgnoreCase(APIRubrique.COMPTE_COMPENSATION)) {
                    _addError(statement.getTransaction(), "La rubrique saisie n'est pas une rubrique de compensation. "
                            + _rub.getIdExterne());
                }
            } else {
                if (_getEnteteFacture(statement.getTransaction()).getIdSousType().equals(
                        APISection.ID_CATEGORIE_SECTION_DECOMPTE_FINAL)
                        || _getEnteteFacture(statement.getTransaction()).getIdSousType().equals(
                                APISection.ID_CATEGORIE_SECTION_BOUCLEMENT_ACOMPTE)
                        || _getEnteteFacture(statement.getTransaction()).getIdSousType().equals(
                                APISection.ID_CATEGORIE_SECTION_LTN)) {
                    cotiMasseAZero = true;
                }
                // Ann�e de cotisation obligatoire si rubrique de type
                // COTISATION_AVEC_MASSE
                // ou COTISATION_SANS_MASSE sinon vide
                if (_rub.getNatureRubrique().equalsIgnoreCase(APIRubrique.COTISATION_AVEC_MASSE)) {
                    validerRubriqueAvecMasse(statement, _rub);
                } else if (_rub.getNatureRubrique().equalsIgnoreCase(APIRubrique.COTISATION_SANS_MASSE)) {
                    validerRubriqueSansMasse(statement, _rub);
                } else {
                    // Si type de rubrique diff�rent de
                    // COTISATION_AVEC_MASSE ou COTISATION_SANS_MASSE
                    validerRubriqueAutre(statement, _rub);
                }
                if (_rub.getNatureRubrique().equalsIgnoreCase(APIRubrique.COMPTE_COMPENSATION)) {
                    _addError(
                            statement.getTransaction(),
                            "Vous ne pouvez pas saisir un compte de compensation avec ce type d'afact."
                                    + _rub.getIdExterne());
                }
            }

            // Masse obligatoire
            if (!noCheckPlausiRubriqueMasse && !cotiMasseAZero) {
                if (_rub.getNatureRubrique().equalsIgnoreCase(APIRubrique.COTISATION_AVEC_MASSE)) {
                    if (cMasseFacture.isZero()
                            && ((cMontantFacture.doubleValue() > 1.00) || (cMontantFacture.doubleValue() < -1.00))) {
                        _addError(statement.getTransaction(),
                                "La masse est obligatoire avec cette rubrique" + _rub.getIdExterne());
                    }
                }
            }
            // Calculer automatiquement le montant sur la base de la masse
            // et du taux si n�cessaire
            // Ne pas faire ce bout de code pour les afacts en tableau qui
            // viennent des DS et des Relev�s.
            if ("true".equals(getSession().getApplication().getProperty("recalculerMontantFacture"))
                    || (!newFacturation && (!getIdModuleFacturation().equals(
                            ServicesFacturation.getIdModFacturationByType(getSession(), statement.getTransaction(),
                                    FAModuleFacturation.CS_MODULE_DECLARATION_SALAIRE)) && !getIdModuleFacturation()
                            .equals(ServicesFacturation.getIdModFacturationByType(getSession(),
                                    statement.getTransaction(), FAModuleFacturation.CS_MODULE_RELEVE))))
                    || (newFacturation && ((getTypeModule().equals(FAModuleFacturation.CS_MODULE_DECLARATION_SALAIRE) == false) && (getIdModuleFacturation()
                            .equals(FAModuleFacturation.CS_MODULE_RELEVE) == false)))) {
                if (cMontantFacture.isZero() && !cMasseFacture.isZero() && (bdTauxFacture.signum() != 0)) {
                    double dMasse = (cMasseFacture.doubleValue() * bdTauxFacture.doubleValue()) / 100;
                    setMontantFacture(JANumberFormatter.formatNoQuote(dMasse));
                    cMontantFacture = new FWCurrency(getMontantFacture());
                    FWMemoryLog journalLog = new FWMemoryLog();
                    journalLog.logMessage("Recalcul tableau utilis� : idAfact " + getIdAfact() + " idEntete "
                            + getIdEnteteFacture(), FWMessage.INFORMATION, "Ajout d'un afact");
                }
            }

            // Montant obligatoire pour type autre que cotisation
            if (noCheckPlausiRubriqueMasse) {
                if (!_rub.getNatureRubrique().equalsIgnoreCase(APIRubrique.COTISATION_AVEC_MASSE)
                        && !_rub.getNatureRubrique().equalsIgnoreCase(APIRubrique.COTISATION_SANS_MASSE)) {
                    if (cMontantFacture.isZero()) {
                        _addError(statement.getTransaction(),
                                "La montant est obligatoire avec cette rubrique" + _rub.getIdExterne());
                    }
                }
            }
        }
        return _rub;
    }

    protected void validerRubriqueAutre(BStatement statement, APIRubrique _rub) {
        if (!JadeStringUtil.isIntegerEmpty(anneeCotisation)) {
            _addError(statement.getTransaction(), "L'ann�e de cotisation doit �tre vide avec ce type de rubrique. "
                    + _rub.getIdExterne());
        }
        if (!JadeStringUtil.isDecimalEmpty(masseFacture) || !JadeStringUtil.isDecimalEmpty(masseInitiale)) {
            _addError(statement.getTransaction(),
                    "La masse ne doit pas �tre saisie avec ce type de rubrique. " + _rub.getIdExterne());
        }
        if (!JadeStringUtil.isIntegerEmpty(anneeCotisation)) {
            _addError(statement.getTransaction(), "L'ann�e de cotisation doit �tre vide avec ce type de rubrique. "
                    + _rub.getIdExterne());
        }
    }

    protected void validerRubriqueAvecMasse(BStatement statement, APIRubrique _rub) {
        if (JadeStringUtil.isIntegerEmpty(anneeCotisation)) {
            _addError(statement.getTransaction(), "L'ann�e de cotisation est obligatoire avec ce type de rubrique. "
                    + _rub.getIdExterne());
        }
        if (JadeStringUtil.isDecimalEmpty(tauxFacture) && JadeStringUtil.isDecimalEmpty(tauxInitial)) {
            _addError(statement.getTransaction(), "Le taux de cotisation est obligatoire avec ce type de rubrique. "
                    + _rub.getIdExterne());
        }
        if (!cotiMasseAZero) {
            if (JadeStringUtil.isDecimalEmpty(masseFacture) && JadeStringUtil.isDecimalEmpty(masseInitiale)) {
                _addError(statement.getTransaction(),
                        "La masse est obligatoire avec ce type de rubrique. " + _rub.getIdExterne());
            }
        }
    }

    protected void validerRubriqueSansMasse(BStatement statement, APIRubrique _rub) {
        if (!JadeStringUtil.isDecimalEmpty(tauxFacture) || !JadeStringUtil.isDecimalEmpty(tauxInitial)) {
            _addError(statement.getTransaction(),
                    "Le taux de cotisation ne doit pas �tre saisie avec ce type de rubrique. " + _rub.getIdExterne());
        }
        if (!JadeStringUtil.isDecimalEmpty(masseFacture) || !JadeStringUtil.isDecimalEmpty(masseInitiale)) {
            _addError(statement.getTransaction(),
                    "La masse ne doit pas �tre saisie avec ce type de rubrique. " + _rub.getIdExterne());
        }
        if (JadeStringUtil.isIntegerEmpty(anneeCotisation)) {
            _addError(statement.getTransaction(), "L'ann�e de cotisation est obligatoire avec ce type de rubrique. "
                    + _rub.getIdExterne());
        }
        if (JadeStringUtil.isDecimalEmpty(montantInitial) && JadeStringUtil.isDecimalEmpty(montantFacture)) {
            _addError(statement.getTransaction(),
                    "Le montant est obligatoire avec ce type de rubrique. " + _rub.getIdExterne());
        }
    }

}
