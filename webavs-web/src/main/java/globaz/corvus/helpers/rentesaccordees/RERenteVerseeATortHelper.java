package globaz.corvus.helpers.rentesaccordees;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRente;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRenteManager;
import globaz.corvus.db.rentesverseesatort.RERenteVerseeATortJointRenteAccordee;
import globaz.corvus.db.rentesverseesatort.RERenteVerseeATortJointRenteAccordeeManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.rentesaccordees.RERenteVerseeATortViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRHybridHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.business.services.CorvusCrudServiceLocator;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.utils.rentesverseesatort.REApercuRenteVerseeATort;
import ch.globaz.corvus.utils.rentesverseesatort.RELigneApercuRenteVerseeATort;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;

/**
 * @author PBA
 */
public class RERenteVerseeATortHelper extends PRHybridHelper {

    static boolean modificationPossible(final Collection<REDecisionJointDemandeRente> decisions) {

        boolean modificationPossible = false;
        boolean uniquementDesDecisionsCourant = true;
        if (decisions.size() > 0) {
            for (REDecisionJointDemandeRente uneDecision : decisions) {
                uniquementDesDecisionsCourant &= IREDecision.CS_TYPE_DECISION_COURANT.equals(uneDecision
                        .getCsTypeDecision());
                if (!IREDecision.CS_TYPE_DECISION_COURANT.equals(uneDecision.getCsTypeDecision())) {
                    modificationPossible |= !IREDecision.CS_ETAT_VALIDE.equals(uneDecision.getCsEtat());
                }
            }
        } else {
            modificationPossible = true;
        }

        return modificationPossible || uniquementDesDecisionsCourant;
    }

    @Override
    protected void _retrieve(final FWViewBeanInterface viewBean, final FWAction action, final BISession session)
            throws Exception {
        RERenteVerseeATortViewBean renteVerseeATortViewBean = (RERenteVerseeATortViewBean) viewBean;
        renteVerseeATortViewBean.setSession((BSession) session);
        renteVerseeATortViewBean = chargerDonnesPourMenuOptions(chargerApercuesPourDemande(renteVerseeATortViewBean));

        REDecisionJointDemandeRenteManager manager = new REDecisionJointDemandeRenteManager();
        manager.setSession((BSession) session);
        manager.setForIdDemandeRente(renteVerseeATortViewBean.getIdDemandeRente());
        manager.find();

        if ((manager.size() == 0) && !JadeStringUtil.isBlankOrZero(renteVerseeATortViewBean.getIdDecision())) {
            renteVerseeATortViewBean.setDecisionSupprimee(true);
        }
        renteVerseeATortViewBean.setModificationPossible(RERenteVerseeATortHelper.modificationPossible(manager
                .getContainerAsList()));

        DemandeRente demande = CorvusCrudServiceLocator.getDemandeRenteCrudService().read(
                renteVerseeATortViewBean.getIdDemandeRente());
        // pour savoir si on affiche le message de confirmation lors de la modification des rentes versées à tort,
        // spécifiant que toutes les décisions de la demande vont être supprimées si on valide.
        renteVerseeATortViewBean.setSupprimerLaDecisionSiModification(!JadeStringUtil
                .isBlankOrZero(renteVerseeATortViewBean.getIdDecision())
                && (demande.comporteDesCreances() || demande.comporteDesInteretsMoratoires()));
    }

