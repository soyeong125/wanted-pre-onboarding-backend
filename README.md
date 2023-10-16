# 10월 프리온보딩 백엔드 인턴십 선발과제

## 1. 요구사항 정리
1. 채용공고를 등록한다.
   -  고려사항1: 회사는 이미 DB에 등록되어 있다고 가정한다.
   -  고려사항2: 회사는 이미 로그인 되어 있다고 가정한다.
   -  2가지 고려사항은 3,4 요구사항에도 적용된다.
3. 채용공고를 수정한다.
   - 고려사항: 회사 id는 수정할 수 없다.
4. 채용공고를 삭제한다.
5. 채용공고 전체 목록을 조회한다.
   - 고려사항: 채용 내용은 불러오지 않는다.
6. 채용 공고를 검색한다.
7. 채용 상세 페이지를 조회한다.
    - 채용 내용을 추가로 조회한다.
    - 회사가 올린 다른 채용 공고를 함께 조회한다.
8. 사용자는 채용공고에 지원한다.
    - 사용자는 채용공고에 중복지원 할 수 없다. 
    - 고려사항1: 사용자는 이미 DB에 등록되어 있다고 가정한다.
    - 고려사항2: 사용자는 이미 로그인 되어 있다고 가정한다.

## 2. DB 모델링
![image](https://github.com/soyeong125/wanted-pre-onboarding-backend/assets/57309311/94a749df-5f94-4a50-b6cf-2d71159aaaad)


## 3. API 설계
|No.|Funtion|URI|HTTP|
|--|-------|---|----|
|1|채용공고 등록|/recruitment|POST|
|2|채용공고 수정|/recruitment/{id}|PATCH|
|3|채용공고 삭제|/recruitment/{id}|DELTET|
|4|채용공고 목록 조회|/recruitment/|GET|
|5|채용공고 검색 |/recruitment?search={keyword}|GET|
|6|채용공고 상세 조회|/recruitment/{id}|GET|
|7|채용공고 지원|/apply|POST|

## 4. 구현 과정
### 1. 체용공고 등록
- 채용공고 등록을 요청한 회사가 있는지 먼저 조회한다. 없으면 오류가 발생한다.</br>
- 회사와 채용공고의 연관관계는 매핑되어있다.</br>
   - 회사가 저장한 공고는 회사의 recruitList 에 저장된다.
   
![image](https://github.com/soyeong125/wanted-pre-onboarding-backend/assets/57309311/5a68b0c2-70e5-4cb2-a33d-c084ac4e37ba)

### 2. 채용공고 수정
- 수정할 채용공고가 먼저 있는지 조회한다. 없으면 오류가 발생한다.</br>
- Dirty Checking을 활용하기 위해 Recruit Entity에 수정 로직을 구현했다.

```
    public void updateInfo(String position, int compensation, String location, String content, String skill) {
        this.position = position;
        this.compensation = compensation;
        this.location = location;
        this.content = content;
        this.skill = skill;
    }
```
![image](https://github.com/soyeong125/wanted-pre-onboarding-backend/assets/57309311/71cdd254-c247-4604-869d-91ff2251e2f7)

### 3. 채용공고 삭제
- 삭제할 채용공고가 먼저 있는지 조회한다. 없으면 오류가 발생한다.

![image](https://github.com/soyeong125/wanted-pre-onboarding-backend/assets/57309311/eb2b33b6-f431-4e21-87c2-65b0e60ac9b3)

### 4. 채용공고 목록 조회 및 검색 기능
- 전체 목록 조회와 검색 조회의 기능을 파라미터 여부를 사용해 함께 구현했다.

```
    @GetMapping
    public List<RecruitResponse> getRecruitments(@RequestParam(required = false) String keyword) {
        return recruitService.getRecruitments(keyword);
    }
```

- keyword 파라미터가 없으면 전체 목록을 조회한다. (cotent는 결과값에 포함되지 않는다.)</br>
- keyword 파라미터가 있으면 해당하는 목록을 조회한다.(cotent는 결과값에 포함되지 않는다.)</br>
- keyword로 검색가능한 항목은 회사이름, 채용포지션, 사용기술이다.
  
```
    @Query("""
            SELECT r FROM Recruit r 
            WHERE r.company.name like %:keyword% 
               OR r.position like %:keyword% 
               OR r.skill like %:keyword%
            """)
```
![image](https://github.com/soyeong125/wanted-pre-onboarding-backend/assets/57309311/6bd317a6-e7d9-447c-a695-cc1a03e42bde)

![image](https://github.com/soyeong125/wanted-pre-onboarding-backend/assets/57309311/4086e695-1bc0-4cfb-8e1e-fb069ad3bc50)

### 5. 채용공고 상세 조회
- 상세 조회할 채용공고가 먼저 있는지 조회한다. 없으면 오류가 발생한다.</br>
- 채용 내용을 추가적으로 포함한다.</br>
- 해당 회사가 올린 다른 채용공고 리스트를 추가적으로 포함한다.</br>
- 회사와 채용공고가 연관관계로 매핑되어 있기 때문에, recruitList 를 불러오도록 구현했다.</br>
- 이때 현재 채용공고의 id는 제외하고 나머지 리스트만 불러오도록 구현했다.

```
    public static RecruitDetailResponse buildEntity (Recruit recruit) {

        List<Long> list = recruit.getCompany().getRecruitList() //회사가 소유한 recruitList 조회
                    .stream()
                    .filter(rlist -> !rlist.getId().equals(recruit.getId())) //현재공고 제외
                    .map(Recruit::getId)
                    .toList();

        return builder()
                .recruitId(recruit.getId())
                .companyName(recruit.getCompany().getName())
                .location(recruit.getLocation())
                .position(recruit.getPosition())
                .compensation(recruit.getCompensation())
                .content(recruit.getContent())
                .skill(recruit.getSkill())
                .recruitList(list)
                .build();
    }
```

![image](https://github.com/soyeong125/wanted-pre-onboarding-backend/assets/57309311/a5683564-cee9-42cd-9817-ec28129e6167)

### 7. 채용공고 지원
- 공고에 지원할 사용자가 먼저 있는지 조회한다. 없으면 오류가 발생한다.
- 사용자가 지원할 공고가 있는지 먼저 조회한다. 없으면 오류가 발생한다.
- 사용자가 이전에 지원한 내역이 있는지 확인한다.
- 지원한 내역이 있다면 "이미 지원하셨습니다." 라는 오류 메세지를 출력한다.

![image](https://github.com/soyeong125/wanted-pre-onboarding-backend/assets/57309311/eb424d9c-34aa-451b-a544-f177a4586564)
![image](https://github.com/soyeong125/wanted-pre-onboarding-backend/assets/57309311/bb7fc214-edc6-4d8e-8759-500fdd394e11)

