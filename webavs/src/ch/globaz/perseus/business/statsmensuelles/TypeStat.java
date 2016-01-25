package ch.globaz.perseus.business.statsmensuelles;

/**
 * �num�ration des divers types de statistiques � compter. La statistique AUTRE a �t� ajout� afin de faciliter
 * certains traitement ne concernant pas un type de statistique pr�cis mais un ensemble.
 * Ce n'est donc pas un "four tout" mais une mani�re de ne pas avoir � appeler toutes les autres statistiques alors
 * qu'on
 * cherche, par exemple, � savoir si on a juste une DEMANDE ou une AUTRE statistique.
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