    private RERenteVerseeATortViewBean chargerApercuesPourDemande(final RERenteVerseeATortViewBean viewBean)
            throws Exception {

        Collection<REApercuRenteVerseeATort> apercues = new ArrayList<REApercuRenteVerseeATort>();

        RERenteVerseeATortJointRenteAccordeeManager manager = new RERenteVerseeATortJointRenteAccordeeManager();
        manager.setSession(viewBean.getSession());
        manager.setForIdDemandeRente(viewBean.getIdDemandeRente());
        manager.find(BManager.SIZE_NOLIMIT);

        class InfoTiers {
            public String dateDeces;
            public String dateNaissance;
            public Long idTiers;
            public String nationalite;
            public String nom;
            public NumeroSecuriteSociale nss;
            public String prenom;
            public String sexe;

            @Override
            public boolean equals(final Object obj) {
                if (obj instanceof InfoTiers) {
                    return ((InfoTiers) obj).idTiers.equals(idTiers);
                }
                return false;
            }

            @Override
            public int hashCode() {
                StringBuilder hashCodeBuilder = new StringBuilder();
                hashCodeBuilder.append(idTiers).append(nom).append(prenom).append(nss);
                return hashCodeBuilder.toString().hashCode();
            }
        }

        Map<InfoTiers, SortedSet<RELigneApercuRenteVerseeATort>> apercuesParTiers = new HashMap<InfoTiers, SortedSet<RELigneApercuRenteVerseeATort>>();
        for (RERenteVerseeATortJointRenteAccordee uneRenteVerseeATort : manager.getContainerAsList()) {
            Periode periodeRenteVerseeATort = new Periode(uneRenteVerseeATort.getDateDebutRenteVerseeATort(),
                    uneRenteVerseeATort.getDateFinRenteVerseeATort());

            InfoTiers tiers = new InfoTiers();
            tiers.idTiers = uneRenteVerseeATort.getIdTiersBeneficiaire();
            tiers.nom = uneRenteVerseeATort.getNom();
            tiers.prenom = uneRenteVerseeATort.getPrenom();
            tiers.nss = uneRenteVerseeATort.getNss();
            tiers.dateDeces = uneRenteVerseeATort.getDateDeces();
            tiers.dateNaissance = uneRenteVerseeATort.getDateNaissance();
            tiers.nationalite = uneRenteVerseeATort.getNationalite();
            tiers.sexe = uneRenteVerseeATort.getSexe();

            CodePrestation codePrestation = CodePrestation.INCONNU;
            if (uneRenteVerseeATort.getCodePrestationAncienDroit() != null) {
                codePrestation = CodePrestation.getCodePrestation(uneRenteVerseeATort.getCodePrestationAncienDroit());
            } else if (uneRenteVerseeATort.getCodePrestationNouveauDroit() != null) {
                codePrestation = CodePrestation.getCodePrestation(uneRenteVerseeATort.getCodePrestationNouveauDroit());
            }

            RELigneApercuRenteVerseeATort nouvelleLigne = new RELigneApercuRenteVerseeATort(codePrestation,
                    periodeRenteVerseeATort, uneRenteVerseeATort.getMontant(),
                    uneRenteVerseeATort.getIdRenteAncienDroit(), uneRenteVerseeATort.getIdRenteNouveauDroit(),
                    uneRenteVerseeATort.getIdRenteVerseeATort(), uneRenteVerseeATort.getTypeRenteVerseeATort(),
                    uneRenteVerseeATort.isSaisieManuelle());
            if (apercuesParTiers.containsKey(tiers)) {
                apercuesParTiers.get(tiers).add(nouvelleLigne);
            } else {
                apercuesParTiers.put(tiers, new TreeSet<RELigneApercuRenteVerseeATort>(Arrays.asList(nouvelleLigne)));
            }
        }

        for (Entry<InfoTiers, SortedSet<RELigneApercuRenteVerseeATort>> unTiers : apercuesParTiers.entrySet()) {
            apercues.add(new REApercuRenteVerseeATort(unTiers.getKey().idTiers, unTiers.getKey().nom,
                    unTiers.getKey().prenom, unTiers.getKey().nss, unTiers.getKey().dateNaissance,
                    unTiers.getKey().dateDeces, unTiers.getKey().nationalite, unTiers.getKey().sexe, unTiers.getValue()));
        }

        viewBean.setApercues(apercues);

        return viewBean;
    }

    private RERenteVerseeATortViewBean chargerDonnesPourMenuOptions(final RERenteVerseeATortViewBean viewBean)
            throws Exception {

        viewBean.setValidationDecisionAuthorisee(REPmtMensuel.isValidationDecisionAuthorise(viewBean.getSession()));

        return viewBean;
    }
}
