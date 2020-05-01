package com.clinbox.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentHystrixService {
    public String get_ok(Integer id){
        return "当前线程："+Thread.currentThread().getName()+"   payment_ok: id "+"\t"+id;
    }
    @HystrixCommand(fallbackMethod = "get_timeoutHandler", commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value = "5000")
    })
    public String get_timeout(Integer id){
        int time = 3000;
        try {
             TimeUnit.MILLISECONDS.sleep(time);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
        return "当前线程："+Thread.currentThread().getName()+"   payment_timeout: id "+"\t"+id+"   休眠时间："+time;
       /* int age = 10/0;
        return "当前线程："+Thread.currentThread().getName()+"   当前年龄为: id "+"\t"+id;*/
    }
    public String get_timeoutHandler(Integer id){
        return "当前线程："+Thread.currentThread().getName()+"   8001系统繁忙:   id "+"\t"+id+"    服务调用失败";
    }
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")
    })
    public String paymentCircuitBreaker(Integer id){
        if(id < 0){
            throw new RuntimeException("*******id 不能为负数");
        }
        String SerialNumber = IdUtil.simpleUUID();
        return Thread.currentThread().getName()+"\t"+" 调用成功，流水号："+SerialNumber;
    }
    public String paymentCircuitBreaker_fallback(Integer id){
        return "id 不能为负数，请稍后再试！   id: "+id;
    }
}
