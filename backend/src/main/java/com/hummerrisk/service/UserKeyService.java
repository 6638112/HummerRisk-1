package com.hummerrisk.service;

import com.hummerrisk.base.domain.UserKey;
import com.hummerrisk.base.domain.UserKeyExample;
import com.hummerrisk.base.mapper.UserKeyMapper;
import com.hummerrisk.base.mapper.ext.ExtUserKeyMapper;
import com.hummerrisk.commons.constants.ApiKeyConstants;
import com.hummerrisk.commons.constants.ResourceConstants;
import com.hummerrisk.commons.constants.ResourceOperation;
import com.hummerrisk.commons.exception.HRException;
import com.hummerrisk.commons.utils.SessionUtils;
import com.hummerrisk.controller.request.user.UserKeyRequest;
import com.hummerrisk.i18n.Translator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author harris
 */
@Service
public class UserKeyService {

    @Resource
    private UserKeyMapper userKeyMapper;

    @Resource
    private ExtUserKeyMapper extUserKeyMapper;

    @Resource
    private UserService userService;

    public List<UserKey> getUserKeysInfo(UserKeyRequest request) {
        String userId = Objects.requireNonNull(SessionUtils.getUser()).getId();
        if (!StringUtils.equals(userId, "admin")) request.setUserId(userId);
        return extUserKeyMapper.getUserKeysInfo(request);
    }

    public UserKey generateUserKey(String userId) {
        if (userService.getUserDTO(userId) == null) {
            HRException.throwException(Translator.get("user_not_exist") + userId);
        }
        UserKeyExample userKeysExample = new UserKeyExample();
        userKeysExample.createCriteria().andUserIdEqualTo(userId);
        List<UserKey> userKeysList = userKeyMapper.selectByExample(userKeysExample);

        if (!CollectionUtils.isEmpty(userKeysList) && userKeysList.size() >= 5) {
            HRException.throwException(Translator.get("user_apikey_limit"));
        }

        UserKey userKeys = new UserKey();
        userKeys.setId(UUID.randomUUID().toString());
        userKeys.setUserId(userId);
        userKeys.setStatus(ApiKeyConstants.ACTIVE.name());
        userKeys.setAccessKey(RandomStringUtils.randomAlphanumeric(16));
        userKeys.setSecretKey(RandomStringUtils.randomAlphanumeric(16));
        userKeys.setCreateTime(System.currentTimeMillis());
        userKeyMapper.insert(userKeys);
        OperationLogService.log(SessionUtils.getUser(), userKeys.getAccessKey(), ApiKeyConstants.ACTIVE.name(), ResourceConstants.SystemConstants, ResourceOperation.CREATE, "创建API Keys");
        return userKeyMapper.selectByPrimaryKey(userKeys.getId());
    }

    public void deleteUserKey(String id) {
        userKeyMapper.deleteByPrimaryKey(id);
    }

    public void activeUserKey(String id) {
        UserKey userKeys = new UserKey();
        userKeys.setId(id);
        userKeys.setStatus(ApiKeyConstants.ACTIVE.name());
        userKeyMapper.updateByPrimaryKeySelective(userKeys);
    }

    public void disableUserKey(String id) {
        UserKey userKeys = new UserKey();
        userKeys.setId(id);
        userKeys.setStatus(ApiKeyConstants.DISABLED.name());
        userKeyMapper.updateByPrimaryKeySelective(userKeys);
    }

    public UserKey getUserKey(String accessKey) {
        UserKeyExample userKeyExample = new UserKeyExample();
        userKeyExample.createCriteria().andAccessKeyEqualTo(accessKey).andStatusEqualTo(ApiKeyConstants.ACTIVE.name());
        List<UserKey> userKeysList = userKeyMapper.selectByExample(userKeyExample);
        if (!CollectionUtils.isEmpty(userKeysList)) {
            return userKeysList.get(0);
        }
        return null;
    }
}
