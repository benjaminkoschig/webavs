/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.pcfaccordee;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Hashtable;
import java.util.Set;
import ch.globaz.perseus.business.calcul.OutputCalcul;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.CSEtatPcfaccordee;
import ch.globaz.perseus.business.constantes.CSTypeBareme;
import ch.globaz.perseus.business.constantes.CSTypeConjoint;
import ch.globaz.perseus.business.constantes.CSTypeCreance;
import ch.globaz.perseus.business.constantes.CSTypeRetenue;
import ch.globaz.perseus.business.constantes.CSVariableMetier;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.exceptions.calcul.CalculException;
import ch.globaz.perseus.business.exceptions.models.creancier.CreancierException;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.impotsource.TauxException;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueException;
import ch.globaz.perseus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.creancier.Creancier;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.impotsource.Taux;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordeeForDetailsCalcul;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordeeForDetailsCalculSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordeeSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.SimpleDetailsCalcul;
import ch.globaz.perseus.business.models.pcfaccordee.SimpleDetailsCalculSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.SimplePCFAccordee;
import ch.globaz.perseus.business.models.retenue.Retenue;
import ch.globaz.perseus.business.models.retenue.SimpleRetenueSearchModel;
import ch.globaz.perseus.business.models.variablemetier.VariableMetier;
import ch.globaz.perseus.business.models.variablemetier.VariableMetierSearch;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.pcfaccordee.PCFAccordeeService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author DDE
 * 
 */
public class PCFAccordeeServiceImpl extends PerseusAbstractServiceImpl implements PCFAccordeeService {

    static final BigDecimal ZERO = new BigDecimal(0.0);

    @Override
    public PCFAccordee calculer(String idDemande) throws JadePersistenceException, PCFAccordeeException {
        PCFAccordee pcfAccordee = null;
        try {
            // Vérifier que la demande peut être calculée
            Demande demande = PerseusServiceLocator.getDemandeService().read(idDemande);
            if (CSEtatDemande.VALIDE.getCodeSystem().equals(demande.getSimpleDemande().getCsEtatDemande())) {
                throw new PCFAccordeeException(
                        "La demande ne peut pas être calculée vu qu'elle est en état 'Validée' !");
            }

            // Exécution du calcul
            OutputCalcul outputCalcul = PerseusServiceLocator.getCalculPCFService().calculerPCF(idDemande);

            // Lecture de la PCFAccordée pour la demande si elle existe
            PCFAccordeeSearchModel pcfAccordeeSearchModel = new PCFAccordeeSearchModel();
            pcfAccordeeSearchModel.setForIdDemande(idDemande);
            pcfAccordeeSearchModel = search(pcfAccordeeSearchModel);
            if (pcfAccordeeSearchModel.getSize() > 1) {
                throw new PCFAccordeeException("Erreur : Il y'a plusieurs PCF Accordées pour cette demande");
            }

            // Si elle existe déjà
            if (pcfAccordeeSearchModel.getSize() == 1) {
                pcfAccordee = (PCFAccordee) pcfAccordeeSearchModel.getSearchResults()[0];
            } else {
                // Si elle existe pas encore, création de la PCF Accordée
                pcfAccordee = new PCFAccordee();
                pcfAccordee.getDemande().setId(idDemande);
            }

            pcfAccordee.setCalcul(outputCalcul);
            pcfAccordee.getSimplePCFAccordee().setDateCalcul(JadeDateUtil.getGlobazFormattedDate(new Date()));
            pcfAccordee.getSimplePCFAccordee().setExcedantRevenu(
                    outputCalcul.getDonneeString(OutputData.EXCEDANT_REVENU));
            pcfAccordee.getSimplePCFAccordee()
                    .setMontant(outputCalcul.getDonneeString(OutputData.PRESTATION_MENSUELLE));
            pcfAccordee.getSimplePCFAccordee().setCsEtat(CSEtatPcfaccordee.CALCULE.getCodeSystem());

            // Enregistrement ou création de la PCF Accordée
            if (pcfAccordeeSearchModel.getSize() == 1) {
                this.update(pcfAccordee);
            } else if (pcfAccordeeSearchModel.getSize() == 0) {
                create(pcfAccordee);
            }

            // Mettre la demande en état "Calculé"
            demande.getSimpleDemande().setCsEtatDemande(CSEtatDemande.CALCULE.getCodeSystem());
            pcfAccordee.setDemande(PerseusServiceLocator.getDemandeService().update(demande));

            if (demande.getSimpleDemande().getPermisB()) {
                creeRetenuSiTauxImpotSource(outputCalcul.getCalcul().get(OutputData.REVENUS_BRUT_IMPOT_SOURCE),
                        pcfAccordee);
            }

        } catch (CalculException e) {
            throw new PCFAccordeeException("CalculException during Calcul of PCFAccordee : " + e.getMessage());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PCFAccordeeException("Service not available : " + e.getMessage());
        } catch (DemandeException e) {
            throw new PCFAccordeeException("DemandeException during Calcul of PCFAccordee : " + e.getMessage());
        } catch (TauxException e) {
            throw new PCFAccordeeException("TauxException during Calcul of PCFAccordee : " + e.getMessage());
        } catch (RetenueException e) {
            throw new PCFAccordeeException("RetenueException during Calcul of PCFAccordee : " + e.getMessage());
        } catch (CreancierException e) {
            throw new PCFAccordeeException("CreancierException during Calcul of PCFAccordee : " + e.getMessage());
        } catch (PerseusException e) {
            throw new PCFAccordeeException("PerseusException during Calcul of PCFAccordee : " + e.getMessage());
        }

        return pcfAccordee;
    }

