
var help = {

    $oldTarget:null,
    $currentTarget:null,
    $popup:null,
    $contents:null,
    t_frame:[],
    
    init: function (){
        this.$contents = $("body");//.contents();
        this.$html = this.$contents;
        this.createPopup();
        this.bindallEvent(this.$html);
    },
    
    createPopup:function(){
        var that = this;
        this.$popup = $('<div>\n\
                            <div id="tabs">\n\
                                <ul>\n\
                                    <li><a href="#tabs-1">Fr</a></li>\n\
                                    <li><a href="#tabs-2">De</a></li>\n\
                                    <li><a href="#tabs-3">It</a></li>\n\
                                </ul>\n\
                                <div id="tabs-1">\n\
                                    <label>Titre</label>\n\<input type="text" name="titre"> <br /><br />\n\
                                    <label>Ordre</label>\n\<input type="text" name="titre"> <br /><br />\n\
                                    <label>Description</label>\n\
                                    <textarea rows="2" cols="20"> frssdf</textarea>\n\
                                </div>\n\
                                <div id="tabs-2">\n\
                                     <textarea rows="2" cols="20"> detttewr</textarea>\n\
                                </div>\n\
                                <div id="tabs-3">\n\
                                     <textarea rows="2" cols="20"> itesdf</textarea>\n\
                                </div>\n\
                            </div>\n\
                        </div>');
        this.$popup.dialog({autoOpen: false,   
                               close: function(event, ui) {that.bindAlleventOnDom(that.$html);}});
        //this.$popup.popup();
        this.$popup.tabs();
        /*this.$popup.bind('dialogclose', function(){
            that.bindAlleventOnDom();
        });*/
    },

    removeBorder : function ($element) {
        $element.css("outline","0px");    
    },

    bindAlleventOnDom: function (){
       for (frame in this.t_frame){
           this.bindallEvent(this.t_frame[frame].frame);
       }
       this.bindallEvent(this.$html);
    },

    stopDetectElment: function  (){
        this.$html.die();
        for (frame in this.t_frame){
            this.t_frame[frame].frame.die();
        }
    },

    testHasId: function ($currentTarget){
        if($currentTarget.attr('id')){
            $currentTarget.css("outline","1px solid red");
        }
    },

    bindallEvent: function ($zone) {
         this.detectElementDom($zone);
         this.bindClick($zone);
    },

    isInFrame : function () {
      return typeof this.$currentTarget.parents('html').get(0).idFrame !== "undefined";
    },

    iframe: function ($currentTarget){
        var $ifram = null;
        var idTarget = $currentTarget.attr("id");
        if($currentTarget.get(0).nodeName.toUpperCase() ==="IFRAME" && !this.t_frame[idTarget]){
            $ifram = $($currentTarget.get(0).contentDocument).find("html");
            $ifram.get(0).idFrame = idTarget;
            this.bindallEvent($ifram);
            this.t_frame[$ifram.get(0).idFrame]={frame:$ifram,offset:$currentTarget.offset()};
        }
    },

    detectElementDom: function ($zone) {
        var that = this,
           currentTarget = null;
        $zone.live('mouseover',function (event){
            $currentTarget = $(event.target)
            if(event.type=="mouseover"){
                if(that.$oldTarget!==null){ 
                   that.removeBorder(that.$oldTarget);
                }
            }    
            $currentTarget.css("outline","1px solid blue");
           
            that.iframe($currentTarget);
            that.testHasId($currentTarget);
            that.$currentTarget = $currentTarget;
            that.$oldTarget = that.$currentTarget; 
        });
    },

    bindClick: function ($zone) {
        var that = this , o_decalageOffset={};
        $zone.live('click',function (event){
            if(event.type=="click"){
                that.stopDetectElment();
                that.removeBorder(that.$currentTarget); 
                event.stopImmediatePropagation();
                event.preventDefault();
                if(that.isInFrame()){
                    o_decalageOffset = that.t_frame[$zone.get(0).idFrame].offset;
                }else{
                    o_decalageOffset.left = 0;
                    o_decalageOffset.top = 0;
                }
                that.$popup.dialog("option", "position", [that.$currentTarget.offset().left+o_decalageOffset.left,that.$currentTarget.offset().top+o_decalageOffset.top]);
                that.$popup.dialog("open");
            }
        });
    }
} 



var testJson = {
    t_element: [ {id:"titreH1",titre:"test"},
    {id:"paramettreDonnesPourLeProcess",titre:"test"},{id:"creationDeLaPopulation",titre:"test"},{id:"steps",titre:"test"},{id:"tr1",titre:"test"}

    ]
}

var parsHelp = {
    $buttonDisplayHelp : {},
    $body : {},
    init: function () {
        this.$body = $('body');
        this.addMiseterMark();
        var that = this;
        this.$buttonDisplayHelp = $('<span>Affiche aide</span>').button();
          this.$buttonDisplayHelp.toggle(function () {
              that.displayHelp();
          },
           function(){ $('.helpMeAppli').hide()}
         )
        this.$body.append(this.$buttonDisplayHelp)
    },
//question mark
    addMiseterMark: function (){
       
        for (e in testJson.t_element){
   
           var  o_obj = testJson.t_element[e],
                $element = $( "#"+o_obj.id),
                n_top = $element.offset().top,
                n_left = $element.offset().left+$element.width()-15;


               var $miseterMarck = $('<span>',{
                "class":"ui-state-default ui-icon ui-icon-help helpMeAppli",
                style:"position:absolute;left:"+n_left+"px;top:"+n_top+"px;",
                id:"infos_"+o_obj.id,
                text:'test'
            }).hide();

            
            
            /*
            

            */
            
             var s_template =  '<div id="kDialog" style="display:none;margin: -20px 0px 0px 30px ">' +
                                '<div id="kDialogHead">' +
                                  '<a id="kPrev" href="#"><span>{textPrev}</span></a>' +
                                  '<span id="kCount"><span id="kCurrentStep">{currentStep}</span> {textOf} <span id="kStepCount">{stepCount}</span></span>' +
                                  '<a id="kNext" href="#"><span>{textNext}</span></a>' +
                                  '<a id="kClose" href="#">{textClose}</a>' +
                                '</div>' +
                                '<div id="kDialogBody">'+o_obj.titre+'</div>' +
                                '<div class="arrow">' +
                                  '<div class="arrow-inner"></div>' +
                                '</div>'+ 
                            '</div>';
           // var $info = $("<div id='kDialog' style='position:absolute;display:none'> <h1>"+o_obj.titre+"</h1><div>"+o_obj.id+"</div></div>");
             var $info =  $(s_template);
            this.$body.append($miseterMarck);
            
            //
            
            $miseterMarck.after($info)
                        $miseterMarck.data("$infos",$info)
            $miseterMarck.hover(function () {
                var $this = $(this);
                var $infos = $this.data("$infos");
                KingTour.Dialog.open( $this , $infos )
                $infos.css("left",$this.css("left").replace("px","")*1+5);
                $infos.css("top", $this.css("top").replace("px","")*1+5);
                $infos.show();
            },function () {  
                var $infos = $(this).data("$infos");
                $infos.hide();
            })
            //$infos.dialog()
       /*     $miseterMarck.tooltip({
			position: {
				my: "center top",
				at: "center bottom+5"
			},
			show: {
				duration: "fast"
			},
			hide: {
				effect: "hide"
			}
            })*/
        }
    },

    displayHelp: function () {
        $('.helpMeAppli').show();
    }
}


var addTooltip = {
    
}