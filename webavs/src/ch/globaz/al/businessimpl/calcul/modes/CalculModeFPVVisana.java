package ch.globaz.al.businessimpl.calcul.modes;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashSet;
import ch.globaz.al.business.constantes.ALCSCantons;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.droit.DroitModel;

/**
 * Mode de calcul pour le calcul de prestations de la FPV (affiliés Visana).
 * 
 * @author jts
 * 
 */
public class CalculModeFPVVisana extends CalculModeAbstract {

    @Override
    protected HashSet<String> getCategoriesList(DossierComplexModelRoot dossier, DroitModel droitModel,
            String dateCalcul) throws JadeApplicationException, JadePersistenceException {

        return super.getCategoriesList(dossier, droitModel, dateCalcul);
        // TODO : actuellement la FPV force le tarif des dossiers de la VISANA
        // if (dossier == null) {
        // throw new ALCalculException("CalculModeFPVVisana#getCategoriesList : dossier is null");
        // }
        //
        // if (droitModel == null) {
        // throw new ALCalculException("CalculModeFPVVisana#getCategoriesList : droitModel is null");
        // }
        //
        // if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
        // throw new ALCalculException("CalculModeFPVVisana#getCategoriesList : " + dateCalcul
        // + " is not a valid date");
        // }
        //
        // HashSet<String> set = new HashSet<String>();
        //
        // if (this.setCategoriesForceesList(dossier, droitModel, set)) {
        // return set;
        // } else {
        // set.add(this.getTarifVisana(ALImplServiceLocator.getAffiliationService().convertCantonNaos2CantonAF(
        // this.context.getAssuranceInfo().getCanton())));
        //
        // return set;
        // }
    }

    /**
     * Recherche le tarif Visana correspondant au canton passé en parmètre
     * 
     * @param canton
     *            Canton pour lequel récupérer le paramètre (
     *            {@link ch.globaz.al.business.constantes.ALCSCantons#GROUP_CANTONS}
     * @return code système de la catégorie de tarif Visana (
     *         {@link ch.globaz.al.business.constantes.ALCSTarif#GROUP_CATEGORIE}
     * 
     * @throws JadeApplicationException
     *             Exception levée si le canton passé en paramètre ne correspond à aucun tarif
     */
    @SuppressWarnings("unused")
    private String getTarifVisana(String canton) throws JadeApplicationException {

        if (ALCSCantons.AG.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_AG;
        } else if (ALCSCantons.AI.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_AI;
        } else if (ALCSCantons.AR.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_AR;
        } else if (ALCSCantons.BE.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_BE;
        } else if (ALCSCantons.BL.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_BL;
        } else if (ALCSCantons.BS.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_BS;
        } else if (ALCSCantons.FR.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_FR;
        } else if (ALCSCantons.GE.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_GE;
        } else if (ALCSCantons.GL.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_GL;
        } else if (ALCSCantons.GR.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_GR;
        } else if (ALCSCantons.JU.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_JU;
        } else if (ALCSCantons.LU.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_LU;
        } else if (ALCSCantons.NE.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_NE;
        } else if (ALCSCantons.NW.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_NW;
        } else if (ALCSCantons.OW.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_OW;
        } else if (ALCSCantons.SG.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_SG;
        } else if (ALCSCantons.SH.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_SH;
        } else if (ALCSCantons.SO.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_SO;
        } else if (ALCSCantons.SZ.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_SZ;
        } else if (ALCSCantons.TG.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_TG;
        } else if (ALCSCantons.TI.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_TI;
        } else if (ALCSCantons.UR.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_UR;
        } else if (ALCSCantons.VD.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_VD;
        } else if (ALCSCantons.VS.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_VS;
        } else if (ALCSCantons.ZG.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_ZG;
        } else if (ALCSCantons.ZH.equals(canton)) {
            return ALCSTarif.CATEGORIE_FPV_VISANA_ZH;
        } else {
            throw new ALCalculException(
                    "CalculModeFPVVisana#getTarifVisana : aucun tarif n'a pu être déterminé pour le canton : " + canton);
        }
    }
}
