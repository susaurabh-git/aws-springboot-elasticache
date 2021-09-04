package us.innodea.aws.springboot.elasticache.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = 8166913065696134416L;
    private String id;
    private String firstName;
    private String lastName;
}
