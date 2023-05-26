package com.opendev.odata.util;

import org.apache.olingo.commons.api.edm.FullQualifiedName;

public final class Constants {

	private Constants() {
        throw new IllegalStateException("Utility class");
    }
	
	// Service Namespace
	public static final String NAMESPACE = "OData.Demo";
	public static final String NAMESPACE_ALIAS = "MyNameSpace";

	// EDM Container
	public static final String CONTAINER_NAME = "Container";
	public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

	// Entity Types Names
	public static final String ET_PRODUCT_NAME = "Product";
	public static final String ET_PRODUCT_PROP_NAME = ET_PRODUCT_NAME + "/Name";
	public static final String ET_PRODUCT_PROP_ID = "ID";
	public static final FullQualifiedName ET_PRODUCT_FQN = new FullQualifiedName(NAMESPACE, ET_PRODUCT_NAME);
	public static final FullQualifiedName ET_PRODUCT_NAME_FQN = new FullQualifiedName(NAMESPACE, ET_PRODUCT_PROP_NAME);

	// Entity Set Names
	public static final String ES_PRODUCTS_NAME = "Products";
}
