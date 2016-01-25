var utilsTable = {
	createTable: function (t_defCells, t_values, s_caption) {
		var s_trs = "", s_table = "", s_odd = "", s_tagCatpion = "";
		for (var i = 0; i < t_values.length; i++) {
			s_odd = ((i % 2 === 0) ? "" : "class='odd'");
			s_trs += "<tr " + s_odd + ">" + this.createTds(t_defCells, t_values[i]) + "</tr>\r";
		}
		if (typeof s_caption !== "undefined") {
			s_tagCatpion = "<caption class='ui-widget-header'>" + s_caption + "</caption>";
		}
		s_table = "<div class='mainContainerAjax zoneAjaxWithoutBackgroundImage zoneAjaxWithoutBorder'><table class='areaDataTable detailEntity'>" + s_tagCatpion + "<thead></tr>" + this.createTds(t_defCells) + "</tr></thead><tbody>" + s_trs + "</tbody></table></div>";
		return $(s_table);
	},
	
	createTds: function (t_defCells, o_object) {
		var s_tds = "", s_value = "", s_dh = "d";
		for (var i = 0; i < t_defCells.length; i++) {
			if (typeof o_object === "undefined") {
				s_value = t_defCells[i].s_lable; 
				s_dh = "h class='" + t_defCells[i].s_class + "'";
			} else if (t_defCells[i].s_name) {
				s_value = o_object[t_defCells[i].s_name];
			} else {
				s_value = o_object[t_defCells[i].s_name];
			}
			s_value = (s_value === undefined) ? "" : s_value;
			s_tds += "<t" + s_dh + ">" + s_value + "</t" + s_dh + ">";
		}
		return s_tds;
	},
	
	createTableKeyValue: function (t_defCells, o_values, s_caption) {
		var s_trs = "", s_table = "", s_odd = "", s_tagCatpion = "", i = 0;
		for (var key in o_values) {
			s_odd = ((i % 2 === 0) ? "" : "class='odd'");
			
			s_trs += "<tr " + s_odd + ">" + this.createTdsKeyValue(key, o_values[key] + "") + "</tr>\r";
			i++;
		}
		if (typeof s_caption !== "undefined") {
			s_tagCatpion = "<caption class='ui-widget-header'>" + s_caption + "</caption>";
		}
		s_table = "<div class='mainContainerAjax zoneAjaxWithoutBackgroundImage zoneAjaxWithoutBorder'><table class='areaDataTable detailEntity'>" + s_tagCatpion + "<thead></tr>" + this.createTds(t_defCells) + "</tr></thead><tbody>" + s_trs + "</tbody></table></div>";
		return s_table;
	},
	
	createTdsKeyValue: function (s_key, s_value) {
		var s_tds = "";
		s_value = (s_value === undefined) ? "" : s_value;
		s_key = (s_key === undefined) ? "" : s_key;
		s_tds += "<td>" + s_key + "</td><td>" + s_value + "</td>";
		return s_tds;
	}
};
	