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
 * Contexte pour l'ex�cution du calcul
 * 
 * @author jts
 * 
 */
public class ContextCalcul {

    /**
     * Initialise et retourne une instance de <code>ContextCalcul</code>
     * 
     * @param dossier
     *            Dossier pour lequel est ex�cut� le calcul
     * @param dateCalcul
     *            Date pour laquelle le calcul est ex�cut�
     * @return instance de ContextCalcul
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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
     *            Dossier pour lequel est ex�cut� le calcul
     * @param dateCalcul
     *            Date pour laquelle le calcul est ex�cut�
     * @param assuranceInfo
     *            Information concernant l'affili�
     * @return instance de ContextCalcul
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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
     * Informations sur l'affili�
     */
    AssuranceInfo assuranceInfo = null;
    /**
     * Date pour laquelle le calcul est effectu�
     */
    String dateCalcul = null;
    /**
     * Mod�le complexe pour lequel le calcul est effectu�
     */
    DossierComplexModelRoot dossier = null;
    /**
     * Tarif en vigueur dans le canton de l'affili�
     */
    String tarifCantonAssurance = null;

    /**
     * Constructeur priv�, utiliser <code>getContextCalcul</code> pour obtenir une instance
     * 
     * @see ContextCalcul#getContextCalcul(DossierComplexModelRoot, String)
     * @see ContextCalcul#getContextCalcul(DossierComplexModelRoot, String, AssuranceInfo)
     */
    private ContextCalcul() {
        // utiliser getContextCalcul
    }

    /**
     * Retourne (charge si n�cessaire), les informations concernant l'assurance (affili�)
     * 
     * @return Les informations de l'affili�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception lev�e lorsque l'appel au service n'a pas pu se faire
     */
    public AssuranceInfo getAssuranceInfo() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        if (assuranceInfo == null) {
            // r�cup�ration des informations de l'affili�
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
     * V�rifie si un tarif a �t� forc� au niveau du dossier et le retourne si c'est le cas. Sinon le tarif appliqu� dans
     * le canton de l'employeur est retourn�.
     * 
     * @return le tarif � utiliser dans le calcul des droits
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * R�cup�re le code syst�me du tarif de la caisse dans la table des param�tres
     * 
     * @param date
     *            Date pour laquelle r�cup�rer le param�tre
     * @return code syst�me du tarif de la caisse
     * @throws JadeApplicationException
     *             Exception lev�e si le param�tre n'a pas un valeur valide
     * @throws JadePersistenceException
     *             Exception lev�e si le param�tre n'a pas pu �tre r�cup�r�
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
     * Recherche le tarif correspondant � l'assurance AF de l'affili�
     * 
     * @return Le tarif en vigueur dans le canton de l'affili�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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