    @Override
    public int count(PCFAccordeeSearchModel search) throws PCFAccordeeException, JadePersistenceException {
        if (search == null) {
            throw new PCFAccordeeException("Unable to count PCFAccordee, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public PCFAccordee create(PCFAccordee pCFAccordee) throws JadePersistenceException, PCFAccordeeException {
        if (pCFAccordee == null) {
            throw new PCFAccordeeException("Unable to create PCFAccordee, the model passed is null!");
        }
        try {
            SimplePCFAccordee simplePCFAccordee = pCFAccordee.getSimplePCFAccordee();
            simplePCFAccordee.setIdDemande(pCFAccordee.getDemande().getId());
            simplePCFAccordee.setCalcul(serializeCalcul(pCFAccordee.getCalcul()));
            simplePCFAccordee = PerseusImplServiceLocator.getSimplePCFAccordeeService().create(simplePCFAccordee);

            pCFAccordee.setSimplePCFAccordee(simplePCFAccordee);
            createDetailsCalcul(pCFAccordee);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PCFAccordeeException("Service not avaiable - " + e.getMessage());
        }

        return pCFAccordee;
    }

    private void createDetailsCalcul(PCFAccordee pcfAccordee) throws PCFAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        Set<OutputData> keySet = pcfAccordee.getCalcul().getCalcul().keySet();

        for (OutputData dataType : keySet) {
            SimpleDetailsCalcul simpleDetailsCalcul = new SimpleDetailsCalcul();
            simpleDetailsCalcul.setIdPCFAccordee(pcfAccordee.getId());
            simpleDetailsCalcul.setTypeData(dataType.getCodeChamp());
            simpleDetailsCalcul.setMontant(pcfAccordee.getCalcul().getDonneeString(dataType));
            simpleDetailsCalcul = PerseusImplServiceLocator.getSimpleDetailsCalculService().create(simpleDetailsCalcul);
        }
    }

    private void creeRetenuSiTauxImpotSource(final Float revBrut, PCFAccordee pcfAccordee)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, PaiementException,
            CreancierException, PerseusException {

        BigDecimal revenuBrutAnnuel = new BigDecimal(0.00);
        BigDecimal revenuBrut = new BigDecimal(revBrut.toString());
        BigDecimal montantPCFAccordee = new BigDecimal(pcfAccordee.getSimplePCFAccordee().getMontant());
        revenuBrutAnnuel = revenuBrut.add(montantPCFAccordee.multiply(new BigDecimal(12)));

        // Float revenuBrutAnnuel = revenuBrut + (Float.parseFloat(pcfAccordee.getSimplePCFAccordee().getMontant()) *
        // 12);

        Taux taux = definirTauxImpotsSource(pcfAccordee.getDemande(), revenuBrutAnnuel);
        BigDecimal valeurTaux;
        if (null == taux) {
            valeurTaux = PCFAccordeeServiceImpl.ZERO;
        } else {
            valeurTaux = new BigDecimal(taux.getSimpleTaux().getValeurTaux());
        }

        BigDecimal montantRetenu = definirMontantRetenu(pcfAccordee, valeurTaux);

        supprimeRetenueSiExistante(pcfAccordee);
        // Si le montantRetenu est supérieur à 0 créer la retenue
        creerRetenu(pcfAccordee.getDemande().getSimpleDemande().getIdDemande(), pcfAccordee, taux, montantRetenu);

    }

    private void creerRetenu(String idDemande, PCFAccordee pcfAccordee, Taux taux, BigDecimal montantRetenu)
            throws RetenueException, JadePersistenceException, JadeApplicationServiceNotAvailableException,
            PaiementException, DemandeException, CreancierException, PerseusException {
        // Si le montantRetenu est supérieur à 0 créer la retenue
        if (montantRetenu.intValue() > 0) {
            // Insertion de la retenue
            Retenue retenue = new Retenue();
            retenue.setPcfAccordee(pcfAccordee);
            retenue.getSimpleRetenue().setMontantRetenuMensuel(montantRetenu.toString());
            retenue.getSimpleRetenue().setCsTypeRetenue(CSTypeRetenue.IMPOT_SOURCE.getCodeSystem());
            retenue.getSimpleRetenue().setDateDebutRetenue(
                    pcfAccordee.getDemande().getSimpleDemande().getDateDebut().substring(3));
            retenue.getSimpleRetenue().setTauxImposition(taux.getSimpleTaux().getValeurTaux());
            retenue.getSimpleRetenue().setBaremeIS(taux.getSimpleBareme().getNomCategorie());
            retenue.getSimpleRetenue().setIdTauxIS(taux.getSimpleTaux().getIdTaux());
            retenue = PerseusServiceLocator.getRetenueService().create(retenue);

            String date = "01." + PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();

            // Si la date de fin de la demande est avant prendre la date de fin de la demande
            if (JadeDateUtil.isDateBefore(pcfAccordee.getDemande().getSimpleDemande().getDateFin(), date)) {
                date = pcfAccordee.getDemande().getSimpleDemande().getDateFin();
            }

            Float montantAttribuable = PerseusServiceLocator.getDemandeService().calculerRetroPourCreanciers(
                    pcfAccordee.getDemande());
            if (montantAttribuable != 0) {
                montantRetenu = new BigDecimal(PerseusServiceLocator.getDemandeService()
                        .calculerMontantVerseImpotSource(pcfAccordee.getDemande()));
                montantRetenu = montantRetenu.setScale(0, BigDecimal.ROUND_HALF_EVEN);
                if (montantRetenu.intValue() < 0) {
                    montantRetenu = montantRetenu.multiply(new BigDecimal(-1));
                }

                if ((montantRetenu.intValue() < montantAttribuable) && (montantRetenu.intValue() > 0)) {
                    Creancier creancier = new Creancier();
                    creancier.getSimpleCreancier().setIdDemande(idDemande);
                    creancier.getSimpleCreancier().setMontantAccorde(montantRetenu.toString());
                    creancier.getSimpleCreancier().setMontantRevendique(montantRetenu.toString());
                    creancier.getSimpleCreancier().setCsTypeCreance(
                            CSTypeCreance.TYPE_CREANCE_IMPOT_SOURCE.getCodeSystem());
                    creancier = PerseusServiceLocator.getCreancierService().create(creancier);
                }
            }
        }
    }

    private String definirBareme(Demande demande) throws VariableMetierException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        String csTypeBareme;
        if (hasConjoint(demande) && isConjointPrisEnComptePourImpotSource(demande)) {
            csTypeBareme = CSTypeBareme.BAREME_B.getCodeSystem();
        } else {
            VariableMetierSearch varMetSearch = new VariableMetierSearch();
            varMetSearch.setForCsTypeVariableMetier(CSVariableMetier.DATE_TAUX_H_IMPOT_SOURCE.getCodeSystem());
            varMetSearch.setForDateValable(JadeStringUtil.substring(demande.getSimpleDemande().getDateDebut(), 3));
            varMetSearch.setWhereKey(VariableMetierSearch.WITH_DATE_VALABLE);

            if (0 < PerseusServiceLocator.getVariableMetierService().count(varMetSearch)) {
                csTypeBareme = CSTypeBareme.BAREME_H.getCodeSystem();
            } else {
                csTypeBareme = CSTypeBareme.BAREME_B.getCodeSystem();
            }

        }

        return csTypeBareme;
    }

