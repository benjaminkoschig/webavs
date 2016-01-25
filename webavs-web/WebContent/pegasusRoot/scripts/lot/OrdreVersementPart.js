/**
 * @author DMA
 */

$(function () {
	var b_displayOvsRow = false;
	
	defaultTableAjax.init({
		s_container: ".areaOV",
		b_hasButtonNew: false,
		
		
		clearFields: function () {
			this.defaultClearFields();
			$("#adressePaiement").empty();
		},
		
		getParametresForFind: function () {
			var that = this;
			var map = {
				"searchModel.forIdPrestation": globazGlobal.idPrestation,
				"displayOvsRow": b_displayOvsRow
			};
			return map;
		},

		addParametersForRead: function () {
			return this.getParametresForFind();
		},
		
		afterRetrieve: function (data) {
			$(".btnAjax").hide();
			this.defaultLoadData(data, "#");
		
			if (data.ordreVersementCsType === globazGlobal.CS_ORDRE_VERSEMENT_TYPE_DETTE) {
				$(".notInDette").hide();
			} else {
				$(".notInDette").show();
			}
		},
		
		init: function () {	
			this.capage();
			this.detail.hide();
			var that = this;
			$("#displayRowOv").button().click(function (){
				b_displayOvsRow=!b_displayOvsRow;
				that.ajaxFind();
				that.detail.hide();
			});
		}
	});
});
