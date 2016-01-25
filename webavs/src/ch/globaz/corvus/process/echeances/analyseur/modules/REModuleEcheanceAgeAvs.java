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
 * Module interne permettant de g�rer la d�tection de l'�ge AVS pour les deux sexe.
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
        // si le tiers est d�c�d�, aucune �ch�ance � retenir
        // ou si le tiers n'est pas du bon sexe, erreur sp�cifique remont�e
        if (!JadeStringUtil.isBlankOrZero(echeancesPourUnTiers.getDateDecesTiers())
                || !csSexe.equals(echeancesPourUnTiers.getCsSexeTiers())) {
            return REReponseModuleAnalyseEcheance.Faux;
        }

        String moisTraitementPlusUn = JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01."
                + getMoisTraitement(), 1));

        switch (REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(csSexe,
                echeancesPourUnTiers.getDateNaissanceTiers(), getMoisTraitement())) {
            case 0:
                // le tiers est � l'�ge AVS dans le mois de traitement
                IRERenteEcheances renteVieillesse = REModuleAnalyseEcheanceUtils.getRenteVieillesseEnCours(
                        echeancesPourUnTiers, getMoisTraitement());
                // recherche des rentes commen�ant le mois suivant
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
                        // si c'est une rente compl�mentaire pour conjoint (33, 43), motif : �ge AVS
                        return REReponseModuleAnalyseEcheance.Vrai(renteComplementaire, motifAgeAv,
                                echeancesPourUnTiers.getIdTiers());
                    } else if (REModuleAnalyseEcheanceUtils.hasRenteAnticipeeDemandantRecalcul(echeancesPourUnTiers,
                            getMoisTraitement())) {
                        // si anticip�e, motif : �ge AVS avec rente anticip�e
                        return REReponseModuleAnalyseEcheance.Vrai(renteVieillesse, motifAgeAvsAvecRenteAnticipee,
                                echeancesPourUnTiers.getIdTiers());
                    } else if ((renteAPIAIEnCoursDansLeMoisDeTraitement != null)
                            && (renteAPIAVSEnCoursPourLeMoisSuivant == null)) {
                        // si rente API-AI, m�me si une rente AVS est en cours sortir le motif �ge AVS avec rente API-AI
                        // AME: seulement si pas de rente API AVS planifi�e pour le mois suivant
                        if (CodePrestation.getCodePrestation(
                                Integer.parseInt(renteAPIAIEnCoursDansLeMoisDeTraitement.getCodePrestation()))
                                .isAPIAI()) {
                            return REReponseModuleAnalyseEcheance.Vrai(renteAPIAIEnCoursDansLeMoisDeTraitement,
                                    motifAgeAvsAvecApi, echeancesPourUnTiers.getIdTiers());
                        }
                    } else {
                        // si elle est standard (non-anticip�e) et en cours, aucune �ch�ance � retenir
                        return REReponseModuleAnalyseEcheance.Faux;
                    }
                } else if (renteAPIAIEnCoursDansLeMoisDeTraitement != null) {
                    // si le tiers est au b�n�fice d'une rente API mais qu'il n'a pas de rente vieillesse en cours
                    // motif : �ge AVS avec une API en cours
                    return REReponseModuleAnalyseEcheance.Vrai(renteAPIAIEnCoursDansLeMoisDeTraitement,
                            motifAgeAvsAvecApi, echeancesPourUnTiers.getIdTiers());
                } else {
                    // cas des rentes anticip�es recalcul�es : si une rente anticip�e valid�e avec de l'anticipation
                    // commence le mois suivant le mois de traitement, c'est que l'anticipation a �t� recalcul�, aucune
                    // �ch�ance � retenir
                    IRERenteEcheances renteRecalculee = REModuleAnalyseEcheanceUtils
                            .getRenteAnticipeeRecalculee(echeancesPourUnTiers);
                    if (renteRecalculee != null) {
                        return REReponseModuleAnalyseEcheance.Faux;
                    }

                    IRERenteEcheances rentePrincipale = REModuleAnalyseEcheanceUtils
                            .getRentePrincipale(echeancesPourUnTiers);
                    // si le dossier a �t� transf�r�, on ne sort pas de motif
                    if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(rentePrincipale.getCsEtatDemandeRente())) {
                        return REReponseModuleAnalyseEcheance.Faux;
                    }

                    // si aucune rente en cours, motif : �ge AVS
                    return REReponseModuleAnalyseEcheance.Vrai(rentePrincipale, motifAgeAv,
                            echeancesPourUnTiers.getIdTiers());

                }
            case 1:
                // le tiers a d�j� l'�ge AVS dans le mois de traitement
                // si pas de rente vieillesse en cours, motif : �ge AVS d�pass�
                // note : seul les droits survenant apr�s le 1er janvier 2012 sont pris en compte
                // afin d'�viter trop de faux positif avec les reprises de donn�es (date de d�c�s manquantes)
                IRERenteEcheances renteVieillesseEnCours = REModuleAnalyseEcheanceUtils.getRenteVieillesseEnCours(
                        echeancesPourUnTiers, getMoisTraitement());
                if ((renteVieillesseEnCours == null)
                        || REModuleAnalyseEcheanceUtils.hasRenteAnticipeeDemandantRecalcul(echeancesPourUnTiers,
                                getMoisTraitement())) {

                    // Dans le cas d'une rente compl�mentaire vieillesse en cours, doit retourner faux, car ce cas est
                    // assez
                    // courant (dans le cas d'une b�n�ficiaire f�minine) et aura une implementation propre dans
                    // l'application (rente compl�mentaire vieillesse perdure) dans un futur proche
                    IRERenteEcheances renteVieillesseComplementaire = REModuleAnalyseEcheanceUtils
                            .getRenteVieillesseComplementaire(echeancesPourUnTiers);
                    if (renteVieillesseComplementaire != null) {
                        return REReponseModuleAnalyseEcheance.Faux;
                    }

                    // On ignore les droit survenus avant le 01.01.2012 (si rien sp�cifi� en propri�t� en base, sinon la
                    // date d�finie par "corvus.date.mise.en.production.rentes"), ceci afin d'�viter que tous les cas de
                    // reprises de donn�es sans date de d�c�s ne viennent polluer la liste.
                    if (REModuleAnalyseEcheanceUtils.isSurvenanceDroitAVSAvant(echeancesPourUnTiers,
                            dateDebutPriseEnCompteAgeAvsDepasse)) {
                        return REReponseModuleAnalyseEcheance.Faux;
                    }

                    // on ignore les cas d'ajournement, car g�r� par le module d'ajournement
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

                    // on ignore les cas qui on d�j� �t� trait�s (une rente diminu� avec date de fin dans le mois de
                    // traitement et une rente reprenant le droit le mois suivant)
                    if (REModuleAnalyseEcheanceUtils.hasRenteEnCours(echeancesPourUnTiers, getMoisTraitement())
                            && REModuleAnalyseEcheanceUtils.hasRenteVieillesseValideMoisSuivant(echeancesPourUnTiers,
                                    getMoisTraitement())) {
                        return REReponseModuleAnalyseEcheance.Faux;
                    }

                    // on ignore les demandes transf�r�es
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
