/**
 * 
 */
package ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters;

import globaz.jade.client.util.JadeNumericUtil;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.util.CodeSystemUtil;

/**
 * Convertisseur d'objet {@link Adresse} <--> {@link AdresseTiersDetail}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 23 déc. 2013
 * 
 */
public final class AdresseConverter {
    /**
     * Constructeur vide empêchant l'instantiation de la classe
     */
    private AdresseConverter() {

    }

    /**
     * Conversion d'un {@link Adresse} en un {@link AdresseTiersDetail} pouvant être manipulé en base de données.
     * 
     * @param adresseTiersDetail {@link AdresseTiersDetail} représentant une adresse complete d'un tiers
     * @return {@link AdresseTiersDetail} représentation d0une adresse provenant de la base de données
     */
    public static Adresse convertToDomain(AdresseTiersDetail adresseTiersDetail, String langueIso) {
        String titre = adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_CS_TITRE);
        Adresse adresse = new Adresse();
        adresse.setCsTitre(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_CS_TITRE));
        if (!JadeNumericUtil.isEmptyOrZero(titre)) {
            adresse.setTitre(CodeSystemUtil.getCodeSysteme(titre, Langues.getLangueDepuisCodeIso(langueIso))
                    .getLibelle());
        }
        adresse.setIdLocalite(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE_ID));
        adresse.setDescription1(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1));
        adresse.setDescription2(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2));
        adresse.setDescription3(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D3));
        adresse.setDescription4(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D4));
        adresse.setAttention(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_ATTENTION));
        adresse.setCanton(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_CANTON));
        adresse.setCantonCourt(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_CANTON_COURT));
        adresse.setCantonOFAS(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_CANTON_CODE_OFAS));
        adresse.setCasePostale(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_CASE_POSTALE));
        adresse.setLocalite(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE));
        adresse.setNpa(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA));
        adresse.setPays(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_PAYS));
        adresse.setIsoPays(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_PAYS_ISO));
        adresse.setRue(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE));
        adresse.setRueNumero(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO));
        return adresse;
    }
}
