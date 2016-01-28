package ch.globaz.corvus.domaine.constantes;

/**
 * La provenance de cette API, bas� sur une rente AI ou sur une rente AVS. La non valeur {@link #NON_DEFINI} est utilis�
 * lorsque le code prestation traite d'une rente AI, Vieillesse ou Survivant
 */
public enum TypeRenteAPI {

    // @formatter:off
	/** API d�coulant d'une rente AI */
	API_AI,
	/** API d�coulant d'une rente AVS */
	API_AVS,
	/** non valeur, pour les rentes AI, Vieillesse et Survivant */
	NON_DEFINI
	// @formatter:on
}
