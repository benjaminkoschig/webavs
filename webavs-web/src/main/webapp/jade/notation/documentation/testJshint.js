/*
javascript:var jsAnalyse=document.createElement('script');
jsAnalyse.setAttribute('src','http://localhost/notation/jshint.jsp_fichiers/testJshint.js');
document.body.appendChild(jsAnalyse);
(function(){
    if(window.testJsHint){
console.log(jsAnalyse)
        testJsHint.init();
    }else{
        setTimeout(arguments.callee);
    }
})();
void(jsAnalyse);*/
var JSHINT = JSHINT;
var variablesGlobales = variablesGlobales;
var testJsHint = {
    o_scriptsChecked: {},
    b_hasJquery: false,
    templates: {
        error: '<a data-line="{line}" href="javascript:void(0)">Line {line}</a>:  <code>{code}</code></p><p>{msg}<p>'
    },

    initOnline: function () {
		var that = this;
		variablesGlobales.postFetch("http://localhost/notation/jshint.jsp_fichiers/jshint.js", "script");
		
		if (typeof jQuery === "undefined" && !this.b_hasJquery || (typeof jQuery !== "undefined" && parseFloat(jQuery.fn.jquery.slice(0, 3)) < 1.7 && !this.b_hasJquery)) {
			// variablesGlobales.postFetch("http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js","script");
			variablesGlobales.postFetch("http://localhost/notation/jshint.jsp_fichiers/jquery_002.js", "script");      
			this.b_hasJquery = true;
		}
		 
		if (typeof jQuery === "undefined") {
			setTimeout(function () {
				that.initOnline();
			});
		} else {
			variablesGlobales.postFetch("http://localhost/notation/highcharts/js/highcharts.js", "script");
			if (typeof jQuery.ui === "undefined") {
				variablesGlobales.postFetch("http://localhost/notation/jshint.jsp_fichiers/jquery-ui.js", "script");
				variablesGlobales.postFetch("http://localhost/notation/jshint.jsp_fichiers/jquery-ui.css", "link");
			}
			$ = jQuery;
			var container =   $('<div id="containerStatJsAnalayse" style="width: 100%; height: 900px"></div>');
			var $body = $("body");
			$body.empty();
			$body.append(container);
			this.init({});
		}
    },

    
	init: function (options) {
		var o_options = options || {}, that = this;
		$(document).bind("ScriptRead", function (event, data) {
			if (Object.keys(data).length) {
				var o_scriptsChecked = that.checkAllScript(data);
				that.o_scriptsChecked = o_scriptsChecked;
				if(typeof o_options.callback  === "function"){
					o_options.callback (o_scriptsChecked);  
				}
				var series =  that.createDatatForStat(o_scriptsChecked);
				console.log(series);
				that.displayStatistic(series);
			}
			//that.displayAllScript(this.o_scriptsChecked);
		});
		this.getScript(o_options);
	},

    getScript: function (options) {
        var $srciptNotation = options.scripts || $('script');
        var i = 1;
        var o_scripts = {};
        var that = this;
        var notIn = options.notIn || "highcharts.js,jshint.js,jquery.i18n.properties.js,notationManager.js,jquery-ui.js,jquery.js,jquery_002.js,testJshint.js";
        var length = $srciptNotation.length;
        $srciptNotation.each(function () {
            var s_src = this.src;
            if (s_src && $.trim(s_src) && notIn.indexOf($.trim(that.getNameScritp(s_src))) === -1) {
                $.ajax({
                    url: this.src,
                    dataType: 'text',
                    contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                    success: function (data, textStatus) {
                        o_scripts[s_src] = data;
                        if (length === i) {
                            $(document).trigger("ScriptRead", o_scripts);
                        }
                        i++;
                    },
                    error: function () {
                        if (length === i) {
                            $(document).trigger("ScriptRead", o_scripts);
                        }
                        i++;
                    }
                });
            } else {
                length--;
                if (length === i) {
                    $(document).trigger("ScriptRead", o_scripts);
                }
            }
        });
        return o_scripts;
    },

    add: function (source, src, o_conteur) {
        var table = src.split("\/");
        var size = table.length;
        var o_courant = o_conteur;
	
        for (var i = 0; i < size; i++) {
            if (!o_courant[table[i]]) {
                if (i === (size - 1)) {
                    o_courant[table[i]] = source;
                } else {
                    if ($.trim(table[i])) {
                        o_courant = o_courant[table[i]] = {};
                    }
                }
            } else {
                o_courant = o_courant[table[i]];
            }
        }

        return o_conteur ;
    },
    
    getNameScritp: function (url) {
        var t_url = url.split('\/');
        return t_url[t_url.length - 1];
    },

    escapeHTML: function (text) {
        var esc = text;
        var re = [[/&/g, "&amp;"], [/</g, "&lt;"], [/>/g, "&gt;"]];

        for (var i = 0, len = re.length; i < len; i++) {
            esc = esc.replace(re[i][0], re[i][1]);
        }

        return esc;
    },

    checkAllScript: function (o_scripts) {
        var o_scriptsChecked = {};
        for (var key in o_scripts) {
            o_scriptsChecked[key] = this.callJsHint(o_scripts[key]);
        }
        return o_scriptsChecked;
    },

    replace: function (s_template, o_data) {
        var html = s_template;
        for (var key in o_data) {
            html = html.replace(new RegExp("{" + key + "}", 'g'), o_data[key]);
        }
        return html;
    },

    displayAllScript: function (o_scriptsChecked) {
        var o_conteur = {}, html="", key;	
        for (key in o_scriptsChecked) {
            html += this.creatHtmlReportForOnScript(o_scriptsChecked[key], key);
        }
    // $(".report").append(html);
    },

    creatHtmlReportForOnScript: function (report, key) {
        var html = " ";
        if(report.errors){
            var templates = '<li><p>' + this.templates.error + '</p></li>';
		
            for (var i = 0, err; err = report.errors[i]; i++) {
                if (err.raw !== "Unsafe character.") {
                    html += this.replace(templates, {
                        line: err.line,
                        code: err.evidence ? this.escapeHTML(err.evidence) : '',
                        msg: err.reason
                    });
                }
            }
            //variablesGlobales.init();
            //var t_vglobal = variablesGlobales.getVariablesGlobals()
            //variablesGlobales.display(t_vglobal);
            html = '<h1>' + this.getNameScritp(key) + '</h1>' + "<ul>"+html+"</ul>";
        }
        return html;
    },

    displayStatistic: function (series) {
        var that = this;
        chart1 = new Highcharts.Chart({
            chart: {
                renderTo: 'containerStatJsAnalayse',
                type: 'bar'
            },
            plotOptions: {
            	bar: {
            		pointPadding:0.1,
            		groupPadding:0 
            	},
                series: {
                    cursor: 'pointer',
                    point: {
                        events: {
                            click: function() {
                                //var $infos = $("<div  class='report' >"+that.creatHtmlReportForOnScript(that.o_scriptsChecked[this.id], this.id)+"</div>");
                                //$infos.hide();
                                $infos = $("<div  class='report' >"+that.o_scriptsChecked[this.id].report+"</div>");
                                $("body").append($infos);
                                $infos.dialog({
                                    autoOpen: true,
                                    width: 700,
                                    title:this.id
                                });
                             
                            }
                        }
                    }
                }
            },
            title: {
                text: 'Code js Stat'
            },
            xAxis: {
                categories: ['Nb errors']
            },
            yAxis: {
                title: {
                    text: 'Name script'
                }
            },
            series: series
        });
    },
    createDatatForStat: function (o_scriptsChecked) {
        var o_conteur = {}, s_name = "" , o_data = {}, t_dataStistics = [], report;
       
        for(var key in o_scriptsChecked){
            report = o_scriptsChecked[key];
            s_name = this.getNameScritp(key);
            o_data = {};
            o_data.name = s_name;
            o_data.data = [{
                y:(report.errors)?report.errors.length:0,
                id:key
            }];
            o_conteur = this.add(o_scriptsChecked[key], key, o_conteur);
            t_dataStistics.push(o_data);
        }
        return t_dataStistics;
    },
    callJsHint: function (s_script) {
        var options = {
            forin: true,
            noarg: true,
            noempty: true,
            eqeqeq: true,
            bitwise: true,
            undef: true,
            curly: true,
            browser: true,
            jquery: true,
            indent: 4,
            maxerr: 10000,
            strict: false, // on n'utilise pas le mode strict de js
            smarttabs: true
        };

        var o_globals = {
            globazGlobal: true,
            globazNotation: true,
            jQuery: true,
            AbstractScalableAJAXTableZone: true,
            AbstractSimpleAJAXDetailZone: true,
            notationManager: true,
            ajaxUtils: true,
            jsManager: true,
            defaultTableAjax: true,
            eventConstant: true
        };

        var result = JSHINT(s_script, options, o_globals);
        
        return  {
            data:JSHINT.data(),
            report:JSHINT.report(),
            errors:JSHINT.errors
        }// $.extend(true, {}, JSHINT);
    }

};
var variablesGlobales = {

    browser: "",

    init: function () {
		
        var browserFF8 = "JSHINT,window,document,console,constructor,location,getInterface,InstallTrigger,keys,key,_FirebugCommandLine,sessionStorage,globalStorage,localStorage,addEventListener,removeEventListener,dispatchEvent,name,parent,top,getComputedStyle,getSelection,scrollByLines,dump,closed,length,scrollbars,applicationCache,scrollX,scrollY,scrollTo,scrollBy,scrollByPages,sizeToContent,setTimeout,setInterval,clearTimeout,clearInterval,setResizable,captureEvents,releaseEvents,routeEvent,enableExternalCapture,disableExternalCapture,open,openDialog,frames,self,navigator,screen,history,content,menubar,toolbar,locationbar,personalbar,statusbar,crypto,pkcs11,controllers,opener,status,defaultStatus,innerWidth,innerHeight,outerWidth,outerHeight,screenX,screenY,mozInnerScreenX,mozInnerScreenY,pageXOffset,pageYOffset,scrollMaxX,scrollMaxY,fullScreen,alert,confirm,prompt,focus,blur,back,forward,home,stop,print,moveTo,moveBy,resizeTo,resizeBy,scroll,close,updateCommands,find,atob,btoa,frameElement,showModalDialog,postMessage,mozPaintCount,mozRequestAnimationFrame,mozAnimationStartTime,matchMedia,URL,mozIndexedDB,performance";
        var browserFF4 = "JSHINT,window,document,_createFirebugConsole,console,loadFirebugConsole,_FirebugCommandLine,_firebug,variablesGlobales,getInterface,sessionStorage,globalStorage,localStorage,getComputedStyle,dispatchEvent,removeEventListener,addEventListener,name,parent,top,getSelection,scrollByLines,dump,setInterval,scrollbars,scrollX,scrollY,scrollTo,scrollBy,scrollByPages,sizeToContent,setTimeout,clearTimeout,clearInterval,setResizable,captureEvents,releaseEvents,routeEvent,enableExternalCapture,disableExternalCapture,open,openDialog,frames,applicationCache,self,navigator,screen,history,content,menubar,toolbar,locationbar,personalbar,statusbar,closed,crypto,pkcs11,controllers,opener,status,defaultStatus,location,innerWidth,innerHeight,outerWidth,outerHeight,screenX,screenY,mozInnerScreenX,mozInnerScreenY,pageXOffset,pageYOffset,scrollMaxX,scrollMaxY,length,fullScreen,alert,confirm,prompt,focus,blur,back,forward,home,stop,print,moveTo,moveBy,resizeTo,resizeBy,scroll,close,updateCommands,find,atob,btoa,frameElement,showModalDialog,postMessage,mozPaintCount,mozRequestAnimationFrame,mozAnimationStartTime,mozIndexedDB,URL";
        var browserIE8 = "JSHINT,status,onresize,onmessage,parent,onhashchange,defaultStatus,name,history,maxConnectionsPerServer,opener,location,screenLeft,document,onbeforeprint,screenTop,clientInformation,onerror,onfocus,event,onload,onblur,window,closed,screen,onscroll,length,frameElement,self,onunload,onafterprint,navigator,frames,sessionStorage,top,clipboardData,external,onhelp,offscreenBuffering,localStorage,onbeforeunload,console,FirebugIFrame,";
        var browserIE9 = "JSHINT,console,__IE_DEVTOOLBAR_CONSOLE_COMMAND_LINE,key,keys,document,styleMedia,clientInformation,clipboardData,closed,defaultStatus,event,external,maxConnectionsPerServer,offscreenBuffering,onfocusin,onfocusout,onhelp,onmouseenter,onmouseleave,screenLeft,screenTop,status,innerHeight,innerWidth,outerHeight,outerWidth,pageXOffset,pageYOffset,screen,screenX,screenY,frameElement,frames,history,length,location,name,navigator,onabort,onafterprint,onbeforeprint,onbeforeunload,onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onhashchange,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmessage,onmousedown,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onoffline,ononline,onpause,onplay,onplaying,onprogress,onratechange,onreadystatechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onstalled,onstorage,onsubmit,onsuspend,ontimeupdate,onunload,onvolumechange,onwaiting,opener,parent,self,top,window,localStorage,performance,sessionStorage,addEventListener,dispatchEvent,removeEventListener,attachEvent,detachEvent,createPopup,execScript,item,moveBy,moveTo,msWriteProfilerMark,navigate,resizeBy,resizeTo,showHelp,showModelessDialog,toStaticHTML,scroll,scrollBy,scrollTo,getComputedStyle,alert,blur,close,confirm,focus,getSelection,open,postMessage,print,prompt,showModalDialog,toString,clearInterval,clearTimeout,setInterval,setTimeout";
        var browserChrome = "JSHINT,top,window,location,external,chrome,v8Locale,document,eventLog,logEvent,EventTracker,LocalStrings,allowedTags,allowedAttributes,parseHtmlSubset,global,IS_MAC,$,chromeSend,url,parseQueryParams,findAncestorByClass,findAncestor,swapDomNodes,disableTextSelectAndDrag,cr,DragWrapper,ntp4,appNotificationChanged,appsPrefChangeCallback,launchAppAfterEnable,CardSlider,Grabber,assert,getAppsCallback,themeChanged,recentlyClosedTabs,setMostVisitedPages,TouchHandler,templateData,i18nTemplate,variablesGlobales,jQuery,SVGPathSegLinetoVerticalRel,SVGFESpotLightElement,HTMLButtonElement,Worker,webkitIDBTransaction,webkitNotifications,EntityReference,NodeList,screenY,SVGAnimatedNumber,SVGTSpanElement,navigator,MimeTypeArray,sessionStorage,SVGPoint,SVGScriptElement,OverflowEvent,HTMLTableColElement,OfflineAudioCompletionEvent,HTMLOptionElement,HTMLInputElement,webkitIDBIndex,SVGFEPointLightElement,SVGPathSegList,SVGImageElement,HTMLLinkElement,defaultStatus,MutationEvent,HTMLMetaElement,XMLHttpRequestProgressEvent,WebKitCSSTransformValue,Clipboard,HTMLTableElement,SharedWorker,SVGAElement,SVGAnimatedRect,webkitIDBDatabaseError,HTMLSpanElement,SVGGElement,toolbar,SVGLinearGradientElement,innerHeight,webkitIndexedDB,SVGForeignObjectElement,SVGAnimateElement,applicationCache,SVGFontElement,webkitAudioContext,pageXOffset,SVGFontFaceElement,ErrorEvent,Element,SVGPathSegCurvetoQuadraticSmoothRel,opener,SVGStopElement,HTMLUnknownElement,StyleSheetList,Float64Array,WebGLShader,Uint32Array,TimeRanges,HTMLHRElement,MediaStreamEvent,WebKitPoint,screenLeft,SVGViewElement,SVGGradientElement,WebGLContextEvent,SVGPathSegMovetoRel,CanvasPattern,WebGLActiveInfo,HTMLProgressElement,HTMLDivElement,HashChangeEvent,KeyboardEvent,SVGHKernElement,HTMLTitleElement,HTMLQuoteElement,webkitIDBDatabaseException,SVGFEImageElement,DOMTokenList,screenX,WebGLProgram,SVGPathSegMovetoAbs,RangeException,SVGTextPathElement,SVGAnimatedTransformList,webkitIDBFactory,HTMLLegendElement,SVGPathSegCurvetoQuadraticAbs,MouseEvent,MediaError,AudioProcessingEvent,CompositionEvent,Uint16Array,HTMLObjectElement,HTMLFontElement,SVGFilterElement,WebKitTransitionEvent,MediaList,SVGVKernElement,SVGPaint,SVGFETileElement,Document,XPathException,innerWidth,TextMetrics,personalbar,HTMLHeadElement,SVGFEComponentTransferElement,ProgressEvent,SVGAnimatedPreserveAspectRatio,Node,SVGRectElement,CSSPageRule,SVGLineElement,CharacterData,length,FileError,MessagePort,SVGDocument,ClientRect,Option,SVGDescElement,Notation,WebGLBuffer,StorageEvent,HTMLFieldSetElement,HTMLVideoElement,locationbar,SVGRenderingIntent,SVGPathSegLinetoRel,WebGLTexture,webkitAudioPannerNode,SVGGlyphRefElement,UIEvent,HTMLTableRowElement,HTMLDListElement,File,SVGEllipseElement,SVGFEFuncRElement,Int32Array,HTMLAllCollection,CSSValue,SVGAnimatedNumberList,HTMLParamElement,SVGElementInstance,HTMLModElement,scrollY,SVGPathSegLinetoHorizontalRel,outerHeight,SVGAltGlyphDefElement,CSSFontFaceRule,SVGPathSeg,CSSStyleDeclaration,WebSocket,TouchEvent,Rect,StyleSheet,SVGPathSegLinetoHorizontalAbs,SVGColor,ArrayBuffer,SVGComponentTransferFunctionElement,SVGStyleElement,Int16Array,HTMLOutputElement,SVGNumberList,DataView,DeviceOrientationEvent,Blob,SVGFEFloodElement,clientInformation,CloseEvent,webkitStorageInfo,HTMLStyleElement,HTMLBaseElement,HTMLBRElement,FileReader,SVGFEBlendElement,HTMLHtmlElement,SVGFEConvolveMatrixElement,SVGFEGaussianBlurElement,HTMLTextAreaElement,HTMLBaseFontElement,scrollbars,webkitIDBCursor,screen,localStorage,defaultstatus,WebGLRenderbuffer,SVGTextElement,SVGFEOffsetElement,RGBColor,SVGGlyphElement,Float32Array,HTMLCanvasElement,ProcessingInstruction,SVGZoomEvent,HTMLFrameElement,SVGElementInstanceList,SVGFEDisplacementMapElement,PopStateEvent,frames,SVGPathSegCurvetoCubicSmoothRel,HTMLElement,HTMLSelectElement,Int8Array,SVGFEDistantLightElement,ImageData,SVGFEFuncBElement,HTMLIsIndexElement,HTMLDocument,SVGCircleElement,HTMLCollection,SVGSetElement,SVGFEMergeElement,HTMLDirectoryElement,CSSMediaRule,MessageEvent,SVGFESpecularLightingElement,DOMException,SVGNumber,SVGFontFaceSrcElement,CSSRule,SVGElement,WebKitCSSMatrix,status,SVGMissingGlyphElement,HTMLScriptElement,CustomEvent,DOMImplementation,SVGLength,HTMLOptGroupElement,SVGPathSegLinetoVerticalAbs,SVGTextPositioningElement,HTMLKeygenElement,styleMedia,SVGFEFuncGElement,HTMLAreaElement,HTMLFrameSetElement,SVGPathSegCurvetoQuadraticRel,name,SVGAnimateMotionElement,self,HTMLIFrameElement,Comment,XMLSerializer,Event,performance,statusbar,Range,HTMLPreElement,DOMStringList,Image,SVGAltGlyphItemElement,SVGPathSegCurvetoQuadraticSmoothAbs,SVGRect,parent,SVGFontFaceFormatElement,closed,crypto,SVGAnimateTransformElement,webkitIDBDatabase,HTMLOListElement,HTMLFormElement,SVGPathSegCurvetoCubicSmoothAbs,DOMParser,SVGPathSegClosePath,console,SVGPathSegArcRel,EventException,SVGAnimatedString,SVGTransformList,webkitIDBRequest,SVGFEMorphologyElement,SVGAnimatedLength,SVGPolygonElement,scrollX,SVGPathSegLinetoAbs,WebKitFlags,HTMLMediaElement,XMLDocument,webkitIDBObjectStore,SVGMaskElement,HTMLHeadingElement,XMLHttpRequest,TextEvent,event,HTMLMeterElement,SVGPathElement,SVGStringList,HTMLAppletElement,devicePixelRatio,FileList,webkitURL,CanvasRenderingContext2D,MessageChannel,SVGFEDropShadowElement,WebGLRenderingContext,webkitIDBKeyRange,HTMLMarqueeElement,WebKitCSSKeyframesRule,XSLTProcessor,CSSImportRule,BeforeLoadEvent,PageTransitionEvent,CSSRuleList,SVGAnimatedLengthList,SVGTransform,HTMLSourceElement,SVGTextContentElement,HTMLTableSectionElement,SVGRadialGradientElement,HTMLTableCellElement,SVGCursorElement,DocumentFragment,SVGPathSegCurvetoCubicAbs,SVGUseElement,FormData,SVGPreserveAspectRatio,HTMLMapElement,XPathResult,HTMLLIElement,SVGSwitchElement,SVGLengthList,Plugin,HTMLParagraphElement,HTMLBlockquoteElement,SVGPathSegArcAbs,SVGAnimatedBoolean,outerWidth,CSSStyleRule,SVGFontFaceUriElement,Text,HTMLUListElement,SpeechInputEvent,WebGLUniformLocation,SVGPointList,CSSPrimitiveValue,HTMLEmbedElement,PluginArray,SVGPathSegCurvetoCubicRel,ClientRectList,SVGMetadataElement,SVGTitleElement,SVGAnimatedAngle,CSSCharsetRule,menubar,SVGAnimateColorElement,SVGMatrix,HTMLBodyElement,SVGSymbolElement,HTMLAudioElement,CDATASection,SVGFEDiffuseLightingElement,SVGFETurbulenceElement,WebKitBlobBuilder,SVGAnimatedEnumeration,WebKitCSSKeyframeRule,Audio,SVGFEMergeNodeElement,history,Entity,SQLException,HTMLTableCaptionElement,DOMStringMap,MimeType,EventSource,SVGException,NamedNodeMap,WebGLFramebuffer,XMLHttpRequestUpload,WebKitAnimationEvent,Uint8Array,SVGAnimatedInteger,HTMLMenuElement,SVGDefsElement,SVGAngle,SVGSVGElement,XPathEvaluator,HTMLImageElement,NodeFilter,SVGAltGlyphElement,SVGClipPathElement,Attr,Counter,SVGPolylineElement,DOMSettableTokenList,SVGPatternElement,SVGFECompositeElement,CSSValueList,XMLHttpRequestException,SVGFEColorMatrixElement,SVGTRefElement,WheelEvent,SVGUnitTypes,HTMLLabelElement,HTMLAnchorElement,SVGFEFuncAElement,CanvasGradient,frameElement,DocumentType,Storage,SVGMPathElement,CSSStyleSheet,SVGMarkerElement,offscreenBuffering,pageYOffset,SVGFontFaceNameElement,screenTop,onseeked,onkeypress,onwebkitanimationend,onmouseup,onemptied,onseeking,onclick,onmousedown,onwebkitanimationiteration,onpopstate,onmessage,onunload,onmouseover,blur,ontimeupdate,onstalled,onvolumechange,ondragenter,ondurationchange,onwaiting,ondragstart,onstorage,ononline,onbeforeunload,onplay,onloadstart,onsuspend,ondrag,onscroll,ondblclick,ondragend,onloadedmetadata,onpagehide,onmousewheel,onreset,onpageshow,onpause,onmouseout,oninvalid,close,onsubmit,oncontextmenu,onoffline,onload,ondragleave,onkeyup,onkeydown,oncanplay,oncanplaythrough,ondrop,ondragover,onratechange,onerror,onloadeddata,onwebkitanimationstart,onabort,onselect,onplaying,ondeviceorientation,focus,onsearch,onhashchange,onended,onmousemove,onwebkittransitionend,postMessage,onchange,onprogress,oninput,onblur,onresize,onfocus,getSelection,print,stop,open,showModalDialog,alert,confirm,prompt,find,scrollBy,scrollTo,scroll,moveBy,moveTo,resizeBy,resizeTo,matchMedia,setTimeout,clearTimeout,setInterval,clearInterval,webkitRequestAnimationFrame,webkitCancelRequestAnimationFrame,atob,btoa,addEventListener,removeEventListener,captureEvents,releaseEvents,getComputedStyle,getMatchedCSSRules,webkitConvertPointFromPageToNode,webkitConvertPointFromNodeToPage,openDatabase,webkitRequestFileSystem,webkitResolveLocalFileSystemURL,dispatchEvent,TEMPORARY,PERSISTENT";
        this.browser = browserFF4;
        if (typeof jQuery === "undefined") {
    // this.postFetch("http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js","script");
    }
    },

    display: function (t_variablesGlobales) {
		
        var s_table = "<div style='position:absolute; top:20px; left:20px; background-color:white; padding:15px'><h1>Nb Variables globales:" + t_variablesGlobales.lengt + " </h1><table><thead><tr><th>Key</th><th>Value</th></tr></thead><tbody>{each}</tbody></table></div>";
        var s_html = "";
        var s_template = "<tr><td>{key}</td><td><code><pre>{value}</pre></code></td>";
        for (var i = 0; i < t_variablesGlobales.length; i++) {
            var s_ligne = s_template;
            var o_obj = t_variablesGlobales[i];
            s_html += s_ligne.replace("{key}", o_obj.key).replace("{value}", o_obj.value);
        }
		
        /*
		 * for (var keyGlobal in variablesGlobales) { var s_ligne = s_template; s_html += s_ligne.replace("{key}", keyGlobal).replace("{value}", variablesGlobales[keyGlobal]); }
		 * 
		 */

        var htmlTable = s_table.replace("{each}", s_html);
        setTimeout(function () {
            $(function () {
                $("body").append(htmlTable);
            });
        }, 100);
    },

    getVariablesGlobals: function () {
        var t_variablesGlobales = [];
        var value;
        for (var key in window) {
            if (this.browser.indexOf(key) === -1) {
                if (window[key] !== undefined && window[key] !== null) {
                    value = window[key].toString();
                } else {
                    value = undefined;
                }
                t_variablesGlobales.push({
                    key: key,
                    value: value
                });
            }
        }
        t_variablesGlobales.sort(function (a, b) {
            return (a.key < b.key) ? -1 : (a.key > b.key) ? 1 : 0;
        });
        return t_variablesGlobales;
    },

    postFetch: function (url, type) {
        var scriptOrStyle = null;
        if (type === 'script') {
            scriptOrStyle = document.createElement('script');
            scriptOrStyle.type = 'text/javascript';
            scriptOrStyle.src = url;
        } else {
            scriptOrStyle = document.createElement('link');
            scriptOrStyle.type = 'text/css';
            scriptOrStyle.rel = 'stylesheet';
            scriptOrStyle.href = url;
        }
        if (scriptOrStyle !== null) {
            document.getElementsByTagName('head')[0].appendChild(scriptOrStyle); // head MUST be present, else js error
        }
        return;
    }

};
//$(function () {
  //  testJsHint.init();
//});

// $('button[data-action=lint]').bind('click', function () {
// var opts = {};
// var checks = $('ul.inputs-list input[type=checkbox]');
//
// for (var i = 0, ch; ch = checks[i]; i++) {
// ch = $(ch);
//
// if (ch.hasClass('neg')) {
// if (!ch.attr('checked')) {
// opts[ch.attr('name')] = true;
// }
// } else {
// if (ch.attr('checked')) {
// opts[ch.attr('name')] = true;
// }
// }
// }
//
// $('div.report > div.alert-message').hide();
// $('div.editorArea div.alert-message').hide();
// $('div.report pre').html('');
// JSHINT(Editor.getValue(), opts) ? reportSuccess(JSHINT.data()) : reportFailure(JSHINT.data());
// });
