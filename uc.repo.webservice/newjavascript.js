/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

// This code is structured on to require a 'new' of an object of type
// CxfApacheOrgUtil.
// Alternative, it could be made 'static', but this allowed us to use this same
// object to carry some state.
var org_apache_cxf_XSI_namespace_uri = "http://www.w3.org/2001/XMLSchema-instance";
var org_apache_cxf_XSD_namespace_uri = "http://www.w3.org/2001/XMLSchema";

function cxf_apache_org_util_null_trace(message) {
}

function CxfApacheOrgUtil() {
	// Set up tracing if there is a trace object.
	if ("function" == typeof(org_apache_cxf_trace)) {
		this.trace = org_apache_cxf_trace.trace;
		this.trace("Javascript tracing enabled.");
	} else {
		this.trace = cxf_apache_org_util_null_trace;
	}
}

// define a constant for the DOM node type for an element.
CxfApacheOrgUtil.prototype.ELEMENT_NODE = 1;

// compensate for Microsoft's weakness here.
function org_apache_cxf_getNodeLocalName(node) {
	if ("localName" in node) {
		return node.localName;
	} else {
		return node.baseName;
	}
}

CxfApacheOrgUtil.prototype.getNodeLocalName = org_apache_cxf_getNodeLocalName;

// compensate for lack of namespace support in IE.
function org_apache_cxf_getNamespaceURI(elementNode, namespacePrefix) {
	var namespaceURI = null;
	if (elementNode.nodeType == 9)
		return null;
	else {
		namespaceURI = org_apache_cxf_findNamespace(elementNode,
				namespacePrefix);
		if (namespaceURI == null)
			namespaceURI = org_apache_cxf_getNamespaceURI(
					elementNode.parentNode, namespacePrefix);
		else
			return namespaceURI;
	}
	return namespaceURI;
}

// Search through the attributes of one node to find a namespace prefix definition.
function org_apache_cxf_findNamespace(elementNode, namespacePrefix) {
	var attributes = elementNode.attributes;
	if ((attributes != null) && (attributes.length > 0)) {
		for (var x = 0;x < attributes.length; x++) {
			var attributeNodeName = attributes.item(x).nodeName;
			var attributeNamespacePrefix = org_apache_cxf_getPrefix(attributes
					.item(x).nodeName);
			var attributeNamespaceSuffix = org_apache_cxf_getLocalName(attributes
					.item(x).nodeName);

			if ((namespacePrefix == null) && (attributeNamespacePrefix == null)
					&& (attributeNamespaceSuffix == "xmlns"))
				return attributes.item(x).nodeValue;
			else if ((attributeNamespacePrefix == "xmlns")
					&& (attributeNamespaceSuffix == namespacePrefix))
				return attributes.item(x).nodeValue;
		}
		return null;
	}
}

// Get namespace for a node.
function org_apache_cxf_get_node_namespaceURI(elementNode) {
	var prefix = org_apache_cxf_getPrefix(elementNode.nodeName);
	return org_apache_cxf_getNamespaceURI(elementNode, prefix);
}

CxfApacheOrgUtil.prototype.getElementNamespaceURI = org_apache_cxf_get_node_namespaceURI;

// Supprt functions for xsd:any start here.

// Object that can test an element against an 'any' specification.
function org_apache_cxf_any_ns_matcher(style, tns, nslist, nextLocalPart) {
	this.style = style;
	this.tns = tns;
	this.nslist = nslist;
	this.nextLocalPart = nextLocalPart;
}

org_apache_cxf_any_ns_matcher.ANY = "##any";
org_apache_cxf_any_ns_matcher.OTHER = "##other";
org_apache_cxf_any_ns_matcher.LOCAL = "##local";
org_apache_cxf_any_ns_matcher.LISTED = "listed";

function org_apache_cxf_any_ns_matcher_match(namespaceURI, localName) {
	switch (this.style) {
		// should this match local elements?
		case org_apache_cxf_any_ns_matcher.ANY :
			return true;
		case org_apache_cxf_any_ns_matcher.OTHER :
			return namespaceURI != this.tns;
		case org_apache_cxf_any_ns_matcher.LOCAL :
			return namespaceURI == null || namespaceURI == '';
		case org_apache_cxf_any_ns_matcher.LISTED :
			for (var x in this.nslist) {
				var ns = this.nslist[x];
				if (ns == "##local") {
					if ((namespaceURI == null || namespaceURI == '')
							&& (this.nextLocalPart != null && localName != this.nextLocalPart))
						return true;
				} else {
					if (ns == namespaceURI)
						return true;
				}
			}
			return false;
	}
}

org_apache_cxf_any_ns_matcher.prototype.match = org_apache_cxf_any_ns_matcher_match;

function org_apache_cxf_getPrefix(tagName) {
	var prefix;
	var prefixIndex = tagName.indexOf(":");
	if (prefixIndex == -1)
		return null;
	else
		return prefix = tagName.substring(0, prefixIndex);
}

function org_apache_cxf_getLocalName(tagName) {
	var suffix;
	var prefixIndex = tagName.indexOf(":");

	if (prefixIndex == -1)
		return tagName;
	else
		return suffix = tagName.substring(prefixIndex + 1, tagName.length);
}

function org_apache_cxf_element_name_for_trace(node) {
	if (node == null)
		return "Null";
	else if (node == undefined)
		return "Undefined";
	else {
		var n = '';
		if (node.namespaceURI != null && node.namespaceURI != '') {
			n = n + "{" + node.namespaceURI + "}";
		}
		return n + this.getNodeLocalName(node);
	}
}

CxfApacheOrgUtil.prototype.traceElementName = org_apache_cxf_element_name_for_trace;

function org_apache_cxf_escapeXmlEntities(val) {
	if (val == null || val == undefined)
		return "";
	else {
		val = String(val);
		return val.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g,
				"&gt;");
	}
}

CxfApacheOrgUtil.prototype.escapeXmlEntities = org_apache_cxf_escapeXmlEntities;

// Is an element xsi:nil? Note, in IE this requires the use of the prefix 'xsi', literally.
function org_apache_cxf_isElementNil(node) {
	if (node == null)
		throw "null node passed to isElementNil";
	// we need to look for an attribute xsi:nil, where xsi is
	// http://www.w3.org/2001/XMLSchema-instance. we have the usual
	// problem here with namespace-awareness.
	if ('function' == typeof node.getAttributeNS) {
		var nillness = node.getAttributeNS(
				"http://www.w3.org/2001/XMLSchema-instance", "nil");
		return nillness != null && nillness == "true";
	} else { // we assume the standard prefix and hope for the best.
		var nillness = node.getAttribute("xsi:nil");
		return nillness != null && nillness == "true";
	}
}

CxfApacheOrgUtil.prototype.isElementNil = org_apache_cxf_isElementNil;

function org_apache_cxf_getFirstElementChild(node) {
	if (node == undefined)
		throw "undefined node to getFirstElementChild";

	var n;
	for (n = node.firstChild;n != null && n.nodeType != this.ELEMENT_NODE; n = n.nextSibling) {
	}

	return n;
}

CxfApacheOrgUtil.prototype.getFirstElementChild = org_apache_cxf_getFirstElementChild;

function org_apache_cxf_getNextElementSibling(node) {
	if (node == undefined)
		throw "undefined node to getNextElementSibling";
	if (node == null)
		throw "null node to getNextElementSibling";
	var n;
	for (n = node.nextSibling;n != null && n.nodeType != this.ELEMENT_NODE; n = n.nextSibling);
	return n;
}

CxfApacheOrgUtil.prototype.getNextElementSibling = org_apache_cxf_getNextElementSibling;

function org_apache_cxf_isNodeNamedNS(node, namespaceURI, localName) {
	if (node == undefined)
		throw "undefined node to isNodeNamedNS";

	if (namespaceURI == '' || namespaceURI == null) {
		if (node.namespaceURI == '' || node.namespaceURI == null) {
			return localName == org_apache_cxf_getNodeLocalName(node);
		} else
			return false;
	} else {
		return namespaceURI == node.namespaceURI
				&& localName == org_apache_cxf_getNodeLocalName(node);
	}
}

CxfApacheOrgUtil.prototype.isNodeNamedNS = org_apache_cxf_isNodeNamedNS;

// Firefox splits large text regions into multiple Text objects (4096 chars in
// each). Glue it back together.
function org_apache_cxf_getNodeText(node) {
	var r = "";
	for (var x = 0;x < node.childNodes.length; x++) {
		r = r + node.childNodes[x].nodeValue;
	}
	return r;
}

CxfApacheOrgUtil.prototype.getNodeText = org_apache_cxf_getNodeText;

// This always uses soap-env, soap, and xsi as prefixes.
function org_apache_cxf_begin_soap11_message(namespaceAttributes) {
	var value = '<?xml version="1.0" encoding="UTF-8"?>'
			+ '<soap-env:Envelope xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"'
			+ ' xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"'
			+ ' xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"'
			+ '><soap-env:Body ' + namespaceAttributes + '>';
	return value;
}

CxfApacheOrgUtil.prototype.beginSoap11Message = org_apache_cxf_begin_soap11_message;

function org_apache_cxf_end_soap11_message() {
	return '</soap-env:Body></soap-env:Envelope>';
}

CxfApacheOrgUtil.prototype.endSoap11Message = org_apache_cxf_end_soap11_message;

var org_apache_cxf_base64_keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

function org_apache_cxf_base64_encode64array(input) {
	var output = "";
	var chr1, chr2, chr3;
	var enc1, enc2, enc3, enc4;
	var i = 0;

	do {
		var count = 1;
		chr1 = chr2 = chr3 = 0;

		chr1 = input[i++];
		if (i < input.length) {
			chr2 = input[i++];
			count++;
		}

		if (i < input.length) {
			chr3 = input[i++];
			count++;
		}

		enc1 = chr1 >> 2;
		enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
		if (count > 1) {
			enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			if (count > 2)
				enc4 = chr3 & 63;
			else
				enc4 = 64;
		} else
			enc3 = enc4 = 64;

		output = output + org_apache_cxf_base64_keyStr.charAt(enc1)
				+ org_apache_cxf_base64_keyStr.charAt(enc2)
				+ org_apache_cxf_base64_keyStr.charAt(enc3)
				+ org_apache_cxf_base64_keyStr.charAt(enc4);
	} while (i < input.length);

	return output;
}

function org_apache_cxf_base64_encode64Unicode(input) {
	var data = new Array(2 + (input.length * 2));
	data[0] = 0xff;
	data[1] = 0xfe;
	for (var x = 0;x < input.length; x++) {
		var c = input.charCodeAt(x);
		data[2 + (x * 2)] = c & 0xff;
		data[3 + (x * 2)] = (c >> 8) & 0xff;
	}
	return encode64array(data);
}

// we may be able to do this more cleanly with unescape( encodeURIComponent(
// input ) );
function org_apache_cxf_base64_encode64UTF8(input) {

	// determine how many bytes are needed for the complete conversion
	var bytesNeeded = 0;
	for (var i = 0;i < input.length; i++) {
		if (input.charCodeAt(i) < 0x80) {
			++bytesNeeded;
		} else if (input.charCodeAt(i) < 0x0800) {
			bytesNeeded += 2;
		} else if (input.charCodeAt(i) < 0x10000) {
			bytesNeeded += 3;
		} else {
			bytesNeeded += 4;
		}
	}

	// allocate a byte[] of the necessary size
	var data = new Array(bytesNeeded);
	// do the conversion from character code points to utf-8
	var bytes = 0;
	for (var i = 0;i < input.length; i++) {
		if (input.charCodeAt(i) < 0x80) {
			data[bytes++] = input.charCodeAt(i);
		} else if (input.charCodeAt(i) < 0x0800) {
			data[bytes++] = ((input.charCodeAt(i) >> 6) | 0xC0);
			data[bytes++] = ((input.charCodeAt(i) & 0x3F) | 0x80);
		} else if (input.charCodeAt(i) < 0x10000) {
			data[bytes++] = ((input.charCodeAt(i) >> 12) | 0xE0);
			data[bytes++] = (((input.charCodeAt(i) >> 6) & 0x3F) | 0x80);
			data[bytes++] = ((input.charCodeAt(i) & 0x3F) | 0x80);
		} else {
			data[bytes++] = ((input.charCodeAt(i) >> 18) | 0xF0);
			data[bytes++] = (((input.charCodeAt(i) >> 12) & 0x3F) | 0x80);
			data[bytes++] = (((input.charCodeAt(i) >> 6) & 0x3F) | 0x80);
			data[bytes++] = ((input.charCodeAt(i) & 0x3F) | 0x80);
		}
	}
	return encode64array(data);
}

