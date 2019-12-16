package ch.globaz.al.business.constantes;

/**
 * Rubriques comptables
 * 
 * @author jts
 * 
 */
public interface ALConstRubriques {

    String RUBRIQUE_CAISSE_AGRICOLE = "rubrique.caisse.agricole";
    String RUBRIQUE_CAISSE_AGRICOLE_ADI = "rubrique.caisse.agricole.adi";

    String RUBRIQUE_CAISSE_AGRICULTEUR = "rubrique.caisse.agriculteur";

    String RUBRIQUE_CAISSE_AGRICULTEUR_ADI = "rubrique.caisse.agriculteur.adi";
    String RUBRIQUE_CAISSE_AGRICULTEUR_MENAGE_LJU = "rubrique.caisse.agriculteur.menage.lju";

    String RUBRIQUE_CAISSE_COLLABORATEUR_ADI = "rubrique.caisse.collaborateur.adi";
    String RUBRIQUE_CAISSE_COLLABORATEUR_LF = "rubrique.caisse.collaborateur.lf";
    String RUBRIQUE_CAISSE_INDEPENDANT = "rubrique.caisse.independant";
    String RUBRIQUE_CAISSE_RESTITUTION = "rubrique.caisse.restitution";
    String RUBRIQUE_CAISSE_RESTITUTION_H_STANDARD = "rubrique.caisse.restitution.h.standard";
    String RUBRIQUE_CAISSE_RESTITUTION_INDEPENDANT = "rubrique.caisse.restitution.independant";
    String RUBRIQUE_CAISSE_RESTITUTION_LOI_FEDERALE = "rubrique.caisse.restitution.lf";
    String RUBRIQUE_CAISSE_RESTITUTION_LOI_JU = "rubrique.caisse.restitution.lju";
    String RUBRIQUE_CAISSE_RESTITUTION_NON_ACTIF = "rubrique.caisse.restitution.non.actif";
    String RUBRIQUE_CAISSE_RESTITUTION_RA_STANDARD = "rubrique.caisse.restitution.ra.standard";
    String RUBRIQUE_CAISSE_RESTITUTION_RB_STANDARD = "rubrique.caisse.restitution.rb.standard";
    String RUBRIQUE_CAISSE_RESTITUTION_RC_STANDARD = "rubrique.caisse.restitution.rc.standard";
    String RUBRIQUE_CAISSE_RESTITUTION_RD_STANDARD = "rubrique.caisse.restitution.rd.standard";
    String RUBRIQUE_CAISSE_RESTITUTION_RE_STANDARD = "rubrique.caisse.restitution.re.standard";
    String RUBRIQUE_CAISSE_RESTITUTION_RF_STANDARD = "rubrique.caisse.restitution.rf.standard";
    String RUBRIQUE_CAISSE_RESTITUTION_RL_STANDARD = "rubrique.caisse.restitution.rl.standard";
    String RUBRIQUE_CAISSE_RESTITUTION_S_STANDARD = "rubrique.caisse.restitution.s.standard";
    String RUBRIQUE_CAISSE_RESTITUTION_SALARIE = "rubrique.caisse.restitution.salarie";

