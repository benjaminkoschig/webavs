package ch.globaz.vulpecula.businessimpl.services.myprodis;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import com.google.common.base.Predicate;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

/**
 * @author jmc
 *
 */
/**
 * @author jmc
 *
 */
public class ComparaisonMyProdisService {
    BSession session;

    public BSession getSession() {
        return session;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * Méthode qui retourne si > 0
     *
     * @param nb
     * @return
     */
    public boolean hasNbSup0(BigDecimal nb) {
        return nb.compareTo(new BigDecimal(0)) > 0;
    }

    /**
     * Définit si un salaire doit être annoncé
     *
     * @return le predicat
     */
    public Predicate<ComparaisonMyProdisSalaireCP> returnPredicate() {
        return new Predicate<ComparaisonMyProdisSalaireCP>() {

            @Override
            public boolean apply(ComparaisonMyProdisSalaireCP salaire) {
                salaire.setSource("webMetier");
                salaire.setMustBeAnnonced(getSession());
                return salaire.mustBeAnnonced();
            }
        };

    }

    /**
     * Définit si un CP doit être annoncé
     *
     * @return le predicat
     */
    public Predicate<ComparaisonMyProdisSalaireCP> returnPredicateCP() {
        return new Predicate<ComparaisonMyProdisSalaireCP>() {

            @Override
            public boolean apply(ComparaisonMyProdisSalaireCP salaire) {
                salaire.setSource("webMetier");
                salaire.hasLppCP(getSession());
                salaire.determineMontantBrut();
                salaire.miseAjourPeriode();
                return salaire.isMustBeAnnoncedCP() && !salaire.isDeduction();
            }
        };

    }

    /**
     * Retourne l'élément Cp ou salaire pour la comparaison
     *
     * @param input
     * @return C ou S
     */
    public String cpOrSalaire(String input) {
        if ("8".equals(input)) {
            return "C";
        } else {
            return "S";
        }
    }

    /**
     * Permet de transformer le no d'affilié depuis la liste my Prodis
     *
     * @param noAffilieToSub
     * @return
     */
    public String transformeNoAffilie(String noAffilieToSub) {
        if (null == noAffilieToSub) {
            return null;
        }
        if (noAffilieToSub.length() < 16) {
            return null;
        }
        return StringUtils.mid(noAffilieToSub, 3, 13);

    }

    /**
     * transofrme une date du format dd.MM.yyyy en yyyyMMdd
     *
     * @param dateToTransform
     * @return
     */
    public String transformeDate(String dateToTransform) {
        if (null == dateToTransform) {
            return null;
        }
        if (dateToTransform.length() < 8) {
            return null;
        }
        Date dateGreg;
        try {
            SimpleDateFormat dateFormatterInput = new SimpleDateFormat("dd.MM.yyyy");
            dateGreg = dateFormatterInput.parse(dateToTransform);
        } catch (Exception e) {
            return null;
        }
        SimpleDateFormat dateFormatterOutput = new SimpleDateFormat("yyyyMMdd");

        return dateFormatterOutput.format(dateGreg);

    }

    /**
     * Donne la vrai date de fin pour les CP par rapport à la date de fin du poste de travail
     *
     * @param dateVersement
     * @param dateFin
     * @return la date de fin effective
     */
    public String transformeDateFin(String dateVersement, String dateFin) {
        try {
            SimpleDateFormat dateFormatterInput = new SimpleDateFormat("yyyyMMdd");
            Date dateGregV = dateFormatterInput.parse(dateVersement);

            DateTime dateTimeV = new DateTime(dateGregV);
            if (JadeStringUtil.isBlankOrZero(dateFin)) {
                DateTime dateFinD = dateTimeV.dayOfMonth().withMaximumValue();
                return dateFormatterInput.format(dateFinD.toDate());
            }
            Date dateFinP = dateFormatterInput.parse(dateFin);
            DateTime dateTimeFinP = new DateTime(dateFinP);
            if (dateTimeV.isAfter(dateTimeFinP)) {
                return dateFormatterInput.format(dateFinP);
            }
            DateTime dateFinD = dateTimeV.dayOfMonth().withMaximumValue();
            return dateFormatterInput.format(dateFinD.toDate());
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Donne la vrai date de début pour les CP par rapport à la date de début du poste
     *
     * @param dateVersement
     * @param dateFin
     * @return la date de début effective
     */
    public String transformeDateDebut(String dateVersement, String dateDebutPoste, String dateFinPoste) {

        try {
            SimpleDateFormat dateFormatterInput = new SimpleDateFormat("yyyyMMdd");
            DateTime dateTimeV = retourneDateTime(dateVersement);
            Date dateDebutPosteP = dateFormatterInput.parse(dateDebutPoste);
            DateTime dateTimeDebutPoste = new DateTime(dateDebutPosteP);

            if (dateTimeV.isBefore(dateTimeDebutPoste)) {
                return dateFormatterInput.format(dateDebutPosteP);
            } else {
                if (JadeStringUtil.isBlankOrZero(dateFinPoste)) {
                    DateTime dateDebutD = dateTimeV.dayOfMonth().withMinimumValue();
                    return dateFormatterInput.format(dateDebutD.toDate());
                } else {
                    Date dateFinPosteP = dateFormatterInput.parse(dateFinPoste);
                    DateTime dateTimeFinPoste = new DateTime(dateFinPosteP);
                    if (dateTimeV.isAfter(dateTimeFinPoste)) {
                        dateTimeFinPoste = dateTimeFinPoste.dayOfMonth().withMinimumValue();
                        return dateFormatterInput.format(dateTimeFinPoste.toDate());
                    } else {
                        DateTime dateDebutD = dateTimeV.dayOfMonth().withMinimumValue();
                        return dateFormatterInput.format(dateDebutD.toDate());
                    }

                }
            }

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Petit utilitaire qui retour un format dateTime
     *
     * @param date
     * @return
     * @throws Exception
     */
    private DateTime retourneDateTime(String date) throws Exception {
        SimpleDateFormat dateFormatterInput = new SimpleDateFormat("yyyyMMdd");
        Date dateGregV = dateFormatterInput.parse(date);
        return new DateTime(dateGregV);
    }

    // private String transformeDateDebutForSalaires(String dateDebut, String dateFin) throws Exception {
    // SimpleDateFormat dateFormatterInput = new SimpleDateFormat("yyyyMMdd");
    // DateTime dateDebutDT = retourneDateTime(dateDebut);
    // DateTime dateFinDT = retourneDateTime(dateFin);
    // if (isAnneeComplete(dateDebutDT, dateFinDT)) {
    // return dateFormatterInput.format(dateDebutDT.monthOfYear().addToCopy(2).toDate());
    // } else {
    // return dateDebut;
    // }
    //
    // }
    //
    // private boolean isAnneeComplete(DateTime dateDebutDT, DateTime dateFinDT) {
    // return dateDebutDT.getMonthOfYear() == 1 && dateDebutDT.getDayOfMonth() == 1 && dateFinDT.getMonthOfYear() == 2
    // && dateFinDT.getDayOfMonth() == 31;
    // }
    //
    // private String transformeDateFinForSalaires(String dateDebut, String dateFin) throws Exception {
    // SimpleDateFormat dateFormatterInput = new SimpleDateFormat("yyyyMMdd");
    // DateTime dateDebutDT = retourneDateTime(dateDebut);
    // DateTime dateFinDT = retourneDateTime(dateFin);
    // if (isAnneeComplete(dateDebutDT, dateFinDT)) {
    // return dateFormatterInput
    // .format(dateFinDT.monthOfYear().addToCopy(1).year().addToCopy(dateFinDT.getYear() + 1).toDate());
    // } else {
    // return dateDebut;
    // }
    //
    // }

    @Deprecated
    public boolean validateEnregistrement(ComparaisonMyProdisSalaireCP enr) {
        return !JadeStringUtil.isBlankOrZero(enr.getCp()) && !JadeStringUtil.isBlankOrZero(enr.getDateDebut())
                && !JadeStringUtil.isBlankOrZero(enr.getDateFin()) && !JadeStringUtil.isBlankOrZero(enr.getNoAffilie())
                && !JadeStringUtil.isBlankOrZero(enr.getNss()) && !JadeStringUtil.isBlankOrZero(enr.getSalaire());

    }

    /**
     * Valide par bean validation que le CP ou salaire est conforme à ce qu'on attend
     *
     * @param enr
     * @return
     */
    public boolean validateBeanEnr(ComparaisonMyProdisSalaireCP enr) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ComparaisonMyProdisSalaireCP>> constraintViolations = validator.validate(enr);
        return constraintViolations.isEmpty();
    }

    /**
     * Méthode qui compare les listes excel et le contenu des salaires CP en db
     *
     * @param mapSalaireRegroupeParNSS
     * @param listFromExcelFile
     * @return une liste de différence pour impression.
     */
    public List<ComparaisonMyProdisSalaireCP> compareList(
            Map<String, List<ComparaisonMyProdisSalaireCP>> mapSalaireRegroupeParNSS,
            List<ComparaisonMyProdisSalaireCP> listFromExcelFile) {
        try {
            // On parcours la liste excel$
            int Ok = 0;
            int Ko = 0;
            Iterator<ComparaisonMyProdisSalaireCP> iteratorFromExcel = listFromExcelFile.iterator();
            while (iteratorFromExcel.hasNext()) {
                ComparaisonMyProdisSalaireCP salaireFromExcel = iteratorFromExcel.next();
                List<ComparaisonMyProdisSalaireCP> salairesPourUnNss = mapSalaireRegroupeParNSS
                        .get(salaireFromExcel.getNss());

                if (null != salairesPourUnNss) {
                    Iterator<ComparaisonMyProdisSalaireCP> iteratorSalaires = salairesPourUnNss.iterator();
                    while (iteratorSalaires.hasNext()) {
                        ComparaisonMyProdisSalaireCP salaireRegroupe = iteratorSalaires.next();
                        if (compareSalaire(salaireRegroupe, salaireFromExcel)) {
                            Ok++;
                            mapSalaireRegroupeParNSS.remove(salaireRegroupe.getNss());
                            iteratorFromExcel.remove();
                            iteratorSalaires.remove();
                            // Si la liste est vide, on ne rejoute pas
                            if (!salairesPourUnNss.isEmpty()) {
                                mapSalaireRegroupeParNSS.put(salaireRegroupe.getNss(), salairesPourUnNss);
                            }
                            break;
                        } else {
                            Ko++;
                        }
                    }
                }

            }
            // On ajout tous les éléments à la liste
            for (Map.Entry<String, List<ComparaisonMyProdisSalaireCP>> listeSalaires : mapSalaireRegroupeParNSS
                    .entrySet()) {
                for (ComparaisonMyProdisSalaireCP salairesNonTrouves : listeSalaires.getValue()) {
                    listFromExcelFile.add(salairesNonTrouves);
                }
            }
        } catch (Exception e) {
            JadeLogger.error(e, e.getMessage());
        }

        return listFromExcelFile;
    }

    /**
     * méthode unitaire qui compare deux enregistrements
     *
     * @param fromExcel
     * @param fromDB
     * @return true si c'est identique
     */
    public boolean compareSalaire(ComparaisonMyProdisSalaireCP fromExcel, ComparaisonMyProdisSalaireCP fromDB) {
        return fromExcel.getCp().equals(fromDB.getCp()) && fromExcel.getDateDebut().equals(fromDB.getDateDebut())
                && fromExcel.getDateFin().equals(fromDB.getDateFin())
                // && fromExcel.getNoAffilie().equals(fromDB.getNoAffilie())
                && fromExcel.getNss().equals(fromDB.getNss())
                && new BigDecimal(fromExcel.getSalaire()).compareTo(new BigDecimal(fromDB.getSalaire())) == 0;
    }

    /**
     * Si la date de début est 01.01.YYYY et la date de fin 31.12.YYYY => on retourne 01.02
     *
     * @param dateDebut
     * @param dateFin
     * @return
     */
    public String determineDateDebutForSalaire(String dateDebut, String dateFin) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dateDebutDT = formatter.parseDateTime(dateDebut);
        DateTime dateFinDT = formatter.parseDateTime(dateFin);
        if (isPremierJourAnnee(dateDebutDT) && isDernierJourAnnee(dateFinDT)) {
            return formatter.print(formatter.parseDateTime(String.valueOf(dateDebutDT.getYear()) + "0201"));
        }
        return formatter.print(dateDebutDT);

    }

    private Boolean isPremierJourAnnee(DateTime dateFirst) {
        return 1 == dateFirst.dayOfMonth().get() && 1 == dateFirst.monthOfYear().get();
    }

    private Boolean isDernierJourAnnee(DateTime dateLast) {
        return 31 == dateLast.dayOfMonth().get() && 12 == dateLast.monthOfYear().get();
    }

    public String determineDateFinForSalaire(String dateDebut, String dateFin) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dateDebutDT = formatter.parseDateTime(dateDebut);
        DateTime dateFinDT = formatter.parseDateTime(dateFin);
        if (isPremierJourAnnee(dateDebutDT) && isDernierJourAnnee(dateFinDT)) {
            return formatter.print(formatter.parseDateTime(String.valueOf(dateFinDT.getYear() + 1) + "0131"));
        }
        return formatter.print(dateFinDT);
    }
}
