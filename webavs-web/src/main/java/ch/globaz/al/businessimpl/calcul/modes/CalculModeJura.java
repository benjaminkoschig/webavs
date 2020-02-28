package ch.globaz.al.businessimpl.calcul.modes;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;

/**
 * Mode de calcul sp�cifique au canton de Jura (avant LAFam).
 * 
 * Il fonctionne de la m�me mani�re que le type standard mais traite, en plus, le m�nage jurassien qui est d� d�s que
 * l'allocataire a un enfant
 * 
 * Ce type est valide pour les calculs r�pondant aux conditions suivantes :
 * <ul>
 * <li>date de calcul ant�rieur au 01.01.2009 (non LAFam)</li>
 * <li>tarif jurassien</li>
 * </ul>
 * 
 * @author jts
 */
public class CalculModeJura extends CalculModeAbstract {

    /**
     * Liste permettant de stocker les droits MEN en attendant leur traitement
     */
    private ArrayList<DroitComplexModel> menageTmp = new ArrayList<DroitComplexModel>();

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.calcul.modes.CalculMode#compute(ch.globaz.al
     * .business.models.dossier.DossierComplexModelAbstract, java.lang.String)
     */
    @Override
    public List<CalculBusinessModel> compute(ContextCalcul context) throws JadeApplicationException,
            JadePersistenceException {

        if (context == null) {
            throw new ALCalculException("CalculModeJura#compute : context is null");
        }

        initCalculMode(context);

        processDroits(context.getDossier(), context.getDateCalcul());
        processDroitsMenageJura(context.getDossier(), context.getDateCalcul());
        Collections.sort(droitsCalcules);
        return droitsCalcules;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.calcul.modes.CalculModeAbstract#computeMenage
     * (ch.globaz.al.business.models.dossier.DossierComplexModelRoot,
     * ch.globaz.al.business.models.droit.DroitComplexModel, java.lang.String, java.lang.String)
     */
    @Override
    protected void computeMenage(DossierComplexModelRoot dossier, DroitComplexModel droit, String dateCalcul,
            String typeResident) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeJura#computeMenage : dossier is null");
        }

        if (droit == null) {
            throw new ALCalculException("CalculModeJura#computeMenage : droit is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeJura#computeMenage : " + dateCalcul + " is not a valid date");
        }

        if (JadeStringUtil.isEmpty(typeResident)) {
            throw new ALCalculException("CalculModeJura#computeMenage : typeResident is null or empty");
        }

        // Ajoute le droit MEN dans une liste temporaire. Ils seront trait� en
        // fin de traitement
        menageTmp.add(droit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.calcul.modes.CalculModeAbstract#processDroits4Nbr
     * (ch.globaz.al.business.models.dossier.DossierComplexModelRoot, java.lang.String, java.lang.String,
     * ch.globaz.al.business.models.droit.DroitComplexSearchModel)
     */
    @Override
    protected void processDroitsForNbr(DossierComplexModelRoot dossier, String dateCalcul, String typeResident,
            DroitComplexSearchModel droits) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#processDroits4Nbr : dossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeAbstract#processDroits4Nbr : " + dateCalcul + " is not a valid date");
        }

        if (JadeStringUtil.isEmpty(typeResident)) {
            throw new ALCalculException("CalculModeAbstract#processDroits4Nbr : typeResident is null or empty");
        }

        if (droits == null) {
            throw new ALCalculException("CalculModeAbstract#processDroits4Nbr : droits is null");
        }

        if (potetialNumberCriterion) {
            menageTmp = new ArrayList<DroitComplexModel>();
            super.processDroitsForNbr(dossier, dateCalcul, typeResident, droits);
        }
    }

    /**
     * Ex�cute le calcul des prestations de m�nage contenue dans <code>menageTmp</code> pour le <code>dossier</code>.
     * Les prestations m�nages jurassiennes ne sont due que si au moins un enfant b�n�ficie d'une prestation
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est ex�cut�
     * @param dateCalcul
     *            Date pour laquelle le calcul est ex�cut�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private void processDroitsMenageJura(DossierComplexModelRoot dossier, String dateCalcul)
            throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#processDroitsMenageJura : dossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeAbstract#processDroitsMenageJura : " + dateCalcul
                    + " is not a valid date");
        }

        if ((nombre > 0) && (menageTmp.size() > 0)) {

            String typeResident = getTypeResident(dossier);

            for (int i = 0; i < menageTmp.size(); i++) {
                super.computeMenage(dossier, menageTmp.get(i), dateCalcul, typeResident);
            }
        }
    }
}
