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
 *	CFBamXmlLoaderDelDepParse XML SAX Element Handler implementation
 *	for DelDep.
 */
public class CFBamXmlLoaderDelDepHandler
	extends CFLibXmlCoreElementHandler
{
	public CFBamXmlLoaderDelDepHandler( CFBamXmlLoader saxLoader ) {
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
			// DelDep Attributes
			String	attrName = null;
			String	attrDelDepChain = null;
			// Attribute Extraction
			String	attrLocalName;
			int		numAttrs;
			int		idxAttr;
			final String S_ProcName = "startElement";
			final String S_LocalName = "LocalName";

			assert qName.equals( "DelDep" );

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
				else if( attrLocalName.equals( "DelDepChain" ) ) {
					if( attrDelDepChain != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrDelDepChain = attrs.getValue( idxAttr );
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

			if( ( attrDelDepChain == null ) || ( attrDelDepChain.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"DelDepChain" );
			}

			// Save named attributes to context
			CFLibXmlCoreContext curContext = saxLoader.getCurContext();
			if( ! CFBamXmlLoader.getProcessSchema( curContext ) ) {
				return;
			}

			curContext.putNamedValue( "Id", attrId );
			curContext.putNamedValue( "Name", attrName );
			curContext.putNamedValue( "DelDepChain", attrDelDepChain );

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
			firstDot = attrDelDepChain.indexOf( '.' );
			String nextRelationName;
			String remainder;
			if( firstDot > 0 ) {
				nextRelationName = attrDelDepChain.substring( 0, firstDot );
				remainder = attrDelDepChain.substring( firstDot + 1 );
			}
			else {
				nextRelationName = attrDelDepChain;
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
			ICFBamDelTopDepObj origTopDep = schemaObj.getDelTopDepTableObj().newInstance();
			ICFBamDelTopDepEditObj editTopDep = (ICFBamDelTopDepEditObj)origTopDep.beginEdit();
			editTopDep.setRequiredOwnerTenant( fromTable.getRequiredOwnerTenant() );
			editTopDep.setRequiredContainerTable( fromTable );
			editTopDep.setRequiredName( attrName );
			editTopDep.setRequiredLookupRelation( resolvedRelation );
			origTopDep = (ICFBamDelTopDepObj)editTopDep.create();

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
				ICFBamDelSubDep1Obj origSubDep1 = schemaObj.getDelSubDep1TableObj().newInstance();
				ICFBamDelSubDep1EditObj editSubDep1 = (ICFBamDelSubDep1EditObj)origSubDep1.beginEdit();
				editSubDep1.setRequiredOwnerTenant( fromTable.getRequiredOwnerTenant() );
				editSubDep1.setRequiredContainerDelTopDep( origTopDep );
				editSubDep1.setRequiredName( resolvedRelation.getRequiredName() );
				editSubDep1.setRequiredLookupRelation( resolvedRelation );
				origSubDep1 = (ICFBamDelSubDep1Obj)editSubDep1.create();

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
					ICFBamDelSubDep2Obj origSubDep2 = schemaObj.getDelSubDep2TableObj().newInstance();
					ICFBamDelSubDep2EditObj editSubDep2 = (ICFBamDelSubDep2EditObj)origSubDep2.beginEdit();
					editSubDep2.setRequiredOwnerTenant( fromTable.getRequiredOwnerTenant() );
					editSubDep2.setRequiredContainerDelSubDep1( origSubDep1 );
					editSubDep2.setRequiredName( resolvedRelation.getRequiredName() );
					editSubDep2.setRequiredLookupRelation( resolvedRelation );
					origSubDep2 = (ICFBamDelSubDep2Obj)editSubDep2.create();

					if( remainder != null ) {
						curTable = origSubDep2.getRequiredLookupRelation().getRequiredLookupToTable();
						firstDot = remainder.indexOf( '.' );
						if( firstDot > 0 ) {
							throw new CFLibRuntimeException( getClass(),
								S_ProcName,
								saxLoader.getLocationInfo() + "Relation DelDepChain is invalid -- only 4 levels of indirection are supported" );
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
						ICFBamDelSubDep3Obj origSubDep3 = schemaObj.getDelSubDep3TableObj().newInstance();
						ICFBamDelSubDep3EditObj editSubDep3 = (ICFBamDelSubDep3EditObj)origSubDep3.beginEdit();
						editSubDep3.setRequiredOwnerTenant( fromTable.getRequiredOwnerTenant() );
						editSubDep3.setRequiredContainerDelSubDep2( origSubDep2 );
						editSubDep3.setRequiredName( resolvedRelation.getRequiredName() );
						editSubDep3.setRequiredLookupRelation( resolvedRelation );
						origSubDep3 = (ICFBamDelSubDep3Obj)editSubDep3.create();
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
