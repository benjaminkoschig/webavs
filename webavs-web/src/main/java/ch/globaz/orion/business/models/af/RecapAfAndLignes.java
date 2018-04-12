package ch.globaz.orion.business.models.af;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ch.globaz.xmlns.eb.recapaf.LigneRecapAfEntity;
import ch.globaz.xmlns.eb.recapaf.MotifChangementAfEnum;
import ch.globaz.xmlns.eb.recapaf.StatutLigneRecapEnum;
import ch.globaz.xmlns.eb.recapaf.StatutRecapEnum;
import ch.globaz.xmlns.eb.recapaf.UniteTempsEnum;

public class RecapAfAndLignes {

    private RecapAf recapAf;
    private List<LigneRecapAfEnrichie> listLignesRecapAf;

    private static final DateFormat jourMoisAnneeFormat = new SimpleDateFormat("dd.MM.yyyy");

    public RecapAfAndLignes(ch.globaz.xmlns.eb.recapaf.RecapAfAndLignes recapAfAndLignesEbu) throws Exception {

        StatutRecapAfWebAvsEnum statutWebAvs = defineStatutRecapWebAvs(recapAfAndLignesEbu.getRecap().getStatut(),
                recapAfAndLignesEbu.getRecap().isAucunChangement());

        Date anneeMoisRecapAf = recapAfAndLignesEbu.getRecap().getAnneeMoisRecap().toGregorianCalendar().getTime();

        Date dateMiseADispoRecapAf = recapAfAndLignesEbu.getRecap().getDateMiseADisposition().toGregorianCalendar()
                .getTime();

        Date lastModificationDate = recapAfAndLignesEbu.getRecap().getLastModificationDate().toGregorianCalendar()
                .getTime();

        recapAf = new RecapAf(recapAfAndLignesEbu.getRecap().getIdRecap(), recapAfAndLignesEbu.getRecap().getPartner(),
                anneeMoisRecapAf, dateMiseADispoRecapAf, lastModificationDate, statutWebAvs, recapAfAndLignesEbu
                        .getRecap().isAucunChangement());

        listLignesRecapAf = prepareListFromEbu(recapAfAndLignesEbu.getLignesRecap());

    }

