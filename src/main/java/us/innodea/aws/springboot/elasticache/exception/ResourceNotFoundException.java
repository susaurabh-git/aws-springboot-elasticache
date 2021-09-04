package us.innodea.aws.springboot.elasticache.exception;

public class ResourceNotFoundException extends RuntimeException {

  private static final String ERROR_MESSAGE = "%s is not found with Id: %s";

  private final String type;
  private final String id;

  public ResourceNotFoundException(String type, String id) {
    super(String.format(ERROR_MESSAGE, type, id));
    this.type = type;
    this.id = id;
  }
}
