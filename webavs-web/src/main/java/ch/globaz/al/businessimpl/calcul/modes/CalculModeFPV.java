package ch.globaz.al.businessimpl.calcul.modes;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashSet;
import java.util.Set;

import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALConstAttributsEntite;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;
import ch.globaz.al.business.models.attribut.AttributEntiteSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Mode de calcul pour le calcul de prestations de la FPV.
 * 
 * La catégorie de tarif est déterminée en fonction de la caisse professionnel à laquelle l'affilié est lié. Une fois le
 * code de la caisse déterminé, le code système correspondant est récupéré dans la table des attributs d'entité (
 * {@link ch.globaz.al.business.models.attribut.AttributEntiteModel})
 * 
 * @author jts
 * 
 */
public class CalculModeFPV extends CalculModeAbstract {

    @Override
    protected Set<String> getCategoriesList(DossierComplexModelRoot dossier, DroitModel droitModel,
                                            String dateCalcul) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeFPVVisana#getCategoriesList : dossier is null");
        }

        if (droitModel == null) {
            throw new ALCalculException("CalculModeFPVVisana#getCategoriesList : droitModel is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeFPVVisana#getCategoriesList : " + dateCalcul
                    + " is not a valid date");
        }

        Set<String> set = new HashSet<>();

        if (setCategoriesForceesList(dossier, droitModel, set)) {
            return set;
        } else {

            if (!ALCSDossier.ACTIVITE_INDEPENDANT.equals(dossier.getDossierModel().getActiviteAllocataire())) {
                AttributEntiteSearchModel search = new AttributEntiteSearchModel();
                search.setForCleEntiteAlpha(context.getAssuranceInfo().getCodeCaisseProf());
                search.setForNomAttribut(ALConstAttributsEntite.ATTRIBUT_TARIF_CALC_FORCE);
                search = ALServiceLocator.getAttributEntiteModelService().search(search);

                if (search.getSize() == 1) {
                    set.add(((AttributEntiteModel) search.getSearchResults()[0]).getValeurNum());
                } else if (search.getSize() > 0) {
                    throw new ALCalculException("CalculModeFPV#getTarifFPV : Plusieurs tarif pour la caisse prof. "
                            + context.getAssuranceInfo().getCodeCaisseProf() + " ont été trouvés");
                }
            }

            set.add(context.getTarifCantonAssurance());

            return set;
        }
    }
}
