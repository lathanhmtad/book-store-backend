package com.app.entity.listener;

import com.app.constant.AppConstant;
import com.app.entity.Category;
import com.app.util.CloudinaryUtil;
import jakarta.persistence.PreRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryListener {
    @Autowired
    private CloudinaryUtil cloudinaryUtil;
    @PreRemove
    public void onRemove(final Category toRemove) {
        boolean hasChildren = toRemove.getChildren().size() > 0;
        if(hasChildren)
            throw new RuntimeException("Cannot remove a category that has child categories");
        if(!toRemove.getImage().equals(AppConstant.CATEGORY_DEFAULT_IMAGE)) {
            cloudinaryUtil.delete(toRemove.getImage());
        }
    }
}
