/*
 * Created on 14-Jan-05
 */
package globaz.naos.db.masse;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * Cette classe est crées pour facilité le travail du FWDispatcher.
 * 
 * Elle etend AFMasseViewBean, et est utilisée par l'écran masseModifier_de.jsp.
 * 
 * @author sau
 */
public class AFMasseModifierViewBean extends BEntity implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AFAffiliation _affiliation = null;
    private TITiers _tiers = null;

    // affiliation
    private java.lang.String affiliationId = new String();

    // Cotisation
    private List<AFCotisation> cotisationList = new ArrayList<AFCotisation>();

    /**
     * Constructeur d'AFMasseModifierViewBean.
     */
    public AFMasseModifierViewBean() {
        super();
    }

    /**
     * Retour le nom de la Table.
     * 
     * Pas utilisé dans ce BEntity.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "";
    };

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * Pas utilisé dans ce BEntity.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * Pas utilisé dans ce BEntity.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * Pas utilisé dans ce BEntity.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * Pas utilisé dans ce BEntity.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * Rechercher l'Affiliation en fonction de sont ID.
     * 
     * @return l'Affiliation
     */
    public AFAffiliation getAffiliation() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
            return null;
        }

        if (_affiliation == null) {

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

    public List<AFCotisation> getCotisationList() {

        try {
            retrieveCotisation();
        } catch (Exception e) {
            JadeLogger.warn(this, "unable to retrieve cotisations");
        }

        return cotisationList;
    }

    /**
     * Rechercher le tiers de l'affiliation en fonction de son ID.
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

    public void retrieveCotisation() throws Exception {

        cotisationList.clear();

        if (getAffiliation().getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_EMPLOY)
                || getAffiliation().getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_INDEP_EMPLOY)
                || getAffiliation().getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_EMPLOY_D_F)
                || getAffiliation().getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_LTN)) {

            AFPlanAffiliationManager planManager = new AFPlanAffiliationManager();
            planManager.setSession(getSession());
            planManager.setForAffiliationId(affiliationId);
            planManager.find();

            for (int i = 0; i < planManager.size(); i++) {
                AFPlanAffiliation planAffiliation = (AFPlanAffiliation) planManager.get(i);
                // ne pas prendre en compte les plans inactifs
                if (!planAffiliation.isInactif().booleanValue()) {
                    AFCotisationManager cotiManager = new AFCotisationManager();
                    cotiManager.setSession(getSession());
                    cotiManager.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
                    cotiManager.setForDateFin(""); // on ne selectionne que les
                    // cotis dont la date est
                    // vide
                    cotiManager.setNotForTypeAssurance(CodeSystem.TYPE_ASS_FFPP); // pas
                    // les
                    // FFPP
                    cotiManager.setOrder("IDEXTERNE");
                    cotiManager.find(BManager.SIZE_NOLIMIT);

                    for (int j = 0; j < cotiManager.size(); j++) {
                        AFCotisation cotisation = (AFCotisation) cotiManager.get(j);
                        if (cotisation.getAssurance().getAssuranceGenre().equals(CodeSystem.GENRE_ASS_PARITAIRE)
                                && (cotisation.getAssurance().isAssurance13().booleanValue() || cotisation
                                        .getAssurance().getDecompte13Releve().booleanValue())) {
                            cotisationList.add(cotisation);
                        }
                    }
                }
            }
        }
    }

    public void setAffiliationId(java.lang.String string) {
        affiliationId = string;
    }

    public void setCotisationList(List<AFCotisation> list) {
        cotisationList = list;
    }

    /**
     * Mise à jour des données transmises vi l'écran de modification des masses
     * 
     * @param request
     *            la requête HTTP
     * @throws Exception
     */
    public void updateMasse(HttpServletRequest request) throws Exception {

        BTransaction transaction = null;
        try {
            transaction = new BTransaction(getSession());
            transaction.openTransaction();

            List<AFCotisation> cotisationList = getCotisationList();
            String periodeMin = CodeSystem.PERIODICITE_ANNUELLE;
            boolean masseDifferenteDeZero = false;
            for (int i = 0; i < cotisationList.size(); i++) {
                AFCotisation cotisation = cotisationList.get(i);
                String nouvelleMasse = request.getParameter("nouvelleMasse" + i);
                String periodiciteNouvelleMasse = request.getParameter("periodiciteNouvelleMasse" + i);
                String anciennePeriodicite = request.getParameter("periodiciteActuId" + i);
                String nouvellePeriodicite = request.getParameter("nouvellePeriodicite" + i);
                // Tester si une masse est > 0 pour mettre motif de fin sinon AC2 à toujours le motif sans personnel en
                // cas de changement de masse
                if (masseDifferenteDeZero == false && !JadeStringUtil.isBlankOrZero(nouvelleMasse)) {
                    masseDifferenteDeZero = true;
                }
                if ((anciennePeriodicite != null) && anciennePeriodicite.equals(nouvellePeriodicite)) {
                    // pas de changement dans la périodicité de la cotisations
                    nouvellePeriodicite = null;
                }
                try {
                    // Double.parseDouble(JANumberFormatter.deQuote(nouvelleMasse));
                    cotisation.updateMasseAnnuelle(periodiciteNouvelleMasse, nouvelleMasse, nouvellePeriodicite,
                            request.getParameter("dateModification" + i), masseDifferenteDeZero);
                    // test si mise à jour d'affiliation
                    periodeMin = AFUtil.getSmallerPeriode(periodeMin, nouvellePeriodicite);
                } catch (NumberFormatException nfe) {
                    // on continue
                    continue;
                }
            }
            // maj de l'affiliation si periodocité plus petite
            String perdiodiciteAffiliation = getAffiliation().getPeriodicite();
            if (!perdiodiciteAffiliation.equals(AFUtil.getSmallerPeriode(perdiodiciteAffiliation, periodeMin))) {
                AFAffiliation aff = getAffiliation();
                aff.setPeriodicite(CodeSystem.PERIODICITE_MENSUELLE);
                aff.wantCallValidate(false);
                aff.wantCallMethodBefore(false);
                aff.wantCallMethodAfter(false);
                aff.update();
            }
            if (transaction.hasErrors() || transaction.hasErrors()) {
                transaction.rollback();
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw (e);
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
                transaction = null;
            }
        }
    }
}
