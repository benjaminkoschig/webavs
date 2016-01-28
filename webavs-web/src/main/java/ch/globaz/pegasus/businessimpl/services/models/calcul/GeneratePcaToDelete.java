package ch.globaz.pegasus.businessimpl.services.models.calcul;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.common.domaine.GroupePeriodes;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplace;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplaceSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.utils.periode.GroupePeriodesResolver;
import ch.globaz.utils.periode.GroupePeriodesResolver.EachPeriode;

/**
 * @formatter:off 
 * Permet de générer les PCA avec le flag isDeleted s'il y en besoin
 * La génération de ces pca permet de masquer une ancienne pca qui ne match plus avec la nouvelle.
 * Pour générer ces PCA isDeleted on se base sur les anciennes PCAs et on se borne sur les dates des nouvelles PCA Créer.
 *  
 * Exemple:
 * Version                            [PCA idEntity 13 isDeleted]    
   (2)     [ PCA idEntity 12                 ][PCA idEntity 14
   (1)     [PCA  idEntity 12][PCA idEntity 13][PCA isEntity 14
 **/
public class GeneratePcaToDelete {
	private CalculPcaReplaceSearch anciennePca;
	private String dateDebutMax;
	private String dateDebutMin;
	private String dateFinMax;
	private String idVersionDroit;
	private Map<String, SimplePCAccordee> mapAllNewPca;

	/**¨
	 * @param newDroit: nouvelle version du droit
	 * @param pcaNew: Liste de toutes les PCAs créer pour la version du doit.
	 * @param anciennePca: Liste des PCAs qui vont être remplacées 
	 */
	public GeneratePcaToDelete(List<SimplePCAccordee> pcaNew, CalculPcaReplaceSearch anciennePca) {
		super();
		this.anciennePca = anciennePca;
		if(pcaNew.size()>0){
			idVersionDroit =pcaNew.get(0).getIdVersionDroit();
		}
		mapAllNewPca = generateMap(pcaNew);
		resolveDateMinMax(pcaNew);
	}

	/**
	 * Dans la plus part des cas la liste sera vide
	 * @return la liste des PCAs avec le flag isDeleted
	 */
	public List<CalculPcaReplace> generate() {
		List<CalculPcaReplace> list = new ArrayList<CalculPcaReplace>();
		for (JadeAbstractModel model : anciennePca.getSearchResults()) {
			CalculPcaReplace pca = (CalculPcaReplace) model;
			if (isInPlageNewPca(pca)) {
				if (!mapAllNewPca.containsKey(pca.getSimplePCAccordee().getIdEntity())) {
					try {
						CalculPcaReplace pcaDeleted =  (CalculPcaReplace) JadePersistenceUtil.clone(pca);
						pcaDeleted.getSimplePCAccordee().setIsSupprime(true);
						pcaDeleted.getSimplePlanDeCalcul().setResultatCalcul(new String().getBytes());
						pcaDeleted.getSimplePCAccordee().setIdVersionDroit(idVersionDroit);
						list.add(pcaDeleted);
					} catch (JadeCloneModelException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return list;
	}

	private Map<String, SimplePCAccordee> generateMap(List<SimplePCAccordee> pcaNew) {
		Map<String, SimplePCAccordee> map = new HashMap<String, SimplePCAccordee>();
		for (SimplePCAccordee pca : pcaNew) {
			map.put(pca.getIdEntity(), pca);
		}
		return map;
	}

	private boolean isInPlageNewPca(CalculPcaReplace pca) {
		String dateDebut = pca.getSimplePCAccordee().getDateDebut();
		String dateFin = pca.getSimplePCAccordee().getDateFin();
		if(JadeStringUtil.isEmpty(dateFin)){
			dateFin = null;
		}

		return ((dateDebut.equals(dateDebutMin) || JadeDateUtil.isDateMonthYearAfter(dateDebut, dateDebutMin)) 
				&& ((dateFin == dateFinMax) || (((dateFin != null) && dateFin.equals(dateFinMax)) || JadeDateUtil.isDateMonthYearBefore(dateFin, dateFinMax) || (dateFinMax ==null))))
				|| isOnTowPeriode(dateDebut);
	}

	private boolean isOnTowPeriode(String dateDebut) {
		return JadeDateUtil.isDateMonthYearAfter(dateDebut, dateDebutMax)
				&& JadeDateUtil.isDateMonthYearBefore(dateDebut, dateFinMax);
	}

	private void resolveDateMinMax(List<SimplePCAccordee> pcaNew) {
		GroupePeriodes periodes = GroupePeriodesResolver.genearateListPeriode(pcaNew, new EachPeriode<SimplePCAccordee>() {
			@Override
			public String[] dateDebutFin(SimplePCAccordee t) {
				return new String[] { t.getDateDebut(), t.getDateFin() };
			}
		});

		dateDebutMin = periodes.getDateDebutMin();
		dateFinMax = periodes.hasDateFinNullValue() ? null : periodes.getDateFinMax();
		dateDebutMax = periodes.getFinMax().getDateDebut();
	}

	@Override
	public String toString() {
		return "GeneratePcaToDeleteNew [anciennePca=" + anciennePca + ", mapAllNewPca=" + mapAllNewPca + "]";
	}
}
