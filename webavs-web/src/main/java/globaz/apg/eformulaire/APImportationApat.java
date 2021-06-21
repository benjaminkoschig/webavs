package globaz.apg.eformulaire;

import apg.amatapat.Child;
import apg.amatapat.Content;
import apg.amatapat.FamilyMembers;
import apg.amatapat.PaternityLeavePeriod;
import ch.globaz.common.util.CaisseInfoPropertiesWrapper;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitPaternite;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.apg.db.droits.APSituationFamilialePat;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeDateUtil;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
public class APImportationApat extends APAbstractImportationAmatApat {

    public APImportationApat(APImportationStatusFile fileStatus, BSession bsession, String nss) {
        super(fileStatus, bsession, IPRDemande.CS_TYPE_PATERNITE, nss);
    }

    @Override
    public APDroitLAPG createDroit(Content content, String npaFormat, PRDemande demande, BTransaction transaction) {
        APDroitPaternite newDroit = new APDroitPaternite();

        try{
            newDroit.setIdDemande(demande.getIdDemande());
            newDroit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ENREGISTRE);
            newDroit.setGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE);
            newDroit.setReference(content.getReferenceData());
            newDroit.setIdCaisse(CaisseInfoPropertiesWrapper.noCaisseNoAgence());
            newDroit.setNpa(npaFormat);
            newDroit.setPays(getIdPays(content.getInsuredAddress().getCountryIso2Code()));
            java.util.Date dateDernierNee = getDateNaissanceDernierNee(content);
            if(dateDernierNee != null) {
                newDroit.setDateDebutDroit(JadeDateUtil.getGlobazFormattedDate(dateDernierNee));
            }
            ch.globaz.common.domaine.Date date = new ch.globaz.common.domaine.Date();
            newDroit.setDateDepot(date.getSwissValue());
            newDroit.setDateReception(date.getSwissValue());
            newDroit.setIsSoumisImpotSource(isSoumisImpotSource(content));
            // TODO : set date fin de droit

            newDroit.setSession(bSession);
            newDroit.add(transaction);

            // récupéreration période du droit
            List<PaternityLeavePeriod> periods = null;
            periods = getPaternityLeavePeriods(content);
            long days = 0;

            for (PaternityLeavePeriod period : periods) {
                Date debutPeriod = period.getFrom().toGregorianCalendar().getTime();
                Date finPeriod = period.getTo().toGregorianCalendar().getTime();
                Date currentDate = new Date();
                if (currentDate.after(debutPeriod)) {
                    if (finPeriod.after(debutPeriod)) {
                        APPeriodeAPG periodeAPG = new APPeriodeAPG();
                        periodeAPG.setDateDebutPeriode(JadeDateUtil.getGlobazFormattedDate(debutPeriod));
                        periodeAPG.setDateFinPeriode(JadeDateUtil.getGlobazFormattedDate(finPeriod));
                        days = JadeDateUtil.getNbDaysBetween(JadeDateUtil.getGlobazFormattedDate(debutPeriod), JadeDateUtil.getGlobazFormattedDate(finPeriod));
                        periodeAPG.setIdDroit(newDroit.getIdDroit());
                        periodeAPG.setNbrJours(String.format("%d", days));
                        periodeAPG.setSession(bSession);
                        periodeAPG.add(transaction);
                    } else {
                        fileStatus.getInformations().add("Incohérence dans les dates de la période et la période n'a pas été ajouté dans WebAVS.");
                    }
                } else {
                    fileStatus.getInformations().add("La période est définie dans le futur et n'a pas été ajouté dans WebAVS.");
                }
            }
        } catch (Exception e) {
            fileStatus.getErrors().add("Une erreur s'est produite lors de la création du droit paternité " + e.getMessage());
            LOG.error("Une erreur s'est produite lors de la création du droit : ", e);
        }

        return newDroit;
    }

    private List<PaternityLeavePeriod> getPaternityLeavePeriods(Content content) {
        if(content.getFormType().equals(APAbstractImportationAmatApat.FORM_INDEPENDANT)) {
            if(content.getActivityCessation() != null &&
                    content.getActivityCessation().getUnemploymentCessation() != null &&
                    content.getActivityCessation().getUnemploymentCessation().getParternityLeave() != null &&
                    content.getActivityCessation().getUnemploymentCessation().getParternityLeave().getPaternityLeavePeriods() != null)
                return content.getActivityCessation().getUnemploymentCessation().getParternityLeave().getPaternityLeavePeriods().getPaternityLeavePeriod();
        }else if(content.getProvidedByEmployer() != null &&
                    content.getProvidedByEmployer().getParternityLeave() != null &&
                    content.getProvidedByEmployer().getParternityLeave().getPaternityLeavePeriods() != null) {
                return content.getProvidedByEmployer().getParternityLeave().getPaternityLeavePeriods().getPaternityLeavePeriod();
        }
        return Collections.emptyList();
    }

    @Override
    public void createSituationFamiliale(FamilyMembers membresFamille, String idDroit, BTransaction transaction) {
        try {
            for (Child child : membresFamille.getChildren().getChild()) {
                APSituationFamilialePat enfant = new APSituationFamilialePat();
                enfant.setNom(child.getOfficialName());
                enfant.setPrenom(child.getFirstName());
                enfant.setDateNaissance(tranformGregDateToGlobDate(child.getDateOfBirth()));
                enfant.setIdDroitPaternite(idDroit);
                enfant.setType(IAPDroitMaternite.CS_TYPE_ENFANT);
                enfant.setSession(bSession);
                enfant.add(transaction);
            }
            fileStatus.getInformations().add("La situation familiale du droit a été ajouté dans WebAVS.");
            LOG.info("La situation familiale du droit a été ajouté dans WebAVS.");
        } catch (Exception e) {
            fileStatus.getInformations().add("Impossible de créer la situation familiale ");
            LOG.error("Erreur lors de la création de la situation familiale ", e);
        }
    }
}
