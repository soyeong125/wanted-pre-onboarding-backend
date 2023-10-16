package com.example.wanted.controller;

import com.example.wanted.domain.apply.Apply;
import com.example.wanted.domain.apply.ApplyRepository;
import com.example.wanted.domain.company.Company;
import com.example.wanted.domain.company.CompanyRepository;
import com.example.wanted.domain.recruit.Recruit;
import com.example.wanted.domain.recruit.RecruitRepository;
import com.example.wanted.domain.user.User;
import com.example.wanted.domain.user.UserRepository;
import com.example.wanted.dto.apply.request.ApplyRequest;
import com.example.wanted.dto.recruit.request.RecruitRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RecruitRepository recruitRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplyRepository applyRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestRestTemplate restTemplate;

    private MockMvc mvc;

    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();


        // Company 저장
        Company company = new Company("원티드랩");
        companyRepository.save(company);

        //사용자 저장
        User user = new User("사용자");
        userRepository.save(user);

    }

    @AfterEach
    public void tearDown() throws Exception{
        applyRepository.deleteAll();
        recruitRepository.deleteAll();
    }

    @Test
    public void 채용_지원() throws Exception {

        String position = "백앤드 주니어 개발자";
        String content = "원티드랩은 차별화된 데이터를 기반으로 각자에게 가장 잘 맞는 커리어 경로를 설계하고 커리어 성장 경험을 제공하여, 모두가 나답게 일하고 즐겁게 성장할 수 있도록 돕는 HR 테크 회사입니다.  우리는 서로 다른 백그라운드를 가진 동료들과 지난 6년간 매년 매출을 약 2배씩 성장시켜왔습니다. 채용을 넘어, 커리어, 긱스, HR솔루션, 글로벌 등  라이프 커리어 플랫폼으로 도약을 함께할 분들을 모십니다.";
        String skill = "Java";

        Company company =  companyRepository.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        Recruit recruit = Recruit.builder()
                .company(company)
                .position(position)
                .compensation(100000)
                .content(content)
                .skill("Python")
                .build();

        recruitRepository.save(recruit);

        ApplyRequest requestDto = ApplyRequest.builder()
                .userId(1L)
                .recruitId(recruit.getId())
                .build();

        String url = "http://localhost:" + port + "/apply";


        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        List<Apply> all = applyRepository.findAll();
        assertThat(all.get(0).getUser().getName()).isEqualTo("사용자");
        assertThat(all.get(0).getRecruit().getContent()).isEqualTo(content);

    }

    @Test
    public void 채용_중복_지원() throws Exception {

        String position = "백앤드 주니어 개발자";
        String content = "원티드랩은 차별화된 데이터를 기반으로 각자에게 가장 잘 맞는 커리어 경로를 설계하고 커리어 성장 경험을 제공하여, 모두가 나답게 일하고 즐겁게 성장할 수 있도록 돕는 HR 테크 회사입니다.  우리는 서로 다른 백그라운드를 가진 동료들과 지난 6년간 매년 매출을 약 2배씩 성장시켜왔습니다. 채용을 넘어, 커리어, 긱스, HR솔루션, 글로벌 등  라이프 커리어 플랫폼으로 도약을 함께할 분들을 모십니다.";
        String skill = "Java";

        Company company =  companyRepository.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        Recruit recruit = Recruit.builder()
                .company(company)
                .position(position)
                .compensation(100000)
                .content(content)
                .skill("Python")
                .build();

        recruitRepository.save(recruit);

        ApplyRequest requestDto = ApplyRequest.builder()
                .userId(1L)
                .recruitId(recruit.getId())
                .build();

        String url = "http://localhost:" + port + "/apply";


        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        Exception exception = assertThrows(Exception.class, () -> {
            mvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(requestDto)));
        });

        assertTrue(exception.getMessage().contains("이미 지원하셨습니다."));
    }
}
