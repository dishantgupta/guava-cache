package com.dishant.java.guava.abcd;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class GuavaCacheConfig {

	@Autowired
	private GuavaRepository guavaRepository;

	@Bean
	public LoadingCache<String, List<GuavaEntity>> cache(){
		LoadingCache<String, List<GuavaEntity>> guavaCache = CacheBuilder.newBuilder()
				.expireAfterAccess(10, TimeUnit.SECONDS)
				.build(new CacheLoader<String, List<GuavaEntity>>() {  // build the cacheloader
					@Override
					public List<GuavaEntity> load(String id) throws Exception {
						throw new Exception("method not implemented");
					}

					@Override
					public Map<String, List<GuavaEntity>> loadAll(Iterable<? extends String> keys) {
						HashSet<String> keySet = new HashSet<>();
						Map<String, List<GuavaEntity>> resultMap = new HashMap<>();
						keys.forEach(key -> keySet.add(key));
						List<GuavaEntity> GuavaEntityList = guavaRepository.method1(keySet);
						log.debug("getDealerTokensByCode {}", keys);
						List<GuavaEntity> tempList;
						for (GuavaEntity GuavaEntity : GuavaEntityList) {
							tempList = resultMap.getOrDefault(GuavaEntity.getCode(),new ArrayList<>());
							tempList.add(GuavaEntity);
							resultMap.put(GuavaEntity.getCode(), tempList);
						}
						updateNullKeys(resultMap, keys);
						return resultMap;
					}
				});
		return guavaCache;
	}

	private void updateNullKeys(Map hashMap, Iterable keys){
		for (Object key: keys){
			if (!hashMap.keySet().contains(key)){
				hashMap.put(key, "");
			}
		}
	}
}