function org_apache_cxf_base64_decode64array(input) {
	var output = new Array();
	var chr1, chr2, chr3;
	var enc1, enc2, enc3, enc4;
	var i = 0;

	// remove all characters that are not A-Z, a-z, 0-9, +, /, or =
	input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

	do {
		enc1 = org_apache_cxf_base64_keyStr.indexOf(input.charAt(i++));
		enc2 = org_apache_cxf_base64_keyStr.indexOf(input.charAt(i++));
		enc3 = org_apache_cxf_base64_keyStr.indexOf(input.charAt(i++));
		enc4 = org_apache_cxf_base64_keyStr.indexOf(input.charAt(i++));

		chr1 = (enc1 << 2) | (enc2 >> 4);
		chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
		chr3 = ((enc3 & 3) << 6) | enc4;

		output[output.length] = chr1;

		if (enc3 != 64) {
			output[output.length] = chr2;
		}
		if (enc4 != 64) {
			output[output.length] = chr3;
		}
	} while (i < input.length);

	return output;
}

var org_apache_cxf_base64_hD = "0123456789ABCDEF";
function org_apache_cxf_base64_d2h(d) {
	var h = org_apache_cxf_base64_hD.substr(d & 15, 1);
	while (d > 15) {
		d >>= 4;
		h = org_apache_cxf_base64_hD.substr(d & 15, 1) + h;
	}
	return h;
}

function org_apache_cxf_base64_decode64Unicode(input) {
	var bytes = org_apache_cxf_base64_decode64array(input);
	var swap;
	var output = "";
	if (bytes[0] == 0xff && bytes[1] == 0xfe) {
		swap = true;
	} else if (bytes[0] == 0xfe && bytes[1] == 0xff) {
		swap = false;
	} else {
		confirm("Problem with decoding utf-16");
	}
	for (var x = 2;x < bytes.length; x = x + 2) {
		var c;
		if (swap)
			c = (bytes[x + 1] << 8) | bytes[x];
		else
			c = (bytes[x] << 8) | bytes[x + 1];

		output = output + String.fromCharCode(c);
	}
	return output;
}

// we may be able to do this more cleanly with decodeURIComponent( escape( input
// ) );
function org_apache_cxf_base64_decode64UTF8(input) {
	var utftext = org_apache_cxf_base64_decode64array(input);
	var plaintext = "";
	var cRay = new Array();
	var i = 0;
	var c;
	var c2;
	var c3;
	while (i < utftext.length) {
		c = utftext[i];
		if (c < 128) {
			plaintext += String.fromCharCode(c);
			i++;
		} else if ((c > 191) && (c < 224)) {
			c2 = utftext[i + 1];
			plaintext += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
			i += 2;
		} else {
			c2 = utftext[i + 1];
			c3 = utftext[i + 2];
			plaintext += String.fromCharCode(((c & 15) << 12)
					| ((c2 & 63) << 6) | (c3 & 63));
			i += 3;
		}
	}
	return plaintext;
}

// MTOM deserialization.
// This assumes that the only content type it will be asked to deal with is text/plain;charset=utf-8.
// This only handles cid: xop URNs.

var org_apache_cxf_XOP_NS = 'http://www.w3.org/2004/08/xop/include';

function org_apache_cxf_deserialize_MTOM_or_base64(element) {
	var elementChild = this.getFirstElementChild(element);
	if (elementChild == null) { // no MTOM, assume base64
		var base64Text = this.getNodeText(element);
		// we assume this is text/plain;charset=utf-8. We could check for the
		// xmime attribute.
		return org_apache_cxf_base64_decode64UTF8(base64Text);
	}
	// 
	if (!org_apache_cxf_isNodeNamedNS(elementChild, org_apache_cxf_XOP_NS, 'Include')) {
		this.trace('Invalid child element of base64 element');
		return ''; // we don't knoww what this is, so we throw it out. We could
					// throw.
	}
	var href = elementChild.getAttribute('href');
	if(!href) {
		this.trace('missing href for xop:Include');
		return ''; // we don't knoww what this is, so we throw it out. We could
					// throw.
	}
	// we only support cid:, not URLs.
	if(href.length < 4 || href.substr(0, 4) != 'cid:') {
		this.trace('Non-cid href in xop:Include: ' + href);
		return ''; 
	}
	var cid = href.substr(4);
	var partobject = this.client.parts[cid];
	if(!partobject) {
		this.trace('xop:Include href points to missing attachment: ' + href);
		return ''; 
	}
	// success.
	return partobject.data;
}

CxfApacheOrgUtil.prototype.deserializeBase64orMom = org_apache_cxf_deserialize_MTOM_or_base64;

/*
 * Client object sends requests and calls back with responses.
 */
function CxfApacheOrgClient(utils) {
	utils.trace("Client constructor");
	this.utils = utils;
	utils.client = this; // we aren't worried about multithreading!
	this.mtomparts = [];
	this.soapAction = "";
	this.messageType = "CALL";
	// handler functions
	this.onsuccess = null;
	this.onerror = null;
	// Firefox is noncompliant with respect to the defined constants,
	// so we define our own.
	this.READY_STATE_UNINITIALIZED = 0;
	this.READY_STATE_LOADING = 1;
	this.READY_STATE_LOADED = 2;
	this.READY_STATE_INTERACTIVE = 3;
	this.READY_STATE_DONE = 4;
}

var org_apache_cxf_pad_string_PAD_LEFT = 0;
var org_apache_cxf_pad_string_PAD_RIGHT = 1;
var org_apache_cxf_pad_string_PAD_BOTH = 2;

function org_apache_cxf_pad_string(string, len, pad, type) {
	var append = new String();

	len = isNaN(len) ? 0 : len - string.length;
	pad = typeof(pad) == 'string' ? pad : ' ';

	if (type == org_apache_cxf_pad_string_PAD_BOTH) {
		string = org_apache_cxf_pad_sring(Math.floor(len / 2) + string.length,
				pad, org_apache_cxf_pad_string_PAD_LEFT);
		return (org_apache_cxf_pad_string(Math.ceil(len / 2) + string.length,
				pad, org_apache_cxf_pad_string_PAD_RIGHT));
	}

	while ((len -= pad.length) > 0)
		append += pad;
	append += pad.substr(0, len + pad.length);

	return (type == org_apache_cxf_pad_string_PAD_LEFT
			? append.concat(string)
			: string.concat(append));
}

/*
 * Generate a uniformly distributed random integer within the range <min> ..
 * <max>. (min) - Lower limit: random >= min (default: 0) (max) - Upper limit:
 * random <= max (default: 1)
 */
function org_apache_cxf_random_int(min, max) {
	if (!isFinite(min))
		min = 0;
	if (!isFinite(max))
		max = 1;
	return Math.floor((Math.random() % 1) * (max - min + 1) + min);
}

function org_apache_cxf_random_hex_string(len) {
	var random = org_apache_cxf_random_int(0, Math.pow(16, len) - 1);
	return org_apache_cxf_pad_string(random.toString(16), len, '0',
			org_apache_cxf_pad_string_PAD_LEFT);
}

function org_apache_cxf_make_uuid(type) {
	switch ((type || 'v4').toUpperCase()) {
		// Version 4 UUID (Section 4.4 of RFC 4122)
		case 'V4' :
			var tl = org_apache_cxf_random_hex_string(8);
			// time_low
			var tm = org_apache_cxf_random_hex_string(4);
			// time_mid
			var thav = '4' + org_apache_cxf_random_hex_string(3);
			// time_hi_and_version
			var cshar = org_apache_cxf_random_int(0, 0xFF);
			// clock_seq_hi_and_reserved
			cshar = ((cshar & ~(1 << 6)) | (1 << 7)).toString(16);
			var csl = org_apache_cxf_random_hex_string(2);
			// clock_seq_low
			var n = org_apache_cxf_random_hex_string(12);
			// node

			return (tl + '-' + tm + '-' + thav + '-' + cshar + csl + '-' + n);

			// Nil UUID (Section 4.1.7 of RFC 4122)
		case 'NIL' :
			return '00000000-0000-0000-0000-000000000000';
	}
	return null;
}

//
// Returns XMLHttpRequest object.
//
var ORG_APACHE_CXF_XMLHTTPREQUEST_MS_PROGIDS = new Array(
    "Msxml2.XMLHTTP.7.0",
    "Msxml2.XMLHTTP.6.0",
    "Msxml2.XMLHTTP.5.0",
    "Msxml2.XMLHTTP.4.0",
    "MSXML2.XMLHTTP.3.0",
    "MSXML2.XMLHTTP",
    "Microsoft.XMLHTTP"
    );    

function org_apache_cxf_getXMLHttpRequest()
{
    var httpRequest = null;
 
    // Create the appropriate HttpRequest object for the browser.
    try {
        httpRequest = new XMLHttpRequest();
        return httpRequest;
    } catch(ex) {
    }
    
    if (window.ActiveXObject != null) {
        // Must be IE, find the right ActiveXObject.
   
        var success = false;
        //
        // Define a list of Microsoft XML HTTP ProgIDs.
        //
        for (var i = 0;
             i < ORG_APACHE_CXF_XMLHTTPREQUEST_MS_PROGIDS.length && !success;
             i++)
        {
            try
            {
                httpRequest = new ActiveXObject(ORG_APACHE_CXF_XMLHTTPREQUEST_MS_PROGIDS[i]);
                success = true;
            }
            catch (ex)
            {
                // no reason to log unless we come up empty.
            }
        }
        if(!success) {
            this.utils.trace("Unable to get any Microsoft XML HttpRequest object.");
            throw "org_apache_cxf no Microsoft XMLHttpRequest";
        }
    }
    // Return it.
    return httpRequest;
}

CxfApacheOrgClient.prototype.getXMLHttpRequest = org_apache_cxf_getXMLHttpRequest;

var ORG_APACHE_CXF_MTOM_REQUEST_HEADER = 'Content-Type: application/xop+xml; type="text/xml"; charset=utf-8\r\n';

// Caller must avoid stupid mistakes like 'GET' with a request body.
// This does not support attempts to cross-script.
// This imposes a relatively straightforward set of HTTP options.
function org_apache_cxf_client_request(url, requestXML, method, sync, headers) 
{
	this.utils.trace("request " + url);

	this.url = url;
	this.sync = sync;

	this.req = null;

	if (method) {
		this.method = method;
	} else {
		if (requestXML)
			this.method = "POST";
		else
			this.method = "GET";
	}

	try {
		this.req = this.getXMLHttpRequest();
	} catch (err) {
		this.utils.trace("Error creating XMLHttpRequest: " + err);
		this.req = null;
	}

	if (this.req == null) {
		this.utils.trace("Unable to create request object.");
		throw "ORG_APACHE_CXF_NO_REQUEST_OBJECT";
	}

	this.utils.trace("about to open " + this.method + " " + this.url);
	this.req.open(this.method, this.url, !this.sync);

	var mimeBoundary;

	// we can't do binary MTOM, but we can do 'text/plain' !
	if (this.mtomparts.length > 0) {
		var uuid = org_apache_cxf_make_uuid('v4');
		mimeBoundary = '@_bOuNDaRy_' + uuid;
		var ctHeader = 'multipart/related; start-info="text/xml"; type="application/xop+xml"; boundary="'
				+ mimeBoundary + '"';
		this.req.setRequestHeader("Content-Type", ctHeader);

	} else {
	// for now, assume SOAP 1.1. 1.2 calls for application/xml.
	// also assume we're talking Unicode here.
		this.req.setRequestHeader("Content-Type", "text/xml;charset=utf-8");
	}

    if (headers) { // must be array indexed by header field.
        // avoid extra properties on the headers.
        for (var h in headers) {
            if(headers.hasOwnProperty(h)) {
                this.req.setRequestHeader(h, headers[h]);
            }
        }
    }	

	this.req.setRequestHeader("SOAPAction", this.soapAction);
	this.req.setRequestHeader("MessageType", this.messageType);

	var requester = this; /* setup a closure */

	this.req.onreadystatechange = function() {
		requester.onReadyState();
	}

	// NOTE: we do not call the onerror callback for a synchronous error
	// at request time. We let the request object throw as it will.
	// onError will only be called for asynchronous errors.
	this.utils.trace("about to send data" + this.method + " " + this.url);
	var dataToSend;
	if (this.mtomparts.length == 0) {
		dataToSend = requestXML;
	} else {
		dataToSend = "--" + mimeBoundary + "\r\n";
		dataToSend = dataToSend + ORG_APACHE_CXF_MTOM_REQUEST_HEADER + "\r\n";
		dataToSend = dataToSend + requestXML;
		for (var bx in this.mtomparts) {
			var part = this.mtomparts[bx];
			dataToSend += "\r\n\r\n--" + mimeBoundary + "\r\n";
			dataToSend += part;
		}
		dataToSend += "--" + mimeBoundary + "--\r\n";
	}

	this.req.send(dataToSend);
}

CxfApacheOrgClient.prototype.request = org_apache_cxf_client_request;

function org_apache_cxf_trim_string(str) {
	return str.replace(/^\s+|\s+$/g, '');
}

