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
import org.msscf.msscf.v2_13.cfbamcust.MSSBamCF.*;

/*
 *	CFBamXmlLoaderSchemaRefParse XML SAX Element Handler implementation
 *	for SchemaRef.
 */
public class CFBamXmlLoaderSchemaRefHandler
	extends CFLibXmlCoreElementHandler
{
	public CFBamXmlLoaderSchemaRefHandler( CFBamXmlLoader saxLoader ) {
		super( saxLoader );
	}

	protected void importMergeSchemaTypes( ICFBamSchemaDefObj dst, ICFBamSchemaDefObj src ) {
		final String S_ProcName = "importMergeSchemaTypes";
		List<ICFBamValueObj> components = src.getOptionalComponentsTypes();
		Iterator<ICFBamValueObj> componentValues = components.iterator();
		ICFBamValueTableObj dstValueTableObj = dst.getSchema().getValueTableObj();
		ICFBamTableTableObj dstTableTableObj = dst.getSchema().getTableTableObj();
		ICFBamTableObj srcDispenser;
		ICFBamTableObj dstDispenser;
		ICFBamValueObj value;
		while( componentValues.hasNext() ) {
			value = componentValues.next();
			if( null == dstValueTableObj.readValueByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), value.getRequiredName() ) ) {
				if( value instanceof ICFBamBoolTypeObj ) {
					ICFBamBoolTypeTableObj GenTableObj = dst.getSchema().getBoolTypeTableObj();
					ICFBamBoolTypeObj srcGenDef = (ICFBamBoolTypeObj)value;
					ICFBamBoolTypeObj dstOrigDef = GenTableObj.readBoolTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamBoolTypeEditObj dstEditDef = (ICFBamBoolTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalFalseString( srcGenDef.getOptionalFalseString() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalNullString( srcGenDef.getOptionalNullString() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						dstEditDef.setOptionalTrueString( srcGenDef.getOptionalTrueString() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamBoolTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamBlobTypeObj ) {
					ICFBamBlobTypeTableObj GenTableObj = dst.getSchema().getBlobTypeTableObj();
					ICFBamBlobTypeObj srcGenDef = (ICFBamBlobTypeObj)value;
					ICFBamBlobTypeObj dstOrigDef = GenTableObj.readBlobTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamBlobTypeEditObj dstEditDef = (ICFBamBlobTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredMaxLen( srcGenDef.getRequiredMaxLen() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamBlobTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamId16GenObj ) {
					ICFBamId16GenTableObj GenTableObj = dst.getSchema().getId16GenTableObj();
					ICFBamId16GenObj srcGenDef = (ICFBamId16GenObj)value;
					ICFBamId16GenObj dstOrigGenDef = GenTableObj.readId16GenByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigGenDef == null ) {

						srcDispenser = srcGenDef.getOptionalLookupDispenser();
						if( srcDispenser != null ) {
							dstDispenser = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(),
								dst.getRequiredId(),
								srcDispenser.getRequiredName() );
							if( dstDispenser == null ) {
								throw new CFLibNullArgumentException( getClass(),
									S_ProcName,
									0,
									"dstDispenser " + srcDispenser.getRequiredName() );
							}
						}
						else {
							dstDispenser = null;
						}

						dstOrigGenDef = GenTableObj.newInstance();
						ICFBamId16GenEditObj dstEditGenDef = (ICFBamId16GenEditObj)dstOrigGenDef.beginEdit();
						dstEditGenDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditGenDef.setRequiredContainerSchemaDef( dst );
						dstEditGenDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditGenDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditGenDef.setOptionalLookupDispenser( dstDispenser );
						dstEditGenDef.setRequiredSlice( srcGenDef.getRequiredSlice() );
						dstEditGenDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditGenDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditGenDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditGenDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditGenDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditGenDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditGenDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditGenDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditGenDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigGenDef = (ICFBamId16GenObj)dstEditGenDef.create();
						dstEditGenDef = null;
					}
				}
				else if( value instanceof ICFBamId32GenObj ) {
					ICFBamId32GenTableObj GenTableObj = dst.getSchema().getId32GenTableObj();
					ICFBamId32GenObj srcGenDef = (ICFBamId32GenObj)value;
					ICFBamId32GenObj dstOrigGenDef = GenTableObj.readId32GenByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigGenDef == null ) {

						srcDispenser = srcGenDef.getOptionalLookupDispenser();
						if( srcDispenser != null ) {
							dstDispenser = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(),
								dst.getRequiredId(),
								srcDispenser.getRequiredName() );
							if( dstDispenser == null ) {
								throw new CFLibNullArgumentException( getClass(),
									S_ProcName,
									0,
									"dstDispenser " + srcDispenser.getRequiredName() );
							}
						}
						else {
							dstDispenser = null;
						}

						dstOrigGenDef = GenTableObj.newInstance();
						ICFBamId32GenEditObj dstEditGenDef = (ICFBamId32GenEditObj)dstOrigGenDef.beginEdit();
						dstEditGenDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditGenDef.setRequiredContainerSchemaDef( dst );
						dstEditGenDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditGenDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditGenDef.setOptionalLookupDispenser( dstDispenser );
						dstEditGenDef.setRequiredSlice( srcGenDef.getRequiredSlice() );
						dstEditGenDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditGenDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditGenDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditGenDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditGenDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditGenDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditGenDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditGenDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditGenDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigGenDef = (ICFBamId32GenObj)dstEditGenDef.create();
						dstEditGenDef = null;
					}
				}
				else if( value instanceof ICFBamId64GenObj ) {
					ICFBamId64GenTableObj GenTableObj = dst.getSchema().getId64GenTableObj();
					ICFBamId64GenObj srcGenDef = (ICFBamId64GenObj)value;
					ICFBamId64GenObj dstOrigGenDef = GenTableObj.readId64GenByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigGenDef == null ) {

						srcDispenser = srcGenDef.getOptionalLookupDispenser();
						if( srcDispenser != null ) {
							dstDispenser = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(),
								dst.getRequiredId(),
								srcDispenser.getRequiredName() );
							if( dstDispenser == null ) {
								throw new CFLibNullArgumentException( getClass(),
									S_ProcName,
									0,
									"dstDispenser " + srcDispenser.getRequiredName() );
							}
						}
						else {
							dstDispenser = null;
						}

						dstOrigGenDef = GenTableObj.newInstance();
						ICFBamId64GenEditObj dstEditGenDef = (ICFBamId64GenEditObj)dstOrigGenDef.beginEdit();
						dstEditGenDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditGenDef.setRequiredContainerSchemaDef( dst );
						dstEditGenDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditGenDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditGenDef.setOptionalLookupDispenser( dstDispenser );
						dstEditGenDef.setRequiredSlice( srcGenDef.getRequiredSlice() );
						dstEditGenDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditGenDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditGenDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditGenDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditGenDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditGenDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditGenDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditGenDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditGenDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigGenDef = (ICFBamId64GenObj)dstEditGenDef.create();
						dstEditGenDef = null;
					}
				}
				else if( value instanceof ICFBamUuidGenObj ) {
					ICFBamUuidGenTableObj GenTableObj = dst.getSchema().getUuidGenTableObj();
					ICFBamUuidGenObj srcGenDef = (ICFBamUuidGenObj)value;
					ICFBamUuidGenObj dstOrigGenDef = GenTableObj.readUuidGenByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigGenDef == null ) {

						dstOrigGenDef = GenTableObj.newInstance();
						ICFBamUuidGenEditObj dstEditGenDef = (ICFBamUuidGenEditObj)dstOrigGenDef.beginEdit();
						dstEditGenDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditGenDef.setRequiredContainerSchemaDef( dst );
						dstEditGenDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditGenDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditGenDef.setRequiredSlice( srcGenDef.getRequiredSlice() );
						dstEditGenDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditGenDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditGenDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditGenDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditGenDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditGenDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditGenDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigGenDef = (ICFBamUuidGenObj)dstEditGenDef.create();
						dstEditGenDef = null;
					}
				}
				else if( value instanceof ICFBamEnumTypeObj ) {
					ICFBamEnumTypeTableObj GenTableObj = dst.getSchema().getEnumTypeTableObj();
					ICFBamEnumTagTableObj srcEnumTagTableObj = src.getSchema().getEnumTagTableObj();
					ICFBamEnumTagTableObj dstEnumTagTableObj = dst.getSchema().getEnumTagTableObj();
					ICFBamEnumTypeObj srcGenDef = (ICFBamEnumTypeObj)value;
					ICFBamEnumTypeObj dstOrigDef = GenTableObj.readEnumTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamEnumTypeEditObj dstEditDef = (ICFBamEnumTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamEnumTypeObj)dstEditDef.create();
						dstEditDef = null;

						List<ICFBamEnumTagObj> srcOrigEnumTags = srcEnumTagTableObj.readEnumTagByEnumIdx( srcGenDef.getRequiredTenantId(), srcGenDef.getRequiredId() );
						if( srcOrigEnumTags != null ) {
							ICFBamEnumTagObj srcEnumTag;
							ICFBamEnumTagObj dstOrigEnumTag;
							ICFBamEnumTagEditObj dstEditEnumTag;
							Iterator<ICFBamEnumTagObj> iterSrcEnumTag = srcOrigEnumTags.iterator();
							if( iterSrcEnumTag != null ) {
								while( iterSrcEnumTag.hasNext() ) {
									srcEnumTag = iterSrcEnumTag.next();
									if( srcEnumTag != null ) {
										dstOrigEnumTag = dstEnumTagTableObj.readEnumTagByEnumNameIdx( dstOrigDef.getRequiredTenantId(), dstOrigDef.getRequiredId(), srcEnumTag.getRequiredName() );
										if( dstOrigEnumTag == null ) {
											dstOrigEnumTag = dstEnumTagTableObj.newInstance();
											dstEditEnumTag = (ICFBamEnumTagEditObj)dstOrigEnumTag.beginEdit();
											dstEditEnumTag.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
											dstEditEnumTag.setRequiredContainerEnumDef( dstOrigDef );
											dstEditEnumTag.setRequiredName( srcEnumTag.getRequiredName() );
											dstEditEnumTag.setOptionalEnumCode( srcEnumTag.getOptionalEnumCode() );
											lookupSchema = srcEnumTag.getOptionalLookupDefSchema();
											if( lookupSchema == null ) {
												lookupSchema = src;
											}
											dstEditEnumTag.setOptionalLookupDefSchema( lookupSchema );
											dstOrigEnumTag = (ICFBamEnumTagObj)dstEditEnumTag.create();
											dstEditEnumTag = null;
										}
									}
								}
							}
						}
					}
				}
				else if( value instanceof ICFBamInt16TypeObj ) {
					ICFBamInt16TypeTableObj GenTableObj = dst.getSchema().getInt16TypeTableObj();
					ICFBamInt16TypeObj srcGenDef = (ICFBamInt16TypeObj)value;
					ICFBamInt16TypeObj dstOrigDef = GenTableObj.readInt16TypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamInt16TypeEditObj dstEditDef = (ICFBamInt16TypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamInt16TypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamInt32TypeObj ) {
					ICFBamInt32TypeTableObj GenTableObj = dst.getSchema().getInt32TypeTableObj();
					ICFBamInt32TypeObj srcGenDef = (ICFBamInt32TypeObj)value;
					ICFBamInt32TypeObj dstOrigDef = GenTableObj.readInt32TypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamInt32TypeEditObj dstEditDef = (ICFBamInt32TypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamInt32TypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamInt64TypeObj ) {
					ICFBamInt64TypeTableObj GenTableObj = dst.getSchema().getInt64TypeTableObj();
					ICFBamInt64TypeObj srcGenDef = (ICFBamInt64TypeObj)value;
					ICFBamInt64TypeObj dstOrigDef = GenTableObj.readInt64TypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamInt64TypeEditObj dstEditDef = (ICFBamInt64TypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamInt64TypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamUInt16TypeObj ) {
					ICFBamUInt16TypeTableObj GenTableObj = dst.getSchema().getUInt16TypeTableObj();
					ICFBamUInt16TypeObj srcGenDef = (ICFBamUInt16TypeObj)value;
					ICFBamUInt16TypeObj dstOrigDef = GenTableObj.readUInt16TypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamUInt16TypeEditObj dstEditDef = (ICFBamUInt16TypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamUInt16TypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamUInt32TypeObj ) {
					ICFBamUInt32TypeTableObj GenTableObj = dst.getSchema().getUInt32TypeTableObj();
					ICFBamUInt32TypeObj srcGenDef = (ICFBamUInt32TypeObj)value;
					ICFBamUInt32TypeObj dstOrigDef = GenTableObj.readUInt32TypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamUInt32TypeEditObj dstEditDef = (ICFBamUInt32TypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamUInt32TypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamUInt64TypeObj ) {
					ICFBamUInt64TypeTableObj GenTableObj = dst.getSchema().getUInt64TypeTableObj();
					ICFBamUInt64TypeObj srcGenDef = (ICFBamUInt64TypeObj)value;
					ICFBamUInt64TypeObj dstOrigDef = GenTableObj.readUInt64TypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamUInt64TypeEditObj dstEditDef = (ICFBamUInt64TypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamUInt64TypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamNumberTypeObj ) {
					ICFBamNumberTypeTableObj GenTableObj = dst.getSchema().getNumberTypeTableObj();
					ICFBamNumberTypeObj srcGenDef = (ICFBamNumberTypeObj)value;
					ICFBamNumberTypeObj dstOrigDef = GenTableObj.readNumberTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamNumberTypeEditObj dstEditDef = (ICFBamNumberTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setRequiredDigits( srcGenDef.getRequiredDigits() );
						dstEditDef.setRequiredPrecis( srcGenDef.getRequiredPrecis() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamNumberTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamFloatTypeObj ) {
					ICFBamFloatTypeTableObj GenTableObj = dst.getSchema().getFloatTypeTableObj();
					ICFBamFloatTypeObj srcGenDef = (ICFBamFloatTypeObj)value;
					ICFBamFloatTypeObj dstOrigDef = GenTableObj.readFloatTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamFloatTypeEditObj dstEditDef = (ICFBamFloatTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamFloatTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamDoubleTypeObj ) {
					ICFBamDoubleTypeTableObj GenTableObj = dst.getSchema().getDoubleTypeTableObj();
					ICFBamDoubleTypeObj srcGenDef = (ICFBamDoubleTypeObj)value;
					ICFBamDoubleTypeObj dstOrigDef = GenTableObj.readDoubleTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamDoubleTypeEditObj dstEditDef = (ICFBamDoubleTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamDoubleTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamNmTokensTypeObj ) {
					ICFBamNmTokensTypeTableObj GenTableObj = dst.getSchema().getNmTokensTypeTableObj();
					ICFBamNmTokensTypeObj srcGenDef = (ICFBamNmTokensTypeObj)value;
					ICFBamNmTokensTypeObj dstOrigDef = GenTableObj.readNmTokensTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamNmTokensTypeEditObj dstEditDef = (ICFBamNmTokensTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredMaxLen( srcGenDef.getRequiredMaxLen() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamNmTokensTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamNmTokenTypeObj ) {
					ICFBamNmTokenTypeTableObj GenTableObj = dst.getSchema().getNmTokenTypeTableObj();
					ICFBamNmTokenTypeObj srcGenDef = (ICFBamNmTokenTypeObj)value;
					ICFBamNmTokenTypeObj dstOrigDef = GenTableObj.readNmTokenTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamNmTokenTypeEditObj dstEditDef = (ICFBamNmTokenTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredMaxLen( srcGenDef.getRequiredMaxLen() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamNmTokenTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamStringTypeObj ) {
					ICFBamStringTypeTableObj GenTableObj = dst.getSchema().getStringTypeTableObj();
					ICFBamStringTypeObj srcGenDef = (ICFBamStringTypeObj)value;
					ICFBamStringTypeObj dstOrigDef = GenTableObj.readStringTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamStringTypeEditObj dstEditDef = (ICFBamStringTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredMaxLen( srcGenDef.getRequiredMaxLen() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamStringTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamTextTypeObj ) {
					ICFBamTextTypeTableObj GenTableObj = dst.getSchema().getTextTypeTableObj();
					ICFBamTextTypeObj srcGenDef = (ICFBamTextTypeObj)value;
					ICFBamTextTypeObj dstOrigDef = GenTableObj.readTextTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamTextTypeEditObj dstEditDef = (ICFBamTextTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredMaxLen( srcGenDef.getRequiredMaxLen() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalXmlElementName( srcGenDef.getOptionalXmlElementName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamTextTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamTokenTypeObj ) {
					ICFBamTokenTypeTableObj GenTableObj = dst.getSchema().getTokenTypeTableObj();
					ICFBamTokenTypeObj srcGenDef = (ICFBamTokenTypeObj)value;
					ICFBamTokenTypeObj dstOrigDef = GenTableObj.readTokenTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamTokenTypeEditObj dstEditDef = (ICFBamTokenTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredMaxLen( srcGenDef.getRequiredMaxLen() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamTokenTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamDateTypeObj ) {
					ICFBamDateTypeTableObj GenTableObj = dst.getSchema().getDateTypeTableObj();
					ICFBamDateTypeObj srcGenDef = (ICFBamDateTypeObj)value;
					ICFBamDateTypeObj dstOrigDef = GenTableObj.readDateTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamDateTypeEditObj dstEditDef = (ICFBamDateTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamDateTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamTimeTypeObj ) {
					ICFBamTimeTypeTableObj GenTableObj = dst.getSchema().getTimeTypeTableObj();
					ICFBamTimeTypeObj srcGenDef = (ICFBamTimeTypeObj)value;
					ICFBamTimeTypeObj dstOrigDef = GenTableObj.readTimeTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamTimeTypeEditObj dstEditDef = (ICFBamTimeTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamTimeTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamTimestampTypeObj ) {
					ICFBamTimestampTypeTableObj GenTableObj = dst.getSchema().getTimestampTypeTableObj();
					ICFBamTimestampTypeObj srcGenDef = (ICFBamTimestampTypeObj)value;
					ICFBamTimestampTypeObj dstOrigDef = GenTableObj.readTimestampTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamTimestampTypeEditObj dstEditDef = (ICFBamTimestampTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamTimestampTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamTZDateTypeObj ) {
					ICFBamTZDateTypeTableObj GenTableObj = dst.getSchema().getTZDateTypeTableObj();
					ICFBamTZDateTypeObj srcGenDef = (ICFBamTZDateTypeObj)value;
					ICFBamTZDateTypeObj dstOrigDef = GenTableObj.readTZDateTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamTZDateTypeEditObj dstEditDef = (ICFBamTZDateTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamTZDateTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamTZTimeTypeObj ) {
					ICFBamTZTimeTypeTableObj GenTableObj = dst.getSchema().getTZTimeTypeTableObj();
					ICFBamTZTimeTypeObj srcGenDef = (ICFBamTZTimeTypeObj)value;
					ICFBamTZTimeTypeObj dstOrigDef = GenTableObj.readTZTimeTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamTZTimeTypeEditObj dstEditDef = (ICFBamTZTimeTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamTZTimeTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamTZTimestampTypeObj ) {
					ICFBamTZTimestampTypeTableObj GenTableObj = dst.getSchema().getTZTimestampTypeTableObj();
					ICFBamTZTimestampTypeObj srcGenDef = (ICFBamTZTimestampTypeObj)value;
					ICFBamTZTimestampTypeObj dstOrigDef = GenTableObj.readTZTimestampTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamTZTimestampTypeEditObj dstEditDef = (ICFBamTZTimestampTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamTZTimestampTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else if( value instanceof ICFBamUuidTypeObj ) {
					ICFBamUuidTypeTableObj GenTableObj = dst.getSchema().getUuidTypeTableObj();
					ICFBamUuidTypeObj srcGenDef = (ICFBamUuidTypeObj)value;
					ICFBamUuidTypeObj dstOrigDef = GenTableObj.readUuidTypeByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
					if( dstOrigDef == null ) {
						dstOrigDef = GenTableObj.newInstance();
						ICFBamUuidTypeEditObj dstEditDef = (ICFBamUuidTypeEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerSchemaDef( dst );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamUuidTypeObj)dstEditDef.create();
						dstEditDef = null;
					}
				}
				else {
					throw new CFLibUnsupportedClassException( getClass(),
						S_ProcName,
						"value",
						value,
						"Hard-coded list of schema definition types" );
				}
			}
		}
	}

	protected void importMergeSchemaTables( ICFBamSchemaDefObj dst, ICFBamSchemaDefObj src ) {
		final String S_ProcName = "importMergeSchemaTables";
		ICFBamTableTableObj dstTableTableObj = dst.getSchema().getTableTableObj();
		List<ICFBamTableObj> srcTables = src.getOptionalComponentsTables();
		Iterator<ICFBamTableObj> iterSrcTable = srcTables.iterator();
		ICFBamTableObj srcTable;
		ICFBamTableObj origDstTable;
		ICFBamTableEditObj editDstTable;
		while( iterSrcTable.hasNext() ) {
			srcTable = iterSrcTable.next();
			origDstTable = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(),
				dst.getRequiredId(),
				srcTable.getRequiredName() );
			if( origDstTable == null ) {
				origDstTable = dstTableTableObj.newInstance();
				editDstTable = (ICFBamTableEditObj)origDstTable.beginEdit();
				editDstTable.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
				editDstTable.setRequiredContainerSchemaDef( dst );
				editDstTable.setRequiredHasAuditColumns( srcTable.getRequiredHasAuditColumns() );
				editDstTable.setRequiredHasHistory( srcTable.getRequiredHasHistory() );
				editDstTable.setRequiredIsInstantiable( srcTable.getRequiredIsInstantiable() );
				editDstTable.setRequiredPageData( srcTable.getRequiredPageData() );
				editDstTable.setRequiredIsMutable( srcTable.getRequiredIsMutable() );
				editDstTable.setRequiredIsServerOnly( srcTable.getRequiredIsServerOnly() );
				editDstTable.setOptionalJDb2LUWTableImplementation( srcTable.getOptionalJDb2LUWTableImplementation() );
				editDstTable.setOptionalJDb2LUWTableImport( srcTable.getOptionalJDb2LUWTableImport() );
				editDstTable.setOptionalJDb2LUWTableMembers( srcTable.getOptionalJDb2LUWTableMembers() );
				editDstTable.setOptionalJEditObjImplementation( srcTable.getOptionalJEditObjImplementation() );
				editDstTable.setOptionalJEditObjImport( srcTable.getOptionalJEditObjImport() );
				editDstTable.setOptionalJEditObjInterface( srcTable.getOptionalJEditObjInterface() );
				editDstTable.setOptionalJMSSqlTableImplementation( srcTable.getOptionalJMSSqlTableImplementation() );
				editDstTable.setOptionalJMSSqlTableImport( srcTable.getOptionalJMSSqlTableImport() );
				editDstTable.setOptionalJMSSqlTableMembers( srcTable.getOptionalJMSSqlTableMembers() );
				editDstTable.setOptionalJMySqlTableImplementation( srcTable.getOptionalJMySqlTableImplementation() );
				editDstTable.setOptionalJMySqlTableImport( srcTable.getOptionalJMySqlTableImport() );
				editDstTable.setOptionalJMySqlTableMembers( srcTable.getOptionalJMySqlTableMembers() );
				editDstTable.setOptionalJObjImplementation( srcTable.getOptionalJObjImplementation() );
				editDstTable.setOptionalJObjImport( srcTable.getOptionalJObjImport() );
				editDstTable.setOptionalJObjInterface( srcTable.getOptionalJObjInterface() );
				editDstTable.setOptionalJObjMembers( srcTable.getOptionalJObjMembers() );
				editDstTable.setOptionalJOracleTableImplementation( srcTable.getOptionalJOracleTableImplementation() );
				editDstTable.setOptionalJOracleTableImport( srcTable.getOptionalJOracleTableImport() );
				editDstTable.setOptionalJOracleTableMembers( srcTable.getOptionalJOracleTableMembers() );
				editDstTable.setOptionalJPgSqlTableImplementation( srcTable.getOptionalJPgSqlTableImplementation() );
				editDstTable.setOptionalJPgSqlTableImport( srcTable.getOptionalJPgSqlTableImport() );
				editDstTable.setOptionalJPgSqlTableMembers( srcTable.getOptionalJPgSqlTableMembers() );
				editDstTable.setOptionalJRamTableImplementation( srcTable.getOptionalJRamTableImplementation() );
				editDstTable.setOptionalJRamTableImport( srcTable.getOptionalJRamTableImport() );
				editDstTable.setOptionalJRamTableMembers( srcTable.getOptionalJRamTableMembers() );
				editDstTable.setOptionalJSaxLoaderEndElement( srcTable.getOptionalJSaxLoaderEndElement() );
				editDstTable.setOptionalJSaxLoaderImport( srcTable.getOptionalJSaxLoaderImport() );
				editDstTable.setOptionalJSaxLoaderStartElement( srcTable.getOptionalJSaxLoaderStartElement() );
				editDstTable.setOptionalJTableImplementation( srcTable.getOptionalJTableImplementation() );
				editDstTable.setOptionalJTableImport( srcTable.getOptionalJTableImport() );
				editDstTable.setOptionalJTableInterface( srcTable.getOptionalJTableInterface() );
				editDstTable.setOptionalJTableMembers( srcTable.getOptionalJTableMembers() );
				editDstTable.setOptionalJTableObjImplementation( srcTable.getOptionalJTableObjImplementation() );
				editDstTable.setOptionalJTableObjImport( srcTable.getOptionalJTableObjImport() );
				editDstTable.setOptionalJTableObjInterface( srcTable.getOptionalJTableObjInterface() );
				editDstTable.setOptionalJTableObjMembers( srcTable.getOptionalJTableObjMembers() );
				editDstTable.setOptionalJXMsgClientTableImport( srcTable.getOptionalJXMsgClientTableImport() );
				editDstTable.setOptionalJXMsgClientTableBody( srcTable.getOptionalJXMsgClientTableBody() );
				editDstTable.setOptionalJXMsgRqstTableBody( srcTable.getOptionalJXMsgRqstTableBody() );
				editDstTable.setOptionalJXMsgRqstTableImport( srcTable.getOptionalJXMsgRqstTableImport() );
				editDstTable.setOptionalJXMsgRspnTableBody( srcTable.getOptionalJXMsgRspnTableBody() );
				editDstTable.setOptionalJXMsgRspnTableImport( srcTable.getOptionalJXMsgRspnTableImport() );
				editDstTable.setOptionalJXMsgTableFormatters( srcTable.getOptionalJXMsgTableFormatters() );
				editDstTable.setOptionalJXMsgTableImport( srcTable.getOptionalJXMsgTableImport() );
				editDstTable.setOptionalHppDb2LUWTableImplementation( srcTable.getOptionalHppDb2LUWTableImplementation() );
				editDstTable.setOptionalHppDb2LUWTableInclude( srcTable.getOptionalHppDb2LUWTableInclude() );
				editDstTable.setOptionalHppDb2LUWTableMembers( srcTable.getOptionalHppDb2LUWTableMembers() );
				editDstTable.setOptionalHppEditObjImplementation( srcTable.getOptionalHppEditObjImplementation() );
				editDstTable.setOptionalHppEditObjInclude( srcTable.getOptionalHppEditObjInclude() );
				editDstTable.setOptionalHppEditObjInterface( srcTable.getOptionalHppEditObjInterface() );
				editDstTable.setOptionalHppMSSqlTableImplementation( srcTable.getOptionalHppMSSqlTableImplementation() );
				editDstTable.setOptionalHppMSSqlTableInclude( srcTable.getOptionalHppMSSqlTableInclude() );
				editDstTable.setOptionalHppMSSqlTableMembers( srcTable.getOptionalHppMSSqlTableMembers() );
				editDstTable.setOptionalHppMySqlTableImplementation( srcTable.getOptionalHppMySqlTableImplementation() );
				editDstTable.setOptionalHppMySqlTableInclude( srcTable.getOptionalHppMySqlTableInclude() );
				editDstTable.setOptionalHppMySqlTableMembers( srcTable.getOptionalHppMySqlTableMembers() );
				editDstTable.setOptionalHppObjImplementation( srcTable.getOptionalHppObjImplementation() );
				editDstTable.setOptionalHppObjInclude( srcTable.getOptionalHppObjInclude() );
				editDstTable.setOptionalHppObjInterface( srcTable.getOptionalHppObjInterface() );
				editDstTable.setOptionalHppObjMembers( srcTable.getOptionalHppObjMembers() );
				editDstTable.setOptionalHppOracleTableImplementation( srcTable.getOptionalHppOracleTableImplementation() );
				editDstTable.setOptionalHppOracleTableInclude( srcTable.getOptionalHppOracleTableInclude() );
				editDstTable.setOptionalHppOracleTableMembers( srcTable.getOptionalHppOracleTableMembers() );
				editDstTable.setOptionalHppPgSqlTableImplementation( srcTable.getOptionalHppPgSqlTableImplementation() );
				editDstTable.setOptionalHppPgSqlTableInclude( srcTable.getOptionalHppPgSqlTableInclude() );
				editDstTable.setOptionalHppPgSqlTableMembers( srcTable.getOptionalHppPgSqlTableMembers() );
				editDstTable.setOptionalHppRamTableImplementation( srcTable.getOptionalHppRamTableImplementation() );
				editDstTable.setOptionalHppRamTableInclude( srcTable.getOptionalHppRamTableInclude() );
				editDstTable.setOptionalHppRamTableMembers( srcTable.getOptionalHppRamTableMembers() );
				editDstTable.setOptionalHppSaxLoaderEndElement( srcTable.getOptionalHppSaxLoaderEndElement() );
				editDstTable.setOptionalHppSaxLoaderInclude( srcTable.getOptionalHppSaxLoaderInclude() );
				editDstTable.setOptionalHppSaxLoaderStartElement( srcTable.getOptionalHppSaxLoaderStartElement() );
				editDstTable.setOptionalHppTableImplementation( srcTable.getOptionalHppTableImplementation() );
				editDstTable.setOptionalHppTableInclude( srcTable.getOptionalHppTableInclude() );
				editDstTable.setOptionalHppTableInterface( srcTable.getOptionalHppTableInterface() );
				editDstTable.setOptionalHppTableMembers( srcTable.getOptionalHppTableMembers() );
				editDstTable.setOptionalHppTableObjImplementation( srcTable.getOptionalHppTableObjImplementation() );
				editDstTable.setOptionalHppTableObjInclude( srcTable.getOptionalHppTableObjInclude() );
				editDstTable.setOptionalHppTableObjInterface( srcTable.getOptionalHppTableObjInterface() );
				editDstTable.setOptionalHppTableObjMembers( srcTable.getOptionalHppTableObjMembers() );
				editDstTable.setOptionalHppXMsgClientTableInclude( srcTable.getOptionalHppXMsgClientTableInclude() );
				editDstTable.setOptionalHppXMsgClientTableBody( srcTable.getOptionalHppXMsgClientTableBody() );
				editDstTable.setOptionalHppXMsgRqstTableBody( srcTable.getOptionalHppXMsgRqstTableBody() );
				editDstTable.setOptionalHppXMsgRqstTableInclude( srcTable.getOptionalHppXMsgRqstTableInclude() );
				editDstTable.setOptionalHppXMsgRspnTableBody( srcTable.getOptionalHppXMsgRspnTableBody() );
				editDstTable.setOptionalHppXMsgRspnTableInclude( srcTable.getOptionalHppXMsgRspnTableInclude() );
				editDstTable.setOptionalHppXMsgTableFormatters( srcTable.getOptionalHppXMsgTableFormatters() );
				editDstTable.setOptionalHppXMsgTableInclude( srcTable.getOptionalHppXMsgTableInclude() );
				editDstTable.setOptionalCppDb2LUWTableImplementation( srcTable.getOptionalCppDb2LUWTableImplementation() );
				editDstTable.setOptionalCppDb2LUWTableInclude( srcTable.getOptionalCppDb2LUWTableInclude() );
				editDstTable.setOptionalCppDb2LUWTableMembers( srcTable.getOptionalCppDb2LUWTableMembers() );
				editDstTable.setOptionalCppEditObjImplementation( srcTable.getOptionalCppEditObjImplementation() );
				editDstTable.setOptionalCppEditObjInclude( srcTable.getOptionalCppEditObjInclude() );
				editDstTable.setOptionalCppEditObjInterface( srcTable.getOptionalCppEditObjInterface() );
				editDstTable.setOptionalCppMSSqlTableImplementation( srcTable.getOptionalCppMSSqlTableImplementation() );
				editDstTable.setOptionalCppMSSqlTableInclude( srcTable.getOptionalCppMSSqlTableInclude() );
				editDstTable.setOptionalCppMSSqlTableMembers( srcTable.getOptionalCppMSSqlTableMembers() );
				editDstTable.setOptionalCppMySqlTableImplementation( srcTable.getOptionalCppMySqlTableImplementation() );
				editDstTable.setOptionalCppMySqlTableInclude( srcTable.getOptionalCppMySqlTableInclude() );
				editDstTable.setOptionalCppMySqlTableMembers( srcTable.getOptionalCppMySqlTableMembers() );
				editDstTable.setOptionalCppObjImplementation( srcTable.getOptionalCppObjImplementation() );
				editDstTable.setOptionalCppObjInclude( srcTable.getOptionalCppObjInclude() );
				editDstTable.setOptionalCppObjInterface( srcTable.getOptionalCppObjInterface() );
				editDstTable.setOptionalCppObjMembers( srcTable.getOptionalCppObjMembers() );
				editDstTable.setOptionalCppOracleTableImplementation( srcTable.getOptionalCppOracleTableImplementation() );
				editDstTable.setOptionalCppOracleTableInclude( srcTable.getOptionalCppOracleTableInclude() );
				editDstTable.setOptionalCppOracleTableMembers( srcTable.getOptionalCppOracleTableMembers() );
				editDstTable.setOptionalCppPgSqlTableImplementation( srcTable.getOptionalCppPgSqlTableImplementation() );
				editDstTable.setOptionalCppPgSqlTableInclude( srcTable.getOptionalCppPgSqlTableInclude() );
				editDstTable.setOptionalCppPgSqlTableMembers( srcTable.getOptionalCppPgSqlTableMembers() );
				editDstTable.setOptionalCppRamTableImplementation( srcTable.getOptionalCppRamTableImplementation() );
				editDstTable.setOptionalCppRamTableInclude( srcTable.getOptionalCppRamTableInclude() );
				editDstTable.setOptionalCppRamTableMembers( srcTable.getOptionalCppRamTableMembers() );
				editDstTable.setOptionalCppSaxLoaderEndElement( srcTable.getOptionalCppSaxLoaderEndElement() );
				editDstTable.setOptionalCppSaxLoaderInclude( srcTable.getOptionalCppSaxLoaderInclude() );
				editDstTable.setOptionalCppSaxLoaderStartElement( srcTable.getOptionalCppSaxLoaderStartElement() );
				editDstTable.setOptionalCppTableImplementation( srcTable.getOptionalCppTableImplementation() );
				editDstTable.setOptionalCppTableInclude( srcTable.getOptionalCppTableInclude() );
				editDstTable.setOptionalCppTableInterface( srcTable.getOptionalCppTableInterface() );
				editDstTable.setOptionalCppTableMembers( srcTable.getOptionalCppTableMembers() );
				editDstTable.setOptionalCppTableObjImplementation( srcTable.getOptionalCppTableObjImplementation() );
				editDstTable.setOptionalCppTableObjInclude( srcTable.getOptionalCppTableObjInclude() );
				editDstTable.setOptionalCppTableObjInterface( srcTable.getOptionalCppTableObjInterface() );
				editDstTable.setOptionalCppTableObjMembers( srcTable.getOptionalCppTableObjMembers() );
				editDstTable.setOptionalCppXMsgClientTableInclude( srcTable.getOptionalCppXMsgClientTableInclude() );
				editDstTable.setOptionalCppXMsgClientTableBody( srcTable.getOptionalCppXMsgClientTableBody() );
				editDstTable.setOptionalCppXMsgRqstTableBody( srcTable.getOptionalCppXMsgRqstTableBody() );
				editDstTable.setOptionalCppXMsgRqstTableInclude( srcTable.getOptionalCppXMsgRqstTableInclude() );
				editDstTable.setOptionalCppXMsgRspnTableBody( srcTable.getOptionalCppXMsgRspnTableBody() );
				editDstTable.setOptionalCppXMsgRspnTableInclude( srcTable.getOptionalCppXMsgRspnTableInclude() );
				editDstTable.setOptionalCppXMsgTableFormatters( srcTable.getOptionalCppXMsgTableFormatters() );
				editDstTable.setOptionalCppXMsgTableInclude( srcTable.getOptionalCppXMsgTableInclude() );
				editDstTable.setOptionalCsDb2LUWTableImplementation( srcTable.getOptionalCsDb2LUWTableImplementation() );
				editDstTable.setOptionalCsDb2LUWTableUsing( srcTable.getOptionalCsDb2LUWTableUsing() );
				editDstTable.setOptionalCsDb2LUWTableMembers( srcTable.getOptionalCsDb2LUWTableMembers() );
				editDstTable.setOptionalCsEditObjImplementation( srcTable.getOptionalCsEditObjImplementation() );
				editDstTable.setOptionalCsEditObjUsing( srcTable.getOptionalCsEditObjUsing() );
				editDstTable.setOptionalCsEditObjInterface( srcTable.getOptionalCsEditObjInterface() );
				editDstTable.setOptionalCsMSSqlTableImplementation( srcTable.getOptionalCsMSSqlTableImplementation() );
				editDstTable.setOptionalCsMSSqlTableUsing( srcTable.getOptionalCsMSSqlTableUsing() );
				editDstTable.setOptionalCsMSSqlTableMembers( srcTable.getOptionalCsMSSqlTableMembers() );
				editDstTable.setOptionalCsMySqlTableImplementation( srcTable.getOptionalCsMySqlTableImplementation() );
				editDstTable.setOptionalCsMySqlTableUsing( srcTable.getOptionalCsMySqlTableUsing() );
				editDstTable.setOptionalCsMySqlTableMembers( srcTable.getOptionalCsMySqlTableMembers() );
				editDstTable.setOptionalCsObjImplementation( srcTable.getOptionalCsObjImplementation() );
				editDstTable.setOptionalCsObjUsing( srcTable.getOptionalCsObjUsing() );
				editDstTable.setOptionalCsObjInterface( srcTable.getOptionalCsObjInterface() );
				editDstTable.setOptionalCsObjMembers( srcTable.getOptionalCsObjMembers() );
				editDstTable.setOptionalCsOracleTableImplementation( srcTable.getOptionalCsOracleTableImplementation() );
				editDstTable.setOptionalCsOracleTableUsing( srcTable.getOptionalCsOracleTableUsing() );
				editDstTable.setOptionalCsOracleTableMembers( srcTable.getOptionalCsOracleTableMembers() );
				editDstTable.setOptionalCsPgSqlTableImplementation( srcTable.getOptionalCsPgSqlTableImplementation() );
				editDstTable.setOptionalCsPgSqlTableUsing( srcTable.getOptionalCsPgSqlTableUsing() );
				editDstTable.setOptionalCsPgSqlTableMembers( srcTable.getOptionalCsPgSqlTableMembers() );
				editDstTable.setOptionalCsRamTableImplementation( srcTable.getOptionalCsRamTableImplementation() );
				editDstTable.setOptionalCsRamTableUsing( srcTable.getOptionalCsRamTableUsing() );
				editDstTable.setOptionalCsRamTableMembers( srcTable.getOptionalCsRamTableMembers() );
				editDstTable.setOptionalCsSaxLoaderEndElement( srcTable.getOptionalCsSaxLoaderEndElement() );
				editDstTable.setOptionalCsSaxLoaderUsing( srcTable.getOptionalCsSaxLoaderUsing() );
				editDstTable.setOptionalCsSaxLoaderStartElement( srcTable.getOptionalCsSaxLoaderStartElement() );
				editDstTable.setOptionalCsTableImplementation( srcTable.getOptionalCsTableImplementation() );
				editDstTable.setOptionalCsTableUsing( srcTable.getOptionalCsTableUsing() );
				editDstTable.setOptionalCsTableInterface( srcTable.getOptionalCsTableInterface() );
				editDstTable.setOptionalCsTableMembers( srcTable.getOptionalCsTableMembers() );
				editDstTable.setOptionalCsTableObjImplementation( srcTable.getOptionalCsTableObjImplementation() );
				editDstTable.setOptionalCsTableObjUsing( srcTable.getOptionalCsTableObjUsing() );
				editDstTable.setOptionalCsTableObjInterface( srcTable.getOptionalCsTableObjInterface() );
				editDstTable.setOptionalCsTableObjMembers( srcTable.getOptionalCsTableObjMembers() );
				editDstTable.setOptionalCsXMsgClientTableUsing( srcTable.getOptionalCsXMsgClientTableUsing() );
				editDstTable.setOptionalCsXMsgClientTableBody( srcTable.getOptionalCsXMsgClientTableBody() );
				editDstTable.setOptionalCsXMsgRqstTableBody( srcTable.getOptionalCsXMsgRqstTableBody() );
				editDstTable.setOptionalCsXMsgRqstTableUsing( srcTable.getOptionalCsXMsgRqstTableUsing() );
				editDstTable.setOptionalCsXMsgRspnTableBody( srcTable.getOptionalCsXMsgRspnTableBody() );
				editDstTable.setOptionalCsXMsgRspnTableUsing( srcTable.getOptionalCsXMsgRspnTableUsing() );
				editDstTable.setOptionalCsXMsgTableFormatters( srcTable.getOptionalCsXMsgTableFormatters() );
				editDstTable.setOptionalCsXMsgTableUsing( srcTable.getOptionalCsXMsgTableUsing() );
				editDstTable.setRequiredLoaderBehaviour( srcTable.getRequiredLoaderBehaviour() );
				editDstTable.setRequiredSecScope( srcTable.getRequiredSecScope() );
				editDstTable.setRequiredName( srcTable.getRequiredName() );
				editDstTable.setRequiredTableClassCode( srcTable.getRequiredTableClassCode() );
				editDstTable.setOptionalDbName( srcTable.getOptionalDbName() );
				editDstTable.setOptionalDescription( srcTable.getOptionalDescription() );
				editDstTable.setOptionalLabel( srcTable.getOptionalLabel() );
				editDstTable.setOptionalShortDescription( srcTable.getOptionalShortDescription() );
				editDstTable.setOptionalShortName( srcTable.getOptionalShortName() );
				ICFBamSchemaDefObj lookupSchema = srcTable.getOptionalLookupDefSchema();
				ICFBamTableObj srcQualTable = srcTable.getOptionalLookupQualTable();
				if( srcQualTable == null ) {
					editDstTable.setOptionalLookupQualTable( null );
				}
				else {
					String srcQualTableName = srcQualTable.getRequiredName();
					ICFBamTableObj dstQualTable = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcQualTableName );
					if( dstQualTable == null ) {
						throw new CFLibNullArgumentException( getClass(),
							S_ProcName,
							"Could not resolve destination qualifying table " + srcQualTableName + " referenced by table " + editDstTable.getRequiredName() );
					}
					editDstTable.setOptionalLookupQualTable( dstQualTable );
				}
				if( lookupSchema == null ) {
					lookupSchema = src;
				}
				editDstTable.setOptionalLookupDefSchema( lookupSchema );
				origDstTable = (ICFBamTableObj)editDstTable.create();
				editDstTable = null;

				origDstTable.getSchema().getId16GenTableObj().readId16GenByDispIdx( origDstTable.getRequiredTenantId(),
						origDstTable.getRequiredId() );
				origDstTable.getSchema().getId32GenTableObj().readId32GenByDispIdx( origDstTable.getRequiredTenantId(),
						origDstTable.getRequiredId() );
				origDstTable.getSchema().getId64GenTableObj().readId64GenByDispIdx( origDstTable.getRequiredTenantId(),
						origDstTable.getRequiredId() );
			}
		}
	}

	protected void importMergeSchemaTableColumns( ICFBamSchemaDefObj dst, ICFBamSchemaDefObj src ) {
		final String S_ProcName = "importMergeSchemaTableColumns";
		ICFBamTableTableObj dstTableTableObj = dst.getSchema().getTableTableObj();
		ICFBamValueTableObj dstValueTableObj = dst.getSchema().getValueTableObj();
		List<ICFBamTableObj> srcTables = src.getOptionalComponentsTables();
		Iterator<ICFBamTableObj> iterSrcTable = srcTables.iterator();
		ICFBamTableObj srcTable;
		ICFBamTableObj origDstTable;
		ICFBamValueObj value;
		ICFBamValueObj origDstColumn;
		ICFBamTableObj dstDispenser;
		ICFBamTableObj srcDispenser;
		while( iterSrcTable.hasNext() ) {
			srcTable = iterSrcTable.next();
			origDstTable = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcTable.getRequiredName() );
			if( origDstTable == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"origDstTable " + srcTable.getRequiredName() );
			}

			List<ICFBamValueObj> srcColumns = srcTable.getOptionalComponentsColumns();
			Iterator<ICFBamValueObj> iterSrcColumns = srcColumns.iterator();
			while( iterSrcColumns.hasNext() ) {
				value = iterSrcColumns.next();
				origDstColumn = dstValueTableObj.readValueByUNameIdx( origDstTable.getRequiredTenantId(), origDstTable.getRequiredId(), value.getRequiredName() );
				if( origDstColumn == null ) {
					if( value instanceof ICFBamTableColObj ) {
						ICFBamTableColTableObj GenTableObj = dst.getSchema().getTableColTableObj();
						ICFBamValueTableObj valueTableObj = dst.getSchema().getValueTableObj();
						ICFBamTableColObj srcGenDef = (ICFBamTableColObj)value;
						ICFBamTableColObj dstOrigDef = GenTableObj.readTableColByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
						if( dstOrigDef == null ) {
							ICFBamValueObj srcLookupData = srcGenDef.getRequiredParentDataType();
							ICFBamValueObj dstLookupData = valueTableObj.readValueByUNameIdx( origDstTable.getRequiredContainerSchemaDef().getRequiredTenantId(), origDstTable.getRequiredContainerSchemaDef().getRequiredId(), srcLookupData.getRequiredName() );
							if( dstLookupData == null ) {
								throw new CFLibNullArgumentException( getClass(),
									S_ProcName,
									0,
									"dstLookupData" );
							}

							dstOrigDef = GenTableObj.newInstance();
							ICFBamTableColEditObj dstEditDef = (ICFBamTableColEditObj)dstOrigDef.beginEdit();
							dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
							dstEditDef.setRequiredContainerTable( origDstTable );
							dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
							dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
							dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
							dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
							dstEditDef.setRequiredParentDataType( dstLookupData );
							dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
							dstEditDef.setOptionalXmlElementName( srcGenDef.getOptionalXmlElementName() );
							dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
							dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
							dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
							dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
							dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
							ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
							if( lookupSchema == null ) {
								lookupSchema = src;
							}
							dstEditDef.setOptionalLookupDefSchema( lookupSchema );

							dstOrigDef = (ICFBamTableColObj)dstEditDef.create();
							dstEditDef = null;
						}
					}
					else if( value instanceof ICFBamBoolColObj ) {
						ICFBamBoolColTableObj GenTableObj = dst.getSchema().getBoolColTableObj();
						ICFBamBoolColObj srcGenDef = (ICFBamBoolColObj)value;
						ICFBamBoolColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamBoolColEditObj dstEditDef = (ICFBamBoolColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalFalseString( srcGenDef.getOptionalFalseString() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalNullString( srcGenDef.getOptionalNullString() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						dstEditDef.setOptionalTrueString( srcGenDef.getOptionalTrueString() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamBoolColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamBlobColObj ) {
						ICFBamBlobColTableObj GenTableObj = dst.getSchema().getBlobColTableObj();
						ICFBamBlobColObj srcGenDef = (ICFBamBlobColObj)value;
						ICFBamBlobColObj dstOrigDef = GenTableObj.readBlobColByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcGenDef.getRequiredName() );
						dstOrigDef = GenTableObj.newInstance();
						ICFBamBlobColEditObj dstEditDef = (ICFBamBlobColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredMaxLen( srcGenDef.getRequiredMaxLen() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamBlobColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamInt16ColObj ) {
						ICFBamInt16ColTableObj GenTableObj = dst.getSchema().getInt16ColTableObj();
						ICFBamInt16ColObj srcGenDef = (ICFBamInt16ColObj)value;
						ICFBamInt16ColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamInt16ColEditObj dstEditDef = (ICFBamInt16ColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamInt16ColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamInt32ColObj ) {
						ICFBamInt32ColTableObj GenTableObj = dst.getSchema().getInt32ColTableObj();
						ICFBamInt32ColObj srcGenDef = (ICFBamInt32ColObj)value;
						ICFBamInt32ColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamInt32ColEditObj dstEditDef = (ICFBamInt32ColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamInt32ColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamInt64ColObj ) {
						ICFBamInt64ColTableObj GenTableObj = dst.getSchema().getInt64ColTableObj();
						ICFBamInt64ColObj srcGenDef = (ICFBamInt64ColObj)value;
						ICFBamInt64ColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamInt64ColEditObj dstEditDef = (ICFBamInt64ColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamInt64ColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamUInt16ColObj ) {
						ICFBamUInt16ColTableObj GenTableObj = dst.getSchema().getUInt16ColTableObj();
						ICFBamUInt16ColObj srcGenDef = (ICFBamUInt16ColObj)value;
						ICFBamUInt16ColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamUInt16ColEditObj dstEditDef = (ICFBamUInt16ColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamUInt16ColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamUInt32ColObj ) {
						ICFBamUInt32ColTableObj GenTableObj = dst.getSchema().getUInt32ColTableObj();
						ICFBamUInt32ColObj srcGenDef = (ICFBamUInt32ColObj)value;
						ICFBamUInt32ColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamUInt32ColEditObj dstEditDef = (ICFBamUInt32ColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamUInt32ColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamUInt64ColObj ) {
						ICFBamUInt64ColTableObj GenTableObj = dst.getSchema().getUInt64ColTableObj();
						ICFBamUInt64ColObj srcGenDef = (ICFBamUInt64ColObj)value;
						ICFBamUInt64ColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamUInt64ColEditObj dstEditDef = (ICFBamUInt64ColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamUInt64ColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamNumberColObj ) {
						ICFBamNumberColTableObj GenTableObj = dst.getSchema().getNumberColTableObj();
						ICFBamNumberColObj srcGenDef = (ICFBamNumberColObj)value;
						ICFBamNumberColObj dstOrigDef =  GenTableObj.newInstance();
						ICFBamNumberColEditObj dstEditDef = (ICFBamNumberColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setRequiredDigits( srcGenDef.getRequiredDigits() );
						dstEditDef.setRequiredPrecis( srcGenDef.getRequiredPrecis() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamNumberColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamFloatColObj ) {
						ICFBamFloatColTableObj GenTableObj = dst.getSchema().getFloatColTableObj();
						ICFBamFloatColObj srcGenDef = (ICFBamFloatColObj)value;
						ICFBamFloatColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamFloatColEditObj dstEditDef = (ICFBamFloatColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamFloatColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamDoubleColObj ) {
						ICFBamDoubleColTableObj GenTableObj = dst.getSchema().getDoubleColTableObj();
						ICFBamDoubleColObj srcGenDef = (ICFBamDoubleColObj)value;
						ICFBamDoubleColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamDoubleColEditObj dstEditDef = (ICFBamDoubleColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalMaxValue( srcGenDef.getOptionalMaxValue() );
						dstEditDef.setOptionalMinValue( srcGenDef.getOptionalMinValue() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamDoubleColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamNmTokensColObj ) {
						ICFBamNmTokensColTableObj GenTableObj = dst.getSchema().getNmTokensColTableObj();
						ICFBamNmTokensColObj srcGenDef = (ICFBamNmTokensColObj)value;
						ICFBamNmTokensColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamNmTokensColEditObj dstEditDef = (ICFBamNmTokensColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredMaxLen( srcGenDef.getRequiredMaxLen() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamNmTokensColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamNmTokenColObj ) {
						ICFBamNmTokenColTableObj GenTableObj = dst.getSchema().getNmTokenColTableObj();
						ICFBamNmTokenColObj srcGenDef = (ICFBamNmTokenColObj)value;
						ICFBamNmTokenColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamNmTokenColEditObj dstEditDef = (ICFBamNmTokenColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredMaxLen( srcGenDef.getRequiredMaxLen() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamNmTokenColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamTextColObj ) {
						ICFBamTextColTableObj GenTableObj = dst.getSchema().getTextColTableObj();
						ICFBamTextColObj srcGenDef = (ICFBamTextColObj)value;
						ICFBamTextColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamTextColEditObj dstEditDef = (ICFBamTextColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredMaxLen( srcGenDef.getRequiredMaxLen() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalXmlElementName( srcGenDef.getOptionalXmlElementName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamTextColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamStringColObj ) {
						ICFBamStringColTableObj GenTableObj = dst.getSchema().getStringColTableObj();
						ICFBamStringColObj srcGenDef = (ICFBamStringColObj)value;
						ICFBamStringColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamStringColEditObj dstEditDef = (ICFBamStringColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredMaxLen( srcGenDef.getRequiredMaxLen() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setRequiredIsPolymorph( srcGenDef.getRequiredIsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamStringColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamTokenColObj ) {
						ICFBamTokenColTableObj GenTableObj = dst.getSchema().getTokenColTableObj();
						ICFBamTokenColObj srcGenDef = (ICFBamTokenColObj)value;
						ICFBamTokenColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamTokenColEditObj dstEditDef = (ICFBamTokenColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredMaxLen( srcGenDef.getRequiredMaxLen() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamTokenColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamDateColObj ) {
						ICFBamDateColTableObj GenTableObj = dst.getSchema().getDateColTableObj();
						ICFBamDateColObj srcGenDef = (ICFBamDateColObj)value;
						ICFBamDateColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamDateColEditObj dstEditDef = (ICFBamDateColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamDateColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamTimeColObj ) {
						ICFBamTimeColTableObj GenTableObj = dst.getSchema().getTimeColTableObj();
						ICFBamTimeColObj srcGenDef = (ICFBamTimeColObj)value;
						ICFBamTimeColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamTimeColEditObj dstEditDef = (ICFBamTimeColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamTimeColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamTimestampColObj ) {
						ICFBamTimestampColTableObj GenTableObj = dst.getSchema().getTimestampColTableObj();
						ICFBamTimestampColObj srcGenDef = (ICFBamTimestampColObj)value;
						ICFBamTimestampColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamTimestampColEditObj dstEditDef = (ICFBamTimestampColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamTimestampColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamTZDateColObj ) {
						ICFBamTZDateColTableObj GenTableObj = dst.getSchema().getTZDateColTableObj();
						ICFBamTZDateColObj srcGenDef = (ICFBamTZDateColObj)value;
						ICFBamTZDateColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamTZDateColEditObj dstEditDef = (ICFBamTZDateColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamTZDateColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamTZTimeColObj ) {
						ICFBamTZTimeColTableObj GenTableObj = dst.getSchema().getTZTimeColTableObj();
						ICFBamTZTimeColObj srcGenDef = (ICFBamTZTimeColObj)value;
						ICFBamTZTimeColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamTZTimeColEditObj dstEditDef = (ICFBamTZTimeColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamTZTimeColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamTZTimestampColObj ) {
						ICFBamTZTimestampColTableObj GenTableObj = dst.getSchema().getTZTimestampColTableObj();
						ICFBamTZTimestampColObj srcGenDef = (ICFBamTZTimestampColObj)value;
						ICFBamTZTimestampColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamTZTimestampColEditObj dstEditDef = (ICFBamTZTimestampColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamTZTimestampColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else if( value instanceof ICFBamUuidColObj ) {
						ICFBamUuidColTableObj GenTableObj = dst.getSchema().getUuidColTableObj();
						ICFBamUuidColObj srcGenDef = (ICFBamUuidColObj)value;
						ICFBamUuidColObj dstOrigDef = GenTableObj.newInstance();
						ICFBamUuidColEditObj dstEditDef = (ICFBamUuidColEditObj)dstOrigDef.beginEdit();
						dstEditDef.setRequiredOwnerTenant( dst.getRequiredOwnerTenant() );
						dstEditDef.setRequiredContainerTable( origDstTable );
						dstEditDef.setRequiredIsNullable( srcGenDef.getRequiredIsNullable() );
						dstEditDef.setRequiredName( srcGenDef.getRequiredName() );
						dstEditDef.setOptionalDefaultXmlValue( srcGenDef.getOptionalDefaultXmlValue() );
						dstEditDef.setRequiredImplementsPolymorph( srcGenDef.getRequiredImplementsPolymorph() );
						dstEditDef.setOptionalDbName( srcGenDef.getOptionalDbName() );
						dstEditDef.setOptionalDescription( srcGenDef.getOptionalDescription() );
						dstEditDef.setOptionalGenerateId( srcGenDef.getOptionalGenerateId() );
						dstEditDef.setOptionalInitValue( srcGenDef.getOptionalInitValue() );
						dstEditDef.setOptionalLabel( srcGenDef.getOptionalLabel() );
						dstEditDef.setOptionalShortDescription( srcGenDef.getOptionalShortDescription() );
						dstEditDef.setOptionalShortName( srcGenDef.getOptionalShortName() );
						ICFBamSchemaDefObj lookupSchema = srcGenDef.getOptionalLookupDefSchema();
						if( lookupSchema == null ) {
							lookupSchema = src;
						}
						dstEditDef.setOptionalLookupDefSchema( lookupSchema );
						dstOrigDef = (ICFBamUuidColObj)dstEditDef.create();
						dstEditDef = null;
					}
					else {
						throw new CFLibUnsupportedClassException( getClass(),
							S_ProcName,
							"srcColumn",
							value,
							"Hard-coded list of table column types" );
					}
				}
			}
		}
	}

	public ICFBamIndexObj importMergeIndex( ICFBamTableObj origDstTable, ICFBamIndexObj srcIndex ) {
		final String S_ProcName = "importMergeIndex";
		ICFBamIndexTableObj dstIndexTableObj = origDstTable.getSchema().getIndexTableObj();
		String indexName = srcIndex.getRequiredName();
		ICFBamIndexObj origDstIndex = dstIndexTableObj.readIndexByUNameIdx( origDstTable.getRequiredTenantId(),
			origDstTable.getRequiredId(),
			indexName );
		if( origDstIndex != null ) {
			return( origDstIndex );
		}

		ICFBamSchemaDefObj defSchema = srcIndex.getOptionalLookupDefSchema();
		if( defSchema == null ) {
			defSchema = srcIndex.getRequiredContainerTable().getRequiredContainerSchemaDef();
		}

		origDstIndex = dstIndexTableObj.newInstance();
		ICFBamIndexEditObj editDstIndex = (ICFBamIndexEditObj)origDstIndex.beginEdit();
		editDstIndex.setRequiredOwnerTenant( origDstTable.getRequiredOwnerTenant() );
		editDstIndex.setRequiredContainerTable( origDstTable );
		editDstIndex.setRequiredIsDbMapped( srcIndex.getRequiredIsDbMapped() );
		editDstIndex.setRequiredIsUnique( srcIndex.getRequiredIsUnique() );
		editDstIndex.setRequiredName( srcIndex.getRequiredName() );
		editDstIndex.setOptionalDbName( srcIndex.getOptionalDbName() );
		editDstIndex.setOptionalDescription( srcIndex.getOptionalDescription() );
		editDstIndex.setOptionalLabel( srcIndex.getOptionalLabel() );
		editDstIndex.setOptionalShortDescription( srcIndex.getOptionalShortDescription() );
		editDstIndex.setOptionalShortName( srcIndex.getOptionalShortName() );
		editDstIndex.setOptionalSuffix( srcIndex.getOptionalSuffix() );
		editDstIndex.setOptionalLookupDefSchema( defSchema );
		origDstIndex = (ICFBamIndexObj)editDstIndex.create();
		editDstIndex = null;

		ICFBamTenantObj dstTenant = (ICFBamTenantObj)origDstIndex.getRequiredOwnerTenant();
		ICFBamTableTableObj dstTableTableObj = origDstIndex.getSchema().getTableTableObj();
		ICFBamIndexColTableObj dstIndexColTableObj = origDstIndex.getSchema().getIndexColTableObj();
		ICFBamValueTableObj dstValueTableObj = origDstIndex.getSchema().getValueTableObj();
		ICFBamTableObj srcTable;
		ICFBamIndexObj dstIndex = origDstIndex;

		Iterator<ICFBamIndexColObj> iterSrcColumn;
		List<ICFBamIndexColObj> srcColumns;
		List<ICFBamIndexObj> componentIndexes;
		Iterator<ICFBamIndexObj> iterSrcIndex;
		ICFBamIndexColObj dstColumn;
		ICFBamIndexColObj srcColumn;
		ICFBamIndexColEditObj dstEditColumn;
		ICFBamIndexColObj dstIndexColumn;
		ICFBamValueObj dstLookupColumn;
		ICFBamValueObj srcLookupColumn;

		srcColumns = srcIndex.getOptionalComponentsColumns();
		if( srcColumns != null ) {
			iterSrcColumn = srcColumns.iterator();
			if( iterSrcColumn == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					srcIndex.getRequiredName() + ".srcColumns.iterator()" );
			}

			while( iterSrcColumn.hasNext() ) {
				srcColumn = iterSrcColumn.next();
				srcLookupColumn = srcColumn.getRequiredLookupColumn();
				dstLookupColumn = dstValueTableObj.readValueByUNameIdx( origDstTable.getRequiredTenantId(),
					origDstTable.getRequiredId(),
					srcColumn.getRequiredName() );
				if( dstLookupColumn == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"dstLookupColumn( " + srcColumn.getRequiredName() + " )" );
				}

				dstColumn = dstIndexColTableObj.newInstance();
				dstEditColumn = (ICFBamIndexColEditObj)dstColumn.beginEdit();
				dstEditColumn.setRequiredOwnerTenant( dstTenant );
				dstEditColumn.setRequiredContainerIndex( origDstIndex );
				dstEditColumn.setRequiredLookupColumn( dstLookupColumn );
				dstEditColumn.setRequiredName( srcColumn.getRequiredName() );
				dstEditColumn.setOptionalDescription( srcColumn.getOptionalDescription() );
				dstEditColumn.setOptionalLabel( srcColumn.getOptionalLabel() );
				dstEditColumn.setOptionalShortDescription( srcColumn.getOptionalShortDescription() );
				dstEditColumn.setOptionalShortName( srcColumn.getOptionalShortName() );
				dstEditColumn.setOptionalLookupDefSchema( defSchema );
				if( null == dstIndexColTableObj.readIndexColByUNameIdx( origDstIndex.getRequiredTenantId(),
					origDstIndex.getRequiredId(),
					srcColumn.getRequiredName() ) )
				{
					dstColumn = (ICFBamIndexColObj)dstEditColumn.create();
				}
				dstEditColumn = null;
			}
		}

		return( origDstIndex );
	}

	protected void importMergeSchemaTableIndexes( ICFBamSchemaDefObj dst, ICFBamSchemaDefObj src ) {
		final String S_ProcName = "importMergeSchemaTableIndexes";
		ICFBamTableTableObj dstTableTableObj = dst.getSchema().getTableTableObj();
		List<ICFBamTableObj> srcTables = src.getOptionalComponentsTables();
		Iterator<ICFBamTableObj> iterSrcTable = srcTables.iterator();
		ICFBamTableObj srcTable;
		ICFBamTableObj origDstTable;
		ICFBamIndexObj dstIndex;
		ICFBamIndexObj srcIndex;
		List<ICFBamIndexObj> componentIndexes;
		Iterator<ICFBamIndexObj> iterSrcIndex;

		while( iterSrcTable.hasNext() ) {
			srcTable = iterSrcTable.next();
			origDstTable = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcTable.getRequiredName() );
			if( origDstTable == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"origDstTable " + srcTable.getRequiredName() );
			}

			componentIndexes = srcTable.getOptionalComponentsIndex();
			if( componentIndexes != null ) {
				iterSrcIndex = componentIndexes.iterator();
				while( iterSrcIndex.hasNext() ) {
					srcIndex = iterSrcIndex.next();
					dstIndex = importMergeIndex( origDstTable, srcIndex );
				}
			}
		}
	}

	public ICFBamServerMethodObj importMergeServerMethod( ICFBamTableObj origDstTable, ICFBamServerMethodObj srcServerMethod ) {
		final String S_ProcName = "importMergeServerMethod";
		ICFBamSchemaDefObj origDstSchemaDef = origDstTable.getRequiredContainerSchemaDef();
		ICFBamTableTableObj dstTableTableObj = origDstTable.getSchema().getTableTableObj();
		ICFBamServerMethodTableObj dstServerMethodTableObj = origDstTable.getSchema().getServerMethodTableObj();
		ICFBamServerProcTableObj dstServerProcTableObj = origDstTable.getSchema().getServerProcTableObj();
		ICFBamServerObjFuncTableObj dstServerObjFuncTableObj = origDstTable.getSchema().getServerObjFuncTableObj();
		ICFBamServerListFuncTableObj dstServerListFuncTableObj = origDstTable.getSchema().getServerListFuncTableObj();
		String serverMethodName = srcServerMethod.getRequiredName();
		ICFBamServerMethodObj origDstServerMethod = dstServerMethodTableObj.readServerMethodByUNameIdx( origDstTable.getRequiredTenantId(),
			origDstTable.getRequiredId(),
			serverMethodName );
		if( origDstServerMethod != null ) {
			return( origDstServerMethod );
		}

		ICFBamSchemaDefObj defSchema = srcServerMethod.getOptionalLookupDefSchema();
		if( defSchema == null ) {
			defSchema = srcServerMethod.getRequiredContainerForTable().getRequiredContainerSchemaDef();
		}

		if( srcServerMethod instanceof ICFBamServerProcObj ) {
			origDstServerMethod = dstServerProcTableObj.newInstance();
		}
		else if( srcServerMethod instanceof ICFBamServerObjFuncObj ) {
			origDstServerMethod = dstServerObjFuncTableObj.newInstance();
		}
		else if( srcServerMethod instanceof ICFBamServerListFuncObj ) {
			origDstServerMethod = dstServerListFuncTableObj.newInstance();
		}
		else {
			throw new CFLibUnsupportedClassException( getClass(),
				S_ProcName,
				"srcServerMethod",
				srcServerMethod,
				"ICFBamServerProcObj, ICFBamServerObjFuncObj, ICFBamServerListFuncObj" );
		}

		ICFBamServerMethodEditObj editDstServerMethod = (ICFBamServerMethodEditObj)origDstServerMethod.beginEdit();
		editDstServerMethod.setRequiredOwnerTenant( origDstTable.getRequiredOwnerTenant() );
		editDstServerMethod.setRequiredContainerForTable( origDstTable );
		editDstServerMethod.setRequiredIsInstanceMethod( srcServerMethod.getRequiredIsInstanceMethod() );
		editDstServerMethod.setRequiredName( srcServerMethod.getRequiredName() );
		editDstServerMethod.setRequiredIsServerOnly( srcServerMethod.getRequiredIsServerOnly() );
		editDstServerMethod.setRequiredJMethodBody( srcServerMethod.getRequiredJMethodBody() );
		editDstServerMethod.setRequiredCppMethodBody( srcServerMethod.getRequiredCppMethodBody() );
		editDstServerMethod.setRequiredCsMethodBody( srcServerMethod.getRequiredCsMethodBody() );
		editDstServerMethod.setOptionalDescription( srcServerMethod.getOptionalDescription() );
		editDstServerMethod.setOptionalLabel( srcServerMethod.getOptionalLabel() );
		editDstServerMethod.setOptionalShortDescription( srcServerMethod.getOptionalShortDescription() );
		editDstServerMethod.setOptionalShortName( srcServerMethod.getOptionalShortName() );
		editDstServerMethod.setOptionalSuffix( srcServerMethod.getOptionalSuffix() );
		editDstServerMethod.setOptionalLookupDefSchema( defSchema );
		editDstServerMethod.setOptionalLookupDefSchema( defSchema );
		if( srcServerMethod instanceof ICFBamServerObjFuncObj ) {
			ICFBamTableObj srcRetTable = ((ICFBamServerObjFuncObj)srcServerMethod).getOptionalLookupRetTable();
			ICFBamTableObj dstRetTable = dstTableTableObj.readTableByUNameIdx( origDstTable.getRequiredTenantId(),
				origDstTable.getRequiredSchemaDefId(),
				srcRetTable.getObjName() );
			if( dstRetTable == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"dstRetTable" );
			}
			((ICFBamServerObjFuncEditObj)editDstServerMethod).setOptionalLookupRetTable( dstRetTable );
		}
		else if( srcServerMethod instanceof ICFBamServerListFuncObj ) {
			ICFBamTableObj srcRetTable = ((ICFBamServerListFuncObj)srcServerMethod).getOptionalLookupRetTable();
			ICFBamTableObj dstRetTable = dstTableTableObj.readTableByUNameIdx( origDstTable.getRequiredTenantId(),
				origDstTable.getRequiredSchemaDefId(),
				srcRetTable.getObjName() );
			if( dstRetTable == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"dstRetTable" );
			}
			((ICFBamServerListFuncEditObj)editDstServerMethod).setOptionalLookupRetTable( dstRetTable );
		}
		origDstServerMethod = (ICFBamServerMethodObj)editDstServerMethod.create();
		editDstServerMethod = null;

		ICFBamTenantObj dstTenant = (ICFBamTenantObj)origDstServerMethod.getRequiredOwnerTenant();
		ICFBamParamTableObj dstParamTableObj = origDstServerMethod.getSchema().getParamTableObj();
		ICFBamValueTableObj dstValueTableObj = origDstServerMethod.getSchema().getValueTableObj();

		Iterator<ICFBamParamObj> iterSrcParam;
		List<ICFBamParamObj> srcParams;
		ICFBamParamObj dstParam;
		ICFBamParamObj srcParam;
		ICFBamParamEditObj dstEditParam;
		ICFBamValueObj dstLookupType;
		ICFBamValueObj srcLookupType;

		srcParams = srcServerMethod.getOptionalComponentsParams();
		if( srcParams != null ) {
			iterSrcParam = srcParams.iterator();
			if( iterSrcParam == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					srcServerMethod.getRequiredName() + ".srcParams.iterator()" );
			}

			while( iterSrcParam.hasNext() ) {
				srcParam = iterSrcParam.next();
				srcLookupType = srcParam.getRequiredLookupType();
				dstLookupType = dstValueTableObj.readValueByUNameIdx( origDstTable.getRequiredTenantId(),
					origDstSchemaDef.getRequiredId(),
					srcLookupType.getRequiredName() );
				if( dstLookupType == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"dstLookupType( " + srcLookupType.getRequiredName() + " )" );
				}

				dstParam = dstParamTableObj.newInstance();
				dstEditParam = (ICFBamParamEditObj)dstParam.beginEdit();
				dstEditParam.setRequiredOwnerTenant( dstTenant );
				dstEditParam.setRequiredContainerServerMeth( origDstServerMethod );
				dstEditParam.setRequiredLookupType( dstLookupType );
				dstEditParam.setRequiredName( srcParam.getRequiredName() );
				dstEditParam.setOptionalDescription( srcParam.getOptionalDescription() );
				dstEditParam.setOptionalShortDescription( srcParam.getOptionalShortDescription() );
				dstEditParam.setOptionalLookupDefSchema( defSchema );
				if( null == dstParamTableObj.readParamByUNameIdx( origDstServerMethod.getRequiredTenantId(),
					origDstServerMethod.getRequiredId(),
					srcParam.getRequiredName() ) )
				{
					dstParam = (ICFBamParamObj)dstEditParam.create();
				}
				dstEditParam = null;
			}
		}

		return( origDstServerMethod );
	}

	protected void importMergeSchemaTableServerMethods( ICFBamSchemaDefObj dst, ICFBamSchemaDefObj src ) {
		final String S_ProcName = "importMergeSchemaTableServerMethods";
		ICFBamTableTableObj dstTableTableObj = dst.getSchema().getTableTableObj();
		List<ICFBamTableObj> srcTables = src.getOptionalComponentsTables();
		Iterator<ICFBamTableObj> iterSrcTable = srcTables.iterator();
		ICFBamTableObj srcTable;
		ICFBamTableObj origDstTable;
		ICFBamServerMethodObj dstServerMethod;
		ICFBamServerMethodObj srcServerMethod;
		List<ICFBamServerMethodObj> componentServerMethods;
		Iterator<ICFBamServerMethodObj> iterSrcServerMethod;

		while( iterSrcTable.hasNext() ) {
			srcTable = iterSrcTable.next();
			origDstTable = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcTable.getRequiredName() );
			if( origDstTable == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"origDstTable " + srcTable.getRequiredName() );
			}

			componentServerMethods = srcTable.getOptionalComponentsServerMethods();
			if( componentServerMethods != null ) {
				iterSrcServerMethod = componentServerMethods.iterator();
				while( iterSrcServerMethod.hasNext() ) {
					srcServerMethod = iterSrcServerMethod.next();
					dstServerMethod = importMergeServerMethod( origDstTable, srcServerMethod );
				}
			}
		}
	}

	protected void importMergeSchemaTableResolvePrimaryIndexes( ICFBamSchemaDefObj dst, ICFBamSchemaDefObj src ) {
		final String S_ProcName = "importMergeSchemaTableResolvePrimaryIndexes";
		ICFBamTableTableObj dstTableTableObj = dst.getSchema().getTableTableObj();
		ICFBamIndexTableObj dstIndexTableObj = dst.getSchema().getIndexTableObj();
		List<ICFBamTableObj> srcTables = src.getOptionalComponentsTables();
		Iterator<ICFBamTableObj> iterSrcTable = srcTables.iterator();
		ICFBamTableObj srcTable;
		ICFBamTableObj origDstTable;
		ICFBamIndexObj dstPrimaryIndex;
		ICFBamIndexObj srcPrimaryIndex;
		ICFBamTableEditObj editDstTable;
		while( iterSrcTable.hasNext() ) {
			srcTable = iterSrcTable.next();
			origDstTable = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcTable.getRequiredName() );
			if( origDstTable == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"origDstTable " + srcTable.getRequiredName() );
			}

			dstPrimaryIndex = origDstTable.getOptionalLookupPrimaryIndex();
			if( dstPrimaryIndex == null ) {
				srcPrimaryIndex = srcTable.getOptionalLookupPrimaryIndex();
				if( srcPrimaryIndex != null ) {
					dstPrimaryIndex = dstIndexTableObj.readIndexByUNameIdx( origDstTable.getRequiredTenantId(), origDstTable.getRequiredId(),
						srcPrimaryIndex.getRequiredName() );
					if( dstPrimaryIndex == null ) {
						throw new CFLibNullArgumentException( getClass(),
							S_ProcName,
							0,
							"dstPrimaryIndex " + srcPrimaryIndex.getRequiredName() );
					}

					editDstTable = (ICFBamTableEditObj)origDstTable.beginEdit();
					editDstTable.setOptionalLookupPrimaryIndex( dstPrimaryIndex );
					editDstTable.update();
					editDstTable = null;
				}
			}
		}
	}

	protected void importMergeSchemaTableResolveLookupIndexes( ICFBamSchemaDefObj dst, ICFBamSchemaDefObj src ) {
		final String S_ProcName = "importMergeSchemaTableResolveLookupIndexes";
		ICFBamTableTableObj dstTableTableObj = dst.getSchema().getTableTableObj();
		ICFBamIndexTableObj dstIndexTableObj = dst.getSchema().getIndexTableObj();
		List<ICFBamTableObj> srcTables = src.getOptionalComponentsTables();
		Iterator<ICFBamTableObj> iterSrcTable = srcTables.iterator();
		ICFBamTableObj srcTable;
		ICFBamTableObj origDstTable;
		ICFBamIndexObj dstLookupIndex;
		ICFBamIndexObj srcLookupIndex;
		ICFBamTableEditObj editDstTable;
		while( iterSrcTable.hasNext() ) {
			srcTable = iterSrcTable.next();
			origDstTable = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcTable.getRequiredName() );
			if( origDstTable == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"origDstTable " + srcTable.getRequiredName() );
			}

			dstLookupIndex = origDstTable.getOptionalLookupLookupIndex();
			if( dstLookupIndex == null ) {
				srcLookupIndex = srcTable.getOptionalLookupLookupIndex();
				if( srcLookupIndex != null ) {
					dstLookupIndex = dstIndexTableObj.readIndexByUNameIdx( origDstTable.getRequiredTenantId(),
						origDstTable.getRequiredId(),
						srcLookupIndex.getRequiredName() );
					if( dstLookupIndex == null ) {
						throw new CFLibNullArgumentException( getClass(),
							S_ProcName,
							0,
							"dstLookupIndex " + srcLookupIndex.getRequiredName() );
					}

					editDstTable = (ICFBamTableEditObj)origDstTable.beginEdit();
					editDstTable.setOptionalLookupLookupIndex( dstLookupIndex );
					editDstTable.update();
					editDstTable = null;
				}
			}
		}
	}

	public ICFBamRelationObj importMergeRelation( ICFBamTableObj origDstTable, ICFBamRelationObj srcRelation ) {
		final String S_ProcName = "importMergeRelation";
		ICFBamRelationTableObj dstRelationTableObj = origDstTable.getSchema().getRelationTableObj();
		String indexName = srcRelation.getRequiredName();
		ICFBamRelationObj origDstRelation = dstRelationTableObj.readRelationByUNameIdx( origDstTable.getRequiredTenantId(),
			origDstTable.getRequiredId(),
			indexName );
		if( origDstRelation != null ) {
			return( origDstRelation );
		}

		ICFBamSchemaDefObj dstSchemaDef = origDstTable.getRequiredContainerSchemaDef();
		ICFBamSchemaDefObj defSchema = srcRelation.getOptionalLookupDefSchema();
		if( defSchema == null ) {
			defSchema = srcRelation.getRequiredContainerFromTable().getRequiredContainerSchemaDef();
		}

		ICFBamTableTableObj dstTableTableObj = origDstTable.getSchema().getTableTableObj();
		ICFBamIndexTableObj dstIndexTableObj = origDstTable.getSchema().getIndexTableObj();
		ICFBamTableObj srcRelationToTable;
		ICFBamIndexObj srcRelationToIndex;
		ICFBamTableObj dstRelationToTable;
		ICFBamIndexObj dstRelationToIndex;
		ICFBamTableObj srcRelationFromTable;
		ICFBamIndexObj srcRelationFromIndex;
		ICFBamTableObj dstRelationFromTable;
		ICFBamIndexObj dstRelationFromIndex;
		ICFBamTenantObj dstTenant = (ICFBamTenantObj)origDstTable.getRequiredOwnerTenant();

		srcRelationFromTable = srcRelation.getRequiredContainerFromTable();
		dstRelationFromTable = dstTableTableObj.readTableByUNameIdx( dstSchemaDef.getRequiredTenantId(),
			dstSchemaDef.getRequiredId(),
			srcRelationFromTable.getRequiredName() );
		if( dstRelationFromTable == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"dstTableTableObj.readFromTable( " + srcRelationFromTable.getRequiredName() + " )" );
		}

		srcRelationToTable = srcRelation.getRequiredLookupToTable();
		dstRelationToTable = dstTableTableObj.readTableByUNameIdx( dstSchemaDef.getRequiredTenantId(),
			dstSchemaDef.getRequiredId(),
			srcRelationToTable.getRequiredName() );
		if( dstRelationToTable == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"dstTableTableObj.readToTable( " + srcRelationToTable.getRequiredName() + " )" );
		}

		srcRelationFromIndex = srcRelation.getRequiredLookupFromIndex();
		dstRelationFromIndex = dstIndexTableObj.readIndexByUNameIdx( dstRelationFromTable.getRequiredTenantId(),
			dstRelationFromTable.getRequiredId(),
			srcRelationFromIndex.getRequiredName() );
		if( dstRelationFromIndex == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"dstIndexTableObj.readFromIndex( " + srcRelationFromIndex.getRequiredName() + " )" );
		}

		srcRelationToIndex = srcRelation.getRequiredLookupToIndex();
		dstRelationToIndex = dstIndexTableObj.readIndexByUNameIdx( dstRelationToTable.getRequiredTenantId(),
			dstRelationToTable.getRequiredId(),
			srcRelationToIndex.getRequiredName() );
		if( dstRelationToIndex == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"dstIndexTableObj.readToIndex( " + srcRelationToIndex.getRequiredName() + " )" );
		}

		origDstRelation = dstRelationTableObj.newInstance();
		ICFBamRelationEditObj editDstRelation = (ICFBamRelationEditObj)origDstRelation.beginEdit();
		editDstRelation.setRequiredOwnerTenant( dstTenant );
		editDstRelation.setRequiredContainerFromTable( origDstTable );
		editDstRelation.setRequiredLookupFromIndex( dstRelationFromIndex );
		editDstRelation.setRequiredLookupToTable( dstRelationToTable );
		editDstRelation.setRequiredLookupToIndex( dstRelationToIndex );
		editDstRelation.setRequiredRelationType( srcRelation.getRequiredRelationType() );
		editDstRelation.setRequiredName( srcRelation.getRequiredName() );
		editDstRelation.setRequiredAllowAddendum( srcRelation.getRequiredAllowAddendum() );
		editDstRelation.setRequiredIsRequired( srcRelation.getRequiredIsRequired() );
		editDstRelation.setRequiredIsXsdContainer( srcRelation.getRequiredIsXsdContainer() );
		editDstRelation.setRequiredIsLateResolver( srcRelation.getRequiredIsLateResolver() );
		editDstRelation.setOptionalDbName( srcRelation.getOptionalDbName() );
		editDstRelation.setOptionalDescription( srcRelation.getOptionalDescription() );
		editDstRelation.setOptionalLabel( srcRelation.getOptionalLabel() );
		editDstRelation.setOptionalShortDescription( srcRelation.getOptionalShortDescription() );
		editDstRelation.setOptionalShortName( srcRelation.getOptionalShortName() );
		editDstRelation.setOptionalSuffix( srcRelation.getOptionalSuffix() );
		editDstRelation.setOptionalLookupDefSchema( defSchema );
		origDstRelation = (ICFBamRelationObj)editDstRelation.create();
		editDstRelation = null;

		ICFBamIndexColTableObj dstIndexColTableObj = origDstRelation.getSchema().getIndexColTableObj();
		ICFBamRelationColTableObj dstRelationColTableObj = origDstRelation.getSchema().getRelationColTableObj();
		ICFBamRelationObj dstRelation = origDstRelation;

		Iterator<ICFBamRelationColObj> iterSrcColumn;
		List<ICFBamRelationColObj> srcColumns;
		ICFBamRelationColObj dstColumn;
		ICFBamRelationColObj srcColumn;
		ICFBamRelationColEditObj dstEditColumn;
		ICFBamRelationColObj dstLookupFromCol;
		ICFBamIndexColObj srcLookupFromCol;
		ICFBamIndexColObj srcLookupToCol;
		ICFBamIndexColObj dstLookupFromIndexCol;
		ICFBamIndexColObj dstLookupToIndexCol;

		srcColumns = srcRelation.getOptionalComponentsColumns();
		if( srcColumns != null ) {
			iterSrcColumn = srcColumns.iterator();
			if( iterSrcColumn == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					srcRelation.getRequiredName() + ".srcColumns.iterator()" );
			}

			while( iterSrcColumn.hasNext() ) {
				srcColumn = iterSrcColumn.next();
				dstColumn = dstRelationColTableObj.readRelationColByUNameIdx( dstRelation.getRequiredTenantId(),
					dstRelation.getRequiredId(),
					srcColumn.getRequiredName() );
				if( dstColumn == null ) {
					srcLookupFromCol = srcColumn.getRequiredLookupFromCol();
					dstLookupFromCol = dstRelationColTableObj.readRelationColByUNameIdx( origDstRelation.getRequiredTenantId(),
						origDstRelation.getRequiredId(),
						srcColumn.getRequiredName() );

					if( dstLookupFromCol == null ) {
						srcLookupToCol = srcColumn.getRequiredLookupToCol();

						dstLookupToIndexCol = dstIndexColTableObj.readIndexColByUNameIdx( dstRelationToIndex.getRequiredTenantId(),
							dstRelationToIndex.getRequiredId(),
							srcLookupToCol.getRequiredName() );
						if( dstLookupToIndexCol == null ) {
							throw new CFLibNullArgumentException( getClass(),
								S_ProcName,
								0,
								"dstLookupToIndexColumn( " + srcLookupToCol.getRequiredName() + " )" );
						}

						dstLookupFromIndexCol = dstIndexColTableObj.readIndexColByUNameIdx( dstRelationFromIndex.getRequiredTenantId(),
							dstRelationFromIndex.getRequiredId(),
							srcLookupFromCol.getRequiredName() );
						if( dstLookupFromIndexCol == null ) {
							throw new CFLibNullArgumentException( getClass(),
								S_ProcName,
								0,
								"dstLookupFromIndexColumn( " + srcLookupFromCol.getRequiredName() + " )" );
						}

						dstColumn = dstRelationColTableObj.newInstance();
						dstEditColumn = (ICFBamRelationColEditObj)dstColumn.beginEdit();
						dstEditColumn.setRequiredOwnerTenant( dstTenant );
						dstEditColumn.setRequiredContainerRelation( origDstRelation );
						dstEditColumn.setRequiredLookupFromCol( dstLookupFromIndexCol );
						dstEditColumn.setRequiredLookupToCol( dstLookupToIndexCol );
						dstEditColumn.setRequiredName( srcColumn.getRequiredName() );
						dstEditColumn.setOptionalDescription( srcColumn.getOptionalDescription() );
						dstEditColumn.setOptionalLabel( srcColumn.getOptionalLabel() );
						dstEditColumn.setOptionalShortDescription( srcColumn.getOptionalShortDescription() );
						dstEditColumn.setOptionalShortName( srcColumn.getOptionalShortName() );
						dstEditColumn.setOptionalLookupDefSchema( defSchema );
						if( null == dstRelationColTableObj.readRelationColByUNameIdx( origDstRelation.getRequiredTenantId(),
							origDstRelation.getRequiredId(),
							srcColumn.getRequiredName() ) )
						{
							dstColumn = (ICFBamRelationColObj)dstEditColumn.create();
						}
						dstEditColumn = null;
					}
				}
			}
		}

		return( origDstRelation );
	}

	protected void importMergeSchemaTableRelations( ICFBamSchemaDefObj dst, ICFBamSchemaDefObj src ) {
		final String S_ProcName = "importMergeSchemaTableRelations";
		ICFBamTableTableObj dstTableTableObj = dst.getSchema().getTableTableObj();
		List<ICFBamTableObj> srcTables = src.getOptionalComponentsTables();
		Iterator<ICFBamTableObj> iterSrcTable = srcTables.iterator();
		ICFBamTableObj srcTable;
		ICFBamTableObj origDstTable;
		ICFBamRelationObj dstRelation;
		ICFBamRelationObj srcRelation;
		List<ICFBamRelationObj> componentRelations;
		Iterator<ICFBamRelationObj> iterSrcRelation;

		while( iterSrcTable.hasNext() ) {
			srcTable = iterSrcTable.next();
			origDstTable = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcTable.getRequiredName() );
			if( origDstTable == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"origDstTable " + srcTable.getRequiredName() );
			}

			componentRelations = srcTable.getOptionalComponentsRelation();
			if( componentRelations != null ) {
				iterSrcRelation = componentRelations.iterator();
				while( iterSrcRelation.hasNext() ) {
					srcRelation = iterSrcRelation.next();
					dstRelation = importMergeRelation( origDstTable, srcRelation );
				}
			}
		}
	}

	protected void importMergeRelationPopDeps( ICFBamTableObj origDstTable, ICFBamRelationObj srcRelation ) {
		final String S_ProcName = "importMergeRelationPopDeps";
		ICFBamSchemaDefObj srcSchema = srcRelation.getOptionalLookupDefSchema();
		if( srcSchema == null ) {
			srcSchema = srcRelation.getSchemaDef();
		}
		ICFBamRelationTableObj dstRelationTableObj = origDstTable.getSchema().getRelationTableObj();
		String relationName = srcRelation.getRequiredName();
		ICFBamRelationObj origDstRelation = dstRelationTableObj.readRelationByUNameIdx( origDstTable.getRequiredTenantId(),
			origDstTable.getRequiredId(),
			relationName );

		ICFBamSchemaDefObj dstSchemaDef = origDstTable.getRequiredContainerSchemaDef();
		ICFBamSchemaDefObj defSchema = srcRelation.getOptionalLookupDefSchema();
		if( defSchema == null ) {
			defSchema = srcRelation.getRequiredContainerFromTable().getRequiredContainerSchemaDef();
		}

		ICFBamTableTableObj dstTableTableObj = origDstTable.getSchema().getTableTableObj();
		ICFBamPopTopDepTableObj dstPopTopDepTableObj = origDstTable.getSchema().getPopTopDepTableObj();
		ICFBamPopSubDep1TableObj dstPopSubDep1TableObj = origDstTable.getSchema().getPopSubDep1TableObj();
		ICFBamPopSubDep2TableObj dstPopSubDep2TableObj = origDstTable.getSchema().getPopSubDep2TableObj();
		ICFBamPopSubDep3TableObj dstPopSubDep3TableObj = origDstTable.getSchema().getPopSubDep3TableObj();
		ICFBamPopTopDepObj srcPopTopDep = null;
		ICFBamPopSubDep1Obj srcPopSubDep1 = null;
		ICFBamPopSubDep2Obj srcPopSubDep2 = null;
		ICFBamPopSubDep3Obj srcPopSubDep3 = null;
		ICFBamTenantObj dstTenant = (ICFBamTenantObj)origDstTable.getRequiredOwnerTenant();
		ICFBamTableObj srcTable;
		ICFBamTableObj dstTable;
		ICFBamRelationObj dstRelation;

		Iterator<ICFBamPopTopDepObj> iterSrcPopTopDep;
		List<ICFBamPopTopDepObj> srcPopTopDepMap = srcRelation.getOptionalComponentsPopDep();
		if( srcPopTopDepMap != null ) {
			iterSrcPopTopDep = srcPopTopDepMap.iterator();
			if( iterSrcPopTopDep.hasNext() ) {
				srcPopTopDep = iterSrcPopTopDep.next();
				List<ICFBamPopSubDep1Obj> srcPopSubDep1Map = srcPopTopDep.getOptionalComponentsPopDep();
				Iterator<ICFBamPopSubDep1Obj> iterSrcPopSubDep1 = srcPopSubDep1Map.iterator();
				if( iterSrcPopSubDep1.hasNext() ) {
					srcPopSubDep1 = iterSrcPopSubDep1.next();
					List<ICFBamPopSubDep2Obj> srcPopSubDep2Map = srcPopSubDep1.getOptionalComponentsPopDep();
					Iterator<ICFBamPopSubDep2Obj> iterSrcPopSubDep2 = srcPopSubDep2Map.iterator();
					if( iterSrcPopSubDep2.hasNext() ) {
						srcPopSubDep2 = iterSrcPopSubDep2.next();
						List<ICFBamPopSubDep3Obj> srcPopSubDep3Map = srcPopSubDep2.getOptionalComponentsPopDep();
						Iterator<ICFBamPopSubDep3Obj> iterSrcPopSubDep3 = srcPopSubDep3Map.iterator();
						if( iterSrcPopSubDep3.hasNext() ) {
							srcPopSubDep3 = iterSrcPopSubDep3.next();
						}
					}
				}
			}
		}

		if( srcPopTopDep != null ) {
			ICFBamPopTopDepObj origDstPopTopDep = dstPopTopDepTableObj.readPopTopDepByUNameIdx( origDstRelation.getRequiredTenantId(),
				origDstRelation.getRequiredId(),
				srcPopTopDep.getRequiredName() );
			if( origDstPopTopDep == null ) {

				srcRelation = srcPopTopDep.getRequiredLookupRelation();
				srcTable = srcRelation.getRequiredContainerFromTable();
				dstTable = dstTableTableObj.readTableByUNameIdx( dstSchemaDef.getRequiredTenantId(),
					dstSchemaDef.getRequiredId(),
					srcTable.getRequiredName() );
				dstRelation = dstRelationTableObj.readRelationByUNameIdx( dstTable.getRequiredTenantId(),
					dstTable.getRequiredId(),
					srcRelation.getRequiredName() );

				origDstPopTopDep = dstPopTopDepTableObj.newInstance();
				ICFBamPopTopDepEditObj editDstPopTopDep = (ICFBamPopTopDepEditObj)origDstPopTopDep.beginEdit();
				editDstPopTopDep.setRequiredOwnerTenant( dstTenant );
				editDstPopTopDep.setRequiredContainerContRelation( origDstRelation );
				editDstPopTopDep.setRequiredLookupRelation( dstRelation );
				editDstPopTopDep.setRequiredName( srcPopTopDep.getRequiredName() );
				editDstPopTopDep.setOptionalLookupDefSchema( srcSchema );
				origDstPopTopDep = (ICFBamPopTopDepObj)editDstPopTopDep.create();
				editDstPopTopDep = null;

				if( srcPopSubDep1 != null ) {
					srcRelation = srcPopSubDep1.getRequiredLookupRelation();
					srcTable = srcRelation.getRequiredContainerFromTable();
					dstTable = dstTableTableObj.readTableByUNameIdx( dstSchemaDef.getRequiredTenantId(),
						dstSchemaDef.getRequiredId(),
						srcTable.getRequiredName() );
					dstRelation = dstRelationTableObj.readRelationByUNameIdx( dstTable.getRequiredTenantId(),
						dstTable.getRequiredId(),
						srcRelation.getRequiredName() );

					ICFBamPopSubDep1Obj origDstPopSubDep1 = dstPopSubDep1TableObj.newInstance();
					ICFBamPopSubDep1EditObj editDstPopSubDep1 = (ICFBamPopSubDep1EditObj)origDstPopSubDep1.beginEdit();
					editDstPopSubDep1.setRequiredOwnerTenant( dstTenant );
					editDstPopSubDep1.setRequiredContainerContPopTopDep( origDstPopTopDep );
					editDstPopSubDep1.setRequiredLookupRelation( dstRelation );
					editDstPopSubDep1.setRequiredName( srcPopSubDep1.getRequiredName() );
					editDstPopSubDep1.setOptionalLookupDefSchema( srcSchema );
					origDstPopSubDep1 = (ICFBamPopSubDep1Obj)editDstPopSubDep1.create();
					editDstPopSubDep1 = null;

					if( srcPopSubDep2 != null ) {
						srcRelation = srcPopSubDep2.getRequiredLookupRelation();
						srcTable = srcRelation.getRequiredContainerFromTable();
						dstTable = dstTableTableObj.readTableByUNameIdx( dstSchemaDef.getRequiredTenantId(),
							dstSchemaDef.getRequiredId(),
							srcTable.getRequiredName() );
						dstRelation = dstRelationTableObj.readRelationByUNameIdx( dstTable.getRequiredTenantId(),
							dstTable.getRequiredId(),
							srcRelation.getRequiredName() );

						ICFBamPopSubDep2Obj origDstPopSubDep2 = dstPopSubDep2TableObj.newInstance();
						ICFBamPopSubDep2EditObj editDstPopSubDep2 = (ICFBamPopSubDep2EditObj)origDstPopSubDep2.beginEdit();
						editDstPopSubDep2.setRequiredOwnerTenant( dstTenant );
						editDstPopSubDep2.setRequiredContainerPopSubDep1( origDstPopSubDep1 );
						editDstPopSubDep2.setRequiredLookupRelation( dstRelation );
						editDstPopSubDep2.setRequiredName( srcPopSubDep2.getRequiredName() );
						editDstPopSubDep2.setOptionalLookupDefSchema( srcSchema );
						origDstPopSubDep2 = (ICFBamPopSubDep2Obj)editDstPopSubDep2.create();
						editDstPopSubDep2 = null;

						if( srcPopSubDep3 != null ) {
							srcRelation = srcPopSubDep3.getRequiredLookupRelation();
							srcTable = srcRelation.getRequiredContainerFromTable();
							dstTable = dstTableTableObj.readTableByUNameIdx( dstSchemaDef.getRequiredTenantId(),
								dstSchemaDef.getRequiredId(),
								srcTable.getRequiredName() );
							dstRelation = dstRelationTableObj.readRelationByUNameIdx( dstTable.getRequiredTenantId(),
								dstTable.getRequiredId(),
								srcRelation.getRequiredName() );

							ICFBamPopSubDep3Obj origDstPopSubDep3 = dstPopSubDep3TableObj.newInstance();
							ICFBamPopSubDep3EditObj editDstPopSubDep3 = (ICFBamPopSubDep3EditObj)origDstPopSubDep3.beginEdit();
							editDstPopSubDep3.setRequiredOwnerTenant( dstTenant );
							editDstPopSubDep3.setRequiredContainerPopSubDep2( origDstPopSubDep2 );
							editDstPopSubDep3.setRequiredLookupRelation( dstRelation );
							editDstPopSubDep3.setRequiredName( srcPopSubDep3.getRequiredName() );
							editDstPopSubDep3.setOptionalLookupDefSchema( srcSchema );
							origDstPopSubDep3 = (ICFBamPopSubDep3Obj)editDstPopSubDep3.create();
							editDstPopSubDep3 = null;
						}
					}
				}
			}
		}
	}

	protected void importMergeSchemaTableRelationPopDeps( ICFBamSchemaDefObj dst, ICFBamSchemaDefObj src ) {
		final String S_ProcName = "importMergeSchemaTableRelationPopDeps";
		ICFBamTableTableObj dstTableTableObj = dst.getSchema().getTableTableObj();
		List<ICFBamTableObj> srcTables = src.getOptionalComponentsTables();
		Iterator<ICFBamTableObj> iterSrcTable = srcTables.iterator();
		ICFBamTableObj srcTable;
		ICFBamTableObj origDstTable;
		ICFBamRelationObj srcRelation;
		List<ICFBamRelationObj> componentRelations;
		Iterator<ICFBamRelationObj> iterSrcRelation;

		while( iterSrcTable.hasNext() ) {
			srcTable = iterSrcTable.next();
			origDstTable = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcTable.getRequiredName() );
			if( origDstTable == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"origDstTable " + srcTable.getRequiredName() );
			}

			componentRelations = srcTable.getOptionalComponentsRelation();
			if( componentRelations != null ) {
				iterSrcRelation = componentRelations.iterator();
				while( iterSrcRelation.hasNext() ) {
					srcRelation = iterSrcRelation.next();
					importMergeRelationPopDeps( origDstTable, srcRelation );
				}
			}
		}
	}

	protected void importMergeTableDelDeps( ICFBamTableObj origDstTable, ICFBamTableObj srcTable ) {
		final String S_ProcName = "importMergeTableDelDeps";
		ICFBamRelationTableObj dstRelationTableObj = origDstTable.getSchema().getRelationTableObj();
		String indexName = srcTable.getRequiredName();

		ICFBamSchemaDefObj dstSchemaDef = origDstTable.getRequiredContainerSchemaDef();
		ICFBamSchemaDefObj defSchema = srcTable.getRequiredContainerSchemaDef();

		ICFBamTableTableObj dstTableTableObj = origDstTable.getSchema().getTableTableObj();
		ICFBamDelTopDepTableObj dstDelTopDepTableObj = origDstTable.getSchema().getDelTopDepTableObj();
		ICFBamDelSubDep1TableObj dstDelSubDep1TableObj = origDstTable.getSchema().getDelSubDep1TableObj();
		ICFBamDelSubDep2TableObj dstDelSubDep2TableObj = origDstTable.getSchema().getDelSubDep2TableObj();
		ICFBamDelSubDep3TableObj dstDelSubDep3TableObj = origDstTable.getSchema().getDelSubDep3TableObj();
		ICFBamDelTopDepObj srcDelTopDep = null;
		ICFBamDelSubDep1Obj srcDelSubDep1 = null;
		ICFBamDelSubDep2Obj srcDelSubDep2 = null;
		ICFBamDelSubDep3Obj srcDelSubDep3 = null;
		List<ICFBamDelSubDep1Obj> srcDelSubDep1Map;
		Iterator<ICFBamDelSubDep1Obj> iterSrcDelSubDep1;
		List<ICFBamDelSubDep2Obj> srcDelSubDep2Map;
		Iterator<ICFBamDelSubDep2Obj> iterSrcDelSubDep2;
		List<ICFBamDelSubDep3Obj> srcDelSubDep3Map;
		Iterator<ICFBamDelSubDep3Obj> iterSrcDelSubDep3;
		ICFBamRelationObj srcRelation;
		ICFBamTableObj dstTable;
		ICFBamRelationObj dstRelation;
		ICFBamDelTopDepObj origDstDelTopDep;
		ICFBamDelTopDepEditObj editDstDelTopDep;
		ICFBamDelSubDep1Obj origDstDelSubDep1;
		ICFBamDelSubDep1EditObj editDstDelSubDep1;
		ICFBamDelSubDep2Obj origDstDelSubDep2;
		ICFBamDelSubDep2EditObj editDstDelSubDep2;
		ICFBamDelSubDep3Obj origDstDelSubDep3;
		ICFBamDelSubDep3EditObj editDstDelSubDep3;
		ICFBamTenantObj dstTenant = (ICFBamTenantObj)origDstTable.getRequiredOwnerTenant();

		Iterator<ICFBamDelTopDepObj> iterSrcDelTopDep;
		List<ICFBamDelTopDepObj> srcDelTopDepMap = srcTable.getOptionalComponentsDelDep();
		if( srcDelTopDepMap != null ) {
			iterSrcDelTopDep = srcDelTopDepMap.iterator();
			while( iterSrcDelTopDep.hasNext() ) {

				srcDelTopDep = iterSrcDelTopDep.next();
				srcDelSubDep1 = null;
				srcDelSubDep2 = null;
				srcDelSubDep3 = null;

				ICFBamSchemaDefObj srcSchema = srcDelTopDep.getOptionalLookupDefSchema();
				if( srcSchema == null ) {
					srcSchema = srcDelTopDep.getRequiredContainerTable().getRequiredContainerSchemaDef();
				}

				srcDelSubDep1Map = srcDelTopDep.getOptionalComponentsDelDep();
				iterSrcDelSubDep1 = srcDelSubDep1Map.iterator();
				if( iterSrcDelSubDep1.hasNext() ) {
					srcDelSubDep1 = iterSrcDelSubDep1.next();
					srcDelSubDep2Map = srcDelSubDep1.getOptionalComponentsDelDep();
					iterSrcDelSubDep2 = srcDelSubDep2Map.iterator();
					if( iterSrcDelSubDep2.hasNext() ) {
						srcDelSubDep2 = iterSrcDelSubDep2.next();
						srcDelSubDep3Map = srcDelSubDep2.getOptionalComponentsDelDep();
						iterSrcDelSubDep3 = srcDelSubDep3Map.iterator();
						if( iterSrcDelSubDep3.hasNext() ) {
							srcDelSubDep3 = iterSrcDelSubDep3.next();
						}
					}
				}

				origDstDelTopDep = dstDelTopDepTableObj.readDelTopDepByUNameIdx( origDstTable.getRequiredTenantId(),
					origDstTable.getRequiredId(),
					srcDelTopDep.getRequiredName() );
				if( origDstDelTopDep == null ) {
					origDstDelTopDep = dstDelTopDepTableObj.newInstance();
					editDstDelTopDep = (ICFBamDelTopDepEditObj)origDstDelTopDep.beginEdit();

					srcRelation = srcDelTopDep.getRequiredLookupRelation();
					srcTable = srcRelation.getRequiredContainerFromTable();
					dstTable = dstTableTableObj.readTableByUNameIdx( origDstTable.getRequiredContainerSchemaDef().getRequiredTenantId(),
						origDstTable.getRequiredContainerSchemaDef().getRequiredId(),
						srcTable.getRequiredName() );
					dstRelation = dstRelationTableObj.readRelationByUNameIdx( dstTable.getRequiredTenantId(),
						dstTable.getRequiredId(),
						srcRelation.getRequiredName() );
					editDstDelTopDep.setRequiredOwnerTenant( dstTenant );
					editDstDelTopDep.setRequiredContainerTable( origDstTable );
					editDstDelTopDep.setRequiredLookupRelation( dstRelation );
					editDstDelTopDep.setRequiredName( srcDelTopDep.getRequiredName() );
					editDstDelTopDep.setOptionalLookupDefSchema( srcSchema );
					origDstDelTopDep = (ICFBamDelTopDepObj)editDstDelTopDep.create();
					editDstDelTopDep = null;

					if( srcDelSubDep1 != null ) {
						srcRelation = srcDelSubDep1.getRequiredLookupRelation();
						srcTable = srcRelation.getRequiredContainerFromTable();
						dstTable = dstTableTableObj.readTableByUNameIdx( origDstTable.getRequiredContainerSchemaDef().getRequiredTenantId(),
							origDstTable.getRequiredContainerSchemaDef().getRequiredId(),
							srcTable.getRequiredName() );
						dstRelation = dstRelationTableObj.readRelationByUNameIdx( dstTable.getRequiredTenantId(),
							dstTable.getRequiredId(),
							srcRelation.getRequiredName() );

						origDstDelSubDep1 = dstDelSubDep1TableObj.newInstance();
						editDstDelSubDep1 = (ICFBamDelSubDep1EditObj)origDstDelSubDep1.beginEdit();
						editDstDelSubDep1.setRequiredOwnerTenant( dstTenant );
						editDstDelSubDep1.setRequiredContainerDelTopDep( origDstDelTopDep );
						editDstDelSubDep1.setRequiredLookupRelation( dstRelation );
						editDstDelSubDep1.setRequiredName( srcDelSubDep1.getRequiredName() );
						editDstDelSubDep1.setOptionalLookupDefSchema( srcSchema );
						origDstDelSubDep1 = (ICFBamDelSubDep1Obj)editDstDelSubDep1.create();
						editDstDelSubDep1 = null;

						if( srcDelSubDep2 != null ) {

							srcRelation = srcDelSubDep2.getRequiredLookupRelation();
							srcTable = srcRelation.getRequiredContainerFromTable();
							dstTable = dstTableTableObj.readTableByUNameIdx( origDstTable.getRequiredContainerSchemaDef().getRequiredTenantId(),
								origDstTable.getRequiredContainerSchemaDef().getRequiredId(),
								srcTable.getRequiredName() );
							dstRelation = dstRelationTableObj.readRelationByUNameIdx( dstTable.getRequiredTenantId(),
								dstTable.getRequiredId(),
								srcRelation.getRequiredName() );

							origDstDelSubDep2 = dstDelSubDep2TableObj.newInstance();
							editDstDelSubDep2 = (ICFBamDelSubDep2EditObj)origDstDelSubDep2.beginEdit();
							editDstDelSubDep2.setRequiredOwnerTenant( dstTenant );
							editDstDelSubDep2.setRequiredContainerDelSubDep1( origDstDelSubDep1 );
							editDstDelSubDep2.setRequiredLookupRelation( dstRelation );
							editDstDelSubDep2.setRequiredName( srcDelSubDep2.getRequiredName() );
							editDstDelSubDep2.setOptionalLookupDefSchema( srcSchema );
							origDstDelSubDep2 = (ICFBamDelSubDep2Obj)editDstDelSubDep2.create();
							editDstDelSubDep2 = null;

							if( srcDelSubDep3 != null ) {
								srcRelation = srcDelTopDep.getRequiredLookupRelation();
								srcTable = srcRelation.getRequiredContainerFromTable();
								dstTable = dstTableTableObj.readTableByUNameIdx( origDstTable.getRequiredContainerSchemaDef().getRequiredTenantId(),
									origDstTable.getRequiredContainerSchemaDef().getRequiredId(),
									srcTable.getRequiredName() );
								dstRelation = dstRelationTableObj.readRelationByUNameIdx( dstTable.getRequiredTenantId(),
									dstTable.getRequiredId(),
									srcRelation.getRequiredName() );
								origDstDelSubDep3 = dstDelSubDep3TableObj.newInstance();
								editDstDelSubDep3 = (ICFBamDelSubDep3EditObj)origDstDelSubDep3.beginEdit();
								editDstDelSubDep3.setRequiredOwnerTenant( dstTenant );
								editDstDelSubDep3.setRequiredContainerDelSubDep2( origDstDelSubDep2 );
								editDstDelSubDep3.setRequiredLookupRelation( dstRelation );
								editDstDelSubDep3.setRequiredName( srcDelSubDep3.getRequiredName() );
								editDstDelSubDep3.setOptionalLookupDefSchema( srcSchema );
								origDstDelSubDep3 = (ICFBamDelSubDep3Obj)editDstDelSubDep3.create();
								editDstDelSubDep3 = null;
							}
						}
					}
				}
			}
		}
	}

	protected void importMergeSchemaTableTableDelDeps( ICFBamSchemaDefObj dst, ICFBamSchemaDefObj src ) {
		final String S_ProcName = "importMergeSchemaTableTableDelDeps";
		ICFBamTableTableObj dstTableTableObj = dst.getSchema().getTableTableObj();
		List<ICFBamTableObj> srcTables = src.getOptionalComponentsTables();
		Iterator<ICFBamTableObj> iterSrcTable = srcTables.iterator();
		ICFBamTableObj srcTable;
		ICFBamTableObj origDstTable;
		while( iterSrcTable.hasNext() ) {
			srcTable = iterSrcTable.next();
			origDstTable = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcTable.getRequiredName() );
			if( origDstTable == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"origDstTable " + srcTable.getRequiredName() );
			}
			importMergeTableDelDeps( origDstTable, srcTable );
		}
	}

	protected void importMergeTableClearDeps( ICFBamTableObj origDstTable, ICFBamTableObj srcTable ) {
		final String S_ProcName = "importMergeTableClearDeps";
		ICFBamRelationTableObj dstRelationTableObj = origDstTable.getSchema().getRelationTableObj();
		String indexName = srcTable.getRequiredName();

		ICFBamSchemaDefObj dstSchemaDef = origDstTable.getRequiredContainerSchemaDef();
		ICFBamSchemaDefObj defSchema = srcTable.getRequiredContainerSchemaDef();

		ICFBamTableTableObj dstTableTableObj = origDstTable.getSchema().getTableTableObj();
		ICFBamClearTopDepTableObj dstClearTopDepTableObj = origDstTable.getSchema().getClearTopDepTableObj();
		ICFBamClearSubDep1TableObj dstClearSubDep1TableObj = origDstTable.getSchema().getClearSubDep1TableObj();
		ICFBamClearSubDep2TableObj dstClearSubDep2TableObj = origDstTable.getSchema().getClearSubDep2TableObj();
		ICFBamClearSubDep3TableObj dstClearSubDep3TableObj = origDstTable.getSchema().getClearSubDep3TableObj();
		ICFBamClearTopDepObj srcClearTopDep = null;
		ICFBamClearSubDep1Obj srcClearSubDep1 = null;
		ICFBamClearSubDep2Obj srcClearSubDep2 = null;
		ICFBamClearSubDep3Obj srcClearSubDep3 = null;
		List<ICFBamClearSubDep1Obj> srcClearSubDep1Map;
		Iterator<ICFBamClearSubDep1Obj> iterSrcClearSubDep1;
		List<ICFBamClearSubDep2Obj> srcClearSubDep2Map;
		Iterator<ICFBamClearSubDep2Obj> iterSrcClearSubDep2;
		List<ICFBamClearSubDep3Obj> srcClearSubDep3Map;
		Iterator<ICFBamClearSubDep3Obj> iterSrcClearSubDep3;
		ICFBamRelationObj srcRelation;
		ICFBamTableObj dstTable;
		ICFBamRelationObj dstRelation;
		ICFBamClearTopDepObj origDstClearTopDep;
		ICFBamClearTopDepEditObj editDstClearTopDep;
		ICFBamClearSubDep1Obj origDstClearSubDep1;
		ICFBamClearSubDep1EditObj editDstClearSubDep1;
		ICFBamClearSubDep2Obj origDstClearSubDep2;
		ICFBamClearSubDep2EditObj editDstClearSubDep2;
		ICFBamClearSubDep3Obj origDstClearSubDep3;
		ICFBamClearSubDep3EditObj editDstClearSubDep3;
		ICFBamTenantObj dstTenant = (ICFBamTenantObj)origDstTable.getRequiredOwnerTenant();

		Iterator<ICFBamClearTopDepObj> iterSrcClearTopDep;
		List<ICFBamClearTopDepObj> srcClearTopDepMap = srcTable.getOptionalComponentsClearDep();
		if( srcClearTopDepMap != null ) {
			iterSrcClearTopDep = srcClearTopDepMap.iterator();
			while( iterSrcClearTopDep.hasNext() ) {

				srcClearTopDep = iterSrcClearTopDep.next();
				srcClearSubDep1 = null;
				srcClearSubDep2 = null;
				srcClearSubDep3 = null;

				srcClearSubDep1Map = srcClearTopDep.getOptionalComponentsClearDep();
				iterSrcClearSubDep1 = srcClearSubDep1Map.iterator();
				if( iterSrcClearSubDep1.hasNext() ) {
					srcClearSubDep1 = iterSrcClearSubDep1.next();
					srcClearSubDep2Map = srcClearSubDep1.getOptionalComponentsClearDep();
					iterSrcClearSubDep2 = srcClearSubDep2Map.iterator();
					if( iterSrcClearSubDep2.hasNext() ) {
						srcClearSubDep2 = iterSrcClearSubDep2.next();
						srcClearSubDep3Map = srcClearSubDep2.getOptionalComponentsClearDep();
						iterSrcClearSubDep3 = srcClearSubDep3Map.iterator();
						if( iterSrcClearSubDep3.hasNext() ) {
							srcClearSubDep3 = iterSrcClearSubDep3.next();
						}
					}
				}

				ICFBamSchemaDefObj srcSchema = srcClearTopDep.getOptionalLookupDefSchema();
				if( srcSchema == null ) {
					srcSchema = srcClearTopDep.getRequiredContainerTable().getRequiredContainerSchemaDef();
				}

				origDstClearTopDep = dstClearTopDepTableObj.readClearTopDepByUNameIdx( origDstTable.getRequiredTenantId(),
					origDstTable.getRequiredId(),
					srcClearTopDep.getRequiredName() );
				if( origDstClearTopDep == null ) {
					origDstClearTopDep = dstClearTopDepTableObj.newInstance();
					editDstClearTopDep = (ICFBamClearTopDepEditObj)origDstClearTopDep.beginEdit();

					srcRelation = srcClearTopDep.getRequiredLookupRelation();
					srcTable = srcRelation.getRequiredContainerFromTable();
					dstTable = dstTableTableObj.readTableByUNameIdx( origDstTable.getRequiredContainerSchemaDef().getRequiredTenantId(),
						origDstTable.getRequiredContainerSchemaDef().getRequiredId(),
						srcTable.getRequiredName() );
					dstRelation = dstRelationTableObj.readRelationByUNameIdx( dstTable.getRequiredTenantId(),
						dstTable.getRequiredId(),
						srcRelation.getRequiredName() );
					editDstClearTopDep.setRequiredOwnerTenant( dstTenant );
					editDstClearTopDep.setRequiredContainerTable( origDstTable );
					editDstClearTopDep.setRequiredLookupRelation( dstRelation );
					editDstClearTopDep.setRequiredName( srcClearTopDep.getRequiredName() );
					editDstClearTopDep.setOptionalLookupDefSchema( srcSchema );
					origDstClearTopDep = (ICFBamClearTopDepObj)editDstClearTopDep.create();
					editDstClearTopDep = null;

					if( srcClearSubDep1 != null ) {
						srcRelation = srcClearSubDep1.getRequiredLookupRelation();
						srcTable = srcRelation.getRequiredContainerFromTable();
						dstTable = dstTableTableObj.readTableByUNameIdx( origDstTable.getRequiredContainerSchemaDef().getRequiredTenantId(),
							origDstTable.getRequiredContainerSchemaDef().getRequiredId(),
							srcTable.getRequiredName() );
						dstRelation = dstRelationTableObj.readRelationByUNameIdx( dstTable.getRequiredTenantId(),
							dstTable.getRequiredId(),
							srcRelation.getRequiredName() );

						origDstClearSubDep1 = dstClearSubDep1TableObj.newInstance();
						editDstClearSubDep1 = (ICFBamClearSubDep1EditObj)origDstClearSubDep1.beginEdit();
						editDstClearSubDep1.setRequiredOwnerTenant( dstTenant );
						editDstClearSubDep1.setRequiredContainerClearTopDep( origDstClearTopDep );
						editDstClearSubDep1.setRequiredLookupRelation( dstRelation );
						editDstClearSubDep1.setRequiredName( srcClearSubDep1.getRequiredName() );
						editDstClearSubDep1.setOptionalLookupDefSchema( srcSchema );
						origDstClearSubDep1 = (ICFBamClearSubDep1Obj)editDstClearSubDep1.create();
						editDstClearSubDep1 = null;

						if( srcClearSubDep2 != null ) {

							srcRelation = srcClearSubDep2.getRequiredLookupRelation();
							srcTable = srcRelation.getRequiredContainerFromTable();
							dstTable = dstTableTableObj.readTableByUNameIdx( origDstTable.getRequiredContainerSchemaDef().getRequiredTenantId(),
								origDstTable.getRequiredContainerSchemaDef().getRequiredId(),
								srcTable.getRequiredName() );
							dstRelation = dstRelationTableObj.readRelationByUNameIdx( dstTable.getRequiredTenantId(),
								dstTable.getRequiredId(),
								srcRelation.getRequiredName() );

							origDstClearSubDep2 = dstClearSubDep2TableObj.newInstance();
							editDstClearSubDep2 = (ICFBamClearSubDep2EditObj)origDstClearSubDep2.beginEdit();
							editDstClearSubDep2.setRequiredOwnerTenant( dstTenant );
							editDstClearSubDep2.setRequiredContainerClearSubDep1( origDstClearSubDep1 );
							editDstClearSubDep2.setRequiredLookupRelation( dstRelation );
							editDstClearSubDep2.setRequiredName( srcClearSubDep2.getRequiredName() );
							editDstClearSubDep2.setOptionalLookupDefSchema( srcSchema );
							origDstClearSubDep2 = (ICFBamClearSubDep2Obj)editDstClearSubDep2.create();
							editDstClearSubDep2 = null;

							if( srcClearSubDep3 != null ) {
								srcRelation = srcClearTopDep.getRequiredLookupRelation();
								srcTable = srcRelation.getRequiredContainerFromTable();
								dstTable = dstTableTableObj.readTableByUNameIdx( origDstTable.getRequiredContainerSchemaDef().getRequiredTenantId(),
									origDstTable.getRequiredContainerSchemaDef().getRequiredId(),
									srcTable.getRequiredName() );
								dstRelation = dstRelationTableObj.readRelationByUNameIdx( dstTable.getRequiredTenantId(),
									dstTable.getRequiredId(),
									srcRelation.getRequiredName() );
								origDstClearSubDep3 = dstClearSubDep3TableObj.newInstance();
								editDstClearSubDep3 = (ICFBamClearSubDep3EditObj)origDstClearSubDep3.beginEdit();
								editDstClearSubDep3.setRequiredOwnerTenant( dstTenant );
								editDstClearSubDep3.setRequiredContainerClearSubDep2( origDstClearSubDep2 );
								editDstClearSubDep3.setRequiredLookupRelation( dstRelation );
								editDstClearSubDep3.setRequiredName( srcClearSubDep3.getRequiredName() );
								editDstClearSubDep3.setOptionalLookupDefSchema( srcSchema );
								origDstClearSubDep3 = (ICFBamClearSubDep3Obj)editDstClearSubDep3.create();
								editDstClearSubDep3 = null;
							}
						}
					}
				}
			}
		}
	}

	protected void importMergeSchemaTableTableClearDeps( ICFBamSchemaDefObj dst, ICFBamSchemaDefObj src ) {
		final String S_ProcName = "importMergeSchemaTableTableClearDeps";
		ICFBamTableTableObj dstTableTableObj = dst.getSchema().getTableTableObj();
		List<ICFBamTableObj> srcTables = src.getOptionalComponentsTables();
		Iterator<ICFBamTableObj> iterSrcTable = srcTables.iterator();
		ICFBamTableObj srcTable;
		ICFBamTableObj origDstTable;
		while( iterSrcTable.hasNext() ) {
			srcTable = iterSrcTable.next();
			origDstTable = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcTable.getRequiredName() );
			if( origDstTable == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"origDstTable " + srcTable.getRequiredName() );
			}
			importMergeTableClearDeps( origDstTable, srcTable );
		}
	}

	protected void importMergeTableChains( ICFBamTableObj origDstTable, ICFBamTableObj srcTable ) {
		final String S_ProcName = "importMergeTableChains";
		ICFBamRelationTableObj dstRelationTableObj = origDstTable.getSchema().getRelationTableObj();
		String indexName = srcTable.getRequiredName();

		ICFBamSchemaDefObj dstSchemaDef = origDstTable.getRequiredContainerSchemaDef();
		ICFBamSchemaDefObj defSchema = srcTable.getRequiredContainerSchemaDef();

		ICFBamTableTableObj dstTableTableObj = origDstTable.getSchema().getTableTableObj();
		ICFBamChainTableObj dstChainTableObj = origDstTable.getSchema().getChainTableObj();
		ICFBamChainObj srcChain = null;
		ICFBamTableObj srcPrevTable;
		ICFBamRelationObj srcPrevRelation;
		ICFBamTableObj dstPrevTable;
		ICFBamRelationObj dstPrevRelation;

		ICFBamTableObj srcNextTable;
		ICFBamRelationObj srcNextRelation;
		ICFBamTableObj dstNextTable;
		ICFBamRelationObj dstNextRelation;
		ICFBamChainObj origDstChain;
		ICFBamChainEditObj editDstChain;
		ICFBamTenantObj dstTenant = (ICFBamTenantObj)origDstTable.getRequiredOwnerTenant();

		Iterator<ICFBamChainObj> iterSrcChain;
		List<ICFBamChainObj> srcChainList = srcTable.getOptionalComponentsChains();
		if( srcChainList != null ) {
			iterSrcChain = srcChainList.iterator();
			while( iterSrcChain.hasNext() ) {

				srcChain = iterSrcChain.next();

				origDstChain = dstChainTableObj.readChainByUNameIdx( origDstTable.getRequiredTenantId(),
					origDstTable.getRequiredId(),
					srcChain.getRequiredName() );
				if( origDstChain == null ) {
					origDstChain = dstChainTableObj.newInstance();
					editDstChain = (ICFBamChainEditObj)origDstChain.beginEdit();

					srcPrevRelation = srcChain.getRequiredLookupPrevRel();
					srcPrevTable = srcPrevRelation.getRequiredContainerFromTable();
					dstPrevTable = dstTableTableObj.readTableByUNameIdx( origDstTable.getRequiredContainerSchemaDef().getRequiredTenantId(),
						origDstTable.getRequiredContainerSchemaDef().getRequiredId(),
						srcPrevTable.getRequiredName() );
					dstPrevRelation = dstRelationTableObj.readRelationByUNameIdx( dstPrevTable.getRequiredTenantId(),
						dstPrevTable.getRequiredId(),
						srcPrevRelation.getRequiredName() );

					srcNextRelation = srcChain.getRequiredLookupNextRel();
					srcNextTable = srcNextRelation.getRequiredContainerFromTable();
					dstNextTable = dstTableTableObj.readTableByUNameIdx( origDstTable.getRequiredContainerSchemaDef().getRequiredTenantId(),
						origDstTable.getRequiredContainerSchemaDef().getRequiredId(),
						srcNextTable.getRequiredName() );
					dstNextRelation = dstRelationTableObj.readRelationByUNameIdx( dstNextTable.getRequiredTenantId(),
						dstNextTable.getRequiredId(),
						srcNextRelation.getRequiredName() );

					editDstChain.setRequiredOwnerTenant( dstTenant );
					editDstChain.setRequiredContainerTable( origDstTable );
					editDstChain.setRequiredLookupPrevRel( dstPrevRelation );
					editDstChain.setRequiredLookupNextRel( dstNextRelation );
					editDstChain.setRequiredName( srcChain.getRequiredName() );
					editDstChain.setOptionalDescription( srcChain.getOptionalDescription() );
					editDstChain.setOptionalLabel( srcChain.getOptionalLabel() );
					editDstChain.setOptionalShortDescription( srcChain.getOptionalShortDescription() );
					editDstChain.setOptionalShortName( srcChain.getOptionalShortName() );
					editDstChain.setOptionalLookupDefSchema( defSchema );
					origDstChain = (ICFBamChainObj)editDstChain.create();
					editDstChain = null;
				}
			}
		}
	}

	protected void importMergeSchemaTableTableChains( ICFBamSchemaDefObj dst, ICFBamSchemaDefObj src ) {
		final String S_ProcName = "importMergeSchemaTableTableChains";
		ICFBamTableTableObj dstTableTableObj = dst.getSchema().getTableTableObj();
		List<ICFBamTableObj> srcTables = src.getOptionalComponentsTables();
		Iterator<ICFBamTableObj> iterSrcTable = srcTables.iterator();
		ICFBamTableObj srcTable;
		ICFBamTableObj origDstTable;
		while( iterSrcTable.hasNext() ) {
			srcTable = iterSrcTable.next();
			origDstTable = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcTable.getRequiredName() );
			if( origDstTable == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"origDstTable " + srcTable.getRequiredName() );
			}
			importMergeTableChains( origDstTable, srcTable );
		}
	}

	protected void importMergeSchemaTableResolveNarrowedRelations( ICFBamSchemaDefObj dst, ICFBamSchemaDefObj src ) {
		final String S_ProcName = "importMergeSchemaTableResolveNarrowedRelations";

		ICFBamTableTableObj dstTableTableObj = dst.getSchema().getTableTableObj();
		ICFBamRelationTableObj dstRelationTableObj = dst.getSchema().getRelationTableObj();
		List<ICFBamTableObj> srcTables = src.getOptionalComponentsTables();
		Iterator<ICFBamTableObj> iterSrcTable = srcTables.iterator();
		ICFBamTableObj srcTable;
		ICFBamTableObj origDstTable;
		ICFBamRelationObj srcRelation;
		ICFBamRelationObj dstRelation;
		List<ICFBamRelationObj> srcRelations;
		Iterator<ICFBamRelationObj> iterSrcRelations;
		ICFBamRelationObj srcRelationNarrowed;
		ICFBamRelationObj dstRelationNarrowed;
		String srcRelationNarrowedTableName;
		ICFBamTableObj srcRelationNarrowedTable;
		ICFBamTableObj dstRelationNarrowedTable;
		ICFBamRelationEditObj editDstRelation;

		while( iterSrcTable.hasNext() ) {

			srcTable = iterSrcTable.next();
			origDstTable = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(), dst.getRequiredId(), srcTable.getRequiredName() );
			if( origDstTable == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"origDstTable " + srcTable.getRequiredName() );
			}

			srcRelations  = srcTable.getOptionalComponentsRelation();
			if( srcRelations != null ) {
				iterSrcRelations = srcRelations.iterator();
				if( iterSrcRelations != null ) {
					while( iterSrcRelations.hasNext() ) {
						srcRelation = iterSrcRelations.next();
						if( srcRelation != null ) {
							dstRelation = dstRelationTableObj.readRelationByUNameIdx( origDstTable.getRequiredTenantId(),
								origDstTable.getRequiredId(),
								srcRelation.getRequiredName() );
							if( dstRelation == null ) {
								throw new CFLibNullArgumentException( getClass(),
									S_ProcName,
									0,
									"dstRelationTableObj.readRelationByUNameIdx( origDefTable.getRequiredId(), \"" + srcRelation.getRequiredName() + "\" )" );
							}

							srcRelationNarrowed = srcRelation.getOptionalLookupNarrowed();
							if( srcRelationNarrowed == null ) {
								srcRelationNarrowedTableName = null;
								srcRelationNarrowedTable = null;
								dstRelationNarrowedTable = null;
								dstRelationNarrowed = null;
							}
							else {
								srcRelationNarrowedTableName = srcRelationNarrowed.getRequiredContainerFromTable().getRequiredName();
								dstRelationNarrowedTable = dstTableTableObj.readTableByUNameIdx( dst.getRequiredTenantId(), 
									dst.getRequiredId(),
									srcRelationNarrowedTableName );
								if( dstRelationNarrowedTable == null ) {
									throw new CFLibNullArgumentException( getClass(),
										S_ProcName,
										0,
										"dstTableTableObj.readTableByUNameIdx( dst.getRequiredId(), \"" + srcRelationNarrowedTableName + "\" )" ); 
								}

								dstRelationNarrowed = dstRelationTableObj.readRelationByUNameIdx( dstRelationNarrowedTable.getRequiredTenantId(),
									dstRelationNarrowedTable.getRequiredId(),
									srcRelationNarrowed.getRequiredName() );
								if( dstRelationNarrowed == null ) {
									throw new CFLibNullArgumentException( getClass(),
											S_ProcName,
											0,
											"dstRelationTableObj.readRelationByUNameIdx( " + srcRelationNarrowedTableName + ".getRequiredId(), \"" + srcRelationNarrowed.getRequiredName() + "\" )" ); 
								}
							}

							editDstRelation = (ICFBamRelationEditObj)dstRelation.beginEdit();
							editDstRelation.setOptionalLookupNarrowed( dstRelationNarrowed );
							editDstRelation.update();
							editDstRelation = null;
						}
						else {
							dstRelation = null;
						}
					}
				}
			}
		}
	}

	public void importMergeSchema( ICFBamSchemaDefObj dst, ICFBamSchemaDefObj src ) {
		final String S_ProcName = "importMergeSchema";

		if( dst == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				1,
				"dst" );
		}

		if( src == null ) {
			return;
		}

		if( dst == src ) {
			return;
		}

		dst.getSchema().getId16GenTableObj().readId16GenByDispIdx( dst.getRequiredTenantId(),
			0L );
		dst.getSchema().getId32GenTableObj().readId32GenByDispIdx( dst.getRequiredTenantId(),
			0L );
		dst.getSchema().getId64GenTableObj().readId64GenByDispIdx( dst.getRequiredTenantId(),
			0L );

		importMergeSchemaTables( dst, src );
		importMergeSchemaTypes( dst, src );
		importMergeSchemaTableColumns( dst, src );
		importMergeSchemaTableIndexes( dst, src );
		importMergeSchemaTableResolvePrimaryIndexes( dst, src );
		importMergeSchemaTableResolveLookupIndexes( dst, src );
		importMergeSchemaTableRelations( dst, src );
		importMergeSchemaTableResolveNarrowedRelations( dst, src );
		importMergeSchemaTableTableChains( dst, src );
		importMergeSchemaTableRelationPopDeps( dst, src );
		importMergeSchemaTableTableClearDeps( dst, src );
		importMergeSchemaTableTableDelDeps( dst, src );
		importMergeSchemaTableServerMethods( dst, src );
	}

	public void startElement(
		String		uri,
		String		localName,
		String		qName,
		Attributes	attrs )
	throws SAXException
	{
		final String S_ProcName = "startElement";
		final String S_LocalNameIndex = "localName";

		// SchemaRef attributes
		String	attrId = null;
		String	attrName = null;
		String	attrRefModelName = null;
		String	attrIncludeRoot = null;

		// Attribute Extraction
		String	attrLocalName;
		int		numAttrs;
		int		idxAttr;

		assert qName.equals( "SchemaRef" );

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

		ICFBamTenantObj tenantObj = (ICFBamTenantObj)saxLoader.getTenant();

		numAttrs = attrs.getLength();
		for( idxAttr = 0; idxAttr < numAttrs; idxAttr++ ) {
			attrLocalName = attrs.getLocalName( idxAttr );
			if( attrLocalName.equals( "Id" ) ) {
				if( attrId != null ) {
					throw new CFLibUniqueIndexViolationException( getClass(),
						S_ProcName,
						S_LocalNameIndex,
						attrLocalName );
				}
				attrId = attrs.getValue( idxAttr );
			}
			else if( attrLocalName.equals( "Name" ) ) {
				if( attrName != null ) {
					throw new CFLibUniqueIndexViolationException( getClass(),
						S_ProcName,
						S_LocalNameIndex,
						attrLocalName );
				}
				attrName = attrs.getValue( idxAttr );
			}
			else if( attrLocalName.equals( "RefModelName" ) ) {
				if( attrRefModelName != null ) {
					throw new CFLibUniqueIndexViolationException( getClass(),
						S_ProcName,
						S_LocalNameIndex,
						attrLocalName );
				}
				attrRefModelName = attrs.getValue( idxAttr );
			}
			else if( attrLocalName.equals( "IncludeRoot" ) ) {
				if( attrIncludeRoot != null ) {
					throw new CFLibUniqueIndexViolationException( getClass(),
						S_ProcName,
						S_LocalNameIndex,
						attrLocalName );
				}
				attrIncludeRoot = attrs.getValue( idxAttr );
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

		if( ( attrName == null ) || ( attrName.length() <= 0 ) ) {
			throw new CFLibEmptyArgumentException( getClass(),
				S_ProcName,
				0,
				"Name" );
		}

		if( ( attrRefModelName == null ) || ( attrRefModelName.length() <= 0 ) ) {
			throw new CFLibEmptyArgumentException( getClass(),
				S_ProcName,
				0,
				"RefModelName" );
		}

		if( ( attrIncludeRoot == null ) || ( attrIncludeRoot.length() <= 0 ) ) {
			throw new CFLibEmptyArgumentException( getClass(),
				S_ProcName,
				0,
				"IncludeRoot" );
		}

		CFLibXmlCoreContext curContext = getParser().getCurContext();
		curContext.putNamedValue( "Id", attrId );
		curContext.putNamedValue( "Name", attrName );
		curContext.putNamedValue( "RefModelName", attrRefModelName );
		curContext.putNamedValue( "IncludeRoot", attrIncludeRoot );

		if( saxLoader.getProcessSchema( curContext ) ) { 

			CFLibXmlCoreContext parentContext = curContext.getPrevContext();
			Object parentObject = parentContext.getNamedValue( "Object" );
			ICFBamSchemaDefObj parentSchema;
			if( parentObject == null ) {
				throw new CFLibNullArgumentException(this.getClass(), S_ProcName, 0, "Object" );
			}
			else if ( parentObject instanceof ICFBamSchemaDefObj ) {
				parentSchema = (ICFBamSchemaDefObj)parentObject;
			}
			else {
				throw new CFLibUnsupportedClassException( this.getClass(), S_ProcName, "Object", 0, "ICFBamSchemaDefObj" );
			}

			ICFBamSchemaRefTableObj schemaRefTableObj = schemaObj.getSchemaRefTableObj();
			ICFBamSchemaRefObj origSchemaRef = schemaRefTableObj.readSchemaRefByUNameIdx( parentSchema.getRequiredTenantId(), parentSchema.getRequiredId(), attrName );
			ICFBamSchemaRefObj schemaRef;
			ICFBamSchemaRefEditObj editSchemaRef;
			ICFBamSchemaDefObj referencedSchemaDef = null;

			boolean needToLoad;

			if( origSchemaRef != null ) {
				schemaRef = origSchemaRef;
				referencedSchemaDef = schemaRef.getOptionalLookupRefSchema();
				needToLoad = false;
			}
			else {
				schemaRef = schemaRefTableObj.newInstance();
				editSchemaRef = (ICFBamSchemaRefEditObj)schemaRef.beginEdit();
				editSchemaRef.setRequiredOwnerTenant( parentSchema.getRequiredOwnerTenant() );
				editSchemaRef.setRequiredContainerSchema( parentSchema );
				editSchemaRef.setRequiredName( attrName );
				editSchemaRef.setRequiredRefModelName( attrRefModelName );
				editSchemaRef.setRequiredIncludeRoot( attrIncludeRoot );
				schemaRef = (ICFBamSchemaRefObj)editSchemaRef.create();
				editSchemaRef = null;
				needToLoad = true;
				referencedSchemaDef = null;
			}

			curContext.putNamedValue( "Object", schemaRef );

			ICFLibAnyObj resolvedObj;
			try {
				resolvedObj = tenantObj.getNamedObject( attrIncludeRoot );
				if( resolvedObj != null ) {
					needToLoad = false;
				}
				else {
					needToLoad = true;
				}
			}
			catch( RuntimeException e ) {
				resolvedObj = null;
				needToLoad = true;
			}

			ICFLibMessageLog log = getLog();

			if( needToLoad ) {
				MSSBamCFEngine engine = saxLoader.getBamEngine();
				if( engine == null ) {
					throw new CFLibNullArgumentException( getClass(),
							S_ProcName,
							0,
							"Engine" );
				}

				if( ! ( engine instanceof MSSBamCFEngine ) ) {
					throw new CFLibUnsupportedClassException(getClass(),
						S_ProcName,
						"Engine", engine, "MSSBamCFEngine" );
				}

				CFBamXmlLoader subParser = new CFBamXmlLoader( engine, log );
				subParser.setSchemaObj( saxLoader.getSchemaObj() );
				subParser.setTenant( saxLoader.getTenant() );

				subParser.loadTenant( attrRefModelName );
			}

			// Re-attempt the resolution by name and complain if the load didn't fix the problem

			try {
				resolvedObj = tenantObj.getNamedObject( attrIncludeRoot );
			}
			catch( CFLibNullArgumentException e ) {
				resolvedObj = null;
			}

			if( resolvedObj == null ) {
				log.message( "WARNING: Could not resolve named object \"" + attrIncludeRoot + "\"\n" );
				referencedSchemaDef = null;
			}
			else if( resolvedObj instanceof ICFBamSchemaDefObj ) {
				referencedSchemaDef = (ICFBamSchemaDefObj)resolvedObj;
				editSchemaRef = (ICFBamSchemaRefEditObj)schemaRef.beginEdit();
				editSchemaRef.setOptionalLookupRefSchema( referencedSchemaDef );
				editSchemaRef.update();
				editSchemaRef = null;
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					attrIncludeRoot,
					resolvedObj,
					"ICFBamSchemaDefObj" );
			}
			curContext.putNamedValue( "ReferencedSchemaDef", referencedSchemaDef );

			importMergeSchema( parentSchema, referencedSchemaDef );
		}
	}

	public void endElement(
		String		uri,
		String		localName,
		String		qName )
	throws SAXException
	{
	}
}
