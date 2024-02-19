package com.app.entity.listener;

import com.app.constant.AppConstant;
import com.app.entity.User;
import com.app.repository.RefreshTokenRepo;
import com.app.util.CloudinaryUtil;
import jakarta.persistence.PreRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class UserListener {
    @Autowired
    private CloudinaryUtil cloudinaryUtil;
    private static RefreshTokenRepo refreshTokenRepo;
    @Autowired
    private ApplicationContext context;

    @PreRemove
    public void onRemove(final User toRemove) {
        if(refreshTokenRepo == null)
            refreshTokenRepo = context.getBean(RefreshTokenRepo.class);
        refreshTokenRepo.deleteByUser_Id(toRemove.getId());
        if(!toRemove.getPhoto().equals(AppConstant.USER_DEFAULT_IMAGE)) {
            cloudinaryUtil.delete(toRemove.getPhoto());
        }
    }
}
