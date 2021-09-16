/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.plancalcul;

import java.util.ArrayList;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * @author SCE
 * 
 *         15 nov. 2010
 */
public class PCGroupeDepensesHandler extends PCGroupeAbstractHandler {
    /************************ CONSTANTES PAR CATEGORIE ***********************************************/
    String[] DEP_COTISATIONS_PSAL = { IPCValeursPlanCalcul.CLE_DEPEN_COT_PSAL_TOTAL };
    // CONstantes par catégorie
    String[] DEP_COUVERTURE_BESOINS_VITAUX = { IPCValeursPlanCalcul.CLE_DEPEN_BES_VITA_TOTAL };

    // String[] DEP_DEDUCTIONS = {};
    String[] DEP_DEPENSES_PERSONELLES = { IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL };
    String[] DEP_FRAIS_IMMOBILIERS = { IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE,
            IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE,
            IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL_PLAFONNE };
    String[] DEP_LOYER = { IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_LOYER_BRUT, IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_LOYER_NET,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_ACCOMPTE_CHARGES,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_VALEUR_LOCATIVE_APP_HABITE,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_CHARGES_FORFAITAIRES,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_DROIT_HABITATION,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_CHAUFFAGE,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TAXES_PENSION_NON_RECONNUE,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL_NON_PLAFONNE, IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_PLAFOND };
    String[] DEP_PENSION_ALIM_VERSE = { IPCValeursPlanCalcul.CLE_DEPEN_PENSVERS_TOTAL };

    String[] DEP_FRAIS_GARDE = { IPCValeursPlanCalcul.CLE_DEPEN_FRAIS_GARDE_TOTAL };
    String[] DEP_PRIME_ASSURANCE_MALADIE = { IPCValeursPlanCalcul.CLE_DEPEN_PRIME_ASSURANCE_MALADIE_TOTAL };
    String[] DEP_SEJOUR_MOIS_PARTIEL = { IPCValeursPlanCalcul.CLE_DEPEN_SEJOUR_MOIS_PARTIEL_TOTAL };
    /************************ FIN CONSTANTES CATEGORIE ***********************************************/
    String[] DEP_PRIMES_MOY_ASS_MALADIE = { IPCValeursPlanCalcul.CLE_TOTAL_PRIMEMAL_TOTAL };
    String[] DEP_TAXES_JOURNALIERE_HOME = { IPCValeursPlanCalcul.CLE_DEPEN_TAXEHOME_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_LONGUE_DUREE };
    String[] DEP_TOTAL_DEPENSES_RECONNUES = { IPCValeursPlanCalcul.CLE_DEPEN_DEP_RECO_TOTAL };

    /**
     * @param tupleRoot
     */
    public PCGroupeDepensesHandler(TupleDonneeRapport tupleRoot) {
        super(tupleRoot);
    }

    private void buildLoyerTotal(PCValeurPlanCalculHandler valeur) {
        // valeur.setCssClass(this.CSS_SOULIGNE);

        String csCol1 = IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL_NON_PLAFONNE;
        PCValeurPlanCalculHandler valCol1 = new PCValeurPlanCalculHandler(csCol1, getValeur(csCol1).toString(),
                ADDITION, NO_CSS_CLASS);

        String csCol2 = IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL;
        PCValeurPlanCalculHandler valCol2 = new PCValeurPlanCalculHandler(csCol2, getValeur(csCol2).toString(),
                ADDITION, CSS_SOULIGNE);

        String csCol3 = IPCValeursPlanCalcul.CLE_DEPEN_DEP_RECO_TOTAL;
        PCValeurPlanCalculHandler valCol3 = new PCValeurPlanCalculHandler(csCol3, getValeur(csCol3).toString(),
                ADDITION, CSS_SOULIGNE);

        // Soulignement derniere valeur avant total
        if (groupList.size() != 0) {
            groupList.get(groupList.size() - 1).getValCol1().setCssClass(CSS_SOULIGNE);
        }

        groupList.add(new PCLignePlanCalculHandler(valeur.getCodeSysteme(), getLegende(valeur.getCodeSysteme()),
                valCol1, valCol2, valCol3));
        groupList.add(setEmptyLigne());
        // this.groupList.add(this.createLignePlancalcul(valeur.getCodeSysteme(),
        // this.getLegende(valeur.getCodeSysteme()), this.NO_CSS_CLASS, valCol1, valeur, valCol3));
    }

