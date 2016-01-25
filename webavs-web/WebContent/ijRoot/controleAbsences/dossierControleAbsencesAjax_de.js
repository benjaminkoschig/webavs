var DetailZoneDialogObject = function(o_options) {
	this.$zone = o_options.$zone;
	this.s_selecteurDeclencheur = o_options.s_selecteurDeclencheur;
	this.f_validate = o_options.f_validate;
	this.s_okLabel = o_options.s_okLabel;
	this.s_cancelLabel = o_options.s_cancelLabel;
	this.s_deleteLabel = o_options.s_deleteLabel;
	this.s_cancelDelete = o_options.s_cancelDelete;
	this.s_titleLabel = o_options.s_titleLabel;
	this.s_width = o_options.s_width;
	this.f_callbackOk = o_options.f_callbackOk;
	this.f_callbackCancel = o_options.f_callbackCancel;
	this.o_ajaxObject = o_options.o_ajaxObject;

};

var detailZoneDialog = {
	options : {
		$zone : $('.detailZoneDialog'),
		s_selecteurDeclencheur : '.showDetailZoneDialog',
		s_okLabel : globazGlobal.labelBoutonValider,
		s_cancelLabel : globazGlobal.labelBoutonAnnuler,
		s_deleteLabel : globazGlobal.labelBoutonSupprimer,
		s_titleLabel : '',
		b_wantStopEdition: true,
		s_width : 'auto',
		f_validate : function() {
			return true;
		},
		f_callbackOk : function() {
		},
		f_callbackCancel : function() {
		},
		o_ajaxObject : null,
	},
	optionsDefinies : null,
	m_options : {},

	init : function(m_options) {
		var that = this;
		if (m_options) {
			this.optionsDefinies = Object.create($.extend({}, this.options,
					m_options));
		}
		var $body = $('body');
		$(this.optionsDefinies.$zone).each(
				function() {
					var detailPopup = new DetailZoneDialogObject(
							that.optionsDefinies);

					detailPopup.$zone.dialog({
						autoOpen : false,
						modal : true,
						width : detailPopup.s_width,
						title : detailPopup.s_titleLabel,
						buttons : [ {
							text : detailPopup.s_okLabel,
							click : function() {
								if (detailPopup.f_validate()) {
									detailPopup.f_callbackOk();
									if(detailPopup.b_wantStopEdition){
										$(this).dialog('close');
										if (detailPopup.o_ajaxObject) {
											detailPopup.o_ajaxObject.stopEdition();
										}
									}
								}
							}
						}, {
							text : detailPopup.s_deleteLabel,
							click : function() {
								if (detailPopup.o_ajaxObject) {
									detailPopup.o_ajaxObject.ajaxDeleteEntity(detailPopup.o_ajaxObject.selectedEntityId);
									$(this).dialog('close');
									detailPopup.o_ajaxObject.stopEdition();
								}
							}
						}, {
							text : detailPopup.s_cancelLabel,
							click : function() {
								if (detailPopup.o_ajaxObject) {
									detailPopup.o_ajaxObject.stopEdition();
								}
								detailPopup.f_callbackCancel();
								$(this).dialog('close');
							}
						} ]
					});

					$(document).on('click', detailPopup.s_selecteurDeclencheur,
							function() {
								if (detailPopup.o_ajaxObject) {
									detailPopup.o_ajaxObject.startEdition();
								}
								detailPopup.$zone.dialog('open');
							});
				});
	}
};

$(document).ready(function() {
});