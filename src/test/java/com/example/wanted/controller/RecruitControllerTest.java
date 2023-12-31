package com.example.wanted.controller;

import com.example.wanted.domain.company.Company;
import com.example.wanted.domain.company.CompanyRepository;
import com.example.wanted.domain.recruit.Recruit;
import com.example.wanted.domain.recruit.RecruitRepository;
import com.example.wanted.dto.recruit.request.RecruitRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecruitControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RecruitRepository recruitRepository;

    @Autowired
    private CompanyRepository companyRepository;

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


        // 사전에 데이터베이스에 2개의 Company 객체 저장
        Company company1 = new Company("원티드랩");
        companyRepository.save(company1);

        Company company2 = new Company("네이버");
        companyRepository.save(company2);

    }

    @AfterEach
    public void tearDown() throws Exception{
        recruitRepository.deleteAll();
    }

    @Test
    public void 채용공고_등록() throws Exception{

        String position = "백앤드 주니어 개발자";
        String content = "원티드랩은 차별화된 데이터를 기반으로 각자에게 가장 잘 맞는 커리어 경로를 설계하고 커리어 성장 경험을 제공하여, 모두가 나답게 일하고 즐겁게 성장할 수 있도록 돕는 HR 테크 회사입니다.  우리는 서로 다른 백그라운드를 가진 동료들과 지난 6년간 매년 매출을 약 2배씩 성장시켜왔습니다. 채용을 넘어, 커리어, 긱스, HR솔루션, 글로벌 등  라이프 커리어 플랫폼으로 도약을 함께할 분들을 모십니다.";

        RecruitRequest requestDto = RecruitRequest.builder()
                .companyId(1)
                .position(position)
                .compensation(100000)
                .content(content)
                .skill("Python")
                .build();

        String url = "http://localhost:" + port + "/recruitment";


        mvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(requestDto)))
                            .andExpect(status().isOk());

        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url,requestDto,Long.class);
        List<Recruit> all = recruitRepository.findAll();
        assertThat(all.get(0).getPosition()).isEqualTo(position);
        assertThat(all.get(0).getContent()).isEqualTo(content);

    }

    @Test
    public void 채용공고_수정() throws Exception{

        String position = "프론트앤드 주니어 개발자";
        String content = "원티드랩은 차별화된 데이터를 기반으로 각자에게 가장 잘 맞는 커리어 경로를 설계하고 커리어 성장 경험을 제공하여, 모두가 나답게 일하고 즐겁게 성장할 수 있도록 돕는 HR 테크 회사입니다.  우리는 서로 다른 백그라운드를 가진 동료들과 지난 6년간 매년 매출을 약 2배씩 성장시켜왔습니다. 채용을 넘어, 커리어, 긱스, HR솔루션, 글로벌 등  라이프 커리어 플랫폼으로 도약을 함께할 분들을 모십니다.";
        String skill = "React";

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

        RecruitRequest requestDto = RecruitRequest.builder()
                .companyId(1)
                .position(position)
                .compensation(100000)
                .content(content)
                .skill(skill)
                .build();

        String url = "http://localhost:" + port + "/recruitment/1";


        mvc.perform(patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        List<Recruit> all = recruitRepository.findAll();
        assertThat(all.get(0).getPosition()).isEqualTo(position);
        assertThat(all.get(0).getSkill()).isEqualTo(skill);

    }

    @Test
    public void 채용공고_삭제() throws Exception{

        String position = "백앤드 주니어 개발자";
        String content = "원티드랩은 차별화된 데이터를 기반으로 각자에게 가장 잘 맞는 커리어 경로를 설계하고 커리어 성장 경험을 제공하여, 모두가 나답게 일하고 즐겁게 성장할 수 있도록 돕는 HR 테크 회사입니다.  우리는 서로 다른 백그라운드를 가진 동료들과 지난 6년간 매년 매출을 약 2배씩 성장시켜왔습니다. 채용을 넘어, 커리어, 긱스, HR솔루션, 글로벌 등  라이프 커리어 플랫폼으로 도약을 함께할 분들을 모십니다.";

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


        String url = "http://localhost:" + port + "/recruitment/1";


        mvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        List<Recruit> all = recruitRepository.findAll();
        assertThat(all.stream().count()).isEqualTo(0);

    }

    @Test
    public void 채용공고_전체조회() throws Exception{

        String position = "백앤드 주니어 개발자";
        String content = "원티드랩은 차별화된 데이터를 기반으로 각자에게 가장 잘 맞는 커리어 경로를 설계하고 커리어 성장 경험을 제공하여, 모두가 나답게 일하고 즐겁게 성장할 수 있도록 돕는 HR 테크 회사입니다.  우리는 서로 다른 백그라운드를 가진 동료들과 지난 6년간 매년 매출을 약 2배씩 성장시켜왔습니다. 채용을 넘어, 커리어, 긱스, HR솔루션, 글로벌 등  라이프 커리어 플랫폼으로 도약을 함께할 분들을 모십니다.";

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


        String url = "http://localhost:" + port + "/recruitment";


        MvcResult mvcResult = mvc.perform(get(url)
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$[0].content").doesNotExist()) // content 필드가 포함되어 있지 않음을 확인
                            .andReturn();

        assertThat(mvcResult.getResponse()).isNotNull();

    }

    @Test
    public void 채용공고_검색조회() throws Exception{

        String position = "백앤드 주니어 개발자";
        String content = "원티드랩은 차별화된 데이터를 기반으로 각자에게 가장 잘 맞는 커리어 경로를 설계하고 커리어 성장 경험을 제공하여, 모두가 나답게 일하고 즐겁게 성장할 수 있도록 돕는 HR 테크 회사입니다.  우리는 서로 다른 백그라운드를 가진 동료들과 지난 6년간 매년 매출을 약 2배씩 성장시켜왔습니다. 채용을 넘어, 커리어, 긱스, HR솔루션, 글로벌 등  라이프 커리어 플랫폼으로 도약을 함께할 분들을 모십니다.";

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

        String keyword = "원티드";

        String url = "http://localhost:" + port + "/recruitment?" + "keyword=" + keyword;


        MvcResult mvcResult = mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].position").value(position)) // 첫 번째 항목의 position 값이 예상값과 일치하는지 확인
                        .andExpect(jsonPath("$[0].content").doesNotExist()) // content 필드가 포함되어 있지 않음을 확인
                        .andReturn();


    }

    @Test
    public void 채용공고_상세조회() throws Exception{

        String position = "백앤드 주니어 개발자";
        String content = "원티드랩은 차별화된 데이터를 기반으로 각자에게 가장 잘 맞는 커리어 경로를 설계하고 커리어 성장 경험을 제공하여, 모두가 나답게 일하고 즐겁게 성장할 수 있도록 돕는 HR 테크 회사입니다.  우리는 서로 다른 백그라운드를 가진 동료들과 지난 6년간 매년 매출을 약 2배씩 성장시켜왔습니다. 채용을 넘어, 커리어, 긱스, HR솔루션, 글로벌 등  라이프 커리어 플랫폼으로 도약을 함께할 분들을 모십니다.";

        Company company =  companyRepository.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        Recruit recruit1 = Recruit.builder()
                .company(company)
                .position(position)
                .compensation(100000)
                .content(content)
                .skill("Python")
                .build();

        Recruit recruit2 = Recruit.builder()
                .company(company)
                .position(position)
                .compensation(500000)
                .content(content)
                .skill("Java")
                .build();

        recruitRepository.save(recruit1);
        recruitRepository.save(recruit2);

        Long recruitId = recruit1.getId();

        String url = "http://localhost:" + port + "/recruitment/" + recruitId;


        mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position").value(position)) //position 값이 예상값과 일치하는지 확인
                .andExpect(jsonPath("$.content").value(content)) //content 값이 예상값과 일치하는지 확인
                .andExpect(jsonPath("$.recruitList").exists()) // recruitList 필드가 존재하는지 확인
                .andExpect(jsonPath("$.recruitList", hasSize(1))) // recruitList의 크기가 1인지 확인
                .andReturn();

    }

}