// this gets an array of a=b strings, and produces a dictionary of x[a]=b;
function org_apache_cxf_parse_mime_keyword_value_pairs(strings) {
	var result = [];
	for (var x = 1;x < strings.length; x = x + 1) {
		var str = strings[x];
		var valequal = str.indexOf("=");
		if (valequal != -1) {
			var k = str.substr(0, valequal);
			var v = str.substr(valequal + 1);
			v = org_apache_cxf_trim_string(v);
			if (v.charAt(0) == '"') {
				v = v.substr(1, v.length - 2);
			}
			if (v.charAt(0) == "'") {
				v = v.substr(1, v.length - 2);
			}

			result[org_apache_cxf_trim_string(k.toLowerCase())] = v;
		}
	}
	return result;
}

function org_apache_cxf_regexp_escape(text) {
	if (!arguments.callee.sRE) {
		var specials = ['/', '.', '*', '+', '?', '|', '(', ')', '[', ']', '{',
				'}', '\\'];
		arguments.callee.sRE = new RegExp('(\\' + specials.join('|\\') + ')',
				'g');
	}
	return text.replace(arguments.callee.sRE, '\\$1');
}

// Called when we don't have response XML.
// returns true if we have multipart-related, false if we don't or can't parse
// it.
function org_apache_cxf_parse_multipart_related() {
	var contentType = this.req.getResponseHeader("content-type");
	if (!contentType)
		return false; // not bloody likely.
	var ctPart = contentType.split(/\s*;\s*/);
	var ctMain = ctPart[0].toLowerCase();
	if (ctMain != "multipart/related")
		return false;
	// now we have keyword-value pairs.
	var params = org_apache_cxf_parse_mime_keyword_value_pairs(ctPart);
	// there is a lot of noise we don't care about. all we really want is the
	// boundary.
	var boundary = params['boundary'];
	if (!boundary)
		return false;
	boundary = "--" + boundary; // the annoying 'extra-dash' convention.
	// var boundarySplitter = org_apache_cxf_regexp_escape(boundary);
	var text = this.req.responseText;
	// we are willing to use a lot of memory here.
	var parts = text.split(boundary);
	// now we have the parts.
	// now we have to pull headers off the parts.
	this.parts = [];
	// the first one is noise due to the initial boundary. The last will just be
	// -- due to MIME.
	for (var px = 1;px < parts.length - 1; px++) {
		var seenOneHeader = false;
		var x = 0; // misc index.
		var parttext = parts[px];
		var headers = [];
		nextHeaderLine : for (var endX = parttext.indexOf('\r', x);endX != -1; x = endX
				+ 1, endX = parttext.indexOf('\r', x)) {
			var headerLine = parttext.slice(x, endX);
			if (headerLine == "") {
				if (parttext.charAt(endX + 1) == '\n')
					endX++;
				if (seenOneHeader) {
					break nextHeaderLine;
				} else {
					continue nextHeaderLine;
				}
			}
			seenOneHeader = true;

			var colonIndex = headerLine.indexOf(":");
            var headerName = headerLine.slice(0, colonIndex).toLowerCase();
            var headerValue = headerLine.substr(colonIndex+1);
			headers[headerName] = org_apache_cxf_trim_string(headerValue);

			if (parttext.charAt(endX + 1) == '\n')
				endX++;
		}

		// Now, see about the mime type (if any) and the ID.
		var thispart = new Object(); // a constructor seems excessive.
		// at exit, x indicates the start of the blank line.
		if (parttext.charAt(x + 1) == '\n')
			x = x + 1;
		thispart.data = parttext.substr(x);
		thispart.contentType = headers['content-type'];
		if (px > 1) {
			var cid = headers['content-id'];
			// take of < and >
			cid = cid.substr(1, cid.length - 2);
			thispart.cid = cid;
			this.parts[cid] = thispart;
		} else {
			// the first part.
			var doc;
			if (window.ActiveXObject) {
				doc = new ActiveXObject("Microsoft.XMLDOM");
				doc.async = "false";
				doc.loadXML(thispart.data);
			} else {
				var parser = new DOMParser();
				doc = parser.parseFromString(thispart.data, "text/xml");
			}
			this.mpResponseXML = doc;
		}
	}
	return true;

}

CxfApacheOrgClient.prototype.parseMultipartRelated = org_apache_cxf_parse_multipart_related;

function org_apache_cxf_client_onReadyState() {
	var req = this.req;
	var ready = req.readyState;

	this.utils.trace("onreadystatechange " + ready);

	if (ready == this.READY_STATE_DONE) {
		var httpStatus;
		try {
			httpStatus = req.status;
		} catch (e) {
			// Firefox throws when there was an error here.
			this.utils
					.trace("onreadystatechange DONE ERROR retrieving status (connection error?)");
			if (this.onerror != null) {
				this.onerror(e);
			}
			return;

		}

		this.utils.trace("onreadystatechange DONE " + httpStatus);

		if (httpStatus == 200 || httpStatus == 0) {
			if (this.onsuccess != null) {
				// the onSuccess function is generated, and picks apart the
				// response.
				if (!req.responseXML) {
					if (this.parseMultipartRelated()) {
						this.onsuccess(this, this.mpResponseXML);
						return;
					}
					if (this.onerror != null) {
						this.onerror("Could not handle content of response.");
						return;
					}
				}
				this.onsuccess(this, req.responseXML);
			}
		} else {
			this.utils.trace("onreadystatechange DONE ERROR "
					+ req.getAllResponseHeaders() + " " + req.statusText + " "
					+ req.responseText);
			if (this.onerror != null)
				this.onerror(this);
		}
	}
}

CxfApacheOrgClient.prototype.onReadyState = org_apache_cxf_client_onReadyState;

function org_apache_cxf_package_mtom(value) {
	var uuid = org_apache_cxf_make_uuid('v4');
	var placeholder = '<xop:Include xmlns:xop="http://www.w3.org/2004/08/xop/include" '
			+ 'href="cid:' + uuid + '" />';
	var mtomObject = 'Content-Type: text/plain; charset="utf-8";\r\nContent-ID: <'
			+ uuid + '>\r\n\r\n' + value + '\r\n';
	this.client.mtomparts.push(mtomObject);
	return placeholder;
}

CxfApacheOrgUtil.prototype.packageMtom = org_apache_cxf_package_mtom;

// Holder object used for xs:any
// The namespaceURI and localName identify the global element from the schema.
// The object to go with it goes into object.
// If the Any is an array, put the array into the object slot.

function org_apache_cxf_any_holder(namespaceURI, localName, object) {
	this.typeMarker = "org_apache_cxf_any_holder";
	this.namespaceURI = namespaceURI;
	this.localName = localName;
	this.qname = "{" + namespaceURI + "}" + localName;
	this.object = object;
	this.raw = false;
}

// the following will simply dump the supplied XML into the message.
function org_apache_cxf_raw_any_holder(xml) {
	this.typeMarker = "org_apache_cxf_raw_any_holder";
	this.xml = xml;
	this.raw = true;
	this.xsiType = false;
}

// The following will get an xsi:type attribute in addition to dumping the XML
// into
// the message.
function org_apache_cxf_raw_typed_any_holder(namespaceURI, localName, xml) {
	this.typeMarker = "org_apache_cxf_raw_any_holder";
	this.namespaceURI = namespaceURI;
	this.localName = localName;
	this.xml = xml;
	this.raw = true;
	this.xsiType = true;
}

function org_apache_cxf_get_xsi_type(elementNode) {
	var attributes = elementNode.attributes;
	if ((attributes != null) && (attributes.length > 0)) {
		for (var x = 0;x < attributes.length; x++) {
			var attributeNodeName = attributes.item(x).nodeName;
			var attributeNamespacePrefix = org_apache_cxf_getPrefix(attributes
					.item(x).nodeName);
			var attributeNamespaceSuffix = org_apache_cxf_getLocalName(attributes
					.item(x).nodeName);
			if (attributeNamespaceSuffix == 'type') {
				// perhaps this is ours
				var ns = org_apache_cxf_getNamespaceURI(elementNode,
						attributeNamespacePrefix);
				if (ns == org_apache_cxf_XSI_namespace_uri) {
					return attributes.item(x).nodeValue;
				}
			}
		}
		return null;
	}
}

// Return an object if we can deserialize an object, otherwise return the
// element itself.
function org_apache_cxf_deserialize_anyType(cxfjsutils, element) {
	var type = org_apache_cxf_get_xsi_type(element);
	if (type != null) {
		// type is a :-qualified name.
		var namespacePrefix = org_apache_cxf_getPrefix(type);
		var localName = org_apache_cxf_getLocalName(type);
		var uri = org_apache_cxf_getNamespaceURI(element, namespacePrefix);
		if (uri == org_apache_cxf_XSD_namespace_uri) {
			// we expect a Text node below
			var textNode = element.firstChild;
			if (textNode == null)
				return null;
			var text = textNode.nodeValue;
			if (text == null)
				return null;
			// For any of the basic types, assume that the nodeValue is what the
			// doctor ordered,
			// converted to the appropriate type.
			// For some of the more interesting types this needs more work.
			if (localName == "int" || localName == "unsignedInt"
					|| localName == "long" || localName == "unsignedLong") {
				return parseInt(text);
			}
			if (localName == "float" || localName == "double")
				return parseFloat(text);
			if (localName == "boolean")
				return text == 'true';
			return text;
		}
		var qname = "{" + uri + "}" + localName;
		var deserializer = cxfjsutils.interfaceObject.globalElementDeserializers[qname];
		if (deserializer != null) {
			return deserializer(cxfjsutils, element);
		}
	}
	return element;
}
//
// Definitions for schema: http://webservice.bean.repository.uc.u.ambimmort.com/
//  schema1.xsd
//
//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}deletePolicy
//
function webservice_bean_repository_uc_u_ambimmort_com__deletePolicy () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__deletePolicy';
    this._arg0 = null;
    this._arg1 = null;
    this._arg2 = 0;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__deletePolicy.prototype.getArg0
// element get for arg0
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg0
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__deletePolicy.prototype.setArg0
//
function webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_getArg0() { return this._arg0;}

webservice_bean_repository_uc_u_ambimmort_com__deletePolicy.prototype.getArg0 = webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_getArg0;

function webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_setArg0(value) { this._arg0 = value;}

webservice_bean_repository_uc_u_ambimmort_com__deletePolicy.prototype.setArg0 = webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_setArg0;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__deletePolicy.prototype.getArg1
// element get for arg1
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg1
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__deletePolicy.prototype.setArg1
//
function webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_getArg1() { return this._arg1;}

webservice_bean_repository_uc_u_ambimmort_com__deletePolicy.prototype.getArg1 = webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_getArg1;

function webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_setArg1(value) { this._arg1 = value;}

webservice_bean_repository_uc_u_ambimmort_com__deletePolicy.prototype.setArg1 = webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_setArg1;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__deletePolicy.prototype.getArg2
// element get for arg2
// - element type is {http://www.w3.org/2001/XMLSchema}int
// - required element
//
// element set for arg2
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__deletePolicy.prototype.setArg2
//
function webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_getArg2() { return this._arg2;}

webservice_bean_repository_uc_u_ambimmort_com__deletePolicy.prototype.getArg2 = webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_getArg2;

function webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_setArg2(value) { this._arg2 = value;}

