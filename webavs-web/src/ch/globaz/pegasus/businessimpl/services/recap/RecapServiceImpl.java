package ch.globaz.pegasus.businessimpl.services.recap;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.pegasus.business.constantes.EPCPMutationPassage;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.exceptions.models.MutationException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.mutation.RecapInfoDomaine;
import ch.globaz.pegasus.business.models.mutation.RecapListMutation;
import ch.globaz.pegasus.business.models.mutation.RecapMutation;
import ch.globaz.pegasus.business.models.recap.MutationType;
import ch.globaz.pegasus.business.models.recap.Recap;
import ch.globaz.pegasus.business.models.recap.RecapCategorie;
import ch.globaz.pegasus.business.models.recap.RecapituatifPaiement;
import ch.globaz.pegasus.business.models.recap.RepracPcaInfos;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.recap.RecapService;
import ch.globaz.pegasus.business.vo.lot.MutationPcaVo;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.mutation.MutationCategorieResolver;
import ch.globaz.pegasus.businessimpl.services.models.mutation.MutationCategorieResolver.RecapDomainePca;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.prestation.business.exceptions.PrestationCommonException;
import ch.globaz.prestation.business.models.recap.RecapitulationPcRfm;
import ch.globaz.prestation.business.services.PrestationCommonServiceLocator;

public class RecapServiceImpl implements RecapService {

    /**
     * Permet d'extraire les info du custom sql et de les insérer dans la bonne map
     * 
     * @param map
     * @param value
     * @param domaine
     */
    private void addInMapAndCreateReacpitulatif(Map<RecapDomainePca, RecapituatifPaiement> map,
            HashMap<String, Object> value, RecapDomainePca domaine) {
        RecapituatifPaiement recapituatifPaiement = new RecapituatifPaiement();
        recapituatifPaiement.setMontantPresation(new BigDecimal(getData("SOMME", value)));
        recapituatifPaiement.setNbDossier(getData("NOMBRE", value));
        if (map.containsKey(domaine)) {
            RecapituatifPaiement rp = map.get(domaine);
            recapituatifPaiement.setMontantPresation(recapituatifPaiement.getMontantPresation().add(
                    rp.getMontantPresation()));
            recapituatifPaiement.setNbDossier(recapituatifPaiement.getNbDossier() + rp.getNbDossier());
        }
        map.put(domaine, recapituatifPaiement);
    }

    /**
     * Fait les calcules par domaine. Retourne un résumé pour les différents domaine
     * 
     * @param recapCourant
     * @param recapAncien
     * @param mapRecap
     * @return Map<RecapDomainePca[AI|AVS,...], RecapInfoDomaine(résumé du domaine)>
     */
    private Map<RecapDomainePca, RecapInfoDomaine> computeTotalDomaine(
            Map<RecapDomainePca, RecapituatifPaiement> recapCourant,
            Map<RecapDomainePca, RecapituatifPaiement> recapAncien,
            Map<RecapCategorie, Map<RecapDomainePca, RecapListMutation>> mapRecap) {

        Map<RecapDomainePca, RecapListMutation> future = mapRecap.get(RecapCategorie.FUTUR);
        Map<RecapDomainePca, RecapListMutation> adaptation = mapRecap.get(RecapCategorie.ADAPTATION);
        Map<RecapDomainePca, RecapListMutation> normal = mapRecap.get(RecapCategorie.NORMAL);

        Map<RecapDomainePca, RecapInfoDomaine> mapRecapInfo = new HashMap<MutationCategorieResolver.RecapDomainePca, RecapInfoDomaine>();

        for (Entry<RecapDomainePca, RecapListMutation> entry : normal.entrySet()) {
            RecapInfoDomaine infoCategorie = new RecapInfoDomaine();
            BigDecimal montantAdaptation = new BigDecimal(0);
            BigDecimal montantFuture = new BigDecimal(0);

            RecapDomainePca domain = entry.getKey();
            RecapListMutation rlm = entry.getValue();

            RecapListMutation domaineAdaptation = adaptation.get(domain);
            RecapListMutation domaineFuture = future.get(domain);

            if (domaineAdaptation != null) {
                montantAdaptation = domaineAdaptation.getTotalAugmentation().subtract(
                        domaineAdaptation.getTotalDiminution());
                infoCategorie.setAdaptation(montantAdaptation);
            }

            if (domaineFuture != null) {
                montantFuture = domaineFuture.getTotalAugmentation().subtract(domaineFuture.getTotalDiminution());
                infoCategorie.setAugmentationFuture(domaineFuture.getTotalAugmentation());
                infoCategorie.setDiminutationFuture(domaineFuture.getTotalDiminution());
            }

            infoCategorie.setAugmentation(rlm.getTotalAugmentation());
            infoCategorie.setDiminution(rlm.getTotalDiminution());

            infoCategorie.setPresationAncien(recapAncien.get(domain).getMontantPresation());
            infoCategorie.setNbDossierAncien(recapAncien.get(domain).getNbDossier());
            if (recapCourant.get(domain) != null) {
                infoCategorie.setNbDossierNouveau(recapCourant.get(domain).getNbDossier());
                infoCategorie.setPresationNouveau(recapCourant.get(domain).getMontantPresation());
            }

            infoCategorie.setVersementAncien(infoCategorie.getPresationAncien());
            infoCategorie.setVersementNouveau(infoCategorie.getPresationNouveau());

            infoCategorie.setSousTotalDimAug(infoCategorie.getPresationAncien()
                    .add(infoCategorie.getAugmentation().subtract(infoCategorie.getDiminution()))
                    .add(montantAdaptation));

            infoCategorie.setTotalMutation(infoCategorie.getSousTotalDimAug().add(montantFuture));

            infoCategorie.setDifference(infoCategorie.getTotalMutation().subtract(infoCategorie.getPresationNouveau()));

            infoCategorie.setRetro(entry.getValue().getTotalRetro());

            infoCategorie.setTotalJoursAppoint(entry.getValue().getTotalJoursAppoint());
            infoCategorie.setTotalAllocationNoel(entry.getValue().getTotalAllocationNoel());

            infoCategorie.setTotalPaiement(infoCategorie.getRetro().add(infoCategorie.getAugmentation())
                    .add(infoCategorie.getPresationAncien()).add(infoCategorie.getTotalAllocationNoel())
                    .add(infoCategorie.getTotalJoursAppoint()));

            mapRecapInfo.put(domain, infoCategorie);
        }

        return mapRecapInfo;
    }

