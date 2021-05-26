/*
 * Créé le 19 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.acor.adapter.plat;

import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APBasesCalculBuilder;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BSession;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRFichierACORPrinter;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe de base pour l'adaptation d'un droit en liste de fichiers ACOR.
 * </p>
 * 
 * <p>
 * Cette classe fournit les méthodes de base permettant la récupération des données relatives à un droit ainsi que la
 * sortie du fichier employeurs qui est le même pour maternité et apg.
 * </p>
 * 
 * @author vre
 */
public abstract class APAbstractACORDroitAdapter extends PRAbstractPlatAdapter {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    protected static final String CA_TYPE_SALAIRE_HORAIRE = "7";
    protected static final String NF_EMPLOYEURS = "EMPLOYEURS";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private List basesCalcul;

    protected APDroitLAPG droit;

    private PRFichierACORPrinter fichierEmployeurPrinter = null;
    private List situationsProfessionnelles;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APAbstractACORDroitAdapter.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param droit
     *            DOCUMENT ME!
     */
    protected APAbstractACORDroitAdapter(BSession session, APDroitLAPG droit) {
        super(session);
        this.droit = droit;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * retourne la base de calcul mentionnée.
     * 
     * @param idBaseCalcul
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public APBaseCalcul baseCalcul(int idBaseCalcul) throws PRACORException {
        return (APBaseCalcul) basesCalcul().get(idBaseCalcul);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public List basesCalcul() throws PRACORException {
        if (basesCalcul == null) {
            try {
                basesCalcul = APBasesCalculBuilder.of(session, droit).createBasesCalcul();
            } catch (Exception e) {
                throw new PRACORException(session.getLabel("ERREUR_CREATION_BASES_CALCUL"), e);
            }
        }

        return basesCalcul;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected PRFichierACORPrinter fichierEmployeurPrinter() {
        if (fichierEmployeurPrinter == null) {
            fichierEmployeurPrinter = new APFichierEmployeurPrinter(this, NF_EMPLOYEURS);
        }

        return fichierEmployeurPrinter;
    }

    /**
     * getter pour l'attribut droit
     * 
     * @return la valeur courante de l'attribut droit
     */
    public APDroitLAPG getDroit() {
        return droit;
    }

    /**
     * getter pour l'attribut montant minimum paye employeur
     * 
     * @return la valeur courante de l'attribut montant minimum paye employeur
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public String getMontantMinimumPayeEmployeur() throws PRACORException {
        try {
            if (droit instanceof APDroitAPG) {
                return session.getApplication().getProperty(APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_EMPLOYEUR_APG);
            } else {
                return session.getApplication().getProperty(APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_EMPLOYEUR_MAT);
            }
        } catch (Exception e) {
            throw new PRACORException(FWMessageFormat.format(session.getLabel("ERREUR_CHARGEMENT_PROPRIETE"),
                    APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_EMPLOYEUR_APG), e);
        }
    }

    /**
     * @see globaz.prestation.acor.plat.PRAbstractPlatAdapter#getTypeCalcul()
     */
    @Override
    public String getTypeCalcul() {
        return PRACORConst.CA_TYPE_CALCUL_STANDARD;
    }

    /**
     * @see globaz.prestation.acor.plat.PRAbstractPlatAdapter#getTypeDemande()
     */
    @Override
    public String getTypeDemande() {
        if (droit instanceof APDroitAPG) {
            return PRACORConst.CA_TYPE_DEMANDE_APG;
        } else {
            return PRACORConst.CA_TYPE_DEMANDE_MATERNITE;
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param noAffilie
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public int nbContrats(String noAffilie) throws PRACORException {
        int retValue = 0;

        for (Iterator iter = situationsProfessionnelles().iterator(); iter.hasNext();) {
            try {
                if (((APSituationProfessionnelle) iter.next()).loadEmployeur().loadNumero().equals(noAffilie)) {
                    retValue += 1;
                }
            } catch (Exception e) {
                throw new PRACORException(getSession().getLabel("ERROR_CHARGEMENT_SITUATION_PROF"), e);
            }

        }

        return retValue;
    }

    /**
     * Retourne le no AVS de l'assuré ayant fait la demande de prestation.
     * 
     * @return le no AVS de l'assuré ayant fait la demande de prestation.
     * 
     * @throws PRACORException
     */
    @Override
    public String numeroAVSAssure() throws PRACORException {
        return tiers().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param idSituationProfessionnelle
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public APSituationProfessionnelle situationProfessionnelle(int idSituationProfessionnelle) throws PRACORException {
        return (APSituationProfessionnelle) situationsProfessionnelles().get(idSituationProfessionnelle);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public List situationsProfessionnelles() throws PRACORException {
        if (situationsProfessionnelles == null) {
            APSituationProfessionnelleManager mgr = new APSituationProfessionnelleManager();

            mgr.setSession(session);
            mgr.setForIdDroit(droit.getIdDroit());

            try {
                mgr.find();
            } catch (Exception e) {
                throw new PRACORException(session.getLabel("ERREUR_CHARGEMENT_SITUATION_PROFESSIONNELLE"), e);
            }

            situationsProfessionnelles = mgr.getContainer();
        }

        return situationsProfessionnelles;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public PRTiersWrapper tiers() throws PRACORException {
        try {
            return droit.loadDemande().loadTiers();
        } catch (Exception e) {
            throw new PRACORException(session.getLabel("ERREUR_CHARGEMENT_TIERS"), e);
        }
    }
}
