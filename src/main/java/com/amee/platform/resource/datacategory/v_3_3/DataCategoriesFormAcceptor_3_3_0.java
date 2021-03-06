package com.amee.platform.resource.datacategory.v_3_3;

import com.amee.base.domain.Since;
import com.amee.base.resource.RequestWrapper;
import com.amee.base.resource.ResponseHelper;
import com.amee.base.transaction.AMEETransaction;
import com.amee.base.validation.ValidationException;
import com.amee.domain.data.DataCategory;
import com.amee.platform.resource.datacategory.DataCategoriesResource;
import com.amee.platform.resource.datacategory.DataCategoryAcceptor;
import com.amee.platform.resource.datacategory.DataCategoryResource;
import com.amee.service.auth.ResourceAuthorizationService;
import com.amee.service.data.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope("prototype")
@Since("3.3.0")
public class DataCategoriesFormAcceptor_3_3_0 extends DataCategoryAcceptor implements DataCategoriesResource.FormAcceptor {

    @Autowired
    private DataService dataService;

    @Autowired
    private ResourceAuthorizationService resourceAuthorizationService;

    @Override
    @AMEETransaction
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Object handle(RequestWrapper requestWrapper) {

        // Create new DataCategory and validate.
        DataCategory dataCategory = new DataCategory();
        DataCategoryResource.DataCategoryValidator validator = getValidator(requestWrapper);
        validator.setObject(dataCategory);
        validator.initialise();
        if (validator.isValid(requestWrapper.getFormParameters())) {

            // Save DataCategory.
            dataService.persist(dataCategory);

            // Authorized?
            resourceAuthorizationService.ensureAuthorizedForAccept(
                    requestWrapper.getAttributes().get("activeUserUid"), dataCategory);

            // Return location of Data Category.
            String location = "/" + requestWrapper.getVersion() + "/categories/" + dataCategory.getUid();
            return ResponseHelper.getOK(requestWrapper, location, dataCategory.getUid());
        } else {
            throw new ValidationException(validator.getValidationResult());
        }
    }
}
