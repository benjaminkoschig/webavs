package globaz.naos.suivi;

import globaz.leo.constantes.ILEConstantes;
import globaz.naos.db.affiliation.AFAffiliation;

/**
 * classe pour le suivi de l'attestation de l'institut de prévoyance pour un questionnaire LPP
 */
public class AFSuiviAttestIP extends AFSuiviGeneral {

    private String idDestinataire = new String();

    @Override
    public String getDefinitionFormule() {
        return ILEConstantes.CS_DEBUT_SUIVI_ATTESTATION_IP;
    }

    @Override
    public String getIdDestinataire(AFAffiliation affiliation) {
        return idDestinataire;
    }

    @Override
    public boolean isAffiliationConcerne(AFAffiliation affiliation) {
        return ((super.isAffiliationConcerne(affiliation)));
    }

    public void setIdDestinataire(String newIdDestinataire) {
        idDestinataire = newIdDestinataire;
    }
}