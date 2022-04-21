package globaz.osiris.db.avance;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.translation.FWTranslation;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.recouvrement.CAPlanRecouvrementViewBean;
import globaz.osiris.external.IntRole;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Class extends de l'entit� du Plan de recouvrement. <br>
 * Repr�sente l'entit� pour la gestion des Avances.
 *
 * @author dda
 */
public class CAAvanceViewBean extends CAPlanRecouvrementViewBean implements FWViewBeanInterface {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final Set CODES_EXCLUS_POUR_ECRANS = new HashSet();

    public static final String FX_ELEMENT_PREFIX = "osiris.avance.mode.";

    private static final String LABEL_ACOMPTE_SUP_PLAFOND = "ACOMPTE_SUP_PLAFOND";

    private static final String LABEL_AUCUNE_ECHEANCE_ASSOCIEE = "AUCUNE_ECHEANCE_ASSOCIEE";
    private static final String LABEL_DATE_FIN_ETOU_PLAFOND = "DATE_FIN_ETOU_PLAFOND";
    private static final String LABEL_MISSING_MODE_AVANCE = "MISSING_MODE_AVANCE";


    private String idExterneRoleFromScreen = "";

    static {
        CAAvanceViewBean.CODES_EXCLUS_POUR_ECRANS.add(CAPlanRecouvrement.CS_BVR);
        CAAvanceViewBean.CODES_EXCLUS_POUR_ECRANS.add(CAPlanRecouvrement.CS_DIRECT);
        CAAvanceViewBean.CODES_EXCLUS_POUR_ECRANS.add(CAPlanRecouvrement.CS_VERSEMENT);
        CAAvanceViewBean.CODES_EXCLUS_POUR_ECRANS.add(CAPlanRecouvrement.CS_COMPENSATION);
        CAAvanceViewBean.CODES_EXCLUS_POUR_ECRANS.add(CAPlanRecouvrement.CS_RECOUVREMENT);
    }

    /**
     * Retourne la liste de tous les idCodes s�par�s par des virgules, permis pour cet utilisateur et n'�tant pas
     * contenus dans le set exclus.
     *
     * @see #modesPourUtilisateurCourant(BSession, Set, boolean)
     */
    public static final String listeIdsModesPourUtilisateurCourant(BSession session, Set exclus) throws Exception {
        return ((String[]) CAAvanceViewBean.modesPourUtilisateurCourant(session, exclus, true).get(0))[0];
    }

    /**
     * idem que modesPourUtilisateurCourant(session, CODES_EXCLUS_POUR_ECRANS, true).
     *
     * @see #modesPourUtilisateurCourant(BSession, Set, boolean)
     */
    public static final Vector /* String[]{idCode, libelleCode} */modesPourUtilisateurCourant(BSession session)
            throws Exception {
        return CAAvanceViewBean.modesPourUtilisateurCourant(session, CAAvanceViewBean.CODES_EXCLUS_POUR_ECRANS, true);
    }

    /**
     * Retourne un vecteur utilisable dans un FWListSelectTag contenant des tableaux de String[2] {idCode, libelleCode},
     * les codes sont ceux sur lesquels l'utilisateur courant a le droit de lecture et qui ne sont pas dans le set
     * exclus.
     *
     * @param session la session permettant de retrouver l'utilisateur
     * @param exclus  un ensemble d'id de code � exclure du r�sultat
     * @param tous    vrai pour que le vecteur contiennent l'option 'Tous'
     * @return un vecteur, jamais null, peut-etre vide
     */
    public static final Vector /* String[]{idCode, libelleCode} */modesPourUtilisateurCourant(BSession session,
                                                                                              Set exclus, boolean tous) throws Exception {
        Vector retValue = new Vector();
        StringBuffer tousModes = new StringBuffer();
        FWParametersSystemCodeManager codes = FWTranslation.getSystemCodeList("OSIPLRMOD", session);

        for (int id = 0; id < codes.size(); ++id) {
            FWParametersSystemCode code = (FWParametersSystemCode) codes.get(id);
            String idCode = code.getIdCode();

            if (!exclus.contains(idCode)
                    && session.hasRight(CAAvanceViewBean.FX_ELEMENT_PREFIX + idCode, FWSecureConstants.READ)) {
                // cr�er les infos
                String[] codeInfo = new String[]{idCode, code.getCurrentCodeUtilisateur().getLibelle()};

                retValue.add(codeInfo);

                // renseigner l'option tous
                if (tousModes.length() > 0) {
                    tousModes.append(',');
                }

                tousModes.append(idCode);
            }
        }

        if (tous) {
            retValue.insertElementAt(new String[]{tousModes.toString(), session.getLabel("TOUS")}, 0);
        }

        return retValue;
    }

