package com.clinbox.springcloud.controller;

import com.clinbox.springcloud.entities.CommonResult;
import com.clinbox.springcloud.entities.Payment;
import com.clinbox.springcloud.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.Year;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;
    @Value("${server.port}")
    private String serverPort;
    @Resource
    private DiscoveryClient discoveryClient;

    @PostMapping(value ="/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("****插入结果: " + result);
        if(result > 0){
            return new CommonResult(200,"插入成功",result);
        }else{
            return new CommonResult(444,"插入失败",null);
        }
    }

    @GetMapping(value ="/payment/get/{id}")
    public CommonResult getPayment(@PathVariable("id") Long id){
        Payment result = paymentService.getPaymentById(id);
        log.info("****查询结果: " + result);
        if(result != null){
            return new CommonResult(200,"查询成功,端口："+serverPort,result);
        }else{
            return new CommonResult(444,"查询失败，没有对应id："+id,null);
        }
    }

    @GetMapping("/payment/discovery")
    public Object getClient(){
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }
        List<String> services = discoveryClient.getServices();
        for(String service:services){
            log.info(service);
        }
        return this.discoveryClient;

    }

    @GetMapping("/payment/lb")
    @HystrixCommand(fallbackMethod = "getPaymentLBHandler")
    public String getPaymentLB(){
        return serverPort;
    }

    public String getPaymentLBHandler(){
        return "error page";
    }

    @GetMapping("/payment/feign/timeout")
    public String paymentFeignTimeout(){
        try {
             TimeUnit.SECONDS.sleep(3);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
        return serverPort;
    }
}
