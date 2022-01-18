package org.example.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @Author: ShuaiYu_Jia
 * @Data: 2022/1/17
 * @Description:
 */

/**
 * @Component代表当前类为spring的Bean
 */
@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    //实现校验并返回校验结果
    public ValidationResult validate(Object bean){
        final ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
        if(constraintViolationSet.size()>0)
        {
//            有错误
            result.setHasErrors(true);
            constraintViolationSet.forEach(objectConstraintViolation -> {
//                错误信息
                String errMsg = objectConstraintViolation.getMessage();
//                错误路径
                String propertyName = objectConstraintViolation.getPropertyPath().toString();
                result.getErrorMsgMap().put(propertyName, errMsg);
            });

        }
        return result;
    }


//    spring中的bean初始化完成后，会回调afterPropertiesSet()
    @Override
    public void afterPropertiesSet() throws Exception {
//        将hibernate validator通过工厂的初始化方式使其实例化
        this.validator = (Validator) Validation.buildDefaultValidatorFactory().getValidator();
    }
}
