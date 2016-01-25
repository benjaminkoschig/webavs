/**
 * 
 */
package ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters;

import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.vulpecula.external.models.pyxis.Pays;

/**
 * @author Arnaud Geiser (AGE) | Créé le 23 déc. 2013
 * 
 */
public class PaysConverter {
    public static Pays convertToDomain(final PaysSimpleModel paysSimpleModel) {
        Pays pays = new Pays();
        pays.setId(paysSimpleModel.getId());
        pays.setIdMonnaie(paysSimpleModel.getIdPays());
        pays.setCodeIso(paysSimpleModel.getCodeIso());
        pays.setCodeCentrale(paysSimpleModel.getCodeCentrale());
        pays.setIndicatif(paysSimpleModel.getIndicatifTel());
        pays.setCodeFormatNpa(paysSimpleModel.getCodeFormatNpa());
        pays.setCodeFormatTel(paysSimpleModel.getCodeFormatTel());
        pays.setLibelleFr(paysSimpleModel.getLibelleFr());
        pays.setActif(true);
        return pays;
    }
}
