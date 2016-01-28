package globaz.naos.suivi;

import globaz.leo.constantes.ILEConstantes;
import globaz.naos.db.affiliation.AFAffiliation;

public class AFSuiviRadiationRetroactive extends AFSuiviDemRevBilan {

    @Override
    public void genererControle(AFAffiliation entity) throws Exception {
        if ((entity != null) && (entity instanceof AFAffiliation)) {
            AFAffiliation aff = entity;
            if (this.isAlreadySent(aff) == null) {
                super.genererControle(aff);
            }
        }
    }

    @Override
    public void genererControle(AFAffiliation entity, String annee, String eMail) throws Exception {
        if ((entity != null) && (entity instanceof AFAffiliation)) {
            AFAffiliation aff = entity;
            if (this.isAlreadySent(aff, String.valueOf(annee)) == null) {
                super.genererControle(aff, String.valueOf(annee), eMail);
            }
        }
    }

    @Override
    public String getDefinitionFormule() {
        return ILEConstantes.CS_DEBUT_RADIATION_RETROACTIVE;
    }

    @Override
    public String getIdDestinataire(AFAffiliation affiliation) {
        return affiliation.getIdTiers();
    }

    @Override
    public boolean isAffiliationConcerne(AFAffiliation affiliation) {
        // toutes les affiliations ont concernées
        return true;
    }

    @Override
    protected Boolean isGenererEtapeSuivante(AFAffiliation affiliation) throws Exception {
        return new Boolean(false);
    }

}
