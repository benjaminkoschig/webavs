/*
 * Créé le 13 oct. 05
 */
package globaz.ij.regles;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.application.IJApplication;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.lots.IJLot;
import globaz.ij.db.prestations.IJCotisation;
import globaz.ij.db.prestations.IJCotisationManager;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJRepartitionPaiements;
import globaz.ij.db.prestations.IJRepartitionPaiementsManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.factory.PRCloneFactory;
import java.util.HashSet;
import java.util.Set;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une classe qui propose des méthodes pour gérer le cycle de vie des prestations.
 * </p>
 * 
 * <p>
 * Les prestations peuvent être de deux types (famille IJTYPEPRES):
 * </p>
 * 
 * <dl>
 * <dt>CS_NORMAL</dt>
 * <dd>Pour toutes les prestations normales</dd>
 * 
 * <dt>CS_RESTITUTION</dt>
 * <dd>Pour toutes les prestations qui restituent une autre prestation</dd>
 * </dl>
 * 
 * <p>
 * Les prestations peuvent être dans cinq états (famille de cs IJETATPRES):
 * </p>
 * 
 * <dl>
 * <dt>CS_ATTENTE</dt>
 * <dd>Lorsqu'une prestation normales est modifiée, elle passe dans cet état.</dd>
 * 
 * <dt>CS_VALIDE</dt>
 * <dd>Etat d'une prestation fraichement créée</dd>
 * 
 * <dt>CS_MIS_EN_LOT</dt>
 * <dd>Etat d'une prestation qui vient d'être ajoutée dans un lot</dd>
 * 
 * <dt>CS_DEFINITIF</dt>
 * <dd>Etat d'un prestation dans un lot qui vient d'être communiqué</dd>
 * 
 * <dt>CS_ANNULE</dt>
 * <dd>Etat d'une prestation qui a été annulée AVANT d'être mise en lot. Cet état permet d'éviter que des prestations ne
 * soient versées puis immédiatement restituées lors d'une correction.</dd>
 * </dl>
 * 
 * <p>
 * Le cycle de vie général d'une prestation est le suivant:
 * </p>
 * 
 * <p>
 * <img src="doc-files/prestation.gif" alt="Cycle de vie général d'une prestation" title="Cycle de vie général d'une
 * prestation">
 * </p>
 * 
 * @author dvh
 */