    private BigDecimal definirMontantRetenu(PCFAccordee pcfAccordee, BigDecimal taux) {
        // Définir le montant retenu mensuel
        BigDecimal montantRetenu = new BigDecimal(0.0);
        taux = taux.multiply(new BigDecimal(pcfAccordee.getSimplePCFAccordee().getMontant()));
        taux = taux.divide(new BigDecimal(100));
        montantRetenu = montantRetenu.add(taux);
        // Arrondi du montant retenu
        montantRetenu = montantRetenu.setScale(0, BigDecimal.ROUND_HALF_EVEN);
        return montantRetenu;
    }

    private Taux definirTauxImpotsSource(Demande demande, BigDecimal revenuBrut) throws JadePersistenceException,
            DemandeException, JadeApplicationServiceNotAvailableException, TauxException, VariableMetierException {
        // Trouvé le nombre de personnes
        int nbPersonnes = 1;

        // On prend les enfants de plus de 18 ans
        nbPersonnes += PerseusServiceLocator.getDemandeService().getListEnfants(demande).size();

        String csTypeBareme = definirBareme(demande);

        if (CSTypeBareme.BAREME_B.getCodeSystem().equals(csTypeBareme)) {
            // prise en compte du conjoint car barème B s'applique au couple marié
            if (hasConjoint(demande) && isConjointPrisEnComptePourImpotSource(demande)) {
                nbPersonnes++;
            }
        }

        Taux taux = PerseusServiceLocator.getTauxService().getTauxImpotSource(revenuBrut, nbPersonnes,
                demande.getSimpleDemande().getDateDebut().substring(6), csTypeBareme);
        return taux;
    }