    // La date de fin de l'avance
    private String dateMax = new String();

    /**
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        CAAvanceEcheance echeance = new CAAvanceEcheance();
        echeance.setSession(getSession());

        setEcheanceValue(echeance);
        echeance.setDateRappel(getDateEcheance());
        if (!JadeStringUtil.isBlank(getPremierAcompte())) {
            echeance.setMontant(getPremierAcompte());
        } else {
            echeance.setMontant(getAcompte());
        }
        echeance.add(transaction);
    }

    /**
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        setDateMax(getEcheance(transaction).getDateExigibilite());
    }

    /**
     * @see globaz.globall.db.BEntity#_afterUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        CAAvanceEcheance echeance = getEcheance(transaction);

        setEcheanceValue(echeance);
        echeance.setDateRappel(getDateEcheance());
        if (!JadeStringUtil.isBlank(getPremierAcompte())) {
            echeance.setMontant(getPremierAcompte());
        } else {
            echeance.setMontant(getAcompte());
        }
        echeance.update(transaction);
    }

    /**
     * @see globaz.osiris.db.access.recouvrement.CAPlanRecouvrement#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);

        setCollaborateur(getSession().getUserName());
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        CAAvanceEcheance echeance = getEcheance(transaction);

        echeance.delete(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeRetrieve(BTransaction transaction) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        CAAvanceViewBean avance = new CAAvanceViewBean();
        avance.setSession(getSession());
        avance.setIdPlanRecouvrement(getIdPlanRecouvrement());
        avance.retrieve();

        // contr�le de l'�ch�ance
        if (!avance.getIdTypeEcheance().equals(getIdTypeEcheance())) {
            _addError(transaction, getSession().getLabel("AVANCE_ECHEANCE_NON_AUTORISEE"));
        }
        // contr�le acompte
        FWCurrency acompteold = new FWCurrency(avance.getAcompte());
        FWCurrency acompte = new FWCurrency(getAcompte());
        if (!acompte.equals(acompteold)) {
            _addError(transaction, getSession().getLabel("AVANCE_ACOMPTE_NON_AUTORISEE"));
        }
        // contr�le montant max.
        FWCurrency montantMaxOld = new FWCurrency(avance.getPlafond());
        FWCurrency montantMax = new FWCurrency(getPlafond());
        if (!montantMax.equals(montantMaxOld)) {
            _addError(transaction, getSession().getLabel("AVANCE_ACOMPTE_MAX_NON_AUTORISEE"));
        }
        // premier acompte
        FWCurrency premierAcompteOld = new FWCurrency(avance.getPremierAcompte());
        FWCurrency premierAcompte = new FWCurrency(getPremierAcompte());
        if (!premierAcompte.equals(premierAcompteOld)) {
            _addError(transaction, getSession().getLabel("AVANCE_PREMIER_ACOMPTE_NON_AUTORISEE"));
        }

        // 1�re date d'�ch�ance
        JACalendarGregorian dateEcheance = new JACalendarGregorian();
        if (dateEcheance.compare(getDateEcheance(), avance.getDateEcheance()) != JACalendar.COMPARE_EQUALS) {
            _addError(transaction, getSession().getLabel("AVANCE_PREMIERE_ECHEANCE_NON_AUTORISEE"));
        }

        // date de fin
        JACalendarGregorian dateFin = new JACalendarGregorian();
        if (dateFin.compare(getDateMax(), avance.getDateMax()) != JACalendar.COMPARE_EQUALS) {
            _addError(transaction, getSession().getLabel("AVANCE_DATE_FIN_NON_AUTORISEE"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) {
        // date d'octroi obligatoire et valide
        _propertyMandatory(statement.getTransaction(), getDate(),
                getSession().getLabel(CAPlanRecouvrement.LABEL_MISSING_DATE_OCTROI));
        _checkDate(statement.getTransaction(), getDate(),
                getSession().getLabel(CAPlanRecouvrement.LABEL_DATE_OCTROI_INCORRECT));

        // libell�
        _propertyMandatory(statement.getTransaction(), getDate(),
                getSession().getLabel(CAPlanRecouvrement.LABEL_MISSING_LIBELLE));

        // mode de recouvrement
        _propertyMandatory(statement.getTransaction(), getIdModeRecouvrement(),
                getSession().getLabel(CAAvanceViewBean.LABEL_MISSING_MODE_AVANCE));

        // L'acompte ne doit pas �tre vide et >0
        _propertyMandatory(statement.getTransaction(), getAcompte(),
                getSession().getLabel(CAPlanRecouvrement.LABEL_MISSING_ACOMPTE));
        if (Double.parseDouble(JANumberFormatter.deQuote(getAcompte())) <= 0) {
            _addError(statement.getTransaction(), getSession().getLabel(CAPlanRecouvrement.LABEL_ACOMPTE_NEGATIF));
        }

        if (!JadeStringUtil.isBlank(getPlafond())) {
            FWCurrency tmpAcompte = new FWCurrency(JANumberFormatter.deQuote(getAcompte()));
            FWCurrency tmpPlafond = new FWCurrency(JANumberFormatter.deQuote(getPlafond()));

            if (!tmpPlafond.isZero() && (tmpAcompte.compareTo(tmpPlafond) == 1)) {
                _addError(statement.getTransaction(), getSession().getLabel(CAAvanceViewBean.LABEL_ACOMPTE_SUP_PLAFOND));
            }
        }

        if (JadeStringUtil.isBlank(getDateMax()) && JadeStringUtil.isBlank(getPlafond())) {
            _addError(statement.getTransaction(), getSession().getLabel(CAAvanceViewBean.LABEL_DATE_FIN_ETOU_PLAFOND));
        }

        // Date �ch�ance
        if (hasEcheanceAuto()) {
            _propertyMandatory(statement.getTransaction(), getDateEcheance(),
                    getSession().getLabel(CAPlanRecouvrement.LABEL_MISSING_DATE_ECHEANCE));
            _checkDate(statement.getTransaction(), getDateEcheance(),
                    getSession().getLabel(CAPlanRecouvrement.LABEL_DATE_ECHEANCE_INCORRECT));
        } else {
            if (!JadeStringUtil.isBlank(getDateEcheance())) {
                _addError(statement.getTransaction(),
                        getSession().getLabel(CAPlanRecouvrement.LABEL_DATE_ECHEANCE_MUST_BE_EMPTY));
            }
        }
        if (getIdModeRecouvrement().equals(CAPlanRecouvrement.CS_AVANCE_PTRA) &&
                JadeStringUtil.isBlankOrZero(getIdCompteAnnexe()) || !checkIfIsPTRA()) {
            CompteAnnexeSimpleModel compteAnnexe = null;
            String nss = this.idExterneRoleFromScreen;
            String idTiers = "";
            JadeThreadContext threadContext = initThreadContext(getSession());
            try {
                CACompteAnnexe compteAnnexeExist = getCompteAnnexe();
                if (compteAnnexeExist != null) {
                    idTiers = compteAnnexeExist.getIdTiers();
                } else {
                    PRTiersWrapper tw = PRTiersHelper.getTiers(getSession(), nss);
                    if (tw != null) {
                        idTiers = tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                    } else {
                        IPRAffilie aff = PRAffiliationHelper.getEmployeurParNumAffilie(getSession(), nss);
                        if (aff != null) {
                            idTiers = aff.getIdTiers();
                        }
                    }
                }
                JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
                compteAnnexe = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(null,
                        idTiers, IntRole.ROLE_PTRA,
                        nss, true);
                getSession().getCurrentThreadTransaction().commit();
            } catch (Exception e) {
                JadeLogger.error(this, e);
                _addError(statement.getTransaction(), e.getMessage());
            } finally {
                JadeThreadActivator.stopUsingContext(threadContext);
            }
            if (compteAnnexe != null) {
                setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
            }
        }

        // Compte annexe
        _propertyMandatory(statement.getTransaction(), getIdCompteAnnexe(),
                getSession().getLabel(CAPlanRecouvrement.LABEL_COMPTEANNEXE_OBLIGATOIRE));
    }

    public void setIdExterneRoleFromScreen(String idExterneRoleFromScreen) {
        this.idExterneRoleFromScreen = idExterneRoleFromScreen;
    }

    public String getIdExterneRoleFromScreen() {
        return this.idExterneRoleFromScreen;
    }

    private boolean checkIfIsPTRA() {
        if (!JadeStringUtil.isBlankOrZero(getIdCompteAnnexe())) {
            CACompteAnnexe compteAnnexe = getCompteAnnexe();
            if (compteAnnexe.getIdRole().equals(IntRole.ROLE_PTRA)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne la valeur format�. Si cette derni�re est d�j� formatt� aucune op�ration ne sera effectu� pour la
     * reformat�.
     *
     * @see globaz.osiris.db.access.recouvrement.CAPlanRecouvrement#getPlafondFormate()
     */
    @Override
    public String getAcompteFormate() {
        if (getAcompte().indexOf("'") > -1) {
            return getAcompte();
        } else {
            return JANumberFormatter.formatNoRound(getAcompte(), 2);
        }
    }

