package com.amee.platform.resource.dataitem.v_3_2;

import com.amee.base.domain.Since;
import com.amee.domain.DataItemService;
import com.amee.domain.data.ItemDefinition;
import com.amee.domain.item.BaseItemValue;
import com.amee.domain.item.NumberValue;
import com.amee.domain.item.data.DataItem;
import com.amee.platform.resource.dataitem.DataItemResource;
import org.jdom2.Document;
import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
@Since("3.2.0")
public class DataItemDOMRenderer_3_2_0 implements DataItemResource.Renderer {

    @Autowired
    protected DataItemService dataItemService;

    protected DataItem dataItem;
    protected Element rootElem;
    protected Element dataItemElem;
    protected Element valuesElem;

    public void start() {
        rootElem = new Element("Representation");
    }

    public void ok() {
        rootElem.addContent(new Element("Status").setText("OK"));
    }

    public void newDataItem(DataItem dataItem) {
        this.dataItem = dataItem;
        dataItemElem = new Element("Item");
        if (rootElem != null) {
            rootElem.addContent(dataItemElem);
        }
    }

    public void addBasic() {
        dataItemElem.setAttribute("uid", dataItem.getUid());
    }

    public void addName() {
        dataItemElem.addContent(new Element("Name").setText(dataItem.getName()));
    }

    public void addLabel() {
        dataItemElem.addContent(new Element("Label").setText(dataItemService.getLabel(dataItem)));
    }

    public void addPath() {
        dataItemElem.addContent(new Element("Path").setText(dataItem.getPath()));
        dataItemElem.addContent(new Element("FullPath").setText(dataItem.getFullPath()));
    }

    public void addParent() {
        dataItemElem.addContent(new Element("CategoryUid").setText(dataItem.getDataCategory().getUid()));
        dataItemElem.addContent(new Element("CategoryWikiName").setText(dataItem.getDataCategory().getWikiName()));
    }

    public void addAudit() {
        dataItemElem.setAttribute("status", dataItem.getStatus().getName());
        dataItemElem.setAttribute("created", DATE_FORMAT.print(dataItem.getCreated().getTime()));
        dataItemElem.setAttribute("modified", DATE_FORMAT.print(dataItem.getModified().getTime()));
    }

    public void addWikiDoc() {
        dataItemElem.addContent(new Element("WikiDoc").setText(dataItem.getWikiDoc()));
    }

    public void addProvenance() {
        dataItemElem.addContent(new Element("Provenance").setText(dataItem.getProvenance()));
    }

    public void addItemDefinition(ItemDefinition itemDefinition) {
        Element e = new Element("ItemDefinition");
        dataItemElem.addContent(e);
        e.setAttribute("uid", itemDefinition.getUid());
        e.addContent(new Element("Name").setText(itemDefinition.getName()));
    }

    public void startValues() {
        valuesElem = new Element("Values");
        dataItemElem.addContent(valuesElem);
    }

    public void newValue(BaseItemValue itemValue) {
        Element valueElem = new Element("Value");
        valueElem.setAttribute("history", Boolean.toString(itemValue.isHistoryAvailable()));
        valueElem.addContent(new Element("Path").setText(itemValue.getPath()));
        valueElem.addContent(new Element("Value").setText(itemValue.getValueAsString()));
        if (NumberValue.class.isAssignableFrom(itemValue.getClass())) {
            NumberValue nv = (NumberValue) itemValue;
            if (nv.hasUnit()) {
                valueElem.addContent(new Element("Unit").setText(nv.getCompoundUnit().toString()));
            }
        }
        valuesElem.addContent(valueElem);
    }

    public String getMediaType() {
        return "application/xml";
    }

    public Object getObject() {
        return new Document(rootElem);
    }
}
