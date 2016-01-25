package globaz.itucana.constantes;

/**
 * Interface de properties pour définition des nom de classes dans fichier properties
 * (tucanaproperties.itucana.itucana.properties)
 * 
 * @author fgo date de création : 7 juin 2006
 * @version : version 1.0
 * 
 */
public interface IPropertiesNames {
    public final String ACM_ADAPTER = "tucana.acm.adapter.implementation.class";
    // constante pour l'implémentation de l'adapteur
    public final String AF_ADAPTER = "tucana.af.adapter.implementation.class";
    // AF
    public final String AF_DATE_DEBUT_BOUCLEMENT = "tucana.af.date.debut.bouclement";
    public final String CA_ADAPTER = "tucana.ca.adapter.implementation.class";
    public final String CG_ADAPTER = "tucana.cg.adapter.implementation.class";
    public final String HELIOS_RUBRIQUE_CC_SIEGE = "tucana.ca.rubrique.cc.siege";
    // Helios
    public final String HELIOS_RUBRIQUE_CCP_ALFA = "tucana.ca.rubrique.ccp.alfa";
    public final String HELIOS_RUBRIQUE_INTERETS_CCP = "tucana.ca.rubrique.interet.ccp";
    public final String HELIOS_RUBRIQUE_QUOTE_PART_ADM_ACM = "tucana.ca.rubrique.quotepart.adm.acm";
    public final String HELIOS_RUBRIQUE_QUOTE_PART_ADM_ACM_MATERNITE = "tucana.ca.rubrique.quotepart.adm.acm.maternite";
    public final String HELIOS_RUBRIQUE_QUOTE_PART_ADM_ACM_MILITAIRE = "tucana.ca.rubrique.quotepart.adm.acm.militaire";
    public final String HELIOS_RUBRIQUE_QUOTE_PART_ADM_ALFA = "tucana.ca.rubrique.quotepart.adm.alfa";
    public final String LABEL_AUCUN_COMPTE_RESOLU_DE = "tucana.compta.label.aucun.decompte.de";

    public final String LABEL_AUCUN_COMPTE_RESOLU_FR = "tucana.compta.label.aucun.decompte.fr";
    public final String LABEL_GLOBAL_PERIODE_INEXISTANT_DE = "tucana.compta.label.periode.inexistante.de";
    // LABLE pour CA CG
    public final String LABEL_GLOBAL_PERIODE_INEXISTANT_FR = "tucana.compta.label.periode.inexistante.fr";
    public final String LABEL_MONTANT_RUBRIQUE_DIFF_PERTES_PROFITS_DE = "tucana.compta.label.diff.pp.de";
    public final String LABEL_MONTANT_RUBRIQUE_DIFF_PERTES_PROFITS_FR = "tucana.compta.label.diff.pp.fr";

    // constante pour la définition du modèle de bouclement AF
    public final String MODEL_BOUCLEMENT = "tucana.model.bouclement.implementation.class";

    public final String OSIRIS_RUBRIQUE_CONTRIBUTIONS_AF = "tucana.cg.rubrique.contributions.af";
    public final String OSIRIS_RUBRIQUE_CONTRIBUTIONS_AF_INDEPENDANT = "tucana.cg.rubrique.contributions.af.independant";

    // Osiris
    public final String OSIRIS_RUBRIQUE_CONTRIBUTIONS_AF_INDEPENDANT_SUPPLEMENT = "tucana.cg.rubrique.contributions.af.independant.supplement";
    /** @deprecated */
    @Deprecated
    public final String OSIRIS_RUBRIQUE_FONDS_FORMATION_PROF = "tucana.cg.rubrique.fonds.formation.prof";
    public final String OSIRIS_RUBRIQUE_FONDS_FORMATION_PROF_GE = "tucana.cg.rubrique.fonds.formation.prof.ge";
    public final String OSIRIS_RUBRIQUE_FONDS_FORMATION_PROF_NE = "tucana.cg.rubrique.fonds.formation.prof.ne";
    public final String OSIRIS_RUBRIQUE_FONDS_FORMATION_PROF_ZH = "tucana.cg.rubrique.fonds.formation.prof.zh";
    public final String OSIRIS_RUBRIQUE_PART_COT_CHARGE_ACM_APG = "tucana.cg.rubrique.cot.charge.acm.apg";
    public final String OSIRIS_RUBRIQUE_REMISES_COTISATIONS = "tucana.cg.rubrique.remises.cotisations";
}
