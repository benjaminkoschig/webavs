package ch.globaz.corvus.process.dnra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.codesystem.CodeSystemeResolver;

class DifferenceFinder {

    private final Locale locale;
    private final CodeSystemeResolver codeSystemeResolver;

    public DifferenceFinder(Locale locale, CodeSystemeResolver codeSystemeResolver) {
        this.locale = locale;
        this.codeSystemeResolver = codeSystemeResolver;
    }

    public List<DifferenceTrouvee> findAllDifference(List<Mutation> mutations, Set<InfoTiers> infosTiers,
            Set<InfoTiers> infosTiersInvalide) {

        final Map<String, InfoTiers> map = new HashMap<String, InfoTiers>();
        final List<DifferenceTrouvee> differences = new ArrayList<DifferenceTrouvee>();

        for (InfoTiers tiers : infosTiersInvalide) {
            DifferenceTrouvee differenceTrouvee = newDifference(TypeDifference.NSS_INVALIDE, tiers, "", "");
            differences.add(differenceTrouvee);
        }

        for (InfoTiers infoTiers : infosTiers) {
            map.put(infoTiers.getNss(), infoTiers);
        }
        for (Mutation mutation : mutations) {
            InfoTiers tiers = map.get(mutation.getNewNss());
            // si on a pas trouvé par le nss on tante de regarde si le nss a été inactivé
            if (tiers == null) {
                tiers = map.get(mutation.getNssInactive());
            }
            // On ne trait pas que les mutation pour la caisse;
            if (tiers != null) {
                differences.addAll(findDifference(mutation, tiers));
            }
        }

        return differences;
    }

    List<DifferenceTrouvee> findDifference(Mutation mutation, InfoTiers infoTiers) {
        List<DifferenceTrouvee> list = new ArrayList<DifferenceTrouvee>();
        if (!isNomSame(mutation, infoTiers)) {
            DifferenceTrouvee differenceTrouvee = newDifference(TypeDifference.NOM, infoTiers, infoTiers.getNom(),
                    mutation.getNom());
            list.add(differenceTrouvee);
        }
        if (!isPrenomSame(mutation, infoTiers)) {
            DifferenceTrouvee differenceTrouvee = newDifference(TypeDifference.PRENOM, infoTiers,
                    infoTiers.getPrenom(), mutation.getPrenom());
            list.add(differenceTrouvee);
        }
        if (!isDateDecesSame(mutation, infoTiers)) {
            String dateMutation = null;
            String dateTiers = null;
            if (infoTiers.getDateDeces() != null) {
                dateTiers = infoTiers.getDateDeces().getSwissValue();
            }
            if (mutation.getDateDece() != null) {
                dateMutation = mutation.getDateDece().getSwissValue();
            }

            DifferenceTrouvee differenceTrouvee = newDifference(TypeDifference.DATE_DECES, infoTiers, dateTiers,
                    dateMutation);
            list.add(differenceTrouvee);
        }
        if (!isDateNaissanceSame(mutation, infoTiers)) {
            String dateMutation = null;
            String dateTiers = null;
            if (infoTiers.getDateNaissance() != null) {
                dateTiers = infoTiers.getDateNaissance().getSwissValue();
            }
            if (mutation.getDateNaissance() != null) {
                dateMutation = mutation.getDateNaissance().getSwissValue();
            }
            DifferenceTrouvee differenceTrouvee = newDifference(TypeDifference.DATE_NAISSANCE, infoTiers, dateTiers,
                    dateMutation);
            list.add(differenceTrouvee);
        }
        if (!isEtatCivilSame(mutation, infoTiers)) {
            String etatCivilMutation = null;
            String etatCivilTiers = null;
            if (infoTiers.getEtatCivil() != null) {
                etatCivilTiers = codeSystemeResolver.resovleTraduction(infoTiers.getEtatCivil().getCodeSysteme());
            }
            if (mutation.getEtatCivil() != null) {
                etatCivilMutation = codeSystemeResolver.resovleTraduction(mutation.getEtatCivil().getCodeSysteme());
            }

            DifferenceTrouvee differenceTrouvee = newDifference(TypeDifference.ETAT_CIVIL, infoTiers, etatCivilTiers,
                    etatCivilMutation);
            differenceTrouvee.setDateChangement(mutation.getDateChangementEtatCivil());
            list.add(differenceTrouvee);
        }

        if (!isSexeSame(mutation, infoTiers)) {
            String sexeMutation = null;
            String sexeTiers = null;

            if (mutation.getSexe() != null) {
                sexeMutation = codeSystemeResolver.resovleTraduction(mutation.getSexe().getCodeSysteme());
            }

            if (infoTiers.getSexe() != null) {
                sexeTiers = codeSystemeResolver.resovleTraduction(infoTiers.getSexe().getCodeSysteme());
            }
            DifferenceTrouvee differenceTrouvee = newDifference(TypeDifference.SEXE, infoTiers, sexeTiers, sexeMutation);
            list.add(differenceTrouvee);
        }
        if (!isNssSame(mutation, infoTiers)) {
            DifferenceTrouvee differenceTrouvee = newDifference(TypeDifference.NSS, infoTiers, infoTiers.getNss(),
                    mutation.getNewNss());
            list.add(differenceTrouvee);
        }

        if (!isNationaliteSame(mutation, infoTiers)) {
            DifferenceTrouvee differenceTrouvee = newDifference(TypeDifference.NATIONALITE, infoTiers, infoTiers
                    .getPays().getTraduction(locale), mutation.getPays().getTraduction(locale));
            list.add(differenceTrouvee);
        }
        return list;
    }

