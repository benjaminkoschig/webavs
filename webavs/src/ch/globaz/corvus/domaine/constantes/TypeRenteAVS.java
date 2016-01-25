package ch.globaz.corvus.domaine.constantes;

import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * Le type de rente AVS, à ne pas confondre avec le {@link DomaineCodePrestation}. Cet énuméré n'est valide que pour les
 * rentes AVS (donc tout sauf les API). Il décrit si la rente est ordinaire ou extraordinaire.
 */
public enum TypeRenteAVS {
    // @formatter:off
	/** Rente AVS extraordinaire */
	EXTRAORDINAIRE,
	/** non valeur, utilisé pour les API */
	NON_DEFINI,
	/** Rente AVS ordinaire */
	ORDINAIRE;
	// @formatter:on
}
