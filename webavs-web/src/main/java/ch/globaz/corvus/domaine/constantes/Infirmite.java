package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

/**
 * <p>
 * Codes pour les infirmités.
 * </p>
 * <p>
 * Tiré du manuel de la confédération <a href="www.bsv.admin.ch/vollzug/storage/documents/3952/3952_6_fr.pdf">Codes pour
 * la statistique des infirmités et des prestations</a> (Version du 1er janvier 2012)
 * </p>
 */
public enum Infirmite {
    // @formatter:off

	/** Cicatrices cutanées congénitales, lorsqu’une opération est nécessaire (voir aussi chiffre 112) */
	INFIRMITE_101(52810101),
	/** Ptérygion et syndactylies cutanées */
	INFIRMITE_102(52810102),
	/** Kystes dermoïdes congénitaux de l’orbite, de la racine du nez, du cou, du médiastin et de la région sacrée */
	INFIRMITE_103(52810103),
	/** Dysplasies ectodermiques */
	INFIRMITE_104(52810104),
	/**
	 * Maladies bulleuses congénitales de la peau (Epidermolyse bulleuse héréditaire, acrodermatite entéropathique et
	 * pemphigus chronique bénin familial)
	 */
	INFIRMITE_105(52810105),
	/** Maladies ichthyosiformes congénitales et kératodermies palmo-plantaires héréditaires */
	INFIRMITE_107(52810107),
	/**
	 * Naevi congénitaux, lorsqu’ils présentent une dégénérescence maligne ou lorsqu’une simple excision n’est pas
	 * possible en raison de la grandeur ou de la localisation
	 */
	INFIRMITE_109(52810109),
	/** Mastocytoses cutanées congénitales (urticaire pigmentaire et mastocytose cutanée diffuse) */
	INFIRMITE_110(52810110),
	/** Xeroderma pigmentosum */
	INFIRMITE_111(52810111),
	/** Aplasies tégumentaires congénitales, lorsqu’une opération ou un traitement hospitalier est nécessaire */
	INFIRMITE_112(52810112),
	/** Amastie congénitale et athélie congénitale */
	INFIRMITE_113(52810113),
	/** Chondrodystrophie (par exemple : achondroplasie, hypochondroplasie, dysplasie épiphysaire multiple) */
	INFIRMITE_121(52810121),
	/** Enchondromatose */
	INFIRMITE_122(52810122),
	/** Dysostoses congénitales */
	INFIRMITE_123(52810123),
	/** Exostoses cartilagineuses, lorsqu’une opération est nécessaire */
	INFIRMITE_124(52810124),
	/**
	 * Hémihypertrophies et autres asymétries corporelles congénitales, lorsqu’une opération est nécessaire Infirmités
	 * congénitales Codes pour la statistique AI
	 */
	INFIRMITE_125(52810125),
	/** Osteogenesis imperfecta */
	INFIRMITE_126(52810126),
	/** Ostéopétrose */
	INFIRMITE_127(52810127),
	/** Dysplasie fibreuse */
	INFIRMITE_128(52810128),
	/** Lacunes congénitales du crâne */
	INFIRMITE_141(52810141),
	/** Craniosynostoses */
	INFIRMITE_142(52810142),
	/** Platybasie (impression basilaire) */
	INFIRMITE_143(52810143),	

