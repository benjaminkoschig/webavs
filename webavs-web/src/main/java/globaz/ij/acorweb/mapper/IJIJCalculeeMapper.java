package globaz.ij.acorweb.mapper;

import acor.ij.xsd.ij.out.FCalcul;
import ch.globaz.common.persistence.EntityService;
import ch.globaz.common.util.Strings;
import globaz.ij.api.prestations.IIJPetiteIJCalculee;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prestations.IJGrandeIJCalculee;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJPetiteIJCalculee;
import globaz.ij.db.prononces.IJPetiteIJ;
import globaz.ij.db.prononces.IJPrononce;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRAcorDomaineException;
import globaz.prestation.tools.PRDateFormater;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class IJIJCalculeeMapper {

    private final String nss;
    private final IJPrononce prononce;
    private final EntityService entityService;

    public IJIJCalculee map(FCalcul.Cycle.BasesCalcul basesCalcul) {
        IJIJCalculee ijijCalculee;

        if(PRACORConst.CA_TYPE_IJ_GRANDE.equals(Strings.toStringOrNull(basesCalcul.getGenre()))){
            ijijCalculee = createAndMapGrandeIJ(basesCalcul);
        }else if(PRACORConst.CA_TYPE_IJ_PETITE.equals(Strings.toStringOrNull(basesCalcul.getGenre()))){
            ijijCalculee = createAndMapPetiteIJ(basesCalcul, prononce);
        }else{
            throw new PRAcorDomaineException("Réponse invalide : Type d' IJ non réconnu.");
        }

        return mapIJIJCalculee(basesCalcul, nss, prononce, entityService, ijijCalculee);
    }

    private IJIJCalculee mapIJIJCalculee(FCalcul.Cycle.BasesCalcul basesCalcul, String nss, IJPrononce prononce, EntityService entityService, IJIJCalculee ijijCalculee) {

        if(Objects.equals(1, basesCalcul.getDroitPrestationEnfant())){
            ijijCalculee.setIsDroitPrestationPourEnfant(Boolean.TRUE);
        }else if(Objects.equals(0, basesCalcul.getDroitPrestationEnfant())){
            ijijCalculee.setIsDroitPrestationPourEnfant(Boolean.FALSE);
        }else{
            ijijCalculee.setIsDroitPrestationPourEnfant(null);
        }

        ijijCalculee.setNoAVS(nss);
        ijijCalculee.setOfficeAI(String.valueOf(basesCalcul.getOfficeAi()));
        ijijCalculee.setCsGenreReadaptation(PRACORConst.caGenreReadaptationToCS(entityService.getSession(), Strings.toStringOrNull(basesCalcul.getGenreReadaptation())));
        ijijCalculee.setDatePrononce(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(Strings.toStringOrNull(basesCalcul.getDatePrononce())));
        ijijCalculee.setDateDebutDroit(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(Strings.toStringOrNull(basesCalcul.getDebutDroit())));
        ijijCalculee.setDateFinDroit(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(Strings.toStringOrNull(basesCalcul.getFinDroit())));
        if(basesCalcul.getRevenuDeterminant() != null) {
            ijijCalculee.setRevenuDeterminant(Strings.toStringOrNull(basesCalcul.getRevenuDeterminant().getRevenuJournalier()));
            ijijCalculee.setDateRevenu(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(Strings.toStringOrNull(basesCalcul.getRevenuDeterminant().getDate())));
        }

        ijijCalculee.setMontantBase(Strings.toStringOrNull(basesCalcul.getMontantBase()));
        if(basesCalcul.getRevenuReadaptation() != null) {
            ijijCalculee.setRevenuJournalierReadaptation(Strings.toStringOrNull(basesCalcul.getRevenuReadaptation().getRevenuJournalier()));
            ijijCalculee.setDemiIJACBrut(Strings.toStringOrNull(basesCalcul.getRevenuReadaptation().getACDemiBrut()));
        }
        ijijCalculee.setCsStatutProfessionnel(PRACORConst.caStatutProfessionnelToCS(entityService.getSession(), Strings.toStringOrNull(basesCalcul.getStatut())));

        ijijCalculee.setDifferenceRevenu(Strings.toStringOrNull(basesCalcul.getDifferenceRevenu()));
        ijijCalculee.setIdPrononce(prononce.getIdPrononce());
        if(basesCalcul.getGenre() == 1){
            ijijCalculee.setCsTypeIJ(IIJPrononce.CS_GRANDE_IJ);
        }else if(basesCalcul.getGenre() == 2){
            ijijCalculee.setCsTypeIJ(IIJPrononce.CS_PETITE_IJ);
        }
        ijijCalculee.setNoRevision(Strings.toStringOrNull(basesCalcul.getRevision()));
        entityService.add(ijijCalculee);
        return ijijCalculee;
    }

    private IJIJCalculee createAndMapPetiteIJ(FCalcul.Cycle.BasesCalcul basesCalcul, IJPrononce prononce) {
        IJIJCalculee ijijCalculee;
        IJPetiteIJCalculee petiteIJ = new IJPetiteIJCalculee();
        petiteIJ.setCsModeCalcul(Strings.toStringOrNull(mapModeCalcul(basesCalcul, prononce)));
        ijijCalculee = petiteIJ;
        return ijijCalculee;
    }

    private IJIJCalculee createAndMapGrandeIJ(FCalcul.Cycle.BasesCalcul basesCalcul) {
        IJIJCalculee ijijCalculee;
        IJGrandeIJCalculee grandeIJ = new IJGrandeIJCalculee();
        grandeIJ.setMontantIndemniteEnfant(Strings.toStringOrNull(basesCalcul.getMontantEnfants()));
        grandeIJ.setNbEnfants(Strings.toStringOrNull(basesCalcul.getNEnfants()));
        ijijCalculee = grandeIJ;
        return ijijCalculee;
    }

    private String mapModeCalcul(FCalcul.Cycle.BasesCalcul basesCalcul, IJPrononce prononceEncours){

        switch(basesCalcul.getFormation()){
            case 1:
                // école spéciale
                return IIJPetiteIJCalculee.CS_ECOLE_SPECIALE;
            case 2:
                // sans (mesures médicales)
                return IIJPetiteIJCalculee.CS_MESURES_MEDICALES;
            case 3:
                // formation prof. initiale
                return IIJPetiteIJCalculee.CS_FORMATION_PROFESSIONNELLE_INITIALE;
            case 4:
                // formation initiale prolongée
                return IIJPetiteIJCalculee.CS_FORMATION_INITIALE_PROLONGEE;
            case 5:
                // formation initiale changée
                return IIJPetiteIJCalculee.CS_NOUVELLE_FORME_APRES_INTERRUPTION;
            case 6:
                return IIJPetiteIJCalculee.CS_FORMATION_PROLONGE_APRES_INTERRUPTION;
            case 7:
                // activité aux. ou atelier protégé
                return IIJPetiteIJCalculee.CS_ACTIVITE_AUXILLIAIRE_OU_ATELIER_PROTEGE;
            case 8:
                // étudiant avec act. lucrative
                return IIJPetiteIJCalculee.CS_ETUDIANT_AVEC_ACTIVITE_LUCRATIVE;
            default:
                return ((IJPetiteIJ) prononceEncours).getCsSituationAssure();
        }
    }
}