    @Override
    public PCFAccordee delete(PCFAccordee pCFAccordee) throws JadePersistenceException, PCFAccordeeException {
        if ((pCFAccordee == null) || pCFAccordee.isNew()) {
            throw new PCFAccordeeException("Unable to delete PCFAccordee, the model passed is null or new !");
        }

        try {
            // Changememnt de l'état de la demande
            pCFAccordee.getDemande().getSimpleDemande().setCsEtatDemande(CSEtatDemande.ENREGISTRE.getCodeSystem());
            pCFAccordee.getDemande().setSimpleDemande(
                    PerseusImplServiceLocator.getSimpleDemandeService().update(
                            pCFAccordee.getDemande().getSimpleDemande()));
            SimplePCFAccordee simplePCFAccordee = PerseusImplServiceLocator.getSimplePCFAccordeeService().delete(
                    pCFAccordee.getSimplePCFAccordee());
            pCFAccordee.setSimplePCFAccordee(simplePCFAccordee);

            // Si la pcf a été supprimée
            if (simplePCFAccordee.isNew()) {
                PerseusServiceLocator.getRetenueService().deleteForPCFAccordee(pCFAccordee.getId());

            }

            deleteDetailsCalcul(pCFAccordee);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PCFAccordeeException("Service not avaiable - " + e.toString());
        } catch (DemandeException e) {
            throw new PCFAccordeeException("Demande exception : " + e.toString());
        } catch (RetenueException e) {
            throw new PCFAccordeeException("Retenue exception : " + e.toString());
        }

        return pCFAccordee;
    }