	/**
	 * Malformations vertébrales congénitales (vertèbres très fortement cunéïformes, vertèbres soudées en bloc type
	 * Klippel- Feil, vertèbres aplasiques et vertèbres très fortement dysplasiques)
	 */
	INFIRMITE_152(52810152),
	/** Côtes cervicales, lorsqu’une opération est nécessaire */
	INFIRMITE_161(52810161),
	/** Fissure congénitale du sternum */
	INFIRMITE_162(52810162),
	/** Thorax en entonnoir */
	INFIRMITE_163(52810163),
	/** Thorax en carène */
	INFIRMITE_164(52810164),
	/** Scapula alata congenita et déformation de Sprengel */
	INFIRMITE_165(52810165),
	/** Torsion congénitale du sternum, lorsqu’une opération est nécessaire */
	INFIRMITE_166(52810166),
	/** Déformations congénitales latérales de la paroi thoracique, lorsqu’une opération est nécessaire */
	INFIRMITE_167(52810167),
	/** Coxa vara congénitale, lorsqu’une opération est nécessaire */
	INFIRMITE_170(52810170),
	/** Coxa antetorta ou retrotorta congénitale, lorsqu’une opération est nécessaire */
	INFIRMITE_171(52810171),
	/** Pseudarthroses congénitales des extrémités */
	INFIRMITE_172(52810172),
	/** Amélies, dysmélies et phocomélies */
	INFIRMITE_176(52810176),
	/**
	 * Autres défauts congénitaux et malformations congénitales des extrémités, lorsqu’une opération, un appareillage ou
	 * un traitement par appareil plâtré sont nécessaires
	 */
	INFIRMITE_177(52810177),
	/**
	 * Torsion tibiale interne et externe, lorsque l’enfant a quatre ans révolus et pour autant qu’une opération soit
	 * nécessaire
	 */
	INFIRMITE_178(52810178),
	/** Pied adductus ou pied metatarsus varus congénital, lorsqu’une opération est nécessaires */
	INFIRMITE_180(52810180),
	/** Arthromyodysplasie congénitale (arthrogrypose) */
	INFIRMITE_181(52810181),
	/** Pied varus équin congénital */
	INFIRMITE_182(52810182),
	/** Luxation congénitale de la hanche et dysplasie congénitale de la hanche */
	INFIRMITE_183(52810183),
	/** Dystrophie musculaire progressive et autres myopathies congénitales */
	INFIRMITE_184(52810184),
	/** Myasthénie grave congénitale */
	INFIRMITE_185(52810185),
	/** Torticolis congénital, lorsqu’une opération est nécessaire */
	INFIRMITE_188(52810188),
	/** Myosite ossifiante progressive congénitale */
	INFIRMITE_189(52810189),
	/** Aplasie et très forte hypoplasie de muscles striés */
	INFIRMITE_190(52810190),
	/** Ténosynovite sténosante congénitale */
	INFIRMITE_191(52810191),
	/** Adynamie épisodique héréditaire */
	INFIRMITE_192(52810192),
	/** Pied plat congénital, lorsqu’une opération ou un traitement par appareil plâtre sont nécessaires */
	INFIRMITE_193(52810193),
	/**
	 * Luxation congénitale du genou, lorsqu’une opération, un appareillage ou un traitement par appareil plâtré sont
	 * nécessaires
	 */
	INFIRMITE_194(52810194),
	/** Luxation congénitale de la rotule, lorsqu’une opération est nécessaire */
	INFIRMITE_195(52810195),
	/** Cheilo-gnatho-palatoschisis (fissure labiale, maxillaire, division palatine) */
	INFIRMITE_201(52810201),
	/** Fissures faciales, médianes, obliques et transverses congénitales */
	INFIRMITE_202(52810202),
	/** Fistules congénitales du nez et des lèvres */
	INFIRMITE_203(52810203),
	/** Proboscis lateralis */
	INFIRMITE_204(52810204),
	/**
	 * Dysplasies dentaires congénitales, lorsqu’au moins 12 dents de la seconde dentition après éruption sont très
	 * fortement atteintes. En cas d’odontodysplasie (ghost teeth), il suffit qu’au moins deux dents dans un quadrant
	 * soient atteintes
	 */
	INFIRMITE_205(52810205),
	/**
	 * Anodontie congénitale totale ou anodontie congénitale partielle par absence d’au moins deux dents permanentes jux
	 * taposées ou de quatre dents permanentes par mâchoire à l’exclusion des dents de sagesse
	 */
	INFIRMITE_206(52810206),
	/**
	 * Hyperodontie congénitale, lorsque la ou les dents surnuméraires provoquent une déviation intramaxillaire ou
	 * intramandibulaire qui nécessitent un traitement au moyen d’appareils
	 */
	INFIRMITE_207(52810207),
	/**
	 * Micromandibulie congénitale, lorsqu’elle entraîne au cours de la première année de la vie des troubles de la
	 * déglutition et de la respiration nécessitant un traitement ou lorsque l’appréciation céphalométrique après
	 * l’apparition des incisives définitives montre une divergence des rapports sagittaux de la mâchoire mesurée par un
	 * angle ANB de 9 degrés et plus (respectivement par un angle ANB d’au moins 7 degrés combiné à un angle
	 * maxillo-basal d’au moins 37 degrés) ou lorsque les dents permanentes, à l’exclusion des dents de sagesse,
	 * présentent une non occlusion d’au moins trois paires de dents antagonistes dans les segments latéraux par moitié
	 * de mâchoire
	 */
	INFIRMITE_208(52810208),
	/**
	 * Mordex apertus congénital, lorsqu’il entraîne une béance verticale après éruption des incisives permanentes et
	 * que l’appréciation céphalométrique montre un angle maxillobasal de 40 degrés et plus (respectivement de 37 degrés
	 * au moins combiné à un angle ANB de 7 degrés et plus). Mordex clausus congénital, lorsqu’il entraîne une
	 * supraclusie après éruption des incisives permanentes et que l’appréciation céphalométrique montre un angle
	 * maxillo-basal de 12 degrés et moins (respectivement de 15 degrés et moins combiné à un angle ANB de 7 degrés et
	 * plus)
	 */
	INFIRMITE_209(52810209),
	/**
	 * Prognathie inférieure congénitale, lorsque l’appréciation céphalométrique après l’apparition des incisives
	 * définitives montre une divergence des rapports sagittaux de la mâchoire mesurée par un angle ANB d’au moins –1
	 * degré et qu’au moins deux paires antagonistes antérieures de la seconde dentition se trouvent en position
	 * d’occlusion croisée ou en bout à bout, ou lorsqu’il existe une divergence de +1 degré et moins combinée à un
	 * angle maxillobasal de 37 degrés et plus, ou de 15 degrés et moins
	 */
	INFIRMITE_210(52810210),
	/** Epulis du nouveau-né */
	INFIRMITE_211(52810211),
	/** Atrésie des choanes (uni- ou bilatérale) */
	INFIRMITE_212(52810212),
	/** Glossoschisis */
	INFIRMITE_213(52810213),
	/** Macroglossie et microglossie congénitales, lorsqu’une opération de la langue est nécessaire */
	INFIRMITE_214(52810214),
	/** Kystes congénitaux et tumeurs congénitales de la langue */
	INFIRMITE_215(52810215),
	/**
	 * Affections congénitales des glandes salivaires et de leurs canaux excréteurs (fistules, sténoses, kystes,
	 * tumeurs, ectasies et hypo- ou aplasies de toutes les glandes salivaires importantes).
	 */
	INFIRMITE_216(52810216),
	/**
	 * Rétention ou ankylose congénitale des dents, lorsque plusieurs molaires ou au moins deux prémolaires ou molaires
	 * de la seconde dentition placées l’une à côté de l’autre (à exclusion des dents de sagesse) sont touchées,
	 * l’absence de dents (à l’exclusion des dents de sagesse) est traitée de la même manière que la rétention ou
	 * l’ankylose.
	 */
	INFIRMITE_218(52810218),
	/** Goitre congénital */
	INFIRMITE_231(52810231),
	/**
	 * Kystes congénitaux du cou, fistules et fentes cervicales congénitales et tumeurs congénitales (cartilage de
	 * Reichert)
	 */
	INFIRMITE_232(52810232),
	/** Bronchectasies congénitales */
	INFIRMITE_241(52810241),
	/** Emphysème lobaire congénital */
	INFIRMITE_242(52810242),
	/** Agénésie partielle et hypoplasie des poumons */
	INFIRMITE_243(52810243),
	/** Kystes congénitaux et tumeurs congénitales des poumons */
	INFIRMITE_244(52810244),
	/** Séquestration pulmonaire congénitale */
	INFIRMITE_245(52810245),
	/** Syndrome des membranes hyalines */
	INFIRMITE_247(52810247),
	/** Syndrome de Mikity-Wilson */
	INFIRMITE_248(52810248),
	/**
	 * Dyskinésie primaire des cils immobiles (lorsque l’examen au microscope électronique est exécuté en dehors d’une
	 * période d’infection)
	 */
	INFIRMITE_249(52810249),
	/** Malformations congénitales du larynx et de la trachée */
	INFIRMITE_251(52810251),
	/** Tumeurs congénitales et kystes congénitaux du médiastin */
	INFIRMITE_261(52810261),
	/** Atrésie et sténose congénitales de l’oesophage et fistule oesophago-trachéale */
	INFIRMITE_271(52810271),
	/** Mégaoesophage congénital */
	INFIRMITE_272(52810272),
	/** Sténose hypertrophique du pylore */
	INFIRMITE_273(52810273),
	/** Atrésie et sténose congénitales de l’estomac, de l’intestin, du rectum ou de l’anus */
	INFIRMITE_274(52810274),
	/** Kystes, tumeurs, duplicatures et diverticules congénitaux du tube digestif */
	INFIRMITE_275(52810275),
	/** Anomalies du situs intestinal, à l’exclusion du caecum mobile */
	INFIRMITE_276(52810276),
	/** Iléus du nouveau-né */
	INFIRMITE_277(52810277),
	/** Aganglionose et anomalies des cellules ganglionnaires du gros intestin ou de l’intestin grêle */
	INFIRMITE_278(52810278),
	/** Coeliakie consécutive à l’intolérance congénitale à la gliadine */
	INFIRMITE_279(52810279),
	/** Reflux gastro-oesophagien congénital, lorsqu’une opération est nécessaire */
	INFIRMITE_280(52810280),
	/** Malformations congénitales du diaphragme */
	INFIRMITE_281(52810281),
	/**
	 * Entérocolite nécrosante des prématurés ayant à la naissance un poids inférieur à 2000 grammes ou des nouveaunés,
	 * lorsqu’elle se manifeste dans les quatre semaines après la naissance.
	 */
	INFIRMITE_282(52810282),
	/** Atrésie et hypoplasie des voies biliaires */
	INFIRMITE_291(52810291),
	/** Kyste congénital du cholédoque */
	INFIRMITE_292(52810292),
	/** Kystes congénitaux du foie */
	INFIRMITE_293(52810293),
	/** Fibrose congénitale du foie */
	INFIRMITE_294(52810294),
	/** Tumeurs congénitales du foie */
	INFIRMITE_295(52810295),
	/** Malformations congénitales et kystes congénitaux du pancréas */
	INFIRMITE_296(52810296),
	/** Omphalocèle et laparoschisis */
	INFIRMITE_302(52810302),
	/** Hernie inguinale latérale */
	INFIRMITE_303(52810303),
	/** Hémangiome caverneux ou tubéreux */
	INFIRMITE_311(52810311),
	/** Lymphangiome congénital, lymphangiectasie congénitale */
	INFIRMITE_312(52810312),
	/** Malformations congénitales du coeur et des vaisseaux */
	INFIRMITE_313(52810313),
	/** Lymphangiectasie intestinale congénitale */
	INFIRMITE_314(52810314),
	/** Anémies, leucopénies et thrombocytopénies du nouveau-né */
	INFIRMITE_321(52810321),
	/** Anémies congénitales hypoplastiques ou aplastiques, leucopénies et thrombocytopénies congénitales */
	INFIRMITE_322(52810322),
	/** Anémies hémolytiques congénitales (affections des érythrocytes, des enzymes ou de l’hémoglobine) */
	INFIRMITE_323(52810323),
	/** Coagulopathies et thrombocytopathies congénitales (hémophilies et autres anomalies des facteurs de coagulation) */
	INFIRMITE_324(52810324),
	/** Hyperbilirubinémie du nouveau-né de causes diverses, lorsqu’une exsanguino-transfusion a été nécessaire */
	INFIRMITE_325(52810325),
	/** Syndrome congénital de déficience immunitaire (IDS) */
	INFIRMITE_326(52810326),
	/** Angio-oedème héréditaire */
	INFIRMITE_327(52810327),
	/** Leucémie du nouveau-né */
	INFIRMITE_329(52810329),
	/** Histiocytoses (granulome éosinophilique, maladie de Hand-Schüller-Christian et maladie de Letterer-Siwe) */
	INFIRMITE_330(52810330),
	/**
	 * Polyglobulie congénitale, lorsqu’une soustraction thérapeutique de sang (saignée) avec remplacement par du plasma
	 * a été nécessaire
	 */
	INFIRMITE_331(52810331),
	/** Malformations congénitales et ectopies de la rate */
	INFIRMITE_333(52810333),
	/** Glomérulopathies et tubulopathies congénitales */
	INFIRMITE_341(52810341),
	/**
	 * Malformations du rein, dédoublements et altérations congénitales des reins, y compris l’hypoplasie, l’agénésie et
	 * la dystopie
	 */
	INFIRMITE_342(52810342),
	/** Tumeurs congénitales et kystes congénitaux des reins */
	INFIRMITE_343(52810343),
	/** Hydronéphrose congénitale */
	INFIRMITE_344(52810344),
	/** Malformations urétérales congénitales (sténoses, atrésies, urétérocèle, dystopies et mégaluretère) */
	INFIRMITE_345(52810345),
	/** Reflux vésico-urétéral congénital */
	INFIRMITE_346(52810346),
	/** Malformations congénitales de la vessie (par exemple : diverticule de la vessie, mégavessie congénitale) */
	INFIRMITE_348(52810348),
	/** Tumeurs congénitales de la vessie */
	INFIRMITE_349(52810349),
	/** Exstrophie de la vessie */
	INFIRMITE_350(52810350),
	/** Atrésie et sténose congénitales de l’urètre et diverticule de l’urètre */
	INFIRMITE_351(52810351),
	/** Hypospadias et épispadias */
	INFIRMITE_352(52810352),
	/** Fistule vésico-ombilicale congénitale et kyste congénital de l’ouraque */
	INFIRMITE_353(52810353),
	/** Fistules recto-uro-génitales congénitales */
	INFIRMITE_354(52810354),
	/** Cryptorchidie (unilatérale ou bilatérale), lorsqu’une opération est nécessaire */
	INFIRMITE_355(52810355),
	/** Hydrocèle testiculaire et kystes du cordon spermatique ou du ligament rond, lorsqu’une opération est nécessaire */
	INFIRMITE_356(52810356),
	/** Palmure et courbure congénitales du pénis */
	INFIRMITE_357(52810357),
	/** Atrésie congénitale de l’hymen, du vagin, du col utérin ou de l’utérus et sténose congénitale du vagin */
	INFIRMITE_358(52810358),
	/** Hermaphrodisme vrai et pseudo-hermaphrodisme */
	INFIRMITE_359(52810359),
	/**
	 * Dédoublement des organes génitaux féminins (utérus bicorne à col simple ou double, utérus unicollis et utérus
	 * double avec ou sans vagin double)
	 */
	INFIRMITE_361(52810361),
	/**
	 * Malformations du système nerveux et de ses enveloppes (encéphalocèle, kyste arachnoïdien, myéloméningocèle,
	 * hydromyélie, méningocèle, mégalencéphalie, porencéphalie et diastématomyélie)
	 */
	INFIRMITE_381(52810381),
	/** Troubles de l’hypoventilation d’origine centrale du nouveauné */
	INFIRMITE_382(52810382),
	/**
	 * Affections hérédo-dégénératices du système nerveux (p. ex. ataxie de Friedreich, leucodystrophies et affections
	 * progressives de la matière grise, atrophies musculaires d’origine spinale ou neutrale, dys-autonomie familiale,
	 * analgésie congénitale, syndrome de Rett)
	 */
	INFIRMITE_383(52810383),
	/** Médulloblastome, épendymome, gliome, papillome des plexus choroïdes et chordome */
	INFIRMITE_384(52810384),
	/**
	 * Tumeurs et malformations congénitales de l’hypophyse (comme le craniopharyngiome, le kyste de Rathke et la poche
	 * persistante de Rathke)
	 */
	INFIRMITE_385(52810385),
	/** Hydrocéphalie congénitale */
	INFIRMITE_386(52810386),
	/** Epilepsies congénitales */
	INFIRMITE_387(52810387),
	/** Paralysies cérébrales congénitales (spastiques, athétosiques et ataxiques) */
	INFIRMITE_390(52810390),
	/** Légers troubles moteurs cérébraux (traitement jusqu’à l’accomplissement de la deuxième année de la vie) */
	INFIRMITE_395(52810395),
	/** Sympathogoniome (neuroblastome sympathique), sympathicoblastome, ganglioneuroblastome et ganglioneurome */
	INFIRMITE_396(52810396),
	/** Paralysies et parésies congénitales */
	INFIRMITE_397(52810397),
	/** Oligophrénie congénitale (seulement pour le traitement du comportement éréthique ou apathique) */
	INFIRMITE_403(52810403),
	/**
	 * Troubles cérébraux congénitaux ayant pour conséquence prépondérante des symptômes psychiques et cognitifs chez
	 * les sujets d’intelligence normale, lorsqu’ils ont été diagnostiqués et traités comme tels avant l’accomplissement
	 * de la neuvième année (syndrome psycho-organique, psycho-syndrome dû à une lésion diffuse ou localisée du cerveau
	 * et syndrome psycho-orgaique congénital infantile); l’oligophrénie congénitale est classée exclusivement sous
	 * chiffre 403
	 */
	INFIRMITE_404(52810404),
	/**
	 * Troubles du spectre autistique, lorsque leurs symptômes ont été manifestes avant l’accomplissement de la
	 * cinquième année
	 */
	INFIRMITE_405(52810405),
	/**
	 * Psychoses primaires du jeune enfant, lorsque leurs symptômes ont été manifestes avant l’accomplissement de la
	 * cinquième année
	 */
	INFIRMITE_406(52810406),
	/** Malformations des paupières (colobome et ankyloblépharon) */
	INFIRMITE_411(52810411),
	/** Ptose congénitale de la paupière */
	INFIRMITE_412(52810412),
	/** Aplasie des voies lacrymales */
	INFIRMITE_413(52810413),
	/** Anophtalmie, buphtalmie et glaucome congénital */
	INFIRMITE_415(52810415),
	/**
	 * Opacités congénitales de la cornée avec acuité visuelle de 0,2 ou moins à un oeil ou 0,4 ou moins aux deux yeux
	 * (après correction du vice de réfraction)
	 */
	INFIRMITE_416(52810416),
	/** Nystagmus congénital, lorsqu’une opération est nécessaire */
	INFIRMITE_417(52810417),
	/**
	 * Anomalies congénitales de l’iris et de l’uvée avec acuité visuelle de 0,2 ou moins à un oeil ou 0,4 ou moins aux
	 * deux yeux (après correction du vice de réfraction)
	 */
	INFIRMITE_418(52810418),
	/**
	 * Opacités congénitales du cristallin ou du corps vitré et anomalies de position du cristallin avec acuité visuelle
	 * de 0,2 ou moins à un oeil ou 0,4 ou moins aux deux yeux (après correction du vice de réfraction)
	 */
	INFIRMITE_419(52810419),
	/** Rétinopathie des prématurés et pseudogliome congénital (y compris la maladie de Coats) */
	INFIRMITE_420(52810420),
	/** Rétinoblastome */
	INFIRMITE_421(52810421),
	/** Dégénérescences tapétorétiniennes congénitales */
	INFIRMITE_422(52810422),
	/**
	 * Malformations et affections congénitales du nerf optique avec acuité visuelle de 0,2 ou moins à un oeil ou 0,4 ou
	 * moins aux deux yeux (après correction du vice de réfraction)
	 */
	INFIRMITE_423(52810423),
	/** Tumeurs congénitales de la cavité orbitaire */
	INFIRMITE_424(52810424),
	/**
	 * Anomalies congénitales de réfraction avec acuité visuelle de 0,2 ou moins à un oeil ou 0,4 ou moins aux deux yeux
	 * (après correction du vice de réfraction)
	 */
	INFIRMITE_425(52810425),
	/**
	 * Strabisme et micro strabisme concomitant unilatéral, lorsqu’il existe une amblyopie de 0,2 ou moins (après
	 * correction)
	 */
	INFIRMITE_427(52810427),
	/** Parésie congénitales des muscles de l’oeil */
	INFIRMITE_428(52810428),
	/** Atrésie congénitale de l’oreille, y compris l’anotie et la microtie */
	INFIRMITE_441(52810441),
	/**
	 * Fentes congénitales dans la région de l’oreille, fistules congéniales de l’oreille moyenne et défauts congénitaux
	 * du tympan
	 */
	INFIRMITE_443(52810443),
	/**
	 * Malformations congénitales de l’oreille moyenne avec surdité partielle uni- ou bilatérale entraînant une perte
	 * auditive d’au moins 30 dB à l’audiogramme tonal dans deux domaines des fréquences de la conversation de 500, 1
	 * 000, 2 000 et 4 000 Hz
	 */
	INFIRMITE_444(52810444),
	/** Surdité congénitale des deux oreilles */
	INFIRMITE_445(52810445),
	/**
	 * Surdité congénitale neurosensorielle avec, à l’audiogramme toal, une perte de l’audition de 30 dB au moins dans
	 * le do maine des fréquences de la conversation de 500, 1 000, 2 000 et 4 000 Hz
	 */
	INFIRMITE_446(52810446),
	/** Cholestéatome congénital */
	INFIRMITE_447(52810447),
	/**
	 * Troubles congénitaux du métabolisme des hydrates de carbone (glycogénose, galactosémie, intolérance au fructose,
	 * hypoglycémie de Mac Quarrie, hypoglycémie de Zetterstroem, hypoglycémie par leucino-dépendance, hyperoxalurie
	 * primaire, anomalies congénitales du métabolisme du pyruvate, malabsorption du lactose, malabsorption du
	 * saccharose et diabète sucré, lorsque celui-ci est constaté dans les quatre premières semaines de la vie ou qu’il
	 * était sans aucun doute manifeste durant cette période
	 */
	INFIRMITE_451(52810451),
	/**
	 * Troubles congénitaux du métabolisme des acides aminés et des protéines (par exemple : phénylcétonurie, cystinose,
	 * cystinurie, oxalose, syndrome oculo-cérébro-rénal de Lowe, anomalies congénitales du cycle de l’urée et autres
	 * hyperammoniémies congénitales)
	 */
	INFIRMITE_452(52810452),
	/**
	 * Troubles congénitaux du métabolisme des graisses et des lipoprotéines (par exemple : idiotie amaurotique, maladie
	 * de Niemann-Pick, maladie de Gaucher, hypercholestérolémie héréditaire, hyperlipémie héréditaire,
	 * leucodystrophies)
	 */
	INFIRMITE_453(52810453),
	/**
	 * Troubles congénitaux du métabolisme des mucopolysaccharides et des glycoprotéines (par exemple : maladie
	 * Pfaundler-Hurler, maladie de Morquio)
	 */
	INFIRMITE_454(52810454),
	/** Troubles congénitaux de métabolisme des purines et pyrimidines (xanthinurie) */
	INFIRMITE_455(52810455),
	/** Troubles congénitaux du métabolisme des métaux (maladie de Wilson, hémochromatose et syndrome de Menkes) */
	INFIRMITE_456(52810456),
	/**
	 * Troubles congénitaux du métabolisme de la myoglobine, de l’hémoglobine et de la bilirubine (porphyrie et
	 * myoglobinurie)
	 */
	INFIRMITE_457(52810457),
	/** Troubles congénitaux de la fonction du foie (ictères héréditaires non hémolytiques) */
	INFIRMITE_458(52810458),
	/** Troubles congénitaux de la fonction du pancréas (mucoviscidose et insuffisance primaire du pancréas) */
	INFIRMITE_459(52810459),
	/**
	 * Troubles congénitaux du métabolisme des os (par exemple : hypophosphatasie, dysplasie diaphysaire progressive de
	 * Camurati-Engelmann, ostéodystrophie de Jaffé-Lichtenstein, rachitisme résistant au traitement par la vitamine D)
	 */
	INFIRMITE_461(52810461),
	/**
	 * Troubles congénitaux de la fonction hypothalamohypophysaire (petite taille d’origine hypophysaire, diabète
	 * insipide, syndrome de Prader-Willi et syndrome de Kallmann)
	 */
	INFIRMITE_462(52810462),
	/** Troubles congénitaux de la fonction de la glande thyroïde (athyroïdie, hypothyroïdie et crétinisme) */
	INFIRMITE_463(52810463),
	/** Troubles congénitaux de la fonction des glandes parathyroïdes (hypoparathyroïdisme et pseudohypoparathyroïdisme) */
	INFIRMITE_464(52810464),
	/** Troubles congénitaux de la fonction des glandes surrénales (syndrome adréno-génital et insuffisance surrénale) */
	INFIRMITE_465(52810465),
	/**
	 * Troubles congénitaux de la fonction des gonades (malformation des ovaires, anorchie, syndrome de Klinefelter et
	 * féminisation testiculaire congénitale; voir aussi chiffre 488)
	 */
	INFIRMITE_466(52810466),
	/**
	 * Défaut d’enzyme congénital du métabolisme intermédiaire lorsque ses symptômes ont été manifestes avant
	 * l’accomplissement de la cinquième année
	 */
	INFIRMITE_467(52810467),
	/** Phéochromocytome et phéochromoblastome */
	INFIRMITE_468(52810468),
	/** Neurofibromatose */
	INFIRMITE_481(52810481),
	/** Angiomatose cérébrale et rétinienne (von Hippel-Lindau) */
	INFIRMITE_482(52810482),
	/** Angiomatose encéphalo-trigéminée (Sturge-Weber-Krabbe) */
	INFIRMITE_483(52810483),
	/** Syndrome télangiectasies-ataxie (Louis Bar) */
	INFIRMITE_484(52810484),
	/**
	 * Dystrophies congénitales du tissu conjonctif (par exemple : syndrome de Marfan, syndrome d’Ehlers-Danlos, cutis
	 * laxa conge-nita, pseudoxanthome élastique)
	 */
	INFIRMITE_485(52810485),
	/**
	 * Tératomes et autres tumeurs des cellules germinales (par exemple : dysgerminome, carcinome embryonnaire, tumeur
	 * mixte des cellules germinales, tumeur vitelline, choriocarcinome, gonadoblastome)
	 */
	INFIRMITE_486(52810486),
	/** Sclérose cérébrale tubéreuse (Bourneville) */
	INFIRMITE_487(52810487),
	/** Syndrome de Turner (seulement troubles de la fonction des gonades et de la croissance) */
	INFIRMITE_488(52810488),
	/** Trisomie 21 (Down-Syndrom)*/
	INFIRMITE_489(52810489),
	/** Infection congénitales par HIV */
	INFIRMITE_490(52810490),
	/** Tumeurs du nouveau-né */
	INFIRMITE_491(52810491),
	/** Monstres doubles (par exemple : frères siamois, épignathe) */
	INFIRMITE_492(52810492),
	/**
	 * Séquelles d’embryopathies et de foetopathies (l’oligophrénie congénitale est classée sous chiffre 403); maladies
	 * infectieuses congénitales (par exemple : luès, toxoplasmose, tuberculose, listériose, cytomégalie)
	 */
	INFIRMITE_493(52810493),
	/**
	 * Nouveaux-nés ayant à la naissance un poids inférieur à 2000 grammes, jusqu’à la reprise d’un poids de 3000
	 * grammes
	 */
	INFIRMITE_494(52810494),
	/**
	 * Infections néonatales sévères, lorsqu’elles sont manifestes au cours des 72 premières heures de la vie et qu’un
	 * traitement intensif est nécessaire
	 */
	INFIRMITE_495(52810495),
	/** Pharmacodépendance néonatale, lorsqu’un traitement intensif est nécessaire */
	INFIRMITE_496(52810496),
	/**
	 * Sévères troubles respiratoires d’adaptation (par exemple : asphyxie, syndrome de détresse respiratoire, apnée),
	 * lorsqu’ils sont manifestes au cours des 72 premières heures de la vie et qu’un traitement intensif est nécessaire
	 */
	INFIRMITE_497(52810497),
	/**
	 * Troubles métaboliques néonataux sévères (hypoglycémie, hypocalcémie, hypomagnésiémie), lorsqu’ils sont manifestes
	 * au cours des 72 premières heures de la vie et qu’un traitement intensif est nécessaire
	 */
	INFIRMITE_498(52810498),
	/** Sévères lésions traumatiques dues à la naissance, lorsqu’un traitement intensif est nécessaire */
	INFIRMITE_499(52810499),
	/** Down-Syndrom */
	INFIRMITE_501(52810501),
	/** Oligophrénie (idiotie, imbécillité, débilité, voir aussi chiffre 403) */
	INFIRMITE_502(52810502),
	/** Autres infirmités congénitales en dehors de l’OIC */
	INFIRMITE_503(52810503),
	/** (maladie) Tuberculose de l’appareil respiratoire */
	INFIRMITE_601(52810601),
	/** (maladie) Autres formes de tuberculose */
	INFIRMITE_602(52810602),
	/** (maladie) Poliomyélite */
	INFIRMITE_603(52810603),
	/**
	 * (maladie) Autres affections d’origine infectieuse ou parasitaire (à l’exclusion des affections du système
	 * nerveux, voir sous XXVII, et de l’appareil respiratoire, voir sous XXX)
	 */
	INFIRMITE_604(52810604),
	/** (maladie) Tumeurs malignes */
	INFIRMITE_611(52810611),
	/**
	 * (maladie) Tumeurs des tissus lymphatiques et hématopoïétiques (lymphosarcome, reticulosarcome,
	 * lymphogranulomatose, autres lymphomes, myélome multiple, leucémie et aleucémie, mycosis fongoïde)
	 */
	INFIRMITE_612(52810612),
	/** (maladie) Autres tumeurs */
	INFIRMITE_613(52810613),
	/** (maladie) Asthme bronchique */
	INFIRMITE_621(52810621),
	/** (maladie) Autres allergies */
	INFIRMITE_622(52810622),
	/** (maladie) Diabète sucré */
	INFIRMITE_623(52810623),
	/** (maladie) Autres maladies endocriniennes */
	INFIRMITE_624(52810624),
	/** (maladie) Maladies du métabolisme et de la nutrition, avitaminoses (voir aussi sous XXXI) */
	INFIRMITE_625(52810625),
	/** (maladie) Maladie du sang et de la rate (à l’exception des infirmités congénitales et des tumeurs) */
	INFIRMITE_631(52810631),
	/** (maladie) Schizophrénie */
	INFIRMITE_641(52810641),
	/** (maladie) Accès maniaque dépressif */
	INFIRMITE_642(52810642),
	/** (maladie) Psychoses organiques et lésions cérébrales */
	INFIRMITE_643(52810643),
	/**
	 * (maladie) Autres psychoses (cas rares qui ne peuvent pas se ranger sous 641 à 643 ou 841 à 843, tels que
	 * psychoses mixtes dites psychoses schizo-affectives, schizophrénie chez l’oligophrène); dépressions involutives
	 */
	INFIRMITE_644(52810644),
	/** (maladie) Psychopathie */
	INFIRMITE_645(52810645),
	/**
	 * (maladie) Troubles réactifs du milieu ou psychogènes; névroses, borderline cases (limite entre la psychose et la
	 * névrose); anomalie psychique simple, par exemple; caractère hypocondriaque ou démentiel : troubles fonctionnels
	 * du système nerveux et troubles du langage qui en sont la conséquence, dans la mesure où ils n’ont pas été
	 * codifiés comme troubles physiques
	 */
	INFIRMITE_646(52810646),
	/** (maladie) Alcoolisme */
	INFIRMITE_647(52810647),
	/** (maladie) Autres toxicomanies */
	INFIRMITE_648(52810648),
	/**
	 * (maladie) Autres troubles du caractère, du comportement et de l’intelligence, y compris les troubles du
	 * développement du langage; oligophrénie (débilité, imbécillité, idiotie) – voir sous XXI
	 */
	INFIRMITE_649(52810649),
	/** (maladie) Hémorragies cérébrales et autres lésions vasculaires affectant le système nerveux central */
	INFIRMITE_651(52810651),
	/** (maladie) Encéphalite et méningite */
	INFIRMITE_652(52810652),
	/** (maladie) Sclérose en plaques (sclérose multiple) */
	INFIRMITE_653(52810653),
	/** (maladie) Epilepsie acquise, à l’exclusion des formes reconnues comme infirmités congénitales */
	INFIRMITE_654(52810654),
	/** (maladie) Autres affections du cerveau */
	INFIRMITE_655(52810655),
	/** (maladie) Affections de la moelle */
	INFIRMITE_656(52810656),
	/** (maladie) Autres affections du système nerveux; Poliomyélite – voir sous XXII, 603 */
	INFIRMITE_657(52810657),
	/** (maladie) Affections de l’oeil (conjonctive, paupières et orbite) */
	INFIRMITE_661(52810661),
	/** (maladie) Affections de l’oreille (oreille externe, oreille moyenne, et oreille interne) */
	INFIRMITE_671(52810671),
	/**
	 * (maladie) Affections rhumatismales fébriles (polyarthrites aiguës et suraiguës, chorée mineure) accompagnées de
	 * troubles circulatoires
	 */
	INFIRMITE_681(52810681),
	/** (maladie) Lésions organiques du coeur, y compris l’infarctus */
	INFIRMITE_682(52810682),
	/** (maladie) Troubles fonctionnels du coeur et de la circulation */
	INFIRMITE_683(52810683),
	/** (maladie) Hypertension, artériosclérose, anévrisme et autres affections des artères */
	INFIRMITE_684(52810684),
	/** (maladie) Affections des veines et des vaisseaux lymphatiques */
	INFIRMITE_685(52810685),
	/** (maladie) Infections des voies respiratoires */
	INFIRMITE_691(52810691),
	/** (maladie) Pneumoconioses (y compris la silicose) */
	INFIRMITE_692(52810692),
	/** (maladie) Autres affections de l’appareil respiratoire (à l’exclusion de la tuberculose) */
	INFIRMITE_693(52810693),
	/** (maladie) Affections du tube digestif (bouche, oesophage, estomac et intestin), y compris les hernies */
	INFIRMITE_701(52810701),
	/** (maladie) Affections du foie et des voies biliaires */
	INFIRMITE_702(52810702),
	/** (maladie) Affections du pancréas (à l’exclusion du diabète sucré) */
	INFIRMITE_703(52810703),
	/** (maladie) Affections des reins et des voies urinaires */
	INFIRMITE_711(52810711),
	/** (maladie) Affections des organes génitaux */
	INFIRMITE_712(52810712),
	/**
	 * (maladie) Altérations de la peau et du tissu cellulaire souscutané (à l’exclusion des tumeurs, voir sous XXIII,
	 * et des allergies, voir sous XXIV)
	 */
	INFIRMITE_721(52810721),
	/** (maladie) Rhumatisme articulaire primaire chronique (y compris la maladie de Bechterew) */
	INFIRMITE_731(52810731),
	/** (maladie) Coxarthrose */
	INFIRMITE_732(52810732),
	/** (maladie) Autres arthroses */
	INFIRMITE_733(52810733),
	/** (maladie) Epiphyséolyse */
	INFIRMITE_734(52810734),
	/** (maladie) Maladie de Perthes */
	INFIRMITE_735(52810735),
	/** (maladie) Spondyloses et ostéochondroses (y compris la maladie de Scheuermann) */
	INFIRMITE_736(52810736),
	/** (maladie) Scoliose idiopathique */
	INFIRMITE_737(52810737),
	/** (maladie) Autres altérations des os et des organes du mouvement (ligaments, muscles et tendons) */
	INFIRMITE_738(52810738),
	/** (accident) Tuberculose de l’appareil respiratoire */
	INFIRMITE_801(52810801),
	/** (accident) Autres formes de tuberculose */
	INFIRMITE_802(52810802),
	/** (accident) Poliomyélite */
	INFIRMITE_803(52810803),
	/**
	 * (accident) Autres affections d’origine infectieuse ou parasitaire (à l’exclusion des affections du système
	 * nerveux, voir sous XXVII, et de l’appareil respiratoire,
	 */
	INFIRMITE_804(52810804),
	/** (accident) Tumeurs malignes */
	INFIRMITE_811(52810811),
	/**
	 * (accident) Tumeurs des tissus lymphatiques et hématopoïétiques (lymphosarcome, reticulosarcome,
	 * lymphogranulomatose, autres lymphomes, myélome multiple, leucémie et aleucémie, mycosis fongoïde)
	 */
	INFIRMITE_812(52810812),
	/** (accident) Autres tumeurs */
	INFIRMITE_813(52810813),
	/** (accident) Asthme bronchique */
	INFIRMITE_821(52810821),
	/** (accident) Autres allergies */
	INFIRMITE_822(52810822),
	/** (accident) Diabète sucré */
	INFIRMITE_823(52810823),
	/** (accident) Autres maladies endocriniennes */
	INFIRMITE_824(52810824),
	/** (accident) Maladies du métabolisme et de la nutrition, avitaminoses (voir aussi sous XXXI) */
	INFIRMITE_825(52810825),
	/** (accident) Maladie du sang et de la rate (à l’exception des infirmités congénitales et des tumeurs) */
	INFIRMITE_831(52810831),
	/** (accident) Schizophrénie */
	INFIRMITE_841(52810841),
	/** (accident) Accès maniaque dépressif */
	INFIRMITE_842(52810842),
	/** (accident) Psychoses organiques et lésions cérébrales */
	INFIRMITE_843(52810843),
	/**
	 * (accident) Autres psychoses (cas rares qui ne peuvent pas se ranger sous 641 à 643 ou 841 à 843, tels que
	 * psychoses mixtes dites psychoses schizo-affectives, schizophrénie chez l’oligophrène); dépressions involutives
	 */
	INFIRMITE_844(52810844),
	/** (accident) Psychopathie */
	INFIRMITE_845(52810845),
	/**
	 * (accident) Troubles réactifs du milieu ou psychogènes; névroses, borderline cases (limite entre la psychose et la
	 * névrose); anomalie psychique simple, par exemple; caractère hypocondriaque ou démentiel : troubles fonctionnels
	 * du système nerveux et troubles du langage qui en sont la conséquence, dans la mesure où ils n’ont pas été
	 * codifiés comme troubles physiques
	 */
	INFIRMITE_846(52810846),
	/** (accident) Alcoolisme */
	INFIRMITE_847(52810847),
	/** (accident) Autres toxicomanies */
	INFIRMITE_848(52810848),
	/**
	 * (accident) Autres troubles du caractère, du comportement et de l’intelligence, y compris les troubles du
	 * développement du langage; oligophrénie (débilité, imbécillité, idiotie) – voir sous XXI
	 */
	INFIRMITE_849(52810849),
	/** (accident) Hémorragies cérébrales et autres lésions vasculaires affectant le système nerveux central */
	INFIRMITE_851(52810851),
	/** (accident) Encéphalite et méningite */
	INFIRMITE_852(52810852),
	/** (accident) Sclérose en plaques (sclérose multiple) */
	INFIRMITE_853(52810853),
	/** (accident) Epilepsie acquise, à l’exclusion des formes reconnues comme infirmités congénitales */
	INFIRMITE_854(52810854),
	/** (accident) Autres affections du cerveau */
	INFIRMITE_855(52810855),
	/** (accident) Affections de la moelle */
	INFIRMITE_856(52810856),
	/** (accident) Autres affections du système nerveux; Poliomyélite – voir sous XXII, 603 */
	INFIRMITE_857(52810857),
	/** (accident) Affections de l’oeil (conjonctive, paupières et orbite) */
	INFIRMITE_861(52810861),
	/** (accident) Affections de l’oreille (oreille externe, oreille moyenne, et oreille interne) */
	INFIRMITE_871(52810871),
	/**
	 * (accident) Affections rhumatismales fébriles (polyarthrites aiguës et suraiguës, chorée mineure) accompagnées de
	 * troubles circulatoires
	 */
	INFIRMITE_881(52810881),
	/** (accident) Lésions organiques du coeur, y compris l’infarctus */
	INFIRMITE_882(52810882),
	/** (accident) Troubles fonctionnels du coeur et de la circulation */
	INFIRMITE_883(52810883),
	/** (accident) Hypertension, artériosclérose, anévrisme et autres affections des artères */
	INFIRMITE_884(52810884),
	/** (accident) Affections des veines et des vaisseaux lymphatiques */
	INFIRMITE_885(52810885),
	/** (accident) Infections des voies respiratoires */
	INFIRMITE_891(52810891),
	/** (accident) Pneumoconioses (y compris la silicose) */
	INFIRMITE_892(52810892),
	/** (accident) Autres affections de l’appareil respiratoire (à l’exclusion de la tuberculose) */
	INFIRMITE_893(52810893),
	/** (accident) Affections du tube digestif (bouche, oesophage, estomac et intestin), y compris les hernies */
	INFIRMITE_901(52810901),
	/** (accident) Affections du foie et des voies biliaires */
	INFIRMITE_902(52810902),
	/** (accident) Affections du pancréas (à l’exclusion du diabète sucré) */
	INFIRMITE_903(52810903),
	/** (accident) Affections des reins et des voies urinaires */
	INFIRMITE_911(52810911),
	/** (accident) Affections des organes génitaux */
	INFIRMITE_912(52810912),
	/**
	 * (accident) Altérations de la peau et du tissu cellulaire souscutané (à l’exclusion des tumeurs, voir sous XXIII,
	 * et des allergies, voir sous XXIV)
	 */
	INFIRMITE_921(52810921),
	/** (accident) Rhumatisme articulaire primaire chronique (y compris la maladie de Bechterew) */
	INFIRMITE_931(52810931),
	/** (accident) Coxarthrose */
	INFIRMITE_932(52810932),
	/** (accident) Autres arthroses */
	INFIRMITE_933(52810933),
	/** (accident) Epiphyséolyse */
	INFIRMITE_934(52810934),
	/** (accident) Maladie de Perthes */
	INFIRMITE_935(52810935),
	/** (accident) Spondyloses et ostéochondroses (y compris la maladie de Scheuermann) */
	INFIRMITE_936(52810936),
	/** (accident) Scoliose idiopathique */
	INFIRMITE_937(52810937),
	/** (accident) Autres altérations des os et des organes du mouvement (ligaments, muscles et tendons) */
	INFIRMITE_938(52810938),
	// @formatter:on

