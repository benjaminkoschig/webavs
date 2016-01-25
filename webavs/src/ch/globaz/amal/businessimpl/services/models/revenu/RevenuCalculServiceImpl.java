package ch.globaz.amal.businessimpl.services.models.revenu;

import java.util.HashMap;
import ch.globaz.amal.business.calcul.CalculsRevenuFormules;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistorique;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.revenu.RevenuCalculService;

public class RevenuCalculServiceImpl implements RevenuCalculService {

    /**
     * Get parameters for revenu sourcier, do calcul of deduction and return the result
     */
    @Override
    public RevenuFullComplex calculDeductionSourcier(HashMap<?, ?> values) throws RevenuException {

        RevenuFullComplex revenuFullComplex = new RevenuFullComplex();
        CalculsRevenuFormules calculsRevenuFormules = new CalculsRevenuFormules();

        try {
            // Gets the parameters
            String anneeTaxation = values.get("anneeTaxation").toString();
            String idContribuable = values.get("idContribuable").toString();
            String etatCivil = values.get("etatCivil").toString();
            String nbEnfants = values.get("nbEnfants").toString();
            String revenuAnnuelEpoux = values.get("revenuAnnuelEpoux").toString();
            String revenuAnnuelEpouse = values.get("revenuAnnuelEpouse").toString();
            String revenuMensuelEpoux = values.get("revenuMensuelEpoux").toString();
            String revenuMensuelEpouse = values.get("revenuMensuelEpouse").toString();
            String nbMois = values.get("nbMois").toString();
            // set the values into the model
            revenuFullComplex.getSimpleRevenu().setAnneeTaxation(anneeTaxation);
            revenuFullComplex.getSimpleRevenu().setIdContribuable(idContribuable);
            revenuFullComplex.getSimpleRevenu().setEtatCivil(etatCivil);
            revenuFullComplex.getSimpleRevenu().setNbEnfants(nbEnfants);
            revenuFullComplex.getSimpleRevenuSourcier().setRevenuEpouxAnnuel(revenuAnnuelEpoux);
            revenuFullComplex.getSimpleRevenuSourcier().setRevenuEpouseAnnuel(revenuAnnuelEpouse);
            revenuFullComplex.getSimpleRevenuSourcier().setRevenuEpouxMensuel(revenuMensuelEpoux);
            revenuFullComplex.getSimpleRevenuSourcier().setRevenuEpouseMensuel(revenuMensuelEpouse);
            revenuFullComplex.getSimpleRevenuSourcier().setNombreMois(nbMois);
            // Do the calcul
            // Set as sourcier
            revenuFullComplex.getSimpleRevenu().setIsSourcier(true);
            revenuFullComplex.getSimpleRevenu().setTypeRevenu(IAMCodeSysteme.CS_TYPE_SOURCIER);
            revenuFullComplex = calculsRevenuFormules.doCalculSourcier(revenuFullComplex);

        } catch (Exception e) {
            throw new RevenuException("Fatal error while searching RevenuFullComplex for calcul !!");
        }

        return revenuFullComplex;
    }

    /**
     * Get parameters for revenu sourcier, do calcul of deduction and return the result
     */
    @Override
    public RevenuFullComplex calculDeductionStandard(HashMap<?, ?> values) throws RevenuException {
        String anneeTaxation = values.get("anneeTaxation").toString();
        String idContribuable = values.get("idContribuable").toString();
        String nbEnfants = values.get("nbEnfants").toString();
        String nbEnfantsSuspens = values.get("nbEnfantsSuspens").toString();
        String revenuImposable = values.get("revenuImposable").toString();
        String revenuTaux = values.get("revenuTaux").toString();
        String persChargeEnf = values.get("persChargeEnf").toString();

        RevenuFullComplex revenuFullComplex = new RevenuFullComplex();
        revenuFullComplex.getSimpleRevenu().setAnneeTaxation(anneeTaxation);
        revenuFullComplex.getSimpleRevenu().setIdContribuable(idContribuable);
        revenuFullComplex.getSimpleRevenu().setNbEnfants(nbEnfants);
        revenuFullComplex.getSimpleRevenu().setNbEnfantSuspens(nbEnfantsSuspens);
        revenuFullComplex.getSimpleRevenuContribuable().setRevenuImposable(revenuImposable);
        revenuFullComplex.getSimpleRevenuContribuable().setRevenuTaux(revenuTaux);
        revenuFullComplex.getSimpleRevenuContribuable().setPersChargeEnf(persChargeEnf);

        CalculsRevenuFormules calculsRevenuFormules = new CalculsRevenuFormules();
        calculsRevenuFormules.calculDeductionsFiscalesEnfantsPleinTempsEtSuspens(revenuFullComplex);

        return revenuFullComplex;
    }

    /**
     * Gets parameters for revenu historique, do the calcul of revenu determinant and return it
     */
    @Override
    public RevenuHistoriqueComplex calculRDet(HashMap<?, ?> values) throws RevenuException {

        RevenuHistoriqueComplex revenuHistoriqueComplex = new RevenuHistoriqueComplex();
        CalculsRevenuFormules calculsRevenuFormules = new CalculsRevenuFormules();

        try {
            String anneeHistorique = values.get("anneeHistorique").toString();
            SimpleRevenuHistorique revenuHistorique = new SimpleRevenuHistorique();
            revenuHistorique.setAnneeHistorique(anneeHistorique);
            // Read the current taxation and affect it to revenuHistorique
            String idTaxationLiee = values.get("taxationID").toString();
            RevenuFullComplex revenuFullComplex = AmalServiceLocator.getRevenuService().readFullComplex(idTaxationLiee);
            // Set the revenuHistorique and revenuFullComplex to revenuHistoriqueComplex
            revenuHistoriqueComplex.setRevenuFullComplex(revenuFullComplex);
            revenuHistoriqueComplex.setSimpleRevenuHistorique(revenuHistorique);
        } catch (Exception e) {
            throw new RevenuException("Fatal error while searching RevenuFullComplex for calcul !!");
        }

        if (revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getTypeRevenu()
                .equals(IAMCodeSysteme.CS_TYPE_CONTRIBUABLE)
                || revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getTypeRevenu()
                        .equals(IAMCodeSysteme.CS_TYPE_SOURCIER)) {
            revenuHistoriqueComplex = calculsRevenuFormules.doCalcul(revenuHistoriqueComplex);
        } else {
            throw new RevenuException("Fatal error : TypeRevenu not found !");
        }

        return revenuHistoriqueComplex;
    }
}
