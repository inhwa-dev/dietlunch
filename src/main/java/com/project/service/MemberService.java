
package com.project.service;

import com.project.dto.MemberDTO;
import java.util.*;

public class MemberService {
    ArrayList<MemberDTO> members = new ArrayList<>();

    // 회원가입
    public boolean signup(String id, String pw, double h, double w, String goal, int cal) {
        for (MemberDTO m : members) {
            if (m.getMemberId().equals(id)) return false;
        }

        MemberDTO m = new MemberDTO();
        m.setMemberId(id);
        m.setPassword(pw);
        m.setHeight(h);
        m.setWeight(w);
        m.setTargetGoal(goal);
        m.setTargetCalories(cal);
        members.add(m);
        return true;
    }

    // 로그인
    public MemberDTO login(String id, String pw) {
        for (MemberDTO m : members) {
            if (m.getMemberId().equals(id) && m.getPassword().equals(pw))
                return m;
        }
        return null;
    }
}
