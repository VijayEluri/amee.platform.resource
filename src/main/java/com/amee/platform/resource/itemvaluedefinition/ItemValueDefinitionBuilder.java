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
@Since("3.0.0")
public class ItemValueDefinitionBuilder implements ResourceBuilder {

    @Autowired
    private DefinitionService definitionService;

    @Autowired
    private ResourceAuthorizationService resourceAuthorizationService;

    @Autowired
    private RendererBeanFinder rendererBeanFinder;

    private ItemValueDefinitionRenderer itemValueDefinitionRenderer;

    @Transactional(readOnly = true)
    public Object handle(RequestWrapper requestWrapper) {
        // Get ItemDefinition identifier.
        String itemDefinitionIdentifier = requestWrapper.getAttributes().get("itemDefinitionIdentifier");
        if (itemDefinitionIdentifier != null) {
            // Get ItemDefinition.
            ItemDefinition itemDefinition = definitionService.getItemDefinitionByUid(itemDefinitionIdentifier);
            if (itemDefinition != null) {
                // Get ItemValueDefinition identifier.
                String itemValueDefinitionIdentifier = requestWrapper.getAttributes().get("itemValueDefinitionIdentifier");
                if (itemValueDefinitionIdentifier != null) {
                    // Get ItemValueDefinition.
                    ItemValueDefinition itemValueDefinition = definitionService.getItemValueDefinitionByUid(
                            itemDefinition, itemValueDefinitionIdentifier);
                    if (itemValueDefinition != null) {
                        // Authorized?
                        resourceAuthorizationService.ensureAuthorizedForBuild(
                                requestWrapper.getAttributes().get("activeUserUid"), itemValueDefinition);
                        // Handle the ItemValueDefinition.
                        handle(requestWrapper, itemValueDefinition);
                        ItemValueDefinitionRenderer renderer = getItemValueDefinitionRenderer(requestWrapper);
                        renderer.ok();
                        return renderer.getObject();
                    } else {
                        throw new NotFoundException();
                    }
                } else {
                    throw new MissingAttributeException("itemValueDefinitionIdentifier");
                }
            } else {
                throw new NotFoundException();
            }
        } else {
            throw new MissingAttributeException("itemDefinitionIdentifier");
        }
    }

    public void handle(
            RequestWrapper requestWrapper,
            ItemValueDefinition itemValueDefinition) {

        ItemValueDefinitionRenderer renderer = getItemValueDefinitionRenderer(requestWrapper);
        renderer.start();

        boolean full = requestWrapper.getMatrixParameters().containsKey("full");
        boolean name = requestWrapper.getMatrixParameters().containsKey("name");
        boolean path = requestWrapper.getMatrixParameters().containsKey("path");
        boolean value = requestWrapper.getMatrixParameters().containsKey("value");
        boolean audit = requestWrapper.getMatrixParameters().containsKey("audit");
        boolean wikiDoc = requestWrapper.getMatrixParameters().containsKey("wikiDoc");
        boolean itemDefinition = requestWrapper.getMatrixParameters().containsKey("itemDefinition");
        boolean valueDefinition = requestWrapper.getMatrixParameters().containsKey("valueDefinition");
        boolean usages = requestWrapper.getMatrixParameters().containsKey("usages");
        boolean choices = requestWrapper.getMatrixParameters().containsKey("choices");
        boolean units = requestWrapper.getMatrixParameters().containsKey("units");
        boolean flags = requestWrapper.getMatrixParameters().containsKey("flags");
        boolean versions = requestWrapper.getMatrixParameters().containsKey("versions");

        // New ItemValueDefinition & basic.
        renderer.newItemValueDefinition(itemValueDefinition);
        renderer.addBasic();

        // Optional attributes.
        if (name || full) {
            renderer.addName();
        }
        if (path || full) {
            renderer.addPath();
        }
        if (value || full) {
            renderer.addValue();
        }
        if (audit || full) {
            renderer.addAudit();
        }
        if (wikiDoc || full) {
            renderer.addWikiDoc();
        }
        if ((itemDefinition || full) && (itemValueDefinition.getItemDefinition() != null)) {
            renderer.addItemDefinition(itemValueDefinition.getItemDefinition());
        }
        if ((valueDefinition || full) && (itemValueDefinition.getValueDefinition() != null)) {
            renderer.addValueDefinition(itemValueDefinition.getValueDefinition());
        }
        if (usages || full) {
            renderer.addUsages();
        }
        if (choices || full) {
            renderer.addChoices();
        }
        if (units || full) {
            renderer.addUnits();
        }
        if (flags || full) {
            renderer.addFlags();
        }
        if (versions || full) {
            renderer.addVersions();
        }
    }

    public ItemValueDefinitionRenderer getItemValueDefinitionRenderer(RequestWrapper requestWrapper) {
        if (itemValueDefinitionRenderer == null) {
            itemValueDefinitionRenderer = (ItemValueDefinitionRenderer) rendererBeanFinder.getRenderer(ItemValueDefinitionRenderer.class, requestWrapper);
        }
        return itemValueDefinitionRenderer;
    }
}
