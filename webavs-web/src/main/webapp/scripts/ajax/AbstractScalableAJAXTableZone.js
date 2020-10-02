var AbstractScalableAJAXTableZone = {
	
	// variables
	selectedEntityId: null,
	isModifyingEntity: false,
	
	modifiedZoneClass: 'areaAJAXModified',
	highlightRowClass: 'highlight',
	hoverRowClass: 'hover',
	
	table: null,
	mainContainer: null,
	titleContainer: null,
	detail: null,
	currentViewBean: null,
	
	ACTION_AJAX: null,
	$inputsButton: null,
	$formElement: null,
	$trInTbody: null,
	b_init: null, // Permet de savoir si on se trouve dans la fonction d'initialisation
	b_initDefaultArea: null,
	b_applyStyle: true, // indique si l'on veux appliquer les styles css 
	b_wantReloadEntityAfterUpdate: true,
	s_spy: null, // Indique le chemin de l'objet qui contient le spy
	
	/*-----pagination----*/
	n_offset: 0,
	n_definedSearchSize: 100, // nombre d'éléments à afficher pour la pagination
	n_pageCourante: 1,
	b_pagination: false, // indique si l'on fait de la pagination
	b_paginationHasMoreElement: false, // indique à la pagination si il y a encore des éléments à afficher
	$paginationButonLeft: null,
	$paginationButonRight: null,
	s_orderKey: null,
	b_shift: false, // indique si la touche shift est pressé
	t_tableSize: null,
	s_uniqueNameObjet: null,
	// constants
	XML_DETAIL_TAG: "message",
	XML_LIST_TAG: "liste",
	s_defaultOrder: null,
	
	// functions
	getXMLData: function ($tree, tag) { 
		return $tree.find(tag).toXML().replace("<" + tag + ">", "").replace("</" + tag + ">", "");
	},

	colorTableRowsOdd: function () {
		this.$trInTbody.filter(':odd').addClass('odd');
	},
	
	colorTableRows: function (b_fromateTable) {
		var b_fromat = (typeof b_fromateTable !== 'undefined') ? b_fromateTable : true, that = this;
		this.colorTableRowsOdd();
		// create blank line if empty
		if (this.$trInTbody.length === 0) {
			var nbcol = this.table.find('thead tr').eq(0).find("td,th").length;
				
			var s_line = "<tr class='trVide' style='line-height:5px; background-color:white'>";
			var s_td = '';
			
			for (var i = 0; i < nbcol; i++) {
				s_td = s_td + "<td>&#160;</td>";
			}
			s_line = s_line + s_td + "</tr>";
	
			// add line
			this.table.find('tbody').append(s_line);
		}
		
		if (b_fromat) {
			this.formatTable();
		}
		
		this.$trInTbody.hover(function () {
			var $this = $(this);
			if (that.isEntitySelectable() && $this.attr('idEntity')) {
				$this.addClass(that.hoverRowClass);
			}
		}, function () {
			var $this = $(this);
			if($this.attr('idEntity')){
				$this.removeClass(that.hoverRowClass);
			}
		});
	},
	
	isEntitySelectable: function () {
		return !this.isModifyingEntity;
	},	
	
	afterStopEdition: function () {},
	
	// permet de lancer les événements pour les notations
	triggerEvent: function () {
		this.mainContainer.triggerHandler(eventConstant.AJAX_DETAIIL_REFRESH);
	},
	
	stopEdition: function () {
		this.$inputsButton.hide();
		this.disabeldEnableForm(true);
		this.mainContainer.find('.btnAjaxAdd').show();
		if (!this.b_init) {
			this.executeClearFields();
			this.mainContainer.removeClass(this.modifiedZoneClass);
			this.$trInTbody.filter('.' + this.highlightRowClass).removeClass(this.highlightRowClass);
			this.triggerEvent(); 
			this.mainContainer.triggerHandler(eventConstant.AJAX_STOP_EDITION);
		}
		this.isModifyingEntity = false;
		this.selectedEntityId = null;
		this.currentViewBean = null;
		this.afterStopEdition();	
	},
	

	sorTableWithAjax: function () {

	},
	
	sortTable: function () {
		var $thead = this.table.find('thead');
		
		if ($thead.hasClass('complexAjaxHeader')) {
			var $tr = $thead.find('tr');
			
			if ($tr.length === 1) {
				this.sortStandardTable($tr.find('th'));
			} else if ($tr.length > 1) {
				var $th = $tr.find('th');
				
				var n_increment = 0;
				$th.each(function (i) {
					var $this = $(this);
					
					if ($this.hasClass('notSortable')) {
						if (n_increment === i && $this.attr('rowspan') > 1) {
							n_increment++;
						}
					} else {
						$(this).attr('complexAjaxTableIndex', n_increment);
						n_increment++;
					}
				});

				this.sortStandardTable($th.filter(':not(.notSortable)'));
			}
		} else {
			this.sortStandardTable($thead.find('th:not(.notSortable)'));
		}
	},

	sortComplexTable: function ($th) {
		
	},

	sortStandardTable: function ($th) {
		var that = this,
			getNumberForAmount = function (s_value) {
				var f_amount = globazNotation.utilsFormatter.amountTofloat(s_value);
				if (!isNaN(f_amount)) {
					return f_amount;
				} else {
					return false;
				}
			},
			findSortKey = function ($cell) {
				var s_value = $cell.find('.sort-key').text().toUpperCase() + ' ' + $cell.text().toUpperCase(),
					f_amount = getNumberForAmount.call(this, s_value),
					s_date = globazNotation.utilsDate.normalizeFullGlobazDate(s_value);
				if (s_date) {
					return s_date;
				} else if (f_amount) {
					return f_amount;
				} else {
					return s_value;
				}
			};

		$th.wrapInner('<span>');

		$th.append($('<span/>', {
			"class": "ui-icon ui-icon-arrowthick-2-n-s",
			style: 'display:inline-block'
		}));
		
		$th.each(function (column) {
			var $this = $(this);
			$this.attr('nowrap', 'nowrap');
			$this.addClass('sortable ' + that.hoverRowClass).click(function () {
				var $this = $(this),
					sortDirection = $this.is('.sorted-asc') ? -1 : 1,
					$rows = that.$trInTbody,
					$uiIconsNotWithThis = $th.not(this).find('.ui-icon'),
					$uiIcon = $this.find('.ui-icon'),
					$tbody = that.table.find('tbody'),
					s_whereKey = $this.attr('data-orderKey');
				
				$uiIconsNotWithThis.removeClass('ui-icon-arrowthick-1-n ui-icon-arrowthick-1-s');
				$uiIconsNotWithThis.addClass('ui-icon-arrowthick-2-n-s');
				$rows.detach();
				$uiIcon.removeClass('ui-icon-arrowthick-2-n-s');

				if (sortDirection === -1) {
					$uiIcon.removeClass('ui-icon-arrowthick-1-s');
					$uiIcon.addClass('ui-icon-arrowthick-1-n');
				} else {
					$uiIcon.removeClass('ui-icon-arrowthick-1-n');
					$uiIcon.addClass('ui-icon-arrowthick-1-s');
				}
				
				if (!globazNotation.utils.isEmpty(s_whereKey)) {
					if (sortDirection === -1) {
						that.ajaxFind(true, s_whereKey + "Desc");
					} else {
						that.ajaxFind(true, s_whereKey + "Asc");
					}
				} else {
					//loop through all the rows and find  
					$.each($rows, function (index, row) {
						row.$row = $(row);
						if (row.id === "") {
							row.id = index;
						}

						var n_index = $this.attr('complexAjaxTableIndex');
						
						if (!n_index) {
							n_index = $this.get(0).cellIndex;
						}

						row.notSortable = row.$row.is('.notSortable');
						row.sortKey = findSortKey.call(that, row.$row.find('td:eq(' + n_index + ')'));
					});

					if ($this.hasClass('dateSortable')) {
						$rows.sort(function (date1, date2) {
							if (date1.notSortable || date2.notSortable) {
								return 0;
							}

							if ($.type(date1.sortKey) === "string" && $.type(date2.sortKey) === "string") {
								var s_date1 = $.trim(date1.sortKey),
									s_date2 = $.trim(date2.sortKey);
								
								if (s_date1 === '' && s_date1 === s_date2) {
									return 0;
								}
								
								if (s_date1 === '') {
									return sortDirection;
								}
								if (s_date2 === '') {
									return -sortDirection;
								}
	
								if (globazNotation.utilsDate.areDatesSame(s_date1, s_date2)) {
									return 0;
								}
								if (globazNotation.utilsDate.convertJSDateToDBstringDateFormat(s_date1) > globazNotation.utilsDate.convertJSDateToDBstringDateFormat(s_date2)) {
									return sortDirection;
								} else {
									return -sortDirection;
								}
							}
							return 0;
						});
					} else {
						//compare and sort the rows alphabetically
						$rows.sort(function (a, b) {
							if (a.notSortable || b.notSortable) {
								return 0;
							}
							
							if (a.sortKey < b.sortKey) {
								return -sortDirection;  
							}
							if (a.sortKey > b.sortKey) {
								return sortDirection;  
							}
							return 0;  
						});
					}

					$rows.each(function (row) {
						if (row % 2 !== 0) {
							this.$row.addClass('odd');
						} else {
							this.$row.removeClass('odd');
						}
						$tbody.append(this); 
						this.sortKey = null;  
					});  
				}

				//identify the column sort order  
				$th.removeClass('sorted-asc sorted-desc');  

				if (sortDirection === 1) {
					$this.addClass('sorted-asc');
				} else {
					$this.addClass('sorted-desc');
				}
			}); 
		});
	},
	
	afterStartEdition: function () {},
	
	disabeldEnableForm: function (b_disabeEnable) {
		ajaxUtils.disabeldEnableForm(this.mainContainer, this.$formElement, b_disabeEnable);
	},
	
	startEdition: function () {
		this.mainContainer.triggerHandler(eventConstant.EXECUTE_MOTEUR_ON_HIDDEN_ELEMENTS);
		this.disabeldEnableForm(false);
		this.$inputsButton.hide().filter('.btnAjaxValidate,.btnAjaxCancel').show();
		this.isModifyingEntity = true;
		this.mainContainer.addClass(this.modifiedZoneClass);
		ajaxUtils.addFocusOnFirstElement(this.detail);
		this.triggerEvent();
		this.afterStartEdition();
	},
	
	validateEdition: function () {
		if (this.selectedEntityId) {
			this.ajaxUpdateEntity(this.selectedEntityId);
		} else {
			this.ajaxAddEntity();
		}
		this.mainContainer.triggerHandler(eventConstant.AJAX_VALIDATE_EDITION);		
	},
	validateEditionV2: function () {
		if (this.selectedEntityId) {
			this.ajaxUpdateEntityV2(this.selectedEntityId);
		} else {
			this.ajaxAddEntityWithLog();
		}
		this.mainContainer.triggerHandler(eventConstant.AJAX_VALIDATE_EDITION);
	},
	
	addNoCache : function  (data) {
		//data.noCache = globazNotation.utilsDate.toDayInStringJadeFormate() + (new Date()).getMilliseconds();
		return ajaxUtils.addNoCache(data);
	},
	
	addParamettersForRead: function(data) {
		return {};
	},
	
	ajaxLoadEntity: function (idEntity, b_loadViewBean) {	
		var that = this, data;
		if (!b_loadViewBean) {
			ajaxUtils.addAnimLoading(this.mainContainer);
			this.selectedEntityId = idEntity;
			this.$trInTbody.filter('.' + this.highlightRowClass).removeClass(this.highlightRowClass);
			this.$trInTbody.filter('[idEntity="' + idEntity + '"]').addClass(this.highlightRowClass);
		}
		if (!globazNotation.utils.isEmpty(idEntity)) {
			data = {"userAction": this.ACTION_AJAX + ".afficherAJAX",
					"idEntity": idEntity};
			data = this.addNoCache(data);
			data = $.extend({},this.addParamettersForRead(),data);
			
			$.ajax({ 
				data: data,
				context: that,		
				success: function (data) {
							this.onLoadAjaxData(data, b_loadViewBean, idEntity);
						},
				type: "GET"			
			});		
		}
	},
	
	ajaxLoadEntityViewBean: function () {
		
	},
	
	hasError: function (data) {
		return ajaxUtils.hasError(data);
	},
	hasErrorNew: function (data) {
		var log = $(data).find('messageError').text();
		if (log) {
			return true;
		}else{
			return false;
		}
	},
	
	onLoadAjaxData: function (data, b_loadViewBean, idEntity) {
		var $tree = $(data), isXml = $.isXMLDoc(data); 
		if (($tree.find(this.XML_DETAIL_TAG).length > 0 && isXml) || $.isPlainObject(data)) {
			if (this.hasError(data)) {
				ajaxUtils.displayError(data);
				return;
			} else {
				ajaxUtils.displayLogsIfExsite(data);
			}
			if (isXml) {
				this.currentViewBean = $tree.find('viewBean').text();
				data = $tree;
			} else {
				this.currentViewBean = data.viewBeanSerialized;
				data = data.viewBean;
			}
			this.mainContainer.triggerHandler('loaded.ajax');
			if (!b_loadViewBean) {
				this.mainContainer.triggerHandler('stopEdition.ajax');
				this.executeClearFields();
				this.executeAfterRetrieve(data,idEntity);
				//this.triggerEvent();

				this.mainContainer.find('span').trigger('change',{from:eventConstant.AJAX_CHANGE});
				this.$formElement.trigger('change', {from:eventConstant.AJAX_CHANGE});
				this.mainContainer.find('span').trigger(eventConstant.AJAX_CHANGE);
		
				this.$formElement.trigger(eventConstant.AJAX_CHANGE);
				this.mainContainer.triggerHandler(eventConstant.AJAX_LOAD_DATA);
				this.mainContainer.find('.btnAjaxDelete,.btnAjaxUpdate').show();
			}
		} else {
			ajaxUtils.displayError(data);
		}	

		this.mainContainer.find(".loading_horizonzal").remove();

	},
	
	ajaxUpdateEntity: function (idEntity) {
		ajaxUtils.beforeAjax(this.mainContainer);
		var that = this;
		var parametres = this.getParametres();
		parametres.userAction = this.ACTION_AJAX + ".modifierAJAX";
		parametres.viewBean = this.currentViewBean;
		parametres.parentViewBean = this.getParentViewBean();
		//this.n_offset = 0;
		parametres = this.addParmetresForPagination(parametres);
		$.ajax({
			data: parametres,
			contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			context: that,
			success: function (data) {
				this.onUpdateAjaxComplete(data, idEntity);
			},
			type: "POST"
		});
	},
	ajaxUpdateEntityV2: function (idEntity) {
		ajaxUtils.beforeAjax(this.mainContainer);
		var that = this;
		var parametres = this.getParametres();
		parametres.userAction = this.ACTION_AJAX + ".modifierAJAX";
		parametres.viewBean = this.currentViewBean;
		parametres.parentViewBean = this.getParentViewBean();
		//this.n_offset = 0;
		parametres = this.addParmetresForPagination(parametres);
		$.ajax({
			data: parametres,
			contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			context: that,
			success: function (data) {
				this.onUpdateAjaxCompleteV2(data, idEntity);
			},
			type: "POST"
		});

	},

	onUpdateAjaxComplete: function (data, n_idEntity) {
		var $tree = $(data), n_currentIdEntity = "";
		if (this.hasError(data)) {
			ajaxUtils.displayError(data);
			ajaxUtils.afterAjaxComplete(this.mainContainer);
			return;
		} else  {
			ajaxUtils.displayLogsIfExsite(data);
		}
		
		
		ajaxUtils.afterAjaxComplete(this.mainContainer);
		if ($tree.find(this.XML_DETAIL_TAG).length > 0) {
			var xmlData = this.getXMLData($tree, this.XML_LIST_TAG);
			var $tbody = this.table.find('tbody');
			$tbody.children().remove().end().append(xmlData);
			
			this.$trInTbody = $tbody.find('tr');
			this.setParentViewBean($tree.find('viewBean').text());
			this.onAddTableEvent();
			this.colorTableRows();
			this.mainContainer.triggerHandler(eventConstant.AJAX_UPDATE_COMPLETE);
			this.stopEdition();
			n_currentIdEntity = $tree.find('message').attr('currentIdEntity');
			if (n_currentIdEntity != undefined && n_currentIdEntity.length) {
				n_idEntity = n_currentIdEntity;
			}
			if (this.b_wantReloadEntityAfterUpdate && n_idEntity && !globazNotation.utils.isEmpty(n_idEntity)) {
				if($tree.find("idEntity").length > 0){
					n_idEntity = $tree.find('idEntity').text();
				}
				this.displayLoadDetail(n_idEntity);
			}
			this.activeDesactiveButtonPagination();
			this.changeNbResultPagination();
			this.fixSizeToThInTable();
			notationManager.addNotationOnFragment($tbody);
		} else {
			if (typeof data.error === "undefined") {
				ajaxUtils.displayError(data);
			}
		}
		
		if(typeof this.afterUpdate === "function"){
			if(data.viewBean){
				this.afterUpdate(data.viewBean);
			} else {
				this.afterUpdate(data);
			}
		}
	},
	onUpdateAjaxCompleteV2: function (data, n_idEntity) {
		var $tree = $(data), n_currentIdEntity = "";
		if (this.hasErrorNew(data)) {
			ajaxUtils.displayLogsIfExisteV2(data,'error');
			ajaxUtils.afterAjaxComplete(this.mainContainer);
			return;
		} else  {
			ajaxUtils.displayLogsIfExisteV2(data,'warn');
		}


		ajaxUtils.afterAjaxComplete(this.mainContainer);
		if ($tree.find(this.XML_DETAIL_TAG).length > 0) {
			var xmlData = this.getXMLData($tree, this.XML_LIST_TAG);
			var $tbody = this.table.find('tbody');
			$tbody.children().remove().end().append(xmlData);

			this.$trInTbody = $tbody.find('tr');
			this.setParentViewBean($tree.find('viewBean').text());
			this.onAddTableEvent();
			this.colorTableRows();
			this.mainContainer.triggerHandler(eventConstant.AJAX_UPDATE_COMPLETE);
			this.stopEdition();
			n_currentIdEntity = $tree.find('message').attr('currentIdEntity');
			if (n_currentIdEntity != undefined && n_currentIdEntity.length) {
				n_idEntity = n_currentIdEntity;
			}
			if (this.b_wantReloadEntityAfterUpdate && n_idEntity && !globazNotation.utils.isEmpty(n_idEntity)) {
				if($tree.find("idEntity").length > 0){
					n_idEntity = $tree.find('idEntity').text();
				}
				this.displayLoadDetail(n_idEntity);
			}
			this.activeDesactiveButtonPagination();
			this.changeNbResultPagination();
			this.fixSizeToThInTable();
			notationManager.addNotationOnFragment($tbody);
		} else {
			if (typeof data.error === "undefined") {
				ajaxUtils.displayError(data);
			}
		}

		if(typeof this.afterUpdate === "function"){
			if(data.viewBean){
				this.afterUpdate(data.viewBean);
			} else {
				this.afterUpdate(data);
			}
		}
	},
	onFindAjaxComplete: function (data, n_idEntity) {
		var $tree = $(data), n_currentIdEntity = "";
		if (this.hasErrorNew(data)) {
			ajaxUtils.displayError(data);
			ajaxUtils.afterAjaxComplete(this.mainContainer);
			return;
		} else {
			ajaxUtils.displayLogsIfExsite(data);
		}
		ajaxUtils.afterAjaxComplete(this.mainContainer);
		
		if ($tree.find(this.XML_DETAIL_TAG).length > 0) {
			var xmlData = this.getXMLData($tree, this.XML_LIST_TAG);
			var $tbody = this.table.find('tbody');
			
			$tbody.empty().append(xmlData);
			this.$trInTbody = $tbody.find('tr');
		
			this.colorTableRows();
			this.mainContainer.triggerHandler(eventConstant.AJAX_FIND_COMPLETE);
			this.mainContainer.triggerHandler(eventConstant.AJAX_UPDATE_COMPLETE);
			this.stopEdition();
			n_currentIdEntity = $tree.find('message').attr('currentIdEntity');
			if (n_currentIdEntity != undefined && n_currentIdEntity.length) {
				n_idEntity = n_currentIdEntity;
			}
			if (!globazNotation.utils.isEmpty(n_idEntity) && n_idEntity > 0) {
				this.displayLoadDetail(n_idEntity);
			}
			if (this.b_pagination) {
				this.b_paginationHasMoreElement = ($tree.find('message').attr('hasMoreElement') === 'true') ? true : false;
				this.n_querySearchSize = ($tree.find('message').attr('querySize')) * 1;
				//this.n_querySearchSize = this.n_querySearchSize + this.n_definedSearchSize*1;
			}
			this.activeDesactiveButtonPagination();
			this.changeNbResultPagination();
			this.fixSizeToThInTable();
			notationManager.addNotationOnFragment($tbody);
		} else {
			//ajaxUtils.displayError(data);
		}
	},
	
	replaceTable: function () {
		
	},
	
	ajaxAddEntity: function () {
		ajaxUtils.beforeAjax(this.mainContainer);
		var that = this;
		var parametres = this.getParametres();
		parametres.parentViewBean = this.getParentViewBean();
		parametres.userAction = this.ACTION_AJAX + ".ajouterAJAX";
		this.n_offset = 0;
		parametres = this.addParmetresForPagination(parametres);
		$.ajax({
			data: parametres,
			context: that,
			success: function (data) {
				this.onUpdateAjaxComplete(data);
			},
			type: "POST"
		});					
	},

	ajaxAddEntityWithLog: function () {
		ajaxUtils.beforeAjax(this.mainContainer);
		var that = this;
		var parametres = this.getParametres();
		parametres.parentViewBean = this.getParentViewBean();
		parametres.userAction = this.ACTION_AJAX + ".ajouterAJAX";
		this.n_offset = 0;
		parametres = this.addParmetresForPagination(parametres);
		$.ajax({
			data: parametres,
			context: that,
			success: function (data) {
				this.onUpdateAjaxCompleteV2(data);
			},
			type: "POST"
		});
	},
	addParmetresForPagination: function (parametres) {
		if (this.b_pagination) {
			parametres.offset = this.n_offset;
			parametres.definedSearchSize = this.n_definedSearchSize;
		}
		return parametres;
	},
	
	addSorting : function () {
		var orderkey = "";
		this.table.find("th").each(function() {
			$that = $(this);

			if ($that.attr("data-defaultOrder") != undefined) {
				orderkey = $that.attr("data-orderkey")+$that.attr("data-defaultOrder");
			}
		});
		return orderkey;
//		if (typeof s_orderKey !== 'undefined') {
//			this.s_orderKey = s_orderKey;
//		} else {
//			var s_defaultOrder = this.table.attr("data-defaultOrder");
//			if (typeof s_defaultOrder !== 'undefined') {
//				this.s_orderKey = s_defaultOrder;
//			}
//		}
//		if (s_orderKey !== null) {
//			parametres.orderKey = this.s_orderKey;
//		}
//		return parametres;
	},
	
	ajaxDeleteEntity: function (data) {
		var message ;
		
	
		
		if (typeof JSP_DELETE_MESSAGE_INFO !== "undefined"){
			message = JSP_DELETE_MESSAGE_INFO;
		} else {
			message = jQuery.i18n.prop("ajax.deleteMessage");
		}
		if (window.confirm(message)) {
			ajaxUtils.beforeAjax(this.mainContainer);
			var that = this;
			var parametres = this.getParametres();
			parametres.userAction = this.ACTION_AJAX + ".supprimerAJAX";
			parametres.viewBean = this.currentViewBean;
			parametres.parentViewBean = this.getParentViewBean();
			//this.n_offset = 0;
			parametres = this.addParmetresForPagination(parametres);
			if($.isPlainObject(data)){
				parametres = $.extend(data, parametres);
			}
			$.ajax({
				data: parametres,
				context: that,
				success: function (data) {
					this.onUpdateAjaxComplete(data);
					this.onDeleteAjax(data);
				},
				type: "POST"	
			});
		}
	},
	
	onDeleteAjax: function (data){},
	
	displayLoadDetail: function (n_idEntity) {
		this.ajaxLoadEntity(n_idEntity);
		this.showDetail();
	},
	
	addTableEventOnElements: function ($elements) {
		var that = this, $trs = null;
		if($elements.length == 0){
			$trs = this.table;
		} else {
			$trs = $elements;
		}
		//$trs.find('td').unbind('click');
		$trs.delegate('td', 'click', function (event) {
			var $this = $(this), $parent = $this.parent();
			if (that.isEntitySelectable() &&  event.target == this && !$parent.is(".notSortable") && $parent.attr('idEntity')) {
				that.displayLoadDetail($parent.attr('idEntity'));
			}
		});	
	},
	
	activeDesactiveButtonPagination: function () {
		if (this.b_pagination) {
			if (!this.b_paginationHasMoreElement) {
				this.$paginationButonRight.button({ disabled: true });
			} else {
				this.$paginationButonRight.button({ disabled: false });
			}
			if (this.n_offset - this.n_definedSearchSize < 0) {
				this.$paginationButonLeft.button({ disabled: true });
			} else {
				this.$paginationButonLeft.button({ disabled: false });
			}
			
			if (this.n_offset == 0) {
				this.$paginationButonFirst.button({ disabled: true });
			} else {
				this.$paginationButonFirst.button({ disabled: false });
			}

			if (this.n_offset == this.countNbOffsetStepForPagination() || this.$trInTbody.length <= 1) {
				this.$paginationButonLast.button({ disabled: true });
			} else {
				this.$paginationButonLast.button({ disabled: false });
			}
		}
	},
	
	createListeForPagination: function (t_sizeArray, $zoneBouton) {
		if (t_sizeArray != undefined && t_sizeArray.length > 0) {
			var $sizeSelector = $('<select class="sizeSelector noFocus"></select>'),
				s_option = '',
				b_valExist = false,
				that = this,
				cookie_size = $.cookie(that.s_uniqueNameObjet + "_n_definedSearchSize");
			
			for (var i = 0; i < t_sizeArray.length; i++) {
				if (t_sizeArray[i] == this.n_definedSearchSize) {
					b_valExist = true;
				}
			}
			
			if (!b_valExist) {
				t_sizeArray.push(this.n_definedSearchSize * 1);
				t_sizeArray.sort(function (a, b) {
					return a - b;
				});
			}
			
			if (cookie_size !== null) {
				that.n_definedSearchSize = cookie_size;
			}
			
			for (var i = 0; i < t_sizeArray.length; i++) {
				if (this.n_definedSearchSize == t_sizeArray[i]) {
					s_option += '<option selected="selected" value="' + t_sizeArray[i] + '">' + t_sizeArray[i] + '</option>';
				} else {
					s_option += '<option value="' + t_sizeArray[i] + '">' + t_sizeArray[i] + '</option>';
				}
			}
			
			$zoneBouton.append($sizeSelector);
			$sizeSelector.append(s_option);
			
			$sizeSelector.change(function () {
				that.n_definedSearchSize = $(this).val();
				$.cookie(that.s_uniqueNameObjet + "_n_definedSearchSize", that.n_definedSearchSize, { expires: 7});
				that.n_pageCourante = 1;
				that.ajaxFind(true);				
			});
		} 
	},
	
	counNbPage: function () {
		var n_page = 0, n_modulo;
		
		n_modulo = this.n_querySearchSize % this.n_definedSearchSize;
		
		this.n_querySearchSize = this.n_querySearchSize * 1;
		if (n_modulo === 0) {
			n_page = (this.n_querySearchSize / this.n_definedSearchSize);
		} else {
			n_page = ((this.n_querySearchSize - n_modulo) / this.n_definedSearchSize) + 1;
		}	
		return n_page;
	},
	
	countNbOffsetStepForPagination: function () {
		var n_setp = 0, n_modulo;
		
		n_modulo = this.n_querySearchSize % this.n_definedSearchSize;
		
		this.n_querySearchSize = this.n_querySearchSize * 1;
		if (n_modulo === 0) {
			n_setp = this.n_definedSearchSize * ((this.n_querySearchSize / this.n_definedSearchSize) - 1);
		} else {
			n_setp = this.n_definedSearchSize * (((this.n_querySearchSize - n_modulo) / this.n_definedSearchSize));
		}	
		return n_setp;
	},
	
	changeNbResultPagination: function () {
		if (this.b_pagination) {
			this.nbResutl = '[' + (1 + this.n_offset) + '-' + (this.n_offset + this.$trInTbody.length) + "]" + this.n_querySearchSize;
			this.$nbResutl.text(this.nbResutl);
			this.mainContainer.find('.paginationButton .nbPages').text(this.n_pageCourante + "/" + this.counNbPage());
		}
	},

	getTableWidth: function () {
		var n_tableWidth = this.table.width();
		if (!jQuery.support.boxModel) {
			var n_window = $(window).width();
			if (n_window < n_tableWidth) {
				n_tableWidth = n_window - 15;
			}
		}
		return n_tableWidth;
	},

	createPagination: function (n_definedSearchSize, t_sizeArray) {
		var zoneBouton = $('<div class="paginationButton ui-widget-header">' +
							'<span class="refresh">&nbsp;</span>' +
							'<span class="nbResult">[0-0]0</span>' +
				            '<span class="first">&nbsp;</span>' +
							'<span class="left">&nbsp;</span>' +
							'<span class="page"><span class="nbPages"></span><input class="choixPage" type="text" /> </span>' +
							'<span class="right">&nbsp;</span>' +
							'<span class="last">&nbsp;</span>' +
							'</div>'),
		that = this,
		$html = $('html');
	
	
		var n_tableWidth = this.getTableWidth();
		var $conteneurTable = $('<div>', {
			'class': "masterAreaTable",
			style: 'width:' + (n_tableWidth)
		});
	
		var $choixPage = zoneBouton.find(".choixPage");
		$choixPage.change(function () {
			if (that.counNbPage() >= this.value) {
				that.n_pageCourante = this.value;
				if (this.value * 1 == 0) {
					that.n_offset = 1 * that.n_definedSearchSize;
				} else {
					that.n_offset = (this.value - 1) * that.n_definedSearchSize;
					that.ajaxFind(false);
				}
			}
		});
		
		this.table.wrap($conteneurTable);
		this.$paginationButonRight = zoneBouton.find(".right");
		this.$paginationButonRefresh = zoneBouton.find(".refresh");
		this.$paginationButonLeft = zoneBouton.find(".left");
		this.$paginationButonFirst = zoneBouton.find(".first");
		this.$paginationButonLast = zoneBouton.find(".last");
		this.table.after(zoneBouton);
		this.$paginationButonRefresh.button({text: false, icons: {primary: "ui-icon-refresh"}});
		this.$paginationButonLeft.button({text: false, icons: {primary: "ui-icon-carat-1-w"}});
		this.$paginationButonRight.button({text: false, icons: {primary: "ui-icon-carat-1-e"}});
		this.$paginationButonFirst.button({text: false, icons: {primary: "ui-icon-seek-first"}});
		this.$paginationButonLast.button({text: false, icons: {primary: "ui-icon-seek-end"}});
		this.$nbResutl = zoneBouton.find(".nbResult");
		if (typeof n_definedSearchSize !== 'undefined') {
			this.n_definedSearchSize = n_definedSearchSize;	
		}
		
		this.$paginationButonRefresh.click(function () {
			that.ajaxFind(false);
		});
		
		this.$paginationButonRight.click(function () {
			if (that.b_paginationHasMoreElement) {
				that.n_offset = (that.n_offset * 1) + (that.n_definedSearchSize * 1);
				that.n_pageCourante = that.n_pageCourante + 1;
				that.ajaxFind(false);
			}
		});
		
		this.$paginationButonLeft.click(function () {
			if (that.n_offset > 0) {
				that.n_offset = (that.n_offset * 1) - (that.n_definedSearchSize * 1);
				that.n_pageCourante = that.n_pageCourante - 1;
				that.ajaxFind(false);
			}
		});
		
		this.$paginationButonFirst.click(function () {
			if (that.n_offset > 0) {
				that.n_offset = 0;
				that.n_pageCourante = 1;
				that.ajaxFind(false);
			}
		});
		
		this.$paginationButonLast.click(function () {
			if ((that.n_offset + that.$trInTbody.length) < that.n_querySearchSize) {
				that.n_offset =  that.countNbOffsetStepForPagination();
				that.n_pageCourante = that.counNbPage();
				that.ajaxFind(false);
			}
		});
		
		$html.keyup(function (e) {
			that.setMasterKey(false, e);
		});
		
		$html.keydown(function (e) {
			that.setMasterKey(true, e);
			if (that.b_shift) {
				//fléche gauche
				if (e.which === 37) {
					that.$paginationButonLeft.click();
				}
				// fléche droite
				if (e.which === 39) {
					that.$paginationButonRight.click();
				}
			}
		});
		
		this.createListeForPagination(t_sizeArray, zoneBouton);
		this.createTableHeaderForPagination();
		this.b_pagination = true;
		var s_width = that.table.attr('width');
		that.table.css("border", '0');
		if (typeof s_width !== "undefined" && s_width.indexOf("%")) {
			that.table.attr('width', '100%');
		}
	},

	createTableHeaderForPagination: function () {
		var $thead = this.table.children('thead');
		if ($thead.length === 1 && $thead.hasClass('useAjaxTableHeader')) {
			
			if ($thead.hasClass('trHeader')) {
				var n_nbColonnes = $thead.children('tr:last').children('th').length;
				var n_nbTr = $thead.children('tr').length;
				if (n_nbColonnes > 0) {
					if (n_nbTr > 1) {
						var $firstTr = $thead.children('tr:first');
						var $th = $firstTr.children('th');
						$th.addClass('notSortable ui-widget-header');
						$th.filter(':first').addClass('borderTopLeftRound');
						$th.filter(':last').addClass('borderTopRightRound');
					} else {
						var s_tr = '<tr class="ui-widget-header search ajaxTableHeader">';	
						for (var i = 0; i < n_nbColonnes; i++) {
							s_tr += '<th class="notSortable ui-widget-header">&nbsp;</th>';
						}
						s_tr += '</tr>';
						
						var $tr = $(s_tr);
						$thead.prepend($tr);
					}
				}
			} else {
				var $div = $('<div>', {
					'class': 'ui-widget-header ajaxTableHeader borderTopLeftRound borderTopRightRound',
					style: 'width:' + this.getTableWidth()
				});
				this.table.before($div);
			}
		}
	},
	
	setMasterKey: function (b_actived, event) {
		//Touche shift
		if (16 === event.which) {
			this.b_shift = b_actived;
		}
	},
	
	addTableEvent: function () {
		this.addTableEventOnElements(this.$trInTbody);
		this.onAddTableEvent();
	},
	
	onAddTableEvent: function () {},
	
	
	beforeShowDetail: function () {
		var that = this;
		this.mainContainer.triggerHandler(eventConstant.EXECUTE_MOTEUR_ON_HIDDEN_ELEMENTS);
		this.mainContainer.addClass('areaSelected');
		this.triggerEvent();
		this.getBtnContainer().show();
		var $buttons = this.getBtnContainer().find(":button");	
		$buttons.prop('disabled', false);
		//hack pour ne pas excuter l'action du bouton sur ENTER et mettre le focus sur le bon bouton
		setTimeout(function () {
				ajaxUtils.addFocusOnFirstElement(that.detail); 
			}, 15);
	},
	
	showDetail: function () {
		this.beforeShowDetail();
		this.detail.show();
		this.mainContainer.triggerHandler(eventConstant.AJAX_STOP_SHOW_DETAIL);
	},
	
	hideDetail: function () {
		this.detail.hide();
		this.mainContainer.removeClass('areaSelected');
		this.getBtnContainer().hide();
		this.stopEdition();
	},
	
	// abstract functions
	clearFields: function () {},

	executeClearFields: function () {
		this.clearFields();
	},
	
	afterRetrieve: function ($data, idEntity) {
		throw "afterRetrieve function not implemented";
	},
	
	executeAfterRetrieve: function (data,idEntity){
		this.afterRetrieve(data,idEntity);
	},
	
	getParametres: function ($data) {
		throw "getParametres function not implemented";
	},
	
	formatTable: function () {},
	
	getParametresForFind: function () {
		
	},
	
	fixSizeToThInTable: function () {
		var that = this;
		if (this.t_tableSize === null) {
			this.t_tableSize = [];
			this.table.find('thead tr th').each(function () {
				var $th = $(this);
				$th.css('width', $th.width());
				that.t_tableSize.push($th.width()); 
			});
		}
	},
	
	ajaxFind: function (b_setOffsetToZero, s_orderKey) {
		var parametres = this.getParametresForFind(),
			that = this;
		parametres.userAction = this.ACTION_AJAX + ".listerAJAX";
		parametres.viewBean = this.currentViewBean;
		parametres.parentViewBean = this.getParentViewBean();

		ajaxUtils.beforeAjax(this.mainContainer);
		
		if ((typeof b_setOffsetToZero === 'undefined') || b_setOffsetToZero) {
			this.n_offset = 0;
			this.n_pageCourante = 1;
		} 
		
		parametres = this.addParmetresForPagination(parametres);
		
		this.s_orderKey = this.addSorting();
		
		if (typeof s_orderKey !== 'undefined') {
			this.s_orderKey = s_orderKey;
		}
		
		if (s_orderKey !== null) {
			parametres.orderKey = this.s_orderKey;
		}
		
		$.ajax({
			data: parametres,
			context: that,
			success: function (data) {
						this.onFindAjaxComplete(data);
					},
			type: "POST"
		});	
	},
	
	getParentViewBean: function () {
		throw "getParentViewBean not implemented";
	},
	setParentViewBean: function (newViewBean) {
		throw "setParentViewBean not implemented";
	},	
	
	getBtnContainer: function () {
		return this.mainContainer.find('.btnAjax');
	},
	
	addInteractionOnTitre: function () {
		var $areaTitre = this.titleContainer,
			n_fontSize = null,
			s_fontSize = ($areaTitre.css('font-size'));
		if (s_fontSize != undefined && s_fontSize.length) {
			n_fontSize = parseFloat(s_fontSize.split('px')[0]);	
			$areaTitre.mouseover(function () {
				var $this = $(this);
				$this.addClass("ui-state-hover");
				$this.css("font-weight","normal");
			}).mouseleave(function () {
				var $this = $(this);
				$this.removeClass("ui-state-hover");
			});
		}
	},

	createMapForSendData: function (s_selector) {
		return ajaxUtils.createMapForSendData(this.detail, s_selector);
	},
	
	defaultLoadData: function ($data, s_selector, b_shortId) {
		var $detail = this.detail;
		ajaxUtils.displaySpy(this.s_spy, $data, $detail);
		return ajaxUtils.defaultLoadData($data, $detail, s_selector, b_shortId);
	},
	
	formatAndAddEventOnTitle: function () { 
		var that = this;
		if (this.titleContainer != null) {
			if (this.titleContainer.length) {
				this.addInteractionOnTitre();
				this.titleContainer.click(function () {
					if (that.detail.is(':visible')) {
						that.hideDetail();
					} else {
						that.showDetail();
					}
				});
			} else {
				this.showDetail();
			}
		}
	},
	
	applyStyle: function () { 
		if (!this.b_initDefaultArea && this.b_applyStyle) {
			this.table.addClass('areaDataTable');
			this.detail.addClass('areaDetail');
			if (this.titleContainer != null && this.titleContainer.length) {
				this.titleContainer.addClass('areaTitre');
			}
		}
	},
	
	initDefaultArea: function ($containr) {
		this.mainContainer = $containr;
		this.table = this.mainContainer.find(".areaDataTable");
		this.detail = this.mainContainer.find(".areaDetail");
		this.titleContainer = this.mainContainer.find(".areaTitre");
		this.modifiedZoneClass = "areaDFModified";
		this.b_initDefaultArea = true;
	},
	
	/**
	 * Simule le comportment d'un CAPAGE en beaucoup mieux :)
	 * @param n_definedSearchSize
	 * @param t_sizeArray
	 */
	capage: function (n_definedSearchSize, t_sizeArray, b_executefind) {
		// On ajoute les comportement sur le tabelaux
		this.addTableEventOnElements(this.$trInTbody);
		this.addSorting();
		//Pour afficher par défaut la zone détail
		this.detail.show();
		this.stopEdition();
		this.list(n_definedSearchSize, t_sizeArray, b_executefind);
	},
	
	
	/**
	 * Simule le comportment d'une page de liste en beaucoup mieux :)
	 * @param n_definedSearchSize
	 * @param t_sizeArray
	 */
	list: function (n_definedSearchSize, t_sizeArray, b_executefind) {
		this.onAddTableEvent();
		this.colorTableRows();

		this.getBtnContainer().show();
		
		if (typeof n_definedSearchSize !== "undefined") {
			this.createPagination(n_definedSearchSize, t_sizeArray);
		}
		//excute la recheche directement
		if (typeof b_executefind === "undefined" || b_executefind) {
			this.ajaxFind();
		}
		
		this.sortTable();
	},
	
	
	addSearch: function () {
		var $tdSearch = this.table.find('thead tr').eq(0).find("td"), that = this;
		$tdSearch.addClass("ui-widget-header");
		
		jsManager.addBefore(function () {
$tdSearch.each(function () {
				var $td = $(this);
				var $div = $("<div style='position:relative'>");
				$td.children().wrapAll($div);
				var $inputs = $td.find(":input:visible");
				$td.css("padding", "4px 8px");
				$inputs.css({"width": "100%"});
				
				
				$inputs.change(function () {
					that.ajaxFind();
				});
				setTimeout(function () {
						$inputs.each(function () {
						var $input = $(this);
						
						var $img = $("<img>", {
							src:  globazNotation.utils.getContextUrl() + "/scripts/ajax/imgs/search32.png",
							css: {position: "absolute", 
								  right: 0,
								  top: 0,
								  margin: "2px 0 0 0"
								  },
							height: "16px",
							width: "16px"
						});

						$input.focusin(function () {
							$img.fadeOut();
						});
						$input.focusout(function () {
							$img.fadeIn();
						});
						$input.closest("div").append($img);
					});
				},50);
			});
		},"Ajout des fonction de search");
	
	
	},
	
	init: function (callback) {
		this.s_uniqueNameObjet = "ObjetAjax_" + $.getUrlVar('userAction') + "_" + ajaxUtils.conteur_nb_call_afterInint;
		this.mainContainer.data("UID_ajax_container",ajaxUtils.conteur_nb_call_afterInint);
		this.b_init = true;
		this.applyStyle();
		this.getBtnContainer().hide();
		this.detail.hide();	
		this.mainContainer.prepend("<div class='imgLoading' />");
		this.$inputsButton = this.mainContainer.find('.btnAjax input:button');
		this.$formElement = this.detail.find('input,select,textarea').not(this.$inputsButton);
		this.$trInTbody = this.table.find('tbody tr:visible');
		this.formatAndAddEventOnTitle();
		if (jQuery.isFunction(callback)) {
			callback.call(this);
		}
		ajaxUtils.afterInit(this.mainContainer);
		this.b_init = false;
	
	}
};
