/*
 * Copyright 2013-2014 Richard M. Hightower
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * __________                              _____          __   .__
 * \______   \ ____   ____   ____   /\    /     \ _____  |  | _|__| ____    ____
 *  |    |  _//  _ \ /  _ \ /    \  \/   /  \ /  \\__  \ |  |/ /  |/    \  / ___\
 *  |    |   (  <_> |  <_> )   |  \ /\  /    Y    \/ __ \|    <|  |   |  \/ /_/  >
 *  |______  /\____/ \____/|___|  / \/  \____|__  (____  /__|_ \__|___|  /\___  /
 *         \/                   \/              \/     \/     \/       \//_____/
 *      ____.                     ___________   _____    ______________.___.
 *     |    |____ ___  _______    \_   _____/  /  _  \  /   _____/\__  |   |
 *     |    \__  \\  \/ /\__  \    |    __)_  /  /_\  \ \_____  \  /   |   |
 * /\__|    |/ __ \\   /  / __ \_  |        \/    |    \/        \ \____   |
 * \________(____  /\_/  (____  / /_______  /\____|__  /_______  / / ______|
 *               \/           \/          \/         \/        \/  \/
 */

package org.boon.json.serializers.impl;

import org.boon.core.reflection.fields.FieldAccess;
import org.boon.json.serializers.JsonSerializerInternal;
import org.boon.json.serializers.ObjectSerializer;
import org.boon.primitive.CharBuf;

import java.util.Collection;
import java.util.Map;

/**
 * Created by rick on 1/4/14.
 */
public class ObjectSerializationWithTypeInfo implements ObjectSerializer {

    private final boolean includeNulls;

    public ObjectSerializationWithTypeInfo(boolean includeNulls) {

        this.includeNulls = includeNulls;
    }

    @Override
    public final void serializeObject ( JsonSerializerInternal serializer, Object instance, CharBuf builder ) {

        if (instance instanceof Map) {

            serializer.serializeMap ( ( Map ) instance, builder );
            return;
        } else if (instance instanceof Collection) {
            serializer.serializeCollection((Collection) instance, builder);
            return;

        } else if (instance == null) {

            if (includeNulls) {
                builder.addNull();
                return;
            }

        }

        builder.addString( "{\"class\":" );
        builder.addQuoted ( instance.getClass ().getName () );
        final Map<String, FieldAccess> fieldAccessors = serializer.getFields ( instance.getClass () );

        int index = 0;
        Collection<FieldAccess> values = fieldAccessors.values();
        int length = values.size();

        if ( length > 0 ) {
            builder.addChar( ',' );
           

        for ( FieldAccess fieldAccess : values ) {
            boolean sent = serializer.serializeField ( instance, fieldAccess, builder );
            if (sent) {
                index++;
                builder.addChar( ',' );
            }
        }


        if ( index > 0 ) {
            builder.removeLastChar();
        }

        builder.addChar( '}' );

    }
}

}
