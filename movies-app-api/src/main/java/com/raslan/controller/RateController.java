package com.raslan.controller;

import com.raslan.dto.rate.RateRequest;
import com.raslan.model.User;
import com.raslan.service.rate.RateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/movies/{movieId}/rate")
@RequiredArgsConstructor
public class RateController {
    private final RateService rateService;

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addRate(@Valid @RequestBody RateRequest request,
                                                       @PathVariable Integer movieId,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return ResponseEntity.ok(rateService.addRate(user.getId(), movieId, request.getRate()));
    }


    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getRate(@PathVariable Integer movieId,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return ResponseEntity.ok(rateService.getRate(user.getId(), movieId));
    }
}
