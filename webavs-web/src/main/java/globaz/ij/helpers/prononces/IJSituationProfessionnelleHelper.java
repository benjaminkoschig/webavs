/*
 * Créé le 29 sept. 05
 */
package globaz.ij.helpers.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.ij.db.prononces.IJEmployeur;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.ij.regles.IJPrononceRegles;
import globaz.ij.vb.prononces.IJSituationProfessionnelleViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.employeurs.PRDepartement;
import globaz.prestation.db.employeurs.PRDepartementManager;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;

import java.util.List;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJSituationProfessionnelleHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJSituationProfessionnelleViewBean spViewBean = (IJSituationProfessionnelleViewBean) viewBean;

        if (reinitialiserPrononce((BSession) session,
                IJPrononce.loadPrononce((BSession) session, null, spViewBean.getIdPrononce(), spViewBean.getCsTypeIJ()))) {
            spViewBean.add();
        }
    }

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see #actionPreparerChercher(FWViewBeanInterface, FWAction, BSession)
     */
    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        chargerInfosAffiliations((BSession) session, (IJSituationProfessionnelleViewBean) viewBean);
        setPermissions((BSession) session, (IJSituationProfessionnelleViewBean) viewBean);
    }

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJSituationProfessionnelleViewBean spViewBean = (IJSituationProfessionnelleViewBean) viewBean;

        if (reinitialiserPrononce((BSession) session,
                IJPrononce.loadPrononce((BSession) session, null, spViewBean.getIdPrononce(), spViewBean.getCsTypeIJ()))) {
            spViewBean.delete();
        }
    }

    /**
     * @param arg0
     *            DOCUMENT ME!
     * @param arg1
     *            DOCUMENT ME!
     * @param arg2
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _init(FWViewBeanInterface arg0, FWAction arg1, BISession arg2) throws Exception {
        setPermissions((BSession) arg2, (IJSituationProfessionnelleViewBean) arg0);
    }

    /**
     * Charger la situation pro et rechercher les affiliations pour le tiers employeur courant.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);
        chargerInfosAffiliations((BSession) session, (IJSituationProfessionnelleViewBean) viewBean);
        setPermissions((BSession) session, (IJSituationProfessionnelleViewBean) viewBean);
    }

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJSituationProfessionnelleViewBean spViewBean = (IJSituationProfessionnelleViewBean) viewBean;

        if (reinitialiserPrononce((BSession) session, spViewBean.loadPrononce())) {
            spViewBean.update();
        }
    }

    /**
     * recherche les affiliations pour le tiers courant.
     * 
     * @param session
     * @param spViewBean
     * 
     * @throws Exception
     */
    private void chargerInfosAffiliations(BSession session, IJSituationProfessionnelleViewBean spViewBean)
            throws Exception {
        // dresser la liste des affiliations pour ce tiers
        if (!JadeStringUtil.isIntegerEmpty(spViewBean.getIdTiersEmployeur())) {
            spViewBean.setAffiliationsEmployeur(PRAffiliationHelper.getAffiliationsTiers(session,
                    spViewBean.getIdTiersEmployeur()));
        }

        // retrouver le id affilie en fonction du numero affilie si retour
        // depuis pyxis
        if (JadeStringUtil.isIntegerEmpty(spViewBean.getIdAffilieEmployeur())
                && !JadeStringUtil.isIntegerEmpty(spViewBean.getNumAffilieEmployeur())) {
            IPRAffilie affilie = PRAffiliationHelper.getEmployeurParNumAffilie(session,
                    spViewBean.getNumAffilieEmployeur(), spViewBean.getDateDebutPrononce());

            if (affilie != null) {
                spViewBean.loadEmployeur().setIdAffilie(affilie.getIdAffilie());
            }
        }

        // renseigner le numero d'affilie par defaut pour le cas ou un id
        // affilie a deja ete choisi.
        if (!JadeStringUtil.isIntegerEmpty(spViewBean.getIdAffilieEmployeur())
                && (spViewBean.getAffiliationsEmployeur() != null)) {
            for (int idAffiliation = 0; idAffiliation < spViewBean.getAffiliationsEmployeur().size(); ++idAffiliation) {
                String[] affiliation = (String[]) spViewBean.getAffiliationsEmployeur().get(idAffiliation);

                if (spViewBean.getIdAffilieEmployeur().equals(affiliation[0])) {
                    spViewBean.setNumAffilieEmployeur(affiliation[1]);
                }
            }
        }

        // charger les infos sur le departement
        if (JadeStringUtil.isIntegerEmpty(spViewBean.getIdAffilieEmployeur())) {
            spViewBean.setMenuDepartement(null); // menu vide
        } else {
            PRDepartementManager mgr = new PRDepartementManager();

            mgr.setISession(session);
            mgr.setForIdAffilie(spViewBean.getIdAffilieEmployeur());
            mgr.find();

            if (!mgr.isEmpty()) {
                Vector retValue = new Vector();

                for (int idDepartement = 0; idDepartement < mgr.size(); ++idDepartement) {
                    PRDepartement departement = (PRDepartement) mgr.get(idDepartement);

                    retValue.add(new String[] { departement.getIdParticularite(), departement.getDepartement() });
                }

                spViewBean.setMenuDepartement(retValue);
            } else {
                spViewBean.setMenuDepartement(null); // menu vide
            }
        }
    }

    /**
     * retrouver par introspection le nom de la methode de cette class qu'il faut executer.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    public void rechercherAffilie(FWViewBeanInterface viewBean, FWAction action, BSession session) throws Exception {

        IJSituationProfessionnelleViewBean spviewBean = (IJSituationProfessionnelleViewBean) viewBean;

        IPRAffilie affilie;
        affilie = PRAffiliationHelper.getEmployeurParNumAffilie(session, spviewBean.getNumAffilieEmployeur(),
                spviewBean.getDateDebutPrononce());

        if (affilie == null) {
            spviewBean.setNomEmployeur("");
            spviewBean.setIdAffilieEmployeur("");
            spviewBean.setIdTiersEmployeur("");
            spviewBean.setNumAffilieEmployeur("");

            spviewBean.setMessage(session.getLabel("AFFILIE_NON_TROUVE"));
            spviewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        else {
            spviewBean.setIdTiersEmployeur(affilie.getIdTiers());
            spviewBean.setIdAffilieEmployeur(affilie.getIdAffilie());
            spviewBean.setNomEmployeur(affilie.getNom());
            spviewBean.setNumAffilieEmployeur(affilie.getNumAffilie());
        }
    }

    private boolean reinitialiserPrononce(BSession session, IJPrononce prononce) throws Exception {
        BITransaction transaction = null;

        try {
            transaction = session.newTransaction();
            transaction.openTransaction();

            // reinitialiser le prononce
            IJPrononceRegles.reinitialiser(session, transaction, prononce);
            prononce.update(transaction);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();

                        return false;
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        return true;
    }

    private void setPermissions(BSession session, IJSituationProfessionnelleViewBean sitPro) throws Exception {
        sitPro.setModifierPermis(IJPrononceRegles.isModifierPermis(sitPro.loadPrononce()));
    }

    /**
     * recherche le canton dans les situations professionnelles
     * @param domaine
     * @param situationsProf
     * @return
     * @throws Exception
     */
    public String rechercheCantonAdressePaiementSitProf(BSession session, String domaine, List<IJSituationProfessionnelle> situationsProf, String dateDebut) throws Exception {
        String canton = "";
        // vérification du canton de la situation professionnelle
         for (IJSituationProfessionnelle sit : situationsProf) {
            if (!JadeStringUtil.isEmpty(sit.getIdEmployeur())) {
                IJEmployeur employeur = sit.loadEmployeur();
                TIAdressePaiementData data = PRTiersHelper.getAdressePaiementData(session, session.getCurrentThreadTransaction(), employeur.getIdTiers(),
                        domaine, employeur.getIdAffilie(), dateDebut);

                if (!data.isNew()) {
                    String cantonComparaison = PRTiersHelper.getCanton(session, data.getNpa());
                    if (cantonComparaison == null) {
                        // canton de l'adresse de paiement de la banque (indépendant étranger ?)
                        cantonComparaison = PRTiersHelper.getCanton(session, data.getNpa_banque());
                    }
                    // toutes les situations professionnelles du droit doivent avoir le même canton sinon impossible de déterminer
                    if (!canton.isEmpty() && !canton.equals(cantonComparaison)) {
                        throw new Exception("impossible de déterminer le canton d'imposition : plusieurs cantons différents pour plusieurs employeurs : ");
                    } else {
                        canton = cantonComparaison;
                    }
                }

            }
        }
        return canton;
    }
}
