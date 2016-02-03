globazNotation.nss = {

	author: 'CEL',
	forTagHtml: 'input',
	type: globazNotation.typesNotation.BUSINESS_NOTATION,

	description: "util nss notation",

	descriptionOptions: {},
	options: {},

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initialiser l'objet
	 */
	init: function () {
		var that = this;
		var $inputNSSPrefix = $("<input name='NssPrefixe' tabIndex='-1' title='Préfixe NSS' disabled='' class='' style='text-align: right;' type='text' size='3' maxLength='3' readOnly='' value='756.'/>");
		this.$elementToPutObject.before($inputNSSPrefix);
	}
};