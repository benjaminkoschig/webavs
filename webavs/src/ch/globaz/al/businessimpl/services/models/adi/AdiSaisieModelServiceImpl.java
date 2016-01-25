package ch.globaz.al.businessimpl.services.models.adi;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import ch.globaz.al.business.exceptions.model.adi.ALAdiSaisieModelException;
import ch.globaz.al.business.exceptions.model.adi.ALDecompteAdiModelException;
import ch.globaz.al.business.models.adi.AdiSaisieModel;
import ch.globaz.al.business.models.adi.AdiSaisieSearchModel;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.adi.AdiSaisieModelService;
import ch.globaz.al.businessimpl.checker.model.adi.AdiSaisieModelChecker;

/**
 * Classe d'implémentation des services persistance du modèle adi saisie
 * 
 * @author GMO
 * 
 */
public class AdiSaisieModelServiceImpl implements AdiSaisieModelService {
    @Override
    public AdiSaisieModel create(AdiSaisieModel adiSaisieModel) throws JadeApplicationException,
            JadePersistenceException {
        if (adiSaisieModel == null) {
            throw new ALDecompteAdiModelException(
                    "AdiSaisieModelServiceImpl#create : unable to create adiSaisieModel - the model passed is null");
        }
        AdiSaisieModelChecker.validate(adiSaisieModel);
        // ajoute le Droit dans la persistance et le retourne
        return (AdiSaisieModel) JadePersistenceManager.add(adiSaisieModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.adi.AdiSaisieModelService#delete
     * (ch.globaz.al.business.models.adi.AdiSaisieModel)
     */
    @Override
    public AdiSaisieModel delete(AdiSaisieModel adiSaisieModel) throws JadeApplicationException,
            JadePersistenceException {
        if (adiSaisieModel == null) {
            throw new ALDecompteAdiModelException(
                    "AdiSaisieModelServiceImpl#delete : unable to delete the model - model passed is null");
        }
        if (adiSaisieModel.isNew()) {
            throw new ALDecompteAdiModelException("Unable to delete adiSaisieModel, th id passed is new");
        }

        // suppression
        return (AdiSaisieModel) JadePersistenceManager.delete(adiSaisieModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.adi.AdiSaisieModelService#initModel
     * (ch.globaz.al.business.models.adi.AdiSaisieModel, java.util.HashMap,
     * ch.globaz.al.business.models.adi.AdiSaisieSearchModel)
     */
    @Override
    public AdiSaisieModel initModel(AdiSaisieModel adiSaisieModel, HashMap listeASaisir)
            throws JadeApplicationException, JadePersistenceException {

        if (adiSaisieModel == null) {
            throw new ALAdiSaisieModelException(
                    "AdiSaisieModelServiceImpl#initModel: unable to init model - model passed is null");
        }

        if (listeASaisir == null) {
            throw new ALAdiSaisieModelException(
                    "AdiSaisieModelServiceImpl#initModel: unable to init model - listeASaisir passed is null");
        }
        if (!JadeNumericUtil.isEmptyOrZero(adiSaisieModel.getId())) {
            adiSaisieModel = read(adiSaisieModel.getId());
        } else {
            adiSaisieModel.setMontantSaisi("0");
        }

        List keySort = new ArrayList(listeASaisir.keySet());

        Collections.sort(keySort);

        HashMap initHashMapEnfant = new HashMap();
        String idEnfant = null;
        // Parmi toutes les listes à saisir des enfants, on récupère la première
        // où il reste qqch à saisir
        for (int i = 0; i < keySort.size(); i++) {

            boolean resteASaisir = ((HashMap) listeASaisir.get(keySort.get(i))).containsValue(new Integer(0));
            if (resteASaisir) {
                initHashMapEnfant = (HashMap) listeASaisir.get(keySort.get(i));
                idEnfant = keySort.get(i).toString();
                break;
            }

        }

        keySort = new ArrayList(initHashMapEnfant.keySet());
        Collections.sort(keySort);

        String debutPeriode = "";
        String finPeriode = "";

        for (int i = 0; i < keySort.size(); i++) {
            // si le mois courant de la liste enfant vaut 0 et qu'on a pas
            // encore défini le nouveau début
            if ((((Integer) initHashMapEnfant.get(keySort.get(i))).compareTo(new Integer(0)) == 0)
                    && (JadeStringUtil.isEmpty(debutPeriode))) {
                debutPeriode = keySort.get(i).toString();
            }
            // si le mois courant de la liste enfant vaut autre que 0 et qu'on a
            // défini
            // le début, alors le précédent est la fin
            if (((((Integer) initHashMapEnfant.get(keySort.get(i))).compareTo(new Integer(0)) != 0))
                    && (!JadeStringUtil.isEmpty(debutPeriode))) {

                finPeriode = keySort.get(i - 1).toString();

            }
            // si le mois courant de la liste enfant vaut 0, que c'est le
            // dernier
            // et qu'on a défini
            // le début, alors le courant est la fin
            if (((((Integer) initHashMapEnfant.get(keySort.get(i))).compareTo(new Integer(0)) == 0))
                    && (i + 1 == keySort.size()) && (!JadeStringUtil.isEmpty(debutPeriode))) {

                finPeriode = keySort.get(i).toString();

            }

        }

        // Si on n'a plus de période à saisir, on ne met rien dans les périodes
        // du modèles
        if (!JadeStringUtil.isEmpty(debutPeriode) && !JadeStringUtil.isEmpty(finPeriode)) {
            // on récupère le décompte lié pour avoir l'année des périodes
            DecompteAdiModel decompteLie = ALServiceLocator.getDecompteAdiModelService().read(
                    adiSaisieModel.getIdDecompteAdi());
            // traitement des périodes pour avoir bon format
            if (Integer.parseInt(debutPeriode) < 10) {
                debutPeriode = "0".concat(debutPeriode);
            }
            if (Integer.parseInt(finPeriode) < 10) {
                finPeriode = "0".concat(finPeriode);
            }

            adiSaisieModel.setPeriodeDe(debutPeriode.concat("." + decompteLie.getAnneeDecompte()));
            adiSaisieModel.setPeriodeA(finPeriode.concat("." + decompteLie.getAnneeDecompte()));
        } else {
            adiSaisieModel.setPeriodeDe("");
            adiSaisieModel.setPeriodeA("");
        }
        adiSaisieModel.setIdEnfant(idEnfant);

        return adiSaisieModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.adi.AdiSaisieModelService#read( java.lang.String)
     */
    @Override
    public AdiSaisieModel read(String idSaisieModel) throws JadeApplicationException, JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idSaisieModel)) {
            throw new ALDecompteAdiModelException("unable to read adiSaisieModel-the id passed is empty");
        }
        AdiSaisieModel adiSaisieModel = new AdiSaisieModel();
        adiSaisieModel.setId(idSaisieModel);
        return (AdiSaisieModel) JadePersistenceManager.read(adiSaisieModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.adi.AdiSaisieModelService#search
     * (ch.globaz.al.business.models.adi.AdiSaisieSearchModel)
     */
    @Override
    public AdiSaisieSearchModel search(AdiSaisieSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new ALDecompteAdiModelException(
                    "AdiSaisieModelServiceImpl#search:unable to search the model - the searchModel is null");
        }
        return (AdiSaisieSearchModel) JadePersistenceManager.search(searchModel);

    }

}
