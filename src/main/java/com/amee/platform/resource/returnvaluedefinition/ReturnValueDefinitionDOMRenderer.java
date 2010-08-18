package com.amee.platform.resource.returnvaluedefinition;

import com.amee.base.domain.Since;
import com.amee.domain.data.ItemDefinition;
import com.amee.domain.data.ReturnValueDefinition;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
@Since("3.1.0")
public class ReturnValueDefinitionDOMRenderer implements ReturnValueDefinitionRenderer {

    // TODO: Add audit (created, modified). 

    private ReturnValueDefinition returnValueDefinition;
    private Element rootElem;
    private Element returnValueDefinitionElem;

    @Override
    public void start() {
        rootElem = new Element("Representation");
    }

    @Override
    public void ok() {
        rootElem.addContent(new Element("Status").setText("OK"));
    }

    @Override
    public void newReturnValueDefinition(ReturnValueDefinition returnValueDefinition) {
        this.returnValueDefinition = returnValueDefinition;
        returnValueDefinitionElem = new Element("ReturnValueDefinition");
        if (rootElem != null) {
            rootElem.addContent(returnValueDefinitionElem);
        }
    }

    @Override
    public void addBasic() {
        returnValueDefinitionElem.setAttribute("uid", returnValueDefinition.getUid());
    }

    @Override
    public void addType() {
        returnValueDefinitionElem.addContent(new Element("Type").setText(returnValueDefinition.getType()));
    }

    @Override
    public void addItemDefinition(ItemDefinition itemDefinition) {
        Element e = new Element("ItemDefinition");
        returnValueDefinitionElem.addContent(e);
        e.setAttribute("uid", itemDefinition.getUid());
        e.addContent(new Element("Name").setText(itemDefinition.getName()));
    }

    @Override
    public void addUnits() {
        returnValueDefinitionElem.addContent(new Element("Unit").setText(returnValueDefinition.getUnit().toString()));
        returnValueDefinitionElem.addContent(new Element("PerUnit").setText(returnValueDefinition.getPerUnit().toString()));
    }

    @Override
    public void addFlags() {
        returnValueDefinitionElem.addContent(new Element("Default").setText(Boolean.toString(returnValueDefinition.isDefaultType())));
    }

    public String getMediaType() {
        return "application/xml";
    }

    @Override
    public Object getObject() {
        return new Document(rootElem);
    }
}
