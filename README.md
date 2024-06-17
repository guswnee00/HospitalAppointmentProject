# Hospital Appointment Service
병원 예약 서비스
## 🏥 프로젝트 소개

환자가 받고싶은 진료에 대한 진료과목을 선택하여 병원을 예약할 수 있는 병원 예약 서비스입니다.
*****

## ⚙️ 개발 환경
- Java 17
- SpringBoot 3.2.5
- Gradle 8.7
- Spring Data JPA
- Spring Security(JWT)
- MySQL
- Elasticsearch
- CoolSMS API
- Swagger 2.2.0
- Ehcache 3.8.1
- Junit
- Mockito
- IntelliJ Idea
*****

## 🧩 프로젝트 기능 및 설계

### 회원 가입 및 로그인
- 병원 검색을 제외한 기능을 이용하기 위해서는 회원가입과 로그인을 해야한다.   
- 환자는 `ROLE_PATIENT`, 의사는 `ROLE_DOCTOR`, 병원 관계자는 `ROLE_STAFF`의 **PersonRole**을 가진다.

### 환자 Patient
- 환자는 본인의 개인 정보를 조회 / 수정 / 삭제할 수 있다.
- 환자는 본인의 예약 정보를 조회 / 수정 / 삭제할 수 있다.
  - 단, 수정과 삭제는 예약상태가 확정이 아닐 때(**예약대기** 상태) 가능
- 환자는 본인의 진료 기록을 조회할 수 있다.
  - 날짜, 병원이름, 진료 과목별로 조회 가능

### 의사 Doctor
- 의사는 본인의 개인 정보를 조회 / 수정 / 삭제할 수 있다.
- 의사는 본인 앞으로 생성된 진료 예약을 확인할 수 있다.
- 의사는 환자의 진료기록을 작성할 수 있다.
- 의사는 작성했던 모든 진료 기록을 조회 / 수정할 수 있다.

### 병원 관계자 Staff
- 병원 관계자는 본인의 개인 정보를 조회 / 수정할 수 있다.
  - 병원 관계자가 병원을 관두는 경우 아이디는 삭제하지 않고 후임 관계자의 정보로 수정한 뒤 사용
- 병원 관리자는 자신의 병원을 등록할 수 있다.
- 병원 관리자는 담당 병원에 대해 모든 예약을 조회할 수 있다.
- 병원 담당자는 예약 상태를 변경할 수 있다. 
  - 오전 9시에 자동으로 처리하는 것으로 변경
    - 예약 일자 3일전 예약들에 대해 예약 상태 자동 변경
  - 예약을 확인한 후 **예약대기** 상태에서 **예약확정** 상태로 변경

### 병원 Hospital
- 병원을 조회할 수 있다.
  - 병원 이름 또는 진료 과목으로 검색
  - 사용자 위치 근처의 병원 검색
- 병원의 진료과목을 조회할 수 있다.
  
### 예약 Appointment
- 예약시 환자는 원하는 병원, 진료 과목, 진료 날짜와 시간을 작성한다.
  - 환자가 예약을 생성하거나 수정하면 문자로 예약에 대한 내용이 전송
- 환자는 진료 예약 시간 15분 전에 도착해서 접수를 해야한다.
  - 접수가 되면 **예약확정** 상태에서 **진료대기** 상태로 변경
  - 15분 전에 도착하지 못하면 **예약확정** 상태에서 **예약취소** 상태로 변경
  
### 진료 기록 Medical Record
- 의사는 환자의 진료가 끝나면 진료 기록을 작성한다.
  - 진료가 시작되고 일정 시간이 지나면 자동으로 **진료대기** 상태에서 **진료완료** 상태로 변경

*****

## 🔗 URI 설계
### 환자
- 회원가입 : `signup/patient`
- 개인 정보 확인 : `patient/my-info`
- 개인 정보 수정 : `patient/my-info`
- 개인 정보 삭제 : `patient/my-info`


### 의사
- 회원가입 : `signup/doctor`
- 개인 정보 확인 : `doctor/my-info`
- 개인 정보 수정 : `doctor/my-info`
- 개인 정보 삭제 : `doctor/my-info`


### 병원 관계자
- 회원가입 : `signup/staff`
- 개인 정보 확인 : `staff/my-info`
- 개인 정보 수정 : `staff/my-info`


### 병원
- 병원 관계자의 병원 등록 : `/staff/my-hospital`
- 병원 관계자의 병원 수정 : `/staff/my-hospital`
- 병원 관계자의 병원 삭제 : `/staff/my-hospital`


- 병원의 진료과목 목록 검색 : `/search/{hospitalName}/specialties`
- 진료과목으로 병원 검색 : `/search/specialty-name`
- 병원 이름으로 병원 검색 : `/search/hospital-name`
- 위치로 근처의 병원 검색 : `/search/location`


### 예약
- 환자의 병원 예약 생성 : `/patient/my-appointment`
- 환자의 병원 예약 수정 : `/patient/my-appointment/{appointmentId}`
- 환자의 병원 예약 취소 : `/patient/my-appointment/{appointmentId}`
- 환자의 병원 예약 목록 확인 : `/patient/my-appointments`
- 환자의 병원 도착 확인 : `/patient/check-arrival/{appointmentId}`


- 병원 관계자의 예약 목록 확인 : `/staff/my-hospital-appointments`


- 의사의 예약 목록 확인 : `/doctor/my-appointments`

### 진료 기록
- 의사의 진료기록 생성 : `/doctor/medical-record`
- 의사의 진료기록 수정 : `/doctor/medical-record/{medicalRecordId}`
- 의사의 진료기록 조회 : `/doctor/medical-record`


- 환자의 진료기록 조회 : `/patient/medical-record`
### 로그인
- 환자 로그인 : `login/patient`
- 의사 로그인 : `login/doctor`
- 병원 관계자 로그인 : `login/staff`
*****

## 🗂️ ERD
![ERD](https://github.com/guswnee00/HospitalAppointmentProject/assets/124776145/5a0f8e4d-3ac6-46c3-9443-df23d0b8ca7c)
