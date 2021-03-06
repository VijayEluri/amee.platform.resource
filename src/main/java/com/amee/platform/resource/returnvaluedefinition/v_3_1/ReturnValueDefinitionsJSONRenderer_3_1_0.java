package com.amee.platform.resource.returnvaluedefinition.v_3_1;

import com.amee.base.domain.Since;
import com.amee.base.resource.ResponseHelper;
import com.amee.platform.resource.returnvaluedefinition.ReturnValueDefinitionResource;
import com.amee.platform.resource.returnvaluedefinition.ReturnValueDefinitionsResource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
@Since("3.1.0")
public class ReturnValueDefinitionsJSONRenderer_3_1_0 implements ReturnValueDefinitionsResource.Renderer {

    private JSONObject rootObj;
    private JSONArray returnValueDefinitionsArr;

    public void start() {
        rootObj = new JSONObject();
        returnValueDefinitionsArr = new JSONArray();
        ResponseHelper.put(rootObj, "returnValueDefinitions", returnValueDefinitionsArr);
    }

    public void ok() {
        ResponseHelper.put(rootObj, "status", "OK");
    }

    public void newReturnValueDefinition(ReturnValueDefinitionResource.Renderer renderer) {
        try {
            returnValueDefinitionsArr.put(((JSONObject) renderer.getObject()).getJSONObject("returnValueDefinition"));
        } catch (JSONException e) {
            throw new RuntimeException("Caught JSONException: " + e.getMessage(), e);
        }
    }

    public String getMediaType() {
        return "application/json";
    }

    public JSONObject getObject() {
        return rootObj;
    }
}
