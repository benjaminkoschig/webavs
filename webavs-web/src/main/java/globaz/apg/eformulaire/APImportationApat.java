package globaz.apg.eformulaire;

import apg.amatapat.Child;
import apg.amatapat.Content;
import apg.amatapat.FamilyMembers;
import apg.amatapat.PaternityLeavePeriod;
import ch.globaz.common.util.CaisseInfoPropertiesWrapper;
import ch.globaz.common.util.Dates;
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
import globaz.prestation.utils.PRDateUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
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
            newDroit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
            newDroit.setGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE);
            newDroit.setReference(content.getReferenceData());
            newDroit.setIdCaisse(CaisseInfoPropertiesWrapper.noCaisseNoAgence());
            setPaysEtNpa(content, newDroit, npaFormat);
            java.util.Date dateDernierNee = getDateNaissanceDernierNee(content);
            if(dateDernierNee != null) {
                newDroit.setDateDebutDroit(JadeDateUtil.getGlobazFormattedDate(dateDernierNee));
            }
            ch.globaz.common.domaine.Date date = new ch.globaz.common.domaine.Date();
            newDroit.setDateDepot(date.getSwissValue());
            newDroit.setDateReception(date.getSwissValue());
            newDroit.setIsSoumisImpotSource(isSoumisImpotSource(content));
            newDroit.setSession(bSession);
            newDroit.add(transaction);
            createPeriodes(content, transaction, newDroit);
        } catch (Exception e) {
            fileStatus.addError("Une erreur s'est produite lors de la cr�ation du droit paternit� " + e.getMessage());
            LOG.error("APImportationAPGApat#createDroit - Une erreur s'est produite lors de la cr�ation du droit : ", e);
        }

        return newDroit;
    }

    private void createPeriodes(Content content, BTransaction transaction, APDroitPaternite newDroit) throws Exception {
        // r�cup�reration p�riode du droit
        List<PaternityLeavePeriod> periods = null;
        periods = getPaternityLeavePeriods(content);
        long days = 0;
        List<String> datesDeFin = new ArrayList<String>();
        for (PaternityLeavePeriod period : periods) {
            Date debutPeriod = period.getFrom().toGregorianCalendar().getTime();
            Date finPeriod = period.getTo().toGregorianCalendar().getTime();
            Date currentDate = new Date();
            if (currentDate.after(debutPeriod)) {
                if (finPeriod.after(debutPeriod)) {
                    APPeriodeAPG periodeAPG = new APPeriodeAPG();
                    periodeAPG.setDateDebutPeriode(JadeDateUtil.getGlobazFormattedDate(debutPeriod));
                    periodeAPG.setDateFinPeriode(JadeDateUtil.getGlobazFormattedDate(finPeriod));
                    datesDeFin.add(JadeDateUtil.getGlobazFormattedDate(finPeriod));
                    days = Dates.daysBetween(JadeDateUtil.getGlobazFormattedDate(debutPeriod), JadeDateUtil.getGlobazFormattedDate(finPeriod));
                    periodeAPG.setIdDroit(newDroit.getIdDroit());
                    periodeAPG.setNbrJours(String.format("%d", days));
                    periodeAPG.setSession(bSession);
                    periodeAPG.add(transaction);
                } else {
                    fileStatus.addInformation("Incoh�rence dans les dates de la p�riode et la p�riode n'a pas �t� ajout� dans WebAVS.");
                }
            } else {
                fileStatus.addInformation("La p�riode est d�finie dans le futur et n'a pas �t� ajout� dans WebAVS.");
            }
        }
    }

    private List<PaternityLeavePeriod> getPaternityLeavePeriods(Content content) {
        if(APAbstractImportationAmatApat.FORM_INDEPENDANT.equals(content.getFormType())) {
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
            fileStatus.addInformation("La situation familiale du droit a �t� ajout� dans WebAVS.");
        } catch (Exception e) {
            fileStatus.addInformation("Impossible de cr�er la situation familiale ");
            LOG.error("APImportationAPGApat#createSituation - Erreur lors de la cr�ation de la situation familiale ", e);
        }
    }
}
