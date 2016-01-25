package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * Module interne permettant de gérer la détection de l'âge AVS pour les deux sexe.
 * 
 * @see REModuleEcheanceHommeAgeAvs
 * @see REModuleEcheanceFemmeAgeAvs
 * @author PBA
 */
public class REModuleEcheanceAgeAvs extends REModuleAnalyseEcheance {

    private String csSexe;
    private final String dateDebutPriseEnCompteAgeAvsDepasse;
    private final REModuleAnalyseEcheance moduleConjointAgeAvs;
    private final REMotifEcheance motifAgeAv;
    private final REMotifEcheance motifAgeAvDepasse;
    private final REMotifEcheance motifAgeAvsAnticipationDepassee;
    private final REMotifEcheance motifAgeAvsAvecApi;
    private final REMotifEcheance motifAgeAvsAvecRenteAnticipee;

    public REModuleEcheanceAgeAvs(BISession session, String moisTraitement, String csSexe,
            String dateDebutPriseEnCompteAgeAvsDepasse, REMotifEcheance motifAgeAvs,
            REMotifEcheance motifAgeAvsDepasse, REMotifEcheance motifAgeAvsAvecApi,
            REMotifEcheance motifAgeAvsAvecRenteAnticipee, REMotifEcheance motifAgeAvsAnticipationDepassee,
            boolean wantConjointAgeAvsDepasse) {
        super(session, moisTraitement);
        this.csSexe = csSexe;
        this.dateDebutPriseEnCompteAgeAvsDepasse = dateDebutPriseEnCompteAgeAvsDepasse;

        moduleConjointAgeAvs = new REModuleEcheanceConjointAgeAvs(session, moisTraitement, wantConjointAgeAvsDepasse);

        motifAgeAv = motifAgeAvs;
        motifAgeAvDepasse = motifAgeAvsDepasse;
        this.motifAgeAvsAvecApi = motifAgeAvsAvecApi;
        this.motifAgeAvsAvecRenteAnticipee = motifAgeAvsAvecRenteAnticipee;
        this.motifAgeAvsAnticipationDepassee = motifAgeAvsAnticipationDepassee;
    }

