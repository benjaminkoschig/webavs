var boutonValider = {
	$boutonValider: null,
	$form: null,
	$html: null,

	init: function () {
		this.$boutonValider = $("#boutonValider");
		this.$html = $('html');
		this.$form = $("form[name=mainForm]");
		this.$boutonValider.button();
		
		if (processLauched) {
			this.$boutonValider.button('disable');
		}
		
		this.addEvent();
		globazNotation.utils.validateFormOnSubmit(this.$form);
	},

	addEvent:function  () {
		var that = this;
		 
		this.$boutonValider.click(function (){
			that.$form.submit();
		});
		
		this.$html.bind(eventConstant.FORM_VALIDATION, function(param){
			if (!param.b_isFormValid) {
				that.$boutonValider.button('disable');
			}
		});
		globazNotation.utils.eventCloseConsole(function() {
			that.$boutonValider.button('enable');
		}, this);
	}
};

$(function(){
	boutonValider.init();
	boutonValider.$html.triggerHandler(eventConstant.AJAX_INIT_DONE);
});