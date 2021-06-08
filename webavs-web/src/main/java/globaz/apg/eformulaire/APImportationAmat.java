package globaz.apg.eformulaire;

import apg.amatapat.Child;
import apg.amatapat.Content;
import apg.amatapat.FamilyMembers;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.util.CaisseInfoPropertiesWrapper;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APSituationFamilialeMat;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeDateUtil;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class APImportationAmat extends APAbstractImportationAmatApat {

    public APImportationAmat(APImportationStatusFile fileStatus, BSession bsession, String nss) {
        super(fileStatus, bsession, IPRDemande.CS_TYPE_MATERNITE, nss);
    }

    @Override
    public APDroitLAPG createDroit(Content content, String npaFormat, PRDemande demande, BTransaction transaction) {
        APDroitMaternite newDroit = new APDroitMaternite();

        try {
            newDroit.setIdDemande(demande.getIdDemande());
            newDroit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ENREGISTRE);
            newDroit.setGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE);
            newDroit.setReference(content.getReferenceData());
            newDroit.setIdCaisse(CaisseInfoPropertiesWrapper.noCaisseNoAgence());
            newDroit.setNpa(npaFormat);
            newDroit.setPays(getIdPays(content.getInsuredAddress().getCountryIso2Code()));
            java.util.Date dateDernierNee = getDateNaissanceDernierNee(content);
            if(dateDernierNee != null) {
                newDroit.setDateDebutDroit(JadeDateUtil.getGlobazFormattedDate(dateDernierNee));
            }
            Date date = new Date();
            newDroit.setDateDepot(date.getSwissValue());
            newDroit.setDateReception(date.getSwissValue());
            newDroit.setIsSoumisImpotSource(isSoumisImpotSource(content));
            // TODO : set date fin de droit

            newDroit.setSession(bSession);
            newDroit.add(transaction);

        } catch (Exception e) {
            fileStatus.getErrors().add("Une erreur s'est produite lors de la création du droit maternité " + e.getMessage());
            LOG.error("Une erreur s'est produite lors de la création du droit : ", e);
        }

        return newDroit;
    }

    @Override
    public void createSituationFamiliale(FamilyMembers membresFamille, String idDroit, BTransaction transaction) {
        try {
            for (Child child : membresFamille.getChildren().getChild()) {
                APSituationFamilialeMat enfant = new APSituationFamilialeMat();
                enfant.setNom(child.getOfficialName());
                enfant.setPrenom(child.getFirstName());
                enfant.setDateNaissance(tranformGregDateToGlobDate(child.getDateOfBirth()));
                enfant.setIdDroitMaternite(idDroit);
                enfant.setType(IAPDroitMaternite.CS_TYPE_ENFANT);
                enfant.setSession(bSession);
                enfant.add(transaction);
            }
            fileStatus.getInformations().add("La situation familiale du droit a été ajouté dans WebAVS.");
            LOG.info("La situation familiale du droit a été ajouté dans WebAVS.");
        } catch (Exception e) {
            fileStatus.getInformations().add("Impossible de créer la situation familiale.");
            LOG.error("Erreur lors de la création de la situation familiale ", e);
        }
    }
}
