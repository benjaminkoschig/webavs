package ch.globaz.perseus.businessimpl.checkers.rentepont;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.constantes.CSEtatRentePont;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.qd.SimpleFacture;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.models.rentepont.RentePontSearchModel;
import ch.globaz.perseus.business.models.rentepont.SimpleRentePont;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class SimpleRentePontChecker extends PerseusAbstractChecker {

    /**
     * @param rentePont
     * @throws JadePersistenceException
     * @throws RentePontException
     * @throws Exception
     */
    public static void checkForCreate(SimpleRentePont rentePont) throws RentePontException, JadePersistenceException {
        SimpleRentePontChecker.checkMandatory(rentePont);
        if (!PerseusAbstractChecker.threadOnError()) {
            SimpleRentePontChecker.checkIntegrity(rentePont);
        }
    }

    /**
     * @param rentePont
     * @throws RentePontException
     *             , JadePersistenceException
     */
    public static void checkForDelete(SimpleRentePont rentePont) throws RentePontException, JadePersistenceException {

    }

    /**
     * @param rentePont
     * @throws JadePersistenceException
     * @throws RentePontException
     * @throws Exception
     */
    public static void checkForUpdate(SimpleRentePont rentePont) throws RentePontException, JadePersistenceException {
        SimpleRentePontChecker.checkMandatory(rentePont);
        if (!PerseusAbstractChecker.threadOnError()) {
            SimpleRentePontChecker.checkIntegrity(rentePont);
        }
    }

    /**
     * @param rentePont
     * @throws JadePersistenceException
     * @throws RentePontException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkIntegrity(SimpleRentePont rentePont) throws RentePontException, JadePersistenceException {
        RentePontSearchModel searchModel = new RentePontSearchModel();
        searchModel.setForIdDossier(rentePont.getIdDossier());
        try {

            if (!JadeStringUtil.isEmpty(rentePont.getDateDepot())
                    && !JadeDateUtil.isGlobazDate(rentePont.getDateDepot())) {

                JadeThread.logError(SimpleFacture.class.getName(), "perseus.rentePont.date.depot");

            }

            if (!JadeStringUtil.isEmpty(rentePont.getDateDebut())
                    && !JadeDateUtil.isGlobazDate(rentePont.getDateDebut())) {

                JadeThread.logError(SimpleFacture.class.getName(), "perseus.rentePont.date.debut");

            }

            if (!JadeStringUtil.isEmpty(rentePont.getDateFin()) && !JadeDateUtil.isGlobazDate(rentePont.getDateFin())) {

                JadeThread.logError(SimpleFacture.class.getName(), "perseus.rentePont.date.fin");

            }

            searchModel = PerseusServiceLocator.getRentePontService().search(searchModel);
            // Si on a deja des demandes de rente pont pour ce dossier
            if (searchModel.getSize() > 0) {
                for (JadeAbstractModel abstractModel : searchModel.getSearchResults()) {
                    RentePont rp = (RentePont) abstractModel;
                    // Et qu'on est pas en train de mettre à jour cette demande
                    if (!rp.getId().equals(rentePont.getId())) {

                        if (JadeStringUtil.isEmpty(rentePont.getDateFin())) {
                            if (JadeStringUtil.isEmpty(rp.getSimpleRentePont().getDateFin())) {
                                JadeThread.logError(SimpleRentePontChecker.class.getName(),
                                        "perseus.rentePont.rentePont.doubleDemande.integrity");
                            } else {
                                if (!JadeDateUtil.isDateAfter(rentePont.getDateDebut(), rp.getSimpleRentePont()
                                        .getDateFin())) {
                                    JadeThread.logError(SimpleRentePontChecker.class.getName(),
                                            "perseus.rentePont.rentePont.doubleDemandeChevauchement.integrity");
                                }
                            }
                        } else {
                            if (JadeStringUtil.isEmpty(rp.getSimpleRentePont().getDateFin())) {
                                if (!JadeDateUtil.isDateBefore(rentePont.getDateFin(), rp.getSimpleRentePont()
                                        .getDateDebut())) {
                                    JadeThread.logError(SimpleRentePontChecker.class.getName(),
                                            "perseus.rentePont.rentePont.doubleDemandeChevauchement.integrity");
                                }
                            } else {

                                boolean isPeriodePrecedente = false;

                                if (JadeDateUtil.isDateBefore(rentePont.getDateDebut(), rp.getSimpleRentePont()
                                        .getDateDebut())) {
                                    isPeriodePrecedente = true;
                                }

                                if (isPeriodePrecedente) {
                                    if (!JadeDateUtil.isDateBefore(rentePont.getDateFin(), rp.getSimpleRentePont()
                                            .getDateDebut())) {
                                        JadeThread.logError(SimpleRentePontChecker.class.getName(),
                                                "perseus.rentePont.rentePont.doubleDemandeChevauchement.integrity");
                                    }
                                } else {
                                    if (!JadeDateUtil.isDateAfter(rentePont.getDateDebut(), rp.getSimpleRentePont()
                                            .getDateFin())) {
                                        JadeThread.logError(SimpleRentePontChecker.class.getName(),
                                                "perseus.rentePont.rentePont.doubleDemandeChevauchement.integrity");
                                    }
                                }
                            }
                        }
                    }
                }
                // BZ 8084, ne plus pouvoir fermer une demande avant le dernier pmt mensuel
                // Si la demande est purement retroactive, elle doit pouvoir être fermée avant
                if (CSEtatRentePont.VALIDE.getCodeSystem().equals(rentePont.getCsEtat())) {

                    String dateDernierPaiement = PerseusServiceLocator.getPmtMensuelRentePontService()
                            .getDateDernierPmt();
                    String dateProchainPaiement = PerseusServiceLocator.getPmtMensuelRentePontService()
                            .getDateProchainPmt();
                    // Comparaison avec la date de décision et la date du prochain paiement mensuel pour déterminer si
                    // la demande est rétroactive
                    if (!dateProchainPaiement.equals(rentePont.getDateDecision())) {
                        if (!JadeStringUtil.isEmpty(rentePont.getDateFin())
                                && JadeDateUtil.isDateMonthYearBefore(rentePont.getDateFin().substring(3),
                                        dateDernierPaiement)) {
                            String[] param = new String[1];
                            param[0] = dateDernierPaiement;
                            JadeThread.logError(SimpleRentePontChecker.class.getName(),
                                    "perseus.demande.update.valide.paiementdejaeffectue", param);

                        }
                    }

                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            e.printStackTrace();
        } catch (PaiementException e) {
            throw new RentePontException("Exception during Rente-pont validation : " + e.toString(), e);
        }
    }

    /**
     * @param rentePont
     */
    private static void checkMandatory(SimpleRentePont rentePont) {
        if (JadeStringUtil.isEmpty(rentePont.getIdTiersAdressePaiement())) {
            JadeThread.logError(SimpleRentePontChecker.class.getName(),
                    "perseus.rentePont.rentePont.idTiersAdressePaiement.mandatory");
        }
        if (JadeStringUtil.isEmpty(rentePont.getIdDossier())) {
            JadeThread.logError(SimpleRentePontChecker.class.getName(),
                    "perseus.rentePont.rentePont.idDossier.mandatory");
        }
        if (JadeStringUtil.isEmpty(rentePont.getDateDepot())) {
            JadeThread.logError(SimpleRentePontChecker.class.getName(),
                    "perseus.rentepont.rentePont.dateDepot.mandatory");
        }
        if (JadeStringUtil.isEmpty(rentePont.getDateDebut())) {
            JadeThread.logError(SimpleRentePontChecker.class.getName(),
                    "perseus.rentepont.rentePont.dateDebut.mandatory");
        }
        if (JadeStringUtil.isEmpty(rentePont.getIdGestionnaire())) {
            JadeThread.logError(SimpleRentePontChecker.class.getName(),
                    "perseus.rentepont.rentePont.gestionnaire.mandatory");
        }
        if (JadeStringUtil.isEmpty(rentePont.getCsCaisse())) {
            JadeThread.logError(SimpleRentePontChecker.class.getName(), "perseus.rentepont.rentePont.caisse.mandatory");
        }
        // if (JadeStringUtil.isEmpty(rentePont.getCsEtat())) {
        // JadeThread.logError(SimpleRentePontChecker.class.getName(), "perseus.rentepont.rentePont.etat.mandatory");
        // }
        if (JadeStringUtil.isEmpty(rentePont.getMontant())) {
            JadeThread
                    .logError(SimpleRentePontChecker.class.getName(), "perseus.rentepont.rentePont.montant.mandatory");
        }

    }
}