    /**
     * Permet de créer la liste qui sera utilisé pour l'affichage.
     * 
     * @param list
     * @param typeList
     * @param recapCategorie
     * @return La liste des différente mutation des manière syntétique ainsi que la somme des dim, aug, et retro
     */
    private RecapListMutation createListMutationRecap(List<MutationPcaVo> list, RecapDomainePca typeList,
            RecapCategorie recapCategorie) {

        RecapListMutation recapListMutation = new RecapListMutation();

        List<RecapMutation> listRetrour = new ArrayList<RecapMutation>();
        for (MutationPcaVo mutation : list) {
            RecapMutation recapMutation = createReacpMutation(mutation, typeList, recapCategorie);

            if (recapMutation.getAugmentation() != null) {
                recapListMutation.addTotalAugmentation(recapMutation.getAugmentation());
            }

            if (recapMutation.getDimminution() != null) {
                recapListMutation.addTotalDiminution(recapMutation.getDimminution());
            }

            if (recapMutation.getRetro() != null) {
                recapListMutation.addTotalRetro(recapMutation.getRetro());
            }

            if (recapMutation.getAllocationNoel() != null) {
                recapListMutation.addTotalAllocationNoel(recapMutation.getAllocationNoel());
            }

            if (recapMutation.getJoursAppoint() != null) {
                recapListMutation.addTotalJoursAppoint(recapMutation.getJoursAppoint());
            }

            if ((!mutation.isPurDiminution() && !mutation.isAugementationFutur())
                    || (RecapCategorie.FUTUR.equals(recapCategorie))
                    || (RecapCategorie.ADAPTATION.equals(recapCategorie))) {
                listRetrour.add(recapMutation);
            }

            if (mutation.isCurrent() && mutation.isHasDiminutation() && !RecapCategorie.FUTUR.equals(recapCategorie)) {
                RecapMutation r = recapMutation.clone();
                r.setRetro(null);
                r.setAugmentation(null);
                r.setPeriode("");
                listRetrour.add(r);
                recapMutation.setDimminution(null);
            }
        }

        recapListMutation.setList(listRetrour);
        return recapListMutation;
    }

    private Map<RecapDomainePca, RecapListMutation> createMapRecap(
            Entry<RecapCategorie, Map<RecapDomainePca, List<MutationPcaVo>>> entry2) {
        Map<RecapDomainePca, RecapListMutation> mapRetour = new HashMap<RecapDomainePca, RecapListMutation>();
        for (Entry<RecapDomainePca, List<MutationPcaVo>> entry : entry2.getValue().entrySet()) {
            RecapListMutation r = createListMutationRecap(entry.getValue(), entry.getKey(), entry2.getKey());
            mapRetour.put(entry.getKey(), r);
        }
        return mapRetour;
    }

