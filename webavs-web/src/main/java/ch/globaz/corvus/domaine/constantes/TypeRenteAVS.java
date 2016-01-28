package ch.globaz.corvus.domaine.constantes;

import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * Le type de rente AVS, � ne pas confondre avec le {@link DomaineCodePrestation}. Cet �num�r� n'est valide que pour les
 * rentes AVS (donc tout sauf les API). Il d�crit si la rente est ordinaire ou extraordinaire.
 */
public enum TypeRenteAVS {
    // @formatter:off
	/** Rente AVS extraordinaire */
	EXTRAORDINAIRE,
	/** non valeur, utilis� pour les API */
	NON_DEFINI,
	/** Rente AVS ordinaire */
	ORDINAIRE;
	// @formatter:on
}
