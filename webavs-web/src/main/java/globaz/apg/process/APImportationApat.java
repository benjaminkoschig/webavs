package globaz.apg.process;

import apg.amatapat.*;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.*;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeDateUtil;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;
import org.slf4j.Logger;

import java.util.*;

public class APImportationApat extends APAbstractImportationAmatApat {

    public APImportationApat(LinkedList<String> err, LinkedList<String> inf, Logger log) {
        super(err, inf, log);
        typeDemande = IPRDemande.CS_TYPE_PATERNITE;
    }

    @Override
    public APDroitLAPG createDroit(Content content, String npaFormat, PRDemande demande, BTransaction transaction, BSession bsession) {
        APDroitPaternite newDroit = new APDroitPaternite();

        try{
            // Creation du droit
            newDroit.setIdDemande(demande.getIdDemande());
            newDroit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ENREGISTRE);
            newDroit.setGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE);
            newDroit.setReference(content.getReferenceData());
            newDroit.setIdCaisse(creationIdCaisse());
            newDroit.setNpa(npaFormat);

            // TODO : récupérer code du pays
            newDroit.setPays("100");

            // récupéreration Date réception et dépôt
            newDroit.setDateReception(JadeDateUtil.getGlobazFormattedDate(content.getConfirmation().getEmployeeConfirmationDate().toGregorianCalendar().getTime()));
            newDroit.setDateDepot(JadeDateUtil.getGlobazFormattedDate(content.getConfirmation().getInsuredConfirmationDate().toGregorianCalendar().getTime()));

            // Récupération de la date de naissance du dernier enfant -> date de début du droit
            Children children = content.getFamilyMembers().getChildren();
            java.util.Date dateNaissance = null;
            for (Child child : children.getChild()) {
                java.util.Date newDateNaissance = child.getDateOfBirth().toGregorianCalendar().getTime();
                if (Objects.isNull(dateNaissance)) {
                    dateNaissance = newDateNaissance;
                } else {
                    if (newDateNaissance.after(dateNaissance)) {
                        dateNaissance = newDateNaissance;
                    }
                }
            }
            newDroit.setDateDebutDroit(JadeDateUtil.getGlobazFormattedDate(dateNaissance));
            // TODO : set date fin de droit

            // récupéreration information impôt
            newDroit.setIsSoumisImpotSource(isSoumisImpotSource(content));

            newDroit.setSession(bsession);
            newDroit.add(transaction);

            // récupéreration période du droit
            List<PaternityLeavePeriod> periods = content.getProvidedByEmployer().getParternityLeave().getPaternityLeavePeriods().getPaternityLeavePeriod();
            long days = 0;
            for (PaternityLeavePeriod period:periods) {
                Date debutPeriod = period.getFrom().toGregorianCalendar().getTime();
                Date finPeriod = period.getTo().toGregorianCalendar().getTime();
                APPeriodeAPG periodeAPG = new APPeriodeAPG();
                periodeAPG.setDateDebutPeriode(JadeDateUtil.getGlobazFormattedDate(debutPeriod));
                periodeAPG.setDateFinPeriode(JadeDateUtil.getGlobazFormattedDate(finPeriod));
                days = JadeDateUtil.getNbDaysBetween(JadeDateUtil.getGlobazFormattedDate(finPeriod), JadeDateUtil.getGlobazFormattedDate(debutPeriod));
                periodeAPG.setIdDroit(newDroit.getIdDroit());
                periodeAPG.setNbrJours(String.format("%d", days));
                periodeAPG.setSession(bsession);
                periodeAPG.add(transaction);
            }
        } catch (Exception e){
            errors.add("Une erreur s'est produite lors de la création du droit maternité " + e.getMessage());
            LOG.error("Une erreur s'est produite lors de la création du droit : ", e);
        }

        return newDroit;
    }

    @Override
    public void createSituationFamiliale(FamilyMembers membresFamille, String idDroit, BTransaction transaction, BSession bsession) {
        try {
            for (Child child : membresFamille.getChildren().getChild()) {
                APSituationFamilialePat enfant = new APSituationFamilialePat();
                enfant.setNom(child.getOfficialName());
                enfant.setPrenom(child.getFirstName());
                enfant.setDateNaissance(tranformGregDateToGlobDate(child.getDateOfBirth()));
                enfant.setIdDroitPaternite(idDroit);
                enfant.setType(IAPDroitMaternite.CS_TYPE_ENFANT);
                enfant.setSession(bsession);
                enfant.add(transaction);
            }
        } catch (Exception e) {
            errors.add("Impossible de créer la situation familiale ");
            LOG.error("Erreur lors de la création de la situation familiale ", e);
        }
    }
}
