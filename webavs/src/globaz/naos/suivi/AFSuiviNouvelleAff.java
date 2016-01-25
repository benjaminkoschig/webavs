package globaz.naos.suivi;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.translation.CodeSystem;

public class AFSuiviNouvelleAff extends AFSuiviDemRevBilan {
    private BSession session = null;

    public AFSuiviNouvelleAff(BSession Bsession) {
        setSession(Bsession);
    }

    public void genererControle(BEntity entity, String annee, String eMail) throws Exception {
        if ((entity != null) && (entity instanceof AFAffiliation)) {
            AFAffiliation aff = (AFAffiliation) entity;
            if (this.isAlreadySent(aff, String.valueOf(annee)) == null) {
                super.genererControle(aff, String.valueOf(annee), eMail);
            }
        }
    }

    public void genererControle(String dateDebut, AFAffiliation entity, String eMail) throws Exception {

        // genererControle(entity, annee);
        if ((entity != null) && (entity instanceof AFAffiliation)) {
            AFAffiliation aff = entity;

            if (JadeStringUtil.isEmpty(dateDebut)) {
                dateDebut = aff.getDateDebut();
            }

            int anneeDebut = JadeStringUtil.toInt(dateDebut.substring(6, 10));
            int anneeCrt = JACalendar.today().getYear();

            int nbreSuivis = anneeCrt - anneeDebut;
            // On génère le suivi (demande de revenu pour l'année courrante)
            if (this.isAlreadySent(aff) == null) {
                if (isCotisationAVSPourAnnee(aff, anneeCrt)) {
                    super.genererControle(aff, String.valueOf(anneeCrt), eMail);
                }
            }
            if (aff.isAffiliationAVS()) {
                if (nbreSuivis > 0) {
                    // On génère les suivis(demandes de bilan) pour les autres
                    // années
                    for (int i = 0; i < nbreSuivis; i++) {
                        // Pour les 5 dernières années seulement
                        if (anneeCrt - anneeDebut <= 5) {
                            if (aff.getTypeAffiliation().equalsIgnoreCase(CodeSystem.TYPE_AFFILI_INDEP)
                                    || aff.getTypeAffiliation().equalsIgnoreCase(CodeSystem.TYPE_AFFILI_INDEP_EMPLOY)
                                    || aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_NON_ACTIF)) {
                                if (isCotisationAVSPourAnnee(aff, anneeDebut)) {
                                    AFSuiviNouvelleAffRetroactive suiviAffiliationRetroactive = new AFSuiviNouvelleAffRetroactive();
                                    suiviAffiliationRetroactive.genererControle(aff, String.valueOf(anneeDebut), eMail);
                                }
                            }
                        }
                        anneeDebut++;
                    }
                }
            }
        }
    }

    @Override
    public String getDefinitionFormule() {
        return ILEConstantes.CS_DEBUT_NOUVELLE_AFFILIATION;
    }

    @Override
    public String getIdDestinataire(AFAffiliation affiliation) {
        return affiliation.getIdTiers();
    }

    public BSession getSession() {
        return session;
    }

    @Override
    public boolean isAffiliationConcerne(AFAffiliation affiliation) {
        // toutes les affiliations ont concernées
        return true;
    }

    private boolean isCotisationAVSPourAnnee(AFAffiliation aff, int annee) throws Exception {
        try {
            AFCotisationManager cotisationManager = new AFCotisationManager();
            cotisationManager.setForAffiliationId(aff.getAffiliationId());
            cotisationManager.setSession(getSession());
            cotisationManager.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
            cotisationManager.find(getSession().getCurrentThreadTransaction());
            for (int i = 0; i < cotisationManager.size(); i++) {
                AFCotisation cotisation = (AFCotisation) cotisationManager.getEntity(i);
                // Cas 3 : test pour les demandes de revenu et bilan
                // la date de fin d'affiliation peut ne pas être renseignée
                if ((BSessionUtil.compareDateFirstLowerOrEqual(getSession(), "01.01." + annee, cotisation.getDateFin()) || JadeStringUtil
                        .isBlankOrZero(cotisation.getDateFin()))
                        && (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), "31.12." + annee,
                                cotisation.getDateDebut()) || JadeStringUtil.isBlankOrZero("31.12." + annee))) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    protected Boolean isGenererEtapeSuivante(AFAffiliation affiliation) throws Exception {
        return new Boolean(false);
    }

    public void setSession(BSession session) {
        this.session = session;
    }
}
