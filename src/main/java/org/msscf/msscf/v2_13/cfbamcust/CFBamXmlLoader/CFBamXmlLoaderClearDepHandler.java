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

import java.util.Iterator;

import org.msscf.msscf.v2_13.cflib.CFLib.*;

import org.xml.sax.*;

import org.msscf.msscf.v2_13.cfbam.CFBam.ICFBamSchema;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;

/*
 *	CFBamXmlLoaderClearDepParse XML SAX Element Handler implementation
 *	for ClearDep.
 */
public class CFBamXmlLoaderClearDepHandler
	extends CFLibXmlCoreElementHandler
{
	public CFBamXmlLoaderClearDepHandler( CFBamXmlLoader saxLoader ) {
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
			// ClearDep Attributes
			String	attrName = null;
			String	attrClearDepChain = null;
			// Attribute Extraction
			String	attrLocalName;
			int		numAttrs;
			int		idxAttr;
			final String S_ProcName = "startElement";
			final String S_LocalName = "LocalName";

			assert qName.equals( "ClearDep" );

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
				else if( attrLocalName.equals( "ClearDepChain" ) ) {
					if( attrClearDepChain != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrClearDepChain = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "schemaLocation" ) ) {
					// ignored
				}
				else {
					throw new CFLibUnrecognizedAttributeException( getClass(),
						S_ProcName,
						saxLoader.getLocationInfo(),
						attrLocalName );
				}
			}

			// Ensure that required attributes have values
			if( ( attrName == null ) || ( attrName.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"Name" );
			}

			if( ( attrClearDepChain == null ) || ( attrClearDepChain.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"ClearDepChain" );
			}

			// Save named attributes to context
			CFLibXmlCoreContext curContext = saxLoader.getCurContext();
			if( ! CFBamXmlLoader.getProcessSchema( curContext ) ) {
				return;
			}

			curContext.putNamedValue( "Id", attrId );
			curContext.putNamedValue( "Name", attrName );
			curContext.putNamedValue( "ClearDepChain", attrClearDepChain );

			if( ! CFBamXmlLoader.getProcessSchema( curContext ) ) {
				return;
			}

			CFLibXmlCoreContext parentContext = curContext.getPrevContext();
			ICFBamTableObj fromTable;
			if( parentContext != null ) {
				fromTable = (ICFBamTableObj)parentContext.getNamedValue( "Object" );
			}
			else {
				fromTable = null;
			}
			if( fromTable == null ) {
				throw new CFLibRuntimeException( getClass(),
					S_ProcName,
					saxLoader.getLocationInfo() + " PrevContext.Object is null, Table is required" );
			}

			ICFBamRelationTableObj relationTable = schemaObj.getRelationTableObj();

			int firstDot;

			ICFBamTableObj curTable = fromTable;
			firstDot = attrClearDepChain.indexOf( '.' );
			String nextRelationName;
			String remainder;
			if( firstDot > 0 ) {
				nextRelationName = attrClearDepChain.substring( 0, firstDot );
				remainder = attrClearDepChain.substring( firstDot + 1 );
			}
			else {
				nextRelationName = attrClearDepChain;
				remainder = null;
			}
			ICFBamRelationObj resolvedRelation = resolveInheritedRelation( relationTable, curTable, nextRelationName );
			if( resolvedRelation == null ) {
				throw new CFLibRuntimeException( getClass(),
						S_ProcName,
						saxLoader.getLocationInfo() + "Relation \""
							+ nextRelationName
							+ "\" not found for table \""
							+ curTable.getRequiredContainerSchemaDef().getRequiredName()
							+ "."
							+ curTable.getRequiredName()
							+ "\"" );
			}
			ICFBamClearTopDepObj origTopDep = schemaObj.getClearTopDepTableObj().newInstance();
			ICFBamClearTopDepEditObj editTopDep = (ICFBamClearTopDepEditObj)origTopDep.beginEdit();
			editTopDep.setRequiredOwnerTenant( fromTable.getRequiredOwnerTenant() );
			editTopDep.setRequiredContainerTable( fromTable );
			editTopDep.setRequiredName( attrName );
			editTopDep.setRequiredLookupRelation( resolvedRelation );
			origTopDep = (ICFBamClearTopDepObj)editTopDep.create();

			curContext.putNamedValue( "Object", origTopDep );

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
							saxLoader.getLocationInfo() + "Relation \""
								+ nextRelationName
								+ "\" not found for table \""
								+ curTable.getRequiredContainerSchemaDef().getRequiredName()
								+ "."
								+ curTable.getRequiredName()
								+ "\"" );
				}
				ICFBamClearSubDep1Obj origSubDep1 = schemaObj.getClearSubDep1TableObj().newInstance();
				ICFBamClearSubDep1EditObj editSubDep1 = (ICFBamClearSubDep1EditObj)origSubDep1.beginEdit();
				editSubDep1.setRequiredOwnerTenant( fromTable.getRequiredOwnerTenant() );
				editSubDep1.setRequiredContainerClearTopDep( origTopDep );
				editSubDep1.setRequiredName( resolvedRelation.getRequiredName() );
				editSubDep1.setRequiredLookupRelation( resolvedRelation );
				origSubDep1 = (ICFBamClearSubDep1Obj)editSubDep1.create();

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
								saxLoader.getLocationInfo() + "Relation \""
									+ nextRelationName
									+ "\" not found for table \""
									+ curTable.getRequiredContainerSchemaDef().getRequiredName()
									+ "."
									+ curTable.getRequiredName()
									+ "\"" );
					}
					ICFBamClearSubDep2Obj origSubDep2 = schemaObj.getClearSubDep2TableObj().newInstance();
					ICFBamClearSubDep2EditObj editSubDep2 = (ICFBamClearSubDep2EditObj)origSubDep2.beginEdit();
					editSubDep2.setRequiredOwnerTenant( fromTable.getRequiredOwnerTenant() );
					editSubDep2.setRequiredContainerClearSubDep1( origSubDep1 );
					editSubDep2.setRequiredName( resolvedRelation.getRequiredName() );
					editSubDep2.setRequiredLookupRelation( resolvedRelation );
					origSubDep2 = (ICFBamClearSubDep2Obj)editSubDep2.create();

					if( remainder != null ) {
						curTable = origSubDep2.getRequiredLookupRelation().getRequiredLookupToTable();
						firstDot = remainder.indexOf( '.' );
						if( firstDot > 0 ) {
							throw new CFLibRuntimeException( getClass(),
								S_ProcName,
								saxLoader.getLocationInfo() + "Relation ClearDepChain is invalid -- only 4 levels of indirection are supported" );
						}
						else {
							nextRelationName = remainder;
							remainder = null;
						}
						resolvedRelation = resolveInheritedRelation( relationTable, curTable, nextRelationName );
						if( resolvedRelation == null ) {
							throw new CFLibRuntimeException( getClass(),
									S_ProcName,
									saxLoader.getLocationInfo() + "Relation \""
										+ nextRelationName
										+ "\" not found for table \""
										+ curTable.getRequiredContainerSchemaDef().getRequiredName()
										+ "."
										+ curTable.getRequiredName()
										+ "\"" );
						}
						ICFBamClearSubDep3Obj origSubDep3 = schemaObj.getClearSubDep3TableObj().newInstance();
						ICFBamClearSubDep3EditObj editSubDep3 = (ICFBamClearSubDep3EditObj)origSubDep3.beginEdit();
						editSubDep3.setRequiredOwnerTenant( fromTable.getRequiredOwnerTenant() );
						editSubDep3.setRequiredContainerClearSubDep2( origSubDep2 );
						editSubDep3.setRequiredName( resolvedRelation.getRequiredName() );
						editSubDep3.setRequiredLookupRelation( resolvedRelation );
						origSubDep3 = (ICFBamClearSubDep3Obj)editSubDep3.create();
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
