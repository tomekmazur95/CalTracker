package com.crud.api.service.unit

import com.crud.api.dto.RequestMeasurementDTO
import com.crud.api.dto.RequestUserDTO
import com.crud.api.dto.ResponseMeasurementDTO
import com.crud.api.dto.ResponseUserDTO
import com.crud.api.entity.Measurement
import com.crud.api.entity.User
import com.crud.api.entity.UserInfo
import com.crud.api.enums.*
import com.crud.api.error.UserAlreadyExistsException
import com.crud.api.error.UserNotFoundException
import com.crud.api.mapper.*
import com.crud.api.repository.MeasurementRepository
import com.crud.api.repository.UserInfoRepository
import com.crud.api.repository.UserRepository
import com.crud.api.service.UserService
import org.springframework.data.domain.Sort
import spock.lang.Specification
import java.time.LocalDate


class UserServiceTest extends Specification {

    UserRepository userRepository = Mock()
    RequestUserMapper requestUserMapper = Mock()
    ResponseUserMapper responseUserMapper = Mock()
    RequestMeasurementMapper requestMeasurementMapper = Mock()
    MeasurementRepository measurementRepository = Mock()
    UserInfoRepository userInfoRepository = Mock()
    RequestUserActivityMapper requestUserActivityMapper = Mock()
    ResponseUserActivityMapper responseUserActivityMapper = Mock()
    UserService userService = new UserService(
            userRepository, requestUserMapper, responseUserMapper, requestMeasurementMapper,
            measurementRepository, userInfoRepository, requestUserActivityMapper, responseUserActivityMapper)

    def "positive path for createUser"() {
        given:
        def requestMeasurementDTO = new RequestMeasurementDTO(MeasureType.HEIGHT, 183, Unit.CENTIMETERS, LocalDate.now())
        def userInfoId = 2L
        def requestUserDTO = new RequestUserDTO("John", 30, Gender.MALE, Activity.MODERATELY_ACTIVE, requestMeasurementDTO)
        def userInfoFist = new UserInfo(2L, "john@gmail.com", "password", Role.USER)
        def userDomain = new User(1, "John", 30, Gender.MALE, Activity.MODERATELY_ACTIVE, userInfoFist)
        def measurementDomain = new Measurement(1L, MeasureType.HEIGHT, 183, Unit.CENTIMETERS, LocalDate.now(), userDomain)
        def responseMeasurementDTO = new ResponseMeasurementDTO(1L, MeasureType.HEIGHT, 183, Unit.CENTIMETERS, LocalDate.now())
        def responseUserDTO = new ResponseUserDTO(1L, "John", 30, Gender.MALE, Activity.MODERATELY_ACTIVE, responseMeasurementDTO)

        when:
        def result = userService.createUser(requestUserDTO, userInfoId)

        then:
        1 * userRepository.existsByUserInfoId(_ as Long) >> false
        1 * requestUserMapper.toDomain(_ as RequestUserDTO) >> userDomain
        1 * userInfoRepository.findById(_ as Long) >> Optional.of(userInfoFist)
        1 * userRepository.save(_ as User) >> userDomain
        1 * requestMeasurementMapper.toDomain(_ as RequestMeasurementDTO) >> measurementDomain
        1 * measurementRepository.save(_ as Measurement) >> measurementDomain
        1 * responseUserMapper.fromDomain(_ as User, _ as Measurement) >> responseUserDTO

        result == responseUserDTO
        result.getId() == responseUserDTO.getId()
        result.getUserName() == responseUserDTO.getUserName()
        result.getAge() == responseUserDTO.getAge()
        result.getGender() == responseUserDTO.getGender()
        result.getActivity() == responseUserDTO.getActivity()
        result.getHeight() == responseUserDTO.getHeight()
        result.getAge() == userDomain.getAge()
        result.getUserName() == userDomain.getUserName()
    }