public final class IJPrestationRegles {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static String CLONE_DEFINITION;
    private static String CLONE_PREST_COP;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Annule une prestation.
     * 
     * <p>
     * Note: le lot dans lequel pourrait se trouver la prestation n'est pas remis dans l'état ouvert.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prestation
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void annuler(BSession session, BITransaction transaction, IJPrestation prestation)
            throws Exception {

        // Une prestations dans l'état DEFINITIF ne peut en aucun cas passer
        // dans l'état annulé.
        if (IIJPrestation.CS_DEFINITIF.equals(prestation.getCsEtat())) {
            return;
        } else {
            prestation.setCsEtat(IIJPrestation.CS_ANNULE);
            prestation.setIdLot("0");
            prestation.update(transaction);
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Si la prestation a ete mise en lot, l'enleve de ce lot et remet le lot dans l'état ouvert.
     * 
     * <p>
     * Le nouvel état de la prestation n'est pas sauvé dans la base.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prestation
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void annulerMiseEnLot(BSession session, BITransaction transaction, IJPrestation prestation)
            throws Exception {
        if (IIJPrestation.CS_MIS_EN_LOT.equals(prestation.getCsEtat())
                || !JadeStringUtil.isIntegerEmpty(prestation.getIdLot())) {
            if (!JadeStringUtil.isIntegerEmpty(prestation.getIdLot())) {
                // remettre le lot dans l'etat ouvert
                IJLot lot = new IJLot();

                lot.setIdLot(prestation.getIdLot());
                lot.setSession(session);
                lot.retrieve(transaction);

                IJLotsRegles.setEtatOuvert(session, transaction, lot);
            }

            // enleve la prestation du lot
            prestation.setIdLot("0");
            prestation.setCsEtat(IIJPrestation.CS_VALIDE);
        }
    }

    /**
     * copie une prestation vers une base donnée.
     * 
     * <p>
     * La prestation est clonée et la base d'indemnisation vers laquelle elle pointe est mise à jour.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prestation
     *            DOCUMENT ME!
     * @param baseDest
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void copier(BSession session, BITransaction transaction, IJPrestation prestation,
            IJBaseIndemnisation baseDest) throws Exception {
        PRCloneFactory cf = PRCloneFactory.getInstance();
        IJPrestation copie = (IJPrestation) cf.clone(getCloneDefinition(session), session, transaction, prestation,
                getClonePrestationId(session), IIJPrestation.CLONE_NORMAL);

        copie.setIdBaseIndemnisation(baseDest.getIdBaseIndemisation());
        copie.update(transaction);
    }

    private static final String getCloneDefinition(BSession session) throws Exception {
        if (CLONE_DEFINITION == null) {
            CLONE_DEFINITION = session.getApplication().getProperty(IJApplication.PROPERTY_CLONE_DEFINITION);
        }

        return CLONE_DEFINITION;
    }

    private static final String getClonePrestationId(BSession session) throws Exception {
        if (CLONE_PREST_COP == null) {
            CLONE_PREST_COP = session.getApplication().getProperty(IJApplication.PROPERTY_CLONE_PREST_COP);
        }

        return CLONE_PREST_COP;
    }

    /**
     * renvoie, en fonction de l'état transmis en parametre, les etats qui ne doivent pas apparaître pour une
     * modification manuelle de la prestation.
     * 
     * @param csEtatPrestation
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut except etat
     */
    public static final Set getExceptEtat(String csEtatPrestation) {
        Set except = new HashSet();

        if (!csEtatPrestation.equals(IIJPrestation.CS_DEFINITIF)) {
            except.add(IIJPrestation.CS_DEFINITIF);

            if (!csEtatPrestation.equals(IIJPrestation.CS_MIS_EN_LOT)) {
                except.add(IIJPrestation.CS_MIS_EN_LOT);
            }
        }

        return except;
    }

    /**
     * Détermine si la prestation peut être mise en lot.
     * 
     * @param prestation
     *            une IJPrestation
     * 
     * @return true ou false
     */
    public static final boolean isMettableEnLot(IJPrestation prestation) {
        return IIJPrestation.CS_VALIDE.equals(prestation.getCsEtat());
    }

    /**
     * getter pour l'attribut modifiable.
     * 
     * @param prestation
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut modifiable
     */
    public static final boolean isModifierPermis(IJPrestation prestation) {
        return !IIJPrestation.CS_DEFINITIF.equals(prestation.getCsEtat());
    }

    /**
     * getter pour l'attribut supprimable.
     * 
     * @param prestation
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut supprimable
     */
    public static final boolean isSupprimerPermis(IJPrestation prestation) {
        return IIJPrestation.CS_VALIDE.equals(prestation.getCsEtat())
                && IIJPrestation.CS_NORMAL.equals(prestation.getCsType());
    }

    /**
     * Restitue entièrement une prestation.
     * 
     * <p>
     * Crée une prestation de restitution en inversant les montants de la prestation, des répartitions de paiement et
     * des cotisations.
     * </p>
     * 
     * <p>
     * La prestation est rattachée à la base d'indemnisation portant l'identifiant 'idBaseIndemnisation'.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prestation
     *            la prestation a restituer
     * @param idBaseIndemnisation
     *            l'id de la base d'indemnisation à laquelle rattacher la restitution.
     * 
     * @return la restitution
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final IJPrestation restituer(BSession session, BITransaction transaction, IJPrestation prestation,
            String idBaseIndemnisation) throws Exception {
        // créer la prestation
        IJPrestation restitution = new IJPrestation();

        restitution.setCsEtat(IIJPrestation.CS_VALIDE);
        restitution.setCsType(IIJPrestation.CS_RESTITUTION);
        restitution.setDateDebut(prestation.getDateDebut());
        restitution.setDateDecompte(prestation.getDateDecompte());
        restitution.setDateFin(prestation.getDateFin());
        restitution.setDroitAcquis(prestation.getDroitAcquis());
        restitution.setIdBaseIndemnisation(idBaseIndemnisation);
        restitution.setIdIJCalculee(prestation.getIdIJCalculee());
        restitution.setNombreJoursExt(prestation.getNombreJoursExt());
        restitution.setNombreJoursInt(prestation.getNombreJoursInt());

        // inversion des montants
        restitution.setMontantBrut(String.valueOf(-JadeStringUtil.toDouble(prestation.getMontantBrut())));
        restitution.setMontantBrutExterne(String.valueOf(-JadeStringUtil.toDouble(prestation.getMontantBrutExterne())));
        restitution.setMontantBrutIncapPartielle(String.valueOf(-JadeStringUtil.toDouble(prestation
                .getMontantBrutIncapPartielle())));
        restitution.setMontantBrutInterne(String.valueOf(-JadeStringUtil.toDouble(prestation.getMontantBrutInterne())));
        restitution.setMontantJournalierExterne(String.valueOf(-JadeStringUtil.toDouble(prestation
                .getMontantJournalierExterne())));
        restitution.setMontantJournalierInterne(String.valueOf(-JadeStringUtil.toDouble(prestation
                .getMontantJournalierInterne())));

        // insertion dans la base
        restitution.wantCallValidate(false);
        restitution.setSession(session);
        restitution.add(transaction);

        // copie des répartitions de paiements
        IJRepartitionPaiementsManager repartitions = new IJRepartitionPaiementsManager();
        // HashMap origToCopy = new HashMap();

        repartitions.setSession(session);
        repartitions.setForIdPrestation(prestation.getIdPrestation());
        repartitions.setOrderBy(IJRepartitionPaiements.FIELDNAME_IDPARENT);
        repartitions.setForParentOnly("TRUE");
        repartitions.find(transaction);

        for (int idRepartition = 0; idRepartition < repartitions.size(); ++idRepartition) {
            IJRepartitionPaiements repartition = (IJRepartitionPaiements) repartitions.get(idRepartition);
            IJRepartitionPaiements nouvelleRepartition = new IJRepartitionPaiements();

            nouvelleRepartition.setDateValeur(repartition.getDateValeur());
            nouvelleRepartition.setIdAffilie(repartition.getIdAffilie());
            nouvelleRepartition.setIdDomaineAdressePaiement(repartition.getIdDomaineAdressePaiement());
            nouvelleRepartition.setIdPrestation(restitution.getIdPrestation());
            nouvelleRepartition.setIdTiers(repartition.getIdTiers());
            nouvelleRepartition.setIdTiersAdressePaiement(repartition.getIdTiersAdressePaiement());
            nouvelleRepartition.setNom(repartition.getNom());
            nouvelleRepartition.setIdSituationProfessionnelle(repartition.getIdSituationProfessionnelle());
            nouvelleRepartition.setTypePaiement(repartition.getTypePaiement());

            // inversion des montants
            nouvelleRepartition.setMontantBrut(String.valueOf(-JadeStringUtil.toDouble(repartition.getMontantBrut())));
            nouvelleRepartition.setMontantNet(String.valueOf(-JadeStringUtil.toDouble(repartition.getMontantNet())));
            nouvelleRepartition.setMontantVentile(String.valueOf(-JadeStringUtil.toDouble(repartition
                    .getMontantVentile())));

            // if (!JAUtil.isIntegerEmpty(repartition.getIdParent())) {
            // nouvelleRepartition.setIdParent((String)
            // origToCopy.get(repartition.getIdParent()));
            // }

            // insertion dans la base
            nouvelleRepartition.wantCallValidate(false);
            nouvelleRepartition.wantMiseAJourLot(false);
            nouvelleRepartition.setSession(session);
            nouvelleRepartition.add(transaction);
            // origToCopy.put(repartition.getIdRepartitionPaiement(),
            // nouvelleRepartition.getIdRepartitionPaiement());

            // copie des cotisations pour cette répartition de paiement
            IJCotisationManager cotisations = new IJCotisationManager();

            cotisations.setForIdRepartitionPaiements(repartition.getIdRepartitionPaiement());
            cotisations.setSession(session);
            cotisations.find(transaction);

            for (int idCotisation = 0; idCotisation < cotisations.size(); ++idCotisation) {
                IJCotisation cotisation = (IJCotisation) cotisations.get(idCotisation);
                IJCotisation nouvelleCotisation = new IJCotisation();

                nouvelleCotisation.setDateDebut(cotisation.getDateDebut());
                nouvelleCotisation.setDateFin(cotisation.getDateFin());
                nouvelleCotisation.setIdExterne(cotisation.getIdExterne());
                nouvelleCotisation.setIdRepartitionPaiement(nouvelleRepartition.getIdRepartitionPaiement());
                nouvelleCotisation.setIsImpotSource(cotisation.getIsImpotSource());
                nouvelleCotisation.setNomExterne(cotisation.getNomExterne());
                nouvelleCotisation.setTaux(cotisation.getTaux());

                // inversion des montants
                nouvelleCotisation.setMontant(String.valueOf(-JadeStringUtil.toDouble(cotisation.getMontant())));
                nouvelleCotisation
                        .setMontantBrut(String.valueOf(-JadeStringUtil.toDouble(cotisation.getMontantBrut())));

                // insertion dans la base
                nouvelleCotisation.wantCallValidate(false);
                nouvelleCotisation.wantMiseAJourMontantRepartition(false);
                nouvelleCotisation.setSession(session);
                nouvelleCotisation.add(transaction);
            }
        }

        return restitution;
    }

    /**
     * Fait passer cette prestation dans l'état définitif.
     * 
     * <p>
     * Attention: cette méthode ne FAIT PAS passer la base dont cette prestation dépend dans l'état communiqué !
     * </p>
     * 
     * <p>
     * Note: le nouvel etat de la prestation n'est pas sauvegarde dans la base.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prestation
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void setEtatDefinitif(BSession session, BITransaction transaction, IJPrestation prestation)
            throws Exception {
        prestation.setCsEtat(IIJPrestation.CS_DEFINITIF);
    }

    private IJPrestationRegles() {
        // peut pas etre instancie
    }
}
