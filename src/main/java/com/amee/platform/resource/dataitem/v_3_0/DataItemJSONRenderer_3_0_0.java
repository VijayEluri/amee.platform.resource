package com.amee.platform.resource.dataitem.v_3_0;

import com.amee.base.domain.Since;
import com.amee.domain.data.ItemValue;
import com.amee.platform.resource.dataitem.v_3_1.DataItemJSONRenderer_3_1_0;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Version 3.0 does not include the 'history' flag of an ItemValue. Otherwise the representation is the same as 3.1.
 */
@Service
@Scope("prototype")
@Since("3.0.0")
public class DataItemJSONRenderer_3_0_0 extends DataItemJSONRenderer_3_1_0 {

    public void newValue(ItemValue itemValue) {
        JSONObject valueObj = new JSONObject();
        put(valueObj, "path", itemValue.getPath());
        put(valueObj, "value", itemValue.getValue());
        if (itemValue.hasUnit()) {
            put(valueObj, "unit", itemValue.getUnit().toString());
        }
        if (itemValue.hasPerUnit()) {
            put(valueObj, "perUnit", itemValue.getPerUnit().toString());
            put(valueObj, "compoundUnit", itemValue.getCompoundUnit().toString());
        }
        valuesArr.put(valueObj);
    }
}