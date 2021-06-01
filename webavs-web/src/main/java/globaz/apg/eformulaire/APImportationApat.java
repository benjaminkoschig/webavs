package globaz.apg.eformulaire;

import apg.amatapat.*;
import ch.globaz.common.util.CaisseInfoPropertiesWrapper;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.*;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeDateUtil;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;

import java.util.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class APImportationApat extends APAbstractImportationAmatApat {

    public APImportationApat(List<String> err, List<String> inf, BSession bsession) {
        super(err, inf, bsession, IPRDemande.CS_TYPE_PATERNITE);
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
            List<PaternityLeavePeriod> periods = content.getActivityCessation().getUnemploymentCessation().getParternityLeave().getPaternityLeavePeriods().getPaternityLeavePeriod();
            long days = 0;
            for (PaternityLeavePeriod period:periods) {
                Date debutPeriod = period.getFrom().toGregorianCalendar().getTime();
                Date finPeriod = period.getTo().toGregorianCalendar().getTime();
                APPeriodeAPG periodeAPG = new APPeriodeAPG();
                periodeAPG.setDateDebutPeriode(JadeDateUtil.getGlobazFormattedDate(debutPeriod));
                periodeAPG.setDateFinPeriode(JadeDateUtil.getGlobazFormattedDate(finPeriod));
                days = JadeDateUtil.getNbDaysBetween(JadeDateUtil.getGlobazFormattedDate(debutPeriod), JadeDateUtil.getGlobazFormattedDate(finPeriod));
                periodeAPG.setIdDroit(newDroit.getIdDroit());
                periodeAPG.setNbrJours(String.format("%d", days));
                periodeAPG.setSession(bSession);
                periodeAPG.add(transaction);
            }
        } catch (Exception e){
            errors.add("Une erreur s'est produite lors de la création du droit maternité " + e.getMessage());
            LOG.error("Une erreur s'est produite lors de la création du droit : ", e);
        }

        return newDroit;
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
        } catch (Exception e) {
            infos.add("Impossible de créer la situation familiale ");
            LOG.error("Erreur lors de la création de la situation familiale ", e);
        }
    }
}
