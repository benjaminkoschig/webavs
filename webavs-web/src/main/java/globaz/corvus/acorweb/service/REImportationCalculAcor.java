package globaz.corvus.acorweb.service;

import acor.ch.admin.zas.rc.annonces.rente.pool.PoolMeldungZurZAS;
import acor.ch.eahv_iv.xmlns.eahv_iv_2401_000501._1.Message;
import acor.rentes.xsd.fcalcul.FCalcul;
import acor.ch.admin.zas.xmlns.acor_rentes9_out_resultat._0.Resultat9;
import ch.globaz.corvus.business.services.CorvusCrudServiceLocator;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.constantes.EtatDemandeRente;
import ch.globaz.corvus.domaine.constantes.TypeCalculDemandeRente;
import ch.globaz.corvus.domaine.constantes.TypeDemandeRente;
import ch.globaz.corvus.utils.rentesverseesatort.RECalculRentesVerseesATort;
import ch.globaz.corvus.utils.rentesverseesatort.REDetailCalculRenteVerseeATort;
import ch.globaz.pegasus.utils.RpcUtil;
import ch.globaz.pyxis.business.services.PyxisCrudServiceLocator;
import ch.globaz.pyxis.domaine.PersonneAVS;
import globaz.corvus.acor.parser.REFeuilleCalculVO;
import globaz.corvus.acor.parser.rev09.REACORParser;
import globaz.corvus.acorweb.mapper.REAcorMapper;
import globaz.corvus.acorweb.mapper.ReturnedValue;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.dao.REDeleteCascadeDemandeAPrestationsDues;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculManager;
import globaz.corvus.db.ci.RERassemblementCI;
import globaz.corvus.db.ci.RERassemblementCIManager;
import globaz.corvus.db.demandes.*;
import globaz.corvus.db.rentesaccordees.*;
import globaz.corvus.db.rentesverseesatort.RECalculRentesVerseesATortManager;
import globaz.corvus.db.rentesverseesatort.wrapper.RECalculRentesVerseesATortConverter;
import globaz.corvus.db.rentesverseesatort.wrapper.RECalculRentesVerseesATortWrapper;
import globaz.corvus.exceptions.REBusinessException;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.helpers.annonces.REAnnoncePonctuelleHelper;
import globaz.corvus.regles.REDemandeRegles;
import globaz.corvus.regles.REReglesException;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.acor.BaseCalculWrapper;
import globaz.corvus.utils.acor.DemandeRenteWrapper;
import globaz.corvus.vb.annonces.REAnnoncePonctuelleViewBean;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.helpers.PRHybridHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class REImportationCalculAcor {

    public static final int CAS_NOUVEAU_CALCUL = 1;
    public static final int CAS_RECALCUL_DEMANDE_VALIDEE = 2;
    public static final int CAS_RECALCUL_DEMANDE_NON_VALIDEE = 3;
    private static final String DATE_FIN_DEMANDE = "01.01.1000";
    private static final String DATE_DEBUT_DEMANDE = "31.12.9999";

    private Set<String> rentesWithoutBte = new HashSet<>();
    private List<String> remarquesParticulieres = new ArrayList<>();

    public void actionImporterScriptACOR(String idDemande, String idTiers, FCalcul fCalcul,
                                         final BSession session) throws Exception {
        LOG.info("Importation des donn�es calcul�es.");
        Long idCopieDemande = null;
        BITransaction transaction = null;
        LinkedList<Long> idsRentesAccordeesNouveauDroit = new LinkedList<>();
        ISFMembreFamilleRequerant[] mf;

        try {
            REDemandeRente demandeRente = loadDemandeRente(session, null, idDemande);

            if (IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL.equals(demandeRente.getCsTypeCalcul())) {
                throw new REBusinessException(session.getLabel("ERREUR_IMPORT_CALCUL_PREVISIONNEL"));
            }
            if (IREDemandeRente.CS_TYPE_CALCUL_BILATERALES.equals(demandeRente.getCsTypeCalcul())) {
                throw new REBusinessException(session.getLabel("ERREUR_IMPORT_CALCUL_DEMANDE_BILATERALE"));
            }

            // HACK: on cree une transaction pour etre sur que tous les ajouts
            // peuvent etre rollbackes
            // note: la transaction est enregistree dans la session est sera
            // utilisee dans tous les entity qui l'utilise
            transaction = session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            demandeRente = loadDemandeRente(session, transaction, idDemande);
            reinitialiserToutesDemandesNonValideesFamille(session, idTiers);

            // Identification du cas � traiter :
            int noCasATraiter = 0;

            if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE.equals(demandeRente.getCsEtat())
                    || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL.equals(demandeRente.getCsEtat())) {
                noCasATraiter = CAS_NOUVEAU_CALCUL;
            } else if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(demandeRente.getCsEtat())) {
                noCasATraiter = CAS_RECALCUL_DEMANDE_VALIDEE;
            } else if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE.equals(demandeRente.getCsEtat())) {
                noCasATraiter = CAS_RECALCUL_DEMANDE_NON_VALIDEE;
            } else {
                throw new PRACORException(session.getLabel("ERREUR_RECALCUL_CAS"));
            }

            List<FCalcul.Evenement> filterEvents = new ArrayList<>();

            if (Objects.isNull(fCalcul)) {
                throw new PRACORException("FCalcul est null. Le json n'a pas pu �tre converti.");
            }

            if (fCalcul.getEvenement() != null) {
                // On filtre les �v�nements de la feuille de calcul pour ne garder que ceux qui poss�dent une base de calcul dont la d�cision n'est pas vide.
                filterEvents = fCalcul.getEvenement().stream().filter(evenement -> !evenement.getBasesCalcul().stream().filter(basesCalcul -> !basesCalcul.getDecision().isEmpty()).collect(Collectors.toList()).isEmpty()).collect(Collectors.toList());
            }

            // K191213_001, suite � l'incident I191211_020
            // Passage dans le IF si c'est une 10�me r�vision mais qu'il n'y a pas de d�cisions dans le f_calcul.xml
            // Si on se trouve dans une 10�me r�vision et qu'il y a des d�cisions, on fait le calcul normalement
            if (filterEvents.isEmpty()) {
                // Dans ce cas de figure, il n'y a rien � remonter.
                // On contr�le l'arriv�e d'un ci additionnel pour le requ�rant
                // ainsi que ces conjoints et ex-conjoints !!!!

                ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                        ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiers);

                mf = sf.getMembresFamilleRequerant(idTiers);
                String idMFRequerant = "";
                for (int i = 0; i < mf.length; i++) {
                    if (ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT.equals(mf[i].getRelationAuRequerant())) {
                        idMFRequerant = mf[i].getIdMembreFamille();
                        break;
                    }
                }

                ISFMembreFamille[] mfe = sf.getMembresFamilleEtendue(idMFRequerant, Boolean.FALSE);
                for (int i = 0; i < mfe.length; i++) {
                    if (ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT.equals(mfe[i].getRelationAuLiant())) {
                        if (!JadeStringUtil.isBlankOrZero(mfe[i].getIdTiers())) {
                            doMajDateTraitementCIAdditionnel(session, transaction, mfe[i].getIdTiers());
                        }
                    }
                }
                doMajDateTraitementCIAdditionnel(session, transaction, idTiers);
            } else {

                // !!!!! Contr�le de l'arriv�e d'un CI additionnel ici....

                // Recherche des demandes de RCI de type CI Additionnel,
                // pour tous les membres de la famille ayant une rente active
                // et maj du ci additionnel avec la date de traitement
                ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                        ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiers);

                mf = sf.getMembresFamilleRequerant(idTiers);
                String ids = "";
                for (int i = 0; i < mf.length; i++) {
                    if (!JadeStringUtil.isBlankOrZero(mf[i].getIdTiers())) {
                        ids += mf[i].getIdTiers() + ",";
                    }
                }
                // La string se termine par une 'virgule' !!
                ids += "-1";

                RERenteAccordeeManager mgr = new RERenteAccordeeManager();
                mgr.setSession(session);
                mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_PARTIEL + ", " + IREPrestationAccordee.CS_ETAT_VALIDE);
                mgr.setForEnCoursAtMois(REPmtMensuel.getDateDernierPmt(session));
                mgr.setForIdTiersBeneficiaireIn(ids);
                mgr.find(transaction);

                // Map pour le traitement des annonces ponctuelles
                /*
                 * |---------|---------------------------------| | Key | Value |
                 * |---------|---------------------------------| |KeyAP | ValueAP | | -idTiers| -ancienRAM | | | -mntRA
                 * | | | -nouveauRAM | | | -ancienMontantRA | | | -nouveauMontantRA |
                 * |---------|---------------------------------| | ... | ... |
                 * |---------|---------------------------------|
                 */

                Map<KeyAP, ValueAP> mapAP = new HashMap<>();

                boolean isCIAdditionnel = false;

                // Init de la map avec les anciens montant et RAM
                for (int i = 0; i < mgr.size(); i++) {
                    RERenteAccordee ra = (RERenteAccordee) mgr.getEntity(i);

                    // Identification de l'arriv�e d'un CI additionnel
                    RERassemblementCIManager mgr2 = new RERassemblementCIManager();
                    mgr2.setSession(session);
                    mgr2.setForIdTiers(ra.getIdTiersBeneficiaire());
                    mgr2.setForCIAdditionnelOnly(Boolean.TRUE);
                    mgr2.find(transaction);

                    if (!mgr2.isEmpty()) {
                        isCIAdditionnel = true;
                        List<String> idsRCI = new ArrayList<String>();
                        // Parcours de tous les rassemblements de ci additionnels, des
                        // fois qu'il y en ait plus qu'un !!
                        for (int ii = 0; ii < mgr2.size(); ii++) {
                            RERassemblementCI rci = (RERassemblementCI) mgr2.getEntity(ii);
                            rci.setDateTraitement(JACalendar.todayJJsMMsAAAA());
                            rci.update(transaction);
                            idsRCI.add(rci.getIdRCI());
                        }

                        KeyAP k = new KeyAP();
                        k.idTiers = ra.getIdTiersBeneficiaire();
                        k.genreRente = ra.getCodePrestation();
                        ValueAP v = initVAP(session, transaction, ra, idsRCI);
                        mapAP.put(k, v);
                    }
                }

                if (isCIAdditionnel) {
                    int ret = importationCIAdditionnelsDepuisCalculACOR(session, transaction, mapAP, demandeRente, fCalcul);
                    // Traitement standard....
                    if (ret == 1) {
                        // TODO : refacto m�thode pour diminuer les param�tres.
                        idCopieDemande = importationDesAnnoncesDuCalculACOR(session, transaction,
                                idsRentesAccordeesNouveauDroit, noCasATraiter, demandeRente, fCalcul);
                    }
                }
                // Pas de ci additionnel, traitement standard.
                else {
                    idCopieDemande = importationDesAnnoncesDuCalculACOR(session, transaction,
                            idsRentesAccordeesNouveauDroit, noCasATraiter, demandeRente, fCalcul);
                }
            }

            /*
             * Si le calcul � �t� remont� sur une demande valid�e, une copie sera automatiquement r�alis�e
             * Il faut donc travailler sur la copie de la demande et non sur l'original
             */

            if (idCopieDemande != null && !idCopieDemande.equals(0)) {
                idDemande = (String.valueOf(idCopieDemande));
            }
            // Inforom D0112 : recherche si des remarques particuli�res sont pr�sentes dans la feuille de calcul
            traiterLesRemarquesParticulieresDeLaFeuilleDeCalculAcor(session, transaction, idDemande);

            /*
             * On ne lance le calcul des rentes vers�es � tort que si des rentes accord�es ont �t� cr��es lors de
             * l'importation du calcul, sinon cela r�sulte en une requ�te SQL avec une clause WHERE trop large et une
             * explosion de la consommation m�moire lors de la recherche des donn�es pour le calcul des rentes vers�es �
             * tort
             */
            if (idsRentesAccordeesNouveauDroit.size() > 0) {
                calculerEtSauverRentesVerseesATort(session, (BTransaction) transaction,
                        Long.valueOf(idDemande), idsRentesAccordeesNouveauDroit);
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        try {
            BSessionUtil.initContext(session, this);
            /*
             * Dispatch des rentes accord�es devant �tre d�plac� dans une autre demande
             * Try/catch suivant uniquement pour donner du context � l'exception
             */
            long idDemandeLong = 0;
            try {
                /*
                 * Si la demande � �t� copi�e dans le traitement pr�c�dent, on travaille avec la copie de la demande
                 */
                if (idCopieDemande != null) {
                    idDemandeLong = idCopieDemande;
                } else {
                    idDemandeLong = Long.valueOf(idDemande);
                }


                long idTiersDemande = Long.valueOf(idTiers);

                // L'appel de cette m�thode peut dans certain cas retourner un message d'information � l'utilisateur
                String message = separerLesDemandesSiBesoin(idTiersDemande, idDemandeLong);
                LOG.info(message);

            } catch (NumberFormatException exception) {
                String msg = "Unable to get id of the REDemandeRente [" + idDemande
                        + "] or the idTiersDemandeRente [" + idTiers + "]. Cause : "
                        + exception.toString();
                throw new IllegalArgumentException(msg, exception);
            }

            /*
             * BZ 9627 : on parcours les rentes vers�es � tort de la demande sur laquelle on vient d'importer, et on
             * v�rifie que les rentes vers�es � tort soient bien rattach�es � la bonne demande (en comparant la demande
             * sur laquelle est rattach� la rente du nouveau droit ayant permis de calculer cette rente vers�e � tort)
             */

            RERenteVerseeATortManager renteVerseeATortManager = new RERenteVerseeATortManager();
            renteVerseeATortManager.setForIdDemandeRente(idDemandeLong);
            renteVerseeATortManager.find(BManager.SIZE_NOLIMIT);

            for (RERenteVerseeATort uneRenteVerseeATort : renteVerseeATortManager.getContainerAsList()) {

                /*
                 * Dans le cas o� il n'y a pas d'ID de rente du nouveau droit (c'est � dire qu'il y avait un trou
                 * dans
                 * la p�riode du nouveau droit), on ne fait rien pour la rente vers�e � tort
                 */
                if (uneRenteVerseeATort.getIdRenteAccordeeNouveauDroit() == null) {
                    continue;
                }

                RERenteAccordee renteAccordee = new RERenteAccordee();
                renteAccordee.setIdPrestationAccordee(uneRenteVerseeATort.getIdRenteAccordeeNouveauDroit().toString());
                renteAccordee.retrieve();

                REBasesCalcul baseCalcul = new REBasesCalcul();
                baseCalcul.setIdBasesCalcul(renteAccordee.getIdBaseCalcul());
                baseCalcul.retrieve();

                REDemandeRente demandeRente2 = new REDemandeRente();
                demandeRente2.setIdRenteCalculee(baseCalcul.getIdRenteCalculee());
                demandeRente2.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
                demandeRente2.retrieve();

                // si la demande sur laquelle est li�e la rente accord�e est diff�rente de la demande de la rente
                // vers�e
                // � tort, on met � jour la rente vers�e � tort pour refl�ter la rente accord�e
                if (!uneRenteVerseeATort.getIdDemandeRente()
                        .equals(Long.parseLong(demandeRente2.getIdDemandeRente()))) {
                    uneRenteVerseeATort.retrieve();
                    uneRenteVerseeATort.setIdDemandeRente(Long.parseLong(demandeRente2.getIdDemandeRente()));
                    uneRenteVerseeATort.update();
                }
            }

        } catch (RETechnicalException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        } finally {
            BSessionUtil.stopUsingContext(this);
        }

        if (!rentesWithoutBte.isEmpty()) {
            Iterator<String> iterator = rentesWithoutBte.iterator();
            StringBuilder allNumber = new StringBuilder();
            while (iterator.hasNext()) {
                allNumber.append(iterator.next());
                if (iterator.hasNext()) {
                    allNumber.append(", ");
                } else {
                    allNumber.append(".");
                }
            }

            List<String> nss = Arrays.stream(mf).filter(e -> StringUtils.equals(e.getIdTiers(), idTiers)).map(e -> e.getNss()).collect(Collectors.toList());

            String object = FWMessageFormat.format(JadeI18n.getInstance().getMessage(session.getIdLangueISO(), "warn.acro.export.subject"), nss.isEmpty() ? "" : nss.get(0));
            String content = FWMessageFormat.format(JadeI18n.getInstance().getMessage(session.getIdLangueISO(), "warn.acor.export.bte"), allNumber.toString());
            sendMailWarn(session, object, content);
        }
        //swap
        generationFormulaireSwap(fCalcul,session);
    }
    public void generationFormulaireSwap(FCalcul fCalcul,BSession session) throws Exception {
        if(isSwap(fCalcul)){
            Message messageFromFcalcul=fCalcul.getAnnexes().getMessage().get(0);
            String xmlFromAcor= getSwapXmlFromAcor(messageFromFcalcul);
            sendSwapFormulaireMail(xmlFromAcor,fCalcul,session);
        }
    }

    public boolean isSwap(FCalcul fCalcul){
        boolean response=false;
        if(fCalcul.getAnnexes()!=null && fCalcul.getAnnexes().getMessage().size()!=0){
            if(!StringUtils.isBlank(fCalcul.getAnnexes().getMessage().get(0).getSwapId())){
                response=true;
            }
        }
        return response;
    }
    public String getSwapXmlFromAcor(Message message) throws PRACORException {
        return REAcorSwapService.getInstance().getSwap(message);
    }
    private void sendSwapFormulaireMail(String xmlAcor, FCalcul fCalcul,BSession session) throws Exception {
        String nss = RpcUtil.formatNss(fCalcul.getAssure().get(0).getId().getValue());
        String userMail= session.getUserEMail();
        String filePath=Jade.getInstance().getHomeDir() + "work/"+"p["+nss+"].xml";
        String subject=session.getLabel("ACOR_FORMULAIRE_SWAP_SUBJECT")+ "["+nss+"]";
        String body=session.getLabel("ACOR_FORMULAIRE_SWAP_BODY");
        File swapXmlFile= createSwapXmlFile(filePath,xmlAcor);

        JadeSmtpClient.getInstance().sendMail(userMail,subject,body,new String[]{filePath});

        deleteSwapXmlFile(swapXmlFile);

    }
    public File createSwapXmlFile(String filePath,String xmlAcor) throws IOException {
        File file = new File(filePath);
        FileWriter writer= new FileWriter(file);
        writer.write(xmlAcor);
        writer.close();
        return file;
    }
    public void deleteSwapXmlFile(File file) throws IOException {
        FileUtils.forceDelete(file);
    }

    private void sendMailWarn(BSession session, String object, String content) throws Exception {
        JadeSmtpClient.getInstance().sendMail(session.getUserEMail(), object, content, null);
    }

    public void actionImporterScriptACOR9(String idDemande, String idTiers, Resultat9 resultat9, BSession session) throws Exception {
        Long idCopieDemande = null;
        BITransaction transaction = null;
        LinkedList<Long> idsRentesAccordeesNouveauDroit = new LinkedList<>();

        try {
            REDemandeRente demandeRente = loadDemandeRente(session, null, idDemande);

            if (IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL.equals(demandeRente.getCsTypeCalcul())) {
                throw new REBusinessException(session.getLabel("ERREUR_IMPORT_CALCUL_PREVISIONNEL"));
            }
            if (IREDemandeRente.CS_TYPE_CALCUL_BILATERALES.equals(demandeRente.getCsTypeCalcul())) {
                throw new REBusinessException(session.getLabel("ERREUR_IMPORT_CALCUL_DEMANDE_BILATERALE"));
            }

            // HACK: on cree une transaction pour etre sur que tous les ajouts
            // peuvent etre rollbackes
            // note: la transaction est enregistree dans la session est sera
            // utilisee dans tous les entity qui l'utilise
            transaction = session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            demandeRente = loadDemandeRente(session, transaction, idDemande);
            reinitialiserToutesDemandesNonValideesFamille(session, idTiers);

            // Identification du cas � traiter :
            int noCasATraiter = 0;

            if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE.equals(demandeRente.getCsEtat())
                    || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL.equals(demandeRente.getCsEtat())) {
                noCasATraiter = CAS_NOUVEAU_CALCUL;
            } else if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(demandeRente.getCsEtat())) {
                noCasATraiter = CAS_RECALCUL_DEMANDE_VALIDEE;
            } else if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE.equals(demandeRente.getCsEtat())) {
                noCasATraiter = CAS_RECALCUL_DEMANDE_NON_VALIDEE;
            } else {
                throw new PRACORException(session.getLabel("ERREUR_RECALCUL_CAS"));
            }


            // !!!!! Contr�le de l'arriv�e d'un CI additionnel ici....

            // Recherche des demandes de RCI de type CI Additionnel,
            // pour tous les membres de la famille ayant une rente active
            // et maj du ci additionnel avec la date de traitement
            ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiers);

            ISFMembreFamilleRequerant[] mf = sf.getMembresFamilleRequerant(idTiers);
            String ids = "";
            for (int i = 0; i < mf.length; i++) {
                if (!JadeStringUtil.isBlankOrZero(mf[i].getIdTiers())) {
                    ids += mf[i].getIdTiers() + ",";
                }
            }
            // La string se termine par une 'virgule' !!
            ids += "-1";

            RERenteAccordeeManager mgr = new RERenteAccordeeManager();
            mgr.setSession(session);
            mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_PARTIEL + ", " + IREPrestationAccordee.CS_ETAT_VALIDE);
            mgr.setForEnCoursAtMois(REPmtMensuel.getDateDernierPmt(session));
            mgr.setForIdTiersBeneficiaireIn(ids);
            mgr.find(transaction);

            // Map pour le traitement des annonces ponctuelles
            /*
             * |---------|---------------------------------| | Key | Value |
             * |---------|---------------------------------| |KeyAP | ValueAP | | -idTiers| -ancienRAM | | | -mntRA
             * | | | -nouveauRAM | | | -ancienMontantRA | | | -nouveauMontantRA |
             * |---------|---------------------------------| | ... | ... |
             * |---------|---------------------------------|
             */

            Map<KeyAP, ValueAP> mapAP = new HashMap<>();

            boolean isCIAdditionnel = false;

            // Init de la map avec les anciens montant et RAM
            for (int i = 0; i < mgr.size(); i++) {
                RERenteAccordee ra = (RERenteAccordee) mgr.getEntity(i);

                // Identification de l'arriv�e d'un CI additionnel
                RERassemblementCIManager mgr2 = new RERassemblementCIManager();
                mgr2.setSession(session);
                mgr2.setForIdTiers(ra.getIdTiersBeneficiaire());
                mgr2.setForCIAdditionnelOnly(Boolean.TRUE);
                mgr2.find(transaction);

                if (!mgr2.isEmpty()) {
                    isCIAdditionnel = true;
                    List<String> idsRCI = new ArrayList<String>();
                    // Parcours de tous les rassemblements de ci additionnels, des
                    // fois qu'il y en ait plus qu'un !!
                    for (int ii = 0; ii < mgr2.size(); ii++) {
                        RERassemblementCI rci = (RERassemblementCI) mgr2.getEntity(ii);
                        rci.setDateTraitement(JACalendar.todayJJsMMsAAAA());
                        rci.update(transaction);
                        idsRCI.add(rci.getIdRCI());
                    }

                    KeyAP k = new KeyAP();
                    k.idTiers = ra.getIdTiersBeneficiaire();
                    k.genreRente = ra.getCodePrestation();
                    ValueAP v = initVAP(session, transaction, ra, idsRCI);
                    mapAP.put(k, v);
                }
            }

            if (isCIAdditionnel) {
                int ret = importationCIAdditionnelsDepuisCalculACOR9(session, transaction, mapAP, demandeRente, resultat9);
                // Traitement standard....
                if (ret == 1) {
                    idCopieDemande = importationDesAnnoncesDuCalculACOR9(session, transaction,
                            idsRentesAccordeesNouveauDroit, noCasATraiter, demandeRente, resultat9);
                }
            }
            // Pas de ci additionnel, traitement standard.
            else {
                idCopieDemande = importationDesAnnoncesDuCalculACOR9(session, transaction,
                        idsRentesAccordeesNouveauDroit, noCasATraiter, demandeRente, resultat9);
            }

            /*
             * Si le calcul � �t� remont� sur une demande valid�e, une copie sera automatiquement r�alis�e
             * Il faut donc travailler sur la copie de la demande et non sur l'original
             */

            if (idCopieDemande != null && !idCopieDemande.equals(0)) {
                idDemande = (String.valueOf(idCopieDemande));
            }

            // Inforom D0112 : recherche si des remarques particuli�res sont pr�sentes dans la feuille de calcul
            traiterLesRemarquesParticulieresDeLaFeuilleDeCalculAcor(session, transaction, idDemande);

            /*
             * On ne lance le calcul des rentes vers�es � tort que si des rentes accord�es ont �t� cr��es lors de
             * l'importation du calcul, sinon cela r�sulte en une requ�te SQL avec une clause WHERE trop large et une
             * explosion de la consommation m�moire lors de la recherche des donn�es pour le calcul des rentes vers�es �
             * tort
             */
            if (idsRentesAccordeesNouveauDroit.size() > 0) {
                calculerEtSauverRentesVerseesATort(session, (BTransaction) transaction,
                        Long.valueOf(idDemande), idsRentesAccordeesNouveauDroit);
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        try {
            BSessionUtil.initContext(session, this);
            /*
             * Dispatch des rentes accord�es devant �tre d�plac� dans une autre demande
             * Try/catch suivant uniquement pour donner du context � l'exception
             */
            long idDemandeLong = 0;
            try {
                /*
                 * Si la demande � �t� copi�e dans le traitement pr�c�dent, on travaille avec la copie de la demande
                 */
                if (idCopieDemande != null) {
                    idDemandeLong = idCopieDemande;
                } else {
                    idDemandeLong = Long.valueOf(idDemande);
                }


                long idTiersDemande = Long.valueOf(idTiers);

                // L'appel de cette m�thode peut dans certain cas retourner un message d'information � l'utilisateur
                String message = separerLesDemandesSiBesoin(idTiersDemande, idDemandeLong);
                LOG.info(message);

            } catch (NumberFormatException exception) {
                String msg = "Unable to get id of the REDemandeRente [" + idDemande
                        + "] or the idTiersDemandeRente [" + idTiers + "]. Cause : "
                        + exception.toString();
                throw new IllegalArgumentException(msg, exception);
            }

            /*
             * BZ 9627 : on parcours les rentes vers�es � tort de la demande sur laquelle on vient d'importer, et on
             * v�rifie que les rentes vers�es � tort soient bien rattach�es � la bonne demande (en comparant la demande
             * sur laquelle est rattach� la rente du nouveau droit ayant permis de calculer cette rente vers�e � tort)
             */

            RERenteVerseeATortManager renteVerseeATortManager = new RERenteVerseeATortManager();
            renteVerseeATortManager.setForIdDemandeRente(idDemandeLong);
            renteVerseeATortManager.find(BManager.SIZE_NOLIMIT);

            for (RERenteVerseeATort uneRenteVerseeATort : renteVerseeATortManager.getContainerAsList()) {

                /*
                 * Dans le cas o� il n'y a pas d'ID de rente du nouveau droit (c'est � dire qu'il y avait un trou
                 * dans
                 * la p�riode du nouveau droit), on ne fait rien pour la rente vers�e � tort
                 */
                if (uneRenteVerseeATort.getIdRenteAccordeeNouveauDroit() == null) {
                    continue;
                }

                RERenteAccordee renteAccordee = new RERenteAccordee();
                renteAccordee.setIdPrestationAccordee(uneRenteVerseeATort.getIdRenteAccordeeNouveauDroit().toString());
                renteAccordee.retrieve();

                REBasesCalcul baseCalcul = new REBasesCalcul();
                baseCalcul.setIdBasesCalcul(renteAccordee.getIdBaseCalcul());
                baseCalcul.retrieve();

                REDemandeRente demandeRente2 = new REDemandeRente();
                demandeRente2.setIdRenteCalculee(baseCalcul.getIdRenteCalculee());
                demandeRente2.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
                demandeRente2.retrieve();

                // si la demande sur laquelle est li�e la rente accord�e est diff�rente de la demande de la rente
                // vers�e
                // � tort, on met � jour la rente vers�e � tort pour refl�ter la rente accord�e
                if (!uneRenteVerseeATort.getIdDemandeRente()
                        .equals(Long.parseLong(demandeRente2.getIdDemandeRente()))) {
                    uneRenteVerseeATort.retrieve();
                    uneRenteVerseeATort.setIdDemandeRente(Long.parseLong(demandeRente2.getIdDemandeRente()));
                    uneRenteVerseeATort.update();
                }
            }

        } catch (RETechnicalException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        } finally {
            BSessionUtil.stopUsingContext(this);
        }
    }


    /**
     * M�thode de chargement d'une demande de rente � partir de son id.
     *
     * @param session        session
     * @param transaction    transaction
     * @param idDemandeRente id de la demande
     * @return La demande de rente
     * @throws Exception
     */
    private REDemandeRente loadDemandeRente(BSession session, BITransaction transaction, String idDemandeRente) throws Exception {
        REDemandeRente retValue;

        // recupere le type de demande de rente
        retValue = new REDemandeRente();
        retValue.setIdDemandeRente(idDemandeRente);
        retValue.setSession(session);

        if (transaction == null) {
            retValue.retrieve();
        } else {
            retValue.retrieve(transaction);
        }

        String csTypeDemandeRente = retValue.getCsTypeDemandeRente();

        // charge la rente
        if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(csTypeDemandeRente)) {
            retValue = new REDemandeRenteAPI();
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(csTypeDemandeRente)) {
            retValue = new REDemandeRenteInvalidite();
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(csTypeDemandeRente)) {
            retValue = new REDemandeRenteSurvivant();
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(csTypeDemandeRente)) {
            retValue = new REDemandeRenteVieillesse();
        } else {
            // oops, ben zut alors, on n'a pas le type de la rente
            throw new Exception(session.getLabel("ERREUR_TYPE_DEMANDE_VIDE"));
        }

        retValue.setSession(session);
        retValue.setIdDemandeRente(idDemandeRente);
        if (transaction == null) {
            retValue.retrieve();
        } else {
            retValue.retrieve(transaction);
        }

        return retValue;
    }

    /**
     * R�initialise toutes les demandes de la famille (sauf API) en �tat non valid� avant remont� du calcul
     *
     * @param session
     * @param idTiersRequerant
     * @throws IllegalArgumentException
     * @throws NumberFormatException
     * @throws ServletException
     */
    private void reinitialiserToutesDemandesNonValideesFamille(final BSession session, String idTiersRequerant)
            throws IllegalArgumentException, NumberFormatException, ServletException {

        if (JadeStringUtil.isBlankOrZero(idTiersRequerant)) {
            throw new IllegalArgumentException("actionImporterScriptACOR : the idTiers is empty");
        }

        Set<DemandeRente> demandesNonValideesDeLaFamille = null;

        PRHybridHelper.initContext(session, this);
        try {
            PersonneAVS requerant = PyxisCrudServiceLocator.getPersonneAvsCrudService().read(Long.valueOf(idTiersRequerant));
            Set<DemandeRente> demandesDeLaFamille = CorvusServiceLocator.getDemandeRenteService().demandesDuRequerantEtDeSaFamille(requerant);
            demandesNonValideesDeLaFamille = demandesStandardsOuTransitoires(demandesNonValidees(demandesDeLaFamille));

        } finally {
            PRHybridHelper.stopUsingContext(this);
        }

        /*
         * On filtre les demande de type API et celles en �tat VALIDE
         */
        List<Long> idDemandesAReinitialise = new ArrayList<Long>();
        for (DemandeRente dem : demandesNonValideesDeLaFamille) {
            if (filtrerDemandeAReinitialiser(dem)) {
                idDemandesAReinitialise.add(dem.getId());
            }
        }

        for (Long id : idDemandesAReinitialise) {
            reinitialiserDemande(session, id);
        }
    }

    /**
     * Filtre les demandes et retourne les demandes dans l'�tat ENREGISTRE, AU_CALCUL et CALCULE
     *
     * @param demandes Les demandes � filtrer
     * @return Les demandes filtr�es
     */
    private Set<DemandeRente> demandesNonValidees(final Set<DemandeRente> demandes) {
        return demandes.stream().filter(eachDemande -> Objects.equals(EtatDemandeRente.AU_CALCUL, eachDemande.getEtat()) || Objects.equals(EtatDemandeRente.CALCULE, eachDemande.getEtat()) || Objects.equals(EtatDemandeRente.ENREGISTRE, eachDemande.getEtat())).collect(Collectors.toSet());
    }

    private Set<DemandeRente> demandesStandardsOuTransitoires(final Set<DemandeRente> demandes) {
        return demandes.stream().filter(eachDemande -> Objects.equals(TypeCalculDemandeRente.STANDARD, eachDemande.getTypeCalcul()) || Objects.equals(TypeCalculDemandeRente.TRANSITOIRE, eachDemande.getTypeCalcul())).collect(Collectors.toSet());
    }

    /**
     * Return true si la demande doit �tre r�initialis�e
     *
     * @param dem
     * @return
     */
    private boolean filtrerDemandeAReinitialiser(DemandeRente dem) {
        if (!TypeDemandeRente.DEMANDE_API.equals(dem.getTypeDemandeRente())) {
            if (!EtatDemandeRente.VALIDE.equals(dem.getEtat()) && !EtatDemandeRente.COURANT_VALIDE.equals(dem.getEtat())
                    && !EtatDemandeRente.TRANSFERE.equals(dem.getEtat())
                    && !EtatDemandeRente.TERMINE.equals(dem.getEtat())) {
                return true;
            }
        }
        return false;
    }

    /**
     * R�initialise une demande
     *
     * @param session
     * @param id
     */
    private void reinitialiserDemande(BSession session, long id) {
        REDemandeRente demande = new REDemandeRente();
        demande.setSession(session);
        demande.setId(String.valueOf(id));
        try {
            demande.retrieve();
            if (!demande.isNew()) {
                REDeleteCascadeDemandeAPrestationsDues.reinitiliserDemandeRente(demande);
                demande.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
                demande.setIdRenteCalculee("");
                demande.update();
            }
        } catch (Exception e) {
            LOG.error("Erreur lors de la r�initialisation de la demande.", e);
            throw new RETechnicalException(e.toString(), e);
        }
    }

    private ValueAP initVAP(final BSession session, final BITransaction transaction, final RERenteAccordee ra,
                            final List<String> idsRCI) throws Exception {

        ValueAP v = new ValueAP();
        v.ancienMontantRA = ra.getMontantPrestation();
        REBasesCalcul bc = new REBasesCalcul();
        bc.setSession(session);
        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
        bc.retrieve(transaction);
        v.ancienRAM = bc.getRevenuAnnuelMoyen();
        v.idRA = ra.getIdPrestationAccordee();
        v.idsRCI = idsRCI;

        return v;
    }

    /*
     *
     * Voir avec RJE, besoin de cas tests !!!!
     *
     * Si RAM ne change pas, ACOR ne va rien cr��er. -> mettre date de traitement au CI Additionnel lors de
     * l'importation et permettre l'importation sans planter l'applic.
     */
    private void doMajDateTraitementCIAdditionnel(final BSession session, final BITransaction transaction,
                                                  final String idTiers) throws Exception {

        // Identification de l'arriv�e d'un CI additionnel
        RERassemblementCIManager mgr = new RERassemblementCIManager();
        mgr.setSession(session);
        mgr.setForIdTiers(idTiers);
        mgr.setForCIAdditionnelOnly(Boolean.TRUE);
        mgr.find(transaction);
        if (!mgr.isEmpty()) {
            // Parcours de tous les rassemblements de ci additionnels, des fois
            // qu'il y en ait plus qu'un !!
            for (int i = 0; i < mgr.size(); i++) {
                RERassemblementCI rci = (RERassemblementCI) mgr.getEntity(i);
                rci.setDateTraitement(JACalendar.todayJJsMMsAAAA());
                rci.update(transaction);
            }
        }
    }

    /**
     * @param session
     * @param
     * @param transaction
     * @param mapAP
     * @return
     * @throws PRACORException
     * @throws Exception
     */
    private int importationCIAdditionnelsDepuisCalculACOR(final BSession session, final BITransaction transaction,
                                                          final Map<KeyAP, ValueAP> mapAP, final REDemandeRente demandeRente, final FCalcul fCalcul) throws PRACORException, Exception {

        List<REFeuilleCalculVO> fcs = REAcorMapper.parseCIAdd(session, transaction, demandeRente, fCalcul);

        // Contr�le des cas � traiter en faisant les diff�rences entre
        // la MAP de traitement des annonces ponctuelles et les donn�es
        // de la feuille de calcul !!!
        Set<KeyAP> keys = mapAP.keySet();

        for (Iterator<KeyAP> iterator2 = keys.iterator(); iterator2.hasNext(); ) {
            KeyAP keyAP = iterator2.next();

            // On it�re pour voir si cette cl� correspond � une BC-RA
            // remont�e de ACOR !!!
            for (Iterator<REFeuilleCalculVO> iterator = fcs.iterator(); iterator.hasNext(); ) {
                REFeuilleCalculVO fcvo = iterator.next();

                List<REFeuilleCalculVO.ElementVO> elementsFC = fcvo.getElementsAP();

                for (Iterator<REFeuilleCalculVO.ElementVO> iterator3 = elementsFC.iterator(); iterator3.hasNext(); ) {
                    REFeuilleCalculVO.ElementVO elmFC = iterator3.next();

                    if (keyAP.idTiers.equals(elmFC.getIdTiers()) && keyAP.genreRente.equals(elmFC.getGenreRente())) {

                        // !!!Element found, on set les nouveaux montant dans la MAP
                        ValueAP val = mapAP.get(keyAP);
                        val.nouveauMontantRA = elmFC.getMontantRente();
                        val.nouveauRAM = elmFC.getRAM();
                    }
                }
            }
        }

        int ret = doTraitementForCIAdditionnel(session, transaction, mapAP);
        return ret;
    }

    /**
     * @param session
     * @param
     * @param transaction
     * @param mapAP
     * @return
     * @throws PRACORException
     * @throws Exception
     */
    private int importationCIAdditionnelsDepuisCalculACOR9(final BSession session, final BITransaction transaction,
                                                           final Map<KeyAP, ValueAP> mapAP, final REDemandeRente demandeRente, Resultat9 resultat9) throws PRACORException, Exception {

        List<REFeuilleCalculVO> fcs = REACORParser.parseCIAdd(session, transaction,
                demandeRente, new StringReader(resultat9.getAnnexes().getPay()));

        // Contr�le des cas � traiter en faisant les diff�rences entre
        // la MAP de traitement des annonces ponctuelles et les donn�es
        // de la feuille de calcul !!!
        Set<KeyAP> keys = mapAP.keySet();

        for (Iterator<KeyAP> iterator2 = keys.iterator(); iterator2.hasNext(); ) {
            KeyAP keyAP = iterator2.next();

            // On it�re pour voir si cette cl� correspond � une BC-RA
            // remont�e de ACOR !!!
            for (Iterator<REFeuilleCalculVO> iterator = fcs.iterator(); iterator.hasNext(); ) {
                REFeuilleCalculVO fcvo = iterator.next();

                List<REFeuilleCalculVO.ElementVO> elementsFC = fcvo.getElementsAP();

                for (Iterator<REFeuilleCalculVO.ElementVO> iterator3 = elementsFC.iterator(); iterator3.hasNext(); ) {
                    REFeuilleCalculVO.ElementVO elmFC = iterator3.next();

                    if (keyAP.idTiers.equals(elmFC.getIdTiers()) && keyAP.genreRente.equals(elmFC.getGenreRente())) {

                        // !!!Element found, on set les nouveaux montant dans la MAP
                        ValueAP val = mapAP.get(keyAP);
                        val.nouveauMontantRA = elmFC.getMontantRente();
                        val.nouveauRAM = elmFC.getRAM();
                    }
                }
            }
        }

        int ret = doTraitementForCIAdditionnel(session, transaction, mapAP);
        return ret;
    }

    private int doTraitementForCIAdditionnel(final BSession session, final BITransaction transaction,
                                             final Map<KeyAP, ValueAP> mapAP) throws Exception {

        /*
         * 1)
         */
        // Parcours et traitement de la map
        Set<KeyAP> keys = mapAP.keySet();
        Iterator<KeyAP> i = keys.iterator();

        // Si au moins un changement dans les montants, on sort et effectue le traitement
        // standard.
        while (i.hasNext()) {
            KeyAP keyAP = i.next();
            ValueAP v = mapAP.get(keyAP);

            if (!v.ancienMontantRA.equals(v.nouveauMontantRA)) {
                return 1;
            }
        }

        // IMPORTANT : La suite n'est jamais ex�cut�e ! v.ancienMontantRA n'est et ne sera jamais �gal �
        // v.nouveauMontantRA (les montants ne sont pas formatt� de la m�me mani�re -> 2350.00 et 0002350)
        // Cette partie n'est pas � corriger !!

        i = keys.iterator();

        // Si on arrive ici, on cr�� les annonces ponctuelles uniquement pour
        // les RA
        // ayant un RAM qui diff�re.
        while (i.hasNext()) {
            KeyAP keyAP = i.next();
            ValueAP v = mapAP.get(keyAP);
            if (!v.ancienRAM.equals(v.nouveauRAM)) {
                doTraitementCIAdd(session, transaction, keyAP, v);
            }
        }

        return 0;
    }

    private void doTraitementCIAdd(final BSession session, final BITransaction transaction, final KeyAP k,
                                   final ValueAP v) throws Exception {
        List<String> l = v.idsRCI;
        for (Iterator<String> iterator = l.iterator(); iterator.hasNext(); ) {
            String idRCI = iterator.next();
            RERassemblementCI rci = new RERassemblementCI();
            rci.setSession(session);
            rci.setIdRCI(idRCI);
            rci.retrieve(transaction);
            rci.setDateTraitement(JACalendar.todayJJsMMsAAAA());
            rci.update(transaction);
        }

        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(session);
        ra.setIdPrestationAccordee(v.idRA);
        ra.retrieve(transaction);

        REBasesCalcul bc = new REBasesCalcul();
        bc.setSession(session);
        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
        bc.retrieve(transaction);

        bc.setRevenuAnnuelMoyen(v.nouveauMontantRA);
        bc.update(transaction);

        // Cr�ation des annonces ponctuelles pour toutes les RA, y compris les
        // compl�mentaires, ayant des montant diff�rents.
        REAnnoncePonctuelleViewBean vb = new REAnnoncePonctuelleViewBean();
        vb.setISession(session);
        vb.setIdRenteAccordee(ra.getIdPrestationAccordee());
        vb.setCsEtatCivil(ra.getCsEtatCivil());
        vb.setCs1(ra.getCodeCasSpeciaux1());
        vb.setCs2(ra.getCodeCasSpeciaux2());
        vb.setCs3(ra.getCodeCasSpeciaux3());
        vb.setCs4(ra.getCodeCasSpeciaux4());
        vb.setCs5(ra.getCodeCasSpeciaux5());

        vb.setCodeRefugie(ra.getCodeRefugie());
        vb.setRevenuPrisEnCompte9(bc.getRevenuPrisEnCompte());
        vb.setRAM(bc.getRevenuAnnuelMoyen());
        vb.setDegreInvalidite(bc.getDegreInvalidite());
        vb.setCleInfirmite(bc.getCleInfirmiteAyantDroit());
        vb.setSurvenanceEvenementAssure(bc.getSurvenanceEvtAssAyantDroit());
        vb.setIsInvaliditePrecoce(bc.isInvaliditePrecoce());
        vb.setOfficeAI(bc.getCodeOfficeAi());
        vb.setDroitApplique(bc.getDroitApplique());
        vb.setIdTiersBeneficiaire(ra.getIdTiersBeneficiaire());

        vb.setIdTiersComplementaire1(ra.getIdTiersComplementaire1());
        vb.setIdTiersComplementaire2(ra.getIdTiersComplementaire2());

        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, ra.getIdTiersComplementaire1());
        vb.setNssComplementaire1(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
        tw = PRTiersHelper.getTiersParId(session, ra.getIdTiersComplementaire2());
        vb.setNssComplementaire2(tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

        // Set code etat civil et code canton !!!
        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, ra.getIdTiersBeneficiaire());
        String csEtatCivil = null;
        String csCantonDomicile = null;

        ISFMembreFamilleRequerant[] mf = sf.getMembresFamille(ra.getIdTiersBeneficiaire());
        for (int i = 0; i < mf.length; i++) {
            ISFMembreFamilleRequerant membre = mf[i];
            // On r�cup�re le b�n�ficiaire en tant que membre de famille
            if (ra.getIdTiersBeneficiaire().equals(membre.getIdTiers())) {
                csEtatCivil = membre.getCsEtatCivil();
                csCantonDomicile = membre.getCsCantonDomicile();
                if (csCantonDomicile == null) {
                    csCantonDomicile = session.getCode(membre.getCsNationalite());
                }
                break;
            }
        }
        // Peut arriver dans le cas d'un enfant de la situation familialle, par exemple.
        if (csEtatCivil == null) {
            csEtatCivil = ISFSituationFamiliale.CS_ETAT_CIVIL_CELIBATAIRE;
        }
        vb.setCodeEtatCivil(PRACORConst.csEtatCivilHeraToAcorForRentes(session, csEtatCivil));
        vb.setCsEtatCivil(PRACORConst.csEtatCivilHeraToCsEtatCivil(csEtatCivil));
        vb.setCanton(PRACORConst.csCantonToAcor(csCantonDomicile));

        vb.setCodeRefugie(ra.getCodeRefugie());
        vb.setGenrePrestation(ra.getCodePrestation());
        vb.setIdBaseCalcul(ra.getIdBaseCalcul());

        // Cr�er l'annonce ponctuelle.
        REAnnoncePonctuelleHelper helper = new REAnnoncePonctuelleHelper();

        helper.ajouterAnnoncePonctuelle(vb, null, session);
    }


    /**
     * @param session
     * @param transaction
     * @param rentesAccordees
     * @param noCasATraiter
     * @param fCalcul
     * @return L'id de la copie de la demande si la demande original � �t� copi�
     * @throws PRACORException
     * @throws Exception
     */
    private Long importationDesAnnoncesDuCalculACOR(final BSession session, final BITransaction transaction,
                                                    final LinkedList<Long> rentesAccordees, final int noCasATraiter, final REDemandeRente demande, final FCalcul fCalcul)
            throws PRACORException, Exception {


        ReturnedValue returnedValue = REAcorMapper.doMAJPrestations(session, transaction, demande, fCalcul, noCasATraiter);

        rentesAccordees.addAll(returnedValue.getIdRenteAccordees());
        remarquesParticulieres = returnedValue.getRemarquesParticulieres();
        /*
         * Maj de qqes donn�es suppl�mentaire r�cup�r�e depuis la
         * feuille de calcul acor taux de reduction, BTE enti�re, demi, quart...
         */
        if ((fCalcul != null) && (fCalcul.getEvenement().size() > 0)) {
           rentesWithoutBte = REAcorMapper.doMAJExtraData(session, (BTransaction) transaction, fCalcul, rentesAccordees);
        }

        // Connexion au WebService ACOR pour r�cup�rer les annonces.
        PoolMeldungZurZAS annonces = REAcorAnnoncesService.getInstance().getAnnonces(fCalcul);
        if (Objects.nonNull(annonces)) {
            REImportAnnoncesAcor.getInstance().importAnnonces(session, (BTransaction) transaction, annonces, rentesAccordees);
        }

        return returnedValue.getIdCopieDemande();
    }

    /**
     * @param session
     * @param transaction
     * @param rentesAccordees
     * @param noCasATraiter
     * @param demande
     * @param resultat9
     * @return L'id de la copie de la demande si la demande original � �t� copi�
     * @throws PRACORException
     * @throws Exception
     */
    private Long importationDesAnnoncesDuCalculACOR9(final BSession session, final BITransaction transaction,
                                                     final LinkedList<Long> rentesAccordees, final int noCasATraiter, final REDemandeRente demande, final Resultat9 resultat9)
            throws PRACORException, Exception {

        // On r�cup�re l'ancien fichier .pay pour conserver le mapping
            StringReader annoncePayReader = new StringReader(resultat9.getAnnexes().getPay());
            // Ancien parsing du fichier annonce.pay
            REACORParser.ReturnedValue returnedValue = REACORParser.parse(session, transaction, demande,
                    annoncePayReader, noCasATraiter);

            rentesAccordees.addAll(returnedValue.getIdRenteAccordees());

            return returnedValue.getIdCopieDemande();
    }


    /**
     * Mandat InfoRom D0112 : ajout de remarque dans la d�cision si rente veuf limit�e Il s'agit de rechercher une
     * phrase contenue dans le feuille de calcul ACOR Si cette phrase est pr�sente, mise � jour du champ WCBVLI dans
     * PRINFCOM. Le but de la mise � jour de ce champs est; lors de la pr�paration de la d�cision, si ce champs est �
     * vrai, des remarques seront automatiquement ins�r�e dans la d�cision
     *
     * @param session
     * @param transaction
     * @throws Exception
     */
    private void traiterLesRemarquesParticulieresDeLaFeuilleDeCalculAcor(final BSession session, final BITransaction transaction, String idDemande) throws Exception {

        boolean isRenteLimitee = false;
        boolean isRenteAvecSupplementPourPersonneVeuve = false;
        boolean isRenteAvecDebutDroit5AnsAvantDepotDemande = false;
        boolean isRenteAvecMontantMinimumMajoreInvalidite = false;
        boolean isRenteReduitePourSurassurance = false;

        // Si la feuille de calcul existe, on va rechercher si des remarques particuli�re sont pr�sentes
        if (!remarquesParticulieres.isEmpty()) {
            final String renteLimitee18ANSPlusJeuneEnfant = session.getLabel("CALCULER_DECISION_CLE_ACOR_RENTE_LIMITEE_18ANS_PLUS_JEUNE_ENFANT");
            isRenteLimitee = remarquesParticulieres.stream().anyMatch(each -> each.contains(renteLimitee18ANSPlusJeuneEnfant));

            final String montantAvecSupplementPersonneVeuve = session.getLabel("CALCULER_DECISION_CLE_ACOR_MONTANT_AVEC_SUPPLEMENT_PERSONNE_VEUVE");
            isRenteAvecSupplementPourPersonneVeuve = remarquesParticulieres.stream().anyMatch(each -> each.contains(montantAvecSupplementPersonneVeuve));

            final String debutDroit5AnsAvantDepotDemande = session.getLabel("CALCULER_DECISION_CLE_ACOR_DEBUT_DROIT_5ANS_AVANT_DEPOT_DEMANDE");
            isRenteAvecDebutDroit5AnsAvantDepotDemande = remarquesParticulieres.stream().anyMatch(each -> each.contains(debutDroit5AnsAvantDepotDemande));

            final String montantMinimumMajoreInvalidite = session.getLabel("CALCULER_DECISION_CLE_ACOR_MONTANT_MINIMUM_MAJORE_INVALIDITE");
            isRenteAvecMontantMinimumMajoreInvalidite = remarquesParticulieres.stream().anyMatch(each -> each.contains(montantMinimumMajoreInvalidite));

            final String renteReduitePourSurassurance = session.getLabel("CALCULER_DECISION_CLE_ACOR_RENTE_REDUITE_POUR_SURASSURANCE");
            isRenteReduitePourSurassurance = remarquesParticulieres.stream().anyMatch(each -> each.contains(renteReduitePourSurassurance));

        }
        // Dans tous les cas on met � jour les champs des infos compl�mentaires
        miseAJourInfoComplementaire(session, transaction, idDemande, isRenteLimitee,
                isRenteAvecSupplementPourPersonneVeuve, isRenteAvecDebutDroit5AnsAvantDepotDemande,
                isRenteAvecMontantMinimumMajoreInvalidite, isRenteReduitePourSurassurance);
    }

    /**
     * R�cup�re ou cr�er les info compl�mentaire li�es � la demande et renseigne les flags boolean li�s au rente avec
     * remarque particuli�re
     *
     * @param session
     * @param transaction
     * @param idDemande
     * @param hasRenteLimitee
     * @param isRenteAvecSupplementPourPersonneVeuve
     * @param isRenteAvecDebutDroit5AnsAvantDepotDemande
     * @throws Exception
     */
    private void miseAJourInfoComplementaire(final BSession session, final BITransaction transaction,
                                             final String idDemande, final boolean hasRenteLimitee, final boolean isRenteAvecSupplementPourPersonneVeuve,
                                             final boolean isRenteAvecDebutDroit5AnsAvantDepotDemande,
                                             final boolean isRenteAvecMontantMinimumMajoreInvalidite, final boolean isRenteReduitePourSurassurance)
            throws Exception {
        if (JadeStringUtil.isBlankOrZero(idDemande)) {
            String message = session.getLabel("CALCULER_DECISION_ERREUR_ID_DEMANDE_EST_VIDE");
            throw new Exception(message);
        }
        REDemandeRente demande = new REDemandeRente();
        demande.setSession(session);
        demande.setIdDemandeRente(idDemande);
        demande.retrieve(transaction);
        if (demande.isNew()) {
            String message = session.getLabel("CALCULER_DECISION_ERREUR_IMPOSSIBLE_TROUVER_DEMANDE");
            message = message.replace("{0}", idDemande);
            throw new Exception(message);
        }
        PRInfoCompl infoComplementaire = getOrCreateInfoComplementaire(session, (BTransaction) transaction, demande);
        infoComplementaire.setIsRenteLimitee(hasRenteLimitee);
        infoComplementaire.setIsRenteAvecSupplementPourPersonneVeuve(isRenteAvecSupplementPourPersonneVeuve);
        infoComplementaire.setIsRenteAvecDebutDroit5AnsAvantDepotDemande(isRenteAvecDebutDroit5AnsAvantDepotDemande);
        infoComplementaire.setIsRenteAvecMontantMinimumMajoreInvalidite(isRenteAvecMontantMinimumMajoreInvalidite);
        infoComplementaire.setIsRenteReduitePourSurassurance(isRenteReduitePourSurassurance);
        infoComplementaire.update(transaction);
    }

    /**
     * R�cup�re les info compl�mentaire li�es � la demande si elle existent, le cas �ch�ant les info compl�mentaire
     * seront cr�es et r�f�renc� dans la demande
     *
     * @return Dans tous les cas, l'entit� PRInfoCompl associ�e � la demande
     * @throws Exception S'il n'est pas possible de r�cup�rer les infos compl
     */
    private PRInfoCompl getOrCreateInfoComplementaire(final BSession session, final BTransaction transaction,
                                                      final REDemandeRente demande) throws Exception {
        PRInfoCompl infoComplementaire = new PRInfoCompl();
        infoComplementaire.setSession(session);

        String idInfoComplementaire = demande.getIdInfoComplementaire();

        // Si les info compl n'existent pas, on cr�er l'entit� et on la r�f�rence dans la demande
        if (JadeStringUtil.isBlankOrZero(idInfoComplementaire)) {
            infoComplementaire.add(transaction);
            demande.setIdInfoComplementaire(infoComplementaire.getIdInfoCompl());
            demande.update(transaction);
        }
        // Si les info compl existent, on tente de la r�cup�rer
        else {
            infoComplementaire.setIdInfoCompl(idInfoComplementaire);
            infoComplementaire.retrieve(transaction);
            if (infoComplementaire.isNew()) {
                String message = session
                        .getLabel("CALCULER_DECISION_ERREUR_IMPOSSIBLE_TROUVER_INFORMATION_COMPLEMENTAIRE");
                message = message.replace("{0}", idInfoComplementaire);
                throw new Exception(message);
            }
        }
        return infoComplementaire;
    }

    private void calculerEtSauverRentesVerseesATort(final BSession session, final BTransaction transaction,
                                                    final Long idDemandeRente, final List<Long> idsRA) throws Exception {

        // suppression des rentes vers�es � tort de la demande (dans le cas d'une r�-importation)
        RERenteVerseeATortManager renteVerseeATortManager = new RERenteVerseeATortManager();
        renteVerseeATortManager.setSession(session);
        renteVerseeATortManager.setForIdDemandeRente(idDemandeRente);
        renteVerseeATortManager.find(BManager.SIZE_NOLIMIT);

        for (RERenteVerseeATort uneRenteVerseeATortDejaExistante : renteVerseeATortManager.getContainerAsList()) {
            uneRenteVerseeATortDejaExistante.delete(transaction);
        }

        // calcul des rentes pour la demande import�e
        RECalculRentesVerseesATortManager calculRentesVerseesATortManager = new RECalculRentesVerseesATortManager();
        calculRentesVerseesATortManager.setSession(session);
        calculRentesVerseesATortManager.setIdsRenteAccordeeNouveauDroit(new HashSet<Long>(idsRA));
        calculRentesVerseesATortManager.find(BManager.SIZE_NOLIMIT);

        RECalculRentesVerseesATortWrapper wrapper = RECalculRentesVerseesATortConverter
                .convertToWrapper(calculRentesVerseesATortManager.getContainerAsList());
        // si aucune rente, l'ID de la demande ne sera pas assign�
        if (wrapper.getIdDemandeRente() == null) {
            wrapper.setIdDemandeRente(idDemandeRente);
        }

        String moisDernierPaiement = REPmtMensuel.getDateDernierPmt(session);

        Collection<REDetailCalculRenteVerseeATort> detailsCalculRentesVerseesATort = RECalculRentesVerseesATort
                .calculerRentesVerseesATort(wrapper, moisDernierPaiement);
        Collection<RERenteVerseeATort> rentesVerseesATort = RECalculRentesVerseesATortConverter
                .convertToEntity(detailsCalculRentesVerseesATort);

        for (RERenteVerseeATort uneRenteVerseeATort : rentesVerseesATort) {
            if (uneRenteVerseeATort.getIdRenteVerseeATort() != null) {
                RERenteVerseeATort renteVerseeATortEnBase = new RERenteVerseeATort();
                renteVerseeATortEnBase.setSession(session);
                renteVerseeATortEnBase.setIdRenteVerseeATort(uneRenteVerseeATort.getIdRenteVerseeATort());
                renteVerseeATortEnBase.retrieve(transaction);

                renteVerseeATortEnBase.setIdDemandeRente(uneRenteVerseeATort.getIdDemandeRente());
                renteVerseeATortEnBase
                        .setIdRenteAccordeeAncienDroit(uneRenteVerseeATort.getIdRenteAccordeeAncienDroit());
                renteVerseeATortEnBase
                        .setIdRenteAccordeeNouveauDroit(uneRenteVerseeATort.getIdRenteAccordeeNouveauDroit());
                renteVerseeATortEnBase.setTypeRenteVerseeATort(uneRenteVerseeATort.getTypeRenteVerseeATort());
                renteVerseeATortEnBase.setMontant(uneRenteVerseeATort.getMontant());
                renteVerseeATortEnBase.update(transaction);
            } else {
                if (uneRenteVerseeATort.getIdRenteAccordeeNouveauDroit() != null
                        && uneRenteVerseeATort.getIdRenteAccordeeNouveauDroit() != 0) {
                    uneRenteVerseeATort.setSession(session);
                    uneRenteVerseeATort.add(transaction);
                }
            }
        }
    }

    /**
     * @param idTiersRequrant   L'id tiers du requ�rant de la demande pour laquelle on remonte le calcul
     * @param idDemandeCourante Id de la demande pour laquelle on est en train de remonter le calcul
     * @throws Exception
     */
    private String separerLesDemandesSiBesoin(final Long idTiersRequrant, long idDemandeCourante) throws
            Exception {

        BSession session = BSessionUtil.getSessionFromThreadContext();
        if (session == null) {
            throw new RETechnicalException(
                    "separerLesDemandesSiBesoin(idTiersRequrant, idDemandeCourante) : Unable to get a  session from the threadContext");
        }

        if (idDemandeCourante == 0) {
            String message = session.getLabel("IMPORTATION_CALCUL_ACOR_IMPOSSIBLE_RETROUVER_DEMANDE_COURANTE_ID_0");
            throw new REBusinessException(message);
        }

        PersonneAVS requerant = PyxisCrudServiceLocator.getPersonneAvsCrudService().read(idTiersRequrant);
        Set<DemandeRente> demandesDeLaFamille = CorvusServiceLocator.getDemandeRenteService()
                .demandesDuRequerantEtDeSaFamille(requerant);

        // En premier lieu il faut r�cup�rer la demande courante avant le filtrage sous peine de probl�me dans le cas de
        // calcul de demande valid�e
        DemandeRente demandeCourante = recupererDemandeCourante(demandesDeLaFamille, idDemandeCourante);
        if (demandeCourante == null) {
            String message = session.getLabel("IMPORTATION_CALCUL_ACOR_IMPOSSIBLE_RETROUVER_DEMANDE_COURANTE");
            message = message.replace("{0}", Long.toString(idDemandeCourante));
            throw new REBusinessException(message);
        }

        // On ne prend que les demandes non valid�es
        demandesDeLaFamille = demandesNonValidees(demandesDeLaFamille);
        // de type standard ou transitoire
        demandesDeLaFamille = demandesStandardsOuTransitoires(demandesDeLaFamille);
        // sans les demandes API
        demandesDeLaFamille = filtrerDemandeAPI(demandesDeLaFamille);

        /*
         * R�cup�ration des base de calcul de la demande courante pour voir si des CCS 08 sont pr�sent ou pas dans les
         * bases de calcul
         */
        List<BaseCalculWrapper> basesCalculsDemandeCourante = getBCWrapperDeLaDemande(demandeCourante, session);

        boolean bcAvecCCS08Present = false;
        boolean bcAvecCCS08Absent = false;
        for (BaseCalculWrapper baseCalcul : basesCalculsDemandeCourante) {
            if (baseCalcul.hasCodeCasSpecial08()) {
                bcAvecCCS08Present = true;
            } else {
                bcAvecCCS08Absent = true;
            }
        }

        /*
         * G�n�ration des cl� de comparaison pour les demandes ouvertes de la famille
         */
        DemandeRenteWrapper demandeCouranteWrapper = new DemandeRenteWrapper(demandeCourante);
        Set<DemandeRenteWrapper> demandesDeLaFamilleWrappers = new HashSet<DemandeRenteWrapper>();
        for (DemandeRente demandeRente : demandesDeLaFamille) {
            DemandeRenteWrapper demandeRenteCle = new DemandeRenteWrapper(demandeRente);
            demandesDeLaFamilleWrappers.add(demandeRenteCle);
        }

        /*
         * Le fait que l'on trouve des bases de calcul avec CCS 08 et d'autres sans CCS 08 va d�terminer la fa�on dont
         * on va dispatcher les BC dans les demandes ouvertes de la familles
         */
        String message = null;
        if (bcAvecCCS08Present) {
            if (bcAvecCCS08Absent) {
                message = deplacerRenteAccordeesModeB(requerant, demandeCouranteWrapper, basesCalculsDemandeCourante,
                        demandesDeLaFamilleWrappers, session);
            } else {
                deplacerRenteAccordeesModeA(requerant, demandeCouranteWrapper, basesCalculsDemandeCourante,
                        demandesDeLaFamilleWrappers, session);
            }
        } else {
            deplacerRenteAccordeesModeA(requerant, demandeCouranteWrapper, basesCalculsDemandeCourante,
                    demandesDeLaFamilleWrappers, session);
        }

        miseAjourDateDeLaDemande(session, demandeCourante.getId());
        miseAjourDateDesDemandes(session, demandesDeLaFamilleWrappers);
        return message;
    }

    /**
     * Recherche la demande en fonction de l'id <code>idDemandeCourante</code> pass� en argument</br>
     * 1 - Boucle sur les demandes afin de trouver la demande recherch�e en fonction de son id
     * 2 - Si la demande � �t� trouv�e, elle sera supprim� du Set de demandes <code>demandes</code> M�thode safe si
     * demandes est null
     *
     * @param demandes          Le Set de demandes dans lequel la recherche sera effectu�
     * @param idDemandeCourante L'id de la demande � r�cup�rer
     * @return La demande courante si trouv�e sinon null.
     */
    private DemandeRente recupererDemandeCourante(Set<DemandeRente> demandes, long idDemandeCourante) {
        DemandeRente demandeCourante = null;
        if (demandes != null) {
            for (DemandeRente demandeRente : demandes) {
                if (idDemandeCourante == demandeRente.getId()) {
                    demandeCourante = demandeRente;
                    break;
                }
            }
        }

        if (demandeCourante != null) {
            demandes.remove(demandeCourante);
        }
        return demandeCourante;
    }

    /**
     * Filtre les demandes de type API. Retourne un Set de DemandeRente sans les rentes API.
     * M�thode safe face � l'argument <code>demandes</code> null.
     *
     * @param demandes Les demandes � filtrer
     * @return Un Set de DemandeRente sans les demandes de type API. <strong>Ne retourne jamais null</strong>
     */
    private Set<DemandeRente> filtrerDemandeAPI(Set<DemandeRente> demandes) {
        Set<DemandeRente> demandesFiltrees = new HashSet<DemandeRente>();
        if (demandes != null && demandes.size() > 0) {
            for (DemandeRente demande : demandes) {
                if (!TypeDemandeRente.DEMANDE_API.equals(demande.getTypeDemandeRente())) {
                    demandesFiltrees.add(demande);
                }
            }
        }
        return demandesFiltrees;
    }

    /**
     * @param demandeCourante
     * @param session
     * @throws Exception
     */
    private List<BaseCalculWrapper> getBCWrapperDeLaDemande(DemandeRente demandeCourante, BSession session)
            throws Exception {
        List<BaseCalculWrapper> basesCalculWrappers = new ArrayList<>();

        REDemandeRente demandeEntity = new REDemandeRente();
        demandeEntity.setSession(session);
        demandeEntity.setIdDemandeRente(demandeCourante.getId().toString());
        demandeEntity.retrieve();
        if (demandeEntity.isNew()) {
            String message = session.getLabel("IMPORTATION_CALCUL_ACOR_IMPOSSIBLE_RETROUVER_DEMANDE");
            message = message.replace("{0}", demandeCourante.getId().toString());
            throw new REBusinessException(message);
        }

        if (!JadeStringUtil.isBlankOrZero(demandeEntity.getIdRenteCalculee())) {

            REBasesCalculManager manager = new REBasesCalculManager();
            manager.setSession(session);
            manager.setForIdRenteCalculee(demandeEntity.getIdRenteCalculee());
            manager.find(BManager.SIZE_NOLIMIT);

            RERenteAccordeeManager raManager = new RERenteAccordeeManager();
            raManager.setSession(session);

            // Conversion ou wrapping des base de calcul dans un objet qui nous permet d'analyser les infos voulue
            for (Object o : manager.getContainer()) {
                REBasesCalcul basesCalcul = (REBasesCalcul) o;
                raManager.setForIdBaseCalcul(basesCalcul.getId());
                raManager.find(BManager.SIZE_NOLIMIT);
                List<RERenteAccordee> ras = new ArrayList<RERenteAccordee>();
                for (Object object : raManager.getContainer()) {
                    ras.add((RERenteAccordee) object);
                }
                basesCalculWrappers.add(new BaseCalculWrapper(basesCalcul, ras));
            }
        }
        return basesCalculWrappers;
    }

    /**
     * R�partis les rentes accord�es selon le mod�le B.
     * Le mod�le B de r�partition des rentes accord�es est utilis� lorsque des rentes accord�es contiennent des CCS 08
     * et d'autres non
     *
     * @param demandeCouranteCle
     * @param demandesDeLaFamilleWrapper
     * @throws Exception
     */
    private String deplacerRenteAccordeesModeB(PersonneAVS requerant, DemandeRenteWrapper demandeCouranteCle,
                                               List<BaseCalculWrapper> basesCalculsDemandeCourante, Set<DemandeRenteWrapper> demandesDeLaFamilleWrapper,
                                               BSession session) throws Exception {

        List<String> messages = new ArrayList<String>();

        /**
         * Ce qui nous int�resse dans un premier temps est de savoir si la demande est ajourn�e et si la date de
         * r�vocation de l'ajournement est vide (la m�thode isAjournement() nous donne cette info).
         * Si c'est le cas :
         * - On laisse les base de calcul contenant des CCS 08 et on enl�ve les BCs qui n'en ont pas
         * Si ce n'est pas le cas, on effectue le traitement inverse .
         * - On enl�ve les base de calcul contenant des CCS 08 et on laisse les BCs qui n'en ont pas
         */
        List<BaseCalculWrapper> baseCalculAvecCCS08ADeplacer = new ArrayList<BaseCalculWrapper>();
        List<BaseCalculWrapper> baseCalculSansCCS08ADeplacer = new ArrayList<BaseCalculWrapper>();
        boolean deplacementBCAvecCCS08 = false;
        boolean deplacementBCSansCCS08 = false;

        // D�placement des BC SANS CCS 08
        if (demandeCouranteCle.isAjournement()) {
            deplacementBCSansCCS08 = true;
            for (BaseCalculWrapper bc : basesCalculsDemandeCourante) {
                if (!bc.hasCodeCasSpecial08()) {
                    baseCalculSansCCS08ADeplacer.add(bc);
                }
            }
        }
        // D�placement des BC AVEC CCS 08
        else {
            deplacementBCAvecCCS08 = true;
            for (BaseCalculWrapper bc : basesCalculsDemandeCourante) {
                if (bc.hasCodeCasSpecial08()) {
                    baseCalculAvecCCS08ADeplacer.add(bc);
                }
            }
        }

        // --------------------------------------------------//
        Set<DemandeRente> demandesValideesDeLaFamille = CorvusServiceLocator.getDemandeRenteService()
                .demandesDuRequerantEtDeSaFamille(requerant);

        // On ne prend que les demandes valid�es
        demandesValideesDeLaFamille = demandesValidees(demandesValideesDeLaFamille);
        // de type standard ou transitoire
        demandesValideesDeLaFamille = demandesStandardsOuTransitoires(demandesValideesDeLaFamille);
        // sans les demandes API
        demandesValideesDeLaFamille = filtrerDemandeAPI(demandesValideesDeLaFamille);

        List<DemandeRenteWrapper> demandesValideesDeLaFamilleWrappers = new LinkedList<DemandeRenteWrapper>();
        for (DemandeRente demandeRente : demandesValideesDeLaFamille) {
            demandesValideesDeLaFamilleWrappers.add(new DemandeRenteWrapper(demandeRente));
        }
        Collections.sort(demandesValideesDeLaFamilleWrappers);

        // --------------------------------------------------//

        /**
         * La il peu y avoir soit un d�placement des bases de calcul avec CCS 08, soit d�placement des bases de calcul
         * sans CS08
         */

        /**
         * 1er cas (ajournement = non)
         * ajournement = false OU date de r�vocation ajournement n'est pas vide
         * On doit d�placer les bases de calcul avec CCS 08
         */
        List<BaseCalculWrapper> baseCalculADeplacer = new ArrayList<BaseCalculWrapper>();
        if (deplacementBCAvecCCS08 && baseCalculAvecCCS08ADeplacer.size() > 0) {
            baseCalculADeplacer.addAll(baseCalculAvecCCS08ADeplacer);
        }
        /**
         * 2�me cas
         * On doit d�placer les bases de calcul SANS CCS 08
         * Dans ce cas, on vas rechercher
         */
        else if (deplacementBCSansCCS08 && baseCalculSansCCS08ADeplacer.size() > 0) {
            baseCalculADeplacer.addAll(baseCalculSansCCS08ADeplacer);
        }

        if (baseCalculADeplacer.size() > 0) {
            Iterator<BaseCalculWrapper> iterator = baseCalculADeplacer.iterator();

            // On boucle sur toute les bases de calcul a d�placer

            while (iterator.hasNext()) {
                BaseCalculWrapper baseDeCalcul = iterator.next();
                String cleBaseCalcul = baseDeCalcul.getCleDeComparaison();
                boolean deplace = false;

                // On a la base de calcul � d�placer, on va regarder dans les demandes ouverte de la famille s'il est
                // possible de d�placer la BC dans une de ces demandes
                for (DemandeRenteWrapper demande : demandesDeLaFamilleWrapper) {

                    // Si la cl� de la BC est �gal � la cl� de la demande on peut d�placer la BC dans la demande
                    if (cleBaseCalcul.equals(demande.getCleDeComparaison())) {
                        deplacerBaseDeCalculDansDemande(session, baseDeCalcul.getBasesCalcul(),
                                demande.getDemandeRente().getId().toString());
                        deplace = true;
                        break;
                    }
                }

                /*
                 * Si la base de calcul n'a pas pu �tre d�plac�e c'est qu'on � pas trouver de demande ouverte du bon
                 * type.
                 * Il faut voir s'il y a possibilit� de copier un demande existante
                 */
                if (!deplace) {
                    // On boucle sur les demandes valid�es de la famille
                    for (DemandeRenteWrapper demandeValidee : demandesValideesDeLaFamilleWrappers) {

                        /*
                         * Si la cl� de la BC correspond � la demande, on va copier la demande et d�placer la base de
                         * calcul dans la nouvelle demande
                         */
                        if (cleBaseCalcul.equals(demandeValidee.getCleDeComparaison())) {
                            REDemandeRente copie = deplacerBaseDeCalculDansCopieDeLaDemande(session,
                                    baseDeCalcul.getBasesCalcul(), demandeValidee);

                            /*
                             * On ajoute la nouvelle demande non valid�e que l'on vient de copier dans la liste des
                             * demande non valid�es de la famille. Si ce n'est pas fait et qu'il y a plusieurs base de
                             * calcul � d�placer on risque de retrouver des copies � double
                             */
                            DemandeRente demandeDomain = CorvusCrudServiceLocator.getDemandeRenteCrudService()
                                    .read(new Long(copie.getIdDemandeRente()));
                            demandesDeLaFamilleWrapper.add(new DemandeRenteWrapper(demandeDomain));
                            deplace = true;
                            if (deplace) {
                                break;
                            }
                        }
                    }
                    /*
                     * Si on � pas trouv� de demande correspondant � la BC, on va notifier l'utilisateur
                     */
                    if (!deplace) {
                        String userMessage = session
                                .getLabel("IMPORTATION_CALCUL_ACOR_IMPOSSIBLE_DEPLACER_BASE_CALCUL");

                        String typeDemande = session
                                .getCodeLibelle(String.valueOf(baseDeCalcul.getTypeDemandeRente().getCodeSysteme()));

                        String ajournement = " ";
                        if (baseDeCalcul.isAjournement()) {
                            ajournement = " " + session.getLabel("IMPORTATION_CALCUL_ACOR_AJOURNEMENT") + " ";
                        }

                        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session,
                                baseDeCalcul.getBasesCalcul().getIdTiersBaseCalcul());
                        String infoTiers = tiers.getNom() + " " + tiers.getPrenom() + " " + tiers.getNSS();

                        userMessage = userMessage.replace("{0}", typeDemande);
                        userMessage = userMessage.replace("{1}", ajournement);
                        userMessage = userMessage.replace("{2}", infoTiers);
                        messages.add(userMessage);
                    }
                }
            }

        }
        StringBuilder sb = new StringBuilder();
        if (messages.size() > 0) {
            for (String m : messages) {
                sb.append(m).append("</br>");
            }
        }
        return sb.toString();
    }

    /**
     * Filtre les demandes et retourne les demandes dans l'�tat VALIDE et COURANT_VALIDE
     *
     * @param demandes Les demandes � filtrer
     * @return Les demandes filtr�es
     */
    private Set<DemandeRente> demandesValidees(final Set<DemandeRente> demandes) {
        Set<DemandeRente> demandesValidees = new HashSet<>();
        if (demandes != null) {
            for (DemandeRente uneDemande : demandes) {
                switch (uneDemande.getEtat()) {
                    case VALIDE:
                    case COURANT_VALIDE:
                        demandesValidees.add(uneDemande);
                        break;
                    default:
                        break;
                }
            }
        }
        return demandesValidees;
    }

    /**
     * D�place la base de calcul dans la demande
     *
     * @param session
     * @param basesCalcul La base de calcul � d�placer
     * @param idDemande
     * @throws Exception
     */
    private void deplacerBaseDeCalculDansDemande(BSession session, REBasesCalcul basesCalcul, String idDemande)
            throws Exception {

        // 1 - R�cup�ration de la demande
        if (JadeStringUtil.isBlankOrZero(idDemande)) {
            String message = session
                    .getLabel("IMPORTATION_CALCUL_ACOR_IMPOSSIBLE_RETROUVER_DEMANDE_POUR_INSERER_BASE_CALCUL");
            message = message.replace("{0}", idDemande);
            message = message.replace("{1}", basesCalcul.getIdBasesCalcul());
            throw new REBusinessException(message);
        }

        REDemandeRente demande = new REDemandeRente();
        demande.setSession(session);
        demande.setId(idDemande);
        demande.retrieve();

        if (demande.isNew()) {
            String message = session
                    .getLabel("IMPORTATION_CALCUL_ACOR_IMPOSSIBLE_RETROUVER_DEMANDE_POUR_INSERER_BASE_CALCUL");
            message = message.replace("{0}", demande.getId().toString());
            message = message.replace("{1}", basesCalcul.getIdBasesCalcul());
            throw new REBusinessException(message);
        }

        // 2 - Est-ce qu'une rente calcul�e existe pour cette demande
        RERenteCalculee renteCalculee = new RERenteCalculee();
        renteCalculee.setSession(session);

        String idRenteCalculee = demande.getIdRenteCalculee();
        if (!JadeStringUtil.isBlankOrZero(idRenteCalculee)) {
            renteCalculee.setIdRenteCalculee(idRenteCalculee);
            renteCalculee.retrieve();

            if (renteCalculee.isNew()) {
                // Ok, on a un id rente calcul�e dans la demande mais pas de rente calcul�e... va cr�er la rente
                // calcul�e
                renteCalculee.save();
                demande.setIdRenteCalculee(renteCalculee.getIdRenteCalculee());
            }
        }
        // Pas de rente calcul�e -> on la cr��e
        else {
            renteCalculee.save();
            demande.setIdRenteCalculee(renteCalculee.getIdRenteCalculee());
        }
        demande.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
        demande.update();

        basesCalcul.setIdRenteCalculee(renteCalculee.getIdRenteCalculee());
        basesCalcul.update();

    }


    /**
     * Cr�er une copie de la demande et d�place la rente accord�es dedans
     *
     * @param session
     * @param baseDeCalcul    La base de calcul � d�placer
     * @param demandeOriginal
     * @return <code>true</code> si le d�placement � pu �tre r�alis�
     * @throws Exception
     * @throws REReglesException
     */
    private REDemandeRente deplacerBaseDeCalculDansCopieDeLaDemande(BSession session, REBasesCalcul
            baseDeCalcul,
                                                                    DemandeRenteWrapper demandeOriginal) throws REReglesException, Exception {

        // Recherche du bon type de demande avant la copie
        TypeDemandeRente type = demandeOriginal.getDemandeRente().getTypeDemandeRente();
        REDemandeRente demande = null;

        switch (type) {
            case DEMANDE_INVALIDITE:
                demande = new REDemandeRenteInvalidite();
                break;
            case DEMANDE_SURVIVANT:
                demande = new REDemandeRenteSurvivant();
                break;
            case DEMANDE_VIEILLESSE:
                demande = new REDemandeRenteVieillesse();
                break;
            case DEMANDE_API:
            default:
                throw new Exception("Impossible de copier la demande. Le type [" + type + "] de la demande avec l'id ["
                        + demandeOriginal.getDemandeRente().getId() + "] est inccorecte ");
        }

        demande.setSession(session);
        demande.setId(demandeOriginal.getDemandeRente().getId().toString());
        demande.retrieve();
        if (demande.isNew()) {
            String message = session.getLabel("IMPORTATION_CALCUL_ACOR_IMPOSSIBLE_RETROUVER_DEMANDE_POUR_COPIE");
            message = message.replace("{0}", demande.getId().toString());
            throw new REBusinessException(message);
        }

        REDemandeRente copieDemande = REDemandeRegles.copierDemandeRente(session, session.getCurrentThreadTransaction(),
                demande);
        if (copieDemande == null) {
            String message = session.getLabel("IMPORTATION_CALCUL_ACOR_IMPOSSIBLE_COPIER_DEMANDE");
            message = message.replace("{0}", demande.getId().toString());
            message = message.replace("{1}", baseDeCalcul.getIdBasesCalcul());
            throw new REBusinessException(message);
        }
        deplacerBaseDeCalculDansDemande(session, baseDeCalcul, copieDemande.getIdDemandeRente());
        return copieDemande;
    }

    /**
     * R�partis les rentes accord�es selon le mod�le A.
     * Le mod�le A de r�partition des rentes accord�es est utilis� lorsque de code cas sp�ciaux 08 sont pr�sent dans au
     * moins une des RA de toutes les demandes ouverte de la famille
     *
     * @param demandeCouranteCle
     * @param demandesDeLaFamilleCle
     * @throws Exception
     */
    private void deplacerRenteAccordeesModeA(PersonneAVS requerant, DemandeRenteWrapper demandeCouranteCle,
                                             List<BaseCalculWrapper> basesCalculsDemandeCourante, Set<DemandeRenteWrapper> demandesDeLaFamilleCle,
                                             BSession session) throws Exception {
        // Si des demandes ouverte existent pour la famille. Sinon on ne fait rien
        if (demandesDeLaFamilleCle != null && demandesDeLaFamilleCle.size() > 0) {
            String cleDemandeCourante = demandeCouranteCle.getCleDeComparaison();

            // On va boucler sur toutes les bases de calcul de la demande courante
            for (BaseCalculWrapper bcDemandeCourante : basesCalculsDemandeCourante) {

                String cleBaseCalcul = bcDemandeCourante.getCleDeComparaison();

                // Si la cl� de la base de calcul est la m�me que la demande courante, on ne la d�place pas
                if (!cleDemandeCourante.equals(cleBaseCalcul)) {

                    // On va rechercher dans les demandes ouverte de la famille si une cl� correspond
                    for (DemandeRenteWrapper demandeRenteWrapper : demandesDeLaFamilleCle) {

                        if (cleBaseCalcul.equals(demandeRenteWrapper.getCleDeComparaison())) {
                            // les 2 cl�s sont identiques, on peut d�placer la base de calcul dans la demande
                            deplacerBaseDeCalculDansDemande(session, bcDemandeCourante.getBasesCalcul(),
                                    demandeRenteWrapper.getDemandeRente().getId().toString());
                        }
                    }
                }
            }
        }
    }

    /**
     * Met � jour les date de d�but et de fin de la demande en fonction des dates de d�but et de fin des rentes
     * accord�es.
     * Si aucune rente accord�e n'est pr�sente les dates de la demande ne seront pas mis a jour.
     *
     * @param session la session � utiliser
     * @param id      L'id de la demande � mettre � jour
     * @throws Exception
     */
    private void miseAjourDateDeLaDemande(BSession session, Long id) throws Exception {

        RERenteAccJoinTblTiersJoinDemRenteManager raMgr2 = new RERenteAccJoinTblTiersJoinDemRenteManager();
        raMgr2.setSession(session);
        raMgr2.setForNoDemandeRente(Long.toString(id));
        raMgr2.find();

        // Mise � jour des date de la demande uniquement si au moins une rente accord�e sont pr�sentes
        if (raMgr2.getContainer().size() > 0) {
            REDemandeRente demRente = new REDemandeRente();
            demRente.setIdDemandeRente(Long.toString(id));
            demRente.setSession(session);
            demRente.retrieve();

            String dd = DATE_DEBUT_DEMANDE;
            String df = DATE_FIN_DEMANDE;
            boolean isPeriodeInfinie = false;
            JACalendar cal = new JACalendarGregorian();

            // Boucler sur toutes les ra pour avoir la plus petite date de d�but
            for (RERenteAccJoinTblTiersJoinDemandeRente ra2 : raMgr2.getContainerAsList()) {

                if (cal.compare(dd, ra2.getDateDebutDroit()) == JACalendar.COMPARE_SECONDLOWER) {
                    dd = ra2.getDateDebutDroit();
                }

                if (JadeStringUtil.isBlankOrZero(ra2.getDateFinDroit())) {
                    isPeriodeInfinie = true;
                } else {
                    // pour la date de fin, il faut prendre le dernier jour du mois et non pas le premier comme le fait
                    // les JADate
                    JACalendarGregorian calendar = new JACalendarGregorian();
                    JADate ra2Df = calendar.addMonths(new JADate(ra2.getDateFinDroit()), 1);
                    ra2Df = calendar.addDays(ra2Df, -1);

                    if (cal.compare(df, ra2Df.toStr(".")) == JACalendar.COMPARE_SECONDUPPER) {
                        df = ra2Df.toStr(".");
                    }
                }
            }
            // Mis � jour de la date de d�but uniquement si elle est diff�rente de DATE_DEBUT_DEMANDE
            if (!DATE_DEBUT_DEMANDE.equals(dd)) {
                demRente.setDateDebut(dd);
            }

            // et la plus grande date de fin (ou vide)
            if (isPeriodeInfinie) {
                demRente.setDateFin("");
            } else if (!DATE_FIN_DEMANDE.equals(df)) {
                demRente.setDateFin(df);
            } else {
                demRente.setDateFin("");
            }

            demRente.setDateTraitement(REACORParser.retrieveDateTraitement(demRente));
            demRente.update();
        }

    }

    private void miseAjourDateDesDemandes(BSession session, Set<DemandeRenteWrapper> ids) throws Exception {
        for (DemandeRenteWrapper demande : ids) {
            miseAjourDateDeLaDemande(session, demande.getDemandeRente().getId());
        }
    }


    // Cl� pour le traitement des annonces ponctuelles
    class KeyAP {
        public String genreRente = "";
        public String idTiers = "";
    }

    // Valeur pour le traitement des annonces ponctuelles
    class ValueAP {
        String ancienMontantRA = "";
        String ancienRAM = "";
        String idRA = "";
        List<String> idsRCI = new ArrayList<String>();
        String nouveauMontantRA = "";
        String nouveauRAM = "";
    }

}
