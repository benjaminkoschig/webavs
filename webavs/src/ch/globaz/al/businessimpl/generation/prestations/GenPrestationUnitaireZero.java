package ch.globaz.al.businessimpl.generation.prestations;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;

/**
 * Classe de g�n�ration pour les dossiers unitaires (jour ou heure). Elle est utilis�e pendant une g�n�ration globale ou
 * pour un affili� afin de g�n�rer des prestations � z�ro qui seront par la suite remplac�e par des prestations r�elles
 * � l'aide d'une g�n�ration manuelle au niveau des dossiers concern�s.
 * 
 * Elle fonctionne sur le m�me principe qu'une g�n�ration standard mais tous les montants des d�tails de prestations
 * sont forc�s � z�ro
 * 
 * @author jts
 */
public class GenPrestationUnitaireZero extends GenPrestationAbstract {
    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant les informations n�cessaires � la g�n�ration
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
