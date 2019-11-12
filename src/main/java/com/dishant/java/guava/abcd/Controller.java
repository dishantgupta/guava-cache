package com.dishant.java.guava.abcd;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping
public class


Controller {

	@Autowired
	private LoadingCache<String, List<GuavaEntity>> guavaCacheConfig;

	@Autowired
	private GuavaRepository guavaRepository;

	@GetMapping
	public List<GuavaEntity> getDealerTokensByCode(@RequestParam Set<String> dealerCodes) throws Exception {
		List<GuavaEntity> appUserTokenEntities = new ArrayList<>();
		Map<String, List<GuavaEntity>> immutableResultMap = null;
		Map<String, List<GuavaEntity>> mutableResultMap = null;
		try {
			immutableResultMap = guavaCacheConfig.getAll(dealerCodes);
			mutableResultMap = removeNullValues(immutableResultMap);
			mutableResultMap.values().iterator().forEachRemaining(appUserTokenEntities::addAll);
		} catch (ExecutionException | CacheLoader.InvalidCacheLoadException e) {
			System.out.println((e.getMessage()));
		}
		if (null == mutableResultMap || mutableResultMap.values().size() == 0){
			throw new Exception(String.format("dealer codes: %s not found.", String.valueOf(dealerCodes)));
		}
		return appUserTokenEntities;
	}


	private Map removeNullValues(Map immutableMap){
		Map hashMap = new HashMap();
		hashMap.putAll(immutableMap);
		List nullKeys = new ArrayList();
		for (Object key: hashMap.keySet()){
			if ("" == hashMap.getOrDefault(key, "")){
				nullKeys.add(key);
			}
		}
		nullKeys.forEach(key -> hashMap.remove(key));
		return hashMap;
	}
}
