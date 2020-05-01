package com.clinbox.springcloud.controller;

import com.clinbox.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "get_global_fallbackMethod")
public class OrderHystrixController {
    @Resource
    PaymentHystrixService paymentHystrixService;
    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    public String get_ok(@PathVariable("id") Integer id){
        return paymentHystrixService.get_ok(id);
    }
    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
  /*  @HystrixCommand(fallbackMethod = "get_timeoutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "2900")
    })*/
    @HystrixCommand
    public String get_timeout(@PathVariable("id") Integer id){
        int age = 10/0;
        return paymentHystrixService.get_timeout(id);
    }
    public String get_timeoutHandler(@PathVariable("id") Integer id){
        return "我是消费方80,请等待10秒后重试！";
    }
    public String get_global_fallbackMethod(){
        return "Global 异常处理信息，请稍后重试！";
    }
}