    private List<LigneRecapAfEnrichie> prepareListFromEbu(List<LigneRecapAfEntity> lignesRecap) throws Exception {
        List<LigneRecapAfEnrichie> listLignesRecapFromEbu = new ArrayList<LigneRecapAfEnrichie>();

        int nbLignes = 0;

        for (LigneRecapAfEntity ligneEbu : lignesRecap) {
            LigneRecapAfEnrichie ligneWebAvs = new LigneRecapAfEnrichie();
            ligneWebAvs.setIdLigneRecap(ligneEbu.getIdLigneRecap());
            ligneWebAvs.setIdRecap(ligneEbu.getIdRecap());
            ligneWebAvs.setNss(ligneEbu.getNss());
            ligneWebAvs.setNumeroDossierAf(ligneEbu.getNumeroDossierAf());
            ligneWebAvs.setNomAllocataire(ligneEbu.getNomAllocataire());
            ligneWebAvs.setPrenomAllocataire(ligneEbu.getPrenomAllocataire());
            ligneWebAvs.setNbUniteTravail(ligneEbu.getNbUniteTravail());

            if (ligneEbu.getUniteTravail() != null) {
                ligneWebAvs.setUniteTravail(defineUniteTravailWebAvs(ligneEbu.getUniteTravail()));
            }
            if (ligneEbu.getMotifChangement() != null) {
                ligneWebAvs.setMotifChangement(defineMotifChangementWebAvs(ligneEbu.getMotifChangement()));
            }

            if (ligneEbu.getStatutLigne() != null) {
                ligneWebAvs.setStatutLigne(defineStatutLigneWebAvs(ligneEbu.getStatutLigne()));
            }

            if (ligneEbu.getDateDebutChangement() != null) {
                ligneWebAvs.setDateDebutChangement(ligneEbu.getDateDebutChangement().toGregorianCalendar().getTime());
                ligneWebAvs.setDateDebutChangementStr(jourMoisAnneeFormat.format(ligneWebAvs.getDateDebutChangement()));
            }

            if (ligneEbu.getDateFinChangement() != null) {
                ligneWebAvs.setDateFinChangement(ligneEbu.getDateFinChangement().toGregorianCalendar().getTime());
                ligneWebAvs.setDateFinChangementStr(jourMoisAnneeFormat.format(ligneWebAvs.getDateFinChangement()));
            }

            ligneWebAvs.setNbEnfant(ligneEbu.getNbEnfant());
            ligneWebAvs.setMontantAllocation(ligneEbu.getMontantAllocation());
            ligneWebAvs.setRemarque(ligneEbu.getRemarque());

            // champs de la caisse
            ligneWebAvs.setNbUniteTravailCaisse(ligneEbu.getNbUniteTravailCaisse());
            if (ligneEbu.getUniteTravailCaisse() != null) {
                ligneWebAvs.setUniteTravailCaisse(defineUniteTravailWebAvs(ligneEbu.getUniteTravailCaisse()));
            }
            if (ligneEbu.getMotifChangementCaisse() != null) {
                ligneWebAvs.setMotifChangementCaisse(defineMotifChangementWebAvs(ligneEbu.getMotifChangementCaisse()));
            }
            if (ligneEbu.getDateDebutChangementCaisse() != null) {
                ligneWebAvs.setDateDebutChangementCaisse(ligneEbu.getDateDebutChangementCaisse().toGregorianCalendar()
                        .getTime());
                ligneWebAvs.setDateDebutChangementStrCaisse(jourMoisAnneeFormat.format(ligneWebAvs
                        .getDateDebutChangementCaisse()));
            }
            if (ligneEbu.getDateFinChangementCaisse() != null) {
                ligneWebAvs.setDateFinChangementCaisse(ligneEbu.getDateFinChangementCaisse().toGregorianCalendar()
                        .getTime());
                ligneWebAvs.setDateFinChangementStrCaisse(jourMoisAnneeFormat.format(ligneWebAvs
                        .getDateFinChangementCaisse()));
            }
            ligneWebAvs.setRemarqueCaisse(ligneEbu.getRemarqueCaisse());

            ligneWebAvs.populateDataEcranDetail(nbLignes, ligneEbu.getNumeroDossierAf());

            listLignesRecapFromEbu.add(ligneWebAvs);

            nbLignes++;
        }

        return listLignesRecapFromEbu;
    }

    private ch.globaz.orion.business.models.af.StatutLigneRecapEnum defineStatutLigneWebAvs(
            StatutLigneRecapEnum statutLigneEbu) {
        ch.globaz.orion.business.models.af.StatutLigneRecapEnum statutLigneWebAvs = null;
        if (statutLigneEbu.equals(StatutLigneRecapEnum.A_TRAITER)) {
            statutLigneWebAvs = ch.globaz.orion.business.models.af.StatutLigneRecapEnum.GENEREE;
        } else if (statutLigneEbu.equals(StatutLigneRecapEnum.TRAITEE)) {
            statutLigneWebAvs = ch.globaz.orion.business.models.af.StatutLigneRecapEnum.A_TRAITER;
        } else if (statutLigneEbu.equals(StatutLigneRecapEnum.AUCUN_CHANGEMENT)) {
            statutLigneWebAvs = ch.globaz.orion.business.models.af.StatutLigneRecapEnum.AUCUN_CHANGEMENT;
        } else if (statutLigneEbu.equals(StatutLigneRecapEnum.TRAITEE_CAISSE)) {
            statutLigneWebAvs = ch.globaz.orion.business.models.af.StatutLigneRecapEnum.TRAITEE;
        }

        return statutLigneWebAvs;
    }

