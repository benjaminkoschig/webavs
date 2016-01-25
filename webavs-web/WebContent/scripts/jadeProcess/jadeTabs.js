/**
 * 
 */

var tabs = {
	$tab_title_input: null,
	tab_counter : 2,
	$tabs: null,
	t_tabs: {},
	t_pbar: {},
	
	init: function () {
		var $tab_content_input = $("#tab_content");
		this.$tab_title_input = $("#tab_title");
		var that = this;
		this.$tabs = $("#tabs").tabs({
			tabTemplate: "<li><a href='#{href}'>#{label}</a> <span class='ui-icon ui-icon-close'>Remove Tab</span></li>",
			/*panelTemplate:"",*/
			cache: true,
			select: function (event, ui) {
				if (ui.index === 0) {
					$("body").append(this.overlay);
				} else {
					this.overlay = $("#overlayStepid").detach();
				}
				//$(ui.tab).closest("ul").find("li").css("background-image","");
				//$(ui.tab).parent("li").css("background-image", "url('scripts/jadeProcess/fonds.png')");
				
			},
			
			load: function (event, ui) {
				var $uiPanle = $(ui.panel), idStep, value, s_image;
				notationManager.addNotationOnFragment($uiPanle);
				$pbar = $uiPanle.find(".progressStep");
				if ($pbar.length) {
					idStep = $pbar.attr("id").split("_")[1];
					value = that.t_pbar[idStep].progressbar("value");
					$pbar.progressbar("value", value);
					s_image = that.t_pbar[idStep].find(".ui-progressbar-value").css("background-image");
					if(s_image || s_image.indexOf("pbar-wait") >= 0 ){
						$pbar.find(".ui-progressbar-value").css("background-image",s_image);
					}
				}
			},
			
			show: function(event, ui) { 
				
			},
			
			add: function (event, ui) {
	
			}
		});
		this.panelTemplate = this.$tabs.tabs("option", "panelTemplate");
		this.eventCloseEvent();
	},

	addTab: function (s_title, b_iframe, s_url, id, $elementTrigger) {
		var tab, id1 = id || this.tab_counte;
		if (!this.t_tabs[id]) {
			if (b_iframe) {
				var tab_title = s_title;
				this.$tabs.tabs("option", "panelTemplate", '<div><iframe scrolling="yes" style="margin:0;padding:0,width:100%" height="' + this.$tabs.height() + '" width="100%" src="' + s_url + '"></iframe><div>');
				tab = this.$tabs.tabs("add", "#ui-tabs-" + this.tab_counter + 1, s_title);
			} else {
				if($elementTrigger){
					this.t_pbar[id] = $elementTrigger;
					var $progressStep = $elementTrigger.find(".progressStep");
					tab = this.$tabs.tabs("add", s_url, s_title);	
				}else {
					tab = this.$tabs.tabs("add", s_url, s_title);
				}
			}
			this.$tabs.tabs("option", "selected", this.tab_counter);
			this.$tabs.tabs("select", this.tab_counter);
			this.t_tabs[id] = this.tab_counter;
			this.tab_counter++;
		} else {
			
			this.$tabs.tabs("option", "selected", this.t_tabs[id]);
			this.$tabs.tabs("select", this.t_tabs[id]);
			
			/*
			this.$tabs.tabs("option", "panelTemplate", this.panelTemplate);
			this.$tabs.tabs("option", "selected", this.t_tabs[id]);
			this.$tabs.tabs("select", this.t_tabs[id]);*/
		}
	},
	
	getIdInTabsTable: function (index) {		
		for (var key in this.t_tabs) {
			if(this.t_tabs[key] === index){
				return key;
			}
		}
	},
	
	eventCloseEvent: function () {
		var that = this;
		$("#tabs span.ui-icon-close").live("click", function () {
			var index = $("li", that.$tabs).index($(this).parent());
			that.$tabs.tabs("remove", index);
			that.t_tabs[that.getIdInTabsTable(index)] = undefined;
		});
	}
};
		