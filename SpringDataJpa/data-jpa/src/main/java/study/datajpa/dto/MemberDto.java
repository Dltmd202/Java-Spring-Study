package study.datajpa.dto;

import lombok.Data;

@Data
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String testName) {
        this.id = id;
        this.username = username;
        this.teamName = testName;
    }
}
