/**
 * 
 */
 
var buttonExecut = {
	$executeStep: null,
	$validerStep: null,
	$historique: null,
	b_loadHistorique: false,
	$tableStep: null,
	$createEntitys: null,
	
	init: function () {
		this.$executeStep = $(".executeStep");
		this.$validerStep = $(".validerStep");
		this.$createEntitys = $(".createEntitys");
		this.$tableStep = $("#steps");
		this.$abortStep = $(".abortStep");
		
		this.$executeStep.button({ disabled: !B_PROCESS_CURRENT_STEP_IS_EXECUTABLE});
		this.$validerStep.button();

		this.$abortStep.button({disabled: true});
		if (S_STATE_PROCESS === S_INIT || S_STATE_PROCESS === S_RUNNING_STEP) {
			this.$executeStep.button({disabled: true});
			this.$validerStep.button({disabled: true});
			this.$abortStep.button({disabled: false});
		}
		this.$createEntitys.button();
		this.addEvent();
	},

	confirm: function (f_ok, s_text) {
		$("<div>", {text:s_text}).dialog({
			resizable: false,
			height: 140,
			modal: true,
			buttons: {
				"Oui": function () {
					f_ok();
					$(this).dialog("close");
				},
				"Non": function () {
					$(this).dialog("close");
				}
			}
		});
	},
	
	execute: function (o_data, f_callBack,f_errorCallBack) {
		o_data.keyProcess = S_KEY_PROCESS;
		o_data.idExecutionProcess = ID_EXECUTE_PROCEESS;
		if (typeof executeSelectedError !== "undefined") {
			executeSelectedError.$executeSelected.button("option", "disabled", true);
			executeSelectedError.$executeOnError.button("option", "disabled", true);
		}
		ajaxUtils.ajaxExecut(S_ACTION, o_data, f_callBack, f_errorCallBack);
	},
	 /*
	 disable: function (b_disable) {
		 this.b_executionChecked = !b_disable;
	     this.$executeSelected.button( "option", "disabled", b_disable );
	 },
	 */
	filProgressBar: function (element, b_ajax, s_methode) {
		var $tr = $(element).closest("tr");
		
	    var pbar = $tr.find(".progressBar").eq(0).data("notation_progressbar");
		pbar.vars.b_ajax = true;
		pbar.options.className = "ch.globaz.jade.process.business.service.JadeProcessInfoService";
		pbar.options.methodName = s_methode;
	    pbar.options.init = true;
		pbar.options.wait = true;
		if (b_ajax) {
			pbar.setOptionsForAjax();
			pbar.peopleProgressBar(0); 
		} else {
			pbar.setValue(0);
		}
	},
	 
	addEvent: function  () {
		var that = this;  
		if (S_STATE_PROCESS === "INIT" || S_STATE_PROCESS === S_WAITING_TO_EXECUTE_STEP) {
			$(".currentInfosTimeTrForProcess").hide();
		}
		that.$executeStep.click(function () {
			var element = this;
			that.confirm(function () {
				that.$executeStep.button({disabled: true});
				that.$abortStep.button({disabled: false});
				that.$validerStep.button({disabled: true});
				that.execute({}, function (data) {that.filProgressBar(element, true, "getInfos"); } ); 
				$(".currentInfosTimeTrForProcess").show();
				overlayProgress.changOverlay();
			},"Voulez-vous éxecuter l'étape ?");
		});
		 
		this.$createEntitys.click(function (event) {
			var o_data = {serviceMethodName: "createEntitys"},
				element = this;
			that.$createEntitys.button({disabled: true});
			$(".param").find(".btnAjaxUpdate").button({disabled: true});			 
			that.execute(o_data, function (data) {
				//that.$executeStep.button({ disabled: false});
				that.$createEntitys.hide();
				that.$createEntitys.button({ disabled: true});
				$(".param").find(".btnAjaxUpdate").button({ disabled: true});
				that.filProgressBar(element, true, "getInfoCreationEntity");
			}, function (data){
				
				that.$createEntitys.button({ disabled: false});
			});
		});

		this.$abortStep.click(function () {
			that.confirm(function () {
				var o_data = {};
				var $zoneInfos = $("#process > div");
				var $process = $("#process");
				var n_heigth = $process.height();
				$process.css("height", n_heigth);
				o_data.userAction = "fx.job.job.abort";
				o_data.selectedId = o_data.uidJob = $(".uidJob").val();
				that.$abortStep.button({disabled: true});
				var $imageLoading = $("<img />", {
					src: notationManager.s_contextUrl + "/scripts/jsnotation/imgs/loading_big.gif",
					"class": "loading",
					id: "imageProcessLoading",
					heigth: $zoneInfos.innerHeight(),
					width: $zoneInfos.innerHeight(),
					style: "position: relative; " +
							"heigth: " + ($zoneInfos.innerHeight() * 1 - 6) + "px; " +
							"width: " + ($zoneInfos.innerHeight() * 1 - 6) + "px; " +
							"margin:0; padding:0; float:right; top:-7px; left:-3px"
							
							
				}).appendTo($("#processWait"));
				$process.css("height",n_heigth);
				$.ajax({ 
						data: o_data,
						url: ajaxUtils.url,
						success: function (data) {
						},
						type: "GET"			
					});	
			}, "Voulez-vous interrompre le processus ?");
		});		
		this.$validerStep.click(function () {
			var o_data = {
				userAction: S_ACTION + ".modifierAJAX",
				keyProcess: S_KEY_PROCESS,
				idExecutionProcess: ID_EXECUTE_PROCEESS,
				validerStep: true,
				nexteStep: false
			};
			var $tr = $(this).closest("tr");
			var that = this;			
			var $stpeInfos = $("#stpeInfos");
			 
			$.ajax({ 
					data: o_data,
					url: ajaxUtils.url,
					success: function (data) {
						if (!$.trim(data.error) &&  !(typeof data.messages !== "undefined"  && data.messages.length > 0)) {
							$current = $("#current");
							$current.attr("id","");
							if (typeof data.viewBean.customHtml !== "undefined") {
								$tr.find(".customHtml").empty().append("<span class='customHtmlAjax'>" + data.viewBean.customHtml + "</span>");
							}
							var $bouttons = $tr.find(".buttonProcess").detach();
							notationManager.addNotationOnFragment($tr.find(".customHtmlAjax"));
							$tr.find(".progressBar").find(".ui-progressbar-value").css("background-image", "url(images/pbar-ok.png)");
							var $trInfo = $tr.next();
						
							var $trInfoClone =  $trInfo.clone(false);
							$trInfoClone.find("span").text("");
							$trInfo.removeClass("currentInfosTimeTrForProcess");
							
							var $trNext = $trInfo.next();
							$trNext.find(".buttonProcess").append($bouttons);
							$trNext.find(".progressStep").attr("id","current");
							$trNext.after($trInfoClone);
							$trNext.next().hide();
							$bouttons.find(".executeStep").button("option", "disabled", false);
							$bouttons.find(".validerStep").button("option", "disabled", true);
							if (typeof data.viewBean.currentStepCustomHtml !== "undefined") {
								var $customCurrentHtml = $trNext.find(".customHtml");
								$customCurrentHtml.append("<span class='customHtmlAjax'>" + data.viewBean.currentStepCustomHtml + "</span>");
								notationManager.addNotationOnFragment($customCurrentHtml);
							}
							var t_split = $stpeInfos.text().split("/");
					
							overlayProgress.changOverlay($trNext);
							if ($.trim(t_split[0] === t_split[1])) {
								$stpeInfos.text(($.trim(t_split[0]) * 1 + 1) + " / " + t_split[1]);
							}
						} else { 
							ajaxUtils.displayError(data);
						}
					},
					type: "GET"			
				});	
		});
	}
};

