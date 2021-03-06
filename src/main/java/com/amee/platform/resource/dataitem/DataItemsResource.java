package com.amee.platform.resource.dataitem;

import com.amee.base.resource.RequestWrapper;
import com.amee.base.resource.ResourceAcceptor;
import com.amee.base.resource.ResourceBuilder;
import com.amee.base.resource.ResourceRenderer;
import com.amee.base.validation.ValidationException;

public interface DataItemsResource {

    interface Builder extends ResourceBuilder {
    }

    interface Renderer extends ResourceRenderer {

        void newDataItem(DataItemResource.Renderer renderer);

        void setTruncated(boolean truncated);
    }

    interface FormAcceptor extends ResourceAcceptor {

        Object handle(RequestWrapper requestWrapper) throws ValidationException;
    }
}
