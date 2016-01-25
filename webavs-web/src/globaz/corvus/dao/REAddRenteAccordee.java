package globaz.corvus.dao;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;

/**
 * 
 * <H1>Description</H1>
 * 
 * 
 * <p>
 * Une classe qui propose des méthodes pour ajouter en cascade les objets liées
 * </p>
 * 
 * Pour l'ajout d'une rente accordée :
 * 
 * --> Ajoute la rente accordée --> Création de info compta, sans l'id du compte annexe
 * 
 * 
 * 
 * @author scr
 */

public final class REAddRenteAccordee {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * Ajoute une rente accordée dans la DB avec les traitements suivants :
     * 
     * --> Ajoute la rente accordée --> Création de la section 'Info compta' --> MAJ de la référence du compte annexe
     * 
     * return : id de la rente accordée.
     * 
     */
    public static final String addRenteAccordeeCascade_noCommit(BSession session, BITransaction transaction,
            RERenteAccordee ra, int validationLevel) throws Exception {

        REInformationsComptabilite ic = new REInformationsComptabilite();
        ic.setSession(session);

        // La création du compte annexe doit se faire lors de la validation de
        // la décision.
        // Ceci pour éviter de créer potentiellement des compte annexes
        // inutiles.

        // Recherche de l'adresse de paiement
        ic.setAdressePaiement(PRTiersHelper.getAdressePaiementData(session, (BTransaction) transaction,
                ra.getIdTiersBeneficiaire(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "",
                JACalendar.todayJJsMMsAAAA()));

        if (JadeStringUtil.isBlankOrZero(ic.getIdTiersAdressePmt())) {

            ic.setAdressePaiement(PRTiersHelper.getAdressePaiementData(session, (BTransaction) transaction,
                    ra.getIdTiersBaseCalcul(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "",
                    JACalendar.todayJJsMMsAAAA()));

        }

        // bz-5163
        if (JadeStringUtil.isBlankOrZero(ic.getIdTiersAdressePmt())) {
            RERenteAccordeeManager mgr = new RERenteAccordeeManager();
            mgr.setSession(session);
            mgr.setForIdTiersBeneficiaire(ra.getIdTiersBeneficiaire());
            mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL);
            mgr.setOrderBy(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + " DESC ");
            mgr.find(transaction, 1);
            if (!mgr.isEmpty()) {
                RERenteAccordee ancienneRA = (RERenteAccordee) mgr.getFirstEntity();
                REInformationsComptabilite ic2 = new REInformationsComptabilite();
                ic2.setSession(session);
                ic2.setIdInfoCompta(ancienneRA.getIdInfoCompta());
                ic2.retrieve(transaction);
                ic.setIdTiersAdressePmt(ic2.getIdTiersAdressePmt());
            }
        }

        ic.add(transaction);

        ra.setIdInfoCompta(ic.getIdInfoCompta());
        ra.add(transaction);
        return ra.getIdPrestationAccordee();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private REAddRenteAccordee() {
        // peut pas creer d'instances
    }
}
