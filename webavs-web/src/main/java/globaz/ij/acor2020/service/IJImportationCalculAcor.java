package globaz.ij.acor2020.service;

import acor.ij.xsd.ij.out.FCalcul;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.eavs.utils.StringUtils;
import globaz.corvus.exceptions.RETechnicalException;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.ij.api.prestations.IIJPetiteIJCalculee;
import globaz.ij.db.prestations.IJGrandeIJCalculee;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJPetiteIJCalculee;
import globaz.ij.db.prononces.IJPetiteIJ;
import globaz.ij.db.prononces.IJPrononce;

import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRAcorDomaineException;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;

class IJImportationCalculAcor {
    IJPrononce prononce;
    BSession session;
    PRTiersWrapper tiers;

    void importCalculAcor(String idPrononce, FCalcul fCalcul) {

        try {
            loadSession();
            // Chargement des informations du prononcé correspondant à la demande de calcul Acor
            // dans WebAVS
            loadPrononce(idPrononce, fCalcul.getTypeDem());
            if(prononce == null){
                throw new PRACORException("Réponse invalide : Impossible de retrouver le prononcé du calcul. ");
            }
            loadTiers();
        }catch(Exception e){
            throw new CommonTechnicalException(e);
        }

            // Récupère le NSS du FCalcul reçu d'ACOR
            String nss = "";
            for (FCalcul.Assure assure:
                    fCalcul.getAssure()) {
                if("req".equals(assure.getFonction())){
                    if("nss".equals(assure.getId().getType()) || "navs".equals(assure.getId().getType())){
                        nss = assure.getId().getValue();
                        break;
                    }
                }
            }
            if(StringUtils.isBlank(nss)){
                throw new PRAcorDomaineException("Réponse invalide : Impossible de retrouver le NSS du requérant. ");
            }
            if(nss.equals(tiers.getNSS())){
                throw new PRAcorDomaineException(session.getLabel("IMPORTATION_MAUVAIS_PRONONCE") + " (8)");
            }

            // Mapping des données liées aux bases de calcul.
            for (FCalcul.Cycle cycle:
                    fCalcul.getCycle()) {

                for (FCalcul.Cycle.BasesCalcul baseCalcul:
                        cycle.getBasesCalcul()) {
                    IJIJCalculee ijCalculee = mapBaseCalcul(baseCalcul);
                }
            }

    }

    private void loadPrononce(String idPrononce, String csTypeIJ) throws Exception {
        if ((prononce == null) & !JadeStringUtil.isIntegerEmpty(idPrononce)) {
            prononce = IJPrononce.loadPrononce(session, null, idPrononce, csTypeIJ);
        }
    }

    private void loadSession(){
        if(session == null){
            session = BSessionUtil.getSessionFromThreadContext();
        }
    }

    private void loadTiers() throws Exception {
        if(tiers == null) {
            tiers = prononce.loadDemande(null).loadTiers();
        }
    }

    private IJIJCalculee mapBaseCalcul(FCalcul.Cycle.BasesCalcul basesCalcul) {
        IJIJCalculee ijijCalculee;

        if(PRACORConst.CA_TYPE_IJ_GRANDE.equals(basesCalcul.getGenre())){
            IJGrandeIJCalculee grandeIJ = new IJGrandeIJCalculee();
            grandeIJ.setMontantIndemniteEnfant(String.valueOf(basesCalcul.getMontantEnfants()));
            grandeIJ.setNbEnfants(String.valueOf(basesCalcul.getNEnfants()));
            ijijCalculee = grandeIJ;
        }else if(PRACORConst.CA_TYPE_IJ_PETITE.equals(basesCalcul.getGenre())){
            IJPetiteIJCalculee petiteIJ = new IJPetiteIJCalculee();
            petiteIJ.setCsModeCalcul(String.valueOf(mapModeCalcul(basesCalcul, prononce)));
            ijijCalculee = petiteIJ;
        }else{
            throw new PRAcorDomaineException("Réponse invalide : Type d' IJ non réconnu.");
        }

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
        // TODO : Valeur mise à jour dans fichier plat mais pas possible de la trouver dans le xsd
        // ijijCalculee.setPourcentDegreIncapaciteTravail(????);
        ijijCalculee.setDemiIJACBrut(String.valueOf(basesCalcul.getRevenuReadaptation().getACDemiBrut()));
        ijijCalculee.setDifferenceRevenu(String.valueOf(basesCalcul.getDifferenceRevenu()));
        ijijCalculee.setIdPrononce(prononce.getIdPrononce());
        ijijCalculee.setCsTypeIJ(String.valueOf(basesCalcul.getGenre()));
        ijijCalculee.setNoRevision(String.valueOf(basesCalcul.getRevision()));

        saveEntity(ijijCalculee);

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
            case 6:
                // TODO : A confirmer
                // formation initiale changée
                // formation initiale changée et prolongée
                return IIJPetiteIJCalculee.CS_NOUVELLE_FORME_APRES_INTERRUPTION;
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

    private final void saveEntity(BEntity entity){
        entity.setSession(session);

        try {
            entity.add(session.getCurrentThreadTransaction());
        } catch (Exception e) {
            throw new CommonTechnicalException("impossible de sauver dans la base", e);
        }
    }
}