    private void deleteDetailsCalcul(PCFAccordee pcfAccordee) throws PCFAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleDetailsCalculSearchModel searchModel = new SimpleDetailsCalculSearchModel();
        searchModel.setForIdPCFAccordee(pcfAccordee.getId());
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        PerseusImplServiceLocator.getSimpleDetailsCalculService().delete(searchModel);
    }

    @Override
    public PCFAccordee deleteNoUpdateEtatDemande(PCFAccordee pCFAccordee) throws JadePersistenceException,
            PCFAccordeeException {
        if ((pCFAccordee == null) || pCFAccordee.isNew()) {
            throw new PCFAccordeeException("Unable to delete PCFAccordee, the model passed is null or new !");
        }

        try {
            SimplePCFAccordee simplePCFAccordee = PerseusImplServiceLocator.getSimplePCFAccordeeService().delete(
                    pCFAccordee.getSimplePCFAccordee());
            pCFAccordee.setSimplePCFAccordee(simplePCFAccordee);

            // Si la pcf a été supprimée
            if (simplePCFAccordee.isNew()) {
                PerseusServiceLocator.getRetenueService().deleteForPCFAccordee(pCFAccordee.getId());

            }

            deleteDetailsCalcul(pCFAccordee);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PCFAccordeeException("Service not avaiable - " + e.toString());
        } catch (RetenueException e) {
            throw new PCFAccordeeException("Retenue exception : " + e.toString());
        }

        return pCFAccordee;
    }

    @Override
    public OutputCalcul deserializeCalcul(byte[] outputCalcul) {
        XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(outputCalcul));
        @SuppressWarnings("unchecked")
        Hashtable<String, String> calculXml = (Hashtable<String, String>) decoder.readObject();
        OutputCalcul outputCalculObject = new OutputCalcul();
        outputCalculObject.setCalculXml(calculXml);

        return outputCalculObject;
    }

    private boolean hasConjoint(Demande demande) {
        boolean hasConjoint = false;
        if (!JadeStringUtil.isEmpty(demande.getSituationFamiliale().getConjoint().getId())) {
            hasConjoint = true;
        }
        return hasConjoint;
    }

    private boolean isConjointPrisEnComptePourImpotSource(Demande demande) {
        boolean isConjointPrisEnCompte = false;
        String csTypeConjoint = demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsTypeConjoint();
        if (CSTypeConjoint.CONJOINT.getCodeSystem().equals(csTypeConjoint)
                || CSTypeConjoint.PARTENAIRE_ENREGISTRE.getCodeSystem().equals(csTypeConjoint)) {
            isConjointPrisEnCompte = true;
        }
        return isConjointPrisEnCompte;
    }

    @Override
    public PCFAccordee read(String idPCFAccordee) throws JadePersistenceException, PCFAccordeeException {
        if (JadeStringUtil.isEmpty(idPCFAccordee)) {
            throw new PCFAccordeeException("Unable to read PCFAccordee, the id passed is null!");
        }
        PCFAccordee pcfAccordee = new PCFAccordee();
        pcfAccordee.setId(idPCFAccordee);
        pcfAccordee = (PCFAccordee) JadePersistenceManager.read(pcfAccordee);
        pcfAccordee.setCalcul(deserializeCalcul(pcfAccordee.getSimplePCFAccordee().getCalcul()));
        return pcfAccordee;
    }

    @Override
    public PCFAccordee readForDemande(String idDemande) throws JadePersistenceException, PCFAccordeeException {
        if (JadeStringUtil.isEmpty(idDemande)) {
            throw new PCFAccordeeException("Unable to read PCFAccordee, the idDemande passed is empty !");
        }
        PCFAccordeeSearchModel searchModel = new PCFAccordeeSearchModel();
        searchModel.setForIdDemande(idDemande);

        // searchModel = (PCFAccordeeSearchModel) JadePersistenceManager.search(searchModel, true);
        searchModel = (PCFAccordeeSearchModel) JadePersistenceManager.search(searchModel);
        if (searchModel.getSize() == 1) {
            return read(((PCFAccordee) searchModel.getSearchResults()[0]).getId());
            // return (PCFAccordee) searchModel.getSearchResults()[0];
        } else {
            return null;
        }
    }

    @Override
    public PCFAccordeeSearchModel search(PCFAccordeeSearchModel searchModel) throws JadePersistenceException,
            PCFAccordeeException {
        if (searchModel == null) {
            throw new PCFAccordeeException("Unable to search PCFAccordee, the search model passed is null!");
        }
        return (PCFAccordeeSearchModel) JadePersistenceManager.search(searchModel);

    }

    @Override
    public PCFAccordeeForDetailsCalculSearchModel searchWithBlobs(PCFAccordeeForDetailsCalculSearchModel searchModel)
            throws JadePersistenceException, PCFAccordeeException {
        if (searchModel == null) {
            throw new PCFAccordeeException(
                    "Unable to search PCFAccordeeForDetailsCalcul, the search model passed is null!");
        }

        searchModel = (PCFAccordeeForDetailsCalculSearchModel) JadePersistenceManager.search(searchModel, true);

        for (int i = 0; i < searchModel.getSize(); i++) {
            PCFAccordeeForDetailsCalcul pcfa = (PCFAccordeeForDetailsCalcul) searchModel.getSearchResults()[i];

            pcfa.setCalcul(deserializeCalcul(pcfa.getSimplePCFAccordee().getCalcul()));

            searchModel.getSearchResults()[i] = pcfa;
        }

        return searchModel;
    }

    @Override
    public PCFAccordeeSearchModel searchWithBlobs(PCFAccordeeSearchModel searchModel) throws JadePersistenceException,
            PCFAccordeeException {
        if (searchModel == null) {
            throw new PCFAccordeeException("Unable to search PCFAccordee, the search model passed is null!");
        }

        searchModel = (PCFAccordeeSearchModel) JadePersistenceManager.search(searchModel, true);

        for (int i = 0; i < searchModel.getSize(); i++) {
            PCFAccordee pcfa = (PCFAccordee) searchModel.getSearchResults()[i];

            pcfa.setCalcul(deserializeCalcul(pcfa.getSimplePCFAccordee().getCalcul()));

            searchModel.getSearchResults()[i] = pcfa;
        }

        return searchModel;
    }

    private byte[] serializeCalcul(OutputCalcul outputCalcul) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(out);

        encoder.writeObject(outputCalcul.getCalculXml());
        encoder.close();

        return out.toString().getBytes();
    }

    private void supprimeRetenueSiExistante(PCFAccordee pcfAccordee) throws RetenueException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        // Supprimer la retenue si il y'en a déjà une
        SimpleRetenueSearchModel deleteModel = new SimpleRetenueSearchModel();
        deleteModel.setForIdPcfAccordee(pcfAccordee.getId());
        deleteModel.setForCsTypeRetenue(CSTypeRetenue.IMPOT_SOURCE.getCodeSystem());
        PerseusImplServiceLocator.getSimpleRetenueService().delete(deleteModel);
    }

    @Override
    public PCFAccordee update(PCFAccordee pCFAccordee) throws JadePersistenceException, PCFAccordeeException {
        if ((pCFAccordee == null) || pCFAccordee.isNew()) {
            throw new PCFAccordeeException("Unable to update PCFAccordee, the model passed is null or new !");
        }

        try {
            SimplePCFAccordee simplePCFAccordee = pCFAccordee.getSimplePCFAccordee();
            simplePCFAccordee.setCalcul(serializeCalcul(pCFAccordee.getCalcul()));
            pCFAccordee.setSimplePCFAccordee(PerseusImplServiceLocator.getSimplePCFAccordeeService().update(
                    simplePCFAccordee));

            updateDetailsCalcul(pCFAccordee);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PCFAccordeeException("Service not avaiable - " + e.getMessage());
        }

        return pCFAccordee;
    }

    @Override
    public PCFAccordee updateWithoutCalcul(PCFAccordee pCFAccordee) throws JadePersistenceException,
            PCFAccordeeException {
        if ((pCFAccordee == null) || pCFAccordee.isNew()) {
            throw new PCFAccordeeException("Unable to update PCFAccordee, the model passed is null or new !");
        }

        try {
            SimplePCFAccordee simplePCFAccordee = pCFAccordee.getSimplePCFAccordee();
            simplePCFAccordee.setCalcul(serializeCalcul(pCFAccordee.getCalcul()));
            pCFAccordee.setSimplePCFAccordee(PerseusImplServiceLocator.getSimplePCFAccordeeService().update(
                    simplePCFAccordee));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PCFAccordeeException("Service not avaiable - " + e.getMessage());
        }

        return pCFAccordee;
    }

    /*
     * Utilisé uniquement dans le cadre de la validation d'une décision.
     */
    @Override
    public PCFAccordee updateValidationDecision(PCFAccordee pCFAccordee) throws JadePersistenceException,
            PCFAccordeeException {
        if ((pCFAccordee == null) || pCFAccordee.isNew()) {
            throw new PCFAccordeeException("Unable to update PCFAccordee, the model passed is null or new !");
        }

        try {
            SimplePCFAccordee simplePCFAccordee = pCFAccordee.getSimplePCFAccordee();
            pCFAccordee.setSimplePCFAccordee(PerseusImplServiceLocator.getSimplePCFAccordeeService().update(
                    simplePCFAccordee));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PCFAccordeeException("Service not avaiable - " + e.getMessage());
        }

        return pCFAccordee;
    }

    @Override
    public PCFAccordee update(PCFAccordee pCFAccordee, String mesureEncouragement, String mesureChargesLoyer,
            String mesureCoaching) throws JadePersistenceException, PCFAccordeeException {
        if ((pCFAccordee == null) || pCFAccordee.isNew()) {
            throw new PCFAccordeeException("Unable to update PCFAccordee, the model passed is null or new !");
        }

        // BZ 8003 Enlever le separateur de millier
        Float fMesureEncouragement = this.roundFloat(PRStringUtils.replaceString(mesureEncouragement, "'", ""));
        Float fMesureChargesLoyer = this.roundFloat(PRStringUtils.replaceString(mesureChargesLoyer, "'", ""));
        Float fMesureCoaching = this.roundFloat(PRStringUtils.replaceString(mesureCoaching, "'", ""));

        // Mettre la montant d'encouragement dans le calcul
        pCFAccordee.getCalcul().addDonnee(OutputData.MESURE_ENCOURAGEMENT, fMesureEncouragement);
        // Mettre la montant d'encouragement pour le loyer dans le calcul
        pCFAccordee.getCalcul().addDonnee(OutputData.MESURE_CHARGES_LOYER, fMesureChargesLoyer);
        // Mettre le montant de la mesure de caoching dans le calcul
        pCFAccordee.getCalcul().addDonnee(OutputData.MESURE_COACHING, fMesureCoaching);

        Float totalMesure = fMesureEncouragement + fMesureChargesLoyer + fMesureCoaching;

        // Répartir la mesure
        Float excedantRevenu = pCFAccordee.getCalcul().getDonnee(OutputData.EXCEDANT_REVENU);
        Float montantMensuel = pCFAccordee.getCalcul().getDonnee(OutputData.PRESTATION_MENSUELLE);

        excedantRevenu -= totalMesure * 12;

        if (excedantRevenu < 0) {
            montantMensuel -= excedantRevenu / 12;
            Double pmFloored = Math.floor(montantMensuel);
            if (montantMensuel - pmFloored > 0) {
                montantMensuel = new Float(pmFloored + 1);
            }
            excedantRevenu = new Float(0);
        }

        // Prestation minimum
        Float prestationMinimum = new Float(0);
        try {
            VariableMetier vm = PerseusServiceLocator.getVariableMetierService().getFromCS(
                    CSVariableMetier.MONTANT_MINIMAL_PCF.getCodeSystem(),
                    JadeDateUtil.getGlobazDate(pCFAccordee.getDemande().getSimpleDemande().getDateDebut()));
            prestationMinimum = vm.getMontant();
        } catch (VariableMetierException e) {
            throw new PCFAccordeeException("VariableMetierException pendant la définition de la prestation minimum");
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PCFAccordeeException(
                    "JadeApplicationServiceNotAvailableException pendant la définition de la prestation minimum");
        }
        // Si le montant mensuel est plus petit que le montant minimum de la prestation, arrondir
        if ((montantMensuel > 0) && (montantMensuel < prestationMinimum)) {
            montantMensuel = prestationMinimum;
        }

        pCFAccordee.getSimplePCFAccordee().setExcedantRevenu(excedantRevenu.toString());
        pCFAccordee.getSimplePCFAccordee().setMontant(montantMensuel.toString());

        if (pCFAccordee.getDemande().getSimpleDemande().getPermisB()) {
            try {

                creeRetenuSiTauxImpotSource(pCFAccordee.getCalcul().getDonnee(OutputData.REVENUS_BRUT_IMPOT_SOURCE),
                        pCFAccordee);

            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new PCFAccordeeException(
                        "JadeApplicationServiceNotAvailableException pendant le calcul de l'impôt à la source");

            } catch (DemandeException e) {
                throw new PCFAccordeeException("DemandeException pendant l'update de l'impôt à la source");
            } catch (RetenueException e) {
                throw new PCFAccordeeException("RetenueException pendant l'update de l'impôt à la source");
            } catch (TauxException e) {
                throw new PCFAccordeeException("TauxException pendant l'update de l'impôt à la source");
            } catch (PaiementException e) {
                throw new PCFAccordeeException("PaiementException pendant l'update de l'impôt à la source");
            } catch (CreancierException e) {
                throw new PCFAccordeeException("CreancierException pendant l'update de l'impôt à la source : "
                        + e.getMessage());
            } catch (PerseusException e) {
                throw new PCFAccordeeException("PerseusException pendant l'update de l'impôt à la source : "
                        + e.getMessage());
            }
        }

        return this.update(pCFAccordee);

    }

    private void updateDetailsCalcul(PCFAccordee pcfAccordee) throws PCFAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        Set<OutputData> keySet = pcfAccordee.getCalcul().getCalcul().keySet();

        for (OutputData dataType : keySet) {
            SimpleDetailsCalculSearchModel searchModel = new SimpleDetailsCalculSearchModel();
            searchModel.setForIdPCFAccordee(pcfAccordee.getId());
            searchModel.setForTypeData(dataType.getCodeChamp());
            searchModel = PerseusImplServiceLocator.getSimpleDetailsCalculService().search(searchModel);

            if (searchModel.getSearchResults().length > 0) {
                SimpleDetailsCalcul simpleDetailsCalcul = (SimpleDetailsCalcul) searchModel.getSearchResults()[0];
                simpleDetailsCalcul.setIdPCFAccordee(pcfAccordee.getId());
                simpleDetailsCalcul.setTypeData(dataType.getCodeChamp());
                simpleDetailsCalcul.setMontant(pcfAccordee.getCalcul().getDonneeString(dataType));
                simpleDetailsCalcul = PerseusImplServiceLocator.getSimpleDetailsCalculService().update(
                        simpleDetailsCalcul);
            } else {
                SimpleDetailsCalcul simpleDetailsCalcul = new SimpleDetailsCalcul();
                simpleDetailsCalcul.setIdPCFAccordee(pcfAccordee.getId());
                simpleDetailsCalcul.setTypeData(dataType.getCodeChamp());
                simpleDetailsCalcul.setMontant(pcfAccordee.getCalcul().getDonneeString(dataType));
                simpleDetailsCalcul = PerseusImplServiceLocator.getSimpleDetailsCalculService().create(
                        simpleDetailsCalcul);
            }
        }
    }

}
