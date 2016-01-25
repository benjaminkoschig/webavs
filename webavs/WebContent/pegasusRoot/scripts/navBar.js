//		s_subMenu1Template = '<li>'+
//								'<a href="{{link}}">'+
//									'{{libelle}}'+
//								'</a>'+
//								'<ul>{{subMenu}}<ul>'+
//							'</li>';

amd = {
	o_map: {},
	o_mapCallBack: {},
	addDependence: function (path, callback) {
		if(!this.o_map[path]){
			this.o_map[path] = true;
			var scriptOrStyle = null,
				t_path = path.split("."); 
			if(t_path[t_path.length-1].toLowerCase()==="js"){
				s_typeElement = 'text/javascript';
				scriptOrStyle = document.createElement("script");
				scriptOrStyle.src = path;
			} else {
				scriptOrStyle = document.createElement("link");
				s_typeElement = 'text/css';
				scriptOrStyle.rel="stylesheet"; 
				scriptOrStyle.href = path;
			}
	
			scriptOrStyle.type = s_typeElement;
			document.getElementsByTagName('head')[0].appendChild(scriptOrStyle);
			if(callback){
				if(scriptOrStyle.onload !== undefined){
					scriptOrStyle.onload = callback;
				} else {
					// IE 6 & 7
					scriptOrStyle.onreadystatechange = function(e) {
						if (/*scriptOrStyle.readyState == 'complete'*/ scriptOrStyle.readyState == 'loaded') {
							callback();
							//o_mapCallBack[]
						}
					};
				}
				
			}
		}
	}
}


navBar = {
			
	s_menuTemplat: '<li>'+
						'<a href="{{link}}">'+
							'{{libelle}}'+
						'</a>'+
					'</li>',
					
	init: function (o_jsonNavbar) {
		this.addAllDependence(o_jsonNavbar);
	},
	
	start: function (o_jsonNavbar) {
		//$("body").prepend(this.createMenu(o_jsonNavbar));
		var $areaAssure = $(".areaAssure");
		$areaAssure.append(this.createMenu(o_jsonNavbar));
		$('.dropdown-toggle').dropdown();
		$areaAssure.css({height: "auto"});
	},
	
	addAllDependence: function (o_jsonNavbar) {
		var that = this;
		amd.addDependence("/webavs/pegasusRoot/css/bootstrapMenu.css");
		amd.addDependence("/webavs/pegasusRoot/scripts/bootstrap.js", function (){		
			that.start(o_jsonNavbar);
		});
	},
	

	
	createMenu: function (o_jsonNavbar) {
		var link = null, s_menu='', s_menuSub='', menuGlobal , list =  o_jsonNavbar.list;
		for ( var i = 0; i < list.length; i++) {
			link =list[i];
			if(link){
				if(link.list.length){
					s_menuSub = this.createSubMenu(link,true);
					s_menu = s_menu+s_menuSub;
				} else {
					//s_menu = globazNotation.template.compile(o,s_subMenu1Template);
					s_menu = s_menu+globazNotation.template.compile(link,this.s_menuTemplat);
				}
				//s_menu = 
			}
		}
		
		//nav nav-pills
		menuGlobal = '<div class="subnav ">'+
						'<ul class="nav nav-pills">'+s_menu+'</ul>'+
					 '</div>';
		
		
		return menuGlobal;

	},
	
	createSubMenu: function (t_liens, b_first) {
		var s_subMenu = "", classSubMenu = "", classFirstMenu="",s_icon="" ;
		if(b_first){
			classFirstMenu = "dropdown-toggle";
			s_icon='<b class="caret"></b>';
			s_data = 'data-toggle1="dropdown1" data-target="#"';
		}
		
		for ( var i = 0; i < t_liens.list.length; i++) {
			link =t_liens.list[i];
			classSubMenu = "";
			if(link) {
				if(link.list.length){
					s_subMenu = s_subMenu + this.createSubMenu(link);
					classSubMenu="submenu";
				} else {
					s_subMenu = s_subMenu+globazNotation.template.compile(link,this.s_menuTemplat);
				}
			}
		}
		return 	'<li class="dropdown">'+
					'<a  href="'+t_liens.link+'" class="'+classFirstMenu+'"' +s_data+'>'+t_liens.libelle+
						s_icon+
					'</a>'+				
				 	'<ul class="dropdown-menu '+classSubMenu+'">'+s_subMenu+'</ul>'+
				 '</li>'; 
						
	}
	
			
}