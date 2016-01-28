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

public class AFSuiviRadiation extends AFSuiviDemRevBilan {

    private BSession session;

    public AFSuiviRadiation(BSession Bsession) {
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

    public void genererControle(String dateFin, AFAffiliation entity, String eMail) throws Exception {
        if ((entity != null) && (entity instanceof AFAffiliation)) {
            AFAffiliation aff = entity;

            if (JadeStringUtil.isEmpty(dateFin)) {
                dateFin = aff.getDateFin();
            }

            if (!JadeStringUtil.isEmpty(dateFin)) {
                int anneeFin = JadeStringUtil.toInt(dateFin.substring(6, 10));
                int anneeDebut = anneeFin - 5;
                int nbreSuivis = anneeFin - anneeDebut;
                int anneeCrt = JACalendar.today().getYear();
                // On g�n�re de toute fa�on pour la derni�re ann�e / pas si
                // d�finitive
                // Que pour les 5 derni�res ann�es
                if (anneeCrt - anneeFin <= 5) {
                    if (this.isAlreadySent(aff, String.valueOf(anneeFin)) == null) {
                        if (!aff.hasDefinitiveForAnnee(anneeFin)) {
                            if (isCotisationAVSPourAnnee(aff, anneeFin)) {
                                super.genererControle(aff, String.valueOf(anneeFin), eMail);
                            }
                            anneeFin--;
                            nbreSuivis--;
                        }
                    }
                }
                if (nbreSuivis > 0) {
                    int anneeDebutAffiliation = (Integer.valueOf(aff.getDateDebut().substring(6, 10))).intValue();
                    for (int i = 0; i <= nbreSuivis; i++) {
                        // On regarde si il y a une d�cision d�finitive pour
                        // cette ann�e
                        if (!aff.hasDefinitiveForAnnee(anneeFin)) {
                            if (anneeFin >= anneeDebutAffiliation) {
                                // Que pour les 5 derni�res ann�es
                                if (anneeCrt - anneeFin <= 5) {
                                    if (this.isAlreadySent(aff, String.valueOf(anneeFin)) == null) {
                                        if (isCotisationAVSPourAnnee(aff, anneeFin)) {
                                            AFSuiviRadiationRetroactive suiviRadiationRetroactive = new AFSuiviRadiationRetroactive();
                                            suiviRadiationRetroactive.genererControle(aff, String.valueOf(anneeFin),
                                                    eMail);
                                        }
                                        // anneeFin--;
                                    }
                                }
                            }
                        }
                        anneeFin--;
                    }
                }
            }
        }
    }

    @Override
    public String getDefinitionFormule() {
        return ILEConstantes.CS_DEBUT_RADIATION;
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
        // toutes les affiliations ont concern�es
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
                // la date de fin d'affiliation peut ne pas �tre renseign�e
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
