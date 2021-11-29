package com.zergclan.wormhole.web.api.contorller;

import com.zergclan.wormhole.web.api.vo.HttpResult;
import com.zergclan.wormhole.web.application.domain.entity.UserInfo;
import com.zergclan.wormhole.web.application.service.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Controller of {@link UserInfo}.
 */
@RestController
public final class UserInfoController extends AbstractRestController {

    @Resource
    private UserInfoService userInfoService;

    /**
     * Add {@link UserInfo}.
     *
     * @param userInfo {@link UserInfo}
     * @return {@link HttpResult}
     */
    @PostMapping("/user")
    public HttpResult<Void> add(@RequestBody final UserInfo userInfo) {
        userInfoService.save(userInfo);
        return success();
    }
    
    /**
     * Add {@link UserInfo}.
     *
     * @param userInfo {@link UserInfo}
     * @return {@link HttpResult}
     */
    @PutMapping("/user")
    public HttpResult<Void> update(@RequestBody final UserInfo userInfo) {
        userInfoService.update(userInfo);
        return success();
    }
    
    

    /**
     * Get {@link UserInfo} by id.
     * @param id id
     * @return {@link HttpResult}
     */
    @GetMapping("/user/{id}")
    public HttpResult<UserInfo> getById(@PathVariable(value = "id") final Integer id) {
        UserInfo userInfo = userInfoService.getById(id);
        return success(userInfo);
    }
}