    /**
     * Retourne le nom du tiers du compte annexe. <br>
     * Utilis� dans la page jsp de d�tail.
     *
     * @return
     */
    public String getCompteAnnexeNom() {
        if (JadeStringUtil.isIntegerEmpty(getIdCompteAnnexe())) {
            return "";
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());
        compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());

        try {
            compteAnnexe.retrieve();

            if (!compteAnnexe.isNew()) {
                return compteAnnexe.getTiers().getNom();
            } else {
                return "";
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return "";
        }
    }

    /**
     * Retourne la date de fin de l'avance.
     *
     * @return
     */
    public String getDateMax() {
        return dateMax;
    }

    /**
     * Retourne l'�ch�ance de l'avance. <br>
     * Utilisation du manager car la recherche se fait sur l'idPlanRecouvrement.
     *
     * @param transaction
     * @return
     * @throws Exception
     */
    public CAAvanceEcheance getEcheance(BTransaction transaction) throws Exception {
        CAAvanceEcheanceManager manager = new CAAvanceEcheanceManager();
        manager.setSession(getSession());

        manager.setForIdPlanRecouvrement(getIdPlanRecouvrement());

        manager.find(transaction);

        if (manager.hasErrors() || manager.isEmpty()) {
            throw new Exception(getSession().getLabel(CAAvanceViewBean.LABEL_AUCUNE_ECHEANCE_ASSOCIEE));
        }

        return (CAAvanceEcheance) manager.getFirstEntity();
    }

