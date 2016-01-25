package globaz.corvus.regles;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.demandes.REDemandeRenteSurvivant;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.prestation.clone.factory.PRCloneFactory;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une classe qui propose des méthodes pour gérer le cycle de vie des demandes.
 * </p>
 * 
 * 
 * @author scr
 */
public final class REDemandeRegles {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static String CLONE_DEFINITION = null;
    private static String CLONE_DEMANDE_API_COPIE = null;
    private static String CLONE_DEMANDE_API_CORRECTION = null;
    private static String CLONE_DEMANDE_INVALIDITE_COPIE = null;
    private static String CLONE_DEMANDE_INVALIDITE_CORRECTION = null;

    private static String CLONE_DEMANDE_SURVIVANT_COPIE = null;
    private static String CLONE_DEMANDE_SURVIVANT_CORRECTION = null;
    private static String CLONE_DEMANDE_VIEILLESSE_COPIE = null;
    private static String CLONE_DEMANDE_VIEILLESSE_CORRECTION = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public static final REDemandeRente copierDemandeRente(BSession session, BITransaction transaction,
            REDemandeRente demandeSource) throws Exception, REReglesException {

        PRCloneFactory cf = PRCloneFactory.getInstance();
        REDemandeRente clone = (REDemandeRente) cf.clone(getCloneDefinition(session), session, transaction,
                demandeSource, getCloneDemandeImportationForCopie(session, demandeSource), REDemandeRente.CLONE_NORMAL);
        return clone;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée un prononcé de correction pour le prononcé donné.
     * 
     * <ol>
     * <li>Si le prononcé est annulé, les actions suivantes sont effectuées sur son enfant actif.</li>
     * <li>Si le prononcé a déjà une correction on ne fait rien</li>
     * <li>Créer un clone du prononcé et le rajouter comme enfant.</li>
     * </ol>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final REDemandeRente corrigerDemandeRente(BSession session, BITransaction transaction,
            REDemandeRente demandeSource) throws Exception, REReglesException {

        // importe la demande comme enfant de la demande source
        if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(demandeSource.getCsEtat())
                && !IREDemandeRente.CS_ETAT_DEMANDE_RENTE_PAYE.equals(demandeSource.getCsEtat())) {

            throw new REReglesException(session.getLabel("ERREUR_REGLES_DEMANDE_VIEIL_IMP") + demandeSource.getCsEtat()
                    + "idDemande = " + demandeSource.getIdDemandeRente());
        }

        PRCloneFactory cf = PRCloneFactory.getInstance();
        REDemandeRente clone = (REDemandeRente) cf.clone(getCloneDefinition(session), session, transaction,
                demandeSource, getCloneDemandeImportation(session, demandeSource), REDemandeRente.CLONE_FILS);

        return clone;
    }

    private static final String getCloneDefinition(BSession session) throws Exception {
        if (CLONE_DEFINITION == null) {
            CLONE_DEFINITION = session.getApplication().getProperty(REApplication.PROPERTY_CLONE_DEFINITION);
        }

        return CLONE_DEFINITION;
    }

    private static final String getCloneDemandeImportation(BSession session, REDemandeRente demande) throws Exception {

        if (demande instanceof REDemandeRenteVieillesse) {
            if (CLONE_DEMANDE_VIEILLESSE_CORRECTION == null) {
                CLONE_DEMANDE_VIEILLESSE_CORRECTION = session.getApplication().getProperty(
                        REApplication.PROPERTY_CLONE_DEM_VIEILLESSE_CORRECTION);
            }
            return CLONE_DEMANDE_VIEILLESSE_CORRECTION;
        } else if (demande instanceof REDemandeRenteSurvivant) {
            if (CLONE_DEMANDE_SURVIVANT_CORRECTION == null) {
                CLONE_DEMANDE_SURVIVANT_CORRECTION = session.getApplication().getProperty(
                        REApplication.PROPERTY_CLONE_DEM_SURVIVANT_CORRECTION);
            }
            return CLONE_DEMANDE_SURVIVANT_CORRECTION;
        } else if (demande instanceof REDemandeRenteAPI) {
            if (CLONE_DEMANDE_API_CORRECTION == null) {
                CLONE_DEMANDE_API_CORRECTION = session.getApplication().getProperty(
                        REApplication.PROPERTY_CLONE_DEM_API_CORRECTION);
            }
            return CLONE_DEMANDE_API_CORRECTION;
        } else if (demande instanceof REDemandeRenteInvalidite) {
            if (CLONE_DEMANDE_INVALIDITE_CORRECTION == null) {
                CLONE_DEMANDE_INVALIDITE_CORRECTION = session.getApplication().getProperty(
                        REApplication.PROPERTY_CLONE_DEM_INVALIDITE_CORRECTION);
            }
            return CLONE_DEMANDE_INVALIDITE_CORRECTION;
        } else {
            throw new REReglesException(session.getLabel("ERREUR_TYPE_DEM_INCORRECT") + demande.getIdDemandeRente());

        }
    }

    private static final String getCloneDemandeImportationForCopie(BSession session, REDemandeRente demande)
            throws Exception {

        if (demande instanceof REDemandeRenteAPI) {
            if (CLONE_DEMANDE_API_COPIE == null) {
                CLONE_DEMANDE_API_COPIE = session.getApplication().getProperty(
                        REApplication.PROPERTY_CLONE_DEM_API_COPIE);
            }
            return CLONE_DEMANDE_API_COPIE;
        } else if (demande instanceof REDemandeRenteInvalidite) {
            if (CLONE_DEMANDE_INVALIDITE_COPIE == null) {
                CLONE_DEMANDE_INVALIDITE_COPIE = session.getApplication().getProperty(
                        REApplication.PROPERTY_CLONE_DEM_INVALIDITE_COPIE);
            }
            return CLONE_DEMANDE_INVALIDITE_COPIE;

        } else if (demande instanceof REDemandeRenteVieillesse) {
            if (CLONE_DEMANDE_VIEILLESSE_COPIE == null) {
                CLONE_DEMANDE_VIEILLESSE_COPIE = session.getApplication().getProperty(
                        REApplication.PROPERTY_CLONE_DEM_VIEILLESSE_COPIE);
            }
            return CLONE_DEMANDE_VIEILLESSE_COPIE;

        } else if (demande instanceof REDemandeRenteSurvivant) {
            if (CLONE_DEMANDE_SURVIVANT_COPIE == null) {
                CLONE_DEMANDE_SURVIVANT_COPIE = session.getApplication().getProperty(
                        REApplication.PROPERTY_CLONE_DEM_SURVIVANT_COPIE);
            }
            return CLONE_DEMANDE_SURVIVANT_COPIE;
        } else {
            throw new REReglesException(session.getLabel("ERREUR_TYPE_DEM_INCORRECT") + demande.getIdDemandeRente());

        }
    }

    private REDemandeRegles() {
        // peut pas creer d'instances
    }

}
