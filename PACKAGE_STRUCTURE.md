# 패키지 구조 예시
## /interfaces
- ### /user
    - UserController
    - UserResponse.java
    - UserRequest.java<br>
---
## /application
- ### /user
    - UserFacade.java (UserApp.java, UserUsecase.java)
    - UserResult.java
    - UserCriteria.java
---
## /domain
- ### /user
    - UserInfo.java
    - User.java // POJO 객체
    - UserCommand.java
    - UserService.java
    - UserRepository.java
---
## /infrastructure
- ### /user
    - UserJpaRepository.java
    - UserRepositoryImpl.java
    - UserEntity.java // @Entity 객체
    - UserParams.java