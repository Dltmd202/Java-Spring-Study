package com.example.servlet3.domain.member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 동시성 문제가 고려되어 있지 않음, ConcurrentHashMap, AtomicLong 사용 고려
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    private static final MemberRepository intstance = new MemberRepository();

    public static MemberRepository getInstance(){
        return intstance;
    }

    public Member save(Member member){
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }


    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }

    public Member findById(Long id){
        return store.get(id);
    }
}
