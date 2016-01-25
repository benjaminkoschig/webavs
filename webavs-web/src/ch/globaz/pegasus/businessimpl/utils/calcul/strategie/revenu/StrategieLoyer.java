package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul;

/**
 * @author SCE Calcul du revenu des loyers Cas des sous-locations, un calcul additionel sera potentiellement n�cessaire,
 *         � voir dans un second temps -->1.12.03, 30.01.2014
 */
public class StrategieLoyer extends StrategieCalculRevenu {

    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        // r�cup�ration des donn�es
        float montantMensuelSousLocation = Float.parseFloat(donnee.getLoyerMontantSousLocations());

        // on traite seulement si le montant de sous location est saisi
        if (montantMensuelSousLocation > 0.0f) {

            // annualisation du montant
            float montantAnnuelSousLocation = montantMensuelSousLocation * StrategieCalcul.NB_MOIS;

            // on g�re l'affichage de la cl�, � partir du moment ou il y a un calcul il faut afficher la cl�
            // si le montant calcul� est plaffonn� � z�ro, on affiche quand meme avec l'aide d'une cl� intermediaire
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_LOYER_SOUS_LOCATION_NET,
                    montantAnnuelSousLocation);

        }

        return resultatExistant;

    }

}
