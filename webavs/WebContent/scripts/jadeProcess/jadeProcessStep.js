/**
 * @author DMA
 */
function ExecutionProcess(container) {
	// variables
	var that = this;
	this.ACTION_AJAX = "fx.process.jadeStepAjax";//ACTION_AJAX_EXECUTION
	this.mainContainer = container.find('.area');
	this.table = this.mainContainer.find(".areaTabel");
	this.detail = this.mainContainer.find(".areaDetail");
	this.$properties = this.mainContainer.find("#properties");
	this.$logInfos = this.mainContainer.find("#logInfos");
	this.$userActionDetail = this.mainContainer.find('.getWithUserAction');
	this.idExecuteProcess = ID_EXECUTE_PROCEESS;
	this.isManual = false;
	this.isInject = false;
	this.n_idEntity = null;
	this.ids = "";
	//this.titleContainer = this.mainContainer.find(".areaTitre");
	
	// functions	
	this.afterRetrieve = function (data) {
		var t_defTablePropertie = [{s_lable: "Libelle", s_name: "libelle", s_class: "libelle"}, {s_lable: "Value", s_name: "value", s_class: "propertiesValues"}];
		var t_defTableInfoLog = [{s_lable: "TranslateMessage", s_name: "messageId"}, {s_lable: "InfosLog", s_name: "infosLog"}, {s_lable: "idEntite", s_name: "idEntite"}];
		var n_maxDialogue = ($(document).width() * 1) - 60;
		var n_widthDialogue = 0;
		this.$properties.children().remove();
		if (data.proprietes && data.proprietes.length > 0) {
			var $tableProperties = utilsTable.createTable(t_defTablePropertie, data.proprietes, "Properties compute");
			this.$properties.append($tableProperties);
		}
		this.$logInfos.children().remove();
		if (data.simpleLogInfos && data.simpleLogInfos.length > 0) {
			var $tableInfoLog = utilsTable.createTable(t_defTableInfoLog, data.simpleLogInfos, "InfoLogs");
			this.$logInfos.append($tableInfoLog);
		}	
		
		if (data.urlEntite) {
			this.$userActionDetail.show();
			this.$userActionDetail.find('a').attr("href", data.urlEntite);
		} else {
			this.$userActionDetail.hide();
		}
		
		//this.detail.dialog( "option", "width",this.detail.outerWidth() );
		this.detail.dialog("open");
		this.detail.dialog({ title: data.simpleEntite.description});
		n_widthDialogue = this.detail.find("table").width() + 60;
		if (n_widthDialogue > n_maxDialogue) {
			n_widthDialogue = n_maxDialogue;
		} else if (this.detail.width() > n_widthDialogue) {
			//n_widthDialogue = this.detail.width() + 60;
		} 
		this.detail.dialog("option", "width", n_widthDialogue);
		this.detail.dialog("option", "position", 'center');

	};
	
	this.getParametresForFind = function () {
		var that = this;
		var map = {"searchModel.forRecherche": that.mainContainer.find(".forRecherche").val(),
				"searchModel.forStepOrdre": that.mainContainer.find(".forStep").val(),
				"searchModel.forCsEtat": that.mainContainer.find(".forCsEtat").val(),
				"searchModel.forId": that.mainContainer.find(".forId").val(),
				"searchModel.forIdExecuteProcess": that.idExecuteProcess,
				'displayInject': this.table.find("thead").find(".inject").length >0,
				"idStep":this.mainContainer.find(".idStep").val()
			};
		$(".dynamicSearch").each(function () {
			var s_search = $.trim(this.className.replace("dynamicSearch",""));
			map["searchModel."+s_search] = that.mainContainer.find("."+s_search).val();
		});
		return map;
	};
	
	this.addParamettersForRead = function () {
		return {"idStep":this.mainContainer.find(".idStep").val()};
	};
	
	
	this.getParametres = function() {
		var that = this;

		var o_map = {
			'simpleEntite.isManual': that.isManual,
			'idExecutionProcess:': that.idExecuteProcess,
			'keyProcess': S_KEY_PROCESS,
			'idEntity': that.n_idEntity,
			'injectEntity': that.isInject,
			'ids': that.ids,
			"idStep":this.mainContainer.find(".idStep").val(),
			'displayInject': this.table.find("thead").find(".inject").length >0
		};
		return $.extend(o_map, this.getParametresForFind());
	};	
	
	this.clearFields = function() {
	//	this.detail.clearInputForm();
	};
	
	this.getParentViewBean=function() {
		//return droit;	
	};
	
	this.setParentViewBean = function (newViewBean) {
		//droit=newViewBean;
	};
	
	//override
	
	this.onUpdateAjaxComplete = function (data, idEntity) {
		AbstractScalableAJAXTableZone.onUpdateAjaxComplete.call(this, data, idEntity);
		var $validerStep = $(".validerStep");
		if(data.error){
			globazNotation.utils.consoleError(data.error);
		}
		if(data && data.viewBean) {
			if (!data.viewBean.isStepValidable) {
				$validerStep.button("disable");
			} else {
				$validerStep.button("enable");
			}
		}
		
		var $tr = this.table.find("[identity='"+data.viewBean.simpleEntite.idEntite+"']");
		var map = {};
		this.table.find(".forCsEtat").find("option").each(function () {
			map[this.value]=this.label;
		});
			
		var $tds = $tr.find("td"); 
		$tds.eq(2).text(map[data.viewBean.simpleEntite.csEtat]);
		$tds.eq(1).text(data.viewBean.numStep);
		$tds.find(".manual").prop("checked",data.viewBean.simpleEntite.isManual);
		if("INJECT" === data.viewBean.simpleEntite.csEtat){
			$tr.removeClass("entiteError");
			$tr.removeClass("entiteWarning");
		
		}
		if("ERROR" === data.viewBean.simpleEntite.csEtat){
			$tr.addClass("entiteError");
		}
		if("WARNING" === data.viewBean.simpleEntite.csEtat){
			$tr.addClass("entiteWarning");
		}
	};
	
	this.changeManual = function (element) {
		var $this = $(element), $execute, $tr, b_entiteError;
		that.currentViewBean = null;
		that.isManual = $this.prop("checked");
		$tr = $this.closest('tr');
		$execute = $tr.find(".execute");
		if (that.isManual) {
			$execute.prop("checked", false);
		}
		
		$execute.prop("disabled", that.isManual);
		that.n_idEntity = $tr.attr('idEntity');
		that.ajaxUpdateEntity();
	};
	

	this.inject = function (element) {
		var $this = $(element), $tr ;
		this.currentViewBean = null;
		this.isInject = true;
		$tr = $this.closest('tr');
		this.n_idEntity = $tr.attr('idEntity');
		this.ids = this.n_idEntity; 
		this.idExecuteProcess = ID_EXECUTE_PROCEESS;
		this.ajaxUpdateEntity();
		this.isInject = false;
	};
	
	
	this.formatTable = function () {
		var that = this;
		$manual = this.mainContainer.find(".manual");
		$manual.each(function () {
			var $this = $(this), $execute, $tr, b_entiteError;
			$tr = $this.closest('tr');
			$execute = $tr.find(".execute");
			that.isManual = $this.prop("checked");
			if (!B_PROCESS_CURRENT_STEP_IS_EXECUTABLE && !that.isManual) {
				b_entiteError = $tr.hasClass("entiteError");
				$execute.prop("disabled", !b_entiteError);
			}
			
			$this.change(function () {
				that.changeManual(this);
			})
		});

		this.table.find("button.inject").button();
		//serialize
		this.$trInTbody.find('.execute').change(function () {
			var $this = $(this);
			cookieIds.put($this);
		});
	};
	
	this.saveManual = function () {
		
	};
	
	// initialization

	this.init(function () {
		this.$userActionDetail.find('a').button();
		var that = this;
		this.capage(10, [5, 25, 50, 100]);
		this.detail.hide();
		this.addSearch();
		this.$userActionDetail.click(function (event) {
			event.isImmediatePropagationStopped();
			event.preventDefault();
			event.stopPropagation();
			$(".highlight").find("td:first").text();
			tabs.addTab($(".highlight").find("td:first").text(), true, $(this).find("a").attr("href"));
			that.detail.dialog("close");
		});
		this.detail.dialog({ autoOpen: false});
		
		this.table.on("click", ".inject", function () {
			that.inject(this);
		});
	});

}


