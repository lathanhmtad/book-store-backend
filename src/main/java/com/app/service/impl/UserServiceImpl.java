package com.app.service.impl;

import com.app.constant.AppConstant;
import com.app.constant.CloudinaryConstant;
import com.app.entity.User;
import com.app.exception.ResourceExistsException;
import com.app.payload.user.UserDto;
import com.app.payload.user.UserRequest;
import com.app.repository.RoleRepo;
import com.app.repository.UserRepo;
import com.app.security.MyUserDetails;
import com.app.service.UserService;
import com.app.util.CloudinaryUtil;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserServiceImpl extends AbstractBaseServiceImpl<User, UserDto> implements UserService {
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final CloudinaryUtil cloudinaryUtil;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo,
                           ModelMapper modelMapper,
                           RoleRepo roleRepo,
                           CloudinaryUtil cloudinaryUtil,
                           PasswordEncoder passwordEncoder
    ) {
        super(userRepo, UserDto.class, User.class);
        this.modelMapper = modelMapper;
        this.roleRepo = roleRepo;
        this.cloudinaryUtil = cloudinaryUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    @Override
    public UserDto retrieveCurrentUser() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = userDetails.getId();
        return this.findById(currentUserId);
    }

    @Override
    public User transformDtoToEntity(UserDto userDto) {
        UserRequest userRequest = (UserRequest) userDto;
        Long userId = userRequest.getId();

        if(userId == null) {
            // create user
            User userEntity = modelMapper.map(userRequest, User.class);
            this.mapUserRoles(userEntity, userRequest);

            MultipartFile imgFile = userRequest.getImageFile();
            userEntity.setPhoto(
                    imgFile == null ? AppConstant.USER_DEFAULT_IMAGE :
                            cloudinaryUtil.upload(imgFile, CloudinaryConstant.USER_IMG_FOLDER)
            );
            userEntity.setPassword(encodePassword(userRequest.getPassword()));
            return userEntity;
        }
        else {
            // update user
            User userInDb = userRepo.findById(userId).get();
            modelMapper.map(userRequest, userInDb);
            this.mapUserRoles(userInDb, userRequest);

            MultipartFile imgFile = userRequest.getImageFile();
            if(imgFile != null) {
                if(!userInDb.getPhoto().equals(AppConstant.USER_DEFAULT_IMAGE)) {
                    cloudinaryUtil.delete(userInDb.getPhoto());
                }
                userInDb.setPhoto(cloudinaryUtil.upload(imgFile, CloudinaryConstant.USER_IMG_FOLDER));
            }
            if(userRequest.getPassword() != null) {
                userInDb.setPassword(encodePassword(userRequest.getPassword()));
            }
            return userInDb;
        }
    }

    @Override
    public boolean isExists(UserDto userDto) throws ResourceExistsException {
        boolean isUnique = this.isEmailUnique(userDto.getId(), userDto.getEmail());
        if(isUnique) {
            return false;
        }
        throw new ResourceExistsException("User", "email", userDto.getEmail());
    }

    private void mapUserRoles(User user, UserRequest userRequest) {
        if(userRequest.getRoleIds() != null) {
            user.setRoles(roleRepo.findAllById(userRequest.getRoleIds()));
        }
        if(userRequest.getRoleNames() != null) {
            user.setRoles(roleRepo.findAllByName(userRequest.getRoleNames()));
        }
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    private boolean isEmailUnique(Long userId, String email) {
        Optional<User> userByEmail = userRepo.findByEmail(email);

        if(userByEmail.isEmpty()) return true;

        boolean isCreatingNew = (userId == null);

        if(isCreatingNew) {
            if(userByEmail.isPresent()) return false;
        } else {
            if(userByEmail.get().getId() != userId) {
                return false;
            }
        }
        return true;
    }
}