    def "should throw IllegalArgumentException for createUser"() {
        given:
        def userInfoId = 1L

        when:
        userService.createUser(requestUserDTO, userInfoId)

        then:
        def exception = thrown(IllegalArgumentException)
        exception.message == "The given fields must not be null"

        verifyAll {
            0 * userRepository.existsByUserInfoId(_ as Long)
            0 * requestUserMapper.toDomain(_ as RequestUserDTO)
            0 * userInfoRepository.findById(_ as Long)
            0 * userRepository.save(_ as User)
            0 * requestMeasurementMapper.toDomain(_ as RequestMeasurementDTO)
            0 * measurementRepository.save(_ as Measurement)
            0 * responseUserMapper.fromDomain(_ as User, _ as Measurement)
        }

        where:
        requestUserDTO                                                                                                                                           | _
        null                                                                                                                                                     | _
        new RequestUserDTO(null, 30, Gender.MALE, Activity.EXTRA_ACTIVE, new RequestMeasurementDTO(MeasureType.HEIGHT, 180D, Unit.CENTIMETERS, LocalDate.now())) | _
    }

    def "should throw UserAlreadyExistsException for createUser"() {
        given:
        def userInfoId = 5L
        def requestMeasurementDTO = new RequestMeasurementDTO(MeasureType.HEIGHT, 183, Unit.CENTIMETERS, LocalDate.now())
        def requestUserDTO = new RequestUserDTO("John", 30, Gender.MALE, Activity.MODERATELY_ACTIVE, requestMeasurementDTO)

        when:
        userService.createUser(requestUserDTO, userInfoId)

        then:
        1 * userRepository.existsByUserInfoId(_ as Long) >> true
        def exception = thrown(UserAlreadyExistsException)
        exception.message == "User with user info id: " + userInfoId + " already exists"

        verifyAll {
            0 * userInfoRepository.findById(_ as Long)
            0 * requestUserMapper.toDomain(_ as RequestUserDTO)
            0 * userRepository.save(_ as User)
            0 * requestMeasurementMapper.toDomain(_ as RequestMeasurementDTO)
            0 * measurementRepository.save(_ as Measurement)
            0 * responseUserMapper.fromDomain(_ as User, _ as Measurement)
        }
    }

    def "should throw UserNotFoundException for createUser"() {
        given:
        def userInfoId = 5L
        def requestMeasurementDTO = new RequestMeasurementDTO(MeasureType.HEIGHT, 183, Unit.CENTIMETERS, LocalDate.now())
        def requestUserDTO = new RequestUserDTO("John", 30, Gender.MALE, Activity.MODERATELY_ACTIVE, requestMeasurementDTO)

        when:
        userService.createUser(requestUserDTO, userInfoId)

        then:
        1 * userRepository.existsByUserInfoId(_ as Long) >> false
        1 * userInfoRepository.findById(_ as Long) >> Optional.empty()
        def exception = thrown(UserNotFoundException)
        exception.message == "User with id: " + userInfoId + " not found"

        verifyAll {
            0 * requestUserMapper.toDomain(_ as RequestUserDTO)
            0 * userRepository.save(_ as User)
            0 * requestMeasurementMapper.toDomain(_ as RequestMeasurementDTO)
            0 * measurementRepository.save(_ as Measurement)
            0 * responseUserMapper.fromDomain(_ as User, _ as Measurement)
        }
    }

