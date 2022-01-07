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
import globaz.pyxis.constantes.IConstantes;
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
            newDroit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
            newDroit.setGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE);
            newDroit.setReference(content.getReferenceData());
            newDroit.setIdCaisse(CaisseInfoPropertiesWrapper.noCaisseNoAgence());


            java.util.Date dateDernierNee = getDateNaissanceDernierNee(content);
            if(dateDernierNee != null) {
                newDroit.setDateDebutDroit(JadeDateUtil.getGlobazFormattedDate(dateDernierNee));
                newDroit.calculerDateFinDroit();
            }
            Date date = new Date();
            newDroit.setDateDepot(date.getSwissValue());
            newDroit.setDateReception(date.getSwissValue());
            newDroit.setIsSoumisImpotSource(isSoumisImpotSource(content));
            setPaysEtNpa(content, newDroit, npaFormat);
            newDroit.setSession(bSession);
            newDroit.add(transaction);

        } catch (Exception e) {
            fileStatus.addError("Une erreur s'est produite lors de la cr�ation du droit maternit� " + e.getMessage());
            LOG.error("APImportationAPGAmat#createDroit - Une erreur s'est produite lors de la cr�ation du droit : ", e);
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
            fileStatus.addInformation("La situation familiale du droit a �t� ajout� dans WebAVS.");
        } catch (Exception e) {
            fileStatus.addInformation("Impossible de cr�er la situation familiale.");
            LOG.error("APImportationAPGAmat#createSituationFamiliale - Erreur lors de la cr�ation de la situation familiale ", e);
        }
    }
}
