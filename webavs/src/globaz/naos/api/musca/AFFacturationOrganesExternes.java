/*
 * Créé le 28 avr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.api.musca;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.external.IntModuleFacturation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.translation.CodeSystem;

/**
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFFacturationOrganesExternes extends AFFacturationGenericImpl implements IntModuleFacturation {
    /**
	 * 
	 */
    private String rubrique = "";

    public AFFacturationOrganesExternes() {
        super();
        // TODO Raccord de constructeur auto-généré
    }

    /**
     * Création de la compensation pour l'organe externe
     * 
     * @param passage
     * @param context
     * @param idModuleFacturation
     * @param organesExternes
     * @param montant
     * @throws Exception
     */
    protected void creerCompensationPourOrganeExterne(IFAPassage passage, BProcess context, String idModuleFacturation,
            AFOrganeExternes organesExternes, FWCurrency montant) throws Exception {
        montant.negate();
        FAAfact afact = new FAAfact();
        afact.setIdEnteteFacture(organesExternes.getIdEnteteFacture());
        afact.setMontantFacture(montant.toString());
        afact.setIdPassage(passage.getIdPassage());
        afact.setIdRubrique(getRubrique());
        afact.setIdTypeAfact(FAAfact.CS_AFACT_COMPENSATION);
        afact.setNonImprimable(Boolean.FALSE);
        afact.setNonComptabilisable(Boolean.FALSE);
        afact.setAQuittancer(Boolean.FALSE);
        afact.setSession(context.getSession());
        afact.setCheckIdExterneFactureCompensation(Boolean.FALSE);
        afact.setIdModuleFacturation(idModuleFacturation);
        afact.add(context.getTransaction());
    }

    /**
     * Déterminationd e la rubrique comptable selon le type de l'organe externe
     * 
     * @param typeOrganesExternes
     * @param rubriqueRefugie
     * @param rubriqueAssiste
     * @param rubriqueBeneficiaire
     * @param rubriqueBeneficiaireFamille
     * @param session
     * 
     * @throws Exception
     */
    protected void determineRubriqueOrganeExterne(String typeOrganesExternes, String rubriqueRefugie,
            String rubriqueAssiste, String rubriqueBeneficiaire, String rubriqueBeneficiaireFamille, BSession session)
            throws Exception {
        setRubrique("");
        if (typeOrganesExternes.equals(CodeSystem.PARTIC_AFFILIE_REFUGIE)) {
            setRubrique(rubriqueRefugie);
        } else if (typeOrganesExternes.equals(CodeSystem.PARTIC_AFFILIE_NON_ACTIF_ASSISSTE)) {
            setRubrique(rubriqueAssiste);
        } else if (typeOrganesExternes.equals(CodeSystem.PARTIC_AFFILIE_NON_ACTIF_BENEFICIAIRE)) {
            setRubrique(rubriqueBeneficiaire);
        } else if (typeOrganesExternes.equals(CodeSystem.PARTIC_AFFILIE_NON_ACTIF_BENEFICIAIRE_FAMILLE)) {
            setRubrique(rubriqueBeneficiaireFamille);
        }
        if (JadeStringUtil.isEmpty(getRubrique())) {
            throw new Exception(session.getLabel("RUBRIQUE_NUL") + " " + typeOrganesExternes);

        }
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        BTransaction cursorTransaction = null;
        BStatement statement = null;
        AFFacturationOrganesExternesManager manager = null;
        try {
            manager = new AFFacturationOrganesExternesManager();
            manager.setForPassageId(passage.getIdPassage());
            manager.setWantInnerJoinWhitEnteteFacture(true);
            manager.setSession(context.getSession());

            // ************************************************************
            // Création du cursorTransaction
            // ************************************************************
            cursorTransaction = (BTransaction) context.getSession().newTransaction();
            cursorTransaction.openTransaction();
            statement = manager.cursorOpen(cursorTransaction);

            String rubriqueRefugie = AFParticulariteAffiliation.findParameterNaos(context.getTransaction(),
                    CodeSystem.PARTIC_AFFILIE_REFUGIE, "RUBRIQUE1");
            String rubriqueAssiste = AFParticulariteAffiliation.findParameterNaos(context.getTransaction(),
                    CodeSystem.PARTIC_AFFILIE_NON_ACTIF_ASSISSTE, "RUBRIQUE2");
            String rubriqueBeneficiaire = AFParticulariteAffiliation.findParameterNaos(context.getTransaction(),
                    CodeSystem.PARTIC_AFFILIE_NON_ACTIF_BENEFICIAIRE, "RUBRIQUE3");
            String rubriqueBeneficiaireFamille = AFParticulariteAffiliation.findParameterNaos(context.getTransaction(),
                    CodeSystem.PARTIC_AFFILIE_NON_ACTIF_BENEFICIAIRE_FAMILLE, "RUBRIQUE4");

            AFOrganeExternes organesExternes = null;
            String idEnteteFactureTraite = "";
            while ((organesExternes = (AFOrganeExternes) manager.cursorReadNext(statement)) != null) {
                // Recherche année à prendre en compte
                if (!idEnteteFactureTraite.equalsIgnoreCase(organesExternes.getIdEnteteFacture())) {
                    FAAfactManager afactMng = new FAAfactManager();
                    afactMng.setSession(context.getSession());
                    afactMng.setForIdEnteteFacture(organesExternes.getIdEnteteFacture());
                    afactMng.find();
                    String anneeCotisation = "";
                    for (int i = 0; i < afactMng.getSize() && JadeStringUtil.isBlank(anneeCotisation); i++) {
                        FAAfact afactEntete = (FAAfact) afactMng.getEntity(i);
                        if (!JadeStringUtil.isBlankOrZero(afactEntete.getAnneeCotisation())) {
                            anneeCotisation = afactEntete.getAnneeCotisation();
                        }
                    }
                    if (JadeStringUtil.isBlank(anneeCotisation)) {
                        anneeCotisation = organesExternes.getIdExterneFacture().substring(0, 4);
                    }
                    String dateFinComparaison = "01.01." + anneeCotisation;
                    String dateDebutComparaison = "31.12." + anneeCotisation;

                    boolean dateFinOk = (JadeStringUtil.isBlankOrZero(organesExternes.getDateFin()) || BSessionUtil
                            .compareDateFirstGreaterOrEqual(context.getSession(), organesExternes.getDateFin(),
                                    dateFinComparaison));
                    boolean dateDebutOk = (JadeStringUtil.isBlankOrZero(organesExternes.getDateDebut()) || BSessionUtil
                            .compareDateFirstLowerOrEqual(context.getSession(), organesExternes.getDateFin(),
                                    dateDebutComparaison));
                    // Si dates ok et date de début différente de date de fin (cas ou on annule la période en mettant
                    // date de début = date de fin)
                    if (dateDebutOk && dateFinOk
                            && !organesExternes.getDateDebut().equalsIgnoreCase(organesExternes.getDateFin())) {
                        // Détermination de la rubrique comptable de compensation pour l'organe externe
                        determineRubriqueOrganeExterne(organesExternes.getTypeOrgane(), rubriqueRefugie,
                                rubriqueAssiste, rubriqueBeneficiaire, rubriqueBeneficiaireFamille,
                                context.getSession());
                        try {
                            if (haveDoubleParticularite(anneeCotisation, organesExternes.getNumAff(),
                                    context.getSession())) {
                                context.getMemoryLog().logMessage(
                                        context.getSession().getLabel("PARTIC_ERREUR_DOUBLE_1")
                                                + organesExternes.getNumAff()
                                                + context.getSession().getLabel("PARTIC_ERREUR_DOUBLE_2")
                                                + organesExternes.getIdExterneFacture(), FWMessage.AVERTISSEMENT,
                                        this.getClass().getName());
                            }
                            // Sélection maintenant faite dans le manager => test inutile
                            // if (this.isParticulariteValide(context, organesExternes, entete)) {
                            FWCurrency montant = new FWCurrency(organesExternes.getTotalFacture());
                            if (!montant.isZero()) {
                                // Création de la compensation pour l'organe externe
                                creerCompensationPourOrganeExterne(passage, context, idModuleFacturation,
                                        organesExternes, montant);
                                idEnteteFactureTraite = organesExternes.getIdEnteteFacture();
                            }
                        } catch (Exception e) {
                            context.getMemoryLog().logMessage(
                                    e.getMessage() + " " + organesExternes.getIdExterneFacture() + " "
                                            + organesExternes.getNumAff() + " " + organesExternes.getIdEnteteFacture(),
                                    FWMessage.AVERTISSEMENT, this.getClass().getName());
                        }
                    }
                }
            }
            if (!context.isAborted()) {
                return true;
            } else {
                return false;
            }
        } finally {
            try {
                if (statement != null) {
                    try {
                        manager.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } finally {
                if (cursorTransaction != null) {
                    cursorTransaction.closeTransaction();
                }
            }
        }
    }

    public String getRubrique() {
        return rubrique;
    }

    /**
     * Test si l'affilié à plus d'une particularité de type bénéficiare différent pour une année
     * 
     * @param idEntetefacture
     * @param numAffilie
     * @param session
     * @return
     * @throws Exception
     */

    private boolean haveDoubleParticularite(String anneeCotisation, String numAffilie, BSession session)
            throws Exception {
        if (JadeStringUtil.isEmpty(anneeCotisation) || JadeStringUtil.isEmpty(numAffilie)) {
            throw new Exception(session.getLabel("FACTURATION_ORGANEEXTERNE"));
        }
        AFFacturationOrganesExternesManager manager = new AFFacturationOrganesExternesManager();
        manager.setSession(session);
        manager.setForNumAffilie(numAffilie);
        manager.setAnneeSelection(anneeCotisation);
        manager.setWantInnerJoinWhitEnteteFacture(false);
        manager.find();
        if (manager.size() > 1) {
            return true;
        } else {
            return false;
        }
    }

    public void setRubrique(String rubrique) {
        this.rubrique = rubrique;
    }
}