    def "positive path for findAll"() {
        given:
        def userInfoFist = new UserInfo(2L, "john@gmail.com", "password", Role.USER)
        def userDomainFirst = new User(1L, "John", 30, Gender.MALE, Activity.MODERATELY_ACTIVE, userInfoFist)
        def heightFirst = new Measurement(1L, MeasureType.HEIGHT, 185D, Unit.CENTIMETERS, LocalDate.now(), userDomainFirst)

        def userInfoSecond = new UserInfo(3L, "ann@gmail.com", "password", Role.USER)
        def userDomainSecond = new User(2L, "Ann", 25, Gender.FEMALE, Activity.EXTRA_ACTIVE, userInfoSecond)
        def heightSecond = new Measurement(2L, MeasureType.HEIGHT, 170D, Unit.CENTIMETERS, LocalDate.now(), userDomainSecond)

        def heightResponseFirst = new ResponseMeasurementDTO(heightFirst.getId(), heightFirst.getType(), heightFirst.getValue(), heightFirst.getUnit(), heightFirst.getDate())
        def userResponseFirst = new ResponseUserDTO(userDomainFirst.getId(), userDomainFirst.getUserName(), userDomainFirst.getAge(), userDomainFirst.getGender(), userDomainFirst.getActivity(), heightResponseFirst)

        def heightResponseSecond = new ResponseMeasurementDTO(heightSecond.getId(), heightSecond.getType(), heightSecond.getValue(), heightSecond.getUnit(), heightSecond.getDate())
        def userResponseSecond = new ResponseUserDTO(userDomainSecond.getId(), userDomainSecond.getUserName(), userDomainSecond.getAge(), userDomainSecond.getGender(), userDomainSecond.getActivity(), heightResponseSecond)

        when:
        def result = userService.findAll()

        then:
        1 * userRepository.findAll() >> Arrays.asList(userDomainFirst, userDomainSecond)
        1 * measurementRepository.findAllByUserId(_ as Long, _ as Sort) >> Arrays.asList(heightFirst)
        1 * measurementRepository.findAllByUserId(_ as Long, _ as Sort) >> Arrays.asList(heightSecond)
        1 * responseUserMapper.fromDomain(_ as User, _ as Measurement) >> userResponseFirst
        1 * responseUserMapper.fromDomain(_ as User, _ as Measurement) >> userResponseSecond

        result.size() == 2
        result.get(0) == userResponseFirst
        result.get(1) == userResponseSecond
        result.get(0).getHeight() == heightResponseFirst
        result.get(1).getHeight() == heightResponseSecond
        result.get(0).getId() == userDomainFirst.getId()
        result.get(1).getId() == userDomainSecond.getId()
    }

    def "should return empty list for findAll"() {
        when:
        userService.findAll()

        then:
        1 * userRepository.findAll() >> Collections.emptyList()

        verifyAll {
            0 * responseUserMapper.fromDomain(_ as User, _ as Measurement)
            0 * measurementRepository.findAllByUserId(_ as Long, _ as Sort)
        }
    }

    def "positive path for findById"() {
        given:
        def userInfoFist = new UserInfo(2L, "john@gmail.com", "password", Role.USER)
        def userDomainFirst = new User(1L, "John", 30, Gender.MALE, Activity.MODERATELY_ACTIVE, userInfoFist)
        def heightFirst = new Measurement(1L, MeasureType.HEIGHT, 185D, Unit.CENTIMETERS, LocalDate.now(), userDomainFirst)
        def heightResponseFirst = new ResponseMeasurementDTO(heightFirst.getId(), heightFirst.getType(), heightFirst.getValue(), heightFirst.getUnit(), heightFirst.getDate())
        def userResponseFirst = new ResponseUserDTO(userDomainFirst.getId(), userDomainFirst.getUserName(), userDomainFirst.getAge(), userDomainFirst.getGender(), userDomainFirst.getActivity(), heightResponseFirst)

        when:
        ResponseUserDTO result = userService.findById(1L)

        then:
        1 * userRepository.findById(_ as Long) >> Optional.of(userDomainFirst)
        1 * measurementRepository.findAllByUserId(_ as Long, _ as Sort) >> Arrays.asList(heightFirst)
        1 * responseUserMapper.fromDomain(_ as User, _ as Measurement) >> userResponseFirst

        verifyAll {
            result.getId() == userDomainFirst.getId()
            result.getHeight() == heightResponseFirst
            result.getUserName() == userDomainFirst.getUserName()
            result.getAge() == userDomainFirst.getAge()
            result.getGender() == userDomainFirst.getGender()
            result.getActivity() == userDomainFirst.getActivity()
        }
    }

