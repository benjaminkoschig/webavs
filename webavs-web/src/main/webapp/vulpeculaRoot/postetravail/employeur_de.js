var employeurSearch;

$(function() {
	$(".areaSearch :input").change(function() {
			var $element = $(this);
			if($element.attr('id')=='searchModel.likeNumeroAffilie') {
				vulpeculaUtils.formatNoAffilie($element);
			}
			employeurSearch.ajaxFind();
	});
	
	defaultTableAjaxList.init({
		s_selector: "#",
		s_actionAjax : globazGlobal.ACTION_AJAX,
		userActionDetail : "vulpecula?userAction=vulpecula.postetravailvueglobale.employeurvueglobale.afficher&selectedId=",
		
		init : function() {
			employeurSearch = this;
			this.capage(20, [ 10, 20, 30, 50, 100 ]);
			this.addSearch();
		},
		getParametresForFind: function() {
			$search = $('.areaSearch');
			var m_map = ajaxUtils.createMapForSendData($search, this.s_selector);
			m_map['searchModel.forIdConvention'] = $('#idConvention').val();
			return m_map;
		},
		b_changeStack : true
	});
});
