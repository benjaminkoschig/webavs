/*
 * Créé le 31 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import ch.globaz.common.util.Dates;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.properties.APProperties;
import globaz.globall.db.BStatement;
import globaz.prestation.api.PRTypeDemande;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class APEnfantPat extends APSituationFamilialePat {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String NUMERO_AVS_OBLIGATOIRE = "NUMERO_AVS_OBLIGATOIRE";
    private static final String ERREUR_DATE_NAISSANCE_INCORRECTE = "DATE_NAISSANCE_INCORRECTE";
    private static final String AGE_LEGALE_DEPASSE = "AGE_LEGALE_DEPASSE";



    @Setter
    @Getter
    private PRTypeDemande typeDemande = PRTypeDemande.PATERNITE;
    @Setter
    @Getter
    private String dateDebut;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APEnfantPat.
     */
    protected APEnfantPat() {
        super(IAPDroitMaternite.CS_TYPE_ENFANT);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (_propertyMandatory(statement.getTransaction(), dateNaissance,
                getSession().getLabel(ERREUR_DATE_NAISSANCE_INCORRECTE))) {
            _checkDate(statement.getTransaction(), dateNaissance,
                    getSession().getLabel(ERREUR_DATE_NAISSANCE_INCORRECTE));
        }

        if(typeDemande.isProcheAidant()) {
            _propertyMandatory(statement.getTransaction(), this.noAVS, getSession().getLabel(NUMERO_AVS_OBLIGATOIRE));
            LocalDate dateLegale = Dates.toDate(dateNaissance).plusYears(Integer.parseInt(APProperties.PROCHE_AIDANT_AGE_LEGAL.getValue()));
            if(!hasErrors() && Dates.toDate(dateDebut).isAfter(dateLegale)) {
                _addError(statement.getTransaction(), getSession().getLabel(AGE_LEGALE_DEPASSE));
                this.setId(null);
            }
        }
    }
}
