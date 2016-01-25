/*
 * Créé le 23 juil. 07
 */

package globaz.corvus.vb.creances;

import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculManager;
import globaz.corvus.db.creances.RECreanceAccordee;
import globaz.corvus.db.creances.RECreanceAccordeeManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.vb.rentesaccordees.RERenteAccordeeListViewBean;
import globaz.corvus.vb.rentesaccordees.RERenteAccordeeViewBean;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author BSC
 * 
 */

public class RERepartirLesCreancesViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // Creances accordees
    RECreanceAccordee[][] creancesAccordees = null;
    // Creanciers
    ArrayList creanciers = new ArrayList();

    // différences
    ArrayList differences = new ArrayList();
    private String idDemandeRente = "";

    // Demande de rente
    private String idTiers = "";

    FWCurrency montantRetroactifTotal = new FWCurrency("0.0");

    // montants disponibles
    ArrayList montantsDisponibles = new ArrayList();

    // Rentes accordees
    ArrayList rentesAccordees = new ArrayList();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public RERepartirLesCreancesViewBean(BISession session, String idDemandeRente) {
        super();
        setISession(session);
        setIdDemandeRente(idDemandeRente);

        // recherche les creanciers
        initCreanciers();

        // recherche les rentes accordees
        initRentesAccodees();

        // recherche les creances accordees
        initCreancesAccordees();

        // calcul des montants disponible
        initMontantsDisponible();

        // calcul des differences
        initCalculDesDifferences();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     * @param i
     * @return
     */
    public RECreanceAccordee getCreanceAccordee(int r, int m) {
        return creancesAccordees[r][m];
    }

    /**
     * 
     * @param i
     * @return
     */
    public RECreancierViewBean getCreancier(int i) {
        return (RECreancierViewBean) creanciers.get(i);
    }

    /**
     * 
     * @param i
     * @return
     */
    public String getDifference(int i) {
        return (String) differences.get(i);
    }

    /**
     * @return
     */
    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * @return
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * 
     * @param i
     * @return
     */
    public String getMontantDisponible(int i) {
        return (String) montantsDisponibles.get(i);
    }

    /**
     * @return
     */
    public String getMontantRetroactifTotal() {
        return montantRetroactifTotal.toStringFormat();
    }

    /**
     * 
     * @return le nombre de creanciers
     */
    public int getNombreCreanciers() {
        return creanciers.size();
    }

    /**
     * 
     * @return le nombre de rentes accordees
     */
    public int getNombreRentesAccordees() {
        return rentesAccordees.size();
    }

    /**
     * 
     * @param i
     * @return
     */
    public RERenteAccordee getRenteAccordee(int i) {
        return (RERenteAccordeeViewBean) rentesAccordees.get(i);
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * 
     * @param cr
     * @return
     */
    public String getTotalMontantReparti(int cr) {

        FWCurrency montantDistribue = new FWCurrency("0.00");

        // on somme les creances accordee sur ce creancier
        for (int ra = 0; ra < getNombreRentesAccordees(); ra++) {
            montantDistribue.add(getCreanceAccordee(ra, cr).getMontant());
        }

        return montantDistribue.toString();
    }

    /**
     * 
     * @return
     */
    public String getTotalMontantsDeisponibles() {

        FWCurrency totalMontantDisponible = new FWCurrency("0.00");

        for (int i = 0; i < montantsDisponibles.size(); i++) {

            totalMontantDisponible.add(new FWCurrency(getMontantDisponible(i)));
        }

        return totalMontantDisponible.toString();
    }

    /**
	 *
	 */
    private void initCalculDesDifferences() {

        // pour tous les creancier
        for (int cr = 0; cr < getNombreCreanciers(); cr++) {

            FWCurrency montantRevandique = new FWCurrency(getCreancier(cr).getMontantRevandique());
            FWCurrency montantRecupere = new FWCurrency("0.00");

            // on somme les montants donnes pour chacune des rentes accordees
            for (int ra = 0; ra < getNombreRentesAccordees(); ra++) {

                montantRecupere.add(getCreanceAccordee(ra, cr).getMontant());
            }
            montantRevandique.sub(montantRecupere);

            differences.add(montantRevandique.toStringFormat());
        }
    }

    /**
	 *
	 */
    private void initCreancesAccordees() {
        creancesAccordees = new RECreanceAccordee[getNombreRentesAccordees()][getNombreCreanciers()];

        // pour toutes les rentes accordees
        for (int ra = 0; ra < getNombreRentesAccordees(); ra++) {

            // pour toutes les creanciers
            for (int cr = 0; cr < getNombreCreanciers(); cr++) {

                RECreanceAccordeeManager caManager = new RECreanceAccordeeManager();
                caManager.setSession(getSession());
                caManager.setForIdCreancier(getCreancier(cr).getIdCreancier());
                caManager.setForIdRenteAccordee(getRenteAccordee(ra).getIdPrestationAccordee());
                try {
                    caManager.find();

                    if (caManager.size() > 0) {
                        creancesAccordees[ra][cr] = (RECreanceAccordee) caManager.getFirstEntity();
                    } else {
                        RECreanceAccordee creanceAccordee = new RECreanceAccordee();
                        creanceAccordee.setSession(getSession());
                        creanceAccordee.setIdCreancier(getCreancier(cr).getIdCreancier());
                        creanceAccordee.setIdRenteAccordee(getRenteAccordee(ra).getIdPrestationAccordee());
                        creanceAccordee.setMontant("0.00");
                        creancesAccordees[ra][cr] = creanceAccordee;
                    }
                } catch (Exception e) {
                    RECreanceAccordee creanceAccordee = new RECreanceAccordee();
                    creanceAccordee.setSession(getSession());
                    creanceAccordee.setIdCreancier(getCreancier(cr).getIdCreancier());
                    creanceAccordee.setIdRenteAccordee(getRenteAccordee(ra).getIdPrestationAccordee());
                    creanceAccordee.setMontant("0.00");
                    creancesAccordees[ra][cr] = creanceAccordee;
                }
            }
        }
    }

    /**
     * Initialisation des creanciers
     * 
     */
    private void initCreanciers() {
        RECreancierListViewBean crManager = new RECreancierListViewBean();
        crManager.setSession(getSession());
        crManager.setForIdDemandeRente(getIdDemandeRente());
        try {
            crManager.find();
        } catch (Exception e) {
            return;
        }

        Iterator iter = crManager.iterator();
        RECreancierViewBean cr = null;
        while (iter.hasNext()) {
            cr = (RECreancierViewBean) iter.next();

            creanciers.add(cr);
        }
    }

    /**
	 *
	 */
    private void initMontantsDisponible() {

        // pour toutes les rentes accordees
        for (int ra = 0; ra < getNombreRentesAccordees(); ra++) {

            FWCurrency montantRenteAccordee = new FWCurrency(
                    ((RERenteAccordeeViewBean) getRenteAccordee(ra)).getMontantRetroactif());
            FWCurrency montantDistribue = new FWCurrency("0.00");

            // on somme les creances accordee sur cette rente
            for (int cr = 0; cr < getNombreCreanciers(); cr++) {

                montantDistribue.add(getCreanceAccordee(ra, cr).getMontant());
            }
            montantRenteAccordee.sub(montantDistribue);

            montantsDisponibles.add(montantRenteAccordee.toStringFormat());
        }
    }

    /**
     * Initialisation des rentes accordees
     * 
     */
    private void initRentesAccodees() {

        try {
            REDemandeRente dr = new REDemandeRente();
            dr.setSession(getSession());
            dr.setIdDemandeRente(getIdDemandeRente());
            dr.retrieve();

            if (dr != null && !JadeStringUtil.isIntegerEmpty(dr.getIdRenteCalculee())) {

                // on cherche les bases de calcul pour la demande de rents
                REBasesCalculManager bcManager = new REBasesCalculManager();
                bcManager.setSession(getSession());
                bcManager.setForIdRenteCalculee(dr.getIdRenteCalculee());
                bcManager.find();

                for (int i = 0; i < bcManager.size(); i++) {

                    REBasesCalcul bc = (REBasesCalcul) bcManager.getEntity(i);

                    // pour toutes les rentes accordees de la base de calcul
                    RERenteAccordeeListViewBean raManager = new RERenteAccordeeListViewBean();
                    raManager.setSession(getSession());
                    raManager.setForIdBaseCalcul(bc.getIdBasesCalcul());
                    raManager.find();

                    for (int j = 0; j < raManager.size(); j++) {

                        RERenteAccordeeViewBean ra = (RERenteAccordeeViewBean) raManager.getEntity(j);

                        // on cherche le montant retroactif pour cette rente
                        REPrestationsDuesManager pdManager = new REPrestationsDuesManager();
                        pdManager.setSession(getSession());
                        pdManager.setForIdRenteAccordes(ra.getIdPrestationAccordee());
                        pdManager.setForCsType(IREPrestationDue.CS_TYPE_MNT_TOT);
                        pdManager.find();

                        if (pdManager.size() > 0) {
                            ra.setMontantRetroactif(((REPrestationDue) pdManager.getFirstEntity()).getMontant());
                            montantRetroactifTotal.add(((REPrestationDue) pdManager.getFirstEntity()).getMontant());
                        } else {
                            continue;
                        }

                        rentesAccordees.add(ra);
                    }
                }
            }
        } catch (Exception e) {
            return;
        }
    }

    public boolean isModifiable() {

        REDemandeRente dr = new REDemandeRente();
        dr.setSession(getSession());
        dr.setIdDemandeRente(getIdDemandeRente());
        try {
            dr.retrieve();
        } catch (Exception e) {
            return false;
        }

        if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(dr.getCsEtat())
                || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(dr.getCsEtat())
                || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_PAYE.equals(dr.getCsEtat())
                || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(dr.getCsEtat())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param string
     */
    public void setIdDemandeRente(String string) {
        idDemandeRente = string;
    }

    /**
     * @param string
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    @Override
    public boolean validate() {
        return false;
    }
}
