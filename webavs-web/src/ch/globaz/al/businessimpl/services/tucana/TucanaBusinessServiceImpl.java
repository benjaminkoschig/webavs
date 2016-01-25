package ch.globaz.al.businessimpl.services.tucana;

import globaz.itucana.constantes.ITUCSRubriqueListeDesRubriques;
import globaz.itucana.model.ITUModelBouclement;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.exceptions.tucana.ALTucanaException;
import ch.globaz.al.business.models.allocataire.AllocataireModel;
import ch.globaz.al.business.models.droit.EnfantModel;
import ch.globaz.al.business.models.prestation.TransfertTucanaComplexModel;
import ch.globaz.al.business.models.prestation.TransfertTucanaModel;
import ch.globaz.al.business.models.prestation.TransfertTucanaSearchComplexModel;
import ch.globaz.al.business.models.prestation.TransfertTucanaSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.prestation.TransfertTucanaModelService;
import ch.globaz.al.business.services.tucana.TucanaBusinessService;
import ch.globaz.al.business.tucana.TucanaTransfertBusinessModel;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;

public class TucanaBusinessServiceImpl extends ALAbstractBusinessServiceImpl implements TucanaBusinessService {

    /**
     * Ajouter les montants dans <code>transfBusiness</code> en fonction des informations de <code>model</code>
     * 
     * @param transfBusiness
     * @param model
     * @throws JadeApplicationException
     */

