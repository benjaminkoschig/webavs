package ch.globaz.al.businessimpl.services.generation.factures;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.text.NumberFormat;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstFactures;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.generation.factures.NumeroFactureService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * Implémentation du service permettant de générer, récupérer et contrôler des numéros de facture
 * 
 * @author jts
 * 
 */
public class NumeroFactureServiceImpl extends ALAbstractBusinessServiceImpl implements NumeroFactureService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.generation.factures.NumeroFactureService #checkNumFacture(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean checkNumFacture(String numFacture, String periode, String periodicite, String bonification)
            throws JadeApplicationException {

        /*
         * vérification des paramètres
         */
        if (JadeStringUtil.isEmpty(numFacture)) {
            throw new ALGenerationException("NumeroFactureServiceImpl#checkNumFacture : numFacture is null or empty");
        }

        if (JadeDateUtil.isGlobazDateMonth(periode)) {
            throw new ALGenerationException("NumeroFactureServiceImpl#checkNumFacture : '" + periode
                    + "' is not a vlid period");
        }

        if (!ALCSAffilie.PERIODICITE_ANN.equals(periodicite) && !ALCSAffilie.PERIODICITE_MEN.equals(periodicite)
                && !ALCSAffilie.PERIODICITE_TRI.equals(periodicite)) {
            throw new ALGenerationException(
                    "NumeroFactureServiceImpl#checkNumFacture : periodicite has not a valid value");
        }

        if (!ALCSPrestation.BONI_DIRECT.equals(bonification) && !ALCSPrestation.BONI_INDIRECT.equals(bonification)
                && !ALCSPrestation.BONI_RESTITUTION.equals(bonification)) {
            throw new ALGenerationException(
                    "NumeroFactureServiceImpl#checkNumFacture : bonification has not a valid value");
        }

        if (numFacture.length() != 9) {
            return false;
        }

        String anneePeriode = periode.substring(3, 7);
        String moisPeriode = periode.substring(0, 2);
        String anneeFacture = numFacture.substring(0, 4);
        String moisFacture = numFacture.substring(4, 6);
        String num = numFacture.substring(6);

        /*
         * Vérification de l'année (4 premiers chiffres)
         */
        if (!anneePeriode.equals(anneeFacture)) {
            return false;
        }

        /*
         * Vérification du mois (chiffres 5 et 6)
         */
        // périodicité mensuelle ou paiement direct
        if (ALCSAffilie.PERIODICITE_TRI.equals(periodicite)) {

            switch (Integer.parseInt(moisPeriode)) {
                case 1:
                case 2:
                case 3:
                    if (!ALConstFactures.NUM_VAL_TRIMESTRE1.equals(moisFacture) && !moisPeriode.equals(moisFacture)) {
                        return false;
                    }
                    break;
                case 4:
                case 5:
                case 6:
                    if (!ALConstFactures.NUM_VAL_TRIMESTRE2.equals(moisFacture) && !moisPeriode.equals(moisFacture)) {
                        return false;
                    }
                    break;
                case 7:
                case 8:
                case 9:
                    if (!ALConstFactures.NUM_VAL_TRIMESTRE3.equals(moisFacture) && !moisPeriode.equals(moisFacture)) {
                        return false;
                    }
                    break;
                case 10:
                case 11:
                case 12:
                    if (!ALConstFactures.NUM_VAL_TRIMESTRE4.equals(moisFacture) && !moisPeriode.equals(moisFacture)) {
                        return false;
                    }
                    break;
                default:
                    return false;
            }

            // périodicité annuelle
        } else if (ALCSAffilie.PERIODICITE_ANN.equals(periodicite)) {
            if (ALConstFactures.NUM_VAL_ANNUEL.equals(moisFacture) && (Integer.parseInt(moisPeriode) != 12)) {
                return false;
            }
        } else if (ALCSAffilie.PERIODICITE_MEN.equals(periodicite) || ALCSPrestation.BONI_DIRECT.equals(bonification)
                || ALCSPrestation.BONI_RESTITUTION.equals(bonification)) {

            if (!moisPeriode.equals(moisFacture)) {
                return false;
            }

            // périodicité trimestrielle
        } else {
            return false;
        }

        /*
         * Vérification du numéro
         */
        if (!JadeNumericUtil.isInteger(num) && (Integer.parseInt(num) >= 0) && (Integer.parseInt(num) < 1000)) {
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.generation.factures.NumeroFactureService #getLastNumFacture(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, boolean)
     */
    @Override
    public String getLastNumFacture(String periode, String periodicite, String bonification, String numAffilie,
            boolean onlyOpen) throws JadeApplicationException, JadePersistenceException {

        /*
         * Vérification des paramètres
         */
        if (JadeDateUtil.isGlobazDateMonth(periode)) {
            throw new ALGenerationException("NumeroFactureServiceImpl#getLastNumFacture : '" + periode
                    + "' is not a vlid period");
        }

        if (!ALCSAffilie.PERIODICITE_ANN.equals(periodicite) && !ALCSAffilie.PERIODICITE_MEN.equals(periodicite)
                && !ALCSAffilie.PERIODICITE_TRI.equals(periodicite)) {
            throw new ALGenerationException(
                    "NumeroFactureServiceImpl#getLastNumFacture : periodicite has not a valid value");
        }

        if (!ALCSPrestation.BONI_DIRECT.equals(bonification) && !ALCSPrestation.BONI_INDIRECT.equals(bonification)
                && !ALCSPrestation.BONI_RESTITUTION.equals(bonification)) {
            throw new ALGenerationException(
                    "NumeroFactureServiceImpl#getLastNumFacture : bonification has not a valid value");
        }

        if (JadeStringUtil.isEmpty(numAffilie)) {
            throw new ALGenerationException("NumeroFactureServiceImpl#getLastNumFacture : numAffilie is null or rempty");
        }

        // recherche d'un numéro de facture existant
        RecapitulatifEntrepriseSearchModel search = new RecapitulatifEntrepriseSearchModel();
        if (onlyOpen) {
            search.setForEtatRecap(ALCSPrestation.ETAT_SA);
        }
        search.setForPeriodeA(periode);
        search.setForNumeroAffilie(numAffilie);
        search.setForBonification(bonification);
        search.setOrderKey("lastNumFacture");
        search = ALServiceLocator.getRecapitulatifEntrepriseModelService().search(search);

        // parcours des résultat et retour du premier numéro valide
        for (int i = 0; i < search.getSearchResults().length; i++) {
            // FIXME: bz8825
            RecapitulatifEntrepriseModel recap = (RecapitulatifEntrepriseModel) search.getSearchResults()[0];

            if (checkNumFacture(recap.getNumeroFacture(), periode, periodicite, bonification)) {
                return recap.getNumeroFacture();
            }
        }

        // si aucune facture n'est ouverte
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.generation.factures.NumeroFactureService #getNumFacture(java.lang.String,
     * ch.globaz.al.business.models.dossier.DossierModel)
     */
    @Override
    public String getNumFacture(String periode, DossierModel dossier) throws JadeApplicationException,
            JadePersistenceException {

        /*
         * Vérification des paramètres
         */
        if (!JadeDateUtil.isGlobazDateMonthYear(periode)) {
            throw new ALGenerationException("GenerationServiceImpl#getNumFacture : " + periode
                    + " is not a valid period");
        }

        if (dossier == null) {
            throw new ALGenerationException("GenerationServiceImpl#getNumFacture : dossier is null");
        }

        AssuranceInfo info = ALServiceLocator.getAffiliationBusinessService()
                .getAssuranceInfo(dossier, "01." + periode);

        return this.getNumFacture(periode, info.getPeriodicitieAffiliation(), (JadeNumericUtil.isEmptyOrZero(dossier
                .getIdTiersBeneficiaire()) ? ALCSPrestation.BONI_INDIRECT : ALCSPrestation.BONI_DIRECT), dossier
                .getNumeroAffilie());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.generation.factures.NumeroFactureService #getNumFacture(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String getNumFacture(String periode, String periodicite, String bonification, String numAffilie)
            throws JadeApplicationException, JadePersistenceException {

        /*
         * Vérification des paramètres
         */
        if (!JadeDateUtil.isGlobazDateMonthYear(periode)) {
            throw new ALGenerationException("GenerationServiceImpl#getNumFacture : " + periode
                    + " is not a valid period");
        }

        if (!ALCSAffilie.PERIODICITE_ANN.equals(periodicite) && !ALCSAffilie.PERIODICITE_MEN.equals(periodicite)
                && !ALCSAffilie.PERIODICITE_TRI.equals(periodicite)) {
            throw new ALGenerationException("GenerationServiceImpl#getNumFacture : periodicite has not a valid value");
        }

        // recherche du dernier numéro d'une récap ouverte
        String lastNum = getLastNumFacture(periode, periodicite, bonification, numAffilie, true);

        // si on en a trouvé un on le retourne
        if (!JadeStringUtil.isEmpty(lastNum)) {
            return lastNum;
            // sinon génération d'un nouveau numéro
        } else {

            /*
             * année
             */
            StringBuffer nextNum = new StringBuffer(periode.substring(3, 7));

            /*
             * Période
             */
            String mois = periode.substring(0, 2);

            if (ALCSAffilie.PERIODICITE_MEN.equals(periodicite) || ALCSPrestation.BONI_DIRECT.equals(bonification)
                    || ALCSPrestation.BONI_RESTITUTION.equals(bonification)) {
                nextNum.append(mois);
            } else if (ALCSAffilie.PERIODICITE_TRI.equals(periodicite)) {
                switch (Integer.parseInt(mois)) {
                    case 1:
                    case 2:
                    case 3:
                        nextNum.append("41");
                        break;
                    case 4:
                    case 5:
                    case 6:
                        nextNum.append("42");
                        break;
                    case 7:
                    case 8:
                    case 9:
                        nextNum.append("43");
                        break;
                    case 10:
                    case 11:
                    case 12:
                        nextNum.append("44");
                        break;
                }
            } else {
                // cas annuel
                nextNum.append("40");
            }

            /*
             * Numéro
             */
            lastNum = getLastNumFacture(periode, periodicite, bonification, numAffilie, false);

            if (JadeStringUtil.isEmpty(lastNum)) {
                nextNum.append("000");
            } else {
                int num = Integer.parseInt(lastNum.substring(6));

                ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                        ALConstParametres.APPNAME, ALConstParametres.INCREMENT_FACTURE, "01." + periode);

                NumberFormat nf = NumberFormat.getInstance();
                nf.setMinimumIntegerDigits(3);
                nf.setGroupingUsed(false);
                nextNum.append(nf.format(num + Integer.parseInt(param.getValeurAlphaParametre())));
            }

            return nextNum.toString();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.generation.factures.NumeroFactureService #isAvailable(java.lang.String)
     */
    @Override
    public boolean isAvailable(String numFacture, String numAffilie, String bonif) throws JadeApplicationException,
            JadePersistenceException {

        /*
         * vérification des paramètres
         */
        if (JadeStringUtil.isEmpty(numFacture)) {
            throw new ALGenerationException("NumeroFactureServiceImpl#checkNumFacture : numFacture is null or empty");
        } else if (numFacture.length() != 9) {
            throw new ALGenerationException(
                    "NumeroFactureServiceImpl#checkNumFacture : the number must have a length of nine characters");
        }

        RecapitulatifEntrepriseSearchModel search = new RecapitulatifEntrepriseSearchModel();
        search.setForNumeroFacture(numFacture);
        search.setForNumeroAffilie(numAffilie);
        search.setForBonification(bonif);
        search.setForEtatRecap(ALCSPrestation.ETAT_SA);
        search.setWhereKey("numFactureNotAvailable");
        search.setOrderKey("lastNumFacture");
        search = ALServiceLocator.getRecapitulatifEntrepriseModelService().search(search);

        return search.getSize() == 0;
    }
}
