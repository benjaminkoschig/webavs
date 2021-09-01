package globaz.ij.acor2020.mapper;

import acor.ij.xsd.ij.out.FCalcul;
import ch.globaz.common.persistence.EntityUtils;
import globaz.globall.db.BSession;
import globaz.ij.api.prestations.IIJPetiteIJCalculee;
import globaz.ij.db.prestations.IJGrandeIJCalculee;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJPetiteIJCalculee;
import globaz.ij.db.prononces.IJPetiteIJ;
import globaz.ij.db.prononces.IJPrononce;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRAcorDomaineException;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;

public class IJIJCalculeeMapper {

    public static IJIJCalculee baseCalculMapToIJIJCalculee(FCalcul.Cycle.BasesCalcul basesCalcul, PRTiersWrapper tiers, IJPrononce prononce, BSession session) {
        IJIJCalculee ijijCalculee;

        if(PRACORConst.CA_TYPE_IJ_GRANDE.equals(basesCalcul.getGenre())){
            ijijCalculee = createAndMapGrandeIJ(basesCalcul);
        }else if(PRACORConst.CA_TYPE_IJ_PETITE.equals(basesCalcul.getGenre())){
            ijijCalculee = createAndMapPetiteIJ(basesCalcul, prononce);
        }else{
            throw new PRAcorDomaineException("R�ponse invalide : Type d' IJ non r�connu.");
        }

        return mapIJIJCalculee(basesCalcul, tiers, prononce, session, ijijCalculee);
    }

    private static IJIJCalculee mapIJIJCalculee(FCalcul.Cycle.BasesCalcul basesCalcul, PRTiersWrapper tiers, IJPrononce prononce, BSession session, IJIJCalculee ijijCalculee) {

        if("1".equals(basesCalcul.getDroitPrestationEnfant())){
            ijijCalculee.setIsDroitPrestationPourEnfant(Boolean.TRUE);
        }else if("0".equals(basesCalcul.getDroitPrestationEnfant())){
            ijijCalculee.setIsDroitPrestationPourEnfant(Boolean.FALSE);
        }else{
            ijijCalculee.setIsDroitPrestationPourEnfant(null);
        }

        ijijCalculee.setNoAVS(tiers.getNSS());
        ijijCalculee.setOfficeAI(String.valueOf(basesCalcul.getOfficeAi()));
        ijijCalculee.setCsGenreReadaptation(PRACORConst.caGenreReadaptationToCS(session, String.valueOf(basesCalcul.getGenreReadaptation())));
        ijijCalculee.setDatePrononce(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(String.valueOf(basesCalcul.getDatePrononce())));
        ijijCalculee.setDateDebutDroit(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(String.valueOf(basesCalcul.getDebutDroit())));
        ijijCalculee.setDateFinDroit(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(String.valueOf(basesCalcul.getFinDroit())));
        ijijCalculee.setRevenuDeterminant(String.valueOf(basesCalcul.getRevenuDeterminant().getRevenuJournalier()));
        ijijCalculee.setDateRevenu(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(String.valueOf(basesCalcul.getRevenuDeterminant().getDate())));
        ijijCalculee.setMontantBase(String.valueOf(basesCalcul.getMontantBase()));
        ijijCalculee.setRevenuJournalierReadaptation(String.valueOf(basesCalcul.getRevenuReadaptation().getRevenuJournalier()));
        ijijCalculee.setCsStatutProfessionnel(PRACORConst.caStatutProfessionnelToCS(session, String.valueOf(basesCalcul.getStatut())));
        // TODO : Valeur mise � jour dans fichier plat mais pas possible de la trouver dans le xsd
        // ijijCalculee.setPourcentDegreIncapaciteTravail(????);
        ijijCalculee.setDemiIJACBrut(String.valueOf(basesCalcul.getRevenuReadaptation().getACDemiBrut()));
        ijijCalculee.setDifferenceRevenu(String.valueOf(basesCalcul.getDifferenceRevenu()));
        ijijCalculee.setIdPrononce(prononce.getIdPrononce());
        ijijCalculee.setCsTypeIJ(String.valueOf(basesCalcul.getGenre()));
        ijijCalculee.setNoRevision(String.valueOf(basesCalcul.getRevision()));

        EntityUtils.saveEntity(ijijCalculee, session);
        return ijijCalculee;
    }

    private static IJIJCalculee createAndMapPetiteIJ(FCalcul.Cycle.BasesCalcul basesCalcul, IJPrononce prononce) {
        IJIJCalculee ijijCalculee;
        IJPetiteIJCalculee petiteIJ = new IJPetiteIJCalculee();
        petiteIJ.setCsModeCalcul(String.valueOf(mapModeCalcul(basesCalcul, prononce)));
        ijijCalculee = petiteIJ;
        return ijijCalculee;
    }

    private static IJIJCalculee createAndMapGrandeIJ(FCalcul.Cycle.BasesCalcul basesCalcul) {
        IJIJCalculee ijijCalculee;
        IJGrandeIJCalculee grandeIJ = new IJGrandeIJCalculee();
        grandeIJ.setMontantIndemniteEnfant(String.valueOf(basesCalcul.getMontantEnfants()));
        grandeIJ.setNbEnfants(String.valueOf(basesCalcul.getNEnfants()));
        ijijCalculee = grandeIJ;
        return ijijCalculee;
    }

    private static String mapModeCalcul(FCalcul.Cycle.BasesCalcul basesCalcul, IJPrononce prononceEncours){

        switch(basesCalcul.getFormation()){
            case 1:
                // �cole sp�ciale
                return IIJPetiteIJCalculee.CS_ECOLE_SPECIALE;
            case 2:
                // sans (mesures m�dicales)
                return IIJPetiteIJCalculee.CS_MESURES_MEDICALES;
            case 3:
                // formation prof. initiale
                return IIJPetiteIJCalculee.CS_FORMATION_PROFESSIONNELLE_INITIALE;
            case 4:
                // formation initiale prolong�e
                return IIJPetiteIJCalculee.CS_FORMATION_INITIALE_PROLONGEE;
            case 5:
            case 6:
                // TODO : A confirmer
                // formation initiale chang�e
                // formation initiale chang�e et prolong�e
                return IIJPetiteIJCalculee.CS_NOUVELLE_FORME_APRES_INTERRUPTION;
            case 7:
                // activit� aux. ou atelier prot�g�
                return IIJPetiteIJCalculee.CS_ACTIVITE_AUXILLIAIRE_OU_ATELIER_PROTEGE;
            case 8:
                // �tudiant avec act. lucrative
                return IIJPetiteIJCalculee.CS_ETUDIANT_AVEC_ACTIVITE_LUCRATIVE;
            default:
                return ((IJPetiteIJ) prononceEncours).getCsSituationAssure();
        }
    }
}
