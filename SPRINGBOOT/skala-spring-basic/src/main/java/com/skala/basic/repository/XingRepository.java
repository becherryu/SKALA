package com.skala.basic.repository;

import java.util.Arrays;
import java.util.List;

import com.skala.basic.data.XingEntity;

public class XingRepository {

    private static final List<XingEntity> XING_LIST = Arrays.asList(
            new XingEntity(1L, "판교역 횡단보도", "경기도 성남시 분당구 판교역로 166", 37.3949, 127.1115, 30),
            new XingEntity(2L, "알파돔시티 앞 횡단보도", "경기도 성남시 분당구 판교로 242", 37.4020, 127.1080, 40),
            new XingEntity(3L, "판교테크노밸리 횡단보도", "경기도 성남시 분당구 대왕판교로 645", 37.4015, 127.1050, 50),
            new XingEntity(4L, "정자역 횡단보도", "경기도 성남시 분당구 정자일로 19", 37.3596, 127.1079, 60),
            new XingEntity(5L, "서현역 횡단보도", "경기도 성남시 분당구 서현로180번길 10", 37.3851, 127.1232, 30),
            new XingEntity(6L, "이매역 횡단보도", "경기도 성남시 분당구 이매로 30", 37.3902, 127.1271, 50),
            new XingEntity(7L, "야탑역 횡단보도", "경기도 성남시 분당구 야탑로81번길 11", 37.4111, 127.1279, 40),
            new XingEntity(8L, "판교 현대백화점 앞 횡단보도", "경기도 성남시 분당구 판교역로 146", 37.3938, 127.1106, 30),
            new XingEntity(9L, "네이버 본사 앞 횡단보도", "경기도 성남시 분당구 불정로 6", 37.3595, 127.1053, 40),
            new XingEntity(10L, "카카오 판교오피스 앞 횡단보도", "경기도 성남시 분당구 판교역로 235", 37.3943, 127.1114, 30)
    );

    public List<XingEntity> findAll() {
        return XING_LIST;
    }

    public XingEntity findById(Long id) {
        return XING_LIST.stream()
                .filter(xing -> xing.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<XingEntity> findByNames(String[] names) {
        return XING_LIST.stream()
                .filter(xing -> Arrays.stream(names).anyMatch(name -> xing.getName().contains(name)))
                .toList();
    }
}