    private void dealBlocFraisImmeuble() {
        //A voir si utiliser
    }

    /**
     * Traitement de la categorie des cotisations PSAL
     */
    private void dealCategorieCotisationsPSAL() {
        String[] tabCategorie = DEP_COTISATIONS_PSAL;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        Float val = getValeur(cs);// recup valeur
        String leg = getLegende(cs);
        // SI valeur diff de 0
        if (getValeur(cs) != 0f) {
            groupList.add(createLigneForGroupeList(cs, leg, val, 2));
        }
    }
    private void dealCategorieFraisGarde() {
        String[] tabCategorie = DEP_FRAIS_GARDE;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        Float val = getValeur(cs);// recup valeur
        String leg = getLegende(cs);
        // SI valeur diff de 0
        if (getValeur(cs) != 0f) {
            groupList.add(createLigneForGroupeList(cs, leg, val, 2));
        }
    }

    private void dealCategorieSejourMoisPartiel() {
        String[] tabCategorie = DEP_SEJOUR_MOIS_PARTIEL;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        Float val = getValeur(cs);// recup valeur
        String leg = getLegende(cs);
        // SI valeur diff de 0
        if (getValeur(cs) != 0f) {
            groupList.add(createLigneForGroupeList(cs, leg, val, 2));
        }
    }

    /**
     * Traitement de la catégorie frais Assurance Maladie
     */
    private void dealCategoriePrimeAssuranceMaladie() {
        String[] tabCategorie = DEP_PRIME_ASSURANCE_MALADIE;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        Float val = getValeur(cs);// recup valeur
        String leg = getLegende(cs);
        // SI valeur diff de 0
        if (getValeur(cs) != 0f) {
            groupList.add(createLigneForGroupeList(cs, leg, val, 2));
        }
    }

    /**
     * Traitement de la categorie couverture des besoins vitaux
     */
    private void dealCategorieCouvertureBesoinsVitaux() {
        String[] tabCategorie = DEP_COUVERTURE_BESOINS_VITAUX;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legCs = getLegende(cs);
        Float val = getValeur(cs);// recup valeur
        // SI valeur diff de 0
        if (getValeur(cs) != 0f) {
            // Ajoute la ligne
            groupList.add(createLigneForGroupeList(cs, legCs, val, 2));

        }
    }

    /**
     * Traitement de la categorie depenses personelles
     */
    private void dealCategorieDepensesPersonelles() {

        String[] tabCategorie = DEP_DEPENSES_PERSONELLES;

        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        // SI valeur diff de 0
        if (getValeur(cs) != 0f) {
            // on traite les membres si il y en a

            // iteration sur les membres
            for (int cpt = 0; cpt < tabCategorie.length; cpt++) {
                String csMembre = tabCategorie[cpt];
                String legCsMembre = getLegende(csMembre);
                Float valMembre = getValeur(csMembre);
                // si valeur != 0, on traite
                if (valMembre != 0f) {
                    groupList.add(createLigneForGroupeList(csMembre, legCsMembre, valMembre, 2));
                }
            }
        }
    }

