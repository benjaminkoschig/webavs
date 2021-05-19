package globaz.apg.process;

import apg.amatapat.*;
import ch.globaz.common.domaine.Date;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.*;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeDateUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.phenix.api.ICPDonneesCalcul;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.api.IPRSituationProfessionnelle;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRSession;
import org.slf4j.Logger;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Objects;

public class APImportationAmat extends APAbstractImportationAmatApat {

    private static final String BENEFICIAIRE_MERE = "MERE";
    private static final String BENEFICIAIRE_EMPLOYEUR = "EMPLOYEUR";

    public APImportationAmat(LinkedList<String> err, LinkedList<String> inf, Logger log) {
        super(err, inf, log);
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

    @Override
    public void createSituationProfessionnel(Content content, String idDroit, BTransaction transaction, BSession bsession) {
        switch (content.getFormType()) {
            case FORM_INDEPENDANT:
                creationSituationProIndependant(content, idDroit, transaction, bsession);
                break;
            case FORM_SALARIE:
                creationSituationProEmploye(content, idDroit, transaction, bsession);
                break;
            default:
                break;
        }
    }

    private void creationSituationProEmploye(Content content, String idDroit, BTransaction transaction, BSession bsession) {
        Salary salaire = content.getProvidedByEmployer().getSalary();
        String salaireMensuel = String.valueOf(salaire.getLastIncome().getAmount());
        MainEmployer mainEmployeur = content.getMainEmployer();
        boolean isVersementEmployeur;
        switch (content.getPaymentContact().getBeneficiaryType()) {
            case BENEFICIAIRE_EMPLOYEUR:
                isVersementEmployeur = true;
                break;
            case BENEFICIAIRE_MERE:
            default:
                isVersementEmployeur = false;
                break;
        }

        try {
            AFAffiliation affiliation = findAffiliationByNumero(mainEmployeur.getAffiliateID(), bsession);
            if (affiliation != null) {
                APEmployeur emp = new APEmployeur();
                emp.setSession(bsession);
                emp.setIdTiers(affiliation.getIdTiers());
                emp.setIdAffilie(affiliation.getAffiliationId());
                emp.add(transaction);

                APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
                situationProfessionnelle.setSession(bsession);
                situationProfessionnelle.setIdDroit(idDroit);
                situationProfessionnelle.setIdEmployeur(emp.getIdEmployeur());
                situationProfessionnelle.setIsIndependant(false);
                situationProfessionnelle.setIsVersementEmployeur(isVersementEmployeur);
                situationProfessionnelle.setSalaireMensuel(salaireMensuel);

                // Vague 2 - Si le salarié est payé sur 13 mois
                // On ajoute son 13eme mois sans une autre rémunération annuelle
                if (salaire.getLastIncome().isHasThirteenthMonth()) {
                    situationProfessionnelle.setAutreRemuneration(salaireMensuel);
                    situationProfessionnelle.setPeriodiciteAutreRemun(IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE);
                }

                situationProfessionnelle.wantCallValidate(false);
                situationProfessionnelle.add(transaction);
            }
        } catch (Exception e) {
            errors.add("Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ");
            LOG.error("APImportationAPGPandemie#creerSituationProf : Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ", e);
            if (isJadeThreadError()) {
                addJadeThreadErrorToListError("Situation Professionnelle Employé");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'ajout de la situation prof
//                JadeThread.logClear();
            }
        }
    }

    private void creationSituationProIndependant(Content content, String idDroit, BTransaction transaction, BSession bsession) {
        String masseAnnuel = "0";
        MainEmployer mainEmployeur = content.getMainEmployer();
        boolean isVersementEmployeur;
        switch (content.getPaymentContact().getBeneficiaryType()) {
            case BENEFICIAIRE_EMPLOYEUR:
                isVersementEmployeur = true;
                break;
            case BENEFICIAIRE_MERE:
            default:
                isVersementEmployeur = false;
                break;
        }

        try {
            AFAffiliation affiliation = findAffiliationByNumero(mainEmployeur.getAffiliateID(), bsession);
            if (affiliation != null) {
                APEmployeur emp = new APEmployeur();
                emp.setSession(bsession);
                emp.setIdTiers(affiliation.getIdTiers());
                emp.setIdAffilie(affiliation.getAffiliationId());
                emp.add(transaction);

                // on cherche la decision
                final CPDecisionManager decision = new CPDecisionManager(); //(CPDecision) bsession.getAPIFor(CPDecision.class);
                BSession sessionPhenix = new BSession("PHENIX");
                bsession.connectSession(sessionPhenix);

                decision.setSession(sessionPhenix);
                decision.setForIdAffiliation(affiliation.getAffiliationId());
                decision.setForIsActive(true);
                // TODO : année prise en compte.
//                decision.setForAnneeDecision(ANNEE_PRISE_COMPTE_SALAIRE);
                decision.find(BManager.SIZE_NOLIMIT);

                // on cherche les données calculées en fonction de la
                // decision
                if ((decision != null) && (decision.size() > 0)) {

                    final ICPDonneesCalcul donneesCalcul = (ICPDonneesCalcul) bsession
                            .getAPIFor(ICPDonneesCalcul.class);
                    donneesCalcul.setISession(PRSession.connectSession(bsession, "PHENIX"));

                    final Hashtable<Object, Object> parms = new Hashtable<Object, Object>();
                    parms.put(ICPDonneesCalcul.FIND_FOR_ID_DECISION, ((CPDecision) decision.getEntity(0)).getIdDecision());
                    parms.put(ICPDonneesCalcul.FIND_FOR_ID_DONNEES_CALCUL, ICPDonneesCalcul.CS_REV_NET);

                    final ICPDonneesCalcul[] donneesCalculs = donneesCalcul.findDonneesCalcul(parms);

                    if ((donneesCalculs != null) && (donneesCalculs.length > 0)) {
                        masseAnnuel = donneesCalculs[0].getMontant();
                    }
                }


                APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
                situationProfessionnelle.setSession(bsession);
                situationProfessionnelle.setIdDroit(idDroit);
                situationProfessionnelle.setIdEmployeur(emp.getIdEmployeur());
                situationProfessionnelle.setIsIndependant(true);
                situationProfessionnelle.setIsVersementEmployeur(isVersementEmployeur);
                // TODO : comment obtenir le salaire
                situationProfessionnelle.setRevenuIndependant(masseAnnuel);

                situationProfessionnelle.wantCallValidate(false);
                situationProfessionnelle.add(transaction);
            }
        } catch (Exception e) {
            errors.add("Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ");
            LOG.error("APImportationAPGPandemie#creerSituationProf : Erreur rencontré lors de la création de la situation professionnelle pour l'assuré ", e);
            if (isJadeThreadError()) {
                addJadeThreadErrorToListError("Situation Professionnelle Employé");
                // Il faut qu'on puisse ajouter le droit même s'il y a eu un problème dans l'ajout de la situation prof
//                JadeThread.logClear();
            }
        }

    }
}