var overlayProgress = {
		
	changOverlay: function ($trNewStep) {
		$("#overlayStepid").remove();
		if($trNewStep){
			$trNewStep.removeClass("stepNotExecuted");
		}
		$(".stepNotExecuted").overlay({b_height: true, b_width: false, id: "overlayStepid"});
	}		
}
var jquerySupport = {
	init: function () {
	 //this.show();
	},
	show: function () {
		var t_defTable = [{s_lable: "Key"}, {s_lable: "Value"}];
		var s_html = utilsTable.createTableKeyValue(t_defTable, jQuery.support);
		globazNotation.utils.console(s_html, "", "", 300);
	},
	callAll: function () {
	}
};
 
var callBackProgressBar = {
	$infosError: null,
	$tabs: null,
	n_compteur: 0,
	init: function (zone) {
		this.$tabs = $("#step");
		this.$infosError = $("#infosError");
		this.o_zoneAjax = zone; 
	},	
	
	formateTime: function (miliscondes) {
		return globazNotation.utilsFormatter.formateTime(miliscondes);
	},
	
	setValue : function (data, o_PorgressBar) {
		var id = "stepPbar_"+data.idCurrentStep;
		var $stepPbar =  $("#"+id);
		var $current = $("#current");
		var $elementToPutObject = o_PorgressBar.$elementToPutObject;
		if ($stepPbar.length && $elementToPutObject.attr("id") !== id){
			o_PorgressBar.$elementToPutObject = $stepPbar;
			this.changeBackgroundImage(data, o_PorgressBar );
			o_PorgressBar.setValue(data.progress * 100);
		} 
		if($current.length && $elementToPutObject.attr("id") !== "current" ){
			o_PorgressBar.$elementToPutObject = $current;
			o_PorgressBar.setValue(data.progress * 100);
			this.changeBackgroundImage(data, o_PorgressBar);
		}
		o_PorgressBar.$elementToPutObject = $elementToPutObject;
	},
	
	changeBackgroundImage: function (data, o_PorgressBar) {	
		if (data.csEtatProcess !== S_WAITING_TO_RUN && data.nbEntiteTreatCurrentStep > 0) {
		
			if (data.nbEntiteOnError > 0) {
				o_PorgressBar.changeBackground("pbar-orange.png");
			} else {
				o_PorgressBar.changeBackground("");
			}
		} else if (data.nbEntiteTreatCurrentStep === 0 && data.csEtatProcess ==S_RUNNING_STEP){
			o_PorgressBar.changeBackground("pbar-wait.gif");
		} 
	},
	 	
	callBack: function (data, o_PorgressBar) {
		var that = this;	
	
		if (typeof data.isCurrentStepExecutable !== "undefined" && !data.isCurrentStepExecutable ) {
			buttonExecut.$executeStep.button("disable");
		}

		if (typeof data.isStepValidable !== "undefined" && !data.isStepValidable) {
			buttonExecut.$validerStep.button("disable");
		}
		
		if (!data.createEntity) {
			data.timeCurrentStep = this.formateTime(data.timeCurrentStep);
			data.timeCurrentStep2 = this.formateTime(data.timeCurrentStep2);
			data.expectedTimeCurrentStep = this.formateTime(data.expectedTimeCurrentStep);
		}
		that.n_compteur++;
		var $elementChange = this.$tabs.find(".csEtatTranslate");
		if($.trim($elementChange.text()) != $.trim(data.csEtatTranslate)){
			globazNotation.growl.poll();
			$contentChang = $("#process > div"); 
			$contentChang.css("backgroundImage","none");
			$contentChang.css("backgroundColor", "wihte"),
			$contentChang.animate({
				backgroundColor: "#AF1154",
				opacity: 0.5,
				borderColor: "#FFFFFF"
			  }, 500, function() {
				  $contentChang.animate({
					backgroundColor: "#FFF",
					opacity: 1,
					borderColor: "#A6C9E2"
				  },2000, function (){
					  $contentChang.css("backgroundImage","");
				  });
			  });
			var s_color = $elementChange.css("color");
			$elementChange.animate({
					color: "#C1DEC3"
				},1000,
				function () {
				$elementChange.animate({
					color: s_color
					},2000);
			}); 
			$(".csEtatTranslate").text(data.csEtatTranslate);
			
		}
		
		if(data.createEntity) {
			ajaxUtils.defaultLoadData(data, this.$tabs, ".", true);
		}else {
			ajaxUtils.defaultLoadData(data, this.$tabs.find(".currentInfosTimeTrForProcess"), ".", true);
			var $currentStepForTime = $("#stepIdDisplay_"+data.idCurrentStep);
			if($currentStepForTime.length) {
				ajaxUtils.defaultLoadData(data, $currentStepForTime, ".", true);
			}
		}

		if (data.logs.length > 0) {
			o_PorgressBar.stop();
			var logsWarning = [];
			var logsErrors = [];
			for (var i = 0; i < data.logs.length; i++) {
				var log = data.logs[i];
				if (log.csType === S_LOG_PROCESS_WARNING) {
					logsWarning.push(log);
				} else if (log.csType === S_LOG_PROCESS_ERROR) {
					logsErrors.push(log);
				}
			}
			
			if (logsWarning.length) {
			
				boxLog.addBoxWarning($("#forWarning"), boxLog.createMessage(logsWarning));
				overlayProgress.changOverlay();
			}
			
			if (logsErrors.length) {
				boxLog.addBoxError($("#forError"), boxLog.createMessage(logsErrors));
				o_PorgressBar.$elementToPutObject.progressbar("widget").addClass("ui-state-error");
				overlayProgress.changOverlay();
			}
		
		} else if (data.processHasExceptionError !== true) {
			boxLog.removeBox();
			overlayProgress.changOverlay();
			o_PorgressBar.$elementToPutObject.progressbar("widget").removeClass("ui-state-error"); 
		}

		if ("ABORTED" === data.csEtatProcess) {
			o_PorgressBar.stop();
			$("#imageProcessLoading").remove();
			if (typeof data.isCurrentStepExecutable !== "undefined" && data.isCurrentStepExecutable) {
				buttonExecut.$executeStep.button("enable");
				
				if (typeof executeSelectedError !== "undefined" && $currentStep.length) {
					executeSelectedError.$executeSelected.button("enable");
				}
			}
		}
		if (S_WAITING_FOR_VALIDATE === data.csEtatProcess) {
			o_PorgressBar.options.wait = false;
			var $currentStep = $(".currentStep");
			o_PorgressBar.stop();

			buttonExecut.$abortStep.button({disabled: true});
	
			if (typeof data.isCurrentStepExecutable !== "undefined" && data.isCurrentStepExecutable) {
				buttonExecut.$executeStep.button("enable");
				
				if (typeof executeSelectedError !== "undefined" && $currentStep.length) {
					executeSelectedError.$executeSelected.button("enable");
				}
			}
			if (data.isStepValidable) {
				buttonExecut.$validerStep.button("enable");
		    }
			if ($currentStep.length) {
				if (typeof executeSelectedError !== "undefined") {
					executeSelectedError.$executeOnError.button("enable");
				}
				$currentStep.get(0).zone.ajaxFind();
			}
			
		} else if (S_POPULATION_CREATED === data.csEtatProcess ) {
			buttonExecut.$executeStep.button("enable");
			buttonExecut.$validerStep.button("disable");
			overlayProgress.changOverlay($("#creationDeLaPopulation").next());
			$current = $("#current");
			$current.attr("id",""); 
			if (!data.createEntity) { // Test si on execute bien une etape et non la création des entités
				o_PorgressBar.options.wait = false;
				o_PorgressBar.setValue(0);
			}
		} else if (S_WAITING_TO_EXECUTE_STEP === data.csEtatProcess) {
			o_PorgressBar.options.wait = false;
			o_PorgressBar.setValue(0);
		} else if ("INIT" === data.csEtatProcess) { 
			if (!data.createEntity) {
				o_PorgressBar.options.wait = false;
				o_PorgressBar.setValue(0);
			}
		} else if ("EXCEPTION_ERROR" === data.csEtatProcess) {
			o_PorgressBar.options.wait = false;
			o_PorgressBar.setValue(0);
		} else if ("COMPUTING_POPULATION" === data.csEtatProcess ){
			if (!o_PorgressBar.vars.n_idInterval) {
				o_PorgressBar.vars.b_ajax = true;
				o_PorgressBar.options.methodName = "getInfoCreationEntity";
				o_PorgressBar.options.timer = 2000;
				o_PorgressBar.setOptionsForAjax();
				o_PorgressBar.peopleProgressBar();
			}
			if (data.progress === 1) {
				o_PorgressBar.peopleProgressBar();
			}
		}
		if (!data.createEntity) {
			this.setValue(data, o_PorgressBar);
		}
		
	}
};

