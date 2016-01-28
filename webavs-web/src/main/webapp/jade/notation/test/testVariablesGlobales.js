var variablesGlobales = {
	init: function () {
		var keys = "";
		var browserFF8 = "window,document,console,constructor,location,getInterface,InstallTrigger,keys,key,_FirebugCommandLine,sessionStorage,globalStorage,localStorage,addEventListener,removeEventListener,dispatchEvent,name,parent,top,getComputedStyle,getSelection,scrollByLines,dump,closed,length,scrollbars,applicationCache,scrollX,scrollY,scrollTo,scrollBy,scrollByPages,sizeToContent,setTimeout,setInterval,clearTimeout,clearInterval,setResizable,captureEvents,releaseEvents,routeEvent,enableExternalCapture,disableExternalCapture,open,openDialog,frames,self,navigator,screen,history,content,menubar,toolbar,locationbar,personalbar,statusbar,crypto,pkcs11,controllers,opener,status,defaultStatus,innerWidth,innerHeight,outerWidth,outerHeight,screenX,screenY,mozInnerScreenX,mozInnerScreenY,pageXOffset,pageYOffset,scrollMaxX,scrollMaxY,fullScreen,alert,confirm,prompt,focus,blur,back,forward,home,stop,print,moveTo,moveBy,resizeTo,resizeBy,scroll,close,updateCommands,find,atob,btoa,frameElement,showModalDialog,postMessage,mozPaintCount,mozRequestAnimationFrame,mozAnimationStartTime,matchMedia,URL,mozIndexedDB,performance";
		var browserFF4 = "window,document,_createFirebugConsole,console,loadFirebugConsole,_FirebugCommandLine,_firebug,variablesGlobales,getInterface,sessionStorage,globalStorage,localStorage,getComputedStyle,dispatchEvent,removeEventListener,addEventListener,name,parent,top,getSelection,scrollByLines,dump,setInterval,scrollbars,scrollX,scrollY,scrollTo,scrollBy,scrollByPages,sizeToContent,setTimeout,clearTimeout,clearInterval,setResizable,captureEvents,releaseEvents,routeEvent,enableExternalCapture,disableExternalCapture,open,openDialog,frames,applicationCache,self,navigator,screen,history,content,menubar,toolbar,locationbar,personalbar,statusbar,closed,crypto,pkcs11,controllers,opener,status,defaultStatus,location,innerWidth,innerHeight,outerWidth,outerHeight,screenX,screenY,mozInnerScreenX,mozInnerScreenY,pageXOffset,pageYOffset,scrollMaxX,scrollMaxY,length,fullScreen,alert,confirm,prompt,focus,blur,back,forward,home,stop,print,moveTo,moveBy,resizeTo,resizeBy,scroll,close,updateCommands,find,atob,btoa,frameElement,showModalDialog,postMessage,mozPaintCount,mozRequestAnimationFrame,mozAnimationStartTime,mozIndexedDB,URL";
		var browserIE8 = "status,onresize,onmessage,parent,onhashchange,defaultStatus,name,history,maxConnectionsPerServer,opener,location,screenLeft,document,onbeforeprint,screenTop,clientInformation,onerror,onfocus,event,onload,onblur,window,closed,screen,onscroll,length,frameElement,self,onunload,onafterprint,navigator,frames,sessionStorage,top,clipboardData,external,onhelp,offscreenBuffering,localStorage,onbeforeunload,console,FirebugIFrame,";
		var browserIE9 = "console,__IE_DEVTOOLBAR_CONSOLE_COMMAND_LINE,key,keys,document,styleMedia,clientInformation,clipboardData,closed,defaultStatus,event,external,maxConnectionsPerServer,offscreenBuffering,onfocusin,onfocusout,onhelp,onmouseenter,onmouseleave,screenLeft,screenTop,status,innerHeight,innerWidth,outerHeight,outerWidth,pageXOffset,pageYOffset,screen,screenX,screenY,frameElement,frames,history,length,location,name,navigator,onabort,onafterprint,onbeforeprint,onbeforeunload,onblur,oncanplay,oncanplaythrough,onchange,onclick,oncontextmenu,ondblclick,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,ondurationchange,onemptied,onended,onerror,onfocus,onhashchange,oninput,onkeydown,onkeypress,onkeyup,onload,onloadeddata,onloadedmetadata,onloadstart,onmessage,onmousedown,onmousemove,onmouseout,onmouseover,onmouseup,onmousewheel,onoffline,ononline,onpause,onplay,onplaying,onprogress,onratechange,onreadystatechange,onreset,onresize,onscroll,onseeked,onseeking,onselect,onstalled,onstorage,onsubmit,onsuspend,ontimeupdate,onunload,onvolumechange,onwaiting,opener,parent,self,top,window,localStorage,performance,sessionStorage,addEventListener,dispatchEvent,removeEventListener,attachEvent,detachEvent,createPopup,execScript,item,moveBy,moveTo,msWriteProfilerMark,navigate,resizeBy,resizeTo,showHelp,showModelessDialog,toStaticHTML,scroll,scrollBy,scrollTo,getComputedStyle,alert,blur,close,confirm,focus,getSelection,open,postMessage,print,prompt,showModalDialog,toString,clearInterval,clearTimeout,setInterval,setTimeout"
		var browserChrome = "top,window,location,external,chrome,v8Locale,document,eventLog,logEvent,EventTracker,LocalStrings,allowedTags,allowedAttributes,parseHtmlSubset,global,IS_MAC,$,chromeSend,url,parseQueryParams,findAncestorByClass,findAncestor,swapDomNodes,disableTextSelectAndDrag,cr,DragWrapper,ntp4,appNotificationChanged,appsPrefChangeCallback,launchAppAfterEnable,CardSlider,Grabber,assert,getAppsCallback,themeChanged,recentlyClosedTabs,setMostVisitedPages,TouchHandler,templateData,i18nTemplate,variablesGlobales,jQuery,SVGPathSegLinetoVerticalRel,SVGFESpotLightElement,HTMLButtonElement,Worker,webkitIDBTransaction,webkitNotifications,EntityReference,NodeList,screenY,SVGAnimatedNumber,SVGTSpanElement,navigator,MimeTypeArray,sessionStorage,SVGPoint,SVGScriptElement,OverflowEvent,HTMLTableColElement,OfflineAudioCompletionEvent,HTMLOptionElement,HTMLInputElement,webkitIDBIndex,SVGFEPointLightElement,SVGPathSegList,SVGImageElement,HTMLLinkElement,defaultStatus,MutationEvent,HTMLMetaElement,XMLHttpRequestProgressEvent,WebKitCSSTransformValue,Clipboard,HTMLTableElement,SharedWorker,SVGAElement,SVGAnimatedRect,webkitIDBDatabaseError,HTMLSpanElement,SVGGElement,toolbar,SVGLinearGradientElement,innerHeight,webkitIndexedDB,SVGForeignObjectElement,SVGAnimateElement,applicationCache,SVGFontElement,webkitAudioContext,pageXOffset,SVGFontFaceElement,ErrorEvent,Element,SVGPathSegCurvetoQuadraticSmoothRel,opener,SVGStopElement,HTMLUnknownElement,StyleSheetList,Float64Array,WebGLShader,Uint32Array,TimeRanges,HTMLHRElement,MediaStreamEvent,WebKitPoint,screenLeft,SVGViewElement,SVGGradientElement,WebGLContextEvent,SVGPathSegMovetoRel,CanvasPattern,WebGLActiveInfo,HTMLProgressElement,HTMLDivElement,HashChangeEvent,KeyboardEvent,SVGHKernElement,HTMLTitleElement,HTMLQuoteElement,webkitIDBDatabaseException,SVGFEImageElement,DOMTokenList,screenX,WebGLProgram,SVGPathSegMovetoAbs,RangeException,SVGTextPathElement,SVGAnimatedTransformList,webkitIDBFactory,HTMLLegendElement,SVGPathSegCurvetoQuadraticAbs,MouseEvent,MediaError,AudioProcessingEvent,CompositionEvent,Uint16Array,HTMLObjectElement,HTMLFontElement,SVGFilterElement,WebKitTransitionEvent,MediaList,SVGVKernElement,SVGPaint,SVGFETileElement,Document,XPathException,innerWidth,TextMetrics,personalbar,HTMLHeadElement,SVGFEComponentTransferElement,ProgressEvent,SVGAnimatedPreserveAspectRatio,Node,SVGRectElement,CSSPageRule,SVGLineElement,CharacterData,length,FileError,MessagePort,SVGDocument,ClientRect,Option,SVGDescElement,Notation,WebGLBuffer,StorageEvent,HTMLFieldSetElement,HTMLVideoElement,locationbar,SVGRenderingIntent,SVGPathSegLinetoRel,WebGLTexture,webkitAudioPannerNode,SVGGlyphRefElement,UIEvent,HTMLTableRowElement,HTMLDListElement,File,SVGEllipseElement,SVGFEFuncRElement,Int32Array,HTMLAllCollection,CSSValue,SVGAnimatedNumberList,HTMLParamElement,SVGElementInstance,HTMLModElement,scrollY,SVGPathSegLinetoHorizontalRel,outerHeight,SVGAltGlyphDefElement,CSSFontFaceRule,SVGPathSeg,CSSStyleDeclaration,WebSocket,TouchEvent,Rect,StyleSheet,SVGPathSegLinetoHorizontalAbs,SVGColor,ArrayBuffer,SVGComponentTransferFunctionElement,SVGStyleElement,Int16Array,HTMLOutputElement,SVGNumberList,DataView,DeviceOrientationEvent,Blob,SVGFEFloodElement,clientInformation,CloseEvent,webkitStorageInfo,HTMLStyleElement,HTMLBaseElement,HTMLBRElement,FileReader,SVGFEBlendElement,HTMLHtmlElement,SVGFEConvolveMatrixElement,SVGFEGaussianBlurElement,HTMLTextAreaElement,HTMLBaseFontElement,scrollbars,webkitIDBCursor,screen,localStorage,defaultstatus,WebGLRenderbuffer,SVGTextElement,SVGFEOffsetElement,RGBColor,SVGGlyphElement,Float32Array,HTMLCanvasElement,ProcessingInstruction,SVGZoomEvent,HTMLFrameElement,SVGElementInstanceList,SVGFEDisplacementMapElement,PopStateEvent,frames,SVGPathSegCurvetoCubicSmoothRel,HTMLElement,HTMLSelectElement,Int8Array,SVGFEDistantLightElement,ImageData,SVGFEFuncBElement,HTMLIsIndexElement,HTMLDocument,SVGCircleElement,HTMLCollection,SVGSetElement,SVGFEMergeElement,HTMLDirectoryElement,CSSMediaRule,MessageEvent,SVGFESpecularLightingElement,DOMException,SVGNumber,SVGFontFaceSrcElement,CSSRule,SVGElement,WebKitCSSMatrix,status,SVGMissingGlyphElement,HTMLScriptElement,CustomEvent,DOMImplementation,SVGLength,HTMLOptGroupElement,SVGPathSegLinetoVerticalAbs,SVGTextPositioningElement,HTMLKeygenElement,styleMedia,SVGFEFuncGElement,HTMLAreaElement,HTMLFrameSetElement,SVGPathSegCurvetoQuadraticRel,name,SVGAnimateMotionElement,self,HTMLIFrameElement,Comment,XMLSerializer,Event,performance,statusbar,Range,HTMLPreElement,DOMStringList,Image,SVGAltGlyphItemElement,SVGPathSegCurvetoQuadraticSmoothAbs,SVGRect,parent,SVGFontFaceFormatElement,closed,crypto,SVGAnimateTransformElement,webkitIDBDatabase,HTMLOListElement,HTMLFormElement,SVGPathSegCurvetoCubicSmoothAbs,DOMParser,SVGPathSegClosePath,console,SVGPathSegArcRel,EventException,SVGAnimatedString,SVGTransformList,webkitIDBRequest,SVGFEMorphologyElement,SVGAnimatedLength,SVGPolygonElement,scrollX,SVGPathSegLinetoAbs,WebKitFlags,HTMLMediaElement,XMLDocument,webkitIDBObjectStore,SVGMaskElement,HTMLHeadingElement,XMLHttpRequest,TextEvent,event,HTMLMeterElement,SVGPathElement,SVGStringList,HTMLAppletElement,devicePixelRatio,FileList,webkitURL,CanvasRenderingContext2D,MessageChannel,SVGFEDropShadowElement,WebGLRenderingContext,webkitIDBKeyRange,HTMLMarqueeElement,WebKitCSSKeyframesRule,XSLTProcessor,CSSImportRule,BeforeLoadEvent,PageTransitionEvent,CSSRuleList,SVGAnimatedLengthList,SVGTransform,HTMLSourceElement,SVGTextContentElement,HTMLTableSectionElement,SVGRadialGradientElement,HTMLTableCellElement,SVGCursorElement,DocumentFragment,SVGPathSegCurvetoCubicAbs,SVGUseElement,FormData,SVGPreserveAspectRatio,HTMLMapElement,XPathResult,HTMLLIElement,SVGSwitchElement,SVGLengthList,Plugin,HTMLParagraphElement,HTMLBlockquoteElement,SVGPathSegArcAbs,SVGAnimatedBoolean,outerWidth,CSSStyleRule,SVGFontFaceUriElement,Text,HTMLUListElement,SpeechInputEvent,WebGLUniformLocation,SVGPointList,CSSPrimitiveValue,HTMLEmbedElement,PluginArray,SVGPathSegCurvetoCubicRel,ClientRectList,SVGMetadataElement,SVGTitleElement,SVGAnimatedAngle,CSSCharsetRule,menubar,SVGAnimateColorElement,SVGMatrix,HTMLBodyElement,SVGSymbolElement,HTMLAudioElement,CDATASection,SVGFEDiffuseLightingElement,SVGFETurbulenceElement,WebKitBlobBuilder,SVGAnimatedEnumeration,WebKitCSSKeyframeRule,Audio,SVGFEMergeNodeElement,history,Entity,SQLException,HTMLTableCaptionElement,DOMStringMap,MimeType,EventSource,SVGException,NamedNodeMap,WebGLFramebuffer,XMLHttpRequestUpload,WebKitAnimationEvent,Uint8Array,SVGAnimatedInteger,HTMLMenuElement,SVGDefsElement,SVGAngle,SVGSVGElement,XPathEvaluator,HTMLImageElement,NodeFilter,SVGAltGlyphElement,SVGClipPathElement,Attr,Counter,SVGPolylineElement,DOMSettableTokenList,SVGPatternElement,SVGFECompositeElement,CSSValueList,XMLHttpRequestException,SVGFEColorMatrixElement,SVGTRefElement,WheelEvent,SVGUnitTypes,HTMLLabelElement,HTMLAnchorElement,SVGFEFuncAElement,CanvasGradient,frameElement,DocumentType,Storage,SVGMPathElement,CSSStyleSheet,SVGMarkerElement,offscreenBuffering,pageYOffset,SVGFontFaceNameElement,screenTop,onseeked,onkeypress,onwebkitanimationend,onmouseup,onemptied,onseeking,onclick,onmousedown,onwebkitanimationiteration,onpopstate,onmessage,onunload,onmouseover,blur,ontimeupdate,onstalled,onvolumechange,ondragenter,ondurationchange,onwaiting,ondragstart,onstorage,ononline,onbeforeunload,onplay,onloadstart,onsuspend,ondrag,onscroll,ondblclick,ondragend,onloadedmetadata,onpagehide,onmousewheel,onreset,onpageshow,onpause,onmouseout,oninvalid,close,onsubmit,oncontextmenu,onoffline,onload,ondragleave,onkeyup,onkeydown,oncanplay,oncanplaythrough,ondrop,ondragover,onratechange,onerror,onloadeddata,onwebkitanimationstart,onabort,onselect,onplaying,ondeviceorientation,focus,onsearch,onhashchange,onended,onmousemove,onwebkittransitionend,postMessage,onchange,onprogress,oninput,onblur,onresize,onfocus,getSelection,print,stop,open,showModalDialog,alert,confirm,prompt,find,scrollBy,scrollTo,scroll,moveBy,moveTo,resizeBy,resizeTo,matchMedia,setTimeout,clearTimeout,setInterval,clearInterval,webkitRequestAnimationFrame,webkitCancelRequestAnimationFrame,atob,btoa,addEventListener,removeEventListener,captureEvents,releaseEvents,getComputedStyle,getMatchedCSSRules,webkitConvertPointFromPageToNode,webkitConvertPointFromNodeToPage,openDatabase,webkitRequestFileSystem,webkitResolveLocalFileSystemURL,dispatchEvent,TEMPORARY,PERSISTENT";
		var variablesGlobales = {};
		var t_variablesGlobales = [];
		var nbVariables = 0;
		var value = undefined;
		if (typeof jQuery === "undefined") {

			// this.postFetch("http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js","script");
		}

		for (var key in window) {
			keys += key + ",";
			if (browserIE9.indexOf(key) === -1) {
				if (window[key] !== undefined && window[key] !== null) {
					value = window[key].toString();
				} else {
					value = undefined;
				}
				t_variablesGlobales.push({key: key, value: value});
				nbVariables++;
			}
		}
		
		t_variablesGlobales.sort(function (a, b) {
			return (a.key < b.key) ? -1 : (a.key > b.key) ? 1 : 0;
		});

		
		var s_table = "<div style='position:absolute; top:20px; left:20px; background-color:white; padding:15px'><h1>Nb Variables globales:" + nbVariables + " </h1><table><thead><tr><th>Key</th><th>Value</th></tr></thead><tbody>{each}</tbody></table></div>";
		var s_html = "";
		var s_template = "<tr><td>{key}</td><td><code><pre>{value}</pre></code></td>";

		for(var i=0; i < t_variablesGlobales.length; i++){
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
variablesGlobales.init();
