# Hospital Appointment Service
병원 예약 서비스
## 🏥 프로젝트 소개

환자가 받고싶은 진료에 대한 진료과목을 선택하여 병원을 예약할 수 있는 병원 예약 서비스입니다.
*****

## ⚙️ 개발 환경
- Java 17, SpringBoot 3.2.5, gradle 8.7
- Spring data JPA, Spring Security(JWT)
- MySQL
- IntelliJ Idea
*****

## 🧩 프로젝트 기능 및 설계

### 회원 가입 및 로그인
병원 예약을 하기 위해서는 회원가입과 로그인을 해야한다.   
환자는 `ROLE_PATIENT`, 의사는 `ROLE_DOCTOR`, 병원 관계자는 `ROLE_STAFF`의 **PersonRole**을 가진다.

### 환자 Patient
- 환자는 본인의 개인 정보를 조회 / 수정 / 삭제할 수 있다.
- 환자는 본인의 예약 정보를 조회 / 수정 / 삭제할 수 있다.
  - 단, 수정과 삭제는 예약상태가 확정이 아닐 때(**예약대기** 상태) 가능
- 환자는 본인의 진료 기록을 조회할 수 있다.
  - 날짜, 병원이름, 진료 과목별로 조회 가능

### 의사 Doctor
- 의사는 환자의 진료기록을 작성할 수 있다.
- 의사는 본인의 개인 정보를 조회 / 수정 / 삭제할 수 있다.
- 의사는 작성했던 모든 진료 기록을 조회 / 수정할 수 있다.

### 병원 관계자 Staff
- 병원 관계자는 본인의 개인 정보를 조회 / 수정 / 삭제할 수 있다.
- 병원 관리자는 담당 병원을 등록할 수 있다.
- 병원 관리자는 담당 병원에 대해 모든 예약을 조회하고 예약 상태를 변경할 수 있다.
  - 예약을 확인한 후 **예약대기** 상태에서 **예약확정** 상태로 변경

### 병원
- 환자는 병원을 조회할 수 있다.
  - 병원 이름 또는 진료 과목으로 검색
  
### 예약
- 예약시 환자는 원하는 병원, 진료 과목, 진료 날짜와 시간을 작성한다.
  - 해당 병원과 진료과목에 대해 진료 기록이 있을 경우 동일한 의사 배정
  - 진료 기록이 없을 경우 진료 날짜와 시간에 가능한 의사 배정
- 환자는 진료 예약 시간 15분 전에 도착해서 접수를 해야한다.
  - 접수가 되면 **예약확정** 상태에서 **진료대기** 상태로 변경
  - 15분 전에 도착하지 못하면 **예약확정** 상태에서 **예약취소** 상태로 변경
  
### 진료 기록
- 의사는 환자의 진료가 끝나면 진료 기록을 작성한다.
  - 진료 기록이 작성된 경우 **진료대기** 상태에서 **진료완료** 상태로 변경

*****

## 🔗 URI 설계
### 환자
#### 회원가입 및 로그인
- 회원가입 : `signup/patient`
- 로그인 : `signin/patient`
#### 개인 정보
- 조회 / 수정 / 삭제 : `/patient/{patientId}`
#### 예약 정보
- 조회 / 수정 / 삭제 : `/patient/appointment/{patientId}`
#### 진료 기록
- 진료 기록 작성 : 
- 진료 기록 조회 : 
- 진료 기록 수정 : 

### 의사
#### 회원가입 및 로그인
- 회원가입 : `signup/doctor`
- 로그인 : `signin/doctor`
#### 개인 정보
- 조회 / 수정 / 삭제 : `/doctor/{doctorId}`
#### 진료 기록
- 진료 기록 작성 : 
- 진료 기록 조회 : 
- 진료 기록 수정 : 

### 병원 관계자
#### 회원가입 및 로그인
- 회원가입 : `signup/staff`
- 로그인 : `signin/staff`
#### 개인 정보
- 조회 / 수정 / 삭제 : `/staff/{staffId}`
#### 예약 정보
- 예약 정보 조회 : 
- 예약 확정 : 
*****

## 🗂️ ERD
![이미지 2024  5  18  오후 5 25](https://github.com/guswnee00/HospitalAppointmentProject/assets/124776145/2b420e99-858b-421c-877e-3980cdfc49e7)

