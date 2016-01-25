package ch.globaz.al.businessimpl.generation.prestations;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;

/**
 * Classe de génération pour les dossiers unitaires (jour ou heure). Elle est utilisée pendant une génération globale ou
 * pour un affilié afin de générer des prestations à zéro qui seront par la suite remplacée par des prestations réelles
 * à l'aide d'une génération manuelle au niveau des dossiers concernés.
 * 
 * Elle fonctionne sur le même principe qu'une génération standard mais tous les montants des détails de prestations
 * sont forcés à zéro
 * 
 * @author jts
 */
public class GenPrestationUnitaireZero extends GenPrestationAbstract {
    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant les informations nécessaires à la génération
     */
    public GenPrestationUnitaireZero(ContextAffilie context) {
        super(context);
    }

    @Override
    protected void addDetailPrestation(ContextAffilie context, CalculBusinessModel droitCalcule)
            throws JadeApplicationException, JadePersistenceException {

        if (context == null) {
            throw new ALGenerationException("GenPrestationGlobaleUnitaire#addDetailPrestation : context is null");
        }

        DetailPrestationModel detail = initDetailPrestation(context, droitCalcule);

        detail.setMontant("0");
        detail.setMontantCaisse("0");
        detail.setMontantCanton("0");
        detail.setCategorieTarif(null);
        detail.setCategorieTarifCaisse(null);
        detail.setCategorieTarifCanton(null);

        detail.setTarifForce(new Boolean(false));

        context.getContextDossier().addDetailPrestation(detail, droitCalcule.getDroit());
    }
}
