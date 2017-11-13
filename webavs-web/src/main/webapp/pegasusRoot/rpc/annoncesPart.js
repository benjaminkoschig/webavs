/**
 * @author DMA
 */

$(function () {
	var t = defaultTableAjax.init({
		s_actionAjax: globazGlobal.ACTION_AJAX,
		
		getParametresForFind: function () {
			var map = ajaxUtils.createMapForSendData(defaultTableAjax.optionsDefinit.$search, '#');
			if(map.nss){
				var numAVS = $.trim(map.nss);
				numAVS = numAVS.replace(new RegExp("\\.", 'g'), '');
				var newNumAVS = numAVS.substring(0,4);
				if (numAVS.length > 3) {
					newNumAVS += "." + numAVS.substring(4,8);
					if (numAVS.length > 7) {
						newNumAVS += "." + numAVS.substring(8,11);
					}
				}
				map.nss = "756."+ newNumAVS;
			}
			return map;
		},
		
		getParametres : function () {
			return {};
		},
		
		init: function () {	
			this.capage(15, [20, 30, 50, 100]);
		}
	});
});