    private DifferenceTrouvee newDifference(TypeDifference difference, InfoTiers infoTiers, String actuelle,
            String nouveau) {
        DifferenceTrouvee differenceTrouvee = new DifferenceTrouvee();
        differenceTrouvee.setDateNaissance(infoTiers.getDateNaissance());
        differenceTrouvee.setNom(infoTiers.getNom());
        differenceTrouvee.setPrenom(infoTiers.getPrenom());
        differenceTrouvee.setNss(infoTiers.getNss());

        differenceTrouvee.setDifference(difference);
        differenceTrouvee.setValeurActuelle(actuelle);
        differenceTrouvee.setValeurNouvelle(nouveau);
        return differenceTrouvee;
    }

    static boolean isNomSame(Mutation mutation, InfoTiers infoTiers) {
        if (mutation.getNom() != null) {
            return mutation.getNom().equalsIgnoreCase(infoTiers.getNom());
        }
        return true;
    }

    static boolean isPrenomSame(Mutation mutation, InfoTiers infoTiers) {
        if (mutation.getPrenom() != null) {
            return mutation.getPrenom().equalsIgnoreCase(infoTiers.getPrenom());
        }
        return true;
    }

    static boolean isNssSame(Mutation mutation, InfoTiers infoTiers) {
        if (mutation.getNewNss() != null) {
            return mutation.getNewNss().equals(infoTiers.getNss());
        }
        return true;
    }

    static boolean isSexeSame(Mutation mutation, InfoTiers infoTiers) {
        if (mutation.getSexe() != null && !mutation.getSexe().isUndefined()) {
            return mutation.getSexe().equals(infoTiers.getSexe());
        }
        return true;
    }

    static boolean isDateNaissanceSame(Mutation mutation, InfoTiers infoTiers) {
        if (mutation.getDateNaissance() != null) {
            return mutation.getDateNaissance().equals(infoTiers.getDateNaissance());
        }
        return true;
    }

    static boolean isDateDecesSame(Mutation mutation, InfoTiers infoTiers) {

        if (mutation.getDateDece() != null) {
            return mutation.getDateDece().equals(infoTiers.getDateDeces());
        } else if (mutation.getDateDece() == null && infoTiers.getDateDeces() != null) {
            return false;
        }
        return true;
    }

    static boolean isEtatCivilSame(Mutation mutation, InfoTiers infoTiers) {
        if (!mutation.getEtatCivil().isUndefined()) {
            return mutation.getEtatCivil().equals(infoTiers.getEtatCivil());
        }
        return true;
    }

    static boolean isNationaliteSame(Mutation mutation, InfoTiers infoTiers) {
        if (mutation.getCodeNationalite() != null) {
            return mutation.getCodeNationalite().equals(infoTiers.getCodeNationalite());
        }
        return true;
    }

}