var replaceInput = {
	t_old: [],  
	$container: null,
	
	init: function ($container) {
		this.$container = $container;
		this.addInputsToOldTalbe();
	},

	addInputsToOldTalbe: function ()  {
		var that = this;
		this.$container.find(":input").not(":button").each(function () {
			var $this = $(this), $new = null;
			if (this.type !== "radio") {
				$new = $("<span>", { 
					text: $this.val(),
					id: $this.attr("id"),
					style: "vertical-align: middel;margin:2px;padding:1px;" +
					"width:" + $this.outerWidth() + "px;display:inline-block;",
					"class": $this.attr("class")
				});
				that.t_old.push({oldElement: $this, newElement: $new});
			}
		});
	},
	
	replaceInputBySpan: function () {
		$.each(this.t_old, function () {
			this.oldElement.replaceWith(this.newElement);
		}); 
    },
    
    replaceSpanByInput: function () {
		$.each(this.t_old, function () {
			this.newElement.replaceWith(this.oldElement);
		}); 
	}
};
 
var executionProcess = {
	init: function () {
		this.formatTime($(".infoProcesstime"));
		this.addEnvent();
		this.$detailProperties = $("#detailProperties");
		this.addEnvenButtonProperty();
	},
	
	formatTime: function ($container) {
		$container.find(".time").each(function () {
			var $this = $(this);
			$(this).text(globazNotation.utilsFormatter.formateTime($this.text()));
		});
	},

	addEnvent: function () {
		var $container =  $(".area");
		var that = this;
		var $trInTbody = $container.find("tbody").children();
		
		//progressEntity 
		$trInTbody.delegate('.progressStep', 'click', function () {
			var $this = $(this);
			var $tr =  $this.closest('tr');
			var id = $tr.attr("idEntity");
			tabs.addTab($tr.find('.description').text(), false, "fx?userAction=fx.process.jadeStep.afficher&id=" + id, id, $this);
		});

		$trInTbody.find(".progressStep").hover(function () {
			$(this).addClass("hover");
		}, function () {
			$(this).removeClass("hover");
		});
	},
	 
	addEnvenButtonProperty : function () {
		var $btnAjax = $(".btnAjax");
		$btnAjax.children().button(); 
		$btnAjax.find('.btnAjaxUpdate').click(function () {
			replaceInput.replaceSpanByInput();
			properties.propertiesObject.stopEdition();
			properties.propertiesObject.startEdition();
			
			$btnAjax.find(".btnAjaxValidate,.btnAjaxCancel").show();
			$btnAjax.find(".btnAjaxUpdate").hide();
		}).end()
		.find('.btnAjaxCancel').click(function () {
			replaceInput.replaceInputBySpan();
			properties.propertiesObject.stopEdition();
			$btnAjax.find(".btnAjaxUpdate").show();
			$btnAjax.find(".btnAjaxValidate,.btnAjaxCancel").hide();
		}).end()
		.find('.btnAjaxValidate').click(function () {
			if (notationManager.validateAndDisplayError()) {
				properties.propertiesObject.ajaxUpdateEntity();
				properties.propertiesObject.stopEdition();
				$btnAjax.find(".btnAjaxUpdate").show();
				$btnAjax.find(".btnAjaxValidate,.btnAjaxCancel").hide();
			}
		});
	}
};
 
	

$(function () {	
	$("body").hide();
	executionProcess.init();
	
	tabs.init();

	buttonExecut.init();
	callBackProgressBar.init();
	
	globazNotation.progressbar.options.callBack = function (data, o_PorgressBar) {
		callBackProgressBar.callBack(data, o_PorgressBar); 
	};

	jsManager.addAfter(function () {
		var timer = null;
		$("#current").each(function () {
			var $this = $(this);
			var pbar = $this.data("notation_progressbar");
			timer = pbar.options.timer;
			pbar.vars.b_ajax = true;
			pbar.options.className = "ch.globaz.jade.process.business.service.JadeProcessInfoService";
			pbar.options.methodName = "getInfos";
			pbar.setOptionsForAjax();
			pbar.vars.o_read.read();
			if (S_STATE_PROCESS === S_RUNNING_STEP) {
				setTimeout(function () {
					pbar.peopleProgressBar();
				}, 1300);
			}
			// pbar.options.timer=timer;
		});
		
		setTimeout(function () {
			properties.init($("#tabs"), S_ACTION_SPECIFIED);
			properties.appendProperties();
		}, 300); 

	}, "Execution du da la progress bar");
	$("body").show();
    ajaxUtils.triggerStartNotation();
	
	
 }); 