webservice_bean_repository_uc_u_ambimmort_com__deletePolicy.prototype.setArg2 = webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_setArg2;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}deletePolicy
//
function webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._arg0 != null) {
      xml = xml + '<arg0>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg0);
      xml = xml + '</arg0>';
     }
    }
    // block for local variables
    {
     if (this._arg1 != null) {
      xml = xml + '<arg1>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg1);
      xml = xml + '</arg1>';
     }
    }
    // block for local variables
    {
     xml = xml + '<arg2>';
     xml = xml + cxfjsutils.escapeXmlEntities(this._arg2);
     xml = xml + '</arg2>';
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__deletePolicy.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__deletePolicy();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg0');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg0')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg0(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg1');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg1')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg1(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg2');
    var value = null;
    if (!cxfjsutils.isElementNil(curElement)) {
     value = cxfjsutils.getNodeText(curElement);
     item = parseInt(value);
    }
    newobject.setArg2(item);
    var item = null;
    if (curElement != null) {
     curElement = cxfjsutils.getNextElementSibling(curElement);
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}repositoryOperationLogBean
//
function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean';
    this._createTime = 0;
    this._id = 0;
    this._operation = 0;
    this._svnFile = null;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.getCreateTime
// element get for createTime
// - element type is {http://www.w3.org/2001/XMLSchema}long
// - required element
//
// element set for createTime
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.setCreateTime
//
function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_getCreateTime() { return this._createTime;}

webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.getCreateTime = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_getCreateTime;

function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_setCreateTime(value) { this._createTime = value;}

webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.setCreateTime = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_setCreateTime;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.getId
// element get for id
// - element type is {http://www.w3.org/2001/XMLSchema}long
// - required element
//
// element set for id
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.setId
//
function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_getId() { return this._id;}

webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.getId = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_getId;

function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_setId(value) { this._id = value;}

webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.setId = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_setId;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.getOperation
// element get for operation
// - element type is {http://www.w3.org/2001/XMLSchema}int
// - required element
//
// element set for operation
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.setOperation
//
function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_getOperation() { return this._operation;}

webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.getOperation = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_getOperation;

function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_setOperation(value) { this._operation = value;}

webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.setOperation = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_setOperation;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.getSvnFile
// element get for svnFile
// - element type is {http://webservice.bean.repository.uc.u.ambimmort.com/}policyBean
// - optional element
//
// element set for svnFile
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.setSvnFile
//
function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_getSvnFile() { return this._svnFile;}

webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.getSvnFile = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_getSvnFile;

function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_setSvnFile(value) { this._svnFile = value;}

webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.setSvnFile = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_setSvnFile;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}repositoryOperationLogBean
//
function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     xml = xml + '<createTime>';
     xml = xml + cxfjsutils.escapeXmlEntities(this._createTime);
     xml = xml + '</createTime>';
    }
    // block for local variables
    {
     xml = xml + '<id>';
     xml = xml + cxfjsutils.escapeXmlEntities(this._id);
     xml = xml + '</id>';
    }
    // block for local variables
    {
     xml = xml + '<operation>';
     xml = xml + cxfjsutils.escapeXmlEntities(this._operation);
     xml = xml + '</operation>';
    }
    // block for local variables
    {
     if (this._svnFile != null) {
      xml = xml + this._svnFile.serialize(cxfjsutils, 'svnFile', null);
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing createTime');
    var value = null;
    if (!cxfjsutils.isElementNil(curElement)) {
     value = cxfjsutils.getNodeText(curElement);
     item = parseInt(value);
    }
    newobject.setCreateTime(item);
    var item = null;
    if (curElement != null) {
     curElement = cxfjsutils.getNextElementSibling(curElement);
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing id');
    var value = null;
    if (!cxfjsutils.isElementNil(curElement)) {
     value = cxfjsutils.getNodeText(curElement);
     item = parseInt(value);
    }
    newobject.setId(item);
    var item = null;
    if (curElement != null) {
     curElement = cxfjsutils.getNextElementSibling(curElement);
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing operation');
    var value = null;
    if (!cxfjsutils.isElementNil(curElement)) {
     value = cxfjsutils.getNodeText(curElement);
     item = parseInt(value);
    }
    newobject.setOperation(item);
    var item = null;
    if (curElement != null) {
     curElement = cxfjsutils.getNextElementSibling(curElement);
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing svnFile');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'svnFile')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      item = webservice_bean_repository_uc_u_ambimmort_com__policyBean_deserialize(cxfjsutils, curElement);
     }
     newobject.setSvnFile(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadPolicies
//
function webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies';
    this._arg0 = null;
    this._arg1 = null;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies.prototype.getArg0
// element get for arg0
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg0
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies.prototype.setArg0
//
function webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_getArg0() { return this._arg0;}

webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies.prototype.getArg0 = webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_getArg0;

function webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_setArg0(value) { this._arg0 = value;}

webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies.prototype.setArg0 = webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_setArg0;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies.prototype.getArg1
// element get for arg1
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg1
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies.prototype.setArg1
//
function webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_getArg1() { return this._arg1;}

webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies.prototype.getArg1 = webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_getArg1;

function webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_setArg1(value) { this._arg1 = value;}

webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies.prototype.setArg1 = webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_setArg1;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadPolicies
//
function webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._arg0 != null) {
      xml = xml + '<arg0>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg0);
      xml = xml + '</arg0>';
     }
    }
    // block for local variables
    {
     if (this._arg1 != null) {
      xml = xml + '<arg1>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg1);
      xml = xml + '</arg1>';
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg0');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg0')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg0(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg1');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg1')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg1(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}updatePolicy
//
function webservice_bean_repository_uc_u_ambimmort_com__updatePolicy () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__updatePolicy';
    this._arg0 = null;
    this._arg1 = null;
    this._arg2 = 0;
    this._arg3 = null;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.getArg0
// element get for arg0
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg0
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.setArg0
//
function webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_getArg0() { return this._arg0;}

webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.getArg0 = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_getArg0;

function webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_setArg0(value) { this._arg0 = value;}

webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.setArg0 = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_setArg0;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.getArg1
// element get for arg1
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg1
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.setArg1
//
function webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_getArg1() { return this._arg1;}

webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.getArg1 = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_getArg1;

function webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_setArg1(value) { this._arg1 = value;}

webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.setArg1 = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_setArg1;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.getArg2
// element get for arg2
// - element type is {http://www.w3.org/2001/XMLSchema}int
// - required element
//
// element set for arg2
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.setArg2
//
function webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_getArg2() { return this._arg2;}

webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.getArg2 = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_getArg2;

function webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_setArg2(value) { this._arg2 = value;}

webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.setArg2 = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_setArg2;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.getArg3
// element get for arg3
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg3
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.setArg3
//
function webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_getArg3() { return this._arg3;}

webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.getArg3 = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_getArg3;

function webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_setArg3(value) { this._arg3 = value;}

webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.setArg3 = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_setArg3;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}updatePolicy
//
function webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._arg0 != null) {
      xml = xml + '<arg0>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg0);
      xml = xml + '</arg0>';
     }
    }
    // block for local variables
    {
     if (this._arg1 != null) {
      xml = xml + '<arg1>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg1);
      xml = xml + '</arg1>';
     }
    }
    // block for local variables
    {
     xml = xml + '<arg2>';
     xml = xml + cxfjsutils.escapeXmlEntities(this._arg2);
     xml = xml + '</arg2>';
    }
    // block for local variables
    {
     if (this._arg3 != null) {
      xml = xml + '<arg3>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg3);
      xml = xml + '</arg3>';
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__updatePolicy.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__updatePolicy();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg0');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg0')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg0(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg1');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg1')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg1(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg2');
    var value = null;
    if (!cxfjsutils.isElementNil(curElement)) {
     value = cxfjsutils.getNodeText(curElement);
     item = parseInt(value);
    }
    newobject.setArg2(item);
    var item = null;
    if (curElement != null) {
     curElement = cxfjsutils.getNextElementSibling(curElement);
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg3');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg3')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg3(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoriesResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse';
    this._return = [];
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse.prototype.getReturn
// element get for return
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - required element
// - array
//
// element set for return
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse.prototype.setReturn
//
function webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse_getReturn() { return this._return;}

webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse.prototype.getReturn = webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse_getReturn;

function webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse_setReturn(value) { this._return = value;}

webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse.prototype.setReturn = webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse_setReturn;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoriesResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._return != null) {
      for (var ax = 0;ax < this._return.length;ax ++) {
       if (this._return[ax] == null) {
        xml = xml + '<return/>';
       } else {
        xml = xml + '<return>';
        xml = xml + cxfjsutils.escapeXmlEntities(this._return[ax]);
        xml = xml + '</return>';
       }
      }
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing return');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'return')) {
     item = [];
     do  {
      var arrayItem;
      var value = null;
      if (!cxfjsutils.isElementNil(curElement)) {
       value = cxfjsutils.getNodeText(curElement);
       arrayItem = value;
      }
      item.push(arrayItem);
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
       while(curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'return'));
     newobject.setReturn(item);
     var item = null;
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}policyBean
//
function webservice_bean_repository_uc_u_ambimmort_com__policyBean () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__policyBean';
    this._content = null;
    this._id = 0;
    this._isDeleted = '';
    this._isNewest = '';
    this._messageNo = 0;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.getContent
// element get for content
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for content
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.setContent
//
function webservice_bean_repository_uc_u_ambimmort_com__policyBean_getContent() { return this._content;}

webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.getContent = webservice_bean_repository_uc_u_ambimmort_com__policyBean_getContent;

function webservice_bean_repository_uc_u_ambimmort_com__policyBean_setContent(value) { this._content = value;}

webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.setContent = webservice_bean_repository_uc_u_ambimmort_com__policyBean_setContent;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.getId
// element get for id
// - element type is {http://www.w3.org/2001/XMLSchema}long
// - required element
//
// element set for id
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.setId
//
function webservice_bean_repository_uc_u_ambimmort_com__policyBean_getId() { return this._id;}

webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.getId = webservice_bean_repository_uc_u_ambimmort_com__policyBean_getId;

function webservice_bean_repository_uc_u_ambimmort_com__policyBean_setId(value) { this._id = value;}

webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.setId = webservice_bean_repository_uc_u_ambimmort_com__policyBean_setId;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.getIsDeleted
// element get for isDeleted
// - element type is {http://www.w3.org/2001/XMLSchema}boolean
// - required element
//
// element set for isDeleted
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.setIsDeleted
//
function webservice_bean_repository_uc_u_ambimmort_com__policyBean_getIsDeleted() { return this._isDeleted;}

webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.getIsDeleted = webservice_bean_repository_uc_u_ambimmort_com__policyBean_getIsDeleted;

function webservice_bean_repository_uc_u_ambimmort_com__policyBean_setIsDeleted(value) { this._isDeleted = value;}

webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.setIsDeleted = webservice_bean_repository_uc_u_ambimmort_com__policyBean_setIsDeleted;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.getIsNewest
// element get for isNewest
// - element type is {http://www.w3.org/2001/XMLSchema}boolean
// - required element
//
// element set for isNewest
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.setIsNewest
//
function webservice_bean_repository_uc_u_ambimmort_com__policyBean_getIsNewest() { return this._isNewest;}

webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.getIsNewest = webservice_bean_repository_uc_u_ambimmort_com__policyBean_getIsNewest;

function webservice_bean_repository_uc_u_ambimmort_com__policyBean_setIsNewest(value) { this._isNewest = value;}

webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.setIsNewest = webservice_bean_repository_uc_u_ambimmort_com__policyBean_setIsNewest;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.getMessageNo
// element get for messageNo
// - element type is {http://www.w3.org/2001/XMLSchema}int
// - required element
//
// element set for messageNo
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.setMessageNo
//
function webservice_bean_repository_uc_u_ambimmort_com__policyBean_getMessageNo() { return this._messageNo;}

webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.getMessageNo = webservice_bean_repository_uc_u_ambimmort_com__policyBean_getMessageNo;

function webservice_bean_repository_uc_u_ambimmort_com__policyBean_setMessageNo(value) { this._messageNo = value;}

webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.setMessageNo = webservice_bean_repository_uc_u_ambimmort_com__policyBean_setMessageNo;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}policyBean
//
function webservice_bean_repository_uc_u_ambimmort_com__policyBean_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._content != null) {
      xml = xml + '<content>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._content);
      xml = xml + '</content>';
     }
    }
    // block for local variables
    {
     xml = xml + '<id>';
     xml = xml + cxfjsutils.escapeXmlEntities(this._id);
     xml = xml + '</id>';
    }
    // block for local variables
    {
     xml = xml + '<isDeleted>';
     xml = xml + cxfjsutils.escapeXmlEntities(this._isDeleted);
     xml = xml + '</isDeleted>';
    }
    // block for local variables
    {
     xml = xml + '<isNewest>';
     xml = xml + cxfjsutils.escapeXmlEntities(this._isNewest);
     xml = xml + '</isNewest>';
    }
    // block for local variables
    {
     xml = xml + '<messageNo>';
     xml = xml + cxfjsutils.escapeXmlEntities(this._messageNo);
     xml = xml + '</messageNo>';
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__policyBean.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__policyBean_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__policyBean_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__policyBean();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing content');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'content')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setContent(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing id');
    var value = null;
    if (!cxfjsutils.isElementNil(curElement)) {
     value = cxfjsutils.getNodeText(curElement);
     item = parseInt(value);
    }
    newobject.setId(item);
    var item = null;
    if (curElement != null) {
     curElement = cxfjsutils.getNextElementSibling(curElement);
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing isDeleted');
    var value = null;
    if (!cxfjsutils.isElementNil(curElement)) {
     value = cxfjsutils.getNodeText(curElement);
     item = (value == 'true');
    }
    newobject.setIsDeleted(item);
    var item = null;
    if (curElement != null) {
     curElement = cxfjsutils.getNextElementSibling(curElement);
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing isNewest');
    var value = null;
    if (!cxfjsutils.isElementNil(curElement)) {
     value = cxfjsutils.getNodeText(curElement);
     item = (value == 'true');
    }
    newobject.setIsNewest(item);
    var item = null;
    if (curElement != null) {
     curElement = cxfjsutils.getNextElementSibling(curElement);
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing messageNo');
    var value = null;
    if (!cxfjsutils.isElementNil(curElement)) {
     value = cxfjsutils.getNodeText(curElement);
     item = parseInt(value);
    }
    newobject.setMessageNo(item);
    var item = null;
    if (curElement != null) {
     curElement = cxfjsutils.getNextElementSibling(curElement);
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageLogs
//
function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs';
    this._arg0 = null;
    this._arg1 = null;
    this._arg2 = 0;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs.prototype.getArg0
// element get for arg0
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg0
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs.prototype.setArg0
//
function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_getArg0() { return this._arg0;}

webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs.prototype.getArg0 = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_getArg0;

function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_setArg0(value) { this._arg0 = value;}

webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs.prototype.setArg0 = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_setArg0;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs.prototype.getArg1
// element get for arg1
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg1
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs.prototype.setArg1
//
function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_getArg1() { return this._arg1;}

webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs.prototype.getArg1 = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_getArg1;

function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_setArg1(value) { this._arg1 = value;}

webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs.prototype.setArg1 = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_setArg1;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs.prototype.getArg2
// element get for arg2
// - element type is {http://www.w3.org/2001/XMLSchema}int
// - required element
//
// element set for arg2
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs.prototype.setArg2
//
function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_getArg2() { return this._arg2;}

webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs.prototype.getArg2 = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_getArg2;

function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_setArg2(value) { this._arg2 = value;}

webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs.prototype.setArg2 = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_setArg2;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageLogs
//
function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._arg0 != null) {
      xml = xml + '<arg0>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg0);
      xml = xml + '</arg0>';
     }
    }
    // block for local variables
    {
     if (this._arg1 != null) {
      xml = xml + '<arg1>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg1);
      xml = xml + '</arg1>';
     }
    }
    // block for local variables
    {
     xml = xml + '<arg2>';
     xml = xml + cxfjsutils.escapeXmlEntities(this._arg2);
     xml = xml + '</arg2>';
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg0');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg0')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg0(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg1');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg1')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg1(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg2');
    var value = null;
    if (!cxfjsutils.isElementNil(curElement)) {
     value = cxfjsutils.getNodeText(curElement);
     item = parseInt(value);
    }
    newobject.setArg2(item);
    var item = null;
    if (curElement != null) {
     curElement = cxfjsutils.getNextElementSibling(curElement);
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}getMessage
//
function webservice_bean_repository_uc_u_ambimmort_com__getMessage () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__getMessage';
    this._arg0 = null;
    this._arg1 = null;
    this._arg2 = 0;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getMessage.prototype.getArg0
// element get for arg0
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg0
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getMessage.prototype.setArg0
//
function webservice_bean_repository_uc_u_ambimmort_com__getMessage_getArg0() { return this._arg0;}

webservice_bean_repository_uc_u_ambimmort_com__getMessage.prototype.getArg0 = webservice_bean_repository_uc_u_ambimmort_com__getMessage_getArg0;

function webservice_bean_repository_uc_u_ambimmort_com__getMessage_setArg0(value) { this._arg0 = value;}

webservice_bean_repository_uc_u_ambimmort_com__getMessage.prototype.setArg0 = webservice_bean_repository_uc_u_ambimmort_com__getMessage_setArg0;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getMessage.prototype.getArg1
// element get for arg1
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg1
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getMessage.prototype.setArg1
//
function webservice_bean_repository_uc_u_ambimmort_com__getMessage_getArg1() { return this._arg1;}

webservice_bean_repository_uc_u_ambimmort_com__getMessage.prototype.getArg1 = webservice_bean_repository_uc_u_ambimmort_com__getMessage_getArg1;

function webservice_bean_repository_uc_u_ambimmort_com__getMessage_setArg1(value) { this._arg1 = value;}

webservice_bean_repository_uc_u_ambimmort_com__getMessage.prototype.setArg1 = webservice_bean_repository_uc_u_ambimmort_com__getMessage_setArg1;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getMessage.prototype.getArg2
// element get for arg2
// - element type is {http://www.w3.org/2001/XMLSchema}int
// - required element
//
// element set for arg2
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getMessage.prototype.setArg2
//
function webservice_bean_repository_uc_u_ambimmort_com__getMessage_getArg2() { return this._arg2;}

webservice_bean_repository_uc_u_ambimmort_com__getMessage.prototype.getArg2 = webservice_bean_repository_uc_u_ambimmort_com__getMessage_getArg2;

function webservice_bean_repository_uc_u_ambimmort_com__getMessage_setArg2(value) { this._arg2 = value;}

webservice_bean_repository_uc_u_ambimmort_com__getMessage.prototype.setArg2 = webservice_bean_repository_uc_u_ambimmort_com__getMessage_setArg2;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}getMessage
//
function webservice_bean_repository_uc_u_ambimmort_com__getMessage_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._arg0 != null) {
      xml = xml + '<arg0>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg0);
      xml = xml + '</arg0>';
     }
    }
    // block for local variables
    {
     if (this._arg1 != null) {
      xml = xml + '<arg1>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg1);
      xml = xml + '</arg1>';
     }
    }
    // block for local variables
    {
     xml = xml + '<arg2>';
     xml = xml + cxfjsutils.escapeXmlEntities(this._arg2);
     xml = xml + '</arg2>';
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__getMessage.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__getMessage_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__getMessage_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__getMessage();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg0');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg0')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg0(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg1');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg1')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg1(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg2');
    var value = null;
    if (!cxfjsutils.isElementNil(curElement)) {
     value = cxfjsutils.getNodeText(curElement);
     item = parseInt(value);
    }
    newobject.setArg2(item);
    var item = null;
    if (curElement != null) {
     curElement = cxfjsutils.getNextElementSibling(curElement);
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}repositoryOperationBean
//
function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean';
    this._content = null;
    this._messageNo = 0;
    this._operation = 0;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean.prototype.getContent
// element get for content
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for content
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean.prototype.setContent
//
function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_getContent() { return this._content;}

webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean.prototype.getContent = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_getContent;

function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_setContent(value) { this._content = value;}

webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean.prototype.setContent = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_setContent;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean.prototype.getMessageNo
// element get for messageNo
// - element type is {http://www.w3.org/2001/XMLSchema}int
// - required element
//
// element set for messageNo
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean.prototype.setMessageNo
//
function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_getMessageNo() { return this._messageNo;}

webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean.prototype.getMessageNo = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_getMessageNo;

function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_setMessageNo(value) { this._messageNo = value;}

webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean.prototype.setMessageNo = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_setMessageNo;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean.prototype.getOperation
// element get for operation
// - element type is {http://www.w3.org/2001/XMLSchema}int
// - required element
//
// element set for operation
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean.prototype.setOperation
//
function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_getOperation() { return this._operation;}

webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean.prototype.getOperation = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_getOperation;

function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_setOperation(value) { this._operation = value;}

webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean.prototype.setOperation = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_setOperation;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}repositoryOperationBean
//
function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._content != null) {
      xml = xml + '<content>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._content);
      xml = xml + '</content>';
     }
    }
    // block for local variables
    {
     xml = xml + '<messageNo>';
     xml = xml + cxfjsutils.escapeXmlEntities(this._messageNo);
     xml = xml + '</messageNo>';
    }
    // block for local variables
    {
     xml = xml + '<operation>';
     xml = xml + cxfjsutils.escapeXmlEntities(this._operation);
     xml = xml + '</operation>';
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing content');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'content')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setContent(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing messageNo');
    var value = null;
    if (!cxfjsutils.isElementNil(curElement)) {
     value = cxfjsutils.getNodeText(curElement);
     item = parseInt(value);
    }
    newobject.setMessageNo(item);
    var item = null;
    if (curElement != null) {
     curElement = cxfjsutils.getNextElementSibling(curElement);
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing operation');
    var value = null;
    if (!cxfjsutils.isElementNil(curElement)) {
     value = cxfjsutils.getNodeText(curElement);
     item = parseInt(value);
    }
    newobject.setOperation(item);
    var item = null;
    if (curElement != null) {
     curElement = cxfjsutils.getNextElementSibling(curElement);
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadVersion
//
function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion';
    this._arg0 = null;
    this._arg1 = null;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion.prototype.getArg0
// element get for arg0
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg0
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion.prototype.setArg0
//
function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_getArg0() { return this._arg0;}

webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion.prototype.getArg0 = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_getArg0;

function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_setArg0(value) { this._arg0 = value;}

webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion.prototype.setArg0 = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_setArg0;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion.prototype.getArg1
// element get for arg1
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg1
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion.prototype.setArg1
//
function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_getArg1() { return this._arg1;}

webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion.prototype.getArg1 = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_getArg1;

function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_setArg1(value) { this._arg1 = value;}

webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion.prototype.setArg1 = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_setArg1;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadVersion
//
function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._arg0 != null) {
      xml = xml + '<arg0>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg0);
      xml = xml + '</arg0>';
     }
    }
    // block for local variables
    {
     if (this._arg1 != null) {
      xml = xml + '<arg1>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg1);
      xml = xml + '</arg1>';
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg0');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg0')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg0(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg1');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg1')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg1(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse';
    this._return = null;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse.prototype.getReturn
// element get for return
// - element type is {http://webservice.bean.repository.uc.u.ambimmort.com/}repositoryOperationLogBean
// - optional element
//
// element set for return
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse.prototype.setReturn
//
function webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse_getReturn() { return this._return;}

webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse.prototype.getReturn = webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse_getReturn;

function webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse_setReturn(value) { this._return = value;}

webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse.prototype.setReturn = webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse_setReturn;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._return != null) {
      xml = xml + this._return.serialize(cxfjsutils, 'return', null);
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing return');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'return')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      item = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_deserialize(cxfjsutils, curElement);
     }
     newobject.setReturn(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoryTypes
//
function webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes';
}

//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoryTypes
//
function webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}addPolicyResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse';
    this._return = null;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse.prototype.getReturn
