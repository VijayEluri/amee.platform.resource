package com.amee.platform.service.v3.itemvaluedefinition.v_3_0;

import com.amee.base.domain.Since;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Scope("prototype")
@Since("3.0.0")
public class ItemValueDefinitionBuilder extends com.amee.platform.service.v3.itemvaluedefinition.ItemValueDefinitionBuilder {

    private final static Map<String, Class> RENDERERS = new HashMap<String, Class>() {
        {
            put("application/json", ItemValueDefinitionJSONRenderer.class);
            put("application/xml", ItemValueDefinitionDOMRenderer.class);
        }
    };

    public Map<String, Class> getRenderers() {
        return RENDERERS;
    }
}