    /**
     * Permet de créer une mutation de type recap
     * 
     * @param mutation
     * @param typeListe
     * @param recapCategorie
     * @return une mutation pour la recp
     */
    private RecapMutation createReacpMutation(MutationPcaVo mutation, RecapDomainePca typeListe,
            RecapCategorie recapCategorie) {
        RecapMutation recapMutation = new RecapMutation();
        // si on fait le passage de AI à AVS on a automatiquement une augmentation pour l'avs et une diminution pour
        // l'ai

        BigDecimal montantRetro = null;
        if (!JadeStringUtil.isBlank(mutation.getMontantRetro())) {
            montantRetro = new BigDecimal(mutation.getMontantRetro());
        }
        if (IPCDecision.CS_TYPE_SUPPRESSION_SC.equals(mutation.getCsTypeDecision())) {
            recapMutation.setTypeDeMutation(MutationType.SUPRESSION);
        } else if (mutation.isAugementationFutur()) {
            recapMutation.setTypeDeMutation(MutationType.FUTURE);
        } else {
            recapMutation.setTypeDeMutation(MutationType.STDANDAR);
        }

        if (mutation.getNoVersion().equals("1")) {
            recapMutation.setNewDroit(true);
        }

        if (!JadeStringUtil.isBlankOrZero(mutation.getMontantAllocationNoel())) {
            recapMutation.setAllocationNoel(new BigDecimal(mutation.getMontantAllocationNoel()));
        }

        if (!JadeStringUtil.isBlankOrZero(mutation.getMontantJourAppointActuel())) {
            recapMutation.setJoursAppoint(new BigDecimal(mutation.getMontantJourAppointActuel()));
        }

        recapMutation.setNss(mutation.getNss());
        recapMutation.setNom(mutation.getNom());
        recapMutation.setPrenom(mutation.getPrenom());
        recapMutation.setPeriode(mutation.getDateDebutPcaActuel() + " - " + mutation.getDateFinPcaActuel());
        recapMutation.setPassage(mutation.getPassage());
        if ((IPCDecision.CS_PREP_STANDARD.equals(mutation.getCsTypePreparationDecision()) || IPCDecision.CS_PREP_COURANT
                .equals(mutation.getCsTypePreparationDecision())) && mutation.isCurrent()) {
            if (EPCPMutationPassage.AUCUN.equals(mutation.getPassage())) {

                recapMutation.setRetro(montantRetro);

                if (!mutation.isPurRetro()) {
                    // Si on une augmentation futur et que l'on traite la catégorie future on met que l'augmentation.
                    // Car la diminution doit se trouver dans la catégorie normale.
                    // Si c'est n'est pas du futur alors on est dans un cas normale
                    if ((recapCategorie.isFutur() && mutation.isAugementationFutur())
                            || !mutation.isAugementationFutur()) {
                        recapMutation.setAugmentation(new BigDecimal(mutation.getMontantActuel()));
                    }
                }
                if (mutation.getMontantPrecedant() != null) {
                    BigDecimal dim = null;
                    if (JadeStringUtil.isBlankOrZero(mutation.getMontantPrecedant())) {
                        dim = new BigDecimal(0);
                    } else {
                        dim = new BigDecimal(mutation.getMontantPrecedant());
                    }
                    // On ne met pas la diminution si on traite la catégorie du future
                    if (!(recapCategorie.isFutur() && mutation.isAugementationFutur())) {
                        recapMutation.setDimminution(dim);
                    }
                }
            } else {
                // Si l'ancien type de pca correspond aux type de la liste alors on fait qu'une seule diminution. Car on
                // traite le passage d'AVS a AI ou inversement
                if (typeListe.equals(mutation.getCodeCategroriePcaPrecedante())) {
                    recapMutation.setDimminution(new BigDecimal(mutation.getMontantPrecedant()));
                } else { // Si il s'agit d'un transfert il faut setter l'augmentation pour l'afficher dans le domaine et
                         // le retro
                    recapMutation.setRetro(montantRetro);
                    recapMutation.setAugmentation(new BigDecimal(mutation.getMontantActuel()));
                }
            }
        } else if (!IPCDecision.CS_TYPE_SUPPRESSION_SC.equals(mutation.getCsTypeDecision())
                && (IPCDecision.CS_PREP_RETRO.equals(mutation.getCsTypePreparationDecision()) || (!mutation.isCurrent()))) {
            recapMutation.setRetro(montantRetro);
        } else if (IPCDecision.CS_TYPE_SUPPRESSION_SC.equals(mutation.getCsTypeDecision())) {
            if (mutation.getMontantPrecedant() != null) {
                recapMutation.setDimminution(new BigDecimal(mutation.getMontantPrecedant()));
            }
        }

        if (IPCDecision.CS_TYPE_ADAPTATION_AC.equals(mutation.getCsTypeDecision())) {
            if (RecapCategorie.ADAPTATION.equals(recapCategorie)) {
                recapMutation.setAugmentation(new BigDecimal(mutation.getMontantActuel()));
            }
        }

        return recapMutation;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.recap.RecapService#createRecap(java.lang.String)
     */
    @Override
    public Recap createRecap(String dateMonth) throws MutationException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        Map<RecapCategorie, Map<RecapDomainePca, List<MutationPcaVo>>> map = PegasusImplServiceLocator
                .getMutationPcaService().findAndgroupeByCategorie(dateMonth);

        Map<RecapCategorie, Map<RecapDomainePca, RecapListMutation>> mapRecap = new HashMap<RecapCategorie, Map<RecapDomainePca, RecapListMutation>>();

        RepracPcaInfos recapPaimentCourant = null;
        String dateProchainPmtComptat;
        try {
            dateProchainPmtComptat = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
        } catch (PmtMensuelException e) {
            throw new MutationException("Unable to obtain the next date of paiement", e);
        }
        Map<RecapDomainePca, RecapituatifPaiement> recapCourant = null;
        String datePmtRecap = PegasusDateUtil.addMonths(dateMonth, +1);

        // Si on a déjà fait le paiement il faut utilise la table qui à sauvegardé la donnée et plus faire la somme des
        // repracs
        if (dateProchainPmtComptat.equals(datePmtRecap)) {
            recapPaimentCourant = findRecapPcaReprac(dateMonth);
            recapCourant = recapPaimentCourant.getReprac();
        } else {
            recapCourant = findReacpituationPC(datePmtRecap);
        }

        Map<RecapDomainePca, RecapituatifPaiement> mapRecapPaimentAncien = findReacpituationPC(dateMonth);

        for (Entry<RecapCategorie, Map<RecapDomainePca, List<MutationPcaVo>>> entry : map.entrySet()) {
            mapRecap.put(entry.getKey(), createMapRecap(entry));
        }

        Map<RecapDomainePca, RecapInfoDomaine> mapRetour = computeTotalDomaine(recapCourant, mapRecapPaimentAncien,
                mapRecap);

        Recap recap = new Recap();

        recap.setRecapPaimentCourant(recapPaimentCourant);
        recap.setInfosDomaine(mapRetour);
        recap.setRecapMutation(mapRecap);

        for (Entry<RecapDomainePca, RecapInfoDomaine> entry : recap.getInfosDomaine().entrySet()) {
            recap.setTotalMois(recap.getTotalMois().add(entry.getValue().getVersementAncien()));
            recap.setTotalMutation(recap.getTotalMutation().add(entry.getValue().getTotalMutation()));
            recap.setTotalPaiement(recap.getTotalPaiement().add(entry.getValue().getVersementNouveau()));
        }

        return recap;

    }

