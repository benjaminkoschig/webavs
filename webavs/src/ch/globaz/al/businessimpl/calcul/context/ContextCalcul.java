package ch.globaz.al.businessimpl.calcul.context;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * Contexte pour l'exécution du calcul
 * 
 * @author jts
 * 
 */
public class ContextCalcul {

    /**
     * Initialise et retourne une instance de <code>ContextCalcul</code>
     * 
     * @param dossier
     *            Dossier pour lequel est exécuté le calcul
     * @param dateCalcul
     *            Date pour laquelle le calcul est exécuté
     * @return instance de ContextCalcul
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static ContextCalcul getContextCalcul(DossierComplexModelRoot dossier, String dateCalcul)
            throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("ContextCalcul#getContextCalcul : dossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("ContextCalcul#getContextCalcul : " + dateCalcul + "is not a valid date");
        }

        ContextCalcul context = new ContextCalcul();
        context.dossier = dossier;
        context.dateCalcul = dateCalcul;
        return context;
    }

    /**
     * Initialise et retourne une instance de <code>ContextCalcul</code>
     * 
     * @param dossier
     *            Dossier pour lequel est exécuté le calcul
     * @param dateCalcul
     *            Date pour laquelle le calcul est exécuté
     * @param assuranceInfo
     *            Information concernant l'affilié
     * @return instance de ContextCalcul
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static ContextCalcul getContextCalcul(DossierComplexModelRoot dossier, String dateCalcul,
            AssuranceInfo assuranceInfo) throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("ContextCalcul#getContextCalcul : dossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("ContextCalcul#getContextCalcul : " + dateCalcul + "is not a valid date");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("ContextCalcul#getContextCalcul : assuranceInfo is null");
        }

        ContextCalcul context = ContextCalcul.getContextCalcul(dossier, dateCalcul);
        context.assuranceInfo = assuranceInfo;
        return context;
    }

    /**
     * Informations sur l'affilié
     */
    AssuranceInfo assuranceInfo = null;
    /**
     * Date pour laquelle le calcul est effectué
     */
    String dateCalcul = null;
    /**
     * Modèle complexe pour lequel le calcul est effectué
     */
    DossierComplexModelRoot dossier = null;
    /**
     * Tarif en vigueur dans le canton de l'affilié
     */
    String tarifCantonAssurance = null;

    /**
     * Constructeur privé, utiliser <code>getContextCalcul</code> pour obtenir une instance
     * 
     * @see ContextCalcul#getContextCalcul(DossierComplexModelRoot, String)
     * @see ContextCalcul#getContextCalcul(DossierComplexModelRoot, String, AssuranceInfo)
     */
    private ContextCalcul() {
        // utiliser getContextCalcul
    }

    /**
     * Retourne (charge si nécessaire), les informations concernant l'assurance (affilié)
     * 
     * @return Les informations de l'affilié
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée lorsque l'appel au service n'a pas pu se faire
     */
    public AssuranceInfo getAssuranceInfo() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        if (assuranceInfo == null) {
            // récupération des informations de l'affilié
            assuranceInfo = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(
                    dossier.getDossierModel(), dateCalcul);
        }

        return assuranceInfo;
    }

    /**
     * @return the dateCalcul
     */
    public String getDateCalcul() {
        return dateCalcul;
    }

    /**
     * @return the dossier
     */
    public DossierComplexModelRoot getDossier() {
        return dossier;
    }

    /**
     * Vérifie si un tarif a été forcé au niveau du dossier et le retourne si c'est le cas. Sinon le tarif appliqué dans
     * le canton de l'employeur est retourné.
     * 
     * @return le tarif à utiliser dans le calcul des droits
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTarif() throws JadePersistenceException, JadeApplicationException {

        if (dossier == null) {
            throw new ALCalculException("ContextCalcul#getTarif : Dossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("ContextCalcul#getTarif : " + dateCalcul + " is not a valid date");
        }

        if (JadeNumericUtil.isNumericPositif(dossier.getDossierModel().getTarifForce())) {
            return dossier.getDossierModel().getTarifForce();
        } else {

            String modeCalcul = (ParamServiceLocator.getParameterModelService().getParameterByName(
                    ALConstParametres.APPNAME, ALConstParametres.CAT_TARIF_CAISSE, dateCalcul))
                    .getValeurAlphaParametre();

            if (ALConstParametres.MODE_CALCUL_CAISSEAF.equals(modeCalcul)) {
                return getTarifCaisse(dateCalcul);
            } else {

                String canton = getTarifCantonAssurance();

                if (JadeStringUtil.isEmpty(canton)) {
                    throw new ALCalculException("ContextCalcul#getTarif : unable to get 'canton employeur'");
                }

                return getTarifCantonAssurance();
            }
        }
    }

    /**
     * Récupère le code système du tarif de la caisse dans la table des paramètres
     * 
     * @param date
     *            Date pour laquelle récupérer le paramètre
     * @return code système du tarif de la caisse
     * @throws JadeApplicationException
     *             Exception levée si le paramètre n'a pas un valeur valide
     * @throws JadePersistenceException
     *             Exception levée si le paramètre n'a pas pu être récupéré
     */
    public String getTarifCaisse(String date) throws JadeApplicationException, JadePersistenceException {
        String catTarifCaisse = (ParamServiceLocator.getParameterModelService().getParameterByName(
                ALConstParametres.APPNAME, ALConstParametres.CAT_TARIF_CAISSE, date)).getValeurAlphaParametre();

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_CATEGORIE, catTarifCaisse)) {
                throw new ALCalculException("CalculModeAbstract#getCategoriesList : " + catTarifCaisse
                        + " is not a valid tarif");
            }
        } catch (Exception e) {
            throw new ALCalculException(
                    "CalculModeAbstract#getCategoriesList : unable to check 'categorie tarif caisse'", e);
        }
        return catTarifCaisse;
    }

    /**
     * Recherche le tarif correspondant à l'assurance AF de l'affilié
     * 
     * @return Le tarif en vigueur dans le canton de l'affilié
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTarifCantonAssurance() throws JadePersistenceException, JadeApplicationException {

        if (tarifCantonAssurance == null) {

            tarifCantonAssurance = ALImplServiceLocator.getCalculService().getTarifForCanton(
                    ALImplServiceLocator.getAffiliationService().convertCantonNaos2CantonAF(
                            getAssuranceInfo().getCanton()));
        }

        return tarifCantonAssurance;
    }
}