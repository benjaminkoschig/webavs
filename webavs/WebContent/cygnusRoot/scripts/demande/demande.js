/**
 * @author mbo
 */


var  hasConvention = {
		
		init: function () {
			var that = this;
			$("#widget5-multiwidget, #widget5-multiwidget").on(eventConstant.AJAX_SELECT_SUGGESTION,function(event,data) {
				that.callHasConvention(data.idTiers,$('[name="codeTypeDeSoin"]').val(),$('[name="codeSousTypeDeSoin"]').val());
			});
		},
		
		callback: function (data) {
			var $imgConvention =  $("#isConventionneImg");
			var s_src = "/webavs/images/erreur.gif";
			if (data) {
				s_src = "/webavs/images/ok.gif";
			}
			$imgConvention.attr("src",s_src);
		},
		
		callHasConvention: function (s_idTiers,s_typeDeSoin, s_sousTypeDeSoin) {
			var that = this;
			var options = {
				serviceClassName: 'ch.globaz.cygnus.business.services.ConventionService',
				serviceMethodName: 'getfournisseurConventionne',
				wantInitThreadContext: true,
				parametres: s_idTiers+","+s_typeDeSoin+","+s_sousTypeDeSoin,
				callBack: function (data) {
					that.callback(data);
				}
			};
			options = $.extend({},options);
			ajax = Object.create($.extend(true, {}, globazNotation.readwidget));
			ajax.options = options;
			ajax.read();
		}
};


$(function (){				
	hasConvention.init();
});
	