// element get for return
// - element type is {http://webservice.bean.repository.uc.u.ambimmort.com/}repositoryOperationLogBean
// - optional element
//
// element set for return
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse.prototype.setReturn
//
function webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse_getReturn() { return this._return;}

webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse.prototype.getReturn = webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse_getReturn;

function webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse_setReturn(value) { this._return = value;}

webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse.prototype.setReturn = webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse_setReturn;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}addPolicyResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._return != null) {
      xml = xml + this._return.serialize(cxfjsutils, 'return', null);
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing return');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'return')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      item = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_deserialize(cxfjsutils, curElement);
     }
     newobject.setReturn(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositories
//
function webservice_bean_repository_uc_u_ambimmort_com__listRepositories () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__listRepositories';
    this._arg0 = null;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__listRepositories.prototype.getArg0
// element get for arg0
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg0
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__listRepositories.prototype.setArg0
//
function webservice_bean_repository_uc_u_ambimmort_com__listRepositories_getArg0() { return this._arg0;}

webservice_bean_repository_uc_u_ambimmort_com__listRepositories.prototype.getArg0 = webservice_bean_repository_uc_u_ambimmort_com__listRepositories_getArg0;

function webservice_bean_repository_uc_u_ambimmort_com__listRepositories_setArg0(value) { this._arg0 = value;}

webservice_bean_repository_uc_u_ambimmort_com__listRepositories.prototype.setArg0 = webservice_bean_repository_uc_u_ambimmort_com__listRepositories_setArg0;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositories
//
function webservice_bean_repository_uc_u_ambimmort_com__listRepositories_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._arg0 != null) {
      xml = xml + '<arg0>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg0);
      xml = xml + '</arg0>';
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__listRepositories.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__listRepositories_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__listRepositories_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__listRepositories();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg0');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg0')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg0(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}createRepositoryResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__createRepositoryResponse () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__createRepositoryResponse';
}

//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}createRepositoryResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__createRepositoryResponse_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__createRepositoryResponse.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__createRepositoryResponse_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__createRepositoryResponse_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__createRepositoryResponse();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadPoliciesResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse';
    this._return = [];
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse.prototype.getReturn
// element get for return
// - element type is {http://webservice.bean.repository.uc.u.ambimmort.com/}policyBean
// - required element
// - array
//
// element set for return
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse.prototype.setReturn
//
function webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse_getReturn() { return this._return;}

webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse.prototype.getReturn = webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse_getReturn;

function webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse_setReturn(value) { this._return = value;}

webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse.prototype.setReturn = webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse_setReturn;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadPoliciesResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._return != null) {
      for (var ax = 0;ax < this._return.length;ax ++) {
       if (this._return[ax] == null) {
        xml = xml + '<return/>';
       } else {
        xml = xml + this._return[ax].serialize(cxfjsutils, 'return', null);
       }
      }
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing return');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'return')) {
     item = [];
     do  {
      var arrayItem;
      var value = null;
      if (!cxfjsutils.isElementNil(curElement)) {
       arrayItem = webservice_bean_repository_uc_u_ambimmort_com__policyBean_deserialize(cxfjsutils, curElement);
      }
      item.push(arrayItem);
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
       while(curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'return'));
     newobject.setReturn(item);
     var item = null;
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageLogsResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse';
    this._return = [];
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse.prototype.getReturn
// element get for return
// - element type is {http://webservice.bean.repository.uc.u.ambimmort.com/}repositoryOperationLogBean
// - required element
// - array
//
// element set for return
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse.prototype.setReturn
//
function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse_getReturn() { return this._return;}

webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse.prototype.getReturn = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse_getReturn;

function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse_setReturn(value) { this._return = value;}

webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse.prototype.setReturn = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse_setReturn;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageLogsResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._return != null) {
      for (var ax = 0;ax < this._return.length;ax ++) {
       if (this._return[ax] == null) {
        xml = xml + '<return/>';
       } else {
        xml = xml + this._return[ax].serialize(cxfjsutils, 'return', null);
       }
      }
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing return');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'return')) {
     item = [];
     do  {
      var arrayItem;
      var value = null;
      if (!cxfjsutils.isElementNil(curElement)) {
       arrayItem = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_deserialize(cxfjsutils, curElement);
      }
      item.push(arrayItem);
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
       while(curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'return'));
     newobject.setReturn(item);
     var item = null;
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}createRepository
//
function webservice_bean_repository_uc_u_ambimmort_com__createRepository () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__createRepository';
    this._arg0 = null;
    this._arg1 = null;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__createRepository.prototype.getArg0
// element get for arg0
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg0
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__createRepository.prototype.setArg0
//
function webservice_bean_repository_uc_u_ambimmort_com__createRepository_getArg0() { return this._arg0;}

webservice_bean_repository_uc_u_ambimmort_com__createRepository.prototype.getArg0 = webservice_bean_repository_uc_u_ambimmort_com__createRepository_getArg0;

function webservice_bean_repository_uc_u_ambimmort_com__createRepository_setArg0(value) { this._arg0 = value;}

webservice_bean_repository_uc_u_ambimmort_com__createRepository.prototype.setArg0 = webservice_bean_repository_uc_u_ambimmort_com__createRepository_setArg0;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__createRepository.prototype.getArg1
// element get for arg1
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg1
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__createRepository.prototype.setArg1
//
function webservice_bean_repository_uc_u_ambimmort_com__createRepository_getArg1() { return this._arg1;}

webservice_bean_repository_uc_u_ambimmort_com__createRepository.prototype.getArg1 = webservice_bean_repository_uc_u_ambimmort_com__createRepository_getArg1;

function webservice_bean_repository_uc_u_ambimmort_com__createRepository_setArg1(value) { this._arg1 = value;}

webservice_bean_repository_uc_u_ambimmort_com__createRepository.prototype.setArg1 = webservice_bean_repository_uc_u_ambimmort_com__createRepository_setArg1;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}createRepository
//
function webservice_bean_repository_uc_u_ambimmort_com__createRepository_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._arg0 != null) {
      xml = xml + '<arg0>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg0);
      xml = xml + '</arg0>';
     }
    }
    // block for local variables
    {
     if (this._arg1 != null) {
      xml = xml + '<arg1>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg1);
      xml = xml + '</arg1>';
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__createRepository.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__createRepository_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__createRepository_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__createRepository();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg0');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg0')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg0(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg1');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg1')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg1(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoryTypesResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse';
    this._return = [];
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse.prototype.getReturn
// element get for return
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - required element
// - array
//
// element set for return
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse.prototype.setReturn
//
function webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse_getReturn() { return this._return;}

webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse.prototype.getReturn = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse_getReturn;

function webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse_setReturn(value) { this._return = value;}

webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse.prototype.setReturn = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse_setReturn;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoryTypesResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._return != null) {
      for (var ax = 0;ax < this._return.length;ax ++) {
       if (this._return[ax] == null) {
        xml = xml + '<return/>';
       } else {
        xml = xml + '<return>';
        xml = xml + cxfjsutils.escapeXmlEntities(this._return[ax]);
        xml = xml + '</return>';
       }
      }
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing return');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'return')) {
     item = [];
     do  {
      var arrayItem;
      var value = null;
      if (!cxfjsutils.isElementNil(curElement)) {
       value = cxfjsutils.getNodeText(curElement);
       arrayItem = value;
      }
      item.push(arrayItem);
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
       while(curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'return'));
     newobject.setReturn(item);
     var item = null;
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}deletePolicyResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse';
    this._return = null;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse.prototype.getReturn
// element get for return
// - element type is {http://webservice.bean.repository.uc.u.ambimmort.com/}repositoryOperationLogBean
// - optional element
//
// element set for return
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse.prototype.setReturn
//
function webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse_getReturn() { return this._return;}

webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse.prototype.getReturn = webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse_getReturn;

function webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse_setReturn(value) { this._return = value;}

webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse.prototype.setReturn = webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse_setReturn;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}deletePolicyResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._return != null) {
      xml = xml + this._return.serialize(cxfjsutils, 'return', null);
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing return');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'return')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      item = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_deserialize(cxfjsutils, curElement);
     }
     newobject.setReturn(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}getUpdatingOperationBeansResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse';
    this._return = [];
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse.prototype.getReturn
// element get for return
// - element type is {http://webservice.bean.repository.uc.u.ambimmort.com/}repositoryOperationBean
// - required element
// - array
//
// element set for return
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse.prototype.setReturn
//
function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse_getReturn() { return this._return;}

webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse.prototype.getReturn = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse_getReturn;

function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse_setReturn(value) { this._return = value;}

webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse.prototype.setReturn = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse_setReturn;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}getUpdatingOperationBeansResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._return != null) {
      for (var ax = 0;ax < this._return.length;ax ++) {
       if (this._return[ax] == null) {
        xml = xml + '<return/>';
       } else {
        xml = xml + this._return[ax].serialize(cxfjsutils, 'return', null);
       }
      }
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing return');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'return')) {
     item = [];
     do  {
      var arrayItem;
      var value = null;
      if (!cxfjsutils.isElementNil(curElement)) {
       arrayItem = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_deserialize(cxfjsutils, curElement);
      }
      item.push(arrayItem);
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
       while(curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'return'));
     newobject.setReturn(item);
     var item = null;
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}getUpdatingOperationBeans
//
function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans';
    this._arg0 = null;
    this._arg1 = null;
    this._arg2 = 0;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans.prototype.getArg0
// element get for arg0
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg0
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans.prototype.setArg0
//
function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_getArg0() { return this._arg0;}

webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans.prototype.getArg0 = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_getArg0;

function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_setArg0(value) { this._arg0 = value;}

webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans.prototype.setArg0 = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_setArg0;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans.prototype.getArg1
// element get for arg1
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg1
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans.prototype.setArg1
//
function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_getArg1() { return this._arg1;}

webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans.prototype.getArg1 = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_getArg1;

function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_setArg1(value) { this._arg1 = value;}

webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans.prototype.setArg1 = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_setArg1;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans.prototype.getArg2
// element get for arg2
// - element type is {http://www.w3.org/2001/XMLSchema}long
// - required element
//
// element set for arg2
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans.prototype.setArg2
//
function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_getArg2() { return this._arg2;}

webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans.prototype.getArg2 = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_getArg2;

function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_setArg2(value) { this._arg2 = value;}

webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans.prototype.setArg2 = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_setArg2;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}getUpdatingOperationBeans
//
function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._arg0 != null) {
      xml = xml + '<arg0>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg0);
      xml = xml + '</arg0>';
     }
    }
    // block for local variables
    {
     if (this._arg1 != null) {
      xml = xml + '<arg1>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg1);
      xml = xml + '</arg1>';
     }
    }
    // block for local variables
    {
     xml = xml + '<arg2>';
     xml = xml + cxfjsutils.escapeXmlEntities(this._arg2);
     xml = xml + '</arg2>';
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg0');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg0')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg0(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg1');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg1')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg1(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg2');
    var value = null;
    if (!cxfjsutils.isElementNil(curElement)) {
     value = cxfjsutils.getNodeText(curElement);
     item = parseInt(value);
    }
    newobject.setArg2(item);
    var item = null;
    if (curElement != null) {
     curElement = cxfjsutils.getNextElementSibling(curElement);
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadVersionResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse';
    this._return = 0;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse.prototype.getReturn
// element get for return
// - element type is {http://www.w3.org/2001/XMLSchema}long
// - required element
//
// element set for return
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse.prototype.setReturn
//
function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse_getReturn() { return this._return;}

webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse.prototype.getReturn = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse_getReturn;

function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse_setReturn(value) { this._return = value;}

webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse.prototype.setReturn = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse_setReturn;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadVersionResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     xml = xml + '<return>';
     xml = xml + cxfjsutils.escapeXmlEntities(this._return);
     xml = xml + '</return>';
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing return');
    var value = null;
    if (!cxfjsutils.isElementNil(curElement)) {
     value = cxfjsutils.getNodeText(curElement);
     item = parseInt(value);
    }
    newobject.setReturn(item);
    var item = null;
    if (curElement != null) {
     curElement = cxfjsutils.getNextElementSibling(curElement);
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}updatePolicyResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse';
    this._return = null;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse.prototype.getReturn
// element get for return
// - element type is {http://webservice.bean.repository.uc.u.ambimmort.com/}repositoryOperationLogBean
// - optional element
//
// element set for return
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse.prototype.setReturn
//
function webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse_getReturn() { return this._return;}

webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse.prototype.getReturn = webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse_getReturn;

function webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse_setReturn(value) { this._return = value;}

webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse.prototype.setReturn = webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse_setReturn;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}updatePolicyResponse
//
function webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._return != null) {
      xml = xml + this._return.serialize(cxfjsutils, 'return', null);
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing return');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'return')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      item = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_deserialize(cxfjsutils, curElement);
     }
     newobject.setReturn(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://webservice.bean.repository.uc.u.ambimmort.com/}addPolicy
//
function webservice_bean_repository_uc_u_ambimmort_com__addPolicy () {
    this.typeMarker = 'webservice_bean_repository_uc_u_ambimmort_com__addPolicy';
    this._arg0 = null;
    this._arg1 = null;
    this._arg2 = null;
}

//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__addPolicy.prototype.getArg0
// element get for arg0
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg0
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__addPolicy.prototype.setArg0
//
function webservice_bean_repository_uc_u_ambimmort_com__addPolicy_getArg0() { return this._arg0;}

webservice_bean_repository_uc_u_ambimmort_com__addPolicy.prototype.getArg0 = webservice_bean_repository_uc_u_ambimmort_com__addPolicy_getArg0;

function webservice_bean_repository_uc_u_ambimmort_com__addPolicy_setArg0(value) { this._arg0 = value;}

webservice_bean_repository_uc_u_ambimmort_com__addPolicy.prototype.setArg0 = webservice_bean_repository_uc_u_ambimmort_com__addPolicy_setArg0;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__addPolicy.prototype.getArg1
// element get for arg1
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg1
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__addPolicy.prototype.setArg1
//
function webservice_bean_repository_uc_u_ambimmort_com__addPolicy_getArg1() { return this._arg1;}

webservice_bean_repository_uc_u_ambimmort_com__addPolicy.prototype.getArg1 = webservice_bean_repository_uc_u_ambimmort_com__addPolicy_getArg1;

function webservice_bean_repository_uc_u_ambimmort_com__addPolicy_setArg1(value) { this._arg1 = value;}

webservice_bean_repository_uc_u_ambimmort_com__addPolicy.prototype.setArg1 = webservice_bean_repository_uc_u_ambimmort_com__addPolicy_setArg1;
//
// accessor is webservice_bean_repository_uc_u_ambimmort_com__addPolicy.prototype.getArg2
// element get for arg2
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for arg2
// setter function is is webservice_bean_repository_uc_u_ambimmort_com__addPolicy.prototype.setArg2
//
function webservice_bean_repository_uc_u_ambimmort_com__addPolicy_getArg2() { return this._arg2;}

webservice_bean_repository_uc_u_ambimmort_com__addPolicy.prototype.getArg2 = webservice_bean_repository_uc_u_ambimmort_com__addPolicy_getArg2;

function webservice_bean_repository_uc_u_ambimmort_com__addPolicy_setArg2(value) { this._arg2 = value;}

webservice_bean_repository_uc_u_ambimmort_com__addPolicy.prototype.setArg2 = webservice_bean_repository_uc_u_ambimmort_com__addPolicy_setArg2;
//
// Serialize {http://webservice.bean.repository.uc.u.ambimmort.com/}addPolicy
//
function webservice_bean_repository_uc_u_ambimmort_com__addPolicy_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._arg0 != null) {
      xml = xml + '<arg0>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg0);
      xml = xml + '</arg0>';
     }
    }
    // block for local variables
    {
     if (this._arg1 != null) {
      xml = xml + '<arg1>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg1);
      xml = xml + '</arg1>';
     }
    }
    // block for local variables
    {
     if (this._arg2 != null) {
      xml = xml + '<arg2>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._arg2);
      xml = xml + '</arg2>';
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__addPolicy.prototype.serialize = webservice_bean_repository_uc_u_ambimmort_com__addPolicy_serialize;

function webservice_bean_repository_uc_u_ambimmort_com__addPolicy_deserialize (cxfjsutils, element) {
    var newobject = new webservice_bean_repository_uc_u_ambimmort_com__addPolicy();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg0');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg0')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg0(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg1');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg1')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg1(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing arg2');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'arg2')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setArg2(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Definitions for service: {http://webservice.repository.uc.u.ambimmort.com/}RepositoryManagementWebServiceBeanImplService
//

// Javascript for {http://webservice.bean.repository.uc.u.ambimmort.com/}RepositoryManagementWebServiceBean

function webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean () {
    this.jsutils = new CxfApacheOrgUtil();
    this.jsutils.interfaceObject = this;
    this.synchronous = false;
    this.url = 'http://192.168.0.105:9000/UcRepositoryAdminWebService';
    this.client = null;
    this.response = null;
    this.globalElementSerializers = [];
    this.globalElementDeserializers = [];
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}deletePolicy'] = webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}deletePolicy'] = webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadPolicies'] = webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadPolicies'] = webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}updatePolicy'] = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}updatePolicy'] = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoriesResponse'] = webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoriesResponse'] = webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageLogs'] = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageLogs'] = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getMessage'] = webservice_bean_repository_uc_u_ambimmort_com__getMessage_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getMessage'] = webservice_bean_repository_uc_u_ambimmort_com__getMessage_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadVersion'] = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadVersion'] = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoryTypes'] = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoryTypes'] = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}addPolicyResponse'] = webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}addPolicyResponse'] = webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositories'] = webservice_bean_repository_uc_u_ambimmort_com__listRepositories_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositories'] = webservice_bean_repository_uc_u_ambimmort_com__listRepositories_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}createRepositoryResponse'] = webservice_bean_repository_uc_u_ambimmort_com__createRepositoryResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}createRepositoryResponse'] = webservice_bean_repository_uc_u_ambimmort_com__createRepositoryResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageLogsResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageLogsResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadPoliciesResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadPoliciesResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}createRepository'] = webservice_bean_repository_uc_u_ambimmort_com__createRepository_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}createRepository'] = webservice_bean_repository_uc_u_ambimmort_com__createRepository_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoryTypesResponse'] = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoryTypesResponse'] = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}deletePolicyResponse'] = webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}deletePolicyResponse'] = webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getUpdatingOperationBeansResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getUpdatingOperationBeansResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getUpdatingOperationBeans'] = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getUpdatingOperationBeans'] = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadVersionResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadVersionResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}updatePolicyResponse'] = webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}updatePolicyResponse'] = webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}addPolicy'] = webservice_bean_repository_uc_u_ambimmort_com__addPolicy_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}addPolicy'] = webservice_bean_repository_uc_u_ambimmort_com__addPolicy_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}deletePolicy'] = webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}deletePolicy'] = webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}repositoryOperationLogBean'] = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}repositoryOperationLogBean'] = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationLogBean_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadPolicies'] = webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadPolicies'] = webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}updatePolicy'] = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}updatePolicy'] = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoriesResponse'] = webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoriesResponse'] = webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}policyBean'] = webservice_bean_repository_uc_u_ambimmort_com__policyBean_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}policyBean'] = webservice_bean_repository_uc_u_ambimmort_com__policyBean_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageLogs'] = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageLogs'] = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getMessage'] = webservice_bean_repository_uc_u_ambimmort_com__getMessage_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getMessage'] = webservice_bean_repository_uc_u_ambimmort_com__getMessage_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}repositoryOperationBean'] = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}repositoryOperationBean'] = webservice_bean_repository_uc_u_ambimmort_com__repositoryOperationBean_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadVersion'] = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadVersion'] = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoryTypes'] = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoryTypes'] = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}addPolicyResponse'] = webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}addPolicyResponse'] = webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositories'] = webservice_bean_repository_uc_u_ambimmort_com__listRepositories_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositories'] = webservice_bean_repository_uc_u_ambimmort_com__listRepositories_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}createRepositoryResponse'] = webservice_bean_repository_uc_u_ambimmort_com__createRepositoryResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}createRepositoryResponse'] = webservice_bean_repository_uc_u_ambimmort_com__createRepositoryResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadPoliciesResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadPoliciesResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageLogsResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageLogsResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}createRepository'] = webservice_bean_repository_uc_u_ambimmort_com__createRepository_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}createRepository'] = webservice_bean_repository_uc_u_ambimmort_com__createRepository_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoryTypesResponse'] = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoryTypesResponse'] = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}deletePolicyResponse'] = webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}deletePolicyResponse'] = webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getUpdatingOperationBeansResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getUpdatingOperationBeansResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getUpdatingOperationBeans'] = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getUpdatingOperationBeans'] = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadVersionResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadVersionResponse'] = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}updatePolicyResponse'] = webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}updatePolicyResponse'] = webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse_deserialize;
    this.globalElementSerializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}addPolicy'] = webservice_bean_repository_uc_u_ambimmort_com__addPolicy_serialize;
    this.globalElementDeserializers['{http://webservice.bean.repository.uc.u.ambimmort.com/}addPolicy'] = webservice_bean_repository_uc_u_ambimmort_com__addPolicy_deserialize;
}

