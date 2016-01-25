package ch.globaz.pegasus.process.adaptation.stepRenteReception;

import globaz.globall.db.BSessionUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import ch.globaz.pegasus.business.constantes.EPCRenteAdaptation;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import ch.globaz.pegasus.business.exceptions.models.process.RenteAdapationDemandeException;
import ch.globaz.pegasus.business.models.process.adaptation.RenteAdapationDemande;
import ch.globaz.pegasus.business.models.process.adaptation.SimpleRenteAdaptation;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.process.adaptation.PCProcessDroitUpdateAbsract;

public class PCProcessAdaptationEntityHandler extends PCProcessDroitUpdateAbsract {
    private Map<String, List<IHEOutputAnnonce>> mapAnnonceRentes = null;
    private Map<String, List<RenteAdapationDemande>> rentes;
    private Map<String, String> typeRente = null;

    public PCProcessAdaptationEntityHandler(Map<String, List<RenteAdapationDemande>> rentes,
            Map<String, List<IHEOutputAnnonce>> mapAnnonceRentes, Map<String, String> typesRente) {
        this.rentes = rentes;
        this.mapAnnonceRentes = mapAnnonceRentes;
        typeRente = typesRente;
    }

    private void checkRenteDemande(RenteAdapationDemande rente, SimpleRenteAdaptation annonceRente)
            throws JadeNoBusinessLogSessionError, AdaptationException {

        if (annonceRente != null) {

            if (EPCRenteAdaptation.SYNCHRONIZE.equals(annonceRente.getEtat())) {
                //
                if (!rente.getSimpleRenteAdaptation().getGenre().equals(annonceRente.getGenre())) {
                    String[] param = new String[1];
                    param[0] = BSessionUtil.getSessionFromThreadContext().getCode(annonceRente.getGenre());
                    param[1] = BSessionUtil.getSessionFromThreadContext().getCode(
                            rente.getSimpleRenteAdaptation().getGenre());
                    param[2] = rente.getSimpleDemandeCentrale().getNss();
                    JadeThread.logWarn(this.getClass().getName(),
                            "pegasus.process.adaptaion.reception.genreRenteChange", param);
                }

                if (!annonceRente.getCsTypdDonneeFinacire().equals(
                        rente.getSimpleRenteAdaptation().getCsTypdDonneeFinacire())) {
                    throw new AdaptationException("Type of donnefinancier not match with this nss: "
                            + rente.getSimpleDemandeCentrale().getNss() + ". new type: "
                            + annonceRente.getCsTypdDonneeFinacire() + " - old type computed: "
                            + rente.getSimpleRenteAdaptation().getCsTypdDonneeFinacire());
                }

                if (JadeNumericUtil.isEmptyOrZero(annonceRente.getNouveauMontant())) {
                    String[] param = new String[1];
                    param[0] = rente.getSimpleDemandeCentrale().getNss();
                    JadeThread.logWarn(this.getClass().getName(),
                            "pegasus.process.adaptaion.reception.aucunNouveauMontantTrouve", param);
                }

                if (!JadeNumericUtil.isEmptyOrZero(annonceRente.getObservation())) {
                    String[] param = new String[2];
                    param[0] = annonceRente.getObservation();
                    param[1] = rente.getSimpleDemandeCentrale().getNss();
                    JadeThread.logWarn(this.getClass().getName(),
                            "pegasus.process.adaptaion.reception.observateionCentrale", param);
                }

                if (JadeNumericUtil.isEmptyOrZero(annonceRente.getAncienMontant())) {
                    String[] param = new String[2];
                    param[0] = rente.getSimpleDemandeCentrale().getNss();
                    JadeThread.logWarn(this.getClass().getName(),
                            "pegasus.process.adaptaion.reception.aucunAncienMontantTrouve", param);

                } else {
                    BigDecimal ancienMontantCentrale = (new BigDecimal(annonceRente.getAncienMontant()).setScale(0,RoundingMode.HALF_DOWN));
                    BigDecimal ancienMontantSysteme = (new BigDecimal(rente.getSimpleRenteAdaptation()
                            .getAncienMontant()).setScale(0,RoundingMode.HALF_DOWN));
                    if (!ancienMontantCentrale.equals(ancienMontantSysteme)) {
                        String[] param = new String[4];
                        param[0] = ancienMontantSysteme.toString();
                        param[1] = ancienMontantCentrale.toString();
                        param[2] = rente.getSimpleDemandeCentrale().getNss();
                        param[3] = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                                rente.getSimpleRenteAdaptation().getGenre());
                        JadeThread.logWarn(this.getClass().getName(),
                                "pegasus.process.adaptaion.reception.ancienMontantNotSame", param);

                    }
                }
            }
        }

    }

    private SimpleRenteAdaptation convertAnnonce(IHEOutputAnnonce annonce) throws AdaptationException {

        SimpleRenteAdaptation renteAdaptation = new SimpleRenteAdaptation();
        globaz.webavs.common.CommonNSSFormater formater = new globaz.webavs.common.CommonNSSFormater();
        try {
            renteAdaptation.setNss(formater.format(annonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT)));
            renteAdaptation.setGenre(annonce.getField(IHEAnnoncesViewBean.PC_GENRE_DE_PRESTATION));
            renteAdaptation.setNouveauMontant(annonce
                    .getField(IHEAnnoncesViewBean.CS_MENSUALITE_RENTE_ORDINAIRE_REMPLACEE_FRANCS));
            renteAdaptation.setGenre(annonce.getField(IHEAnnoncesViewBean.PC_GENRE_DE_PRESTATION));

            renteAdaptation.setDebutDroit(mmyyToDdmmYyyy(annonce.getField(IHEAnnoncesViewBean.CS_DEBUT_DU_DROIT_MMAA)));

            renteAdaptation.setDateRapport(mmyyToDdmmYyyy(annonce.getField(IHEAnnoncesViewBean.CS_MOIS_DU_RAPPORT)));

            renteAdaptation.setCodeRetour(annonce.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT));
            renteAdaptation.setDegreInvalidite(annonce.getField(IHEAnnoncesViewBean.CS_DEGREINVALIDITE_AYANT_DROIT));
            renteAdaptation.setObservation(annonce.getField(IHEAnnoncesViewBean.OBSERVATION_1_CENTRALE));
            renteAdaptation.setAncienMontant(annonce.getField(IHEAnnoncesViewBean.CS_MENSUALITE_PRESTATION_FRANCS));
            if ("0".equals(renteAdaptation.getCodeRetour())) {
                renteAdaptation.setCsTypdDonneeFinacire(mapTypeDonneeFinanciere(renteAdaptation.getGenre()));
            }
            if (!JadeStringUtil.isBlankOrZero(renteAdaptation.getGenre())) {
                renteAdaptation.setGenre(typeRente.get(renteAdaptation.getGenre()));
                if (JadeStringUtil.isEmpty(renteAdaptation.getGenre())) {
                    throw new AdaptationException("Unable to map this type of rente: "
                            + annonce.getField(IHEAnnoncesViewBean.PC_GENRE_DE_PRESTATION));
                }
            }
        } catch (Exception e) {
            throw new AdaptationException("Annonce exception error durring the maping", e);
        }
        return renteAdaptation;
    }

    /**
     * Convertie la date dans le format attendue par la persistence
     * Si la date est vide on retourne null car la seul date utilis est date de rapport qui est vérifié plus tard
     * On fait cela pour ne pas avoir des erreures en plus
     * 
     * @param date
     * @return
     */
    protected String mmyyToDdmmYyyy(String date) {
        if (JadeStringUtil.isBlankOrZero(date)) {
            return null;
        }
        return "01." + date.substring(0, 2) + ".20" + date.substring(2, 4);
    }

    private void checkIsEmpty(String date, String messageTypeDate) {
        if (JadeStringUtil.isBlankOrZero(date)) {
            throw new IllegalArgumentException(messageTypeDate);
        }
    }

    private Date parseDate(String date) throws ParseException {
        String format = "";

        if (date.length() == 10) {
            format = "dd.MM.yyyy";
        } else if (date.length() == 7) {
            format = "MM.yyyy";
        } else {
            throw new IllegalArgumentException("The date:" + date + " is not a valid format");
        }

        Date newDate = new SimpleDateFormat(format).parse(date);
        return newDate;
    }

    private int compareTo(String beforeDate, String afterDate) throws ParseException {
        checkIsEmpty(beforeDate, "La date de rapport ne peut être vide quand il y plusieurs rente");
        checkIsEmpty(afterDate, "La date de rapport ne peut être vide quand il y plusieurs rente");
        Date theBeforeDate = parseDate(beforeDate);
        Date theAfterDate = parseDate(afterDate);
        return theBeforeDate.compareTo(theAfterDate);
    }

    /**
     * Le but est de filtrer les rentes qui sont à double, pour cela on prend l'annonce qui à la date de rapport le plus
     * récente
     * 
     * @param listeAnnonces
     * @return
     * @throws AdaptationException
     */
    protected List<SimpleRenteAdaptation> filtreAnnonces(List<SimpleRenteAdaptation> listeAnnonces)
            throws AdaptationException {

        if (listeAnnonces.size() == 1) {
            // on ne veut pas filtrer comme il y une seule annonce, permet d'éviter d'avoir certaines erreurs
            return listeAnnonces;
        } else {
            Map<String, SimpleRenteAdaptation> mapAnnoncesFiltrer = new HashMap<String, SimpleRenteAdaptation>();
            List<SimpleRenteAdaptation> annoncesFiltrees = new ArrayList<SimpleRenteAdaptation>();

            Map<String, List<SimpleRenteAdaptation>> mapAnnonceRente = JadeListUtil.groupBy(listeAnnonces,
                    new JadeListUtil.Key<SimpleRenteAdaptation>() {
                        @Override
                        public String exec(SimpleRenteAdaptation rente) {
                            return rente.getGenre();
                        }
                    });

            // on filtre seulement sur les rentes à doubles.
            for (Entry<String, List<SimpleRenteAdaptation>> entry : mapAnnonceRente.entrySet()) {

                if (entry.getValue().size() > 1) {
                    // On fait order by desc
                    Collections.sort(entry.getValue(), new Comparator<SimpleRenteAdaptation>() {
                        @Override
                        public int compare(SimpleRenteAdaptation o1, SimpleRenteAdaptation o2) {
                            try {
                                return compareTo(o1.getDateRapport(), o2.getDateRapport()) * -1;
                            } catch (Exception e) {
                                throw new IllegalArgumentException("Impossible de filtrer la rente ", e);
                            }
                        }
                    });

                    for (SimpleRenteAdaptation an : entry.getValue()) {
                        if (!mapAnnoncesFiltrer.containsKey(an.getGenre())) {
                            mapAnnoncesFiltrer.put(an.getGenre(), an);
                            annoncesFiltrees.add(an);
                        } else {
                            // JadeThread.logInfo("", "Annonce filtrée: " + an.toString());
                        }
                    }
                } else if (entry.getValue().size() == 1) {
                    annoncesFiltrees.add(entry.getValue().get(0));
                } else {
                    throw new AdaptationException("Aaucune rente n'est définit dans la liste");
                }
            }
            return annoncesFiltrees;
        }

    }

    private String mapTypeDonneeFinanciere(String genre) throws JadeNoBusinessLogSessionError, AdaptationException {
        if (JadeStringUtil.isBlankOrZero(genre)) {
            throw new AdaptationException("Unable to findTypeDonneeFianciere, genre passed is null!");
        }
        Integer genreDeRente = Integer.valueOf(genre);
        String csTypeDonneFinanciere;
        // Rente AVS
        if ((genreDeRente >= 10) && (genreDeRente <= 76)) {
            csTypeDonneFinanciere = IPCDroits.CS_RENTE_AVS_AI;
        } else
        // Rente API
        if ((genreDeRente >= 81) && (genreDeRente <= 97)) {
            csTypeDonneFinanciere = IPCDroits.CS_RENTE_API;
        } else {
            throw new AdaptationException("Unable to find the csTypeDonneeFinanciere");
        }
        return csTypeDonneFinanciere;
    }

    // permet d'identifier l'annonce car on peut avoir 3 réponses pour une Annonce (rente avs/ai(2max) et API)
    private SimpleRenteAdaptation matchedAnnonce(RenteAdapationDemande rente,
            List<SimpleRenteAdaptation> listRenteAnnonce) throws AdaptationException {

        for (SimpleRenteAdaptation annonce : listRenteAnnonce) {
            if (annonce.getCsTypdDonneeFinacire().equals(rente.getSimpleRenteAdaptation().getCsTypdDonneeFinacire())) {
                if (listRenteAnnonce.size() > 2) {
                    if (BSessionUtil.getSessionFromThreadContext().getCode(rente.getSimpleRenteAdaptation().getGenre())
                            .trim().equals(annonce.getGenre())) {
                        return annonce;
                    }
                } else {
                    return annonce;
                }
            }
        }

        return null;
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        List<RenteAdapationDemande> listeRenteFamille = rentes.get(getEntity().getIdRef());

        // On regroupe les rente par l'id droitMembre famille (referenceInterne)
        Map<String, List<RenteAdapationDemande>> mapRenteAdaptation = JadeListUtil.groupBy(listeRenteFamille,
                new JadeListUtil.Key<RenteAdapationDemande>() {
                    @Override
                    public String exec(RenteAdapationDemande e) {
                        return e.getSimpleDemandeCentrale().getReferenceInterne();
                    }
                });

        if (listeRenteFamille == null) {
            throw new AdaptationException("Aucunue rente trouvé pour cette idDmeande: " + getEntity().getIdRef());
        }

        for (Entry<String, List<RenteAdapationDemande>> listRente : mapRenteAdaptation.entrySet()) {

            // On regroupe les rentes demandé par leur type pour déterminer si on a deux fois la même rente
            Map<String, List<RenteAdapationDemande>> mapTypeRente = JadeListUtil.groupBy(listRente.getValue(),
                    new JadeListUtil.Key<RenteAdapationDemande>() {
                        @Override
                        public String exec(RenteAdapationDemande e) {
                            return e.getSimpleRenteAdaptation().getGenre();
                        }
                    });

            for (Entry<String, List<RenteAdapationDemande>> listTypeRente : mapTypeRente.entrySet()) {
                if (listTypeRente.getValue().size() > 1) {
                    throw new AdaptationException("Trop de rente de même type ont été trouvées pour ce type de rente"
                            + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                                    listRente.getValue().get(0).getSimpleRenteAdaptation().getGenre())
                            + "( pour le membre de famille Nss: "
                            + listRente.getValue().get(0).getSimpleDemandeCentrale().getNss() + ")");
                }
            }

            if (listRente.getValue().size() == 0) {
                throw new AdaptationException("no pension found");
            }

            // On récupère l'annonce de retour de la centrale pour le membre de famille.
            List<IHEOutputAnnonce> listeAnnonces = mapAnnonceRentes.get(listRente.getValue().get(0)
                    .getSimpleDemandeCentrale().getReferenceInterne());

            if (listeAnnonces == null) {
                throw new AdaptationException(
                        "Aucune annonce à été trouvée avec ce numero d'idDroitMembreFamille (Nss: "
                                + listRente.getValue().get(0).getSimpleDemandeCentrale().getNss() + ")"
                                + listRente.getValue().get(0).getSimpleDemandeCentrale().getReferenceInterne());

            }

            List<SimpleRenteAdaptation> listRenteAnnonce = new ArrayList<SimpleRenteAdaptation>();
            for (IHEOutputAnnonce an : listeAnnonces) {
                SimpleRenteAdaptation renteAnnonce = convertAnnonce(an);
                listRenteAnnonce.add(renteAnnonce);
            }

            listRenteAnnonce = filtreAnnonces(listRenteAnnonce);

            if (listRenteAnnonce.size() > 3) {
                throw new AdaptationException("Too many annonce was founded with this nss: "
                        + listRente.getValue().get(0).getSimpleDemandeCentrale().getNss());
            }

            saveAllAnnonce(listRente, listRenteAnnonce);
        }

    }

    /**
     *@formatter:off
     * On vas sauvegarder les annonces recues. Plus précisément on vas mapper les annonces recues avec le rente
     * sauvegardé dans l'étape d'avant. La rente mapper peut-être dans 3 état différents. 
     *  SYNCHRONIZE : On a réussi à mapper l'annonce de la centrale tout c'est bien passé. 
     *  RENTE_CHANGE: Définit que le type de rente a changé. Dans ce cas une nouvelle rente est créer. 
     *  NEW: Nouvelle rente recue par la central cette rente n'existe pas dans notre système ou que l'on n'a pas reussi à synchroniser la rente.
     *  WAIT: On a pas réussi à mapper l'annonce
     *  NOT_RETURN: Rente non retourné par la central.
     * @param listRente
     * @param listRenteAnnonce
     * @throws JadeNoBusinessLogSessionError
     * @throws AdaptationException
     * @throws RenteAdapationDemandeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private void saveAllAnnonce(Entry<String, List<RenteAdapationDemande>> listRente,
            List<SimpleRenteAdaptation> listRenteAnnonce)
            throws JadeNoBusinessLogSessionError, AdaptationException, RenteAdapationDemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        boolean traite;

       // List<String> listRenteTraite = new ArrayList<String>();
        
        Set<String> listRenteTraite = new HashSet<String>();
        /*
         * Lorsqu'une rente est dans l'état rente_change on vas créer une nouvelle rente pour le cas en question.
         * C'est pour cette raison que l'on fait une NEW
         * */
        for (SimpleRenteAdaptation annonce : listRenteAnnonce) {
            traite = false;
            // Si la central connais la rente
            if ("0".equals(annonce.getCodeRetour())) {
                for (RenteAdapationDemande rente : listRente.getValue()) {
                    if (annonce.getCsTypdDonneeFinacire().equals(
                            rente.getSimpleRenteAdaptation().getCsTypdDonneeFinacire())) {
                        if (rente.getSimpleRenteAdaptation().getGenre().equals(annonce.getGenre())) {
                            annonce.setEtat(EPCRenteAdaptation.SYNCHRONIZE);
                            saveRenteDemande(rente, annonce);
                            traite = true;
                            listRenteTraite.add(rente.getSimpleRenteAdaptation().getIdRenteAdaptation());
                        } else {
                            if (!listRenteTraite.contains(rente.getSimpleRenteAdaptation().getIdRenteAdaptation())) {
                                rente.getSimpleRenteAdaptation().setEtat(EPCRenteAdaptation.RENTE_CHANGE);
                                String[] param = new String[3];
                                param[1] = BSessionUtil.getSessionFromThreadContext().getCode(
                                        rente.getSimpleRenteAdaptation().getGenre());
                                param[0] = BSessionUtil.getSessionFromThreadContext().getCode(annonce.getGenre());
                                param[2] = annonce.getNss();
                                JadeThread.logWarn(this.getClass().getName(),
                                        "pegasus.process.adaptaion.reception.genreRenteChange", param);
                                PegasusServiceLocator.getSimpleRenteAdaptationService().update(
                                        rente.getSimpleRenteAdaptation());
                                listRenteTraite.add(rente.getSimpleRenteAdaptation().getIdRenteAdaptation());
                            }
                        }
                    
                    }
                }
                if (!traite) {
                    annonce.setEtat(EPCRenteAdaptation.NEW);
                    saveRenteDemande(listRente.getValue().get(0), annonce);
                    //listRenteTraite.add(listRente.getValue().get(0).getSimpleRenteAdaptation().getIdRenteAdaptation());
                }
            } else
            // Numéro d'assuré erroné
            if ("1".equals(annonce.getCodeRetour())) {
                String[] param = new String[1];
                param[0] = annonce.getNss();
                JadeThread.logError(this.getClass().getName(), "pegasus.process.adaptaion.reception.nssErrone", param);
            } else
            // TYPE sans rente
            if ("2".equals(annonce.getCodeRetour())) {
                boolean isSansRente = false;

                // Si la central ne connaît pas la rente ceci et peut-être du que c'est un cas sans rente
                for (RenteAdapationDemande rente : listRente.getValue()) {
                    if (IPCRenteAvsAi.CS_TYPE_SANS_RENTE.equals(rente.getSimpleRenteAdaptation().getGenre())) {
                        isSansRente = true;
                        annonce.setEtat(EPCRenteAdaptation.SYNCHRONIZE);
                        rente.getSimpleRenteAdaptation().setEtat(EPCRenteAdaptation.SYNCHRONIZE);
                        PegasusServiceLocator.getSimpleRenteAdaptationService()
                                .update(rente.getSimpleRenteAdaptation());
                        listRenteTraite.add(rente.getSimpleRenteAdaptation().getIdRenteAdaptation());
                    }
                }
                // On ne lance pas d'erreur pour un cas sans rente
                if (!isSansRente) {
                    // for (RenteAdapationDemande rente : listRente.getValue()) {
                    String[] p = new String[1];
                    p[0] = annonce.getNss();
                    JadeThread.logError(this.getClass().getName(),
                            "pegasus.process.adaptaion.reception.renteNonConueParLaCentrale", p);
                    // }
                }

            } else
            // Si on trouve un type sans rente qui a une rente on lance une erreur
            if (annonce.getGenre().equals(IPCRenteAvsAi.CS_TYPE_SANS_RENTE) && !"2".equals(annonce.getCodeRetour())) {
                JadeThread.logError(this.getClass().getName(),
                        "pegasus.process.adaptaion.reception.rentreTrouvePourSansRente");

            } else {
                JadeThread.logError(this.getClass().getName(),
                        "pegasus.process.adaptaion.reception.codeDeRetourNonPrisEnCharge");
            }
        }

        if (listRenteAnnonce.size() < listRente.getValue().size()) {
            for (RenteAdapationDemande rente : listRente.getValue()) {
                if (!listRenteTraite.contains(rente.getSimpleRenteAdaptation().getIdRenteAdaptation())) {
                    rente.getSimpleRenteAdaptation().setEtat(EPCRenteAdaptation.NOT_RETURN);
                    String[] p = new String[1];
                    p[0] = rente.getSimpleDemandeCentrale().getNss();
                    JadeThread.logWarn(this.getClass().getName(),
                            "pegasus.process.adaptaion.reception.renteNonRetourneParLaCentral", p);
                    PegasusServiceLocator.getSimpleRenteAdaptationService().update(rente.getSimpleRenteAdaptation());
                }
            }
        }
    }



    private void saveRenteDemande(RenteAdapationDemande rente, SimpleRenteAdaptation annonceRente)
            throws JadeNoBusinessLogSessionError, AdaptationException, RenteAdapationDemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        checkRenteDemande(rente, annonceRente);

        annonceRente.setIdDemandeCentral(rente.getSimpleDemandeCentrale().getIdDemandeCentral());

        // on a un mapping parfait entre les annonces et les rentes
        if (EPCRenteAdaptation.SYNCHRONIZE.equals(annonceRente.getEtat())) {
            annonceRente.setSpy(rente.getSpy());
            annonceRente.setId(rente.getId());
            annonceRente.setIdDonneeFinanciereHeaderOld(rente.getSimpleRenteAdaptation()
                    .getIdDonneeFinanciereHeaderOld());
            PegasusServiceLocator.getSimpleRenteAdaptationService().update(annonceRente);
        } else if (EPCRenteAdaptation.NOT_RETURN.equals(annonceRente.getEtat())) {
            annonceRente.setSpy(rente.getSpy());
            annonceRente.setId(rente.getId());
            annonceRente.setIdDonneeFinanciereHeaderOld(rente.getSimpleRenteAdaptation()
                    .getIdDonneeFinanciereHeaderOld());
            rente.getSimpleRenteAdaptation().setEtat(EPCRenteAdaptation.NOT_RETURN);
            PegasusServiceLocator.getSimpleRenteAdaptationService().update(annonceRente);
        } else if (EPCRenteAdaptation.NEW.equals(annonceRente.getEtat())) {
            PegasusServiceLocator.getSimpleRenteAdaptationService().create(annonceRente);
        } else if (EPCRenteAdaptation.ERROR.equals(annonceRente.getEtat())) {
            // on fait rien car on il y un rollback qui est fait.
            // il faudrai faire un transaction spécifique ou tout mettre en erreur on début
            // TODO
        } else {
            throw new RenteAdapationDemandeException("L'etat de l'annonce n'a pas été trouvé");
        }

    }

}
