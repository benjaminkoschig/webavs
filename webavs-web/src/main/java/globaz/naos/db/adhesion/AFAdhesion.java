/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.adhesion;

import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.helper.IAFAdhesionHelper;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationListViewBean;
import globaz.naos.db.couverture.AFCouverture;
import globaz.naos.db.couverture.AFCouvertureListViewBean;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliationListViewBean;
import globaz.naos.db.planAffiliation.AFPlanAffiliationManager;
import globaz.naos.db.planCaisse.AFPlanCaisse;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TITiers;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * La classe définissant l'entité Adhésion.
 * 
 * @author sau
 */
public class AFAdhesion extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ADHESION_ID = "MRIADH";
    public static final String FIELDNAME_AFFILIATION_ID = "MAIAFF";
    public static final String FIELDNAME_PLANCAISSE_ID = "MSIPLC";
    public static final String FIELDNAME_TIERS_ID = "HTITIE";
    public static final String TABLE_NAME = "AFADHEP";
    private TIAdministrationViewBean _administrationAssocia = null;
    private TIAdministrationViewBean _administrationCaisse = null;

    // private FWParametersSystemCode csTypeAdhesion = null;

    private AFAffiliation _affiliation = null;
    private AFPlanCaisse _planCaisse = null;
    private TITiers _tiers = null;
    private boolean addOnlyAdhesion = false;
    // DB Table AFADHEP
    // Primary Key
    private java.lang.String adhesionId = new String();

    // Foreign Key
    private java.lang.String affiliationId = new String();

    private boolean allowChildDelete = false;

    private java.lang.String dateDebut = new String();
    private java.lang.String dateFin = new String();

    private java.lang.String idTiers = new String();
    private AFAdhesion oldAdhesion = null;
    private String planAffiliationId;
    // Fields
    private java.lang.String planCaisseId = new String();
    private java.lang.String typeAdhesion = new String();

    /**
     * Constructeur d'AFAdhesion.
     */
    public AFAdhesion() {
        super();
        setMethodsToLoad(IAFAdhesionHelper.METHODS_TO_LOAD);
    }

    /**
     * Effectue des traitements après une mise à jour dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_afterUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {

        if (getTypeAdhesion().equals(CodeSystem.TYPE_ADHESION_CAISSE)
                || getTypeAdhesion().equals(CodeSystem.TYPE_ADHESION_CAISSE_PRINCIPALE)) {
            // *********************************************************
            // Cotisation Caisse Professionnel
            // Modification Date de début, Date de fin
            // Update et Delete
            // *********************************************************

            // recherche des cotisations
            AFCotisationListViewBean cotisationList = new AFCotisationListViewBean();
            cotisationList.setSession(transaction.getSession());
            cotisationList.setForAdhesionId(getAdhesionId());
            cotisationList.setOrder(_getCollection() + "AFCOTIP.MBIASS, MEDDEB DESC");
            cotisationList.find(transaction);

            // recherche de l'affiliation
            AFAffiliation affiliation = getAffiliation();
            for (int i = 0; i < cotisationList.size(); i++) {
                AFCotisation cotisation = (AFCotisation) cotisationList.getEntity(i);

                AFCouverture couverture = cotisation.getCouverture();
                // BZ 9035
                if (couverture == null) {
                    _addError(transaction, getSession().getLabel("IDPLAN_IDASSURANCE") + " (id cotisation: "
                            + cotisation.getCotisationId() + " - " + cotisation.getAssurance().getAssuranceLibelle()
                            + ")");
                }

                boolean deleteCotisation = false;
                boolean updateCotisation = false;
                boolean setCotisationInactive = false;

                // variable de maj
                String dateDebutToUpd = null;
                String dateFinToUpd = null;
                String motifFinUpd = null;

                if (JadeStringUtil.isIntegerEmpty(getDateFin())
                        && JadeStringUtil.isIntegerEmpty(couverture.getDateFin())) {

                    // if (!CodeSystem.MOTIF_FIN_EXCEPTION.equals(cotisation.getMotifFin())) {
                    if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateDebut(),
                            couverture.getDateDebut())) {
                        dateDebutToUpd = getData(getDateDebut(), cotisation.getDateDebut(), oldAdhesion.getDateDebut(),
                                cotisation.getMotifFin());
                    } else {
                        dateDebutToUpd = getData(couverture.getDateDebut(), cotisation.getDateDebut(),
                                oldAdhesion.getDateDebut(), cotisation.getMotifFin());
                    }
                    if (cotisation.getInactive().booleanValue() == true) {
                        dateFinToUpd = getDataCotisationInactive(cotisation.getDateDebut(), oldAdhesion.getDateFin());
                        cotisation.setInactive(new Boolean(false));
                    } else {
                        dateFinToUpd = getData("", cotisation.getDateFin(), oldAdhesion.getDateFin(),
                                cotisation.getMotifFin());
                    }
                    motifFinUpd = CodeSystem.MOTIF_FIN_AUCUN;
                    // }
                } else if (!JadeStringUtil.isIntegerEmpty(getDateFin())
                        && !JadeStringUtil.isIntegerEmpty(couverture.getDateFin())) {
                    if (BSessionUtil.compareDateFirstLower(getSession(), couverture.getDateDebut(), getDateDebut())
                            && BSessionUtil.compareDateBetweenOrEqual(getSession(), getDateDebut(), getDateFin(),
                                    couverture.getDateFin())) {
                        dateDebutToUpd = getData(getDateDebut(), cotisation.getDateDebut(), oldAdhesion.getDateDebut(),
                                cotisation.getMotifFin());
                        dateFinToUpd = getData(couverture.getDateFin(), cotisation.getDateFin(),
                                oldAdhesion.getDateFin(), cotisation.getMotifFin());
                        motifFinUpd = CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE;

                    } else if (BSessionUtil.compareDateBetweenOrEqual(getSession(), getDateDebut(), getDateFin(),
                            couverture.getDateDebut())
                            && BSessionUtil.compareDateBetweenOrEqual(getSession(), getDateDebut(), getDateFin(),
                                    couverture.getDateFin())) {
                        dateDebutToUpd = getData(couverture.getDateDebut(), cotisation.getDateDebut(),
                                oldAdhesion.getDateDebut(), cotisation.getMotifFin());
                        dateFinToUpd = getData(couverture.getDateFin(), cotisation.getDateFin(),
                                oldAdhesion.getDateFin(), cotisation.getMotifFin());
                        motifFinUpd = CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE;
                    } else if (BSessionUtil.compareDateBetweenOrEqual(getSession(), getDateDebut(), getDateFin(),
                            couverture.getDateDebut())
                            && BSessionUtil.compareDateFirstLower(getSession(), getDateFin(), couverture.getDateFin())) {
                        dateDebutToUpd = getData(couverture.getDateDebut(), cotisation.getDateDebut(),
                                oldAdhesion.getDateDebut(), cotisation.getMotifFin());
                        dateFinToUpd = getData(getDateFin(), cotisation.getDateFin(), oldAdhesion.getDateFin(),
                                cotisation.getMotifFin());
                        motifFinUpd = affiliation.getMotifFin();
                    } else if (BSessionUtil.compareDateFirstLower(getSession(), couverture.getDateDebut(),
                            getDateDebut())
                            && BSessionUtil
                                    .compareDateFirstGreater(getSession(), couverture.getDateFin(), getDateFin())) {
                        dateDebutToUpd = getData(getDateDebut(), cotisation.getDateDebut(), oldAdhesion.getDateDebut(),
                                cotisation.getMotifFin());
                        dateFinToUpd = getData(getDateFin(), cotisation.getDateFin(), oldAdhesion.getDateFin(),
                                cotisation.getMotifFin());
                        motifFinUpd = affiliation.getMotifFin();
                    } else {
                        deleteCotisation = true;
                    }

                } else if (JadeStringUtil.isIntegerEmpty(getDateFin())
                        && !JadeStringUtil.isIntegerEmpty(couverture.getDateFin())) {

                    if (BSessionUtil.compareDateFirstLower(getSession(), couverture.getDateDebut(), getDateDebut())
                            && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(),
                                    couverture.getDateFin())) {
                        dateDebutToUpd = getData(getDateDebut(), cotisation.getDateDebut(), oldAdhesion.getDateDebut(),
                                cotisation.getMotifFin());
                        dateFinToUpd = getData(couverture.getDateFin(), cotisation.getDateFin(),
                                oldAdhesion.getDateFin(), cotisation.getMotifFin());
                        motifFinUpd = CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE;
                    } else if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(),
                            couverture.getDateDebut())
                            && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(),
                                    couverture.getDateFin())) {
                        dateDebutToUpd = getData(couverture.getDateDebut(), cotisation.getDateDebut(),
                                oldAdhesion.getDateDebut(), cotisation.getMotifFin());
                        dateFinToUpd = getData(couverture.getDateFin(), cotisation.getDateFin(),
                                oldAdhesion.getDateFin(), cotisation.getMotifFin());
                        motifFinUpd = CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE;
                    } else {
                        deleteCotisation = true;
                    }
                } else {
                    if (BSessionUtil.compareDateFirstLower(getSession(), couverture.getDateDebut(), getDateDebut())) {
                        dateDebutToUpd = getData(getDateDebut(), cotisation.getDateDebut(), oldAdhesion.getDateDebut(),
                                cotisation.getMotifFin());
                        dateFinToUpd = getData(getDateFin(), cotisation.getDateFin(), oldAdhesion.getDateFin(),
                                cotisation.getMotifFin());
                        if (!JadeStringUtil.isIntegerEmpty(affiliation.getDateFin())
                                && BSessionUtil.compareDateEqual(getSession(), affiliation.getDateFin(), getDateFin())) {
                            motifFinUpd = affiliation.getMotifFin();
                        } else {
                            motifFinUpd = CodeSystem.MOTIF_FIN_FIN_ADHESION;
                        }

                    } else if (BSessionUtil.compareDateBetweenOrEqual(getSession(), getDateDebut(), getDateFin(),
                            couverture.getDateDebut())) {
                        dateDebutToUpd = getData(couverture.getDateDebut(), cotisation.getDateDebut(),
                                oldAdhesion.getDateDebut(), cotisation.getMotifFin());
                        dateFinToUpd = getData(getDateFin(), cotisation.getDateFin(), oldAdhesion.getDateFin(),
                                cotisation.getMotifFin());
                        if (!JadeStringUtil.isIntegerEmpty(affiliation.getDateFin())
                                && BSessionUtil.compareDateEqual(getSession(), affiliation.getDateFin(), getDateFin())) {
                            motifFinUpd = affiliation.getMotifFin();
                        } else {
                            motifFinUpd = CodeSystem.MOTIF_FIN_FIN_ADHESION;
                        }
                    } else {
                        setCotisationInactive = true;

                    }
                }

                if (JadeDateUtil.isDateBefore(getDateFin(), cotisation.getDateDebut())) {
                    setCotisationInactive = true;
                }

                if (setCotisationInactive) {
                    cotisation.setDateFin(cotisation.getDateDebut());
                    // PO 3466 HNA: Mérite un refactoring profond, update de classe appelée dans tous les sens ->
                    // maintenance périlleuse.
                    // on garde le motif exception
                    if (!CodeSystem.MOTIF_FIN_EXCEPTION.equals(cotisation.getMotifFin())) {
                        cotisation.setMotifFin(affiliation.getMotifFin());
                    }
                    cotisation.setInactive(new Boolean(true));
                    dateDebutToUpd = null;
                    dateFinToUpd = null;
                    updateCotisation = true;
                }

                if (deleteCotisation) {
                    if (cotisation._allowDelete()) {
                        cotisation.delete(transaction);
                    } else {
                        cotisation.setDateFin(cotisation.getDateDebut());
                        // on garde le motif exception
                        if (!CodeSystem.MOTIF_FIN_EXCEPTION.equals(cotisation.getMotifFin())) {
                            cotisation.setMotifFin(affiliation.getMotifFin());
                        }
                        cotisation.setInactive(new Boolean(true));
                        dateDebutToUpd = null;
                        dateFinToUpd = null;
                        cotisation.setMiseAjourDepuisEcran(Boolean.FALSE);
                        cotisation.update(transaction);
                    }

                } else {
                    if (dateDebutToUpd != null) {
                        // Mise à jour date de début si la nouvelle date est supérieur => évite ainsi d'avoir des débuts
                        // < à la date de début de l'affiliation
                        if (BSessionUtil.compareDateBetween(getSession(), cotisation.getDateDebut(),
                                cotisation.getDateFin(), dateDebutToUpd)
                                || JadeStringUtil.isEmpty(cotisation.getDateFin())) {
                            cotisation.setDateDebut(dateDebutToUpd);
                            updateCotisation = true;
                        }
                    }
                    if (dateFinToUpd != null) {
                        // PO 3466 : Mise à jour de la date de fin des exceptions uniquement si la date de fin est
                        // inférieur à celle de l'exeption
                        // Evite ainsi d'avoir des dates d'exception > à l'affiliation
                        if (!CodeSystem.MOTIF_FIN_EXCEPTION.equals(cotisation.getMotifFin())
                                || (!JadeStringUtil.isBlankOrZero(dateFinToUpd) && BSessionUtil.compareDateFirstLower(
                                        getSession(), dateFinToUpd, cotisation.getDateFin()))) {
                            cotisation.setDateFin(dateFinToUpd);
                            // on garde le motif exception
                            if (!CodeSystem.MOTIF_FIN_EXCEPTION.equals(cotisation.getMotifFin())) {
                                cotisation.setMotifFin(motifFinUpd);
                            }
                            updateCotisation = true;
                        }
                    }
                    if (updateCotisation) {
                        cotisation.setMiseAjourDepuisEcran(Boolean.FALSE); // nécessaire pour IDE car l'affiliation met
                        // à jour les coti... qui met à jour
                        // l'affiliation pour les dates d'annonce
                        cotisation.update(transaction);
                    }
                }
            }
        }
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // Incrémente de +1 le numéro
        setAdhesionId(this._incCounter(transaction, "0"));

        if (getTypeAdhesion().equals(CodeSystem.TYPE_ADHESION_CAISSE)
                || getTypeAdhesion().equals(CodeSystem.TYPE_ADHESION_CAISSE_PRINCIPALE)
                || getTypeAdhesion().equals(CodeSystem.TYPE_ADHESION_AFFILIATION)) {
            setIdTiers("");
        } else {
            setPlanCaisseId("");
        }

        if (!addOnlyAdhesion
                && (getTypeAdhesion().equals(CodeSystem.TYPE_ADHESION_CAISSE) || getTypeAdhesion().equals(
                        CodeSystem.TYPE_ADHESION_CAISSE_PRINCIPALE))) {
            _planCaisseAutomatique(transaction);
        }
    }

    /**
     * Effectue des traitements avant une suppression de la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {

        AFCotisationListViewBean cotisationList = new AFCotisationListViewBean();
        cotisationList.setForAdhesionId(getAdhesionId());
        cotisationList.setSession(transaction.getSession());
        cotisationList.find(transaction);

        if (isAllowChildDelete()) {
            for (int i = 0; i < cotisationList.size(); i++) {
                AFCotisation cotisation = (AFCotisation) cotisationList.getEntity(i);
                // cotisation.setAllowChildDelete(true);
                cotisation.delete(transaction);
            }
        } else {
            if (cotisationList.size() > 0) {
                _addError(transaction, transaction.getSession().getLabel("1280"));
            }
        }
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        // on mémorise les anciennes données
        oldAdhesion = new AFAdhesion();
        oldAdhesion.setAdhesionId(getAdhesionId());
        oldAdhesion.setSession(getSession());
        oldAdhesion.retrieve(transaction);
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return AFAdhesion.TABLE_NAME; // "AFADHEP";
    }

    /**
     * Création des cotisations en fonction du plan de caisse selectionné.
     * 
     * @param transaction
     *            la transaction a utiliser
     * @throws Exception
     */
    public void _planCaisseAutomatique(BTransaction transaction) throws Exception {

        // Selection de l'affiliation
        AFAffiliation affiliation = new AFAffiliation();
        affiliation.setSession(getSession());
        affiliation.setAffiliationId(getAffiliationId());
        affiliation.retrieve(transaction);

        AFPlanAffiliation planAffiliation = null;
        if (!JadeStringUtil.isEmpty(planAffiliationId)) {
            // le plan affiliation est donné - > retrieve
            planAffiliation = new AFPlanAffiliation();
            planAffiliation.setSession(getSession());
            planAffiliation.setPlanAffiliationId(planAffiliationId);
            planAffiliation.retrieve(transaction);

        }
        // si erreur ou aucun plan de défini:
        // - chercher s'il existe déja un plan d'affiliation
        // - création du plan d'affiliation sinon
        if ((planAffiliation == null) || planAffiliation.isNew()) {
            AFPlanAffiliationListViewBean planAffiliationList = new AFPlanAffiliationListViewBean();
            planAffiliationList.setSession(getSession());
            planAffiliationList.setForAffiliationId(getAffiliationId());
            planAffiliationList.find(transaction);

            if (planAffiliationList.size() == 0) {

                _addError(transaction, getSession().getLabel("3100"));
            }

            /*
             * planAffiliation = new AFPlanAffiliation(); planAffiliation.setSession(this.getSession());
             * planAffiliation.setAffiliationId(this.getAffiliationId()); String planAffiliationLibelle =
             * affiliation.getAffilieNumero() + "_" + affiliation.getDateDebut();
             * planAffiliation.setLibelle(planAffiliationLibelle); planAffiliation.add(transaction); }
             */else {
                planAffiliation = (AFPlanAffiliation) planAffiliationList.getEntity(0);
            }
        }

        // Initialisation de la nouvelle cotisation
        AFCotisation cotisation = new AFCotisation();
        cotisation.setSession(getSession());
        cotisation.setPlanAffiliationId(planAffiliation.getPlanAffiliationId());
        cotisation.setPlanCaisseId(getPlanCaisseId());
        cotisation.setAdhesionId(getAdhesionId());
        cotisation.setPeriodicite(affiliation.getPeriodicite());

        // Pour chaque Couverture, lister les Assurances
        AFCouvertureListViewBean couvertureList = new AFCouvertureListViewBean();
        couvertureList.setForPlanCaisseId(getPlanCaisseId());
        couvertureList.setSession(getSession());
        couvertureList.find(transaction);

        for (int i = 0; i < couvertureList.size(); i++) {
            AFCouverture couverture = (AFCouverture) couvertureList.getEntity(i);

            // Création de la nouvelle cotisation
            cotisation.setAssuranceId(couverture.getAssuranceId());

            // Adhesion - Date de fin : -
            // Couverture - Date de fin : -
            if (JadeStringUtil.isIntegerEmpty(getDateFin()) && JadeStringUtil.isIntegerEmpty(couverture.getDateFin())) {

                if (BSessionUtil
                        .compareDateFirstGreaterOrEqual(getSession(), getDateDebut(), couverture.getDateDebut())) {
                    cotisation.setDateDebut(getDateDebut());
                } else {
                    cotisation.setDateDebut(couverture.getDateDebut());
                }
                cotisation.setDateFin("");
                cotisation.setMotifFin(CodeSystem.MOTIF_FIN_AUCUN);
                cotisation.add(transaction);

                // Adhesion - Date de fin : Definie
                // Couverture - Date de fin : Definie
            } else if (!JadeStringUtil.isIntegerEmpty(getDateFin())
                    && !JadeStringUtil.isIntegerEmpty(couverture.getDateFin())) {
                if (BSessionUtil.compareDateFirstLower(getSession(), couverture.getDateDebut(), getDateDebut())
                        && BSessionUtil.compareDateBetweenOrEqual(getSession(), getDateDebut(), getDateFin(),
                                couverture.getDateFin())) {

                    cotisation.setDateDebut(getDateDebut());
                    cotisation.setDateFin(couverture.getDateFin());
                    cotisation.setMotifFin(CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE);
                    cotisation.add(transaction);

                } else if (BSessionUtil.compareDateBetweenOrEqual(getSession(), getDateDebut(), getDateFin(),
                        couverture.getDateDebut())
                        && BSessionUtil.compareDateBetweenOrEqual(getSession(), getDateDebut(), getDateFin(),
                                couverture.getDateFin())) {

                    cotisation.setDateDebut(couverture.getDateDebut());
                    cotisation.setDateFin(couverture.getDateFin());
                    cotisation.setMotifFin(CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE);
                    cotisation.add(transaction);

                } else if (BSessionUtil.compareDateBetweenOrEqual(getSession(), getDateDebut(), getDateFin(),
                        couverture.getDateDebut())
                        && BSessionUtil.compareDateFirstLower(getSession(), getDateFin(), couverture.getDateFin())) {

                    cotisation.setDateDebut(couverture.getDateDebut());
                    cotisation.setDateFin(getDateFin());
                    cotisation.setMotifFin(affiliation.getMotifFin());
                    cotisation.add(transaction);

                } else if (BSessionUtil.compareDateFirstLower(getSession(), couverture.getDateDebut(), getDateDebut())
                        && BSessionUtil.compareDateFirstGreater(getSession(), couverture.getDateFin(), getDateFin())) {

                    cotisation.setDateDebut(getDateDebut());
                    cotisation.setDateFin(getDateFin());
                    cotisation.setMotifFin(affiliation.getMotifFin());
                    cotisation.add(transaction);
                }

                // Adhesion - Date de fin : -
                // Couverture - Date de fin : Definie
            } else if (JadeStringUtil.isIntegerEmpty(getDateFin())
                    && !JadeStringUtil.isIntegerEmpty(couverture.getDateFin())) {

                if (BSessionUtil.compareDateFirstLower(getSession(), couverture.getDateDebut(), getDateDebut())
                        && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(),
                                couverture.getDateFin())) {

                    cotisation.setDateDebut(getDateDebut());
                    cotisation.setDateFin(couverture.getDateFin());
                    cotisation.setMotifFin(CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE);
                    cotisation.add(transaction);

                } else if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(),
                        couverture.getDateDebut())
                        && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(),
                                couverture.getDateFin())) {

                    cotisation.setDateDebut(couverture.getDateDebut());
                    cotisation.setDateFin(couverture.getDateFin());
                    cotisation.setMotifFin(CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE);
                    cotisation.add(transaction);
                }
                // Adhesion - Date de fin : Definie
                // Couverture - Date de fin : -
            } else {
                if (BSessionUtil.compareDateFirstLower(getSession(), couverture.getDateDebut(), getDateDebut())) {

                    cotisation.setDateDebut(getDateDebut());
                    cotisation.setDateFin(getDateFin());

                    if (!JadeStringUtil.isIntegerEmpty(affiliation.getDateFin())
                            && BSessionUtil.compareDateEqual(getSession(), affiliation.getDateFin(), getDateFin())) {
                        cotisation.setMotifFin(affiliation.getMotifFin());
                    } else {
                        cotisation.setMotifFin(CodeSystem.MOTIF_FIN_FIN_ADHESION);
                    }
                    cotisation.add(transaction);

                } else if (BSessionUtil.compareDateBetweenOrEqual(getSession(), getDateDebut(), getDateFin(),
                        couverture.getDateDebut())) {

                    cotisation.setDateDebut(couverture.getDateDebut());
                    cotisation.setDateFin(getDateFin());

                    if (!JadeStringUtil.isIntegerEmpty(affiliation.getDateFin())
                            && BSessionUtil.compareDateEqual(getSession(), affiliation.getDateFin(), getDateFin())) {
                        cotisation.setMotifFin(affiliation.getMotifFin());
                    } else {
                        cotisation.setMotifFin(CodeSystem.MOTIF_FIN_FIN_ADHESION);
                    }
                    cotisation.add(transaction);
                }
            }
            // si la transaction est en erreur, on continue tout de même pour
            // les autre cotisations
            if (transaction.hasErrors()) {
                transaction.clearErrorBuffer();
            }
        }
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        adhesionId = statement.dbReadNumeric("MRIADH");
        affiliationId = statement.dbReadNumeric("MAIAFF");
        planCaisseId = statement.dbReadNumeric("MSIPLC");
        idTiers = statement.dbReadNumeric("HTITIE");
        dateDebut = statement.dbReadDateAMJ("MRDDEB");
        dateFin = statement.dbReadDateAMJ("MRDFIN");
        typeAdhesion = statement.dbReadNumeric("MRTADH");
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        boolean validationOK = true;

        validationOK &= _propertyMandatory(statement.getTransaction(), getTypeAdhesion(), getSession().getLabel("1900"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getDateDebut(), getSession().getLabel("20"));

        if (JadeStringUtil.isIntegerEmpty(getPlanAffiliationId())) {
            AFPlanAffiliationManager plan = new AFPlanAffiliationManager();
            plan.setForAffiliationId(getAffiliationId());
            plan.setSession(getSession());
            plan.find();
            if (plan.size() < 1) {
                _addError(statement.getTransaction(), getSession().getLabel("3110"));
                validationOK = false;
            }

        }

        if (validationOK) {
            if (getTypeAdhesion().equals(CodeSystem.TYPE_ADHESION_CAISSE)
                    || getTypeAdhesion().equals(CodeSystem.TYPE_ADHESION_CAISSE_PRINCIPALE)
                    || getTypeAdhesion().equals(CodeSystem.TYPE_ADHESION_AFFILIATION)) {
                validationOK &= _propertyMandatory(statement.getTransaction(), getPlanCaisseId(), getSession()
                        .getLabel("1910"));
            } else if (getTypeAdhesion().equals(CodeSystem.TYPE_ADHESION_ASSOCIATION)) {
                validationOK &= _propertyMandatory(statement.getTransaction(), getIdTiers(),
                        getSession().getLabel("1920"));
            }
        }

        validationOK &= _checkRealDate(statement.getTransaction(), getDateDebut(), getSession().getLabel("160"));

        if (validationOK) {
            String dateLimiteInf = "01.01.1900";
            String dateInitiale = JACalendar.todayJJsMMsAAAA();
            String dateLimiteSup = getSession().getApplication().getCalendar().addYears(dateInitiale, 10);

            if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateDebut(), dateLimiteInf)) {
                if (BSessionUtil.compareDateFirstGreater(getSession(), getDateDebut(), dateLimiteSup)) {
                    _addError(statement.getTransaction(), getSession().getLabel("50"));
                    validationOK = false;
                }
            } else {
                _addError(statement.getTransaction(), getSession().getLabel("60"));
                validationOK = false;
            }
        }

        if (validationOK) {
            if (!JadeStringUtil.isIntegerEmpty(getDateFin())) {
                validationOK = _checkRealDate(statement.getTransaction(), getDateFin(), getSession().getLabel("180"));

                if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(), getDateFin())) {
                    _addError(statement.getTransaction(), getSession().getLabel("550"));
                    validationOK = false;
                }
            }
        }

        if (validationOK) {
            // Controle des Date avec les dates de debut et fin d'affiliation
            if (JadeStringUtil.isIntegerEmpty(getDateFin())
                    && JadeStringUtil.isIntegerEmpty(getAffiliation().getDateFin())) {
                if (BSessionUtil.compareDateFirstLower(getSession(), getDateDebut(), getAffiliation().getDateDebut())) {
                    _addError(statement.getTransaction(), FWMessageFormat.format(getSession().getLabel("1220"),
                            getDateDebut(), getAffiliation().getDateDebut()));
                    validationOK = false;
                }
            } else if (!JadeStringUtil.isIntegerEmpty(getDateFin())
                    && JadeStringUtil.isIntegerEmpty(getAffiliation().getDateFin())) {
                if (BSessionUtil.compareDateFirstLower(getSession(), getDateDebut(), getAffiliation().getDateDebut())) {
                    _addError(statement.getTransaction(), FWMessageFormat.format(getSession().getLabel("1220"),
                            getDateDebut(), getAffiliation().getDateDebut()));
                    validationOK = false;
                }
            } else if (!JadeStringUtil.isIntegerEmpty(getDateFin())
                    && !JadeStringUtil.isIntegerEmpty(getAffiliation().getDateFin())) {
                if (BSessionUtil.compareDateFirstLower(getSession(), getDateDebut(), getAffiliation().getDateDebut())) {
                    _addError(statement.getTransaction(), FWMessageFormat.format(getSession().getLabel("1220"),
                            getDateDebut(), getAffiliation().getDateDebut()));
                    validationOK = false;
                }
                if (BSessionUtil.compareDateFirstGreater(getSession(), getDateFin(), getAffiliation().getDateFin())) {
                    _addError(statement.getTransaction(), FWMessageFormat.format(getSession().getLabel("1230"),
                            getDateFin(), getAffiliation().getDateFin()));
                    validationOK = false;
                }
            } else {
                _addError(statement.getTransaction(), getSession().getLabel("30") + " pour l'affiliation"
                        + getAffiliation().getRaisonSociale() + " qui commence le " + getAffiliation().getDateDebut());
                validationOK = false;
            }
        }

        if (validationOK) {
            _validationDate(statement.getTransaction());
        }
    }

    /**
     * Control les dates de début et de fin d'adhesion avec les autres adhesion pour le meme plan de caisse.
     * 
     * @param transaction
     *            la transaction a utiliser
     * @return true - Si les dates sont valides
     * @throws Exception
     */
    private boolean _validationDate(BTransaction transaction) throws Exception {

        boolean result = true;

        // *********************************************************
        // Test Date de Début, Date de Fin
        // *********************************************************

        AFAdhesionManager adhesionList = new AFAdhesionManager();
        adhesionList.setForAffiliationId(getAffiliationId());
        adhesionList.setForTypeAdhesion(getTypeAdhesion());
        if (getTypeAdhesion().equals(CodeSystem.TYPE_ADHESION_CAISSE)
                || getTypeAdhesion().equals(CodeSystem.TYPE_ADHESION_CAISSE_PRINCIPALE)
                || getTypeAdhesion().equals(CodeSystem.TYPE_ADHESION_AFFILIATION)) {
            adhesionList.setForPlanCaisseId(getPlanCaisseId());
        } else {
            adhesionList.setForIdTiers(getIdTiers());
        }
        adhesionList.setSession(getSession());
        adhesionList.find(transaction);

        for (int i = 0; i < adhesionList.size(); i++) {

            AFAdhesion adhesion = (AFAdhesion) adhesionList.getEntity(i);

            // Ne pas tester l'affiliation avec elle meme
            if (!adhesion.getAdhesionId().equalsIgnoreCase(getAdhesionId())) {

                if (JadeStringUtil.isIntegerEmpty(getDateFin())) {

                    // Test si il y a déjà une adhésion sans une date de fin
                    if (JadeStringUtil.isIntegerEmpty(adhesion.getDateFin())) {
                        _addError(transaction, getSession().getLabel("1180") + adhesion.getPlanCaisse().getLibelle());
                        result = false;

                    } else {
                        // Test si il n'y a pas de chevauchement avec une
                        // adhésion
                        if (BSessionUtil.compareDateFirstLowerOrEqual(transaction.getSession(), getDateDebut(),
                                adhesion.getDateFin())) {
                            _addError(
                                    transaction,
                                    FWMessageFormat.format(getSession().getLabel("1190"), getDateDebut(),
                                            adhesion.getDateDebut(), adhesion.getDateFin()));
                            result = false;
                        }
                    }
                } else {
                    if (BSessionUtil.compareDateFirstLower(transaction.getSession(), getDateDebut(), getDateFin())) {

                        // Test si il n'y a pas de chevauchement pour un
                        // nouvelle affiliation
                        // avec date de fin avec un affiliation sans date de fin
                        if (JadeStringUtil.isIntegerEmpty(adhesion.getDateFin())) {

                            if (BSessionUtil.compareDateFirstGreaterOrEqual(transaction.getSession(), getDateFin(),
                                    adhesion.getDateDebut())) {

                                _addError(transaction, FWMessageFormat.format(getSession().getLabel("1200"),
                                        getDateDebut(), getDateFin(), adhesion.getDateDebut()));
                                result = false;
                            }

                        } else {
                            if (BSessionUtil.compareDateBetweenOrEqual(transaction.getSession(),
                                    adhesion.getDateDebut(), adhesion.getDateFin(), getDateDebut())
                                    || BSessionUtil.compareDateBetweenOrEqual(transaction.getSession(),
                                            adhesion.getDateDebut(), adhesion.getDateFin(), getDateFin())) {

                                _addError(transaction, FWMessageFormat.format(getSession().getLabel("1210"),
                                        getDateDebut(), getDateFin(), adhesion.getDateDebut(), adhesion.getDateFin()));
                                result = false;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MRIADH", this._dbWriteNumeric(statement.getTransaction(), getAdhesionId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MRIADH", this._dbWriteNumeric(statement.getTransaction(), getAdhesionId(), "adhesionId"));
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getAffiliationId(), "affiliationId"));
        statement.writeField("MSIPLC",
                this._dbWriteNumeric(statement.getTransaction(), getPlanCaisseId(), "planCaisseId"));
        statement.writeField("HTITIE", this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField("MRDDEB", this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut"));
        statement.writeField("MRDFIN", this._dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "dateFin"));
        statement.writeField("MRTADH",
                this._dbWriteNumeric(statement.getTransaction(), getTypeAdhesion(), "typeAdhesion"));
    }

    /**
     * Methode utilisée par les API.
     * 
     * @param params
     * @return
     * @throws Exception
     */
    public BManager find(Hashtable<?, ?> params) throws Exception {
        BManager manager = getManager();
        manager.setSession(getSession());
        if (params != null) {
            Enumeration<?> methods = params.keys();
            while (methods.hasMoreElements()) {
                String methodName = (String) methods.nextElement();
                String value = (String) params.get(methodName);
                Method m = manager.getClass().getMethod(methodName, new Class[] { String.class });
                if (m != null) {
                    m.invoke(manager, new Object[] { value });
                }
            }
        }
        manager.find();
        return manager;
    }

    public java.lang.String getAdhesionId() {
        return adhesionId;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    /**
     * Rechercher l'Administration (Tiers) de l'adhésion en fonction de son ID.
     * 
     * @return l'Administration
     */
    public TIAdministrationViewBean getAdministrationCaisse() {

        // Si pas d'identifiant => pas d'objet
        if (_administrationCaisse == null) {
            if (_planCaisse == null) {
                getPlanCaisse();
                if (_planCaisse == null) {
                    return null;
                }
            }
            _administrationCaisse = new TIAdministrationViewBean();
            _administrationCaisse.setSession(getSession());
            _administrationCaisse.setIdTiersAdministration(_planCaisse.getIdTiers());
            try {
                _administrationCaisse.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _administrationCaisse = null;
            }
        }
        return _administrationCaisse;
    }

    /**
     * Return le code du tiers administration.
     * 
     * @return Le code else null si aucune administration.
     */
    public String getAdministrationCaisseCode() {
        if (getAdministrationCaisse() != null) {
            return _administrationCaisse.getCodeAdministration();
        } else {
            return null;
        }
    }

    /**
     * Return l'id du tiers administration.
     * 
     * @return L'id else null si aucune administration.
     */
    public String getAdministrationCaisseId() {
        if (getAdministrationCaisse() != null) {
            return _administrationCaisse.getIdTiersAdministration();
        } else {
            return null;
        }
    }

    /**
     * Return le libelle du tiers administration (getNom()).
     * 
     * @return Le libelle else null si aucune administration.
     */
    public String getAdministrationCaisseLibelle() {
        if (getAdministrationCaisse() != null) {
            return _administrationCaisse.getNom();
        } else {
            return null;
        }
    }

    /**
     * Rechercher le tiers pour l'adhesion en fonction de son ID.
     * 
     * @return le tiers
     */
    public TIAdministrationViewBean getAdminstrationAssocia() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getIdTiers())) {
            return null;
        }

        if ((_administrationAssocia == null)
                || ((_administrationAssocia != null) && !getIdTiers().equals(_administrationAssocia.getIdTiers()))) {

            _administrationAssocia = new TIAdministrationViewBean();
            _administrationAssocia.setSession(getSession());
            _administrationAssocia.setIdTiersAdministration(getIdTiers());
            try {
                _administrationAssocia.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _administrationAssocia = null;
            }
        }
        return _administrationAssocia;
    }

    /**
     * Rechercher l'Affiliation de l'adhésion en fonction de son ID.
     * 
     * @return l'Affiliation
     */
    public AFAffiliation getAffiliation() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
            return null;
        }

        if ((_affiliation == null)
                || ((_affiliation != null) && !_affiliation.getAffiliationId().equals(getAffiliationId()))) {

            _affiliation = new AFAffiliation();
            _affiliation.setSession(getSession());
            _affiliation.setAffiliationId(getAffiliationId());
            try {
                _affiliation.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _affiliation = null;
            }
        }
        return _affiliation;
    }

    public java.lang.String getAffiliationId() {
        return affiliationId;
    }

    private String getData(String newValue, String oldValueCoti, String oldValueAdhesion, String motifFin) {
        if (oldValueCoti.equals(oldValueAdhesion) || CodeSystem.MOTIF_FIN_EXCEPTION.equals(motifFin)) {
            // valeur de la coti est la même que l'ancienne valeur, on met à
            // jour si la donnée a changé
            if (!oldValueCoti.equals(newValue)) {
                return newValue;
            }
        }
        // sinon, on ne met pas à jour -> retourner null
        return null;
    }

    private String getDataCotisationInactive(String dateDebCoti, String oldDateFinAdhesion) {
        if (JadeDateUtil.isDateAfter(dateDebCoti, oldDateFinAdhesion)) {
            return "";
        }
        return null;
    }

    public java.lang.String getDateDebut() {
        return dateDebut;
    }

    public java.lang.String getDateFin() {
        return dateFin;
    }

    public java.lang.String getIdTiers() {
        return idTiers;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new AFAdhesionManager();
    }

    /**
     * @return
     */
    public String getPlanAffiliationId() {
        return planAffiliationId;
    }

    /**
     * Rechercher le Plan de Caisse de l'adhésion en fonction de son ID.
     * 
     * @return le Plan de Caisse
     */
    public AFPlanCaisse getPlanCaisse() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getPlanCaisseId())) {
            return null;
        }

        if (_planCaisse == null) {

            _planCaisse = new AFPlanCaisse();
            _planCaisse.setSession(getSession());
            _planCaisse.setPlanCaisseId(getPlanCaisseId());
            try {
                _planCaisse.retrieve();

            } catch (Exception e) {
                _addError(null, e.getMessage());
                _planCaisse = null;
            }
        }
        return _planCaisse;
    }

    public java.lang.String getPlanCaisseId() {
        return planCaisseId;
    }

    /**
     * Rechercher le tiers pour l'adhesion en fonction de l'affiliation.
     * 
     * @return le tiers
     */
    public TITiers getTiers() {

        // Si pas d'identifiant => pas d'objet
        if (_tiers == null) {
            if (_affiliation == null) {
                getAffiliation();
                if (_affiliation == null) {
                    return null;
                }
            }
            _tiers = new TITiers();
            _tiers.setSession(getSession());
            _tiers.setIdTiers(_affiliation.getIdTiers());
            try {
                _tiers.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _tiers = null;
            }
        }
        return _tiers;
    }

    /*
     * Le Code System de TypeAdhesion
     * 
     * @return le Code System
     */
    /*
     * public FWParametersSystemCode getCsTypeAdhesion() { if (csTypeAdhesion == null) { csTypeAdhesion = new
     * FWParametersSystemCode(); csTypeAdhesion.getCode(getTypeAdhesion()); } return csTypeAdhesion; }
     */

    // *******************************************************
    // Setter
    // *******************************************************

    public java.lang.String getTypeAdhesion() {
        return typeAdhesion;
    }

    /**
     * Retourne le Libelle du Type d'ahesion.
     * 
     * @return
     */
    public java.lang.String getTypeAdhesionLibelle() {
        if ((typeAdhesion != null) && (getSession() != null)) {
            return getSession().getCodeLibelle(typeAdhesion);
        }
        return "";
    }

    /**
     * Renvoie TRUE si les cotisations peuvent etre supprimée
     * 
     * @return TRUE si les cotisations peuvent etre supprimée.
     */
    public boolean isAllowChildDelete() {
        return allowChildDelete;
    }

    /**
     * Pour ajouter seulement l'adhésion sans les cotisations.
     * 
     * @param b
     *            TRUE pour ajouter seulement l'adhesion.
     */
    public void setAddOnlyAdhesion(boolean b) {
        addOnlyAdhesion = b;
    }

    public void setAdhesionId(java.lang.String string) {
        adhesionId = string;
    }

    public void setAffiliationId(java.lang.String string) {
        affiliationId = string;
    }

    /**
     * Pour forcer la suppression des Cotisations si l'adhésion est supprimée.
     * 
     * @param b
     *            TRUE pour forcer la suppression.
     */
    public void setAllowChildDelete(boolean b) {
        allowChildDelete = b;
    }

    public void setDateDebut(java.lang.String string) {
        dateDebut = string;
    }

    public void setDateFin(java.lang.String string) {
        dateFin = string;
    }

    public void setIdTiers(java.lang.String string) {
        idTiers = string;
    }

    /**
     * @param string
     */
    public void setPlanAffiliationId(String string) {
        planAffiliationId = string;
    }

    public void setPlanCaisseId(java.lang.String string) {
        if (!planCaisseId.equals(string)) {
            _planCaisse = null;
            _administrationCaisse = null;
        }
        planCaisseId = string;
    }

    public void setTypeAdhesion(java.lang.String string) {
        typeAdhesion = string;
    }

    // @BMS-ONLY : Accesor needed to rollback a RADIATION
    public AFAdhesion getOldAdhesion() {
        return oldAdhesion;
    }
}