    /**
     * Retourne l'idExterneRole du Compte Annexe. <br>
     * Utilis� dans la page jsp de d�tail.
     *
     * @return
     */
    public String getIdExterneRole() {
        if (JadeStringUtil.isIntegerEmpty(getIdCompteAnnexe())) {
            return "";
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());
        compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());

        try {
            compteAnnexe.retrieve();

            if (!compteAnnexe.isNew()) {
                return compteAnnexe.getIdExterneRole();
            } else {
                return "";
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return "";
        }
    }

    /**
     * Retourne la valeur format�. Si cette derni�re est d�j� formatt� aucune op�ration ne sera effectu� pour la
     * reformat�.
     *
     * @see globaz.osiris.db.access.recouvrement.CAPlanRecouvrement#getPlafondFormate()
     */
    @Override
    public String getPlafondFormate() {
        if (getPlafond().indexOf("'") > -1) {
            return getPlafond();
        } else {
            return JANumberFormatter.formatNoRound(getPlafond(), 2);
        }
    }

    /**
     * Retourne la valeur format�. Si cette derni�re est d�j� formatt� aucune op�ration ne sera effectu� pour la
     * reformat�.
     *
     * @see globaz.osiris.db.access.recouvrement.CAPlanRecouvrement#getPlafondFormate()
     */
    @Override
    public String getPremierAcompteFormate() {
        if (getPremierAcompte().indexOf("'") > -1) {
            return getPremierAcompte();
        } else {
            return JANumberFormatter.formatNoRound(getPremierAcompte(), 2);
        }
    }

    /**
     * Set la date de fin de l'avance.
     *
     * @param string
     */
    public void setDateMax(String string) {
        dateMax = string;
    }

    /**
     * Mise � jour ou ajout des valeurs de l'�ch�ance (table CAECHPP).
     *
     * @param echeance
     */
    private void setEcheanceValue(CAAvanceEcheance echeance) {
        echeance.setIdPlanRecouvrement(getIdPlanRecouvrement());
        echeance.setDateExigibilite(getDateMax());
        echeance.setDateRappel(getDateEcheance());
    }

    protected JadeThreadContext initThreadContext(BSession session) {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles;
        try {
            roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                    .findAllIdRoleForIdUser(session.getUserId());
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);

        return context;
    }
}
