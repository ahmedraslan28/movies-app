package com.raslan.controller;

import com.raslan.dto.rate.RateRequest;
import com.raslan.exception.UnauthorizedActionException;
import com.raslan.model.User;
import com.raslan.service.rate.RateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("api/movies/rate")
@RequiredArgsConstructor
public class RateController {
    private final RateService rateService;

    @PostMapping("")
    public Map<String, Object> addRate(@Valid @RequestBody RateRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;

        if(user.getId() != request.getUserId()){
            throw new UnauthorizedActionException("You are not allowed to rate on behalf of another user.");
        }

        return rateService.addRate(request);
    }
}
