package com.amee.platform.resource.itemvaluedefinition;

import com.amee.base.domain.Since;
import com.amee.base.resource.*;
import com.amee.domain.data.ItemDefinition;
import com.amee.domain.data.ItemValueDefinition;
import com.amee.service.auth.ResourceAuthorizationService;
import com.amee.service.definition.DefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope("prototype")
@Since("3.1.0")
public class ItemValueDefinitionsBuilder implements ResourceBuilder {

    @Autowired
    private DefinitionService definitionService;

    @Autowired
    private ResourceAuthorizationService resourceAuthorizationService;

    @Autowired
    private ItemValueDefinitionBuilder itemValueDefinitionBuilder;

    @Autowired
    private RendererBeanFinder rendererBeanFinder;

    private ItemValueDefinitionsRenderer itemValueDefinitionsRenderer;

    @Transactional(readOnly = true)
    public Object handle(RequestWrapper requestWrapper) {
        // Get ItemDefinition identifier.
        String itemDefinitionIdentifier = requestWrapper.getAttributes().get("itemDefinitionIdentifier");
        if (itemDefinitionIdentifier != null) {
            // Get ItemDefinition.
            ItemDefinition itemDefinition = definitionService.getItemDefinitionByUid(itemDefinitionIdentifier);
            if (itemDefinition != null) {
                // Authorized?
                resourceAuthorizationService.ensureAuthorizedForBuild(
                        requestWrapper.getAttributes().get("activeUserUid"), itemDefinition);
                // Handle the ItemDefinition & ItemValueDefinitions.
                handle(requestWrapper, itemDefinition);
                ItemValueDefinitionsRenderer renderer = getItemValueDefinitionsRenderer(requestWrapper);
                renderer.ok();
                return renderer.getObject();
            } else {
                throw new NotFoundException();
            }
        } else {
            throw new MissingAttributeException("itemDefinitionIdentifier");
        }
    }

    protected void handle(RequestWrapper requestWrapper, ItemDefinition itemDefinition) {

        // Start Renderer.
        ItemValueDefinitionsRenderer renderer = getItemValueDefinitionsRenderer(requestWrapper);
        renderer.start();

        // Add ItemValueDefinition.
        for (ItemValueDefinition itemValueDefinition : itemDefinition.getActiveItemValueDefinitions()) {
            itemValueDefinitionBuilder.handle(requestWrapper, itemValueDefinition);
            renderer.newItemValueDefinition(itemValueDefinitionBuilder.getItemValueDefinitionRenderer(requestWrapper));
        }
    }

    public ItemValueDefinitionsRenderer getItemValueDefinitionsRenderer(RequestWrapper requestWrapper) {
        if (itemValueDefinitionsRenderer == null) {
            itemValueDefinitionsRenderer = (ItemValueDefinitionsRenderer) rendererBeanFinder.getRenderer(ItemValueDefinitionsRenderer.class, requestWrapper);
        }
        return itemValueDefinitionsRenderer;
    }
}