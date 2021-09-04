package us.innodea.aws.springboot.elasticache.model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class CreateUserRequest implements Serializable {
    private static final long serialVersionUID = -1802739462080006423L;
    private String id;
    private String firstName;
    private String lastName;
}
