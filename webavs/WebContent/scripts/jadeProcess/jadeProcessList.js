/**
 * 
 */
function ProcessList(container) {
	var that = this;
	this.ACTION_AJAX = "fx.process.jadeProcessListAjax";//ACTION_AJAX_EXECUTION
	this.mainContainer = container;
	this.table = this.mainContainer.find(".areaTabel");
	this.detail = this.mainContainer.find(".areaDetail");
	this.isManual = false;
	this.afterRetrieve = function (data) {};
	this.idExecutionProcess = null;		
	this.s_key_process = S_KEY_PROCESS;
	this.getParametresForFind = function () {
		var that = this;
		return {"searchModel.forStep": that.mainContainer.find("#forStep").val(),
				"searchModel.forCsEtat": that.mainContainer.find("#forCsEtat").val(),
				"searchModel.forDescription": that.mainContainer.find("#forDescription").val(),
				"searchModel.forDateFinExecution": that.mainContainer.find("#forDateFinExecution").val(),
				"searchModel.forDateDebutExecution": that.mainContainer.find("#forDateDebutExecution").val(),
				"searchModel.forDateCreation": that.mainContainer.find("#forDateCreation").val(),
				"searchModel.forNbEntity": that.mainContainer.find("#forNbEntity").val(),
				"searchModel.forNameProcess": this.s_key_process	
				};
	};
	
	this.getParametres = function () {
		var o_map =	{
			'keyProcess': this.s_key_process, 
			'idExecutionProcess': this.idExecutionProcess
		};
		var o_mapDetail =  $.extend(o_map, properties.serialize());	
		return $.extend(o_mapDetail, this.getParametresForFind());
	};	
	
	this.clearFields = function () {};
	this.formatTable = function () {
		var that  = this;
		this.$trInTbody.delegate('td', 'click', function () {
			var $td = $(this);
			that.idExecutionProcess = null;
			var id = $td.closest('tr').attr("idEntity");
			
			if ($td.hasClass("deleteProcess")) {
				that.idExecutionProcess = id; 
				that.ajaxDeleteEntity();
			
			} else {
				location.href = "fx?userAction=fx.process.jadeProcess.afficher&id=" + id;
			}	
		
		});
		
		
		$(".deleteProcessButton").button();
		
		//zone.ajaxDeleteEntity(zone.selectedEntityId);
	};
	
	this.getParentViewBean = function () {
		return null;
	};
	
	this.setParentViewBean = function (newViewBean) {
		
	};
	
	this.init(function () {
		this.colorTableRows();
		this.stopEdition();
		this.createPagination(10, [10, 20, 50]);
		this.sortTable();
		this.addSearch();
		this.ajaxFind();
	});
} 

ProcessList.prototype = AbstractScalableAJAXTableZone;


$(function () {
	$('.area').each(function () {
		var $that = $(this);
		var that = this;
		var zone = new ProcessList($that);
		this.zone = zone;
		var $newProcess = $("#newProcess");
		$newProcess.button();
		properties.init($that, S_ACTION_SPECIFIED);
		
		$newProcess.click(function  () {
			zone.stopEdition();
			zone.startEdition();
			zone.detail.show();
		});
		 
		$that.find('.btnAjaxCancel').click(function () {
			zone.stopEdition();
		}).end()
		.find('.btnAjaxValidate').click(function () {			
			if (notationManager.validateAndDisplayError()) {
				zone.validateEdition();
			    $(".areaDetail").dialog("close");
				//zone.detail.hide();
			}
		}).end()
		.find('.btnAjaxDelete').click(function () {
			zone.ajaxDeleteEntity(zone.selectedEntityId);
		}).end()
		.find('.btnAjaxAdd').click(function () {
			if ($.trim(S_ACTION_SPECIFIED).length > 0) {
			    properties.appendProperties();
			    $(".areaDetail").dialog({ width: 680 });
				zone.stopEdition();
				zone.startEdition();
			} else {
				zone.validateEdition();
				zone.detail.hide();
			}
		}); 
		 
	});
});