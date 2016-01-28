package ch.globaz.perseus.business.statsmensuelles;

/**
 * Énumération des divers types de statistiques à compter. La statistique AUTRE a été ajouté afin de faciliter
 * certains traitement ne concernant pas un type de statistique précis mais un ensemble.
 * Ce n'est donc pas un "four tout" mais une manière de ne pas avoir à appeler toutes les autres statistiques alors
 * qu'on
 * cherche, par exemple, à savoir si on a juste une DEMANDE ou une AUTRE statistique.
 * 
 * @author RCO
 * 
 */
public enum TypeStat {
    DEMANDE,
    OCTROI_COMPLET,
    OCTROI_PARTIEL,
    PROJET,
    REFUS_SANS_CALCUL,
    RENONCIATION,
    SUPPRESSION,
    NON_ENTREE_MATIERE,
    REVISION_EXTRAORDINAIRE,
    ENFANTS_OCTROI_MOINS_6,
    ENFANTS_OCTROI_PLUS_6,
    ENFANTS_PARTIEL_MOINS_6,
    ENFANTS_PARTIEL_PLUS_6,
    AUTRE;
}