    String RUBRIQUE_CAISSE_RESTITUTION_TSE_STANDARD = "rubrique.caisse.restitution.tse.standard";
    String RUBRIQUE_CAISSE_RESTITUTION_VS_STANDARD = "rubrique.caisse.restitution.vs.standard";
    String RUBRIQUE_CAISSE_SALARIE = "rubrique.caisse.salarie";
    String RUBRIQUE_CAISSE_SALARIE_H_NAISSANCE = "rubrique.caisse.salarie.h.naissance";
    String RUBRIQUE_CAISSE_SALARIE_H_STANDARD = "rubrique.caisse.salarie.h.standard";
    String RUBRIQUE_CAISSE_SALARIE_RA_NAISSANCE = "rubrique.caisse.salarie.ra.naissance";
    String RUBRIQUE_CAISSE_SALARIE_RA_STANDARD = "rubrique.caisse.salarie.ra.standard";
    String RUBRIQUE_CAISSE_SALARIE_RB_NAISSANCE = "rubrique.caisse.salarie.rb.naissance";
    String RUBRIQUE_CAISSE_SALARIE_RB_STANDARD = "rubrique.caisse.salarie.rb.standard";
    String RUBRIQUE_CAISSE_SALARIE_RC_NAISSANCE = "rubrique.caisse.salarie.rc.naissance";
    String RUBRIQUE_CAISSE_SALARIE_RC_STANDARD = "rubrique.caisse.salarie.rc.standard";
    String RUBRIQUE_CAISSE_SALARIE_RD_NAISSANCE = "rubrique.caisse.salarie.rd.naissance";
    String RUBRIQUE_CAISSE_SALARIE_RD_STANDARD = "rubrique.caisse.salarie.rd.standard";
    String RUBRIQUE_CAISSE_SALARIE_RE_NAISSANCE = "rubrique.caisse.salarie.re.naissance";
    String RUBRIQUE_CAISSE_SALARIE_RE_STANDARD = "rubrique.caisse.salarie.re.standard";
    String RUBRIQUE_CAISSE_SALARIE_RF_NAISSANCE = "rubrique.caisse.salarie.rf.naissance";
    String RUBRIQUE_CAISSE_SALARIE_RF_STANDARD = "rubrique.caisse.salarie.rf.standard";
    String RUBRIQUE_CAISSE_SALARIE_RL_NAISSANCE = "rubrique.caisse.salarie.rl.naissance";
    String RUBRIQUE_CAISSE_SALARIE_RL_STANDARD = "rubrique.caisse.salarie.rl.standard";
    String RUBRIQUE_CAISSE_SALARIE_S_NAISSANCE = "rubrique.caisse.salarie.s.naissance";
    String RUBRIQUE_CAISSE_SALARIE_S_STANDARD = "rubrique.caisse.salarie.s.standard";
    String RUBRIQUE_CAISSE_SALARIE_TSE_NAISSANCE = "rubrique.caisse.salarie.tse.naissance";
    String RUBRIQUE_CAISSE_SALARIE_TSE_STANDARD = "rubrique.caisse.salarie.tse.standard";

    String RUBRIQUE_CAISSE_SALARIE_VS_NAISSANCE = "rubrique.caisse.salarie.vs.naissance";

    String RUBRIQUE_CAISSE_SALARIE_VS_STANDARD = "rubrique.caisse.salarie.vs.standard";
    String RUBRIQUE_CAISSE_TRAVAILLEUR_AGRICOLE_ADI = "rubrique.caisse.travailleur.agricole.adi";
    String RUBRIQUE_CAISSE_TRAVAILLEUR_AGRICOLE_MENAGE_LJU = "rubrique.caisse.travailleur.agricole.menage.lju";
    String RUBRIQUE_CAISSE_TRAVAILLEUR_AGRICOLE_MENAGE_MONTAGNE = "rubrique.caisse.travailleur.agricole.menage.montagne";
    String RUBRIQUE_CAISSE_TRAVAILLEUR_AGRICOLE_MENAGE_PLAINE = "rubrique.caisse.travailleur.agricole.menage.plaine";
    String RUBRIQUE_CAISSE_TRAVAILLEUR_AGRICOLE_MONTAGNE = "rubrique.caisse.travailleur.agricole.montagne";

    String RUBRIQUE_CAISSE_TRAVAILLEUR_AGRICOLE_NAISSANCE = "rubrique.caisse.travailleur.agricole.naissance";
    /**
     * Rubriques multicaisses les CAF multicaisses ont (potentiellement) des rubriques par caisse, code_ofas est le code
     * ofas de la caisse
     */
    String RUBRIQUE_MULTICAISSE_CODE_PATTERN = "code_ofas";
    String RUBRIQUE_MULTICAISSE_INDEPENDANT_ADI = "rubrique.multicaisse."
     + ALConstRubriques.RUBRIQUE_MULTICAISSE_CODE_PATTERN + ".independant.adi";
    String RUBRIQUE_MULTICAISSE_INDEPENDANT_ENF = "rubrique.multicaisse."
     + ALConstRubriques.RUBRIQUE_MULTICAISSE_CODE_PATTERN + ".independant.enfant";
    String RUBRIQUE_MULTICAISSE_INDEPENDANT_FORM = "rubrique.multicaisse."
     + ALConstRubriques.RUBRIQUE_MULTICAISSE_CODE_PATTERN + ".independant.formation";
    String RUBRIQUE_MULTICAISSE_INDEPENDANT_NAIS_ACCE = "rubrique.multicaisse."
     + ALConstRubriques.RUBRIQUE_MULTICAISSE_CODE_PATTERN + ".independant.naissance";

