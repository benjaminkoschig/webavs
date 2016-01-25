package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IRERelationEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

/**
 * <p>
 * Module analysant la situation d'une �ch�ance et regarde si le conjoint du tiers est en �ge vieillesse dans le mois
 * courant
 * </p>
 * Motifs de r�ponses positives possibles :
 * <ul>
 * <li>{@link REMotifEcheance#ConjointArrivantAgeAvs}</li>
 * <li>{@link REMotifEcheance#ConjointAgeAvsDepasse} si l'option {@link #wantAgeAvsDepasse} est activ�e</li>
 * </ul>
 * <p>
 * Si le conjoint est au b�n�fice d'une rente, c'est le module de d�tection de l'�ge AVS selon son sexe qui analysera sa
 * situation.
 * </p>
 * <p>
 * Il est n�cessaire de passer les testes unitaires (REModuleEcheanceConjointAgeAvsTest dans le projet __TestJUnit) si
 * une modification est fait sur ce module.
 * </p>
 * 
 * @see REModuleEcheanceFemmeAgeAvs
 * @see REModuleEcheanceHommeAgeAvs
 * @author PBA
 */
public class REModuleEcheanceConjointAgeAvs extends REModuleAnalyseEcheance {

    /**
     * D�fini si le motif {@link REMotifEcheance#ConjointAgeAvsDepasse} doit �tre retourn� ou non.
     */
    private boolean wantAgeAvsDepasse;

    /**
     * <p>
     * Construit un module ne retournant pas le motif {@link REMotifEcheance#ConjointAgeAvsDepasse}.<br/>
     * C'est le comportement par d�faut qui a �t� choisi, car trop de cas de reprise de donn�es n'ont pas les dates de
     * d�c�s des conjoints et l'�ch�ancier est ainsi "pollu�" par ces cas.
     * </p>
     * <p>
     * Si vous d�sirez tout de m�me avoir le motif {@link REMotifEcheance#ConjointAgeAvsDepasse}, utiliser
     * {@link #REModuleEcheanceConjointAgeAvs(BISession, String, boolean) l'autre constructeur}, ou faites appelle � la
     * m�thode {@link #setWantAgeAvsDepasse(boolean)} apr�s instanciation.
     * </p>
     * 
     * @param session
     * @param moisTraitement
     * @throws Exception
     */
    public REModuleEcheanceConjointAgeAvs(BISession session, String moisTraitement) {
        this(session, moisTraitement, false);
    }

    /**
     * Construit un module retournant les deux types de motifs si {@link #wantAgeAvsDepasse} est <code>true</code>. <br/>
     * Si {@link #wantAgeAvsDepasse} est <code>false</code>, m�me comportement que
     * {@link #REModuleEcheanceConjointAgeAvs(BISession, String) l'autre constructeur}.
     * 
     * @param session
     * @param moisTraitement
     * @param wantAgeAvsDepassee
     * @throws Exception
     */
    public REModuleEcheanceConjointAgeAvs(BISession session, String moisTraitement, boolean wantAgeAvsDepassee) {
        super(session, moisTraitement);
        wantAgeAvsDepasse = wantAgeAvsDepassee;
    }

