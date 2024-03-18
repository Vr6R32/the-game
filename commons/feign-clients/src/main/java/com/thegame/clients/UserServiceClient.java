package com.thegame.clients;


import com.thegame.dto.AppUserAuthDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        value = "user-service"
//        url = "${clients.user-service.url}"
//        configuration = FeignConfiguration.class
)
public interface UserServiceClient {

    @GetMapping(path = "api/v1/users/{username}")
    AppUserAuthDTO getUserByName(@PathVariable("username") String username);

}
