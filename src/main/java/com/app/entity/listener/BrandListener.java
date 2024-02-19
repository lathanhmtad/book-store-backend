package com.app.entity.listener;

import com.app.constant.AppConstant;
import com.app.entity.Brand;
import com.app.util.CloudinaryUtil;
import jakarta.persistence.PreRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BrandListener {
    @Autowired
    private CloudinaryUtil cloudinaryUtil;
    @PreRemove
    public void onRemove(final Brand toRemove) {
        if(!toRemove.getLogo().equals(AppConstant.BRAND_DEFAULT_IMAGE)) {
            cloudinaryUtil.delete(toRemove.getLogo());
        }
    }
}