    private void dealCategorieFraisImmobiliers() {

        // in traite suelement le bloc si les intérêtes hypothécaires sont définies
        boolean isInteretsHypoDefinis = isBlocFraisImmoDisplayable();

        if (isInteretsHypoDefinis) {

            // iteration sur les membres
            for (String csMembre : DEP_FRAIS_IMMOBILIERS) {
                String legCsMembre = getLegende(csMembre);
                Float valMembre = getValeur(csMembre);

                // Si ligne totale
                if (csMembre.equals(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL_PLAFONNE)) {

                    // Valeur totale
                    PCValeurPlanCalculHandler valeurTot = createValeurPlanCalcul(
                            IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL,
                            getValeur(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL).toString(), ADDITION, NO_CSS_CLASS);
                    // Valeur plafond
                    // TODO vériifer clé pour plafond
                    // String csValForf = IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_REVENU;//Modif forfait
                    String csValForf = IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL_PLAFONNE;
                    Float valCsForf = getValeur(csValForf);
                    PCValeurPlanCalculHandler valeurForfait = createValeurPlanCalcul(csValForf, valCsForf.toString(),
                            ADDITION, CSS_SOULIGNE);
                    // Valeur sous total (en fait total)
                    String csSousTot = IPCValeursPlanCalcul.CLE_DEPEN_DEP_RECO_TOTAL;
                    Float valSousTot = getValeur(csSousTot);
                    PCValeurPlanCalculHandler valeurSousTotal = createValeurPlanCalcul(csSousTot,
                            valSousTot.toString(), ADDITION, CSS_SOULIGNE);
                    groupList.add(new PCLignePlanCalculHandler(csMembre, legCsMembre, valeurTot, valeurForfait,
                            valeurSousTotal));
                } else if (csMembre.equals(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE)) {
                    // Valeur plafond
                    String cs = IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE;
                    Float val = getValeur(cs);

                    if (val > 0.0f) {
                        String leg = getLegende(cs);
                        PCValeurPlanCalculHandler valeurForfait = createValeurPlanCalcul(cs, val.toString(), ADDITION,
                                CSS_SOULIGNE);

                        groupList.get(groupList.size() - 2).setValCol3(VALEUR_VIDE);

                        groupList.add(createLignePlanCalcul(cs, leg, valeurForfait, VALEUR_VIDE, VALEUR_VIDE));
                    } else {
                        groupList.get(groupList.size() - 1).getValCol1().setCssClass(CSS_SOULIGNE);
                    }

                } else {
                    // Si interert hypothécaire on supprime la valeur de la colone 3 du plan de calcul, loyer total
                    // souligne
                    // TODO PCAL à voir
                    if (csMembre.equals(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE)
                            && (groupList.size() > 1)) {
                        groupList.get(groupList.size() - 2).setValCol3(VALEUR_VIDE);
                    }
                    groupList.add(createLigneForGroupeList(csMembre, legCsMembre, valMembre, 1));
                }

            }
        }

    }

    /**
     * Traitement de la categorie Loyer
     */
    private void dealCategorieLoyer() {
        String[] tabCategorie = DEP_LOYER;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legCs = getLegende(cs);
        Float val = getValeur(cs);// recup valeur
        // SI valeur diff de 0
        if (getValeur(cs) != 0f) {
            // on traite les membres si il y en a

            // iteration sur les membres
            for (int cpt = 1; cpt < tabCategorie.length; cpt++) {
                String csMembre = tabCategorie[cpt];
                String legCsMembre = getLegende(csMembre);
                Float valMembre = getValeur(csMembre);
                // si valeur != 0, on traite
                if (valMembre != 0f) {
                    // Si loyer net ou brut affichage standard, ou interet
                    // hypothecaires
                    if (csMembre.equals(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_LOYER_BRUT)
                            || csMembre.equals(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_LOYER_NET)
                            || csMembre.equals(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE)
                            || csMembre.equals(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_CHAUFFAGE)
                            || csMembre.equals(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_ACCOMPTE_CHARGES)) {
                        groupList.add(createLigneForGroupeList(csMembre, legCsMembre, valMembre, 1));
                    } else if (csMembre.equals(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_PLAFOND)) {

                        buildLoyerTotal(createValeurPlanCalcul(csMembre, getValeur(csMembre).toString(), ADDITION,
                                NO_CSS_CLASS));
                    } else if (!csMembre.equals(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL_NON_PLAFONNE)) {
                        // Si soustotal loyer bien immo

                        // traitement charges a additionner
                        // @TODO
                        groupList.add(createLigneForGroupeList(csMembre, legCsMembre, valMembre, 1));
                    }
                }
            }

        } else {
            groupList.add(createLigneForGroupeList(cs, legCs, val, 2));
        }

    }