    String RUBRIQUE_MULTICAISSE_INDEPENDANT_RESTITUTION = "rubrique.multicaisse."
     + ALConstRubriques.RUBRIQUE_MULTICAISSE_CODE_PATTERN + ".independant.restitution";
    String RUBRIQUE_MULTICAISSE_SALARIE_ADI = "rubrique.multicaisse."
     + ALConstRubriques.RUBRIQUE_MULTICAISSE_CODE_PATTERN + ".salarie.adi";
    String RUBRIQUE_MULTICAISSE_SALARIE_ENF = "rubrique.multicaisse."
     + ALConstRubriques.RUBRIQUE_MULTICAISSE_CODE_PATTERN + ".salarie.enfant";
    String RUBRIQUE_MULTICAISSE_SALARIE_FORM = "rubrique.multicaisse."
     + ALConstRubriques.RUBRIQUE_MULTICAISSE_CODE_PATTERN + ".salarie.formation";
    String RUBRIQUE_MULTICAISSE_SALARIE_NAIS_ACCE = "rubrique.multicaisse."
     + ALConstRubriques.RUBRIQUE_MULTICAISSE_CODE_PATTERN + ".salarie.naissance";

    String RUBRIQUE_MULTICAISSE_SALARIE_RESTITUTION = "rubrique.multicaisse."
     + ALConstRubriques.RUBRIQUE_MULTICAISSE_CODE_PATTERN + ".salarie.restitution";
    String RUBRIQUE_STANDARD_ADI = "rubrique.standard.adi";

    String RUBRIQUE_STANDARD_AGRICULTEUR = "rubrique.standard.agriculteur";

    String RUBRIQUE_STANDARD_COLLABORATEUR_AGRICOLE = "rubrique.standard.collaborateur";
    String RUBRIQUE_STANDARD_INDEPENDANT = "rubrique.standard.independant";
    String RUBRIQUE_STANDARD_INDEPENDANT_ADI = "rubrique.standard.independant.adi";
    String RUBRIQUE_STANDARD_NON_ACTIF = "rubrique.standard.non.actif";
    String RUBRIQUE_STANDARD_NON_ACTIF_ADI = "rubrique.standard.non.actif.adi";
    String RUBRIQUE_STANDARD_PECHEUR = "rubrique.standard.pecheur";
    String RUBRIQUE_STANDARD_RESTITUTION = "rubrique.standard.restitution";
    String RUBRIQUE_STANDARD_RESTITUTION_INDEPENDANT = "rubrique.standard.restitution.independant";
    String RUBRIQUE_STANDARD_RESTITUTION_SALARIE = "rubrique.standard.restitution.salarie";
    String RUBRIQUE_STANDARD_SALARIE = "rubrique.standard.salarie";
    String RUBRIQUE_STANDARD_SALARIE_NAISSANCE = "rubrique.standard.salarie.naissance";
    String RUBRIQUE_STANDARD_TRAVAILLEUR_AGRICOLE = "rubrique.standard.travailleur.agricole";

    String RUBRIQUE_CAISSE_INDEPENDANT_IMPOT_SOURCE = "rubrique.multicaisse." +RUBRIQUE_MULTICAISSE_CODE_PATTERN + ".independant.is";
    String RUBRIQUE_CAISSE_SALARIE_IMPOT_SOURCE = "rubrique.multicaisse." + RUBRIQUE_MULTICAISSE_CODE_PATTERN +  ".salarie.is";
}