    def "should throw UserNotFoundException for findById"() {
        when:
        userService.findById(givenId)

        then:
        1 * userRepository.findById(_ as Long) >> Optional.empty()
        def exception = thrown(UserNotFoundException)
        exception.message == "User with id: " + givenId + " not found"

        verifyAll {
            0 * measurementRepository.findAllByUserId(_ as Long, _ as Sort)
            0 * responseUserMapper.fromDomain(_ as User, _ as Measurement)
        }

        where:
        givenId | _
        1L      | _
        10L     | _
        4L      | _
    }

    def "should throw UserNotFoundException for updateUser"() {

        given:
        def requestMeasurementDTO = new RequestMeasurementDTO(MeasureType.HEIGHT, 190D, Unit.CENTIMETERS, LocalDate.now())
        def requestUserDTO = new RequestUserDTO("John", 35, Gender.MALE, Activity.EXTRA_ACTIVE, requestMeasurementDTO)

        when:
        userService.updateUser(givenId, requestUserDTO)

        then:
        1 * userRepository.findById(_ as Long) >> Optional.empty()
        def exception = thrown(UserNotFoundException)
        exception.message == "User with id: " + givenId + " not found"


        where:
        givenId | _
        1L      | _
        10L     | _
    }

    def "positive path for updateUser's activity"() {
        given:
        def givenId = 1L
        def userInfo = new UserInfo(2L, "john@gmail.com", "password", Role.USER)
        def requestMeasurementDTO = new RequestMeasurementDTO(MeasureType.HEIGHT, 190D, Unit.CENTIMETERS, LocalDate.now())
        def requestUserDTO = new RequestUserDTO("John", 35, Gender.MALE, Activity.EXTRA_ACTIVE, requestMeasurementDTO)
        def userDomain = new User(givenId, "John", 35, Gender.MALE, Activity.LIGHTLY_ACTIVE, userInfo)
        def updatedUser = new User(givenId, requestUserDTO.getUserName(), requestUserDTO.getAge(), requestUserDTO.getGender(), requestUserDTO.getActivity(), userDomain.getUserInfo())
        def measurementDomain = new Measurement(1L, MeasureType.HEIGHT, 190D, Unit.CENTIMETERS, LocalDate.now(), userDomain)
        def updatedHeight = new Measurement(1L, MeasureType.HEIGHT, 190D, Unit.CENTIMETERS, LocalDate.now(), userDomain)
        def responseMeasurementDTO = new ResponseMeasurementDTO(updatedHeight.getId(), updatedHeight.getType(), updatedHeight.getValue(), updatedHeight.getUnit(), updatedHeight.getDate())
        def responseUserDTO = new ResponseUserDTO(userDomain.getId(), updatedUser.getUserName(), updatedUser.getAge(), updatedUser.getGender(), updatedUser.getActivity(), responseMeasurementDTO)
        when:
        userService.updateUser(givenId, requestUserDTO)
        then:
        1 * userRepository.findById(_ as Long) >> Optional.of(userDomain)
        1 * requestUserMapper.editableToDomain(_ as RequestUserDTO, userDomain) >> updatedUser
        1 * userRepository.save(_ as User) >> updatedUser
        1 * measurementRepository.findAllByUserId(_ as Long, _ as Sort) >> Arrays.asList(measurementDomain)
        1 * requestMeasurementMapper.editableToDomain(_ as RequestMeasurementDTO, _ as Measurement) >> updatedHeight
        1 * measurementRepository.save(_ as Measurement) >> updatedHeight
        1 * responseUserMapper.fromDomain(_ as User, _ as Measurement) >> responseUserDTO

        verifyAll {
             requestUserDTO.getActivity() == responseUserDTO.getActivity()
             requestUserDTO.getHeight().getValue() == responseUserDTO.getHeight().getValue()
        }
    }

}
