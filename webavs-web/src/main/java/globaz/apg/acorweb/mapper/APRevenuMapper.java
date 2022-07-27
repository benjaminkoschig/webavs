package globaz.apg.acorweb.mapper;

import ch.admin.zas.xmlns.in_apg._0.EmployeurAPG;
import ch.admin.zas.xmlns.in_apg._0.RevenuAPG;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.util.Dates;
import globaz.apg.acor.adapter.plat.APFichierEmployeurPrinter;
import globaz.apg.acor.parser.APACORPrestationsParser;
import globaz.apg.acorweb.service.APExportationCalculAcor;
import globaz.apg.db.droits.*;
import globaz.apg.module.calcul.APReferenceDataParser;
import globaz.apg.module.calcul.salaire.APSalaire;
import globaz.apg.module.calcul.salaire.APSalaireAdapter;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.api.IPRSituationProfessionnelle;
import globaz.prestation.tools.PRSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class APRevenuMapper {

    private static final Logger LOG = LoggerFactory.getLogger(APRevenuMapper.class);
    public static final double POURCENT_TOTAL_VERSE = 100D;

    private static final int ETAT_NORMAL = 1;
    private static final int ETAT_PENDANT_MONTANT_VERSE = 2;
    private static final int ETAT_APRES_MONTANT_VERSE = 3;

    List<APSituationProfessionnelle> situationProfessionnelles;
    APDroitLAPG droit;

    public List<RevenuAPG> map(final BSession session) {
        // return situationProfessionnelle.stream().map(s -> mapRevenu(s, session)).collect(Collectors.toList());
        List<RevenuAPG> revenus = new ArrayList<>();
        int etat = ETAT_NORMAL;
        for(APSituationProfessionnelle sitPro : situationProfessionnelles) {
            String dateDebut = PRACORConst.CA_DATE_VIDE;
            String dateFin = PRACORConst.CA_DATE_VIDE;
            boolean forcer100Pourcent = false;
            switch (etat) {
                case ETAT_NORMAL:
                    if (!JAUtil.isDateEmpty(sitPro.getDateDebut())) {
                        dateFin = Dates.formatSwiss(Dates.toDate(sitPro.getDateDebut()).minusDays(1));;
                        forcer100Pourcent = true;
                        etat = ETAT_PENDANT_MONTANT_VERSE;
                    } else if (!JAUtil.isDateEmpty(sitPro.getDateFin())) {
                        dateFin = sitPro.getDateFin();
                        etat = ETAT_APRES_MONTANT_VERSE;
                    } else if (!JAUtil.isDateEmpty(sitPro.getDateFinContrat())) {
                        dateFin = sitPro.getDateFinContrat();
                    }
                    break;
                case ETAT_PENDANT_MONTANT_VERSE:
                    dateDebut = sitPro.getDateDebut();
                    if (!JAUtil.isDateEmpty(sitPro.getDateFin())) {
                        dateFin = sitPro.getDateFin();
                        etat = ETAT_APRES_MONTANT_VERSE;
                    } else {
                        if (!JAUtil.isDateEmpty(sitPro.getDateFinContrat())) {
                            dateFin = sitPro.getDateFinContrat();
                        }

                        etat = ETAT_NORMAL;
                    }
                    break;
                case ETAT_APRES_MONTANT_VERSE:
                    dateDebut = Dates.formatSwiss(Dates.toDate(sitPro.getDateFin()).plusDays(1));;
                    if (!JAUtil.isDateEmpty(sitPro.getDateFinContrat())) {
                        dateFin = sitPro.getDateFinContrat();
                    }
                    forcer100Pourcent = true;
                    etat = ETAT_NORMAL;
                    break;
                default:
                    throw new CommonTechnicalException("nous sommes tombés dans un état inconnu");

            }
            RevenuAPG revenu = mapRevenu(sitPro, forcer100Pourcent, session);
            if (sitPro.getIsPourcentAutreRemun().booleanValue()) {
                FWCurrency sal = new FWCurrency();
                // on ajoute le salaire de base
                sal.add(revenu.getSalaire());

                // si pourcent, on calcule ce qu'on doit ajouter au salaire de base
                FWCurrency autreRem = new FWCurrency(sitPro.getAutreRemuneration());

                sal.add((sal.doubleValue() / 100) * autreRem.doubleValue());
                revenu.setSalaire(sal.doubleValue());
            }

            revenu.setDebutContrat(Dates.toXMLGregorianCalendar(dateDebut));
            revenu.setFinContrat(Dates.toXMLGregorianCalendar(dateFin));
            revenus.add(revenu);
            while (etat != ETAT_NORMAL) {
                dateDebut = PRACORConst.CA_DATE_VIDE;
                dateFin = PRACORConst.CA_DATE_VIDE;
                forcer100Pourcent = false;
                switch (etat) {
                    case ETAT_NORMAL:
                        if (!JAUtil.isDateEmpty(sitPro.getDateDebut())) {
                            dateFin = Dates.formatSwiss(Dates.toDate(sitPro.getDateDebut()).minusDays(1));
                            ;
                            forcer100Pourcent = true;
                            etat = ETAT_PENDANT_MONTANT_VERSE;
                        } else if (!JAUtil.isDateEmpty(sitPro.getDateFin())) {
                            dateFin = sitPro.getDateFin();
                            etat = ETAT_APRES_MONTANT_VERSE;
                        } else if (!JAUtil.isDateEmpty(sitPro.getDateFinContrat())) {
                            dateFin = sitPro.getDateFinContrat();
                        }
                        break;
                    case ETAT_PENDANT_MONTANT_VERSE:
                        dateDebut = sitPro.getDateDebut();
                        if (!JAUtil.isDateEmpty(sitPro.getDateFin())) {
                            dateFin = sitPro.getDateFin();
                            etat = ETAT_APRES_MONTANT_VERSE;
                        } else {
                            if (!JAUtil.isDateEmpty(sitPro.getDateFinContrat())) {
                                dateFin = sitPro.getDateFinContrat();
                            }

                            etat = ETAT_NORMAL;
                        }
                        break;
                    case ETAT_APRES_MONTANT_VERSE:
                        dateDebut = Dates.formatSwiss(Dates.toDate(sitPro.getDateFin()).plusDays(1));
                        ;
                        if (!JAUtil.isDateEmpty(sitPro.getDateFinContrat())) {
                            dateFin = sitPro.getDateFinContrat();
                        }
                        forcer100Pourcent = true;
                        etat = ETAT_NORMAL;
                        break;
                    default:
                        throw new CommonTechnicalException("nous sommes tombés dans un état inconnu");

                }
                revenu = mapRevenu(sitPro, forcer100Pourcent, session);
                if (sitPro.getIsPourcentAutreRemun().booleanValue()) {
                    FWCurrency sal = new FWCurrency();
                    // on ajoute le salaire de base
                    sal.add(revenu.getSalaire());

                    // si pourcent, on calcule ce qu'on doit ajouter au salaire de base
                    FWCurrency autreRem = new FWCurrency(sitPro.getAutreRemuneration());

                    sal.add((sal.doubleValue() / 100) * autreRem.doubleValue());
                    revenu.setSalaire(sal.doubleValue());
                }

                revenu.setDebutContrat(Dates.toXMLGregorianCalendar(dateDebut));
                revenu.setFinContrat(Dates.toXMLGregorianCalendar(dateFin));
                revenus.add(revenu);
            }
        }
        return revenus;
    }

    private RevenuAPG mapRevenu(APSituationProfessionnelle sitPro, boolean forcer100Pourcent, final BSession session) {
        APEmployeur employeur;
        try {
            employeur = sitPro.loadEmployeur();
        } catch (Exception e) {
            LOG.error(session.getLabel("ERREUR_CHARGEMENT_EMPLOYEUR")+sitPro.getIdEmployeur(), e);
            throw new CommonTechnicalException(session.getLabel("ERREUR_CHARGEMENT_EMPLOYEUR")+sitPro.getIdEmployeur(), e);
        }


        EmployeurAPG employeurAPG = new EmployeurAPG();
        try {
            employeurAPG.setNumeroAffilie(employeur.loadNumero());
        } catch (Exception e) {
            LOG.error("Impossible de récupérer le numéro d'affilié' "+employeur.getIdAffilie(), e);
            throw new CommonTechnicalException("Impossible de récupérer le numéro d'affilié' "+employeur.getIdAffilie(), e);
        }

        employeurAPG.setRaisonSociale(buildNomAffilie(employeur, session));
        RevenuAPG revenu = buildRevenu(sitPro, forcer100Pourcent, session);
        revenu.setEmployeur(employeurAPG);

        revenu.setPrestationNature(Double.valueOf(sitPro.getSalaireNature()));
        revenu.setTypePrestationNature(Integer.valueOf(PRACORConst.csPeriodiciteSalaireToAcor(sitPro.getPeriodiciteSalaireNature())));
        if (!sitPro.getIsPourcentAutreRemun().booleanValue()) {
            // 8. 13e, gratifications, autre salaire
            revenu.setSalaireAutre(Double.valueOf(sitPro.getAutreRemuneration()));
            revenu.setTypeSalaireAutre(Integer.valueOf(PRACORConst.csPeriodiciteSalaireToAcor(sitPro.getPeriodiciteAutreRemun())));
        }
//        revenu.setSalaireIrregulier();
        revenu.setCotisationsAC(false);
        revenu.setCotisationsAVSAIAPG(false);
        revenu.setCotisationsLFA(false);
        return revenu;
    }

    private RevenuAPG buildRevenu(APSituationProfessionnelle sitPro, boolean forcer100Pourcent,BSession session) {
        APSalaireAdapter adapter = new APSalaireAdapter(sitPro);
        RevenuAPG revenu = new RevenuAPG();
        if (sitPro.getIsIndependant().booleanValue()) {
            revenu.setSalaire(Double.valueOf(sitPro.getRevenuIndependant()));
            revenu.setTypeSalaire(Integer.valueOf(PRACORConst.CA_TYPE_SALAIRE_ANNUEL));
//            revenuAPG.setNombreHeuresHebdomadaires(); -> 0 ?
            revenu.setPourcentSalaireVerse(POURCENT_TOTAL_VERSE);
            revenu.setMontantSalaireVerse(Double.valueOf(sitPro.getRevenuIndependant()));
            revenu.setTypeSalaireVerse(Integer.valueOf(PRACORConst.CA_TYPE_SALAIRE_ANNUEL));
            revenu.setStatut(Integer.valueOf(PRACORConst.CA_STATUT_INDEPENDANT));
            revenu.setVerseAQui(Integer.valueOf(PRACORConst.CA_VERSEMENT_POURCENTAGE));
        } else {
            APSalaire salaire = adapter.salairePrincipal();
            revenu.setSalaire(Double.valueOf(salaire.getMontant()));
            revenu.setTypeSalaire(Integer.parseInt(PRACORConst.csPeriodiciteSalaireToAcor(salaire.getCsPeriodiciteSalaire())));
            revenu.setNombreHeuresHebdomadaires(roundNombreHeureSemaine(sitPro.getHeuresSemaine()));
            revenu.setStatut(Integer.valueOf(PRACORConst.CA_STATUT_SALARIE));
            if (!forcer100Pourcent && (adapter.montantVerse() != null)) {
                revenu.setPourcentSalaireVerse(JANumberFormatter.round(Double.valueOf(adapter.montantVerse().getPourcentage()), 0.01, 2,
                        JANumberFormatter.NEAR));
                revenu.setMontantSalaireVerse(Double.valueOf(adapter.montantVerse().getMontant()));
                revenu.setTypeSalaireVerse(Integer.valueOf(PRACORConst.csPeriodiciteSalaireToAcor(adapter.montantVerse().getCsPeriodiciteSalaire())));
                revenu.setVerseAQui(Integer.valueOf(adapter.montantVerse().isPourcent() ? PRACORConst.CA_VERSEMENT_POURCENTAGE
                        : PRACORConst.CA_VERSEMENT_MONTANT));
            } else {
                revenu.setPourcentSalaireVerse(POURCENT_TOTAL_VERSE);
                revenu.setMontantSalaireVerse(Double.valueOf(adapter.salairePrincipal().getMontant()));
                revenu.setTypeSalaireVerse(Integer.valueOf(PRACORConst.csPeriodiciteSalaireToAcor(adapter.salairePrincipal().getCsPeriodiciteSalaire())));
                revenu.setVerseAQui(Integer.valueOf(PRACORConst.CA_VERSEMENT_POURCENTAGE));
            }
        }
        if (!sitPro.getIsVersementEmployeur().booleanValue()) {
            // écraser le type de paiement si la prestation doit etre versée à
            // l'assuré
            revenu.setVerseAQui(Integer.valueOf(PRACORConst.CA_VERSEMENT_ASSURE));
        }

        if (sitPro.getIsAllocationMax().booleanValue()) {
            // ecraser les montants si on veut l'allocation max.

            try {
                FWCurrency montantMax;

                if (droit instanceof APDroitMaternite) {
                    montantMax = APReferenceDataParser.loadReferenceData(session, "MATERNITE",
                            new JADate(droit.getDateDebutDroit()),
                            new JADate(droit.getDateFinDroit()),
                            new JADate(droit.getDateFinDroit())).getMontantJournalierMax();
                } else {
                    montantMax = APReferenceDataParser.loadReferenceData(session, "APG",
                            new JADate(droit.getDateDebutDroit()),
                            new JADate(droit.getDateFinDroit()),
                            new JADate(droit.getDateFinDroit())).getMontantJournalierMax();
                }

                revenu.setSalaire(Double.valueOf(montantMax.getBigDecimalValue().multiply(new BigDecimal(40d)).toString()));
            } catch (Exception e) {
                LOG.error("ERROR_CHARGEMENT_MONTANT_MAXIMUM", e);
                throw new CommonTechnicalException("ERROR_CHARGEMENT_MONTANT_MAXIMUM", e);
            }
            revenu.setTypeSalaire(Integer.valueOf(PRACORConst.CA_TYPE_SALAIRE_MENSUEL));
            revenu.setPourcentSalaireVerse(POURCENT_TOTAL_VERSE);
            revenu.setVerseAQui(Integer.valueOf(PRACORConst.CA_VERSEMENT_POURCENTAGE));
        }
        return revenu;
    }

    // 5. Le nombre d'heures par semaines s'il s'agit d'un salaire horaire
    // si le nombre d'heures est un float on arrondit au 0.1 le plus proche
    // sinon au 1 le plus proche
    private Double roundNombreHeureSemaine(String heureSemaine) {
        if (heureSemaine.lastIndexOf(".") == -1) {
            return JANumberFormatter.round(Double.valueOf(heureSemaine), 1, 0, JANumberFormatter.NEAR);
        } else {
            return  JANumberFormatter.round(Double.valueOf(heureSemaine), 0.1, 1, JANumberFormatter.NEAR);
        }
    }

    private String buildNomAffilie(APEmployeur employeur, BSession session) {

        String nomAffilie = null;
        try {
            nomAffilie = employeur.loadNom();
        } catch (Exception e) {
            LOG.error("Impossible de récupérer le nom employeur' "+employeur.getIdAffilie(), e);
            throw new CommonTechnicalException("Impossible de récupérer le nom employeur' "+employeur.getIdAffilie(), e);
        }
        if (!JadeStringUtil.isBlankOrZero(employeur.getIdAffilie())) {
            AFAffiliation af = new AFAffiliation();
            try {
                af.setSession((BSession) PRSession.connectSession(session,
                        AFApplication.DEFAULT_APPLICATION_NAOS));
                af.setAffiliationId(employeur.getIdAffilie());
                af.retrieve();
            } catch (Exception e) {
                LOG.error("Impossible de récupérer l'affiliation employeur' " + employeur.getIdAffilie(), e);
                throw new CommonTechnicalException("Impossible de récupérer l'affiliation employeur' " + employeur.getIdAffilie(), e);
            }

            if (IAFAffiliation.TYPE_AFFILI_INDEP.equals(af.getTypeAffiliation())) {
                nomAffilie = APACORPrestationsParser.CONST_TYPE_AFFILI_INDEP + nomAffilie;
            } else if (IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY.equals(af.getTypeAffiliation())) {
                nomAffilie = APACORPrestationsParser.CONST_TYPE_AFFILI_INDEP_EMPLOY + nomAffilie;
            } else if (IAFAffiliation.TYPE_AFFILI_EMPLOY.equals(af.getTypeAffiliation())) {
                nomAffilie = APACORPrestationsParser.CONST_TYPE_AFFILI_EMPLOY + nomAffilie;
            } else if (IAFAffiliation.TYPE_AFFILI_EMPLOY_D_F.equals(af.getTypeAffiliation())) {
                nomAffilie = APACORPrestationsParser.CONST_TYPE_AFFILI_EMPLOY_D_F + nomAffilie;
            } else if (IAFAffiliation.TYPE_AFFILI_LTN.equals(af.getTypeAffiliation())) {
                nomAffilie = APACORPrestationsParser.CONST_TYPE_AFFILI_LTN + nomAffilie;
            } else if (IAFAffiliation.TYPE_AFFILI_TSE.equals(af.getTypeAffiliation())) {
                nomAffilie = APACORPrestationsParser.CONST_TYPE_AFFILI_TSE + nomAffilie;
            } else if (IAFAffiliation.TYPE_AFFILI_TSE_VOLONTAIRE.equals(af.getTypeAffiliation())) {
                nomAffilie = APACORPrestationsParser.CONST_TYPE_AFFILI_TSE_VOLONTAIRE + nomAffilie;
            }


        }
        return nomAffilie;
    }

    public long nbContrats(String noAffilie, BSession session) throws PRACORException {
            return situationProfessionnelles.stream().filter(s -> loadNumeroWithRunTimeException(s, session).equals(noAffilie)).count();
    }

    private APEmployeur loadEmployeurWithRunTimeException(APSituationProfessionnelle situationProfessionnelle, BSession session) {
        try {
            return situationProfessionnelle.loadEmployeur();
        } catch (Exception e) {
            LOG.error(session.getLabel("ERREUR_CHARGEMENT_EMPLOYEUR") + situationProfessionnelle.getIdEmployeur(), e);
            throw new CommonTechnicalException(session.getLabel("ERREUR_CHARGEMENT_EMPLOYEUR") + situationProfessionnelle.getIdEmployeur(), e);
        }
    }

    private String loadNumeroWithRunTimeException(APSituationProfessionnelle situationProfessionnelle, BSession session) {
        try {
            return situationProfessionnelle.loadEmployeur().loadNumero();
        } catch (Exception e) {
            LOG.error(session.getLabel("ERREUR_CHARGEMENT_EMPLOYEUR") + situationProfessionnelle.getIdEmployeur(), e);
            throw new CommonTechnicalException(session.getLabel("ERREUR_CHARGEMENT_EMPLOYEUR") + situationProfessionnelle.getIdEmployeur(), e);
        }
    }

}