    /**
     * Permet de rechercher les paiement qui ont été réalisé le mois passé
     * 
     * @param date
     * @return Une map avec comme clé le domaine
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws MutationException
     */
    private Map<RecapDomainePca, RecapituatifPaiement> findReacpituationPC(String date)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException, MutationException {
        RecapitulationPcRfm recapitulationPcRfm = null;
        Map<RecapDomainePca, RecapituatifPaiement> map = new HashMap<RecapDomainePca, RecapituatifPaiement>();

        try {
            recapitulationPcRfm = PrestationCommonServiceLocator.getRecapitulationPcRfmService().findInfoRecapByDate(
                    date);
            if (recapitulationPcRfm == null) {
                throw new MutationException("Unable to find recapitualtionPcRfm(PCRFMREC) for this date: " + date);
            }

            RecapituatifPaiement ai = new RecapituatifPaiement();
            ai.setMontantPresation(recapitulationPcRfm.getMontantTotalPCAI().getBigDecimalValue());
            ai.setNbDossier(recapitulationPcRfm.getNbPrestationPCAI());
            map.put(RecapDomainePca.AI, ai);

            RecapituatifPaiement avs = new RecapituatifPaiement();
            avs.setMontantPresation(recapitulationPcRfm.getMontantTotalPCAVS().getBigDecimalValue());
            avs.setNbDossier(recapitulationPcRfm.getNbPrestationPCAVS());
            map.put(RecapDomainePca.AVS, avs);

        } catch (PrestationCommonException e) {
            throw new MutationException("Unable to find the infoRecap", e);
        }

        return map;
    }

