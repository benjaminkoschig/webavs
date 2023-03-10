package globaz.ij.acorweb.mapper;

import acor.ij.xsd.ij.out.FCalcul;
import ch.globaz.common.persistence.EntityService;
import ch.globaz.common.util.Strings;
import globaz.ij.api.prestations.IIJPetiteIJCalculee;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.api.prononces.IJGenrePrestation;
import globaz.ij.api.prononces.IJGenrePrestationPetiteIJ;
import globaz.ij.db.prestations.IJFpiCalculee;
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

        if(PRACORConst.CA_TYPE_IJ_GRANDE.equals(Strings.toStringOrNull(basesCalcul.getGenre()))
                || PRACORConst.CA_TYPE_IJ_AVEC_REVENU.equals(Strings.toStringOrNull(basesCalcul.getGenre()))){
            ijijCalculee = createAndMapGrandeIJ(basesCalcul);
        }else if(PRACORConst.CA_TYPE_IJ_PETITE.equals(Strings.toStringOrNull(basesCalcul.getGenre()))) {
            ijijCalculee = createAndMapPetiteIJ(basesCalcul, prononce);
        }else if(PRACORConst.CA_TYPE_FPI.equals(Strings.toStringOrNull(basesCalcul.getGenre()))) {
            ijijCalculee = createAndMapFpi(basesCalcul, prononce);
        }else{throw new PRAcorDomaineException("R?ponse invalide : Type d' IJ non r?connu.");
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
        ijijCalculee.setCsGenreReadaptation(PRACORConst.caGenreReadaptationToCS(entityService.getSession(), Strings.toStringOrNull(basesCalcul.getGenreReadaptationPrononce())));
        ijijCalculee.setDatePrononce(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(Strings.toStringOrNull(basesCalcul.getDatePrononce())));
        ijijCalculee.setDateDebutDroit(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(Strings.toStringOrNull(basesCalcul.getDebutDroit())));
        ijijCalculee.setDateFinDroit(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(Strings.toStringOrNull(basesCalcul.getFinDroit())));
        if(basesCalcul.getRevenuDeterminant() != null && IJGenrePrestation.calculRevenuDeterminant(ijijCalculee.getGenreReadaptationAnnonce())) {
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
        ijijCalculee.setNoRevision(Strings.toStringOrNull(basesCalcul.getRevision()));
        entityService.add(ijijCalculee);
        return ijijCalculee;
    }

    private IJIJCalculee createAndMapPetiteIJ(FCalcul.Cycle.BasesCalcul basesCalcul, IJPrononce prononce) {
        IJPetiteIJCalculee petiteIJ = new IJPetiteIJCalculee();
        petiteIJ.setCsModeCalcul(Strings.toStringOrNull(mapModeCalcul(basesCalcul, prononce)));
        petiteIJ.setGenreReadaptationAnnonce(IJGenrePrestationPetiteIJ.convertCode(Integer.toString(basesCalcul.getGenreReadaptation())));
        petiteIJ.setCsTypeIJ(IIJPrononce.CS_PETITE_IJ);
        return petiteIJ;
    }

    private IJIJCalculee createAndMapGrandeIJ(FCalcul.Cycle.BasesCalcul basesCalcul) {
        IJGrandeIJCalculee grandeIJ = new IJGrandeIJCalculee();
        grandeIJ.setMontantIndemniteEnfant(Strings.toStringOrNull(basesCalcul.getMontantEnfants()));
        grandeIJ.setNbEnfants(Strings.toStringOrNull(basesCalcul.getNEnfants()));
        grandeIJ.setGenreReadaptationAnnonce(Strings.toStringOrNull(basesCalcul.getGenreReadaptation()));
        grandeIJ.setCsTypeIJ(IIJPrononce.CS_GRANDE_IJ);
        return grandeIJ;
    }

    private IJIJCalculee createAndMapFpi(FCalcul.Cycle.BasesCalcul basesCalcul, IJPrononce prononce) {
        IJFpiCalculee fpi = new IJFpiCalculee();
        fpi.setMontantBase(Strings.toStringOrNull(basesCalcul.getMontantBase()));
        fpi.setNbEnfants(Strings.toStringOrNull(basesCalcul.getNEnfants()));
        fpi.setMontantEnfants(Strings.toStringOrNull(basesCalcul.getMontantEnfants()));
        fpi.setSalaireMensuel(Strings.toStringOrNull(basesCalcul.getMontantMensuel()));
        fpi.setGenreReadaptationAnnonce(Strings.toStringOrNull(basesCalcul.getGenreReadaptation()));
        fpi.setCsTypeIJ(IIJPrononce.CS_FPI);
        return fpi;
    }

    private IJIJCalculee createAndMaFPI(FCalcul.Cycle.BasesCalcul basesCalcul) {
        return null;
    }

    private String mapModeCalcul(FCalcul.Cycle.BasesCalcul basesCalcul, IJPrononce prononceEncours){

        switch(basesCalcul.getFormation()){
            case 1:
                // ?cole sp?ciale
                return IIJPetiteIJCalculee.CS_ECOLE_SPECIALE;
            case 2:
                // sans (mesures m?dicales)
                return IIJPetiteIJCalculee.CS_MESURES_MEDICALES;
            case 3:
                // formation prof. initiale
                return IIJPetiteIJCalculee.CS_FORMATION_PROFESSIONNELLE_INITIALE;
            case 4:
                // formation initiale prolong?e
                return IIJPetiteIJCalculee.CS_FORMATION_INITIALE_PROLONGEE;
            case 5:
                // formation initiale chang?e
                return IIJPetiteIJCalculee.CS_NOUVELLE_FORME_APRES_INTERRUPTION;
            case 6:
                return IIJPetiteIJCalculee.CS_FORMATION_PROLONGE_APRES_INTERRUPTION;
            case 7:
                // activit? aux. ou atelier prot?g?
                return IIJPetiteIJCalculee.CS_ACTIVITE_AUXILLIAIRE_OU_ATELIER_PROTEGE;
            case 8:
                // ?tudiant avec act. lucrative
                return IIJPetiteIJCalculee.CS_ETUDIANT_AVEC_ACTIVITE_LUCRATIVE;
            default:
                return ((IJPetiteIJ) prononceEncours).getCsSituationAssure();
        }
    }
}
