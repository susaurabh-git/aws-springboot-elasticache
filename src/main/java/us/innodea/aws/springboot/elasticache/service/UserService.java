package us.innodea.aws.springboot.elasticache.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import us.innodea.aws.springboot.elasticache.exception.ResourceNotFoundException;
import us.innodea.aws.springboot.elasticache.model.CreateUserRequest;
import us.innodea.aws.springboot.elasticache.model.User;

import java.util.*;
@Slf4j
@Service
@CacheConfig(cacheNames = "userCache")
public class UserService {

  @CachePut(key = "#result.getId().getId()", unless = "#result == null")
  public User createUser(CreateUserRequest request) {
    User newUser =
        User.builder()
            .id(UUID.randomUUID().toString())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .build();
    return newUser;
  }

  @Cacheable
  public User getUser(String id) {
    throw new ResourceNotFoundException(User.class.getSimpleName(), id);
  }

  @CacheEvict
  public  void deleteUser(String id) {
    // No implementation required.
  }

  @CachePut(key = "#result.getId().getId()", unless = "#result == null")
  public User updateUser(CreateUserRequest request) {
    User updatedUser =
        User.builder()
            .id(request.getId())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .build();
    return updatedUser;
  }

}