    /**
     * Traitement de la categorie des pensions alimentaires
     */
    private void dealCategoriePensionAlimentaireVerse() {
        String[] tabCategorie = DEP_PENSION_ALIM_VERSE;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        // SI valeur diff de 0
        if (getValeur(cs) != 0f) {
            // on traite les membres si il y en a

            // iteration sur les membres
            for (int cpt = 0; cpt < tabCategorie.length; cpt++) {
                String csMembre = tabCategorie[cpt];
                String legCsMembre = getLegende(csMembre);
                Float valMembre = getValeur(csMembre);
                // si valeur != 0, on traite
                if (valMembre != 0f) {
                    groupList.add(createLigneForGroupeList(csMembre, legCsMembre, valMembre, 2));
                }
            }
        }
    }

    /**
     * Traitement de la categorie primes moyennes assurances maladie
     */
    private void dealCategoriePrimesMoyennesAssMaladie() {
        String[] tabCategorie = DEP_PRIMES_MOY_ASS_MALADIE;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        // SI valeur diff de 0
        if (getValeur(cs) != 0f) {
            // on traite les membres si il y en a

            // iteration sur les membres
            for (int cpt = 1; cpt < tabCategorie.length; cpt++) {
                String csMembre = tabCategorie[cpt];
                String legCsMembre = getLegende(csMembre);
                Float valMembre = getValeur(csMembre);
                // si valeur != 0, on traite
                if (valMembre != 0f) {
                    groupList.add(createLigneForGroupeList(csMembre, legCsMembre, valMembre, 2));
                }
            }
        }
    }

    /**
     * Traitement de la categorie taxes journalieres home
     */
    private void dealcategorieTaxesJournaliereHomes() {
        String[] tabCategorie = DEP_TAXES_JOURNALIERE_HOME;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        // SI valeur diff de 0
        if (getValeur(cs) != 0f) {
            // on traite les membres si il y en a

            // iteration sur les membres
            for (int cpt = 0; cpt < tabCategorie.length; cpt++) {
                String csMembre = tabCategorie[cpt];
                Float valMembre = getValeur(csMembre);
                String legCsMembre = getLegende(csMembre);
                // si valeur != 0, on traite
                if (valMembre != 0f || csMembre.equals(IPCValeursPlanCalcul.CLE_DEPEN_TAXEHOME_TOTAL)) {
                    groupList.add(createLigneForGroupeList(csMembre, legCsMembre, valMembre, 2));
                }
            }
        }
    }

    /**
     * Traitement du bloc total
     */
    private void dealCategorieTotal() {
        String[] tabCategorie = DEP_TOTAL_DEPENSES_RECONNUES;
        String cs = tabCategorie[0];
        String legCs = getLegende(cs);
        Float val = getValeur(cs);
        PCValeurPlanCalculHandler valeur = createValeurPlanCalcul(cs, val.toString(), ADDITION, NO_CSS_CLASS);
        PCLignePlanCalculHandler ligneTotal = createLignePlanCalcul(cs, legCs, VALEUR_VIDE, VALEUR_VIDE, valeur);
        ligneTotal.setCssClass("total");
        groupList.add(ligneTotal);
    }

    /**
     * Génération des lignes à afficher dans la page jsp
     */
    private void generateLigneProcess() {
        dealCategorieCouvertureBesoinsVitaux();
        dealCategoriePrimeAssuranceMaladie();
        dealCategoriePrimesMoyennesAssMaladie();
        dealcategorieTaxesJournaliereHomes();
        dealCategorieDepensesPersonelles();

        dealCategoriePensionAlimentaireVerse();
        dealCategorieFraisGarde();
        dealCategorieCotisationsPSAL();
        dealCategorieSejourMoisPartiel();
        dealCategorieLoyer();
        dealCategorieFraisImmobiliers();
        // this.dealCategorieRevenusDessaisis();

        groupList.add(setEmptyLigne());
        dealCategorieTotal();
    }

    /**
     * @return the groupList
     */
    public ArrayList<PCLignePlanCalculHandler> getGroupList() {
        generateLigneProcess();
        return groupList;
    }

    private boolean isBlocFraisImmoDisplayable() {
        return (getValeur(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE) != 0f)
                || (getValeur(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE) != 0.f);
    }
}