    @Override
    protected REReponseModuleAnalyseEcheance analyserEcheance(IREEcheances echeancesPourUnTiers) {

        for (IRERelationEcheances relation : echeancesPourUnTiers.getRelations()) {
            // si le sexe du conjoint ou sa date de naissance n'est pas d�fini, on ignore cette relation
            if (JadeStringUtil.isBlankOrZero(relation.getCsSexeConjoint())
                    || JadeStringUtil.isBlankOrZero(relation.getDateNaissanceConjoint())) {
                continue;
            }

            // si le conjoint est d�c�d�, on ignore cette relation
            if (JadeDateUtil.isGlobazDate(relation.getDateDecesConjoint())) {
                continue;
            }

            switch (REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(relation.getCsSexeConjoint(),
                    relation.getDateNaissanceConjoint(), getMoisTraitement())) {
                case 0:
                    // le conjoint est � l'�ge AVS dans le mois de traitement
                    // on v�rifie qu'il n'aie pas de rente en cours (si c'est le cas, il sera trait� par le module
                    // �ge AVS correspondant � son sexe)
                    IRERenteEcheances renteConjoint = hasRenteEnCours(relation);

                    if (renteConjoint != null) {
                        // si le conjoint a des rente en cours, le cas sera trait� par le module
                        // �ge AVS correspondant � son sexe
                        return REReponseModuleAnalyseEcheance.Faux;
                    }
                    IRERenteEcheances rentePrincipale = REModuleAnalyseEcheanceUtils
                            .getRentePrincipale(echeancesPourUnTiers);
                    return REReponseModuleAnalyseEcheance.Vrai(rentePrincipale, REMotifEcheance.ConjointArrivantAgeAvs,
                            relation.getIdTiersConjoint());
                case 1:
                    /*
                     * Le traitement du conjoint lorsque �ge AVS d�pass� n'est pas pris en compte (par d�faut). Trop de
                     * "faux" avec les reprises de donn�es
                     */
                    if (wantAgeAvsDepasse()) {
                        // si le conjoint est d�j� � l'�ge AVS dans le mois courant,
                        // on v�rifie s'il a une rente en cours (si c'est le cas, il sera trait� par le module
                        // �ge AVS correspondant � son sexe)
                        IRERenteEcheances renteConjoint2 = hasRenteEnCours(relation);
                        if (renteConjoint2 != null) {
                            // si le conjoint a d�j� une rente en cours, aucune �ch�ance � retenir
                            return REReponseModuleAnalyseEcheance.Faux;
                        }
                        // si le conjoint n'a pas encore de rente, motif : conjoint �ge AVS d�pass�
                        return REReponseModuleAnalyseEcheance.Vrai(null, REMotifEcheance.ConjointAgeAvsDepasse,
                                relation.getIdTiersConjoint());
                    }
            }
        }
        return REReponseModuleAnalyseEcheance.Faux;
    }

    /**
     * Test effectu� sur la rente :</br> -> La rente doit �tre en �tat valid�e</br> </br> -> La date de d�but du droit
     * doit �tre :</br> --> �gale au mois de traitement + 1 OU</br> --> se situer avant le mois de traitement + 1</br>
     * </br> -> La date de fin de droit doit �tre : --> vide OU</br> --> se situer apr�s le mois de traitement
     * 
     * @param relation
     * @return
     */
    private IRERenteEcheances hasRenteEnCours(IRERelationEcheances relation) {
        for (IRERenteEcheances uneRenteDuConjoint : relation.getRentesDuConjoint()) {
            String moisTtmInc = JadeDateUtil.addMonths("01." + getMoisTraitement(), 1);
            moisTtmInc = moisTtmInc.substring(3);
            if (IREPrestationAccordee.CS_ETAT_VALIDE.equals(uneRenteDuConjoint.getCsEtat())
                    && (JadeDateUtil.isDateMonthYearAfter(moisTtmInc, uneRenteDuConjoint.getDateDebutDroit()) || moisTtmInc
                            .equals(uneRenteDuConjoint.getDateDebutDroit()))

                    && (JadeDateUtil.isDateMonthYearBefore(getMoisTraitement(), uneRenteDuConjoint.getDateFinDroit()) || JadeStringUtil
                            .isBlankOrZero(uneRenteDuConjoint.getDateFinDroit()))) {
                return uneRenteDuConjoint;
            }
        }
        return null;
    }

    /**
     * D�fini si le motif {@link REMotifEcheance#ConjointAgeAvsDepasse} doit �tre retourn� ou non.
     * 
     * @param wantAgeAvsDepasse
     *            <code>true</code> si le motif doit �tre retourn�, sinon <code>false</code>
     */
    public void setWantAgeAvsDepasse(boolean wantAgeAvsDepasse) {
        this.wantAgeAvsDepasse = wantAgeAvsDepasse;
    }

    public boolean wantAgeAvsDepasse() {
        return wantAgeAvsDepasse;
    }
}
