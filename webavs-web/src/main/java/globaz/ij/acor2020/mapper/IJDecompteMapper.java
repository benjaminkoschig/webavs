package globaz.ij.acor2020.mapper;

import acor.ij.xsd.ij.out.Decompte;
import acor.ij.xsd.ij.out.DecompteIJ;
import acor.ij.xsd.ij.out.FCalcul;
import ch.globaz.common.persistence.EntityService;
import ch.globaz.common.util.Strings;
import globaz.ij.db.prestations.IJPrestation;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.tools.PRDateFormater;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class IJDecompteMapper {
    private IJDecompteMapper(){ }

    public static List baseCalculDecompteMapToIJPrestation(FCalcul.Cycle.BasesCalcul basesCalcul, String idIJCalculee, String idBaseIndemnisation, EntityService entityService){
        List prestations = new LinkedList();
        for (DecompteIJ decompte:
                basesCalcul.getDecompte()
             ) {
            IJPrestation prestation = new IJPrestation();

            prestation.setDateDebut(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(Strings.toStringOrNull(decompte.getDateDebut())));
            prestation.setDateFin(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(Strings.toStringOrNull(decompte.getDateFin())));
            prestation.setIdIJCalculee(idIJCalculee);
            prestation.setIdBaseIndemnisation(idBaseIndemnisation);
            prestation.setNombreJoursExt(Strings.toStringOrNull(decompte.getNjoursExt()));
            prestation.setNombreJoursInt(Strings.toStringOrNull(decompte.getNjoursInt()));
            prestation.setMontantBrut(Strings.toStringOrNull(decompte.getMontantGlobal()));
            setPrestationDataFromDecompteCategorie(prestation, decompte, PRACORConst.CA_TYPE_MESURE_EXTERNE);
            setPrestationDataFromDecompteCategorie(prestation, decompte, PRACORConst.CA_TYPE_MESURE_INTERNE);

            entityService.add(prestation);
            prestations.add(prestation);
        }
        return prestations;
    }

    private static void setPrestationDataFromDecompteCategorie(IJPrestation prestation, DecompteIJ decompte, String typeMesure){
        Optional<Decompte.DecompteCategorie> decompteCategorie = decompte.getDecompteCategorie().stream()
                .filter(decompCat ->
                        typeMesure.equals(String.valueOf(decompCat.getCategorie()))).findFirst();
        if(decompteCategorie.isPresent()) {
            if(PRACORConst.CA_TYPE_MESURE_EXTERNE.equals(typeMesure)) {
                prestation.setMontantBrutExterne(Strings.toStringOrNull(decompteCategorie.get().getMontantCategorieTotal()));
                prestation.setMontantJournalierExterne(Strings.toStringOrNull(decompteCategorie.get().getMontantCategorieJ()));
            }else if(PRACORConst.CA_TYPE_MESURE_INTERNE.equals(typeMesure)){
                prestation.setMontantBrutInterne(Strings.toStringOrNull(decompteCategorie.get().getMontantCategorieTotal()));
                prestation.setMontantJournalierInterne(Strings.toStringOrNull(decompteCategorie.get().getMontantCategorieJ()));
            }
        }
    }
}
