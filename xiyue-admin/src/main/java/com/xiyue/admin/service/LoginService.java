package com.xiyue.admin.service;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiyue.admin.config.ApiContext;
import com.xiyue.admin.entity.SysUser;
import com.xiyue.common.constants.GlobalConstants;
import com.xiyue.common.enums.DeleteFlag;
import com.xiyue.common.enums.DeviceType;
import jakarta.annotation.Resource;
import com.xiyue.common.enums.BusinessErrorCode;
import com.xiyue.common.utils.AesUtil;
import com.xiyue.common.vo.Result;

import com.xiyue.admin.dto.UserLoginDto;
import com.xiyue.admin.dto.UserSignDto;
import com.xiyue.admin.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hcl
 * @since 2022-04-25
 */
@Slf4j
@Service
public class LoginService extends ServiceImpl<SysUserMapper, SysUser>{

   @Resource
   private SysUserMapper sysUserMapper;


   public Result<Object> signIn(UserLoginDto userLoginDto) {

      LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();

      lambdaQueryWrapper.eq(SysUser::getAccount, userLoginDto.getAccount());
      SysUser sysUser =  sysUserMapper.selectOne(lambdaQueryWrapper);

      if(null != sysUser){
         if(userLoginDto.getPassword().equals(AesUtil.decryptStr(sysUser.getPassword(), sysUser.getSalt()))){

            if (!sysUser.getStatus()) {
               return Result.fail(BusinessErrorCode.ACCOUNT_BLOCKING);
            }

            StpUtil.login(sysUser.getId(), userLoginDto.getDevice() == null ? DeviceType.PC.name(): userLoginDto.getDevice().name());
            sysUser.setLogged(new Date());
            sysUserMapper.updateById(sysUser);

            return Result.ok(StpUtil.getTokenInfo());
         }
      }

      return Result.fail(BusinessErrorCode.USER_PHONE_ERROR);

   }


   public Result<Object> signUp(UserSignDto userSignDto) {

      if(this.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getAccount, userSignDto.getAccount())) > 0){
         return Result.fail(BusinessErrorCode.EXISTING_ACCOUNT);
      }

      SysUser sysUser = BeanUtil.toBean(userSignDto, SysUser.class);

      sysUser.setSalt(RandomUtil.randomString(16));

      sysUser.setPassword(AesUtil.encryptHex(sysUser.getPassword(),sysUser.getSalt()));

      sysUser.setDeleteFlag(DeleteFlag.NOT_DELETE.getValue());

      if(this.save(sysUser)){
         return Result.ok(GlobalConstants.signUpMsg);
      }

      return Result.fail(BusinessErrorCode.SYS_ERROR);
   }
}