    private ch.globaz.orion.business.models.af.MotifChangementAfEnum defineMotifChangementWebAvs(
            MotifChangementAfEnum motifChangementEbu) {
        ch.globaz.orion.business.models.af.MotifChangementAfEnum motifWebAvs = null;

        if (motifChangementEbu.equals(MotifChangementAfEnum.ACCIDENT)) {
            motifWebAvs = ch.globaz.orion.business.models.af.MotifChangementAfEnum.ACCIDENT;
        } else if (motifChangementEbu.equals(MotifChangementAfEnum.CONGE_MATERNITE)) {
            motifWebAvs = ch.globaz.orion.business.models.af.MotifChangementAfEnum.CONGE_MATERNITE;
        } else if (motifChangementEbu.equals(MotifChangementAfEnum.CONGE_NON_PAYE)) {
            motifWebAvs = ch.globaz.orion.business.models.af.MotifChangementAfEnum.CONGE_NON_PAYE;
        } else if (motifChangementEbu.equals(MotifChangementAfEnum.DECES)) {
            motifWebAvs = ch.globaz.orion.business.models.af.MotifChangementAfEnum.DECES;
        } else if (motifChangementEbu.equals(MotifChangementAfEnum.FIN_ACTIVITE)) {
            motifWebAvs = ch.globaz.orion.business.models.af.MotifChangementAfEnum.FIN_ACTIVITE;
        } else if (motifChangementEbu.equals(MotifChangementAfEnum.MALADIE)) {
            motifWebAvs = ch.globaz.orion.business.models.af.MotifChangementAfEnum.MALADIE;
        } else if (motifChangementEbu.equals(MotifChangementAfEnum.VACANCES)) {
            motifWebAvs = ch.globaz.orion.business.models.af.MotifChangementAfEnum.VACANCES;
        }

        return motifWebAvs;
    }

    private ch.globaz.orion.business.models.af.UniteTempsEnum defineUniteTravailWebAvs(UniteTempsEnum uniteTravailEbu) {
        ch.globaz.orion.business.models.af.UniteTempsEnum uniteTempsWebAvs = null;

        if (uniteTravailEbu.equals(UniteTempsEnum.HEURE)) {
            uniteTempsWebAvs = ch.globaz.orion.business.models.af.UniteTempsEnum.HEURE;
        } else if (uniteTravailEbu.equals(UniteTempsEnum.JOUR)) {
            uniteTempsWebAvs = ch.globaz.orion.business.models.af.UniteTempsEnum.JOUR;
        } else if (uniteTravailEbu.equals(UniteTempsEnum.MOIS)) {
            uniteTempsWebAvs = ch.globaz.orion.business.models.af.UniteTempsEnum.MOIS;
        }

        return uniteTempsWebAvs;
    }

    private StatutRecapAfWebAvsEnum defineStatutRecapWebAvs(StatutRecapEnum statutEbu, Boolean aucunChangement) {
        StatutRecapAfWebAvsEnum statutWebAvs = null;
        if (statutEbu.equals(StatutRecapEnum.A_TRAITER)) {
            statutWebAvs = StatutRecapAfWebAvsEnum.GENEREE;
        } else if (statutEbu.equals(StatutRecapEnum.TRAITEE) && !aucunChangement) {
            statutWebAvs = StatutRecapAfWebAvsEnum.A_TRAITER;
        } else if (statutEbu.equals(StatutRecapEnum.TRAITEE) && aucunChangement) {
            statutWebAvs = StatutRecapAfWebAvsEnum.AUCUN_CHANGEMENT;
        } else if (statutEbu.equals(StatutRecapEnum.CLOTUREE)) {
            statutWebAvs = StatutRecapAfWebAvsEnum.CLOTUREE;
        } else if (statutEbu.equals(StatutRecapEnum.TRAITEE_CAISSE)) {
            statutWebAvs = StatutRecapAfWebAvsEnum.TRAITEE;
        } else {
            throw new IllegalArgumentException("this statut is not allowed" + statutEbu);
        }

        return statutWebAvs;
    }

    public LigneRecapAfEnrichie getLigneForId(Integer idLigneRecap) {
        for (LigneRecapAfEnrichie ligne : listLignesRecapAf) {
            if (ligne.getIdLigneRecap().equals(idLigneRecap)) {
                return ligne;
            }
        }
        return null;
    }

    public RecapAf getRecapAf() {
        return recapAf;
    }

    public void setRecapAf(RecapAf recapAf) {
        this.recapAf = recapAf;
    }

    public List<LigneRecapAfEnrichie> getListLignesRecapAf() {
        return listLignesRecapAf;
    }

    public void setListLignesRecapAf(List<LigneRecapAfEnrichie> listLignesRecapAf) {
        this.listLignesRecapAf = listLignesRecapAf;
    }

}
