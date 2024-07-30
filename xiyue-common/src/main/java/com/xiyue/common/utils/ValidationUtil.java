package com.xiyue.common.utils;

import com.xiyue.common.enums.BusinessErrorCode;
import com.xiyue.common.vo.Result;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class ValidationUtil {


    public static Result<Object> checkParam(BindingResult bindingResult){
        if (bindingResult != null) {
            //获取校验内容是否为空
            if (bindingResult.hasErrors()) {
                List<ObjectError> errorList = bindingResult.getAllErrors();
                List<String> resultList = new ArrayList<>();
                for (ObjectError error : errorList) {
                    //获取所有校验错误信息
                    resultList.add(error.getDefaultMessage());
                }
                //将错误信息返回
                return Result.fail(BusinessErrorCode.ARGS_VALID_ERROR, String.join(",", resultList));
            }
        }
        return Result.ok();
    }

}
