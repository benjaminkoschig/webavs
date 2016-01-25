/**
 * 
 * 
 */

var properties = {
	$datail: null,
	zone: null,
	userAction: null,
	propertiesObject: null,
	templateFileImport:'<label for="FILE_PATH_FOR_POPULATION">File </label>'+
					   '<input type = "hidden" id="FILE_PATH_FOR_POPULATION">'+
		          	   '<input class="notSend" type = "file" data-g-upload="callBack:properties.callBackUpload"> ',
	
	
	init: function (zone, userAction) {
		this.$datail = zone.find("#detailProperties");
		this.zone = zone;
		this.userAction = userAction;
		if (typeof ID_EXECUTE_PROCEESS !== "undefined") {
			this.propertiesObject = new Properties(this.$datail);
		}
	},
	

	callBackUpload: function (data) {
		$("#FILE_PATH_FOR_POPULATION").val(data.path + "/" + data.fileName);
		var $btn = $(".areaDetail").find(".btnAjaxValidate");
		$btn.prop("disabled", false);
	},
	
	
	display: function (data) { 
		var $data = $(data);
		this.$datail.children().remove();
		this.$datail.append($data);
		if (globazGlobal.useFile) {
			this.$datail.prepend(this.templateFileImport);
			var $btn = $(".areaDetail").find(".btnAjaxValidate");
			$btn.prop("disabled", true);
		}
		notationManager.addNotationOnFragment(this.$datail);
	},
	
	
	serialize: function () {
		var map = {}, val = "";
		this.$datail.find(":input").each(function () {
			var s_name = this.name;
			var $this = $(this);
			if (!$this.hasClass("notSend")) {
				if (!$.trim(s_name).length) {
					s_name = this.id;
				}
				
				if ($.trim(s_name).length) {
					if (this.type === "radio" || this.type === "checkbox") {
						if ($this.prop("checked")) {
							map["jadeProcessProperties." + s_name] = $this.val();
						} 
					} else {
						map["jadeProcessProperties." + s_name] = $this.val();
					}
				}	
			}
		});
		return map;
	},
	
	appendProperties: function () {
		if ($.trim(this.userAction).length > 0) {
			var o_data = {};
			var that = this;
			var t_split = this.userAction.split("?");
			
			o_data.userAction = t_split[1];
			o_data.keyProcess = S_KEY_PROCESS;
		     if(typeof ID_EXECUTE_PROCEESS != "undefined") {
                 o_data.id = ID_EXECUTE_PROCEESS;
           }
			$.ajax({ 
					data: o_data,
					url: ajaxUtils.url.match(/(^\/.*\/)/g)[0] + t_split[0],
					success: function (data) {
						that.display(data);
						if (that.propertiesObject !== null) {
							that.zone.find(".btnAjaxValidate,.btnAjaxCancel").hide();
							that.propertiesObject.ajaxLoadEntity();
						} else {
							$(".btnAjax").show();
							that.zone.find(".btnAjaxValidate").show();
						}
						if (typeof overlayProgress !== "undefined") {
							setTimeout(function () {
								overlayProgress.changOverlay();
							}, 400);
						}
					},
					type: "GET"			
				});	
		}
		
	}
};

function Properties(container) {
	this.ACTION_AJAX = "fx.process.jadeProcessAjax";
	this.mainContainer = container;
	this.modifiedZoneClass = "areaPeriodesModified";
	
	this.selectedEntityId = ID_EXECUTE_PROCEESS;
	
	this.b_initReplace = false;
	// functions
	this.getInputProperties = function () {
		return properties.serialize();
	};
	
	this.getParametersForLoad = function () {
		
	};
	 
	this.onError = function (data) {
		for (var key in data.properties) {
			var $element = this.mainContainer.find("#" + key.replace(/\./g, "\\."));
			var value = data.properties[key];
			if ($element.length) {
				if ($element.get(0).type === "checkbox") {
					$element.prop('checked', value);
				}
				if ($element.is(":input")) {
					$element.val(value);
				} else {
					$element.text(value);
				}
			} else {	
				$element = this.mainContainer.find("#" + key.replace(/\./g, "\\.") + "_" + value);
				if ($element.length && $element.get(0).type === "radio") {
					$element.prop('checked', value);
				}
			}
			$element.change();
			//this.mainContainer.find("#"+key.replace(/\./g,"\\.")).val(data.properties[key]);
		}
	};
	
	this.onUpdate = function (data) {
		replaceInput.replaceInputBySpan();
	};
	
	this.onRetrieve = function (data) {
		this.onError(data);
		if (typeof replaceInput !== "undefined" && !this.b_initReplace) {
			// replaceInput.init( this.mainContainer);
			this.b_initReplace = true;
		}
		replaceInput.replaceInputBySpan();
		this.mainContainer.find(":input").not(":button").prop("disabled", true);
	};
			
	this.getParametres = function () {
		var that  = this;
		var o_map = {
			'keyProcess': S_KEY_PROCESS,
			idExecutionProcess: ID_EXECUTE_PROCEESS
		};
		return $.extend(true, o_map, this.getInputProperties());
	};
	
	this.getParentViewBean = function () {

	};
	this.setParentViewBean = function (newViewBean) {

	};
	
	this.init(function () {
		this.stopEdition();
	});
}

Properties.prototype = AbstractSimpleAJAXDetailZone;
 
