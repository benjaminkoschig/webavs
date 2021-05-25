package globaz.apg.process;

import apg.amatapat.*;
import ch.globaz.common.domaine.Date;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.*;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeDateUtil;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Objects;

public class APImportationAmat extends APAbstractImportationAmatApat {

    private static final Logger LOG = LoggerFactory.getLogger(APImportationAmat.class);

    public APImportationAmat(LinkedList<String> err, LinkedList<String> inf) {
        super(err, inf);
        typeDemande = IPRDemande.CS_TYPE_MATERNITE;
    }

    @Override
    public APDroitLAPG createDroit(Content content, String npaFormat, PRDemande demande, BTransaction transaction, BSession bsession) {
        APDroitMaternite newDroit = new APDroitMaternite();

        try {
            // TODO : Création du droit maternité
            newDroit.setIdDemande(demande.getIdDemande());
            newDroit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ENREGISTRE);
            newDroit.setGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE);
            newDroit.setReference(content.getReferenceData());
            newDroit.setIdCaisse(creationIdCaisse());
            newDroit.setNpa(npaFormat);
            // TODO : récupérer code du pays
            newDroit.setPays("100");

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
            Date date = new Date();
            newDroit.setDateDepot(date.getSwissValue());
            newDroit.setDateReception(date.getSwissValue());

            newDroit.setIsSoumisImpotSource(isSoumisImpotSource(content));

            newDroit.setSession(bsession);
            newDroit.add(transaction);

        } catch (Exception e) {
            errors.add("Une erreur s'est produite lors de la création du droit maternité " + e.getMessage());
            LOG.error("Une erreur s'est produite lors de la création du droit : ", e);
        }

        return newDroit;
    }

    @Override
    public void createSituationFamiliale(FamilyMembers membresFamille, String idDroit, BTransaction transaction, BSession bsession) {
        try {
            for (Child child : membresFamille.getChildren().getChild()) {
                APSituationFamilialeMat enfant = new APSituationFamilialeMat();
                enfant.setNom(child.getOfficialName());
                enfant.setPrenom(child.getFirstName());
                enfant.setDateNaissance(tranformGregDateToGlobDate(child.getDateOfBirth()));
                enfant.setIdDroitMaternite(idDroit);
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
