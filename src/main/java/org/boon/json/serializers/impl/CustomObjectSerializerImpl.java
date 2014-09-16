package org.boon.json.serializers.impl;

import org.boon.Boon;
import org.boon.core.Type;
import org.boon.json.serializers.CustomObjectSerializer;
import org.boon.json.serializers.JsonSerializerInternal;
import org.boon.json.serializers.ObjectSerializer;
import org.boon.primitive.CharBuf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by Richard on 9/14/14.
 */
public class CustomObjectSerializerImpl implements ObjectSerializer {


    private final Map<Class, CustomObjectSerializer> overrideMap;


    private final boolean typeInfo;

    private final boolean includeNulls;

    public CustomObjectSerializerImpl(boolean typeInfo, Map<Class, CustomObjectSerializer> overrideMap, boolean includeNulls) {
        this.overrideMap = overrideMap;
        this.typeInfo = typeInfo;
        this.includeNulls = includeNulls;
    }


    @Override
    public final void serializeObject (JsonSerializerInternal jsonSerializer, Object obj, CharBuf builder )  {

        Type type = Type.getInstanceType (obj);
        Class<?> cls = Boon.cls(obj);
        if (cls != null) {
            final CustomObjectSerializer customObjectSerializer = overrideMap.get(cls);
            if (customObjectSerializer != null) {
                customObjectSerializer.serializeObject(jsonSerializer, obj, builder);
                return;
            }
        }
        switch ( type ) {

            case NULL:
                if (includeNulls) builder.addNull();
                return;
            case INT:
                builder.addInt ( int.class.cast ( obj ) );
                return;
            case BOOLEAN:
                builder.addBoolean ( boolean.class.cast ( obj ) );
                return;
            case BYTE:
                builder.addByte ( byte.class.cast ( obj ) );
                return;
            case LONG:
                builder.addLong ( long.class.cast ( obj ) );
                return;
            case DOUBLE:
                builder.addDouble ( double.class.cast ( obj ) );
                return;
            case FLOAT:
                builder.addFloat ( float.class.cast ( obj ) );
                return;
            case SHORT:
                builder.addShort ( short.class.cast ( obj ) );
                return;
            case CHAR:
                builder.addChar ( char.class.cast ( obj ) );
                return;
            case BIG_DECIMAL:
                builder.addBigDecimal ( (BigDecimal) obj );
                return;
            case BIG_INT:
                builder.addBigInteger ( (BigInteger) obj );
                return;
            case DATE:
                jsonSerializer.serializeDate ( (Date) obj, builder );
                return;
            case STRING:
                jsonSerializer.serializeString ( ( String ) obj, builder );
                return;
            case CLASS:
                builder.addQuoted ( (( Class ) obj).getName() );
                return;

            case TIME_ZONE:
                TimeZone zone = (TimeZone) obj;

                builder.addQuoted ( zone.getID() );
                return;


            case CHAR_SEQUENCE:
                jsonSerializer.serializeString ( obj.toString (), builder );
                return;
            case BOOLEAN_WRAPPER:
                builder.addBoolean ( ( Boolean ) obj );
                return;
            case INTEGER_WRAPPER:
                builder.addInt ( (Integer) obj);
                return;
            case LONG_WRAPPER:
                builder.addLong ( (Long) obj);
                return;
            case FLOAT_WRAPPER:
                builder.addFloat ( (Float) obj);
                return;
            case DOUBLE_WRAPPER:
                builder.addDouble ( (Double) obj);
                return;
            case SHORT_WRAPPER:
                builder.addShort ( (Short) obj);
                return;
            case BYTE_WRAPPER:
                builder.addByte ( (Byte) obj);
                return;
            case CHAR_WRAPPER:
                builder.addChar ( (Character) obj);
                return;
            case ENUM:
                builder.addQuoted ( obj.toString () );
                return;

            case COLLECTION:
            case LIST:
            case SET:
                jsonSerializer.serializeCollection ( (Collection) obj, builder );
                return;
            case MAP:
                jsonSerializer.serializeMap ( (Map) obj, builder );
                return;
            case ARRAY:
                jsonSerializer.serializeArray ( obj, builder );
                return;
            case INSTANCE:
                jsonSerializer.serializeInstance ( obj, builder, typeInfo );
                return;
            case CURRENCY:
                builder.addCurrency((Currency) obj );
                return;
            default:
                if (obj instanceof Map) {

                    jsonSerializer.serializeMap ( ( Map ) obj, builder );
                } else if (obj instanceof Collection) {
                    jsonSerializer.serializeCollection((Collection) obj, builder);

                } else {
                    jsonSerializer.serializeUnknown(obj, builder);
                }

        }


    }

}
