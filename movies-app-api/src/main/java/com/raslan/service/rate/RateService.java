package com.raslan.service.rate;

import com.raslan.dto.rate.RateRequest;
import com.raslan.model.Rate;

import java.util.Map;

public interface RateService {
    Map<String, Object> addRate(Integer userId, Integer movieId, Integer rateValue);

    Map<String, Object> getRate(Integer userId, Integer movieId);
}