function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse_deserializeResponse');
     responseObject = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getMessageLogs_onsuccess = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_op_onsuccess;

function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getMessageLogs_onerror = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_op_onerror;

//
// Operation {http://webservice.bean.repository.uc.u.ambimmort.com/}getMessageLogs
// Wrapped operation.
// parameter arg0
// - simple type {http://www.w3.org/2001/XMLSchema}string// parameter arg1
// - simple type {http://www.w3.org/2001/XMLSchema}string// parameter arg2
// - simple type {http://www.w3.org/2001/XMLSchema}int//
function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_op(successCallback, errorCallback, arg0, arg1, arg2) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(3);
    args[0] = arg0;
    args[1] = arg1;
    args[2] = arg2;
    xml = this.getMessageLogs_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.getMessageLogs_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.getMessageLogs_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = '';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getMessageLogs = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_op;

function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_serializeInput(cxfjsutils, args) {
    var wrapperObj = new webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs();
    wrapperObj.setArg0(args[0]);
    wrapperObj.setArg1(args[1]);
    wrapperObj.setArg2(args[2]);
    var xml;
    xml = cxfjsutils.beginSoap11Message("xmlns:jns0='http://webservice.bean.repository.uc.u.ambimmort.com/' ");
    // block for local variables
    {
     xml = xml + wrapperObj.serialize(cxfjsutils, 'jns0:getMessageLogs', null);
    }
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getMessageLogs_serializeInput = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogs_serializeInput;

function webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse_deserializeResponse(cxfjsutils, partElement) {
    var returnObject = webservice_bean_repository_uc_u_ambimmort_com__getMessageLogsResponse_deserialize (cxfjsutils, partElement);

    return returnObject;
}
function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse_deserializeResponse');
     responseObject = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getUpdatingOperationBeans_onsuccess = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_op_onsuccess;

function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getUpdatingOperationBeans_onerror = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_op_onerror;

//
// Operation {http://webservice.bean.repository.uc.u.ambimmort.com/}getUpdatingOperationBeans
// Wrapped operation.
// parameter arg0
// - simple type {http://www.w3.org/2001/XMLSchema}string// parameter arg1
// - simple type {http://www.w3.org/2001/XMLSchema}string// parameter arg2
// - simple type {http://www.w3.org/2001/XMLSchema}long//
function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_op(successCallback, errorCallback, arg0, arg1, arg2) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(3);
    args[0] = arg0;
    args[1] = arg1;
    args[2] = arg2;
    xml = this.getUpdatingOperationBeans_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.getUpdatingOperationBeans_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.getUpdatingOperationBeans_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = '';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getUpdatingOperationBeans = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_op;

function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_serializeInput(cxfjsutils, args) {
    var wrapperObj = new webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans();
    wrapperObj.setArg0(args[0]);
    wrapperObj.setArg1(args[1]);
    wrapperObj.setArg2(args[2]);
    var xml;
    xml = cxfjsutils.beginSoap11Message("xmlns:jns0='http://webservice.bean.repository.uc.u.ambimmort.com/' ");
    // block for local variables
    {
     xml = xml + wrapperObj.serialize(cxfjsutils, 'jns0:getUpdatingOperationBeans', null);
    }
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getUpdatingOperationBeans_serializeInput = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeans_serializeInput;

function webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse_deserializeResponse(cxfjsutils, partElement) {
    var returnObject = webservice_bean_repository_uc_u_ambimmort_com__getUpdatingOperationBeansResponse_deserialize (cxfjsutils, partElement);

    return returnObject;
}
function webservice_bean_repository_uc_u_ambimmort_com__addPolicy_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse_deserializeResponse');
     responseObject = webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.addPolicy_onsuccess = webservice_bean_repository_uc_u_ambimmort_com__addPolicy_op_onsuccess;

function webservice_bean_repository_uc_u_ambimmort_com__addPolicy_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.addPolicy_onerror = webservice_bean_repository_uc_u_ambimmort_com__addPolicy_op_onerror;

//
// Operation {http://webservice.bean.repository.uc.u.ambimmort.com/}addPolicy
// Wrapped operation.
// parameter arg0
// - simple type {http://www.w3.org/2001/XMLSchema}string// parameter arg1
// - simple type {http://www.w3.org/2001/XMLSchema}string// parameter arg2
// - simple type {http://www.w3.org/2001/XMLSchema}string//
function webservice_bean_repository_uc_u_ambimmort_com__addPolicy_op(successCallback, errorCallback, arg0, arg1, arg2) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(3);
    args[0] = arg0;
    args[1] = arg1;
    args[2] = arg2;
    xml = this.addPolicy_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.addPolicy_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.addPolicy_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = '';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.addPolicy = webservice_bean_repository_uc_u_ambimmort_com__addPolicy_op;

function webservice_bean_repository_uc_u_ambimmort_com__addPolicy_serializeInput(cxfjsutils, args) {
    var wrapperObj = new webservice_bean_repository_uc_u_ambimmort_com__addPolicy();
    wrapperObj.setArg0(args[0]);
    wrapperObj.setArg1(args[1]);
    wrapperObj.setArg2(args[2]);
    var xml;
    xml = cxfjsutils.beginSoap11Message("xmlns:jns0='http://webservice.bean.repository.uc.u.ambimmort.com/' ");
    // block for local variables
    {
     xml = xml + wrapperObj.serialize(cxfjsutils, 'jns0:addPolicy', null);
    }
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.addPolicy_serializeInput = webservice_bean_repository_uc_u_ambimmort_com__addPolicy_serializeInput;

function webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse_deserializeResponse(cxfjsutils, partElement) {
    var returnObject = webservice_bean_repository_uc_u_ambimmort_com__addPolicyResponse_deserialize (cxfjsutils, partElement);

    return returnObject;
}
function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse_deserializeResponse');
     responseObject = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getHeadVersion_onsuccess = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_op_onsuccess;

function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getHeadVersion_onerror = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_op_onerror;

//
// Operation {http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadVersion
// Wrapped operation.
// parameter arg0
// - simple type {http://www.w3.org/2001/XMLSchema}string// parameter arg1
// - simple type {http://www.w3.org/2001/XMLSchema}string//
function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_op(successCallback, errorCallback, arg0, arg1) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(2);
    args[0] = arg0;
    args[1] = arg1;
    xml = this.getHeadVersion_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.getHeadVersion_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.getHeadVersion_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = '';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getHeadVersion = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_op;

function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_serializeInput(cxfjsutils, args) {
    var wrapperObj = new webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion();
    wrapperObj.setArg0(args[0]);
    wrapperObj.setArg1(args[1]);
    var xml;
    xml = cxfjsutils.beginSoap11Message("xmlns:jns0='http://webservice.bean.repository.uc.u.ambimmort.com/' ");
    // block for local variables
    {
     xml = xml + wrapperObj.serialize(cxfjsutils, 'jns0:getHeadVersion', null);
    }
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getHeadVersion_serializeInput = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersion_serializeInput;

function webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse_deserializeResponse(cxfjsutils, partElement) {
    var returnObject = webservice_bean_repository_uc_u_ambimmort_com__getHeadVersionResponse_deserialize (cxfjsutils, partElement);

    return returnObject;
}
function webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse_deserializeResponse');
     responseObject = webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.updatePolicy_onsuccess = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_op_onsuccess;

function webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.updatePolicy_onerror = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_op_onerror;

//
// Operation {http://webservice.bean.repository.uc.u.ambimmort.com/}updatePolicy
// Wrapped operation.
// parameter arg0
// - simple type {http://www.w3.org/2001/XMLSchema}string// parameter arg1
// - simple type {http://www.w3.org/2001/XMLSchema}string// parameter arg2
// - simple type {http://www.w3.org/2001/XMLSchema}int// parameter arg3
// - simple type {http://www.w3.org/2001/XMLSchema}string//
function webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_op(successCallback, errorCallback, arg0, arg1, arg2, arg3) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(4);
    args[0] = arg0;
    args[1] = arg1;
    args[2] = arg2;
    args[3] = arg3;
    xml = this.updatePolicy_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.updatePolicy_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.updatePolicy_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = '';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.updatePolicy = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_op;

function webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_serializeInput(cxfjsutils, args) {
    var wrapperObj = new webservice_bean_repository_uc_u_ambimmort_com__updatePolicy();
    wrapperObj.setArg0(args[0]);
    wrapperObj.setArg1(args[1]);
    wrapperObj.setArg2(args[2]);
    wrapperObj.setArg3(args[3]);
    var xml;
    xml = cxfjsutils.beginSoap11Message("xmlns:jns0='http://webservice.bean.repository.uc.u.ambimmort.com/' ");
    // block for local variables
    {
     xml = xml + wrapperObj.serialize(cxfjsutils, 'jns0:updatePolicy', null);
    }
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.updatePolicy_serializeInput = webservice_bean_repository_uc_u_ambimmort_com__updatePolicy_serializeInput;

function webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse_deserializeResponse(cxfjsutils, partElement) {
    var returnObject = webservice_bean_repository_uc_u_ambimmort_com__updatePolicyResponse_deserialize (cxfjsutils, partElement);

    return returnObject;
}
function webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse_deserializeResponse');
     responseObject = webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.deletePolicy_onsuccess = webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_op_onsuccess;

function webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.deletePolicy_onerror = webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_op_onerror;

//
// Operation {http://webservice.bean.repository.uc.u.ambimmort.com/}deletePolicy
// Wrapped operation.
// parameter arg0
// - simple type {http://www.w3.org/2001/XMLSchema}string// parameter arg1
// - simple type {http://www.w3.org/2001/XMLSchema}string// parameter arg2
// - simple type {http://www.w3.org/2001/XMLSchema}int//
function webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_op(successCallback, errorCallback, arg0, arg1, arg2) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(3);
    args[0] = arg0;
    args[1] = arg1;
    args[2] = arg2;
    xml = this.deletePolicy_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.deletePolicy_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.deletePolicy_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = '';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.deletePolicy = webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_op;

function webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_serializeInput(cxfjsutils, args) {
    var wrapperObj = new webservice_bean_repository_uc_u_ambimmort_com__deletePolicy();
    wrapperObj.setArg0(args[0]);
    wrapperObj.setArg1(args[1]);
    wrapperObj.setArg2(args[2]);
    var xml;
    xml = cxfjsutils.beginSoap11Message("xmlns:jns0='http://webservice.bean.repository.uc.u.ambimmort.com/' ");
    // block for local variables
    {
     xml = xml + wrapperObj.serialize(cxfjsutils, 'jns0:deletePolicy', null);
    }
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.deletePolicy_serializeInput = webservice_bean_repository_uc_u_ambimmort_com__deletePolicy_serializeInput;

function webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse_deserializeResponse(cxfjsutils, partElement) {
    var returnObject = webservice_bean_repository_uc_u_ambimmort_com__deletePolicyResponse_deserialize (cxfjsutils, partElement);

    return returnObject;
}
function webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse_deserializeResponse');
     responseObject = webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getHeadPolicies_onsuccess = webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_op_onsuccess;

function webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getHeadPolicies_onerror = webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_op_onerror;

//
// Operation {http://webservice.bean.repository.uc.u.ambimmort.com/}getHeadPolicies
// Wrapped operation.
// parameter arg0
// - simple type {http://www.w3.org/2001/XMLSchema}string// parameter arg1
// - simple type {http://www.w3.org/2001/XMLSchema}string//
function webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_op(successCallback, errorCallback, arg0, arg1) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(2);
    args[0] = arg0;
    args[1] = arg1;
    xml = this.getHeadPolicies_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.getHeadPolicies_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.getHeadPolicies_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = '';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getHeadPolicies = webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_op;

function webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_serializeInput(cxfjsutils, args) {
    var wrapperObj = new webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies();
    wrapperObj.setArg0(args[0]);
    wrapperObj.setArg1(args[1]);
    var xml;
    xml = cxfjsutils.beginSoap11Message("xmlns:jns0='http://webservice.bean.repository.uc.u.ambimmort.com/' ");
    // block for local variables
    {
     xml = xml + wrapperObj.serialize(cxfjsutils, 'jns0:getHeadPolicies', null);
    }
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getHeadPolicies_serializeInput = webservice_bean_repository_uc_u_ambimmort_com__getHeadPolicies_serializeInput;

function webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse_deserializeResponse(cxfjsutils, partElement) {
    var returnObject = webservice_bean_repository_uc_u_ambimmort_com__getHeadPoliciesResponse_deserialize (cxfjsutils, partElement);

    return returnObject;
}
function webservice_bean_repository_uc_u_ambimmort_com__listRepositories_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse_deserializeResponse');
     responseObject = webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.listRepositories_onsuccess = webservice_bean_repository_uc_u_ambimmort_com__listRepositories_op_onsuccess;

function webservice_bean_repository_uc_u_ambimmort_com__listRepositories_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.listRepositories_onerror = webservice_bean_repository_uc_u_ambimmort_com__listRepositories_op_onerror;

//
// Operation {http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositories
// Wrapped operation.
// parameter arg0
// - simple type {http://www.w3.org/2001/XMLSchema}string//
function webservice_bean_repository_uc_u_ambimmort_com__listRepositories_op(successCallback, errorCallback, arg0) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(1);
    args[0] = arg0;
    xml = this.listRepositories_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.listRepositories_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.listRepositories_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = '';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.listRepositories = webservice_bean_repository_uc_u_ambimmort_com__listRepositories_op;

function webservice_bean_repository_uc_u_ambimmort_com__listRepositories_serializeInput(cxfjsutils, args) {
    var wrapperObj = new webservice_bean_repository_uc_u_ambimmort_com__listRepositories();
    wrapperObj.setArg0(args[0]);
    var xml;
    xml = cxfjsutils.beginSoap11Message("xmlns:jns0='http://webservice.bean.repository.uc.u.ambimmort.com/' ");
    // block for local variables
    {
     xml = xml + wrapperObj.serialize(cxfjsutils, 'jns0:listRepositories', null);
    }
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.listRepositories_serializeInput = webservice_bean_repository_uc_u_ambimmort_com__listRepositories_serializeInput;

function webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse_deserializeResponse(cxfjsutils, partElement) {
    var returnObject = webservice_bean_repository_uc_u_ambimmort_com__listRepositoriesResponse_deserialize (cxfjsutils, partElement);

    return returnObject;
}
function webservice_bean_repository_uc_u_ambimmort_com__getMessage_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse_deserializeResponse');
     responseObject = webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getMessage_onsuccess = webservice_bean_repository_uc_u_ambimmort_com__getMessage_op_onsuccess;

function webservice_bean_repository_uc_u_ambimmort_com__getMessage_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getMessage_onerror = webservice_bean_repository_uc_u_ambimmort_com__getMessage_op_onerror;

//
// Operation {http://webservice.bean.repository.uc.u.ambimmort.com/}getMessage
// Wrapped operation.
// parameter arg0
// - simple type {http://www.w3.org/2001/XMLSchema}string// parameter arg1
// - simple type {http://www.w3.org/2001/XMLSchema}string// parameter arg2
// - simple type {http://www.w3.org/2001/XMLSchema}int//
function webservice_bean_repository_uc_u_ambimmort_com__getMessage_op(successCallback, errorCallback, arg0, arg1, arg2) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(3);
    args[0] = arg0;
    args[1] = arg1;
    args[2] = arg2;
    xml = this.getMessage_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.getMessage_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.getMessage_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = '';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getMessage = webservice_bean_repository_uc_u_ambimmort_com__getMessage_op;

function webservice_bean_repository_uc_u_ambimmort_com__getMessage_serializeInput(cxfjsutils, args) {
    var wrapperObj = new webservice_bean_repository_uc_u_ambimmort_com__getMessage();
    wrapperObj.setArg0(args[0]);
    wrapperObj.setArg1(args[1]);
    wrapperObj.setArg2(args[2]);
    var xml;
    xml = cxfjsutils.beginSoap11Message("xmlns:jns0='http://webservice.bean.repository.uc.u.ambimmort.com/' ");
    // block for local variables
    {
     xml = xml + wrapperObj.serialize(cxfjsutils, 'jns0:getMessage', null);
    }
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.getMessage_serializeInput = webservice_bean_repository_uc_u_ambimmort_com__getMessage_serializeInput;

function webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse_deserializeResponse(cxfjsutils, partElement) {
    var returnObject = webservice_bean_repository_uc_u_ambimmort_com__getMessageResponse_deserialize (cxfjsutils, partElement);

    return returnObject;
}
function webservice_bean_repository_uc_u_ambimmort_com__createRepository_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling webservice_bean_repository_uc_u_ambimmort_com__createRepositoryResponse_deserializeResponse');
     responseObject = webservice_bean_repository_uc_u_ambimmort_com__createRepositoryResponse_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.createRepository_onsuccess = webservice_bean_repository_uc_u_ambimmort_com__createRepository_op_onsuccess;

function webservice_bean_repository_uc_u_ambimmort_com__createRepository_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.createRepository_onerror = webservice_bean_repository_uc_u_ambimmort_com__createRepository_op_onerror;

//
// Operation {http://webservice.bean.repository.uc.u.ambimmort.com/}createRepository
// Wrapped operation.
// parameter arg0
// - simple type {http://www.w3.org/2001/XMLSchema}string// parameter arg1
// - simple type {http://www.w3.org/2001/XMLSchema}string//
function webservice_bean_repository_uc_u_ambimmort_com__createRepository_op(successCallback, errorCallback, arg0, arg1) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(2);
    args[0] = arg0;
    args[1] = arg1;
    xml = this.createRepository_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.createRepository_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.createRepository_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = '';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.createRepository = webservice_bean_repository_uc_u_ambimmort_com__createRepository_op;

function webservice_bean_repository_uc_u_ambimmort_com__createRepository_serializeInput(cxfjsutils, args) {
    var wrapperObj = new webservice_bean_repository_uc_u_ambimmort_com__createRepository();
    wrapperObj.setArg0(args[0]);
    wrapperObj.setArg1(args[1]);
    var xml;
    xml = cxfjsutils.beginSoap11Message("xmlns:jns0='http://webservice.bean.repository.uc.u.ambimmort.com/' ");
    // block for local variables
    {
     xml = xml + wrapperObj.serialize(cxfjsutils, 'jns0:createRepository', null);
    }
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.createRepository_serializeInput = webservice_bean_repository_uc_u_ambimmort_com__createRepository_serializeInput;

function webservice_bean_repository_uc_u_ambimmort_com__createRepositoryResponse_deserializeResponse(cxfjsutils, partElement) {
}
function webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse_deserializeResponse');
     responseObject = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.listRepositoryTypes_onsuccess = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes_op_onsuccess;

function webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.listRepositoryTypes_onerror = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes_op_onerror;

//
// Operation {http://webservice.bean.repository.uc.u.ambimmort.com/}listRepositoryTypes
// Wrapped operation.
//
function webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes_op(successCallback, errorCallback) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(0);
    xml = this.listRepositoryTypes_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.listRepositoryTypes_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.listRepositoryTypes_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = '';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.listRepositoryTypes = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes_op;

function webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes_serializeInput(cxfjsutils, args) {
    var wrapperObj = new webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes();
    var xml;
    xml = cxfjsutils.beginSoap11Message("xmlns:jns0='http://webservice.bean.repository.uc.u.ambimmort.com/' ");
    // block for local variables
    {
     xml = xml + wrapperObj.serialize(cxfjsutils, 'jns0:listRepositoryTypes', null);
    }
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean.prototype.listRepositoryTypes_serializeInput = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypes_serializeInput;

function webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse_deserializeResponse(cxfjsutils, partElement) {
    var returnObject = webservice_bean_repository_uc_u_ambimmort_com__listRepositoryTypesResponse_deserialize (cxfjsutils, partElement);

    return returnObject;
}
function webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean_webservice_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBeanImplPort () {
  this.url = 'http://192.168.0.105:9000/UcRepositoryAdminWebService';
}
webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean_webservice_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBeanImplPort.prototype = new webservice_bean_repository_uc_u_ambimmort_com__RepositoryManagementWebServiceBean;
