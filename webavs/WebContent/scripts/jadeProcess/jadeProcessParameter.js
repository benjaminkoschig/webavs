/**
 * 
 */


function Parameter($container) {
	this.s_trTemplate = "<tr id='{{idParameter}}'>" +
						"<td><input type='text' style='width:95%' class='parameter.keyStep' name='parameter.keyStep' value='{{keyStep}}' ></input></td>"+
						"<td>{{selectKey}}</td>"+
						"<td><input style='width:95%' type='text'class='parameter.value' name='parameter.value' value='{{value}}'></input></td>" +
						"<td style='text-align:center'><span style='width:20px; height:20px;'  class='delete ui-state-default ui-corner-all'></span></td>" +
					"</tr>";
  
	this.s_table = "<table width='100%'><thead><th>Key Step</th><th>Key</th><th>Value</th><th>Del</th></thead><tbody>{{tbody}}</tbody></table>";
  	
	this.ACTION_AJAX = "fx.process.jadeProcessParameterAjax";
	this.mainContainer = $container.find(".ajax");
	this.modifiedZoneClass = "areaPeriodesModified";
	this.$entityTr = null;
	
	this.selectedEntityId = ID_EXECUTE_PROCEESS;
	
	this.b_initReplace = false;
	/**
	 * On override cette fonction pour ne pas desactiver les champs inputs
	 */
	this.disabeldEnableForm = function () {
		
	};
	
	this.getInputProperties = function () {
		return properties.serialize();
	};
	
	this.getParametersForLoad = function () {
		return {'keyProcess': S_KEY_PROCESS};
	};
	 
	this.onError = function (data) {
		
	};
	
	this.onUpdate = function (data) {
		this.$entityTr.attr("id", data.parameter.idParameter);	
	};
	
	this.createSelect = function (data, s_key) {
		var s_select = "<select style='width:100%' class='parameter.key' name='parameter.key'> <option value=''></option>";
		var s_option = "";
		var s_selected;
		for (var i=0; data.jadeProcessParameterEnum.length>i; i++) {
			if(s_key === data.jadeProcessParameterEnum[i]){
				s_selected = "selected='selected'";
			}else {
				s_selected = "";
			}
			s_option += "<option "+s_selected+" value='"+data.jadeProcessParameterEnum[i]+"'>"+data.jadeProcessParameterEnum[i]+"</option>"
		}
		return s_select +s_option+"</select>"; 
	};
	
	this.onRetrieve = function (data) {
		var s_tr = "", 
			that = this,
			$table = null,
			s_select = "",
			t_parameters = data.parameters;
		
		for (var i=0;t_parameters.length>i;i++){
			t_parameters[i].selectKey = this.createSelect(data, t_parameters[i].key);
			s_tr += globazNotation.template.compile(t_parameters[i], this.s_trTemplate);
		}
		s_tr += globazNotation.template.compile({idParameter: "", keyStep: "", selectKey: this.createSelect(data, ""), value: ""}, this.s_trTemplate);
		
		$table = $(this.s_table.replace(new RegExp("{{tbody}}", 'g'), s_tr)); 
		
		$container.find(".close").button({
			text: false,
			icons: {primary:'ui-icon-closethick'}
		}).click(function (){
			$container.hide("slide", { direction: "up" }, 500);
		});
		
		$table.find(".delete").button({
			text: false,
			icons: {primary:'ui-icon-circle-minus'}
		
		}).click(function () {
			var $tr = $(this).closest("tr");
			var map = ajaxUtils.createMapForSendData($tr, ".");
			map.keyProcess = S_KEY_PROCESS,
			map.idEntity = $tr.attr("id");
			that.ajaxDeleteEntity(map);
			$tr.remove();
		});
		 
		$table.find("input").change(function () {
			var $tr = $(this).closest("tr");
			var map = ajaxUtils.createMapForSendData($tr, ".");
			map.keyProcess = S_KEY_PROCESS,
			map.idEntity = $tr.attr("id");
			that.$entityTr = $tr;
			that.ajaxAddEntity(map);
		});
		
		$template = $($table);
		this.mainContainer.append($template);
	
	};
			
	this.getParametres = function () {
		var that  = this;
		
		var o_map = {
			'keyProcess': S_KEY_PROCESS,
			idExecutionProcess: ID_EXECUTE_PROCEESS
		};
		return $.extend(true, o_map, this.getInputProperties());
	};
	
	this.isParametersEmpty = function (parametres) {
		if((parametres["parameter.keyStep"].length && parametres["parameter.value"].length && parametres["parameter.key"].length  )){
			return false ;
		} else {
			return true;
		}
	};
	
	this.ajaxAddEntity = function (parametres) {
		var that = this;
		if(!this.isParametersEmpty(parametres)){
			ajaxUtils.beforeAjax(this.mainContainer);
			parametres.userAction = this.ACTION_AJAX + ".ajouterAJAX";
			$.ajax({
				data: parametres,
				success: function (data) {
					that.onUpdateAjaxComplete(data);
				},
				type: "POST"
			});
		}
	};
	
	this.ajaxUpdateEntity = function (parametres) {
		var that = this;
		if(!this.isParametersEmpty(parametres)){
			ajaxUtils.beforeAjax(this.mainContainer);
			parametres.userAction = this.ACTION_AJAX + ".modifierAJAX";
			parametres.viewBean = this.currentViewBean;
			parametres.parentViewBean = this.getParentViewBean;
			
			$.ajax({
				data: parametres,
				contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
				success: function (data) {
					that.onUpdateAjaxComplete(data);
				},
				type: "POST"
			});
		}
	};
	
	
	
	this.ajaxDeleteEntity = function (parametres) {
		var that = this;
		ajaxUtils.beforeAjax(this.mainContainer);
		parametres.userAction = this.ACTION_AJAX + ".supprimerAJAX";
		parametres.viewBean = this.currentViewBean;
		parametres.parentViewBean = this.getParentViewBean;
		
		$.ajax({
			data: parametres,
			contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			success: function (data) {
				that.onUpdateAjaxComplete(data);
			},
			type: "POST"
		});					
	};
	
	this.getParentViewBean = function () {

	};
	this.setParentViewBean = function (newViewBean) {

	};
	
	this.init(function () {
		this.stopEdition();
	});
}

Parameter.prototype = AbstractSimpleAJAXDetailZone;


$(function (){
	var parKey = "";
	var ctrlAlt = "";
	$("html").keydown(function (e) {
		/*
		 * 80: p
		 * 65: a
		 * 82: r
		 */
		var par = "80_65_82_";
			
		if(e.keyCode === 18 || e.keyCode === 17 ){
			ctrlAlt += e.keyCode+"_";
			if(ctrlAlt === "17_18_" ){
				parKey= "";
				ctrlAlt = "";
			} 
		}
		
		if(e.altKey && e.ctrlKey){
			//On ne prends pas ctrl et alt
			if( e.keyCode != 18 && e.keyCode != 17){
				parKey += e.keyCode+"_";
				if(parKey === par){
					parKey = "";
					var $divContainer = $("<div class='container'><div class='ui-widget-header' style='text-align:right;background-color: #C7C7D0;'><span class='close' style='width:20px; height:20px;'></span></div><div class='ajax'></div><div>");
					$divContainer.css({
						position: "absolute",
						top: 0,
						left: "50%",
						"margin-left": "-330px",
					 	width: "660px"
					});
					$("body").append($divContainer);
					var parameter = new Parameter($divContainer); 
					parameter.ajaxLoadEntity();
				}
			}
		
		}
	});
});