var executeSelectedError = {
	$executeSelected: null,
	$executeOnError: null,
	$executeStep: null,
	$validerStep: null,
	$nextStep: null,
	b_executionChecked: false,
	init: function ($container) {
		this.$executeSelected =  $container.find(".executeSelected");
		this.$executeOnError = $container.find(".executeOnError");
		this.$executeSelected.button({disabled: true});
		this.$executeOnError.button({disabled: (!B_PROCESS_ON_ERROR && (S_STATE_PROCESS === S_INIT || S_STATE_PROCESS === S_RUNNING_STEP))});
		this.addEvent();
	},

	execute: function(o_data, $button) { 
		o_data.idExecutionProcess = ID_EXECUTE_PROCEESS;
		o_data.keyProcess = S_KEY_PROCESS;
		this.$executeOnError.button("option", "disabled", true);
		this.$executeSelected.button("option", "disabled", true);
		buttonExecut.$executeStep.button("option", "disabled", true);
		buttonExecut.$validerStep.button("option", "disabled", true);
		var pbar = $button.closest(".stepInfos").find(".progressStep").data("notation_progressbar");
		if(pbar){
			pbar.setValue(0);
		}
		ajaxUtils.ajaxExecut(S_ACTION, o_data, function (data) {
			cookieIds.clean();
			buttonExecut.filProgressBar($(".executeStep"), true, "getInfos"); 
		});
	},

	disable: function (b_disable) {
		this.b_executionChecked = !b_disable;
		this.$executeSelected.button("option", "disabled", b_disable);
	},

	addEvent: function () {
		var that = this;
		this.$executeOnError.click(function () {
			that.execute({serviceMethodName: "executeEntiteOnError"}, that.$executeOnError);
		});
		
		this.$executeSelected.click(function () {
			if (that.b_executionChecked) {
				var ids = cookieIds.getIds();
				that.execute({serviceMethodName: "executeEntiteSelected", ids: ids}, that.$executeSelected);
			}
		});
	}
};


//fonction d'initialisation de la page lorsque JQuery est prêt
$(function () {
	try {
		executionProcess.formatTime($(".stepInfos"));
		ExecutionProcess.prototype = AbstractScalableAJAXTableZone;
		cookieIds.init();
	} catch (e) {

	}
	$(".currentStep").each(function () {
		if (this.zone === undefined) {
			var $that = $(this);
			var zone = new ExecutionProcess($that);
			$that.find(".recherche").change(function () {
				zone.ajaxFind();
			});
			this.zone = zone;

			executeSelectedError.init($that);
			
		}
	});

});