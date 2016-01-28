
jQuery.fn.globazWidget = function(	serviceClassName, 
									serviceMethodName, 
									managerClassName,
									criterias, 
									cstCriterias, 
									lineFormat, 
									modelReturnVariables, 
									help, 
									nbOfResult, 
									onSelectSuggestion, 
									nbOfCharBeforeLaunch, 
									dynamicCriterias,
									wantValueCache,
									initThreadContext) {
	var accentsTidy = function(s){
		var r=s.toLowerCase();
		r = r.replace(new RegExp("[àáâãäå]", 'g'),"a");
		r = r.replace(new RegExp("ç", 'g'),"c");
		r = r.replace(new RegExp("[èéêë]", 'g'),"e");
		r = r.replace(new RegExp("[ìíîï]", 'g'),"i");
		r = r.replace(new RegExp("ñ", 'g'),"n");
		r = r.replace(new RegExp("[òóôõö]", 'g'),"o");
		r = r.replace(new RegExp("[ùúûü]", 'g'),"u");
		r = r.replace(new RegExp("[ýÿ]", 'g'),"y");
		return r;
	};	

	return this.each(function() {
		this.options = {
				serviceClassName: serviceClassName, 
				serviceMethodName: serviceMethodName, 
				managerClassName: managerClassName,
				criterias: criterias, 
				cstCriterias: cstCriterias, 
				lineFormat: lineFormat, 
				modelReturnVariables: modelReturnVariables, 
				help: help, 
				nbOfResult: nbOfResult, 
				onSelectSuggestion: onSelectSuggestion, 
				nbOfCharBeforeLaunch: nbOfCharBeforeLaunch, 
				dynamicCriterias: dynamicCriterias,
				wantValueCache: wantValueCache,
				initThreadContext: initThreadContext
		};
		this.selectedSuggestion = null;
		var that = this;
		this.element = null;
		this.help = help;
		this.onSelectSuggestion=onSelectSuggestion;
		this.numberOfResult = nbOfResult;
		this.currentCursor = 0;
		this.currentPosition = 0;
		this.nbOfCharBeforeLaunch= typeof nbOfCharBeforeLaunch != 'undefined' ? nbOfCharBeforeLaunch : -1;
		this.tocken = null;
		this.oldValue = $(this).val();
		this.o_cache = {page:0};
		this.n_key ='';
		this.$currentListe = null;

		if(this.nbOfCharBeforeLaunch == -1){
			this.nbOfCharBeforeLaunch=3;
		}

		var helpInitialString = "No help available";
		if (this.help) {
			helpInitialString = $.merge( [], that.help);
			helpInitialString[0] = '<b>' + helpInitialString[0] + '</b>';
			helpInitialString = helpInitialString.join(',');
		}

		this.waitingWindow = $('<div/>');
		var $imageLoading = $('<img/>',{	'id': 'loadImg', 
											'src': globazNotation.utils.getContextUrl() + '/images/loading_animated.gif',
											'width': $(this).height(),
											'height': $(this).height()
										});
		this.waitingWindow.append($imageLoading).hide().appendTo('body');
		
		this.positionWaitingWindow = function($element){
			that.waitingWindow.css( {	'position': 'absolute',
										'left': ($element.offset().left + $element.outerWidth() - $element.height() - 3) + 'px',
										'top': ($element.offset().top + 3) + 'px',
										'z-index': 10000
									}).find('#loadImg').attr('width',$element.height()).attr('height',$element.height());
			
		};

		this.hintWindow = $('<div/>',{'class':'widgetHint'});
		this.hintWindow.append(helpInitialString).hide().appendTo('body');

		this.hintWindow.css( {	'left': $(this).offset().left + 'px',
								'top': ($(this).offset().top - this.hintWindow.outerHeight()) + 'px'
								});

		this.suggestionWindow = $('<div class="widget"/>');

		this.suggestionWindow.css({	'left': $(this).offset().left + 'px',
									'top': ($(this).offset().top + $(this).outerHeight()) + 'px',
									'width': '500px'
								}).hide().appendTo('body');

		this.getCriterias = function (cstCriterias){
			var s_criterias = "";
			if (cstCriterias.length){
				s_criterias = cstCriterias;
			}
			if (dynamicCriterias!=null){
				if (cstCriterias.length){
					s_criterias+=",";
				}
				s_criterias+=dynamicCriterias();
			}
			return s_criterias;
		};

		this.getUrl = function(){
			var url = null;
			if (typeof MAIN_URL == 'undefined') {
				url = $('[name=formAction]').attr('content');
				if (globazNotation.utils.isEmpty(url)) { 
					globazNotation.utils.consoleError('Unable to find URL for Auto-complete !','Error autoComplete');
				}
			}else{
				url = MAIN_URL;
			}
			return url;
		};

		
		
		this.loadSuggestions = function() {
			var $this = $(this);
			var value = $this.val();
			// le replace() enlève les éventuelles virgules présentes avant de comparer avec nbOfCharBeforeLaunch
			if ($this.val().replace(/,/g, '').length < that.nbOfCharBeforeLaunch) {
				return;
			}

			var cursor = that.numberOfResult + ',' + that.currentCursor;
			that.positionWaitingWindow($this);
			that.waitingWindow.show();
			that.tocken = globazNotation.utilsDate.convertJSDateToDBstringDateFormat(new Date()) + (new Date()).getMilliseconds();
			
			that.suggestionWindow.css( {
				"left" : $this.offset().left+ "px",
				"top" : ($this.offset().top + $this.outerHeight())+ "px"
			});

			var dataPost = {	"userAction" : "widget.action.jade.lister",
					"serviceClassName" : serviceClassName,
					"serviceMethodName" : serviceMethodName,
					"managerClassName" : managerClassName,
					"lineFormat" : lineFormat,
					"cursor" : cursor,
					"criterias" : criterias,
					"cstCriterias" : that.getCriterias(cstCriterias),
					"modelReturnVariables" : modelReturnVariables,
					"searchText" : accentsTidy($(this).val()),
					"tocken":that.tocken
			};
			
			if (this.options.initThreadContext) {
				dataPost.initThreadContext = initThreadContext;
			}
			
			$.ajax({	
				type: 'POST',
				url: that.getUrl(),
				dataType:'xml',
				data: dataPost,
				success: function(data) {
					var $data = $(data);
					var $ul = $($data.find("ul").toXML());
					if ($data.find('tocken').text() == that.tocken){
						var $errorJson = $data.find('errorJson');
						if($errorJson.length){
						    s_errorJson = $errorJson.text();
						    
						    try {
						    	var o_error = $.parseJSON(s_errorJson);
						    	data.errorBean = o_error;
						    	ajaxUtils.displayError(data);
						    } catch (e) {
						    	globazNotation.utils.consoleError(s_errorJson);
						    }
							
						} else {
							if (!$(that).is(":focus") && $ul.find('li.widgetSuggestion').length == 1 && that.options.wantValueCache) {
								that.selectSuggestion($ul.find('li.widgetSuggestion'));
								that.waitingWindow.hide();
							} else if (!$(that).is(":focus") && !that.options.wantValueCache) {
								that.waitingWindow.hide();
							} else {
								if (!$(that).is(":focus")) {
									that.focus();
								}
								
								$ul.find('li.widgetSuggestion').mouseenter(function() {
									if (that.selectedSuggestion) {
										$(that.selectedSuggestion).removeClass('widgetSuggestionHOVER');
									}
									that.selectedSuggestion = this;
									$(this).addClass('widgetSuggestionHOVER');
									that.currentPosition = $(that.suggestionWindow.find('li.widgetSuggestion')).index(that.selectedSuggestion);
								}).click(function() {
									that.selectSuggestion(this);
									$this.focus();
									$this.focusNextInputField();
									$this.change();
								});
								if(that.o_cache[value] == undefined){
									that.o_cache[value] = [];
								}
								that.o_cache[value].push($ul);
								that.$currentListe = $ul;
								that.displayList();
							}
						}
					}
				}
			});
		};

		this.displayList = function () {
			that.suggestionWindow.find('ul').remove();
			that.suggestionWindow.append(that.$currentListe);
			that.suggestionWindow.show();
			that.waitingWindow.hide();
			that.changeSelection();
		};

		this.refreshHint = function() {
			var Sel = document.selection.createRange();
			Sel.moveStart('character', -that.value.length);
			var caretRes = Sel.text.match(/,/g);
			var cursorPos = 0;
			if (caretRes) {
				cursorPos = caretRes.length;
			}

			if (this.help) {
				var helpString = $.merge( [], this.help);
				if (cursorPos < this.help.length) {
					helpString[cursorPos] = '<b>' + helpString[cursorPos] + '</b>';
				}
				helpString = helpString.join(',');
				this.hintWindow.html(helpString);
			}

		};

		this.goToFirstPage = function() {

		};

		this.goToLastPage = function() {

		};

		this.goToNextPage = function() {
			if (((that.currentCursor + 1) * that.numberOfResult) < that.suggestionWindow.find('#totalCount').html()) {
				that.currentCursor++;
				clearTimeout(this.suggestionTimeOut);
				that.selectedSuggestion = null;
				that.loadSuggestions();
				that.refreshHint();
			}
		};

		this.goToPreviousPage = function() {
			if (that.currentCursor > 0) {
				that.currentCursor--;
				clearTimeout(this.suggestionTimeOut);
				this.selectedSuggestion = null;
				this.loadSuggestions();
				this.refreshHint();
			}
		};

		this.manageCache = function () {
			if(this.options.wantValueCache) {
				this.value = this.oldValue;
			}
		};
		
		this.selectSuggestion = function(suggestion) {
			var $element = $(this.element);
		
			if(typeof suggestion!=="undefined" && suggestion!== null ){
				this.oldValue = suggestion.value;
				if (this.onSelectSuggestion) {
					var value = this.onSelectSuggestion.call(this.element,suggestion);
					$element.trigger(eventConstant.AJAX_SELECT_SUGGESTION_RETURN_VALUE,value);
					this.oldValue = this.value;
				}
				$element.triggerHandler(eventConstant.AJAX_SELECT_SUGGESTION,[suggestion]);
			}else{
				this.manageCache;
			}
			this.suggestionWindow.hide();
			this.selectedSuggestion = null;
		};

		this.changeSelection = function(
				newSuggestion) {
			var s_selector = undefined ;
			
			if(that.$currentListe == null){
				return;
			}
			
			if(this.n_key == 38){
				s_selector = ':last';
			}
			if(this.n_key == 40){
				s_selector = ':first';
			}
			if (that.selectedSuggestion) {
				if (newSuggestion) {
					
					$(that.selectedSuggestion).removeClass('widgetSuggestionHOVER');
					that.selectedSuggestion = newSuggestion;
				}else{
					if (s_selector!= undefined){
						$(that.suggestionWindow.find('li')).removeClass('widgetSuggestionHOVER');
						that.selectedSuggestion = that.$currentListe.find('.widgetSuggestion'+s_selector)[0];
					}
				}
			} else {
				if(s_selector!= undefined){
					that.selectedSuggestion = that.$currentListe.find('.widgetSuggestion'+s_selector)[0];
				}else{
					$(that.suggestionWindow.find('li')).removeClass('widgetSuggestionHOVER');
					that.selectedSuggestion = that.$currentListe.find('.widgetSuggestion:first')[0];
				}
			}
			$(that.selectedSuggestion).addClass('widgetSuggestionHOVER');
			that.currentPosition = $(that.$currentListe.find('.widgetSuggestion')).index(that.selectedSuggestion);
		};
		
		this.isReadOnly = function(element) {
			var $element = $(element);
			return $element.hasClass('readOnly') || $element.prop('readonly') || $element.prop('disabled');
		};
		
		this.clearCache = function () {
			this.oldValue = '';
			that.oldValue = '';			
		};

		$(this).keydown(function(evt) {
			// Utilisé pour ne pas insérer le + ou le - dans le text
			that.element = this;
			switch (evt.which) {
				case 107: // +
					evt.preventDefault();
					break;
				case 109: // -
					evt.preventDefault();
					break;
				case 9: // TAB
					that.selectSuggestion(that.selectedSuggestion);
					/*
					 * clearTimeout(this.suggestionTimeOut); that.suggestionWindow.hide();
					 */
					$(this).change();
					break;
				case 27:
					clearTimeout(that.suggestionTimeOut);
					that.suggestionWindow.hide();
					break;
				case 37: // left key
				case 39: // right key
					this.refreshHint();
					break;
				case 38: // up key
					clearTimeout(that.suggestionTimeOut);
					that.n_key = 38;
				
					if (this.currentPosition == 0
						&& this.currentCursor != 0) {
						that.currentPosition = that.numberOfResult - 1;
						that.currentCursor--;
						that.selectedSuggestion = null;
						that.o_cache.page = that.o_cache.page -1;
						that.loadSuggestions.call(that.element);
						this.refreshHint();
					} else {
						if(that.currentPosition != 0){
							that.changeSelection($(that.selectedSuggestion).prev('.widgetSuggestion')[0]);
						}
					}
					break;
				case 40: // down key
					clearTimeout(that.suggestionTimeOut);
					that.n_key = 40;
				
					if ((that.currentPosition + 1) == ($(that.suggestionWindow.find('li.widgetSuggestion')).size())) {
						if (((that.currentCursor + 1) * that.numberOfResult) < that.suggestionWindow.find('#totalCount').html()) {
							that.currentPosition = 0;
							that.currentCursor++;
							that.selectedSuggestion = null;
							that.o_cache.page = that.o_cache.page + 1;
							that.loadSuggestions.call(that.element);
							this.refreshHint();
						}
					} else {
						that.changeSelection($(that.selectedSuggestion).next('.widgetSuggestion')[0]);
					}
			
					break;
				case 13: // ENTER
					evt.preventDefault();
					evt.stopImmediatePropagation();
					that.selectSuggestion(that.selectedSuggestion);
					$(this).focusNextInputField();
					$(this).change();
					break;
			}
		});

		$(this).keyup(function(evt) {
			if(that.isReadOnly(this)) return;
			that.element = this;
			switch (evt.which) {
				case 107: // +
					this.goToNextPage();
					break;
				case 109: // -
					this.goToPreviousPage();
					break;
				// aucune recherche avec les touches suivantes
				case 13: // ENTER
				case 37: // LEFT KEY
					this.goToPreviousPage();
					break;
				case 39: // RIGHT KEY
					this.goToNextPage();
					break;
				case 38:
					clearTimeout(that.suggestionTimeOut);
					break;
				case 40:
					clearTimeout(that.suggestionTimeOut);
					if(that.selectedSuggestion == null || $(that.selectedSuggestion).is(':hidden')){
						that.currentCursor = 0;
						
						that.loadSuggestions.call(that.element);
						that.refreshHint();
					}
					break;
				case 27: // ESC
					clearTimeout(that.suggestionTimeOut);
					that.manageCache();
					$(this).change();
					that.suggestionWindow.hide();
					break;
				default:
					clearTimeout(that.suggestionTimeOut);
					that.selectedSuggestion = null;
					that.currentCursor = 0;
					
					that.suggestionTimeOut = setTimeout(
							function() {
								that.loadSuggestions.call(that.element);
							}, 1000);
					that.refreshHint();
					break;
			}
		});

		$(this).bind(eventConstant.WIDGET_FORCE_LOAD, function() {
			that.loadSuggestions.call(this);
		});

		$(document).click(function(e) {
			if (e.target != that.suggestionWindow
					&& e.target != that) {
				if(that.suggestionWindow.is(':visible')){
					that.suggestionWindow.hide();
					that.manageCache();
					$(this).change();
				}
			}
		});

		$('.mainContainerAjax').bind('stopEdition.ajax',function(){
			that.oldValue='';
		});

		this.val = function (s_value){
			that.oldValue = s_value;
			that.value = s_value;
		};

		$('html').bind('ajaxChange.ajax',function(){
			that.oldValue=that.value;
		}).bind(eventConstant.AJAX_DISABLE_ENABLED_INPUT,function(){
			var $that = $(that);
			
			globazNotation.utilsInput.changeColor(globazNotation.utilsInput.isDisabled($that), $that);
			
// var isDisabled = $that.prop('disabled');
// if(typeof isDisabled === 'boolean'){
// $that.attr('disabled',!isDisabled);
// if(isDisabled){
// $that.addClass('jadeAutocompleteAjaxDisable');
// } else {
// $that.removeClass('jadeAutocompleteAjaxDisable');
// }
// } else {
// $that.attr('disabled',true);
// $that.removeClass('jadeAutocompleteAjaxDisable');
// }
		});

		$(this).data('globazWidget',this);
		// add events to show and hide the help/hint window
		$(this).focus(function() {
			if(that.isReadOnly(this)) return;
			// position again the help window at the right place
			that.element = this;
			that.hintWindow.css( {	'left': $(this).offset().left + 'px',
									'top': ($(this).offset().top - that.hintWindow.outerHeight()) + 'px',
									'margin-left': ($(this).css('margin-left'))
			});
						
			that.hintWindow.show();
		}).blur(function() {
			that.hintWindow.hide();
		}).click(function() {
			that.refreshHint();
		});
	});
};