    @Override
    protected REReponseModuleAnalyseEcheance analyserEcheance(IREEcheances echeancesPourUnTiers) {
        // si le tiers est décédé, aucune échéance à retenir
        // ou si le tiers n'est pas du bon sexe, erreur spécifique remontée
        if (!JadeStringUtil.isBlankOrZero(echeancesPourUnTiers.getDateDecesTiers())
                || !csSexe.equals(echeancesPourUnTiers.getCsSexeTiers())) {
            return REReponseModuleAnalyseEcheance.Faux;
        }

        String moisTraitementPlusUn = JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01."
                + getMoisTraitement(), 1));

        switch (REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(csSexe,
                echeancesPourUnTiers.getDateNaissanceTiers(), getMoisTraitement())) {
            case 0:
                // le tiers est à l'âge AVS dans le mois de traitement
                IRERenteEcheances renteVieillesse = REModuleAnalyseEcheanceUtils.getRenteVieillesseEnCours(
                        echeancesPourUnTiers, getMoisTraitement());
                // recherche des rentes commençant le mois suivant
                if (renteVieillesse == null) {
                    renteVieillesse = REModuleAnalyseEcheanceUtils.getRenteVieillesseEnCours(echeancesPourUnTiers,
                            moisTraitementPlusUn);
                }
                IRERenteEcheances renteAPIAIEnCoursDansLeMoisDeTraitement = REModuleAnalyseEcheanceUtils
                        .getRenteAPIAIEnCoursDurantLeMois(echeancesPourUnTiers, getMoisTraitement());
                IRERenteEcheances renteAPIAVSEnCoursPourLeMoisSuivant = REModuleAnalyseEcheanceUtils
                        .getRenteAPIAVSEnCoursDurantLeMois(echeancesPourUnTiers, moisTraitementPlusUn);

                if (renteVieillesse != null) {

                    // s'il a une rente vieillesse en cours
                    IRERenteEcheances renteComplementaire = REModuleAnalyseEcheanceUtils
                            .getRenteVieillesseComplementaire(echeancesPourUnTiers);
                    if (renteComplementaire != null) {
                        // si c'est une rente complémentaire pour conjoint (33, 43), motif : âge AVS
                        return REReponseModuleAnalyseEcheance.Vrai(renteComplementaire, motifAgeAv,
                                echeancesPourUnTiers.getIdTiers());
                    } else if (REModuleAnalyseEcheanceUtils.hasRenteAnticipeeDemandantRecalcul(echeancesPourUnTiers,
                            getMoisTraitement())) {
                        // si anticipée, motif : âge AVS avec rente anticipée
                        return REReponseModuleAnalyseEcheance.Vrai(renteVieillesse, motifAgeAvsAvecRenteAnticipee,
                                echeancesPourUnTiers.getIdTiers());
                    } else if ((renteAPIAIEnCoursDansLeMoisDeTraitement != null)
                            && (renteAPIAVSEnCoursPourLeMoisSuivant == null)) {
                        // si rente API-AI, même si une rente AVS est en cours sortir le motif âge AVS avec rente API-AI
                        // AME: seulement si pas de rente API AVS planifiée pour le mois suivant
                        if (CodePrestation.getCodePrestation(
                                Integer.parseInt(renteAPIAIEnCoursDansLeMoisDeTraitement.getCodePrestation()))
                                .isAPIAI()) {
                            return REReponseModuleAnalyseEcheance.Vrai(renteAPIAIEnCoursDansLeMoisDeTraitement,
                                    motifAgeAvsAvecApi, echeancesPourUnTiers.getIdTiers());
                        }
                    } else {
                        // si elle est standard (non-anticipée) et en cours, aucune échéance à retenir
                        return REReponseModuleAnalyseEcheance.Faux;
                    }
                } else if (renteAPIAIEnCoursDansLeMoisDeTraitement != null) {
                    // si le tiers est au bénéfice d'une rente API mais qu'il n'a pas de rente vieillesse en cours
                    // motif : âge AVS avec une API en cours
                    return REReponseModuleAnalyseEcheance.Vrai(renteAPIAIEnCoursDansLeMoisDeTraitement,
                            motifAgeAvsAvecApi, echeancesPourUnTiers.getIdTiers());
                } else {
                    // cas des rentes anticipées recalculées : si une rente anticipée validée avec de l'anticipation
                    // commence le mois suivant le mois de traitement, c'est que l'anticipation a été recalculé, aucune
                    // échéance à retenir
                    IRERenteEcheances renteRecalculee = REModuleAnalyseEcheanceUtils
                            .getRenteAnticipeeRecalculee(echeancesPourUnTiers);
                    if (renteRecalculee != null) {
                        return REReponseModuleAnalyseEcheance.Faux;
                    }

                    IRERenteEcheances rentePrincipale = REModuleAnalyseEcheanceUtils
                            .getRentePrincipale(echeancesPourUnTiers);
                    // si le dossier a été transféré, on ne sort pas de motif
                    if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(rentePrincipale.getCsEtatDemandeRente())) {
                        return REReponseModuleAnalyseEcheance.Faux;
                    }

                    // si aucune rente en cours, motif : âge AVS
                    return REReponseModuleAnalyseEcheance.Vrai(rentePrincipale, motifAgeAv,
                            echeancesPourUnTiers.getIdTiers());

                }
            case 1:
                // le tiers a déjà l'âge AVS dans le mois de traitement
                // si pas de rente vieillesse en cours, motif : âge AVS dépassé
                // note : seul les droits survenant après le 1er janvier 2012 sont pris en compte
                // afin d'éviter trop de faux positif avec les reprises de données (date de décès manquantes)
                IRERenteEcheances renteVieillesseEnCours = REModuleAnalyseEcheanceUtils.getRenteVieillesseEnCours(
                        echeancesPourUnTiers, getMoisTraitement());
                if ((renteVieillesseEnCours == null)
                        || REModuleAnalyseEcheanceUtils.hasRenteAnticipeeDemandantRecalcul(echeancesPourUnTiers,
                                getMoisTraitement())) {

                    // Dans le cas d'une rente complémentaire vieillesse en cours, doit retourner faux, car ce cas est
                    // assez
                    // courant (dans le cas d'une bénéficiaire féminine) et aura une implementation propre dans
                    // l'application (rente complémentaire vieillesse perdure) dans un futur proche
                    IRERenteEcheances renteVieillesseComplementaire = REModuleAnalyseEcheanceUtils
                            .getRenteVieillesseComplementaire(echeancesPourUnTiers);
                    if (renteVieillesseComplementaire != null) {
                        return REReponseModuleAnalyseEcheance.Faux;
                    }

                    // On ignore les droit survenus avant le 01.01.2012 (si rien spécifié en propriété en base, sinon la
                    // date définie par "corvus.date.mise.en.production.rentes"), ceci afin d'éviter que tous les cas de
                    // reprises de données sans date de décès ne viennent polluer la liste.
                    if (REModuleAnalyseEcheanceUtils.isSurvenanceDroitAVSAvant(echeancesPourUnTiers,
                            dateDebutPriseEnCompteAgeAvsDepasse)) {
                        return REReponseModuleAnalyseEcheance.Faux;
                    }

                    // on ignore les cas d'ajournement, car géré par le module d'ajournement
                    if (REReponseModuleAnalyseEcheance.Faux != REModuleAnalyseEcheanceUtils.isRenteAjournee(
                            REModuleAnalyseEcheanceUtils.getRentePrincipale(echeancesPourUnTiers),
                            echeancesPourUnTiers.getDateNaissanceTiers(), echeancesPourUnTiers.getCsSexeTiers(),
                            getMoisTraitement())) {
                        return REReponseModuleAnalyseEcheance.Faux;
                    }

                    if (REModuleAnalyseEcheanceUtils.hasRenteAnticipeeDemandantRecalcul(echeancesPourUnTiers,
                            getMoisTraitement())) {
                        return REReponseModuleAnalyseEcheance.Vrai(
                                REModuleAnalyseEcheanceUtils.getRentePrincipale(echeancesPourUnTiers),
                                motifAgeAvsAnticipationDepassee, echeancesPourUnTiers.getIdTiers());
                    }

                    // on ignore les cas qui on déjà été traités (une rente diminué avec date de fin dans le mois de
                    // traitement et une rente reprenant le droit le mois suivant)
                    if (REModuleAnalyseEcheanceUtils.hasRenteEnCours(echeancesPourUnTiers, getMoisTraitement())
                            && REModuleAnalyseEcheanceUtils.hasRenteVieillesseValideMoisSuivant(echeancesPourUnTiers,
                                    getMoisTraitement())) {
                        return REReponseModuleAnalyseEcheance.Faux;
                    }

                    // on ignore les demandes transférées
                    IRERenteEcheances rentePrincipale = REModuleAnalyseEcheanceUtils
                            .getRentePrincipale(echeancesPourUnTiers);
                    if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(rentePrincipale.getCsEtatDemandeRente())) {
                        return REReponseModuleAnalyseEcheance.Faux;
                    }

                    return REReponseModuleAnalyseEcheance.Vrai(
                            REModuleAnalyseEcheanceUtils.getRentePrincipale(echeancesPourUnTiers), motifAgeAvDepasse,
                            echeancesPourUnTiers.getIdTiers());

                }
        }
        return moduleConjointAgeAvs.analyserEcheance(echeancesPourUnTiers);
    }

    public String getCsSexe() {
        return csSexe;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    @Override
    public void setMoisTraitement(String moisTraitement) {
        super.setMoisTraitement(moisTraitement);
        moduleConjointAgeAvs.setMoisTraitement(moisTraitement);
    }
}
