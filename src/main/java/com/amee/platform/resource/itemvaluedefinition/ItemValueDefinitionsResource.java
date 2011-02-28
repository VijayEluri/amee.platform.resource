package com.amee.platform.resource.itemvaluedefinition;

import com.amee.base.resource.ResourceAcceptor;
import com.amee.base.resource.ResourceBuilder;
import com.amee.base.resource.ResourceRenderer;

public interface ItemValueDefinitionsResource {

    interface Builder extends ResourceBuilder {
    }

    interface Renderer extends ResourceRenderer {

        public void newItemValueDefinition(ItemValueDefinitionResource.Renderer renderer);
    }

    interface FormAcceptor extends ResourceAcceptor {
        
    }
}
