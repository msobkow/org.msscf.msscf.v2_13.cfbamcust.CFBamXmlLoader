/*
 *	MSS Code Factory CFBam 2.13 CustXmlLoader
 *
 *	Copyright (c) 2020 Mark Stephen Sobkow
 *	
 *	This file is part of MSS Code Factory.
 *	
 *	MSS Code Factory is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	MSS Code Factory is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with MSS Code Factory.  If not, see https://www.gnu.org/licenses/.
 *	
 *	Donations to support MSS Code Factory can be made at
 *	https://www.paypal.com/paypalme2/MarkSobkow
 *	
 *	Contact Mark Stephen Sobkow at mark.sobkow@gmail.com for commercial licensing.
 */

package org.msscf.msscf.v2_13.cfbamcust.CFBamXmlLoader;

import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;

import org.msscf.msscf.v2_13.cflib.CFLib.*;

import org.apache.commons.codec.binary.Base64;
import org.xml.sax.*;

import org.msscf.msscf.v2_13.cfbam.CFBam.*;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;

/*
 *	CFBamXmlLoaderRelationParse XML SAX Element Handler implementation
 *	for Relation.
 */
public class CFBamXmlLoaderSuperClassRelationHandler
	extends CFLibXmlCoreElementHandler
{
	public CFBamXmlLoaderSuperClassRelationHandler( CFBamXmlLoader saxLoader ) {
		super( saxLoader );
	}

	public void startElement(
		String		uri,
		String		localName,
		String		qName,
		Attributes	attrs )
	throws SAXException
	{
		try {
			// Common XML Attributes
			String	attrId = null;
			// Scope Attributes
			// Scope References
			ICFBamTenantObj refTenant = null;
			// Relation Attributes
			String	attrName = null;
			String	attrShortName = null;
			String	attrLabel = null;
			String	attrShortDescription = null;
			String	attrDescription = null;
			String	attrDbName = null;
			String	attrSuffix = null;
			String	attrFromIndex = null;
			String	attrToIndex = null;
			String	attrNarrowed = null;
			String	attrPopDepChain = null;
			// Relation References
			ICFBamTenantObj refRelTenant = null;
			ICFBamSchema.RelationTypeEnum refRelationType = null;
			ICFBamTableObj refFromTable = null;
			ICFBamIndexObj refFromIndex = null;
			ICFBamTableObj refToTable = null;
			ICFBamIndexObj refToIndex = null;
			ICFBamRelationObj refNarrowed = null;
			// Attribute Extraction
			String	attrLocalName;
			int		numAttrs;
			int		idxAttr;
			final String S_ProcName = "startElement";
			final String S_LocalName = "LocalName";

			assert qName.equals( "SuperClassRelation" );

			CFBamXmlLoader saxLoader = (CFBamXmlLoader)getParser();
			if( saxLoader == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"getParser()" );
			}

			ICFBamSchemaObj schemaObj = saxLoader.getSchemaObj();
			if( schemaObj == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"getParser().getSchemaObj()" );
			}

			// Instantiate an edit buffer for the parsed information
			ICFBamRelationEditObj editBuff = (ICFBamRelationEditObj)schemaObj.getRelationTableObj().newInstance().beginEdit();

			// Extract Attributes
			numAttrs = attrs.getLength();
			for( idxAttr = 0; idxAttr < numAttrs; idxAttr++ ) {
				attrLocalName = attrs.getLocalName( idxAttr );
				if( attrLocalName.equals( "Id" ) ) {
					if( attrId != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrId = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "Name" ) ) {
					if( attrName != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrName = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ShortName" ) ) {
					if( attrShortName != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrShortName = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "Label" ) ) {
					if( attrLabel != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrLabel = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ShortDescription" ) ) {
					if( attrShortDescription != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrShortDescription = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "Description" ) ) {
					if( attrDescription != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrDescription = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "DbName" ) ) {
					if( attrDbName != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrDbName = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "Suffix" ) ) {
					if( attrSuffix != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrSuffix = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "FromIndex" ) ) {
					if( attrFromIndex != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrFromIndex = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "ToIndex" ) ) {
					if( attrToIndex != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrToIndex = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "Narrowed" ) ) {
					if( attrNarrowed != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrNarrowed = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "PopDepChain" ) ) {
					if( attrPopDepChain != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrPopDepChain = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "schemaLocation" ) ) {
					// ignored
				}
				else {
					throw new CFLibUnrecognizedAttributeException( getClass(),
						S_ProcName,
						getParser().getLocationInfo(),
						attrLocalName );
				}
			}

			// Ensure that required attributes have values
			if( attrName == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"Name" );
			}
			if( ( attrFromIndex == null ) || ( attrFromIndex.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"FromIndex" );
			}
			if( ( attrToIndex == null ) || ( attrToIndex.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"ToIndex" );
			}

			// Save named attributes to context
			CFLibXmlCoreContext curContext = getParser().getCurContext();
			if( ! CFBamXmlLoader.getProcessSchema( curContext ) ) {
				return;
			}

			curContext.putNamedValue( "Id", attrId );
			curContext.putNamedValue( "Name", attrName );
			curContext.putNamedValue( "ShortName", attrShortName );
			curContext.putNamedValue( "Label", attrLabel );
			curContext.putNamedValue( "ShortDescription", attrShortDescription );
			curContext.putNamedValue( "Description", attrDescription );
			curContext.putNamedValue( "DbName", attrDbName );
			curContext.putNamedValue( "Suffix", attrSuffix );
			curContext.putNamedValue( "FromIndex", attrFromIndex );
			curContext.putNamedValue( "ToIndex", attrToIndex );
			curContext.putNamedValue( "Narrowed", attrNarrowed );
			curContext.putNamedValue( "PopDepChain", attrPopDepChain );

			// Convert string attributes to native Cafe types
			// and apply the converted attributes to the editBuff.

			Integer natId;
			if( ( attrId != null ) && ( attrId.length() > 0 ) ) {
				natId = Integer.valueOf( Integer.parseInt( attrId ) );
			}
			else {
				natId = null;
			}

			String natName = attrName;
			editBuff.setRequiredName( natName );

			String natShortName = attrShortName;
			editBuff.setOptionalShortName( natShortName );

			String natLabel = attrLabel;
			editBuff.setOptionalLabel( natLabel );

			String natShortDescription = attrShortDescription;
			editBuff.setOptionalShortDescription( natShortDescription );

			String natDescription = attrDescription;
			editBuff.setOptionalDescription( natDescription );

			String natDbName = attrDbName;
			editBuff.setOptionalDbName( natDbName );

			String natSuffix = attrSuffix;
			editBuff.setOptionalSuffix( natSuffix );

			editBuff.setRequiredIsRequired( true );

			editBuff.setRequiredIsXsdContainer( false );

			// Get the scope/container object

			CFLibXmlCoreContext parentContext = curContext.getPrevContext();
			Object scopeObj;
			if( parentContext != null ) {
				scopeObj = parentContext.getNamedValue( "Object" );
			}
			else {
				scopeObj = null;
			}

			// Resolve and apply required Container reference

			if( scopeObj == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"scopeObj" );
			}
			else if( scopeObj instanceof ICFBamTableObj ) {
				refFromTable = (ICFBamTableObj) scopeObj;
				editBuff.setRequiredContainerFromTable( refFromTable );
				refRelTenant = (ICFBamTenantObj)editBuff.getRequiredOwnerRelTenant();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"scopeObj",
					scopeObj,
					"ICFBamTableObj" );
			}

			// Resolve and apply Owner reference

			if( refRelTenant == null ) {
				if( scopeObj instanceof ICFBamTenantObj ) {
					refRelTenant = (ICFBamTenantObj) scopeObj;
					editBuff.setRequiredOwnerRelTenant( refRelTenant );
				}
				else {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Owner<RelTenant>" );
				}
			}

			refTenant = refRelTenant;
			// Lookup refRelationType by key name value attr
			refRelationType = ICFBamSchema.RelationTypeEnum.Superclass;
			editBuff.setRequiredRelationType( refRelationType );

			// Lookup refFromIndex by qualified name
			if( ( attrFromIndex != null ) && ( attrFromIndex.length() > 0 ) ) {
				refFromIndex = (ICFBamIndexObj)(refFromTable.getNamedObject( attrFromIndex ) );
				if( refFromIndex == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Resolve FromIndex reference qualified name \"" + attrFromIndex + "\" to table Index" );
				}
			}
			else {
				refFromIndex = null;
			}
			editBuff.setRequiredLookupFromIndex( refFromIndex );

			// Lookup refToIndex by qualified name
			if( ( attrToIndex != null ) && ( attrToIndex.length() > 0 ) ) {
				refToIndex = (ICFBamIndexObj)(editBuff.getNamedObject( schemaObj.getIndexTableObj().getObjQualifyingClass(),
					attrToIndex ) );
				if( refToIndex == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Resolve ToIndex reference qualified name \"" + attrToIndex + "\" to table Index" );
				}
			}
			else {
				refToIndex = null;
			}
			editBuff.setRequiredLookupToIndex( refToIndex );
			editBuff.setRequiredLookupToTable( refToIndex.getRequiredContainerTable() );

			// Lookup refNarrowed by qualified name
			if( ( attrNarrowed != null ) && ( attrNarrowed.length() > 0 ) ) {
				refNarrowed = (ICFBamRelationObj)(editBuff.getNamedObject( schemaObj.getRelationTableObj().getObjQualifyingClass(),
					attrNarrowed ) );
				if( refNarrowed == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Resolve Narrowed reference qualified name \"" + attrNarrowed + "\" to table Relation" );
				}
			}
			else {
				refNarrowed = null;
			}
			editBuff.setOptionalLookupNarrowed( refNarrowed );

			ICFBamRelationEditObj editRelation = editBuff;
			ICFBamRelationObj origRelation = (ICFBamRelationObj)editRelation.create();
			editRelation = null;

			curContext.putNamedValue( "Object", origRelation );

			int firstDot;
			ICFBamRelationTableObj relationTable = schemaObj.getRelationTableObj();

			if( attrPopDepChain != null ) {
				ICFBamTableObj curTable = refFromTable;
				firstDot = attrPopDepChain.indexOf( '.' );
				String nextRelationName;
				String remainder;
				if( firstDot > 0 ) {
					nextRelationName = attrPopDepChain.substring( 0, firstDot );
					remainder = attrPopDepChain.substring( firstDot + 1 );
				}
				else {
					nextRelationName = attrPopDepChain;
					remainder = null;
				}
				ICFBamRelationObj resolvedRelation = resolveInheritedRelation( relationTable, curTable, nextRelationName );
				if( resolvedRelation == null ) {
					throw new CFLibRuntimeException( getClass(),
							S_ProcName,
							getParser().getLocationInfo() + "Relation \""
								+ nextRelationName
								+ "\" not found for table \""
								+ curTable.getRequiredContainerSchemaDef().getRequiredName()
								+ "."
								+ curTable.getRequiredName()
								+ "\"" );
				}
				ICFBamPopTopDepObj origTopDep = schemaObj.getPopTopDepTableObj().newInstance();
				ICFBamPopTopDepEditObj editTopDep = (ICFBamPopTopDepEditObj)origTopDep.beginEdit();
				editTopDep.setRequiredOwnerTenant( refFromTable.getRequiredOwnerTenant() );
				editTopDep.setRequiredContainerContRelation( origRelation );
				editTopDep.setRequiredName( "PopTopDep_" + resolvedRelation.getRequiredName() );
				editTopDep.setRequiredLookupRelation( resolvedRelation );
				origTopDep = (ICFBamPopTopDepObj)editTopDep.create();

				if( remainder != null ) {
					curTable = origTopDep.getRequiredLookupRelation().getRequiredLookupToTable();
					firstDot = remainder.indexOf( '.' );
					if( firstDot > 0 ) {
						nextRelationName = remainder.substring( 0, firstDot );
						remainder = remainder.substring( firstDot + 1 );
					}
					else {
						nextRelationName = remainder;
						remainder = null;
					}
					resolvedRelation = resolveInheritedRelation( relationTable, curTable, nextRelationName );
					if( resolvedRelation == null ) {
						throw new CFLibRuntimeException( getClass(),
								S_ProcName,
								getParser().getLocationInfo() + "Relation \""
									+ nextRelationName
									+ "\" not found for table \""
									+ curTable.getRequiredContainerSchemaDef().getRequiredName()
									+ "."
									+ curTable.getRequiredName()
									+ "\"" );
					}
					ICFBamPopSubDep1Obj origSubDep1 = schemaObj.getPopSubDep1TableObj().newInstance();
					ICFBamPopSubDep1EditObj editSubDep1 = (ICFBamPopSubDep1EditObj)origSubDep1.beginEdit();
					editSubDep1.setRequiredOwnerTenant( refFromTable.getRequiredOwnerTenant() );
					editSubDep1.setRequiredContainerContPopTopDep( origTopDep );
					editSubDep1.setRequiredName( resolvedRelation.getRequiredName() );
					editSubDep1.setRequiredLookupRelation( resolvedRelation );
					origSubDep1 = (ICFBamPopSubDep1Obj)editSubDep1.create();

					if( remainder != null ) {
						curTable = origSubDep1.getRequiredLookupRelation().getRequiredLookupToTable();
						firstDot = remainder.indexOf( '.' );
						if( firstDot > 0 ) {
							nextRelationName = remainder.substring( 0, firstDot );
							remainder = remainder.substring( firstDot + 1 );
						}
						else {
							nextRelationName = remainder;
							remainder = null;
						}
						resolvedRelation = resolveInheritedRelation( relationTable, curTable, nextRelationName );
						if( resolvedRelation == null ) {
							throw new CFLibRuntimeException( getClass(),
									S_ProcName,
									getParser().getLocationInfo() + "Relation \""
										+ nextRelationName
										+ "\" not found for table \""
										+ curTable.getRequiredContainerSchemaDef().getRequiredName()
										+ "."
										+ curTable.getRequiredName()
										+ "\"" );
						}
						ICFBamPopSubDep2Obj origSubDep2 = schemaObj.getPopSubDep2TableObj().newInstance();
						ICFBamPopSubDep2EditObj editSubDep2 = (ICFBamPopSubDep2EditObj)origSubDep2.beginEdit();
						editSubDep2.setRequiredOwnerTenant( refFromTable.getRequiredOwnerTenant() );
						editSubDep2.setRequiredContainerPopSubDep1( origSubDep1 );
						editSubDep2.setRequiredName( resolvedRelation.getRequiredName() );
						editSubDep2.setRequiredLookupRelation( resolvedRelation );
						origSubDep2 = (ICFBamPopSubDep2Obj)editSubDep2.create();

						if( remainder != null ) {
							curTable = origSubDep2.getRequiredLookupRelation().getRequiredLookupToTable();
							firstDot = remainder.indexOf( '.' );
							if( firstDot > 0 ) {
								throw new CFLibRuntimeException( getClass(),
									S_ProcName,
									"Relation PopDepChain is invalid -- only 4 levels of indirection are supported" );
							}
							else {
								nextRelationName = remainder;
								remainder = null;
							}
							resolvedRelation = resolveInheritedRelation( relationTable, curTable, nextRelationName );
							if( resolvedRelation == null ) {
								throw new CFLibRuntimeException( getClass(),
										S_ProcName,
										getParser().getLocationInfo() + "Relation \""
											+ nextRelationName
											+ "\" not found for table \""
											+ curTable.getRequiredContainerSchemaDef().getRequiredName()
											+ "."
											+ curTable.getRequiredName()
											+ "\"" );
							}
							ICFBamPopSubDep3Obj origSubDep3 = schemaObj.getPopSubDep3TableObj().newInstance();
							ICFBamPopSubDep3EditObj editSubDep3 = (ICFBamPopSubDep3EditObj)origSubDep3.beginEdit();
							editSubDep3.setRequiredOwnerTenant( refFromTable.getRequiredOwnerTenant() );
							editSubDep3.setRequiredContainerPopSubDep2( origSubDep2 );
							editSubDep3.setRequiredName( resolvedRelation.getRequiredName() );
							editSubDep3.setRequiredLookupRelation( resolvedRelation );
							origSubDep3 = (ICFBamPopSubDep3Obj)editSubDep3.create();
						}
					}
				}
			}
		}
		catch( RuntimeException e ) {
			throw new RuntimeException( "Near " + getParser().getLocationInfo() + ": Caught and rethrew " + e.getClass().getSimpleName() + " - " + e.getMessage(),
				e );
		}
		catch( Error e ) {
			throw new Error( "Near " + getParser().getLocationInfo() + ": Caught and rethrew " + e.getClass().getSimpleName() + " - " + e.getMessage(),
				e );
		}
	}

	protected ICFBamRelationObj resolveInheritedRelation( ICFBamRelationTableObj relationTable, ICFBamTableObj tbl, String relName ) {
		ICFBamRelationObj curRelation;
		ICFBamRelationObj superRelation;
		ICFBamRelationObj retval = null;
		ICFBamTableObj curTable = tbl;
		while( ( retval == null ) && ( curTable != null ) ) {
			retval = relationTable.readRelationByUNameIdx( curTable.getRequiredTenantId(),
					curTable.getRequiredId(),
					relName );
			if( retval == null ) {
				Iterator<ICFBamRelationObj> relIter = curTable.getOptionalComponentsRelation().iterator();
				superRelation = null;
				while( ( superRelation == null ) && relIter.hasNext() ) {
					curRelation = relIter.next();
					if( curRelation.getRequiredRelationType() == ICFBamSchema.RelationTypeEnum.Superclass ) {
						superRelation = curRelation;
					}
				}
				if( superRelation != null ) {
					curTable = superRelation.getRequiredLookupToTable();
				}
				else {
					curTable = null;
				}
			}
		}
		return( retval );
	}

	public void endElement(
		String		uri,
		String		localName,
		String		qName )
	throws SAXException
	{
	}
}