    /**
     * Permet de faire la somme des pca et la somme de reprac
     * 
     * @param dateMonth
     *            : Date à la quelle on veut faire la somme
     * @return Un Map content la somme et le nombre de dossier ainsi que la difference entre les pca et les reprac
     *         normalement cette différence deverait toujours être de 0
     * @throws JadePersistenceException
     */
    private RepracPcaInfos findRecapPcaReprac(String dateMonth) throws JadePersistenceException {
        // @formatter:off
		String sql = "Select * from ( "
				+ "select sum(planCalcule.CVMPCM) as SOMME,  count(planCalcule.CVMPCM) as NOMBRE, pcAccordee.CUTTYP as TYPEPC, 1 as ORDRE "
				+ "from {schema}.PCPCACC pcAccordee "
				+ "inner join {schema}.PCPLCAL planCalcule "
				+ "on pcAccordee.CUIPCA = planCalcule.CVIPCA "
				+ "where planCalcule.CVBPLR = 1 "
				+ "and planCalcule.CVLEPC <> 64061003 "// STATUS_REFUS
				+ "and (pcAccordee.CUTETA = 64029002 or pcAccordee.CUTETA = 64029003) " // etat validé et courant validé
				+ "and pcAccordee.CUDDEB <= {date} "
				+ "and (pcAccordee.CUDFIN >= {date} or (pcAccordee.CUDFIN is null or pcAccordee.CUDFIN = 0)) "
				+ "group by pcAccordee.CUTTYP "
				+ "UNION "
				+ "select sum(presation.ZTMPRE) as SOMME, count(presation.ZTMPRE) as NOMBRE, cast(presation.ZTLCPR as DECIMAL) as TYPEPC, 2 as ORDRE "
				+ "from {schema}.REPRACC presation "
				+ "where presation.ZTTGEN = 52849002 "// type PC
				// etat valide ou partiel ou (diminué et en cours)
				+ "and (presation.ZTTETA = 52820002 or presation.ZTTETA = 52820003 or (presation.ZTTETA = 52820004 and presation.ZTDFDR >= {date})) "
				+ "and presation.ZTDDDR <= {date} "
				+ "and (presation.ZTDFDR >= {date} or (presation.ZTDFDR is null or presation.ZTDFDR = 0)) "
				+ "and presation.ZTLCPR not in ('158', '118', '119', '159') " // allocation de noel
				+ "group by presation.ZTLCPR " + ") as t" + " order by ordre";

		sql = sql.replace("{schema}", JadePersistenceUtil.getDbSchema());
		sql = sql.replace(
				"{date}",
				JadePersistenceUtil.parseMonthYearToSql(JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01."
						+ dateMonth, +1))));

		ArrayList<HashMap<String, Object>> result = PersistenceUtil.executeQuery(sql, this.getClass());

		Map<RecapDomainePca, RecapituatifPaiement> mapReprac = new HashMap<RecapDomainePca, RecapituatifPaiement>();
		Map<RecapDomainePca, RecapituatifPaiement> mapPca = new HashMap<RecapDomainePca, RecapituatifPaiement>();

		for (HashMap<String, Object> value : result) {
			String typePresation = String.valueOf(value.get("TYPEPC"));
			RecapDomainePca domaine = MutationCategorieResolver.getCodeCategorieByCodePresation(typePresation);
			if (domaine != null) {
				addInMapAndCreateReacpitulatif(mapReprac, value, domaine);
			} else {
				domaine = MutationCategorieResolver.getCodeCategorie(typePresation);
				addInMapAndCreateReacpitulatif(mapPca, value, domaine);
			}
		}

		RepracPcaInfos infos = new RepracPcaInfos();
		infos.setPca(mapPca);
		infos.setReprac(mapReprac);

		if (infos.getPca() != null) {
			for (Entry<RecapDomainePca, RecapituatifPaiement> e : infos.getPca().entrySet()) {
				BigDecimal montantPca = e.getValue().getMontantPresation();
				BigDecimal montantReprac = new BigDecimal(0);

				if (infos.getReprac().containsKey((e.getKey()))) {
					montantReprac = infos.getReprac().get(e.getKey()).getMontantPresation();
				}
				infos.getDiffPcaReprac().put(e.getKey(), montantPca.subtract(montantReprac));
			}
		}

		return infos;
	}

	private Integer getData(String key, Map<String, Object> map) {
		if (map != null) {
			return (int) Double.parseDouble((String.valueOf(map.get(key))));
		}
		return 0;
	}
}