    /**
     * Vieux codes systemes inactifs, mais utiles pour d'anciennes demandes lors
     * d'utilisation et d'importation de données ACOR
     * 
     * (K160202_002)
     */
    INFIRMITE_004(52810004),
    INFIRMITE_151(52810151),
    INFIRMITE_401(52810401),
    INFIRMITE_426(52810426),
    INFIRMITE_442(52810442);

    /**
     * Retourne l'énuméré correspondant au code système. </br><strong>Renvoie une {@link IllegalArgumentException} si la
     * chaîne de caractère passée en paramètre est invalide (null ou vide) ou si le code système ne correspond pas à un
     * valeur de cette énuméré.</strong>
     * 
     * @param codeSysteme la valeur du code système à rechercher
     * @throws IllegalArgumentException si le paramètre codeSystem est null, une chaîne vide ou ne correspond pas à une
     *             valeur connue
     * @return l'énuméré correspondant au code système. </br><strong>Si la valeur n'est pas trouvée une
     *         IllegalArgumentException sera lancée</strong>
     */
    public static Infirmite parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + Infirmite.class.getName() + "]");
        }
        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return Infirmite.valueOf(intCodeSysteme);
    }

    /**
     * Retourne l'énuméré correspondant au code système.
     * </br><strong>Renvoie une {@link IllegalArgumentException} si la la valeur du paramètre <code>codeSystem</code>
     * est null ou si le code système ne correspond pas à une valeur de cette énuméré.</strong>
     * 
     * @param codeSysteme la valeur du code système à rechercher
     * @throws IllegalArgumentException si le paramètre codeSystem est null, ou ne correspond pas à une
     *             valeur connue
     * @return l'énuméré correspondant au code système. </br><strong>Si la valeur n'est pas trouvée une
     *         IllegalArgumentException sera lancée</strong>
     */
    public static Infirmite valueOf(final Integer codeSysteme) {
        if (codeSysteme != null) {
            for (Infirmite uneInfirmite : Infirmite.values()) {
                if (uneInfirmite.getCodeSysteme().equals(codeSysteme)) {
                    return uneInfirmite;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + Infirmite.class.getName() + "]");
    }

    private Integer codeSysteme;

    private Infirmite(final Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * @return le code système correspondant, sous la forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}