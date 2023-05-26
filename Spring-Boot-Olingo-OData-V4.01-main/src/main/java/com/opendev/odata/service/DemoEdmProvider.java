package com.opendev.odata.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.*;
import org.apache.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression;
import org.apache.olingo.commons.api.edmx.EdmxReference;
import org.apache.olingo.commons.api.edmx.EdmxReferenceInclude;
import org.apache.olingo.commons.api.ex.ODataException;
import org.springframework.stereotype.Component;

import com.opendev.odata.util.Constants;
import org.apache.olingo.commons.api.edmx.EdmxReferenceIncludeAnnotation;


@Component
public class DemoEdmProvider extends CsdlAbstractEdmProvider {

	/**
	 * This method is called for one of the EntityTypes that are configured in the
	 * Schema
	 */

	@Override
	public List<CsdlAliasInfo> getAliasInfos() throws ODataException {
		return Arrays.asList(
				new CsdlAliasInfo().setAlias(Constants.NAMESPACE_ALIAS ).setNamespace(Constants.NAMESPACE),
				new CsdlAliasInfo().setAlias("SAP__common").setNamespace("com.sap.vocabularies.Common.v1"));
	}

	@Override
	public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) throws ODataException {
		if (entityTypeName.equals(Constants.ET_PRODUCT_FQN)) {

			// create EntityType properties
			CsdlProperty id = new CsdlProperty().setName("ID")
					.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty name = new CsdlProperty().setName("Name")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty description = new CsdlProperty().setName("Description")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

			// create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("ID");

			// configure EntityType
			CsdlEntityType entityType = new CsdlEntityType();
			entityType.setName(Constants.ET_PRODUCT_NAME);
			entityType.setProperties(Arrays.asList(id, name, description));
			entityType.setKey(Collections.singletonList(propertyRef));

			return entityType;
		}

		return null;
	}

	@Override
	public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) throws ODataException {
		if (entityContainer.equals(Constants.CONTAINER) && entitySetName.equals(Constants.ES_PRODUCTS_NAME)) {
			CsdlEntitySet entitySet = new CsdlEntitySet();
			entitySet.setName(Constants.ES_PRODUCTS_NAME);
			entitySet.setType(Constants.ET_PRODUCT_FQN);
/*			entitySet.setAnnotations(Arrays.asList(new CsdlAnnotation()
					.setTerm("com.sap.vocabularies.UI.v1.LineItem").setExpression(
							new CsdlConstantExpression(CsdlConstantExpression
									.ConstantExpressionType.String, "someInfo"))));*/

			return entitySet;
		}

		return null;
	}

	@Override
	public CsdlAnnotations getAnnotationsGroup(FullQualifiedName targetName, String qualifier) throws ODataException {
		CsdlAnnotations annotationGroup = new CsdlAnnotations();
		CsdlAnnotation nameAnnotation = new CsdlAnnotation();
		CsdlAnnotation descAnnotation = new CsdlAnnotation();
		List<CsdlAnnotation> annotations = new ArrayList<>();
		FullQualifiedName fqn = new FullQualifiedName("SAP__common","Label");
		nameAnnotation.setTerm("SAP__common.Label");
		descAnnotation.setTerm(fqn.toString());
		annotations.add(nameAnnotation);
		annotations.add(descAnnotation);
		annotationGroup.setTarget(targetName.toString());
		annotationGroup.setAnnotations(annotations);
		return annotationGroup;

	}

	@Override
	public CsdlTerm getTerm(final FullQualifiedName termName) throws ODataException {
		return new CsdlTerm().setName(termName.toString());
	}

	/**
	 * This method is invoked when displaying the Service Document at e.g.
	 * http://localhost:8080/OData/V1.0
	 */
	@Override
	public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) throws ODataException {
		if (entityContainerName == null || entityContainerName.equals(Constants.CONTAINER)) {
			CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
			entityContainerInfo.setContainerName(Constants.CONTAINER);
			return entityContainerInfo;
		}
		return null;
	}

	@Override
	public List<CsdlSchema> getSchemas() throws ODataException {
		// create Schema
		CsdlSchema schema = new CsdlSchema();
		schema.setNamespace(Constants.NAMESPACE);
		schema.setAlias("SAP__self");

		// add EntityTypes
		List<CsdlEntityType> entityTypes = new ArrayList<>();
		entityTypes.add(getEntityType(Constants.ET_PRODUCT_FQN));
		schema.setEntityTypes(entityTypes);

		// add EntityContainer
		schema.setEntityContainer(getEntityContainer());

		// add AnnotationGroup
		List<CsdlAnnotations> annotationGroups = new ArrayList<>();
		annotationGroups.add(getAnnotationsGroup(Constants.ET_PRODUCT_NAME_FQN, ""));
		schema.setAnnotationsGroup(annotationGroups );
		schema.setAnnotations(getAnnotationsGroup(Constants.ET_PRODUCT_NAME_FQN, "").getAnnotations());

		// finally
		List<CsdlSchema> schemas = new ArrayList<>();
		schemas.add(schema);

		return schemas;
	}

	@Override
	public CsdlEntityContainer getEntityContainer() throws ODataException {
		// create EntitySets
		List<CsdlEntitySet> entitySets = new ArrayList<>();
		entitySets.add(getEntitySet(Constants.CONTAINER, Constants.ES_PRODUCTS_NAME));

		// create EntityContainer
		CsdlEntityContainer entityContainer = new CsdlEntityContainer();
		entityContainer.setName(Constants.CONTAINER_NAME);
		entityContainer.setEntitySets(entitySets);

		return entityContainer;
	}

}
