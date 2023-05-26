package com.opendev.odata.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import com.opendev.odata.service.DemoEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlEdmProvider;
import org.apache.olingo.commons.api.edmx.EdmxReferenceInclude;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.olingo.commons.api.edmx.EdmxReference;


@RestController
@RequestMapping(ODataController.URI)
public class ODataController {
	
	protected static final String URI = "/OData";

	@Autowired
	DemoEdmProvider edmProvider;
	
	@Autowired
	EntityCollectionProcessor processor;
	
	@RequestMapping(value = "*")
	public void process(HttpServletRequest request, HttpServletResponse response) {
		OData odata = OData.newInstance();
		java.net.URI uri;
		try {
			uri  = new URI("http://www.sap.com/Protocols/SAPData");
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		ArrayList<EdmxReference> reflist = new ArrayList<>();
		EdmxReference ref = new EdmxReference(uri);
		EdmxReferenceInclude refInc = new EdmxReferenceInclude("com.sap.vocabularies.Common.v1" , "SAP__common");
		ref.addInclude(refInc);
		reflist.add(ref);
		ServiceMetadata edm = odata.createServiceMetadata(edmProvider,
				reflist);
		ODataHttpHandler handler = odata.createHandler(edm);
		handler.register(processor);
		handler.process(new HttpServletRequestWrapper(request) {
			// Spring MVC matches the whole path as the servlet path
			// Olingo wants just the prefix, ie upto /OData/V1.0, so that it
			// can parse the rest of it as an OData path. So we need to override
			// getServletPath()
			@Override
			public String getServletPath() {
				return ODataController.URI;
			}
		}, response);
	}
}
