package com.amee.platform.resource.datacategory.v_3_0;

import com.amee.base.domain.Since;
import com.amee.platform.resource.datacategory.v_3_3.DataCategoryJSONRenderer_3_3_0;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
@Since("3.0.0")
public class DataCategoryJSONRenderer_3_0_0 extends DataCategoryJSONRenderer_3_3_0 {

    public void addHistory() {
        // Not in 3.0.
    }
}