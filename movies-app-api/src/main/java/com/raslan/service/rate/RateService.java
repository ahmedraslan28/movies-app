package com.raslan.service.rate;

import java.util.Map;

public interface RateService {
    Map<String, Object> addRate(Integer userId, Integer movieId, Integer rateValue);

    Map<String, Object> getRate(Integer userId, Integer movieId);
}
