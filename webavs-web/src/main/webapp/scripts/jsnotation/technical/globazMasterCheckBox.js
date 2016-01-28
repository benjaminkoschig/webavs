globazNotation.mastercheckbox = {

	author: 'SCE',
	forTagHtml: 'input',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Objet à appliquer sur un checkbox d'une entete de tableau. Il permet de gérer le checkbox du hedaer en tant que maitre pour les checkbox des lignes du tableau.",

	descriptionOptions: {},
	options: {},

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initiliser l'objet
	 */
	init: function () {
		var that = this;
		var $mainTd = this.$elementToPutObject.parent();
		var n_tdCol = $mainTd.index();
		var $table = this.$elementToPutObject.closest('table');
		this.$elementToPutObject.click(function () {
			$table.find('tr:gt(0)').find('td:eq(' + n_tdCol + ')').each(function () {
				var $checkbox = $(this).find('input[type=checkbox]');
				if(!$checkbox.prop("disabled")){
					$checkbox.prop('checked', that.$elementToPutObject.is(':checked'));
					$checkbox.change();
				}
			});
		});
	}
};