    private TucanaTransfertBusinessModel calculMontants(TucanaTransfertBusinessModel transfBusiness,
            TransfertTucanaComplexModel model) throws JadeApplicationException {

        if (ITUCSRubriqueListeDesRubriques.CS_ALLOCATION_STATUTAIRE_SUPPLEMENT_HORLOGER.equals(model
                .getRubriqueSupplementConventionnel())) {
            transfBusiness.addConvention(new BigDecimal(model.getMontant()));
            transfBusiness.addSupplement(new BigDecimal("0.00"));
            transfBusiness.addCumul(model.getMontant());
        } else {

            BigDecimal suppConventionnel = getSupplementConventionnel(model.getTypePrestation(),
                    new BigDecimal(model.getMontantCaisse()), new BigDecimal(model.getMontantCanton()), new BigDecimal(
                            model.getMontant()));

            BigDecimal supplement = getSupplementLegal(model.getTypePrestation(),
                    new BigDecimal(model.getMontantCaisse()), new BigDecimal(model.getMontantCanton()), new BigDecimal(
                            model.getMontant()));

            transfBusiness.addConvention(suppConventionnel);
            transfBusiness.addSupplement(supplement);
            transfBusiness.addCumul(model.getMontant());
        }

        return transfBusiness;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.tucana.TucanaBusinessService#deleteBouclement(java.lang.String)
     */
    @Override
    public void deleteBouclement(String numBouclement) throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(numBouclement)) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#deleteBouclement : numBouclement is empty");
        }

        TransfertTucanaModelService s = ALImplServiceLocator.getTransfertTucanaModelService();

        TransfertTucanaSearchModel search = new TransfertTucanaSearchModel();
        search.setForNumBouclement(numBouclement);
        search.setWhereKey("deleteBouclement");
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search = s.search(search);

        for (int i = 0; i < search.getSize(); i++) {
            TransfertTucanaModel transfert = (TransfertTucanaModel) search.getSearchResults()[i];
            transfert.setNumBouclement(null);
            s.update(transfert);
        }
    }

    /**
     * 
     * @param a
     *            montant 1 à comparer
     * @param b
     *            montant 2 à comparer
     * @param precis
     *            limite de la précision (tolérance)
     * @return Boolean true si tolérance pas dépassé
     */
    private Boolean egalMontantsTolerance(BigDecimal a, BigDecimal b, Float precis) {

        if ((a.subtract(b).abs().floatValue()) < precis) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.tucana.TucanaBusinessService#getRubriqueAllocation(java.lang.String,
     * ch.globaz.al.business.models.allocataire.AllocataireModel, ch.globaz.al.business.models.droit.EnfantModel)
     */
    @Override
    public String getRubriqueAllocation(String typePrestation, String catTarif, AllocataireModel allocataire,
            EnfantModel enfant) throws JadeApplicationException {

        if (JadeStringUtil.isEmpty(typePrestation)) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#getRubriqueAllocation : typePrestation is empty");
        }

        if (allocataire == null) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#getRubriqueAllocation : allocataire is null");
        }

        if (enfant == null) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#getRubriqueAllocation : enfant is empty");
        }

        String typeResident = ALServiceLocator.getAllocataireBusinessService().getTypeResident(allocataire);

        if (ALCSTarif.CATEGORIE_SUP_HORLO.equals(catTarif)) {
            return ITUCSRubriqueListeDesRubriques.CS_ALLOCATION_STATUTAIRE_SUPPLEMENT_HORLOGER;

            // MEN
        } else if (ALCSDroit.TYPE_MEN.equals(typePrestation)) {
            if (ALCSTarif.RESIDENT_CH.equals(typeResident)) {
                return ITUCSRubriqueListeDesRubriques.CS_AF_ALLOC_MENAGE;
            } else {
                return ITUCSRubriqueListeDesRubriques.CS_AF_ALLOC_MENAGE_ETRANGERS;
            }
            // NAIS
        } else if (ALCSDroit.TYPE_NAIS.equals(typePrestation)) {
            if (ALCSTarif.RESIDENT_CH.equals(typeResident)) {
                return ITUCSRubriqueListeDesRubriques.CS_AF_ALLOCNAISSANCES_SUISSES;
            } else {
                return ITUCSRubriqueListeDesRubriques.CS_AF_ALLOCNAISSANCES_ETRANGERS;
            }
            // ACCE
        } else if (ALCSDroit.TYPE_ACCE.equals(typePrestation)) {
            if (ALCSTarif.RESIDENT_CH.equals(typeResident)) {
                return ITUCSRubriqueListeDesRubriques.CS_AF_ALLOCACCUEILS_SUISSES;
            } else {
                return ITUCSRubriqueListeDesRubriques.CS_AF_ALLOCACCUEILS_ETRANGERS;
            }
        } else if (!enfant.getCapableExercer()) {
            if (ALCSTarif.RESIDENT_CH.equals(typeResident)) {
                return ITUCSRubriqueListeDesRubriques.CS_AF_INVALIDES_SUISSES;
            } else {
                return ITUCSRubriqueListeDesRubriques.CS_AF_INVALIDES_ETRANGERS;
            }
            // FORM
        } else if (ALCSDroit.TYPE_FORM.equals(typePrestation)) {
            if (ALCSTarif.RESIDENT_CH.equals(typeResident)) {
                return ITUCSRubriqueListeDesRubriques.CS_AF_AUTRE_FORMATION_PROFESSIONNELLE_SUISSE;
            } else {
                return ITUCSRubriqueListeDesRubriques.CS_AF_AUTRE_FORMATION_PROFESSIONNELLE_ETRANGERS;
            }
            // ENF
        } else if (ALCSDroit.TYPE_ENF.equals(typePrestation)) {
            if (ALCSTarif.RESIDENT_CH.equals(typeResident)) {
                return ITUCSRubriqueListeDesRubriques.CS_AF_ORDINAIRES_SUISSES;
            } else {
                return ITUCSRubriqueListeDesRubriques.CS_AF_ORDINAIRES_ETRANGERS;
            }
        } else {
            throw new ALTucanaException(
                    "TucanaBusinessServiceImpl#getRubriqueAllocation : Impossible de déterminer le type de rubrique");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.tucana.TucanaBusinessService#getRubriqueSupplements(java.lang.String,
     * ch.globaz.al.business.models.allocataire.AllocataireModel, ch.globaz.al.business.models.droit.EnfantModel)
     */
    @Override
    public ArrayList<String> getRubriqueSupplements(String typePrestation, String catTarif,
            AllocataireModel allocataire, EnfantModel enfant) throws JadeApplicationException {

        if (JadeStringUtil.isEmpty(typePrestation)) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#getRubriqueSupplements : typePrestation is empty");
        }

        if (allocataire == null) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#getRubriqueSupplements : allocataire is null");
        }

        if (enfant == null) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#getRubriqueSupplements : enfant is empty");
        }

        String typeResident = ALServiceLocator.getAllocataireBusinessService().getTypeResident(allocataire);
        ArrayList<String> list = new ArrayList<String>();

        if (ALCSTarif.CATEGORIE_SUP_HORLO.equals(catTarif)) {
            list.add("0"); // pas de supplément légal
            list.add(ITUCSRubriqueListeDesRubriques.CS_ALLOCATION_STATUTAIRE_SUPPLEMENT_HORLOGER);
        } else if (ALCSDroit.TYPE_FNB.equals(typePrestation)) {
            list.add(ITUCSRubriqueListeDesRubriques.CS_AF_FAMILLES_NOMBREUSES_VAUD_SUPPLEMENT_LEGAL);
            list.add(ITUCSRubriqueListeDesRubriques.CS_AF_FAMILLES_NOMBREUSES_VAUD_SUPPLEMENT_CONVENTION);
        } else if (ALCSDroit.TYPE_MEN.equals(typePrestation)) {
            if (ALCSTarif.RESIDENT_CH.equals(typeResident)) {
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_ALLOC_MENAGE_SUPPLEMENT_LEGAL);
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_ALLOC_MENAGE_SUPPLEMENT_CONVENTION);
            } else {
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_ALLOC_MENAGE_ETRANGERS_SUPPLEMENT_LEGAL);
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_ALLOC_MENAGE_ETRANGERS_SUPPLEMENT_CONVENTION);
            }

        } else if (ALCSDroit.TYPE_NAIS.equals(typePrestation)) {
            if (ALCSTarif.RESIDENT_CH.equals(typeResident)) {
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_ALLOCNAISSANCES_SUISSES_SUPPLEMENT_LEGAL);
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_ALLOCNAISSANCES_SUISSES_SUPPLEMENT_CONVENTION);
            } else {
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_ALLOCNAISSANCES_ETRANGERS_SUPPLEMENT_LEGAL);
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_ALLOCNAISSANCES_ETRANGERS_SUPPLEMENT_CONVENTION);
            }
        } else if (ALCSDroit.TYPE_ACCE.equals(typePrestation)) {
            if (ALCSTarif.RESIDENT_CH.equals(typeResident)) {
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_ALLOCACCUEILS_SUISSES_SUPPLEMENT_LEGAL);
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_ALLOCACCUEILS_SUISSES_SUPPLEMENT_CONVENTION);
            } else {
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_ALLOCACCUEILS_ETRANGERS_SUPPLEMENT_LEGAL);
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_ALLOCACCUEILS_ETRANGERS_SUPPLEMENT_CONVENTION);
            }
        } else if (!enfant.getCapableExercer()) {
            if (ALCSTarif.RESIDENT_CH.equals(typeResident)) {
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_INVALIDES_SUISSES_SUPPLEMENT_LEGAL);
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_INVALIDES_SUISSES_SUPPLEMENT_CONVENTION);
            } else {
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_INVALIDES_ETRANGERS_SUPPLEMENT_LEGAL);
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_INVALIDES_ETRANGERS_SUPPLEMENT_CONVENTION);
            }
        } else if (ALCSDroit.TYPE_FORM.equals(typePrestation)) {
            if (ALCSTarif.RESIDENT_CH.equals(typeResident)) {
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_AUTRE_FORMATION_PROFESSIONNELLE_SUISSE_SUPPLEMENT_LEGAL);
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_AUTRE_FORMATION_PROFESSIONNELLE_SUISSE_SUPPLEMENT_CONVENTION);
            } else {
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_AUTRE_FORMATION_PROFESSIONNELLE_ETRANGERS_SUPPLEMENT_LEGAL);
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_AUTRE_FORMATION_PROFESSIONNELLE_ETRANGERS_SUPPLEMENT_CONVENTION);
            }

        } else if (ALCSDroit.TYPE_ENF.equals(typePrestation)) {
            if (ALCSTarif.RESIDENT_CH.equals(typeResident)) {
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_ORDINAIRES_SUISSES_SUPPLEMENT_LEGAL);
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_ORDINAIRES_SUISSES_SUPPLEMENT_CONVENTION);
            } else {
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_ORDINAIRES_ETRANGERS_SUPPLEMENT_LEGAL);
                list.add(ITUCSRubriqueListeDesRubriques.CS_AF_ORDINAIRES_ETRANGERS_SUPPLEMENT_CONVENTION);
            }

        } else {
            throw new ALTucanaException(
                    "TucanaBusinessServiceImpl#getRubriqueSupplements : Impossible de déterminer le type de rubrique");
        }

        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.tucana.TucanaBusinessService#getSupplementConventionnel(java.math.BigDecimal,
     * java.math.BigDecimal, java.math.BigDecimal)
     */
    @Override
    public BigDecimal getSupplementConventionnel(String typePrestation, BigDecimal montantCaisse,
            BigDecimal montantCanton, BigDecimal montant) throws JadeApplicationException {

        if (JadeStringUtil.isEmpty(typePrestation)) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#getSupplementLegal : typePrestation is empty");
        }

        if (montantCaisse == null) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#getSupplementConventionnel : montantCaisse is null");
        }

        if (montantCanton == null) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#getSupplementConventionnel : montantCanton is null");
        }

        if (montant == null) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#getSupplementConventionnel : montant is null");
        }

        BigDecimal montantAbsolutPrestation = new BigDecimal("0.00");
        BigDecimal montantAbolutCaisse = new BigDecimal("0.00");
        BigDecimal montantAbolutCanton = new BigDecimal("0.00");
        BigDecimal montantConvention = new BigDecimal("0.00");
        BigDecimal montantPrestation = montant;

        // Correction allocations ménages négatives (BZ 6533)
        if (ALCSDroit.TYPE_MEN.equals(typePrestation)) {

            // montantCaisse > montantCanton
            if (montantCaisse.compareTo(montantCanton) == 1) {

                // FIXME on reprend les valeurs absolues des montants car dans les prestations présence de montant
                // caisse positif pour montant versé négatif, remonter dans le bugzilla Bug 7871
                montantAbsolutPrestation = montantPrestation.abs();
                montantAbolutCaisse = montantCaisse.abs();
                montantAbolutCanton = montantCanton.abs();

                // Si le montant absolu prestation est identique au montant absolue de la caisse caisse en fonction de
                // la tolérance
                if (egalMontantsTolerance(montantAbsolutPrestation, montantAbolutCaisse,
                        ALConstPrestations.TOLERANCE_COMPARAISON_MONTANT)) {

                    // montant conventionnel montant absolu de la prestation- montant du canton
                    montantConvention = montantAbsolutPrestation.subtract(montantAbolutCanton);

                    // au besoin, on transforme le montant absolu en négatif (identique à la prestation)
                    if (montantAbsolutPrestation.compareTo(montantPrestation) != 0) {
                        montantConvention = montantConvention.negate();
                    }
                }
            } else {
                // FIXME voir FIXME ci-dessus
                if ((montant.floatValue() < 0) && (montantCaisse.floatValue() > 0)) {
                    montantConvention = montantCaisse.negate();
                } else {
                    montantConvention = montantCaisse;
                }
            }
            return montantConvention;
        } else {

            // montantCaisse > montantCanton
            if (montantCaisse.compareTo(montantCanton) == 1) {

                montantAbsolutPrestation = montantPrestation.abs();

                // Si le montant prestation est identique au montant cantonal
                if (montantAbsolutPrestation.compareTo(montantCaisse) == 0) {

                    montantConvention = montantCaisse.subtract(montantCanton);

                    // au besoin, on transforme le montant absolu en négatif (identique à la prestation)
                    if (montantAbsolutPrestation.compareTo(montantPrestation) != 0) {
                        montantConvention = montantConvention.negate();
                    }

                    return montantConvention;
                }
            }

            return montantConvention;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.tucana.TucanaBusinessService#getSupplementLegal(java.lang.String,
     * java.math.BigDecimal, java.math.BigDecimal, java.math.BigDecimal)
     */
    @Override
    public BigDecimal getSupplementLegal(String typePrestation, BigDecimal montantCaisse, BigDecimal montantCanton,
            BigDecimal montant) throws JadeApplicationException {

        if (JadeStringUtil.isEmpty(typePrestation)) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#getSupplementLegal : typePrestation is empty");
        }

        if (montantCaisse == null) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#getSupplementLegal : montantCaisse is null");
        }

        if (montantCanton == null) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#getSupplementLegal : montantCanton is null");
        }

        if (montant == null) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#getSupplementLegal : montant is null");
        }

        BigDecimal montantAbsolutPrestation = new BigDecimal("0.00");
        BigDecimal montantSupplement = new BigDecimal("0.00");
        BigDecimal montantPrestation = montant;

        if (ALCSDroit.TYPE_FNB.equals(typePrestation)) {
            return montantPrestation;
        } else {

            // si montant canton > montant caisse
            if (montantCanton.compareTo(montantCaisse) == 1) {

                montantAbsolutPrestation = montantPrestation.abs();

                // Si le montant prestation est identique au montant cantonal du calcul : le supplément légal peut être
                // calculé immédiatement
                if (montantAbsolutPrestation.compareTo(montantCanton) == 0) {
                    montantSupplement = montantCanton.subtract(montantCaisse);
                    // au besoin, on transforme le montant absolu en négatif (identique à la prestation)
                    if (montantAbsolutPrestation.compareTo(montantPrestation) != 0) {
                        montantSupplement = montantSupplement.negate();
                    }

                    return montantSupplement;
                }
            }
        }
        return montantSupplement;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.tucana.TucanaBusinessService#initBouclement(globaz.itucana.model.ITUModelBouclement
     * , java.lang.String, java.lang.String)
     */
    @Override
    public ITUModelBouclement initBouclement(ITUModelBouclement bouclement, String annee, String mois)
            throws JadeApplicationException, JadePersistenceException {

        if (bouclement == null) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#initBouclement : bouclement is null");
        }

        if (JadeStringUtil.isEmpty(annee)) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#initBouclement : annee is empty");
        }

        if (JadeStringUtil.isEmpty(mois)) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#initBouclement : mois is empty");
        }

        String dateDebut = (new StringBuffer("01.").append(JadeStringUtil.fillWithZeroes(mois, 2)).append(".")
                .append(annee)).toString();

        String numPassage = (new StringBuffer(annee).append(JadeStringUtil.fillWithZeroes(mois, 2))).toString();

        ArrayList<TucanaTransfertBusinessModel> tranferts = prepareData(
                loadData(dateDebut, ALDateUtils.getDateFinMois(dateDebut)), numPassage);

        bouclement.setEntete(annee, mois, numPassage);
        boolean isLAFam = ALImplServiceLocator.getCalculService().isDateLAFam(dateDebut);
        TucanaTransfertBusinessModel model = null;

        for (int i = 0; i < tranferts.size(); i++) {
            model = tranferts.get(i);

            if (!(ITUCSRubriqueListeDesRubriques.CS_ALLOCATION_STATUTAIRE_SUPPLEMENT_HORLOGER.equals(model
                    .getRubriqueTypePrestation()))) {
                bouclement.addLine(model.getCanton(), model.getRubriqueTypePrestation(), model.getCumul()
                        .toPlainString());
            }
            bouclement.addLine(model.getCanton(), model.getRubriqueSupplement(), model.getSupplement().toPlainString());
            if (isLAFam) {
                bouclement.addLine(model.getCanton(), model.getRubriqueConventionnel(), model.getConvention()
                        .toPlainString());
            }
        }

        return bouclement;
    }

    /**
     * 
     * @param dateDebut
     * @param dateFin
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    private TransfertTucanaSearchComplexModel loadData(String dateDebut, String dateFin)
            throws JadePersistenceException, JadeApplicationException {

        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#loadData : dateDebut '" + dateDebut
                    + "' is not a valid date");
        }

        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#loadData : dateFin '" + dateFin
                    + "' is not a valid date");
        }

        TransfertTucanaSearchComplexModel search = new TransfertTucanaSearchComplexModel();
        search.setForDateVersCompDebut(dateDebut);
        search.setForDateVersCompFin(dateFin);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setWhereKey("aTransferer");
        return (TransfertTucanaSearchComplexModel) JadePersistenceManager.search(search);
    }

    /**
     * 
     * @param search
     * @param numBouclement
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private ArrayList<TucanaTransfertBusinessModel> prepareData(TransfertTucanaSearchComplexModel search,
            String numBouclement) throws JadeApplicationException, JadePersistenceException {

        if (search == null) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#prepareData : search is null");
        }

        if (JadeStringUtil.isEmpty(numBouclement)) {
            throw new ALTucanaException("TucanaBusinessServiceImpl#prepareData : numBouclement is empty");
        }

        String rubriqueAllocation = "";
        String rubriqueSupplementLegal = "";
        String rubriqueSupplementConventionnel = "";
        String canton = "";

        TucanaTransfertBusinessModel transfertTmp = null;
        ArrayList<TucanaTransfertBusinessModel> list = new ArrayList<TucanaTransfertBusinessModel>();

        for (int i = 0; i < search.getSize(); i++) {
            TransfertTucanaComplexModel model = (TransfertTucanaComplexModel) search.getSearchResults()[i];

            if (JadeStringUtil.isEmpty(rubriqueAllocation) || !rubriqueAllocation.equals(model.getRubriqueAllocation())
                    || JadeStringUtil.isEmpty(rubriqueSupplementLegal)
                    || !rubriqueSupplementLegal.equals(model.getRubriqueSupplementLegal())
                    || JadeStringUtil.isEmpty(rubriqueSupplementConventionnel)
                    || !rubriqueSupplementConventionnel.equals(model.getRubriqueSupplementConventionnel())
                    || JadeStringUtil.isEmpty(canton) || !canton.equals(model.getCantonAffilie())) {

                if (transfertTmp != null) {
                    list.add(transfertTmp);
                }
                transfertTmp = new TucanaTransfertBusinessModel(JadeCodesSystemsUtil.getCode(model.getCantonAffilie()),
                        model.getRubriqueSupplementConventionnel(), model.getRubriqueSupplementLegal(),
                        model.getRubriqueAllocation());
            }

            transfertTmp = calculMontants(transfertTmp, model);

            // mise à jour de l'état du transfert
            TransfertTucanaModel transfert = ALImplServiceLocator.getTransfertTucanaModelService().read(
                    model.getIdTucana());
            transfert.setNumBouclement(numBouclement);
            ALImplServiceLocator.getTransfertTucanaModelService().update(transfert);

            rubriqueAllocation = model.getRubriqueAllocation();
            rubriqueSupplementLegal = model.getRubriqueSupplementLegal();
            rubriqueSupplementConventionnel = model.getRubriqueSupplementConventionnel();
            canton = model.getCantonAffilie();
        }

        if (transfertTmp != null) {
            list.add(transfertTmp);
        }

        return list;
    }
}
